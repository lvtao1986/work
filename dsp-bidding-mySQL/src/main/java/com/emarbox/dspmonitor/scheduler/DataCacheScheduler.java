package com.emarbox.dspmonitor.scheduler;

import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dspmonitor.billing.dao.BillingCostDao;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.util.BaseScheduler;

/**
 * 每分钟运行一次，获取每个活动设置的毛利要求设置
 */
public class DataCacheScheduler extends BaseScheduler {
	@Autowired
	private BillingCostDao billingCostDao;
	
	public void execute() {
		log.info("update profit rate set begin");
		try {
			FinalData.CAPAIGN_MINI_PROFIT_RATE_CONFIG.putAll(billingCostDao.queryCampaignMiniProfitRateSet());
			FinalData.CAPAIGN_MAX_PROFIT_RATE_CONFIG.putAll(billingCostDao.queryCampaignMaxProfitRateSet());
			//更新STATIC_CAMPAIGN_MAP的数据，map包含活动当天预算
			FinalData.STATIC_CAMPAIGN_MAP.putAll(getBillingCostService().getCampaignCostList());
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		log.info("update profit rate set end");
	}
	
}
