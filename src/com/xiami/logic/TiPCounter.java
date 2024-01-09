package com.xiami.logic;

import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.frame.SysIcon;
import com.xiami.frame.TipWindow;
import com.xiami.frame.WordPanel;
import com.xiami.ui.CommonUIManager;

public class TiPCounter implements Runnable {

	private TipWindow tipWindow;
	
	private static TiPCounter tipCounter;

	private CommonUIManager ui;
	
	public static boolean restartCounter = false;

	public static int TIP_TIME = Integer.parseInt(SystemConfig.getSystemProp("tip_time"));
	
	public TiPCounter() {
		tipWindow = TipWindow.getInstance();
	}

	public static TiPCounter getInstance() {
		if (tipCounter == null) {
			synchronized (TiPCounter.class) {
				if (tipCounter == null) {
					tipCounter = new TiPCounter();
				}
			}
		}
		return tipCounter;
	}
	
	public void run() {
		while (true) {
			try {				
				synchronized (tipCounter) {
					Thread.sleep(10);
					if (SysIcon.getIsTipAllowed() == 0) {						
						tipCounter.wait(TIP_TIME * 1000 * 60);
						if (restartCounter) {
							restartCounter = false;
							continue;
						}
						if ((null != WordPanel.getContents() && WordPanel
										.getContents().size() != 0)
								&& (!tipWindow.isVisible())) {
							if (WordPanel.getContents().get(0)
									.getWordBeanList().size() != 0) {
								if (TipWindow.isInitialized == false) {
									ui = new CommonUIManager(tipWindow);
									CommonUIManager.setUI();
									ui.startUp();
									Logger.getLogger().info("tip window show up.");
								} else {
									tipWindow.showUp();
								}
							}

						}

					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
