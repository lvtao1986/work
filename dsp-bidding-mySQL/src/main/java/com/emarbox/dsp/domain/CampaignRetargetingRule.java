package com.emarbox.dsp.domain;

import app.common.util.EscapeUtil;

/**
 * 访客重定向规则
 * 
 * @author zhaidw
 * 
 */
public class CampaignRetargetingRule extends DspDomain{

	/**
	 * serialVersionUID
	 *
	 * @since Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	private Long ruleId;

	private String ruleName;

	private Long campaignId;

	// 首次访问/最近访问
	private String visitType;

	// 首次访问/最近访问 多少天 内
	private Integer visitDay;
	
	/**
	 * 访客代码
	 */
	private String visitCode;
	
	/**
	 * 重定向部署代码
	 */
	private String ruleCode;

	public CampaignRetargetingRule() {
		super();
	}

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

	public String getVisitType() {
		return EscapeUtil.escape(visitType);
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Integer getVisitDay() {
		return visitDay;
	}

	public void setVisitDay(Integer visitDay) {
		this.visitDay = visitDay;
	}

	public String getRuleName() {
		return EscapeUtil.escape(ruleName);
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getVisitCode() {
		return visitCode;
	}

	public void setVisitCode(String visitCode) {
		this.visitCode = visitCode;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	@Override
	public String toString() {
		return "CampaignRetargetingRule [ruleId=" + ruleId + ", ruleName="
				+ ruleName + ", campaignId=" + campaignId + ", visitType="
				+ visitType + ", visitDay=" + visitDay + ", visitCode="
				+ visitCode + ", ruleCode=" + ruleCode + "]";
	}

}
