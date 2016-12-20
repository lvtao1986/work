/**
 * VisitorProperty.java
 * com.emarbox.dsp.domain
 *
 * ver  1.0
 * date 2012-5-30
 * author 耿志新
 *
 * Copyright (emar) 2012, EMAR All Rights Reserved.
*/

package com.emarbox.dsp.domain;

import java.util.List;

/**
 * ClassName:VisitorProperty 人群属性
 *
 * @author   耿志新
 * @version  1.0
 * @since    Ver 1.0
 * @Date	 2012-5-30	下午2:37:22
 *
 */
public class VisitorProperty {
	
	private List<VisitorArea> visitorAreaList;
	private List<VisitorAge> visitorAgeList;
	private List<VisitorGender> visitorGenderList;
	private List<VisitorMaritalStatus> visitorMaritalStatusList;
	private List<VisitorEducationalBackground> visitorEducationalBackgroundList;

	private List<VisitorInterestPreference> visitorInterestPreferenceList;
	private List<VisitorPurchasePreference> visitorPurchasePreferenceList;
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
	public List<VisitorEducationalBackground> getVisitorEducationalBackgroundList() {
		return visitorEducationalBackgroundList;
	}
	public void setVisitorEducationalBackgroundList(
			List<VisitorEducationalBackground> visitorEducationalBackgroundList) {
		this.visitorEducationalBackgroundList = visitorEducationalBackgroundList;
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
	@Override
	public String toString() {
		return "CampaignCrowdProperty [visitorAreaList=" + visitorAreaList
				+ ", visitorAgeList=" + visitorAgeList + ", visitorGenderList="
				+ visitorGenderList + ", visitorMaritalStatusList="
				+ visitorMaritalStatusList
				+ ", visitorEducationalBackgroundList="
				+ visitorEducationalBackgroundList
				+ ", visitorInterestPreferenceList="
				+ visitorInterestPreferenceList
				+ ", visitorPurchasePreferenceList="
				+ visitorPurchasePreferenceList + "]";
	}
	
	
}

