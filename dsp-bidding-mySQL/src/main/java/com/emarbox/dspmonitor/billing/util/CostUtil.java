package com.emarbox.dspmonitor.billing.util;

import app.base.util.ConfigUtil;
import app.common.util.LogUtil;
import app.common.util.Logs;

import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.domain.SendReport;
import com.emarbox.dspmonitor.billing.domain.SupplierInfo;
import com.google.common.collect.ImmutableList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 费用计算 工具类
 *
 * @author zhujinju
 */
public abstract class CostUtil {
    private static final Logger log = LoggerFactory.getLogger(CostUtil.class);
    private static Logs costLogicLog = LogUtil.getLog("costLogicLog");

    static {
        ConfigUtil.addConfig("config");
    }

    /**
     * 回头客比winnerrice最大倍数
     */
    public static double retargetCostArg = Double.parseDouble(ConfigUtil.getString("cost.arg.retarget")); //
    /**
     * 非回头客比winnerPrice最大倍数
     */
    public static double normalCostArg = Double.parseDouble(ConfigUtil.getString("cost.arg.normal"));
    /**
     * 非回头客cpc下线
     */
    private static double minCpcCostNormal = Double.parseDouble(ConfigUtil.getString("cost.cpc.min.cost.normal"));
    /**
     * 回头客cpc下线
     */
    private static double minCpcCostRetarget = Double.parseDouble(ConfigUtil.getString("cost.cpc.min.cost.retarget"));

    /**
     * 计算用户花费
     *
     * @param actionType 展现/点击
     * @param costType   CPM/CPC
     * @param maxPrice   活动最高限价
     * @param valuePrice rtb出价,单词展现价格
     * @param ctr        预估点击率
     * @param isLogCost  是否记录cost变更记录
     * @return 用户花费
     */
    private static double calCost(String requestId, String campaignId, String actionType, String costType, String maxPrice, double valuePrice, String ctr, String adChannleId, double biddingCost, boolean retarget, double retargetCostArg, double normalCostArg, boolean td, boolean isLogCost, double rtbCost) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(6);

        costType = costType.trim().toLowerCase();
        actionType = actionType.trim().toLowerCase();
        //判断不用计费的情况
        if ("cpm".equals(costType) && !"impression".equals(actionType)) {
            return 0;
        } else if ("cpc".equals(costType) && !"click".equals(actionType)) {
            return 0;
        }

        Double minProfitRate = 0.35; //最小毛利默认值
        boolean profitControl = false; //是否控制毛利
        try{
            long cid = NumberUtils.createLong(campaignId);
            //加载毛利
            minProfitRate = FinalData.CAPAIGN_MINI_PROFIT_RATE_CONFIG.get(cid);
            if(minProfitRate == null){
                minProfitRate = 0.35; //最小毛利默认值
            }else if(minProfitRate >= 1d){ //防止出现毛利大于等于1的情况
                minProfitRate = 0.999;
            }

            //加载活动毛利控制设置
            BillingCampaign campaign = FinalData.STATIC_CAMPAIGN_MAP.get(cid);
            if(campaign != null && campaign.getProfitControl() != null){
                profitControl = campaign.getProfitControl();
            }

        }catch (Exception e){
            log.warn(e.getMessage(),e);
        }


		/*
         * 如果没有读取到最高限价，默认cpm使用2,cpc使用0.3
		 */
        if (StringUtils.isEmpty(maxPrice) || maxPrice.equals("0.0")) {
            if (costType.trim().equalsIgnoreCase("cpm")) {
                maxPrice = "2";
            } else {
                maxPrice = "0.6";
            }
        }

        /**
         * 计算最高限价
         */
        double mprice = 0d;
        if ("cpm".equals(costType)) {
            if ("impression".equals(actionType)) { // 处理展现
                mprice = Double.parseDouble(maxPrice) / 1000d;
            }
        } else if ("cpc".equals(costType)) {
            if ("click".equals(actionType)) { // 处理点击
                mprice = Double.parseDouble(maxPrice);
            }
        }
        
        costLogicLog.info("requestId:" + requestId + ",campaignId:" + campaignId + ",costType:" + costType + ",actionType:" + actionType + ",mprice:" + mprice + ",maxPrice:" + maxPrice);
		
		/*
		 * 处理直投渠道 
		 */
        int channleId = Integer.parseInt(adChannleId);
        if (channleId > 1000 && channleId < 2000) {
            return mprice;
        }

