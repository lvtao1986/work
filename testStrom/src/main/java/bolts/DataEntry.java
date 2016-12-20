package bolts;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.minlog.Log;

import bean.Record;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class DataEntry extends BaseRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2177662855518691312L;
	public static Logger logger = org.apache.log4j.Logger.getLogger(DataEntry.class);
	private OutputCollector collector;
	private JedisCluster userJedis1;
	private JedisCluster backwardJedis;
	private Set<HostAndPort> ucNodes1;
	private Set<HostAndPort> bwNodes;
	private JedisCluster userJedis2;
	private Set<HostAndPort> ucNodes2;
	private Jedis jedis;
	private String jcNodes;
	
	public void cleanup(){
		try {
			userJedis1.close();
			backwardJedis.close();
			userJedis2.close();
		} catch (IOException e) {
			Log.error("jedis close error:",e);
		}
	}
	
	public DataEntry(Set<HostAndPort> userClusterNodes1, Set<HostAndPort> userClusterNodes2,Set<HostAndPort> backwardNodes, String userCmNodes){
		ucNodes1 = userClusterNodes1;
		bwNodes = backwardNodes;
		ucNodes2 = userClusterNodes2;
		jcNodes = userCmNodes;
	}
	/**
	 * The bolt will receive the line from the
	 * words file and process it to Normalize this line
	 * 
	 * The normalize will be put the words in lower case
	 * and split the line to get all words in this 
	 */
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
		userJedis1 =  new JedisCluster(ucNodes1);
		backwardJedis =  new JedisCluster(bwNodes);
		userJedis2 =  new JedisCluster(ucNodes2);
		jedis = new Jedis(jcNodes.split(":")[0],Integer.valueOf(jcNodes.split(":")[1]));
		this.collector = collector;
		logger.info("------DataEntry open-----");
	
	}
	public void execute(Tuple input) {
		String judge = input.getStringByField("judge");
		if("direct".equals(judge)){
			Record record = (Record) input.getValueByField("record");
			logger.info("------DataEntry get " + JSON.toJSONString(record.getBean()) + " Entryed -----");
			for(Entry<String, List<String>> entry :record.getMap().entrySet()){
				if(!entry.getValue().isEmpty()){
					backwardJedis.hset(record.getDmpid(), entry.getKey(), StringUtils.join(entry.getValue(), ","));
					backwardJedis.expire(record.getDmpid(), 2592000);
					for(String device : entry.getValue()){
						if(entry.getKey().startsWith("imei")){
							userJedis1.set(device, record.getDmpid());
							userJedis1.expire(device, 2592000);
						}else{
							userJedis2.set(device, record.getDmpid());
							userJedis2.expire(device, 2592000);
						}
					}
					jedis.rpush("mobile_user_result_mq", record.getDmpid() + " " + entry.getKey() + " " + StringUtils.join(entry.getValue(), ","));
				}
			}

			if(input.contains("dmpids") && input.getStringByField("dmpids") != null){
				String[] dmpids = input.getStringByField("dmpids").split(",");
				logger.info("------DataEntry : " + input.getStringByField("dmpids") + " deleted -----");
				for(String dmpid : dmpids){
					backwardJedis.del(dmpid);
				}
			}
    		this.collector.ack(input);
			
		}else if("combine".equals(judge)){
			Record record = (Record) input.getValueByField("record");
			logger.info("------DataEntry combine get " + record.getDmpid() + " Entryed -----");
			for(Entry<String, List<String>> entry :record.getMap().entrySet()){
				if(!entry.getValue().isEmpty()){
					backwardJedis.hset(record.getDmpid(), entry.getKey(), StringUtils.join(entry.getValue(), ","));
					for(String device : entry.getValue()){
						if(entry.getKey().startsWith("imei")){
							userJedis1.set(device, record.getDmpid());
							userJedis1.expire(device, 2592000);
						}else{
							userJedis2.set(device, record.getDmpid());
							userJedis2.expire(device, 2592000);
						}
					}
				}
			}
			//TODO
			logger.info("------DataEntry combine : " + input.getStringByField("dmpids") + " combined -----");
    		this.collector.ack(input);
		}
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
