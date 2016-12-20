package bean;

import java.util.List;

public class RecordBean{
	
	private Record src;

	public RecordBean(Record record){
		this.src = record;
	}

	public String getDmpid() {
		return src.getDmpid();
	}

	public List<String> getIdfa() {
		return src.getIdfa();
	}

	public List<String> getAndroid_id() {
		return src.getAndroid_id();
	}

	public List<String> getAdvertising_id() {
		return src.getAdvertising_id();
	}

	public List<String> getImei() {
		return src.getImei();
	}

	public List<String> getMac() {
		return src.getMac();
	}

	public List<String> getImei_sha1() {
		return src.getImei_sha1();
	}

	public List<String> getImei_md5() {
		return src.getImei_md5();
	}

	public List<String> getMac_sha1() {
		return src.getMac_sha1();
	}

	public List<String> getMac_md5() {
		return src.getMac_md5();
	}

	public List<String> getIdfa_md5() {
		return src.getIdfa_md5();
	}

	public String getSupplier() {
		return src.getSupplier();
	}

	public String getAppid() {
		return src.getAppid();
	}
}
