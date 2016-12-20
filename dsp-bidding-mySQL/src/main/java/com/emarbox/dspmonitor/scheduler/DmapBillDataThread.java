package com.emarbox.dspmonitor.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.util.DmpLogWriter;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emarbox.dspmonitor.billing.util.CostUtil;
import com.emarbox.dspmonitor.data.CampaignCost;
import com.emarbox.dspmonitor.data.DMapService;
import org.apache.commons.lang.StringUtils;

import app.base.util.ConfigUtil;

public class DmapBillDataThread extends BasicKafkaConsumer {
    protected static final Logger log = LoggerFactory.getLogger(DmapBillDataThread.class);
    //点击处理记录用
    private final Logger billingClickLogger = LoggerFactory.getLogger("billingClick");
    //dmp日志用logger
    private final DmpLogWriter dmpLogger = DmpLogWriter.getInstance();

    private static final char separatorA = '\u0001';

    private long accountId; //线程需要处理的账号ID

    DmapBillDataThread(long accountId) {
        this.accountId = accountId;
    }

    @Override
    protected boolean handleMessageList(List<String> costLogList) {

        try {

            try {
                List<CampaignCost> dmapCostList = new ArrayList<CampaignCost>();
                for (String costLog : costLogList) {
                    String[] logArray = costLog.split(",", -1);
                    String requestId = logArray[0];
                    double cost = Double.parseDouble(logArray[16]);//原先花费为数组的第15个元素,后按照DMP要求计费日志增加IP字段，所以花费后延一位。
                    String actionType = logArray[2];
                    String campaignId = logArray[4];
                    String supplierId = logArray[1];
                    String statTime = logArray[6];
                    String dspCost = logArray[3];
                    String rtbCost = logArray[7];
                    String media = logArray[9];
                    String apid = logArray[10];
                    String proxyIp = logArray[15];//该位置原为花费信息,目前由IP信息占据。

                    String projectId = "";
                    try {
                        BillingCampaign campaign = FinalData.STATIC_CAMPAIGN_MAP.get(NumberUtils.createLong(campaignId));
                        if (campaign != null) {
                            projectId = campaign.getProjectId().toString();
                        }
                    } catch (Exception e) {
                        log.warn("获取活动对应项目ID失败"+e.getMessage(),e);
                    }
                    String creativeId = logArray[5];

                    CampaignCost dmapCost = new CampaignCost();
                    dmapCostList.add(dmapCost);
                    dmapCost.setCampaignId(Long.parseLong(campaignId));
                    dmapCost.setSupplierId(Long.parseLong(supplierId));
                    dmapCost.setStatDate(statTime.substring(0, 12));
                    if (actionType != null) {
                        if (actionType.equals("impression")) {
                            dmapCost.setImpression(1L);
                            dmapCost.setClick(0L);

                            if (StringUtils.isBlank(dspCost)) {
                                dmapCost.setDspCost(0d);
                            } else {
                                try {
                                    dmapCost.setDspCost(CostUtil.calBiddingCost(supplierId, Double.parseDouble(dspCost)));
                                } catch (NumberFormatException e) {
                                    dmapCost.setDspCost(0d);
                                    log.warn(e.getMessage(), e);
                                }

                            }

                            if (StringUtils.isBlank(rtbCost)) {
                                dmapCost.setRtbCost(0d);
                            } else {
                                dmapCost.setRtbCost(CostUtil.calRtbCost(rtbCost));
                            }
                        } else if (actionType.equals("click")) {
                            dmapCost.setClick(1L);
                            dmapCost.setImpression(0L);
                            dmapCost.setRtbCost(0d);
                            dmapCost.setDspCost(0d);
                        }
                    }
                    dmapCost.setCost(cost);

                    if (cost > 0) {
                        //写入dmap日志文件
                        StringBuffer str = new StringBuffer();
                        str.append(requestId).append(",");
                        str.append(cost).append(",");
                        str.append(actionType).append(",");
                        str.append(campaignId).append(",");
                        str.append(supplierId).append(",");
                        str.append(statTime).append(",");
                        str.append(media).append(",");
                        str.append(apid).append(",");
                        str.append(projectId).append(",");
                        str.append(creativeId).append(",");
                        str.append(proxyIp);//新增IP信息
                        billingClickLogger.info(str.toString());

                        //写入dmp日志文件
                        StringBuffer dmpStr = new StringBuffer();
                        dmpStr.append(requestId).append(separatorA);
                        dmpStr.append(cost).append(separatorA);
                        dmpStr.append(actionType).append(separatorA);
                        dmpStr.append(campaignId).append(separatorA);
                        dmpStr.append(supplierId).append(separatorA);
                        dmpStr.append(statTime).append(separatorA);
                        dmpStr.append(media).append(separatorA);
                        dmpStr.append(apid).append(separatorA);
                        dmpStr.append(projectId).append(separatorA);
                        dmpStr.append(creativeId).append(separatorA);
                        dmpStr.append(proxyIp);//新增IP信息
                        dmpLogger.info(supplierId, dmpStr.toString());
                    }

                }

                DMapService.getInstance().saveData(dmapCostList); //结算记录保存到dmap所需数据文件中
            } catch (Exception e) {
                log.error("保存计费数据到dmap文件时出现异常:" + e.getMessage(), e);
            }

            return true;
        } catch (Exception e) {
            log.error("已结算队列统计写入dmap文件时出现异常：" + e.getMessage(), e);
            return false;
        }

    }

    public long getAccountId() {
        return accountId;
    }

    @Override
    public String getTopic() {
        return ConfigUtil.getString("kafka.topic.name.prefix.billed") + accountId;
    }


    @Override
    protected String getClientName() {
        return "billed_dmap_data_" + accountId;
    }

    @Override
    protected int getPartition() {
        return 0;
    }

}
