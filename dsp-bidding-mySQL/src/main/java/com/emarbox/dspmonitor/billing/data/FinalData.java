package com.emarbox.dspmonitor.billing.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emarbox.dsp.domain.CampaignDetailReport;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;

/**
 * 设置保存数据的常量类
 * 
 * @author liumingfeng
 * 
 */
public class FinalData {

	/**
	 * 用来保存本地活动的信息
	 * <campaignId,billingCampaign>
	 */
	public static Map<Long, BillingCampaign> STATIC_CAMPAIGN_MAP =Collections.synchronizedMap( new HashMap<Long, BillingCampaign>());

	/**
	 * 用来保存活动的延迟花费
	 * <campaignId,cost>
	 */
	public static Map<Long, Double> STATIC_CAMPAIGN_COST_MAP =  Collections.synchronizedMap(new HashMap<Long, Double>());
	
	/**
	 * 活动最低毛利率要求设置
	 * <campaignId,rate>
	 */
	public static Map<Long,Double> CAPAIGN_MINI_PROFIT_RATE_CONFIG =  Collections.synchronizedMap(new HashMap<Long, Double>());
	
	/**
	 * 活动最高毛利率要求设置
	 * <campaignId,rate>
	 */
	public static Map<Long,Double> CAPAIGN_MAX_PROFIT_RATE_CONFIG =  Collections.synchronizedMap(new HashMap<Long, Double>());

}
