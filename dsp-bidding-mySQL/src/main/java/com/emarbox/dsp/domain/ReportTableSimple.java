package com.emarbox.dsp.domain;

import java.util.List;

public class ReportTableSimple extends ReportTable {

	private static final long serialVersionUID = -3867215511105763866L;
	private List<List<KeyValuePair>> body;
	private List<List<KeyValuePair>> bodyAmount;

	public ReportTableSimple() {
		super();
	}

	public List<List<KeyValuePair>> getBody() {
		return body;
	}

	public void setBody(List<List<KeyValuePair>> body) {
		this.body = body;
	}

	public List<List<KeyValuePair>> getBodyAmount() {
		return bodyAmount;
	}


	public void setBodyAmount(List<List<KeyValuePair>> bodyAmount) {
		this.bodyAmount = bodyAmount;
	}


	@Override
	public String toString() {
		return "ReportTableSimple [heads=" + super.getHeads() + ", body=" + body + "]";
	}

}
