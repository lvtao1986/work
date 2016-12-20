package com.emarbox.dsp.monitor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.emarbox.dsp.monitor.dto.AppConfig;
import com.emarbox.dsp.monitor.dto.CampaignDomain;

@Repository("campaignDao")
public class CampaignDaoImpl implements CampaignDao {
	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	@Override
	public List<CampaignDomain> getMaxPriceList() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select id campaignId,budget_limit maxPrice from campaign ");

		BeanPropertyRowMapper<CampaignDomain> mapper = new BeanPropertyRowMapper<CampaignDomain>(CampaignDomain.class);
		return simpleJdbcTemplate.query(sql.toString(), mapper);
	}
	
	@Override
	public List<CampaignDomain> getRecentMaxPriceList() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select id campaignId,budget_limit maxPrice from campaign order by update_time desc limit 1000");

		BeanPropertyRowMapper<CampaignDomain> mapper = new BeanPropertyRowMapper<CampaignDomain>(CampaignDomain.class);
		return simpleJdbcTemplate.query(sql.toString(), mapper);
	}

	@Override
	public AppConfig getBiddingClickTime() {
		List<AppConfig> configList = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.param_code paramCode,a.param_name paramName,a.param_value paramValue from app_config a where a.param_code = 'bidding_click_time' ");

		BeanPropertyRowMapper<AppConfig> mapper = new BeanPropertyRowMapper<AppConfig>(AppConfig.class);

		configList = simpleJdbcTemplate.query(sql.toString(), mapper);
		if (configList != null && configList.size() > 0) {
			return configList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Map<String, String> getCreativeIdMap() {
		String sql = " select creative_set_id,creative_id from creative_set ";
		 Map<String,String> map = simpleJdbcTemplate.queryForObject(sql, new RowMapper<Map<String,String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> result = new HashMap<String, String>();
				do{
					result.put(rs.getString(1), rs.getString(2));
				}while(rs.next());
				return result;
			}
		});
		
		return map;
	}

	@Override
	public Map<String, String> getRecentCreativeIdMap() {
		String sql = " select creative_set_id,creative_id from creative_set order by creative_set_id desc limit 1000";
		 Map<String,String> map = simpleJdbcTemplate.queryForObject(sql, new RowMapper<Map<String,String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> result = new HashMap<String, String>();
				do{
					result.put(rs.getString(1), rs.getString(2));
				}while(rs.next());
				return result;
			}
		});
		
		return map;
	}
	
	@Override
	public Map<String, String> getRetargetMap() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t1.id,IF((IFNULL(t2.include_all_old_visitor,0) + IFNULL(t3.rule_id,0))=0,0,1) retarget ");
		sql.append(" FROM campaign t1 LEFT JOIN "); 
		sql.append(" campaign_retargeting t2 ON ");
		sql.append(" t1.id = t2.campaign_id ");
		sql.append(" LEFT JOIN campaign_rule t3 "); 
		sql.append(" ON t1.id = t3.campaign_id ");
		sql.append(" WHERE t1.completed = 1 ");
		
//		String sql = " select t1.id,decode(ifnull(t2.include_all_old_visitor,0) + ifnull(t3.rule_id,0) , 0,0,1) retarget from campaign t1,campaign_retargeting t2,campaign_rule t3 where t1.id = t2.campaign_id(+) and t1.id = t3.campaign_id(+) and t1.completed = 1 ";
		 Map<String,String> map = simpleJdbcTemplate.queryForObject(sql.toString(), new RowMapper<Map<String,String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> result = new HashMap<String, String>();
				do{
					result.put(rs.getString(1), rs.getString(2));
				}while(rs.next());
				return result;
			}
		});
		
		return map;
	}

	@Override
	public Map<Long, Double> queryCampaignProfitRateSet() {
		String sql = " select c.id,ac.param_value from campaign c,app_config ac where ac.param_code='platform_profit_rate' ";
		return getSimpleJdbcTemplate().queryForObject(sql,new RowMapper<Map<Long,Double>>() {
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
	public Map<Long,Long> getParentUserIdMap() {
		String sql = " select id,parent_user_id from campaign ";
		return getSimpleJdbcTemplate().queryForObject(sql,new RowMapper<Map<Long,Long>>() {
			@Override
			public Map<Long,Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<Long, Long> result = new HashMap<Long, Long>();
				do{
					result.put(rs.getLong(1), rs.getLong(2));
				}while(rs.next());
				return result;
			}
		});
	
		
	}
	
	@Override
	public Map<Long,Long> getRecentParentUserIdMap() {
		String sql = " select id,parent_user_id from campaign order by update_time desc limit 1000";
		return getSimpleJdbcTemplate().queryForObject(sql,new RowMapper<Map<Long,Long>>() {
			@Override
			public Map<Long,Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<Long, Long> result = new HashMap<Long, Long>();
				do{
					result.put(rs.getLong(1), rs.getLong(2));
				}while(rs.next());
				return result;
			}
		});
	}
}
