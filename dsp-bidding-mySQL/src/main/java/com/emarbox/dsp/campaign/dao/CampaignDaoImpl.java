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


import org.springframework.stereotype.Repository;

import com.emarbox.dsp.campaign.CampaignBaseDao;
import com.emarbox.dsp.domain.Campaign;


@Repository("campaignDao")
public class CampaignDaoImpl extends CampaignBaseDao implements CampaignDao{

	
	@Override
	public String getCampaignBudgetLimit(Long campaignId) {
		if(null == campaignId){
			return null;
		}
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" select ");
		sql.append(" t.fee_type as feeType");
		sql.append(" from ");
		sql.append(" campaign t");
		sql.append(" where ");
		sql.append(" t.id = "+campaignId);
		
		
		BeanPropertyRowMapper<Campaign> mapper = new BeanPropertyRowMapper<Campaign>(Campaign.class);
		
		List<Campaign> result = getReadSimpleJdbcTemplate().query(sql.toString(),mapper);
		if(result!=null &&result.size()>0 ){
			return result.get(0).getFeeType();
		}else {
			return null;
		}
		
		
	}

	@Override
	public List<Campaign> getCampaignValid(Long parentUserId) {
		List<Campaign> campaignList = null;
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" select c.id as campaignId, ");
		sql.append(" c.project_id as projectId, ");
		sql.append(" c.parent_user_id as parentUserId, ");
		sql.append(" c.campaign_name as campaignName, ");
		sql.append(" c.fee_type as feeType ");
		sql.append(" from campaign c ");
		sql.append(" where 1=1 ");
		sql.append(" and c.parent_user_id = " + parentUserId);
		sql.append(condition());

		// 定义返回值类型
		BeanPropertyRowMapper<Campaign> mapper = new BeanPropertyRowMapper<Campaign>(Campaign.class);
		campaignList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper);

		return campaignList;
		
	}
	
	protected String condition(){
		StringBuffer sql = new StringBuffer();
		sql.append(" and c.campaign_status = '").append(Campaign.CAMPAIGN_STATUS_VALID).append("' and c.completed = 1 and c.deleted = 0 and c.valid = 1 ");
		return sql.toString();
	}

	@Override
	public Long getParentUserId(Long campaignId) {
		if(null == campaignId){
			return null;
		}
		
		String sql = " select max(parent_user_id) from campaign  where id = " + campaignId;
		
		
		
		Long result =getReadSimpleJdbcTemplate().queryForLong(sql);
		
		return result;
		
	}
}

