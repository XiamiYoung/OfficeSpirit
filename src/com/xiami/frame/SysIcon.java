package com.xiami.frame;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import snoozesoft.systray4j.CheckableMenuItem;
import snoozesoft.systray4j.SubMenu;
import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuEvent;
import snoozesoft.systray4j.SysTrayMenuIcon;
import snoozesoft.systray4j.SysTrayMenuItem;
import snoozesoft.systray4j.SysTrayMenuListener;

import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.TiPCounter;
import com.xiami.logic.TimeCounter;
import com.xiami.ui.CommonUIManager;


public class SysIcon implements Runnable,SysTrayMenuListener {

	private static WordPanel wordPanel;

	private static MainFrame mainFrame;

	private static SubMenu tipMenu;

	private static CheckableMenuItem tipItem_on;

	private static CheckableMenuItem tipItem_off;

	private static CheckableMenuItem tipItem_random;

	private static CheckableMenuItem tipItem_order;

	private static CheckableMenuItem tipItem_1;

	private static CheckableMenuItem tipItem_5;
	
	private static CheckableMenuItem tipItem_10;

	private static CheckableMenuItem tipItem_15;

	private static CheckableMenuItem tipItem_30;

	private static CheckableMenuItem tipItem_60;

	private static CheckableMenuItem tipItem_120;

	private static CheckableMenuItem tipItem_customize;

	private static CheckableMenuItem tipItem_current;
	
	private static SysTrayMenuItem menuItem_link_search;
	
	private static SysTrayMenuItem menuItem_link1;
	
	private static SysTrayMenuItem menuItem_link2;
	
	private static SysTrayMenuItem menuItem_link3;
	
	private static SubMenu menu_link;

	private static SysTrayMenuItem itemAbout;

	private static SysTrayMenuItem itemExit;

	private static SysTrayMenuItem searchItem;
	
	private static SysTrayMenuItem optionItem;
	
	private static SysTrayMenuItem mainFrameItem;

	//	private static SysIcon sysicon = null;

	private static int source = 0;

	private static int isTipAllowed = 0;

	private static SysIcon sysicon = null;

	private SearchFrame searchFrame;
	
	private JFrame messageFrame = new JFrame();

	private CommonUIManager ui;

	public static SysIcon getInstance() {
		if (sysicon == null) {
			synchronized (SysIcon.class) {
				if (sysicon == null) {
					sysicon = new SysIcon();
				}
			}
		}
		return sysicon;
	}

	// create icons
	static final SysTrayMenuIcon[] icons = {
			// the extension can be omitted
			new SysTrayMenuIcon("icons/on"),
			new SysTrayMenuIcon("icons/off") };

	static SysTrayMenu menu;
	static int currentIndexIcon;
	static int currentIndexTooltip;

	public SysIcon() {

		icons[0].addSysTrayMenuListener(this);
		icons[1].addSysTrayMenuListener(this);

		createMenu();

	}

	public static void changeIcon() {
		if (currentIndexIcon == 0){
			if(!mainFrame.isVisible()){
				currentIndexIcon = 1;
			}
		}
		else
			currentIndexIcon = 0;

		menu.setIcon(icons[currentIndexIcon]);
	}
	
