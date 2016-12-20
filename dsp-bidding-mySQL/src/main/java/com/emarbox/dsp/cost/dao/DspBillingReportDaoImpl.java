package com.emarbox.dsp.cost.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.emarbox.dsp.DspBaseDao;
import com.emarbox.dsp.domain.CampaignDetailReport;

@Repository("dspBillingReportDao")
public class DspBillingReportDaoImpl extends DspBaseDao implements
		DspBillingReportDao {

	@Override
	public int[] saveCampaingDetailReport(
			List<CampaignDetailReport> reportList) {

		if (null == reportList) {
			return null;
		}

		SqlParameterSource[] batch = SqlParameterSourceUtils
				.createBatch(reportList.toArray());
		StringBuffer sql = new StringBuffer();
		
		sql.append(" insert into campaign_report_detail( ");
		sql.append("    campaign_id, ");
		sql.append("    supplier_id, ");
		sql.append("    user_id, ");
		sql.append("    project_id, ");
		sql.append("    display_count, ");
		sql.append("    click_count, ");
		sql.append("    cost, ");
		sql.append("    stat_date, ");
		sql.append("    stat_year, ");
		sql.append("    stat_month, ");
		sql.append("    stat_week, ");
		sql.append("    stat_hour, ");
		sql.append("    parent_user_id, ");
		sql.append("    update_user, ");
		sql.append("    update_time ");
		sql.append("    ) values ( ");
		sql.append("   :campaignId, ");
		sql.append("   :supplierId, ");
		sql.append("   :userId, ");
		sql.append("   :projectId, ");
		sql.append("   :displayCount, ");
		sql.append("   :clickCount, ");
		sql.append("   0, ");
		sql.append("   :statDate, ");
		sql.append("   :statYear, ");
		sql.append("   :statMonth, ");
		sql.append("   :statWeek, ");
		sql.append("   :statHour, ");
		sql.append("   :parentUserId,");
		sql.append("   'billingAPI', ");
		sql.append("   NOW() ");
		sql.append("   ) ");
		
		sql.append(" on duplicate key update ");
		sql.append("      crd.click_count = :clickCount, ");
		sql.append("      crd.display_count = :displayCount, ");
		sql.append("      crd.update_user = 'billingAPI', ");
		sql.append("      crd.update_time = now() ");
		
		
		/**
		//重要： 报表模块不应该修改 花费值，因为计费发过来的信息只有点击和展现，没有花费信息
		sql.append(" merge into campaign_report_detail crd ");
		sql.append("     using  ( select  ");
		sql.append("   :campaignId as campaign_id, ");
		sql.append("   :userId as user_id, ");
		sql.append("   :projectId as project_id, ");
		sql.append(" :supplierId as supplier_id, ");
		sql.append("   :clickCount as click_count, ");
		sql.append("   :displayCount as display_count, ");

		//默认花费写入 0 
		sql.append("   0 as cost, ");
		sql.append("   :statDate as stat_date, ");
		sql.append("   :statYear as stat_year, ");
		sql.append("   :statMonth as stat_month, ");
		sql.append("   :statWeek as stat_week, ");
		sql.append("   :statHour as stat_hour, ");
		sql.append("   :parentUserId as parent_user_id,");
		sql.append("   '").append("billingAPI").append("' as update_user, ");
		sql.append("   sysdate as update_time ");
		sql.append("      from  dual where exists ( select 1 from campaign ca where ca.id = :campaignId ) ");
		sql.append("      ) t");
		sql.append("   on   (t.campaign_id = crd.campaign_id and t.supplier_id=crd.supplier_id and t.stat_hour = crd.stat_hour and t.stat_date = crd.stat_date) ");
		sql.append(" when matched then   update set ");
		sql.append("      crd.click_count = t.click_count, ");
		sql.append("      crd.display_count = t.display_count, ");
		sql.append("      crd.update_user = t.update_user, ");
		sql.append("      crd.update_time = t.update_time ");
		sql.append(" when not　matched then ");
		sql.append("    insert  (");
		sql.append("    id, ");
		sql.append("    campaign_id, ");
		sql.append(" supplier_id, ");
		sql.append("    user_id, ");
		sql.append("    project_id, ");
		sql.append("    display_count, ");
		sql.append("    click_count, ");
		sql.append("    cost, ");
		sql.append("    stat_date, ");
		sql.append("    stat_year, ");
		sql.append("    stat_month, ");
		sql.append("    stat_week, ");
		sql.append("    stat_hour, ");
		sql.append("    parent_user_id, ");
		sql.append("    update_user, ");
		sql.append("    update_time ");
		sql.append("    ) values ( ");
		sql.append("   seq_campaign_report_detail.nextval,");
		sql.append("   t.campaign_id, ");
		sql.append("  t.supplier_id, ");
		sql.append("   t.user_id, ");
		sql.append("   t.project_id, ");
		sql.append("   t.display_count, ");
		sql.append("   t.click_count, ");
		sql.append("   t.cost, ");
		sql.append("   t.stat_date, ");
		sql.append("   t.stat_year, ");
		sql.append("   t.stat_month, ");
		sql.append("   t.stat_week, ");
		sql.append("   t.stat_hour, ");
		sql.append("   t.parent_user_id,");
		sql.append("   t.update_user, ");
		sql.append("   t.update_time ");
		sql.append("   ) ");
		 **/
		int[] affectRowsArr = getSimpleJdbcTemplate().batchUpdate(
				sql.toString(), batch);

		return affectRowsArr;
	}


}
