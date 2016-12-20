/**
 * CampaignFrequency.java
 * com.emarbox.dsp.campaign.domain
 *
 * ver  1.0
 * date 2012-5-24
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * CampaignFrequency : 广告活动投放频次
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-5-24	下午4:42:29
 *
 */
public class CampaignFrequency extends DspDomain{
	
	private static final long serialVersionUID = -2304918503490910740L;

	/**
	 * 计划ID
	 */
	private Long campaignId;

	/**
	 * 投放周期(小时)
	 */
	private Integer hourValue;

	/**
	 * 投放次数
	 */
	private Integer frequencyValue;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getHourValue() {
		return hourValue;
	}

	public void setHourValue(Integer hourValue) {
		this.hourValue = hourValue;
	}

	public Integer getFrequencyValue() {
		return frequencyValue;
	}

	public void setFrequencyValue(Integer frequencyValue) {
		this.frequencyValue = frequencyValue;
	}

	@Override
	public String toString() {
		return "CampaignFrequency [campaignId=" + campaignId + ", hourValue="
				+ hourValue + ", frequencyValue=" + frequencyValue + "]";
	}
	
}

