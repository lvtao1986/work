package com.emarbox.dsp.domain;

/**
 * 活动偏好设置
 * 
 * @author zhaidw
 * 
 */
public class CampaignPreference {

	// 属性Id
	private Long id;

	// 属性偏好编码
	private String code;

	// 属性偏好 父级编码
	private String parentCode;

	// 属性偏好文本
	private String text;

	// 属性偏好父Id
	private Long parentId;

	// 属性偏好是包含还是排除，默认包含
	private Boolean include = true;
	
	/**
	 * 默认是否选择
	 */
	private Integer selected;
	

	public CampaignPreference() {
		super();
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

	public Boolean getInclude() {
		if (null == include) {
			return true;
		}
		return include;
	}

	public void setInclude(Boolean include) {
		this.include = include;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "CampaignPreference [id=" + id + ", code=" + code
				+ ", parentCode=" + parentCode + ", text=" + text
				+ ", parentId=" + parentId + ", include=" + include
				+ ", selected=" + selected + "]";
	}

}
