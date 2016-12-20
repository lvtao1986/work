package com.emarbox.dspmonitor.costing.dao;

import java.util.List;

import com.emarbox.dspmonitor.data.CampaignCost;

public interface BillingDetailDao {
	
	/**
	 * 下线判断用,查询指定账号和日期的获得花费信息
	 * @param accountId
	 * @param date
	 * @return
	 */
	List<CampaignCost> getCampaignTodayCost(long accountId, String date);
	
	
}
