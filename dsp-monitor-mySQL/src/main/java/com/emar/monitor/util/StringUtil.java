package com.emar.monitor.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class StringUtil {
	private StringUtil(){}
	public synchronized static String parse(Object s) throws UnsupportedEncodingException{
		if(s == null || "".equals(s)){
			return "$N";
		}
		return URLDecoder.decode(s.toString().trim(), "utf-8");
	}
}
