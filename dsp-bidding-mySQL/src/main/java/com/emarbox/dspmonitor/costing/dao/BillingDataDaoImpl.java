package com.emarbox.dspmonitor.costing.dao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import app.base.spring.dao.BaseDao;

import com.emarbox.dspmonitor.data.CampaignCost;

/**
 * Created by ralf.cao on 2015/1/15.
 */
@Repository
public class BillingDataDaoImpl extends BaseDao implements BillingDataDao {

    public void billingCampaignCost(Collection<CampaignCost> costList) throws ParseException{
        DateFormat minuteDf = new SimpleDateFormat("yyyyMMddHHmm");
        DateFormat minuteDf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (CampaignCost cost : costList) {
            cost.setStatTime(minuteDf2.format(minuteDf.parse(cost.getStatTime())));
        }
        // 这里的 key 指的是 campaign_id,stat_time 的唯一索引
        String sql = "insert into campaign_billing_detail "
                + " ( campaign_id,stat_time,user_id,cost,pre_cost,dsp_cost,update_time) "
                + " values (  :campaignId,:statTime,:userId,:cost, :preCost, :dspCost, NOW()) "
                + " on duplicate key UPDATE cost = ifnull(cost,0) + :cost, pre_cost = ifnull(pre_cost,0) + :preCost,"
                + " update_time= NOW(), dsp_cost= ifnull(dsp_cost,0) + :dspCost";

        // 绑定参数
        SqlParameterSource[] sqlParams = SqlParameterSourceUtils.createBatch(costList.toArray(new CampaignCost[costList.size()]));
        getCostSimpleJdbcTemplate().batchUpdate(sql, sqlParams);
    }

    @Override
    public double getConfirmedCost(long campaignId, String statDate) {
        // 这里的 key 指的是 campaign_id,stat_time 的唯一索引
        String sql = "select sum(cost) as total from campaign_billing_detail where campaign_id = :campaignId and stat_time >= :statDate and stat_time < :statTime";
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date=  df.parse(statDate);
        } catch (ParseException e) {
            date = new Date();
            log.error(e.getMessage(),e);
        }
        CampaignCost cost = new CampaignCost();
        cost.setCampaignId(campaignId);
        //今天的日期
        cost.setStatDate(df2.format(date));
        //明天的日期
        cost.setStatTime(df2.format(DateUtils.addDays(date,1)));
        // 绑定参数
        SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(cost);
        Map<String,Object> map = getCostSimpleJdbcTemplate().queryForMap(sql, sqlParam);
        if(map == null){
            return 0d;
        }else{
            Object count = map.get("total");
            if(count == null){
                return 0d;
            }else{
                if(count instanceof BigDecimal){
                    return ((BigDecimal)count).doubleValue();
                }else{
                    return NumberUtils.toDouble(count.toString(), 0);
                }
            }
        }
    }

}

