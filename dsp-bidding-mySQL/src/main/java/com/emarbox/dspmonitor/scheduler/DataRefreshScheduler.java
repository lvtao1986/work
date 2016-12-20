package com.emarbox.dspmonitor.scheduler;


import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dspmonitor.billing.util.BaseScheduler;
import com.emarbox.dspmonitor.costing.dao.CampaignCostDetailDao;

/**
 * 结算报表,将以确定的扣费数据汇总入 campaign_cost_detail 表
 *
 * @author mr_zhu
 */
public class DataRefreshScheduler extends BaseScheduler {

    public static final Long NODE_SLEEP_TIME = 10 * 1000L;

    @Autowired
    CampaignCostDetailDao campaignCostDetailDao;

    @Autowired
    BillingUtil billingUtil;

    public void execute() {
        log.info("start CostReportScheduler");

        while (true) {
            try {
                billingUtil.setCampaignProfitRateMap(campaignCostDetailDao.getCammpaignTodayProfitRate()); //活动当天毛利
                Thread.sleep(NODE_SLEEP_TIME); //休眠 10s
            } catch (Exception e) {
                log.error("开启计费定时任务[数据刷新]异常：" + e.getMessage(), e);
                try {
                    Thread.sleep(500); //休眠 10s
                }catch (Exception evt){
                    log.error("开启计费定时任务[数据刷新-休眠]异常：" + e.getMessage(), e);
                }
            }
        }

    }

}
