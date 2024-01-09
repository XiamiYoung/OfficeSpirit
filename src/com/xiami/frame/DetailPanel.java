package com.xiami.frame;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.SheetBean;
import com.xiami.bean.WordBean;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.DetailDataLoader;
import com.xiami.logic.FileLoader;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.FontManager;

@SuppressWarnings("serial")
public class DetailPanel extends ImagePanel implements ActionListener, Runnable {

	private JTextField word_jp;
	private JTextField word_cn;
	private JTextField pron;
	private JTextField sheet_name;
	private ImageTextArea sentns;
	private CommonScrollPane pane_sentns;
	private CommonScrollPane pane_list;
	private JPanel sheetPanel;
//	private ImageButton button_next;
//	private ImageButton button_pre;
	private ImageButton button_test;
	private ImageButton button_sheet_pre;
	private ImageButton button_sheet_next;
	private ImageButton button_update;
	private ImageButton button_delete;
	private ImageButton button_add;
	private ImageButton button_back;
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_file = new JMenu();
	private JMenu menu_option = new JMenu();
	private JMenu menu_help = new JMenu();
	private JMenu menu_link = new JMenu();
	private JMenuItem menuItem_link_search = new JMenuItem();
	private JMenuItem menuItem_link1 = new JMenuItem();
	private JMenuItem menuItem_link2 = new JMenuItem();
	private JMenuItem menuItem_link3 = new JMenuItem();
	private JMenuItem menuItem_Back = new JMenuItem();
	private JMenuItem menuItem_Preferences = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();	
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JPopupMenu pMenu = new JPopupMenu();
	private JMenuItem menuItem_update = new JMenuItem();
	private JMenuItem menuItem_delete = new JMenuItem();
	private JPopupMenu pMenu_Text = new JPopupMenu();
	private JMenuItem pmenuItem_lookUpC2E = new JMenuItem();
	private JMenuItem pmenuItem_lookUpE2C = new JMenuItem();
	private JMenuItem pmenuItem_lookUpJ2C = new JMenuItem();
	private JMenuItem pmenuItem_lookUpC2J = new JMenuItem();
	private JMenuItem pmenuItem_Copy = new JMenuItem();
	private JMenuItem pmenuItem_Cut = new JMenuItem();
	private JMenuItem pmenuItem_Paste = new JMenuItem();
	private JMenuItem pmenuItem_Undo = new JMenuItem();
	private JMenuItem pmenuItem_SelectAll = new JMenuItem();
	private JMenuItem pmenuItem_Delete = new JMenuItem();

	private JLabel label_word;
	private JLabel label_cn;
	private JLabel label_pron;
	private DefaultListModel model_word = new DefaultListModel();
	private JList detail_list_word = new JList(model_word);
	private int index;
	private JTable table;
	private SheetBean sheet;
	private DetailDataLoader dataloader = new DetailDataLoader();
	private WordPanel wordPanel;
	public static boolean isInitialized = false;
	private int index_date;
	private int max_index_date;
	private static boolean isShowedUp = false;
	private CommonUIManager ui;

	private boolean isFromDetail = false;
	private UndoHandler undoHandler;
	
	private WordAddDialog wordAddDialog;
	private OptionDialog optionDialog;

	@SuppressWarnings("unused")
	private int INITIAL_HEIGHT = 645;
	private int INITIAL_WIDTH = 900;

	public DetailPanel(int max_index_date, int index_date, JTable table,
			final WordPanel wordPanel, SheetBean sheet) {

		this.sheet = sheet;
		this.table = table;
		this.wordPanel = wordPanel;
		this.index_date = index_date;
		this.max_index_date = max_index_date;

	}

	public void initDetailPanel() {

		if (!isInitialized) {
			
			createComponents();
			
			setting();
			
			isInitialized = true;
		}
		
		setShowedUp(true);

		index = table.getSelectedRow();
		if(index == -1){
			index = 0;
		}
		getData(index, sheet);

		wordPanel.getMainFrame().setDetailPanel(getIns());
	}

