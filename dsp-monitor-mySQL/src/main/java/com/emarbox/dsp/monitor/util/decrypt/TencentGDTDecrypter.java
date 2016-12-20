package com.emarbox.dsp.monitor.util.decrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.emarbox.dsp.monitor.util.Config;

public class TencentGDTDecrypter {
	private static String keyStr = Config.getString("tencentGDT.public.key");

	private static String AES128Decode(byte[] encoded_price, String token)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] raw = token.getBytes("utf-8");
		SecretKeySpec key_spec = new SecretKeySpec(raw, "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key_spec);
		byte[] decoded_price = cipher.doFinal(encoded_price);
		return new String(decoded_price, "utf-8");
	}

	private static byte[] Base64Decode(String encoded_price) throws UnsupportedEncodingException {
		return Base64.decodeBase64(encoded_price);
	}

	public static String DecodePrice(String encoded_price)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] after_base64_decode = Base64Decode(encoded_price);
		return AES128Decode(after_base64_decode, keyStr);
	}

	public static void main(String[] args) {
		TencentGDTDecrypter decoder = new TencentGDTDecrypter();
		try {
			String decoded_str = decoder.DecodePrice(
					"O-0mVdLfTGAnt3TClMitSg==");
			String price_str = decoded_str.trim();
			int price = Integer.parseInt(price_str);
			assert price == 2301;

			decoded_str = decoder.DecodePrice("VwSSFElLxs3wWy0LMsTy5Q==");
			price_str = decoded_str.trim();
			price = Integer.parseInt(price_str);
			assert price == 2201;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	}
}
