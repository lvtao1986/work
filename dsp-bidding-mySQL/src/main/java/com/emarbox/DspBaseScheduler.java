package com.emarbox;

import org.apache.commons.configuration.Configuration;

import app.base.util.ConfigUtil;
import app.common.util.LogUtil;
import app.common.util.Logs;

/**
 * 定时器基类
 * 
 * @author zhaidw
 * 
 */
public class DspBaseScheduler {

	protected Logs log;
	/**
	 * 配置项读取类
	 */
	protected static Configuration config;
	public DspBaseScheduler() {
		log = LogUtil.getLog(getClass());
	}
	static{
		config = ConfigUtil.addConfig("config");
	}
	public static Configuration getConfig(){
		return config;
	}
}
