package com.emarbox.dspmonitor.billing.service;

import java.util.List;

import com.emarbox.dsp.api.domain.DspApiResult;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;

public interface BillingSendCampaignService {

	DspApiResult sendCampaignStop(List<BillingCampaign> infoList);
}
