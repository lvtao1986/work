package com.emarbox.dsp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.codehaus.jackson.map.SerializationConfig;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import app.base.spring.service.BaseService;
import app.base.spring.web.render.JsonRender;
import app.base.spring.web.render.XmlRender;
import app.base.util.ConfigUtil;

import com.emarbox.dsp.campaign.dao.AppConfigDao;
import com.emarbox.dsp.domain.AppConfig;
import com.emarbox.dsp.domain.Creative;
import com.emarbox.dsp.domain.DspDomain;
import com.emarbox.dsp.util.AppConfigUtil;

/**
 * 所有服务类的基类
 * @author zhaidw
 *
 */
public abstract class DspBaseService extends BaseService {

	/**
	 * 配置项读取类
	 */
	protected static Configuration config;

	static{
		config = ConfigUtil.addConfig("config");
	}
	
	public static Configuration getConfig(){
		return config;
	}
	

	

	/**
	 * xml 或 json 响应的默认编码
	 */
	protected static String encoding = "UTF-8";


	
	
}
