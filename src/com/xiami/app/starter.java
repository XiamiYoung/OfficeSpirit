package com.xiami.app;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.frame.MainFrame;
import com.xiami.logic.FileLoader;
import com.xiami.logic.NoticeLoader;
import com.xiami.logic.TextFileLoader;
import com.xiami.ui.CommonUIManager;

public class starter {

	private static MainFrame frame;

	public static void main(String args[]) {
		start();
	}

	public static void start(){
		
		Logger.getLogger().info("Initializing System ");

		Logger.getLogger().info("Configuring SystemResource");

		initConfiguration();

		Logger.getLogger().info("Configuration done.");

		Logger.getLogger().info("Initializing MainFrame");

		frame = new MainFrame();
		
		CommonUIManager ui = new CommonUIManager(frame);

		CommonUIManager.setUI();

		ui.startUp();

		Logger.getLogger().info("MainFrame show up successfully");
	}
	
	public static void initConfiguration() {
		try{
			
			SystemConfig.configSystemResource();
	
			Logger.configLogResource();
	
			SystemConfig.configLanguage();
			
			NoticeLoader.configuration();
			
			TextFileLoader.configuration();
		}catch(Exception e){
			JOptionPane.showMessageDialog(frame,
			"Error found when configuring the resource,system will try to fix it and reset it to default settings,system will shutdown now,please start again.");
			try {
				FileLoader.restoreSystemResource();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(frame,
				"Failed to restore system configuration,if could not start again,please re-install the software.");
			}
			System.exit(1);
		}
	}

}
