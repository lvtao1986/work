package com.emarbox.dsp.domain;

/**
 * 访客地区
 * @author zhaidw
 *
 */
public class VisitorArea extends CampaignPreference {
	
	/**
	 * 地域代码
	 */
	private String areaCode;
	
	/**
	 * 地域中文名称
	 */
	private String areaName;
	
	/**
	 * 地域行政级别
	 */
	private String areaLevel;
	
	/**
	 * 地域父级代码
	 */
	private String parentAreaCode;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getParentAreaCode() {
		return parentAreaCode;
	}

	public void setParentAreaCode(String parentAreaCode) {
		this.parentAreaCode = parentAreaCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VisitorArea [areaCode=");
		builder.append(areaCode);
		builder.append(", areaName=");
		builder.append(areaName);
		builder.append(", areaLevel=");
		builder.append(areaLevel);
		builder.append(", parentAreaCode=");
		builder.append(parentAreaCode);
		builder.append(", getCode()=");
		builder.append(getCode());
		builder.append(", getText()=");
		builder.append(getText());
		builder.append(", getInclude()=");
		builder.append(getInclude());
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getParentId()=");
		builder.append(getParentId());
		builder.append(", getParentCode()=");
		builder.append(getParentCode());
		builder.append("]");
		return builder.toString();
	}


}
