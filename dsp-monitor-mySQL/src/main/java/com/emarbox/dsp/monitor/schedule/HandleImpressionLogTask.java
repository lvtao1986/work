package com.emarbox.dsp.monitor.schedule;

import com.codahale.metrics.Timer;
import com.emar.base.message.KafkaProducerManager;
import com.emar.base.message.PaipaiKafkaProducerManager;
import com.emar.monitor.common.Config;
import com.emarbox.dsp.monitor.data.*;
import com.emarbox.dsp.monitor.dto.ActionType;
import com.emarbox.dsp.monitor.dto.ConfigProperties;
import com.emarbox.dsp.monitor.dto.ImpressionLog;
import com.emarbox.dsp.monitor.dto.LogValidRule;
import com.emarbox.dsp.monitor.service.MonitorService;
import com.emarbox.dsp.monitor.service.PaipaiService;
import com.emarbox.dsp.monitor.service.RtbLogService;
import com.emarbox.dsp.monitor.util.*;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理展现日志信息
 * 流程: 判断日志是否有效 -> 有效日志写入文件 -> 有效日志发送计费kafka -> 有效日志发送rtbkafka
 */
public class HandleImpressionLogTask {
    private static final Logger logger = LoggerFactory.getLogger(HandleImpressionLogTask.class);
    private static final Logger errorLog = LoggerFactory.getLogger("errorImpressionLog");
    private static double retargetCostArg = Double.parseDouble(Config.getString("cost.arg.retarget")); //回头客计费参数
    private static double normalCostArg = Double.parseDouble(Config.getString("cost.arg.normal")); //非回头客计费参数
    private static final String ENVIORMENT = ConfigProperties.getEnviorment();

    protected static final String mediaSeparator = "X";
    private static final String KAFKA_TOPIC_BILLING_PREFIX = Config.getString("kafka.topic.name.prefix.billing");//kafka发送主题前缀

    /**
     * Kafka 重发次数设置，最大5次
     **/
    private final int REPEAT_TIMES = 5;

    /*
     * 分隔符
     */
    private static final String separator = ",";
    private static final char separatorA = '\u0001';
    private static final String key = Config.getString("key");

    private Date cacheDate = Function.today();
    public static long todaySumCount = 0L;

    @Autowired
    protected MonitorService monitorService;

    private final int CACHE_SIZE = 2000;
    // MESSAGE cache for producer
    private List<KeyedMessage<String, String>> messageCaches = new ArrayList<KeyedMessage<String, String>>(CACHE_SIZE);
    private List<KeyedMessage<String, String>> paipaiMessageCaches = new ArrayList<KeyedMessage<String, String>>(CACHE_SIZE);

    private Producer<String, String> producer = null;

    //拍拍项目用producer
    private Producer<String, String> paipaiProducer = null;

    //单条日志处理时长记录
    private final Timer impressionLogTimer = MetricsUtil.metricsRegistry.timer("impressionLogTimer");
    //kafka发送时长记录
    private final Timer billingKafkaSentTimer = MetricsUtil.metricsRegistry.timer("impressionBillingKafkaSentTimer");


