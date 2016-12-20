package com.emarbox.dsp.domain;
 
/**   
 * @Title: CampaignPurchaseConfig.java 
 * @Description: 
 * @author LiuYang
 * @date 2013-1-10 下午3:45:55 
 * @version V1.0   
 */

public class CampaignPurchaseConfig {
	/*
	 * 广告id
	 */
	private Long campaignId;

	/*
	 * 地域id
	 */
	private Long audienceId;
	/*
	 * 是否有效: 有效 1 无效 0
	 */
	private Integer enabled;
	
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
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CampaignPurchaseConfig [campaignId=");
		builder.append(campaignId);
		builder.append(", audienceId=");
		builder.append(audienceId);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}

 
