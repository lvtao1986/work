package com.emarbox.dsp.monitor.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 淘宝pmp静态创意代码生成服务
 */
@Controller("taobaoPmpAdsAction")
public class TaobaoPMPAds {
	
	@Autowired
	private TaobaoPMPMonitorAction taobaoPMPMonitorAction;

	private static final Logger log = LoggerFactory.getLogger(TaobaoPMPAds.class);

	@RequestMapping(value="/taobaopmp.do")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String info = req.getParameter("info");
			String guid = req.getParameter("guid");
			String sign = req.getParameter("sign");
			String ctype = req.getParameter("ctype");
			String cwidth = req.getParameter("cwidth");
			String cheight = req.getParameter("cheight");
			String csrc = req.getParameter("csrc");
			String url = req.getParameter("url");
			String price = req.getParameter("c");
			String apid = req.getParameter("apid");
			String ct = req.getParameter("ct");
			String rt = req.getParameter("rt");
			String rt2 = req.getParameter("rt2");
			String bt = req.getParameter("bt");
			String ut = req.getParameter("ut");			
			String tanxclick = req.getParameter("tanxclick");
			String dmpId = req.getParameter("dmpid");
			String td = req.getParameter("td");
			String gid = req.getParameter("gid");

			String rqUrl = null;
			
			if(info==null){
				info="";
				rqUrl = req.getQueryString();
			}
			if(guid==null){
				guid="";
				rqUrl = req.getQueryString();
			}
			if(url==null){
				url="";
				rqUrl = req.getQueryString();
			}
			if (apid == null) {
				apid = "";
				rqUrl = req.getQueryString();
			}
			if(price==null){
				price = "";
				rqUrl = req.getQueryString();
			}
			if(ctype==null){
				ctype="image";
				rqUrl = req.getQueryString();
			}
			
			if(ct==null){
				ct = "";
			}
			if(rt==null){
				rt= "";
			}
			if(rt2 == null){
				rt2="";
			}
			if(rqUrl != null){
				String agent = req.getHeader("user-Agent");
				log.info("error request : " + rqUrl + " user-agent : " +  agent);
			}
			
			String clickUrl = "http://m.emarbox.com/taobaopmp/click?info="
					+ URLEncoder.encode(info, "UTF-8") + "&c="
					+ URLEncoder.encode(price, "UTF-8") + "&sign=" + sign
					+ "&apid=" + URLEncoder.encode(apid, "UTF-8") + "&url="
					+ URLEncoder.encode(url, "UTF-8") + "&guid="
					+ URLEncoder.encode(guid, "UTF-8") + "&ct="+ct + "&rt="+ rt+"&rt2="+rt2+"&td="+td;
			String cmUrl = "http://cm.emarbox.com/_cm?pt=5020&tid="
					+ URLEncoder.encode(guid, "UTF-8") + "&ver=1&edmpid="+dmpId+"&limit";
			
			PrintWriter writer = resp.getWriter();
			writer.println("<!DOCTYPE html>");
			writer.println("<html><head></head><body>");
			if (ctype.equals("image")) {
				this.imageCreative(writer, cwidth, cheight, csrc, clickUrl,cmUrl,tanxclick);
			} else if (ctype.equals("flash")) {
				this.flashCreative(writer, cwidth, cheight, csrc, clickUrl,cmUrl,tanxclick);
			}else{
				log.warn("ctyp error:"+ctype);
			}
			writer.println("</body></html>");
			writer.flush();
			writer.close();
			
