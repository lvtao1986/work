package bolts;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.minlog.Log;

import bean.Record;
import util.Helper;
import util.RecordComparator;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class DataPhrase extends BaseRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2177662855518691312L;
	public static Logger logger = org.apache.log4j.Logger.getLogger(DataPhrase.class);
	private OutputCollector collector;
	private JedisCluster backwardJedis;
	private JedisCluster userJedis1;
	private JedisCluster userJedis2;
	private JedisCluster userJedisOld;
	private Jedis blackBeijingJedis;
	private Jedis blackHangzhouJedis;
	private Jedis userCmJedis;
	private Set<HostAndPort> jcNodes;
	private Set<HostAndPort> ucNodes1;
	private Set<HostAndPort> ucNodes2;
	private Set<HostAndPort> ucNodesOld;
	private String bkbNodes;
	private String bkhNodes;
	private String userCmNodes;

	public void cleanup(){
		try {
			backwardJedis.close();
			userJedis1.close();
			userJedis2.close();
			userJedisOld.close();
			blackBeijingJedis.close();
			blackHangzhouJedis.close();
		} catch (IOException e) {
			Log.error("jedis close error:",e);
		}
	}
	
	public DataPhrase(Set<HostAndPort> backwardNodes,Set<HostAndPort> userClusterNodes1,Set<HostAndPort> userClusterNodes2,Set<HostAndPort> userClusterNodesOld,
			String blackBeijingClusterNodes, String blackHangzhouClusterNodes, String userCmNode){
		jcNodes = backwardNodes;
		ucNodes1 = userClusterNodes1;
		ucNodes2 = userClusterNodes2;
		ucNodesOld = userClusterNodesOld;
		bkbNodes = blackBeijingClusterNodes;
		bkhNodes = blackHangzhouClusterNodes;
		userCmNodes = userCmNode;
	}
	/**
	 * The bolt will receive the line from the
	 * words file and process it to Normalize this line
	 * 
	 * The normalize will be put the words in lower case
	 * and split the line to get all words in this 
	 */
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
		backwardJedis = new JedisCluster(jcNodes);
		userJedis1 =  new JedisCluster(ucNodes1);
		userJedis2 =  new JedisCluster(ucNodes2);
		userJedisOld =  new JedisCluster(ucNodesOld);
		blackBeijingJedis =  new Jedis(bkbNodes.split(":")[0],Integer.valueOf(bkbNodes.split(":")[1]));
		blackHangzhouJedis =  new Jedis(bkhNodes.split(":")[0],Integer.valueOf(bkhNodes.split(":")[1]));
		userCmJedis =  new Jedis(userCmNodes.split(":")[0],Integer.valueOf(userCmNodes.split(":")[1]));
		this.collector = collector;
		logger.info("------DataPhrase open-----");
	
	}
	public void execute(Tuple input) {
		Record record = (Record) input.getValueByField("record");
		logger.debug("------DataPhrase get " + record.getStr() + "-----");
		Map<String,List<String>> allDevice = record.getMap();
		Map<String,List<String>> dmpids = new HashMap<String, List<String>>();
		int dmpidCount = 0;
		List<String> Alllist = new ArrayList<String>();
		for(Entry<String, List<String>> deviceSet : allDevice.entrySet()){
			List<String> list = new ArrayList<String>();
			for(String device : deviceSet.getValue()){
				String dmpid;
				if(deviceSet.getKey().startsWith("imei")){
					dmpid = userJedis1.get(device);
				}else{
					dmpid = userJedis2.get(device);
				}
				if(dmpid == null){
					try{
					    	Date now = new Date();
					    	int hours = now.getHours();
					    	int before = (hours % 3) - 1;
					    	if(before < 0){
					    		before = 2;
					    	}
					    	if(!userCmJedis.sismember("deviceset" + before, device) && !userCmJedis.sismember("deviceset" + (hours % 3), device)){
					        	userCmJedis.sadd("deviceset" + (hours % 3), device);
					    	}else{
					    		logger.info("------DataPhrase : " + device + " existed-----");
					    		this.collector.ack(input);
					    		return;
					    	}
		    		}catch(Exception e){
		    			Log.error("------DataPhrase judge set error -----", e);
		    		}
					continue;
				}
				if(dmpid.contains(",")){
					dmpid = dmpid.split(",")[0];
				}
				if(!Alllist.contains(dmpid)){
					Alllist.add(dmpid);
					list.add(dmpid);
					logger.debug("------DataPhrase : " + record.getStr() + " " + deviceSet.getKey() + " a existed dmpid " + dmpid + "-----");
				}
				dmpidCount ++;
			}
			dmpids.put(deviceSet.getKey(), list);
		}
		
		if(dmpidCount == 0){
			String uuid =genDmpid(record);
			record.setDmpid(uuid);
			logger.debug("------DataPhrase : " + record.getStr() + " no existed dmpid and gen a new dmpid " + uuid + "-----");
			collector.emit(new Values("direct", record, null));
			return;
		}

		List<Record> newRecords = new ArrayList<Record>();
		List<String> tmp = new ArrayList<String>();
		for(Entry<String, List<String>> dmpidSet : dmpids.entrySet()){
			for(String dmpid : dmpidSet.getValue()){
				if(tmp.contains(dmpid)){
					continue;
				}
				tmp.add(dmpid);
				Map<String,String> map = backwardJedis.hgetAll(dmpid);
				Record newRecord = new Record(map);
				newRecord.setDmpid(dmpid);
				int needToDeal = 1;
				for(Entry<String, List<String>> entry : newRecord.getMap().entrySet()){
					Set<String> set = new HashSet<String>();
					set.addAll(entry.getValue());
					set.addAll(allDevice.get(entry.getKey()));
					int count = set.size();
					if(entry.getKey().endsWith("_md5") || entry.getKey().endsWith("_sha1")){
						if(count > 5){
							logger.info("------ At " + entry.getKey() + " DataPhrase : " + record.getStr() + " and " + JSON.toJSONString(newRecord.getBean()) + " has too many devices-----");
							dealDeviceTooMany(newRecord, record, entry.getKey());
						}
						if(count > 2 && entry.getKey().equals(dmpidSet.getKey())){
							logger.debug("------ At " + entry.getKey() + " DataPhrase : " + record.getStr() + " and " + JSON.toJSONString(newRecord.getBean()) + " no need to combine-----");
							needToDeal = 0;
						}
					}else{
						if(count > 3){
							logger.info("------ At " + entry.getKey() + " DataPhrase : " + record.getStr() + " and " + JSON.toJSONString(newRecord.getBean()) + " has too many devices-----");
							dealDeviceTooMany(newRecord, record, entry.getKey());
						}
						if(count > 1 && entry.getKey().equals(dmpidSet.getKey())){
							logger.debug("------ At " + entry.getKey() + " DataPhrase : " + record.getStr() + " and " + JSON.toJSONString(newRecord.getBean()) + " no need to combine-----");
							needToDeal = 0;
						}
					}
				}
				if(!newRecords.contains(newRecord) && needToDeal == 1){
					newRecords.add(newRecord);
				}
			}
		}
		
		if(newRecords.isEmpty()){
			String uuid = genDmpid(record);
			record.setDmpid(uuid);
			logger.debug("------DataPhrase : " + record.getStr() + " no existed dmpid and gen a new dmpid " + uuid + "-----");
			collector.emit(new Values("direct", record, null));
			return;
		}
		
		if(newRecords.size() == 1){
			logger.debug("------DataPhrase : " + record.getStr() + " and " + JSON.toJSONString(newRecords.get(0).getBean()) + " simply combine-----");
			collector.emit(new Values("direct", newRecords.get(0).addRecord(record), null));
			return;
		}
		
		Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.MINUTE, -5);
		Collections.sort(newRecords, new RecordComparator());
		if(newRecords.get(0).getDate().after(beforeTime.getTime())
				||(newRecords.get(0).getDate().before(beforeTime.getTime()) && newRecords.get(1).getDate().after(beforeTime.getTime()))){
			Record result = newRecords.get(0);
			for(int i = 1; i < newRecords.size(); i++){
				result = result.addRecord(newRecords.get(i));
			}
			List<String> list = new ArrayList<String>();
			for(Record r : newRecords.subList(1, newRecords.size())){
				list.add(r.getDmpid());
			}
			logger.debug("------DataPhrase : " + record.getStr() + " and " + JSON.toJSONString(newRecords.get(0).getBean()) + " simply combine; delete " + StringUtils.join(list, ",") + "  -----");
			collector.emit(new Values("direct", result.addRecord(record), StringUtils.join(list, ",")));
			return;
		}else{
			String uuid = genDmpid(record);
			record.setDmpid(uuid);
			List<String> list = new ArrayList<String>();
			for(Record r : newRecords){
				list.add(r.getDmpid());
			}
			logger.debug("------DataPhrase : " + StringUtils.join(list, ",") + " comflex combine-----");
			collector.emit(new Values("combine", record, record.getSupplier() + ":" + StringUtils.join(list, ",")));
		}
		
	}
	
	private String genDmpid(Record record){
		List<String> devices = record.getAll();
		String dmpid = null;
		for(String device : devices){
			String dmpidtmp = userJedisOld.get(device);
			if(dmpidtmp==null || "".equals(dmpidtmp)){
				continue;
			}
			logger.info("------DataPhrase dmpidtmp" + dmpidtmp + " to be ralated-----");
			Map<String,String> map = backwardJedis.hgetAll(dmpidtmp);
			Record newRecord = new Record(map);
			if(newRecord.isNull()){
				dmpid = dmpidtmp;
			}
		}
		if(dmpid==null){
			dmpid = Helper.genDmpid();
		}else{
			logger.info("------DataPhrase dmpidtmp" + dmpid + " is ralated-----");
		}
		return dmpid;
	}
	
	private void dealDeviceTooMany(Record newRecord, Record record, String key){
		new DealThread(newRecord, record, key).start();
	}
	

	class DealThread extends Thread { 
		private Record newRecord;
		private Record record;
		private String key;
		public DealThread(Record newRecord, Record record, String key){
			this.newRecord = newRecord;
			this.record = record;
			this.key = key;
		}
		public void run() {
			blackBeijingJedis.sadd("DmpId_Black_List_all", newRecord.getDmpid());
			blackHangzhouJedis.sadd("DmpId_Black_List_all", newRecord.getDmpid());
			SimpleDateFormat time=new SimpleDateFormat("yyyyMMddHHmmss"); 
			Date nowTime=new Date();
			blackBeijingJedis.set("DmpId_Black_List_all_Ts", time.format(nowTime));
			blackHangzhouJedis.set("DmpId_Black_List_all_Ts", time.format(nowTime));
			List<String> devices = newRecord.getMap().get(key);
			devices.addAll(record.getMap().get(key));
			for(String device : devices){
				backwardJedis.sadd("emar-dm-devide-blaklist-" + key, device);
			}
		}
	};

	/**
	 * The bolt will only emit the field "word" 
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("judge","record","dmpids"));
	}
}
