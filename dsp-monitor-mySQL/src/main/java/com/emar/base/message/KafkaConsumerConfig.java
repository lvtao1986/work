package com.emar.base.message;

import java.util.Properties;

/**
 * Created by ralf.cao on 2015/1/8.
 */
public class KafkaConsumerConfig {
    private String zkConnect;
    private String zkSessionTimeout;
    private String zkSynTime;
    private String zkCommitInterval;

    public String getZkConnect() {
        return zkConnect;
    }

    public void setZkConnect(String zkConnect) {
        this.zkConnect = zkConnect;
    }

    public String getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public void setZkSessionTimeout(String zkSessionTimeout) {
        this.zkSessionTimeout = zkSessionTimeout;
    }

    public String getZkSynTime() {
        return zkSynTime;
    }

    public void setZkSynTime(String zkSynTime) {
        this.zkSynTime = zkSynTime;
    }

    public String getZkCommitInterval() {
        return zkCommitInterval;
    }

    public void setZkCommitInterval(String zkCommitInterval) {
        this.zkCommitInterval = zkCommitInterval;
    }

    public Properties toProperties(){
        Properties props = new Properties();
        props.put("zookeeper.connect", getZkConnect());
        //props.put("group.id", "myLocal");
        props.put("zookeeper.session.timeout.ms", getZkSessionTimeout());
        props.put("zookeeper.sync.time.ms", getZkSynTime());
        props.put("auto.commit.interval.ms", getZkCommitInterval());
        return props;
    }

    public KafkaConsumerConfig fromProperties(Properties props){
        if(props==null||props.isEmpty()){
            return this;
        }
        setZkConnect(props.getProperty("zookeeper.connect"));
        setZkSessionTimeout(props.getProperty("zookeeper.session.timeout.ms"));
        setZkSynTime(props.getProperty("zookeeper.sync.time.ms"));
        setZkCommitInterval(props.getProperty("auto.commit.interval.ms"));
        return this;
    }
}
