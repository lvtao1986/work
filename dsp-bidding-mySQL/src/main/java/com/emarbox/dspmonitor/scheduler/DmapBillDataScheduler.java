package com.emarbox.dspmonitor.scheduler;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dsp.finance.dao.AccountInfoDao;
import com.emarbox.dspmonitor.billing.util.BaseScheduler;
import com.emarbox.dspmonitor.status.data.NodeStatus;

/**
 * 结算报表,将以确定的扣费数据汇总入 campaign_cost_detail 表
 * 
 * @author mr_zhu
 * 
 */
public class DmapBillDataScheduler extends BaseScheduler {

	public static final Long NODE_SLEEP_TIME = 10 * 1000L;
	
	
	@Autowired
	AccountInfoDao accountInfoDao;
	
	private static List<DmapBillDataThread> threadList = new ArrayList<DmapBillDataThread>();
	
	public static long getUnconsumeSize(){
		long size = 0;
		for(DmapBillDataThread thread : threadList){
			size += thread.getUnConsumeMessageSize();
		}
		return size;
	}
	
	public void execute() {
		log.info("start DmapBillDataScheduler");

		while (true) {
			try {
				if (NodeStatus.isMajorNode){//主节点启动计算任务
					startBilling();
				}
				Thread.sleep(NODE_SLEEP_TIME); //休眠 10s
			} catch (Exception e) {
				log.error("开启DmapBillDataScheduler定时任务异常：" + e.getMessage(), e);
			}
		}

	}
	
	private void startBilling(){
		List<Long> accountIdList = accountInfoDao.getAllAccountId();
		for(long accountId : accountIdList){
			if((accountId % NodeStatus.accountModCount) == NodeStatus.accountMod){ //仅处理本节点account
				//从list总获取thread
				DmapBillDataThread thread = null;
				for(DmapBillDataThread th : threadList){
					if(th.getAccountId() == accountId){
						thread = th;
					}
				}
				//账号不在处理中则开启线程
				if(thread == null){
					thread = new DmapBillDataThread(accountId);
					threadList.add(thread);
					thread.start();
				}
			}
		}
		
	}
	
}
