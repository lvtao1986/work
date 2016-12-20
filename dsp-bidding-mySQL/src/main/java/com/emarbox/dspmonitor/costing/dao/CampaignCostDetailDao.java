package com.emarbox.dspmonitor.costing.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.emarbox.dsp.domain.CampaignSnapshot;
import com.emarbox.dspmonitor.costing.domain.CampaignCostDetail;
import com.emarbox.dspmonitor.data.CampaignCost;

public interface CampaignCostDetailDao {

	void addCostDetail(List<CampaignCostDetail> costList);
	
	/**
	 * 获得活动最近的操作记录
	 */
	CampaignSnapshot getCampaignSnapshot(CampaignSnapshot history);
	
	/**
	 * 获得5分钟内的上线记录
	 * @param history
	 * @return
	 */
	CampaignSnapshot getFiveMinuteValidSnapshot(CampaignSnapshot history);
	
	/**
	 * 获取活动当日的毛利率信息
	 * @return <campaignId,profitRate>
	 */
	Map<Long, Double> getCammpaignTodayProfitRate();
	
	void mergePreBillingDetail(Collection<CampaignCost> costList);
}
