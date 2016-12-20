package com.emarbox.dsp.finance.dao;


import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import org.springframework.stereotype.Repository;

import com.emarbox.dsp.cost.CostBaseDao;
import com.emarbox.dsp.cost.domain.AppConfig;
import com.emarbox.dsp.domain.AccountInfo;
import com.emarbox.dsp.domain.Campaign;
import com.emarbox.dsp.util.AppConfigUtil;
@Repository("accountCheckDao")
public class AccountCheckDaoImpl extends CostBaseDao implements AccountCheckDao {

	/**
	 * 返回阀值的CODE
	 * @return
	 */
	protected String getMinAccountBalanceStr(){
		return AppConfigUtil.CODE_MIN_ACCOUNT_BALANCE;
	}
	
	/**
	 * 返回阀值的CODE
	 * @return
	 */
	protected String getMaxCampaignBudgetPercentStr(){
		return AppConfigUtil.CODE_MAX_CAMPAIGN_BUDGET_PERCENT;
	}
	
	/**
	 * 返回系统配置的 账户阀值 (当广告主账户余额小于此值时，该广告主所有活动全部下线)
	 * 
	 * @return
	 */
	@Override
	public Double getMinAccountBalance() {

		Double minAccountBalance = 0.00d;
		
		StringBuffer sql = new StringBuffer();

		sql.append("  select ");
		sql.append("  param_value ");
		sql.append("  from app_config ");
		sql.append("  where param_code = '");
		sql.append(getMinAccountBalanceStr());
		sql.append("'");

		// 定义返回值类型
		BeanPropertyRowMapper<AppConfig> mapper = new BeanPropertyRowMapper<AppConfig>(AppConfig.class);

		SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(new AppConfig());
		List<AppConfig> configList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper, sqlParam);

		if (null != configList && configList.size() > 0) {
			String paramValue = configList.get(0).getParamValue();

			minAccountBalance = NumberUtils.toDouble(paramValue, 0.00d);
		}

		return minAccountBalance;

	}

	
	protected String condition(){
		StringBuffer sql = new StringBuffer();
		sql.append(" and c.campaign_status = '").append(Campaign.CAMPAIGN_STATUS_VALID).append("' and c.completed = 1 and c.deleted = 0 and c.valid = 1 ");
		return sql.toString();
	}
	
	
	@Override
	public Double getMaxCampaignBudgetPercent() {

		Double minCampaignBudgetPercent = 0.00d;
		
		StringBuffer sql = new StringBuffer();

		sql.append("  select ");
		sql.append("  param_value ");
		sql.append("  from app_config ");
		sql.append("  where param_code = '");
		sql.append(getMaxCampaignBudgetPercentStr());
		sql.append("'");

		// 定义返回值类型
		BeanPropertyRowMapper<AppConfig> mapper = new BeanPropertyRowMapper<AppConfig>(AppConfig.class);

		SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(new AppConfig());
		List<AppConfig> configList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper, sqlParam);

		if (null != configList && configList.size() > 0) {
			String paramValue = configList.get(0).getParamValue();

			minCampaignBudgetPercent = NumberUtils.toDouble(paramValue, 0.00d);
		}

		return minCampaignBudgetPercent;

	}
	
	@Override
	public  List<AccountInfo> getAccountList() {

		List<AccountInfo> accountInfoList = null;
		StringBuffer sql = new StringBuffer("");
		sql.append(" select ");
		sql.append(" a.user_id userId, ");
		sql.append(" IFNULL(a.pay_available_amount, 0) + IFNULL(a.present_available_amount, 0) as remainAmount ");
		sql.append(" from account_info a ");

		// 定义返回值类型
		BeanPropertyRowMapper<AccountInfo> mapper = new BeanPropertyRowMapper<AccountInfo>(AccountInfo.class);
		accountInfoList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper);
		if(accountInfoList!=null && accountInfoList.size()>0){
			return accountInfoList;
		}else {
			return null;
		}
		
	}
}
