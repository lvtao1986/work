package com.emarbox.dspmonitor.data;

import java.util.Collection;

/**
 * Created by ralf.cao on 2015/1/15.
 */
public interface BillingDataService {
    /**
     * 将不能处理的数据存入错误日志
     * @param errorMsgList
     */
    void addErrorCostMessage(String topic,Collection<String> errorMsgList);

}
