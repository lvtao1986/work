package com.emarbox.dspmonitor.scheduler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codahale.metrics.Timer;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import com.emarbox.dspmonitor.status.data.NodeStatus;

import app.base.util.ConfigUtil;
import app.common.util.LogUtil;
import app.common.util.Logs;

public abstract class BasicKafkaConsumer extends Thread {

	protected Logs log;
	public static final Long NO_DATA_SLEEP_TIME = 5 * 1000L;
	public static final Long NODE_SLEEP_TIME = 10 * 1000L;

	private String leadBroker = null;
	
	PartitionMetadata metadata = null;
	private SimpleConsumer consumer = null;
	private Long readOffset = null;
	
	
	static {
		ConfigUtil.addConfig("monitor_kafka_config");
	}
	
	abstract protected String getClientName();
	abstract protected String getTopic();
	abstract protected int getPartition();

	/**
	 * 获取未消费的队列大小
	 * @return -1表示读取不到
	 */
	public long getUnConsumeMessageSize(){
		long current = readOffset == null ? 0:readOffset;
		if(consumer == null){
			return -1;
		}
		long last = lastOffset(consumer, getTopic(), getPartition(), getClientName());
		return (last - current);
	}
	
	/**
	 * 批处理条数
	 * @return
	 */
	protected int getBatchSize() {
		return 5000000;
	}
	
	/**
	 * 业务方法,处理消息
	 * @return 是否处理成功
	 */
	abstract protected boolean handleMessageList(List<String> messageList);
	
	private static class ZookClientHolder {
		private static CuratorFramework zookClient = CuratorFrameworkFactory.builder().connectString(ConfigUtil.getString("zookeeper.connect"))
				.retryPolicy(new ExponentialBackoffRetry(1000, 10)).connectionTimeoutMs(Integer.parseInt(ConfigUtil.getString("zookeeper.connection.timeout.ms")))
				.sessionTimeoutMs(Integer.parseInt(ConfigUtil.getString("zookeeper.session.timeout.ms"))).build();
		static{
			zookClient.start();
		}
	}
	
	/**
	 * 获取zookeeper client ,保存和读取 offset用
	 * 
	 * @return
	 */
	private CuratorFramework getZookClient() {
		return ZookClientHolder.zookClient;
	}

	private List<String> m_replicaBrokers = new ArrayList<String>();

	public BasicKafkaConsumer() {
		log = LogUtil.getLog(getClass());

		m_replicaBrokers = new ArrayList<String>();
	}

