package com.xiami.ui;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import com.pagosoft.plaf.PlafOptions;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;


public class CommonUIManager {
	
	private Object Ins = new Object();
				
	private static String theme_current = SystemConfig.getSystemProp(Keys.THEME_DEFAULT);
	
	private static Color bgColor;
	private static Color frColor;
	
	public CommonUIManager(Object ins){
		this.Ins = ins;
	}
	
	//NimbusLookAndFeel
	//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	
	@SuppressWarnings("static-access")
	public static void setUI(){
		 try {		 	 			 			 
			 if(theme_current.equals("Nimrod")){
				 NimRODTheme nt = new NimRODTheme();
				 nt.setFrameOpacity(10);
				 nt.setOpacity(5);
				 NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
				 NimRODLF.setCurrentTheme(nt);
				 UIManager.setLookAndFeel( NimRODLF);
			    }else if(theme_current.equals("Liquid")){
					 UIManager.setLookAndFeel( "com.birosoft.liquid.LiquidLookAndFeel");
			    }else if(theme_current.equals("Office")){
					 UIManager.setLookAndFeel("org.fife.plaf.Office2003.Office2003LookAndFeel"); 
			    }else if(theme_current.equals("Nimrod_Silver")){
			    	 frColor = new Color(249,209,0);
					 bgColor = new Color(234,239,240);
					 NimRODTheme nt = new NimRODTheme();
					 nt.setFrameOpacity(10);
					 nt.setOpacity(5);
					 nt.setPrimary(frColor);
					 nt.setPrimary1(frColor);
					 nt.setPrimary2(frColor);
					 nt.setPrimary3(frColor);
					 nt.setSecondary(bgColor);
					 nt.setSecondary1(bgColor);
					 nt.setSecondary2(bgColor);
					 nt.setSecondary3(bgColor);
					 NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
					 NimRODLF.setCurrentTheme(nt);
					 UIManager.setLookAndFeel( NimRODLF);
			    }else if(theme_current.equals("PGS")){
//			    	UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
//			    	PlafOptions.setCurrentTheme(ThemeFactory.getTheme("vista"));
//			    	PlafOptions.setVistaStyle(true);
//			    	PlafOptions.enableJideFastGradient();
			    	PlafOptions.setAsLookAndFeel();
			    	PlafOptions.setStyle(PlafOptions.MENUBAR, PlafOptions.GRADIENT_STYLE);
			    	PlafOptions.setStyle(PlafOptions.MENUBARMENU, PlafOptions.GRADIENT_STYLE);
			    	PlafOptions.setStyle(PlafOptions.MENU_ITEM, PlafOptions.GRADIENT_STYLE);
			    	PlafOptions.useBoldFonts(false);
			    }
		 		} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();  
			    } catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
	}
	
	public void startUp(){
		SwingUtilities.invokeLater((Runnable)Ins);
	}
}
