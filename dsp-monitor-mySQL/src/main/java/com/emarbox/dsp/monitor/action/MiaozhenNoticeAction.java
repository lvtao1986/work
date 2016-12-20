package com.emarbox.dsp.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emarbox.dsp.monitor.util.SupplierInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emarbox.dsp.monitor.util.decrypt.MiaozhenPriceDecoder;

@Controller("miaozhenNoticeAction")
@RequestMapping(value="/mz")
public class MiaozhenNoticeAction extends BaseMonitorAction {

	private static final Logger log = LoggerFactory.getLogger("winnotice");
	private static final Logger logger = LoggerFactory.getLogger(MiaozhenNoticeAction.class);
	private static final String separator = ",";
	/**
	 * 竞价成功监控接口
	 * 仅记录日志判断数据差异用
	 */
	@RequestMapping(value="/winnotice")
	public void impressionMonitor(String id, String bidid,String impid, String price,HttpServletRequest request, HttpServletResponse response) {
		try {
			
			String p = "";
			
			try{
				p = MiaozhenPriceDecoder.decode(price);
			}catch (Exception e) {
				p = price;
				logger.error(e.getMessage(),e);
			}
			
			StringBuffer info = new StringBuffer();
			info.append(SupplierInfo.MIAOZHEN_ADX);
			info.append(separator);
			info.append(id);
			info.append(separator);
			info.append(bidid);
			info.append(separator);
			info.append(impid);
			info.append(separator);
			info.append(p);
			log.info(info.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}


}
