package com.emarbox.dsp.domain;

import java.util.Date;




public class Target {
	/**
	 * 序列Id
	 * 主键
	 */
	private Long id;
	/**
	 * 目标Id
	 */
	private Long targetId;
	/**
	 * 目标名称
	 */
	private String targetName;
	
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
