package com.emarbox.dsp.monitor.dto;

public class CampaignDomain {

	private Long campaignId; 
	private Double maxPrice;
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	public Double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
	@Override
	public String toString() {
		return "CampaignDomain [campaignId=" + campaignId + ", maxPrice=" + maxPrice + "]";
	}
	
	
}
