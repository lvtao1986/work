/**
 * CampaignResult.java
 * com.emarbox.dsp.campaign.domain
 *
 * ver  1.0
 * date 2012-5-24
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * CampaignResult : 计划操作返回结果
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-5-24	下午3:56:50
 *
 */
public class CampaignResult extends BaseResult{
	
	/**
	 * 计划操作结果
	 */
	
	public String status;
	/**
	 * 计划ID
	 */
	private Long campaignId;
	
	/**
	 * 创意Id
	 */
	private Long creativeId;
	
	/**
	 * 创意名称
	 */
	private String creativeName;
	
	/**
	 * 创意尺寸
	 */
	private String creativeSize;
	
	/**
	 * 创意图片地址
	 */
	private String creativeFileUrl;
	
	/**
	 * 创意操作状态
	 */
	public String creativeStatus;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	


	public String getCreativeStatus() {
		return creativeStatus;
	}

	public void setCreativeStatus(String creativeStatus) {
		this.creativeStatus = creativeStatus;
	}

	public Long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}

	public String getCreativeName() {
		return creativeName;
	}

	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}

	public String getCreativeSize() {
		return creativeSize;
	}

	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}

	public String getCreativeFileUrl() {
		return creativeFileUrl;
	}

	public void setCreativeFileUrl(String creativeFileUrl) {
		this.creativeFileUrl = creativeFileUrl;
	}

	@Override
	public String toString() {
		return "CampaignResult [status=" + status + ", campaignId="
				+ campaignId + ", creativeId=" + creativeId + ", creativeName="
				+ creativeName + ", creativeSize=" + creativeSize
				+ ", creativeFileUrl=" + creativeFileUrl + ", creativeStatus="
				+ creativeStatus + "]";
	}
}

