package com.emarbox.dsp.monitor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emarbox.dsp.monitor.dao.CampaignDao;
import com.emarbox.dsp.monitor.dto.CampaignDomain;

@Service("campaignService")
public class CampaignServiceImpl implements CampaignService {

	@Autowired
	private CampaignDao campaignDao;

	@Override
	public Map<String, Double> getMaxPriceMap() {
		Map<String, Double> maxPriceMap = new HashMap<String, Double>();
		List<CampaignDomain> campaignList = campaignDao.getMaxPriceList();
		if (campaignList != null && campaignList.size() > 0) {
			for (int i = 0; i < campaignList.size(); i++) {
				CampaignDomain campain = campaignList.get(i);
				maxPriceMap.put(String.valueOf(campain.getCampaignId()), campain.getMaxPrice());
			}
		}
		return maxPriceMap;
	}
	
	@Override
	public Map<String, Double> getRecentMaxPriceMap() {
		Map<String, Double> maxPriceMap = new HashMap<String, Double>();
		List<CampaignDomain> campaignList = campaignDao.getRecentMaxPriceList();
		if (campaignList != null && campaignList.size() > 0) {
			for (int i = 0; i < campaignList.size(); i++) {
				CampaignDomain campain = campaignList.get(i);
				maxPriceMap.put(String.valueOf(campain.getCampaignId()), campain.getMaxPrice());
			}
		}
		return maxPriceMap;
	}
	
	@Override
	public Map<String, String> getCreativeIdMap() {
		return campaignDao.getCreativeIdMap();
	}
	@Override
	public Map<String, String> getRecentCreativeIdMap() {
		return campaignDao.getRecentCreativeIdMap();
	}
	@Override
	public Map<String, String> getRetargetMap() {
		return campaignDao.getRetargetMap();
	}

	public CampaignDao getCampaignDao() {
		return campaignDao;
	}

	public void setCampaignDao(CampaignDao campaignDao) {
		this.campaignDao = campaignDao;
	}

	@Override
	public Map<Long, Double> queryCampaignProfitRateSet() {
		return campaignDao.queryCampaignProfitRateSet();
	}

	@Override
	public Map<Long, Long> queryCampaignUserIdMap() {
		return campaignDao.getParentUserIdMap();
	}
	
	@Override
	public Map<Long, Long> queryRecentCampaignUserIdMap() {
		return campaignDao.getRecentParentUserIdMap();
	}
}
