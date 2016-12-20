package com.emarbox.dspmonitor.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by ralf.cao on 2015/1/15.
 */
@Service
public class BillingDataServiceImpl implements BillingDataService {

    /**
     * 增加slf4j日志框架 by ralf.cao at 2015/1/14
     **/
    private static final Logger billingErrorListLogger = LoggerFactory.getLogger("billingErrorList");

    @Override
    public void addErrorCostMessage(String topic, Collection<String> errorMsgList) {
        //logger.info
        if (errorMsgList != null && errorMsgList.size() > 0) {
            for (String message : errorMsgList) {
                billingErrorListLogger.info(topic + " " + message);
            }
        }
    }
}
