package com.emarbox.dsp.monitor.dao;

import java.util.List;
import java.util.Map;

import com.emarbox.dsp.monitor.dto.AppConfig;
import com.emarbox.dsp.monitor.dto.CampaignDomain;


public interface CampaignDao {

	List<CampaignDomain> getMaxPriceList();
	
	List<CampaignDomain> getRecentMaxPriceList();
	
	AppConfig getBiddingClickTime();

	Map<String, String> getCreativeIdMap();
	
	Map<String, String> getRecentCreativeIdMap();

	Map<String, String> getRetargetMap();
	
	Map<Long, Double> queryCampaignProfitRateSet();
	
	/**
	 * 获取活动的主账号ID Map
	 */
	Map<Long,Long> getParentUserIdMap();
	
	/**
	 * 获取最近1000条活动的主账号ID Map
	 */
	Map<Long,Long> getRecentParentUserIdMap();
	
}
