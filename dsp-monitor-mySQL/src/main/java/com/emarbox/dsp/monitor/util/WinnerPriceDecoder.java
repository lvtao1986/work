package com.emarbox.dsp.monitor.util;

import com.emarbox.dsp.monitor.dto.ConfigProperties;
import com.emarbox.dsp.monitor.util.decrypt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 价格解密工具类
 * Created by mrzhu on 15/5/14.
 */
public class WinnerPriceDecoder {
    private static final Logger logger = LoggerFactory.getLogger(WinnerPriceDecoder.class);
    private static final String ENVIORMENT = ConfigProperties.getEnviorment();
    /**
     * 价格解密 , 开发环境则直接返回原文
     * @param supplier 渠道id
     * @param biddingPrice 价格密文
     * @return 解密后的价格,解密失败则返回原字符串
     */
    public static String decodePrice(int supplier, String biddingPrice) {
        if("dev".equals(ENVIORMENT)){
            return biddingPrice;
        }
        if(supplier == SupplierInfo.GOOGLE_ADX.intValue()){
            return GoogleDecrypter.decode(biddingPrice);
        }else if(supplier == SupplierInfo.GOOGLE_APP.intValue()){
            return GoogleAppDecrypter.decode(biddingPrice);
        }else if(supplier == SupplierInfo.BAIDU_ADX.intValue()){
            return BaiduDecrypter.decode(biddingPrice);
        }else if(supplier == SupplierInfo.TAOBAO_TANX.intValue() || supplier == SupplierInfo.TAOBAO_PMP.intValue()){
            try {
                return TanxPriceDecoder.decode(biddingPrice);
            } catch (Exception e) {
                logger.warn("tanx价格解密失败,价格密文：" + biddingPrice + " error :" + e.getMessage(), e);
                return biddingPrice;
            }
        }else if(supplier == SupplierInfo.TENCENT_ADX.intValue()){
            //tencent action 中解密
        }else if(supplier == SupplierInfo.YIGAO_ADX.intValue()){
            try {
                return YigaoPriceDecoder.decode(biddingPrice);
            } catch (Exception e) {
                logger.warn("yigao价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                return biddingPrice;
            }
        }else if(supplier == SupplierInfo.YIGAO_PPB.intValue()){
            try {
                return YigaoPPBPriceDecoder.decode(biddingPrice);
            } catch (Exception e) {
                logger.warn("yigaoppb价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                return biddingPrice;
            }
        }else if(supplier == SupplierInfo.ALLYES_ADX.intValue()){
            try {
                return AllyesPriceDecoder.decode(biddingPrice);
            } catch (Exception e) {
                logger.warn("allyes价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                return biddingPrice;
            }
        }else if(supplier == SupplierInfo.AMAX_ADX.intValue()){
            try {
                return AmaxDecrypter.decode(biddingPrice);
            } catch (Exception e) {
                logger.warn("amax价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                return biddingPrice;
            }
        }else if(supplier == SupplierInfo.MIAOZHEN_ADX.intValue()){
            try {
                return MiaozhenPriceDecoder.decode(biddingPrice);
            } catch (Exception e) {
                logger.warn("miaozhen价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                return biddingPrice;
            }
        }else if(supplier == SupplierInfo.TENCENT_GDT.intValue()){
            //huzhong_adx action 中解密
        }else if(supplier == SupplierInfo.HUZHONG_ADX.intValue()){
        	 try {
                 return HuZhongPriceDecoder.decode(biddingPrice);
             } catch (Exception e) {
                 logger.warn("huzhong价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                 return biddingPrice;
             }
        }else if(supplier == SupplierInfo.MAX_ADX.intValue()){
       	 try {
             return MaxPriceDecoder.decode(biddingPrice);
         } catch (Exception e) {
             logger.warn("聚效价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
             return biddingPrice;
         }
        }else if(supplier == SupplierInfo.VALUEMAKER_ADX.intValue()){
          	 try {
                 return ValueMakerPriceDecoder.decode(biddingPrice);
             } catch (Exception e) {
                 logger.warn("万流客价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                 return biddingPrice;
             }
        }else if(supplier == SupplierInfo.YOUKU_ADX.intValue()){
          	 try {
                 return YoukuPriceDecoder.decode(biddingPrice);
             } catch (Exception e) {
                 logger.warn("优酷价格解密失败,价格密文："+ biddingPrice + " error :"+e.getMessage(),e);
                 return biddingPrice;
             }
        }
        return biddingPrice;
    }
}
