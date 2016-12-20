package spouts;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import redis.clients.jedis.Jedis;

public class RequestReader extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 820179474934873158L;
	public static Logger logger = org.apache.log4j.Logger.getLogger(RequestReader.class);
	private SpoutOutputCollector collector;
	private String host;
	private int port;
	private Jedis jedis;
	private ConcurrentHashMap<UUID, String> pending;
	public void ack(Object msgId) {
		pending.remove(msgId);
	}

	public void close(){
		jedis.quit();
	}
	public void fail(Object msgId) {
		this.collector.emit("mobile_user_data",new Values(pending.get(msgId)),msgId);
	}
	
	public RequestReader(String addr) throws Exception{
		String[] ipPort = addr.split(":");
		if(ipPort.length<2){
			logger.error("redis addr error:"+addr);
			throw new Exception("redis addr error:"+addr);
		}
		host = ipPort[0];
		port = Integer.parseInt(ipPort[1]);
	}

	public void nextTuple() {
		String content = jedis.lpop("mobile_user_data_mq");
        if(content==null){
            return;
        } else {
    		UUID MsgId = UUID.randomUUID();
    		this.pending.put(MsgId, content);
    		logger.info("------RequestReader emit " + content + "-----");
            collector.emit("mobile_user_data", new Values(content), MsgId);
        }
	}

	/**
	 * We will create the file and get the collector object
	 */
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		logger.info("------RequestReader open-----");
		jedis = new Jedis(host,port);
		this.collector = collector;
		pending = new ConcurrentHashMap<UUID,String>();
	}

	/**
	 * Declare the output field "word"
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("mobile_user_data",new Fields("str"));
	}
}
