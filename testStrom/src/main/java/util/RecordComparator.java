package util;

import java.util.Comparator;

import bean.Record;

public class RecordComparator implements Comparator<Record> {

	public int compare(Record arg0, Record arg1) {
		if(arg0.getDate().before(arg1.getDate())){
			return 1;
		}
		if(arg0.getDate().after((arg1.getDate()))){
			return -1;
		}
		return 0;
	}

}
