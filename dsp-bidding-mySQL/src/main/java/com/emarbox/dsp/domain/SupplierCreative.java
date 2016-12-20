package com.emarbox.dsp.domain;

/**
 * 供应商创意审核
 * 
 * @author zhaidw
 * 
 */
public class SupplierCreative extends Creative {

	private static final long serialVersionUID = -7863623420743257489L;

	/**
	 * 供应商的审核状态
	 */
	private String supplierAuditStatus;
	private String supplierRejectReason;

	public String getSupplierAuditStatus() {
		return supplierAuditStatus;
	}

	public void setSupplierAuditStatus(String supplierAuditStatus) {
		this.supplierAuditStatus = supplierAuditStatus;
	}

	public String getSupplierRejectReason() {
		return supplierRejectReason;
	}

	public void setSupplierRejectReason(String supplierRejectReason) {
		this.supplierRejectReason = supplierRejectReason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SupplierCreative [supplierAuditStatus=");
		builder.append(supplierAuditStatus);
		builder.append(", supplierRejectReason=");
		builder.append(supplierRejectReason);
		builder.append("]");
		return builder.toString();
	}

}
