package com.emar.monitor.param;

import java.util.Map;

public class TaskParam {
	
	private String userAgent;
	private String userId;
	private String ip;
	private long projectId;
	private Map request;
	private String clickId;
	
	public TaskParam(String userAgent, String userId, String ip, long projectId, Map request, String clickId){
		this.userAgent = userAgent;
		this.userId = userId;
		this.ip = ip;
		this.projectId = projectId;
		this.request = request;
		this.clickId = clickId;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	
	public long getProjectId() {
		return projectId;
	}
	
	public Map getRequest() {
		return request;
	}

	public void setRequest(Map request) {
		this.request = request;
	}
	
	public String getClickId() {
		return clickId;
	}
	
	public void setClickId(String clickId) {
		this.clickId = clickId;
	}
	
}
