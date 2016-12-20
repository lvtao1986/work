package com.emarbox.dsp.cost.domain;

import java.util.Date;

import com.emarbox.dsp.domain.DspDomain;

/**
 * 活动花费结算流水
 * 
 * @author zhaidw
 * 
 */
public class CampaignCostDetail extends DspDomain {

	private static final long serialVersionUID = 3896028836710868660L;

	private Long id;
	private String nodeCode;
	private String batchCode;
	private String supplierCode;
	private Long supplierId;

	private Long campaignId;
	private Date statTime;
	private Double cost;

	private Integer confirmed;
	private Double confirmedCost;
	private Date confirmedTime;

	private Double rtbCost;
	private Double dspCost;
	private Double commission;
	private Double profit;
	
	//需要支付给供应商的增值税
	private Double valueAddedTax;

	private Integer statYear;// 投放年
	private Integer statMonth;// 投放月
	private Integer statWeek;// 投放周
	private Integer statHour;

	private Long impression;
	private Long click;
	
	public Long getImpression() {
		return impression;
	}

	public void setImpression(Long impression) {
		this.impression = impression;
	}

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Date getStatTime() {
		return statTime;
	}

	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}

	public Double getConfirmedCost() {
		return confirmedCost;
	}

	public void setConfirmedCost(Double confirmedCost) {
		this.confirmedCost = confirmedCost;
	}

	public Date getConfirmedTime() {
		return confirmedTime;
	}

	public void setConfirmedTime(Date confirmedTime) {
		this.confirmedTime = confirmedTime;
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

	public Double getValueAddedTax() {
		return valueAddedTax;
	}

	public void setValueAddedTax(Double valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CampaignCostDetail [id=");
		builder.append(id);
		builder.append(", nodeCode=");
		builder.append(nodeCode);
		builder.append(", batchCode=");
		builder.append(batchCode);
		builder.append(", supplierCode=");
		builder.append(supplierCode);
		builder.append(", campaignId=");
		builder.append(campaignId);
		builder.append(", statTime=");
		builder.append(statTime);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", confirmed=");
		builder.append(confirmed);
		builder.append(", confirmedCost=");
		builder.append(confirmedCost);
		builder.append(", confirmedTime=");
		builder.append(confirmedTime);
		builder.append(", rtbCost=");
		builder.append(rtbCost);
		builder.append(", dspCost=");
		builder.append(dspCost);
		builder.append(", commission=");
		builder.append(commission);
		builder.append(", profit=");
		builder.append(profit);
		builder.append(", valueAddedTax=");
		builder.append(valueAddedTax);
		builder.append(", statYear=");
		builder.append(statYear);
		builder.append(", statMonth=");
		builder.append(statMonth);
		builder.append(", statWeek=");
		builder.append(statWeek);
		builder.append(", statHour=");
		builder.append(statHour);
		builder.append("]");
		return builder.toString();
	}

	

}
