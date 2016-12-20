package com.emarbox.dsp.monitor.util.decrypt;

import com.emarbox.dsp.monitor.util.miaozhen.AES;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YoukuPriceDecoder {

	private static final Logger log = LoggerFactory
			.getLogger(YoukuPriceDecoder.class);

	public static final String TOKEN = "ed50d78865ec4a31b9b0f7639310b960";

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
			log.debug("youku解密后价格是:" + price + ",解密前字符为：" + encodeText);
			System.out.println("Decrypted: " + result + ",price=" + price);
		} catch (Exception e) {
			log.error("优酷解密失败，原因是:", e);
		}
		return price;
	}

	public static void main(String[] args) {
		String str = "sIMG_PUA_2fsohcoNFPKYHVu9q-Rne2Uut9ZVfHt7fk";
		YoukuPriceDecoder.decode(str);
	}

}
