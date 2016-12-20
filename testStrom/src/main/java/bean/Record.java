package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

public class Record implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7490785231572498915L;

	private String str;

	private String dmpid;

	//字符串格式：idfa,android_id,advertising_id,imei,mac,imei_sha1,imei_md5,mac_sha1,mac_md5,idfa_md5,supplier,appid
	private List<String> idfa = new ArrayList<String>();
	private List<String> android_id = new ArrayList<String>();
	private List<String> advertising_id = new ArrayList<String>();
	private List<String> imei = new ArrayList<String>();
	private List<String> mac = new ArrayList<String>();
	private List<String> imei_sha1 = new ArrayList<String>();
	private List<String> imei_md5 = new ArrayList<String>();
	private List<String> mac_sha1 = new ArrayList<String>();
	private List<String> mac_md5 = new ArrayList<String>();
	private List<String> idfa_md5 = new ArrayList<String>();
	private String supplier;
	private String appid;
	private List<Boolean> imei_valid = new ArrayList<Boolean>();
	private List<Boolean> mac_valid = new ArrayList<Boolean>();
	private List<Boolean> idfa_valid = new ArrayList<Boolean>();
	
	public Record(){}
	
	public Record(String str){
		this.str = str;
    	String[] strings = str.split(",");
    	if(strings.length > 0 && notNull(strings[0])){
    		idfa.add(strings[0].toLowerCase());
    		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]{1,})(([/\\s-][0-9a-zA-Z]{1,}){4})$");
        	if(pattern.matcher(strings[0].trim()).matches()){
        		idfa_valid.add(true);
        		idfa_md5.add(DigestUtils.md5Hex(strings[0].toLowerCase()).toLowerCase());
        		idfa_md5.add(DigestUtils.md5Hex(strings[0].toUpperCase()).toLowerCase());
        	}else{
        		idfa_valid.add(false);
        	}
    	}
    	if(strings.length > 1 && notNull(strings[1])){
    		android_id.add(strings[1].toLowerCase());
    	}
    	if(strings.length > 2 && notNull(strings[2])){
    		advertising_id.add(strings[2].toLowerCase());
    	}
    	if(strings.length > 3 && notNull(strings[3])){
    		imei.add(strings[3].toLowerCase());
    		Pattern pattern = Pattern.compile("^[0-9]{15}$");
        	if(!pattern.matcher(strings[3].trim()).matches()){
        		imei_valid.add(false);
        	}else{
        		imei_valid.add(true);
        		imei_md5.add(DigestUtils.md5Hex(strings[3]).toLowerCase());
        		imei_sha1.add(DigestUtils.shaHex(strings[3]).toLowerCase());
        	}
    	}
    	if(strings.length > 4 && notNull(strings[4])){
    		mac.add(strings[4].toLowerCase());
    		Pattern pattern = Pattern.compile("^([0-9a-fA-F]{2})(([\\S][0-9a-fA-F]{2}){5})$");
        	if(!pattern.matcher(strings[4].trim()).matches()){
        		mac_valid.add(false);
        	}else{
        		mac_valid.add(true);
        		mac_md5.add(DigestUtils.md5Hex(strings[4].toLowerCase()).toLowerCase());
        		mac_md5.add(DigestUtils.md5Hex(strings[4].toUpperCase()).toLowerCase());
        		mac_sha1.add(DigestUtils.shaHex(strings[4].toLowerCase()).toLowerCase());
        		mac_sha1.add(DigestUtils.shaHex(strings[4].toUpperCase()).toLowerCase());
        	}
    	}
    	if(strings.length > 5 && notNull(strings[5])){
    		imei_sha1.add(strings[5].toLowerCase());
    	}
    	if(strings.length > 6 && notNull(strings[6])){
    		imei_md5.add(strings[6].toLowerCase());
    	}
    	if(strings.length > 7 && notNull(strings[7])){
    		mac_sha1.add(strings[7].toLowerCase());
    	}
    	if(strings.length > 8 && notNull(strings[8])){
    		mac_md5.add(strings[8].toLowerCase());
    	}
    	if(strings.length > 9 && notNull(strings[9])){
    		idfa_md5.add(strings[9].toLowerCase());
    	}
    	if(strings.length > 10 && notNull(strings[10])){
    		Pattern pattern = Pattern.compile("^[0-9]{4}$");
        	if(pattern.matcher(strings[10].trim()).matches()){
        		supplier = strings[10];
        	}
    	}
    	if(strings.length > 11 && notNull(strings[11])){
    		appid = strings[11];
    	}
		imei_sha1 = removeDuplicate(imei_sha1,5);
		imei_md5 = removeDuplicate(imei_md5,5);
		mac_md5 = removeDuplicate(mac_md5,5);
		mac_sha1 = removeDuplicate(mac_sha1,5);
		idfa_md5 = removeDuplicate(idfa_md5,5);
	}
	
	public Record(Map<String, String> map){
		if(map.get("idfa") != null){
			String idfas = map.get("idfa");
			this.idfa.addAll(removeEmptyList(idfas.split(",")));
    		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]{1,})(([/\\s-][0-9a-zA-Z]{1,}){4})$");
    		for(String idfa : this.idfa){
            	if(pattern.matcher(idfa.trim()).matches()){
            		idfa_valid.add(true);
            		if(map.get("idfa_md5") == null){
            			this.idfa_md5.add(DigestUtils.md5Hex(idfa.toLowerCase()).toLowerCase());
            			this.idfa_md5.add(DigestUtils.md5Hex(idfa.toUpperCase()).toLowerCase());
            		}
            	}
    		}
		}
		if(map.get("android_id") != null){
			String android_ids = map.get("android_id");
			this.android_id.addAll(removeEmptyList(android_ids.split(",")));
		}
		if(map.get("advertising_id") != null){
			String advertising_ids = map.get("advertising_id");
			this.advertising_id.addAll(removeEmptyList(advertising_ids.split(",")));
		}
		if(map.get("imei") != null){
			String imeis = map.get("imei");
			this.imei.addAll(removeEmptyList(imeis.split(",")));
    		Pattern pattern = Pattern.compile("^[0-9]{15}$");
    		for(String imei : this.imei){
	        	if(pattern.matcher(imei.trim()).matches()){
	        		imei_valid.add(true);
	        		if(map.get("imei_md5") == null){
	        			this.imei_md5.add(DigestUtils.md5Hex(imei).toLowerCase());
	        		}
	        		if(map.get("imei_sha1") == null){
	        			this.imei_sha1.add(DigestUtils.shaHex(imei).toLowerCase());
	        		}
	        	}
    		}
		}
		if(map.get("mac") != null){
			String macs = map.get("mac");
			this.mac.addAll(removeEmptyList(macs.split(",")));
    		Pattern pattern = Pattern.compile("^([0-9a-fA-F]{2})(([\\S][0-9a-fA-F]{2}){5})$");
    		for(String mac : this.mac){
	        	if(pattern.matcher(mac.trim()).matches()){
	        		mac_valid.add(true);
	        		if(map.get("mac_md5") == null){
	        			this.mac_md5.add(DigestUtils.md5Hex(mac.toLowerCase()).toLowerCase());
	        			this.mac_md5.add(DigestUtils.md5Hex(mac.toUpperCase()).toLowerCase());
	        		}
	        		if(map.get("mac_sha1") == null){
	        			this.mac_sha1.add(DigestUtils.shaHex(mac.toLowerCase()).toLowerCase());
	        			this.mac_sha1.add(DigestUtils.shaHex(mac.toUpperCase()).toLowerCase());
	        		}
	        	}
    		}
		}
		if(map.get("imei_sha1") != null){
			String imei_sha1s = map.get("imei_sha1");
			this.imei_sha1.addAll(removeEmptyList(imei_sha1s.split(",")));
		}
		if(map.get("imei_md5") != null){
			String imei_md5s = map.get("imei_md5");
			this.imei_md5.addAll(removeEmptyList(imei_md5s.split(",")));
		}
		if(map.get("mac_sha1") != null){
			String mac_sha1s = map.get("mac_sha1");
			this.mac_sha1.addAll(removeEmptyList(mac_sha1s.split(",")));
		}
		if(map.get("mac_md5") != null){
			String mac_md5s = map.get("mac_md5");
			this.mac_md5.addAll(removeEmptyList(mac_md5s.split(",")));
		}
		if(map.get("idfa_md5") != null){
			String idfa_md5s = map.get("idfa_md5");
			this.idfa_md5.addAll(removeEmptyList(idfa_md5s.split(",")));
		}
		imei_sha1 = removeDuplicate(imei_sha1,5);
		imei_md5 = removeDuplicate(imei_md5,5);
		mac_md5 = removeDuplicate(mac_md5,5);
		mac_sha1 = removeDuplicate(mac_sha1,5);
		idfa_md5 = removeDuplicate(idfa_md5,5);
	}
	
	private static List<String> removeEmptyList(String[] list) {  
        List<String> list1 = new ArrayList<String>();  
        Set<String> set = new HashSet<String>();
          
        if(list==null||list.length<=0)  
            return null;  
        for(int i=0;i<list.length;i++) {  
        	String t = list[i].toLowerCase();  
            if(t!=null && !set.contains(t)){
                list1.add(t); 
                set.add(t);
            }
        }  
          
        return list1;  
    }  
	
	private Boolean notNull(String str){
		if(str == null || "".equals(str) || "null".equals(str))
			return false;
		return true;
	}
	
	public boolean isNull(){
		if(idfa.isEmpty() && android_id.isEmpty() && advertising_id.isEmpty() && imei.isEmpty()
				&& mac.isEmpty() && imei_sha1.isEmpty() && imei_md5.isEmpty() && mac_sha1.isEmpty() && idfa_md5.isEmpty()){
			return true;
		}
		return false;
	}


	public List<String> getAll(){
		List<String> list = new ArrayList<String>();
		list.addAll(idfa);
		list.addAll(android_id);
		list.addAll(advertising_id);
		list.addAll(imei);
		list.addAll(mac);
		list.addAll(imei_sha1);
		list.addAll(imei_md5);
		list.addAll(mac_sha1);
		list.addAll(idfa_md5);
		return list;
	}
	
	public Map<String,List<String>> getMap(){
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("idfa", idfa);
		map.put("android_id", android_id);
		map.put("advertising_id", advertising_id);
		map.put("imei", imei);
		map.put("mac", mac);
		map.put("imei_sha1", imei_sha1);
		map.put("imei_md5", imei_md5);
		map.put("mac_md5", mac_md5);
		map.put("mac_sha1", mac_sha1);
		map.put("idfa_md5", idfa_md5);
		
		return map;
	}
	
	public void setNull(String key, String value){
		switch(key){
		case "idfa":
			idfa.remove(value);
			break;
		case "android_id":
			android_id.remove(value);
			break;
		case "advertising_id":
			advertising_id.remove(value);
			break;
		case "imei":
			imei.remove(value);
			break;
		case "mac":
			mac.remove(value);
			break;
		case "imei_sha1":
			imei_sha1.remove(value);
			break;
		case "imei_md5":
			imei_md5.remove(value);
			break;
		case "mac_md5":
			mac_md5.remove(value);
			break;
		case "mac_sha1":
			mac_sha1.remove(value);
			break;
		case "idfa_md5":
			idfa_md5.remove(value);
			break;
		
		}
	}
	
	public Record addRecord(Record newRecord){
		this.idfa.addAll(newRecord.getIdfa());
		this.android_id.addAll(newRecord.getAndroid_id());
		this.advertising_id.addAll(newRecord.getAdvertising_id());
		this.imei.addAll(newRecord.getImei());
		this.mac.addAll(newRecord.getMac());
		this.imei_sha1.addAll(newRecord.getImei_sha1());
		this.imei_md5.addAll(newRecord.getImei_md5());
		this.mac_md5.addAll(newRecord.getMac_md5());
		this.mac_sha1.addAll(newRecord.getMac_sha1());
		this.idfa_md5.addAll(newRecord.getIdfa_md5());
		
		idfa = removeDuplicate(idfa,3);
		android_id = removeDuplicate(android_id,3);
		advertising_id = removeDuplicate(advertising_id,3);
		imei = removeDuplicate(imei,3);
		mac = removeDuplicate(mac,3);
		imei_sha1 = removeDuplicate(imei_sha1,5);
		imei_md5 = removeDuplicate(imei_md5,5);
		mac_md5 = removeDuplicate(mac_md5,5);
		mac_sha1 = removeDuplicate(mac_sha1,5);
		idfa_md5 = removeDuplicate(idfa_md5,5);
		return this;
	}
	
	public static List<String> removeDuplicate(List<String> list, int total) {
		Set<String> set = new HashSet<String>();
		for (int i = list.size() - 1; i >= 0 && i >= list.size() - total; i--) {
			set.add(list.get(i));
		}
		return new ArrayList<String>(set);
	}
	
	public Date getDate(){
		if(dmpid != null){
			String time = dmpid.substring(13);
			return new Date(new Long(time));
		}
		return null;
	}
	
	public RecordBean getBean(){
		return new RecordBean(this);
	}
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getDmpid() {
		return dmpid;
	}

	public void setDmpid(String dmpid) {
		this.dmpid = dmpid;
	}

	public List<String> getIdfa() {
		return idfa;
	}

	public void setIdfa(List<String> idfa) {
		this.idfa = idfa;
	}

	public List<String> getAndroid_id() {
		return android_id;
	}

	public void setAndroid_id(List<String> android_id) {
		this.android_id = android_id;
	}

	public List<String> getAdvertising_id() {
		return advertising_id;
	}

	public void setAdvertising_id(List<String> advertising_id) {
		this.advertising_id = advertising_id;
	}

	public List<String> getImei() {
		return imei;
	}

	public void setImei(List<String> imei) {
		this.imei = imei;
	}

	public List<String> getMac() {
		return mac;
	}

	public void setMac(List<String> mac) {
		this.mac = mac;
	}

	public List<String> getImei_sha1() {
		return imei_sha1;
	}

	public void setImei_sha1(List<String> imei_sha1) {
		this.imei_sha1 = imei_sha1;
	}

	public List<String> getImei_md5() {
		return imei_md5;
	}

	public void setImei_md5(List<String> imei_md5) {
		this.imei_md5 = imei_md5;
	}

	public List<String> getMac_sha1() {
		return mac_sha1;
	}

	public void setMac_sha1(List<String> mac_sha1) {
		this.mac_sha1 = mac_sha1;
	}

	public List<String> getMac_md5() {
		return mac_md5;
	}

	public void setMac_md5(List<String> mac_md5) {
		this.mac_md5 = mac_md5;
	}

	public List<String> getIdfa_md5() {
		return idfa_md5;
	}

	public void setIdfa_md5(List<String> idfa_md5) {
		this.idfa_md5 = idfa_md5;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public List<Boolean> getImei_valid() {
		return imei_valid;
	}

	public void setImei_valid(List<Boolean> imei_valid) {
		this.imei_valid = imei_valid;
	}

	public List<Boolean> getMac_valid() {
		return mac_valid;
	}

	public void setMac_valid(List<Boolean> mac_valid) {
		this.mac_valid = mac_valid;
	}

	public List<Boolean> getIdfa_valid() {
		return idfa_valid;
	}

	public void setIdfa_valid(List<Boolean> idfa_valid) {
		this.idfa_valid = idfa_valid;
	}
	
}
