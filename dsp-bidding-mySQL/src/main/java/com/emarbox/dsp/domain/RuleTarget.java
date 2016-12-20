package com.emarbox.dsp.domain;

public class RuleTarget {
	/**
	 * 规则id
	 */
	private Long ruleId;
	/**
	 * 目标Id
	 */
	private Long targetId;
	/**
	 * 设置排除
	 */
	private Integer isRemove;
	
	
	
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Integer getIsRemove() {
		return isRemove;
	}
	public void setIsRemove(Integer isRemove) {
		this.isRemove = isRemove;
	}

	
}
