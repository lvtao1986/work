package com.emarbox.dspmonitor.scheduler;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import com.emarbox.dspmonitor.data.RequestIdService;
import com.emarbox.dspmonitor.scheduler.cost.CostThreadManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dsp.finance.dao.AccountInfoDao;
import com.emarbox.dspmonitor.billing.service.BillingCostService;
import com.emarbox.dspmonitor.billing.util.BaseScheduler;
import com.emarbox.dspmonitor.costing.dao.BillingDataDao;
import com.emarbox.dspmonitor.costing.dao.CampaignCostDetailDao;
import com.emarbox.dspmonitor.data.BillingDataService;
import com.emarbox.dspmonitor.status.data.NodeStatus;

/**
 * 结算定时任务,每5s将未结算的扣费数据结算入 campaign_cost_detail 表
 * 根据projectID创建多个计费主线程,计费主线程读取kafka数据,然后创建多个计费子线程去计费
 * 
 * @author mr_zhu
 * 
 */
public class CostScheduler extends BaseScheduler {

	public static final Long NODE_SLEEP_TIME = 5 * 60 * 1000L; //5分钟

	 final ReadWriteLock lock = new ReentrantReadWriteLock(); 

	@Autowired
	CostThreadManager costThreadManager;

	@Autowired
	public BillingDataService billingDataService;

	@Autowired
	CampaignCostDetailDao campaignCostDetailDao;
	
	@Autowired
	public BillingUtil billingUtil;
	
	@Autowired
	BillingCostService billingCostService;
	
	@Autowired
	AccountInfoDao accountInfoDao;

	@Autowired
	BillingDataDao billingDataDao;

	@Autowired
	public RequestIdService requestIdService;

	private static Map<Long,HashMap<Long,CostThread>> threadMap = new HashMap<Long, HashMap<Long,CostThread>>(80);

	protected long accountRemainRefreshStamp = -1L;

	private static Counter counter = MetricsUtil.metricsRegistry.counter("costUnConsumeSize");
	private static Timer timer = MetricsUtil.metricsRegistry.timer("unconsumeSizeTimer");

	public static long[] getUnconsumeSize(){
		Timer.Context timeContext = timer.time(); //计时开始
		counter.dec(counter.getCount()); //清空计数器
		long size = 0;
		long sumCount = 0;
		long sendCount = 0;
		Collection<HashMap<Long,CostThread>> maps = threadMap.values();
		for(HashMap<Long,CostThread> tmap : maps){
			Collection<CostThread> threads = tmap.values();
			for(CostThread thread : threads){
				size += thread.getUnConsumeMessageSize();
				sumCount += thread.todaySumCount;
				sendCount += thread.todaySendCount;
			}
		}
		counter.inc(size); //计数
		timeContext.stop(); //计时结束
		return new long[]{size,sumCount,sendCount};
	}
	
	public void execute() {
		log.info("start CostScheduler");

		while (true) {
			try {
				if (NodeStatus.isMajorNode){//主节点启动计算任务
					try{
						log.info("try write lock");
						lock.writeLock().lock();
						log.info("get write lock");
						billingUtil.refreshAccountInfo(); //更新账户余额信息
						accountRemainRefreshStamp = System.currentTimeMillis();
					}finally{
						lock.writeLock().unlock();
					}
					startBilling();
					getUnconsumeSize();//统计未处理队列数
//					Thread.sleep(NODE_SLEEP_TIME); //休眠 10s
				}
				Thread.sleep(10*1000); //休眠 10s
			} catch (Exception e) {
				log.error("开启计费定时任务异常：" + e.getMessage(), e);
			}
		}

	}
	
	private void startBilling(){
		int threadCount = 0;
		Map<Long,Set<Long>> accountProjectMap =  accountInfoDao.getAccountWithProject();
		int accountIdSize = accountProjectMap.size();
		List<Long> accountIdList = new ArrayList<Long>(accountIdSize);
		accountIdList.addAll(accountProjectMap.keySet());
		for (Long accountId : accountIdList){
			if ((accountId % NodeStatus.accountModCount) != NodeStatus.accountMod){
				continue;
			}
			HashMap<Long,CostThread> projectThreadMap = threadMap.get(accountId);
			if(projectThreadMap==null){
				projectThreadMap = new HashMap<Long, CostThread>();
				threadMap.put(accountId,projectThreadMap);
			}
			Set<Long> projectIds = accountProjectMap.get(accountId);
			if(projectIds!=null&&!projectIds.isEmpty()) {
				for (Long projectId : projectIds) {
					CostThread thread = projectThreadMap.get(projectId);
					//账号不在处理中则开启线程
					if(thread == null){
						threadCount ++;
						thread = new CostThread(accountId,projectId,this);
						projectThreadMap.put(projectId,thread);
						thread.start();
					}
				}
			}
		}
		log.info("线程启动完成："+threadCount);
	}
	
}
