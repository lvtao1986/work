package bolts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.esotericsoftware.minlog.Log;

import util.AndroidIdFilter;
import util.BaiduFilter;
import util.CustomFilter;
import bean.Record;
import redis.clients.jedis.Jedis;

public class DataCleaning extends BaseRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2177662855518691312L;
	public static Logger logger = org.apache.log4j.Logger.getLogger(DataCleaning.class);
	private OutputCollector collector;
	private Jedis jedis;
	private String jcNodes;

	public void cleanup(){
		jedis.close();
	}
	
	public DataCleaning(String userCmNodes){
		jcNodes = userCmNodes;
	}
	/**
	 * The bolt will receive the line from the
	 * words file and process it to Normalize this line
	 * 
	 * The normalize will be put the words in lower case
	 * and split the line to get all words in this 
	 */
	public void prepare(Map config, TopologyContext context, OutputCollector collector){
		jedis = new Jedis(jcNodes.split(":")[0],Integer.valueOf(jcNodes.split(":")[1]));
		this.collector = collector;
		logger.info("------DataCleaning open-----");
	
	}
	public void execute(Tuple input) {
    	String str = input.getStringByField("str");
		logger.debug("------DataCleaning get " + str + "-----");
    	Record record = new Record(str);
    	if(record == null || record.getSupplier() == null){
    		logger.info("------DataCleaning : " + str + " supplier is null-----");
    		this.collector.ack(input);
    		return;
    	}

    	Date now = new Date();
    	int hours = now.getHours();
    	Long isExist = 0l;
    	int before = (hours % 3) - 1;
    	if(before < 0){
    		before = 2;
    	}
    	int after = (hours % 3) + 1;
    	if(after == 3){
    		after = 0;
    	}
    	if(Math.random()<0.000001){
    		try{
		    	String temp;
		    	do{
		    		temp = jedis.spop("recordset" + after);
		    	}while(temp != null && !"null".equals(temp));
		    	do{
		    		temp = jedis.spop("deviceset" + after);
		    	}while(temp != null && !"null".equals(temp));
    		}catch(Exception e){
    			Log.error("------DataCleaning delete set error -----", e);
    		}
    	}

		try{
	    	if(!jedis.sismember("recordset" + before, record.getStr())){
	        	isExist = jedis.sadd("recordset" + (hours % 3), record.getStr());
	    	}
		}catch(Exception e){
			Log.error("------DataCleaning judge set error -----", e);
		}
    	
    	if(isExist.equals(0l)){
    		logger.debug("------DataCleaning : " + str + " existed-----");
    		this.collector.ack(input);
    		return;
    	}

		try{
	    	if(record.getAppid() != null && jedis.sismember("emar-dm-app-blaklist", record.getSupplier())){
	    		logger.info("------DataCleaning : " + str + " in emar-dm-app-blaklist-----");
	    		this.collector.ack(input);
	    		return;
			}
			if(record.getSupplier() != null && jedis.sismember("emar-dm-supplier-blaklist", record.getSupplier())){
	    		logger.info("------DataCleaning : " + str + " in emar-dm-supplier-blaklist-----");
	    		this.collector.ack(input);
	    		return;
			}
			if(!record.getAll().isEmpty()){
				for(Entry<String, List<String>> entry : record.getMap().entrySet()){
					for(String device:entry.getValue()){
						if(jedis.sismember("emar-dm-devide-blaklist-" + entry.getKey(), device)){
							record.setNull(entry.getKey(), device);
				    		logger.info("------DataCleaning : " + str + "'s " + entry.getKey() + ": " + device + " in emar-dm-devide-blaklist-----");
						}
					}
				}
			}
		}catch(Exception e){
			Log.error("------DataCleaning judge blacklist error -----", e);
		}
		if(record.isNull()){
    		logger.debug("------DataCleaning : " + str + " is filted by device blaklist-----");
			this.collector.ack(input);
			return;
		}
    	
    	List<CustomFilter> filterList = new ArrayList<CustomFilter>();
    	filterList.add(new BaiduFilter());
    	filterList.add(new AndroidIdFilter());
    	for(CustomFilter filter : filterList){
    		record = filter.filt(record);
    	}
    	if(record.isNull()){
    		logger.debug("------DataCleaning : " + str + " is filted by custom filter-----");
    		this.collector.ack(input);
    		return;
    	}
    	
		logger.debug("------DataCleaning emit " + str + "-----");
		this.collector.emit(new Values(record));
		this.collector.ack(input);
	}
	

	/**
	 * The bolt will only emit the field "word" 
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("record"));
	}
}
