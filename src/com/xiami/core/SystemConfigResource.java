package com.xiami.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SystemConfigResource {

	private String path;

	private String type;

	private final static String LANGUAGE_CONFIG_DEFAULT = "default_language";

	private static final String LOCAL_CONFIG_FILE_PATH = "resources/SystemResources.xml";

	public SystemConfigResource() {

	}

	public void setLanguageResource(String type) {
		this.type = type;
	}

	public FileInputStream loadLanguageResource() throws IOException {
		if (null != path && path.length() != 0) {
			return new FileInputStream(path);
		} else {
			Properties prop = new Properties();
			prop.loadFromXML(new FileInputStream(LOCAL_CONFIG_FILE_PATH));
			if (this.type.equals(LANGUAGE_CONFIG_DEFAULT)) {
				return new FileInputStream(prop.getProperty(prop
						.getProperty(this.type)));
			} else {
				new FileInputStream(prop.getProperty(this.type));
				return new FileInputStream(prop.getProperty(this.type));
			}
		}
	}

	public FileInputStream loadSystemConfigResource() throws IOException {
		return new FileInputStream(LOCAL_CONFIG_FILE_PATH);
	}

	public FileOutputStream getOutPut() throws FileNotFoundException {

		File f = new File(LOCAL_CONFIG_FILE_PATH);
		FileOutputStream fos = new FileOutputStream(f);
		return fos;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
