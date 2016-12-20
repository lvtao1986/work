package com.emarbox.dspmonitor.data;

import com.codahale.metrics.Timer;
import com.emarbox.dspmonitor.billing.util.MetricsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.*;

@Service
public class RequestIdServiceRedisImpl implements RequestIdService {

    private static final Logger log = LoggerFactory.getLogger(RequestIdServiceRedisImpl.class);

    /**
     * 45分钟内去重
     */
    private final int EXPIRE_SECONDS = 2700;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Timer saveResultToDBTimer =  MetricsUtil.metricsRegistry.timer("duplicateClick"); //记录计费完数据入数据库的调用时间

    @Override
    public boolean duplicateClick(final String requestId) {
        Timer.Context timeContext = saveResultToDBTimer.time();
        //40毫秒内没有完成去重则返回true
        Callable call = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    if (requestId == null) {
                        log.info("requestId is null.");
                        return true;
                    }
                    String key = requestId;
                    JedisPool pool = RedisDBPool.newInstance();
                    Jedis jedis = pool.getResource();
                    try {
                        String result = jedis.get(requestId);
                        if (result == null) {
                            jedis.setex(key, EXPIRE_SECONDS, "1");
                            return true;
                        } else {
                            return false;
                        }


                    } finally {
                        if (jedis != null) {
                            pool.returnResource(jedis);
                        }
                    }

                } catch (Throwable e) {
                    log.warn("duplicate click error:" + e.getMessage(), e);
                    return true;//异常返回true
                }
            }
        };


        Future<Boolean> future = executorService.submit(call);

        try {
            Boolean result = future.get(40,TimeUnit.MILLISECONDS);
            log.info("去重成功");
            return result;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            log.info("去重失败");
            return true; //超时则返回true
        }finally {
            timeContext.stop();
        }



    }

}
