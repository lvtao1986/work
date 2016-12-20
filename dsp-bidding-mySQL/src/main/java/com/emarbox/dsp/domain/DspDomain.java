package com.emarbox.dsp.domain;

import java.util.Date;

import app.base.domain.BaseDomain;


/**
 * DSP 业务系统所有业务类的基类
 * 
 * @author zhaidw
 * 
 */


public class DspDomain extends BaseDomain {
	
	/**
	 * 数据操作类型，供同步数据到 RTB 使用
	 * @author zhaidw
	 *
	 */
	public enum EnumOperationType {
		SYNC,NOTIFY,DELETE
	}
	 
	private EnumOperationType operationType;

	private static final long serialVersionUID = 3838698720587612350L;

	/**
	 * 审核状态: 未审核
	 */
	public static final String AUDIT_STATUS_U = "unaudited";
	public static final String AUDIT_STATUS_U_STR = "待审核";

	/**
	 * 审核状态: 审核通过
	 */
	public static final String AUDIT_STATUS_A = "approved";
	public static final String AUDIT_STATUS_A_STR = "已通过";

	/**
	 * 审核状态: 审核不通过
	 */
	public static final String AUDIT_STATUS_R = "rejected";
	public static final String AUDIT_STATUS_R_STR = "未通过";

	public static final String CREATIVE_UNAUDITED = AUDIT_STATUS_U;
	public static final String CREATIVE_APPROVED = AUDIT_STATUS_A;
	public static final String CREATIVE_REJECTED = AUDIT_STATUS_R;

	private Long createUserId;
	private Long updateUserId;

	private Long projectId;
	private Long userId;
	private Long parentUserId;

	private String createUser;
	private String updateUser;

	private Date createTime;
	private Date updateTime;

	private String auditUser;
	private String auditStatus;
	private Date auditTime;
	private String auditMemo;

	public DspDomain() {
		super();
	}

	
	public EnumOperationType getOperationType() {
		return operationType;
	}



	public void setOperationType(EnumOperationType operationType) {
		this.operationType = operationType;
	}



	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Long getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(Long parentUserId) {
		this.parentUserId = parentUserId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DspDomain [createUserId=");
		builder.append(createUserId);
		builder.append(", updateUserId=");
		builder.append(updateUserId);
		builder.append(", projectId=");
		builder.append(projectId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", parentUserId=");
		builder.append(parentUserId);
		builder.append(", createUser=");
		builder.append(createUser);
		builder.append(", updateUser=");
		builder.append(updateUser);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", auditUser=");
		builder.append(auditUser);
		builder.append(", auditStatus=");
		builder.append(auditStatus);
		builder.append(", auditTime=");
		builder.append(auditTime);
		builder.append(", auditMemo=");
		builder.append(auditMemo);
		builder.append("]");
		return builder.toString();
	}

}
