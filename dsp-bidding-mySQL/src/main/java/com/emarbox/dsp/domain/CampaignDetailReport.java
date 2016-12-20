package com.emarbox.dsp.domain;

import java.util.Date;

public class CampaignDetailReport extends DspDomain {

	private static final long serialVersionUID = 1123554003103364820L;
	
	// 基本数据
	private Long id; // 报表ID
	private String projectName; // 项目名称
	private Long campaignId; // 计划ID
	private Long supplierId; //广告平台Id
	private String campaignName; // 计划名称
	private Date statDate; // 投放时间
	private Long biddingCount; // 投放数
	private Long displayCount; // 展现数
	private Long clickCount; // 点击数
	private Double cost; // 消费
	private Integer statYear;// 投放年
	private Integer statMonth;// 投放月
	private Integer statWeek;// 投放周
	private Integer statHour;
	private Double biddingCost;
	private Double rtbCost;
	private Double dspCost;
	private String nodeCode;
	private String batchCode;
	// 衍生数据
	private Double clickRate; // 点击率
	private Double displayRate; // 展现率

	private Double cpm;
	private Double cpc;
	
	private String weekstart;
	private String weekend;

	private String stateTime;

	// 百分号字符串
	public final String percentStr = "%";
	public final String dollarSign = "￥";

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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getId() {
		return id;
	}

	public Double getCpm() {
		return cpm;
	}

	public void setCpm(Double cpm) {
		this.cpm = cpm;
	}

	public Double getCpc() {
		return cpc;
	}

	public void setCpc(Double cpc) {
		this.cpc = cpc;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public Long getBiddingCount() {
		return biddingCount;
	}

	public void setBiddingCount(Long biddingCount) {
		this.biddingCount = biddingCount;
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

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
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

	public Double getClickRate() {
		return clickRate;
	}

	public void setClickRate(Double clickRate) {
		this.clickRate = clickRate;
	}

	public Double getDisplayRate() {
		return displayRate;
	}

	public void setDisplayRate(Double displayRate) {
		this.displayRate = displayRate;
	}

	public String getWeekstart() {
		return weekstart;
	}

	public void setWeekstart(String weekstart) {
		this.weekstart = weekstart;
	}

	public String getWeekend() {
		return weekend;
	}

	public void setWeekend(String weekend) {
		this.weekend = weekend;
	}

	public String getStateTime() {
		return stateTime;
	}

	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}

	public Double getBiddingCost() {
		return biddingCost;
	}

	public void setBiddingCost(Double biddingCost) {
		this.biddingCost = biddingCost;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CampaignDetailReport [id=");
		builder.append(id);
		builder.append(", projectName=");
		builder.append(projectName);
		builder.append(", campaignId=");
		builder.append(campaignId);
		builder.append(", campaignName=");
		builder.append(campaignName);
		builder.append(", statDate=");
		builder.append(statDate);
		builder.append(", biddingCount=");
		builder.append(biddingCount);
		builder.append(", displayCount=");
		builder.append(displayCount);
		builder.append(", clickCount=");
		builder.append(clickCount);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", statYear=");
		builder.append(statYear);
		builder.append(", statMonth=");
		builder.append(statMonth);
		builder.append(", statWeek=");
		builder.append(statWeek);
		builder.append(", statHour=");
		builder.append(statHour);
		builder.append(", clickRate=");
		builder.append(clickRate);
		builder.append(", displayRate=");
		builder.append(displayRate);
		builder.append(", weekstart=");
		builder.append(weekstart);
		builder.append(", weekend=");
		builder.append(weekend);
		builder.append(", stateTime=");
		builder.append(stateTime);
		builder.append(", percentStr=");
		builder.append(percentStr);
		builder.append(", dollarSign=");
		builder.append(dollarSign);
		builder.append("]");
		return builder.toString();
	}

}
