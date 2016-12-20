/**
 * Visitors.java
 * com.emarbox.dsp.domain
 *
 * ver  1.0
 * date 2012-6-5
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;

import app.common.util.EscapeUtil;

/**
 * Visitors 访客重定向规则
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-5	下午8:36:42
 *
 */
public class RetargetingRule extends DspDomain{

	/**
	 * serialVersionUID
	 *
	 * @since Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * 重定向规则ID
	 */
	private Long retargetingRuleId;
	
	/**
	 * 名称
	 */
	private String ruleName;
	
	/**
	 * 重定向部署代码
	 */
	private String ruleCode;
	
	/**
	 * 访问人数（累计）
	 */
	private Integer visitorCount;
	
	/**
	 * 规则的使用数
	 */
	private Integer campaignRetargetingRuleCount;

	public Long getRetargetingRuleId() {
		return retargetingRuleId;
	}

	public void setRetargetingRuleId(Long retargetingRuleId) {
		this.retargetingRuleId = retargetingRuleId;
	}

	public String getRuleName() {
		return EscapeUtil.escape(ruleName);
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public Integer getVisitorCount() {
		return visitorCount;
	}

	public void setVisitorCount(Integer visitorCount) {
		this.visitorCount = visitorCount;
	}

	public Integer getCampaignRetargetingRuleCount() {
		return campaignRetargetingRuleCount;
	}

	public void setCampaignRetargetingRuleCount(Integer campaignRetargetingRuleCount) {
		this.campaignRetargetingRuleCount = campaignRetargetingRuleCount;
	}

	@Override
	public String toString() {
		return "RetargetingRule [retargetingRuleId=" + retargetingRuleId
				+ ", ruleName=" + ruleName + ", ruleCode=" + ruleCode
				+ ", visitorCount=" + visitorCount
				+ ", campaignRetargetingRuleCount="
				+ campaignRetargetingRuleCount + "]";
	}
}

