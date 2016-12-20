package com.emarbox.dspmonitor.billing.dao;

import java.util.List;
import java.util.Map;

import com.emarbox.dspmonitor.billing.domain.BillingAccountInfo;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;

public interface BillingCostDao {
	List<BillingCampaign> queryCampaignCostList();

	List<BillingAccountInfo> queryAccountCostList();
	
	Map<Long, Double> queryCampaignMiniProfitRateSet();
	
	Map<Long, Double> queryCampaignMaxProfitRateSet();

}
