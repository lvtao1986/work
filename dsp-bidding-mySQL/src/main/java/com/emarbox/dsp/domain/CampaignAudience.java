/**
 * CampaignAudience.java
 * com.emarbox.dsp.domain
 *
 * ver  1.0
 * date 2012-6-3
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * CampaignAudience
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-3	下午8:09:41
 *
 */
public class CampaignAudience extends DspDomain{

	private static final long serialVersionUID = -7231514709112646888L;

	/**
	 * 计划ID
	 */
	private Long campaignId;
	
	/**
	 * 人群属性ID
	 */
	private Long audienceId;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getAudienceId() {
		return audienceId;
	}

	public void setAudienceId(Long audienceId) {
		this.audienceId = audienceId;
	}

	@Override
	public String toString() {
		return "CampaignAudience [campaignId=" + campaignId + ", audienceId="
				+ audienceId + "]";
	}
}

