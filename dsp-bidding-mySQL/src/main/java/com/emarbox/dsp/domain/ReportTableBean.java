package com.emarbox.dsp.domain;


/**
 * 报表数据表格对象
 * 
 * @author zhaidw
 * 
 */
public class ReportTableBean {

	private String code;

	private Long recordSize;

	private ReportTableSimple reportTableSimple;


	public ReportTableBean() {
		super();
	}

	public Long getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(Long recordSize) {
		this.recordSize = recordSize;
	}

	public ReportTableSimple getReportTableSimple() {
		return reportTableSimple;
	}

	public void setReportTableSimple(ReportTableSimple reportTableSimple) {
		this.reportTableSimple = reportTableSimple;
	}

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportTableBean [code=");
		builder.append(code);
		builder.append(", recordSize=");
		builder.append(recordSize);
		builder.append(", reportTableSimple=");
		builder.append(reportTableSimple);
		builder.append("]");
		return builder.toString();
	}

}