    public void execute() {
        producer = KafkaProducerManager.getManager().createProducer();
        paipaiProducer = PaipaiKafkaProducerManager.getManager().createProducer();
        boolean sleepSign = false;
        while (true) {
            for (int i = 0; i < CACHE_SIZE; i++) {

                ImpressionLog impressionLog = null;
                try {
                    impressionLog = ImpressionLogQueue.pollLog();
                    if (null == impressionLog) {
                        sleepSign = true;
                        break;
                    } else {
                        handleLog(impressionLog); //处理日志
                    }
                } catch (Exception e) {
                    logger.warn("处理计费日志出现错误：" + e.getMessage() + " log:" + impressionLog.toString(), e);
                    //producer.close();
                }
            }
            processCache();
            todaySumCount += messageCaches.size();
            todaySumCount += paipaiMessageCaches.size();
            sendPaipaiProducerMessage();
            sendProducerMessage();
            if (sleepSign) {
                try {
                    sleepSign = false;
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    logger.warn("处理计费日志休眠线程出现错误：" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 每天0点自动清空前一天的缓存，缓存中的最大数为 前一天的活动数+下一天的活动数
     */
    private void processCache() {
        Date now = new Date();
        if (now.getTime() > DateUtils.addDays(cacheDate, 1).getTime()) {
            todaySumCount = 0L;
            cacheDate = Function.today();
        }
    }

    private void handleLog(ImpressionLog log) {
        final Timer.Context timeContent = impressionLogTimer.time();//计时开始
        //腾讯和百度渠道处理
        if (log.getSupplier() == SupplierInfo.TENCENT_ADX.intValue() || log.getSupplier() == SupplierInfo.BAIDU_ADX.intValue() || log.getSupplier() == SupplierInfo.TENCENT_GDT.intValue() || log.getSupplier() == SupplierInfo.HUZHONG_ADX.intValue() || log.getSupplier() == SupplierInfo.MAX_ADX.intValue()) {
            writeBStyleLog(log);
        } else {//其他渠道处理
            writeLog(log);
        }
        timeContent.stop();//计时结束
    }

    /**
     * 验证sign是否有效
     *
     * @param info sign
     * @return 0和1
     */
    private int validInfo(String info, String sign) {
        if (StringUtils.isEmpty(info) || StringUtils.isEmpty(sign)) {
            return 0;
        }
        String msign = DigestUtils.md5Hex(info + "_" + key);
        boolean result = msign.equalsIgnoreCase(sign);

        /**
         * 修正bug：发现有部分请求连接里的info参数转换成了小写
         */
        if (!result) {
            msign = DigestUtils.md5Hex(info.toUpperCase() + "_" + key);
            result = msign.equalsIgnoreCase(sign);
        }
        if (result) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * 验证展现日志是否有效 根据竞价时间和日志时间判断，如果日志时间比竞价时间晚一分钟以上则认为无效
     *
     * @param time
     * @param logTime
     * @return 0无效，1有效
     */
    public LogValidRule valideImpressLog(String info, String sign, String agent, String time, Date logTime, String biddingRequestId) {
        LogValidRule logValidRule = new LogValidRule();
        //测试环境不走验证规则
        if ("dev".equals(ENVIORMENT)) {
            logValidRule.setIsValid(1);
            logValidRule.setRuleId(0);
            return logValidRule;
        }
        //验证agent有效性
        if (!UserAgentUtil.checkUserAgent(agent)) {
            logValidRule.setIsValid(0);
            logValidRule.setRuleId(1002);
            return logValidRule;
        }

        //校验访问参数中是否包含sign
        if (validInfo(info, sign) == 0) {
            logValidRule.setIsValid(0);
            logValidRule.setRuleId(1001);
            return logValidRule;
        }

        logValidRule.setIsValid(1);
        logValidRule.setRuleId(0);
        return logValidRule;
    }

    /**
     * 最新参数格式
     *
     * @return 返回日志是否有效
     */
    protected void writeLog(ImpressionLog log) {
        String info = log.getInfo();
        String sign = log.getSign();
        String c = log.getC();
        String guid = log.getGuid();
        String apid = log.getApid();
        if (apid == null) {
            apid = "";
        }
        String actionType = log.getActionType();
        String userId = log.getUserId();
        String proxyIp = log.getProxyIp();
        String realIp = log.getRealIp();
        String agent = log.getAgent();
        String referer = log.getReferer();
        int supplier = log.getSupplier();
        String dcid = log.getDcid();
        String ct = log.getCt();
        String rt = log.getRt();
        String rt2 = log.getRt2();
        String pid = log.getPid();
        String td = log.getTd();
        String bt = log.getBt();
        String ut = log.getUt();
        String gid = log.getGid();
        if (StringUtils.isNotBlank(gid)) {
            gid = gid.replace(',', '，');
        }
        Date logDate = log.getLogDate();

        String[] args = info.split("_", -1);
        String campaignId = args[0]; // 计划id
        String creativeId = args[1]; // 创意id
        String adUserId = args[2]; // 广告主id
        String projectId = args[3]; // 项目id
        String biddingId = args[4]; // 竞价id
        if (supplier == SupplierInfo.MIAOZHEN_ADX) {
            biddingId = biddingId.replace("*", "_");
        }

        String biddingUserId = args[5]; // 竞价的userId
        // google user id 单独参数处理
        if (StringUtils.isNotBlank(guid)) {
            try {
                biddingUserId = URLDecoder.decode(guid, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.warn(e.getMessage(), e);
            }
        }

        String time = args[6]; // 竞价时间
        String adChannelId = args[7]; // 广告平台标示
        String costType = args[8]; // 广告计费类型
        String biddingRtbPrice = args[9]; // RTB价格
        String valuePrice = args[10]; // 用户价值
        String ctr = args[11]; // ctr
        String division = args[12];// 利差分成
        String biddingCost = args[13]; // 收取广告主价格
        String commRate = args[14]; // 佣金比例
        String taxRate = args[15]; // 税点比例

        String mediaSite = "";// 媒体站点
        int length = args.length;
        if (length >= 17) {
            mediaSite = args[16];
        }

        String userDomains = "";// 用户访问过的域名
        if (length >= 18) {
            userDomains = args[17];
            if (StringUtils.isNotBlank(userDomains)) {
                userDomains = userDomains.replace(",", "_");
            }
        }

        String userPrice = "";// 用户访问过的域名
        if (length >= 19) {
            userPrice = args[18];
            logger.debug(info);
        }

        String logTime = Function.getTimeString(logDate); // 日志时间

        String biddingPrice = c;// 竞价价格

        // 处理biddingPirce
        biddingPrice = WinnerPriceDecoder.decodePrice(supplier, biddingPrice);


        String maxPrice = String.valueOf(DataCache.getMaxPrice(campaignId));// 活动的最高限价
        String retarget = DataCache.getRetarget(campaignId); //活动回头客定向设置:0/1
        double price = CostUtil.calBiddingCost(adChannelId, NumberUtils.createDouble(biddingPrice));
        String unifyPrice = String.valueOf(Math.pow(10, 5) * price);//同一单位的价格 分/CPM
        String ctStr = ct == null ? "" : ct;
        String rtStr = rt == null ? "" : rt;
        /**
         * 验证日志是否有效
         */
        int isValid = 1;
        LogValidRule logValidRule = null;
        if (ActionType.IMPRESSION.equals(actionType)) {
            logValidRule = valideImpressLog(info, sign, agent, time, logDate, biddingId);
            isValid = logValidRule.getIsValid();
        }
        //毛利率要求
        Double profitRateSet = 0.3;
        try {
            DataCache.CAPAIGN_PROFIT_RATE_CONFIG.get(Long.parseLong(campaignId));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        /**
         * 拼接记录添加到日志文件
         */
        StringBuilder str = new StringBuilder();
        str.append(biddingId).append(separator);
        str.append(biddingUserId).append(separator);
        str.append(adChannelId).append(separator);
        str.append(costType).append(separator);
        str.append(actionType).append(separator);
        str.append(biddingPrice).append(separator);
        str.append(biddingCost).append(separator);
        str.append(campaignId).append(separator);
        str.append(creativeId).append(separator);
        str.append(adUserId).append(separator);
        str.append(projectId).append(separator);
        str.append(time).append(separator);
        str.append(userId).append(separator);
        str.append(proxyIp).append(separator);
        str.append(UserAgentUtil.cleanSpecialChar(agent)).append(separator);
        str.append(UserAgentUtil.cleanSpecialChar(referer)).append(separator);
        str.append(biddingRtbPrice).append(separator);
        str.append(ctr).append(separator);
        str.append(commRate).append(separator);
        str.append(taxRate).append(separator);
        str.append(logTime).append(separator);
        str.append(valuePrice).append(separator);
        str.append(division).append(separator);
        str.append(isValid).append(separator);
        str.append(mediaSite).append(separator);
        str.append(userDomains).append(separator);
        str.append(maxPrice).append(separator);
        str.append(apid).append(separator);
        str.append(logValidRule.getRuleId()).append(separator);
        str.append(dcid).append(separator);
        str.append("").append(separator);//clickId
        str.append(ctStr.replace(',', '，')).append(separator);
        str.append(rtStr.replace(',', '，')).append(separator);
        str.append(retarget).append(separator);//回头客设置
        str.append(retargetCostArg).append(separator); //回头客计费参数
        str.append(normalCostArg).append(separator); //非回头客计费参数
        str.append(rt2).append(separator);
        str.append(profitRateSet).append(separator);
        str.append(pid).append(separator);
        str.append(td).append(separator);
        str.append(userPrice).append(separator);
        str.append(realIp).append(separator);
        str.append(bt).append(separator);
        str.append(unifyPrice).append(separator);
        str.append(ut).append(separator);
        str.append(gid);
        monitorService.writeLog(adChannelId, str.toString());//记录写入日志文件

        /**
         * 拼接dmp记录添加到日志文件
         */
        StringBuilder dmpStr = new StringBuilder();
        dmpStr.append(biddingId).append(separatorA);
        dmpStr.append(biddingUserId).append(separatorA);
        dmpStr.append(adChannelId).append(separatorA);
        dmpStr.append(costType).append(separatorA);
        dmpStr.append(actionType).append(separatorA);
        dmpStr.append(biddingPrice).append(separatorA);
        dmpStr.append(biddingCost).append(separatorA);
        dmpStr.append(campaignId).append(separatorA);
        dmpStr.append(creativeId).append(separatorA);
        dmpStr.append(adUserId).append(separatorA);
        dmpStr.append(projectId).append(separatorA);
        dmpStr.append(time).append(separatorA);
        dmpStr.append(userId).append(separatorA);
        dmpStr.append(proxyIp).append(separatorA);
        dmpStr.append(UserAgentUtil.cleanSpecialChar(agent)).append(separatorA);
        dmpStr.append(UserAgentUtil.cleanSpecialChar(referer)).append(separatorA);
        dmpStr.append(biddingRtbPrice).append(separatorA);
        dmpStr.append(ctr).append(separatorA);
        dmpStr.append(commRate).append(separatorA);
        dmpStr.append(taxRate).append(separatorA);
        dmpStr.append(logTime).append(separatorA);
        dmpStr.append(valuePrice).append(separatorA);
        dmpStr.append(division).append(separatorA);
        dmpStr.append(isValid).append(separatorA);
        dmpStr.append(mediaSite).append(separatorA);
        dmpStr.append(userDomains).append(separatorA);
        dmpStr.append(maxPrice).append(separatorA);
        dmpStr.append(apid).append(separatorA);
        dmpStr.append(logValidRule.getRuleId()).append(separatorA);
        dmpStr.append(dcid).append(separatorA);
        dmpStr.append("").append(separatorA);//clickId
        dmpStr.append(ctStr.replace(',', '，')).append(separatorA);
        dmpStr.append(rtStr.replace(',', '，')).append(separatorA);
        dmpStr.append(retarget).append(separatorA);//回头客设置
        dmpStr.append(retargetCostArg).append(separatorA); //回头客计费参数
        dmpStr.append(normalCostArg).append(separatorA); //非回头客计费参数
        dmpStr.append(rt2).append(separatorA);
        dmpStr.append(profitRateSet).append(separatorA);
        dmpStr.append(pid).append(separatorA);
        dmpStr.append(td).append(separatorA);
        dmpStr.append(userPrice).append(separatorA);
        dmpStr.append(realIp).append(separatorA);
        dmpStr.append(bt).append(separatorA);
        dmpStr.append(unifyPrice).append(separatorA);
        dmpStr.append(ut).append(separatorA);
        dmpStr.append(gid);
        monitorService.writeDmpLog(adChannelId, dmpStr.toString());//记录写入dmp日志文件

//		// 无效日志不进行计费
        if (isValid != 1) {
            return;
        }

        /**
         * 拼接记录添加到billing kafka
         */
        StringBuilder newCalLog = new StringBuilder(400);
        newCalLog.append(biddingId).append(separator);
        newCalLog.append(adChannelId).append(separator);
        newCalLog.append(costType).append(separator);
        newCalLog.append(actionType).append(separator);
        newCalLog.append(biddingPrice).append(separator);
        newCalLog.append(campaignId).append(separator);
        newCalLog.append(creativeId).append(separator);

        newCalLog.append(time).append(separator);
        newCalLog.append(biddingRtbPrice).append(separator);
        newCalLog.append(ctr).append(separator);
        newCalLog.append(logTime).append(separator);

        newCalLog.append(valuePrice).append(separator);
        newCalLog.append(mediaSite).append(separator);
        newCalLog.append(maxPrice).append(separator);
        newCalLog.append(apid).append(separator);
        newCalLog.append(dcid).append(separator);     //dcid
        newCalLog.append("").append(separator);    //clickId
        newCalLog.append(retarget).append(separator);
        //毛利率要求
        newCalLog.append(profitRateSet).append(separator);
        newCalLog.append(pid).append(separator);
        newCalLog.append(td).append(separator);
        newCalLog.append(userPrice).append(separator);
        newCalLog.append(proxyIp).append(separator);
        // 将记录添加到计费队列
        addBiddingLog(projectId, newCalLog.toString());

        /**
         * 将记录添加到RTB实时接口队列
         */
        RtbLogService.addRtbData(biddingId, actionType.equals("click") ? 2 : 1, biddingPrice, userId, campaignId, adChannelId, mediaSite, apid, td, time, logTime, projectId, gid, null);
    }

    /**
     * Tencent参数格式
     */
    protected void writeBStyleLog(ImpressionLog log) {
        String infoStr = log.getInfoStr();
        String sign = log.getSign();
        String biddingPriceStr = log.getC();
        String guid = log.getGuid();
        String apid = log.getApid();
        if (apid == null) {
            apid = "";
        }
        String campaignId = log.getCampaignId();
        String creativeId = log.getCreativeId();
        String adUserId = log.getAdUserId();
        String projectId = log.getProjectId();
        String biddingId = log.getBiddingId();
        String time = log.getTime();
        String adChannelId = log.getAdChannelId();
        String costType = log.getCostType();
        String biddingRtbPrice = log.getBiddingRtbPrice();
        String valuePrice = log.getValuePrice();
        String ctr = log.getCtr();
        String division = log.getDivision();
        String biddingCost = log.getBiddingCost();
        String commRate = log.getCommRate();
        String taxRate = log.getTaxRate();
        String mediaSite = log.getMediaSite();
        String userDomains = log.getUserDomains();
        String actionType = log.getActionType();
        String userId = log.getUserId();
        String proxyIp = log.getProxyIp();
        String realIp = log.getRealIp();
        String agent = log.getAgent();
        String referer = log.getReferer();
        String ct = log.getCt();
        String rt = log.getRt();
        String rt2 = log.getRt2();
        String pid = log.getPid();
        String td = log.getTd();
        String bt = log.getBt();
        String ut = log.getUt();
        String gid = log.getGid();
        if (StringUtils.isNotBlank(gid)) {
            gid = gid.replace(',', '，');
        }
        Date logDate = log.getLogDate();
        String userPrice = log.getUserPrice();

        long time1 = System.nanoTime(), timeBegin = time1;
        String biddingUserId = guid;

        String logTime = Function.getTimeString(logDate); // 日志时间

        String biddingPrice = biddingPriceStr;// 竞价价格

        String maxPrice = String.valueOf(DataCache.getMaxPrice(campaignId));// 活动的最高限价
        String retarget = DataCache.getRetarget(campaignId);//回头客设置

        double price = 0;
        if (NumberUtils.isNumber(biddingPrice)) {
            price = CostUtil.calBiddingCost(adChannelId, NumberUtils.createDouble(biddingPrice));
        }
        String unifyPrice = String.valueOf(Math.pow(10, 5) * price);//同一单位的价格 分/CPM

        String ctStr = ct == null ? "" : ct;
        String rtStr = rt == null ? "" : rt;

        /**
         * 验证日志是否有效
         */
        int isValid = 1;
        LogValidRule logValidRule = null;
        if (ActionType.IMPRESSION.equals(actionType)) {
            logValidRule = valideImpressLog(infoStr, sign, agent, time, logDate, biddingId);
            isValid = logValidRule.getIsValid();
        }
        //毛利率要求
        Double profitRateSet = 0.3;
        try {
            DataCache.CAPAIGN_PROFIT_RATE_CONFIG.get(Long.parseLong(campaignId));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        /**
         * 拼装记录,写入日志文件
         */
        StringBuilder str = new StringBuilder();
        str.append(biddingId).append(separator);
        str.append(biddingUserId).append(separator);
        str.append(adChannelId).append(separator);
        str.append(costType).append(separator);
        str.append(actionType).append(separator);
        str.append(biddingPrice).append(separator);
        str.append(biddingCost).append(separator);
        str.append(campaignId).append(separator);
        str.append(creativeId).append(separator);
        str.append(adUserId).append(separator);
        str.append(projectId).append(separator);
        str.append(time).append(separator);
        str.append(userId).append(separator);
        str.append(proxyIp).append(separator);
        str.append(UserAgentUtil.cleanSpecialChar(agent)).append(separator);
        str.append(UserAgentUtil.cleanSpecialChar(referer)).append(separator);
        str.append(biddingRtbPrice).append(separator);
        str.append(ctr).append(separator);
        str.append(commRate).append(separator);
        str.append(taxRate).append(separator);
        str.append(logTime).append(separator);
        str.append(valuePrice).append(separator);
        str.append(division).append(separator);
        str.append(isValid).append(separator);
        str.append(mediaSite).append(separator);
        str.append(userDomains).append(separator);
        str.append(maxPrice).append(separator);
        str.append(apid).append(separator);
        str.append(logValidRule.getRuleId()).append(separator);
        str.append("").append(separator);//动态创意id，tencent暂不支持
        str.append("").append(separator);//clickId
        str.append(ctStr.replace(',', '，')).append(separator);
        str.append(rtStr.replace(',', '，')).append(separator);
        str.append(retarget).append(separator);//回头客设置
        str.append(retargetCostArg).append(separator); //回头客计费参数
        str.append(normalCostArg).append(separator); //非回头客计费参数
        str.append(rt2).append(separator);
        str.append(profitRateSet).append(separator);
        str.append(pid).append(separator);
        str.append(td).append(separator);
        str.append(userPrice).append(separator);
        str.append(realIp).append(separator);
        str.append(bt).append(separator);
        str.append(unifyPrice).append(separator);
        str.append(ut).append(separator);
        str.append(gid);

        monitorService.writeLog(adChannelId, str.toString());//写入日志文件

        /**
         * 拼装记录,写入日志文件
         */
        StringBuilder dmpStr = new StringBuilder();
        dmpStr.append(biddingId).append(separatorA);
        dmpStr.append(biddingUserId).append(separatorA);
        dmpStr.append(adChannelId).append(separatorA);
        dmpStr.append(costType).append(separatorA);
        dmpStr.append(actionType).append(separatorA);
        dmpStr.append(biddingPrice).append(separatorA);
        dmpStr.append(biddingCost).append(separatorA);
        dmpStr.append(campaignId).append(separatorA);
        dmpStr.append(creativeId).append(separatorA);
        dmpStr.append(adUserId).append(separatorA);
        dmpStr.append(projectId).append(separatorA);
        dmpStr.append(time).append(separatorA);
        dmpStr.append(userId).append(separatorA);
        dmpStr.append(proxyIp).append(separatorA);
        dmpStr.append(UserAgentUtil.cleanSpecialChar(agent)).append(separatorA);
        dmpStr.append(UserAgentUtil.cleanSpecialChar(referer)).append(separatorA);
        dmpStr.append(biddingRtbPrice).append(separatorA);
        dmpStr.append(ctr).append(separatorA);
        dmpStr.append(commRate).append(separatorA);
        dmpStr.append(taxRate).append(separatorA);
        dmpStr.append(logTime).append(separatorA);
        dmpStr.append(valuePrice).append(separatorA);
        dmpStr.append(division).append(separatorA);
        dmpStr.append(isValid).append(separatorA);
        dmpStr.append(mediaSite).append(separatorA);
        dmpStr.append(userDomains).append(separatorA);
        dmpStr.append(maxPrice).append(separatorA);
        dmpStr.append(apid).append(separatorA);
        dmpStr.append(logValidRule.getRuleId()).append(separatorA);
        dmpStr.append("").append(separatorA);//动态创意id，tencent暂不支持
        dmpStr.append("").append(separatorA);//clickId
        dmpStr.append(ctStr.replace(',', '，')).append(separatorA);
        dmpStr.append(rtStr.replace(',', '，')).append(separatorA);
        dmpStr.append(retarget).append(separatorA);//回头客设置
        dmpStr.append(retargetCostArg).append(separatorA); //回头客计费参数
        dmpStr.append(normalCostArg).append(separatorA); //非回头客计费参数
        dmpStr.append(rt2).append(separatorA);
        dmpStr.append(profitRateSet).append(separatorA);
        dmpStr.append(pid).append(separatorA);
        dmpStr.append(td).append(separatorA);
        dmpStr.append(userPrice).append(separatorA);
        dmpStr.append(realIp).append(separatorA);
        dmpStr.append(bt).append(separatorA);
        dmpStr.append(unifyPrice).append(separatorA);
        dmpStr.append(ut).append(separatorA);
        dmpStr.append(gid);

        monitorService.writeDmpLog(adChannelId, dmpStr.toString());//写入日志文件
        // 无效日志不进行计费
        if (isValid != 1) {
            return;
        }

        /**
         * 拼装记录,写入billing kafka
         */
        StringBuilder newCalLog = new StringBuilder(400);
        newCalLog.append(biddingId).append(separator);
        newCalLog.append(adChannelId).append(separator);
        newCalLog.append(costType).append(separator);
        newCalLog.append(actionType).append(separator);
        newCalLog.append(biddingPrice).append(separator);
        newCalLog.append(campaignId).append(separator);
        newCalLog.append(creativeId).append(separator);
        newCalLog.append(time).append(separator);
        newCalLog.append(biddingRtbPrice).append(separator);
        newCalLog.append(ctr).append(separator);
        newCalLog.append(logTime).append(separator);
        newCalLog.append(valuePrice).append(separator);
        newCalLog.append(mediaSite).append(separator);
        newCalLog.append(maxPrice).append(separator);
        newCalLog.append(apid).append(separator);
        newCalLog.append("").append(separator);     //dcid
        newCalLog.append("").append(separator);//clickId
        newCalLog.append(retarget).append(separator);
        newCalLog.append(profitRateSet).append(separator);//毛利率要求
        newCalLog.append(pid).append(separator);
        newCalLog.append(td).append(separator);
        newCalLog.append(userPrice).append(separator);
        newCalLog.append(proxyIp).append(separator);

        // 将记录添加到计费队列
        addBiddingLog(projectId, newCalLog.toString());

        /**
         *将记录添加到RTB实时接口队列
         */
        RtbLogService.addRtbData(biddingId, actionType.equals("click") ? 2 : 1, biddingPrice, userId, campaignId, adChannelId, mediaSite, apid, td, time, logTime, projectId, gid, null);
    }

    /**
     * 向kafka发送组装好的日志数据
     *
     * @param projectId
     * @param logContent
     */
    private void addBiddingLog(String projectId, String logContent) {
        KeyedMessage<String, String> message = new KeyedMessage<String, String>(KAFKA_TOPIC_BILLING_PREFIX + projectId, logContent);
        if (PaipaiService.isPaipaiProject(projectId)) {
            paipaiMessageCaches.add(message);
        } else {
            messageCaches.add(message);
        }
    }

    private void sendPaipaiProducerMessage() {
        if (!paipaiMessageCaches.isEmpty()) {
            logger.info("sendPaipaiProducerMessage ,size: " + paipaiMessageCaches.size());
            Timer.Context timeContext = billingKafkaSentTimer.time(); //计时开始
            try {
                int currentTimes = 0;
                while (currentTimes < REPEAT_TIMES) {
                    try {
                        paipaiProducer.send(paipaiMessageCaches);
                        break;
                    } catch (Exception e) {
                        currentTimes++;
                        Thread.sleep(1000 * currentTimes);
                        if (currentTimes >= REPEAT_TIMES) {
                            throw e;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                for (KeyedMessage<String, String> message : paipaiMessageCaches) {
                    errorLog.error("paipai " + message.topic() + " " + message.message());
                }
            } finally {
                paipaiMessageCaches.clear();
            }
            timeContext.stop(); //计时结束
        }
    }

    private void sendProducerMessage() {
        if (!messageCaches.isEmpty()) {
            Timer.Context timeContext = billingKafkaSentTimer.time(); //计时开始
            try {
                int currentTimes = 0;
                while (currentTimes < REPEAT_TIMES) {
                    try {
                        producer.send(messageCaches);
                        break;
                    } catch (Exception e) {
                        currentTimes++;
                        Thread.sleep(1000 * currentTimes);
                        if (currentTimes >= REPEAT_TIMES) {
                            throw e;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                for (KeyedMessage<String, String> message : messageCaches) {
                    errorLog.error(message.topic() + " " + message.message());
                }
            } finally {
                messageCaches.clear();
            }
            timeContext.stop(); //计时结束
        }
    }
}
