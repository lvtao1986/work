package com.emarbox.dsp.monitor.schedule.rtb;

import org.springframework.beans.factory.annotation.Autowired;

import com.emarbox.dsp.monitor.service.RtbLogService;

/**
 * 应用开启式运行,实时发送竞价成功数据给RTB
 * 
 * @author zhujinju
 * 
 */
public class TimingSentDataTask {
	@Autowired
	private RtbLogService rtbLogService;

	public void execute() {
		rtbLogService.sentData();
	}


}
