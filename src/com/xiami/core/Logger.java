package com.xiami.core;

import org.apache.log4j.xml.DOMConfigurator;

public class Logger {
	
	private static LogResource logResource = new LogResource();
	
	private static final String LOG_CLASS = "log";
	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LOG_CLASS);

	public static void configLogResource() {
		DOMConfigurator.configure(logResource.getLogResource());
	}
		
	public static org.apache.log4j.Logger getLogger(){
		return log;
	}
}
