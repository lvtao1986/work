package com.emarbox.dsp.monitor.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.emarbox.dsp.monitor.schedule.HandleClickLogTask;
import com.emarbox.dsp.monitor.schedule.HandleImpressionLogTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emarbox.dsp.monitor.data.ClickLogQueue;
import com.emarbox.dsp.monitor.data.ImpressionLogQueue;

/**
 * monitor队列状态action
 */
@Controller("runningStatusAction")
@RequestMapping(value = "/status")
public class RunningStatusAction extends BaseMonitorAction {
    private Logger log = LoggerFactory.getLogger(RunningStatusAction.class);

    @RequestMapping(value = "/get")
    public void impressionMonitor( HttpServletResponse response) {
        try {
            response.setContentType("text/html; charset=utf-8");

            PrintWriter writer = response.getWriter();
            long imSize = ImpressionLogQueue.counter.getCount();
            long clSize = ClickLogQueue.counter.getCount();

            writer.println("展现数据队列：" + imSize);
            writer.println("点击数据队列：" + clSize);
            writer.println("合计运算数：" + (HandleClickLogTask.todaySumCount + HandleImpressionLogTask.todaySumCount));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

    }

}
