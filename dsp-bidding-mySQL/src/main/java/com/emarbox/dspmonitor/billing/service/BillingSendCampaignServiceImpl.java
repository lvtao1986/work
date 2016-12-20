package com.emarbox.dspmonitor.billing.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Service;

import app.base.util.ConfigUtil;
import app.common.util.LogUtil;
import app.common.util.Logs;

import com.emarbox.dsp.DspBaseService;
import com.emarbox.dsp.api.domain.DspApiResult;
import com.emarbox.dsp.api.domain.DspCost.DspCostInfo;
import com.emarbox.dsp.api.domain.DspCostRequest.DspCostRequestInfo;
import com.emarbox.dsp.api.domain.DspCostResponse.DspCostResponseInfo;
import com.emarbox.dspmonitor.billing.domain.BillingCampaign;
@Service("billingSendCampaignService")
public class BillingSendCampaignServiceImpl extends DspBaseService implements BillingSendCampaignService {

	protected Logs log = LogUtil.getLog(getClass());
	
	protected static final String DSP_USER_AGENT = "DSP Business Client/1.0";
	
	private static final String TARGET_URL = ConfigUtil.getString("dsp.billing.campaign.send.url");
	
	@Override
	public DspApiResult sendCampaignStop(List<BillingCampaign> infoList) {
		DspCostRequestInfo requestInfo = createRequest(infoList);

		DspCostResponseInfo responseInfo = sendRequest(requestInfo);

		DspApiResult apiResult = processResponse(responseInfo);
		return apiResult;
	}

	public DspCostResponseInfo post(final DspCostRequestInfo dspCostRequestInfo,final String url) {
		DspCostResponseInfo result = null;

		try {

			ContentProducer cp = new ContentProducer() {
				public void writeTo(OutputStream outstream) throws IOException {

					log.debug("dspCostRequestInfo:\n"
							+ dspCostRequestInfo);

					dspCostRequestInfo.writeTo(outstream);
				}
			};

			HttpEntity entity = new EntityTemplate(cp){
				@Override
				public long getContentLength() {
					return dspCostRequestInfo.getSerializedSize();
				}
			};

			HttpPost post = new HttpPost(url);
			post.setEntity(entity);

			Header userAgent = new BasicHeader("User-Agent", DSP_USER_AGENT);
			post.setHeader(userAgent);

			HttpClient client = new DefaultHttpClient();

			HttpResponse res = client.execute(post);

			HttpEntity resEntity = res.getEntity();

			if (null != resEntity) {
				InputStream inputStream = resEntity.getContent();

				if (null != inputStream) {

					//FileUtil.dumpToFile(inputStream,null);

					result = DspCostResponseInfo.parseFrom(inputStream);

					inputStream.close();

				}
			}

			post.abort();

			log.debug("result: " + result);

		} catch (Exception e) {

			log.error("requset failed: ", e);

		}

		return result;
	}

	public DspCostRequestInfo createRequest(List<BillingCampaign> infoList) {
		DspCostRequestInfo result = null;

		if (null == infoList) {
			return null;
		}

		// 活动通知只发送必填字段

		if (null != infoList && infoList.size() > 0) {
			DspCostRequestInfo.Builder requestBuilder = DspCostRequestInfo.newBuilder();
			DspCostInfo.Builder builder = null;
			DspCostInfo dspCostInfo = null;
			for (BillingCampaign info : infoList) {
				if (null != info) {
					builder = DspCostInfo.newBuilder();
//					builder.setUserId(info.getUserId());
					builder.setCampaignId(info.getCampaignId());
					if(info.getCost()!=null){
						 builder.setCost(info.getCost());
					}
                    builder.setDate("");
                    builder.setHour(0);
                    builder.setMinute(0);
                    
					dspCostInfo = builder.build();

					requestBuilder.addDspCost(dspCostInfo);
				}
			}

			result = requestBuilder.build();
		}

		return result;
	}

	public DspCostResponseInfo sendRequest(DspCostRequestInfo requestInfo) {
		String targetUrl = getTargetServerUrl();

		DspCostResponseInfo res = post(requestInfo, targetUrl);

		return res;
	}

	public DspApiResult processResponse(DspCostResponseInfo responseInfo) {
		DspApiResult result = new DspApiResult();

		if (null != responseInfo) {
			result.setSuccess(responseInfo.getSuccess());
			result.setStatusCode(responseInfo.getStatusCode());
			result.setMessage(responseInfo.getMessage());
		} else {
			result.setSuccess(false);
			result.setStatusCode(500);
			result.setMessage("响应解析失败");
		}

		return result;
	}

	public String getTargetServerUrl() {
		return TARGET_URL;
	}

	public String setTargetServerUrl(String targetServerUrl) {
		return null;
	}

}
