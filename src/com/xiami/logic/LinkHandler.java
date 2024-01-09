package com.xiami.logic;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.xiami.core.Logger;

public class LinkHandler {
	public static void toUrl(String url){
		try {
			Logger.getLogger().info("requesting url:"+url);
			Runtime.getRuntime().exec("explorer.exe   "+url);
		} catch (IOException e1) {
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Wrong url!");
		}
	}
}
