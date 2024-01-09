package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.xiami.Component.ImageButton;
import com.xiami.Component.ImageLabel;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.MainTabbedPane;
import com.xiami.bean.NoticeList;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.NoticeCounter;
import com.xiami.logic.NoticeLoader;
import com.xiami.logic.TiPCounter;
import com.xiami.logic.TimeCounter;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class MainFrame extends JFrame implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;

	private static ImagePanel panel;
	private ImageButton button_min;
	private ImageButton button_close;
	private JLabel label_title;
	private JLabel label_title_icon;
	private JLabel label_time;
	private ImageIcon imageIcon;

	private CommonUIManager ui;
	private OptionDialog optionDialog;
	public static WordPanel wordPanel;
	public static TextPanel textPanel;
	public static DetailPanel detailPanel;
	public static MainTabbedPane tabbedPane;
	public static NoticePanel noticePanel;
	public static ECDictPanel ecDictPanel;
	public static CEDictPanel ceDictPanel;
	public static JCDictPanel jcDictPanel;
	public static CJDictPanel cjDictPanel;
	public static PhoneticPanel phoneticPanel;
	private SysIcon sysicon;

	private Color bgColor;


	public void init() {

		this.setSize(Keys.INITIAL_WIDTH_MAIN,Keys.INITIAL_HEIGHT_MAIN);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_MAIN / 2,
				screenSize.height / 2 -Keys.INITIAL_HEIGHT_MAIN / 2);
		this.setUndecorated(true);
		new DragListener(getIns());

		sysicon = SysIcon.getInstance();
		ui = new CommonUIManager(sysicon);
		CommonUIManager.setUI();
		ui.startUp();
		
		createComponent();
		addComponent();
		setting();
		setLanguage();		

		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));

		this.setIconImage(imageIcon.getImage());
		
		setWord();
		
		Logger.getLogger().info("initiating notice resource.");
		
		NoticeLoader.updateCommon();
		
		NoticeList.setNoticelist(NoticeLoader.getNotice());
			
		Thread notice_thread = new Thread(new NoticeCounter(NoticeList.getNoticelist()));
		
		notice_thread.start();
		
		Logger.getLogger().info("notice resource has been initiated successfully.");
		
		Logger.getLogger().info("initiating time counter.");
		
		Thread time_thread = new Thread(TimeCounter.getInstance());
		
		TimeCounter.getInstance().setLabel_time(label_time);
	
		time_thread.start();
		
		Logger.getLogger().info("time counter has been initiated successfully.");
		
		Logger.getLogger().info("initiating tip counter.");
		
		Thread tip_thread = new Thread(TiPCounter.getInstance());
		
		tip_thread.start();
		
		Logger.getLogger().info("tip counter has been initiated successfully.");
		
	}

	public void createComponent() {
		tabbedPane = new MainTabbedPane(getIns(),2, 30, Keys.INITIAL_WIDTH_MAIN-6, 42);
		label_title_icon = new ImageLabel("",5, 5, 20, 20);
		resetIcon();
		label_title = new JLabel();
		label_time = new JLabel();
		button_min = new ImageButton("", Keys.INITIAL_WIDTH_MAIN - 60, 3, 20, 20,
				(ActionListener) getIns());
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_MAIN - 30, 3, 20, 20,
				(ActionListener) getIns());

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_MAIN - 5,Keys.INITIAL_HEIGHT_MAIN - 5);
		this.getContentPane().add(panel);
		panel.setLayout(null);
		imageIcon = new ImageIcon("icons/title.gif");		
		
	}

	public void addComponent() {

		panel.add(label_title);
		panel.add(label_title_icon);
		panel.add(button_close);
		panel.add(button_min);
		panel.add(tabbedPane);
		panel.add(label_time);

	}
	
	public void resetIcon(){
		String IconDynamic = SystemConfig.getSystemProp(Keys.IS_ICON_DYNAMIC);
		if(IconDynamic.equals("0")){
			label_title_icon.setIcon(new ImageIcon("icons/title_dynamic.gif"));
		}else{
			label_title_icon.setIcon(new ImageIcon("icons/title.gif"));
		}
		label_title_icon.repaint();
	}

	public void setting() {
	
		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			bgColor = new Color(237,235,193);
			label_title.setBackground(bgColor);
			this.getContentPane().setBackground(bgColor);
		}else if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod_Silver")) {
			bgColor = new Color(234,239,240);
			label_title.setBackground(bgColor);
			this.getContentPane().setBackground(bgColor);
		}
		label_title.setBounds(28, 3, Keys.INITIAL_WIDTH_MAIN - 100, 25);		
		label_time.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		label_time.setBounds(656, 3, 200, 22);		

		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.setAlwaysOnTop(false);
		sysicon.setMainFrame(getIns());
	}

	public void setLanguage(){
		this.setTitle(SystemConfig.getLanguageProp("MainFrame.title"));	
		label_title.setText(SystemConfig
				.getLanguageProp("MainFrame.labeltitle"));
		tabbedPane.setLanguage();
		label_title.setFont(FontManager.getFont(20,true));
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			if (SystemConfig.getSystemProp(Keys.IS_ICONFIED).equals("0")){
				Logger.getLogger().info("MainFrame Hide.");
				this.setVisible(false);
				this.dispose();
				SysIcon.switchMainFrameEnabled();
				SysIcon.changeIcon();
			}else{
				SystemConfig.setSystemProp(Keys.TOTAL_TIME,String.valueOf(TimeCounter.getTotal_time()));
				System.exit(0);
			}
		}
		if (e.getSource() == button_min) {
			Logger.getLogger().info("MainFrame ICONIFIED");
			this.setExtendedState(JFrame.ICONIFIED);
		}
	}

	public void run() {
		init();
		new Thread(){
			 public void run(){		
				 initDicts();
			 }
			}.start();		
	}

	public void initDicts(){
		
		Logger.getLogger().info("initiating dict panels.");
		
		jcDictPanel = new JCDictPanel(getIns(),tabbedPane);
		ui = new CommonUIManager(jcDictPanel);
		CommonUIManager.setUI();
		ui.startUp();
		jcDictPanel.setBounds(0, 55, 895, 675);
		
		cjDictPanel = new CJDictPanel(getIns(),tabbedPane);
		ui = new CommonUIManager(cjDictPanel);
		CommonUIManager.setUI();
		ui.startUp();
		cjDictPanel.setBounds(0, 55, 895, 675);
		
		ecDictPanel = new ECDictPanel(getIns(),tabbedPane);
		ui = new CommonUIManager(ecDictPanel);
		CommonUIManager.setUI();
		ui.startUp();
		ecDictPanel.setBounds(0, 55, 895, 675);
		
		ceDictPanel = new CEDictPanel(getIns(),tabbedPane);
		ui = new CommonUIManager(ceDictPanel);
		CommonUIManager.setUI();
		ui.startUp();
		ceDictPanel.setBounds(0, 55, 895, 675);
		
		Logger.getLogger().info("dict panels has been initiated successfully.");
	}
	
	public void setWord(){
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		if(null != detailPanel && detailPanel.isFromDetail()){
			detailPanel.switchVisible();
			panel.add(detailPanel);
		}
		else{
			if (!WordPanel.isInitialzed) {
				wordPanel = new WordPanel(getIns());
				ui = new CommonUIManager(wordPanel);
				CommonUIManager.setUI();
				ui.startUp();
				wordPanel.setBounds(0, 55, 895, 675);
				wordPanel.setVisible(true);
			} else {
				wordPanel.switchVisible();
			}
			panel.add(wordPanel);
			SysIcon.setSearchEnabled(true);
		}
		SysIcon.setSource(0);
		
	}
	public void setText(){
		if (!TextPanel.isInitialzed) {
			textPanel = new TextPanel(getIns());
			textPanel.setBounds(0, 55, 895, 675);
			ui = new CommonUIManager(textPanel);
			CommonUIManager.setUI();
			ui.startUp();
		} else {
			textPanel.switchVisible();
		}
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		panel.add(textPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	public void setNotice(){
		if (!NoticePanel.isInitialzed) {
			noticePanel = new NoticePanel(getIns());
			noticePanel.setBounds(0, 55, 895, 675);
			ui = new CommonUIManager(noticePanel);
			CommonUIManager.setUI();
			ui.startUp();
		} else {
			noticePanel.switchVisible();
		}		
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		panel.add(noticePanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	public static void setECDict(){
		if(!ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
		}
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		panel.add(ecDictPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	public static void setJCDict(){

		if(!jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
		}
				
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		panel.add(jcDictPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	public static void setCJDict(){

		if(!cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
		}
				
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		panel.add(cjDictPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	
	public static void setCEDict(){
		if(!ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
		}
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		if(null != phoneticPanel && phoneticPanel.isVisible()){
			phoneticPanel.switchVisible();
			panel.remove(phoneticPanel);
		}
		panel.add(ceDictPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	
	public void setPhonetic(){

		if (!PhoneticPanel.isInitialzed) {
			phoneticPanel = new PhoneticPanel(getIns());
			phoneticPanel.setBounds(0, 55, 895, 675);
			ui = new CommonUIManager(phoneticPanel);
			CommonUIManager.setUI();
			ui.startUp();
		} else {
			phoneticPanel.switchVisible();
		}		
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		if(null != noticePanel && noticePanel.isVisible()){
			noticePanel.switchVisible();
			panel.remove(noticePanel);
		}
		if(null != ecDictPanel && ecDictPanel.isVisible()){
			ecDictPanel.switchVisible();
			panel.remove(ecDictPanel);
		}
		if(null != jcDictPanel && jcDictPanel.isVisible()){
			jcDictPanel.switchVisible();
			panel.remove(jcDictPanel);
		}
		if(null != cjDictPanel && cjDictPanel.isVisible()){
			cjDictPanel.switchVisible();
			panel.remove(cjDictPanel);
		}
		if(null != ceDictPanel && ceDictPanel.isVisible()){
			ceDictPanel.switchVisible();
			panel.remove(ceDictPanel);
		}
		if(null != detailPanel && detailPanel.isVisible()){
			detailPanel.switchVisible();
			detailPanel.setFromDetail(true);
			panel.remove(detailPanel);
		}
		if(null != textPanel && textPanel.isVisible()){
			textPanel.switchVisible();
			panel.remove(textPanel);
		}
		panel.add(phoneticPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	
	public void setDetailPanel(DetailPanel detailPanel){
		if(null != wordPanel && wordPanel.isVisible()){
			wordPanel.switchVisible();
			panel.remove(wordPanel);
		}
		MainFrame.detailPanel = detailPanel;
		detailPanel.setBounds(0, 55, 895, 675);
		panel.add(detailPanel);
		SysIcon.setSource(0);
		SysIcon.setSearchEnabled(false);
	}
	
	public void backFromDetail(){
		panel.remove(detailPanel);
		detailPanel.setFromDetail(false);
		wordPanel.switchVisible();
		panel.add(wordPanel);
		SysIcon.setSearchEnabled(true);
	}
	
	public MainFrame getIns() {
		return this;
	}

	public OptionDialog getOptionDialog() {
		return optionDialog;
	}

	public void setOptionDialog(OptionDialog optionDialog) {
		this.optionDialog = optionDialog;
	}

	public ImagePanel getPanel() {
		return panel;
	}

	public void setPanel(ImagePanel panel) {
		MainFrame.panel = panel;
	}

	public static TextPanel getTextPanel() {
		return textPanel;
	}

	public static void setTextPanel(TextPanel textPanel) {
		MainFrame.textPanel = textPanel;
	}

	public static MainTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public static void setTabbedPane(MainTabbedPane tabbedPane) {
		MainFrame.tabbedPane = tabbedPane;
	}

	public static NoticePanel getNoticePanel() {
		return noticePanel;
	}

	public static void setNoticePanel(NoticePanel noticePanel) {
		MainFrame.noticePanel = noticePanel;
	}

	public static ECDictPanel getEcDictPanel() {
		return ecDictPanel;
	}

	public static void setEcDictPanel(ECDictPanel ecDictPanel) {
		MainFrame.ecDictPanel = ecDictPanel;
	}

	public static CEDictPanel getCeDictPanel() {
		return ceDictPanel;
	}

	public static void setCeDictPanel(CEDictPanel ceDictPanel) {
		MainFrame.ceDictPanel = ceDictPanel;
	}

	public static JCDictPanel getJcDictPanel() {
		return jcDictPanel;
	}

	public static void setJcDictPanel(JCDictPanel jcDictPanel) {
		MainFrame.jcDictPanel = jcDictPanel;
	}

	public static CJDictPanel getCjDictPanel() {
		return cjDictPanel;
	}

	public static void setCjDictPanel(CJDictPanel cjDictPanel) {
		MainFrame.cjDictPanel = cjDictPanel;
	}

	public static PhoneticPanel getPhoneticPanel() {
		return phoneticPanel;
	}

	public static void setPhoneticPanel(PhoneticPanel phoneticPanel) {
		MainFrame.phoneticPanel = phoneticPanel;
	}

	public static DetailPanel getDetailPanel() {
		return detailPanel;
	}

	public static WordPanel getWordPanel() {
		return wordPanel;
	}

	public static void setWordPanel(WordPanel wordPanel) {
		MainFrame.wordPanel = wordPanel;
	}

}
