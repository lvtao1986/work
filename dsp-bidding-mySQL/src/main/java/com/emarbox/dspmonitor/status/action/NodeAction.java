package com.emarbox.dspmonitor.status.action;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emarbox.dsp.finance.dao.AccountInfoDao;
import com.emarbox.dspmonitor.scheduler.CostReportScheduler;
import com.emarbox.dspmonitor.scheduler.CostScheduler;
import com.emarbox.dspmonitor.scheduler.DmapBillDataScheduler;
import com.emarbox.dspmonitor.scheduler.PreBillingScheduler;
import com.emarbox.dspmonitor.status.data.NodeStatus;

@Controller
public class NodeAction {

	private static final Logger log = LoggerFactory.getLogger(NodeAction.class);

	@Autowired
	AccountInfoDao accountInfoDao;
	
	@Autowired
	EhCacheCacheManager cacheManager;
	
	@RequestMapping(value = "/nodeStatus/get")
	public void sendData(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=utf-8");
			StringBuffer header = new StringBuffer();

			OutputStream out = response.getOutputStream();
			
			Long costingCount = 0L;
			Long preBillingCount = 0L;
			Long costReportCount = 0L;
			Long dmapBill = 0L;
			long sendBilledCount = 0L;
			long totalProcess = 0L;
			long reportTotal = 0;
			if(NodeStatus.isMajorNode){
				long[] count = CostScheduler.getUnconsumeSize();
				costingCount = count[0];
				totalProcess = count[1];
				sendBilledCount = count[2];
				preBillingCount = PreBillingScheduler.getUnconsumeSize();
				long[] reportCount = CostReportScheduler.getUnconsumeSize();
				costReportCount = reportCount[0];
				reportTotal =reportCount[1];
				dmapBill = DmapBillDataScheduler.getUnconsumeSize();
			}
			
			// 发送的结果是: 是不是主节点
			header.append("是否为主节点: "+ NodeStatus.isMajorNode 
					+ "  处理账号余数: "	+ NodeStatus.accountMod
					+ " 取余参数: " + NodeStatus.accountModCount 
					+ " cost队列: " +  costingCount
					+ " preBilling队列: " +  preBillingCount
					+ " costReport队列: " +  costReportCount
					+ " dmapBill队列: " +  dmapBill
					+ " 日算总数: " +  totalProcess
					+ " 日发总数: " +  sendBilledCount
					+ " 报表处理总数: " +  reportTotal
					+ " cache_snapshot:" + cacheManager.getCacheManager().getCache("snapshot").getSize()
					+ " snapshotFiveMin:" + cacheManager.getCacheManager().getCache("snapshotFiveMin").getSize());
			// 将消息头信息发送出去
			out.write(header.toString().getBytes());
			out.close();
			
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

}
