/**
 * CampaignBaseDao.java
 * com.emarbox.dsp.campaign
 *
 * ver  1.0
 * date 2012-5-24
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.campaign;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.emarbox.dsp.DspBaseDao;
import com.emarbox.dsp.domain.CampaignPreference;

/**
 * CampaignBaseDao 数据库操作的基类
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-5-24	下午5:40:36
 *
 */
public class CampaignBaseDao extends DspBaseDao{
	
	/**
	 * @param <T>
	 * 
	 * getVisitorSql: 获取人群属性信息
	 *
	 * @author   耿志新
	 * @version  1.0
	 * @Date	 2012-6-6 下午1:50:14
	 * @throws
	 */
	protected List<CampaignPreference> getVisitor(CampaignPreference campaignPreference){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" a.id as id, ");
		sql.append(" a.text as text, ");
		sql.append(" a.parent_id as parentId, ");
		sql.append(" a.selected as selected ");
		sql.append(" from ");
		sql.append(" audience a ");
		sql.append(" start with a.parent_id = (select id from audience where code=:code) ");
		sql.append(" connect by prior a.id=a.parent_id ");
		sql.append(" order siblings by a.display_order ");
		
		// 定义返回值类型
		BeanPropertyRowMapper<CampaignPreference> mapper = new BeanPropertyRowMapper<CampaignPreference>(CampaignPreference.class);
		// 绑定参数
		SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(campaignPreference);
		return getSimpleJdbcTemplate().query(sql.toString(),mapper,sqlParam);
	}
}

