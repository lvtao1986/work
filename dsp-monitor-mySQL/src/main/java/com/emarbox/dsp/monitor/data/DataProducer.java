package com.emarbox.dsp.monitor.data;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import com.emar.monitor.common.ConfigUtil;

public class DataProducer {
	
	private static Properties props = new Properties();
	private static ProducerConfig config = null;
	private static Producer<String, String> producer;
	
	private DataProducer(){}
	
	public synchronized static Producer<String, String> getProducer(String configFileName){
		if(null == producer){
			
			ConfigUtil.addConfig(configFileName);			
	        props.put("metadata.broker.list", ConfigUtil.getString("metadata.broker.list"));
	        props.put("serializer.class", ConfigUtil.getString("serializer.class"));
	        props.put("partitioner.class", "com.emarbox.dsp.monitor.data.SimplePartitioner");
	        props.put("request.required.acks", ConfigUtil.getString("request.required.acks"));
	       
	        config = new ProducerConfig(props);
			
			producer = new Producer<String, String>(config);
		}
		return producer; 
	}
}
