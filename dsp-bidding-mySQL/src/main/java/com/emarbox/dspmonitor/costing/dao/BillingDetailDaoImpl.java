package com.emarbox.dspmonitor.costing.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import app.base.spring.dao.BaseDao;

import com.emarbox.dspmonitor.data.CampaignCost;

@Repository
public class BillingDetailDaoImpl extends BaseDao implements BillingDetailDao {

	@Override
	public List<CampaignCost> getCampaignTodayCost(long accountId, String date) {
		StringBuffer sql = new StringBuffer();
	    sql.append(" select t1.campaign_id campaignId,t1.cost + ifnull(t2.cost,0) cost from ( ");
	    sql.append(" select campaign_id,sum(cost) cost from campaign_pre_billing_detail where user_id= "+accountId+" and date(stat_time)= '"+date+"' group by campaign_id "); 
	    sql.append(" ) t1 left join ( ");
	    sql.append(" select campaign_id,sum(ifnull(cost,0) - ifnull(pre_cost,0)) cost from campaign_billing_detail where user_id=  "+accountId+" and date(stat_time)= '"+date+"' group by campaign_id "); 
	    sql.append(" ) t2 on t1.campaign_id=t2.campaign_id ");
	   
	    
		List<CampaignCost> list = getCostJdbcTemplate().query(sql.toString(),
				new BeanPropertyRowMapper<CampaignCost>(CampaignCost.class));
		return list;
	}

}
