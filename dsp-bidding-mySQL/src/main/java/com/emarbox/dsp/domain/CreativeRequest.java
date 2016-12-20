package com.emarbox.dsp.domain;

import java.util.Date;

public class CreativeRequest extends DspDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long creativeId;
	private Long creativeSetId;
	private Long supplierId;
	private Date requestTime;
	private Date responseTime;
	private String status;
	private String requestResult;
	
	private String supplierCode;
	
	public Long getCreativeSetId() {
		return creativeSetId;
	}
	public void setCreativeSetId(Long creativeSetId) {
		this.creativeSetId = creativeSetId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public Long getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}
	
	
	
}
