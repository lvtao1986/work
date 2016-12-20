package com.emarbox.dsp.finance.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AccountInfoDao {

	/**
	 * 查询指定账户当前余额信息
	 * @param userId
	 * AccountInfo
	 */
	Double getAccountRemainAmount(Long userId);

	/**
	 * 查询所有账户当前余额信息
	 * @param campaignId
	 * AccountInfo
	 */
	Map<Long,Double> getAccountRemainAmount();
	/**
	 * 查询所有账户当日"总可用花费" = 凌晨余额 + 当日充值 + 当日赠送 - 当日退款
	 * @param userId 账号id
	 */
	Map<Long,Double> getAccountTotalAmounnt();

	/**
	 * 根据活动id,获取userId
	 */
	Long getUserId(long campaignId);
	
	List<Long> getAllAccountId();
	
	/**
	 * 获取所有projectId
	 * @return
	 */
	List<Long> getAllProjectId();

	Map<Long,Set<Long>> getAccountWithProject();
}
