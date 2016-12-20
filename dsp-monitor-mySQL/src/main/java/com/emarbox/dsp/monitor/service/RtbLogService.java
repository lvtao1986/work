package com.emarbox.dsp.monitor.service;

import com.codahale.metrics.Timer;
import com.emar.monitor.common.ConfigUtil;
import com.emarbox.dsp.monitor.data.DataProducer;
import com.emarbox.dsp.monitor.util.CostUtil;
import com.emarbox.dsp.monitor.util.MetricsUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 将点击展现数据发送到rtb kafka当中
 */
@Service
public class RtbLogService {

    private static final Logger log = LoggerFactory.getLogger(RtbLogService.class);

    // 存储待发送的数据
    private static List<KeyedMessage<String, String>> dataList = Collections.synchronizedList(new ArrayList<KeyedMessage<String, String>>(500));

    static {
        ConfigUtil.addConfig("rtb_kafka_config");
    }

    //rtb kafka发送计时
    private final Timer rtbKafkaSentTimer = MetricsUtil.metricsRegistry.timer("rtbKafkaSentTimer");


    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);//同时执行10个线程

    //消息发送判断间隔
    private static final long SLEEP_TIME = 100L;

    private static void addData(String supplier, String data) {
        if (StringUtils.isEmpty(supplier)) {
            log.warn("supplier is null.");
            return;
        }
        if (StringUtils.isEmpty(data)) {
            log.warn("log content is null");
            return;
        }
        //topic 默认按渠道指定，如果配置文件all.channel=1,则会创建一个不区分渠道的topic，名称为:all
        String topic = supplier;
        String allChannel = ConfigUtil.getString("all.channel");
        if ("1" .equals(allChannel)) {
            topic = "allChannel";
        }

        KeyedMessage<String, String> message = new KeyedMessage<String, String>(topic, data);
        dataList.add(message);
    }

    /**
     * 添加rtb竞价成功数据
     *
     * @param requestId    竞价id
     * @param adChannelId  渠道id
     * @param actionType   动作类型
     * @param biddingPrice 成功价格
     * @param mediaSite    媒体站点
     * @param apid         广告位id
     * @param td           托底标示: 0 rtb ,1 ppb ,2托底
     * @param biddingTime  竞价时间
     * @param logTime      日志时间
     * @param projectId    项目id
     * @param clickId      点击id(展现传null)
     * @param gid          商品id(动态创意传递,展现为多个商品id,点击为1个商品id)
     */
    public static void addRtbData(String requestId,
                                  int actionType, String biddingPrice,
                                  String userId, String campaignId,
                                  String adChannelId, String mediaSite,
                                  String apid, String td,
                                  String biddingTime, String logTime,
                                  String projectId, String gid, String clickId) {
        if (td != null && td.equals("2")) { //td: 0 rtb ,1 ppb ,2托底
            return; //托底不发rtb
        }

        //将竞价成功价格统一转换成 : (单次展现 * 1000 * 1000)
        double price = 0;
        try {
            price = CostUtil.calBiddingCost(adChannelId, NumberUtils.createDouble(biddingPrice));
            price = price * 1000000;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        String biddingType = "1"; //流量类型:1为pc,2为移动

        String data = requestId + "," + actionType + ","
                + biddingPrice + "," + userId + ","
                + campaignId + "," + adChannelId + ","
                + mediaSite + "," + apid + "," + price + "," + biddingType
                + ","
                + biddingTime + "," + logTime + "," + projectId + "," + (gid==null?"":gid) + "," + (clickId==null?"":clickId);
        addData(adChannelId, data);
    }

    public synchronized void sentData() {
        while (true) {
            try {
                if (dataList.size() == 0 || dataList.isEmpty()) {
                    Thread.sleep(SLEEP_TIME);
                    continue;
                }

                final List<KeyedMessage<String, String>> sentlist = dataList;
                dataList = Collections.synchronizedList(new ArrayList<KeyedMessage<String, String>>(500));
                threadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        sentKafak(sentlist);
                    }
                });

                Thread.sleep(SLEEP_TIME);
            } catch (Exception e) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e1) {
                    log.error(e.getMessage(), e);
                }
                log.error(e.getMessage(), e);
            }
        }

    }

    private void sentKafak(List<KeyedMessage<String, String>> dataList) {
        Timer.Context timeContext = rtbKafkaSentTimer.time(); //开始计时
        try {
            Producer<String, String> producer = DataProducer.getProducer("rtb_kafka_config");
            producer.send(dataList);
        } catch (Throwable ex) {
            log.error("发送RTB_Kafka error:" + ex.getMessage(), ex);
        }
        timeContext.stop(); //结束计时
    }


}
