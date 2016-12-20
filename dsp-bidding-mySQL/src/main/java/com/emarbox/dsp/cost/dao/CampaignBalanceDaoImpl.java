package com.emarbox.dsp.cost.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.emarbox.dsp.cost.CostBaseDao;
import com.emarbox.dsp.cost.domain.AccountInfoCost;
import com.emarbox.dsp.cost.domain.CampaignCostDetail;
import com.emarbox.dsp.domain.DspDomain;

@Repository("campaignBalanceDao")
public class CampaignBalanceDaoImpl extends CostBaseDao implements CampaignBalanceDao {

	@Override
	public Integer saveAccountConsumeLog(List<AccountInfoCost> accountInfoList) {

		if (null == accountInfoList) {
			return null;
		}
		if (accountInfoList.size() == 0) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into account_consume_log ( ");
		sql.append("      user_id, ");
		sql.append("      project_id, ");
		sql.append("      campaign_id, ");
		sql.append("      pay_consume_amount, ");
		sql.append("      present_consume_amount, ");
		sql.append("      consume_time, ");
		sql.append("      audit_user, ");
		sql.append("      audit_status, ");
		sql.append("      audit_time, ");
		sql.append("      audit_memo, ");
		sql.append("      real_pay_consume_amount, ");
		sql.append("      real_present_consume_amount, ");
		sql.append("      create_user, ");
		sql.append("      create_time, ");
		sql.append("      update_user, ");
		sql.append("      update_time ");
		sql.append("   ) values ( ");
		sql.append("      :userId, ");
		sql.append("      :projectId, ");
		sql.append("      :campaignId, ");
		sql.append("      :payConsumeAmount, ");
		sql.append("      :presentConsumeAmount, ");
		sql.append("      DATE_FORMAT(:statTime, '%Y-%m-%d'), ");
		sql.append("      'system', ");
		sql.append("      '").append(DspDomain.AUDIT_STATUS_A).append("', ");
		sql.append("      NOW(), ");
		sql.append("      '系统自动审核', ");
		sql.append("      :payConsumeAmount, ");
		sql.append("      :presentConsumeAmount, ");
		sql.append("      :updateUser, ");
		sql.append("      :updateTime, ");
		sql.append("      :updateUser, ");
		sql.append("      :updateTime ");
		sql.append("      ) ");
		
		sql.append(" on duplicate key update ");
		sql.append("  pay_consume_amount = IFNULL(pay_consume_amount, 0) + ");
		sql.append("                                            IFNULL(:payConsumeAmount, 0), ");
		sql.append("          present_consume_amount      = IFNULL(present_consume_amount, 0) + ");
		sql.append("                                            IFNULL(:presentConsumeAmount, 0), ");
		sql.append("          real_pay_consume_amount     = IFNULL(pay_consume_amount, 0), ");
		sql.append("          real_present_consume_amount = IFNULL(real_present_consume_amount, 0), ");
		sql.append("          update_user                 = :updateUser, ");
		sql.append("          update_time                 = :updateTime ");
		
		SqlParameterSource[] paramArr = SqlParameterSourceUtils.createBatch(accountInfoList.toArray());
		int[] affectRowsArr = getSimpleJdbcTemplate().batchUpdate(sql.toString(), paramArr);

		log.debug("saveAccountConsumeLog affectRowsArr length: " + affectRowsArr.length);

		return affectRowsArr.length;
	}

	protected Integer saveAccountInfoCostLog(List<AccountInfoCost> updateAccountInfoCostList) {
		Integer result = -1;

		if (null == updateAccountInfoCostList) {
			return null;
		}
		if (updateAccountInfoCostList.size() == 0) {
			return null;
		}

		List<Long> detailIdList = new ArrayList<Long>();
		for (AccountInfoCost accountInfoItem : updateAccountInfoCostList) {
			if (null != accountInfoItem) {
				detailIdList.add(accountInfoItem.getUserId());
				// log.debug("id : " + campaignCostDetail.getId());
			}
		}

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("uids", detailIdList);

		StringBuffer sql = new StringBuffer();

		sql.append(" select user_id, ");
		sql.append("   project_id, ");
		sql.append("   total_pay_amount, ");
		sql.append("   total_present_amount, ");
		sql.append("   pay_available_amount, ");
		sql.append("   present_available_amount, ");
		sql.append("   pay_freeze_amount, ");
		sql.append("   present_freeze_amount, ");
		sql.append("   pay_consume_amount, ");
		sql.append("   present_consume_amount, ");
		sql.append("   '").append(getCostSchedulerUserName()).append("' as create_user, ");
		sql.append("   NOW()  ");
		sql.append("     from account_info ");
		sql.append("     where user_id in ( :uids ) ");

		// 定义返回值类型
		BeanPropertyRowMapper<AccountInfoCost> mapper = new BeanPropertyRowMapper<AccountInfoCost>(AccountInfoCost.class);

		// 先查询可用余额
		List<AccountInfoCost> accountInfoList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper, parameters);

		if (null != accountInfoList && accountInfoList.size() > 0) {

			sql = new StringBuffer();
			sql.append(" insert into account_info_log ( ");
			sql.append("    user_id, ");
			sql.append("    project_id, ");
			sql.append("    total_pay_amount, ");
			sql.append("    total_present_amount, ");
			sql.append("    pay_available_amount, ");
			sql.append("    present_available_amount, ");
			sql.append("    pay_freeze_amount, ");
			sql.append("    present_freeze_amount, ");
			sql.append("    pay_consume_amount, ");
			sql.append("    present_consume_amount, ");
			sql.append("    create_user, ");
			sql.append("    create_time ");
			sql.append("    ) values( ");
			sql.append("   :userId, ");
			sql.append("   :projectId, ");
			sql.append("   :totalPayAmount, ");
			sql.append("   :totalPresentAmount, ");
			sql.append("   :payAvailableAmount, ");
			sql.append("   :presentAvailableAmount, ");
			sql.append("   :payFreezeAmount, ");
			sql.append("   :presentFreezeAmount, ");
			sql.append("   :payConsumeAmount, ");
			sql.append("   :presentConsumeAmount, ");
			sql.append("   '").append(getCostSchedulerUserName()).append("' , ");
			sql.append("   NOW() ");
			sql.append("   ) ");

			SqlParameterSource[] paramArr = SqlParameterSourceUtils.createBatch(accountInfoList.toArray());

			int[] affectRowArr = getSimpleJdbcTemplate().batchUpdate(sql.toString(), paramArr);

			result = affectRowArr.length;
			log.info("account_info_log saved affectRowArr length :" + result);

		} else {

			result = 0;
		}

		return result;
	}

