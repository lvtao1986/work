package com.emarbox.dsp.domain;

public class RuleGroup {
	private Long ruleGroupId;
	private Long campaignId;
	private String ruleGroupCode;
	private Boolean deleted;

	public Long getRuleGroupId() {
		return ruleGroupId;
	}

	public void setRuleGroupId(Long ruleGroupId) {
		this.ruleGroupId = ruleGroupId;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}


	public String getRuleGroupCode() {
		return ruleGroupCode;
	}

	public void setRuleGroupCode(String ruleGroupCode) {
		this.ruleGroupCode = ruleGroupCode;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
