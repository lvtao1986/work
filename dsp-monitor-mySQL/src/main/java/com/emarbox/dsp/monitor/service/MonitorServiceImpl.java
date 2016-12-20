package com.emarbox.dsp.monitor.service;

import com.codahale.metrics.Timer;
import com.emarbox.dsp.monitor.util.DmpLogWriter;
import com.emarbox.dsp.monitor.util.MetricsUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.emarbox.dsp.monitor.util.Config;
import com.emarbox.dsp.monitor.util.LogWriter;

@Service
public class MonitorServiceImpl implements MonitorService {

	private static final LogWriter log = LogWriter.getInstance();
	private static final DmpLogWriter dmpLog = DmpLogWriter.getInstance();
	//单条日志写入文件时长记录
	private final Timer writeLogTimer = MetricsUtil.metricsRegistry.timer("writeLogTimer");

	@Override
	public void writeLog(String supplier,String info) {
		final Timer.Context timeContext = writeLogTimer.time(); //计时开始
		log.info(info);
		timeContext.stop();//计时结束
	}

	@Override
	public void writeDmpLog(String supplier,String info) {
		final Timer.Context timeContext = writeLogTimer.time(); //计时开始
		dmpLog.info(supplier,info);
		timeContext.stop();//计时结束
	}

}
