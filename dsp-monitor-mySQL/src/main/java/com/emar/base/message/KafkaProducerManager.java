package com.emar.base.message;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * kafka 消息创建者管理器
 * 提供创建消息连接的功能，提供单独创建Producer的功能，方便Producer的扩展使用；<br>
 * 提供直接发送消息的封装，可直接使用send方法发送消息。<br>
 * 枚举类KafkaTopics用于规范消息队列的发送，便于代码维护。
 * Created by ralf.cao on 2015/1/8.
 */
public class KafkaProducerManager {

    private static Logger log = Logger.getLogger(KafkaProducerManager.class);
    /**
     * 唯一单例
     */
    private static KafkaProducerManager manager;

    /**
     * 创建者连接配置
     */
    private ProducerConfig producerConfig;

    private KafkaProducerManager() {
        Properties props = new Properties();
        try {
            Properties temp = new Properties();
            temp.load(KafkaProducerManager.class.getResourceAsStream("/monitor_kafka_config.properties"));
            for(Map.Entry<Object,Object> entry : temp.entrySet()){
                String key = entry.getKey().toString();
                if(key.startsWith("monitor.")) {
                    props.put(key.substring(8),entry.getValue());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        producerConfig = new ProducerConfig(props);
    }

    /**
     * 获取单例入口
     *
     * @return
     */
    public static KafkaProducerManager getManager() {
        if (manager == null) {
            synchronized (KafkaProducerManager.class) {
                if (manager == null) {
                    manager = new KafkaProducerManager();
                }
            }
        }
        return manager;
    }

    /**
     * 产生一个创建咋
     *
     * @param <K>
     * @param <T>
     * @return
     */
    public <K, T> Producer<K, T> createProducer() {
        return new Producer<K, T>(producerConfig);
    }

    public <K, T> Producer<K, T> createProducer(Properties properties) {
        return new Producer<K, T>(new ProducerConfig(properties));
    }

    public <T> void sendMessage(String topic, T value) {
        sendMessage(topic, null, value);
    }

    public <K, T> void sendMessage(String topic, K key, T value) {
        Producer<K, T> producer = createProducer();
        KeyedMessage<K, T> data = new KeyedMessage<K, T>(topic, key, value);
        producer.send(data);
        producer.close();
    }

}
