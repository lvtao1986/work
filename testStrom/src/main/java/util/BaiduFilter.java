package util;

import bean.Record;

public class BaiduFilter implements CustomFilter {

	public Record filt(Record record) {
		if("5050".equals(record.getSupplier())){
			record.getMac().clear();
			record.getMac_md5().clear();
			record.getMac_sha1().clear();
		}
		return record;
	}

}
