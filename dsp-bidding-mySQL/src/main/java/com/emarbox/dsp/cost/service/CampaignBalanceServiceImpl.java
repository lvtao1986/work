package com.emarbox.dsp.cost.service;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emarbox.dsp.cost.CostBaseService;
import com.emarbox.dsp.cost.dao.CampaignBalanceDao;
import com.emarbox.dsp.cost.dao.DspBillingReportDao;
import com.emarbox.dsp.cost.domain.AccountInfoCost;
import com.emarbox.dsp.cost.domain.CampaignCostDetail;

@Service("campaignBalanceService")
public class CampaignBalanceServiceImpl extends CostBaseService implements CampaignBalanceService {

	@Autowired
	protected CampaignBalanceDao campaignBalanceDao;

	@Autowired
	protected DspBillingReportDao dspBillingReportDao;

	@Override
	public Integer saveAccountConsumeLog(List<AccountInfoCost> accountInfoList) {
		return campaignBalanceDao.saveAccountConsumeLog(accountInfoList);
	}

	@Override
	public List<AccountInfoCost> updateAccountInfo(List<CampaignCostDetail> campaignTotalCostList) {

		return campaignBalanceDao.updateAccountInfo(campaignTotalCostList);
	}

	@Override
	public Integer addTempReportDetail(List<CampaignCostDetail> campaignTotalCostList) {
		int resultRows = -1;
		try {
			if (null != campaignTotalCostList && campaignTotalCostList.size() > 0) {
				campaignBalanceDao.addTempReportDetail(campaignTotalCostList);

				resultRows = 1;
				log.info("插入临时报表完成");

			} else {
				log.info("没有需要插入临时报表的数据");
			}
		} catch (Exception e) {
			log.error("addTempReportDetail failed: " + ExceptionUtils.getFullStackTrace(e));
		}

		return resultRows;
	}

}
