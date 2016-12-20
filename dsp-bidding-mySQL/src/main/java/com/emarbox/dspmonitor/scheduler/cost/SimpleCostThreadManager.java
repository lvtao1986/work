package com.emarbox.dspmonitor.scheduler.cost;

import com.codahale.metrics.Timer;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import com.emarbox.dspmonitor.data.BillingDataService;
import com.emarbox.dspmonitor.scheduler.CostScheduler;
import com.emarbox.dspmonitor.scheduler.CostThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 简单实现,如果账户预算大于10000并且处理条数大于1000,则分20个线程跑,否则一个线程跑
 * Created by mrzhu on 15/5/26.
 */
@Service("SimpleCostThreadManager")
public class SimpleCostThreadManager implements CostThreadManager {
    Timer timer =  MetricsUtil.metricsRegistry.timer("dividMessagesTimer");
    private Logger log = LoggerFactory.getLogger(SimpleCostThreadManager.class);

    @Autowired
    BillingDataService billingDataService;

    /**
     * 分线程数
     */
    public static final int DIVID_SIZE = 20;

    @Override
    public List<CostTask> dividMessages(ReentrantLock costLock,final CostScheduler costScheduler,CostThread costThread, long accountId,double accountBalance, List<String> messageList,String handleTopic) {
        Timer.Context timeContext = timer.time();
        List<CostTask> costTasks = new ArrayList<CostTask>(DIVID_SIZE);

        if(messageList.size() > 1000){
            log.info("divid messageList size:" + messageList.size());
            int size = messageList.size() / DIVID_SIZE;
            for(int i=0;i< DIVID_SIZE;i++){
                final List<String> subList;
                if(DIVID_SIZE == (i+1)){
                    subList = messageList.subList(i*size,messageList.size());
                }else{
                    subList = messageList.subList(i*size,size*(i+1));
                }

                CostTask task = new CostTask(costLock,costThread, accountId, accountBalance, true, costScheduler,handleTopic);
                task.addMessageList(subList);
                costTasks.add(task);
            }
        }else {
            CostTask task = new CostTask(costLock,costThread,accountId,accountBalance,messageList,false,costScheduler,handleTopic);
            costTasks.add(task);
        }
        timeContext.stop();
        return costTasks;
    }

}
