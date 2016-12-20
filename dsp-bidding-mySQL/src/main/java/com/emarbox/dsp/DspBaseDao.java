package com.emarbox.dsp;

import org.apache.commons.configuration.Configuration;

import app.base.spring.dao.BaseDao;

/**
 * 所有 dao 类的基类
 * @author zhaidw
 *
 */
public abstract class DspBaseDao extends BaseDao {

	
	public Configuration getConfig(){
		
		return DspBaseService.getConfig();
	}
}
