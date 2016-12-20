/**
 * CampaignGeo.java
 * com.emarbox.dsp.domain
 *
 * ver  1.0
 * date 2012-6-3
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * CampaignGeo
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-3	下午8:07:20
 *
 */
public class CampaignGeo extends DspDomain{

	private static final long serialVersionUID = 2731127258819024947L;

	/**
	 * 计划Id
	 */
	private Long campaignId;
	
	/**
	 * 地域代码
	 */
	private String areaCode;
	
	/**
	 * 地域全路径名称
	 */
	private String areaFullName;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaFullName() {
		return areaFullName;
	}

	public void setAreaFullName(String areaFullName) {
		this.areaFullName = areaFullName;
	}

	@Override
	public String toString() {
		return "CampaignGeo [campaignId=" + campaignId + ", areaCode="
				+ areaCode + ", areaFullName=" + areaFullName + "]";
	}
}

