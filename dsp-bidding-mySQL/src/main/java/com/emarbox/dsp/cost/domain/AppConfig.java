package com.emarbox.dsp.cost.domain;

public class AppConfig {

	/**
	 * 参数代码
	 */
	private String paramCode;

	/**
	 * 参数名称
	 */
	private String paramName;

	/**
	 * 参数值
	 */
	private String paramValue;

	/**
	 * 配置项值类型代码
	 */
	private String valueTypeCode;

	/**
	 * 配置项值后缀
	 */
	private String valueSuffix;

	/**
	 * 参数说明
	 */
	private String paramDesc;

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

	public String getValueTypeCode() {
		return valueTypeCode;
	}

	public void setValueTypeCode(String valueTypeCode) {
		this.valueTypeCode = valueTypeCode;
	}

	public String getValueSuffix() {
		return valueSuffix;
	}

	public void setValueSuffix(String valueSuffix) {
		this.valueSuffix = valueSuffix;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppConfig [paramCode=");
		builder.append(paramCode);
		builder.append(", paramName=");
		builder.append(paramName);
		builder.append(", paramValue=");
		builder.append(paramValue);
		builder.append(", valueTypeCode=");
		builder.append(valueTypeCode);
		builder.append(", valueSuffix=");
		builder.append(valueSuffix);
		builder.append(", paramDesc=");
		builder.append(paramDesc);
		builder.append("]");
		return builder.toString();
	}

}
