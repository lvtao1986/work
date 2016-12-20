import java.util.HashSet;
import java.util.Set;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

import bolts.DataCleaning;
import bolts.DataEntry;
import bolts.DataPhrase;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import spouts.RequestReader;

public class TopologyMain {
	public static void main(String[] args) throws InterruptedException {
		String name = args[0];
		String[] configJedisNode = args[1].split(":");

		String configName = "cmconfig";
		@SuppressWarnings("resource")
		Jedis configJedis = new Jedis(configJedisNode[0],Integer.parseInt(configJedisNode[1]));
		String userqueueJedis = configJedis.hget(configName, "userqueueJedis");
		String userCacheJedis = configJedis.hget(configName, "userCacheJedis");
		String blackBeijingJedis = configJedis.hget(configName, "blackBeijingJedis");
		String blackHangzhouJedis = configJedis.hget(configName, "blackHangzhouJedis");
		String userCluster1 = configJedis.hget(configName, "userCluster1");
		String userCluster2 = configJedis.hget(configName, "userCluster2");
		String userClusterOld = configJedis.hget(configName, "userClusterOld");
		String backwardCluster = configJedis.hget(configName, "backwardCluster");
		int requestReaderCount = Integer.valueOf(configJedis.hget(configName, "requestReaderCount"));
		int dataCleaningCount = Integer.valueOf(configJedis.hget(configName, "dataCleaningCount"));
		int dataPhraseCount = Integer.valueOf(configJedis.hget(configName, "dataPhraseCount"));
		int dataEntryCount = Integer.valueOf(configJedis.hget(configName, "dataEntryCount"));
		int numWorkers = Integer.valueOf(configJedis.hget(configName, "numWorkers"));
		int numAckers = Integer.valueOf(configJedis.hget(configName, "numAckers"));
		int maxSpoutPending = Integer.valueOf(configJedis.hget(configName, "maxSpoutPending"));
		int timeout = Integer.valueOf(configJedis.hget(configName, "timeout"));

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		try {
			builder.setSpout("request-reader", new RequestReader(
					userqueueJedis), requestReaderCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<HostAndPort> userClusterNodes1 = new HashSet<HostAndPort>();
		String[] userCluster1Array = userCluster1.split(";");
		for(String uc1 : userCluster1Array){
			userClusterNodes1.add(new HostAndPort(uc1.split(":")[0],Integer.valueOf(uc1.split(":")[1])));
		}
		Set<HostAndPort> userClusterNodes2 = new HashSet<HostAndPort>();
		String[] userCluster2Array = userCluster2.split(";");
		for(String uc2 : userCluster2Array){
			userClusterNodes2.add(new HostAndPort(uc2.split(":")[0],Integer.valueOf(uc2.split(":")[1])));
		}
		Set<HostAndPort> userClusterNodesOld = new HashSet<HostAndPort>();
		String[] userClusterOldArray = userClusterOld.split(";");
		for(String ucOld : userClusterOldArray){
			userClusterNodesOld.add(new HostAndPort(ucOld.split(":")[0],Integer.valueOf(ucOld.split(":")[1])));
		}
		Set<HostAndPort> backwardClusterNodes = new HashSet<HostAndPort>();
		String[] backwardClusterArray = backwardCluster.split(";");
		for(String bc : backwardClusterArray){
			backwardClusterNodes.add(new HostAndPort(bc.split(":")[0],Integer.valueOf(bc.split(":")[1])));
		}

		builder.setBolt("data-cleaning",
				new DataCleaning(userCacheJedis), dataCleaningCount)
				.shuffleGrouping("request-reader", "mobile_user_data");
		builder.setBolt(
				"data-phrase",
				new DataPhrase(backwardClusterNodes, userClusterNodes1,userClusterNodes2,userClusterNodesOld,
						blackBeijingJedis, blackHangzhouJedis, userCacheJedis), dataPhraseCount)
				.shuffleGrouping("data-cleaning");
		builder.setBolt("data-entry",
				new DataEntry(userClusterNodes1,userClusterNodes2, backwardClusterNodes, userCacheJedis), dataEntryCount)
				.shuffleGrouping("data-phrase");

		// Configuration
		Config conf = new Config();
		conf.setDebug(false);
		// Topology run
		conf.setNumWorkers(numWorkers);
		conf.setNumAckers(numAckers);
		conf.setMaxSpoutPending(maxSpoutPending);
		conf.setMessageTimeoutSecs(timeout);
		try {
			StormSubmitter.submitTopology(name, conf, builder.createTopology());
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
	}
}
