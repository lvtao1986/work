package com.emarbox.dsp.domain;


/**
 * 账户总信息
 * 
 * @author liumingfeng
 * 
 */
public class AccountInfo extends DspDomain {

	private static final long serialVersionUID = 4318698968210605517L;
	private Double totalPayAmount;// 账户支付总金额
	private Double totalPresentAmount;// 账户赠送总金额
	private Double payAvailableAmount;// 支付可用金额
	private Double presentAvailableAmount;// 赠送可用金额
	private Double payFreezeAmount;// 支付冻结金额
	private Double presentFreezeAmount;// 赠送冻结金额
	private Double payConsumeAmount;// 支付消费金额
	private Double presentConsumeAmount;// 赠送冻结金额
	private String memo;// 备注
	private String today;
	private Long campaignId;
	private Double remainAmount;//剩余余额
	
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Double getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountInfo [totalPayAmount=");
		builder.append(totalPayAmount);
		builder.append(", totalPresentAmount=");
		builder.append(totalPresentAmount);
		builder.append(", payAvailableAmount=");
		builder.append(payAvailableAmount);
		builder.append(", presentAvailableAmount=");
		builder.append(presentAvailableAmount);
		builder.append(", payFreezeAmount=");
		builder.append(payFreezeAmount);
		builder.append(", presentFreezeAmount=");
		builder.append(presentFreezeAmount);
		builder.append(", payConsumeAmount=");
		builder.append(payConsumeAmount);
		builder.append(", presentConsumeAmount=");
		builder.append(presentConsumeAmount);
		builder.append(", memo=");
		builder.append(memo);
		builder.append("]");
		return builder.toString();
	}

}
