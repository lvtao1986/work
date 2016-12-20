package com.emarbox.dspmonitor.costing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import app.base.spring.dao.BaseDao;

import com.emarbox.dsp.domain.CampaignSnapshot;
import com.emarbox.dspmonitor.costing.domain.CampaignCostDetail;
import com.emarbox.dspmonitor.data.CampaignCost;

@Repository
public class CampaignCostDetailDaoImpl extends BaseDao implements CampaignCostDetailDao {
	@Override
	public void addCostDetail(List<CampaignCostDetail> costList) {
		String sql = "insert into campaign_cost_detail "
				+ " ( campaign_id,stat_time,user_id,project_id,cost,CREATE_TIME,confirmed,rtb_cost,dsp_cost,profit,supplier_code,display_count,click_count) "
				+ " values (  :campaignId,:statTime,:userId,:projectId,:cost, "
				+ " NOW(),0,:rtbCost,:dspCost,:profit,:supplierCode,:impressionCount,:clickCount ) ";

		// 绑定参数
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(costList.toArray());
		getCostSimpleJdbcTemplate().batchUpdate(sql.toString(), batch);
	}
	
	@Cacheable(value="snapshot",key="#history") 
	public CampaignSnapshot getCampaignSnapshot(CampaignSnapshot history) {
			StringBuffer sql = new StringBuffer();
			sql.append(" select id,campaign_id campaignId,project_id projectId,user_id userId,create_time createTime,campaign_status campaignStatus ");
			sql.append(" from campaign_snapshot where campaign_id=:campaignId and create_time < :createTime order by create_time desc,id desc limit 1 ");

			//返回参数
			BeanPropertyRowMapper<CampaignSnapshot> mapper = new BeanPropertyRowMapper<CampaignSnapshot>(CampaignSnapshot.class);
			// 绑定参数
			SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(history);
			List<CampaignSnapshot> list = getReadSimpleJdbcTemplate().query(sql.toString(), mapper, sqlParam);
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
	}


	@Override
	public Map<Long, Double> getCammpaignTodayProfitRate() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select campaign_id campaignId,if(ifnull(cost,0)=0,0,(cost-ifnull(dsp_cost,0))/cost) profitRate from campaign_snapshot ");
		sql.append("  where  create_time > date_sub(now(),interval 2 minute ) ");
		sql.append(" union 		 select -1,-1   ");
		
		Map<Long, Double> result = 
		 getReadSimpleJdbcTemplate().queryForObject(sql.toString(),new RowMapper<Map<Long,Double>>() {
			@Override
			public Map<Long,Double> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<Long, Double> result = new HashMap<Long, Double>();
				do{
					result.put(rs.getLong(1), rs.getDouble(2));
				}while(rs.next());
				return result;
			}
		});
		
		//如果快照没有数据，则查询campaign_cost & campaign_cost_detail表
		if(result.size() <=1){
			
			sql = new StringBuffer();
			sql.append(" select campaign_id campaignId ,ifnull(sum(dsp_cost),0) dspCost ,ifnull(sum(cost),0) cost from campaign_cost where stat_date >= curdate() group by campaign_id ");
			sql.append(" union select 0,0,0 ");
			Map<Long,CampaignCostDetail> costMap = getReadJdbcTemplate().queryForObject(sql.toString(),new RowMapper<Map<Long,CampaignCostDetail>>(){
				@Override
				public Map<Long, CampaignCostDetail> mapRow(ResultSet rs,int rowNum) throws SQLException {
					Map<Long, CampaignCostDetail> result = new HashMap<Long, CampaignCostDetail>();
					do{
						CampaignCostDetail cost = new CampaignCostDetail();
						cost.setCampaignId(rs.getLong(1));
						cost.setDspCost(rs.getDouble(2));
						cost.setCost(rs.getDouble(3));
						result.put(cost.getCampaignId(), cost);
					}while(rs.next());
					return result;
				}
			});
			
			sql = new StringBuffer();
			sql.append(" select campaign_id campaignId, ifnull(sum(dsp_cost),0) dspCost,ifnull(sum(cost),0) cost ");
			sql.append(" from campaign_cost_detail  ");
			sql.append(" where stat_time >= curdate() and confirmed =0  ");
			sql.append(" group by campaign_id  ");
			sql.append(" union select 0,0,0 ");
			
			Map<Long,CampaignCostDetail> costDetailMap = getReadJdbcTemplate().queryForObject(sql.toString(),new RowMapper<Map<Long,CampaignCostDetail>>(){
				@Override
				public Map<Long, CampaignCostDetail> mapRow(ResultSet rs,int rowNum) throws SQLException {
					Map<Long, CampaignCostDetail> result = new HashMap<Long, CampaignCostDetail>();
					do{
						CampaignCostDetail cost = new CampaignCostDetail();
						cost.setCampaignId(rs.getLong(1));
						cost.setDspCost(rs.getDouble(2));
						cost.setCost(rs.getDouble(3));
						result.put(cost.getCampaignId(), cost);
					}while(rs.next());
					return result;
				}
			});
			
			for(CampaignCostDetail costDetail : costDetailMap.values()){
				CampaignCostDetail cost = costMap.get(costDetail.getCampaignId());
				if(cost==null){
					costMap.put(costDetail.getCampaignId(), costDetail);
				}else{
					cost.setDspCost(cost.getDspCost() + costDetail.getDspCost());
					cost.setCost(cost.getCost() + costDetail.getCost());
				}
			}
			
			for(CampaignCostDetail cost : costMap.values()){
				Double profit = (cost.getCost().doubleValue() == 0d) ? 0 : ( ( cost.getCost() - cost.getDspCost() ) / cost.getCost() ); 
				result.put(cost.getCampaignId(), profit);
			}
			
			return result;
			
		}else{
			return result;
		}

		
		
	}

	@Override
	@Cacheable(value="snapshotFiveMin",key="#history")
	public CampaignSnapshot getFiveMinuteValidSnapshot(CampaignSnapshot history) {
			StringBuffer sql = new StringBuffer();

			sql.append(" select id,campaign_id campaignId,project_id projectId,user_id userId,create_time createTime,campaign_status campaignStatus ");
			sql.append(" from campaign_snapshot where campaign_status='valid' and campaign_id=:campaignId and create_time < :createTime  and create_time > date_sub(:createTime,interval 5 minute) order by create_time desc,id desc limit 1 ");

			//返回参数
			BeanPropertyRowMapper<CampaignSnapshot> mapper = new BeanPropertyRowMapper<CampaignSnapshot>(CampaignSnapshot.class);
			// 绑定参数
			SqlParameterSource sqlParam = new BeanPropertySqlParameterSource(history);
			List<CampaignSnapshot> list = getReadSimpleJdbcTemplate().query(sql.toString(), mapper, sqlParam);
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
	}

	@Override
	public void mergePreBillingDetail(Collection<CampaignCost> costList) {
		String sql = "insert into campaign_pre_billing_detail ( campaign_id,stat_time,user_id,cost,update_time) values (  :campaignId,str_to_date(:statTime,'%Y%m%d%H'),:userId,:cost, NOW() )   " 
				+ " on duplicate key update  "
				+ " cost = cost + :cost , update_time = now() ";
		// 绑定参数
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(costList.toArray());
		getCostSimpleJdbcTemplate().batchUpdate(sql.toString(), batch);
	}

}
