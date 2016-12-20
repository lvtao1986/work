package com.emarbox.dsp.cost.dao;

import java.util.List;

import com.emarbox.dsp.cost.domain.AccountInfoCost;
import com.emarbox.dsp.cost.domain.CampaignCostDetail;

/**
 * 广告活动 结算DAO
 * <pre>
 * 
 * 写消费表，更新账户，记录账户变更流水，更新报表
 * 如果 账户余额 低于 账户阀值，修改该账户所有有效活动为下线状态，并通知RTB该账户所有活动下线。
 * </pre>
 * @author zhaidw 
 *
 */
public interface CampaignBalanceDao {

	/**
	 * 保存消费流水，merge 到账户消费流水表
	 * @param accountInfoList 应扣的账户信息，里面分了支付部分和赠送部分kou'ch
	 * @return
	 */
	Integer saveAccountConsumeLog(List<AccountInfoCost> accountInfoList);
	
	/**
	 * 保存账户更新日志（在更新账户信息前）
	 * @param query
	 * @return
	 */
	//public Integer saveAccountInfoLog(List<AccountInfo> updateAccountInfoList);
	
	/**
	 * 更新账户信息中的可用余额和消费额
	 * @param campaignTotalCostList
	 * @return
	 */
	List<AccountInfoCost> updateAccountInfo(List<CampaignCostDetail> campaignTotalCostList);
	
	/**
	 * 更新活动明细报表中的花费字段
	 * @param campaignTotalCostList
	 * @return
	 */
	Integer addTempReportDetail(List<CampaignCostDetail> campaignTotalCostList);
	
	
	/**
	 * 查询需要通知下线的活动列表
	 * @param campaignTotalCostList
	 * @return
	 */
	//public List<Campaign> listNofiyCampaign(List<CampaignCostDetail> campaignTotalCostList);
	
	/**
	 * 更新需要下线的活动状态
	 * @param accountInfoList
	 * @return
	 */
	//public Integer updateCampaginStatus(List<Campaign> campaignList);
	
	/**
	 * 通知RTB活动下线
	 * @param accountInfoList
	 * @return
	 */
	//public Integer notifyCampaginStatus(List<Campaign> campaignList);
}
