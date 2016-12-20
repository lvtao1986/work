package com.emarbox.dsp.monitor.util.decrypt;

import com.emarbox.dsp.monitor.util.miaozhen.AES;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiaozhenPriceDecoder {

	private static final Logger log = LoggerFactory
			.getLogger(MiaozhenPriceDecoder.class);

	public static final String TOKEN = "1e56ed0d90a24dbbb391acfcb7da5a4f";

	public static String decode(String encodeText) {
		String price = "";
		try {
			String result = AES.decrypt(encodeText, TOKEN);
			if (StringUtils.isNotEmpty(result)) {
				String[] arr = result.split("_");
				if (arr.length > 0) {
					price = arr[0];
				}
			}
			log.debug("miaozhen解密后价格是:" + price + ",解密前字符为：" + encodeText);
			System.out.println("Decrypted: " + result + ",price=" + price);
		} catch (Exception e) {
			log.error("秒针解密失败，原因是:", e);
		}
		return price;
	}

	public static void main(String[] args) {
		
		String str = "hFE7jN-nyKsGbsAZQP7eMP1VelUhNRB_KzybsAEEFXY";
		MiaozhenPriceDecoder.decode(str);
	}

}
