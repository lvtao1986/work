package com.emarbox.dsp.domain;

import java.util.Date;

public class AccountDepositLog {
	private Long depositId;//	充值流水ID
	private Long userId;//		所有者用户
	private Long projectId;//	项目ID
	private Long depositCatId;//	充值类型ID
	private Long depositTypeId;//	入款方式ID
	private Double depositAmount;//	充值金额	
	private String depositOrderNo;//充值交易流水号
	private Date depositTime;//		充值时间
	private String customerName;//	客户名称
	private Long companyId;//		收款公司ID
	private String operatorName;//	运营人员	
	private String memo;//		备注	
	private String createUser;//	创建人
	private Date createTime;//		创建时间	
	private String updateUser;//	修改人
	private Date updateTime;//		修改时间	
	private Long depositStatusId;//入款状态
	private Long campaignId;//活动ID
	
	
	public Long getDepositId() {
		return depositId;
	}
	public void setDepositId(Long depositId) {
		this.depositId = depositId;
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
	public Long getDepositCatId() {
		return depositCatId;
	}
	public void setDepositCatId(Long depositCatId) {
		this.depositCatId = depositCatId;
	}
	public Long getDepositTypeId() {
		return depositTypeId;
	}
	public void setDepositTypeId(Long depositTypeId) {
		this.depositTypeId = depositTypeId;
	}
	public Double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public String getDepositOrderNo() {
		return depositOrderNo;
	}
	public void setDepositOrderNo(String depositOrderNo) {
		this.depositOrderNo = depositOrderNo;
	}
	public Date getDepositTime() {
		return depositTime;
	}
	public void setDepositTime(Date depositTime) {
		this.depositTime = depositTime;
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
