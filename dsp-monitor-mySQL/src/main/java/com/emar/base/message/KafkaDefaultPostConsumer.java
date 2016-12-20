package com.emar.base.message;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 默认的消息消费者，具体使用还需要重写getRunnable方法
 * Created by ralf.cao on 2015/1/8.
 */
public class KafkaDefaultPostConsumer extends KafkaPostConsumer {
    private static final Logger log = Logger.getLogger(KafkaDefaultPostConsumer.class);
    private static final KafkaConsumerConfig defaultConsumerConfig =  new KafkaConsumerConfig();
    static {
        Properties props = new Properties();
        try {
            Properties temp = new Properties();
            temp.load(KafkaDefaultPostConsumer.class.getResourceAsStream("monitor_kafka_config.properties"));
            for(Map.Entry<Object,Object> entry : temp.entrySet()){
                String key = entry.getKey().toString();
                if(key.startsWith("monitor.")) {
                    props.put(key.substring(8),entry.getValue());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        defaultConsumerConfig.fromProperties(props);
    }
    public KafkaConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public KafkaDefaultPostConsumer(){
        consumerConfig = defaultConsumerConfig;
        init();
    }

    public KafkaDefaultPostConsumer(KafkaConsumerConfig consumerConfig) {
        super(consumerConfig);
    }

    @Override
    public String getTopic() {
        return "mykafka";
    }

    @Override
    public int getThreadNum() {
        return 2;
    }

    @Override
    public Runnable getRunnable(final KafkaStream stream, final int threadIndex) {
        return new Runnable() {
            @Override
            public void run() {
                ConsumerIterator<byte[], byte[]> it = stream.iterator();
                while (it.hasNext()) {
                    System.out.println("Thread " + threadIndex + ": " + new String(it.next().message()));
                }
                System.out.println("Shutting down Thread: " + threadIndex);
            }
        };
    }
}
