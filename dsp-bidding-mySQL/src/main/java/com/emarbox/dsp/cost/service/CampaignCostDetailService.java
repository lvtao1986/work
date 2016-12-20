package com.emarbox.dsp.cost.service;


/**
 * 结算数据处理接口
 * @author zhaidw
 *
 */
public interface CampaignCostDetailService {

	
	/**
	 * 清除旧的CampaignCostDetail数据：一天之前的数据
	 */
	void cleanOldData();
	
}
