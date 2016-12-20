package com.emarbox.dsp.monitor.util;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用zhouLong提供的代码记录DMP日志
 */
public class DmpLogWriter {

    private static final DmpLogWriter instance = new DmpLogWriter();

    private static final Logger log = Logger.getLogger(DmpLogWriter.class);

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    /**
     * Map<yyyyMMdd,Map<supplier_HH,FileWriter>>
     **/
    private static final Map<String, Map<String, FileWriter>> map = new HashMap<String, Map<String, FileWriter>>();

    /**
     * 可重入的互斥锁
     **/
    private static final Lock lock = new ReentrantLock();

    private DmpLogWriter() {

    }

    public static DmpLogWriter getInstance() {
        return instance;
    }

    /**
     * 1.将当前小时的文件指针放入map，一个文件一个FileWriter。重复使用 <br>
     * 2.每小时创建新文件,如果map中的文件指针是前几个小时的，则close，创建新的FileWriter返回。
     *
     * @return java.io.FileWriter
     * @throws IOException
     * @see FileWriter
     */
    @SuppressWarnings("rawtypes")
    private FileWriter fileWriterManage(String supplier) throws Exception {

        String date = df.format(new Date());
        String path = Config.getString("dmp.file.path");
        String prefix = Config.getString("dmp.file.name.prefix");
        String suffix = Config.getString("dmp.file.name.suffix");

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        String hourStr = (hour >= 10) ? (String.valueOf(hour)) : ("0" + hour);

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }

        String fileName = path + "/" + prefix + "_" + supplier + "_" + date + hourStr + suffix;
        String mkey = supplier + "_" + hourStr;

        File file = new File(fileName);
        Map<String, FileWriter> fm = map.get(date); // 通过日期和时间得到文件指针
        if (null != fm) {
            // 如果map中存放的文件指针是当前时间(count)的则返回该文件的指针，否则,创建新的文件指针
            if (fm.containsKey(mkey)) {
                return fm.get(mkey);
            } else {
                try {
                    lock.lock();
                    FileWriter fw = new FileWriter(file, true); // 创建新的FileWriter
                    fm.put(mkey, fw);
                    return fw; // 返回新的文件指针
                } finally {
                    lock.unlock();
                }
            }
        } else {
            try {
                lock.lock();
                //关闭昨天的map
                for (Map.Entry<String, Map<String, FileWriter>> entry : map.entrySet()) {
                    if (!date.equals(entry.getKey())) {
                        for (Map.Entry<String, FileWriter> e : entry.getValue().entrySet()) {
                            e.getValue().close();
                        }
                        map.remove(entry.getKey()); // 从map中移除
                    }
                }
            }catch (Exception e){
                log.warn(e.getMessage(), e);
            }finally {
                lock.unlock();
            }

            //新的一天创建新的map
            try {
                lock.lock();
                Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
                FileWriter fw = new FileWriter(file, true);
                fwm.put(mkey, fw);
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
    public void info(String supplier, String info) {
        try {
            FileWriter fw = fileWriterManage(supplier);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(info + "\n");
            bw.flush();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

    }

}
