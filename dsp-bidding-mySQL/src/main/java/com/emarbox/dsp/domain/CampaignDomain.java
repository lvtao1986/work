/**
 * CampaignDomain.java
 * com.emarbox.dsp.domain
 *
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
 */

package com.emarbox.dsp.domain;

/**
 * CampaignDomain 活动域名限制
 * 
 * @author 耿志新
 * 
 */
public class CampaignDomain extends Domain{

	/**
	 * 计划ID
	 */
	private Long campaignId;


	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	@Override
	public String toString() {
		return "CampaignDomain [campaignId=" + campaignId + "]";
	}

}
