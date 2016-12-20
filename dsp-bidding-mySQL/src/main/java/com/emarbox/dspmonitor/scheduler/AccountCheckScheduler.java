package com.emarbox.dspmonitor.scheduler;

import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.DspBaseScheduler;
import com.emarbox.dsp.cost.service.AccountCheckService;
import com.emarbox.dspmonitor.status.data.NodeStatus;



/**
 * 广告主 余额检查处理 定时处理器
 * 
 * @author zhaidw
 * 
 */
public class AccountCheckScheduler  extends DspBaseScheduler {
	
	
	@Autowired
	protected AccountCheckService accountCheckService;

	public synchronized void execute() {
		if (!NodeStatus.isMajorNode) {
			return;
		}
		
		log.debug("AccountCheckScheduler["+ Thread.currentThread().getName() + "]");

		if (null != accountCheckService) {
			log.debug("accountCheckService start exucuted.");

			accountCheckService.checkProcess();

			log.debug("accountCheckService exucuted finished.");
			
		} else {
			log.error("accountCheckService is null .");
		}

	}
}
