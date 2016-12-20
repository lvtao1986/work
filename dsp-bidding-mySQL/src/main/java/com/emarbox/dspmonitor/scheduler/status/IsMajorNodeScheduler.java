package com.emarbox.dspmonitor.scheduler.status;

import com.emarbox.DspBaseScheduler;
import com.emarbox.dspmonitor.status.util.ElectionerCurator;

/**
 * 确定主节点
 * 
 * @author liumingfeng
 * 
 */
public class IsMajorNodeScheduler extends DspBaseScheduler {

	public void execute() {
		log.info("IsMajorNodeScheduler start");
		try {
			new ElectionerCurator().start();
		} catch (Exception e) {
			log.error("IsMajorNodeScheduler star ERROR:"+e.getMessage(),e);
		}
	}

}
