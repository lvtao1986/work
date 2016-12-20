package com.emarbox.dsp.cost.dao;


/**
 * 结算数据处理DAO
 * @author zhaidw
 *
 */
public interface CampaignCostDetailDao {

	/**
	 * 清除一天前的数据
	 */
	void cleanOldData();
}
