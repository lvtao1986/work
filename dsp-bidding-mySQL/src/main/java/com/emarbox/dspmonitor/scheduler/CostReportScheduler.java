package com.emarbox.dspmonitor.scheduler;


import java.util.ArrayList;
import java.util.List;

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
public class CostReportScheduler extends BaseScheduler {

	public static final Long NODE_SLEEP_TIME = 10 * 1000L;
	
	@Autowired
	CampaignCostDetailDao campaignCostDetailDao;
	
	@Autowired
	AccountInfoDao accountInfoDao;

	private static Counter counter = MetricsUtil.metricsRegistry.counter("costReportUnConsumeSize");
	private static Timer timer = MetricsUtil.metricsRegistry.timer("unconsumeSizeTimer");

	private static List<CostReportThread> threadList = new ArrayList<CostReportThread>();
	public static long[] getUnconsumeSize(){
		Timer.Context timeContext = timer.time(); //计时开始
		counter.dec(counter.getCount()); //清空计数器
		long size = 0;
		long sumCount = 0;
		long sendCount = 0;
		for(CostReportThread thread : threadList){
			size += thread.getUnConsumeMessageSize();
			sumCount += thread.todaySumCount;
		}
		counter.inc(size); //计数
		timeContext.stop(); //计时结束
		return new long[]{size,sumCount,sendCount};
	}
	
	public void execute() {
		log.info("start CostReportScheduler");

		while (true) {
			try {
				if (NodeStatus.isMajorNode){//主节点启动计算任务
					startBilling();
					getUnconsumeSize();//统计未处理队列数
				}
				Thread.sleep(NODE_SLEEP_TIME); //休眠 10s
			} catch (Exception e) {
				log.error("开启计费定时任务异常：" + e.getMessage(), e);
			}
		}

	}
	
	private void startBilling(){
		List<Long> accountIdList = accountInfoDao.getAllAccountId();
		for(long accountId : accountIdList){
			if((accountId % NodeStatus.accountModCount) == NodeStatus.accountMod){ //仅处理本节点account
				//从list总获取thread
				CostReportThread thread = null;
				for(CostReportThread th : threadList){
					if(th.getAccountId() == accountId){
						thread = th;
					}
				}
				//账号不在处理中则开启线程
				if(thread == null){
					thread = new CostReportThread(accountId,this);
					threadList.add(thread);
					thread.start();
				}
				
			}
		}
		
	}
	
}
