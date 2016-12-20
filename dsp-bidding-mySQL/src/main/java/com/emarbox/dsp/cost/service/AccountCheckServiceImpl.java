package com.emarbox.dsp.cost.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emarbox.dspmonitor.status.data.NodeStatus;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emarbox.dsp.DspBaseService;
import com.emarbox.dsp.campaign.dao.CampaignDao;
import com.emarbox.dsp.domain.AccountInfo;
import com.emarbox.dsp.domain.Campaign;
import com.emarbox.dsp.finance.dao.AccountCheckDao;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
import com.emarbox.dspmonitor.billing.service.BillingSendCampaignService;

@Service("accountCheckService")
public class AccountCheckServiceImpl extends DspBaseService implements AccountCheckService {


	@Autowired
	protected AccountCheckDao accountCheckDao;
	@Autowired
	private  CampaignDao campaignDao;
	@Autowired
	private BillingSendCampaignService billingSendCampaignService;
	
	@Override
	public void checkProcess() {
		try {
			log.debug("账户余额扫描开始");
			Double minAccountBalance = accountCheckDao.getMinAccountBalance();
			List<AccountInfo>  accountInfos = accountCheckDao.getAccountList();
			List<BillingCampaign> camList = new ArrayList<BillingCampaign>();
			for (AccountInfo accountInfo : accountInfos) {
				//非本节点处理的账号则跳出
				if ((accountInfo.getUserId() % NodeStatus.accountModCount) != NodeStatus.accountMod){
					continue;
				}
				Double remainAmount = accountInfo.getRemainAmount();

				if(remainAmount <= minAccountBalance){
					List<Campaign> campaignList = campaignDao.getCampaignValid(accountInfo.getUserId());
					for (Campaign cam : campaignList) {
						BillingCampaign info = new BillingCampaign();
						info.setCampaignId(cam.getCampaignId());
						info.setTotalAccountCost(accountInfo.getRemainAmount());
						info.setRemainAccount(remainAmount);
						camList.add(info);
					}
				}
			}
		 
			if (null != camList && camList.size() > 0) {
				billingSendCampaignService.sendCampaignStop(camList);
				log.info("因账户余额不足通知活动下线信息给API：" + camList.toString());
			}else {
				log.debug("没有低于账户阀值的账户,等待下次扫描...");
			}
			
			log.debug("账户余额扫描完成");

		} catch (Exception e) {
			log.error("账户余额扫描处理失败: " + e.getMessage(),e);
		}

	}

	

}
