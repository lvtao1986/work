package com.emarbox.dsp.monitor.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emarbox.dsp.monitor.dto.ActionType;
import com.emarbox.dsp.monitor.service.RequestLogService;
import com.emarbox.dsp.monitor.util.SupplierInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import com.emarbox.dsp.monitor.util.decrypt.BaiduDecrypter;
import com.emarbox.dsp.monitor.util.IpUtil;
import com.emarbox.dsp.monitor.util.XXTEA;

@Controller("baiduMonitorAction")
@RequestMapping(value = "/baidu")
public class BaiduMonitorAction extends BaseMonitorAction {

	private static final Logger log = LoggerFactory.getLogger(BaiduMonitorAction.class);

	/**
	 * 展现监控接口
	 * 
	 * @param info
	 *            竞价信息
	 * @param c 成功价格密文
	 *
	 */
	@RequestMapping(value = "/impression")
	public void impressionMonitor(String info, String c, String ct,String gid, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> infoMap = getInfoMap(info);
			String infoStr = infoMap.get("info");//内容
			String sign = infoMap.get("sign");
			String guid = infoMap.get("guid");
			String apid = infoMap.get("apid");
			String rt = infoMap.get("rt");
			String rt2 = infoMap.get("rt2");
			String bt = infoMap.get("bt");
			String ut = infoMap.get("ut");

			String campaignId = infoMap.get("campaignId"); // 计划id
			String creativeId = infoMap.get("creativeId"); // 创意id
			String adUserId = infoMap.get("adUserId"); // 广告主id
			String projectId = infoMap.get("projectId"); // 项目id
			String biddingId = infoMap.get("biddingId");// 竞价id
			String time = infoMap.get("time");// 竞价时间
			String adChannelId = infoMap.get("adChannelId"); // 广告平台标示
			String costType = infoMap.get("costType");// 广告计费类型
			String biddingRtbPrice = infoMap.get("biddingRtbPrice"); // RTB价格
			String valuePrice = infoMap.get("valuePrice"); // 用户价值
			String ctr = infoMap.get("ctr"); // ctr
			String division = infoMap.get("division");// 利差分成
			String biddingCost = infoMap.get("biddingCost"); // 收取广告主价格
			String commRate = infoMap.get("commRate"); // 佣金比例
			String taxRate = infoMap.get("taxRate"); // 税点比例
			String mediaSite = infoMap.get("mediaSite");
			String userDomains = infoMap.get("userDomains");// 用户访问过的域名
			String userPrice = infoMap.get("userPrice");// 用户估值

			String biddingPrice = BaiduDecrypter.decode(c);


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
			impressionLog.setInfoStr(infoStr);
			impressionLog.setSign(sign);
			impressionLog.setC(biddingPrice);
			impressionLog.setGuid(guid);
			impressionLog.setApid(apid);
			impressionLog.setCampaignId(campaignId);
			impressionLog.setCreativeId(creativeId);
			impressionLog.setAdUserId(adUserId);
			impressionLog.setProjectId(projectId);
			impressionLog.setBiddingId(biddingId);
			impressionLog.setTime(time);
			impressionLog.setAdChannelId(adChannelId);
			impressionLog.setCostType(costType);
			impressionLog.setBiddingRtbPrice(biddingRtbPrice);
			impressionLog.setValuePrice(valuePrice);
			impressionLog.setCtr(ctr);
			impressionLog.setDivision(division);
			impressionLog.setBiddingCost(biddingCost);
			impressionLog.setCommRate(commRate);
			impressionLog.setTaxRate(taxRate);
			impressionLog.setMediaSite(mediaSite);
			impressionLog.setUserDomains(userDomains);
			impressionLog.setActionType(ActionType.IMPRESSION);
			impressionLog.setUserId(userId);
			impressionLog.setProxyIp(proxyIp);
			impressionLog.setRealIp(realIp);
			impressionLog.setAgent(agent);
			impressionLog.setReferer(referer);
			impressionLog.setSupplier(SupplierInfo.BAIDU_ADX);
			impressionLog.setCt(ct);
			impressionLog.setRt(rt);
			impressionLog.setRt2(rt2);
			impressionLog.setBt(bt);
			impressionLog.setUt(ut);
			impressionLog.setGid(gid);
			impressionLog.setUserPrice(userPrice);
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
	 * @param info
	 *            竞价信息
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/click")
	public String clickMonitor(String url, String info,String ct,String gid, HttpServletRequest request, HttpServletResponse response) {
		String durl = "http://www.emarbox.com";

		String mediaSite = "";
		String adChannelId = "";
		String userDomains = "";
		String projectId = "";
		String apid = "";
		String campaignId = "";
		String creativeId = "";
		String userPrice = "";
		String clickId = generateClickId();
		
		try {

			Map<String, String> infoMap = getInfoMap(info);
			String infoStr = infoMap.get("info");
			String sign = infoMap.get("sign");
			String guid = infoMap.get("guid");
			apid = infoMap.get("apid");
			String rt = infoMap.get("rt");
			String rt2 = infoMap.get("rt2");
			String bt = infoMap.get("bt");
			String ut = infoMap.get("ut");

			campaignId = infoMap.get("campaignId"); // 计划id
			creativeId = infoMap.get("creativeId"); // 创意id
			String adUserId = infoMap.get("adUserId"); // 广告主id
			projectId = infoMap.get("projectId"); // 项目id
			String biddingId = infoMap.get("biddingId");// 竞价id
			String time = infoMap.get("time");// 竞价时间
			adChannelId = infoMap.get("adChannelId"); // 广告平台标示
			String costType = infoMap.get("costType");// 广告计费类型
			String biddingRtbPrice = infoMap.get("biddingRtbPrice"); // RTB价格
			String valuePrice = infoMap.get("valuePrice"); // 用户价值
			String ctr = infoMap.get("ctr"); // ctr
			String division = infoMap.get("division");// 利差分成
			String biddingCost = infoMap.get("biddingCost"); // 收取广告主价格
			String commRate = infoMap.get("commRate"); // 佣金比例
			String taxRate = infoMap.get("taxRate"); // 税点比例
			mediaSite = infoMap.get("mediaSite");
			userDomains = infoMap.get("userDomains");// 用户访问过的域名
			userPrice = infoMap.get("userPrice");// 用户估值

			String biddingPrice = "0";// 腾讯的点击价格暂时没有

			// if (validate) {
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
			clickLog.setInfo(infoStr);// zhaojinglei 20130716
			clickLog.setSign(sign);// zhaojinglei 20130716
			clickLog.setC(biddingPrice);
			clickLog.setGuid(guid);
			clickLog.setApid(apid);
			clickLog.setCampaignId(campaignId);
			clickLog.setCreativeId(creativeId);
			clickLog.setAdUserId(adUserId);
			clickLog.setProjectId(projectId);
			clickLog.setBiddingId(biddingId);
			clickLog.setTime(time);
			clickLog.setSupplier(Integer.parseInt(adChannelId));
			clickLog.setAdChannelId(adChannelId);
			clickLog.setCostType(costType);
			clickLog.setBiddingRtbPrice(biddingRtbPrice);
			clickLog.setValuePrice(valuePrice);
			clickLog.setCtr(ctr);
			clickLog.setDivision(division);
			clickLog.setBiddingCost(biddingCost);
			clickLog.setCommRate(commRate);
			clickLog.setTaxRate(taxRate);
			clickLog.setMediaSite(mediaSite);
			clickLog.setUserDomains(userDomains);
			clickLog.setActionType(ActionType.CLICK);
			clickLog.setUserId(userId);
			clickLog.setProxyIp(proxyIp);
			clickLog.setRealIp(realIp);
			clickLog.setAgent(agent);
			clickLog.setReferer(referer);
			clickLog.setClickId(clickId);
			clickLog.setUrl(url);
			clickLog.setCt(ct);
			clickLog.setRt(rt);
			clickLog.setRt2(rt2);
			clickLog.setBt(bt);
			clickLog.setUt(ut);
			clickLog.setGid(gid);
			clickLog.setUserPrice(userPrice);
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
		durl = durl.replace("{media}", XXTEA.encrypt(mediaSite+mediaSeparator+apid));

		/**
		 * 跳转时替换用户登录过的domain
		 */
		durl = durl.replace("{domain}", userDomains);

		/*
		 * 跳转时替换广告平台
		 */
		durl = durl.replace("{supplier}", adChannelId);
		/*
		 * 替换活动id和创意id
		 */
		durl = durl.replace("{campaign_id}", campaignId);
		String cid = DataCache.getCreativeId(creativeId);
		if(cid == null){
			cid = "";
		}
		durl = durl.replace("{creative_id}", cid);
		
		durl = durl.replace("{clkid}", clickId);
		
		log.info("go landing page, url:" + durl);
		
		
		//写etc cookie 开始
		Map<String,String> etcMap = new HashMap<String, String>(); 
		etcMap.put("etc_n","dsp");
		etcMap.put("etc_s", "emar");
		etcMap.put("etc_m",  mediaSite+mediaSeparator+apid);
		etcMap.put("etc_p", userDomains);
		etcMap.put("etc_x",adChannelId);
		etcMap.put("etc_t",creativeId);
		etcMap.put("etc_c", campaignId);
		etcMap.put("etc_clk", clickId);
		String agent = request.getHeader("user-Agent");
		boolean agentStatus = checkUserAgent(agent);
		if(NumberUtils.isNumber(projectId)){
			ETCCookieUtil.getInstance().setCookie(Long.parseLong(projectId), etcMap, response);
			if(agentStatus){
			    setClickId(request.getCookies(), response, clickId, projectId);
			}
		}
		//写etc cookie 结束
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
				if(NumberUtils.isNumber(projectId)){
					TaskManager.addTask(agent, userId, ip, Long.parseLong(projectId), params, clickId);
				}
				return TaskManager.appendClickIdToUrl("redirect:" + durl, clickId);
			}
		}else{
			return null; //agent不正常则不进行跳转
		}
	}

