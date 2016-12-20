package com.emarbox.dspmonitor.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;

import app.base.util.ConfigUtil;

import com.codahale.metrics.Timer;
import com.emarbox.dsp.finance.dao.AccountInfoDao;
import com.emarbox.dsp.util.Function;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.util.BaseScheduler;
import com.emarbox.dspmonitor.costing.dao.BillingDetailDao;
import com.emarbox.dspmonitor.data.CampaignCost;
import com.emarbox.dspmonitor.status.data.NodeStatus;

/**
 * 判断活动下线定时任务，每5秒执行一次 看有没有超出每日预算和总预算的活动
 * 
 */
public class CampaignOfflineScheduler extends BaseScheduler {

	private Double threshold = Double.valueOf(ConfigUtil.getString("dsp.billing.campaign.threshold"));

	private final Timer offlineMeter1 = MetricsUtil.metricsRegistry.timer("offline_getAccountId");
	private final Timer offlineMeter2 = MetricsUtil.metricsRegistry.timer("offline_getCampaignTodayCost");
	private final Timer offlineMeter3 = MetricsUtil.metricsRegistry.timer("offline_sentCampaignStop");
	
	@Autowired
	private BillingDetailDao billingDetailDao;
	
	@Autowired
	private AccountInfoDao accountInfoDao;
	
	public void execute() {
		//非主节点直接退出
		if(!NodeStatus.isMajorNode){
			return;
		}
		
		Map<Long, Double> delayCostMap = FinalData.STATIC_CAMPAIGN_COST_MAP;
		
		log.info("开始5秒判断预算逻辑");
		Timer.Context context1 = offlineMeter1.time();
		Map<Long, BillingCampaign> staticCampaignMap = FinalData.STATIC_CAMPAIGN_MAP;
		List<BillingCampaign> infoList = new ArrayList<BillingCampaign>();
		String time = Function.getDateDayStrings();
		List<Long> accountIdList = accountInfoDao.getAllAccountId();
		context1.stop();
		
		for (long accountId : accountIdList) {
			//仅处理负责账户
			if((accountId % NodeStatus.accountModCount )!= NodeStatus.accountMod){ 
				continue;
			}

			// 查询所有活动当天花费情况，包含未结算和已结算的
			Timer.Context context2 = offlineMeter2.time();
			List<CampaignCost> campaignCostList = billingDetailDao.getCampaignTodayCost(accountId,time);
			context2.stop();
			
			if (null != campaignCostList && campaignCostList.size() > 0) {

				for (CampaignCost campaignCost : campaignCostList) {

					Double curveCost = campaignCost.getCost();
					Double estimateCost = 0.0;

					Long campaignId = campaignCost.getCampaignId();
					// 活动该活动的预估延迟花费
					Double delayCost = delayCostMap.get(campaignId);
					// 若该活动有预估延迟花费时将延迟花费计入花费中
					if (delayCost != null) {
						estimateCost = curveCost + delayCost;
					} else {
						estimateCost = curveCost;
					}

					BillingCampaign cam = staticCampaignMap.get(campaignId);
					if (cam == null) {
						continue;
					}

					// 如果设置了日预算
					if (cam.getDailyBudget() > 0 && cam.getDailyBudget() != 0) {
						if (estimateCost >= cam.getTotalBudget() * threshold
								|| estimateCost >= cam.getDailyBudget()
										* threshold) {
							BillingCampaign info = new BillingCampaign();
							info.setCampaignId(campaignId);
							info.setCost(curveCost);
							info.setDelayCost(delayCost);
							infoList.add(info);
						}
					} else {// 判断花费是否大于总预算
						if (estimateCost >= cam.getTotalBudget() * threshold) {
							BillingCampaign info = new BillingCampaign();
							info.setCampaignId(campaignId);
							info.setCost(curveCost);
							info.setDelayCost(delayCost);
							infoList.add(info);
						}
					}
				}
			}
			
		}
		
		if (null != infoList) {
			if (infoList.size() > 0) {
				Timer.Context context3 = offlineMeter3.time();
				getBillingSendCampaignService().sendCampaignStop(infoList);
				context3.stop();
				log.info("因花费接近预算通知活动下线信息给API：" + infoList.toString());
			}
		}
		
		log.info("结束5秒判断预算逻辑");		
	}

}
