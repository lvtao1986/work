package com.emarbox.dspmonitor.costing.domain;

import java.util.Date;

public class CampaignCostDetail {
	private String nodeCode;
	private String batchCode;
	private Long campaignId;
	private String supplierCode;
	private Date statTime;
	private Double cost;
	private Double rtbCost;
	private Double dspCost;
	private Double commission;
	private Double profit;
	private int confirmed = 0;
	private Double valueAddedTax;
	private Long impressionCount = 0L;
	private Long clickCount = 0L;
	private Double profitCorrectCost=0d;

	private Long projectId;
	private Long userId;
	
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

	public Double getValueAddedTax() {
		return valueAddedTax;
	}

	public void setValueAddedTax(Double valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public int getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Date getStatTime() {
		return statTime;
	}

	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}

	public Double getRtbCost() {
		return rtbCost;
	}

	public void setRtbCost(Double rtbCost) {
		this.rtbCost = rtbCost;
	}

	public Double getDspCost() {
		return dspCost;
	}

	public void setDspCost(Double dspCost) {
		this.dspCost = dspCost;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Long getImpressionCount() {
		return impressionCount;
	}

	public void setImpressionCount(Long impressionCount) {
		this.impressionCount = impressionCount;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	public Double getProfitCorrectCost() {
		return profitCorrectCost;
	}

	public void setProfitCorrectCost(Double profitCorrectCost) {
		this.profitCorrectCost = profitCorrectCost;
	}

}