	public void createComponents() {
		sheetPanel = new JPanel();
//		button_pre = new ImageButton("", 250, 250, 60, 60, getIns());
//		button_next = new ImageButton("", 250, 400, 60, 60, getIns());
		button_sheet_pre = new ImageButton("", 55, 20, 48, 30, getIns());
		button_sheet_next = new ImageButton("", 135, 20, 48, 30, getIns());
		button_back = new ImageButton("", 820, 60, 50, 30, getIns());
		button_test = new ImageButton("",815, 120, 60, 60, getIns());
		button_add = new ImageButton("", 815, 250, 60, 60, getIns());
		button_update = new ImageButton("", 815, 400, 60, 60, getIns());
		button_delete = new ImageButton("", 815, 530, 60, 60, getIns());
		
		sheet_name = new CommonTextField(10, 55, 210, 30);
		sheet_name.setHorizontalAlignment(JTextField.CENTER);
		sheet_name.setEditable(false);
		pane_list = new CommonScrollPane(detail_list_word);
		label_cn = new JLabel();
		label_word = new JLabel();
		label_pron = new JLabel();
		word_jp = new CommonTextField(330, 60, 465, 30);
		pron = new CommonTextField(330, 100, 465, 30);
		word_cn = new CommonTextField(330, 140, 465, 30);	
		sentns = new ImageTextArea();
		pane_sentns = new CommonScrollPane(sentns);
		
		this.add(pane_list);
		this.add(label_cn);
		this.add(label_pron);
		this.add(label_word);		
//		this.add(button_next);
//		this.add(button_pre);			
		this.add(pron);
		this.add(word_jp);
		this.add(word_cn);
		this.add(pane_sentns);
		this.add(button_test);
		this.add(button_update);
		this.add(button_delete);
		this.add(button_add);
		this.add(button_back);
		this.add(sheetPanel);
		sheetPanel.add(button_sheet_next);
		sheetPanel.add(button_sheet_pre);
		sheetPanel.add(sheet_name);
		
		this.add(menubar);
		menubar.add(menu_file);
		menubar.add(menu_option);
		menubar.add(menu_link);
		menubar.add(menu_help);
		
		pMenu.add(menuItem_update);
		pMenu.add(menuItem_delete);
		
		pMenu_Text.add(pmenuItem_lookUpE2C);
		pMenu_Text.add(pmenuItem_lookUpC2E);
		pMenu_Text.addSeparator();
		pMenu_Text.add(pmenuItem_lookUpJ2C);
		pMenu_Text.add(pmenuItem_lookUpC2J);
		pMenu_Text.addSeparator();
		pMenu_Text.add(pmenuItem_Copy);
		pMenu_Text.add(pmenuItem_Cut);
		pMenu_Text.add(pmenuItem_Paste);
		pMenu_Text.add(pmenuItem_Undo);
		pMenu_Text.add(pmenuItem_SelectAll);
		pMenu_Text.add(pmenuItem_Delete);
		
		menu_link.add(menuItem_link_search);
		menu_link.addSeparator();
		menu_link.add(menuItem_link1);
		menu_link.add(menuItem_link2);
		menu_link.add(menuItem_link3);
		menu_option.add(menuItem_Preferences);
		menu_file.add(menuItem_Back);	
		menu_help.add(menuItem_Statis);
		menu_help.add(menuItem_About);
		menuItem_Preferences.addActionListener(this);
		menuItem_Statis.addActionListener(this);
		menuItem_About.addActionListener(this);
		menuItem_Back.addActionListener(this);
		menuItem_link_search.addActionListener(this);
		menuItem_link1.addActionListener(this);
		menuItem_link2.addActionListener(this);
		menuItem_link3.addActionListener(this);
		menuItem_update.addActionListener(this);
		menuItem_delete.addActionListener(this);
		pmenuItem_Copy.addActionListener(this);
		pmenuItem_Cut.addActionListener(this);
		pmenuItem_Paste.addActionListener(this);
		pmenuItem_Undo.addActionListener(this);
		pmenuItem_SelectAll.addActionListener(this);
		pmenuItem_Delete.addActionListener(this);
		pmenuItem_lookUpE2C.addActionListener(this);
		pmenuItem_lookUpC2E.addActionListener(this);
		pmenuItem_lookUpJ2C.addActionListener(this);
		pmenuItem_lookUpC2J.addActionListener(this);
		
		repaint();

		addMouseEvent();
		addKeyBoardEvent();

		new UndoHandler(pron);
		new UndoHandler(word_cn);
		new UndoHandler(word_jp);
		undoHandler = new UndoHandler(sentns);
	}
	
