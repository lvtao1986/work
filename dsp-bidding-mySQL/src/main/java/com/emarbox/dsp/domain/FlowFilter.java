package com.emarbox.dsp.domain;

public class FlowFilter extends DspDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long campaignId;
	private Integer isFilter;//是否开启筛选
	private Double targetArrial;//到达率指标
	private Double targetJump;//跳出率指标
	private Double targetTime;//停留时间指标
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
	public Integer getIsFilter() {
		return isFilter;
	}
	public void setIsFilter(Integer isFilter) {
		this.isFilter = isFilter;
	}
	public Double getTargetArrial() {
		return targetArrial;
	}
	public void setTargetArrial(Double targetArrial) {
		this.targetArrial = targetArrial;
	}
	public Double getTargetJump() {
		return targetJump;
	}
	public void setTargetJump(Double targetJump) {
		this.targetJump = targetJump;
	}
	public Double getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(Double targetTime) {
		this.targetTime = targetTime;
	}
	
	
}
