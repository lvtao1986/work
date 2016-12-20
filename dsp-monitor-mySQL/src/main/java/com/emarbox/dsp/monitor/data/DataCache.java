package com.emarbox.dsp.monitor.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataCache {
	/**
	 * 活动的最高限价map
	 */
	private static Map<String,Double> maxPriceMap = Collections.synchronizedMap(new HashMap<String, Double>());
	
	/**
	 * 创意id map <creativeSetId,creativeId>
	 */
	private static Map<String,String> creativeIdMap = Collections.synchronizedMap(new HashMap<String, String>());
	
	/**
	 * 活动回头客设置map <campaignId,0/1>
	 */
	private static Map<String,String> retargetMap = Collections.synchronizedMap(new HashMap<String, String>());
	
	/**
	 * 活动毛利率要求设置
	 * <campaignId,rate>
	 */
	public static Map<Long,Double> CAPAIGN_PROFIT_RATE_CONFIG = Collections.synchronizedMap(new HashMap<Long, Double>());
	
	/**
	 * 活动主账号map<campiangId,userId>
	 */
	public static Map<Long,Long> campaignUserIdMap = Collections.synchronizedMap(new HashMap<Long, Long>());
	
	/**
	 * 通过活动ID从map中获取最高限价
	 * @param campaignId
	 * @return
	 */
	public static Double getMaxPrice(String campaignId){
	    Double maxPrice = maxPriceMap.get(campaignId);
	    if(maxPrice == null){
	    	return 0d;
	    }
	    return maxPrice;
	}
	
	/**
	 * 根据审核id活动素材id
	 * @param creativeSetId
	 * @return
	 */
	public static String getCreativeId(String creativeSetId){
		String creativeId = creativeIdMap.get(creativeSetId);
		if(creativeId == null){
			return "";
		}else{
			return creativeId;
		}
	}
	
	public static void setMaxPriceMap(Map<String, Double> maxPriceMap) {
		DataCache.maxPriceMap.putAll(maxPriceMap);
	}
	
	public static void setCreativeIdMap(Map<String, String> creativeIdMap) {
		DataCache.creativeIdMap.putAll(creativeIdMap);
	}
	
	public static void setRetargetMap(Map<String,String> retargetMap){
		DataCache.retargetMap.putAll(retargetMap);
	}
	
	public static void setCampaignUserIdMap(Map<Long,Long> map){
		DataCache.campaignUserIdMap.putAll(map);
	}

	public static String getRetarget(String campaignId) {
		String retarget = retargetMap.get(campaignId);
		if(retarget == null){
			retarget = "0";
		}
		return retarget;
	}
}
