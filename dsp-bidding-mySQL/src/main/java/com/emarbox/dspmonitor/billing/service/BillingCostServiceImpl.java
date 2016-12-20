package com.emarbox.dspmonitor.billing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.base.spring.service.BaseService;

import com.emarbox.dspmonitor.billing.dao.BillingCostDao;
import com.emarbox.dspmonitor.billing.domain.BillingAccountInfo;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;

@Service("billingCostService")
public class BillingCostServiceImpl extends BaseService implements BillingCostService {

	@Autowired
	private BillingCostDao billingCostDao;

	@Override
	public Map<Long, BillingCampaign> getCampaignCostList() {
		Map<Long, BillingCampaign> userMap = new HashMap<Long, BillingCampaign>();
		List<BillingCampaign> list = billingCostDao.queryCampaignCostList();
		if (null != list && list.size() > 0) {
			for (BillingCampaign campaign : list) {
				userMap.put(campaign.getCampaignId(), campaign);
			}
		}

		return userMap;
	}

	@Override
	public Map<Long, Double> getAccountCostList() {
		Map<Long, Double> accMap = new HashMap<Long, Double>();
		List<BillingAccountInfo> list = billingCostDao.queryAccountCostList();
		if (null != list) {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					BillingAccountInfo acc = list.get(i);
					accMap.put(acc.getUserId(), acc.getCost());
				}
			}
		}
		return accMap;
	}

	public BillingCostDao getBillingCostDao() {
		return billingCostDao;
	}

	public void setBillingCostDao(BillingCostDao billingCostDao) {
		this.billingCostDao = billingCostDao;
	}

}
