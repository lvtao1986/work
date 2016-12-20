package com.emarbox.dspmonitor.scheduler;

import app.base.util.ConfigUtil;
import app.common.util.LogUtil;
import app.common.util.Logs;

import com.emarbox.dsp.domain.Campaign;
import com.emarbox.dsp.domain.CampaignSnapshot;
import com.emarbox.dsp.finance.dao.AccountInfoDao;
import com.emarbox.dsp.util.Function;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.costing.dao.BillingDataDao;
import com.emarbox.dspmonitor.costing.dao.CampaignBudgetLogDao;
import com.emarbox.dspmonitor.costing.dao.CampaignCostDetailDao;
import com.emarbox.dspmonitor.data.CampaignCost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

@Service("billingUtil")
public class BillingUtil {
	
	private static Logs log = LogUtil.getLog(BillingUtil.class);
	private static Logs costLogicLog = LogUtil.getLog("costLogicLog");
	
	@Autowired
	private CampaignCostDetailDao campaignCostDetailDao;
	
	@Autowired
	private AccountInfoDao accountInfoDao;
	@Autowired
	private BillingDataDao billingDataDao;
	@Autowired
	private CampaignBudgetLogDao campaignBudgetLogDao;
	
	private Map<Long,Double> accountBugetMap = null;//账户当日可用花费<userId,amount>  凌晨余额 + 当日充值 + 当日赠送 - 当日退款
	private Map<Long,Double> accountBalanceMap = null; //当前账户余额<userId,amount>
	private Map<Long,Double> campaignProfitRateMap = null; //活动当天毛利
			
	private Double maxCamBudget =null;//广告主活动花费超出限制预算的固定值
	private Double maxCamBudgetPre = null;//广告主活动花费超出限制百分比
	private Double BalanceLimit = null; //余额花超限制最大比例
	
