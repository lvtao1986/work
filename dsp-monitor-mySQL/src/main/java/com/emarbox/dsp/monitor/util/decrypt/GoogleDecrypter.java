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
class DecrypterException extends Exception {
	private static final long serialVersionUID = 7919375005268369365L;

	public DecrypterException(String message) {
		super(message);
	}
}

/**
 * Java language sample code for 64 bit value decryption
 */
public class GoogleDecrypter {
	
	private static final Logger log = LoggerFactory.getLogger(GoogleDecrypter.class);
	
	/** The length of the initialization vector */
	private static final int INITIALIZATION_VECTOR_SIZE = 16;
	/** The length of the ciphertext */
	private static final int CIPHERTEXT_SIZE = 8;
	/** The length of the signature */
	private static final int SIGNATURE_SIZE = 4;

	private static byte[] encryptionKeyBytes = { (byte) 0xcb, (byte) 0x37, (byte) 0xb1, (byte) 0xe0, (byte) 0x68, (byte) 0xcf, (byte) 0x65,
			(byte) 0xab, (byte) 0x9a, (byte) 0xd0, (byte) 0x26, (byte) 0x15, (byte) 0x23, (byte) 0x69, (byte) 0xe8, (byte) 0x83,
			(byte) 0x6e, (byte) 0x45, (byte) 0x01, (byte) 0x42, (byte) 0x1c, (byte) 0xd3, (byte) 0xa8, (byte) 0xa4, (byte) 0x9f,
			(byte) 0xda, (byte) 0x36, (byte) 0xc9, (byte) 0x63, (byte) 0xad, (byte) 0x9f, 0x78 };
	private static byte[] integrityKeyBytes = { (byte) 0x26, (byte) 0x72, (byte) 0x42, (byte) 0xab, (byte) 0x4e, (byte) 0xf7, (byte) 0x68,
			(byte) 0x8f, (byte) 0x84, (byte) 0xb6, (byte) 0x5e, (byte) 0x16, (byte) 0x6e, (byte) 0xea, (byte) 0x95, (byte) 0xf4,
			(byte) 0x10, (byte) 0xaf, (byte) 0x33, (byte) 0x81, (byte) 0x13, (byte) 0xb5, (byte) 0x07, (byte) 0x9a, (byte) 0x7c,
			(byte) 0x09, (byte) 0x0e, (byte) 0x63, (byte) 0x67, (byte) 0xbe, (byte) 0x8f, (byte) 0x96 };

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
			SecretKey integrityKey) throws DecrypterException {
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
				throw new DecrypterException("Signature mismatch.");
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

		String websafeB64EncodedCiphertext = "UDHwmQAKYm4KgqsS8JkXSXdhSOwxAvx7xQBMOg";
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
		} catch (DecrypterException e) {
			System.err.println("Failed to decode ciphertext. " + e.getMessage());
			return;
		}
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(plaintext));
		long value = dis.readLong();
		Date timestamp = getTimeFromInitializationVector(initializationVector);
		System.out.println("The value is: " + value + " generated on " + DateFormat.getDateTimeInstance().format(timestamp) + " + "
				+ timestamp.getTime() % 1000);
		System.out.println("    Expected: 709959680 generated on " + "Jun 18, 2009 12:45:59 PM + 123");
	}
}
