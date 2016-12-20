package com.emarbox.dspmonitor.status.data;

import app.base.util.ConfigUtil;

public class NodeStatus {
	
	static{
		ConfigUtil.addConfig("zookeeper");
	}
	
	/**
	 * 是否为主节点
	 */
	public volatile static Boolean isMajorNode =false;
	/**
	 * 总账号余数 (账号所分组数)
	 */
	public static final int accountModCount = Integer.parseInt(ConfigUtil.getString("zoo.account.mod.count"));
	/**
	 * 进程可操作的账号余数
	 */
	public static final int accountMod = Integer.parseInt(ConfigUtil.getString("zoo.account.mod"));
	
}
