package com.emarbox.dspmonitor.scheduler.clear;

import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dsp.cost.service.CampaignCostDetailService;
import com.emarbox.dspmonitor.billing.util.BaseScheduler;
import com.emarbox.dspmonitor.status.data.NodeStatus;

/**
 * 清除昨天之前的 campaign_cost_detail 数据
 * @author mr_zhu
 *
 */
public class ClearCostDetailScheduler extends BaseScheduler {
	
	@Autowired
	private CampaignCostDetailService campaignCostDetailService;
	
	public synchronized void execute() {
		//非主节点直接退出
		if(!NodeStatus.isMajorNode){
			return;
		}
				
		log.debug("清空 campaign_cost_detail 昨天之前数据开始");
		
		campaignCostDetailService.cleanOldData();
		
		log.debug("清空 campaign_cost_detail昨天之前数据结束");
	}

	public void setCampaignCostDetailService(
			CampaignCostDetailService campaignCostDetailService) {
		this.campaignCostDetailService = campaignCostDetailService;
	}

	
}
