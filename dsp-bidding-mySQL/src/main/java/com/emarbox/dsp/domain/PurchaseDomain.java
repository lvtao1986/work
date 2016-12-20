package com.emarbox.dsp.domain;

import java.util.Date;
 
/**   
 * @Title: PurchaseDomain.java 
 * @Description: 
 * @author LiuYang
 * @date 2013-1-9 下午4:04:01 
 * @version V1.0   
 */

public class PurchaseDomain {
	/*
	 * 主键id
	 */
	private Long domainId;
	/*
	 * 域名
	 */
	private String domainName;
	/*
	 * 域名中文名字
	 */
	private String domainText;
	/*
	 * 创建时间
	 */
	private Date createTime;
	public Long getDomainId() {
		return domainId;
	}
	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainText() {
		return domainText;
	}
	public void setDomainText(String domainText) {
		this.domainText = domainText;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PurchaseDomain [domainId=");
		builder.append(domainId);
		builder.append(", domainName=");
		builder.append(domainName);
		builder.append(", domainText=");
		builder.append(domainText);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append("]");
		return builder.toString();
	}

	
}

 
