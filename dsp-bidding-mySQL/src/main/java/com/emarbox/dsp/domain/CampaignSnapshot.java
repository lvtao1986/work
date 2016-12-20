package com.emarbox.dsp.domain;

import java.io.Serializable;
import java.util.Date;

public class CampaignSnapshot implements Serializable {
	
	
	private static final long serialVersionUID = 110101010101000101L;
	
	private Long id; //主键ID
	private Long campaignId; //广告活动Id
	private Long projectId; //项目ID
	private Long userId; //用户ID
	private Date createTime; //创建时间
	private String campaignStatus; //计划状态
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	public String getCampaignStatus() {
		return campaignStatus;
	}
	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}
	@Override
	public String toString() {
		return "CampaignHistory [campaignId=" + campaignId + ", campaignStatus=" + campaignStatus + ", createTime=" + createTime
				+ ", id=" + id  + ", projectId="+ projectId + ", userId=" + userId
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campaignId == null) ? 0 : campaignId.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampaignSnapshot other = (CampaignSnapshot) obj;
		if (campaignId == null) {
			if (other.campaignId != null)
				return false;
		} else if (!campaignId.equals(other.campaignId))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		return true;
	}
	
	
	

}
