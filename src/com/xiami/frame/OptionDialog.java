package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import com.xiami.Component.ImageButton;
import com.xiami.Component.ImageLabel;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.OptionTabbedPane;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.RegEditTool;
import com.xiami.core.SystemConfig;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class OptionDialog extends JDialog implements ActionListener, Runnable {


	private static final long serialVersionUID = 1L;
	private JLabel label_title;
	private JLabel label_title_icon;
	private JLabel label_language_en;
	private JLabel label_language_cn;
	private JLabel label_language_jp;
	private JLabel label_locale_en;
	private JLabel label_locale_jp;
	private JLabel label_theme_nimrod;
	private JLabel label_theme_liquid;
	private JLabel label_theme_Office;
	private JLabel label_theme_Nimrod_Silver;
	private JLabel label_theme_PGS;
	private JLabel label_tip_random;
	private JLabel label_tip_order;
	private JLabel label_tip_on;
	private JLabel label_tip_off;
	private JLabel label_start_run;
	private JLabel label_is_iconfied;
	private JLabel label_is_load_word;
	private JLabel label_is_window_attached;
	private JLabel label_is_icon_dynamic;
	private JLabel label_only_Baidu;
	private JLabel label_only_Google;
	private JLabel label_search_random;
	private JLabel label_search_engine;
	private JSeparator link_separator;
	private JSeparator tip_separator;
	private JSeparator locale_separator;
	private JCheckBox check_only_Baidu;
	private JCheckBox check_only_Google;
	private JCheckBox check_search_random;
	private JTextField link_name1;
	private JTextField link_name2;
	private JTextField link_name3;
	private JTextField link_url1;
	private JTextField link_url2;
	private JTextField link_url3;
	private ImageButton button_close;
	private OptionTabbedPane pane;
	private ImagePanel panel;
	private ImagePanel panel_center;
	private ImageButton button_apply;
	private JCheckBox check_language_en;
	private JCheckBox check_language_cn;
	private JCheckBox check_language_jp;
	private JCheckBox check_locale_en;
	private JCheckBox check_locale_jp;
	private JCheckBox check_theme_Nimrod;
	private JCheckBox check_theme_Liquid;
	private JCheckBox check_theme_Office;
	private JCheckBox check_theme_Nimrod_Silver;
	private JCheckBox check_theme_PGS;
	private JCheckBox check_tip_random;
	private JCheckBox check_tip_order;
	private JCheckBox check_tip_on;
	private JCheckBox check_tip_off;
	private JCheckBox check_start_run;
	private JCheckBox check_is_iconfied;
	private JCheckBox check_is_load_word;
	private JCheckBox check_is_window_attached;
	private JCheckBox check_is_icon_dynamic;
	private ButtonGroup bg_language = new ButtonGroup();
	private ButtonGroup bg_locale = new ButtonGroup();
	private ButtonGroup bg_theme = new ButtonGroup();
	private ButtonGroup bg_tip = new ButtonGroup();
	private ButtonGroup bg_link = new ButtonGroup();
	private ButtonGroup bg_tip_switch = new ButtonGroup();
	private TitledBorder titleBorder;
	private MainFrame mainFrame;
	private static OptionDialog optionDialog;

	
	private int default_theme_index;
	private int default_language_index;
	private int default_locale_index;
	private int default_tip_index;
	private int default_tip_switch_index;
	private int default_link_index;
	private boolean isStart;
	private boolean isIconfied;
	private boolean isLoad;
	private boolean isAttached;
	private boolean isDynamic;
	private boolean isPageGeneral;
	private boolean isPageLanguage;
	private boolean isPageTheme;
	private boolean isPageTip;
	private boolean isPageLink;

	
	public static boolean isInitialized = false;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static OptionDialog getInstance() {
		if (optionDialog == null) {
			synchronized (OptionDialog.class) {
				if (optionDialog == null) {
					optionDialog = new OptionDialog();
				}
			}
		}
		return optionDialog;
	}
	
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}


	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public void createComponents() {

		this.setUndecorated(true);

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_OPTION, Keys.INITIAL_HEIGHT_OPTION - 5);
		panel_center = new ImagePanel("", 15, 90, Keys.INITIAL_WIDTH_OPTION-30, Keys.INITIAL_HEIGHT_OPTION-170);
		button_apply = new ImageButton("", 312, 388, 60, 30, getIns());
		pane = new OptionTabbedPane(getIns(), 5, 25, Keys.INITIAL_WIDTH_OPTION-10, 65);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_OPTION - 30, 3, 20, 20,
				getIns());
		label_title_icon = new ImageLabel("icons/title.gif", 5, 3, 20, 20);
		label_title = new JLabel();

		check_language_en = new JCheckBox();
		check_language_cn = new JCheckBox();
		check_language_jp = new JCheckBox();
		label_language_en = new JLabel();
		label_language_cn = new JLabel();
		label_language_jp = new JLabel();

		check_locale_en = new JCheckBox();
		check_locale_jp = new JCheckBox();
		label_locale_en = new JLabel();
		label_locale_jp = new JLabel();
		
		
		check_theme_Nimrod = new JCheckBox();
		check_theme_Liquid = new JCheckBox();
		check_theme_Nimrod_Silver = new JCheckBox();
		check_theme_Office = new JCheckBox();
		check_theme_PGS = new JCheckBox();
		label_theme_nimrod = new JLabel();
		label_theme_liquid = new JLabel();
		label_theme_Office = new JLabel();
		label_theme_Nimrod_Silver = new JLabel();
		label_theme_PGS = new JLabel();
		
		label_search_engine = new JLabel();
		
		check_tip_random = new JCheckBox();
		check_tip_order = new JCheckBox();
		check_tip_on = new JCheckBox();
		check_tip_off = new JCheckBox();
		label_tip_order = new JLabel();
		label_tip_random = new JLabel();
		label_tip_on = new JLabel();
		label_tip_off = new JLabel();

		check_start_run = new JCheckBox();
		label_start_run = new JLabel();
		check_is_iconfied = new JCheckBox();
		label_is_iconfied = new JLabel();
		check_is_load_word = new JCheckBox();
		label_is_load_word = new JLabel();
		check_is_window_attached = new JCheckBox();
		label_is_window_attached = new JLabel();
		label_is_icon_dynamic = new JLabel();
		check_is_icon_dynamic = new JCheckBox();
		
		link_name1 = new JTextField();
		link_name2 = new JTextField();
		link_name3 = new JTextField();
		link_url1 = new JTextField();
		link_url2 = new JTextField();
		link_url3 = new JTextField();
		
		check_only_Baidu = new JCheckBox();
		check_only_Google = new JCheckBox();
		check_search_random = new JCheckBox();
		label_only_Baidu = new JLabel();
		label_only_Google = new JLabel();
		label_search_random = new JLabel();
		link_separator = new JSeparator();
		tip_separator = new JSeparator();
		locale_separator = new JSeparator();
		
		this.getContentPane().add(panel);
		//this.getContentPane().add(panel_center);
		
		titleBorder = new TitledBorder(SystemConfig.getLanguageProp("WordPanel.sheet"));
		titleBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		titleBorder.setBorder(BorderFactory.createEtchedBorder());
		
		new UndoHandler(link_name1);
		new UndoHandler(link_name2);
		new UndoHandler(link_name3);
		new UndoHandler(link_url1);
		new UndoHandler(link_url2);
		new UndoHandler(link_url3);
		
		this.pack();
		new DragListener(this);
		this.setModal(true);
		isInitialized = true;
	}

	public void setting() {
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_OPTION / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_OPTION / 2);
		this.setSize(Keys.INITIAL_WIDTH_OPTION, Keys.INITIAL_HEIGHT_OPTION);
		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));
		panel.setLayout(null);
		panel_center.setLayout(null);
		panel_center.setBorder(titleBorder);
		
		button_apply.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), 
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		button_apply.setToolTipText("Apply all changes.");
		
		setDefautGeneral();
		setDefautLanguage();
		setDefautTip();
		setDefautTheme();
		setDefautLink();
		setDefaultLocale();
			
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	public void setLanguage(){
		label_title.setText(SystemConfig
				.getLanguageProp("option.title"));
		label_language_en.setText(SystemConfig
				.getLanguageProp("option.language.english"));
		label_language_cn.setText(SystemConfig
				.getLanguageProp("option.language.chinese"));
		label_language_jp.setText(SystemConfig
				.getLanguageProp("option.language.japanese"));
		label_locale_en.setText(SystemConfig
				.getLanguageProp("option.locale.en"));
		label_locale_jp.setText(SystemConfig
				.getLanguageProp("option.locale.jp"));
		label_tip_random.setText(SystemConfig
				.getLanguageProp("option.tip.random"));
		label_tip_order.setText(SystemConfig
				.getLanguageProp("option.tip.order"));
		label_tip_on.setText(SystemConfig
				.getLanguageProp("SysIcon.on"));
		label_tip_off.setText(SystemConfig
				.getLanguageProp("SysIcon.off"));
		label_theme_nimrod.setText(SystemConfig
				.getLanguageProp("option.theme.Nimrod"));
		label_theme_Office.setText(SystemConfig.getLanguageProp("option.theme.Office"));
		label_theme_liquid.setText(SystemConfig
				.getLanguageProp("option.theme.Liquid"));
		label_theme_Nimrod_Silver.setText(SystemConfig
				.getLanguageProp("option.theme.Nimrod_Silver"));
		label_theme_PGS.setText(SystemConfig
				.getLanguageProp("option.theme.PGS"));
		label_start_run.setText(SystemConfig
				.getLanguageProp("option.general.start_run"));
		label_is_iconfied.setText(SystemConfig
				.getLanguageProp("option.general.is_iconfied"));
		label_is_load_word.setText(SystemConfig
				.getLanguageProp("option.general.is_load_word"));
		label_is_window_attached.setText(SystemConfig
				.getLanguageProp("option.general.is_window_attached"));
		label_is_icon_dynamic.setText(SystemConfig
				.getLanguageProp("option.general.is_icon_dynamic"));
		label_only_Baidu.setText(SystemConfig
				.getLanguageProp("option.link.baidu"));
		label_only_Google.setText(SystemConfig
				.getLanguageProp("option.link.google"));
		label_search_random.setText(SystemConfig
				.getLanguageProp("option.link.random"));
		label_search_engine.setText(SystemConfig
				.getLanguageProp("Link.search"));
		
		button_apply.setText(SystemConfig.getLanguageProp("option.confirm"));
		
		titleBorder.setTitleFont(FontManager.getFont(18,true));
		label_title.setFont(FontManager.getFont(18));
				
		if(pane.getSelectedIndex()==1){
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.language"));
		}else if(pane.getSelectedIndex()==2){
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.tips"));
		}else if(pane.getSelectedIndex()==3){
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.theme"));
		}else if(pane.getSelectedIndex()==4){
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.general"));
		}
	}
	
	public void addComponents() {

		label_title.setBounds(28, 3, 150, 20);

		button_apply.setVisible(true);
		
		check_language_en.setBounds(50, 50, 25, 25);
		check_language_en.addActionListener(this);
		label_language_en.setBounds(150, 50, 100, 25);
		
		check_language_cn.setBounds(50, 95, 25, 25);
		check_language_cn.addActionListener(this);
		label_language_cn.setBounds(150, 95, 100, 25);
		
		check_language_jp.setBounds(50, 140, 25, 25);
		label_language_jp.setBounds(150, 140, 100, 25);	
		check_language_jp.addActionListener(this);

		check_locale_en.setBounds(50, 200, 25, 25);
		check_locale_en.addActionListener(this);
		label_locale_en.setBounds(150, 200, 150, 25);
		
		check_locale_jp.setBounds(50, 245, 25, 25);
		check_locale_jp.addActionListener(this);
		label_locale_jp.setBounds(150, 245, 150, 25);
		
		label_tip_on.setBounds(170, 50, 100, 25);		
		label_tip_off.setBounds(170, 105, 100, 25);		
		check_tip_on.setBounds(70, 50, 25, 25);
		check_tip_off.setBounds(70, 105, 25, 25);
		tip_separator.setBounds(10, 140, Keys.INITIAL_WIDTH_OPTION-50, 2);
		locale_separator.setBounds(10, 180, Keys.INITIAL_WIDTH_OPTION-50, 2);
		label_tip_random.setBounds(170, 165, 100, 25);		
		label_tip_order.setBounds(170, 220, 100, 25);		
		check_tip_random.setBounds(70, 165, 25, 25);
		check_tip_order.setBounds(70, 220, 25, 25);
		
		check_start_run.setBounds(30, 30, 25, 25);	
		label_start_run.setBounds(70, 30, 300, 25);	
		check_is_iconfied.setBounds(30, 80, 25, 25);
		label_is_iconfied.setBounds(70, 80, 300, 25);	
		label_is_load_word.setBounds(70, 130, 300, 25);	
		check_is_load_word.setBounds(30, 130, 25, 25);
		label_is_window_attached.setBounds(70, 180, 300, 25);	
		check_is_window_attached.setBounds(30, 180, 25, 25);
		label_is_icon_dynamic.setBounds(70, 230, 300, 25);
		check_is_icon_dynamic.setBounds(30, 230, 25, 25);
		
		panel.add(label_title);
		panel.add(label_title_icon);
		panel.add(button_close);
		panel.add(pane);
		panel.add(panel_center);
//		panel.add(panel_buttom);
		bg_language.add(check_language_en);
		bg_language.add(check_language_cn);
		bg_language.add(check_language_jp);

		bg_locale.add(check_locale_en);
		bg_locale.add(check_locale_jp);
		
		bg_theme.add(check_theme_Nimrod);
		bg_theme.add(check_theme_Liquid);
		bg_theme.add(check_theme_Office);
		bg_theme.add(check_theme_Nimrod_Silver);
		bg_theme.add(check_theme_PGS);
		
		bg_tip.add(check_tip_random);
		bg_tip.add(check_tip_order);
		bg_tip_switch.add(check_tip_on);
		bg_tip_switch.add(check_tip_off);
		
		bg_link.add(check_only_Baidu);
		bg_link.add(check_only_Google);
		bg_link.add(check_search_random);
		
		check_theme_Nimrod.setBounds(50, 40, 25, 25);
		check_theme_Nimrod.addActionListener(this);
		check_theme_Nimrod_Silver.setBounds(50, 85, 25, 25);
		check_theme_Nimrod_Silver.addActionListener(this);
		check_theme_PGS.setBounds(50, 130, 25, 25);
		check_theme_PGS.addActionListener(this);
		check_theme_Liquid.setBounds(50, 175, 25, 25);
		check_theme_Liquid.addActionListener(this);
		check_theme_Office.setBounds(50, 220, 25, 25);
		check_theme_Office.addActionListener(this);	
		label_theme_nimrod.setBounds(150, 40, 100, 25);	
		label_theme_Nimrod_Silver.setBounds(150, 85, 100, 25);
		label_theme_PGS.setBounds(150, 130, 100, 25);
		label_theme_liquid.setBounds(150, 175, 100, 25);	
		label_theme_Office.setBounds(150, 220, 100, 25);
	
		link_name1.setBounds(10, 30, 130, 25);
		link_url1.setBounds(145, 30, 220, 25);
		link_name2.setBounds(10, 80, 130, 25);
		link_url2.setBounds(145, 80, 220, 25);
		link_name3.setBounds(10, 130, 130, 25);
		link_url3.setBounds(145, 130, 220, 25);		

		label_search_engine.setBounds(10, 158, 100, 25);
		link_separator.setBounds(10, 178, 346, 2);
		
		check_only_Baidu.setBounds(20, 187, 25, 25);
		label_only_Baidu.setBounds(70, 187, 300, 25);
		check_only_Google.setBounds(20, 217, 25, 25);
		label_only_Google.setBounds(70, 217, 300, 25);
		check_search_random.setBounds(20, 247, 25, 25);
		label_search_random.setBounds(70, 247, 300, 25);
		
		switch_Page_General(true);
		setPageGeneral(true);
		
		setLanguage();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(false);
			this.dispose();
		} 
		else if (e.getSource() == button_apply) {
			
			//general
//			String path = FileLocator.getAppPath(com.xiami.app.starter.class);
			String path = System.getProperty("user.dir");
//			System.out.println(System.getProperty("user.dir"));
			if (check_start_run.isSelected()&&!isStart) {
				SystemConfig.setSystemProp(Keys.START_WITH_WINDOWS, "0");
				RegEditTool.setValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run", "spirit", path+"\\spirit.exe"); 
				Logger.getLogger().info("Set Software startup with Windows");
			} else if (!check_start_run.isSelected()&&isStart){
				SystemConfig.setSystemProp(Keys.START_WITH_WINDOWS, "1");
				RegEditTool.deleteValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run","spirit");
				Logger.getLogger().info("Set Software do not startup with Windows");
			}
			if (check_is_iconfied.isSelected()&&!isIconfied) {
				SystemConfig.setSystemProp(Keys.IS_ICONFIED, "0");
			}else if (!check_is_iconfied.isSelected()&&isIconfied){
				SystemConfig.setSystemProp(Keys.IS_ICONFIED, "1");
			}
			if (check_is_load_word.isSelected()&&!isLoad) {
				SystemConfig.setSystemProp(Keys.IS_LOAD_WORD, "0");
			}else if (!check_is_load_word.isSelected()&&isLoad){
				SystemConfig.setSystemProp(Keys.IS_LOAD_WORD, "1");
			}
			if (check_is_window_attached.isSelected()&&!isAttached) {
				SystemConfig.setSystemProp(Keys.IS_WINDOW_ATTACHED, "0");
				DragListener.setIsDragToBorderAllowed("0");
			}else if (!check_is_window_attached.isSelected()&&isAttached){
				SystemConfig.setSystemProp(Keys.IS_WINDOW_ATTACHED, "1");
				DragListener.setIsDragToBorderAllowed("1");
			}
			if (check_is_icon_dynamic.isSelected()&&!isDynamic) {
				SystemConfig.setSystemProp(Keys.IS_ICON_DYNAMIC, "0");
			}else if (!check_is_icon_dynamic.isSelected()&&isDynamic){
				SystemConfig.setSystemProp(Keys.IS_ICON_DYNAMIC, "1");
			}
			mainFrame.resetIcon();
			
			//language
			boolean isChanged = false;
				
			if (check_locale_en.isSelected() && default_locale_index != 0) {
				SystemConfig.setSystemProp(Keys.LOCALE, Keys.LOCALE_EN);
			} else if (check_locale_jp.isSelected()
					&& default_locale_index != 1) {
				SystemConfig.setSystemProp(Keys.LOCALE, Keys.LOCALE_JP);
			}
			
			if (check_language_jp.isSelected() && default_language_index != 1) {
				SystemConfig.setLanguageType(Keys.LANGUAGE_CONFIG_JP);
				SystemConfig.setSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT,
						Keys.LANGUAGE_CONFIG_JP);
				isChanged = true;
			} else if (check_language_en.isSelected()
					&& default_language_index != 0) {
				SystemConfig.setLanguageType(Keys.LANGUAGE_CONFIG_EN);
				SystemConfig.setSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT,
						Keys.LANGUAGE_CONFIG_EN);
				isChanged = true;
			} else if (check_language_cn.isSelected()
					&& default_language_index != 2) {
				SystemConfig.setLanguageType(Keys.LANGUAGE_CONFIG_CN);
				SystemConfig.setSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT,
						Keys.LANGUAGE_CONFIG_CN);
				isChanged = true;
			}
			if (isChanged) {
				if (null != MainFrame.getWordPanel()) {
					MainFrame.getWordPanel().setting();
					if (null != MainFrame.getWordPanel().getDetailPanel()) {
						MainFrame.getWordPanel().getDetailPanel().setLanguage();
					}
				}
				if (null != mainFrame) {
					mainFrame.setLanguage();
				}
				if (null != MainFrame.getTextPanel()) {
					MainFrame.getTextPanel().setLanguage();
					if (null != MainFrame.getTextPanel().getMaxTextFrame()) {
						MainFrame.getTextPanel().getMaxTextFrame()
								.setLanguage();
					}
				}
				if (null != MainFrame.getNoticePanel()) {
					MainFrame.getNoticePanel().setLanguage();
				}
				if (null != MainFrame.getEcDictPanel()) {
					MainFrame.getEcDictPanel().setLanguage();
				}
				if (null != MainFrame.getCeDictPanel()) {
					MainFrame.getCeDictPanel().setLanguage();
				}
				if (null != MainFrame.getJcDictPanel()) {
					MainFrame.getJcDictPanel().setLanguage();
				}
				if (null != MainFrame.getCjDictPanel()) {
					MainFrame.getCjDictPanel().setLanguage();
				}
				if (null != MainFrame.getPhoneticPanel()) {
					MainFrame.getPhoneticPanel().setLanguage();
				}
				if (TipWindow.isShowUp) {
					TipWindow.getInstance().showUp();
				}

				SysIcon.settings();
				setLanguage();
				pane.setLanguage();
			}
			
			//theme
			int index_theme = -1;
			if (check_theme_Nimrod.isSelected()) {
				index_theme = 0;
			}else if (check_theme_Nimrod_Silver.isSelected()) {
				index_theme = 1;
			}else if (check_theme_PGS.isSelected()) {
				index_theme = 2;
			}else if (check_theme_Liquid.isSelected()) {
				index_theme = 3;
			}else if (check_theme_Office.isSelected()) {
				index_theme = 4;
			}
			
			if(index_theme != default_theme_index ){
				if (index_theme == 0) {
					SystemConfig.setSystemProp(Keys.THEME_DEFAULT, "Nimrod");
				}else if (index_theme == 1) {
					SystemConfig.setSystemProp(Keys.THEME_DEFAULT, "Nimrod_Silver");
				}else if (index_theme == 2) {
					SystemConfig.setSystemProp(Keys.THEME_DEFAULT, "PGS");
				}else if (index_theme == 3) {
					SystemConfig.setSystemProp(Keys.THEME_DEFAULT, "Liquid");
				}else if (index_theme == 4) {
					SystemConfig.setSystemProp(Keys.THEME_DEFAULT, "Office");
				}
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp(Keys.THEME_RESET));
			}
			
			//tips
			if (check_tip_random.isSelected()&&default_tip_index==1) {
				SystemConfig.setSystemProp(Keys.TIP_TYPE, "0");
				SysIcon.setTipType(0);
			} else if (check_tip_order.isSelected()&&default_tip_index==0) {
				SystemConfig.setSystemProp(Keys.TIP_TYPE, "1");
				SysIcon.setTipType(1);
			} 
			if (check_tip_on.isSelected()&&default_tip_switch_index==1) {
				SystemConfig.setSystemProp(Keys.TIP_SWITCH, "0");
				SysIcon.setTipSwitch(0);
			} else if (check_tip_off.isSelected()&&default_tip_switch_index==0) {
				SystemConfig.setSystemProp(Keys.TIP_SWITCH, "1");
				SysIcon.setTipSwitch(1);
			}

			//link 
			boolean isLinkChanged = false;
			if (check_only_Baidu.isSelected()&&default_link_index!=0) {
				SystemConfig.setSystemProp(Keys.SEARCH_ENGINE_TYPE, "0");
			} else if (check_only_Google.isSelected()&&default_link_index!=1) {
				SystemConfig.setSystemProp(Keys.SEARCH_ENGINE_TYPE, "1");
			}else if (check_search_random.isSelected()&&default_link_index!=2){
				SystemConfig.setSystemProp(Keys.SEARCH_ENGINE_TYPE, "2");
			}
			if (link_name1.getText().equals("")) {
				JOptionPane
						.showMessageDialog(this, SystemConfig.getLanguageProp("Link.menu")+"1 "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}else if (link_name2.getText().equals("")) {
				JOptionPane
				.showMessageDialog(this, SystemConfig.getLanguageProp("Link.menu")+"2 "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}else if (link_name3.getText().equals("")) {
				JOptionPane
				.showMessageDialog(this, SystemConfig.getLanguageProp("Link.menu")+"3 "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}else if (link_url1.getText().equals("")) {
				JOptionPane
				.showMessageDialog(this, SystemConfig.getLanguageProp("Link.menu")+"1 "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}else if (link_url2.getText().equals("")) {
				JOptionPane
				.showMessageDialog(this, SystemConfig.getLanguageProp("Link.menu")+"2 "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}else if (link_url3.getText().equals("")) {
				JOptionPane
				.showMessageDialog(this, SystemConfig.getLanguageProp("Link.menu")+"3 "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}
			if(!SystemConfig.getSystemProp(Keys.LINK_NAME1).equals(link_name1.getText())){
				SystemConfig.setSystemProp(Keys.LINK_NAME1,link_name1.getText());
				isLinkChanged = true;
			}
			if(!SystemConfig.getSystemProp(Keys.LINK_NAME2).equals(link_name2.getText())){
				SystemConfig.setSystemProp(Keys.LINK_NAME2,link_name2.getText());
				isLinkChanged = true;
			}
			if(!SystemConfig.getSystemProp(Keys.LINK_NAME3).equals(link_name3.getText())){
				SystemConfig.setSystemProp(Keys.LINK_NAME3,link_name3.getText());
				isLinkChanged = true;
			}
			if(!SystemConfig.getSystemProp(Keys.LINK_URL1).equals(link_url1.getText())){
				SystemConfig.setSystemProp(Keys.LINK_URL1,link_url1.getText());
				isLinkChanged = true;
			}
			if(!SystemConfig.getSystemProp(Keys.LINK_URL2).equals(link_url2.getText())){
				SystemConfig.setSystemProp(Keys.LINK_URL2,link_url2.getText());
				isLinkChanged = true;
			}
			if(!SystemConfig.getSystemProp(Keys.LINK_URL3).equals(link_url3.getText())){
				SystemConfig.setSystemProp(Keys.LINK_URL3,link_url3.getText());
				isLinkChanged = true;
			}
			if (isLinkChanged) {
				MainFrame.getWordPanel().updateLink();
				if (null != MainFrame.getWordPanel().getDetailPanel()) {
					MainFrame.getWordPanel().getDetailPanel().updateLink();
				}
				if (null != MainFrame.getTextPanel()) {
					MainFrame.getTextPanel().updateLink();
				}
				if (null != MainFrame.getNoticePanel()) {
					MainFrame.getNoticePanel().updateLink();
				}
				if (null != MainFrame.getEcDictPanel()) {
					MainFrame.getEcDictPanel().updateLink();
				}
				if (null != MainFrame.getCeDictPanel()) {
					MainFrame.getCeDictPanel().updateLink();
				}
				if (null != MainFrame.getJcDictPanel()) {
					MainFrame.getJcDictPanel().updateLink();
				}
				if (null != MainFrame.getCjDictPanel()) {
					MainFrame.getCjDictPanel().updateLink();
				}
				if (null != MainFrame.getPhoneticPanel()) {
					MainFrame.getPhoneticPanel().updateLink();
				}
				SysIcon.updateLink();
			}
			this.setVisible(false);
		}
	}
	
	public void run() {
		createComponents();
		addComponents();
		setting();
	}
	
	public void switch_Page_General(boolean isVisible) {
		if (isVisible) {
			panel_center.add(label_start_run);
			panel_center.add(check_start_run);
			panel_center.add(label_is_iconfied);
			panel_center.add(check_is_iconfied);
			panel_center.add(label_is_load_word);
			panel_center.add(check_is_load_word);
			panel_center.add(label_is_window_attached);
			panel_center.add(check_is_window_attached);
			panel_center.add(label_is_icon_dynamic);
			panel_center.add(check_is_icon_dynamic);
			panel.add(button_apply);
			label_start_run.setVisible(isVisible);
			check_start_run.setVisible(isVisible);
			label_is_iconfied.setVisible(isVisible);
			check_is_iconfied.setVisible(isVisible);
			label_is_load_word.setVisible(isVisible);
			check_is_load_word.setVisible(isVisible);
			label_is_window_attached.setVisible(isVisible);
			check_is_window_attached.setVisible(isVisible);
			label_is_icon_dynamic.setVisible(isVisible);
			check_is_icon_dynamic.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.general"));
		} else {
			label_start_run.setVisible(isVisible);
			check_start_run.setVisible(isVisible);
			label_is_iconfied.setVisible(isVisible);
			check_is_iconfied.setVisible(isVisible);
			label_is_load_word.setVisible(isVisible);
			check_is_load_word.setVisible(isVisible);
			label_is_window_attached.setVisible(isVisible);
			check_is_window_attached.setVisible(isVisible);
			label_is_icon_dynamic.setVisible(isVisible);
			check_is_icon_dynamic.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			panel_center.remove(label_start_run);
			panel_center.remove(check_start_run);
			panel_center.remove(label_is_iconfied);
			panel_center.remove(check_is_iconfied);
			panel_center.remove(label_is_load_word);
			panel_center.remove(check_is_load_word);
			panel_center.remove(label_is_window_attached);
			panel_center.remove(check_is_window_attached);
			panel_center.remove(label_is_icon_dynamic);
			panel_center.remove(check_is_icon_dynamic);
			panel.remove(button_apply);
		}
	}
	
	public void switch_Page_Language(boolean isVisible) {
		if (isVisible) {
			panel_center.add(check_language_en);
			panel_center.add(check_language_cn);
			panel_center.add(check_language_jp);
			panel_center.add(check_locale_en);
			panel_center.add(check_locale_jp);
			panel_center.add(label_language_en);
			panel_center.add(label_language_cn);
			panel_center.add(label_language_jp);
			panel_center.add(label_locale_en);
			panel_center.add(label_locale_jp);
			panel_center.add(locale_separator);
			panel.add(button_apply);
			check_language_en.setVisible(isVisible);
			check_language_cn.setVisible(isVisible);
			check_language_jp.setVisible(isVisible);
			label_language_en.setVisible(isVisible);
			label_language_cn.setVisible(isVisible);
			label_language_jp.setVisible(isVisible);
			label_locale_jp.setVisible(isVisible);
			label_locale_en.setVisible(isVisible);
			check_locale_en.setVisible(isVisible);
			check_locale_jp.setVisible(isVisible);
			locale_separator.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.language"));
		} else {
			check_language_en.setVisible(isVisible);
			check_language_cn.setVisible(isVisible);
			check_language_jp.setVisible(isVisible);
			check_locale_en.setVisible(isVisible);
			check_locale_jp.setVisible(isVisible);
			label_language_en.setVisible(isVisible);
			label_language_cn.setVisible(isVisible);
			label_language_jp.setVisible(isVisible);
			locale_separator.setVisible(isVisible);
			label_locale_jp.setVisible(isVisible);
			label_locale_en.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			panel_center.remove(check_language_en);
			panel_center.remove(check_language_cn);
			panel_center.remove(check_language_jp);
			panel_center.remove(label_language_en);
			panel_center.remove(label_language_cn);
			panel_center.remove(label_language_jp);
			panel_center.remove(label_locale_en);
			panel_center.remove(label_locale_jp);
			panel_center.remove(check_locale_en);
			panel_center.remove(check_locale_jp);
			panel_center.remove(check_language_en);
			panel.remove(button_apply);
		}
	}
	public void switch_Page_Tip(boolean isVisible) {
		if (isVisible) {
			panel_center.add(label_tip_random);
			panel_center.add(label_tip_order);
			panel_center.add(check_tip_random);
			panel_center.add(check_tip_order);
			panel_center.add(label_tip_on);
			panel_center.add(label_tip_off);
			panel_center.add(check_tip_on);
			panel_center.add(check_tip_off);
			panel_center.add(tip_separator);			
			panel.add(button_apply);
			tip_separator.setVisible(isVisible);
			check_tip_random.setVisible(isVisible);
			check_tip_order.setVisible(isVisible);
			label_tip_order.setVisible(isVisible);
			label_tip_random.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			label_tip_on.setVisible(isVisible);
			label_tip_off.setVisible(isVisible);
			check_tip_on.setVisible(isVisible);
			check_tip_off.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.tips"));
		} else {
			tip_separator.setVisible(isVisible);
			label_tip_random.setVisible(isVisible);
			label_tip_order.setVisible(isVisible);
			check_tip_random.setVisible(isVisible);
			check_tip_order.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			label_tip_on.setVisible(isVisible);
			label_tip_off.setVisible(isVisible);
			check_tip_on.setVisible(isVisible);
			check_tip_off.setVisible(isVisible);
			panel_center.remove(tip_separator);
			panel_center.remove(label_tip_random);
			panel_center.remove(label_tip_order);
			panel_center.remove(check_tip_random);
			panel_center.remove(check_tip_order);
			panel_center.remove(label_tip_on);
			panel_center.remove(label_tip_off);
			panel_center.remove(check_tip_on);
			panel_center.remove(check_tip_off);
			panel.remove(button_apply);
		}
	}
	public void switch_Page_Theme(boolean isVisible) {

		if (isVisible) {
			panel_center.add(check_theme_Nimrod);
			panel_center.add(check_theme_Liquid);
			panel_center.add(check_theme_Office);
			panel_center.add(check_theme_Nimrod_Silver);
			panel_center.add(check_theme_PGS);
			panel.add(button_apply);
			panel_center.add(label_theme_nimrod);
			panel_center.add(label_theme_liquid);
			panel_center.add(label_theme_Office);
			panel_center.add(label_theme_Nimrod_Silver);
			panel_center.add(label_theme_PGS);
			check_theme_Nimrod.setVisible(isVisible);
			check_theme_Liquid.setVisible(isVisible);
			check_theme_Office.setVisible(isVisible);
			check_theme_Nimrod_Silver.setVisible(isVisible);
			check_theme_PGS.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			label_theme_nimrod.setVisible(isVisible);
			label_theme_liquid.setVisible(isVisible);
			label_theme_Office.setVisible(isVisible);
			label_theme_Nimrod_Silver.setVisible(isVisible);
			label_theme_PGS.setVisible(isVisible);
			titleBorder.setTitle(SystemConfig.getLanguageProp("option.header.theme"));
		} else {
			check_theme_Nimrod.setVisible(isVisible);
			check_theme_Liquid.setVisible(isVisible);
			check_theme_Office.setVisible(isVisible);
			check_theme_Nimrod_Silver.setVisible(isVisible);
			check_theme_PGS.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			label_theme_nimrod.setVisible(isVisible);
			label_theme_liquid.setVisible(isVisible);
			label_theme_Office.setVisible(isVisible);
			label_theme_Nimrod_Silver.setVisible(isVisible);
			label_theme_PGS.setVisible(isVisible);
			panel_center.remove(check_theme_Nimrod);
			panel_center.remove(check_theme_Liquid);
			panel_center.remove(check_theme_Office);
			panel_center.remove(check_theme_Nimrod_Silver);
			panel_center.remove(check_theme_PGS);
			panel.remove(button_apply);
			panel_center.remove(label_theme_nimrod);
			panel_center.remove(label_theme_liquid);
			panel_center.remove(label_theme_Office);
			panel_center.remove(label_theme_Nimrod_Silver);
			panel_center.remove(label_theme_PGS);
		}
	}

	public void switch_Page_Link(boolean isVisible) {

		if (isVisible) {
			titleBorder.setTitle(SystemConfig.getLanguageProp("Link.menu"));
			panel_center.add(link_name1);
			panel_center.add(link_name2);
			panel_center.add(link_name3);
			panel_center.add(link_url1);
			panel_center.add(link_url2);
			panel_center.add(link_url3);
			panel_center.add(check_only_Baidu);
			panel_center.add(label_only_Baidu);
			panel_center.add(check_only_Google);
			panel_center.add(label_only_Google);
			panel_center.add(check_search_random);
			panel_center.add(label_search_random);
			panel_center.add(link_separator);
			panel_center.add(label_search_engine);			
			panel.add(button_apply);
			button_apply.setVisible(isVisible);
			link_separator.setVisible(isVisible);
			link_name1.setVisible(isVisible);
			link_name2.setVisible(isVisible);
			link_name3.setVisible(isVisible);
			link_url1.setVisible(isVisible);
			link_url2.setVisible(isVisible);
			link_url3.setVisible(isVisible);
			check_only_Baidu.setVisible(isVisible);
			label_only_Baidu.setVisible(isVisible);
			check_only_Google.setVisible(isVisible);
			label_only_Google.setVisible(isVisible);
			check_search_random.setVisible(isVisible);
			label_search_random.setVisible(isVisible);	
			label_search_engine.setVisible(isVisible);	
		} else {
			link_name1.setVisible(isVisible);
			link_name2.setVisible(isVisible);
			link_name3.setVisible(isVisible);
			link_url1.setVisible(isVisible);
			link_url2.setVisible(isVisible);
			link_url3.setVisible(isVisible);
			check_only_Baidu.setVisible(isVisible);
			label_only_Baidu.setVisible(isVisible);
			check_only_Google.setVisible(isVisible);
			label_only_Google.setVisible(isVisible);
			check_search_random.setVisible(isVisible);
			label_search_random.setVisible(isVisible);
			link_separator.setVisible(isVisible);
			button_apply.setVisible(isVisible);
			label_search_engine.setVisible(isVisible);	
			panel_center.remove(button_apply);
			panel_center.remove(link_name1);
			panel_center.remove(link_name2);
			panel_center.remove(link_name3);
			panel_center.remove(link_url1);
			panel_center.remove(link_url2);
			panel_center.remove(link_url3);
			panel_center.remove(check_only_Baidu);
			panel_center.remove(label_only_Baidu);
			panel_center.remove(check_only_Google);
			panel_center.remove(label_only_Google);
			panel_center.remove(check_search_random);
			panel_center.remove(label_search_random);
			panel_center.remove(link_separator);
			panel_center.remove(label_search_engine);
		}
}
	
	public OptionDialog getIns() {
		return this;
	}

	public void setDefautLanguage() {
		String default_language = SystemConfig
				.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT);
		if (default_language.equals(Keys.LANGUAGE_CONFIG_EN)) {
			check_language_en.setSelected(true);
			default_language_index = 0;
		} else if (default_language.equals(Keys.LANGUAGE_CONFIG_JP)) {
			check_language_jp.setSelected(true);
			default_language_index = 1;
		}
		if (default_language.equals(Keys.LANGUAGE_CONFIG_CN)) {
			check_language_cn.setSelected(true);
			default_language_index = 2;
		}
	}

	public void setDefaultLocale(){
		String default_locale = SystemConfig
		.getSystemProp(Keys.LOCALE);
		if(default_locale.equals("en")){
			check_locale_en.setSelected(true);
			default_locale_index = 0;
		}else{
			check_locale_jp.setSelected(true);
			default_locale_index = 1;
		}
	}
	
	public void setDefautTip() {
		String tip = SystemConfig
				.getSystemProp(Keys.TIP_TYPE);
		String tip_switch = SystemConfig
		.getSystemProp(Keys.TIP_SWITCH);
		if (tip.equals("0")) {
			check_tip_random.setSelected(true);
			default_tip_index = 0;
		} else if (tip.equals("1")) {
			check_tip_order.setSelected(true);
			default_tip_index = 1;
		}
		if (tip_switch.equals("0")) {
			check_tip_on.setSelected(true);
			default_tip_switch_index = 0;
		} else if (tip_switch.equals("1")) {
			check_tip_off.setSelected(true);
			default_tip_switch_index = 1;
		}
	}
	
	public void setDefautGeneral() {
		String Run = SystemConfig.getSystemProp(Keys.START_WITH_WINDOWS);
		String Iconfied = SystemConfig.getSystemProp(Keys.IS_ICONFIED);
		String LoadWord = SystemConfig.getSystemProp(Keys.IS_LOAD_WORD);
		String WindowAttached = SystemConfig.getSystemProp(Keys.IS_WINDOW_ATTACHED);
		String IconDynamic = SystemConfig.getSystemProp(Keys.IS_ICON_DYNAMIC);
		if(Run.equals("0")) {
			check_start_run.setSelected(true);
			isStart = true;
		} else{
			check_start_run.setSelected(false);
			isStart = false;
		}
		if (Iconfied.equals("0")) {
			check_is_iconfied.setSelected(true);
			isIconfied = true;
		} else{
			check_is_iconfied.setSelected(false);
			isIconfied = false;
		}
		if (LoadWord.equals("0")) {
			check_is_load_word.setSelected(true);
			isLoad = true;
		} else{
			check_is_load_word.setSelected(false);
			isLoad = false;
		}
		if (WindowAttached.equals("0")) {
			check_is_window_attached.setSelected(true);
			isAttached = true;
		} else{
			check_is_window_attached.setSelected(false);
			isAttached = false;
		}
		if (IconDynamic.equals("0")) {
			check_is_icon_dynamic.setSelected(true);
			isDynamic = true;
		} else{
			check_is_icon_dynamic.setSelected(false);
			isDynamic = false;
		}
	}
	
	public void setDefautTheme() {
		String default_Theme = SystemConfig.getSystemProp(Keys.THEME_DEFAULT);
		if (default_Theme.equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD))) {
			check_theme_Nimrod.setSelected(true);
			default_theme_index = 0;
		}else if (default_Theme.equals(SystemConfig
				.getSystemProp(Keys.THEME_NIMROD_SILVER))) {
			check_theme_Nimrod_Silver.setSelected(true);
			default_theme_index = 1;
		}else if (default_Theme.equals(SystemConfig
				.getSystemProp(Keys.THEME_PGS))) {
			check_theme_PGS.setSelected(true);
			default_theme_index = 2;
		}else if (default_Theme.equals(SystemConfig
				.getSystemProp(Keys.THEME_LIQUID))) {
			check_theme_Liquid.setSelected(true);
			default_theme_index = 3;
		} else if (default_Theme.equals(SystemConfig.getSystemProp(Keys.THEME_OFFICE))) {
			check_theme_Office.setSelected(true);
			default_theme_index = 4;
		}
	}
	public void setDefautLink() {
		link_name1.setText(SystemConfig.getSystemProp(Keys.LINK_NAME1));
		link_name2.setText(SystemConfig.getSystemProp(Keys.LINK_NAME2));
		link_name3.setText(SystemConfig.getSystemProp(Keys.LINK_NAME3));
		link_url1.setText(SystemConfig.getSystemProp(Keys.LINK_URL1));
		link_url2.setText(SystemConfig.getSystemProp(Keys.LINK_URL2));
		link_url3.setText(SystemConfig.getSystemProp(Keys.LINK_URL3));
		String search_engine = SystemConfig.getSystemProp(Keys.SEARCH_ENGINE_TYPE);
		if(search_engine.equals("0")){
			check_only_Baidu.setSelected(true);
			default_link_index = 0;
		}else if(search_engine.equals("1")){
			check_only_Google.setSelected(true);
			default_link_index = 1;
		}else{
			check_search_random.setSelected(true);
			default_link_index = 2;
		}
	}
	public boolean isPageGeneral() {
		return isPageGeneral;
	}

	public void setPageGeneral(boolean isPageGeneral) {
		this.isPageGeneral = isPageGeneral;
	}

	public boolean isPageLanguage() {
		return isPageLanguage;
	}

	public void setPageLanguage(boolean isPageLanguage) {
		this.isPageLanguage = isPageLanguage;
	}

	public boolean isPageTheme() {
		return isPageTheme;
	}

	public void setPageTheme(boolean isPageTheme) {
		this.isPageTheme = isPageTheme;
	}

	public boolean isPageTip() {
		return isPageTip;
	}

	public void setPageTip(boolean isPageTip) {
		this.isPageTip = isPageTip;
	}

	public boolean isPageLink() {
		return isPageLink;
	}

	public void setPageLink(boolean isPageLink) {
		this.isPageLink = isPageLink;
	}
}
