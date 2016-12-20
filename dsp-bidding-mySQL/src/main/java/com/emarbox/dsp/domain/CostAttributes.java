package com.emarbox.dsp.domain;

import java.util.List;
 
/**   
 * @Title: CostAttributes.java 
 * @Description: 
 * @author LiuYang
 * @date 2013-1-9 下午4:59:55 
 * @version V1.0   
 */

public class CostAttributes {
	/*
	 * 广告id
	 */
	private Long campaignId;
	/*
	 * 广告名称
	 */
	private String campaignName;
	/*
	 * 地域id
	 */
	private Long audienceId;
	/*
	 * 是否有效: 有效 1 无效 0
	 */
	private Integer enabled;
	/*
	 * 活动行业名称
	 */
	private String text;
	/*
	 * 活动行业的多个域名组成的字符患
	 */
	private String domainName;
	/*
	 * 将domailName字符串转换的list对象
	 */
	private List<String> domainList;
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domailName) {
		this.domainName = domailName;
	}
	public List<String> getDomainList() {
		return domainList;
	}
	public void setDomainList(List<String> domainList) {
		this.domainList = domainList;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CostAttributes [campaignId=");
		builder.append(campaignId);
		builder.append(", campaignName=");
		builder.append(campaignName);
		builder.append(", audienceId=");
		builder.append(audienceId);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append(", text=");
		builder.append(text);
		builder.append(", domainName=");
		builder.append(domainName);
		builder.append(", domainList=");
		builder.append(domainList);
		builder.append("]");
		return builder.toString();
	}

	
}

 
