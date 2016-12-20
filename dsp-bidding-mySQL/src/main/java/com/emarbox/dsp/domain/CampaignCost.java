package com.emarbox.dsp.domain;


public class CampaignCost extends DspDomain {

	private static final long serialVersionUID = -5828664908953746289L;
	
	private Long campaignId;
	private Integer statDate;
	private Long projectId;
	private Long userId;
	private Double cost;
	private String date;
	private Integer todayTotalTime;
	private Integer todayRemainTime;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getTodayTotalTime() {
		return todayTotalTime;
	}
	public void setTodayTotalTime(Integer todayTotalTime) {
		this.todayTotalTime = todayTotalTime;
	}
	public Integer getTodayRemainTime() {
		return todayRemainTime;
	}
	public void setTodayRemainTime(Integer todayRemainTime) {
		this.todayRemainTime = todayRemainTime;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	public Integer getStatDate() {
		return statDate;
	}
	public void setStatDate(Integer statDate) {
		this.statDate = statDate;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	
}
