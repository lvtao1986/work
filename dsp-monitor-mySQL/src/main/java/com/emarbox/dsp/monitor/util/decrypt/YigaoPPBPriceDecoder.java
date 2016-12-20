package com.emarbox.dsp.monitor.util.decrypt;

import java.net.URLDecoder;

import com.emarbox.dsp.monitor.util.Base64Utils;
import com.emarbox.dsp.monitor.util.Config;
import com.emarbox.dsp.monitor.util.Md5Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YigaoPPBPriceDecoder {

	private static final Logger log = LoggerFactory.getLogger(YigaoPPBPriceDecoder.class);
	private static String keyStr = Config.getString("yigaoppb.public.key");

	/**
	 * 价格解密key
	 */

	public static void main(String[] args) {
		System.out.println(decode("YTZlNmVlNmI0MWE1ZTgwZWQ5OTUzMzY0YjUxZDZkODQ2MzI1MDJhZDQ%3D"));
	}

	public static String getStr(String enc) {
		try {
			byte[] src = Base64Utils.decode(URLDecoder.decode(enc));
			String aa = new String(src);
			return aa;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密价格
	 * 
	 * @param enc
	 * @return
	 */
	public static String decode(String enc) {
		/*
		 * 处理多次urldecode造成的解密错误
		 */
		if (enc.contains(" ")) {
			enc = enc.replace(" ", "+");
		}

		String src = getStr(enc);
		String prid = src.substring(0, 32);
		String encrypt = src.substring(32, src.length() - 4);

		int priceEnc = Integer.parseInt(encrypt, 16) ^ Integer.parseInt(Md5Encrypt.md5(prid + keyStr, "").substring(0, 4), 16);
		long result = 0;
		result = priceEnc;

		if (result >= 500000 || result < 0) {
			log.warn("解密异常，密文：" + enc + " 解密价格：" + result + ",设置价格为0");
			result = 0;
		}

		return String.valueOf(result);

	}

}
