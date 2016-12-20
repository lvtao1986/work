package com.emarbox.dsp.api.domain;

/**
 * 接口结果对象
 * @author zhaidw
 *
 */
public class DspApiResult {

	private Boolean success;
	private Integer statusCode;
	private String message;

	public DspApiResult() {
		super();
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DspApiResult [success=");
		builder.append(success);
		builder.append(", StatusCode=");
		builder.append(statusCode);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}

}
