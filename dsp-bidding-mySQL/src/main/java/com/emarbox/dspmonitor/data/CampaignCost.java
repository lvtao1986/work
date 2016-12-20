package com.emarbox.dspmonitor.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CampaignCost {
	
	protected static final String CAMPAIGN_ID = "cid"; //活动Id
	protected static final String SUPPLIER_ID= "sid"; //平台Id
	protected static final String STAT_HOUR= "hour"; //统计小时
	protected static final String STAT_MINUTE= "minute"; //统计分钟
	protected static final String COST= "cost";	//消费
	protected static final String IMPRESSION = "impression";
	protected static final String CLICK = "click";
	protected static final String DSP_COST = "dspcost";
	protected static final String RTB_COST = "rtbcost";
	
	protected static final String REQUEST_ID = "request_id";
	protected static final String CLICK_ID = "click_id";
	
	private Long campaignId;
	private Double cost;
	private String statDate;
	private Long userId;
	private String statTime;
	/** 临时属性，不会被序列化：预计花费价格 **/
	private transient Double preCost;

	public Double getPreCost() {
		return preCost;
	}

	public void setPreCost(Double preCost) {
		this.preCost = preCost;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}


	private DBObject dbo; //MongoDB存储对象
	
	public void setClickId(String clickId){
		dbo.put(CLICK_ID, clickId);
	}
	
	public String getClickId(){
		return (String) dbo.get(CLICK_ID);
	}
	
	public void setRequestId(String requestId){
		dbo.put(REQUEST_ID,requestId);
	}
	
	public String getRequestId(){
		return (String) dbo.get(REQUEST_ID);
	}
	
	
	public void setImpression(Long impression){
		dbo.put(IMPRESSION,impression);
	}
	
	public Long getImpression(){
		return (Long)dbo.get(IMPRESSION);
	}
	
	public void setClick(Long click){
		dbo.put(CLICK,click);
	}
	
	public Long getClick(){
		return (Long)  dbo.get(CLICK);
	}
	
	public void setDspCost(Double dspCost){
		dbo.put(DSP_COST,dspCost);
	}
	
	public Double getDspCost(){
		return (Double) dbo.get(DSP_COST);
	}
	
	public void setRtbCost(Double rtbCost){
		dbo.put(RTB_COST,rtbCost);
	}
	
	public Double getRtbCost(){
		return (Double) dbo.get(RTB_COST);
	}
	
	public CampaignCost(){
		this.dbo = new BasicDBObject();
	}
	
	protected CampaignCost(DBObject dbo) {
		this.dbo = dbo;
	}

	protected DBObject getDbo() {
		return dbo;
	}

	@SuppressWarnings("unchecked")
	protected <T> T get(String name) {
		return (T) dbo.get(name);
	}

	protected Object put(String name, Object v) {
		return dbo.put(name,v);
	}
	
	
	public Long getSupplierId() {
		return (Long) dbo.get(SUPPLIER_ID);
	}

	public void setSupplierId(Long supplierId) {
		dbo.put(SUPPLIER_ID,supplierId);
	}

	public Integer getStatHour() {
		return (Integer) dbo.get(STAT_HOUR);
	}

	public void setStatHour(Integer statHour) {
		dbo.put(STAT_HOUR,statHour);
	}

	public Integer getStatMinute() {
		return (Integer) dbo.get(STAT_MINUTE);
	}

	public void setStatMinute(Integer statMinute) {
		dbo.put(STAT_MINUTE,statMinute);
	}


	@Override
	public String toString() {
		return "CampaignCost [dbo=" + dbo + "]";
	}
}
