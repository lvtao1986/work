package com.emarbox.dspmonitor.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.util.ConfigUtil;

import com.emarbox.dsp.util.Function;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.util.CostUtil;
import com.emarbox.dspmonitor.costing.domain.CampaignCostDetail;
import com.emarbox.dspmonitor.data.CampaignCost;

public class CostReportThread extends BasicKafkaConsumer {
    protected static final Logger log = LoggerFactory.getLogger(CostReportThread.class);

    private long accountId; //线程需要处理的账号ID
    private CostReportScheduler costScheduler;
    public long todaySumCount = 0L;
    //public long todaySendCount=0L;
    private Date cacheDate = Function.today();

    CostReportThread(long accountId, CostReportScheduler costScheduler) {
        this.accountId = accountId;
        this.costScheduler = costScheduler;
    }

    /**
     * 每天0点自动清空前一天的缓存，缓存中的最大数为 前一天的活动数+下一天的活动数
     */
    private void processCache() {
        Date now = new Date();
        if (now.getTime() > DateUtils.addDays(cacheDate, 1).getTime()) {
            todaySumCount = 0L;
            //todaySendCount = 0L;
            cacheDate = Function.today();
        }
    }

    @Override
    protected boolean handleMessageList(List<String> costLogList) {
        processCache();
        try {

            Map<String, CampaignCost> mergeMap = new HashMap<String, CampaignCost>();//<date_hour_supplier_campaignId,campaignCost>  存放需要保存到正式mongo cost表的数据

            for (String costLog : costLogList) {
                try {
                    CampaignCost cost = convertLogToCost(costLog);

                    //<date_hour_supplier_campaignId,campaignCost>>>>  存放需要保存到campaign cost detail表的数据
                    String key = cost.getStatDate() + "_" + cost.getStatHour() + "_" + cost.getSupplierId() + "_" + cost.getCampaignId();
                    CampaignCost mergeCost = mergeMap.get(key);
                    if (mergeCost == null) {
                        mergeCost = cost;
                        mergeMap.put(key, mergeCost);
                    } else {
                        mergeCost.setClick(mergeCost.getClick() + cost.getClick());
                        mergeCost.setImpression(mergeCost.getImpression() + cost.getImpression());
                        mergeCost.setRtbCost(mergeCost.getRtbCost() + cost.getRtbCost());
                        mergeCost.setDspCost(mergeCost.getDspCost() + cost.getDspCost());
                        mergeCost.setCost(mergeCost.getCost() + cost.getCost());
                    }
                } catch (Exception e) {
                    log.error("报表处理计费数据时出现异常:" + e.getMessage() + "[" + costLog + "]", e);
                }
            }


            saveResultToDB(mergeMap.values()); //数据存入campaign_cost_detail表
            todaySumCount += costLogList.size();
            return true;
        } catch (Exception e) {
            log.error("已结算队列统计入库时出现异常：" + e.getMessage(), e);
            return false;
        }

    }


    /**
     * cost log 转 campaignCost
     *
     * @param costLog
     * @return
     */
    private CampaignCost convertLogToCost(String costLog) {
        String[] logArray = costLog.split(",", -1);

        Long campaignId = Long.parseLong(logArray[4]);
        String statDate = logArray[6].substring(0, 8);
        Integer hour = Integer.parseInt(logArray[6].substring(8, 10));
        Long supplierId = Long.parseLong(logArray[1]);

        long click = 0;
        long impression = 0;
        Double rtbCost = 0d;
        Double dspCost = 0d;
        if (logArray[2].equals("impression")) {
            impression += 1;
            rtbCost = CostUtil.calRtbCost(StringUtils.isBlank(logArray[7]) ? "0" : logArray[7]);
            dspCost = CostUtil.calBiddingCost(supplierId.toString(), NumberUtils.toDouble(logArray[3], 0d));
        } else if (logArray[2].equals("click")) {
            click += 1;
        }

        Double userCost = Double.parseDouble(logArray[16]);//原先花费为数组的第15个元素,后按照DMP要求计费日志增加IP字段，所以花费后延一位。

        CampaignCost cost = new CampaignCost();
        cost.setCampaignId(campaignId);
        cost.setStatDate(statDate);
        cost.setStatHour(hour);
        cost.setSupplierId(supplierId);
        cost.setClick(click);
        cost.setImpression(impression);
        cost.setRtbCost(rtbCost);
        cost.setDspCost(dspCost);
        cost.setCost(userCost);

        return cost;
    }

    private void saveResultToDB(Collection<CampaignCost> costList) {
        List<CampaignCostDetail> mergeList = new ArrayList<CampaignCostDetail>();//需要存入campaign_cost_detail表的数据

        for (CampaignCost cost : costList) {
            CampaignCostDetail detail = new CampaignCostDetail();
            BillingCampaign campaign = FinalData.STATIC_CAMPAIGN_MAP.get(cost.getCampaignId());
            if (campaign != null) {
                detail.setProjectId(campaign.getProjectId());
            }

            detail.setUserId(accountId);
            detail.setCampaignId(cost.getCampaignId());
            detail.setSupplierCode(cost.getSupplierId().toString());
            detail.setImpressionCount(cost.getImpression());
            detail.setClickCount(cost.getClick());
            detail.setCost(cost.getCost());
            detail.setRtbCost(cost.getRtbCost());
            detail.setDspCost(cost.getDspCost());
            detail.setProfit(detail.getCost() - detail.getDspCost());

            Date date = Function.parseDate(cost.getStatDate(), "yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, cost.getStatHour());

            detail.setStatTime(cal.getTime());

            mergeList.add(detail);
        }

        costScheduler.campaignCostDetailDao.addCostDetail(mergeList); //数据入库  campaign_cost_detail
    }

    public long getAccountId() {
        return accountId;
    }

    @Override
    public String getTopic() {
        return ConfigUtil.getString("kafka.topic.name.prefix.billed") + accountId;
    }


    @Override
    protected String getClientName() {
        return "billed_report_" + accountId;
    }

    @Override
    protected int getPartition() {
        return 0;
    }

}
