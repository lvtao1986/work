package com.emarbox.dspmonitor.billing.domain;

import com.emarbox.dsp.domain.Campaign;

public class BillingCampaign extends Campaign{
	
	private static final long serialVersionUID = 2190579200383929403L;
	
	private Double totalAccountCost;
	private Double delayCost;
	private Double remainAccount;
	
	public Double getTotalAccountCost() {
		return totalAccountCost;
	}

	public void setTotalAccountCost(Double totalAccountCost) {
		this.totalAccountCost = totalAccountCost;
	}
	
	public Double getDelayCost() {
		return delayCost;
	}

	public void setDelayCost(Double delayCost) {
		this.delayCost = delayCost;
	}

	public Double getRemainAccount() {
		return remainAccount;
	}

	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}

	@Override
	public String toString() {
		return "Campaign [campaignId=" + this.getCampaignId() + ", campaignName="
				+ this.getCampaignName() + ", cost=" + this.getCost() 
				+ ", delayCost=" + this.getDelayCost()  
				+ ", remainAccount=" + this.getRemainAccount() +"]";
	}

}
