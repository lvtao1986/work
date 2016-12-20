/**
 * CampaignInfo.java
 * com.emarbox.dsp.campaign.domain
 * <p/>
 * ver  1.0
 * date 2012-5-24
 * author 耿志新
 * <p/>
 * Copyright (emar) 2012, EMAR All Rights Reserved.
 */

package com.emarbox.dsp.domain;

import java.util.Date;
import java.util.List;

import app.common.util.EscapeUtil;

/**
 * Campaign: 计划实体
 *
 * @author 耿志新
 * @version 1.0
 * @Date 2012-5-24 下午3:32:05
 * @since Ver 1.0
 */
public class Campaign extends DspDomain {

    private static final long serialVersionUID = -7816618829788082150L;

    /**
     * 创建第一步 基础数据
     */
    public static final String CREATE_BASE = "F";

    /**
     * 创建第二步 人群定向
     */
    public static final String CREATE_ORIENTATION = "S";

    /**
     * 创建第三步 创意上传 完成状态
     */
    public static final String CREAT_CREATIVE = "T";

    /**
     * 活动状态值：已生效
     */
    public static final String CAMPAIGN_STATUS_VALID = "valid";

    /**
     * 计划状态：未开始
     */
    public static final String CAMPAIGN_STATUS_READY = "ready";

    /**
     * 计划状态：已暂停
     */
    public static final String CAMPAIGN_STATUS_PAUSED = "paused";

    /**
     * 计划状态：已下线/已中止
     */
    public static final String CAMPAIGN_STATUS_BREAK = "break";

    /**
     * 计划状态：已结束
     */
    public static final String CAMPAIGN_STATUS_END = "end";

    /**
     * 计划已经删除
     */
    public static final Integer CAMPAIGN_DELETE = 1;

    /**
     * 计划未删除
     */
    public static final Integer CAMPAIGN_UN_DELETE = 0;

    /**
     * 计划暂停状态：暂停
     */
    public static final Integer PAUSED = 1;

    /**
     * 计划暂停状态：非暂停
     */
    public static final Integer UN_PAUSED = 0;

    /**
     * 计划Id
     */
    private Long campaignId;

    /**
     * 计划名称
     */
    private String campaignName;

    public static final String FEE_TYPE_CPM = "CPM";
    public static final String FEE_TYPE_CPC = "CPC";

    /**
     * 计费方式
     */
    private String feeType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 总预算
     */
    private Double totalBudget;

    /**
     * 每日预算
     */
    private Double dailyBudget;

    /**
     * CPM最高限额
     */
    private Double budgetLimit;

    /**
     * 广告活动展现时间段
     */
    private List<CampaignDayParting> campaignDayPartingList;

    /**
     * 广告活动投放频次
     */
    private CampaignFrequency campaignFrequency;

    /**
     * 计划是否删除
     */
    private Integer deleted;

    /**
     * 是否是完成的计划 完成为1，未完成为0
     */
    private Integer completed;

    /**
     * 是否有时间区域投放 完成为1，未完成为0
     */
    private Integer dayParting;

    /**
     * 是否有投放频次 完成为1，未完成为0
     */
    private Integer frequencyCapping;
    /**
     * 是否只限首屏投放  是  1   否  0
     */
    private Integer aboveTheFold;

    /**
     * 创意个数
     */
    private Integer creativeCount;

    /**
     * 平均出价
     */
    private Double aveBid;

    /**
     * 平均cpc
     */
    private Double cpc;
    /**
     * 平均cpm
     */
    private Double cpm;

    /**
     * 投放次数
     */
    private Long putCount;

    /**
     * 展现次数
     */
    private Long showCount;

    /**
     * 点击次数
     */
    private Long clickCount;

    /**
     * 花费
     */
    private Double cost;

    /**
     * 计划状态
     */
    private String campaignStatus;

    /**
     * 是否有效状态(后台)
     */
    private Integer valid;

    /**
     * 计划状态改变的原因
     */
    private String campaignStatusReason;

    private Double lasMaxBid;
    private Double lastAverageBid;
    private Double lastCtr;
    private Double budgetLimitCpm;

    private Integer hasAudienceTarget;

    private List<CampaignGeo> campaignGeoList;

    private List<CampaignAudience> campaignAudienceList;

    private List<Media> mediaWhiteList;

    private Integer paused;


