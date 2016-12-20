package com.emarbox.dsp.domain;

public class AppConfig extends DspDomain {

	private static final long serialVersionUID = -7285086212039346260L;
	
	private String paramCode;
	private String paramName; 
	private String paramValue; 
	private String displayOrder;
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppConfig [getParamCode()=");
		builder.append(getParamCode());
		builder.append(", getParamName()=");
		builder.append(getParamName());
		builder.append(", getParamValue()=");
		builder.append(getParamValue());
		builder.append(", getDisplayOrder()=");
		builder.append(getDisplayOrder());
		builder.append(", getCreateUserId()=");
		builder.append(getCreateUserId());
		builder.append(", getUpdateUserId()=");
		builder.append(getUpdateUserId());
		builder.append(", getCreateUser()=");
		builder.append(getCreateUser());
		builder.append(", getUpdateUser()=");
		builder.append(getUpdateUser());
		builder.append(", getCreateTime()=");
		builder.append(getCreateTime());
		builder.append(", getUpdateTime()=");
		builder.append(getUpdateTime());
		builder.append(", getProjectId()=");
		builder.append(getProjectId());
		builder.append(", getUserId()=");
		builder.append(getUserId());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append(", getR()=");
		builder.append(getR());
		builder.append(", getClass()=");
		builder.append(getClass());
		builder.append(", hashCode()=");
		builder.append(hashCode());
		builder.append("]");
		return builder.toString();
	} 
	
	
	
}
