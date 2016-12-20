package com.emarbox.dsp.monitor.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Function {

	/**
	 * 返回一个当前日期的字符串 格式为“YYYYMMDD”
	 * 
	 * @return
	 */
	public static String getDateString() {
		Date date = new Date();
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	/**
	 * 返回date的String类型,yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	/**
	 * 将 string类型转换成date类型, yyyyMMddHHmmss
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String time) throws ParseException {
		return new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
	}

	public static String getTimeString(Date date) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}

	/**
	 * 返回当前时间,格式yyyyMMddHHmmss
	 * 
	 * @return 当前时间
	 */
	public static String getNowTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static String getDateString(Date time, String p) {
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		return sdf.format(time);
	}

	public static Date parseTime(String dateStr, String p) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		return sdf.parse(dateStr);
	}

	public static String getYestdayDateString() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}

	public static Date today(){
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);
		now.set(Calendar.MILLISECOND,0);
		return now.getTime();
	}
	
	public static double round(Double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);  
        bd = bd.setScale(scale, roundingMode);  
        double d = bd.doubleValue();  
        bd = null;  
        return d;  
    }  
}