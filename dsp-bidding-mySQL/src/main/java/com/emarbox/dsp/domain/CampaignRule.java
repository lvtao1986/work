package com.emarbox.dsp.domain;

public class CampaignRule {
	/**
	 * 规则id
	 */
	private Long ruleId;
	/**
	 * 活动id
	 */
	private Long campaignId;
	
	/**
	 * 规则类型
	 */
	private String ruleType;
	/**
	 * 规则有效期
	 */
	private Integer vaildDays;
	/**
	 * 规则是否有效
	 */
	private Integer ruleStatus;
	/**
	 * 规则目标表中设置排除
	 */
	private Integer isRemove;
	/**
	 * 更新标识
	 */
	private Integer updateFlag;
	/**
	 * 目标表目标名称
	 */
	private long projectId;
	
	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getVaildDays() {
		return vaildDays;
	}

	public void setVaildDays(Integer vaildDays) {
		this.vaildDays = vaildDays;
	}

	public Integer getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(Integer ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public Integer getIsRemove() {
		return isRemove;
	}

	public void setIsRemove(Integer isRemove) {
		this.isRemove = isRemove;
	}

	public Integer getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(Integer updateFlag) {
		this.updateFlag = updateFlag;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	
}
