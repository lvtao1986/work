package com.emarbox.dsp.monitor.dto;

import java.util.Date;

public class ImpressionLog {
	private String info;
	private String infoStr;//用于验证tagent
	private String sign;
	private String c;
	private String guid;
	private String apid;
	private String actionType;
	private String userId;
	private String ip;
	private String agent;
	private String referer;
	private String ct;
	private String rt;
	private String rt2;
	private String bt;
	private String ut;
	private String pid=""; //ppb 广告位id
	private String td = "0"; //是否为托底

	private String campaignId;
	private String creativeId;
	private String adUserId;
	private String projectId;
	private String biddingId;
	private String time;
	private String adChannelId;
	private String costType;
	private String biddingRtbPrice;
	private String valuePrice;
	private String ctr;
	private String division;
	private String biddingCost;
	private String commRate;
	private String taxRate;
	private String mediaSite;
	private String userDomains;
	private String userPrice;
	
	private String dcid; //动态创意id

	private int supplier;
	
	private Date logDate; //日志时间
	
	private String proxyIp; //代理IP，代理IP与真实IP同时使用，检查客户端是否作弊
	
	private String realIp; //真实IP，被代理包装过的IP，有可能是真实客户端IP、也有可能是被代理伪造的IP。代理IP与真实IP同时使用，检查客户端是否作弊

	private String gid; //商品id

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTd() {
		return td;
	}

	public void setTd(String td) {
		this.td = td;
	}

	public String getRt2() {
		return rt2;
	}

	public void setRt2(String rt2) {
		this.rt2 = rt2;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getDcid() {
		return dcid;
	}

	public void setDcid(String dcid) {
		this.dcid = dcid;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(String creativeId) {
		this.creativeId = creativeId;
	}

	public String getAdUserId() {
		return adUserId;
	}

	public void setAdUserId(String adUserId) {
		this.adUserId = adUserId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBiddingId() {
		return biddingId;
	}

	public void setBiddingId(String biddingId) {
		this.biddingId = biddingId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAdChannelId() {
		return adChannelId;
	}

	public void setAdChannelId(String adChannelId) {
		this.adChannelId = adChannelId;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getBiddingRtbPrice() {
		return biddingRtbPrice;
	}

	public void setBiddingRtbPrice(String biddingRtbPrice) {
		this.biddingRtbPrice = biddingRtbPrice;
	}

	public String getValuePrice() {
		return valuePrice;
	}

	public void setValuePrice(String valuePrice) {
		this.valuePrice = valuePrice;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getBiddingCost() {
		return biddingCost;
	}

	public void setBiddingCost(String biddingCost) {
		this.biddingCost = biddingCost;
	}

	public String getCommRate() {
		return commRate;
	}

	public void setCommRate(String commRate) {
		this.commRate = commRate;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getMediaSite() {
		return mediaSite;
	}

	public void setMediaSite(String mediaSite) {
		this.mediaSite = mediaSite;
	}

	public String getUserDomains() {
		return userDomains;
	}

	public void setUserDomains(String userDomains) {
		this.userDomains = userDomains;
	}

	public int getSupplier() {
		return supplier;
	}

	public void setSupplier(int supplier) {
		this.supplier = supplier;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
 
	
	public String getInfoStr() {
		return infoStr;
	}

	public void setInfoStr(String infoStr) {
		this.infoStr = infoStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getApid() {
		return apid;
	}

	public void setApid(String apid) {
		this.apid = apid;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getUserPrice() {
		return userPrice;
	}

	public void setUserPrice(String userPrice) {
		this.userPrice = userPrice;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public String getRealIp() {
		return realIp;
	}

	public void setRealIp(String realIp) {
		this.realIp = realIp;
	}

	
	public String getUt() {
		return ut;
	}
	
	public void setUt(String ut) {
		this.ut = ut;
	}
	
	public String getBt() {
		return bt;
	}
	
	public void setBt(String bt) {
		this.bt = bt;
	}
	
	@Override
	public String toString() {
		return "ImpressionLog [info=" + info + ", infoStr=" + infoStr
				+ ", sign=" + sign + ", c=" + c + ", guid=" + guid + ", apid="
				+ apid + ", actionType=" + actionType + ", userId=" + userId
				+ ", ip=" + ip + ", agent=" + agent + ", referer=" + referer
				+ ", ct=" + ct + ", rt=" + rt + ", rt2=" + rt2 + ", pid=" + pid
				+ ", td=" + td + ", campaignId=" + campaignId + ", creativeId="
				+ creativeId + ", bt="+ bt + ", ut="+ ut + ", adUserId=" + adUserId + ", projectId="
				+ projectId + ", biddingId=" + biddingId + ", time=" + time
				+ ", adChannelId=" + adChannelId + ", costType=" + costType
				+ ", biddingRtbPrice=" + biddingRtbPrice + ", valuePrice="
				+ valuePrice + ", ctr=" + ctr + ", division=" + division
				+ ", biddingCost=" + biddingCost + ", commRate=" + commRate
				+ ", taxRate=" + taxRate + ", mediaSite=" + mediaSite
				+ ", userDomains=" + userDomains + ", userPrice=" + userPrice
				+ ", dcid=" + dcid + ", supplier=" + supplier + ", logDate="
				+ logDate + ", proxyIp=" + proxyIp + ", realIp=" + realIp
				+ ", getLogDate()=" + getLogDate() + ", getPid()=" + getPid()
				+ ", getTd()=" + getTd() + ", getRt2()=" + getRt2()
				+ ", getRt()=" + getRt() + ", getCt()=" + getCt()
				+ ", getUt()=" + getUt() + ", getBt()=" + getBt()
				+ ", getDcid()=" + getDcid() + ", getCampaignId()="
				+ getCampaignId() + ", getCreativeId()=" + getCreativeId()
				+ ", getAdUserId()=" + getAdUserId() + ", getProjectId()="
				+ getProjectId() + ", getBiddingId()=" + getBiddingId()
				+ ", getTime()=" + getTime() + ", getAdChannelId()="
				+ getAdChannelId() + ", getCostType()=" + getCostType()
				+ ", getBiddingRtbPrice()=" + getBiddingRtbPrice()
				+ ", getValuePrice()=" + getValuePrice() + ", getCtr()="
				+ getCtr() + ", getDivision()=" + getDivision()
				+ ", getBiddingCost()=" + getBiddingCost() + ", getCommRate()="
				+ getCommRate() + ", getTaxRate()=" + getTaxRate()
				+ ", getMediaSite()=" + getMediaSite() + ", getUserDomains()="
				+ getUserDomains() + ", getSupplier()=" + getSupplier()
				+ ", getInfo()=" + getInfo() + ", getInfoStr()=" + getInfoStr()
				+ ", getSign()=" + getSign() + ", getC()=" + getC()
				+ ", getGuid()=" + getGuid() + ", getApid()=" + getApid()
				+ ", getActionType()=" + getActionType() + ", getUserId()="
				+ getUserId() + ", getIp()=" + getIp() + ", getAgent()="
				+ getAgent() + ", getReferer()=" + getReferer()
				+ ", getUserPrice()=" + getUserPrice() + ", getProxyIp()="
				+ getProxyIp() + ", getRealIp()=" + getRealIp()
				+ ", toString()=" + super.toString() + "]";
	}
}
