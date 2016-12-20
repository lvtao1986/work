package com.emarbox.dspmonitor.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import app.base.spring.dao.BaseDao;

import com.emarbox.dspmonitor.billing.domain.BillingAccountInfo;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.costing.domain.CampaignCostDetail;

@Repository("billingCostDao")
public class BillingCostDaoImpl extends BaseDao implements BillingCostDao {

	private Logger log = LoggerFactory.getLogger(BillingCostDaoImpl.class);

	@Override
	public List<BillingCampaign> queryCampaignCostList() {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select c.id campaignId,IFNULL(c.total_budget,0)-IFNULL(r.total_cost,0) totalBudget,IFNULL(c.daily_budget,0) dailyBudget,IFNULL(c.last_ctr,0) lastCtr,c.fee_type feeType,c.budget_limit budgetLimit, ");
		sql.append(" (select IFNULL(a.pay_available_amount,0)+IFNULL(a.present_available_amount,0) totalcost from account_info a where a.user_id = c.parent_user_id) totalAccountCost,");
		sql.append(" c.fee_type feeType,c.parent_user_id userId, c.project_id projectId,ifnull(c.profit_control,0) profitControl ");
		sql.append(" from campaign c left join (select campaign_id,sum(cost) total_cost from campaign_cost where DATE_FORMAT(stat_date,'%Y-%m-%d') < DATE_FORMAT(NOW(), '%Y-%m-%d') group by campaign_id) r on r.campaign_id = c.id ");
		// 定义返回值类型
		BeanPropertyRowMapper<BillingCampaign> mapper = new BeanPropertyRowMapper<BillingCampaign>(
				BillingCampaign.class);
		return getReadSimpleJdbcTemplate().query(sql.toString(), mapper);
	}

	@Override
	public List<BillingAccountInfo> queryAccountCostList() {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.user_id userId,IFNULL(a.pay_available_amount,0)+IFNULL(a.present_available_amount,0) cost from account_info a ");
		// 定义返回值类型
		BeanPropertyRowMapper<BillingAccountInfo> mapper = new BeanPropertyRowMapper<BillingAccountInfo>(
				BillingAccountInfo.class);
		return getReadSimpleJdbcTemplate().query(sql.toString(), mapper);
	}


	@Override
	public Map<Long, Double> queryCampaignMiniProfitRateSet() {
		String sql = " select campaign_id,minprofit from campaign_profit_view ";
		return getReadSimpleJdbcTemplate().queryForObject(sql,new RowMapper<Map<Long,Double>>() {
			@Override
			public Map<Long,Double> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<Long, Double> result = new HashMap<Long, Double>();
				do{
					result.put(rs.getLong(1), rs.getDouble(2));
				}while(rs.next());
				return result;
			}
		});
	}

	@Override
	public Map<Long, Double> queryCampaignMaxProfitRateSet() {
		String sql = " select campaign_id,maxprofit from campaign_profit_view ";
		return getReadSimpleJdbcTemplate().queryForObject(sql,new RowMapper<Map<Long,Double>>() {
			@Override
			public Map<Long,Double> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<Long, Double> result = new HashMap<Long, Double>();
				do{
					result.put(rs.getLong(1), rs.getDouble(2));
				}while(rs.next());
				return result;
			}
		});
	}
	
}
