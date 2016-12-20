/**
 * CampaignDayParting.java
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
 * CampaignDayParting: 广告活动展现时间段
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-5-24	下午4:40:21
 *
 */
public class CampaignDayParting extends DspDomain{
	
	private static final long serialVersionUID = 6491221147627344570L;


	/**
	 * 投放周期类型： 月（值范围： 1-12）
	 */
	public static final String PERIOD_TYPE_MONTH = "month";
	
	
	/**
	 * 投放周期类型： 天（值范围： 1-31）
	 */
	public static final String PERIOD_TYPE_DAY = "day";
	
	/**
	 * 投放周期类型： 星期（值范围： 1-7）
	 */
	public static final String PERIOD_TYPE_WEEK = "week";
	
	/**
	 * 投放周期类型： 小时（值范围： 0-23）
	 */
	public static final String PERIOD_TYPE_HOUR= "hour";
	
	/**
	 *  广告活动展现时间段ID
	 */
	private Long campaignDayPartingId;
	
	/**
	 * 计划ID
	 */
	private Long campaignId;
	
	/**
	 * 父ID
	 */
	private Long parentId;
	
	/**
	 * 时间段类型
	 */
	private String periodType;
	
	/**
	 * 时间段数值区间
	 */
	private String periodValue;

	public Long getCampaignDayPartingId() {
		return campaignDayPartingId;
	}

	public void setCampaignDayPartingId(Long campaignDayPartingId) {
		this.campaignDayPartingId = campaignDayPartingId;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getPeriodValue() {
		return periodValue;
	}

	public void setPeriodValue(String periodValue) {
		this.periodValue = periodValue;
	}

	@Override
	public String toString() {
		return "CampaignDayParting [campaignDayPartingId="
				+ campaignDayPartingId + ", campaignId=" + campaignId
				+ ", parentId=" + parentId + ", periodType=" + periodType
				+ ", periodValue=" + periodValue + "]";
	}
	
}

