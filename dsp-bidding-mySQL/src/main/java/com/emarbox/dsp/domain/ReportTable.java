package com.emarbox.dsp.domain;

import java.io.Serializable;
import java.util.List;

public abstract class ReportTable implements Serializable {

	private static final long serialVersionUID = 1736070296721514480L;

	private List<List<KeyValuePair>> heads;


	public List<List<KeyValuePair>> getHeads() {
		return heads;
	}

	public void setHeads(List<List<KeyValuePair>> heads) {
		this.heads = heads;
	}

	@Override
	public String toString() {
		return "ReportTable [heads=" + heads + "]";
	}

}
