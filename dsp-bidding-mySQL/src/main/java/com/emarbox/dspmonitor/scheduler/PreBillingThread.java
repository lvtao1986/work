package com.emarbox.dspmonitor.scheduler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.util.ConfigUtil;

import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.util.CostUtil;
import com.emarbox.dspmonitor.data.CampaignCost;

public class PreBillingThread extends BasicKafkaConsumer {
	protected static final Logger log = LoggerFactory.getLogger(PreBillingThread.class);
	
	private long projectId; //线程需要处理的账号ID
	private PreBillingScheduler costScheduler;
	
	PreBillingThread(long projectId,PreBillingScheduler costScheduler){
		this.projectId = projectId;
		this.costScheduler = costScheduler;
	}
	
	@Override
	protected boolean handleMessageList(List<String> costLogList) {

			try {
				
					Map<String, CampaignCost> mergeMap = new HashMap<String,CampaignCost>();//<date_hour_supplier_campaignId,campaignCost>  存放需要保存到正式mongo cost表的数据
					try {
						
						for (String costLog : costLogList) {
							CampaignCost cost = convertLogToCost(costLog);
							if(cost.getCost() == null || cost.getCost() == 0d){
								continue;
							}
							
							//<statTime_campaignId,campaignCost>>>>  存放需要保存到pre_billing_detail表的数据
							String key = cost.getStatTime() + "_" + "_" + cost.getCampaignId();
							CampaignCost mergeCost = mergeMap.get(key);
							if(mergeCost == null){
								mergeCost = cost;
								mergeMap.put(key, mergeCost);
								
							}else{
								mergeCost.setCost(mergeCost.getCost() + cost.getCost());
							}
						}
					} catch (Exception e) {
						log.error("处理计费数据时出现异常:"+e.getMessage(),e);
					}
					
					saveResultToDB(mergeMap.values()); //数据存入campaign_cost_detail表
					return true;
			} catch (Exception e) {
				log.error("已结算队列统计入库时出现异常：" + e.getMessage(), e);
				return false;
			}

	}

	
	/**
	 * cost log 转 campaignCost
	 */
	private CampaignCost convertLogToCost(String costLog) {
		String [] logArray = costLog.split(",",-1);
		
		String requestId = logArray[0];
		Long campaignId = Long.parseLong(logArray[5]);
		String statTime = logArray[7].substring(0, 10);
		
		CampaignCost cost = new CampaignCost();
		cost.setCampaignId(campaignId);
		cost.setStatTime(statTime);
		
		String supplierId = logArray[1];
		String actionType = logArray[3];
		String costType = logArray[2];
		String biddingCostStr = logArray[4];
		String rtbCostStr = logArray[8];
		String maxPrice = logArray[13];
		String ctr = logArray[9];
		String valuePriceStr = logArray[11];
		boolean retarget = logArray[17].equals("1");
		boolean td = logArray[20].equals("1");
		
		if((actionType.equalsIgnoreCase("impression")&&costType.equalsIgnoreCase("cpm") )
			||(actionType.equalsIgnoreCase("click")&&costType.equalsIgnoreCase("cpc") )){
			double c = CostUtil.calBidding(requestId,campaignId.toString(),supplierId, actionType, costType, biddingCostStr, rtbCostStr, 
				maxPrice, ctr, valuePriceStr, retarget, td,false).getCost();
			cost.setCost(c);
		}else{
			cost.setCost(0d);
		}
		
		
		return cost;
	}

	private void saveResultToDB(Collection<CampaignCost> costList) {
		
		for(CampaignCost cost : costList){
			BillingCampaign campaign = FinalData.STATIC_CAMPAIGN_MAP.get(cost.getCampaignId());
			if(campaign!= null){
				cost.setUserId(campaign.getUserId());
			}
		}
		
		costScheduler.campaignCostDetailDao.mergePreBillingDetail(costList); //数据入库
	}

	public long getProjectId() {
		return projectId;
	}

	@Override
	public String getTopic() {
		return ConfigUtil.getString("kafka.topic.name.prefix.billing")+projectId;
	}


	@Override
	protected String getClientName() {
		return "pre_billing_report_"+projectId;
	}

	@Override
	protected int getPartition() {
		return 0;
	}

}
