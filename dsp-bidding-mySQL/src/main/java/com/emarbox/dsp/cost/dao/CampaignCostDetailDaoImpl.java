package com.emarbox.dsp.cost.dao;

import org.springframework.stereotype.Repository;

import com.emarbox.dsp.cost.CostBaseDao;


@Repository("campaignCostDetailDao")
public class CampaignCostDetailDaoImpl extends CostBaseDao implements CampaignCostDetailDao {
	
	@Override
	public void cleanOldData() {
		String sql = " DELETE FROM campaign_cost_detail WHERE confirmed=1 and stat_time <= DATE_SUB(now(),INTERVAL 2 hour) ";
		getCostSimpleJdbcTemplate().update(sql);
	}

}
