package com.emarbox.dsp.monitor.service;

import com.emarbox.dsp.monitor.dto.ClickLog;
import com.emarbox.dsp.monitor.dto.ImpressionLog;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 改service记录所有展现和点击请求数据,写入日志文件
 * Created by mrzhu on 15/5/20.
 */
public class RequestLogService {

    private static Logger log = LoggerFactory.getLogger("requestLog");

    private static final String CHAR_SEPARATOR=",";
    /**
     * 记录展现请求日志
     * 格式:supplier,info,sign,c,guid,apid,actionType,userId,ip,agent,referer,ct,rt,rt2,pid,td,url
     */
    public static void impressionLog(ImpressionLog impressionLog) {
        StringBuilder str = new StringBuilder();
        str.append(impressionLog.getSupplier()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getInfo()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getSign()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getC()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getGuid()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getApid()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getActionType()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getUserId()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getIp()).append(CHAR_SEPARATOR);
        String agent = impressionLog.getAgent();
        if(StringUtils.isNotBlank(agent)){
            agent = agent.replace(',','，');
        }
        str.append(agent).append(CHAR_SEPARATOR);
        String referer = impressionLog.getReferer();
        if(StringUtils.isNotBlank(referer)){
            referer = referer.replace(',','，');
        }
        str.append(referer).append(CHAR_SEPARATOR);
        String ct = impressionLog.getCt();
        if(StringUtils.isNotBlank(ct)){
            ct = ct.replace(',','，');
        }
        str.append(ct).append(CHAR_SEPARATOR);

        String rt = impressionLog.getRt();
        if(StringUtils.isNotBlank(rt)){
            rt = rt.replace(',','，');
        }
        str.append(rt).append(CHAR_SEPARATOR);
        String rt2 = impressionLog.getRt2();
        if(StringUtils.isNotBlank(rt2)){
            rt2 = referer.replace(',','，');
        }
        str.append(rt2).append(CHAR_SEPARATOR);
        str.append(impressionLog.getPid()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getTd());
        str.append(impressionLog.getBt()).append(CHAR_SEPARATOR);
        str.append(impressionLog.getUt()).append(CHAR_SEPARATOR);
//        str.append(clickLog.getUrl());//展现url为空
        log.info(str.toString());
    }

    /**
     * 记录点击日志
     * 格式:supplier,info,sign,c,guid,apid,actionType,userId,ip,agent,referer,ct,rt,rt2,pid,td,url
     */
    public static void clickLog(ClickLog clickLog) {
        StringBuilder str = new StringBuilder();
        str.append(clickLog.getSupplier()).append(CHAR_SEPARATOR);
        str.append(clickLog.getInfo()).append(CHAR_SEPARATOR);
        str.append(clickLog.getSign()).append(CHAR_SEPARATOR);
        str.append(clickLog.getC()).append(CHAR_SEPARATOR);
        str.append(clickLog.getGuid()).append(CHAR_SEPARATOR);
        str.append(clickLog.getApid()).append(CHAR_SEPARATOR);
        str.append(clickLog.getActionType()).append(CHAR_SEPARATOR);
        str.append(clickLog.getUserId()).append(CHAR_SEPARATOR);
        str.append(clickLog.getIp()).append(CHAR_SEPARATOR);
        String agent = clickLog.getAgent();
        if(StringUtils.isNotBlank(agent)){
            agent = agent.replace(',','，');
        }
        str.append(agent).append(CHAR_SEPARATOR);
        String referer = clickLog.getReferer();
        if(StringUtils.isNotBlank(referer)){
            referer = referer.replace(',','，');
        }
        str.append(referer).append(CHAR_SEPARATOR);
        String ct = clickLog.getCt();
        if(StringUtils.isNotBlank(ct)){
            ct = ct.replace(',','，');
        }
        str.append(ct).append(CHAR_SEPARATOR);

        String rt = clickLog.getRt();
        if(StringUtils.isNotBlank(rt)){
            rt = rt.replace(',','，');
        }
        str.append(rt).append(CHAR_SEPARATOR);
        String rt2 = clickLog.getRt2();
        if(StringUtils.isNotBlank(rt2)){
            rt2 = referer.replace(',','，');
        }
        str.append(rt2).append(CHAR_SEPARATOR);
        str.append(clickLog.getBt()).append(CHAR_SEPARATOR);
        str.append(clickLog.getUt()).append(CHAR_SEPARATOR);
        str.append(clickLog.getPid()).append(CHAR_SEPARATOR);
        str.append(clickLog.getTd()).append(CHAR_SEPARATOR);
        str.append(clickLog.getUrl());
        log.info(str.toString());
    }

}
