package com.emarbox.dsp.monitor.data;

import kafka.producer.KeyedMessage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ralf.cao on 2015/2/3.
 */
public class KafkaDataBillingMap {

    /**
     *  待计费日志缓冲区
     */
    private static final Queue<KeyedMessage<String, String>> biddingLogQueue = new ConcurrentLinkedQueue<KeyedMessage<String, String>>();

    /**
     * 获取待处理计费日志
     *
     * @return 日志,没有则返回null
     */
    public static KeyedMessage<String, String> pollBiddingLog() {
        return biddingLogQueue.poll();
    }

    /**
     * 将日志添加至计费队列
     *
     * @param message
     *            待处理日志
     * @return 成功返回ture
     */
    public static boolean addBiddingLog(KeyedMessage<String, String> message) {
        return biddingLogQueue.add(message);
    }
}
