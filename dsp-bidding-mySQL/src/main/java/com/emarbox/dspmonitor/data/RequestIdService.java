package com.emarbox.dspmonitor.data;

public interface RequestIdService {
	
	/**
	 * 点击requestID去重
	 * @return
	 * 有重复记录返回false,
	 * 没有则返回true,
	 */
	boolean duplicateClick(String requestId);
	
}
