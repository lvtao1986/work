package com.emar.monitor.handler;

import org.apache.commons.configuration.Configuration;

import com.emar.monitor.common.ConfigUtil;

public class BaseMongoProperties {
    public static final Configuration conf = ConfigUtil
	    .addConfig("mongo_click");
    
    public static final String mongoHost = conf.getString("mongo.host", "122.49.4.4");
    public static final int mongoPort = conf.getInt("mongo.port" ,10001);
    public static final String mongoTableName = conf
	    .getString("mongo.table.name", "ad_click_mst");
}