	public void setting() {

		setLanguage();
		
		pane_list.setBounds(5, 150, 235, 485);
		menubar.setBounds(1, 18, INITIAL_WIDTH-5, 25);
		menubar.setBorderPainted(true);
		menubar.setBorder(BorderFactory.createBevelBorder(0));
		label_word.setBounds(250, 60, 95, 30);
		label_pron.setBounds(250, 100, 95, 30);
		label_cn.setBounds(250, 140, 95, 30);		
		pane_sentns.setBounds(250, 180, 550, 455);
		sheetPanel.setBounds(5, 50, 230, 95);
		
		String locale = SystemConfig.getSystemProp(Keys.LOCALE);
		if(locale.equals(Keys.LOCALE_EN)){
			word_jp.getInputContext().selectInputMethod(Locale.US);
			pron.getInputContext().selectInputMethod(Locale.US);
			sentns.getInputContext().selectInputMethod(Locale.US);
		}else{
			word_jp.getInputContext().selectInputMethod(Locale.JAPAN);
			pron.getInputContext().selectInputMethod(Locale.JAPAN);
			sentns.getInputContext().selectInputMethod(Locale.JAPAN);
		}
		
		word_cn.getInputContext().selectInputMethod(Locale.CHINA);
		
		Cursor corsor = new Cursor(Cursor.HAND_CURSOR);
		menuItem_link_search.setCursor(corsor);
		menuItem_link1.setCursor(corsor);
		menuItem_link2.setCursor(corsor);
		menuItem_link3.setCursor(corsor);
		
		detail_list_word.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		button_test.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_test.setToolTipText("Total words test");
		
		button_update.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_update.setToolTipText("update a word");
		
		button_add.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		button_add.setToolTipText("add a word");
		
		button_delete.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_delete.setToolTipText("delete a word");
		
		button_sheet_pre.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0), 
	            JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_sheet_pre.setToolTipText("switch to previous sheet.");
		