	private Double MINI_CPC = 0.01d; //最小cpc, 当毛利率大于最高毛利率时按此收取cpc
	
	
	/**
	 * 
	 * @param cost 待结算信息
	 * @param billingCam
	 * @param accountNcostMap 本轮账户已扣费信息<userId,cost>
	 * @param campaignNcostMap 本轮活动已扣费信息<campaignId,cost>
	 * @param camTodayCostMap 活动当天花费<campaignId_statDate,cost>
	 */
	public void billing(CampaignCost cost,BillingCampaign billingCam,Calendar statDateCal,Long userId,Map<Long,Double> accountNcostMap,Map<Long,Double> campaignNcostMap,Map<String,Double> camTodayCostMap,Double accountBalance,Map<Long,Double> bugetMap){
		DecimalFormat df = new DecimalFormat("#.#");
		df.setMaximumFractionDigits(6);
		
		StringBuilder sb = new StringBuilder();
		long timeBegin = System.nanoTime();
		Double theCost =cost.getCost(); //本次扣费
		
		Long campaignId = cost.getCampaignId(); 
		double tempCost = theCost; //临时变量
		/*
		 * 毛利下线控制: 判断毛利率是否符合设置，是否需要改用最高限价收费
		 */
		Double crate = campaignProfitRateMap.get(campaignId);
		
		if(crate==null){
			crate = 0d ;
		}

		Double miniProfitRate = FinalData.CAPAIGN_MINI_PROFIT_RATE_CONFIG.get(campaignId); //最低毛利率设置
		Double maxProfitRate = FinalData.CAPAIGN_MAX_PROFIT_RATE_CONFIG.get(campaignId); //最高毛利率设置

		if(miniProfitRate == null){
			miniProfitRate = 0.3; //默认0.3
		}
		if(crate < miniProfitRate){ //使用最高限价,根据点击数和展现数来判断活动类型是cpm/cpc
			if(cost.getClick()!=null && cost.getClick() > 0){//cpc收费
				theCost = cost.getClick() * billingCam.getBudgetLimit();
				log.debug("使用最高限价，campaignId:"+cost.getCampaignId() + " cpc:"+ billingCam.getBudgetLimit());
			}else if(cost.getImpression() != null && cost.getImpression() > 0){//cpm收费
				theCost = cost.getImpression() * billingCam.getBudgetLimit() / 1000d;
				log.debug("使用最高限价，campaignId:"+cost.getCampaignId() + " cpm:"+ billingCam.getBudgetLimit());
			}
		}
		//记录逻辑对花费影响
		if(tempCost != theCost.doubleValue()){
			costLogicLog.info("requestId:"+cost.getRequestId()+",campaignId:"+campaignId+",logic:min_profit_rate_logic,before:"+ df.format(tempCost) + ",after:"+df.format(theCost));//最低毛利逻辑
			tempCost = theCost;
		}
		
		/*
		 * 毛利上线控制: 超出设置上线则收取 点击 0.01元    , cpm活动不涉及毛利上线问题
		 */
		if(maxProfitRate == null){
			maxProfitRate = 1.0; //默认100%
			log.debug("使用默认最高毛利率:"+maxProfitRate);
		}
		if(crate > maxProfitRate  && cost.getClick()!=null && cost.getClick() > 0){
			theCost = cost.getClick() * MINI_CPC;
			log.debug("使用最低CPC，campaignId:"+cost.getCampaignId() + " cpc:"+ MINI_CPC);
		}
		//记录逻辑对花费影响
		if(tempCost != theCost.doubleValue()){
			costLogicLog.info("requestId:"+cost.getRequestId()+",campaignId:"+campaignId+",logic:max_profit_rate_logic,before:"+ df.format(tempCost) + ",after:"+df.format(theCost));//最高毛利逻辑
			tempCost = theCost;
		}	
		
		
		/**
		 * 1.如果活动下线超过5分钟后的点击，不计费。
		 * 2.如果超过活动 日预算5% 不计费。
		 * 3.账户余额小于当天活动开始投放时余额的-2%后的金额不再进行计费, 但点击和展现要计入
		 */
		
		/*
		 * 判断零星投放
		 */
		CampaignSnapshot history = new CampaignSnapshot();
		history.setCampaignId(campaignId);
		history.setCreateTime(statDateCal.getTime());
		// 获取活动快照
		long time1 = System.nanoTime();

		CampaignSnapshot snapshot = campaignCostDetailDao.getCampaignSnapshot(history);
		long time3 =  System.nanoTime();
		sb.append("快1:").append(time3-time1).append("\t");
		if (snapshot != null && !Campaign.CAMPAIGN_STATUS_VALID.equals(snapshot.getCampaignStatus())) {// 处理活动状态无效情况
			//查询五分钟内的上线记录
			CampaignSnapshot fiveshot = campaignCostDetailDao.getFiveMinuteValidSnapshot(snapshot);
			if(fiveshot!=null){
				//如果给下线留1分钟时间
				Calendar c = Calendar.getInstance();
				c.setTime(fiveshot.getCreateTime());
				if(c.get(Calendar.MINUTE) ==0 ){
					c.add(Calendar.MINUTE, -1);
				}
				
				statDateCal.setTime(fiveshot.getCreateTime());
			} else {// 活动状态已为无效或花费超出预算时只计算点击和展现不计算花费
				theCost = 0d;
			}
		}
		long time2 = System.nanoTime();
		sb.append("快5:").append(time2 - time3).append("\t");
		//log.debug("处理快照的时间："+(time2 - time1) +"ns");
		
		cost.setStatHour(statDateCal.get(Calendar.HOUR_OF_DAY));
		cost.setStatDate(Function.parseDateTostr( statDateCal.getTime() ));
		String statDate = cost.getStatDate();
		//记录逻辑对花费影响
		if(tempCost != theCost.doubleValue()){
			costLogicLog.info("requestId:"+cost.getRequestId()+",campaignId:"+campaignId+",logic:offline_5min_bidding_logic,before:"+ df.format(tempCost) + ",after:"+df.format(theCost));//零星投放逻辑
			tempCost = theCost;
		}
				
		/*
		 * 判断活动预算修正本次扣费
		 */
		Double sumCost = camTodayCostMap.get(campaignId+"_"+statDate);
		if(sumCost==null){
			sumCost = billingDataDao.getConfirmedCost(campaignId,statDate);
			camTodayCostMap.put(campaignId+"_"+statDate, sumCost);
		}
		time1 = System.nanoTime();
		sb.append("已费:").append(time1 - time2).append("\t");
		//log.debug("获取已经消费的COST耗时："+(time1 - time2) +"ns");
		if(maxCamBudget == null){
			maxCamBudget = campaignBudgetLogDao.queryAppConfig("max_campaign_budget");//广告主活动花费超出限制预算的固定值
		}
		if(maxCamBudgetPre == null){
			maxCamBudgetPre = campaignBudgetLogDao.queryAppConfig("max_campaign_budget_percent");//广告主活动花费超出限制百分比
		}
		time2 = System.nanoTime();
		sb.append("限额:").append(time2 - time1).append("\t");
		//log.debug("获取广告主限额耗时："+(time2 - time1) +"ns");
		theCost = judgeCampaignimit(billingCam, theCost, maxCamBudget, maxCamBudgetPre,sumCost,campaignNcostMap.get(campaignId));
		
		//记录逻辑对花费影响
		if(tempCost != theCost.doubleValue()){
			costLogicLog.info("requestId:"+cost.getRequestId()+",campaignId:"+campaignId+",logic:campaign_budget_logic,before:"+ df.format(tempCost) + ",after:"+df.format(theCost));//活动预算逻辑
			tempCost = theCost;
		}
		
		/*
		 *  判断账户预算修正本次扣费
		 */
		if(theCost > 0){
			if(accountBalance == null){
				accountBalance = 0d;
			}
			Double accountBudget = bugetMap.get(userId); //账户当日可用预算
			if(accountBudget == null){
				accountBudget = 0d;
			}
			Double a = 2d;

			theCost = judgeAccountimit(userId, theCost,accountBalance,accountBudget, accountNcostMap.get(userId));
		}
		
		//记录逻辑对花费影响
		if(tempCost != theCost.doubleValue()){
			costLogicLog.info("requestId:"+cost.getRequestId()+",campaignId:"+campaignId+",logic:account_budget_logic,before:"+ df.format(tempCost) + ",after:"+df.format(theCost));//账户预算逻辑
			tempCost = theCost;
		}
		
		time1 = System.nanoTime();
		sb.append("修正:").append(time1 - time2).append("\t");
		//log.debug("计算修正扣费耗时："+ (time2 - time1) +"ns");
		//设置最终计费结果
		cost.setCost(theCost);
		
		//数据写入,预算修正用map
		if(cost.getCost()>0){
			Double campaignCost = campaignNcostMap.get(campaignId);
			if(campaignCost ==null){
				campaignCost = 0d;
			}
			campaignCost = campaignCost + cost.getCost();
			campaignNcostMap.put(campaignId, campaignCost);
			
			Double accountCost = accountNcostMap.get(userId); 
			if(accountCost == null){
				accountCost = 0d;
			}
			accountCost = accountCost + cost.getCost();
			accountNcostMap.put(userId, accountCost);
			
		}
		long costTime = System.nanoTime() - timeBegin;
		sb.append("总:").append(costTime);
		if(costTime > 2000000){
			log.info("计费耗时："+sb.toString() +"ns");
		}
	}
	
