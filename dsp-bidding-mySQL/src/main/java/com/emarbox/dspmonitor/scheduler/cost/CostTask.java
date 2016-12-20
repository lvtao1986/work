package com.emarbox.dspmonitor.scheduler.cost;

import com.codahale.metrics.Timer;
import com.emarbox.dsp.util.Function;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.domain.SendReport;
import com.emarbox.dspmonitor.billing.util.CostUtil;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import com.emarbox.dspmonitor.data.CampaignCost;
import com.emarbox.dspmonitor.scheduler.CostScheduler;
import com.emarbox.dspmonitor.scheduler.CostThread;
import com.google.common.base.Strings;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 具体计费线程
 * Created by mrzhu on 15/5/26.
 */
public class CostTask implements Callable<Boolean> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private int[] messageTransferIndex = new int[]{1, 2, 4, 5, 6, 7, 8, 9, 11, 13, 15, 16, 17, 20, 21, 23};

    private CostScheduler costScheduler;
    private CostThread costThread;
    private long accountId;
    private String handleTopic;

    /**
     * 是否是分线程运行的
     */
    private boolean isDivided;

    /**
     * 账户可用预算
     */
    double accountBalance;

    ReentrantLock costLock;

    /**
     * 带计费消息
     */
    List<String> messageList;

    public CostTask(ReentrantLock costLock,CostThread costThread, long accountId, double accountBalance, List<String> messageList, boolean isDivided, CostScheduler costScheduler,String handleTopic) {
        this.costLock = costLock;
        this.costThread = costThread;
        this.accountId = accountId;
        this.accountBalance = accountBalance;
        this.messageList = messageList;
        this.isDivided = isDivided;
        this.costScheduler = costScheduler;
        this.handleTopic = handleTopic;
    }

    public CostTask(ReentrantLock costLock,CostThread costThread, long accountId, double accountBalance, boolean isDivided, CostScheduler costScheduler,String handleTopic) {
        this.costLock = costLock;
        this.costThread = costThread;
        this.accountId = accountId;
        this.accountBalance = accountBalance;
        this.messageList = new ArrayList<String>(1000);
        this.isDivided = isDivided;
        this.costScheduler = costScheduler;
        this.handleTopic = handleTopic;
    }

    @Override
    public Boolean call() {
        try {
            Map<String, CampaignCost> mergeCostMap = new HashMap<String, CampaignCost>(500);

            /**
             * 逐条数据扣费
             */

            Map<Long, Double> accountNcostMap = new HashMap<Long, Double>();//本轮账户已扣费信息<userId,cost>
            Map<Long, Double> campaignNcostMap = new HashMap<Long, Double>();//本轮活动已扣费信息<campaignId,cost>
            List<String> errorCostList = new ArrayList<String>(500); //计算失败的信息
            List<String> messageCache = new ArrayList<String>(messageList.size()); //计算完成需入kafka队列信息
            for (String message : messageList) {
                //处理花费信息
                handleCostMessage(mergeCostMap, accountNcostMap, campaignNcostMap, errorCostList, messageCache, message);
            }
            /**
             * 1.计算完的结果入kafka队列
             */
            costThread.sendMessageList(messageCache);
            /**
             * 2.计算失败的数据进行日志记录
             */
            if (errorCostList.size() > 0) {
                costScheduler.billingDataService.addErrorCostMessage(handleTopic,errorCostList);
            }

            //costList汇总
            Collection<CampaignCost> clist = mergeCostMap.values();
            /**
             * 3.计费完数据存入 campaign_billing_detail 表
             */
            costThread.saveResultToDB(clist);

            /**
             * 4.更新账户余额信息
             */
            costLock.lock();
            try {
                costThread.updateAccountBlance(clist);
            }finally {
                costLock.unlock();
            }
        } catch (Exception e) {
            log.error("处理计费数据时出现异常,将部分数据写入日志文件:" + e.getMessage(), e);
            costScheduler.billingDataService.addErrorCostMessage(handleTopic,messageList);
            //return true;
        }
        return true;
    }

    /**
     * 处理计费信息
     *
     * @param mergeCostMap     存储返回值用
     * @param accountNcostMap  存储返回值用
     * @param campaignNcostMap 存储返回值用
     * @param errorCostList    存储返回值用
     * @param messageCache     存储返回值用
     * @param message          待处理信息
     */
    Timer handleCostMessageTimer = MetricsUtil.metricsRegistry.timer("handleCostMessageTimer");

    private void handleCostMessage(Map<String, CampaignCost> mergeCostMap, Map<Long, Double> accountNcostMap, Map<Long, Double> campaignNcostMap, List<String> errorCostList, List<String> messageCache, String message) {
        Timer.Context timeConext = handleCostMessageTimer.time();
        try {
            String[] costTemp = message.split(",", -1);
            CampaignCost cost = convertCostArray(costTemp);
            Date date = Function.parseDate(cost.getStatDate(), "yyyyMMdd");
            Calendar statDateCal = Calendar.getInstance();
            statDateCal.setTime(date);
            statDateCal.set(Calendar.HOUR_OF_DAY, cost.getStatHour());
            statDateCal.set(Calendar.MINUTE, cost.getStatMinute());

            Double preCost = cost.getCost();//计费前花费

            Long campaignId = cost.getCampaignId();
            BillingCampaign billingCam = FinalData.STATIC_CAMPAIGN_MAP.get(campaignId);//活动预算信息

            if (billingCam == null) { //去除无效数据
                log.warn("计费发现无效数据:" + message);
                return;
            }

            /**
             * 调用计费方法
             */
            if (preCost != null && preCost != 0d) {
                costLock.lock();
                try {
                    costScheduler.billingUtil.billing(cost, billingCam, statDateCal, accountId, accountNcostMap, campaignNcostMap, costThread.camTodayCostMap, accountBalance, costThread.accountBugetMap); //计算
                }finally {
                    costLock.unlock();
                }
            }
            // 将缓冲对象放入容器中
            messageCache.add(buildBilledMessage(cost, costTemp));
            //汇总计费结果
            mergeCostingData(mergeCostMap, cost);
        } catch (Exception evt) {
            log.error(evt.getMessage() + ":" + message, evt);
            errorCostList.add(message);
        }
        timeConext.stop();
    }


    /**
     * 从临时表删除数据,并返回 按照 活动+渠道+时间 合并后的Collection
     * 从原先的 removeCostingData 名称变更为 mergeCostingData， 移除 对mongdb的删除临时表数据的操作。
     *
     * @param costMap 不能为空
     * @param cost
     * @return
     */
    private void mergeCostingData(Map<String, CampaignCost> costMap, CampaignCost cost) {
        String key = cost.getStatTime() + "_" + cost.getCampaignId();
        CampaignCost mCost = costMap.get(key);
        if (mCost == null) {
            costMap.put(key, cost);
        } else {
            mCost.setClick(mCost.getClick() + cost.getClick());
            mCost.setImpression(mCost.getImpression() + cost.getImpression());
            mCost.setRtbCost(mCost.getRtbCost() + cost.getRtbCost());
            mCost.setDspCost(mCost.getDspCost() + cost.getDspCost());
            mCost.setPreCost(mCost.getPreCost() + cost.getPreCost());
            mCost.setCost(mCost.getCost() + cost.getCost());
        }
    }

    private CampaignCost convertCostArray(String[] costTemp) {
        Long campaignId = Long.parseLong(costTemp[5]);
        String statTime = costTemp[7].substring(0, 8);

        CampaignCost cost = new CampaignCost();
        cost.setCampaignId(campaignId);
        cost.setStatDate(statTime);
        cost.setStatTime(costTemp[7].substring(0, 12));
        cost.setStatHour(NumberUtils.toInt(costTemp[7].substring(8, 10)));
        cost.setStatMinute(NumberUtils.toInt(costTemp[7].substring(10, 12)));
        cost.setUserId(accountId);
        cost.setRequestId(costTemp[0]);
        cost.setClickId(costTemp[16]);

        String supplierId = costTemp[1];
        String actionType = costTemp[3];
        String costType = costTemp[2];
        String biddingCostStr = costTemp[4];
        String rtbCostStr = costTemp[8];
        String maxPrice = costTemp[13];
        String ctr = costTemp[9];
        String valuePriceStr = costTemp[11];
        cost.setImpression(actionType.equalsIgnoreCase("impression") ? 1L : 0L);
        cost.setClick(actionType.equalsIgnoreCase("click") ? 1L : 0L);
        if (cost.getImpression() == 0 && cost.getClick() == 0) {
            log.error("错误的操作类型:" + actionType);
        }
        boolean retarget = "1".equals(costTemp[17]);
        boolean td = "1".equals(costTemp[20]);

        //计算竞价花费和出价
        if("impression".equalsIgnoreCase(actionType)){
            cost.setDspCost(CostUtil.calBiddingCost(supplierId,NumberUtils.toDouble(biddingCostStr,0)));
            cost.setRtbCost(CostUtil.calRtbCost(rtbCostStr));
        }else{
            cost.setDspCost(0d);
            cost.setRtbCost(0d);
        }
        //计算投放花费
        if ((actionType.equalsIgnoreCase("impression") && costType.equalsIgnoreCase("cpm"))
                || (actionType.equalsIgnoreCase("click") && costType.equalsIgnoreCase("cpc"))) {
            // 点击去重,重复点击不收费
            boolean depStatus = true; //去重状态, true计费,fale不计费
            if("click".equalsIgnoreCase(actionType)){
                depStatus = costScheduler.requestIdService.duplicateClick(cost.getRequestId());
            }
            SendReport report = CostUtil.calBidding(cost.getRequestId(), campaignId.toString(), supplierId, actionType, costType, biddingCostStr, rtbCostStr,
                    maxPrice, ctr, valuePriceStr, retarget, td, true);
            cost.setPreCost(report.getCost());

            if(depStatus) {
                cost.setCost(report.getCost());
            }else{
                log.info("重复点击,不收取投放花费,requestId:" +cost.getRequestId()+",preCost:"+cost.getPreCost());
                cost.setCost(0d);
            }
        } else {
            cost.setCost(0d);
            cost.setPreCost(0d);
        }

        return cost;
    }


    /**
     * 组装发送Kafka数据
     *
     * @param costTemp
     * @return
     */
    private String buildBilledMessage(CampaignCost cost, String[] costTemp) {
    	int originalArraySize = costTemp.length;
        Double preCost = cost.getPreCost();
        Double actCost = cost.getCost();
        final int transferSize = messageTransferIndex.length;
        final int arraySize = transferSize + 2;
        String[] array = new String[arraySize];
        for (int i = 0; i < transferSize; i++) {
        	String val = null;
        	if(originalArraySize >= messageTransferIndex[i]) {
        		val = costTemp[messageTransferIndex[i] - 1];
        	}
            array[i] = Strings.nullToEmpty(val);
        }
        array[transferSize] = actCost == null ? "" : String.valueOf(actCost);
        array[transferSize + 1] = preCost == null ? "" : String.valueOf(preCost);
        return StringUtils.join(array, ",");
    }

    public void addMessageList(List<String> messageList) {
        this.messageList.addAll(messageList);
    }
}
