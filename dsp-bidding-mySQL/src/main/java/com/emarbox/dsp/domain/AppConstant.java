package com.emarbox.dsp.domain;

public class AppConstant {

	private Long constantId;
	private Long parentId;
	private String code;
	private String text;
	private Integer displayOrder;
	private Integer displayLevel;
	
	public AppConstant() {
		super();
	}
	public AppConstant(String code) {
		super();
		this.code = code;
	}
	public Long getConstantId() {
		return constantId;
	}
	public void setConstantId(Long constantId) {
		this.constantId = constantId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Integer getDisplayLevel() {
		return displayLevel;
	}
	public void setDisplayLevel(Integer displayLevel) {
		this.displayLevel = displayLevel;
	}
	
	
}
