package com.emarbox.dsp.monitor.action;

import com.emar.monitor.common.Config;
import com.emar.monitor.handler.MonitorForwardHandler;
import com.emar.monitor.handler.TaskManager;
import com.emar.monitor.util.StringUtil;
import com.emar.util.ETCCookieUtil;
import com.emarbox.dsp.monitor.data.ClickLogQueue;
import com.emarbox.dsp.monitor.data.DataCache;
import com.emarbox.dsp.monitor.dto.ActionType;
import com.emarbox.dsp.monitor.dto.ClickLog;
import com.emarbox.dsp.monitor.service.RequestLogService;
import com.emarbox.dsp.monitor.util.IpUtil;
import com.emarbox.dsp.monitor.util.XXTEA;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller("directMonitorAction")
@RequestMapping(value="/dsp")
public class DirectMonitorAction extends BaseMonitorAction {

	private static final Logger log = LoggerFactory.getLogger(DirectMonitorAction.class);
	
	/**
	 * 点击监控接口
	 * 
	 * @param url
	 *            跳转url
	 * @param info
	 *            竞价信息
	 * @param sign
	 *            信息签名
	 */
	@RequestMapping(value="/click")
	@ResponseBody
	public String clickMonitor(String url, String c,String guid, String info, String sign,String apid,String dcid,String gid, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			clickLog.setInfo(info);// zhaojinglei 20130716
			clickLog.setSign(sign);// zhaojinglei 20130716
			clickLog.setC(c);
			clickLog.setGuid(guid);
			clickLog.setApid(apid);
			clickLog.setActionType(ActionType.CLICK);
			clickLog.setUserId(userId);
			clickLog.setProxyIp(proxyIp);
			clickLog.setRealIp(realIp);
			clickLog.setAgent(agent);
			clickLog.setReferer(referer);
			clickLog.setSupplier(1001);
			clickLog.setDcid(dcid);
			clickLog.setClickId(clickId);
			clickLog.setUrl(url);
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
		
		log.info("go landing page, url:" + durl);
		
		//写etc cookie 开始
		Map<String,String> etcMap = new HashMap<String, String>(); 
		etcMap.put("etc_n","dsp");
		etcMap.put("etc_s", "emar");
		etcMap.put("etc_m",  mediaSite+mediaSeparator+apid);
		etcMap.put("etc_p", userDomain);
		etcMap.put("etc_x",supplierCode);
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
				String targetUrl = url;
				if( new URL(url).getQuery() != null){
					targetUrl = targetUrl.replace("{mvsid}", clickId);
				}
				String[] parts = info.split("_",-1);
				String etc_s = "emar";
				String etc_c = StringUtil.parse(parts[0]);
				String etc_m = StringUtil.parse((parts[16].indexOf("X") > 0) ? parts[16].split("X")[0] : parts[16]);
				String etc_g = StringUtil.parse(request.getParameter("etc_g"));
				String etc_t = StringUtil.parse(parts[1]);
				String etc_k = StringUtil.parse(request.getParameter("etc_k"));
				String etc_n = "dsp";
				String etc_x = StringUtil.parse(parts[7]);
				String etc_p = StringUtil.parse(request.getParameter("etc_p"));
			
				String ip = IpUtil.getIp(request);			
				String referer = StringUtil.parse(request.getHeader("Referer"));
				String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				String userId = cookieProcess(request, response);
				
				/** 数据分割符 **/
				String SEPARATOR = "\t";
				
				StringBuffer s = new StringBuffer();
				s.append(clickId).append(SEPARATOR);   		//clickid
				s.append(etc_s).append(SEPARATOR);			//etc参数
				s.append(etc_c).append(SEPARATOR);			//etc参数
				s.append(etc_m).append(SEPARATOR);			//etc参数
				s.append(etc_g).append(SEPARATOR);			//etc参数
				s.append(etc_t).append(SEPARATOR);			//etc参数
				s.append(etc_k).append(SEPARATOR);			//etc参数
				s.append(etc_n).append(SEPARATOR);			//etc参数
				s.append(etc_x).append(SEPARATOR);			//etc参数
				s.append(etc_p).append(SEPARATOR);			//etc参数
				s.append(ip).append(SEPARATOR);				//ip
				s.append(referer).append(SEPARATOR);		//referer
				s.append(targetUrl).append(SEPARATOR);		//跳转url
				s.append(userId).append(SEPARATOR);			//userid
				s.append(ts).append("\n");					//时间戳
				String pid = StringUtil.parse(parts[3]);
				bufferedWrite(request, pid, s.toString());
				
				return genContent(targetUrl);
			}else{
				log.info("redirect");
				Map params = new HashMap(); 
				params.putAll(request.getParameterMap());
				params.putAll(etcMap);
				String ip = IpUtil.getIp(request);
				String userId = cookieProcess(request, response);
				TaskManager.addTask(agent, userId, ip, projectId, params, clickId);
				
				return genContent(TaskManager.appendClickIdToUrl(durl, clickId));
			}
		}else{
			return null; //agent有问题则不进行跳转
		}
	}
	/**
	 * 将log写入文件
	 * 
	 * @param request
	 * @param log
	 * @throws IOException
	 */
	private void bufferedWrite(HttpServletRequest request, String pid, String log)
			throws Exception {
		FileWriter fw = fileWriterManage(pid);
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(fw);
			bw.write(log);
		}finally{
			bw.flush();
		}
	}
	@SuppressWarnings("rawtypes")
	private FileWriter fileWriterManage(String pid) throws Exception {
		String date = MonitorForwardHandler.df.format(new Date());		
		String path = Config.getString("taobaofile.path") + pid + "/click";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(path + "/log." + date);
		FileWriter fileWriter;
		Map<String, FileWriter> fm = MonitorForwardHandler.map.get(pid); // 通过项目ID得到文件指针
		if (null != fm) {
			// 如果map中存放的文件指针是当前时间(小时)的则返回该文件的指针，否则，关闭当前小时之前的文件流，重新创建新的文件指针
			if (fm.containsKey(date)) {
				return fm.get(date);
			} else {
				Iterator<?> iter = fm.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					fileWriter = (FileWriter) entry.getValue();
					if (null != fileWriter) {
						try {
							fileWriter.close(); // 关闭之前的filewriter
						} catch (Exception e) {
							log.error(e.getMessage(),e);
						}
					}
				}
				MonitorForwardHandler.map.remove(pid); // 从map中移除
				Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
				FileWriter fw = new FileWriter(file, true); // 创建新的FileWriter
				try {
					MonitorForwardHandler.lock.lock();
					fwm.put(date, fw); // date 是当前日期字符串： 2011-03-23.11 ,fw
										// 是新创建FileWriter
					MonitorForwardHandler.map.put(pid, fwm);// adwa 是项目ID
					return fw; // 返回新的文件指针
				} finally {
					MonitorForwardHandler.lock.unlock();
				}
			}
		} else {
			Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
			FileWriter fw = new FileWriter(file, true);
			try {
				MonitorForwardHandler.lock.lock();
				fwm.put(date, fw);
				MonitorForwardHandler.map.put(pid, fwm);
				return fw;
			} finally {
				MonitorForwardHandler.lock.unlock();
			}
		}
	}
	private String genContent(String url){
		String transferUrl ="<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+
		"<html>"+
		"<head>"+
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">"+
		"<title></title>"+
		"<script type=\"text/javascript\">"+
		"window.onload=function(){" +
		"   var u='"+url+"';"+
		" if(document.all){  "+
		"     var l = document.createElement('a');   "+
		"     l.href = u;   "+
		"     document.body.appendChild(l);   "+
		"     l.click();   "+
		" }else{"+
		" window.location.href = u;"+
		" }"+
		"}"+
		"</script>"+
		"<noscript><meta http-equiv=\"refresh\" content=\"0;url="+url+"\"></noscript>"+
		"</head>"+
		"<body></body>"+
		"</html>";
		return transferUrl;
	}
	
}