	/**
	 * 判断当前余额是否透支超过凌晨余额的2%
	 * @param userId 用户id
	 * @param theCost 本次预计扣费
	 * @param accountBalance 当前账户余额
	 * @param todayBudget 当天账户最多花费
	 * @param ncost 本轮账户已扣费
	 * @return 修正后扣费
	 */
	private Double judgeAccountimit(Long userId, Double theCost,Double accountBalance,Double todayBudget,Double ncost) {
		
		//减去本轮花费
		if(ncost!=null && ncost > 0){
			accountBalance = accountBalance - ncost;
		}
		
		if(accountBalance>theCost){
			return theCost;
		}
		
		if(BalanceLimit == null){
			BalanceLimit = Double.valueOf(ConfigUtil.getString("dsp.billing.account.balance.limit")); //余额花超限制最大比例
		}
		double rc = accountBalance +  todayBudget * BalanceLimit ;  //可用花费 = 剩余+透支
		
		if(rc > 0 ){  //可用花费>0
			if(theCost > rc){
				return rc;
			}else{
				return theCost;
			}
		}else{ //可用花费<0
			return 0d;
		}
		
	}
	
	/**
	 * 实时预算修正逻辑,并根据预算值来判断本次收费 当有日预算时，判断日预算和总预算，取预算最小值 根据预算最小值+限制固定值和预算最小值*限制百分比
	 * 相比取得限制最小值 限制最小值减去已花费，得可用花费 可用花费与本次计费相比，取最小值为最终扣费
	 */
	private Double judgeCampaignimit(BillingCampaign billingCam, Double cost, Double maxCampaignBudget, Double maxCampaignBudgetPercent,Double sumCost,Double ncost) {
		if(ncost != null){
			sumCost = sumCost + ncost; //当天花费+本轮花费
		}

		Double totalBudget = billingCam.getTotalBudget(), dailyBudget = billingCam.getDailyBudget();
		log.debug("sumCost=" + sumCost + ".totalBudget=" + totalBudget + ".dailyBudget=" + dailyBudget + "。cost=" + cost);

		if(totalBudget == null){
			totalBudget = 0d;
		}
		if(dailyBudget == null){
			dailyBudget = 0d;
		}
		Double minTotalBudget =totalBudget == 0? 0d: Math.min(totalBudget * maxCampaignBudgetPercent, totalBudget + maxCampaignBudget);
		Double minDailyBuget = dailyBudget == 0? 0d: Math.min(dailyBudget*maxCampaignBudgetPercent,dailyBudget+maxCampaignBudget);
		Double retCost = cost;
		if(minTotalBudget>0){
			if(sumCost + cost > minTotalBudget){
				retCost = minTotalBudget - sumCost;
			}
		}
		if(minDailyBuget>0){
			if(sumCost + cost > minDailyBuget){
				retCost = Math.min(minDailyBuget-sumCost,retCost);
			}
		}
		if(retCost < 0){
			retCost = 0d;
		}
		return retCost;
	}

