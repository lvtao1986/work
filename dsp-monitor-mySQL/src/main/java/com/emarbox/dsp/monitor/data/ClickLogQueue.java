package com.emarbox.dsp.monitor.data;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.codahale.metrics.Counter;
import com.emarbox.dsp.monitor.dto.ClickLog;
import com.emarbox.dsp.monitor.util.MetricsUtil;

/**
 * 点击记录队列
 */
public class ClickLogQueue {

	public static final Counter counter = MetricsUtil.metricsRegistry.counter("clickLogQueueCount");

	/**
	 *  待发送日志缓冲区
	 */
	private static final Queue<ClickLog> logQueue = new ConcurrentLinkedQueue<ClickLog>();
	
	/**
	 * 获取待处理计费日志
	 * 
	 * @return 日志,没有则返回null
	 */
	public static ClickLog pollLog() {
		if(counter.getCount()>0L) {
			counter.dec();//计数-
		}
		return logQueue.poll();
	}
	
	/**
	 * 将日志添加至计费队列
	 * 
	 * @param log 待处理日志
	 * @return 是否成功
	 */
	public static boolean addLog(ClickLog log) {
		counter.inc(); //计数+
		log.setLogDate(new Date()); //使用当前时间作为日志时间
		return logQueue.add(log);
	}
	
	public static int getQueueSize(){
		return logQueue.size();
	}
}
