package com.xiami.core;

public class LogResource {
	
	private static final String LOG_PATH = "log_config_path";
	
	public String getLogResource(){
		return SystemConfig.getSystemProp(LOG_PATH);
	}
}
