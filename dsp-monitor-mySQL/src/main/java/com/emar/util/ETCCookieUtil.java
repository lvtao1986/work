package com.emar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ETCCookieUtil {
	private static final Logger log = LoggerFactory.getLogger(ETCCookieUtil.class);


	private ETCCookieUtil(){}
	
	private static final String[] keys = new String[]{"etc_n", "etc_s", "etc_m", "etc_c", "etc_g", "etc_t", "etc_k", "etc_x", "etc_p"};
	
	private static final ETCCookieUtil instance =  new ETCCookieUtil();
	
	public static ETCCookieUtil getInstance(){
		return instance;
	}
	
	public void setCookie(long pid, Map<String, String> params, HttpServletResponse response){
//	  	 名称：etc_项目ID
//        值：广告渠道||广告来源|广告媒体||广告活动||广告组||广告创意||广告关键词||扩展参数||etc_p
//     有效期：一个月
		try{
			if(params != null){
				StringBuffer s = new StringBuffer();
				for(int i = 0; i < keys.length; i++){
					String val = params.get(keys[i]);
					if(val == null || "".equals(val.trim())){
						val = "$N";
					}
					val = URLEncoder.encode(val, "utf-8");
					if(i == keys.length - 1){
						s.append(val);
					}else{
						s.append(val).append("||");
					}
				}
				Cookie cookie = new Cookie("etc_" + pid, s.toString());
				cookie.setDomain(".emarbox.com");
				cookie.setPath("/");
				cookie.setMaxAge(60 * 60 * 24 * 30);
				response.addCookie(cookie);
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}
	
	public Map<String, String> getCookie(long pid, HttpServletRequest request){
		try{
			Cookie[] cookies = request.getCookies();
			if(cookies != null){
				Map<String, String> params = new HashMap<String, String>();
				for(Cookie cookie : cookies){
					String cName = "etc_" + pid;
					if(cName.equals(cookie.getName())){
						String cValue = cookie.getValue();
						if(cValue != null){
							String[] parts = cValue.split("\\|\\|");
							for(int i = 0; i < keys.length; i++){
								params.put(keys[i], URLDecoder.decode(parts[i], "utf-8"));
							}
							return params;
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
}
