package com.emarbox.dsp.domain;

import java.util.Date;

public class CreativeSet extends DspDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = -770794048966798192L;
	
	private Long   creativeSetId;
	private Long creativeId;
	private String destinationUrl;
	private String fileUrl;
	private String auditUser;
	private String auditStatus;
	private Date auditTime;
	private String auditMemo;
	private String status;
	private Date statusUpdateTime;
	private Integer deleted;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	public Long getCreativeSetId() {
		return creativeSetId;
	}
	public void setCreativeSetId(Long creativeSetId) {
		this.creativeSetId = creativeSetId;
	}
	public Long getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}
	public String getDestinationUrl() {
		return destinationUrl;
	}
	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditMemo() {
		return auditMemo;
	}
	public void setAuditMemo(String auditMemo) {
		this.auditMemo = auditMemo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStatusUpdateTime() {
		return statusUpdateTime;
	}
	public void setStatusUpdateTime(Date statusUpdateTime) {
		this.statusUpdateTime = statusUpdateTime;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	

}
