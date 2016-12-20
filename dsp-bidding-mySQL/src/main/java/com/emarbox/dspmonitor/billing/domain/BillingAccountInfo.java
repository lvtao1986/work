package com.emarbox.dspmonitor.billing.domain;

import com.emarbox.dsp.domain.AccountInfo;

public class BillingAccountInfo extends AccountInfo {

	private static final long serialVersionUID = 2190579206383910403L;
	private Double cost;

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

}
