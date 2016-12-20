package com.emarbox.dsp.monitor.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emarbox.dsp.monitor.dto.ActionType;
import com.emarbox.dsp.monitor.service.RequestLogService;
import com.emarbox.dsp.monitor.util.SupplierInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emar.monitor.handler.TaskManager;
import com.emar.util.ETCCookieUtil;
import com.emarbox.dsp.monitor.data.ClickLogQueue;
import com.emarbox.dsp.monitor.data.DataCache;
import com.emarbox.dsp.monitor.data.ImpressionLogQueue;
import com.emarbox.dsp.monitor.dto.ClickLog;
import com.emarbox.dsp.monitor.dto.ImpressionLog;
import com.emarbox.dsp.monitor.util.IpUtil;
import com.emarbox.dsp.monitor.util.XXTEA;

@Controller("valuemakerMonitorAction")
@RequestMapping(value="/valuemaker")
public class ValueMakerMonitorAction extends BaseMonitorAction {

	private static final Logger log = LoggerFactory.getLogger(ValueMakerMonitorAction.class);
	
	/**
	 * 展现监控接口
	 *
	 * @param info 竞价信息
	 * @param c 成功价格密文
	 * @param guid adx userId
	 * @param sign info校验密文
	 * @param apid 广告位id
	 * @param dcid 动态创意id
	 * @param ct 动态创意扩展参数
	 * @param rt 算法扩展参数
	 * @param rt2 rtb扩展参数
	 * @param bt 流量类型
	 * @param ut 用户类型
	 */
	@RequestMapping(value="/impression")
	public void impressionMonitor(String info, String c,String guid, String sign,String apid,String dcid,String ct,String rt,String rt2,String bt,String ut,String gid, HttpServletRequest request, HttpServletResponse response) {
		try {
			String userId = getCookieUserId(request);// 浏览用户id

			String proxyIp = IpUtil.getProxyIp(request); // 获取代理IP
			String realIp = IpUtil.getRealIp(request); // 获取真实IP
			String agent = request.getHeader("user-Agent"); // 浏览器类型
			String referer = request.getHeader("referer"); // 广告页面url
			if (agent == null) {
				agent = "";
			}
			if (referer == null) {
				referer = "";
			}
			
			ImpressionLog impressionLog = new ImpressionLog();
			impressionLog.setInfo(info);
			impressionLog.setSign(sign);
			impressionLog.setC(c);
			impressionLog.setGuid(guid);
			impressionLog.setApid(apid);
			impressionLog.setActionType(ActionType.IMPRESSION);
			impressionLog.setUserId(userId);
			impressionLog.setProxyIp(proxyIp);
			impressionLog.setRealIp(realIp);
			impressionLog.setAgent(agent);
			impressionLog.setReferer(referer);
			impressionLog.setSupplier(SupplierInfo.VALUEMAKER_ADX);
			impressionLog.setDcid(dcid);
			impressionLog.setCt(ct);
			impressionLog.setRt(rt);
			impressionLog.setRt2(rt2);
			impressionLog.setBt(bt);
			impressionLog.setUt(ut);
			impressionLog.setGid(gid);
			//记录请求日志
			RequestLogService.impressionLog(impressionLog);
			//加入处理队列
			ImpressionLogQueue.addLog(impressionLog);
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
		}
	}

