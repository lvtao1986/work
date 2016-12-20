package com.emar.monitor.handler;

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
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.monitor.Monitor;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emarbox.dsp.monitor.util.IpUtil;
import org.apache.log4j.Logger;

import com.emar.monitor.common.Config;
import com.emar.monitor.util.StringUtil;

public class MonitorForwardHandler extends HttpServlet{
	
	private static Logger logger = Logger.getLogger(Monitor.class);
	
	/** Map<accountId,Map<date,FileWriter>> **/
	public static Map<String, Map<String, FileWriter>> map = new HashMap<String, Map<String, FileWriter>>();

	/** 可重入的互斥锁 **/
	public static final Lock lock = new ReentrantLock();
	
	/** 数据分割符 **/
	public static final String SEPARATOR = "\t";
	
	public static final SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd.HH");
	
	@SuppressWarnings("rawtypes")
	private FileWriter fileWriterManage(String pid) throws Exception {
		String date = df.format(new Date());		
		String path = Config.getString("taobaofile.path") + pid + "/click";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(path + "/log." + date);
		logger.info("========= file path:" + path + "========");
		FileWriter fileWriter;
		Map<String, FileWriter> fm = map.get(pid); // 通过项目ID得到文件指针
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
							logger.error(e.getMessage());
						}
					}
				}
				map.remove(pid); // 从map中移除
				Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
				FileWriter fw = new FileWriter(file, true); // 创建新的FileWriter
				try {
					lock.lock();
					fwm.put(date, fw); // date 是当前日期字符串： 2011-03-23.11 ,fw
										// 是新创建FileWriter
					map.put(pid, fwm);// adwa 是项目ID
					return fw; // 返回新的文件指针
				} finally {
					lock.unlock();
				}
			}
		} else {
			Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
			FileWriter fw = new FileWriter(file, true);
			try {
				lock.lock();
				fwm.put(date, fw);
				map.put(pid, fwm);
				return fw;
			} finally {
				lock.unlock();
			}
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
	
	
	private String generateId(){
		Random random = new Random();
		return String.valueOf(new Date().getTime())
				+ String.valueOf(random.nextInt(1000000000));
	}
	
	private String cookieProcess(HttpServletRequest request,
			HttpServletResponse response){
		String uid = "";
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("_edc")) {
					uid = cookie.getValue();
				}
			}
		}

		if ("".equals(uid.trim())) {
			uid = generateId();
		}
		
		Cookie cookie = new Cookie("_edc", uid);
		cookie.setDomain(".emarbox.com");
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
		response.addCookie(cookie);
		
		return uid;
	}
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			
			String info = StringUtil.parse(request.getParameter("info"));
			logger.debug("======== info:" + info + " ========");
			String[] parts = info.split("_",-1);
			
			String pid = StringUtil.parse(parts[3]);
			logger.debug("======== pid:" + pid + " ========");
			
			String targetUrl = StringUtil.parse(request.getParameter("url"));
			logger.debug("======== targetUrl:" + targetUrl + " ========");
			
			if("$N".equals(pid)){
				if(!"$N".equals(targetUrl)){
					response.sendRedirect(targetUrl);
				}else{
					return;
				}
			}
			
			String clickId = generateId();
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
			
			URL url = new URL(targetUrl);
			if(url.getQuery() != null){
				targetUrl = targetUrl.replace("{mvsid}", clickId);
			}
			
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
			bufferedWrite(request, pid, s.toString());
			response.sendRedirect(targetUrl);
		}catch(Exception e){
			logger.warn(e.getMessage(),e);
			//发生异常直接跳转url
			String targetUrl = StringUtil.parse(request.getParameter("url"));
			response.sendRedirect(targetUrl);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}
}

