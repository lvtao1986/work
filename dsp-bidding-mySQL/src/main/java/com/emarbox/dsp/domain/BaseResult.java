/**
 * BaseResult.java
 * com.emarbox.dsp.domain
 *
 * Function： ADD Function
 *
 * ver  1.0
 * date 2012-5-28
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;
/**
 * ClassName:BaseResult
 * Function: ADD FUNCTION
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-5-28	下午6:45:14
 *
 */
public class BaseResult {
	/**
	 * 计划基础信息保存成功
	 */
	public static final String CAMPAIGN_BASE_SUCCESS = "1001";
	
	/**
	 * 计划基础信息保存失败
	 */
	public static final String CAMPAIGN_BASE_ERROR = "1000";
	
	/**
	 * 计划人群定向保存成功
	 */
	public static final String CAMPAIGN_ORIENTATION_SUCCESS = "2001";
	
	/**
	 * 计划人群定向保存失败
	 */
	public static final String CAMPAIGN_ORIENTATION_ERROR = "2000";
	
	/**
	 * 计划创意上传成功
	 */
	public static final String CAMPAIGN_CREATIVE_SUCCESS = "3001";
	
	/**
	 * 计划创意上传失败
	 */
	public static final String CAMPAIGN_CREATIVE_ERROR = "3000";
	
	/**
	 * 参数异常
	 */
	public static final String CAMPAIGN_ATTRIBUTE = "4000";
	/**
	 * 计划创建时候没有parent_user_id错误
	 */
	public static final String CAMPAIGN_PARENT_USER_ID_ERROR = "4100";
	/**
	 * 创意删除失败
	 */
	public static final String CREATIVE_DELETE_SUCCESS = "4001";
	
	/**
	 * 创意删除成功
	 */
	public static final String CREATIVE_DELETE_ERROR = "4002";
	
	/**
	 * 创意新建
	 */
	public static final String CREATIVE_CREATE = "4003";
	
	/**
	 * 创意修改（不包含修改图片文件）
	 */
	public static final String CREATIVE_UPDATE_FILE = "4004";
	
	/**
	 * 创意修改
	 */
	public static final String CREATIVE_UPDATE = "4005";
	
	
	/**
	 * 创意文件过大
	 */
	public static final String CREATIVE_FILE_SIZE = "4006";
	
	/**
	 * 创意文件类型
	 */
	public static final String CREATIVE_TYPE = "4007";
	
	/**
	 * 创意文件尺寸问题
	 */
	public static final String CREATIVE_X_Y = "4008";
	
	/**
	 * 创意文件上传失败
	 */
	public static final String CREATIVE_ERROR = "4009";
	
	/**
	 * 上传成功
	 */
	public static final String CREATIVE_UPLOAD_SUCCESS = "4010";
	/**
	 * 修改成功
	 */
	public static final String CREATIVE_UPDATE_SUCCESS = "4011";
	
	/**
	 * 添加部分老访客规则失败
	 */
	public static final String ADD_CAMPAIGN_RETARGERING_RULE_ERROR = "5000";
	
	/**
	 * 添加部分老访客规则成功
	 */
	public static final String ADD_CAMPAIGN_RETARGERING_RULE_SUCCESS = "5001";
	
	/**
	 * 添加部分老访客规则超过限制
	 */
	public static final String ADD_CAMPAIGN_RETARGERING_RULE_RESTRICTION = "5002";
	
	/**
	 * 访客重定向规则操作成功
	 */
	public static final String RETARGETING_RULE_SUCCESS = "6001";
	
}