	private void createMenu() {
		
		messageFrame.toFront();
		messageFrame.requestFocus();

		tipItem_off = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.off"));
		tipItem_off.addSysTrayMenuListener(this);

		tipItem_on = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.on"));
		tipItem_on.addSysTrayMenuListener(this);

		String is_Tip_Allowed = SystemConfig.getSystemProp(Keys.TIP_SWITCH);
		if (is_Tip_Allowed.equals("0")) {
			tipItem_on.setState(true);
		} else if (is_Tip_Allowed.equals("1")) {
			SysIcon.setIsTipAllowed(1);
			tipItem_off.setState(true);
		}
		
		tipItem_random = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.random"));
		tipItem_random.addSysTrayMenuListener(this);

		tipItem_order = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.order"));
		tipItem_order.addSysTrayMenuListener(this);

		String tip = SystemConfig.getSystemProp(Keys.TIP_TYPE);
		if (tip.equals("0")) {
			tipItem_random.setState(true);
		} else if (tip.equals("1")) {
			tipItem_order.setState(true);
		}

		tipItem_1 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.1min"));
		tipItem_1.addSysTrayMenuListener(this);

		tipItem_5 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.5min"));
		tipItem_5.addSysTrayMenuListener(this);
		
		tipItem_10 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.10min"));
		tipItem_10.addSysTrayMenuListener(this);

		tipItem_15 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.15min"));
		tipItem_15.addSysTrayMenuListener(this);

		tipItem_30 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.30min"));
		tipItem_30.addSysTrayMenuListener(this);

		tipItem_60 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.60min"));
		tipItem_60.addSysTrayMenuListener(this);

		tipItem_120 = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.120min"));
		tipItem_120.addSysTrayMenuListener(this);

		tipItem_customize = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.customize"));
		tipItem_customize.addSysTrayMenuListener(this);

		tipItem_current = new CheckableMenuItem(SystemConfig.getLanguageProp("SysIcon.current")+"(" + TiPCounter.TIP_TIME + SystemConfig.getLanguageProp("SysIcon.min")+")");
		tipItem_current.addSysTrayMenuListener(this);
		tipItem_current.setEnabled(false);
		
		mainFrameItem = new SysTrayMenuItem(SystemConfig.getLanguageProp("SysIcon.mainpage"));
		mainFrameItem.addSysTrayMenuListener(this);
		mainFrameItem.setEnabled(false);
		
		optionItem = new SysTrayMenuItem(SystemConfig.getLanguageProp("SysIcon.options"));
		optionItem.addSysTrayMenuListener(this);
		optionItem.setEnabled(true);
		
		
		menu_link = new SubMenu(SystemConfig.getLanguageProp("Link.menu"));
		menu_link.addSysTrayMenuListener(this);
		menuItem_link_search = new SysTrayMenuItem(SystemConfig.getLanguageProp("Link.search"));
		menuItem_link_search.addSysTrayMenuListener(this);
		menuItem_link1 = new SysTrayMenuItem(SystemConfig.getSystemProp("link_name1"));
		menuItem_link1.addSysTrayMenuListener(this);
		menuItem_link2 = new SysTrayMenuItem(SystemConfig.getSystemProp("link_name2"));
		menuItem_link2.addSysTrayMenuListener(this);
		menuItem_link3 = new SysTrayMenuItem(SystemConfig.getSystemProp("link_name3"));
		menuItem_link3.addSysTrayMenuListener(this);
		
		
		tipMenu = new SubMenu(SystemConfig.getLanguageProp("SysIcon.tipsetting"));
		tipMenu.addItem(tipItem_current);
		tipMenu.addSeparator();
		tipMenu.addItem(tipItem_customize);
		tipMenu.addItem(tipItem_120);
		tipMenu.addItem(tipItem_60);
		tipMenu.addItem(tipItem_30);
		tipMenu.addItem(tipItem_15);
		tipMenu.addItem(tipItem_10);
		tipMenu.addItem(tipItem_5);
		tipMenu.addItem(tipItem_1);
		tipMenu.addSeparator();
		tipMenu.addItem(tipItem_order);
		tipMenu.addItem(tipItem_random);	
		tipMenu.addSeparator();
		tipMenu.addItem(tipItem_off);
		tipMenu.addItem(tipItem_on);

		menu_link.addItem(menuItem_link3);
		menu_link.addItem(menuItem_link2);
		menu_link.addItem(menuItem_link1);
		menu_link.addSeparator();
		menu_link.addItem(menuItem_link_search);
	
		// create another submenu and insert the items through addItem()
		searchItem = new SysTrayMenuItem(SystemConfig.getLanguageProp("SysIcon.search"));
		searchItem.addSysTrayMenuListener(this);
		searchItem.setEnabled(true);

		// create an exit item
		itemExit = new SysTrayMenuItem(SystemConfig.getLanguageProp("SysIcon.exit"));
		itemExit.addSysTrayMenuListener(this);

		// create an about item
		itemAbout = new SysTrayMenuItem(SystemConfig.getLanguageProp("SysIcon.about"));
		itemAbout.addSysTrayMenuListener(this);

		// create the main menu
		menu = new SysTrayMenu(icons[0]);

		// insert items
		menu.addItem(itemExit);
		menu.addSeparator();
		menu.addItem(itemAbout);
		menu.addSeparator();
		menu.addItem(optionItem);
		menu.addSeparator();
		menu.addItem(searchItem);
		menu.addSeparator();
		menu.addItem(menu_link);		
		menu.addSeparator();
		menu.addItem(tipMenu);
		menu.addSeparator();
		menu.addItem(mainFrameItem);
		
		setTimeDefault();
		
	}	

