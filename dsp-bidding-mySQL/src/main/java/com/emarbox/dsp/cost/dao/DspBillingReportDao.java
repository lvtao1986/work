package com.emarbox.dsp.cost.dao;

import java.util.List;

import com.emarbox.dsp.domain.CampaignDetailReport;

public interface DspBillingReportDao {
	
	/**
	 * 保存报表明细数据
	 * @param reportList
	 * @return
	 */
	int[] saveCampaingDetailReport(List<CampaignDetailReport> reportList);
	
	/**
	 * 汇总报表明细数据到 活动计划报表中去
	 * @param query
	 * @return
	 */
//	public int saveCampaignReport(CampaignDetailReportQuery query);
	
	/**
	 * 定时汇总报表明细数据到 活动计划报表中去
	 * @param query
	 * @return
	 */
//	public int timingSaveCampaignReport(CampaignDetailReportQuery query);
	
}