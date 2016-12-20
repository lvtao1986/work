package com.emarbox.dsp.monitor.dto;

/**
 * 单条点击或展现记录信息
 * 
 * @author mrzhu
 * 
 */
public class LogInfo {
	private String requestId;
	private String winnerPrice;
	private String actionType; // "impression" or "click"

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getWinnerPrice() {
		return winnerPrice;
	}

	public void setWinnerPrice(String winnerPrice) {
		this.winnerPrice = winnerPrice;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogInfo [requestId=");
		builder.append(requestId);
		builder.append(", winnerPrice=");
		builder.append(winnerPrice);
		builder.append(", actionType=");
		builder.append(actionType);
		builder.append("]");
		return builder.toString();
	}
	
}
