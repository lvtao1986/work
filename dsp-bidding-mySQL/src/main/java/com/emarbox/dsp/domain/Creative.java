package com.emarbox.dsp.domain;

import java.util.List;

/**
 * 广告创意
 * 
 * @author zhaidw
 * 
 */
public class Creative extends DspDomain {

	private static final long serialVersionUID = 139016064831428618L;
	
	/**
	 * 创意类型：图片
	 */
	public static final String CREATIVE_TYPE_IMAGE = "image";
	
	/**
	 * 创意类型：Flash
	 */
	public static final String CREATIVE_TYPE_FLASH = "flash";

	private Long creativeId;
	private Long creativeSetId;
	private Long projectId;
	private Long campaignId;
	
	private Long adgroupId;

	private Long supplierId;

	private String creativeName;
	private String creativeImageAlt;
	private String creativeType;
	private String destinationUrl;
	private String fileUrl;
	private Integer fileWidth;
	private Integer fileHeight;
	private Integer fileSize;
	private String status;

	// /////////////////////////// 审核相关字段，请勿删除
	// ////////////////////////////////////////
    private String campaignDestinationUrl;
    private String campaignFileUrl;
	/**
	 * 创意的最终状态，只读，其值由程序进行控制；
	 * 
	 * 所有供应商审核通过，且运营人员审核通过后，创意状态才修改为审核通过
	 * 
	 */
	private String creativeStatus;

	/**
	 * 创意的运营人员审核状态
	 */
	private String creativeAuditStatus;
	
	/**
	 * 创意审核请求 包括每个供应商的审核状态
	 * @author 耿志新
	 */
	private List<CreativeRequest> creativeRequestList;

	// /////////////////////////// 审核相关字段，请勿删除
	// ////////////////////////////////////////

	// 扩展属性fileUrl的格式，也就是后缀
	private String fileUrlSuffix;

	private boolean isUpdateFile;

	private String fileName;
	private String path;

	public Creative() {
		super();
	}

	public Long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getCreativeName() {
		return creativeName;
	}

	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}

	public String getCreativeImageAlt() {
		return creativeImageAlt;
	}

	public void setCreativeImageAlt(String creativeImageAlt) {
		this.creativeImageAlt = creativeImageAlt;
	}

	public String getCreativeType() {
		return creativeType;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getFileWidth() {
		return fileWidth;
	}

	public void setFileWidth(Integer fileWidth) {
		this.fileWidth = fileWidth;
	}

	public Integer getFileHeight() {
		return fileHeight;
	}

	public void setFileHeight(Integer fileHeight) {
		this.fileHeight = fileHeight;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isUpdateFile() {
		return isUpdateFile;
	}

	public void setUpdateFile(boolean isUpdateFile) {
		this.isUpdateFile = isUpdateFile;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getFileUrlSuffix() {
		return fileUrlSuffix;
	}

	public void setFileUrlSuffix(String fileUrlSuffix) {
		this.fileUrlSuffix = fileUrlSuffix;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreativeStatus() {
		return creativeStatus;
	}

	public void setCreativeStatus(String creativeStatus) {
		this.creativeStatus = creativeStatus;
	}

	public String getCreativeAuditStatus() {
		return creativeAuditStatus;
	}

	public void setCreativeAuditStatus(String creativeAuditStatus) {
		this.creativeAuditStatus = creativeAuditStatus;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getAdgroupId() {
		return adgroupId;
	}

	public void setAdgroupId(Long adgroupId) {
		this.adgroupId = adgroupId;
	}
	
	public List<CreativeRequest> getCreativeRequestList() {
		return creativeRequestList;
	}

	public void setCreativeRequestList(List<CreativeRequest> creativeRequestList) {
		this.creativeRequestList = creativeRequestList;
	}

	public String getCampaignDestinationUrl() {
		return campaignDestinationUrl;
	}

	public void setCampaignDestinationUrl(String campaignDestinationUrl) {
		this.campaignDestinationUrl = campaignDestinationUrl;
	}

	public String getCampaignFileUrl() {
		return campaignFileUrl;
	}

	public void setCampaignFileUrl(String campaignFileUrl) {
		this.campaignFileUrl = campaignFileUrl;
	}

	public Long getCreativeSetId() {
		return creativeSetId;
	}

	public void setCreativeSetId(Long creativeSetId) {
		this.creativeSetId = creativeSetId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Creative [creativeId=");
		builder.append(creativeId);
		builder.append(", creativeSetId=");
		builder.append(creativeSetId);
		builder.append(", projectId=");
		builder.append(projectId);
		builder.append(", campaignId=");
		builder.append(campaignId);
		builder.append(", adgroupId=");
		builder.append(adgroupId);
		builder.append(", supplierId=");
		builder.append(supplierId);
		builder.append(", creativeName=");
		builder.append(creativeName);
		builder.append(", creativeImageAlt=");
		builder.append(creativeImageAlt);
		builder.append(", creativeType=");
		builder.append(creativeType);
		builder.append(", destinationUrl=");
		builder.append(destinationUrl);
		builder.append(", fileUrl=");
		builder.append(fileUrl);
		builder.append(", fileWidth=");
		builder.append(fileWidth);
		builder.append(", fileHeight=");
		builder.append(fileHeight);
		builder.append(", fileSize=");
		builder.append(fileSize);
		builder.append(", campaignDestinationUrl=");
		builder.append(campaignDestinationUrl);
		builder.append(", campaignFileUrl=");
		builder.append(campaignFileUrl);
		builder.append(", creativeStatus=");
		builder.append(creativeStatus);
		builder.append(", creativeAuditStatus=");
		builder.append(creativeAuditStatus);
		builder.append(", creativeRequestList=");
		builder.append(creativeRequestList);
		builder.append(", fileUrlSuffix=");
		builder.append(fileUrlSuffix);
		builder.append(", isUpdateFile=");
		builder.append(isUpdateFile);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", path=");
		builder.append(path);
		builder.append(",status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}



}
