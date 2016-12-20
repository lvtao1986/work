package com.emarbox.dsp.cost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emarbox.dsp.cost.CostBaseService;
import com.emarbox.dsp.cost.dao.CampaignCostDetailDao;

@Service("campaignCostDetailService")
public class CampaignCostDetailServiceImpl extends CostBaseService implements
		CampaignCostDetailService {

	@Autowired
	protected CampaignCostDetailDao campaignCostDetailDao;

	@Override
	public void cleanOldData() {
		campaignCostDetailDao.cleanOldData();
	}

}
