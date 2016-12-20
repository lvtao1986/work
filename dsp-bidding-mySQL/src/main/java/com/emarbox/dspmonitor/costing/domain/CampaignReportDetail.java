package com.emarbox.dspmonitor.costing.domain;

import java.util.Date;

public class CampaignReportDetail {
	private Long campaignId;
	private Long supplierId;
	private Long projectId;
	private Long userId;
	private Long parentUserId;
	private Long displayCount;
	private Long clickCount;
	private Date statDate;
	private Integer statYear;
	private Integer statMonth;
	private Integer statWeek;
	private Integer statHour;
	private String updateUser;
	private Date updateTime;
	private Double cost;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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

	public Long getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(Long parentUserId) {
		this.parentUserId = parentUserId;
	}

	public Long getDisplayCount() {
		return displayCount;
	}

	public void setDisplayCount(Long displayCount) {
		this.displayCount = displayCount;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public Integer getStatYear() {
		return statYear;
	}

	public void setStatYear(Integer statYear) {
		this.statYear = statYear;
	}

	public Integer getStatMonth() {
		return statMonth;
	}

	public void setStatMonth(Integer statMonth) {
		this.statMonth = statMonth;
	}

	public Integer getStatWeek() {
		return statWeek;
	}

	public void setStatWeek(Integer statWeek) {
		this.statWeek = statWeek;
	}

	public Integer getStatHour() {
		return statHour;
	}

	public void setStatHour(Integer statHour) {
		this.statHour = statHour;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
}
