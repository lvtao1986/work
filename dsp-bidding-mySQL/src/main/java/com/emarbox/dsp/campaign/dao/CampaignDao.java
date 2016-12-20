/**
 * AppConfigDap.java
 * com.emarbox.dsp.campaign.dao
 *
 * ver  1.0
 * date 2012-6-19
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.campaign.dao;

import java.util.List;

import com.emarbox.dsp.domain.Campaign;

/**
 * CampaignDao 活动接口
 *
 * @author  赵敬磊
 *
 */
public interface CampaignDao {

	/**
	 * 
	 * 获得活动计费方式
	 *
	 * @param  Long campaignId
	 * @return String
	 */
	String getCampaignBudgetLimit(Long campaignId);
	
	/**
	 * 获取活动的主账号ID
	 * @param campaignId
	 * @return
	 */
	Long getParentUserId(Long campaignId);
	
	/**
	 * 
	 * 获得有效的活动列表
	 *
	 * @param  
	 * @return List<Campaign>
	 */
	List<Campaign> getCampaignValid(Long parentUserId);
}

