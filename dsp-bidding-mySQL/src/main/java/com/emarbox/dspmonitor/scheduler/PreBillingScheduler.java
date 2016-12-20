package com.emarbox.dspmonitor.scheduler;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dsp.finance.dao.AccountInfoDao;
import com.emarbox.dspmonitor.billing.util.BaseScheduler;
import com.emarbox.dspmonitor.costing.dao.CampaignCostDetailDao;
import com.emarbox.dspmonitor.status.data.NodeStatus;

/**
 * 结算报表,将以确定的扣费数据汇总入 campaign_cost_detail 表
 * 
 * @author mr_zhu
 * 
 */
public class PreBillingScheduler extends BaseScheduler {

	public static final Long NODE_SLEEP_TIME = 10 * 1000L;
	
	@Autowired
	CampaignCostDetailDao campaignCostDetailDao;
	
	@Autowired
	AccountInfoDao accountInfoDao;

	private static Counter counter = MetricsUtil.metricsRegistry.counter("preBillingUnConsumeSize");
	private static Timer timer = MetricsUtil.metricsRegistry.timer("unconsumeSizeTimer");

	private static List<PreBillingThread> threadList = new ArrayList<PreBillingThread>();
	public static long getUnconsumeSize(){
		Timer.Context timeContext = timer.time(); //计时开始
		counter.dec(counter.getCount()); //清空计数器
		long size = 0;
		for(PreBillingThread thread : threadList){
			size += thread.getUnConsumeMessageSize();
		}
		counter.inc(size); //计数
		timeContext.stop(); //计时结束
		return size;
	}
	
	public void execute() {
		log.info("start PreBilingScheduler");

		while (true) {
			try {
				if (NodeStatus.isMajorNode){//主节点启动计算任务
					startBilling();
					getUnconsumeSize();//统计未处理队列数
				}
				Thread.sleep(NODE_SLEEP_TIME); //休眠 10s
			} catch (Exception e) {
				log.error("开启prebilling定时任务异常：" + e.getMessage(), e);
			}
		}

	}
	
	private void startBilling(){
		Map<Long,Set<Long>> accountProjectMap =  accountInfoDao.getAccountWithProject();
		
		for(Long accountId : accountProjectMap.keySet()){
			if((accountId % NodeStatus.accountModCount) == NodeStatus.accountMod){ //仅处理本节点account
				Set<Long> projectSet = accountProjectMap.get(accountId);
				for(Long projectId : projectSet){
					//从list总获取thread
					PreBillingThread thread = null;
					for(PreBillingThread th : threadList){
						if(th.getProjectId() == projectId){
							thread = th;
						}
					}
					//账号不在处理中则开启线程
					if(thread == null){
						thread = new PreBillingThread(projectId,this);
						threadList.add(thread);
						thread.start();
					}
					
				}
			}
		}
		
	}
	
}
