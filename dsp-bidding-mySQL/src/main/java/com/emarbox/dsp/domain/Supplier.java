/**
 * Supplier.java
 * com.emarbox.dsp.domain
 *
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * Supplier 供应商信息
 *
 * @author   耿志新
 *
 */
public class Supplier {
	
	/**
	 * 供应商Id
	 */
	private Long supplierId;

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + "]";
	}
}

