package com.emarbox.dsp.monitor.util;

/**
 * 费用计算 工具类
 *
 * @author zhujinju
 */
public abstract class CostUtil {

    public static double calBiddingCost(String adChannleId, double biddingCost) {
        if (adChannleId.equals(SupplierInfo.GOOGLE_ADX.toString()) || adChannleId.equals(SupplierInfo.GOOGLE_APP.toString())) {// 根据广告平台，转换cpm价格单位
            biddingCost = biddingCost / 1000 / 1000;
        } else if (adChannleId.equals(SupplierInfo.TAOBAO_TANX.toString()) || adChannleId.equals(SupplierInfo.TAOBAO_PMP.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.TENCENT_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.YIGAO_ADX.toString()) || adChannleId.equals(SupplierInfo.YIGAO_PPB.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.BAIDU_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.ALLYES_ADX.toString())) {
            biddingCost = biddingCost / 1000;
        } else if (adChannleId.equals(SupplierInfo.SINA_ADX.toString())) {
            biddingCost = biddingCost / 1000 / 100;
        } else if (adChannleId.equals(SupplierInfo.AMAX_ADX.toString())) {
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
        }else if (adChannleId.equals(SupplierInfo.YOUKU_ADX.toString())) {
            biddingCost = biddingCost / 1000 /100;
        }
        return biddingCost;
    }


}