	public static void settings(){
		tipItem_off.setLabel(SystemConfig.getLanguageProp("SysIcon.off"));
		tipItem_on.setLabel(SystemConfig.getLanguageProp("SysIcon.on"));
		tipItem_random.setLabel(SystemConfig.getLanguageProp("SysIcon.random"));
		tipItem_order.setLabel(SystemConfig.getLanguageProp("SysIcon.order"));
		tipItem_1.setLabel(SystemConfig.getLanguageProp("SysIcon.1min"));		
		tipItem_5.setLabel(SystemConfig.getLanguageProp("SysIcon.5min"));
		tipItem_10.setLabel(SystemConfig.getLanguageProp("SysIcon.10min"));
		tipItem_15.setLabel(SystemConfig.getLanguageProp("SysIcon.15min"));
		tipItem_30.setLabel(SystemConfig.getLanguageProp("SysIcon.30min"));
		tipItem_60.setLabel(SystemConfig.getLanguageProp("SysIcon.60min"));
		tipItem_120.setLabel(SystemConfig.getLanguageProp("SysIcon.120min"));
		tipItem_customize.setLabel(SystemConfig.getLanguageProp("SysIcon.customize"));
		menuItem_link_search.setLabel(SystemConfig.getLanguageProp("Link.search"));
		menuItem_link1.setLabel(SystemConfig.getSystemProp("link_name1"));
		menuItem_link2.setLabel(SystemConfig.getSystemProp("link_name2"));
		menuItem_link3.setLabel(SystemConfig.getSystemProp("link_name3"));
		mainFrameItem.setLabel(SystemConfig.getLanguageProp("SysIcon.mainpage"));
		optionItem.setLabel(SystemConfig.getLanguageProp("SysIcon.options"));
		tipMenu.setLabel(SystemConfig.getLanguageProp("SysIcon.tipsetting"));
		menu_link.setLabel(SystemConfig.getLanguageProp("Link.menu"));
		searchItem.setLabel(SystemConfig.getLanguageProp("SysIcon.search"));
		itemExit.setLabel(SystemConfig.getLanguageProp("SysIcon.exit"));
		itemAbout.setLabel(SystemConfig.getLanguageProp("SysIcon.about"));
		tipItem_current.setLabel(SystemConfig.getLanguageProp("SysIcon.current")+"(" + TiPCounter.TIP_TIME + SystemConfig.getLanguageProp("SysIcon.min")+")");
	}
	