	/**
	 * 点击监控接口
	 * 
	 * @param url
	 *            跳转url
	 * @param info 竞价信息
	 * @param c 成功价格密文
	 * @param guid adx userId
	 * @param sign info校验密文
	 * @param apid 广告位id
	 * @param dcid 动态创意id
	 * @param ct 动态创意扩展参数
	 * @param rt 算法扩展参数
	 * @param rt2 rtb扩展参数
	 * @param bt 流量类型
	 * @param ut 用户类型
	 */
	@RequestMapping(value="/click")
	public String clickMonitor(String url, String c,String guid, String info, String sign,String apid,String dcid,String ct,String rt,String rt2,String bt,String ut, String gid,HttpServletRequest request, HttpServletResponse response) {
		log.info("click bengin,url:" + url);
		String durl = "http://www.emarbox.com";
		String clickId = generateClickId();
		try {
			String userId = getCookieUserId(request);// 浏览用户id

			String proxyIp = IpUtil.getProxyIp(request); // 获取代理IP
			String realIp = IpUtil.getRealIp(request); // 获取真实IP
			String agent = request.getHeader("user-Agent"); // 浏览器类型
			String referer = request.getHeader("referer"); // 广告页面url
			if (agent == null) {
				agent = "";
			}
			if (referer == null) {
				referer = "";
			}

			ClickLog clickLog = new ClickLog();
			clickLog.setInfo(info);
			clickLog.setSign(sign);
			clickLog.setC(c);
			clickLog.setGuid(guid);
			clickLog.setApid(apid);
			clickLog.setActionType(ActionType.CLICK);
			clickLog.setUserId(userId);
			clickLog.setProxyIp(proxyIp);
			clickLog.setRealIp(realIp);
			clickLog.setAgent(agent);
			clickLog.setReferer(referer);
			clickLog.setSupplier(SupplierInfo.VALUEMAKER_ADX);
			clickLog.setDcid(dcid);
			clickLog.setClickId(clickId);
			clickLog.setUrl(url);
			clickLog.setCt(ct);
			clickLog.setRt(rt);
			clickLog.setRt2(rt2);
			clickLog.setBt(bt);
			clickLog.setUt(ut);
			clickLog.setGid(gid);

			//记录请求日志
			RequestLogService.clickLog(clickLog);
			//加入处理队列
			ClickLogQueue.addLog(clickLog);
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
		}
		if (checkUrl(url)) {
			durl = url.trim();
		}
		/**
		 * 跳转时替换媒体占位符
		 */
		String[] args = info.split("_",-1);
		String mediaSite = "";//媒体站点
		int length = args.length;
		if(length >= 17){
			mediaSite = args[16];
		}		
		durl = durl.replace("{media}", XXTEA.encrypt(mediaSite+mediaSeparator+apid));
		
		/**
		 * 跳转时替换用户登录过的domain
		 */
		String userDomain = "";//媒体站点
		if(length >= 18){
			userDomain = args[17];
		}		
		durl = durl.replace("{domain}", userDomain);
		
		/*
		 * 跳转时替换广告平台
		 */
		String supplierCode = "";
		if(length >= 8){
			supplierCode = args[7];
		}
		durl = durl.replace("{supplier}",supplierCode);
		
		/*
		 * 替换活动id和创意id
		 */
		if(length>=2){
			durl = durl.replace("{campaign_id}", args[0]);
			String creativeId = DataCache.getCreativeId(args[1]);
			if(creativeId == null){
				creativeId = "";
			}
			durl = durl.replace("{creative_id}", creativeId);
		}
		durl = durl.replace("{clkid}", clickId);
		
		log.info("go landing page, url:" + durl);
		
		//写etc cookie 开始
		Map<String,String> etcMap = new HashMap<String, String>(); 
		etcMap.put("etc_n","dsp");
		etcMap.put("etc_s", "emar");
		etcMap.put("etc_m",  mediaSite+mediaSeparator+apid);
		etcMap.put("etc_p", userDomain);
		etcMap.put("etc_x",supplierCode);
		etcMap.put("etc_clk", clickId);
		if(length>=2){
		etcMap.put("etc_t",args[1]);
		}
		if(length>=1){
			etcMap.put("etc_c", args[0]);
		}
		int projectId = 0;
		if(length>=4){
			projectId = Integer.parseInt(args[3]);
		}
		ETCCookieUtil.getInstance().setCookie(projectId, etcMap, response);
		//写etc cookie 结束

		String agent = request.getHeader("user-Agent");
		boolean agentStatus=checkUserAgent(agent);
		if(agentStatus){
		    setClickId(request.getCookies(), response, clickId, String.valueOf(projectId));
		}
		
		if(agentStatus){
			if(durl.contains("{mvsid}")){ //酷宝跳转
				log.info("forward");
				return "forward:/forward";
			}else{
				log.info("redirect");
				Map params = new HashMap(); 
				params.putAll(request.getParameterMap());
				params.putAll(etcMap);
				String ip = IpUtil.getIp(request);
				String userId = cookieProcess(request, response);
				TaskManager.addTask(agent, userId, ip, projectId, params, clickId);
				
				return TaskManager.appendClickIdToUrl("redirect:" + durl, clickId);
			}
		}else{
			return null; //agent有问题则不进行跳转
		}
	}

}
