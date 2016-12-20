package com.emarbox.dsp.monitor.service;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrzhu on 15/6/1.
 */
public class PaipaiService {

    private static final Logger log = LoggerFactory.getLogger(PaipaiService.class);
    private static final List<String> projectIdList = new ArrayList<String>(5);

    /**
     * 初始化数据
     */
    static {
        CompositeConfiguration config = new CompositeConfiguration();
        try {
            config.addConfiguration(new PropertiesConfiguration("paipai_kafka_config.properties"));
        } catch (ConfigurationException e) {
            log.error(e.getMessage(),e);
        }
        String [] ids = config.getStringArray("paipai.projectId.list");
        for(String id : ids){
            log.info("分流projectId:"+id);
            projectIdList.add(id);
        }

    }

    /**
     * 是否为拍拍的项目
     * @param projectId 项目id
     * @return 拍拍项目id返回true
     */
    public static boolean isPaipaiProject(String projectId){
        return projectIdList.contains(projectId);
    }
}
