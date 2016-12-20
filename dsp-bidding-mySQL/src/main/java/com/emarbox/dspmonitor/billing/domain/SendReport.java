package com.emarbox.dspmonitor.billing.domain;

public class SendReport {
	private Long impression = 0L;
	private Long click = 0L;
	private Double rtbCost = 0d;
	private Double dspCost = 0d;
	private Double cost = 0d;
	private String requestId;
	private String clickId;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getClickId() {
		return clickId;
	}

	public void setClickId(String clickId) {
		this.clickId = clickId;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
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

	@Override
	public String toString() {
		return "SendReport [impression=" + impression + ", click=" + click
				+ ", rtbCost=" + rtbCost + ", dspCost=" + dspCost + ", cost="
				+ cost + "]";
	}

}
