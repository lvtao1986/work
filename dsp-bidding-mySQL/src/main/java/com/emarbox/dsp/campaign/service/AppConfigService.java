/**
 * AppConfigService.java
 * com.emarbox.dsp.campaign.service
 *
 * Function： ADD Function
 *
 * ver  1.0
 * date 2012-6-19
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.campaign.service;

import com.emarbox.dsp.domain.AppConfig;

/**
 * AppConfigService 获取系统信息
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-6-19	下午3:42:12
 *
 */
public interface AppConfigService {
	/**
	 * 
	 * getAppConfig: 获取单个系统信息
	 *
	 * @author   耿志新
	 * @version  1.0
	 * @Date	 2012-6-13 下午12:46:26
	 * @param  系统信息实体
	 * @return 系统信息实体
	 */
	AppConfig getAppConfig(AppConfig appConfig);
}

