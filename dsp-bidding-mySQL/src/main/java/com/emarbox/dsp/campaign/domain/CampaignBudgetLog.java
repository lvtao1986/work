package com.emarbox.dsp.campaign.domain;

import java.util.Date;

import com.emarbox.dsp.domain.DspDomain;

public class CampaignBudgetLog extends DspDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long campaignId;
	private Double dailyBudget;
	private Double totalBudget;
	private Double oldDailyBudget;
	private Double oldTotalBudget;
	private Date logTime;
	private String updateUser;
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
	public Double getDailyBudget() {
		return dailyBudget;
	}
	public void setDailyBudget(Double dailyBudget) {
		this.dailyBudget = dailyBudget;
	}
	public Double getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}
	public Double getOldDailyBudget() {
		return oldDailyBudget;
	}
	public void setOldDailyBudget(Double oldDailyBudget) {
		this.oldDailyBudget = oldDailyBudget;
	}
	public Double getOldTotalBudget() {
		return oldTotalBudget;
	}
	public void setOldTotalBudget(Double oldTotalBudget) {
		this.oldTotalBudget = oldTotalBudget;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	

}
