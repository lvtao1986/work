package com.emarbox.dsp.domain;

import java.util.Date;


/**
 * 账户每天信息
 * 
 * @author zhaojinglei
 * 
 */
public class AccountDayStartInfo  {

	private Long id;//ID
	private Long userId;//所有者用户ID
	private Long projectId;//项目ID
	private Date statDate;//日期
	private Double totalPayAmount;// 账户支付总金额
	private Double totalPresentAmount;// 账户赠送总金额
	private Double payAvailableAmount;// 支付可用金额
	private Double presentAvailableAmount;// 赠送可用金额
	private Double payFreezeAmount;// 支付冻结金额
	private Double presentFreezeAmount;// 赠送冻结金额
	private Double payConsumeAmount;// 支付消费金额
	private Double presentConsumeAmount;// 赠送冻结金额	
	private Date createTime;//创建时间
	private String createUser;//创建人
	private Long campaignId;//活动ID
	private Double totalAmounnt;//账户当天可用花费
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public Double getTotalPayAmount() {
		return totalPayAmount;
	}

	public void setTotalPayAmount(Double totalPayAmount) {
		this.totalPayAmount = totalPayAmount;
	}

	public Double getTotalPresentAmount() {
		return totalPresentAmount;
	}

	public void setTotalPresentAmount(Double totalPresentAmount) {
		this.totalPresentAmount = totalPresentAmount;
	}

	public Double getPayAvailableAmount() {
		return payAvailableAmount;
	}

	public void setPayAvailableAmount(Double payAvailableAmount) {
		this.payAvailableAmount = payAvailableAmount;
	}

	public Double getPresentAvailableAmount() {
		return presentAvailableAmount;
	}

	public void setPresentAvailableAmount(Double presentAvailableAmount) {
		this.presentAvailableAmount = presentAvailableAmount;
	}

	public Double getPayFreezeAmount() {
		return payFreezeAmount;
	}

	public void setPayFreezeAmount(Double payFreezeAmount) {
		this.payFreezeAmount = payFreezeAmount;
	}

	public Double getPresentFreezeAmount() {
		return presentFreezeAmount;
	}

	public void setPresentFreezeAmount(Double presentFreezeAmount) {
		this.presentFreezeAmount = presentFreezeAmount;
	}

	public Double getPayConsumeAmount() {
		return payConsumeAmount;
	}

	public void setPayConsumeAmount(Double payConsumeAmount) {
		this.payConsumeAmount = payConsumeAmount;
	}

	public Double getPresentConsumeAmount() {
		return presentConsumeAmount;
	}

	public void setPresentConsumeAmount(Double presentConsumeAmount) {
		this.presentConsumeAmount = presentConsumeAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Double getTotalAmounnt() {
		return totalAmounnt;
	}

	public void setTotalAmounnt(Double totalAmounnt) {
		this.totalAmounnt = totalAmounnt;
	}

	@Override
	public String toString() {
		return "AccountDayStartInfo [createTime=" + createTime
				+ ", createUser=" + createUser + ", id=" + id
				+ ", payAvailableAmount=" + payAvailableAmount
				+ ", payConsumeAmount=" + payConsumeAmount
				+ ", payFreezeAmount=" + payFreezeAmount
				+ ", presentAvailableAmount=" + presentAvailableAmount
				+ ", presentConsumeAmount=" + presentConsumeAmount
				+ ", presentFreezeAmount=" + presentFreezeAmount
				+ ", projectId=" + projectId + ", statDate=" + statDate
				+ ", totalPayAmount=" + totalPayAmount
				+ ", totalPresentAmount=" + totalPresentAmount + ", userId="
				+ userId + "]";
	}

	

}
