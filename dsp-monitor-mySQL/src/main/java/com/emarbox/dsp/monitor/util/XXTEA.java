
package com.emarbox.dsp.monitor.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.util.BufferRecycler;

public final class XXTEA {
    public static void main(String[] args) throws Exception {
    	
    	String ifile = "/Users/mrzhu/Downloads/a.txt";
    	
    	 BufferedReader br = new BufferedReader(new FileReader(ifile) );
    	while(true){
    	 String line = br.readLine();
    	 if(StringUtils.isNotBlank(line))
    	System.out.println(line+","+decrypt(line));
    	}
    	
	}
    
    public static final String encrypt(String text){
    	String result = Base64.encodeBase64URLSafeString(text.getBytes());
    	char[] arr =result.toCharArray();
    	char tmp = arr[0];
    	arr[0] = arr[arr.length-1];
    	arr[arr.length-1] = tmp;
    	result = new String(arr);
    	
    	return "000000"+result;
    }
    public static final String decrypt(String text){
    	text = text.replace("000000", "");
    	char[] arr =text.toCharArray();
    	char tmp = arr[0];
    	arr[0] = arr[arr.length-1];
    	arr[arr.length-1] = tmp;
    	return new String(Base64.decodeBase64(new String(arr)));
    }
}
