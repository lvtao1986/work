package com.emarbox.dsp.cost;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.emarbox.dsp.DspBaseDao;
import com.emarbox.dsp.cost.domain.CampaignCostDetail;
import com.emarbox.dsp.domain.AccountInfo;

/**
 * 结算模块 dao 基类
 * @author zhaidw
 *
 */
public abstract class CostBaseDao extends DspBaseDao {
	
	protected static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss",Locale.SIMPLIFIED_CHINESE); 
	

	
	public String getCostSchedulerUserName(){
		return getConfig().getString("dsp.campaign.cost.scheduler.user_name", "costScheduler");
	}

	/**
	 * 返回包含 ids 参数的查询条件， ids的值是结算流水的ID列表
	 * @param campaignCostDetailList
	 * @return
	 */
	protected MapSqlParameterSource getCostDetailIdsParameter(List<CampaignCostDetail> campaignCostDetailList){
		
		if(null == campaignCostDetailList){
			return null;
		}
		
		if(campaignCostDetailList.size() == 0){
			return null;
		}
		List<Long> detailIdList = new ArrayList<Long>();
		for (CampaignCostDetail campaignCostDetail : campaignCostDetailList) {
			if(null != campaignCostDetail){
				detailIdList.add(campaignCostDetail.getId());
				//log.debug("id : " + campaignCostDetail.getId());
			}
		}
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", detailIdList);
		
		return parameters;
		
	}
	
	
	/**
	 * 返回包含 userIds 参数的查询条件， userIds的值是 广告主的ID列表
	 * @param campaignCostDetailList
	 * @return
	 */
	protected MapSqlParameterSource getUserIdsParameter(List<CampaignCostDetail> campaignTotalCostList){
		
		if(null == campaignTotalCostList){
			return null;
		}
		
		if(campaignTotalCostList.size() == 0){
			return null;
		}
		List<Long> detailIdList = new ArrayList<Long>();
		for (CampaignCostDetail campaignCostDetail : campaignTotalCostList) {
			if(null != campaignCostDetail){
				detailIdList.add(campaignCostDetail.getUserId());
				//log.debug("id : " + campaignCostDetail.getId());
			}
		}
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("uids", detailIdList);
		
		return parameters;
		
	}

	
	/**
	 * 返回包含 userIds 参数的查询条件， userIds 的值是 广告主的ID列表
	 * @param campaignCostDetailList
	 * @return
	 */
	protected MapSqlParameterSource getAccountInfoUserIdsParameter(List<AccountInfo> accountInfoList){
		
		if(null == accountInfoList){
			return null;
		}
		
		if(accountInfoList.size() == 0){
			return null;
		}
		List<Long> detailIdList = new ArrayList<Long>();
		for (AccountInfo accountInfo : accountInfoList) {
			if(null != accountInfo){
				detailIdList.add(accountInfo.getUserId());
				//log.debug("id : " + campaignCostDetail.getId());
			}
		}
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("uids", detailIdList);
		
		return parameters;
		
	}
	
}
