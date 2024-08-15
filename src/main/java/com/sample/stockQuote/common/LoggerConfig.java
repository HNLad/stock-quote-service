package com.sample.stockQuote.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LoggerConfig {

	protected static final String PATTERN = "%d{dd MMM yyyy HH:mm:ss,SSS} |%t | %5p | %F:%L:%M | %m%n";

	public static Logger setLogger(String userId,Logger logger) {
		logger.removeAllAppenders();
		
		DateFormat df = new SimpleDateFormat("dd-MMM-yy");
		Date dateobj = new Date();
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern(PATTERN);
		DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
		rollingAppender.setName(userId);
		rollingAppender.setFile("e:/logs/stock/" + df.format(dateobj) + "/" + userId + ".log");
		rollingAppender.setDatePattern("'.'yyyy-MM-dd");
		rollingAppender.setLayout(layout);
		rollingAppender.activateOptions();
		logger.setLevel(Level.INFO);
		logger.addAppender(rollingAppender);
		return logger;

	}
}
