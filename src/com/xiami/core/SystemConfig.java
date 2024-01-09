package com.xiami.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SystemConfig {

	public static Properties prop_language = new Properties();

	public static Properties prop_system = new Properties();
	
	private static String type;

	private static SystemConfigResource resource = new SystemConfigResource();;

	public static void configSystemResource() {
		prop_system.clear();
		try {
			prop_system.loadFromXML(resource.loadSystemConfigResource());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not read system config resource!");
		}
	}

	public static Properties configLanguage(String type) {
		try {
			prop_language.clear();
			SystemConfig.type = type;
			resource.setLanguageResource(type);
			prop_language.load(resource.loadLanguageResource());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not read language config resource!");
		}
		return prop_language;
	}

	public static Properties configLanguage() {
		try {
			prop_language.clear();
			resource.setLanguageResource(prop_system
					.getProperty("default_language"));
			prop_language.load(resource.loadLanguageResource());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not read language config resource!");
		}
		return prop_language;
	}

	public static String getLanguageProp(String key) {
		return prop_language.getProperty(key);
	}

	public static void setLanguageProp(String key, String value) {
		prop_language.setProperty(key, value);
	}

	public static String getSystemProp(String key) {
		return prop_system.getProperty(key);
	}

	public static synchronized void setSystemProp(String key, String value) {
		prop_system.setProperty(key, value);
		try {
			FileOutputStream fos = resource.getOutPut();
			prop_system.storeToXML(fos, "");
			fos.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"File resources/SystemResources.xml is not found,please check!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),
			"File resources/SystemResources.xml can not be accessed,please check!");
		}
	}

	public static String getLanguageType() {
		return type;
	}

	public static void setLanguageType(String type) {
		configLanguage(type);
	}
}
