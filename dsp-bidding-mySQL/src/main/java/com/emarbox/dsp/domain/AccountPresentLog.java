package com.emarbox.dsp.domain;

import java.util.Date;

public class AccountPresentLog {
	
	private Long presentId;//赠送流水ID
	private Long userId;//	所有者用户ID	
	private Long projectId;//	项目ID	
	private Long depositId;//	充值流水ID
	private Long presentCatId;//	赠送类型ID	
	private Long presentTypeId;//	入款方式ID
	private String customerName;//	客户名称
	private Date presentTime;//	赠送时间	
	private Double presentAmount;//	赠送金额
	private String presentOrderNo;//	赠送交易流水号
	private Long companyId;//	收款公司ID
	private String operatorName;//	运营人员
	private String presentReason;//	赠送原因
	private String createUser;//	创建人
	private Date createTime;//	创建时间
	private String updateUser;//	修改人
	private Date updateTime;//	修改时间
	private Long depositStatusId;//入款状态
	private Long campaignId;//活动ID
	
	public Long getPresentId() {
		return presentId;
	}
	public void setPresentId(Long presentId) {
		this.presentId = presentId;
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
	public Long getDepositId() {
		return depositId;
	}
	public void setDepositId(Long depositId) {
		this.depositId = depositId;
	}
	public Long getPresentCatId() {
		return presentCatId;
	}
	public void setPresentCatId(Long presentCatId) {
		this.presentCatId = presentCatId;
	}
	public Long getPresentTypeId() {
		return presentTypeId;
	}
	public void setPresentTypeId(Long presentTypeId) {
		this.presentTypeId = presentTypeId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getPresentTime() {
		return presentTime;
	}
	public void setPresentTime(Date presentTime) {
		this.presentTime = presentTime;
	}
	public Double getPresentAmount() {
		return presentAmount;
	}
	public void setPresentAmount(Double presentAmount) {
		this.presentAmount = presentAmount;
	}
	public String getPresentOrderNo() {
		return presentOrderNo;
	}
	public void setPresentOrderNo(String presentOrderNo) {
		this.presentOrderNo = presentOrderNo;
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
	public String getPresentReason() {
		return presentReason;
	}
	public void setPresentReason(String presentReason) {
		this.presentReason = presentReason;
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
	public Long getDepositStatusId() {
		return depositStatusId;
	}
	public void setDepositStatusId(Long depositStatusId) {
		this.depositStatusId = depositStatusId;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	
	

}
