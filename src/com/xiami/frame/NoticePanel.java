package com.xiami.frame;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.DateSelector;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.NoticeBean;
import com.xiami.bean.NoticeList;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.NoticeLoader;
import com.xiami.logic.TimeCounter;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.FontManager;

@SuppressWarnings("serial")
public class NoticePanel extends ImagePanel implements Runnable,
		ActionListener, ItemListener {

	private JLabel label_notice_time;
	private ImagePanel panel;
	private JCheckBox check_isCommon;
	private ImageButton button_add;
	private ImageButton button_update;
	private ImageButton button_delete;
	private ImageButton button_date;
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_file = new JMenu();
	private JMenu menu_edit = new JMenu();
	private JMenu menu_option = new JMenu();
	private JMenu menu_help = new JMenu();
	private JMenu menu_link = new JMenu();
	private JMenuItem menuItem_link_search = new JMenuItem();
	private JMenuItem menuItem_link1 = new JMenuItem();
	private JMenuItem menuItem_link2 = new JMenuItem();
	private JMenuItem menuItem_link3 = new JMenuItem();
	private JMenuItem menuItem_New = new JMenuItem();
	private JMenuItem menuItem_Update = new JMenuItem();
	private JMenuItem menuItem_Delete = new JMenuItem();
	private JMenuItem menuItem_Exit = new JMenuItem();
	private JMenuItem menuItem_Preferences = new JMenuItem();
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();
	private JPopupMenu pMenu = new JPopupMenu();
	private JMenuItem pMenuItem_Update = new JMenuItem();
	private JMenuItem pMenuItem_Delete = new JMenuItem();
	private JTextField text_name;
	private JTextField text_time;
	private JComboBox noticeTime;
	private JTextField text_user;
	private JLabel label_name;
	private JLabel label_noticetime;
	private JLabel label_time;
	private JLabel label_user;
	private JLabel label_isCommon;
	private ImageTextArea content;
	private JComboBox choice;
	private CommonScrollPane pane_title;
	private CommonScrollPane pane_content;
	private DefaultListModel model_title = new DefaultListModel();
	private JList list_title = new JList(model_title);

	private ArrayList<NoticeBean> contents;

	private CommonUIManager ui;
	private int index_title;
	public static boolean isInitialzed = false;

	private MainFrame mainFrame;
	private DateSelector dator;
	private OptionDialog optionDialog;

	public NoticePanel(MainFrame obj) {
		mainFrame = obj;
	}

	public void init() {		
		createComponent();
		addComponent();
		setting();
		isInitialzed = true;
		this.repaint();
	}

	public void createComponent() {
	
		panel = new ImagePanel(220,50,530,390);
		button_add = new ImageButton("", Keys.INITIAL_WIDTH_NOTICEPANEL - 120, 90, 80, 60,
				getIns());
		button_update = new ImageButton("", Keys.INITIAL_WIDTH_NOTICEPANEL - 120, 210, 80, 60,
				getIns());
		button_delete = new ImageButton("", Keys.INITIAL_WIDTH_NOTICEPANEL - 120, 330, 80, 60,
				getIns());
		button_date = new ImageButton("", 410, 175, 90, 30, getIns());	
		label_notice_time = new JLabel();
		label_name = new JLabel();
		label_user = new JLabel();
		label_time = new JLabel();
		label_noticetime = new JLabel();
		label_isCommon = new JLabel();
		text_name = new CommonTextField(218, 35, 170, 30);
		text_user = new CommonTextField(218, 100, 170, 30);
		text_time = new CommonTextField(218, 175, 170, 30);
		noticeTime = new JComboBox();
		content = new ImageTextArea();
		pane_title = new CommonScrollPane("", list_title);
		pane_content = new CommonScrollPane("",content);
		check_isCommon = new JCheckBox();
		choice = new JComboBox();

		addKeyBoardEvent();
		addMouseEvent();
		initChoice();
		initNoticeTime();

		new UndoHandler(text_name);
		new UndoHandler(text_time);
		new UndoHandler(text_user);
		new UndoHandler(content);
	}

	public void addComponent() {

		label_name.setBounds(33, 35, 180, 30);
		label_user.setBounds(33, 100, 180, 30);
		label_time.setBounds(33, 175, 180, 30);
		label_noticetime.setBounds(33, 255, 180, 30);
		label_isCommon.setBounds(33, 335, 180, 30);
		check_isCommon.setBounds(218, 335, 25, 25);
		choice.setBounds(258, 335, 130, 30);
		noticeTime.setBounds(218, 255, 170, 30);
		label_notice_time.setBounds(418, 255, 170, 30);
		pane_content.setBounds(223, Keys.INITIAL_HEIGHT_NOTICEPANEL - 200, 640, 190);
		pane_title.setBounds(5, 45, 180, Keys.INITIAL_HEIGHT_NOTICEPANEL - 55);
		
		text_time.setEnabled(false);

		this.add(button_add);
		this.add(button_update);
		this.add(button_delete);	
		this.add(pane_title);
		this.add(pane_content);
		this.add(panel);		
		this.add(menubar);
		
		panel.add(label_isCommon);
		panel.add(check_isCommon);
		panel.add(choice);
		panel.add(text_name);
		panel.add(button_date);
		panel.add(text_time);
		panel.add(noticeTime);
		panel.add(label_notice_time);
		panel.add(text_user);
		panel.add(label_name);
		panel.add(label_noticetime);
		panel.add(label_time);
		panel.add(label_user);
		
		menubar.add(menu_file);
		menubar.add(menu_edit);
		menubar.add(menu_option);
		menubar.add(menu_link);
		menubar.add(menu_help);
		
		menu_file.add(menuItem_New);
		menu_file.addSeparator();
		menu_file.add(menuItem_Exit);
		menu_edit.add(menuItem_Update);
		menu_edit.add(menuItem_Delete);
		menu_option.add(menuItem_Preferences);
		menu_help.add(menuItem_Statis);
		menu_help.add(menuItem_About);		
		
		menu_link.add(menuItem_link_search);
		menu_link.addSeparator();
		menu_link.add(menuItem_link1);
		menu_link.add(menuItem_link2);
		menu_link.add(menuItem_link3);

		menuItem_New.addActionListener(this);
		menuItem_Exit.addActionListener(this);
		menuItem_Update.addActionListener(this);
		menuItem_Delete.addActionListener(this);
		menuItem_Preferences.addActionListener(this);
		menuItem_About.addActionListener(this);
		menuItem_Statis.addActionListener(this);
		pMenuItem_Update.addActionListener(this);
		pMenuItem_Delete.addActionListener(this);
		menuItem_link_search.addActionListener(this);
		menuItem_link1.addActionListener(this);
		menuItem_link2.addActionListener(this);
		menuItem_link3.addActionListener(this);

		pMenu.add(pMenuItem_Update);
		pMenu.add(pMenuItem_Delete);

		check_isCommon.addActionListener(this);
		choice.addItemListener(this);

	}

	public void setting() {

		setLanguage();

		this.setLayout(null);
		panel.setLayout(null);
		menubar.setBounds(1, 18, Keys.INITIAL_WIDTH_NOTICEPANEL-5, 25);
		menubar.setBorderPainted(true);
		menubar.setBorder(BorderFactory.createBevelBorder(0));
		
		Cursor corsor = new Cursor(Cursor.HAND_CURSOR);
		menuItem_link_search.setCursor(corsor);
		menuItem_link1.setCursor(corsor);
		menuItem_link2.setCursor(corsor);
		menuItem_link3.setCursor(corsor);
		
		button_update.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		this.setVisible(true);
				
		list_title.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		button_update.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_update.setToolTipText("update a notice");
		
		button_add.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		button_add.setToolTipText("add a notice");
		
		button_delete.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_delete.setToolTipText("delete a notice");
		
		String locale = SystemConfig.getSystemProp(Keys.LOCALE);
		if(locale.equals(Keys.LOCALE_EN)){
			text_name.getInputContext().selectInputMethod(Locale.US);
			text_user.getInputContext().selectInputMethod(Locale.US);
			content.getInputContext().selectInputMethod(Locale.US);
		}else{
			text_name.getInputContext().selectInputMethod(Locale.JAPAN);
			text_user.getInputContext().selectInputMethod(Locale.JAPAN);
			content.getInputContext().selectInputMethod(Locale.JAPAN);
		}
		
		loadNotice(0);
	}
	
	public void setLanguage() {

		label_notice_time.setText("NoticePanel.minutes");
		button_add.setText(SystemConfig.getLanguageProp("NoticePanel.add"));
		button_update.setText(SystemConfig
				.getLanguageProp("NoticePanel.update"));
		button_delete.setText(SystemConfig
				.getLanguageProp("NoticePanel.delete"));

		label_name.setText(SystemConfig
				.getLanguageProp("NoticePanel.eventname"));
		label_user.setText(SystemConfig.getLanguageProp("NoticePanel.user"));
		label_noticetime.setText(SystemConfig
				.getLanguageProp("NoticePanel.noticetime"));
		label_notice_time.setText(SystemConfig
				.getLanguageProp("NoticePanel.minutes"));
		label_time
				.setText(SystemConfig.getLanguageProp("NoticePanel.fromtime"));
		label_isCommon.setText(SystemConfig
				.getLanguageProp("NoticePanel.iscommon"));
		button_date.setText(SystemConfig.getLanguageProp("NoticePanel.dator"));

		menu_option.setText(SystemConfig
				.getLanguageProp("NoticePanel.menu.option"));
		menu_file
				.setText(SystemConfig.getLanguageProp("NoticePanel.menu.file"));
		menu_edit
				.setText(SystemConfig.getLanguageProp("NoticePanel.menu.edit"));
		menu_help
				.setText(SystemConfig.getLanguageProp("NoticePanel.menu.help"));
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		menu_link.setText(SystemConfig.getLanguageProp("Link.menu"));
		
		menuItem_New.setText(SystemConfig.getLanguageProp("NoticePanel.add"));
		menuItem_Update.setText(SystemConfig
				.getLanguageProp("NoticePanel.update"));
		menuItem_Delete.setText(SystemConfig
				.getLanguageProp("NoticePanel.delete"));
		pMenuItem_Update.setText(SystemConfig
				.getLanguageProp("NoticePanel.update"));
		pMenuItem_Delete.setText(SystemConfig
				.getLanguageProp("NoticePanel.delete"));
		menuItem_Preferences.setText(SystemConfig
				.getLanguageProp("NoticePanel.menuitem.preferences"));
		menuItem_About.setText(SystemConfig
				.getLanguageProp("NoticePanel.menuitem.about"));
		menuItem_Exit.setText(SystemConfig
				.getLanguageProp("NoticePanel.menuitem.exit"));
		menuItem_link_search.setText(SystemConfig
				.getLanguageProp("Link.search"));
		menuItem_link1.setText(SystemConfig
				.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig
				.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig
				.getSystemProp("link_name3"));
		
		Font font = FontManager.getFont(16);
		Insets insets = new Insets(0,0,0,0);
		button_add.setFont(font);
		button_add.setMargin(insets);
		button_update.setFont(font);
		button_update.setMargin(insets);
		button_delete.setFont(font);
		button_delete.setMargin(insets);
		button_date.setFont(FontManager.getFont(14));
		button_date.setMargin(insets);	
		
		Font font_label = FontManager.getFont(16);
		label_name.setFont(font_label);
		label_user.setFont(font_label);
		label_time.setFont(font_label);
		label_noticetime.setFont(font_label);
		label_isCommon.setFont(font_label);
		label_notice_time.setFont(font_label);
		
		Font font_menu = FontManager.getFont(14);
		menu_file.setFont(font_menu);
		menu_edit.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_option.setFont(font_menu);
		menu_link.setFont(font_menu);

		TitledBorder sheetBorder = new TitledBorder(SystemConfig.getLanguageProp("MainFrame.notice"));
		sheetBorder.setTitleFont(FontManager.getFont(18,true));
		sheetBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		sheetBorder.setBorder(BorderFactory.createEtchedBorder());
		panel.setBorder(sheetBorder);	
	}

	

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == menuItem_About) {
			ui = new CommonUIManager(new AboutDialog());
			CommonUIManager.setUI();
			ui.startUp();
		}
		if (e.getSource() == menuItem_Statis) {
			ui = new CommonUIManager(new StatisDialog());
			CommonUIManager.setUI();
			ui.startUp();
		}
		if (e.getSource() == menuItem_Preferences) {
			if (!OptionDialog.isInitialized) {
				Logger.getLogger().info("Initializing OptionDialog");
				optionDialog = OptionDialog.getInstance();
				optionDialog.setMainFrame(mainFrame);
				ui = new CommonUIManager(optionDialog);
				CommonUIManager.setUI();
				ui.startUp();
				Logger.getLogger().info("Initialization done");
			} else {
				Logger.getLogger().info("OptionDialog show up");
				optionDialog = OptionDialog.getInstance();
				optionDialog.setting();
			}
		}
		if (e.getSource() == menuItem_Exit) {
			SystemConfig.setSystemProp(Keys.TOTAL_TIME,String.valueOf(TimeCounter.getTotal_time()));
			System.exit(0);
		}
		if (e.getSource() == menuItem_link_search) {
			Random ran = new Random();
			int i = ran.nextInt(100);
			String type = SystemConfig.getSystemProp(Keys.SEARCH_ENGINE_TYPE);
			if(type.equals("0")){
				i-=51;
			}else if(type.equals("1")){
				i+=51;
			}
			if(i<=50){
				LinkHandler.toUrl(SystemConfig
						.getSystemProp("link_baidu"));
			}else{
				LinkHandler.toUrl(SystemConfig
						.getSystemProp("link_google"));
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
		if (e.getSource() == button_add || e.getSource() == menuItem_New) {
			ui = new CommonUIManager(new NoticeAddDialog(getIns()));
			CommonUIManager.setUI();
			ui.startUp();
		}
		if (e.getSource() == button_delete || e.getSource() == menuItem_Delete
				|| e.getSource() == pMenuItem_Delete) {
			if (model_title.size() == 0) {
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("Message.OpenFirst"));
			} else {
				int result = JOptionPane.showConfirmDialog(getIns(),
						SystemConfig.getLanguageProp("Message.Delete"));
				if (result == 0) {
					String uuid = contents.get(list_title.getSelectedIndex())
							.getUUID();
					NoticeLoader.removeNotice(uuid);
					NoticeList.setNoticelist(NoticeLoader.getNotice());
					loadNotice(0);
				}
			}
		}
		if (e.getSource() == button_update || e.getSource() == menuItem_Update
				|| e.getSource() == pMenuItem_Update) {
			if (model_title.size() == 0) {
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("Message.OpenFirst"));
			} else {
				if (text_name.getText().equals("")) {
					JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("NoticePanel.eventname")+" "+SystemConfig.getLanguageProp("Message.input"));
				} 
//				else if (text_user.getText().equals("")) {
//					JOptionPane
//							.showMessageDialog(this, SystemConfig.getLanguageProp("NoticePanel.user")+" "+SystemConfig.getLanguageProp("Message.input"));
//				} 
				else if (text_time.getText().equals("")) {
					JOptionPane.showMessageDialog(this,
							SystemConfig.getLanguageProp("NoticePanel.noticetime")+" "+SystemConfig.getLanguageProp("Message.input"));
				} else if (noticeTime.getSelectedItem().toString().equals("")) {
					JOptionPane
					.showMessageDialog(this, SystemConfig.getLanguageProp("NoticePanel.noticetime")+" "+SystemConfig.getLanguageProp("Message.input"));
				} else if (content.getText().equals("")) {
					JOptionPane
							.showMessageDialog(this, SystemConfig.getLanguageProp("Message.content")+" "+SystemConfig.getLanguageProp("Message.input"));
				}  else {
					int result = JOptionPane.showConfirmDialog(getIns(),
							SystemConfig.getLanguageProp("Message.Update"));
					if (result == 0) {
						int index = list_title.getSelectedIndex();
						NoticeBean bean = contents.get(index);
						bean.setName(text_name.getText());
						bean.setUser(text_user.getText());
						bean.setFromtime(text_time.getText());
						bean.setNoticetime(noticeTime.getSelectedItem()
								.toString());
						bean.setContent(content.getText());
						bean.setIsCommon(String.valueOf(check_isCommon
								.isSelected()));
						NoticeLoader.setReNotice(true);
						NoticeLoader.updateNoticeBean(bean);
						NoticeList.setNoticelist(NoticeLoader.getNotice());
						loadNotice(index);
					}
				}
			}
		}
		if (e.getSource() == button_date) {
			dator = new DateSelector(getFromTime());
			ui = new CommonUIManager(dator);
			CommonUIManager.setUI();
			ui.startUp();
		}
		if (e.getSource() == check_isCommon) {
			if (check_isCommon.isSelected()) {
				choice.setEnabled(true);
				button_date.setEnabled(false);
				text_time.setText(choice.getSelectedItem().toString());
			} else {
				choice.setEnabled(false);
				button_date.setEnabled(true);
				if(null != contents){
					text_time.setText(contents.get(index_title).getFromtime());
				if (contents.get(index_title).getIsCommon().equals("false")) {
					choice.setSelectedIndex(0);
				}
				}
			}
		}
	}

	public void run() {
		init();
	}

	public void addMouseEvent() {
		list_title.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pMenu.show(list_title, e.getX(), e.getY());
				}
			}
			
			public void mouseReleased(MouseEvent e){
				if ((e.getClickCount() == 1) && (e.getButton() == 1)
						&& (null != contents)&&(index_title!=list_title.getSelectedIndex())) {
					loadSelectedNotice();
				}
			}
		});
	}

	public void addKeyBoardEvent() {

		list_title.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (null != contents && e.getSource() == list_title) {
					loadSelectedNotice();
					scrollToView();
				}
			}
		});
	}

	public void itemStateChanged(ItemEvent e) {
		if (check_isCommon.isSelected()) {
			text_time.setText(choice.getSelectedItem().toString());
		}
	}

	public void updateLink(){
		menuItem_link1.setText(SystemConfig
				.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig
				.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig
				.getSystemProp("link_name3"));
	}
	
	public void loadSelectedNotice(){
		NoticeBean bean = contents.get(list_title
				.getSelectedIndex());

		index_title = list_title.getSelectedIndex();

		text_name.setText(bean.getName());
		text_user.setText(bean.getUser());
		text_time.setText(bean.getFromtime());
		noticeTime.setSelectedItem(bean.getNoticetime());
		content.setText(bean.getContent());
		content.setCaretPosition(0);
		if (bean.getIsCommon().equals("true")) {
			check_isCommon.setSelected(true);
			choice.setSelectedItem(bean.getFromtime());
			button_date.setEnabled(false);
			choice.setEnabled(true);
		} else {
			text_time.setText(bean.getFromtime());
			check_isCommon.setSelected(false);
			choice.setSelectedIndex(0);
			button_date.setEnabled(true);
			choice.setEnabled(false);
		}
	}
	
	public void loadNotice(int index) {

		index_title = index;

		ArrayList<NoticeBean> contents_temp = NoticeList.getNoticelist();

		if (null != contents_temp) {
			clearData();
			contents = contents_temp;
			for (int i = 0; i < contents.size(); i++) {
				model_title.addElement(" " + contents.get(i).getName() + " ");
			}
			list_title.setSelectedIndex(index);
			if (contents_temp.size()!=0) {
				if(text_name.isEnabled()==false){
					text_name.setEnabled(true);
					text_user.setEnabled(true);
					noticeTime.setEnabled(true);
					content.setEnabled(true);
					check_isCommon.setEnabled(true);
				}
			NoticeBean bean = contents.get(index);
			text_name.setText(bean.getName());
			text_user.setText(bean.getUser());
			text_time.setText(bean.getFromtime());
			noticeTime.setSelectedItem(bean.getNoticetime());
			content.setText(bean.getContent());
			if (bean.getIsCommon().equals("true")) {
				check_isCommon.setSelected(true);
				choice.setSelectedItem(bean.getFromtime());
				button_date.setEnabled(false);
				choice.setEnabled(true);
			} else {
				check_isCommon.setSelected(false);
				button_date.setEnabled(true);
				choice.setEnabled(false);
			}
			}else{
				clearData();
				text_name.setEnabled(false);
				text_user.setEnabled(false);
				noticeTime.setEnabled(false);
				content.setEnabled(false);
				check_isCommon.setEnabled(false);
				choice.setEnabled(false);
				button_date.setEnabled(false);
			}
		}
	}

	public void clearData() {
		this.model_title.clear();
		this.text_name.setText("");
		this.text_user.setText("");
		this.content.setText("");
		this.text_time.setText("");
		this.noticeTime.setSelectedIndex(0);
		this.choice.setSelectedIndex(0);
		this.check_isCommon.setSelected(false);
		this.contents = null;
	}

	public void switchVisible() {
		if (this.isVisible() == false) {
			this.setLanguage();
			this.setVisible(true);
		} else {
			this.setVisible(false);
			this.requestFocus();
		}
	}

	public NoticePanel getIns() {
		return this;
	}

	private void scrollToView() {

		if (model_title.size() != 0) {

			int index = list_title.getSelectedIndex();
			
			int last_visible = list_title.getLastVisibleIndex();

			int first_visible = list_title.getFirstVisibleIndex();

			Rectangle rect = list_title.getCellBounds(index, index);

			if ((index + 1 > last_visible) || (index - 1 < first_visible)) {

				list_title.scrollRectToVisible(rect);
			}
		}
	}
	
	public int getTextCount() {
		return model_title.getSize();
	}

	public JTextField getFromTime() {
		return this.text_time;
	}

	public void initChoice() {
		int h = 0;
		int m = 0;
		for (int i = 0; i < 96; i++) {
			if (m == 60) {
				h++;
				m = 0;
			}
			choice.addItem((h < 10 ? "0" + String.valueOf(h) : String
					.valueOf(h))
					+ ":"
					+ (String.valueOf(m).equals("0") ? String.valueOf(m) + "0"
							: String.valueOf(m)));
			m += 15;
		}
	}

	public void initNoticeTime() {
		noticeTime.addItem("");
		noticeTime.addItem("1");
		noticeTime.addItem("5");
		noticeTime.addItem("10");
		noticeTime.addItem("15");
		noticeTime.addItem("25");
		noticeTime.addItem("30");
		noticeTime.addItem("45");
		noticeTime.addItem("60");
		noticeTime.addItem("90");
		noticeTime.addItem("120");
		noticeTime.addItem("180");
		noticeTime.addItem("240");
		noticeTime.addItem("300");
	}
}
