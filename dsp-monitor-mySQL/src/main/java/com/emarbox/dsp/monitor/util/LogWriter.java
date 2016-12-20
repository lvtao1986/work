package com.emarbox.dsp.monitor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * 使用zhouLong提供的代码记录业务日志
 */
public class LogWriter {

	private static final LogWriter instance = new LogWriter();

	private static final Logger log = Logger.getLogger(LogWriter.class);

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

	/** Map<yyyyMMdd,Map<count,FileWriter>> **/
	private static final Map<String, Map<Integer, FileWriter>> map = new HashMap<String, Map<Integer, FileWriter>>();

	/** 可重入的互斥锁 **/
	private static final Lock lock = new ReentrantLock();

	private LogWriter() {

	}

	public static LogWriter getInstance() {
		return instance;
	}

	/**
	 * 1.将当前小时的文件指针放入map，一个文件一个FileWriter。重复使用 <br>
	 * 2.每小时创建新文件,如果map中的文件指针是前几个小时的，则close，创建新的FileWriter返回。
	 * 
	 * @return java.io.FileWriter
	 * @throws IOException
	 * @see java.io.FileWriter
	 */
	@SuppressWarnings("rawtypes")
	private FileWriter fileWriterManage() throws Exception {

		String date = df.format(new Date());
		String path = Config.getString("file.path");
		String prefix = Config.getString("file.name.prefix");
		String suffix = Config.getString("file.name.suffix");

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		int count = hour * 12 + minute / 5 + 1;

		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}

		String fileName = path + "/" + prefix + "_" + date + "_" + count + suffix;

		File file = new File(fileName);
		FileWriter fileWriter;
		Map<Integer, FileWriter> fm = map.get(date); // 通过日期和时间得到文件指针
		if (null != fm) {
			// 如果map中存放的文件指针是当前时间(count)的则返回该文件的指针，否则，关闭当前小时之前的文件流，重新创建新的文件指针
			if (fm.containsKey(count)) {
				return fm.get(count);
			} else {
				Iterator<?> iter = fm.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					fileWriter = (FileWriter) entry.getValue();
					if (null != fileWriter) {
						try {
							fileWriter.close(); // 关闭之前的filewriter
							// 数据节点处理完一个小时log后，新建一个同名文件，后缀.all
							// File fileall = new File(path + "/log." +
							// entry.getKey() + ".all");
							// fileall.createNewFile();
						} catch (Exception e) {
							log.error(e.getMessage());
						}
					}
				}
				map.remove(date); // 从map中移除
				Map<Integer, FileWriter> fwm = new HashMap<Integer, FileWriter>();
				FileWriter fw = new FileWriter(file, true); // 创建新的FileWriter
				try {
					lock.lock();
					fwm.put(count, fw);
					map.put(date, fwm);
					return fw; // 返回新的文件指针
				} finally {
					lock.unlock();
				}
			}
		} else {
			Map<Integer, FileWriter> fwm = new HashMap<Integer, FileWriter>();
			FileWriter fw = new FileWriter(file, true);
			try {
				lock.lock();
				fwm.put(count, fw);
				map.put(date, fwm);
				return fw;
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * 将log写入文件
	 * 
	 * @throws IOException
	 */
	public void info(String info) {
		try {
			FileWriter fw = fileWriterManage();
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(info + "\n");
			bw.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

}
