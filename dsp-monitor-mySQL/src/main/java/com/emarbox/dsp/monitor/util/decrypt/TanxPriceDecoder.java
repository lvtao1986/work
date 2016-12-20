package com.emarbox.dsp.monitor.util.decrypt;

import java.util.Arrays;

import com.emarbox.dsp.monitor.util.MD5;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TanxPriceDecoder {
	
	private static final Logger log = LoggerFactory.getLogger(TanxPriceDecoder.class);
	
	/**
	 * tanx 价格解密key
	 */
	private static final String keyStr = "74963c193eab48bcdd8be098182c7284";
	private static final byte[] g_key = hexStringToBytes(keyStr);
	
	public static void main(String[] args) {
		System.out.println(decode("AQr2KUtMrFEy XsAAAASnuOf01RwNzCqEw=="));
	}
	
	public static byte[] getSrc(String enc)  {
//		String denc = URLDecoder.decode(enc, "UTF-8");
		byte[] src = Base64.decodeBase64(enc);
		return src;
	}
	
	/**
	 * 解密价格
	 * @param enc
	 * @return
	 */
	public static String decode(String enc)  {
		/*
		 * 处理多次urldecode造成的解密错误
		 */
		if(enc.contains(" ")){
			enc = enc.replace(" ", "+");
		}
		
		byte[] src = getSrc(enc);
		byte[] encrypt = Arrays.copyOfRange(src, 17, 21);

		byte[] buf = new byte[32];
		System.arraycopy(src, 1, buf, 0, 16);
		System.arraycopy(g_key, 0, buf, 16, 16);

		byte[] md5 = new MD5().getMD5ofBytes(buf);

		byte[] h4 = Arrays.copyOfRange(md5, 0, 4);

		long result = 0;

//		for (int i = 0; i < 4; i++) {
//			result |= ((encrypt[i] & 0xFF) ^ (h4[i] & 0xFF)) << ((3 - i) * 8);
//		}

		for (int i = 0; i < 4; i++) {
	         result |= ((encrypt[3-i] & 0xFF) ^ (h4[3-i] & 0xFF)) << ((3 - i) * 8);
	      }
		
		if(result >= 50000 || result < 0){
			log.warn("解密异常，密文：" +enc+" 解密价格：" + result + ",设置价格为0");
			result = 0;
		}
		
		return String.valueOf(result);

	}
	
	/**
	 * 十六进制字符串转byte数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
