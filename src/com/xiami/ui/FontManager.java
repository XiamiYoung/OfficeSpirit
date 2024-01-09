package com.xiami.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;

public class FontManager {

	private static GraphicsEnvironment environment;
	private static String[] systemFontList; 
	private static int default_cn_jp = -1;
	private static String font_cn_name = null;
	private static String font_en_name = null;
	private static String font_jp_name = null;
	
	static{
		environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		systemFontList = environment.getAvailableFontFamilyNames();
		detectSystemFont();
	}
	
	public static void detectSystemFont(){
		String font_cn = SystemConfig.getSystemProp(Keys.FONT_CN);
		String font_en = SystemConfig.getSystemProp(Keys.FONT_EN);
		String font_jp = SystemConfig.getSystemProp(Keys.FONT_JP);
		for(int i=0;i<systemFontList.length;i++){
			if(systemFontList[i].equals(font_cn)){
				font_cn_name = systemFontList[i];
			}
			if(systemFontList[i].equals(font_en)){
				font_en_name = systemFontList[i];
			}
			if(systemFontList[i].equals(font_jp)){
				font_jp_name = systemFontList[i];
			}
			if(systemFontList[i].contains("楷体")){
				default_cn_jp = i;
			}
		}
		if(default_cn_jp != -1){
			if(font_cn_name==null){
				font_cn_name = systemFontList[default_cn_jp];
			}
//			if(font_jp_name==null){
//				font_jp_name = systemFontList[default_cn_jp];
//			}
		}
		if(font_en_name==null){
			font_en_name = "Arial Unicode MS";
		}
		
//		System.out.println(font_cn_name);
//		System.out.println(font_en_name);
//		System.out.println(font_jp_name);
		
		if(font_cn_name==null){
			font_cn_name = SystemConfig.getSystemProp(Keys.FONT_CN);
		}
		if(font_jp_name==null){
			font_jp_name = SystemConfig.getSystemProp(Keys.FONT_JP);
		}
		if(font_en_name==null){
			font_en_name = SystemConfig.getSystemProp(Keys.FONT_EN);
		}
		
		Logger.getLogger().info("cn font :"+font_cn_name);
		
		Logger.getLogger().info("en font :"+font_en_name);
		
		Logger.getLogger().info("jp font :"+font_jp_name);
		
	}
	
	public static Font getFont() {
		Font font_cn = null;
		Font font_en = null;
		Font font_jp = null;
		if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_CN)) {
			if (null == font_cn) {
				font_cn = new Font(font_cn_name, Font.PLAIN, 18);
			}
			return font_cn;
		} else if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_EN)) {
			if (null == font_en) {
				font_en = new Font(font_en_name, Font.PLAIN, 16);
			}
			return font_en;
		} else if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_JP)) {
			if (null == font_jp) {
				font_jp = new Font(font_jp_name, Font.PLAIN, 17);
			}
			return font_jp;
		}else{
			return new Font("Arial Unicode MS", Font.PLAIN, 14);
		}		
	}
	
	public static Font getFont(int size,boolean isBold) {
		Font font_cn_bold = null;
		Font font_en_bold = null;
		Font font_jp_bold = null;
		if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_CN)) {
			if (null == font_cn_bold) {
				font_cn_bold = new Font(font_cn_name, Font.BOLD, size);
			}
			return font_cn_bold;
		} else if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_EN)) {
			if (null == font_en_bold) {
				font_en_bold = new Font(font_en_name, Font.BOLD, size);
			}
			return font_en_bold;
		} else if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_JP)) {
			if (null == font_jp_bold) {
				font_jp_bold = new Font(font_jp_name, Font.BOLD, size+2);
			}
			return font_jp_bold;
		}else{
			return new Font("Arial Unicode MS", Font.BOLD, 14);
		}		
	}
	public static Font getFont(int size) {
		Font font_cn_size = null;
		Font font_en_size = null;
		Font font_jp_size = null;
		if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_CN)) {
			if (null == font_cn_size) {
				font_cn_size = new Font(font_cn_name, Font.PLAIN, size);
			}
			return font_cn_size;
		} else if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_EN)) {
			if (null == font_en_size) {
				font_en_size = new Font(font_en_name, Font.PLAIN, size);
			}
			return font_en_size;
		} else if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_JP)) {
			if (null == font_jp_size) {
				font_jp_size = new Font(font_jp_name, Font.PLAIN, size);
			}
			return font_jp_size;
		}else{
			return new Font("Arial Unicode MS", Font.PLAIN, size);
		}		
	}
}