    /**
     * 广告活动回头客
     */
    private CampaignRetargeting campaignRetargeting;

    /**
     * 投放定向属性
     */
    private List<VisitorArea> visitorAreaList;
    private List<VisitorAge> visitorAgeList;
    private List<VisitorGender> visitorGenderList;
    private List<VisitorMaritalStatus> visitorMaritalStatusList;
    private List<VisitorEducationalBackground> visitorEducationalBackgroundList;

    private List<VisitorInterestPreference> visitorInterestPreferenceList;
    private List<VisitorPurchasePreference> visitorPurchasePreferenceList;

    /**
     * 需要向rtb同步的购买偏好
     * 为满足 消费域名投放与优化需求 添加
     */
    private List<VisitorPurchasePreference> rtbVisitorPurchasePreferenceList;

    /**
     * 需要向rtb同步的人群消费域名
     * 为满足 消费域名投放与优化需求 添加
     */
    private List<VisitorPurchaseDomain> rtbVisitorPurchaseDomainList;


    // 值为true，则应该向没有访问过“当前广告主”的人,再次展示广告
    private Boolean includeNewVisitor;

    //新访客规则组
    private RuleGroup newVisitorRuleGroup;

    // 值为true，则应该向没有访问网站，但看过“当前广告主”的广告的用户再次展示广告
    private Boolean includeAdViewer;

    // 值为true，则应该向曾经访问过“当前广告主”的全部人群,再次展示广告
    private Boolean includeAllOldVisitor;

    //全部老访客规则组
    private RuleGroup allOldVisitorRuleGroup;

    // 值为true，则应该向曾经访问过“当前广告主”的部分人群,再次展示广告
    private Boolean includePartlyOldVisitor;

    private List<CampaignRetargetingRule> retargetingRuleList;

    private List<CampaignCreative> creativeList;
    /**
     * 用来判断是从那个节点发来的数据
     */
    private String nodeSign;

    /**
     * 广告主账号
     */
    private String loginName;

    /**
     * 广告主
     */
    private String displayName;

    /**
     * 域名信息
     */
    private Domain domian;

    /**
     * 活动域名限制
     */
    private List<CampaignDomain> campaignDomainList;

    /**
     * 投放域名限制类型
     */
    private String mediaPreferenceType;

    /**
     * 回头客目标规则ID
     */
    private Long targetRuleId;
    private Boolean isRemoveRule;

    private String userType;

    private FlowFilter filter;

    //回头客类型
    private String regularVisitorsType;
    //目标回头客
    private List<Target> targetList;
    //排除目标回头客
    private List<Target> removeTargetList;

    private CampaignRule campaignRule;

    public Boolean getProfitControl() {
        return profitControl;
    }

    public void setProfitControl(Boolean profitControl) {
        this.profitControl = profitControl;
    }

    /**
     * 是否使用毛利控制: 0 , 1
     * 使用后 计费公式为 valueprice / (1-profitRate) 并且同步profitRate设置到rtb.
     */
    private Boolean profitControl;

    public RuleGroup getNewVisitorRuleGroup() {
        return newVisitorRuleGroup;
    }

    public void setNewVisitorRuleGroup(RuleGroup newVisitorRuleGroup) {
        this.newVisitorRuleGroup = newVisitorRuleGroup;
    }

    public RuleGroup getAllOldVisitorRuleGroup() {
        return allOldVisitorRuleGroup;
    }

    public void setAllOldVisitorRuleGroup(RuleGroup allOldVisitorRuleGroup) {
        this.allOldVisitorRuleGroup = allOldVisitorRuleGroup;
    }

    public List<CampaignDomain> getCampaignDomainList() {
        return campaignDomainList;
    }

    public void setCampaignDomainList(List<CampaignDomain> campaignDomainList) {
        this.campaignDomainList = campaignDomainList;
    }

    public String getMediaPreferenceType() {
        return mediaPreferenceType;
    }

    public void setMediaPreferenceType(String mediaPreferenceType) {
        this.mediaPreferenceType = mediaPreferenceType;
    }