	/**
	 * 查询需要处理的账户列表
	 * 
	 * @param campaignTotalCostList
	 * @return
	 */
	protected List<AccountInfoCost> listAccountInfoCost(List<CampaignCostDetail> campaignTotalCostList) {

		if (null == campaignTotalCostList) {
			return null;
		}
		if (campaignTotalCostList.size() == 0) {
			return null;
		}

		// 绑定参数
		MapSqlParameterSource parameters = getUserIdsParameter(campaignTotalCostList);

		StringBuffer sql = new StringBuffer();

		sql.append("  select ");
		sql.append("    user_id ");
		sql.append("   ,project_id ");
		sql.append("   ,pay_available_amount ");
		sql.append("   ,present_available_amount ");
		sql.append("   ,pay_consume_amount ");
		sql.append("   ,present_consume_amount ");
		sql.append("  from account_info ");
		sql.append("  where user_id in ( :uids ) for update ");

		// 定义返回值类型
		BeanPropertyRowMapper<AccountInfoCost> mapper = new BeanPropertyRowMapper<AccountInfoCost>(AccountInfoCost.class);

		// 先查询可用余额
		List<AccountInfoCost> accountInfoList = null;
		accountInfoList = getReadSimpleJdbcTemplate().query(sql.toString(), mapper, parameters);

		return accountInfoList;
	}

