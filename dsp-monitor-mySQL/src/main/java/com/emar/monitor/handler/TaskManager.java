package com.emar.monitor.handler;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.emar.monitor.param.TaskParam;

public class TaskManager {

	private static Logger logger = Logger.getLogger(TaskManager.class);

	private static BlockingQueue<TaskParam> queue = new ArrayBlockingQueue<TaskParam>(5000);

	private static ExecutorService exec = Executors.newFixedThreadPool(20);

	public synchronized static void start(){
		try{
			for(int i = 0; i < 20; i++){
				exec.submit(new Task());
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}
	public synchronized static String appendClickIdToUrl(String url, String clickId){
		if(url != null && url.indexOf("{clickid}") != -1){
			url = url.replace("{clickid}", clickId);
		}
		if(url != null && url.indexOf("{clkid}") != -1){
			url = url.replace("{clkid}", clickId);
		}
		return url;
	}

	public synchronized static void stop(){
		try{
			exec.shutdownNow();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}

	private static class Task implements Runnable{
		public void run() {
			try{
				while(true){
					TaskParam param = queue.take();
					TaskHandler.getInstance().process(param);
				}
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
		}
	}

	public synchronized static void addTask(String userAgent, String userId, String ip, long projectId, Map request, String clickId){
		try{
			TaskParam param = new TaskParam(userAgent, userId, ip, projectId, request, clickId);
			boolean status = queue.offer(param);
			if(status == false){
				logger.warn("failed to insert element!");
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}
}
