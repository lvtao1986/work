package com.emarbox.dsp.monitor.action;

import com.emarbox.dsp.monitor.service.CampaignService;
import com.emarbox.dsp.monitor.service.MonitorService;
import com.emarbox.dsp.monitor.util.UserAgentUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public abstract class BaseMonitorAction {

    private static final Logger log = Logger.getLogger(BaseMonitorAction.class);
    /**
     * 媒体分割符
     */
    protected static final String mediaSeparator = "X";

    private final Random random = new Random();

    @Autowired
    protected MonitorService monitorService;
    @Autowired
    protected CampaignService campaignService;

    private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取系统的userId,即DMP_USER_ID
     */
    public static String getCookieUserId(HttpServletRequest request) {
        String edc = null;
        String userId = "";
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals("_edc")) {
                    edc = cookie.getValue();
                    if (StringUtils.isNotEmpty(edc)) {
                        userId = edc.split(":")[0];
                    }
                }
            }
        }

        return userId;
    }

    /**
     * 生成clickID,规则:当前时间毫秒数+random.nextInt(1000000000)
     *
     */
    protected String generateClickId() {
        return String.valueOf(new Date().getTime())
                + String.valueOf(random.nextInt(1000000000));
    }

    protected String cookieProcess(HttpServletRequest request,
                                   HttpServletResponse response) {
        String uid = "";
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals("_edc")) {
                    uid = cookie.getValue();
                }
            }
        }

        if ("".equals(uid.trim())) {
            uid = generateClickId();
        }

        Cookie cookie = new Cookie("_edc", uid);
        cookie.setDomain(".emarbox.com");
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
        response.addCookie(cookie);

        return uid;
    }

    protected void setClickId(Cookie[] cookies, HttpServletResponse response, String clickId, String projectId) {

        try {
            //获取已经存在cookie中的clickids
            String clickIds = "";
            if (null != cookies) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals("_etc_cid")) {
                        clickIds = cookie.getValue();
                        break;
                    }
                }
            }
            //如果为空 直接写入当前项目id和clickid
            if (StringUtils.isBlank(clickIds)) {
                String value = projectId + "_" + clickId;
                setClickIdInCookie(response, value);
            }
            //如果已存的数据中不包含'_'和'|' 就认为是旧数据(只存了一个clickid),用现在的规则替换
            else if (!clickIds.contains("_")) {
                String value = projectId + "_" + clickId;
                setClickIdInCookie(response, value);
            }
            //已存的数据是新规则数据,即 pid1_clickid1|pid2_clickid2|pid3_clickid3...
            else {
                //clickList的每个元素用于存储合格的,并且在有消息之内的项目的clickid值字串'pid1_clickid1'
                List<String> clickList = new ArrayList<String>();
                //将cookie的_etc_cid取出来的值用'|'分割
                String clickStrArr[] = clickIds.split("\\|");
                //变量 判断当前pid的clickid是否已写入cookie
                boolean isWrite = false;
                for (int i = 0; i < clickStrArr.length; i++) {
                    //获取出当前元素,用'_'分割
                    String clickStr = clickStrArr[i];
                    String oneStrArr[] = clickStr.split("_");

                    //给取出来的pid,clickid赋值
                    String oneProjectId = "";
                    String oneClickId = "";
                    String oneClickTime = "";
                    if (oneStrArr.length == 2) {
                        oneProjectId = oneStrArr[0];
                        oneClickId = oneStrArr[1];
                        //用clickid给点击时间赋值,用于30天超时验证
                        if (oneClickId.length() > 13) oneClickTime = oneClickId.substring(0, 13);
                    }

                    String value = "";
                    //在时间范围内,并且与当前pid不相等,就写入从cookie中取出的值
                    if (checkCookieTime(oneClickTime, 30) && !oneProjectId.equals(projectId)) {
                        value = oneProjectId + "_" + oneClickId;
                    }
                    //在时间范围内,并且与当前项目id相等,就写入projectId和新的clickId值
                    else if (checkCookieTime(oneClickTime, 30) && oneProjectId.equals(projectId)) {
                        value = projectId + "_" + clickId;
                        isWrite = true;
                    }
                    //value不为空,写入clickList
                    if (StringUtils.isNotBlank(value)) clickList.add(value);
                }

                //如果cookie不存在当前pid的clickid,就写入一个
                if (!isWrite) clickList.add(projectId + "_" + clickId);

                //写入cookie
                StringBuilder value = new StringBuilder();
                if (clickList.size() > 0) {
                    for (int i = 0; i < clickList.size(); i++) {
                        value.append(clickList.get(i));
                        if (i < clickList.size() - 1) value.append("|");
                    }
                    setClickIdInCookie(response, value.toString());
                }
            }
        } catch (Exception e) {
            log.warn("setClickId error : " + e.getLocalizedMessage(),e);
        }

    }

    /**
     * 验证userAgent参数
     *
     * @param userAgent 浏览器agent信息
     * @return 是否为有效
     */
    protected boolean checkUserAgent(String userAgent) {
        return UserAgentUtil.checkUserAgent(userAgent);
    }

    protected boolean checkCookieTime(String cookieTime, int days) {
        long now = Calendar.getInstance().getTimeInMillis();
        long timeAllow = days * 1000L * 60L * 60L * 24L;
        long timeCha = now - NumberUtils.toLong(cookieTime);
        return timeCha < timeAllow;
    }

    protected void setClickIdInCookie(HttpServletResponse response, String value) {
        if (StringUtils.isBlank(value)) return;

        Cookie cookie = new Cookie("_etc_cid", value);
        cookie.setDomain(".emarbox.com");
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
    }

    /**
     * 验证网址Url
     * @param url 待验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    /*
     * http://equity-vip.tmall.com/agent/mobile.htm?spm=a225j.8120520.0.0.YxXlnR&agentId=2902&_bind=true 校验不过去
     */
    public static boolean checkUrl(String url)
    {
        if(StringUtils.isBlank(url)){
            return false;
        }
        /*String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.compile(regex).matcher(url.trim()).lookingAt();*/
        return true;
    }

}
