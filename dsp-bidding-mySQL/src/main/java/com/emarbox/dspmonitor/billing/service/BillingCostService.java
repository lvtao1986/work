package com.emarbox.dspmonitor.billing.service;

import java.util.Map;

import com.emarbox.dspmonitor.billing.domain.BillingCampaign;

public interface BillingCostService {

	Map<Long, BillingCampaign> getCampaignCostList();
	
	Map<Long,Double> getAccountCostList();
	
}
