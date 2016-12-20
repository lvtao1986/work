/**
 * Domain.java
 *
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * Domain 域名信息
 *
 * @author   耿志新
 *
 */
public class Domain {
	
	/**
	 * 域名Id
	 */
	private Long domainId;
	
	/**
	 * 域名
	 */
	private String domainValue;
	
	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getDomainValue() {
		return domainValue;
	}

	public void setDomainValue(String domainValue) {
		this.domainValue = domainValue;
	}

	@Override
	public String toString() {
		return "Domain [domainId=" + domainId + ", domainValue=" + domainValue
				+ "]";
	}
	
}

