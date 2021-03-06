package com.emar.base.message;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by ralf.cao on 2015/1/8.
 */
public abstract class KafkaPostConsumer {

    protected KafkaConsumerConfig consumerConfig;
    private ConsumerConnector consumer;
    private ExecutorService executor;
    public abstract KafkaConsumerConfig getConsumerConfig();

    public KafkaPostConsumer(){
        init();
    }

    public KafkaPostConsumer(KafkaConsumerConfig consumerConfig){
        this.consumerConfig = consumerConfig;
        init();
    }

    public ExecutorService getExecutorService() {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, getTopic());
            }
        });
    }

    protected void init() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(getTopic(), getThreadNum());
        Properties props = getConsumerConfig().toProperties();
        props.put("group.id",getGroupId());
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(getTopic());

        executor = getExecutorService();
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(getRunnable(stream, threadNumber));
            threadNumber++;
        }
    }

    /**
     * 销毁方法，在spring中提供
     */
    public void destroy(){
        if(executor!=null){
            executor.shutdown();
        }
    }

    /** 提供 消费组的 识别号 **/
    public String getGroupId(){ return "dsp.dsp";}

    /**
     * 提供消费队列的名称
     * @return
     */
    public abstract String getTopic();

    /**
     * 对单台机器而言，一次使用几个线程尽心队列处理
     * @return
     */
    public abstract int getThreadNum();

    /**
     * 提供处理的方法
     * @param stream
     * @param threadIndex
     * @return
     */
    public abstract Runnable getRunnable(final KafkaStream stream,final int threadIndex);
}
