package com.emarbox.dspmonitor.costing.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import app.base.spring.dao.BaseDao;

import com.emarbox.dsp.campaign.domain.CampaignBudgetLog;
import com.emarbox.dsp.domain.Campaign;

@Repository("campaignBudgetLogDao")
public class CampaignBudgetLogDaoImpl extends BaseDao implements CampaignBudgetLogDao {

	@Override
	public List<CampaignBudgetLog> querYesterdayBudgetLog(Long campaignId) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select c.campaign_id campaignId,c.total_budget totalBudget,c.daily_budget dailyBudget,");
		sql.append(" c.old_total_budget oldTotalBudget,c.old_daily_budget oldDailyBudget,");
		sql.append(" c.log_time logTime,DATE_FORMAT(c.log_time, '%Y-%m-%d %H:%i') logTimeStr,");
		sql.append(" c.update_user updateUser from campaign_budget_log c where ");
		sql.append(" c.campaign_id =");
		sql.append(campaignId);
		sql.append(" and DATE_FORMAT(c.log_time, '%Y-%m-%d')  = DATE_SUB(CURDATE(),INTERVAL 1 DAY) ");
		sql.append(" order by c.log_time ");

		BeanPropertyRowMapper<CampaignBudgetLog> mapper = new BeanPropertyRowMapper<CampaignBudgetLog>(CampaignBudgetLog.class);
		return getReadSimpleJdbcTemplate().query(sql.toString(), mapper);
	}


	@Override
	public Campaign queryCampaign(Long campaignId) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select c.id campaignId,c.total_budget totalBudget,c.daily_budget dailyBudget from campaign c where ");
		sql.append(" c.id =");
		sql.append(campaignId);

		BeanPropertyRowMapper<Campaign> mapper = new BeanPropertyRowMapper<Campaign>(Campaign.class);
		List<Campaign> list = getReadSimpleJdbcTemplate().query(sql.toString(), mapper);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public Double queryAppConfig(String config) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select a.param_value from app_config a where a.param_code = '").append(config).append("' ");

		return getReadSimpleJdbcTemplate().queryForObject(sql.toString(), Double.class);
	}

}
