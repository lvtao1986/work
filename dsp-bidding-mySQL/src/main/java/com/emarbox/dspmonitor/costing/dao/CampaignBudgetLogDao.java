package com.emarbox.dspmonitor.costing.dao;

import java.util.List;

import com.emarbox.dsp.campaign.domain.CampaignBudgetLog;
import com.emarbox.dsp.domain.Campaign;

public interface CampaignBudgetLogDao {

	Campaign queryCampaign(Long campaignId);

	Double queryAppConfig(String config);

	List<CampaignBudgetLog> querYesterdayBudgetLog(Long campaignId);
}