	public void menuItemSelected(SysTrayMenuEvent e) {
		if (e.getSource() == itemAbout) {
			ui = new CommonUIManager(new AboutDialog());
			CommonUIManager.setUI();
			ui.startUp();
		} else if (e.getSource() == itemExit) {
			distroy();
		} else if (e.getSource() == tipItem_on) {
			if (tipItem_on.getState() == false) {
				tipItem_on.setState(true);
			}
			if (tipItem_off.getState() == true) {
				tipItem_off.setState(false);
			}
			SysIcon.setIsTipAllowed(0);
			SystemConfig.setSystemProp("is_Tip_Allowed", "0");
			if(OptionDialog.isInitialized){
				OptionDialog.getInstance().setDefautTip();
			}
			TiPCounter.restartCounter = true;
			synchronized (TiPCounter.getInstance()) {
				TiPCounter.getInstance().notify();
			}
		} else if (e.getSource() == tipItem_off) {
			if (tipItem_off.getState() == false) {
				tipItem_off.setState(true);
			}
			if (tipItem_on.getState() == true) {
				tipItem_on.setState(false);
			}
			SysIcon.setIsTipAllowed(1);
			SystemConfig.setSystemProp("is_Tip_Allowed", "1");
			if(OptionDialog.isInitialized){
				OptionDialog.getInstance().setDefautTip();
			}
			TiPCounter.restartCounter = true;
			synchronized (TiPCounter.getInstance()) {
				TiPCounter.getInstance().notify();
			}
		} else if (e.getSource() == tipItem_random) {
			SystemConfig.setSystemProp("tip_type", "0");
			if (tipItem_random.getState() == false) {
				tipItem_random.setState(true);
			}
			if (tipItem_order.getState() == true) {
				tipItem_order.setState(false);
			}
			if(OptionDialog.isInitialized){
				OptionDialog.getInstance().setDefautTip();
			}
		} else if (e.getSource() == tipItem_order) {
			SystemConfig.setSystemProp("tip_type", "1");
			if (tipItem_order.getState() == false) {
				tipItem_order.setState(true);
			}
			if (tipItem_random.getState() == true) {
				tipItem_random.setState(false);
			}
			if(OptionDialog.isInitialized){
				OptionDialog.getInstance().setDefautTip();
			}
		} else if (e.getSource() == tipItem_1) {
			if (tipItem_1.getState() == false) {
				tipItem_1.setState(true);
			}
			TiPCounter.TIP_TIME = 1;
			setSelect(tipMenu, tipItem_1);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_5) {
			if (tipItem_5.getState() == false) {
				tipItem_5.setState(true);
			}
			TiPCounter.TIP_TIME = 5;
			setSelect(tipMenu, tipItem_5);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_10) {
			if (tipItem_10.getState() == false) {
				tipItem_10.setState(true);
			}
			TiPCounter.TIP_TIME = 10;
			setSelect(tipMenu, tipItem_10);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_15) {
			if (tipItem_15.getState() == false) {
				tipItem_15.setState(true);
			}
			TiPCounter.TIP_TIME = 15;
			setSelect(tipMenu, tipItem_15);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_30) {
			if (tipItem_30.getState() == false) {
				tipItem_30.setState(true);
			}
			TiPCounter.TIP_TIME = 30;
			setSelect(tipMenu, tipItem_30);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_60) {
			if (tipItem_60.getState() == false) {
				tipItem_60.setState(true);
			}
			TiPCounter.TIP_TIME = 60;
			setSelect(tipMenu, tipItem_60);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_120) {
			if (tipItem_120.getState() == false) {
				tipItem_120.setState(true);
			}
			TiPCounter.TIP_TIME = 120;
			setSelect(tipMenu, tipItem_120);
			if (tipItem_on.getState() == true) {
				TiPCounter.restartCounter = true;
				synchronized (TiPCounter.getInstance()) {
					TiPCounter.getInstance().notify();
				}
			}
		} else if (e.getSource() == tipItem_customize) {
			boolean isSelected = tipItem_customize.getState();
			if (tipItem_customize.getState() == false) {
				tipItem_customize.setState(true);
			}
			String input = JOptionPane
					.showInputDialog("Please Input Time:(Minute)");
			boolean isValid = true;
			if (null == input) {
				isValid = false;
			} else if (null != input && !"".equals(input)) {
				for (int i = 0; i < input.length(); i++) {
					if (input.charAt(i) < 48 || input.charAt(i) > 57) {
						isValid = false;
					}
				}
			}
			if (isValid) {
				TiPCounter.TIP_TIME = Integer.parseInt(input);
				setSelect(tipMenu, tipItem_customize);
				JOptionPane.showMessageDialog(messageFrame, "Tip set to "
						+ TiPCounter.TIP_TIME + "min");
				if (tipItem_on.getState() == true) {
					TiPCounter.restartCounter = true;
					synchronized (TiPCounter.getInstance()) {
						TiPCounter.getInstance().notify();
					}
				}
			} else {
				if (!isSelected) {
					tipItem_customize.setState(true);
				} else {
					tipItem_customize.setState(false);
				}
				JOptionPane.showMessageDialog(messageFrame, "Invalid Time!");
			}
		} else if (e.getSource() == searchItem) {
			if (!WordPanel.isHasContent()) {
				JOptionPane.showMessageDialog(messageFrame,
						SystemConfig.getLanguageProp("Message.OpenFirst"));
			} else {
				if (!SearchFrame.isInitialized()) {
					searchFrame = SearchFrame.getInstance(wordPanel);
					ui = new CommonUIManager(searchFrame);
					CommonUIManager.setUI();
					ui.startUp();
				} else {
					searchFrame = SearchFrame.getInstance(wordPanel);
					searchFrame.setting();
				}
			}
		}
		else if (e.getSource() == mainFrameItem) {
			changeIcon();
			mainFrame.setting();
			switchMainFrameEnabled();
			SysIcon.setSource(0);
		}
	
		else if (e.getSource() == optionItem) {
			OptionDialog optionDialog = null;
			if (!OptionDialog.isInitialized ) {
				Logger.getLogger().info("Initializing OptionDialog");
				optionDialog = OptionDialog.getInstance();
				optionDialog.setMainFrame(mainFrame);
				ui = new CommonUIManager(optionDialog);
				CommonUIManager.setUI();
				ui.startUp();
				Logger.getLogger().info("Initialization done");
				mainFrame.setOptionDialog(optionDialog);
			} else {
				Logger.getLogger().info("OptionDialog show up");
				optionDialog = OptionDialog.getInstance();
				optionDialog.setting();
			}
		}
		if (e.getSource() == menuItem_link1) {
			LinkHandler.toUrl(SystemConfig
					.getSystemProp("link_url1"));
		}
		if (e.getSource() == menuItem_link2) {
			LinkHandler.toUrl(SystemConfig
					.getSystemProp("link_url2"));
		}
		if (e.getSource() == menuItem_link3) {
			LinkHandler.toUrl(SystemConfig
					.getSystemProp("link_url3"));
		}
		if (e.getSource() == menuItem_link_search) {
			Random ran = new Random();
			if(ran.nextInt(100)<=50){
				LinkHandler.toUrl(SystemConfig
						.getSystemProp("link_baidu"));
			}else{
				LinkHandler.toUrl(SystemConfig
						.getSystemProp("link_google"));
			}
		}
	}

