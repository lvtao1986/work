package com.emarbox.dsp.monitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtbErrorDataLog {
	private static final Logger log = LoggerFactory
			.getLogger(RtbErrorDataLog.class);

	public static void info(String info) {
		log.info(info);
	}
}
