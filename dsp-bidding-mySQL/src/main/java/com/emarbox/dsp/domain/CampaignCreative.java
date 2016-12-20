package com.emarbox.dsp.domain;

/**
 * 活动的创意信息
 * 
 * @author zhaidw
 * 
 */
public class CampaignCreative extends Creative {

	private static final long serialVersionUID = 7848432241258087362L;

	private Long campaignId;
	private Long campaignCreativeId;
	private String campaignCreativeDestinationUrl;
	private String campaignCreativeImageAlt;
	
	//二期数据结构修改后的属性
	private Long creativeId;
	private Long creativeSetId;
	
	
	/**
	 * 创意ID集合 查询使用
	 */
	private String creativeIds;

	/**
	 * 尺寸 前台使用
	 */
	private String size;
	
	private Integer deleted;
	
	/**是否自动添加监测链接 */
	private Integer autoEtcParameters;

	public CampaignCreative() {
		super();
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getCampaignCreativeId() {
		return campaignCreativeId;
	}

	public void setCampaignCreativeId(Long campaignCreativeId) {
		this.campaignCreativeId = campaignCreativeId;
	}

	public String getCampaignCreativeDestinationUrl() {
		return campaignCreativeDestinationUrl;
	}

	public void setCampaignCreativeDestinationUrl(
			String campaignCreativeDestinationUrl) {
		this.campaignCreativeDestinationUrl = campaignCreativeDestinationUrl;
	}

	public String getCampaignCreativeImageAlt() {
		return campaignCreativeImageAlt;
	}

	public void setCampaignCreativeImageAlt(String campaignCreativeImageAlt) {
		this.campaignCreativeImageAlt = campaignCreativeImageAlt;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getCreativeIds() {
		return creativeIds;
	}

	public void setCreativeIds(String creativeIds) {
		this.creativeIds = creativeIds;
	}

	public Long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}

	public Long getCreativeSetId() {
		return creativeSetId;
	}

	public void setCreativeSetId(Long creativeSetId) {
		this.creativeSetId = creativeSetId;
	}
	
	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	

	public Integer getAutoEtcParameters() {
		return autoEtcParameters;
	}

	public void setAutoEtcParameters(Integer autoEtcParameters) {
		this.autoEtcParameters = autoEtcParameters;
	}

	@Override
	public String toString() {
		return "CampaignCreative [campaignId=" + campaignId
				+ ", campaignCreativeId=" + campaignCreativeId
				+ ", campaignCreativeDestinationUrl="
				+ campaignCreativeDestinationUrl
				+ ", campaignCreativeImageAlt=" + campaignCreativeImageAlt
				+ ", creativeId=" + creativeId + ", creativeSetId="
				+ creativeSetId + ", creativeIds=" + creativeIds + ", size="
				+ size + ", deleted=" + deleted 
				+", autoEtcParameters=" + autoEtcParameters +"]";
	}

}
