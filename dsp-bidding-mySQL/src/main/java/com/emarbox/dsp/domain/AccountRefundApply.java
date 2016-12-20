package com.emarbox.dsp.domain;

import java.util.Date;

/**
 * 退款申请
 * 
 * @author liumingfeng
 * 
 */
public class AccountRefundApply extends DspDomain {

	private static final long serialVersionUID = -2753614297035381387L;
	private Long refundApplyId;// 退款申请id
	private Double refundApplyAmount;// 退款金额
	private Date refundApplyTime;// 申请时间
	private String refundApplyReason;// 申请原因

	public Long getRefundApplyId() {
		return refundApplyId;
	}

	public void setRefundApplyId(Long refundApplyId) {
		this.refundApplyId = refundApplyId;
	}

	public Double getRefundApplyAmount() {
		return refundApplyAmount;
	}

	public void setRefundApplyAmount(Double refundApplyAmount) {
		this.refundApplyAmount = refundApplyAmount;
	}

	public Date getRefundApplyTime() {
		return refundApplyTime;
	}

	public void setRefundApplyTime(Date refundApplyTime) {
		this.refundApplyTime = refundApplyTime;
	}

	public String getRefundApplyReason() {
		return refundApplyReason;
	}

	public void setRefundApplyReason(String refundApplyReason) {
		this.refundApplyReason = refundApplyReason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountRefundApply [refundApplyId=");
		builder.append(refundApplyId);
		builder.append(", refundApplyAmount=");
		builder.append(refundApplyAmount);
		builder.append(", refundApplyTime=");
		builder.append(refundApplyTime);
		builder.append(", refundApplyReason=");
		builder.append(refundApplyReason);
		builder.append("]");
		return builder.toString();
	}

}