			taobaoPMPMonitorAction.impressionMonitor(info, price, guid, sign, apid, "", ct, rt, rt2,td,bt,ut,gid,req,resp); //记录展现
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
		}
		
		
		
	}
	
	private void imageCreative(PrintWriter writer,String cwidth,String cheight,String csrc,String clickUrl,String cmUrl,String tanxclick) throws UnsupportedEncodingException{
		if( !("").equals(cheight) && cheight != null){
			writer.println("   <style type='text/css'> ");
			writer.println("      .dsp-logo-v { ");
			writer.println("  display:block;");
			writer.println(" width:19px; ");
			writer.println(" height:15px; ");
			writer.println(" background:url(http://img.emarbox.com/dsp/img/e.png) no-repeat 0 0; ");
			writer.println(" } ");
			writer.println(" a.dsp-logo-v:hover {");
			writer.println(" background:url(http://img.emarbox.com/dsp/img/dsp.png) no-repeat 0 0; ");
			writer.println(" width:76px; ");
			writer.println(" height:15px; ");
			writer.println(" } ");
			writer.println(" </style> ");
			writer.println(" <div style='position:relative;width:"+cwidth+"px; height:"+cheight+"px;'> ");
			Long heightDiv = (Long.valueOf(cheight)-15);
			writer.println(" <div style='position:absolute; top:"+heightDiv+"px; right:0; z-index:999;'><a href='http://www.emarbox.com' class='dsp-logo-v' target='_BLANK'></a></div> ");
			
			writer.println("<a style='border:0px solid #f00; display:block; width:"+cwidth+"px; height:"+cheight+"px;text-decoration: none;'  href='"+tanxclick+URLEncoder.encode(clickUrl,"UTF-8")+"' target='_blank'>"); 
			writer.println("<img src='"+csrc+"' border='0' width='"+cwidth+"' height='"+cheight+"'/>");
			writer.println(" </a> ");
			writer.println("<img src='"+cmUrl+"' style='display:none;' border='0' width='1' height='1' />");
			
			writer.println(" </div> ");
		}else{
			writer.println("<a style='border:0px solid #f00; display:block; width:"+cwidth+"px; height:"+cheight+"px;text-decoration: none;'  href='"+clickUrl+"' target='_blank'>"); 
			writer.println("<img src='"+csrc+"' border='0' width='"+cwidth+"' height='"+cheight+"'/>");
			writer.println(" </a> ");
			writer.println("<img src='"+cmUrl+"' style='display:none;' border='0' width='1' height='1' />");
		}
	}
	
	private void flashCreative(PrintWriter writer,String cwidth,String cheight,String csrc,String clickUrl,String cmUrl,String tanxclick) throws UnsupportedEncodingException{
		if( !("").equals(cheight) && cheight != null){
			writer.println("   <style type='text/css'> ");
			writer.println("      .dsp-logo-v { ");
			writer.println("  display:block;");
			writer.println(" width:19px; ");
			writer.println(" height:15px; ");
			writer.println(" background:url(http://img.emarbox.com/dsp/img/e.png) no-repeat 0 0; ");
			writer.println(" } ");
			writer.println(" a.dsp-logo-v:hover {");
			writer.println(" background:url(http://img.emarbox.com/dsp/img/dsp.png) no-repeat 0 0; ");
			writer.println(" width:76px; ");
			writer.println(" height:15px; ");
			writer.println(" } ");
			writer.println(" </style> ");
			writer.println(" <div style='position:relative;width:"+cwidth+"px; height:"+cheight+"px;'> ");
			Long heightDiv = (Long.valueOf(cheight)-15);
			writer.println(" <div style='position:absolute; top:"+heightDiv+"px; right:0; z-index:999;'><a href='http://www.emarbox.com' class='dsp-logo-v' target='_BLANK'></a></div> ");
			
			
			writer.println("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0' width='"+cwidth+"' height='"+cheight+"'>");
			writer.println("<param name='movie' value='"+csrc+"' />");
			writer.println("<param name='quality' value='high' />");
			writer.println("<param name='wmode' value='opaque' />");
			writer.println("<embed src='"+csrc+"' wmode='opaque' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='"+cwidth+"' height='"+cheight+"'></embed>");
			writer.println("</object>");
			writer.println("<a style='border:0px solid #f00; display:block;position: relative; z-index:900; cursor: pointer; margin-top:-"+cheight+"px; width:"+cwidth+"px; height:"+cheight+"px; ' href='"+tanxclick+URLEncoder.encode(clickUrl,"UTF-8")+"' target='_blank' >");
			writer.println("<img border='0' src='http://img.emarbox.com/dsp/img/flash_blank.gif' width='"+cwidth+"' height='"+cheight+"' border='0' /></a>");
			writer.println("<img src='"+cmUrl+"' style='display:none;' border='0' width='1' height='1' />");
			
			
			writer.println(" </div> ");
		}else{
			writer.println("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0' width='"+cwidth+"' height='"+cheight+"'>");
			writer.println("<param name='movie' value='"+csrc+"' />");
			writer.println("<param name='quality' value='high' />");
			writer.println("<param name='wmode' value='opaque' />");
			writer.println("<embed src='"+csrc+"' wmode='opaque' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='"+cwidth+"' height='"+cheight+"'></embed>");
			writer.println("</object>");
			writer.println("<a style='border:0px solid #f00; display:block;position: relative; z-index:900; cursor: pointer; margin-top:-"+cheight+"px; width:"+cwidth+"px; height:"+cheight+"px; ' href='"+tanxclick+URLEncoder.encode(clickUrl,"UTF-8")+"' target='_blank' >");
			writer.println("<img border='0' src='http://img.emarbox.com/dsp/img/flash_blank.gif' width='"+cwidth+"' height='"+cheight+"' border='0' /></a>");
			writer.println("<img src='"+cmUrl+"' style='display:none;' border='0' width='1' height='1' />");
			
		}
	}

}