	public void iconLeftClicked(SysTrayMenuEvent e) {
	
		if (getSource() == 0) {
			System.out.println(mainFrame.isVisible());
			if (mainFrame.isVisible()) {
				if (mainFrame.getState() == JFrame.ICONIFIED) {
					mainFrame.setState(JFrame.NORMAL);
				} else {
					System.out.println("Hide MainFrame");
					mainFrame.setVisible(false);
					mainFrame.dispose();
					switchMainFrameEnabled();
					changeIcon();
				}
			} else {
				System.out.println("Show MainFrame");
				mainFrame.setAlwaysOnTop(true);
				mainFrame.setVisible(true);
				mainFrame.setState(JFrame.NORMAL);
				mainFrame.setAlwaysOnTop(false);
				switchMainFrameEnabled();
				changeIcon();
			}
			
		}else if (getSource() == 5) {
			MaxTextFrame maxTextFrame = MainFrame.getTextPanel().getMaxTextFrame();
			if (maxTextFrame.isVisible()) {
				if (maxTextFrame.getState() == JFrame.ICONIFIED) {
					maxTextFrame.setState(JFrame.NORMAL);
				}else{
					System.out.println("Hide MaxTextFrame");
					maxTextFrame.setVisible(false);
					changeIcon();
				}
			} else {
				System.out.println("Show MaxTextFrame");
				maxTextFrame.setVisible(true);
				changeIcon();
			}		
		}
	}

	public void iconLeftDoubleClicked(SysTrayMenuEvent e) {
		
	}

	public void distroy() {
		SystemConfig.setSystemProp(Keys.TOTAL_TIME,String.valueOf(TimeCounter.getTotal_time()));
		System.exit(0);
	}

