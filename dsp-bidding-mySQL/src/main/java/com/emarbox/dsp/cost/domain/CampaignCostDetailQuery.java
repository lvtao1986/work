package com.emarbox.dsp.cost.domain;

public class CampaignCostDetailQuery extends CampaignCostDetail {

	private static final long serialVersionUID = -107483682509066152L;

	/**
	 * 查询行数最大值
	 */
	private Integer maxQueryRow;

	public Integer getMaxQueryRow() {
		return maxQueryRow;
	}

	public void setMaxQueryRow(Integer maxQueryRow) {
		this.maxQueryRow = maxQueryRow;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CampaignCostDetailQuery [maxQueryRow=");
		builder.append(maxQueryRow);
		builder.append("]");
		return builder.toString();
	}

}
