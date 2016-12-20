package com.emarbox.dsp.cost.domain;

import java.util.Date;

import com.emarbox.dsp.domain.AccountInfo;

/**
 * 账户花费对象
 * @author zhaidw
 *
 */
public class AccountInfoCost extends AccountInfo {


	private static final long serialVersionUID = 5910515660490581945L;
	
	private Long campaignId;
	private Date statTime;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	

	public Date getStatTime() {
		return statTime;
	}

	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("AccountInfoCost [campaignId=");
		builder.append(campaignId);
		builder.append(", statTime=");
		builder.append(statTime);
		builder.append("]");
		return builder.toString();
	}


	

}
