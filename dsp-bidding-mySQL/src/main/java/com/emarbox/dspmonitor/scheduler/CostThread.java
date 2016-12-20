package com.emarbox.dspmonitor.scheduler;

import app.base.util.ConfigUtil;
import com.codahale.metrics.Timer;
import com.emarbox.dsp.util.Function;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import com.emarbox.dspmonitor.data.CampaignCost;
import com.emarbox.dspmonitor.scheduler.cost.CostTask;
import com.emarbox.dspmonitor.scheduler.cost.SendBilledKafkaTask;
import com.emarbox.dspmonitor.scheduler.cost.SimpleCostThreadManager;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class CostThread extends BasicKafkaConsumer {

    private static String QUEUE_BILLING_PREFIX = ConfigUtil.getString("kafka.topic.name.prefix.billing");

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Timer timer =  MetricsUtil.metricsRegistry.timer("costTimer");//记录每批从kafka中取出数据的处理时间
    private Timer sendMessageListTimer =  MetricsUtil.metricsRegistry.timer("sendMessageListTimer"); //记录计费完数据发送kafka方法的调用时间
    private Timer saveResultToDBTimer =  MetricsUtil.metricsRegistry.timer("saveResultToDBTimer"); //记录计费完数据入数据库的调用时间

    private final long accountId; //线程需要处理的账号ID
    private final long projectId; //从topic提取时需要projectId提取
    private final CostScheduler costScheduler;
    private final SendBilledKafkaTask sendBilledKafkaTask = new SendBilledKafkaTask(); //发送计费结果kafka线程
    /**
     * 活动当天花费 key:campaignId_statDate , value:cost
     */
    public Map<String, Double> camTodayCostMap = new HashMap<String, Double>();
    /**
     * 当前线程账户余额，使用Map的原因是 为了方便值的修改和回传
     */
    private Map<Long,Double> accountBalanceMap = new HashMap<Long, Double>();
    /**
     * 当前线程账户当日预算余额，使用Map的原因是 为了方便值的修改和回传
     */
    public Map<Long,Double> accountBugetMap = new HashMap<Long, Double>();
    /**
     * 明天凌晨的时间点,切换天的时候更新
     */
    private long cacheDate = DateUtils.addDays(Function.today(), 1).getTime();
    private long accountRefreshStamp =0L;   // 账户余额刷新时戳
    public long todaySumCount=0L;
    public long todaySendCount=0L;

    CostThread(long accountId, long projectId, CostScheduler costScheduler) {
        this.accountId = accountId;
        this.projectId = projectId;
        this.costScheduler = costScheduler;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected String getClientName() {
        return "billing_" + projectId;
    }

    @Override
    protected String getTopic() {
        return QUEUE_BILLING_PREFIX + projectId;
    }

    @Override
    protected int getPartition() {
        return 0;
    }

    /**
     * 每天0点自动清空前一天的缓存，缓存中的最大数为 前一天的活动数+下一天的活动数
     */
    Timer processCacheTimer =  MetricsUtil.metricsRegistry.timer("processCacheTimer");
    private void processCache(){
        Timer.Context timeContext = processCacheTimer.time();
        long now = System.currentTimeMillis();
        if(now>cacheDate){
            todaySumCount = 0L;
            todaySendCount = 0L;
            cacheDate = DateUtils.addDays(Function.today(),1).getTime();
            camTodayCostMap = new HashMap<String, Double>(camTodayCostMap.size());
        }
        if(accountRefreshStamp!=costScheduler.accountRemainRefreshStamp){
            accountRefreshStamp=costScheduler.accountRemainRefreshStamp;
            accountBalanceMap.put(accountId, costScheduler.billingUtil.getAcountRemain(true).get(accountId));
            accountBugetMap.put(accountId,costScheduler.billingUtil.getBugetRemain(true).get(accountId));
        }
        timeContext.stop();
    }


    ExecutorService executorService = Executors.newFixedThreadPool(SimpleCostThreadManager.DIVID_SIZE);

    @Override
    protected boolean handleMessageList(List<String> messageList) {
        int unSum = messageList.size();
        if (unSum > 0) {
            Timer.Context timerContext = timer.time();
            log.debug("accountId:" + accountId + "projectId:" + projectId + " unConfirmed count:" + unSum);
            todaySumCount += unSum; //计数
            ReentrantLock costLock = new ReentrantLock();

            try {
            	costScheduler.lock.readLock().lock();
                // 缓存清理
                processCache();
                //切分为多线程
                final List<CostTask> taskList = getCostTasks(messageList, costLock);
                //多线程执行
                executorService.invokeAll(taskList);
            } catch (Exception e) {
                log.error("处理计费数据时出现异常,将部分数据写入日志文件:" + e.getMessage(), e);
            }finally{
        		costScheduler.lock.readLock().unlock();
        	}
            timerContext.stop();
        }
        return true;
    }


    private List<CostTask> getCostTasks(List<String> messageList, ReentrantLock costLock) {
        return costScheduler.costThreadManager.dividMessages(costLock,costScheduler, this, accountId, accountBalanceMap.get(accountId), messageList,this.getTopic());
    }


    /**
     * 更新账户余额信息
     * @param campainCostColl 活动花费集合
     */
    public void updateAccountBlance(Collection<CampaignCost> campainCostColl) {
        Double sumCost = 0d;
        for(CampaignCost cc : campainCostColl){
            if(cc.getCost()==null || cc.getCost() == 0d){
                continue;
            }
            String key = cc.getCampaignId()+"_"+cc.getStatDate();
            Double camTodayCost = camTodayCostMap.get(key);
            if(camTodayCost == null){
                camTodayCost = 0d;
            }
            camTodayCostMap.put(key, camTodayCost +cc.getCost());

            sumCost += cc.getCost();
        }
        Double accountBalance = accountBalanceMap.get(accountId);
        accountBalanceMap.put(accountId, ((accountBalance == null ? 0d : accountBalance) - sumCost));
    }

    /**
     * 计费完的数据入kafka队列
     * @param messageCache 需要发送数据
     */
    public void sendMessageList(List<String> messageCache) {
        Timer.Context timerContext = sendMessageListTimer.time();
        todaySendCount+=messageCache.size();
        sendBilledKafkaTask.addMessageList(messageCache,this.accountId);
        timerContext.stop();
    }

    /**
     * 计费完的数据入数据库
     * @param costList 需要入库数据
     * @throws ParseException
     */
    public void saveResultToDB(Collection<CampaignCost> costList) throws ParseException {
        Timer.Context timerContext = saveResultToDBTimer.time();
        costScheduler.billingDataDao.billingCampaignCost(costList);
        timerContext.stop();
    }

}
