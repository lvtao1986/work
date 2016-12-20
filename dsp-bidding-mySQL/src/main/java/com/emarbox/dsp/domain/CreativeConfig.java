package com.emarbox.dsp.domain;

/**
 * 创意尺寸
 * @author ZhangYaDong
 *
 */
public class CreativeConfig extends DspDomain{
	
	private static final long serialVersionUID = 1L;

	private long configId;
	private Integer width;
	private Integer height;
	private Integer status;
	public long getConfigId() {
		return configId;
	}
	public void setConfigId(long configId) {
		this.configId = configId;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
