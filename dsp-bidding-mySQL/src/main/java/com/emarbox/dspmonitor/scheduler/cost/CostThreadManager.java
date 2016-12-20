package com.emarbox.dspmonitor.scheduler.cost;

import com.emarbox.dspmonitor.scheduler.CostScheduler;
import com.emarbox.dspmonitor.scheduler.CostThread;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mrzhu on 15/5/26.
 */
public interface CostThreadManager {

    /**
     * 拆分多线程处理消息
     * @param accountBalance
     * @return
     */
    List<CostTask> dividMessages(ReentrantLock costLock,CostScheduler costScheduler,CostThread costThread, long accountId,double accountBalance, List<String> messageList,String handleTopic);

}