	public static Map<String, String> getInfoMap(String info) {
		Map<String, String> infoMap = new HashMap<String, String>();
		if (StringUtils.isEmpty(info)) {
			return null;
		}
		// 将info按照标识符进行拆分
		String[] str = info.split("_", -1);
		String infoStr = "";

		String campaignId = str[0]; // 计划id
		String creativeId = str[1]; // 创意id
		String adUserId = str[2]; // 广告主id
		String projectId = str[3]; // 项目id
		String biddingId = str[4]; // 竞价id

		String time = str[5]; // 竞价时间
		String adChannelId = str[6]; // 广告平台标示
		String costType = str[7]; // 广告计费类型
		String biddingRtbPrice = str[8]; // RTB价格
		String valuePrice = str[9]; // 用户价值
		String ctr = str[10]; // ctr
		String division = str[11];// 利差分成

		String biddingCost = ""; // 收取广告主价格
		String commRate = ""; // 佣金比例
		String taxRate = ""; // 税点比例

		String mediaSite = str[13];// 媒体站点

		String userDomains = str[14];// 用户浏览站点id

		String sign = "";
		String guid = str[15];// 竞价的userId
		String apid = str[12];// 广告位ID
		
		String ct = ""; //动态创意CT
		String rt = ""; //算法扩展参数
		String rt2 = "";//rtb扩展参数
		String bt = "";//流量类型
		String ut = "";//用户类型
		String userPrice = "";//用户估值
		int length =str.length;
		if(length >= 18){
			ct = str[16]; //动态创意CT
			rt = str[17]; //算法扩展参数
		}
		if(length >= 19){
			rt2 = str[18];//rtb扩展参数
		}
		if(length >= 20){
			userPrice = str[19];//用户价值
		}
		if(length >= 21){
			bt = str[20];//流量类型
		}
		if(length >= 22){
			ut = str[21];//用户类型
		}
		
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				sign = str[i];
			} else {
				infoStr += str[i] + "_";
			}
		}
		infoStr = infoStr.substring(0, infoStr.length() - 1);

		infoMap.put("info", infoStr);
		infoMap.put("sign", sign);
		infoMap.put("campaignId", campaignId);
		infoMap.put("creativeId", creativeId);
		infoMap.put("adUserId", adUserId);
		infoMap.put("projectId", projectId);
		infoMap.put("biddingId", biddingId);
		infoMap.put("time", time);
		infoMap.put("adChannelId", adChannelId);
		infoMap.put("costType", costType);
		infoMap.put("biddingRtbPrice", biddingRtbPrice);
		infoMap.put("valuePrice", valuePrice);
		infoMap.put("ctr", ctr);
		infoMap.put("division", division);
		infoMap.put("biddingCost", biddingCost);
		infoMap.put("commRate", commRate);
		infoMap.put("taxRate", taxRate);
		infoMap.put("mediaSite", mediaSite);
		infoMap.put("userDomains", userDomains);

		infoMap.put("guid", guid);
		infoMap.put("apid", apid);
		
		infoMap.put("ct", ct);
		infoMap.put("rt", rt);
		infoMap.put("rt2", rt2);
		infoMap.put("userPrice", userPrice);
		infoMap.put("bt", bt);
		infoMap.put("ut", ut);
		return infoMap;

	}

}