		button_sheet_next.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), 
	            JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_sheet_next.setToolTipText("switch to next sheet.");
		
		button_back.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_B,InputEvent.CTRL_MASK),
	            JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_back.setToolTipText("back to word frame.");
		
		this.setLayout(null);
		sheetPanel.setLayout(null);
	}
	
	public void setLanguage(){
//		button_pre.setText(SystemConfig.getLanguageProp("DetailPanel.pre"));
		button_sheet_pre.setText(SystemConfig
				.getLanguageProp("DetailPanel.sheet.pre"));
//		button_next.setText(SystemConfig.getLanguageProp("DetailPanel.next"));
		button_sheet_next.setText(SystemConfig
				.getLanguageProp("DetailPanel.sheet.next"));
		button_test.setText(SystemConfig
				.getLanguageProp("WordPanel.button.test"));
		button_add.setText(SystemConfig.getLanguageProp("DetailPanel.add"));
		button_back.setText(SystemConfig.getLanguageProp("DetailPanel.menuitem.back"));
		button_update.setText(SystemConfig
				.getLanguageProp("DetailPanel.update"));
		button_delete.setText(SystemConfig
				.getLanguageProp("DetailPanel.delete"));
		menuItem_update.setText(SystemConfig
				.getLanguageProp("DetailPanel.update"));
		menuItem_delete.setText(SystemConfig
				.getLanguageProp("DetailPanel.delete"));
		label_word.setText(SystemConfig.getLanguageProp("DetailPanel.word"));	
		label_cn.setText(SystemConfig.getLanguageProp("DetailPanel.chinese"));		
		label_pron.setText(SystemConfig.getLanguageProp("DetailPanel.pron"));				
		menu_file.setText(SystemConfig.getLanguageProp("DetailPanel.menu.file"));
		menu_option.setText(SystemConfig
		 .getLanguageProp("DetailPanel.menu.option"));
		menu_help.setText(SystemConfig.getLanguageProp("DetailPanel.menu.help"));
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		menu_link.setText(SystemConfig.getLanguageProp("Link.menu"));
		menuItem_Preferences.setText(SystemConfig
		 .getLanguageProp("DetailPanel.menuitem.preferences"));
		menuItem_About.setText(SystemConfig
				.getLanguageProp("DetailPanel.menuitem.about"));
		menuItem_Back.setText(SystemConfig
				.getLanguageProp("DetailPanel.menuitem.back"));
		menuItem_link_search.setText(SystemConfig
				.getLanguageProp("Link.search"));
		menuItem_link1.setText(SystemConfig
				.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig
				.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig
				.getSystemProp("link_name3"));
		pmenuItem_lookUpE2C.setText(SystemConfig.getLanguageProp("DictPanel.E2C"));
		pmenuItem_lookUpC2E.setText(SystemConfig.getLanguageProp("DictPanel.C2E"));
		pmenuItem_lookUpJ2C.setText(SystemConfig.getLanguageProp("DictPanel.J2C"));
		pmenuItem_lookUpC2J.setText(SystemConfig.getLanguageProp("DictPanel.C2J"));
		pmenuItem_Copy.setText(SystemConfig.getLanguageProp("MaxText.copy"));
		pmenuItem_Cut.setText(SystemConfig.getLanguageProp("MaxText.cut"));
		pmenuItem_Paste.setText(SystemConfig.getLanguageProp("MaxText.paste"));
		pmenuItem_Undo.setText(SystemConfig.getLanguageProp("MaxText.undo"));
		pmenuItem_SelectAll.setText(SystemConfig.getLanguageProp("MaxText.all"));
		pmenuItem_Delete.setText(SystemConfig.getLanguageProp("MaxText.delete"));
		
		Font font = FontManager.getFont(16);
		Insets insets = new Insets(0,0,0,0);
		button_test.setFont(font);
		button_test.setMargin(insets);
		button_add.setFont(font);
		button_add.setMargin(insets);
		button_update.setFont(font);
		button_update.setMargin(insets);
		button_delete.setFont(font);
		button_delete.setMargin(insets);
		button_sheet_pre.setFont(font);
		button_sheet_pre.setMargin(insets);
		button_sheet_next.setFont(font);
		button_sheet_next.setMargin(insets);
//		button_pre.setFont(font);
//		button_pre.setMargin(insets);
//		button_next.setFont(font);
//		button_next.setMargin(insets);
		button_back.setFont(font);
		button_back.setMargin(insets);
		label_word.setFont(font);
		label_cn.setFont(font);
		label_pron.setFont(font);
		
		Font font_menu = FontManager.getFont(14);
		menu_file.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_option.setFont(font_menu);
		menu_link.setFont(font_menu);
		
		TitledBorder fileBorder = new TitledBorder(SystemConfig.getLanguageProp("WordPanel.sheet"));
		fileBorder.setTitleFont(FontManager.getFont(15));
		fileBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		fileBorder.setBorder(BorderFactory.createEtchedBorder());
		sheetPanel.setBorder(fileBorder);
	}
	
	public void getData(int index, SheetBean sheet) {
		dataloader.LoadData(index, sheet, word_jp, word_cn, pron, sheet_name,
				sentns, model_word);
		detail_list_word.setSelectedIndex(index);
		sentns.setCaretPosition(0);
		scrollToView();
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
			if (!OptionDialog.isInitialized ) {
				Logger.getLogger().info("Initializing OptionDialog");
				optionDialog = OptionDialog.getInstance();
				optionDialog.setMainFrame(wordPanel.getMainFrame());
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
		if (e.getSource() == menuItem_Back) {
			switchVisible();
			wordPanel.getMainFrame().backFromDetail();
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
		if (e.getSource() == button_back) {
			switchVisible();
			wordPanel.getMainFrame().backFromDetail();
		}
		if (e.getSource() == button_test) {
			if (sheet.getWordBeanList().size()<5) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.TestValid"));
			} else {
				ui = new CommonUIManager(new TestDialog(sheet));
				CommonUIManager.setUI();
				ui.startUp();
			}
		}
//		if (e.getSource() == button_next&&model_word.size()>0) {
//			if (index < model_word.getSize() - 1) {
//				index++;
//			} else {
//				index = 0;
//			}
//			getData(index, sheet);
//			table.setRowSelectionInterval(index, index);
//			detail_list_word.setSelectedIndex(index);
//			scrollToView();
//		}
//		if (e.getSource() == button_pre) {
//			if (e.getSource() == button_pre&&model_word.size()>0) {
//				if (index > 0) {
//					index--;
//				} else {
//					index = model_word.getSize() - 1;
//				}
//				getData(index, sheet);
//				table.setRowSelectionInterval(index, index);
//				detail_list_word.setSelectedIndex(index);
//				scrollToView();
//			}
//		}

		if (e.getSource() == button_sheet_pre) {
			model_word.clear();
			if (index_date > 0) {
				index_date--;
			} else {
				index_date = max_index_date;
			}
			sheet = WordPanel.getContents().get(index_date);
			getData(0, sheet);
			wordPanel.loadWord(index_date);
			wordPanel.setSheetSelected(index_date);
		}
		if (e.getSource() == button_sheet_next) {
			model_word.clear();
			if (index_date < max_index_date) {
				index_date++;
			} else {
				index_date = 0;
			}
			sheet = WordPanel.getContents().get(index_date);
			index = 0;
			getData(0, sheet);			
			wordPanel.loadWord(index_date);
			wordPanel.setSheetSelected(index_date);
		}
//		if (e.getSource() == detail_list_word) {
//
//			index = detail_list_word.getSelectedIndex();
//
//			detail_list_word.setSelectedIndex(index);
//
//			getData(index, sheet);
//		}		
		if (e.getSource() == pmenuItem_Copy) {
			sentns.copy();
		}
		if (e.getSource() == pmenuItem_Cut) {
			sentns.cut();
		}
		if (e.getSource() == pmenuItem_Paste) {
			sentns.paste();
		}
		if (e.getSource() == pmenuItem_Undo) {
			undoHandler.undo();
		}
		if (e.getSource() == pmenuItem_SelectAll) {
			sentns.selectAll();
		}
		if (e.getSource() == pmenuItem_Delete) {
			sentns.replaceSelection("");
		}
		if (e.getSource() == pmenuItem_lookUpE2C) {
			MainFrame.setECDict();
			MainFrame.getTabbedPane().setSelectedIndex(2);
			MainFrame.getEcDictPanel().clearScreen();
			String str = sentns.getSelectedText();
			MainFrame.getEcDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getEcDictPanel().search(sentns.getSelectedText());
			if(MainFrame.getEcDictPanel().getModel_word().size()!=0){
				if(flag){
					MainFrame.getEcDictPanel().showWord(0);
				}
			}else{
				MainFrame.getEcDictPanel().clearScreen();
				MainFrame.getEcDictPanel().setKeyWord(str);
			}
		}
		if (e.getSource() == pmenuItem_lookUpC2E) {
			MainFrame.setCEDict();
			MainFrame.getTabbedPane().setSelectedIndex(3);
			MainFrame.getCeDictPanel().clearScreen();
			String str = sentns.getSelectedText();
			MainFrame.getCeDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getCeDictPanel().search(sentns.getSelectedText());
			if(MainFrame.getCeDictPanel().getModel_word().size()!=0){
				if(flag){
					MainFrame.getCeDictPanel().showWord(0);
				}
			}else{
				MainFrame.getCeDictPanel().clearScreen();
				MainFrame.getCeDictPanel().setKeyWord(str);
			}
		}
		if (e.getSource() == pmenuItem_lookUpJ2C) {
			MainFrame.setJCDict();
			MainFrame.getTabbedPane().setSelectedIndex(4);
			MainFrame.getJcDictPanel().clearScreen();
			String str = sentns.getSelectedText();
			MainFrame.getJcDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getJcDictPanel().search(sentns.getSelectedText());
			if(MainFrame.getJcDictPanel().getModel_word().size()!=0){
				if(flag){
					MainFrame.getJcDictPanel().showWord(0);
				}
			}else{
				MainFrame.getJcDictPanel().clearScreen();
				MainFrame.getJcDictPanel().setKeyWord(str);
			}
		}
		if (e.getSource() == pmenuItem_lookUpC2J) {
			MainFrame.setCJDict();
			MainFrame.getTabbedPane().setSelectedIndex(5);
			MainFrame.getCjDictPanel().clearScreen();
			String str = sentns.getSelectedText();
			MainFrame.getCjDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getCjDictPanel().search(sentns.getSelectedText());
			if(MainFrame.getCjDictPanel().getModel_word().size()!=0){
				if(flag){
					MainFrame.getCjDictPanel().showWord(0);
				}
			}else{
				MainFrame.getCjDictPanel().clearScreen();
				MainFrame.getCjDictPanel().setKeyWord(str);
			}
		}
		if (e.getSource() == button_update || e.getSource() == menuItem_update) {
			if (model_word.size() == 0) {
				JOptionPane.showMessageDialog(getIns(), SystemConfig.getLanguageProp("Message.OpenFirst"));
			} else {
				if (word_jp.getText().equals("")) {
					JOptionPane
							.showMessageDialog(this, SystemConfig.getLanguageProp("DetailPanel.word")+" "+SystemConfig.getLanguageProp("Message.input"));
				} else if (pron.getText().equals("")) {
					JOptionPane.showMessageDialog(this,
							SystemConfig.getLanguageProp("DetailPanel.pron")+" "+SystemConfig.getLanguageProp("Message.input"));
				} else if (word_cn.getText().equals("")) {
					JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("DetailPanel.chinese")+" "+SystemConfig.getLanguageProp("Message.input"));
				} else {
					int result = JOptionPane.showConfirmDialog(getIns(),
							SystemConfig.getLanguageProp("Message.Update"));

					if (result == 0) {

						int index_sheet = index_date;

						int index_word = detail_list_word.getSelectedIndex();
						
						for(int i=0;i<sheet.getWordBeanList().size();i++){
							System.out.println("word:"+sheet.getWordBeanList().get(i).getWord());
							System.out.println("phisicalNum:"+sheet.getWordBeanList().get(i).getPhysicalNum());
						}

						WordBean bean = new WordBean();

						bean.setWord(word_jp.getText());
						bean.setWord_cn(word_cn.getText());
						bean.setPron(pron.getText());
						bean.setSentns(sentns.getText());
						bean.setPhysicalNum(sheet.getWordBeanList().get(index_word).getPhysicalNum());

						try {
							FileLoader
									.updateWord(index_sheet, index_word, bean);
						} catch (IOException e1) {
							JOptionPane
									.showMessageDialog(getIns(),
											SystemConfig.getLanguageProp("Message.Opened"));
							return;
						}

						sheet.getWordBeanList().set(index_word, bean);

						model_word.set(index_word, bean.getWord());
						
						wordPanel.UpdateTabledata();
						
						table.setRowSelectionInterval(index, index);
						
					}
				}
			}
		}
		if (e.getSource() == button_delete || e.getSource() == menuItem_delete) {

			if (model_word.size() == 0) {
				JOptionPane.showMessageDialog(getIns(), SystemConfig.getLanguageProp("Message.OpenFirst"));
			} else {

				int result = JOptionPane.showConfirmDialog(getIns(),
						SystemConfig.getLanguageProp("Message.Delete"));
				if (result == 0) {

					int index_sheet = index_date;

					int index_word = detail_list_word.getSelectedIndex();

					int phisicalNum = sheet.getWordBeanList().get(
							detail_list_word.getSelectedIndex())
							.getPhysicalNum();

					try {

						FileLoader.deleteWord(index_sheet, index_word,
								phisicalNum, model_word.size(), sheet);
					} catch (Exception ex) {

						JOptionPane.showMessageDialog(getIns(),
								SystemConfig.getLanguageProp("Message.Opened"));
						return;
					}

					model_word.remove(index_word);

					wordPanel.UpdateTabledata();

					if (index_word > 1) {
						table.setRowSelectionInterval(index_word-1, index_word-1);
						getData(index_word - 1, sheet);
						scrollToView();
					} else {
						table.setRowSelectionInterval(0,0);
						getData(0, sheet);
					}
				}
			}
		}
		if (e.getSource() == button_add) {

			wordAddDialog = new WordAddDialog(getIns());

			ui = new CommonUIManager(wordAddDialog);

			ui.startUp();

		}
	}

	public void switchVisible() {
		if (isShowedUp() == false) {
			setShowedUp(true);
//			initDetailPanel();
			this.setVisible(true);
		} else {
			setShowedUp(false);
			this.setVisible(false);
//			model_word.clear(); 
		}
	}
	
	public void scrollToView() {

		if (model_word.size() != 0) {

			int last_visible = detail_list_word.getLastVisibleIndex();

			int first_visible = detail_list_word.getFirstVisibleIndex();
			
			index = detail_list_word.getSelectedIndex();

			Rectangle rect = detail_list_word.getCellBounds(index, index);

			if ((index + 1 > last_visible) || (index - 1 < first_visible)) {

				detail_list_word.scrollRectToVisible(rect);
			}
		}
	}

	public void addMouseEvent() {

		detail_list_word.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				if ((e.getClickCount() == 1) && (e.getButton() == 3)
						&& (null != detail_list_word.getSelectedValue())) {
					pmenuItem_Copy.setEnabled(isCanCopy());
					pmenuItem_Paste.setEnabled(isClipboardString());
					pmenuItem_Cut.setEnabled(isCanCopy());
					pmenuItem_Undo.setEnabled(undoHandler.canUndo());
					pmenuItem_Delete.setEnabled(isCanCopy());
					pMenu.show(detail_list_word, e.getX(), e.getY());
				}
			}
			
			public void mouseReleased(MouseEvent e){
				if ((e.getClickCount() == 1) && (e.getButton() == 1)
						&& (null != detail_list_word.getSelectedValue())
						&& index != detail_list_word.getSelectedIndex()) {

					index = detail_list_word.getSelectedIndex();
					
					table.setRowSelectionInterval(index, index);

					detail_list_word.setSelectedIndex(index);

					getData(index, sheet);

					sentns.setCaretPosition(0);
				}
			}
		});
		
		sentns.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)
						&& (null != detail_list_word.getSelectedValue())) {
					pmenuItem_lookUpE2C.setEnabled(isCanCopy());
					pmenuItem_lookUpC2E.setEnabled(isCanCopy());
					pmenuItem_lookUpJ2C.setEnabled(isCanCopy());
					pmenuItem_lookUpC2J.setEnabled(isCanCopy());
					pmenuItem_Copy.setEnabled(isCanCopy());
					pmenuItem_Paste.setEnabled(isClipboardString());
					pmenuItem_Cut.setEnabled(isCanCopy());
					pmenuItem_Undo.setEnabled(undoHandler.canUndo());
					pmenuItem_Delete.setEnabled(isCanCopy());
					pMenu_Text.show(sentns, e.getX(), e.getY());
				}
			}
		});
	}

	public void addKeyBoardEvent() {

		detail_list_word.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (e.getSource() == detail_list_word) {

					index = detail_list_word.getSelectedIndex();

					table.setRowSelectionInterval(index, index);
					
					detail_list_word.setSelectedIndex(index);

					getData(index, sheet);

					sentns.setCaretPosition(0);
				}
			}
		});
	}

	public void addWordToList(WordBean bean,boolean isAtLast) {

		ArrayList<WordBean> wordlist = sheet.getWordBeanList();

		int index_row = -1;

		int physicalNum = -1;
		
		try {
			if (model_word.size() != 0) {
				if(isAtLast){
					index_row = wordlist.size()-1;
				}else{
					index_row = detail_list_word.getSelectedIndex();
				}
				physicalNum = wordlist.get(index_row).getPhysicalNum();
				FileLoader.addWord(index_date, index_row, physicalNum,
						model_word.size(), sheet, bean);
			} else {
				FileLoader.addWord(index_date, index_row, physicalNum,
						model_word.size(), sheet, bean);
			}
		} catch (Exception e) {
			wordAddDialog.setVisible(false);
			wordAddDialog.dispose();
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(getIns(),
							SystemConfig.getLanguageProp("Message.Opened"));
			return;
		}

		model_word.add(index_row + 1, bean.getWord());

		detail_list_word.setSelectedIndex(index_row + 1);

		word_jp.setText(bean.getWord());

		word_cn.setText(bean.getWord_cn());

		pron.setText(bean.getPron());

		sentns.setText(bean.getSentns());

		wordPanel.UpdateTabledata();
		
		table.setRowSelectionInterval(index_row + 1, index_row + 1);
		
		if(word_jp.isEnabled()==false){
			word_jp.setEnabled(true);
			word_cn.setEnabled(true);
			pron.setEnabled(true);
			sentns.setEnabled(true);
		}
		
		scrollToView();
		
		getData(index, sheet);

		sentns.setCaretPosition(0);
		
	}

	public void updateLink(){
		menuItem_link1.setText(SystemConfig
				.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig
				.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig
				.getSystemProp("link_name3"));
	}
	
	 public boolean isCanCopy() {
			boolean b = false;
			int start = sentns.getSelectionStart();
			int end = sentns.getSelectionEnd();
			if (start != end)
				b = true;
			return b;
		}

		public boolean isClipboardString() {
			boolean b = false;
			Clipboard clipboard = this.getToolkit().getSystemClipboard();
			Transferable content = clipboard.getContents(this);
			try {
				if (content.getTransferData(DataFlavor.stringFlavor) instanceof String)
					b = true;
			} catch (Exception e) {

			}
			return b;
		}
	
	public boolean isFromDetail() {
		return isFromDetail;
	}

	public void setFromDetail(boolean isFromDetail) {
		this.isFromDetail = isFromDetail;
	}
	
	public DetailPanel getIns() {
		return this;
	}

	public void run() {
		initDetailPanel();
	}

	public void setSheet(SheetBean sheet) {
		this.sheet = sheet;
	}

	public void setIndex_Date(int index) {
		this.index_date = index;
	}

	public static boolean isShowedUp() {
		return isShowedUp;
	}

	public static void setShowedUp(boolean isShowedUp) {
		DetailPanel.isShowedUp = isShowedUp;
	}

	public int getMax_index_date() {
		return max_index_date;
	}

	public void setMax_index_date(int max_index_date) {
		this.max_index_date = max_index_date;
	}
}
