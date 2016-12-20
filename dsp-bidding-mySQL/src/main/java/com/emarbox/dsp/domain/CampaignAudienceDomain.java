package com.emarbox.dsp.domain;

 
/**   
 * @Title: CampaignAudienceDomain.java 
 * @Description: 
 * @author LiuYang
 * @date 2013-1-9 下午3:55:57 
 * @version V1.0   
 */

public class CampaignAudienceDomain extends DspDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 主键id
	 */
	private Long id;
	/*
	 * 广告活动id
	 */
	private Long campaignId;
	/*
	 * 行业分类id
	 */
	private Long audienceId;
	/*
	 * 域名id
	 */
	private Long domainId;
	/**
	 * 域名显示顺序
	 */
	private Integer displayOrder;
	/*
	 * 创建人
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Long getDomainId() {
		return domainId;
	}
	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CampaignAudienceDomain [id=");
		builder.append(id);
		builder.append(", campaignId=");
		builder.append(campaignId);
		builder.append(", audienceId=");
		builder.append(audienceId);
		builder.append(", domainId=");
		builder.append(domainId);
		builder.append(", displayOrder=");
		builder.append(displayOrder);
		builder.append("]");
		return builder.toString();
	}
		
	
	
	
	

}

 
