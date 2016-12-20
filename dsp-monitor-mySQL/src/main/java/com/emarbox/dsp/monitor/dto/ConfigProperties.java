package com.emarbox.dsp.monitor.dto;


import com.emar.monitor.common.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ConfigProperties {
	private static final Logger log = LoggerFactory.getLogger(ConfigProperties.class);
	private static String enviorment = null;
	
	public static int getAgentLengthMin() {
		return ConfigUtil.getConfiguration().getInt("agent.length.min.limit");
	}
	public static String getAgentStartswith() {
		return ConfigUtil.getConfiguration().getString("agent.startswith");
	}
	public static String[] getAgentContains() {
		return ConfigUtil.getConfiguration().getStringArray("agent.contains");
	}
	public static int getBiTimeMin() {
		return  ConfigUtil.getConfiguration().getInt("bidding.impression.time.min");
	}
	public static int getCbTimeMin() {
		return ConfigUtil.getConfiguration().getInt("click.bidding.time.min");
	}
	public static int getCbTimeMax() {
		return ConfigUtil.getConfiguration().getInt("click.bidding.time.max");
	}
	public static int getImpressRepeatTime() {
		return ConfigUtil.getConfiguration().getInt("impression.judge.requestid.repeat.time");
	}
	public static int getImpressClickfitTime() {
		return ConfigUtil.getConfiguration().getInt("impression.click.judge.requestid.fit.time");
	}
	public static int getClickRepeatRime() {
		return ConfigUtil.getConfiguration().getInt("click.judge.requestid.repeat.time");
	}
	public static String getEnviorment(){
		if(enviorment == null){
			ReentrantLock lock = new ReentrantLock();
			lock.lock();
			try{
				enviorment = ConfigUtil.addConfig("db_app").getString("db.config.app");
				if (enviorment == null) {
					enviorment = "prd";
				}
			}catch (Exception e){
				log.error(e.getMessage(),e);
			}finally {
				lock.unlock();
			}
		}
		return enviorment;
	}


}
