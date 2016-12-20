package com.emarbox.dsp.domain;
/**
 * 判断活动能否上线的状态类
 */
public class CampaignStatusResult {

	
	private Boolean success;
	private String message;
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
