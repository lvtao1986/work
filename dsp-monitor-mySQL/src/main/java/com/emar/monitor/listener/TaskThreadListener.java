package com.emar.monitor.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.emar.monitor.handler.TaskManager;

public class TaskThreadListener implements ServletContextListener{
	private static Logger logger = Logger.getLogger(TaskThreadListener.class);
	
	public void contextDestroyed(ServletContextEvent contextEvent) {
		TaskManager.stop();
	}

	public void contextInitialized(ServletContextEvent contextEvent) {
		logger.debug("================== TaskThreadListener start ====================");
		TaskManager.start();
	}
	
}
