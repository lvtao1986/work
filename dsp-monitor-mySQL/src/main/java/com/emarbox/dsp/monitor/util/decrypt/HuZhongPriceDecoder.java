package com.emarbox.dsp.monitor.util.decrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emarbox.dsp.monitor.util.Config;
import com.emarbox.dsp.monitor.util.RC4;

public class HuZhongPriceDecoder {
	private static String keyStr = Config.getString("huzhong.public.key");

	private static final Logger log = LoggerFactory
			.getLogger(HuZhongPriceDecoder.class);

	public static String decode(String encodeText) {
		byte[] data = Base64.decodeBase64(encodeText);
		String decry_RC4 = RC4.decry_RC4(data, keyStr);
		log.info(decry_RC4);
		return decry_RC4;
	}

	public static void main(String[] args) {
		
		String str = "ZyHG5hL0ZPEy";
		String key = "21dae5b98021eba7b5cac27c0bbba6028301a802";
		byte[] data = Base64.decodeBase64(str);
		String decry_RC4 = RC4.decry_RC4(data, key);
		log.info(decry_RC4);
	}

}