    public String getCampaignName() {
        return EscapeUtil.escape(campaignName);
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Double getCpc() {
        return cpc;
    }

    public void setCpc(Double cpc) {
        this.cpc = cpc;
    }

    public Double getCpm() {
        return cpm;
    }

    public void setCpm(Double cpm) {
        this.cpm = cpm;
    }

    public String getFeeType() {
        return EscapeUtil.escape(feeType);
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Double getDailyBudget() {
        return dailyBudget;
    }

    public void setDailyBudget(Double dailyBudget) {
        this.dailyBudget = dailyBudget;
    }

    public Double getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(Double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public List<CampaignDayParting> getCampaignDayPartingList() {
        return campaignDayPartingList;
    }

    public void setCampaignDayPartingList(
            List<CampaignDayParting> campaignDayPartingList) {
        this.campaignDayPartingList = campaignDayPartingList;
    }

    public CampaignFrequency getCampaignFrequency() {
        return campaignFrequency;
    }

    public void setCampaignFrequency(CampaignFrequency campaignFrequency) {
        this.campaignFrequency = campaignFrequency;
    }

    public List<VisitorArea> getVisitorAreaList() {
        return visitorAreaList;
    }

    public void setVisitorAreaList(List<VisitorArea> visitorAreaList) {
        this.visitorAreaList = visitorAreaList;
    }

    public List<VisitorAge> getVisitorAgeList() {
        return visitorAgeList;
    }

    public void setVisitorAgeList(List<VisitorAge> visitorAgeList) {
        this.visitorAgeList = visitorAgeList;
    }

    public List<VisitorGender> getVisitorGenderList() {
        return visitorGenderList;
    }

    public void setVisitorGenderList(List<VisitorGender> visitorGenderList) {
        this.visitorGenderList = visitorGenderList;
    }

    public List<VisitorMaritalStatus> getVisitorMaritalStatusList() {
        return visitorMaritalStatusList;
    }

    public void setVisitorMaritalStatusList(
            List<VisitorMaritalStatus> visitorMaritalStatusList) {
        this.visitorMaritalStatusList = visitorMaritalStatusList;
    }

    public List<VisitorInterestPreference> getVisitorInterestPreferenceList() {
        return visitorInterestPreferenceList;
    }

    public void setVisitorInterestPreferenceList(
            List<VisitorInterestPreference> visitorInterestPreferenceList) {
        this.visitorInterestPreferenceList = visitorInterestPreferenceList;
    }

    public List<VisitorPurchasePreference> getVisitorPurchasePreferenceList() {
        return visitorPurchasePreferenceList;
    }

    public void setVisitorPurchasePreferenceList(
            List<VisitorPurchasePreference> visitorPurchasePreferenceList) {
        this.visitorPurchasePreferenceList = visitorPurchasePreferenceList;
    }

    public Boolean getIncludeNewVisitor() {
        return includeNewVisitor;
    }

    public void setIncludeNewVisitor(Boolean includeNewVisitor) {
        this.includeNewVisitor = includeNewVisitor;
    }

    public Boolean getIncludeAdViewer() {
        return includeAdViewer;
    }

    public void setIncludeAdViewer(Boolean includeAdViewer) {
        this.includeAdViewer = includeAdViewer;
    }

    public Boolean getIncludeAllOldVisitor() {
        return includeAllOldVisitor;
    }

    public void setIncludeAllOldVisitor(Boolean includeAllOldVisitor) {
        this.includeAllOldVisitor = includeAllOldVisitor;
    }

    public Boolean getIncludePartlyOldVisitor() {
        return includePartlyOldVisitor;
    }

    public void setIncludePartlyOldVisitor(Boolean includePartlyOldVisitor) {
        this.includePartlyOldVisitor = includePartlyOldVisitor;
    }

    public List<CampaignRetargetingRule> getRetargetingRuleList() {
        return retargetingRuleList;
    }

    public void setRetargetingRuleList(
            List<CampaignRetargetingRule> retargetingRuleList) {
        this.retargetingRuleList = retargetingRuleList;
    }

    public List<CampaignCreative> getCreativeList() {
        return creativeList;
    }

    public void setCreativeList(List<CampaignCreative> creativeList) {
        this.creativeList = creativeList;
    }

    public List<VisitorEducationalBackground> getVisitorEducationalBackgroundList() {
        return visitorEducationalBackgroundList;
    }

    public void setVisitorEducationalBackgroundList(
            List<VisitorEducationalBackground> visitorEducationalBackgroundList) {
        this.visitorEducationalBackgroundList = visitorEducationalBackgroundList;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getDayParting() {
        return dayParting;
    }

    public void setDayParting(Integer dayParting) {
        this.dayParting = dayParting;
    }

    public Integer getFrequencyCapping() {
        return frequencyCapping;
    }

    public void setFrequencyCapping(Integer frequencyCapping) {
        this.frequencyCapping = frequencyCapping;
    }

    public List<CampaignGeo> getCampaignGeoList() {
        return campaignGeoList;
    }

    public void setCampaignGeoList(List<CampaignGeo> campaignGeoList) {
        this.campaignGeoList = campaignGeoList;
    }

    public List<CampaignAudience> getCampaignAudienceList() {
        return campaignAudienceList;
    }

    public void setCampaignAudienceList(
            List<CampaignAudience> campaignAudienceList) {
        this.campaignAudienceList = campaignAudienceList;
    }

    public Integer getHasAudienceTarget() {
        return hasAudienceTarget;
    }

    public void setHasAudienceTarget(Integer hasAudienceTarget) {
        this.hasAudienceTarget = hasAudienceTarget;
    }

    public String getCampaignStatus() {
        return EscapeUtil.escape(campaignStatus);
    }

    public void setCampaignStatus(String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public Integer getCreativeCount() {
        return creativeCount;
    }

    public void setCreativeCount(Integer creativeCount) {
        this.creativeCount = creativeCount;
    }

    public Double getAveBid() {
        return aveBid;
    }

    public void setAveBid(Double aveBid) {
        this.aveBid = aveBid;
    }


    public Long getPutCount() {
        return putCount;
    }

    public void setPutCount(Long putCount) {
        this.putCount = putCount;
    }

    public Long getShowCount() {
        return showCount;
    }

    public void setShowCount(Long showCount) {
        this.showCount = showCount;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getPaused() {
        return paused;
    }

    public void setPaused(Integer paused) {
        this.paused = paused;
    }

    public CampaignRetargeting getCampaignRetargeting() {
        return campaignRetargeting;
    }

    public void setCampaignRetargeting(CampaignRetargeting campaignRetargeting) {
        this.campaignRetargeting = campaignRetargeting;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCampaignStatusReason() {
        return EscapeUtil.escape(campaignStatusReason);
    }

    public void setCampaignStatusReason(String campaignStatusReason) {
        this.campaignStatusReason = campaignStatusReason;
    }

    public Double getLasMaxBid() {
        return lasMaxBid;
    }

    public void setLasMaxBid(Double lasMaxBid) {
        this.lasMaxBid = lasMaxBid;
    }

    public Double getLastAverageBid() {
        return lastAverageBid;
    }

    public void setLastAverageBid(Double lastAverageBid) {
        this.lastAverageBid = lastAverageBid;
    }

    public Double getLastCtr() {
        return lastCtr;
    }

    public void setLastCtr(Double lastCtr) {
        this.lastCtr = lastCtr;
    }

    public Double getBudgetLimitCpm() {
        return budgetLimitCpm;
    }

    public void setBudgetLimitCpm(Double budgetLimitCpm) {
        this.budgetLimitCpm = budgetLimitCpm;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getNodeSign() {
        return nodeSign;
    }

    public void setNodeSign(String nodeSign) {
        this.nodeSign = nodeSign;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Domain getDomian() {
        return domian;
    }

    public void setDomian(Domain domian) {
        this.domian = domian;
    }

    public List<VisitorPurchasePreference> getRtbVisitorPurchasePreferenceList() {
        return rtbVisitorPurchasePreferenceList;
    }

    public void setRtbVisitorPurchasePreferenceList(
            List<VisitorPurchasePreference> rtbVisitorPurchasePreferenceList) {
        this.rtbVisitorPurchasePreferenceList = rtbVisitorPurchasePreferenceList;
    }


    public List<VisitorPurchaseDomain> getRtbVisitorPurchaseDomainList() {
        return rtbVisitorPurchaseDomainList;
    }

    public void setRtbVisitorPurchaseDomainList(
            List<VisitorPurchaseDomain> rtbVisitorPurchaseDomainList) {
        this.rtbVisitorPurchaseDomainList = rtbVisitorPurchaseDomainList;
    }

    public Integer getAboveTheFold() {
        return aboveTheFold;
    }

    public void setAboveTheFold(Integer aboveTheFold) {
        this.aboveTheFold = aboveTheFold;
    }

    public List<Media> getMediaWhiteList() {
        return mediaWhiteList;
    }

    public void setMediaWhiteList(List<Media> mediaWhiteList) {
        this.mediaWhiteList = mediaWhiteList;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public FlowFilter getFilter() {
        return filter;
    }

    public void setFilter(FlowFilter filter) {
        this.filter = filter;
    }

    public Long getTargetRuleId() {
        return targetRuleId;
    }

    public void setTargetRuleId(Long targetRuleId) {
        this.targetRuleId = targetRuleId;
    }

    public String getRegularVisitorsType() {
        return regularVisitorsType;
    }

    public void setRegularVisitorsType(String regularVisitorsType) {
        this.regularVisitorsType = regularVisitorsType;
    }

    public List<Target> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Target> targetList) {
        this.targetList = targetList;
    }

    public List<Target> getRemoveTargetList() {
        return removeTargetList;
    }

    public void setRemoveTargetList(List<Target> removeTargetList) {
        this.removeTargetList = removeTargetList;
    }

    public CampaignRule getCampaignRule() {
        return campaignRule;
    }

    public void setCampaignRule(CampaignRule campaignRule) {
        this.campaignRule = campaignRule;
    }


    public Boolean getIsRemoveRule() {
        return isRemoveRule;
    }

    public void setIsRemoveRule(Boolean isRemoveRule) {
        this.isRemoveRule = isRemoveRule;
    }

    @Override
    public String toString() {
        return "Campaign [campaignId=" + campaignId + ", campaignName="
                + campaignName + ", feeType=" + feeType + ", startTime="
                + startTime + ", endTime=" + endTime + ", totalBudget="
                + totalBudget + ", dailyBudget=" + dailyBudget
                + ", budgetLimit=" + budgetLimit + ", campaignDayPartingList="
                + campaignDayPartingList + ", campaignFrequency="
                + campaignFrequency + ", deleted=" + deleted + ", completed="
                + completed + ", dayParting=" + dayParting
                + ", frequencyCapping=" + frequencyCapping + ", creativeCount="
                + creativeCount + ", aveBid=" + aveBid + ", putCount="
                + putCount + ", showCount=" + showCount + ", clickCount="
                + clickCount + ", cost=" + cost + ", campaignStatus="
                + campaignStatus + ", valid=" + valid
                + ", campaignStatusReason=" + campaignStatusReason
                + ", lasMaxBid=" + lasMaxBid + ", lastAverageBid="
                + lastAverageBid + ", lastCtr=" + lastCtr + ", budgetLimitCpm="
                + budgetLimitCpm + ", hasAudienceTarget=" + hasAudienceTarget
                + ", campaignGeoList=" + campaignGeoList
                + ", campaignAudienceList=" + campaignAudienceList
                + ", paused=" + paused + ", campaignRetargeting="
                + campaignRetargeting + ", visitorAreaList=" + visitorAreaList
                + ", visitorAgeList=" + visitorAgeList + ", visitorGenderList="
                + visitorGenderList + ", visitorMaritalStatusList="
                + visitorMaritalStatusList
                + ", visitorEducationalBackgroundList="
                + visitorEducationalBackgroundList
                + ", visitorInterestPreferenceList="
                + visitorInterestPreferenceList
                + ", visitorPurchasePreferenceList="
                + visitorPurchasePreferenceList
                + ", rtbVisitorPurchasePreferenceList="
                + rtbVisitorPurchasePreferenceList
                + ", rtbVisitorPurchaseDomainList=" + rtbVisitorPurchaseDomainList
                + ", includeNewVisitor=" + includeNewVisitor
                + ", newVisitorRuleGroup=" + newVisitorRuleGroup
                + ", includeAdViewer=" + includeAdViewer
                + ", includeAllOldVisitor=" + includeAllOldVisitor
                + ", allOldVisitorRuleGroup=" + allOldVisitorRuleGroup
                + ", includePartlyOldVisitor=" + includePartlyOldVisitor
                + ", retargetingRuleList=" + retargetingRuleList
                + ", creativeList=" + creativeList + ", nodeSign=" + nodeSign
                + ", loginName=" + loginName + ", displayName=" + displayName
                + ", domian=" + domian + ", campaignDomainList="
                + campaignDomainList + ", mediaPreferenceType="
                + mediaPreferenceType + "]";
    }

}
