package com.emarbox.dspmonitor.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import app.base.util.ConfigUtil;
import app.common.util.LogUtil;
import app.common.util.Logs;

import com.emarbox.dsp.util.Function;
import com.emarbox.dspmonitor.billing.data.FinalData;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;

public class DMapService {
	private final String separator = ","; // 字段拼接符号
	private static DMapService instance = null;

	private String basePath = ConfigUtil.getString("dmap.data.basePath");
	private String prefixName =ConfigUtil.getString("dmap.data.prefixName");
	private String suffixName =ConfigUtil.getString("dmap.data.suffixName");

	private static final Logs log = LogUtil.getLog(DMapService.class);

	private DMapService() {

	}

	public synchronized static DMapService getInstance() {
			if (instance == null) {
				instance = new DMapService();
			}
		return instance;
	}

	/**
	 * 保存campaignCost信息到文件
	 * 
	 * @param costs
	 */
	public void saveData(Collection<CampaignCost> costs) {
		java.text.DecimalFormat numForm = new java.text.DecimalFormat("####0.####");
		// 拼装数据
		Collection<String> infos = new ArrayList<String>();
		for (CampaignCost cost : costs) {
			Long campaignId = cost.getCampaignId();

			BillingCampaign campaign = FinalData.STATIC_CAMPAIGN_MAP.get(campaignId);
			if (campaign == null) {
				continue;
			}
			Long userId = campaign.getUserId();
			Long projectId = campaign.getProjectId();

			Long supplierId = cost.getSupplierId();
			String reportTime = cost.getStatDate();
			Long impression = cost.getImpression();
			Long click = cost.getClick();
			Double rtbCost = cost.getRtbCost();
			Double dspCost = cost.getDspCost();
			Double userCost = cost.getCost();
			
			String rtbCostStr = numForm.format(Function.round(rtbCost, 4, BigDecimal.ROUND_HALF_UP));
			String dspCostStr = numForm.format(Function.round(dspCost, 4, BigDecimal.ROUND_HALF_UP));
			String userCostStr = numForm.format(Function.round(userCost, 4, BigDecimal.ROUND_HALF_UP));

			String info = userId + separator + projectId + separator + campaignId + separator + supplierId + separator + reportTime
					+ separator + impression + separator + click + separator + rtbCostStr + separator + dspCostStr + separator + userCostStr;

			infos.add(info);
		}

		// 确定文件
		Calendar cal = Calendar.getInstance();
		String date = Function.getDateString(cal.getTime(), "yyyyMMdd");
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		String directoryUri = basePath + "/" + date;
		String fileUri = directoryUri + "/" + prefixName + "_" + date + "_" + hour + suffixName;

		File directory = new File(directoryUri);
		if (!directory.exists() || !directory.isDirectory()) {
			directory.mkdirs();
		}

		File file = new File(fileUri);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		// 将记录写入文件
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			for (String info : infos) {
				bw.write(info + "\n");
				bw.flush();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

	}

}
