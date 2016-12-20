package com.emarbox.dspmonitor.costing.dao;

import com.emarbox.dspmonitor.data.CampaignCost;

import java.text.ParseException;
import java.util.Collection;

/**
 * Created by ralf.cao on 2015/1/15.
 */
public interface BillingDataDao {
    void billingCampaignCost(Collection<CampaignCost> costList) throws ParseException;

    /**
     * 获取某一天的活动确认数据
     * @param campaignId
     * @param statDate yyyyMMdd
     * @return
     */
    double getConfirmedCost(long campaignId, String statDate);

}
