package com.emarbox.dsp.monitor.util;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class IpUtil {

	private static Logger log = Logger.getLogger(IpUtil.class);

	private static final Pattern pattern = Pattern
			.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

	private  static String isDottedIp(String ipString) {
		if (ipString != null) {
			Matcher matcher = pattern.matcher(ipString);
			if (matcher.matches()) {
				return matcher.group();
			}
		}
		return null;
	}

	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = isDottedIp(request.getHeader("Proxy-Client-IP"));
			if (ip != null) {
				return ip;
			}
		} else {
			if (ip.indexOf(",") != -1) {
				String[] s = ip.split(",");
				for (int i = 0; i < s.length; i++) {
					if (s[i] != null && !"".equals(s[i])
							&& !"unknown".equalsIgnoreCase(s[i])) {
						ip = isDottedIp(s[i].trim());
						if (ip != null) {
							return ip;
						}
					}
				}
			} else if (ip.indexOf(";") != -1) {
				String[] s = ip.split(";");
				for (int i = 0; i < s.length; i++) {
					if (s[i] != null && !"".equals(s[i])
							&& !"unknown".equals(s[i])) {
						ip = isDottedIp(s[i].trim());
						if (ip != null) {
							return ip;
						}
					}
				}
			} else {
				if ("unknown".equals(ip)) {
					ip = isDottedIp(ip.trim());
					if (ip != null) {
						return ip;
					}
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = isDottedIp(request.getHeader("WL-Proxy-Client-IP"));
			if (ip != null) {
				return ip;
			}
		}
		if (ip != null && ip.indexOf(' ') != -1) {
			ip = isDottedIp(ip.substring(0, ip.indexOf(' ')));
			if (ip != null) {
				return ip;
			}
		}
		if (ip == null) {
			if (request.getHeader("x-forwarded-for") != null
					|| request.getHeader("Proxy-Client-IP") != null
					|| request.getHeader("WL-Proxy-Client-IP") != null) {
				Enumeration<?> headers = request.getHeaderNames();
				while (headers.hasMoreElements()) {
					String headerName = headers.nextElement().toString();
					log.warn(headerName + " --> "
							+ request.getHeader(headerName));
				}
				log.warn("--------request.getRemoteAddr:"
						+ request.getRemoteAddr() + "--------");
			}
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	//将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
    public  static long ipToLong(String strIp){
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3+1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
   
    //将十进制整数形式转换成127.0.0.1形式的ip地址
    public static String longToIP(long longIp){
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }
    
    /*
     * 获取代理IP
     */
    public static String getProxyIp(HttpServletRequest request) {
    	String ip = request.getHeader("X-Real-IP"); // 部署环境是用nginx做代理，nginx会把代理的ip放在X-Real-IP字段里
    	if(ip==null || "".equals(ip.trim())) { // 部署环境没有用nginx做代理，直接读取ip
    		ip = request.getRemoteAddr();
    	}
    	return ip;
    }
    
    /*
     * 获取真实IP地址：代理服务器会隐藏真实IP，通过解析http请求头找到真实的IP，但是有的代理也会伪造请求头，导致获取的IP是不真实的
     * 好的解决办法是：同时获取代理IP和真实IP
     */
    public static String getRealIp(HttpServletRequest request) {
    	return getIp(request);
    }
    
}