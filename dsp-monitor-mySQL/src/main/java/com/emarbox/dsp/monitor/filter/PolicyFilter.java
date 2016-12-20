package com.emarbox.dsp.monitor.filter;

import java.io.IOException;
import java.util.logging.LogRecord;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.emarbox.dsp.monitor.util.Config;

/**
 * @author zhoulong
 *
 */
public class PolicyFilter implements Filter {

	private static String policyStr = "";

	private static final Logger log = Logger.getLogger(PolicyFilter.class);
	
	public PolicyFilter() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		response.setHeader("P3P", policyStr);
		chain.doFilter(servletRequest, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		try {
			policyStr = Config.getString("http.p3p.policy.header");
		} catch (Exception e) {
			log.error(e);
			policyStr = "policyref=\"/w3c/p3p.xml\", CP=\"NOI DEV PSA PSD IVA IVD OTP OUR OTR IND OTC \"";
		}
	}

	public boolean isLoggable(LogRecord record) {
		return false;
	}
}