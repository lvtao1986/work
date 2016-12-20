package com.emarbox.dsp.finance.dao;

import java.util.List;

import com.emarbox.dsp.domain.AccountInfo;


/**
 * 账户余额检查接口
 * @author zhaidw
 *
 */
public interface AccountCheckDao {

	
	
	Double getMinAccountBalance();
	
	/**
	 * 活动 预算阀值(百分比)
	 * @return
	 */
	Double getMaxCampaignBudgetPercent();
	
	/**
	 * 查询当前所有账户信息
	 * @param 
	 * @return List<AccountInfo>
	 */
	List<AccountInfo> getAccountList();
	
}
