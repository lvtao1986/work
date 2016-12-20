package util;

import bean.Record;

public class AndroidIdFilter implements CustomFilter {

	@Override
	public Record filt(Record record) {
		if(!record.getAndroid_id().isEmpty() && record.getAll().size() > record.getAndroid_id().size()){
			record.getAndroid_id().clear();
		}
		return record;
	}

}
