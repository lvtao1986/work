package com.emarbox.dsp.monitor.service;

import java.util.Map;

public interface CampaignService {

	/**
	 * 获取所有活动的最高限价Map
	 * 
	 */
	Map<String, Double> getMaxPriceMap();
	
	/**
	 * 获取最近1000条活动的最高限价Map
	 * 
	 */
	Map<String, Double> getRecentMaxPriceMap();
	
	/**
	 * 获取所有creative的id对应map
	 */
	Map<String,String> getCreativeIdMap();
	
	/**
	 * 获取最近1000条creative的id对应map
	 */
	Map<String,String> getRecentCreativeIdMap();

	/**
	 * 获取所有campaign的回头客设置<campaignId,0/1>
	 */
	Map<String,String> getRetargetMap();
	
	Map<Long, Double> queryCampaignProfitRateSet();
	
	Map<Long,Long> queryCampaignUserIdMap();
	
	Map<Long,Long> queryRecentCampaignUserIdMap();
}
