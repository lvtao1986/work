package com.emarbox.dsp.monitor.util.decrypt;

// Copyright 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Decrypter is sample code showing the steps to decrypt and verify 64-bit
// values. It uses the Base 64 decoder from the Apache commons project
// (http://commons.apache.org).

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Exception thrown by Decrypter if the ciphertext cannot successfully be
 * decrypted.
 */
class AmaxDecrypterException extends Exception {
	private static final long serialVersionUID = 7919375005268369365L;

	public AmaxDecrypterException(String message) {
		super(message);
	}
}

/**
 * Java language sample code for 64 bit value decryption
 */
public class AmaxDecrypter {
	
	private static final Logger log = LoggerFactory.getLogger(AmaxDecrypter.class);
	
	/** The length of the initialization vector */
	private static final int INITIALIZATION_VECTOR_SIZE = 16;
	/** The length of the ciphertext */
	private static final int CIPHERTEXT_SIZE = 8;
	/** The length of the signature */
	private static final int SIGNATURE_SIZE = 4;

//	//e_key	加密密钥（32 个字节 - 在创建帐户时提供）
//	private static byte[] encryptionKeyBytes = { (byte) 0xac, (byte) 0x8e, (byte) 0x46, (byte) 0x91, (byte) 0x40, (byte) 0x99, (byte) 0xa9,
//		(byte) 0x14, (byte) 0x13, (byte) 0xb7, (byte) 0x22, (byte) 0x82, (byte) 0xfe, (byte) 0x2e, (byte) 0x55, (byte) 0x42,
//		(byte) 0x7c, (byte) 0x80, (byte) 0xf7, (byte) 0xcf, (byte) 0xa2, (byte) 0xdd, (byte) 0x47, (byte) 0xc2, (byte) 0x50,
//		(byte) 0x19, (byte) 0x7b, (byte) 0xc0, (byte) 0x6e, (byte) 0x2d, (byte) 0x85, 0x36 };
//	//i_key	完整性密钥（32 个字节 - 在创建帐户时提供）
//	private static byte[] integrityKeyBytes = { (byte) 0x0b,(byte) 0xb7,(byte) 0x46,(byte) 0x48,(byte) 0x47,(byte) 0xf6,(byte) 0x85,(byte) 0xfa,
//	    (byte) 0xdc,(byte) 0x9e,(byte) 0xe9,(byte) 0x54,(byte) 0xaa,(byte) 0x21,(byte) 0xd5,(byte) 0x46,(byte) 0xcd,(byte) 0x54,
//	    (byte) 0x98,(byte) 0xa9,(byte) 0x19,(byte) 0x2B,(byte) 0x91,(byte) 0xca,(byte) 0x39,(byte) 0x25,(byte) 0x8e,(byte) 0xce,
//	    (byte) 0x63,(byte) 0xfb,(byte) 0xfc,(byte) 0x7a };
	//e_key	加密密钥（32 个字节 - 在创建帐户时提供）
		private static byte[] integrityKeyBytes = { (byte) 0x67, (byte) 0xFF, (byte) 0x4F, (byte) 0xBA, (byte) 0x46, (byte) 0x01,
			(byte) 0x16, (byte) 0xC6, (byte) 0xBE, (byte) 0x63, (byte) 0x51, (byte) 0xB0, (byte) 0x3F, (byte) 0x78, (byte) 0x32, 
			(byte) 0x0A, (byte) 0xE7, (byte) 0x5D, (byte) 0x7A, (byte) 0x2C, (byte) 0x8B, (byte) 0xF6, (byte) 0x86, (byte) 0x08, 
			(byte) 0x4D, (byte) 0x8E, (byte) 0xE2, (byte) 0x49, (byte) 0xA9, (byte) 0x11, (byte) 0x64, (byte) 0x4F };
		//i_key	完整性密钥（32 个字节 - 在创建帐户时提供）
		private static byte[] encryptionKeyBytes = { (byte) 0x3E, (byte) 0x69, (byte) 0x5F, (byte) 0x27, (byte) 0x18, (byte) 0xD2, 
			(byte) 0xA9, (byte) 0x78, (byte) 0x30, (byte) 0xD0, (byte) 0x74, (byte) 0x10, (byte) 0x1C, (byte) 0xE2, (byte) 0x42, 
			(byte) 0x37, (byte) 0x74, (byte) 0x3D, (byte) 0xCA, (byte) 0x56, (byte) 0xE8, (byte) 0xBC, (byte) 0xDD, (byte) 0xC0, 
			(byte) 0xA8, (byte) 0x42, (byte) 0x44, (byte) 0xB3, (byte) 0x06, (byte) 0xC7, (byte) 0x2E, (byte) 0xEE };
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
			SecretKey integrityKey) throws AmaxDecrypterException {
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
				throw new AmaxDecrypterException("Signature mismatch.");
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("HmacSHA1 not supported.", e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Key is invalid for this purpose.", e);
		}
		return plaintext;
	}

	/**
	 * Parses the timestamp out of the initialization vector. Note: this method
	 * loses precision. java.util.Date only holds the date to millisecond
	 * precision while the initialization vector contains a timestamp with
	 * microsecond precision.
	 */
	public static Date getTimeFromInitializationVector(byte[] initializationVector) {
		ByteBuffer buffer = ByteBuffer.wrap(initializationVector);
		long seconds = buffer.getInt();
		long micros = buffer.getInt();
		return new Date((seconds * 1000) + (micros / 1000));
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

	/**
	 * Uses hardcoded keys to decrypt an example encoded ciphertext string.
	 */
	public static void main(String[] args) throws IOException {

//		String websafeB64EncodedCiphertext = "UDHwmQAKYm4KgqsS8JkXSXdhSOwxAvx7xQBMOg";
		String websafeB64EncodedCiphertext = "VEXLfQAXIGlURct9ABcgabG_HxDoo28by7ZNcg";
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
		try {
			plaintext = decrypt(initializationVector, ciphertext, signature, encryptionKey, integrityKey);
		} catch (AmaxDecrypterException e) {
			System.err.println("Failed to decode ciphertext. " + e.getMessage());
			return;
		}
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(plaintext));
		long value = dis.readLong();
		Date timestamp = getTimeFromInitializationVector(initializationVector);
		System.out.println("The value is: " + value + " generated on " + DateFormat.getDateTimeInstance().format(timestamp) + " + "
				+ timestamp.getTime() % 1000);
		System.out.println("    Expected: 709959680 generated on " + "Jun 18, 2009 12:45:59 PM + 123");
		
		System.out.println(decode("VEXLfQAXIGlURct9ABcgabG_HxDoo28by7ZNcg"));
	}
}
