package com.emarbox.dsp.domain;

import java.util.Date;

public class AccountRefundLog {
	private Long refundId;//退款流水ID
	private Long refundApplyId;//退款申请ID
	private Long userId;//	所有者用户ID
	private Long projectId;//项目ID
	private Long refundCatId;//	退款类型ID
	private Long refundTypeId;//退款方式ID
	private Double refundAmount;//退款金额
	private String refundOrderNo;//退款交易流水号
	private Date refundTime;//	退款时间
	private String customerName;//	客户名称
	private Long companyId;//	收款公司ID
	private String operatorName;//	运营人员
	private String memo;//	备注
	private String auditUser;//	审核人
	private String auditStatus;//	审核状态
	private Date auditTime;//	审核时间
	private String auditMemo;//	审核意见
	private String createUser;//	创建人
	private Date createTime;//	创建时间
	private String updateUser;//	修改人
	private Date updateTime;//	修改时间
	private Long campaignId;//活动ID
	
	public Long getRefundId() {
		return refundId;
	}
	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}
	public Long getRefundApplyId() {
		return refundApplyId;
	}
	public void setRefundApplyId(Long refundApplyId) {
		this.refundApplyId = refundApplyId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getRefundCatId() {
		return refundCatId;
	}
	public void setRefundCatId(Long refundCatId) {
		this.refundCatId = refundCatId;
	}
	public Long getRefundTypeId() {
		return refundTypeId;
	}
	public void setRefundTypeId(Long refundTypeId) {
		this.refundTypeId = refundTypeId;
	}
	public Double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundOrderNo() {
		return refundOrderNo;
	}
	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}
	public Date getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditMemo() {
		return auditMemo;
	}
	public void setAuditMemo(String auditMemo) {
		this.auditMemo = auditMemo;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

}
