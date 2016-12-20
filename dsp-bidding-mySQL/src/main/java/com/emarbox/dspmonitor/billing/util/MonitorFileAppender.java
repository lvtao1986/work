package com.emarbox.dspmonitor.billing.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.emarbox.dsp.util.Function;

/**
 * 扩展 log4j FilleAppender
 * 
 * @author emarzhu
 * 
 */
public class MonitorFileAppender extends FileAppender {

	// The code assumes that the following constants are in a increasing
	// sequence.
	private String suffix = ".log";
	
	private String fname ;
	
	/**
	 * The log file will be renamed to the value of the scheduledFilename
	 * variable when the next interval is entered. For example, if the rollover
	 * period is one hour, the log file will be renamed to the value of
	 * "scheduledFilename" at the beginning of the next hour.
	 * 
	 * The precise time when a rollover occurs depends on logging activity.
	 */
	// private String scheduledFilename;

	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long nextCheck = System.currentTimeMillis() - 1;

	Date now = new Date();

	SimpleDateFormat sdf;

	private String scheduledFilename;

	RollingCalendar rc = new RollingCalendar();

	// The gmtTimeZone is used only in computeCheckPeriod() method.
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

	/**
	 * The default constructor does nothing.
	 */
	public MonitorFileAppender() {
	}

	public void activateOptions() {
		if (fileName != null) {
			this.fname = fileName;
			now.setTime(System.currentTimeMillis());

			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);

			int count = hour;

			scheduledFilename = fileName +Function.getDateString(c.getTime(), "yyyyMMdd")+"_"+ count + suffix;

			try {
				setFile(scheduledFilename, fileAppend, bufferedIO, bufferSize);
			} catch (java.io.IOException e) {
				errorHandler.error("setFile(" + fileName + "," + fileAppend + ") call failed.", e, ErrorCode.FILE_OPEN_FAILURE);
			}

		} else {
			LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
		}
	}

	// This method computes the roll over period by looping over the
	// periods, starting with the shortest, and stopping when the r0 is
	// different from from r1, where r0 is the epoch formatted according
	// the datePattern (supplied by the user) and r1 is the
	// epoch+nextMillis(i) formatted according to datePattern. All date
	// formatting is done in GMT and not local format because the test
	// logic is based on comparisons relative to 1970-01-01 00:00:00
	// GMT (the epoch).

	// int computeCheckPeriod() {
	// RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone,
	// Locale.getDefault());
	// // set sate to 1970-01-01 00:00:00 GMT
	// Date epoch = new Date(0);
	// if (datePattern != null) {
	// for (int i = TOP_OF_MINUTE; i <= TOP_OF_MINUTE; i++) {
	// SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
	// simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
	// // formatting in GMT
	// String r0 = simpleDateFormat.format(epoch);
	// rollingCalendar.setType(i);
	// Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
	// String r1 = simpleDateFormat.format(next);
	// // System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
	// if (r0 != null && r1 != null && !r0.equals(r1)) {
	// return i;
	// }
	// }
	// }
	// return TOP_OF_TROUBLE; // Deliberately head for trouble...
	// }

	/**
	 * Rollover the current file to a new file.
	 */
	void rollOver() throws IOException {

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		int hour = c.get(Calendar.HOUR_OF_DAY);

		int count = hour;

		String datedFilename = fname + Function.getDateString(c.getTime(), "yyyyMMdd")+"_"+count + suffix;
		// It is too early to roll over because we are still within the
		// bounds of the current interval. Rollover will occur once the
		// next interval is reached.
		if (scheduledFilename.equals(datedFilename)) {
			return;
		}

		// close current file, and rename it to datedFilename
		this.closeFile();

		try {
			// This will also close the file. This is OK since multiple
			// close operations are safe.
			this.setFile(datedFilename, true, this.bufferedIO, this.bufferSize);
		} catch (IOException e) {
			errorHandler.error("setFile(" + fileName + ", true) call failed.");
		}
		scheduledFilename = datedFilename;
	}

	/**
	 * This method differentiates DailyRollingFileAppender from its super class.
	 * 
	 * <p>
	 * Before actually logging, this method will check whether it is time to do
	 * a rollover. If it is, it will schedule the next rollover time and then
	 * rollover.
	 * */
	protected void subAppend(LoggingEvent event) {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);
			try {
				rollOver();
			} catch (IOException ioe) {
				if (ioe instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
				LogLog.error("rollOver() failed.", ioe);
			}
		}
		super.subAppend(event);
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}

/**
 * RollingCalendar is a helper class to DailyRollingFileAppender. Given a
 * periodicity type and the current time, it computes the start of the next
 * interval.
 * */
class RollingCalendar extends GregorianCalendar {
	private static final long serialVersionUID = -3560331770601814177L;

	RollingCalendar() {
		super();
	}

	RollingCalendar(TimeZone tz, Locale locale) {
		super(tz, locale);
	}

	public long getNextCheckMillis(Date now) {
		return getNextCheckDate(now).getTime();
	}

	public Date getNextCheckDate(Date now) {
		this.setTime(now);

		this.set(Calendar.SECOND, 0);
		this.set(Calendar.MILLISECOND, 0);
		this.set(Calendar.MINUTE, 0);
		this.add(Calendar.HOUR_OF_DAY, 1);

		return getTime();
	}
}
