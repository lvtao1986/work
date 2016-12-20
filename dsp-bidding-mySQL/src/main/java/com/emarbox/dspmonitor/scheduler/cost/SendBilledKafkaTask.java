package com.emarbox.dspmonitor.scheduler.cost;

import app.base.message.KafkaProducerManager;
import app.base.util.ConfigUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步发送billed kafka 数据,每个计费线程持有一个实例
 * Created by mrzhu on 15/5/27.
 */
public class SendBilledKafkaTask{
    private final Logger log = LoggerFactory.getLogger(SendBilledKafkaTask.class);
    private final Logger unSendBilledKafkaLogger = LoggerFactory.getLogger("unSendBilledKafka");//记录发送失败数据用
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final Producer<String, String> producer;

    public SendBilledKafkaTask(){
        this.producer = KafkaProducerManager.getManager().createProducer();
    }

    private static String QUEUE_BILLED_PREFIX = ConfigUtil.getString("kafka.topic.name.prefix.billed");


    public void addMessageList(final List<String> messageList,final Long accountId){
       final String producerTopic = QUEUE_BILLED_PREFIX + accountId;

           executorService.submit(new Runnable() {
               @Override
               public void run() {
                   // 组装kafka的发送数据对象
                   List<KeyedMessage<String, String>> messages = new ArrayList<KeyedMessage<String, String>>(messageList.size());
                   for (String message : messageList) {
                       messages.add(new KeyedMessage<String, String>(producerTopic, message));
                   }
                   sendMessage(messages);
               }
           });
    }

    private void sendMessage(List<KeyedMessage<String, String>> messages) {
        try {
            producer.send(messages);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            //记录发送失败的数据
            for(KeyedMessage<String, String> message : messages) {
                unSendBilledKafkaLogger.info(message.key()+","+message.message());
            }
        }
    }
}