	Timer readKafaDataTimer =  MetricsUtil.metricsRegistry.timer("readKafaDataTimer");
	@Override
	public void run(){
		String a_topic = getTopic(); 
		String clientName = getClientName();
		int a_partition = getPartition();
		List<String> seedBrokers = getSeedBrokers();

		// find the meta data about the topic and partition we are interested in
		//
		CuratorFramework zookClient = getZookClient();

		int numErrors = 0;
		
			while (true) {
				Timer.Context timeContext = null;
				try {
				//主节点执行定时任务
				if (!NodeStatus.isMajorNode) {
					try {
						Thread.sleep(NODE_SLEEP_TIME);
						continue;
					} catch (InterruptedException e) {
						log.info(e.getMessage(), e);
					}
				}
					timeContext =	readKafaDataTimer.time();
				if(metadata == null || metadata.leader() == null){
					metadata = findLeader(seedBrokers, a_topic, a_partition);
					if (metadata == null) {
						log.warn("Can't find metadata for Topic and Partition. Exiting");
						Thread.sleep(NODE_SLEEP_TIME);
						continue;
					}
					if (metadata.leader() == null) {
						log.warn("Can't find Leader for Topic and Partition. Exiting");
						Thread.sleep(NODE_SLEEP_TIME);
						continue;
					}
					leadBroker = metadata.leader().host();
				}
				
				if (consumer == null) {
					consumer = new SimpleConsumer(leadBroker, metadata.leader().port(), 100000, 64 * 1024, clientName);
				}
				
				if(readOffset == null){
					readOffset = getLastOffset(zookClient, consumer, a_topic, a_partition, clientName);
				}
				
				long beginReadOffset = readOffset;
				FetchRequest req = new FetchRequestBuilder().clientId(clientName).addFetch(a_topic, a_partition, readOffset, getBatchSize()) // 可以调整这个批量参数
						.build();
				FetchResponse fetchResponse = consumer.fetch(req);

				if (fetchResponse.hasError()) {
					numErrors++;
					// Something went wrong!
					short code = fetchResponse.errorCode(a_topic, a_partition);
					log.error("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
					if (numErrors > 5)
						break;
					if (code == ErrorMapping.OffsetOutOfRangeCode()) {
						// We asked for an invalid offset. For simple case ask for
						// the last element to reset
						readOffset = getLastOffset(zookClient, consumer, a_topic, a_partition, clientName);
						continue;
					}
					consumer.close();
					consumer = null;
					leadBroker = findNewLeader(leadBroker, a_topic, a_partition);
					continue;
				}
				numErrors = 0;

				long numRead = 0;
				List<String> messageList = new ArrayList<String>();//获取到的消息
				for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(a_topic, a_partition)) {
					long currentOffset = messageAndOffset.offset();
					if (currentOffset < readOffset) {
						log.error("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
						continue;
					}
					readOffset = messageAndOffset.nextOffset();
					ByteBuffer payload = messageAndOffset.message().payload();

					byte[] bytes = new byte[payload.limit()];
					payload.get(bytes);
					messageList.add(new String(bytes, "UTF-8"));
					numRead++;
				}
					timeContext.stop();
				if (numRead == 0) {//未读到数据等待下轮
					try {
						Thread.sleep(NO_DATA_SLEEP_TIME);
					} catch (InterruptedException ie) {
					}
				} else {//读到数据处理业务
					boolean result = handleMessageList(messageList);
					// save readOffset
					if (result) {
						updateLastOffset(zookClient, a_topic, a_partition, clientName, readOffset);
					}else{
						readOffset = beginReadOffset;
					}
				}
				} catch (Exception e) {
					log.error("consumer 出现error:"+e.getMessage(),e);
					if(consumer != null){
						consumer.close();
					}
					consumer = null;
					metadata = null;
					readOffset = null;
					if(timeContext != null){
						timeContext.stop();
					}
				}
			}
		
		if (consumer != null)
			consumer.close();
	}
	
	private List<String> getSeedBrokers() {
		String[] seeds =  ConfigUtil.getString("metadata.broker.list").split(",");
		return Arrays.asList(seeds);
	}
	private long lastOffset(SimpleConsumer consumer, String topic, int partition, String clientName) {
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.LatestTime(), 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
		OffsetResponse response = null;
		try{
			response = consumer.getOffsetsBefore(request);
		}catch(Exception e){
			if(consumer != null){
				consumer.close();
			}
			this.consumer = null;
			this.metadata = null;
			this.readOffset = null;
			log.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}

		if (response.hasError()) {
			this.consumer = null;
			this.metadata = null;
			this.readOffset = null;
			log.error("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
			throw new RuntimeException("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
		}
		long[] offsets = response.offsets(topic, partition);
		return offsets[0];
	}

	private long firstOffset(SimpleConsumer consumer, String topic, int partition, String clientName) {
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.EarliestTime(), 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
		OffsetResponse response = null;
		try{
			response = consumer.getOffsetsBefore(request);
		}catch(Exception e){
			if(consumer != null){
				consumer.close();
			}
			consumer = null;
			metadata = null;
			readOffset = null;
			log.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}

		if (response.hasError()) {
			this.consumer = null;
			this.metadata = null;
			this.readOffset = null;
			log.error("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
			throw new RuntimeException("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
		}
		long[] offsets = response.offsets(topic, partition);
		return offsets[0];
	}
	
	private long getZKLastOffset(CuratorFramework zookClient, SimpleConsumer consumer, String topic, int partition, String clientName) {
		Long beginIndex = 0L;
		String path = getOffsetPath(topic,partition,clientName);
		try {
			Stat stat = zookClient.checkExists().forPath(path);
			if (stat == null) {
				log.info("node is null");

				zookClient.create().creatingParentsIfNeeded().forPath(path, beginIndex.toString().getBytes());
			} else {
				byte[] result = zookClient.getData().forPath(path);
				beginIndex = Long.parseLong(new String(result));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return beginIndex;
	}
	
	private long getLastOffset(CuratorFramework zookClient, SimpleConsumer consumer, String topic, int partition, String clientName) {

		Long beginIndex = getZKLastOffset(zookClient, consumer, topic, partition, clientName);

		// 判断保存的index是否有效:不能在topic的offset区间之外
		long firstIndex = firstOffset(consumer, topic, partition, clientName); // topic中第一位
		long lastIndex = lastOffset(consumer, topic, partition, clientName);// topic中最后一位
		if (beginIndex < firstIndex) {
			beginIndex = firstIndex;
		}
		if (beginIndex > lastIndex) {
			beginIndex = lastIndex;
		}

		return beginIndex;
	}

	private void updateLastOffset(CuratorFramework zookClient, String topic, int partition, String clientName, long offset) throws Exception {
		String path = getOffsetPath(topic,partition,clientName);
		zookClient.setData().forPath(path, String.valueOf(offset).getBytes());
	}
	
	private String getOffsetPath(String topic, int partition, String clientName){
		String path = "/billing_offset/topic_" + topic + "/partition_" + partition + "/client_"+ clientName;
		return path;
	}

	private String findNewLeader(String a_oldLeader, String a_topic, int a_partition) throws Exception {
		for (int i = 0; i < 3; i++) {
			boolean goToSleep = false;
			PartitionMetadata metadata = findLeader(m_replicaBrokers, a_topic, a_partition);
			if (metadata == null) {
				goToSleep = true;
			} else if (metadata.leader() == null) {
				goToSleep = true;
			} else if (a_oldLeader.equalsIgnoreCase(metadata.leader().host()) && i == 0) {
				// first time through if the leader hasn't changed give
				// ZooKeeper a second to recover
				// second time, assume the broker did recover before failover,
				// or it was a non-Broker issue
				//
				goToSleep = true;
			} else {
				return metadata.leader().host();
			}
			if (goToSleep) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
				}
			}
		}
		throw new Exception("Unable to find new leader after Broker failure. Exiting");
	}

	private PartitionMetadata findLeader(List<String> seedBrokers, String a_topic, int a_partition) {
		PartitionMetadata returnMetaData = null;
		loop: for (String seed : seedBrokers) {
			SimpleConsumer consumer = null;
			try {
				String host = seed.split(":")[0];
				int port = Integer.parseInt(seed.split(":")[1]);

				consumer = new SimpleConsumer(host, port, 100000, 64 * 1024, "leaderLookup");
				List<String> topics = Collections.singletonList(a_topic);
				TopicMetadataRequest req = new TopicMetadataRequest(topics);
				kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

				List<TopicMetadata> metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						if (part.partitionId() == a_partition) {
							returnMetaData = part;
							break loop;
						}
					}
				}
			} catch (Exception e) {
				log.error("Error communicating with Broker [" + seed + "] to find Leader for [" + a_topic + ", " + a_partition + "] Reason: " + e);
			} finally {
				if (consumer != null)
					consumer.close();
			}
		}
		if (returnMetaData != null) {
			m_replicaBrokers.clear();
			for (kafka.cluster.Broker replica : returnMetaData.replicas()) {
				m_replicaBrokers.add(replica.host() + ":" + replica.port());
			}
		}
		return returnMetaData;
	}

}