	public  Map<Long,Double> getAcountRemain(boolean useCache){
		if(accountBalanceMap==null || !useCache) {
			accountBalanceMap = accountInfoDao.getAccountRemainAmount(); //当前账户余额<userId,amount>
		}
		return accountBalanceMap;
	}

	public  Map<Long,Double> getBugetRemain(boolean useCache){
		if(accountBugetMap==null || !useCache) {
			accountBugetMap = accountInfoDao.getAccountTotalAmounnt(); //账户当日可用花费<userId,amount>  凌晨余额 + 当日充值 + 当日赠送 - 当日退款
		}
		return accountBugetMap;
	}
//
//	public void refreshAcountRemain(Long userId){
//		if (accountBalanceMap == null) {
//			accountBalanceMap = accountInfoDao.getAccountRemainAmount(); //当前账户余额<userId,amount>
//		}else{
//			accountBalanceMap.put(userId, accountInfoDao.getAccountRemainAmount(userId));
//		}
//	}
	
	public void refreshAccountInfo(){
		accountBalanceMap = accountInfoDao.getAccountRemainAmount(); //当前账户余额<userId,amount>
		accountBugetMap = accountInfoDao.getAccountTotalAmounnt(); //账户当日可用花费<userId,amount>  凌晨余额 + 当日充值 + 当日赠送 - 当日退款
		//campaignProfitRateMap = campaignCostDetailDao.getCammpaignTodayProfitRate(); //活动当天毛利
		
		maxCamBudget = campaignBudgetLogDao.queryAppConfig("max_campaign_budget");//广告主活动花费超出限制预算的固定值
		maxCamBudgetPre = campaignBudgetLogDao.queryAppConfig("max_campaign_budget_percent");//广告主活动花费超出限制百分比
	}

	//设置活动当天毛利
	public void setCampaignProfitRateMap( Map<Long,Double> campaignProfitRateMap){
		this.campaignProfitRateMap = campaignProfitRateMap;
	}
/*
	public static void main(String[] args){
		BillingCampaign bc = new BillingCampaign();
		bc.setTotalBudget(2000d);
		bc.setDailyBudget(2500d);
		System.out.println(BillingUtil.judgeCampaignimit(bc, 85d, 10d, 1.05d, 1950d, 0d));
		bc.setTotalBudget(2500d);
		bc.setDailyBudget(2000d);
		System.out.println(BillingUtil.judgeCampaignimit(bc, 85d, 10d, 1.05d, 1950d, 200d));
		bc.setTotalBudget(2500d);
		bc.setDailyBudget(2500d);
		System.out.println(BillingUtil.judgeCampaignimit(bc, 85d, 10d, 1.05d, 1950d, 500d));
		bc.setTotalBudget(2200d);
		bc.setDailyBudget(2200d);
		System.out.println(BillingUtil.judgeCampaignimit(bc, 85d, 10d, 1.05d, 1950d, 220d));
		bc.setTotalBudget(2200d);
		bc.setDailyBudget(2200d);
		System.out.println(BillingUtil.judgeCampaignimit(bc, 85d, 200d, 1.05d, 1950d, 220d));
	}
	*/
}