	public WordPanel getWordPanel() {
		return wordPanel;
	}

	public static void setWordPanel(WordPanel ins) {
		SysIcon.wordPanel = ins;
	}

	public void switchIconVisible() {
		if (!menu.isIconVisible()) {
			menu.showIcon();
		} else {
			menu.hideIcon();
		}
	}

	public void setMainFrame(MainFrame obj) {
		mainFrame = obj;
	}

	public static int getSource() {
		return source;
	}

	public static void setSource(int source) {
		SysIcon.source = source;
	}

	public static int getIsTipAllowed() {
		return isTipAllowed;
	}

	public static void setIsTipAllowed(int sisTipAllowedTipAllowed) {
		SysIcon.isTipAllowed = sisTipAllowedTipAllowed;
	}

	public void setSelect(SubMenu menu, CheckableMenuItem item) {
		for (int i = 2; i < menu.getItemCount() - 6; i++) {
			if (!menu.getItemAt(i).getLabel().equals(item.getLabel())) {
				((CheckableMenuItem) menu.getItemAt(i)).setState(false);
			}
			tipItem_current.setLabel(SystemConfig.getLanguageProp("SysIcon.current")+"(" + TiPCounter.TIP_TIME + SystemConfig.getLanguageProp("SysIcon.min")+")");
			SystemConfig.setSystemProp("tip_time", String.valueOf(TiPCounter.TIP_TIME));
		}
	}
	
	public static void switchMainFrameEnabled(){
		if(mainFrameItem.isEnabled()&&mainFrame.isVisible()){
			mainFrameItem.setEnabled(false);
		}else if(!mainFrameItem.isEnabled()&&!mainFrame.isVisible()){
			mainFrameItem.setEnabled(true);
		}
	}
	
	public void setTimeDefault(){
		String time = SystemConfig.getSystemProp("tip_time");
		if(time.equals("1")){
			tipItem_1.setState(true);
		}else if(time.equals("5")){
			tipItem_5.setState(true);
		}else if(time.equals("10")){
			tipItem_10.setState(true);
		}else if(time.equals("15")){
			tipItem_15.setState(true);
		}else if(time.equals("30")){
			tipItem_30.setState(true);
		}else if(time.equals("60")){
			tipItem_60.setState(true);
		}else if(time.equals("120")){
			tipItem_120.setState(true);
		}else{
			tipItem_customize.setState(true);
		}
	}

	public static void setTipType(int i){
		if(i==0){
			if (tipItem_random.getState() == false) {
				tipItem_random.setState(true);
				tipItem_order.setState(false);
			}
		}else{
			if (tipItem_order.getState() == false) {
				tipItem_order.setState(true);
				tipItem_random.setState(false);
			}
		}
	}
	public static void setTipSwitch(int i){
		if(i==0){
			if (tipItem_on.getState() == false) {
				tipItem_on.setState(true);
				tipItem_off.setState(false);
				SysIcon.setIsTipAllowed(0);
				SystemConfig.setSystemProp("is_Tip_Allowed", "0");
			}
		}else{
			if (tipItem_off.getState() == false) {
				tipItem_off.setState(true);
				tipItem_on.setState(false);
				SysIcon.setIsTipAllowed(1);
				SystemConfig.setSystemProp("is_Tip_Allowed", "1");
			}
		}
		TiPCounter.restartCounter = true;
		synchronized (TiPCounter.getInstance()) {
			TiPCounter.getInstance().notify();
		}
	}
	
	public static void updateLink(){
		menuItem_link1.setLabel(SystemConfig
				.getSystemProp("link_name1"));
		menuItem_link2.setLabel(SystemConfig
				.getSystemProp("link_name2"));
		menuItem_link3.setLabel(SystemConfig
				.getSystemProp("link_name3"));
	}
	
	public static void setSearchEnabled(boolean flag){
		if(flag){
			searchItem.setEnabled(true);
		}else{
			searchItem.setEnabled(false);
		}
	}
	
	public void run() {
		
	}
}
