package com.emar.base.message;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * paipai kafka 消息创建者管理器
 * 提供创建消息连接的功能，提供单独创建Producer的功能，方便Producer的扩展使用；<br>
 * 提供直接发送消息的封装，可直接使用send方法发送消息。<br>
 * 枚举类KafkaTopics用于规范消息队列的发送，便于代码维护。
 * Created by mrzhu on 2015/6/1.
 */
public class PaipaiKafkaProducerManager {

    private static Logger log = Logger.getLogger(PaipaiKafkaProducerManager.class);
    /**
     * 唯一单例
     */
    private static PaipaiKafkaProducerManager manager;

    /**
     * 创建者连接配置
     */
    private ProducerConfig producerConfig;

    private PaipaiKafkaProducerManager() {
        Properties props = new Properties();
        try {
            Properties temp = new Properties();
            temp.load(KafkaDefaultPostConsumer.class.getResourceAsStream("/paipai_kafka_config.properties"));
            for(Map.Entry<Object,Object> entry : temp.entrySet()){
                String key = entry.getKey().toString();
                if(key.startsWith("paipai.")) {
                    props.put(key.substring(7),entry.getValue());
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
    public static PaipaiKafkaProducerManager getManager() {
        if (manager == null) {
            synchronized (PaipaiKafkaProducerManager.class) {
                if (manager == null) {
                    manager = new PaipaiKafkaProducerManager();
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