	@Override
	public List<AccountInfoCost> updateAccountInfo(List<CampaignCostDetail> campaignTotalCostList) {

		if (null == campaignTotalCostList) {
			return null;
		}
		if (campaignTotalCostList.size() == 0) {
			return null;
		}

		List<AccountInfoCost> resultAccountInfoCostList = null;

		List<AccountInfoCost> accountInfoList = listAccountInfoCost(campaignTotalCostList);

		// 对于每个账户的 可用余额中的 支付部分 和 赠送部分 与 消费进行比较，优先从 支付部分 进行扣款

		if (null == accountInfoList) {
			log.info("没有查询需要结算的到账户信息");
			return null;
		}

		if (0 == accountInfoList.size()) {
			log.info("没有查询需要结算的到账户信息");
			return null;
		}

		saveAccountInfoCostLog(accountInfoList);

		Long campaignUserId = null;
		Double campaignCost = null;

		Long accountUserId = null;
		Double accountPayAvailableAmount = null;
		Double accountPresentAvailableAmount = null;
		Double accountPayConsumeAmount = null;
		Double accountPresentConsumeAmount = null;

		List<AccountInfoCost> updateAccountInfoCostList = new ArrayList<AccountInfoCost>();
		AccountInfoCost updateAccountInfoCost = null;
		for (AccountInfoCost accountInfo : accountInfoList) {
			if (null != accountInfo) {

				for (CampaignCostDetail costItem : campaignTotalCostList) {
					if (null != costItem) {
						campaignUserId = costItem.getUserId();

						accountUserId = accountInfo.getUserId();

						// 广告主匹配上之后，对比金额
						if (accountUserId.equals(campaignUserId)) {

							campaignCost = costItem.getCost();

							accountPayAvailableAmount = accountInfo.getPayAvailableAmount();
							accountPresentAvailableAmount = accountInfo.getPresentAvailableAmount();
							accountPayConsumeAmount = accountInfo.getPayConsumeAmount();
							accountPresentConsumeAmount = accountInfo.getPresentConsumeAmount();

							accountPayAvailableAmount = (null == accountPayAvailableAmount) ? 0.00d : accountPayAvailableAmount;
							accountPresentAvailableAmount = (null == accountPresentAvailableAmount) ? 0.00d : accountPresentAvailableAmount;
							accountPayConsumeAmount = (null == accountPayConsumeAmount) ? 0.00d : accountPayConsumeAmount;
							accountPresentConsumeAmount = (null == accountPresentConsumeAmount) ? 0.00d : accountPresentConsumeAmount;

							updateAccountInfoCost = new AccountInfoCost();
							try {
								BeanUtils.copyProperties(updateAccountInfoCost, accountInfo);
							} catch (Exception e) {
								log.error("BeanUtils.copyProperties failed: " + e);
							}

							log.info("本次活动[ID=" + costItem.getCampaignId() + "][" + dateFormat.format(costItem.getStatTime()) + "] 花费: "
									+ campaignCost);

							// 设置要扣除的花费值
							if (accountPayAvailableAmount >= campaignCost && accountPayAvailableAmount > 0) {

								accountInfo.setPayAvailableAmount(accountPayAvailableAmount - campaignCost);
								accountInfo.setPayConsumeAmount(accountPayConsumeAmount + campaignCost);

								updateAccountInfoCost.setPayConsumeAmount(campaignCost);
								updateAccountInfoCost.setPresentConsumeAmount(0.00d);

								log.info("A本次活动[ID=" + costItem.getCampaignId() + "]扣除 账户支付: " + campaignCost + " , 账户赠送: 0");

							}else if (accountPayAvailableAmount >= campaignCost && accountPayAvailableAmount <= 0) {

								accountInfo.setPayAvailableAmount(0.00d);
								accountInfo.setPresentAvailableAmount(accountPresentAvailableAmount - campaignCost);
								
								accountInfo.setPayConsumeAmount(accountPayConsumeAmount);
								accountInfo.setPresentConsumeAmount(accountPresentConsumeAmount + campaignCost);
								
								updateAccountInfoCost.setPayConsumeAmount(0.00d);
								updateAccountInfoCost.setPresentConsumeAmount(campaignCost);

								log.info("A本次活动[ID=" + costItem.getCampaignId() + "]扣除 账户支付: " + campaignCost + " , 账户赠送: 0");

							} else if (accountPresentAvailableAmount >= campaignCost) {

								// 此时，花费肯定大于支付余额
								Double paValue = campaignCost - accountPayAvailableAmount;

								accountInfo.setPayAvailableAmount(0.00d);
								accountInfo.setPresentAvailableAmount(accountPresentAvailableAmount - paValue);

								accountInfo.setPayConsumeAmount(accountPayConsumeAmount + accountPayAvailableAmount);
								accountInfo.setPresentConsumeAmount(accountPresentConsumeAmount + paValue);

								updateAccountInfoCost.setPayConsumeAmount(accountPayAvailableAmount);
								updateAccountInfoCost.setPresentConsumeAmount(paValue);

								log.info("B本次活动[ID=" + costItem.getCampaignId() + "] 扣除 账户支付: " + accountPayAvailableAmount + " , 账户赠送: "
										+ paValue);

							} else if (accountPayAvailableAmount + accountPresentAvailableAmount >= campaignCost) {

								Double reduceValue = campaignCost - accountPayAvailableAmount;

								// 优先从支付部分扣除，再从赠送部分扣除
								accountInfo.setPayAvailableAmount(0.00d);
								accountInfo.setPresentAvailableAmount(accountPresentAvailableAmount - reduceValue);

								accountInfo.setPayConsumeAmount(accountPayConsumeAmount + accountPayAvailableAmount);
								accountInfo.setPresentConsumeAmount(accountPresentConsumeAmount + reduceValue);

								updateAccountInfoCost.setPayConsumeAmount(accountPayAvailableAmount);
								updateAccountInfoCost.setPresentConsumeAmount(reduceValue);

								log.info("C本次活动[ID=" + costItem.getCampaignId() + "] 扣除 账户支付: " + accountPayAvailableAmount + " , 账户赠送: "
										+ reduceValue);
							} else {

								Double reduceValue = campaignCost - accountPayAvailableAmount;

								accountInfo.setPayAvailableAmount(0.00d);
								accountInfo.setPresentAvailableAmount(accountPresentAvailableAmount - reduceValue);

								accountInfo.setPayConsumeAmount(accountPayConsumeAmount + accountPayAvailableAmount);
								accountInfo.setPresentConsumeAmount(accountPresentConsumeAmount + reduceValue);

								// 支付部分 和 赠送部分 都不够消费扣除，那么把 赠送部分扣成负数
								updateAccountInfoCost.setPayConsumeAmount(accountPayAvailableAmount);
								updateAccountInfoCost.setPresentConsumeAmount(reduceValue);

								log.info("D本次活动[ID=" + costItem.getCampaignId() + "] 扣除 账户支付: " + reduceValue + " , 账户赠送: "
										+ accountPresentAvailableAmount);
							}

							// 记录项目活动花费
							updateAccountInfoCost.setProjectId(costItem.getProjectId());
							updateAccountInfoCost.setCampaignId(costItem.getCampaignId());
							updateAccountInfoCost.setStatTime(costItem.getStatTime());

							StringBuffer costSummaryMsg = new StringBuffer("E本次结算");
							costSummaryMsg.append("账户[ID=").append(costItem.getUserId()).append("]的活动[ID=")
									.append(costItem.getCampaignId()).append("] :");
							costSummaryMsg.append("支付部分剩余: ").append(accountInfo.getPayAvailableAmount()).append(" , ");
							costSummaryMsg.append("赠送部分剩余: ").append(accountInfo.getPresentAvailableAmount()).append(" .");
							costSummaryMsg.append("支付部分扣除: ").append(updateAccountInfoCost.getPayConsumeAmount()).append(" , ");
							costSummaryMsg.append("赠送部分扣除: ").append(updateAccountInfoCost.getPresentConsumeAmount()).append(" .");

							log.info(costSummaryMsg.toString());

							updateAccountInfoCostList.add(updateAccountInfoCost);
						}

					}
				}

			}
		}

		// updateAccountInfoCostList 为需要扣除的费用

		if (updateAccountInfoCostList.size() > 0) {

			StringBuffer sql = new StringBuffer();
			sql = new StringBuffer();
			sql.append(" update account_info set ");
			sql.append("     pay_available_amount = :payAvailableAmount ");
			sql.append("    ,present_available_amount = :presentAvailableAmount ");
			sql.append("    ,pay_consume_amount = :payConsumeAmount ");
			sql.append("    ,present_consume_amount = :presentConsumeAmount ");
			sql.append("    ,update_user = '").append(getCostSchedulerUserName()).append("'");
			sql.append("    ,update_time = NOW() ");
			sql.append(" where user_id =:userId ");

			SqlParameterSource[] paramArr = SqlParameterSourceUtils.createBatch(accountInfoList.toArray());

			int[] affectRowsArr = getSimpleJdbcTemplate().batchUpdate(sql.toString(), paramArr);

			log.debug("update account_info affectRowsArr length: " + affectRowsArr.length);

			resultAccountInfoCostList = updateAccountInfoCostList;
		} else {
			log.info("没有查询需要结算的到账户信息: updateAccountInfoCostList is null.");

			resultAccountInfoCostList = null;
		}

		return resultAccountInfoCostList;
	}