		/*
		 * 5041都收取最高限价
		 */
        if (channleId == 5041) {
            if ("cpm".equals(costType)) {
                if ("impression".equals(actionType)) { // 处理展现
                    return mprice;
                }
            } else if ("cpc".equals(costType)) {
                if ("click".equals(actionType)) { // 处理点击
                    return mprice;
                }
            }
        }

		/*
		 * 处理托底
		 */
        if (td) {
            if ("cpm".equals(costType)) {
                if ("impression".equals(actionType)) { // 处理展现
                    return mprice;
                }
            } else if ("cpc".equals(costType)) {
                if ("click".equals(actionType)) { // 处理点击
                    return mprice;
                }
            }

            return 0;

        }

        double tempvp = valuePrice; //临时变量
        if (biddingCost != 0d) {
            if (retarget) { //回头客活动
                double d = biddingCost * retargetCostArg;
                if (valuePrice > d) {
                    valuePrice = d;
                }
            } else {//非回头客活动
                double d = biddingCost * normalCostArg;
                if (valuePrice > d) {
                    valuePrice = d;
                }
            }
        }

        /**
         * 计算rtb预计收取价格
         */
        double rprice = mprice; //默认值给最高限价
        if ("cpm".equals(costType)) {
            if ("impression".equals(actionType)) { // 处理展现
                if(profitControl) {
                    rprice = rtbCost / (1 - minProfitRate);
                }else{
                    rprice = rtbCost;
                }

                //记录变更日志
                if (tempvp != valuePrice && isLogCost) {
                    costLogicLog.info("requestId:" + requestId + ",campaignId:" + campaignId + ",logic:valueprice_low_than_5winnerprice,before:" + df.format(tempvp) + ",after:" + df.format(rprice));
                }
            }
        } else if ("cpc".equals(costType)) {
            if ("click".equals(actionType)) { // 处理点击
                try {
                    double dctr = Double.parseDouble(ctr);
                    if(profitControl) {
                        rprice = valuePrice / dctr / (1 - minProfitRate);
                    }else{
                        rprice = valuePrice / dctr;
                    }
                    //记录变更日志
                    if (tempvp != valuePrice && isLogCost) {
                        tempvp = tempvp / dctr;
                        costLogicLog.info("requestId:" + requestId + ",campaignId:" + campaignId + ",logic:valueprice_low_than_5winnerprice,before:" + df.format(tempvp) + ",after:" + df.format(rprice));
                    }

                    //新增逻辑:增加cpc下线
                    if (retarget) { //回头客活动
                        if (rprice < minCpcCostRetarget) {
                            //记录变更日志
                            if (isLogCost) {
                                costLogicLog.info("requestId:" + requestId + ",campaignId:" + campaignId + ",logic:low_than_cpc_offline,before:" + df.format(rprice) + ",after:" + df.format(minCpcCostRetarget));//小于最低cpc
                            }
                            rprice = minCpcCostRetarget;
                        }
                    } else {//非回头客活动
                        if (rprice < minCpcCostNormal) {
                            //记录变更日志
                            if (isLogCost) {
                                costLogicLog.info("requestId:" + requestId + ",campaignId:" + campaignId + ",logic:low_than_cpc_offline,before:" + df.format(rprice) + ",after:" + df.format(minCpcCostNormal));//小于最低cpc
                            }
                            rprice = minCpcCostNormal;
                        }
                    }

                } catch (Exception e) {
                    log.warn("ctr:" + ctr + e.getMessage(), e);
                }
            }
        }

        if (mprice < rprice) {
            //记录变更日志
            if (isLogCost) {
                costLogicLog.info("requestId:" + requestId + ",campaignId:" + campaignId + ",logic:big_than_campaign_limit,before:" + df.format(rprice) + ",after:" + df.format(mprice));//大于活动限价
            }
        }


