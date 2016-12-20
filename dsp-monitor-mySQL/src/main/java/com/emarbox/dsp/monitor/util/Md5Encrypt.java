package com.emarbox.dsp.monitor.util;

/**
 * Alipay.com Inc. Copyright (c) 2004-2005 All Rights Reserved.
 * 
 * <p>
 * Created on 2005-7-9
 * </p>
 */


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密算法
 */
public class Md5Encrypt {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 对字符串进行MD5加密
	 * @param text  明文
	 * @return 密文
	 */
	public static String md5(String text) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes("GBK"));

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}
	public final static String md5(String s, String key) {
    	char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        				'a', 'b', 'c', 'd', 'e', 'f'};
    	try {
	    	byte[] strTemp = (key + s).getBytes();
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	        mdTemp.update(strTemp);
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char str[] = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	        	byte byte0 = md[i];
	        	str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	        	str[k++] = hexDigits[byte0 & 0xf];
	      	}
      		return new String(str);
    	} catch (Exception e) {
      		return null;
    	}
  	}

}