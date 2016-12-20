package com.emarbox.dsp.finance.dao;

import com.emarbox.dsp.DspBaseDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository("accountInfoDao")
public class AccountInfoDaoImpl extends DspBaseDao implements AccountInfoDao {

	@Override
	public Double getAccountRemainAmount(Long userId) {
		//查询账户余额
		String sqlStr = " select IFNULL(a.pay_available_amount, 0) + IFNULL(a.present_available_amount, 0) as remainAmount from account_info a where a.user_id = ?";

		Double amount = getReadSimpleJdbcTemplate().queryForObject(sqlStr, new RowMapper<Double>() {
			@Override
			public Double mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return rs.next()?rs.getDouble(1) : 0d;
			}
		});
		//查询未扣花费
		sqlStr = " select ifnull(sum(cost), 0) cost from campaign_cost_detail  where confirmed = 0 and cost!=0 and user_id = ?";
		Double unconfirmedAmount = getCostSimpleJdbcTemplate().queryForObject(sqlStr,new RowMapper<Double>() {
			@Override
			public Double mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return rs.next()?rs.getDouble(1) : 0d;
			}
		});
		//计算出最终余额
		amount = amount.doubleValue() - unconfirmedAmount.doubleValue();

		return amount;
	}

	@Override
	public Map<Long,Double> getAccountRemainAmount() {
		//查询账户余额
		String sqlStr = " select a.user_id userId,IFNULL(a.pay_available_amount, 0) + IFNULL(a.present_available_amount, 0) as remainAmount from account_info a  ";
		
		 Map<Long, Double> remainAmount = getReadSimpleJdbcTemplate().queryForObject(sqlStr,new RowMapper<Map<Long, Double>>() {
			@Override
			public Map<Long, Double> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<Long,Double> result = new HashMap<Long, Double>();
				do {
					Long userId = rs.getLong(1);
					Double cost = rs.getDouble(2);
					result.put(userId, cost);
				} while (rs.next());
				return result;
			}
		});
		//查询未扣花费
		sqlStr = " select user_id,ifnull(sum(cost), 0) cost from campaign_cost_detail  where confirmed = 0 and cost!=0 group by user_id "
				+ " union select 0,0 from dual ";
		Map<Long, Double> unconfirmedMap = getCostSimpleJdbcTemplate().queryForObject(sqlStr,new RowMapper<Map<Long, Double>>() {
			@Override
			public Map<Long, Double> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<Long,Double> result = new HashMap<Long, Double>();
				do {
					Long userId = rs.getLong(1);
					Double cost = rs.getDouble(2);
					result.put(userId, cost);
				} while (rs.next());
				return result;
			}
		});
		//计算出最终余额
		for(Long userId : unconfirmedMap.keySet()){
			Double unconfirmedAmount = unconfirmedMap.get(userId);
			Double amount = remainAmount.get(userId);
			if(amount != null && unconfirmedAmount != null){
				amount = amount.doubleValue() - unconfirmedAmount.doubleValue();
				remainAmount.put(userId, amount);
			}
		}
		
		return remainAmount;
		
		
	}

	@Override
	public Map<Long, Double> getAccountTotalAmounnt() {
		StringBuffer sql = new StringBuffer();
		sql.append("  select user_id,IFNULL(sum(amount),0) as totalAmounnt from ( ");
		sql.append("  select user_id,IFNULL(pay_available_amount, 0) + IFNULL(present_available_amount, 0) amount from account_day_start_info where stat_date = CURDATE()  ");
		sql.append(" union  ");
		sql.append(" select user_id,sum(IFNULL(deposit_amount,0)) amount from account_deposit_log where DATE_FORMAT(create_time, '%Y-%m-%d') = CURDATE() group by user_id ");
		sql.append("  union   ");
		sql.append(" select user_id,sum(IFNULL(present_amount,0)) amount from account_present_log where DATE_FORMAT(create_time, '%Y-%m-%d') = CURDATE() group by user_id ");
		sql.append(" union   ");
		sql.append("  select user_id,0-sum(IFNULL(refund_amount,0)) amount from account_refund_log where DATE_FORMAT(create_time, '%Y-%m-%d') = CURDATE() group by user_id ");
		sql.append(" union select 0,0 from dual "); // 防止无数据报错
		sql.append("  ) AS d group by user_id ");

		return getReadSimpleJdbcTemplate().queryForObject(
				sql.toString(), new RowMapper<Map<Long, Double>>() {
					@Override
					public Map<Long, Double> mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Map<Long, Double> result = new HashMap<Long, Double>();
						do {
							Long userId = rs.getLong(1);
							Double cost = rs.getDouble(2);
							result.put(userId, cost);
						} while (rs.next());
						return result;
					}

				});
	}

	@Override
	public Long getUserId(long campaignId) {
		String sql = " select min(parent_user_id) from campaign where id=? ";
		Long userId = getReadSimpleJdbcTemplate().queryForLong(sql, campaignId);
		return userId;
	}

	@Override
	public List<Long> getAllAccountId() {
		String sql = " select user_id from account_info ";
		return getReadSimpleJdbcTemplate().query(sql, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong(1);
			}
		});
	}

	@Override
	public List<Long> getAllProjectId() {
		String sql = " select distinct(project_id) from campaign ";
		return getReadSimpleJdbcTemplate().query(sql, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong(1);
			}
		});
	}

	@Override
	public Map<Long, Set<Long>> getAccountWithProject() {
		final StringBuilder sql = new StringBuilder();
		sql.append("select parent_user_id as user_id,project_id from project_info");
		Map<Long,Set<Long>> resultMap = null;
		List<Map<String, Object>> list = getReadSimpleJdbcTemplate().queryForList(sql.toString());
		if (list == null || list.isEmpty()) {
			resultMap = new LinkedHashMap<Long, Set<Long>>();
		} else {
			resultMap = new LinkedHashMap<Long,Set<Long>>(list.size());
			for (Map<String, Object> map : list) {
				Long uid = (Long)map.get("user_id");
				Long pid = (Long)map.get("project_id");
				Set<Long> set = resultMap.get(uid);
				if(set==null){
					set = new HashSet<Long>();
					resultMap.put(uid,set);
				}
				set.add(pid);
			}
		}
		return resultMap;
	}

}
