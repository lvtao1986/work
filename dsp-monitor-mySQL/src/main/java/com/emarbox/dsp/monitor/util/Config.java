package com.emarbox.dsp.monitor.util;

import java.io.FileInputStream;
import java.util.PropertyResourceBundle;
import org.apache.log4j.Logger;

public class Config {

	private static final Logger log = Logger.getLogger(Config.class);

	private static PropertyResourceBundle bundle;

	private static String propFilePath = null;

	static {
		try {
			java.net.URL url = Config.class.getResource("");
			int yigaoIndex = url.getPath().indexOf("com/emar");
			String pathPrefix = url.getPath().substring(0, yigaoIndex)
					.replace("%20", " ");
			propFilePath = pathPrefix + "config.properties";

			FileInputStream fis = new FileInputStream(propFilePath);
			bundle = new PropertyResourceBundle(fis);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public static void reloadConfig() throws Exception {
		try {
			FileInputStream fis = new FileInputStream(propFilePath);
			bundle = new PropertyResourceBundle(fis);
			log.warn("the property-file been changed.");
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getString(String key) {
		try {
			String result = bundle.getString(key);
			return result;
		} catch (java.util.MissingResourceException mre) {
			return "";
		}
	}
}