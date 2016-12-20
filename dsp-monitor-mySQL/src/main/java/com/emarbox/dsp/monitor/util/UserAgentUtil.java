package com.emarbox.dsp.monitor.util;

import com.emarbox.dsp.monitor.dto.ConfigProperties;
import org.apache.commons.lang.StringUtils;

/**
 * userAgent 工具类
 * Created by mrzhu on 15/5/19.
 */
public class UserAgentUtil {

    private static final int AGENT_LEN_MIN = ConfigProperties.getAgentLengthMin();
    private static final String AGENT_START = ConfigProperties.getAgentStartswith();
    private static final String[] AGENT_CONTAINS = ConfigProperties.getAgentContains();

    /**
     * 验证useragent是否有效
     * @param agent 待验证字符串
     * @return 有效返回true
     */
    public static boolean checkUserAgent(String agent){

        if (StringUtils.isBlank(agent)) {
            return false;
        }
        //判断长度
        if (agent.length() < AGENT_LEN_MIN) {
            return false;
        }

        String lowCaseAgent = agent.toLowerCase();
        //判断开始字符串
        if (agent.startsWith(AGENT_START)) {
            return false;
        }
        //判断字符串包含
        for (int i = 0; i < AGENT_CONTAINS.length; i++) {
            String content = AGENT_CONTAINS[i];
            if (lowCaseAgent.contains(content)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 清理特殊字符
     * @param str 原始字符串
     * @return 清理后的字符串
     */
    public static String cleanSpecialChar(String str){
        if(str == null){
            return null;
        }
        return str.replace(',', ' ').replace('\u0001',' ').replace("\r\n"," ");
    }
}