	protected void setStateDate(CampaignCostDetail ccd) {

		Date date = ccd.getStatTime();

		DateTime dt = new DateTime(date.getTime());

		ccd.setStatYear(dt.getYear());
		ccd.setStatMonth(Integer.valueOf(String.valueOf(dt.getYear()) + String.valueOf(dt.getMonthOfYear())));
		ccd.setStatHour(dt.getHourOfDay());

		ccd.setStatWeek(Integer.valueOf(String.valueOf(dt.getYear()) + String.valueOf(dt.getWeekOfWeekyear())));

		log.info("item: " + ccd);

	}

	@Override
	public Integer addTempReportDetail(List<CampaignCostDetail> campaignTotalCostList) {

		if (null == campaignTotalCostList) {
			return null;
		}
		if (campaignTotalCostList.size() == 0) {
			return null;
		}

		StringBuffer sql = new StringBuffer();

		sql = new StringBuffer();

		for (CampaignCostDetail campaignCostDetail : campaignTotalCostList) {
			if (null != campaignCostDetail) {
				setStateDate(campaignCostDetail);
			}
		}

		sql.append(" insert into temp_campaign_report_detail");
		sql.append("   ( ");
		sql.append("      campaign_id, ");
		sql.append(" supplier_id, ");
		sql.append("      display_count,click_count,cost, dsp_cost,rtb_cost,profit, ");
		sql.append("      stat_date, ");
		sql.append("      stat_year, ");
		sql.append("      stat_month, ");
		sql.append("      stat_week, ");
		sql.append("      stat_hour, ");
		sql.append("      update_user, ");
		sql.append("      update_time ");
		sql.append("    ) values ( ");
		sql.append("      :campaignId, ");
		sql.append("  :supplierId, ");
		sql.append("      :impression,:click,:cost, :dspCost,:rtbCost,:profit, ");
		sql.append("      :statTime, ");
		sql.append("      :statYear, ");
		sql.append("      :statMonth, ");
		sql.append("      :statWeek, ");
		sql.append("      :statHour, ");
		sql.append("      'costScheduler', ");
		sql.append("      NOW() ");
		sql.append("    ) ");

		SqlParameterSource[] paramArr = SqlParameterSourceUtils.createBatch(campaignTotalCostList.toArray());

		int[] affectRowsArr = getSimpleJdbcTemplate().batchUpdate(sql.toString(), paramArr);

		log.debug("updateCampaginReportDetail affectRowsArr length: " + affectRowsArr.length);

		return affectRowsArr.length;
	}

}