        return (mprice > rprice) ? rprice : mprice;
    }

    /**
     * 计算报表数据：点击，展现，RTB出价，竞价花费
     *
     * @param actionType     展现/点击
     * @param costType       CPM/CPC
     * @param biddingCostStr 供应商返回的1次展现收取价格（竞价成功价格），如果千次展现出价5元，则单次展现此值是 5000
     * @param rtbCostStr     是RTB的1次展现竞价出价，如果千次展现出价5元，则单次展现此值是 5000
     * @param retarget       是否为回头客定向活动
     * @param td             是否为托底
     * @param isLogCost      是否记录cost变更记录
     * @return 报表数据
     */
    public static SendReport calBidding(String requestId, String campaignId, String adChannleId, String actionType, String costType, String biddingCostStr, String rtbCostStr, String maxPrice, String ctr, String valuePriceStr, Boolean retarget, boolean td, boolean isLogCost) {
        SendReport cost = new SendReport();

        if (StringUtils.isEmpty(valuePriceStr)) {
            valuePriceStr = String.valueOf(Double.parseDouble(maxPrice) * 1000);
        }

        double valuePrice = Double.parseDouble(valuePriceStr) / 1000 / 1000; //用户价值_转换成一次展现的钱数,单位是元
		
		/*
		 * 计算 竞价价格
		 */
        double biddingCost = 0; // 竞价成功价格_转换成一次展现的钱数,单位是元
        try {
            biddingCost = Double.parseDouble(biddingCostStr);
        } catch (NumberFormatException e) {
            log.warn("parse : '" + biddingCost + "' to number error," + e.getMessage(), e);
        }

        biddingCost = calBiddingCost(adChannleId, biddingCost);

        /**
         * 处理结算
         */
        double rtbCost = 0;
        if (actionType.equalsIgnoreCase("impression")) { // 处理展现
            cost.setImpression(1L);

            cost.setDspCost(biddingCost); // biddingCost

            rtbCost = calRtbCost(rtbCostStr); // rtb出价_转换成一次展现的钱数,单位是元

            cost.setRtbCost(rtbCost); // rtbcost

        } else if (actionType.equalsIgnoreCase("click")) { // 处理点击
            cost.setClick(1L);
        }

        cost.setCost(calCost(requestId, campaignId, actionType, costType, maxPrice, valuePrice, ctr, adChannleId, biddingCost, retarget, retargetCostArg, normalCostArg, td, isLogCost, rtbCost)); //计算用户花费

        return cost;
    }

    public static double calRtbCost(String rtbCostStr) {
        return Double.parseDouble(rtbCostStr) / 1000 / 1000 / 1000;
    }

    public static double calBiddingCost(String adChannleId, double biddingCost) {
    	final List<String> centCPMSupplier = ImmutableList.of("5210","5240","5250");
        if (adChannleId.equals(SupplierInfo.GOOGLE_ADX.toString()) || adChannleId.equals(SupplierInfo.GOOGLE_APP.toString())) {// 根据广告平台，转换cpm价格单位
            biddingCost = biddingCost / 1000 / 1000;
        } else if (adChannleId.equals(SupplierInfo.TAOBAO_TANX.toString()) || adChannleId.equals(SupplierInfo.TAOBAO_PMP.toString()) || adChannleId.equals(SupplierInfo.TAOBAO_MOBILE.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.TENCENT_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.YIGAO_ADX.toString()) || adChannleId.equals(SupplierInfo.YIGAO_PPB.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.BAIDU_ADX.toString()) || adChannleId.equals(SupplierInfo.BAIDU_MOBILE.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.ALLYES_ADX.toString())) {
            biddingCost = biddingCost / 1000;
        } else if (adChannleId.equals(SupplierInfo.SINA_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.AMAX_ADX.toString()) || adChannleId.equals(SupplierInfo.ADVIEW_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 10000;
        } else if (adChannleId.equals(SupplierInfo.MIAOZHEN_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.TENCENT_GDT.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.HUZHONG_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        }else if (adChannleId.equals(SupplierInfo.MAX_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 1000 / 1000;
        }else if (adChannleId.equals(SupplierInfo.VALUEMAKER_ADX.toString())) {
            biddingCost = biddingCost / 1000 /1000;
        }else if (adChannleId.equals(SupplierInfo.YOUKU_ADX.toString())||adChannleId.equals(SupplierInfo.EMARBOX_SSP.toString())) {
            biddingCost = biddingCost / 1000 /100;
        }else if (adChannleId.equals(SupplierInfo.IFENG_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        }else if (centCPMSupplier.contains(adChannleId)) {
        	biddingCost = biddingCost / 1000 / 100;
        }else if (adChannleId.equals(SupplierInfo.INMOBI_ADX.toString())) {
        	biddingCost = biddingCost / 1000;
        }
        return biddingCost;
    }


}
