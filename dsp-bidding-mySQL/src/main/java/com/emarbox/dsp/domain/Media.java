package com.emarbox.dsp.domain;

import java.io.Serializable;

public class Media implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long mediaId;
	private Long supplierId;
	private String mediaName;
	private Long campaignId;
	public Long getMediaId() {
		return mediaId;
	}
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	
}
