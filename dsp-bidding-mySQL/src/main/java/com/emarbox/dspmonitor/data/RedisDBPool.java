package com.emarbox.dspmonitor.data;

import app.base.util.ConfigUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDBPool {

	static{
	   ConfigUtil.addConfig("redis");
	}
	
	private static final String REDIS_IP = ConfigUtil.getString("redis.ip");
	private static final int REDIS_PORT = NumberUtils.toInt(ConfigUtil.getString("redis.port"), 6379);
	public static final int TIMEOUT = NumberUtils.toInt(ConfigUtil.getString("redis.connect_time_out"), 5000);
	
	private static JedisPool pool = null;
	private static JedisPoolConfig config = new JedisPoolConfig();
	
	private static final Logger log = LoggerFactory.getLogger(RedisDBPool.class);
	
	static{
		 //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		config.setMaxActive(NumberUtils.toInt(ConfigUtil.getString("redis.max_active"), 1000));
		 //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
		config.setMaxIdle(NumberUtils.toInt(ConfigUtil.getString("redis.max_idle"), 10));
		 //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
		config.setMaxWait(NumberUtils.toInt(ConfigUtil.getString("redis.max_wait"), 20));
  		 //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		config.setTestOnBorrow(true);
	}

	private RedisDBPool(){}
	
	public synchronized static JedisPool newInstance(){
		if(pool == null){
			pool = new JedisPool(config, REDIS_IP, REDIS_PORT, TIMEOUT);
		}
		return pool;
	}
	
}
