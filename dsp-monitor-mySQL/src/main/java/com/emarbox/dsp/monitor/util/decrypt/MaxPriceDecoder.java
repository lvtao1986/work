package com.emarbox.dsp.monitor.util.decrypt;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaxPriceDecoder {
	private static final Logger log = LoggerFactory.getLogger(MaxPriceDecoder.class);
	/** The length of the initialization vector */
	private static final int INITIALIZATION_VECTOR_SIZE = 16;
	/** The length of the ciphertext */
	private static final int CIPHERTEXT_SIZE = 8;
	/** The length of the signature */
	private static final int SIGNATURE_SIZE = 4;
	
	private static byte[] encryptionKeyBytes = {(byte) 0x5c, (byte) 0x7b, (byte) 0x76, (byte) 0x88, (byte) 0x9b, (byte) 0xb4, (byte) 0x22, 
			(byte) 0x3b, (byte) 0x8d, (byte) 0x8f, (byte) 0xcd, (byte) 0x81, (byte) 0x0b, (byte) 0xba, (byte) 0x5c, (byte) 0xec, 
			(byte) 0x49, (byte) 0x2f, (byte) 0x45, (byte) 0xe9, (byte) 0xba, (byte) 0xf1, (byte) 0x49, (byte) 0xa1, (byte) 0x6e, 
			(byte) 0xa5, (byte) 0xe4, (byte) 0xfb, (byte) 0x7f, (byte) 0x06, (byte) 0x10, (byte) 0xdc };
	private static byte[] integrityKeyBytes = { (byte) 0x8e, (byte) 0xb5, (byte) 0x70, (byte) 0xeb, (byte) 0xd8, (byte) 0xf2, (byte) 0xa7, 
			(byte) 0xe5, (byte) 0x56, (byte) 0x16, (byte) 0x8b, (byte) 0x49, (byte) 0x14, (byte) 0xc9, (byte) 0x5b, (byte) 0xfb, 
			(byte) 0xd4, (byte) 0x01, (byte) 0x79, (byte) 0xe2, (byte) 0x73, (byte) 0x18, (byte) 0x23, (byte) 0x9d, (byte) 0x64, 
			(byte) 0xb3, (byte) 0x61, (byte) 0xb7, (byte) 0xa4, (byte) 0x07, (byte) 0x60, (byte) 0x81 };

	/**
	 * Uses hardcoded keys to decrypt an example encoded ciphertext string.
	 */
	public static void main(String[] args) throws IOException {

		String websafeB64EncodedCiphertext = "AAAAAFXdHIYAAAAAAAvMm+lXhZ3+hN1QYf5uag==";
		System.out.println(decode(websafeB64EncodedCiphertext));
	}
	/**
	 * Converts from unpadded web safe base 64 encoding (RFC 3548) to standard
	 * base 64 encoding (RFC 2045) and pads the result.
	 */
	public static String unWebSafeAndPad(String webSafe) {
		String pad = "";
		if ((webSafe.length() % 4) == 2) {
			pad = "==";
		} else if ((webSafe.length() % 4) == 1) {
			pad = "=";
		}
		return webSafe.replace('-', '+').replace('_', '/') + pad;
	}

	/**
	 * Performs the decryption algorithm.
	 */
	public static byte[] decrypt(byte[] initializationVector, byte[] ciphertext, byte[] signature, SecretKey encryptionKey,
			SecretKey integrityKey) throws MaxDecrypterException {
		byte[] plaintext = new byte[CIPHERTEXT_SIZE];
		try {
			// Decrypt the value
			Mac encryptionHmac = Mac.getInstance("HmacSHA1");
			encryptionHmac.init(encryptionKey);
			byte[] encryptionPad = encryptionHmac.doFinal(initializationVector);
			for (int i = 0; i < plaintext.length; i++) {
				plaintext[i] = (byte) (ciphertext[i] ^ encryptionPad[i]);
			}

			// Compute the signature
			Mac integrityHmac = Mac.getInstance("HmacSHA1");
			integrityHmac.init(integrityKey);
			integrityHmac.update(plaintext);
			integrityHmac.update(initializationVector);
			byte[] computedSignature = new byte[SIGNATURE_SIZE];
			System.arraycopy(integrityHmac.doFinal(), 0, computedSignature, 0, computedSignature.length);
			if (!Arrays.equals(signature, computedSignature)) {
				throw new MaxDecrypterException("Signature mismatch.");
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("HmacSHA1 not supported.", e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Key is invalid for this purpose.", e);
		}
		return plaintext;
	}

	public static String decode(String encodeText) {

		try {
			String websafeB64EncodedCiphertext = encodeText;
			String b64EncodedCiphertext = unWebSafeAndPad(websafeB64EncodedCiphertext);
			// Base 64 decode using Apache commons library. You should replace this
			// with whatever you use internally.
			byte[] codeString = Base64.decodeBase64(b64EncodedCiphertext.getBytes("US-ASCII"));
			byte[] initializationVector = new byte[INITIALIZATION_VECTOR_SIZE];
			System.arraycopy(codeString, 0, initializationVector, 0, initializationVector.length);
			byte[] ciphertext = new byte[CIPHERTEXT_SIZE];
			System.arraycopy(codeString, initializationVector.length, ciphertext, 0, ciphertext.length);
			byte[] signature = new byte[SIGNATURE_SIZE];
			System.arraycopy(codeString, initializationVector.length + ciphertext.length, signature, 0, signature.length);
			byte[] plaintext;
			SecretKey encryptionKey = new SecretKeySpec(encryptionKeyBytes, "HmacSHA1");
			SecretKey integrityKey = new SecretKeySpec(integrityKeyBytes, "HmacSHA1");
			plaintext = decrypt(initializationVector, ciphertext, signature, encryptionKey, integrityKey);
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(plaintext));
			long value = dis.readLong();
			return String.valueOf(value);
		} catch (Exception e) {
			log.warn("decode price error , encodeText : "+ encodeText ,e);
		}
		return encodeText;
	}

}
class MaxDecrypterException extends Exception {
	private static final long serialVersionUID = 7919375005268369365L;

	public MaxDecrypterException(String message) {
		super(message);
	}
}
