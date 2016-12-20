package com.emarbox.dspmonitor.billing.util;

import org.springframework.beans.factory.annotation.Autowired;

import app.common.util.LogUtil;
import app.common.util.Logs;

import com.emarbox.dsp.DspBaseService;
import com.emarbox.dspmonitor.billing.service.BillingCostService;
import com.emarbox.dspmonitor.billing.service.BillingSendCampaignService;

public class BaseScheduler extends DspBaseService{

	protected Logs log;
	
	
	public BaseScheduler() {
		log = LogUtil.getLog(getClass());
	}
	
	@Autowired
	private BillingSendCampaignService billingSendCampaignService;

	@Autowired
	private BillingCostService billingCostService;

	public BillingSendCampaignService getBillingSendCampaignService() {
		return billingSendCampaignService;
	}

	public void setBillingSendCampaignService(BillingSendCampaignService billingSendCampaignService) {
		this.billingSendCampaignService = billingSendCampaignService;
	}

	public BillingCostService getBillingCostService() {
		return billingCostService;
	}

	public void setBillingCostService(BillingCostService billingCostService) {
		this.billingCostService = billingCostService;
	}

	
}