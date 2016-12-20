/**
 * AppConfigDapImpl.java
 * com.emarbox.dsp.campaign.dao
 *
 * ver  1.0
 * date 2012-6-19
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.campaign.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.emarbox.dsp.campaign.CampaignBaseDao;
import com.emarbox.dsp.domain.AppConfig;

/**
 * AppConfigDaoImpl 接口AppConfigDao的实现
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-19	下午3:40:05
 *
 */
@Repository("appConfigDao")
public class AppConfigDaoImpl extends CampaignBaseDao implements AppConfigDao{

	@Override
	public AppConfig getAppConfig(AppConfig appConfig) {
		if(null == appConfig){
			return null;
		}
		
		List<AppConfig> appConfigList = null;
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" select ");
		sql.append(" ac.param_value as paramValue ");
		sql.append(" from ");
		sql.append(" app_config ac");
		sql.append(" where ");
		sql.append(" ac.param_code=:paramCode ");
		
		// 定义返回值类型
		BeanPropertyRowMapper<AppConfig> mapper = new BeanPropertyRowMapper<AppConfig>(AppConfig.class);
		// 绑定参数
		SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(appConfig);
		appConfigList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper,sqlParam);
		if(null != appConfigList && appConfigList.size()>0){
			return appConfigList.get(0);
		}else{
			return null;
		}
	}

}

