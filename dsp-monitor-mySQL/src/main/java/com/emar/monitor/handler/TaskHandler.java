package com.emar.monitor.handler;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.monitor.Monitor;

import org.apache.log4j.Logger;

import com.emar.monitor.common.Config;
import com.emar.monitor.param.TaskParam;
import com.emar.monitor.util.StringUtil;

public class TaskHandler {
	
	private static Logger logger = Logger.getLogger(Monitor.class);
	
	/** Map<accountId,Map<date,FileWriter>> **/
	private static Map<String, Map<String, FileWriter>> map = new HashMap<String, Map<String, FileWriter>>();

	/** 可重入的互斥锁 **/
	private static final Lock lock = new ReentrantLock();
	
	/** 数据分割符 **/
	private static final String SEPARATOR = "\t";
	
	private static final SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd.HH");
	
	private static TaskHandler instance = null;
	
	private TaskHandler(){}
	
	public synchronized static TaskHandler getInstance(){
		if(instance == null){
			return new TaskHandler();
		}
		return instance;
	}
	
	
	@SuppressWarnings("rawtypes")
	private FileWriter fileWriterManage(String pid) throws Exception {
		String date = df.format(new Date());		
		String path = Config.getString("pushfile.path") + pid + "/logger";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(path + "/log." + date);
		logger.debug("========= file path:" + path + "========");
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
							logger.warn(e.getMessage());
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
	private void bufferedWrite(String pid, String log)
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
	
	public void process(TaskParam param){
		try{
			Map request = param.getRequest();
			String agent = param.getUserAgent(); // 浏览器类型
			if(agent == null) agent = "";
			agent = agent.trim().toLowerCase();
			if(agent.length() <= 20 || agent.startsWith("wget") || agent.contains("scrapy")){
				logger.warn("======== illegal user-agent info: " + agent + " ========");
				return;
			}
			String info = StringUtil.parse(request.get("info").toString());
			logger.debug("======== info:" + info + " ========");
			String pid = StringUtil.parse(param.getProjectId());
			if("$N".equals(pid)){
				logger.warn("======== no pid were found ========");
				return;
			}
			logger.debug("======== pid:" + pid + " ========");
		
			String clickId = param.getClickId();
			String etc_s = StringUtil.parse(request.get("etc_s"));
			String etc_c = StringUtil.parse(request.get("etc_c"));
			String etc_m = StringUtil.parse(request.get("etc_m"));
			String etc_g = StringUtil.parse(request.get("etc_g"));
			String etc_t = StringUtil.parse(request.get("etc_t"));
			String etc_k = StringUtil.parse(request.get("etc_k"));
			String etc_n = StringUtil.parse(request.get("etc_n"));
			String etc_x = StringUtil.parse(request.get("etc_x"));			
			String etc_p = StringUtil.parse(request.get("etc_p"));
			String ip =  StringUtil.parse(param.getIp());
			
			String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String userId = param.getUserId();		
					
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
			s.append(etc_p).append(SEPARATOR);			//etc参数  dsp专用
			s.append(ip).append(SEPARATOR);				//ip
			s.append(userId).append(SEPARATOR);			//userid
			s.append(ts).append("\n");					//时间戳
			bufferedWrite(pid, s.toString());
		}catch(Exception e){
			logger.warn(e.getMessage(),e );
		}
	}
}

