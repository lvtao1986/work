package com.emarbox.dsp.monitor.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dsp.monitor.data.DataCache;
import com.emarbox.dsp.monitor.service.CampaignService;

/**
 * 更新缓存定时任务
 */
public class DataCacheUpdateTask {

	private static final Logger log = LoggerFactory.getLogger(DataCacheUpdateTask.class);
	
	@Autowired
	private CampaignService campaignService;
	
	public void execute() {
		log.info("update cache data task begin");
		try {
			//初始化活动最高限价信息
			DataCache.setMaxPriceMap(campaignService.getMaxPriceMap());
			//初始化活动UserID信息
			DataCache.setCampaignUserIdMap(campaignService.queryCampaignUserIdMap());
			//初始化creativeId 和creativeSetId 对应关系
			DataCache.setCreativeIdMap(campaignService.getCreativeIdMap());
			
			commonTaskStep();
		} catch (Exception e) {
			log.error("update cache data ERROR:"+e.getMessage(),e);
		}
		log.info("update cache data task end");
	}
	
	public void executePeriodically() {
		log.info("periodically update cache data task begin");
		try {
			//定期更新活动最高限价信息
			DataCache.setMaxPriceMap(campaignService.getRecentMaxPriceMap());
			//定期更新活动UserID信息
			DataCache.setCampaignUserIdMap(campaignService.queryRecentCampaignUserIdMap());
			//定期更新creativeId 和creativeSetId 对应关系
			DataCache.setCreativeIdMap(campaignService.getRecentCreativeIdMap());
			
			commonTaskStep();
		} catch (Exception e) {
			log.error("periodically update cache data ERROR:"+e.getMessage(),e);
		}
		log.info("periodically update cache data task end");
	}
	
	/**
	 * 通用任务执行步骤(目前包含的步骤包含活动回头客和回头客毛利率加载)
	 */
	private void commonTaskStep(){
		//定期更新活动回头客设置信息
		DataCache.setRetargetMap(campaignService.getRetargetMap());
		//定期更新活动回头客毛利率设置信息
		DataCache.CAPAIGN_PROFIT_RATE_CONFIG.putAll(campaignService.queryCampaignProfitRateSet());
	}
}
