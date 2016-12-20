package com.emarbox.dsp.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Function {
	private static final Logger log = LoggerFactory
	.getLogger(Function.class);
	/**
	 * 取得给定时间的字符串表示
	 * 
	 * @param ts
	 *            java.sql.Timestamp
	 * @return String 格式 如2003-03-03 19:01:00
	 */
	public static String getDateTimeString(java.sql.Timestamp ts) {
		if (ts == null)
			return "";
		String str = ts.toString();
		if (str.length() >= 19) {
			str = str.substring(0, 19);
		}
		return str;
	}
    
	/**
	 * 取得系统当前的时间，以Timestamp 表示
	 * 
	 * @ return 返回Timestamp对象
	 */
	public static Timestamp getDateTime() {
		java.util.Date date = new java.util.Date();
		return (new java.sql.Timestamp(date.getTime()));
	}

	/**
	 * 取得当前系统时间的字符串表示
	 * 
	 * @return String 格式 如2003-03-03 19:01:00
	 */
	public static String getDateTimeString() {
		java.sql.Timestamp ts = getDateTime();
		return getDateTimeString(ts);
	}

	/**
	 * 返回一个当前日期的字符串 格式为“YYYY-MM-DD”
	 * 
	 * @return
	 */
	public static String getDateString() {
		java.sql.Timestamp ts = getDateTime();
		String str = getDateTimeString(ts);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		return str;
	}

	/**
	 * 返回一个当前日期的字符串 格式为“YYYY-MM-DD”
	 * 
	 * @return
	 */
	public static String getDateString(java.util.Date date) {
		if (date == null)
			return "";
		java.sql.Timestamp ts = new Timestamp(date.getTime());
		String str = getDateTimeString(ts);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		return str;
	}

	/**
	 * 更据年份，月份，得到一个月有多少天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDateMonthString(int year, int month) {
		int day = 0;
		if (((month >= 1) && (month <= 12)) && (year >= 0)) {
			if (month == 2) {
				if ((((year % 4) == 0) && ((year % 100) != 0))
						|| ((year % 400) == 0)) {
					day = 29;
				} else {
					day = 28;
				}
			} else {
				if ((month == 1) || (month == 3) || (month == 5)
						|| (month == 7) || (month == 8) || (month == 10)
						|| (month == 12)) {
					day = 31;
				} else {
					day = 30;
				}
			}
		}
		return day;
	}

	/**
	 * 返回两个String类型时间相差的天数，
	 * 
	 * @param stateTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 相差的天数
	 */

	public static int fromDateStringToDay(String stateTime, String endTime) {
		Date dateState = null; // 定义时间类型
		Date dateEnd = null; // 定义时间类型
		int day = 0; // 返回的天数
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // 定义String类型的格式
		try {
			dateState = inputFormat.parse(stateTime); // 将字符型转换成日期型
			dateEnd = inputFormat.parse(endTime); // 将字符型转换成日期型
			day = (int) ((dateEnd.getTime() - dateState.getTime()) / (1000 * 24 * 3600)); // 共计相差的天数
		} catch (Exception e) {
			log.error("error",e);
		}
		return day; // 返回相差的天数
	}
	/**
	 * 将两个数的商百分数形式展示
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static String percentage(double num1, double num2) {
		String quotient = null;
		if (num2 == 0) {
			quotient = "-";
		} else if (num1 == 0) {
			quotient = "0";
		} else {
			BigDecimal number = BigDecimal.valueOf(num1).divide(
					BigDecimal.valueOf(num2), 4, BigDecimal.ROUND_HALF_UP);// 求两个数的商，并保留4位小数
			// 将小数转换成百分数
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(2);// 这个0的意识是保存结果到小数点后几位，但是特别声明：这个结果已经先＊100了。
			quotient = nf.format(number);// 自动四舍五入。
		}
		return quotient;
	}
	/**
	 * 
	 * @param strDate
	 *            日期
	 * @param dateParrentStr
	 *            日期格式
	 * @return
	 */
	public static java.util.Date parseDate(String strDate, String dateParrentStr) {
		java.text.SimpleDateFormat fmtDate = null;
		java.text.ParsePosition pos = new java.text.ParsePosition(0);
		fmtDate = new java.text.SimpleDateFormat(dateParrentStr);
		java.util.Date dtRet = null;
		try {
			dtRet = fmtDate.parse(strDate, pos);
		} catch (Exception e) {

		}
		return dtRet;
	}
	
	/**
	 * 返回一个当前日期的字符串 格式为“YYYYMMDD”
	 * @param strDate
	 * @param dateParrentStr
	 * @return
	 */
	public static String parseStrTostr(){
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE,0);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyyMMdd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		return str;
	}
	/**
	 * 返回一个当前日期的字符串 格式为“YYYYMMDD”
	 * @param time
	 * @param p
	 * @return
	 */
	public static String getDateString(Date time, String p) {
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		return sdf.format(time);
	}
	
	/**
	 * 返回一个日期的字符串 格式为“YYYYMMDD”
	 * @param strDate
	 * @param dateParrentStr
	 * @return
	 */
	public static String parseDateTostr(Date date){
		String str = new SimpleDateFormat("yyyyMMdd").format(date);
		return str;
	}
	
	/**
	 * 返回一个当前日期的字符串 格式为“YYYYMMDDHHmmss”
	 * @param strDate
	 * @param dateParrentStr
	 * @return
	 */
	public static String parseStrToAllStr(){
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE,0);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyyMMddHHmmss").format(time);
		if (str.length() >= 14) {
			str = str.substring(0, 14);
		}
		return str;
	}

	/**
	 * 返回一个当前日期前一天的字符串 格式为“YYYY-MM-DD”
	 * 
	 * @return
	 */
	public static String getDateYerterdayString() {
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -1);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		return str;
	}

	/**
	 * 返回一个当前日期一个月的字符串 格式为“YYYY-MM-DD”
	 * 
	 * @return
	 */
	public static String getDateBeforeMonthString() {
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.MONTH, -1);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		return str;
	}
	/**
	 * 获取当前时间的小时值
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Integer getDateHour() {
		Date date = new Date();
		return date.getHours();
	}
	/**
	 * 获取当前时间的前一个小时值
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Integer getDateHourBefore() {
		Date date = new Date();
		return date.getHours()-1;
	}
	
	/**
	 * 返回一个当前日期前一天的字符串 格式为“YYYY-MM-DD”
	 * 
	 * @return
	 */
	public static String getDateThreeDayString() {
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -9);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		return str;
	}

	/**
	 * 将时间类型的字符串转换成"YYYY-MM-dd"格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimesString(String date) {
		if (date.length() >= 10) {
			date = date.substring(0, 10);
		}
		String dateTime = null;
		String[] dateTimes = date.split("-");
		for (int i = 0; i < dateTimes.length; i++) {
			if (dateTimes[i].length() == 1) {
				dateTimes[i] = "0" + dateTimes[i];
			}
		}
		dateTime = dateTimes[0] + "-" + dateTimes[1] + "-" + dateTimes[2];
		return dateTime;
	}

	/**
	 * 返回一个当前日期前一天的字符串 格式为“YYYYMMDD”
	 * 
	 * @return
	 */
	public static String getDateYerterdayStrings() {
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -1);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		String[] ss = str.split("-");
		str = "";
		for (int i = 0; i < ss.length; i++) {
			str += ss[i];
		}
		return str;
	}
	/**
	 * 返回一个当前日期的字符串 格式为“YYYYMMDD”
	 * 
	 * @return
	 */
	public static String getDateDayStrings() {
		Calendar cal = Calendar.getInstance();		
//		cal.add(Calendar.DATE, -1);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		String[] ss = str.split("-");
		str = "";
		for (int i = 0; i < ss.length; i++) {
			str += ss[i];
		}
		return str;
	}
	/**
	 * 返回一个当前日期前两天的字符串 格式为“YYYYMMDD”
	 * 
	 * @return
	 */
	public static String getDateBeforeYerterdayStrings() {
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -2);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 10);
		}
		String[] ss = str.split("-");
		str = "";
		for (int i = 0; i < ss.length; i++) {
			str += ss[i];
		}
		return str;
	}
	/**
	 * 返回一个当前日期前七天的字符串 格式为“YYYY-MM-DD”
	 * 
	 * @return
	 */
	public static String getDateBeforeSevenStrings() {
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -7);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(time);
		return str;
	}

	/**
	 * 返回一个当前日期前一天的年份 格式为“YYYY”
	 * 
	 * @return
	 */
	public static String getDateYerterdayOfYearString() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date time = cal.getTime();
		String str = new SimpleDateFormat("yyyy").format(time);
		if (str.length() >= 10) {
			str = str.substring(0, 4);
		}
		return str;
	}

	/**
	 * 获得某一天的开始时间
	 * 
	 * @param someDate
	 * @return someDate 00:00:00
	 */
	public static Date getDayStart(Date someDate) {
		String startTime = Function.getDateString(someDate) + " 00:00:00";
		return Function.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getDayStart(String someDate) {
		String startTime = someDate + " 00:00:00";
		return Function.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获得某一天的结束时间
	 * 
	 * @param someDate
	 * @return someDate 23:59:59
	 */
	public static Date getDayEnd(Date someDate) {
		String startTime = Function.getDateString(someDate) + " 23:59:59";
		return Function.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getDayEnd(String someDate) {
		String startTime = someDate + " 23:59:59";
		return Function.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前日期的昨天
	 * 
	 * @return 昨天的日期，格式：yyyy-MM-dd
	 */
	public static String getYesterday() {
		GregorianCalendar cal = new GregorianCalendar();
		if (cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DAY_OF_YEAR) == 1) {
			cal.roll(Calendar.YEAR, -1);
		}
		cal.roll(Calendar.DAY_OF_YEAR, -1);
		return new java.sql.Date(cal.getTime().getTime()).toString();
	}

	/**
	 * 获取上周的开始日期与结束日期；
	 * 
	 * @return Map<String, String> 键值对，开始日期：key = start；结束日期：key = end
	 */
	public static Map<String, String> getLastWeek() {
		GregorianCalendar cal = new GregorianCalendar();
		int minus = cal.get(GregorianCalendar.DAY_OF_WEEK) - 1;
		if (minus == 0) {// 如果minus == 0，则是星期日
			minus = Calendar.SUNDAY + 6;// 参数值需往后减7天
		}
		// 结束日期
		cal.add(GregorianCalendar.DATE, -minus);
		String end = new java.sql.Date(cal.getTime().getTime()).toString();
		// 开始日期
		cal.add(GregorianCalendar.DATE, -6);
		String start = new java.sql.Date(cal.getTime().getTime()).toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("start", start);
		map.put("end", end);
		return map;
	}

	/**
	 * 获取上月的开始日期与结束日期；
	 * 
	 * @return Map<String, String> 键值对，开始日期：key = start；结束日期：key = end；
	 */
	public static Map<String, String> getLastMonth() {
		GregorianCalendar cale = new GregorianCalendar();
		GregorianCalendar cals = new GregorianCalendar();

		cale.set(cale.get(GregorianCalendar.YEAR), cale
				.get(GregorianCalendar.MONTH), 1);
		cale.add(GregorianCalendar.DATE, -1);
		// 结束日期
		String end = new java.sql.Date(cale.getTime().getTime()).toString();
		// 开始日期
		cals.set(GregorianCalendar.DATE, 1);
		cals.add(GregorianCalendar.MONTH, -1);
		String start = new java.sql.Date(cals.getTime().getTime()).toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("start", start);
		map.put("end", end);
		return map;
	}

	/**
	 * 获取当前月的开始日期与结束日期，开始日期为当前月的01号，结束日期为当前日期的昨天，
	 * 如果当前日期是当前月的01号，那么开始日期和结束日期均为当前月的01号；
	 * 
	 * @return Map<String, String> 键值对，开始日期：key = start；结束日期：key = end；
	 */
	public static Map<String, String> getCurrentMonth() {
		Map<String, String> map = new HashMap<String, String>();
		String currentDate = Function.getDateString();// 当前日期
		// 如果当前日期是本月的01号，则开始和结束日期均返回本月01号
		if (currentDate.substring(8, 10).equals("01")) {
			map.put("start", currentDate);
			map.put("end", currentDate);
			return map;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.add(GregorianCalendar.DATE, -1);
		String end = new java.sql.Date(cal.getTime().getTime()).toString();

		int dayOfMonth = cal.get(GregorianCalendar.DATE);
		cal.add(GregorianCalendar.DATE, -dayOfMonth + 1);
		String start = new java.sql.Date(cal.getTime().getTime()).toString();

		map.put("start", start);
		map.put("end", end);
		return map;
	}

	/**
	 * 获取今天的前七日的开始日期和数据日期；
	 * 
	 * @return Map<String, String> 键值对，开始日期：key = start；结束日期：key = end；
	 */
	public static Map<String, String> getSevenDaysBefore() {
		GregorianCalendar cal = new GregorianCalendar();
		if (cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DAY_OF_YEAR) == 1) {
			cal.roll(Calendar.YEAR, -1);
		}
		cal.roll(Calendar.DAY_OF_YEAR, -1);
		String end = new java.sql.Date(cal.getTime().getTime()).toString();

		cal.roll(Calendar.DAY_OF_YEAR, -6);
		String start = new java.sql.Date(cal.getTime().getTime()).toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("start", start);
		map.put("end", end);
		return map;
	}
	/**
	 * 将百分数转换成小数
	 * 
	 * @param percent
	 * @return
	 */
	public static String percent(String percent) {
		String values = percent;
		values = values.substring(0, values.length() - 1); // 去掉最后一个字符，也就是"%"
		double num1 = Double.parseDouble(values); // 将这个字符串转换为int类型
		double ss= num1 /100; //保留四位小数
		values =String.valueOf(ss);
		return values;
	}
	
	/**
	 * 将小鼠转换成百分数并保留两位小数
	 * @param num
	 * @return
	 */
	public static String doubleToPercent(double num){
		String values ="";
		double num1 = num*100;
		values = df(num1,"0.00");
		return values;
	}

	public static String df(double num,String pattern){
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(num);
	}

	public static double df(double num1, double num2,int  precision) {
		String s = "";
		if (num2 == 0) {
			s = "-";
		} else {
			double num = num1 / num2;
			num=num+0.000000001;
			//四舍五入，保留两位小数
			BigDecimal   b=new BigDecimal(num);			
		    num=b.setScale(precision,BigDecimal.ROUND_HALF_UP).doubleValue();    
			s=String.valueOf(num);
		}
		return Double.parseDouble(s);
	}
	
	/**
	 * 将long数据转换成两位小数的数据
	 * 
	 * @param data
	 * @return
	 */
	public static String df(long num1, long num2) {
		String s = "";
		if (num2 == 0) {
			s = "0";
		} else {
			double num = (double) num1 / num2;
			num=num+0.000000001;
			//四舍五入，保留两位小数
			BigDecimal   b=new BigDecimal(num);			
		    num=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();    
			s=String.valueOf(num);
		}
		return s;
	}
	/**
	 * 将double数据转换成两位小数的数据
	 * 
	 * @param data
	 * @return
	 */
	public static String df(double num1, double num2) {
		String s = "";
		if (num2 == 0) {
			s = "-";
		} else {
			double num = num1 / num2;
			num=num+0.000000001;
			//四舍五入，保留两位小数
			BigDecimal   b=new BigDecimal(num);			
		    num=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();    
			s=String.valueOf(num);
		}
		return s;
	}

	public static String df(int num1, int num2) {
		String s = "";
		if (num2 == 0) {
			s = "-";
		} else {
			double num = (double) num1 / num2;
			num=num+0.000000001;
			//四舍五入，保留两位小数
			BigDecimal   b=new BigDecimal(num);			
		    num=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();    
			s=String.valueOf(num);
		}
		return s;
	}

	public static String df(double num1, int num2) {
		String s = "";
		if (num2 == 0) {
			s = "-";
		} else {
			double num = num1 / num2;
			num=num+0.000000001;
			//四舍五入，保留两位小数
			BigDecimal   b=new BigDecimal(num);			
		    num=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();    
			s=String.valueOf(num);
		}
		return s;
	}

	public static String df(double num1) {
		String s = "";
		if (num1 == 0) {
			s = "-";      
		} else {
			//四舍五入，保留两位小数
			num1=num1+0.000000001;
			BigDecimal   b=new BigDecimal(num1);			
		    num1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();    
			s=String.valueOf(num1);
		}
		return s;
	}

	public static double round(Double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);  
        bd = bd.setScale(scale, roundingMode);  
        double d = bd.doubleValue();  
        bd = null;  
        return d;  
    }

	public static Date today(){
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);
		now.set(Calendar.MILLISECOND,0);
		return now.getTime();
	}
}