/**
 * CampaignRetargeting.java
 * com.emarbox.dsp.domain
 *
 * ver  1.0
 * date 2012-6-7
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * CampaignRetargeting 广告活动回头客
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-7	下午8:56:39
 *
 */
public class CampaignRetargeting extends DspDomain{

	private static final long serialVersionUID = -7210076998281641330L;

	/**
	 * 计划ID
	 */
	private Long campaignId;
	
	/**
	 * 全部/部分老访客
	 */
	private String oldVisitorType;
	
	/**
	 * 是否包含新访客
	 */
	private Integer includeNewVisitor;
	
	/**
	 * 是否包含看过广告的客户
	 */
	private Integer includeAdViewer;
	
	/**
	 * 全部老访客投放代码
	 */
	private String newVisitorCode;
	
	/**
	 * 新访客投放代码
	 */
	private String allOldVisitorCode;
	
	/**
	 * 是否包含全部老访客
	 */
	private Integer includeAllOldVisitor;
	/**
	 * 全部老访客规则组ID
	 */
	private Long allOldVisitorRuleId;
	/**
	 * 新房客规则组ID
	 */
	private Long newVisitorRuleId;

	public Integer getIncludeAllOldVisitor() {
		return includeAllOldVisitor;
	}

	public void setIncludeAllOldVisitor(Integer includeAllOldVisitor) {
		this.includeAllOldVisitor = includeAllOldVisitor;
	}

	public Long getAllOldVisitorRuleId() {
		return allOldVisitorRuleId;
	}

	public void setAllOldVisitorRuleId(Long allOldVisitorRuleId) {
		this.allOldVisitorRuleId = allOldVisitorRuleId;
	}

	public Long getNewVisitorRuleId() {
		return newVisitorRuleId;
	}

	public void setNewVisitorRuleId(Long newVisitorRuleId) {
		this.newVisitorRuleId = newVisitorRuleId;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getOldVisitorType() {
		return oldVisitorType;
	}

	public void setOldVisitorType(String oldVisitorType) {
		this.oldVisitorType = oldVisitorType;
	}

	public Integer getIncludeNewVisitor() {
		return includeNewVisitor;
	}

	public void setIncludeNewVisitor(Integer includeNewVisitor) {
		this.includeNewVisitor = includeNewVisitor;
	}

	public Integer getIncludeAdViewer() {
		return includeAdViewer;
	}

	public void setIncludeAdViewer(Integer includeAdViewer) {
		this.includeAdViewer = includeAdViewer;
	}

	public String getNewVisitorCode() {
		return newVisitorCode;
	}

	public void setNewVisitorCode(String newVisitorCode) {
		this.newVisitorCode = newVisitorCode;
	}

	public String getAllOldVisitorCode() {
		return allOldVisitorCode;
	}

	public void setAllOldVisitorCode(String allOldVisitorCode) {
		this.allOldVisitorCode = allOldVisitorCode;
	}

	@Override
	public String toString() {
		return "CampaignRetargeting [campaignId=" + campaignId
				+ ", oldVisitorType=" + oldVisitorType + ", includeNewVisitor="
				+ includeNewVisitor + ", includeAdViewer=" + includeAdViewer
				+ ", newVisitorCode=" + newVisitorCode + ", allOldVisitorCode="
				+ allOldVisitorCode + "]";
	}
}

