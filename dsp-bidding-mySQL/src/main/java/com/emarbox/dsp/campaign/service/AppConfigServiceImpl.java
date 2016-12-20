/**
 * AppConfigServiceImpl.java
 * com.emarbox.dsp.campaign.service
 *
 * ver  1.0
 * date 2012-6-19
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.campaign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emarbox.dsp.campaign.CampaignBaseService;
import com.emarbox.dsp.campaign.dao.AppConfigDao;
import com.emarbox.dsp.domain.AppConfig;

/**
 * ClassName:AppConfigServiceImpl 接口AppConfigService的实现
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-19	下午3:42:55
 *
 */
@Service("appConfigService")
public class AppConfigServiceImpl extends CampaignBaseService implements AppConfigService {


	@Autowired
	private AppConfigDao appConfigDao;
	
	@Override
	public AppConfig getAppConfig(AppConfig appConfig) {
		return appConfigDao.getAppConfig(appConfig);
	}

}

