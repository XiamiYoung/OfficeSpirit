package com.xiami.frame;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.xiami.Component.CommonComboBox;
import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImageTextArea;
import com.xiami.Component.MainTabbedPane;
import com.xiami.bean.DictWordBean;
import com.xiami.bean.WordBean;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.DictLoader;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.TimeCounter;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.FontManager;

public class JCDictPanel extends JPanel implements Runnable,ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField word_jp;
	private JTextField word_pron;
	private JTextField keyWord;
	private CommonComboBox keyWordBox;
	private ImageTextArea sentns;
	private CommonScrollPane pane_sentns;
	private CommonScrollPane pane_list;
	private JPanel wordPanel;
	private ImageButton button_confirm;
	private ImageButton button_addWord;
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_file = new JMenu();
	private JMenu menu_option = new JMenu();
	private JMenu menu_help = new JMenu();
	private JMenu menu_link = new JMenu();
	private JMenuItem menuItem_link_search = new JMenuItem();
	private JMenuItem menuItem_link1 = new JMenuItem();
	private JMenuItem menuItem_link2 = new JMenuItem();
	private JMenuItem menuItem_link3 = new JMenuItem();
	private JMenuItem menuItem_exit = new JMenuItem();
	private JMenuItem menuItem_Preferences = new JMenuItem();
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();	
	private JPopupMenu pMenu_Text = new JPopupMenu();
	private JMenuItem pmenuItem_lookUpChinese = new JMenuItem();
	private JMenuItem pmenuItem_lookUpJapanese = new JMenuItem();
	private JMenuItem pmenuItem_Copy = new JMenuItem();
	private JMenuItem pmenuItem_SelectAll = new JMenuItem();
	private JPopupMenu pMenu_list = new JPopupMenu();
	private JMenuItem pmenuItem_addWord = new JMenuItem();
	private JPopupMenu pMenu_word = new JPopupMenu();
	private JMenuItem pmenuItemWord_copy = new JMenuItem();
	private JMenuItem pmenuItemWord_paste = new JMenuItem();
	private JLabel label_word;
	private JLabel label_pron;
	private DefaultListModel model_word = new DefaultListModel();
	private JList showList = new JList(model_word);
	
	private ArrayList<DictWordBean> wordList = new ArrayList<DictWordBean>();
	private String lastSearch = "";
	private static final int MAX_SHOWED  = Integer.parseInt(SystemConfig.getSystemProp(Keys.MAX_SHOWED));
	public static boolean isInitialized = false;
	
	private CommonUIManager ui;
	private MainFrame mainFrame;
	private MainTabbedPane tabbedPane;
	private OptionDialog optionDialog;
	private DictLoader loader;

	public JCDictPanel(MainFrame obj,MainTabbedPane tabbedPane) {
		this.mainFrame = obj;
		this.tabbedPane = tabbedPane;
	}

	public void initDictPanel() {
		
		if (!isInitialized) {
			
			createComponents();
			
			this.add(pane_list);
			this.add(label_word);		
			this.add(label_pron);		
			this.add(word_jp);
			this.add(word_pron);
			this.add(pane_sentns);
			this.add(wordPanel);
			this.add(button_confirm);
			this.add(button_addWord);
			wordPanel.add(keyWordBox);
			
			this.add(menubar);
			menubar.add(menu_file);
			menubar.add(menu_option);
			menubar.add(menu_link);
			menubar.add(menu_help);
			
			pMenu_Text.add(pmenuItem_lookUpJapanese);
			pMenu_Text.add(pmenuItem_lookUpChinese);
			pMenu_Text.addSeparator();
			pMenu_Text.add(pmenuItem_Copy);
			pMenu_Text.add(pmenuItem_SelectAll);
			
			pMenu_list.add(pmenuItem_addWord);
			
			pMenu_word.add(pmenuItemWord_copy);
			pMenu_word.add(pmenuItemWord_paste);
			
			menu_link.add(menuItem_link_search);
			menu_link.addSeparator();
			menu_link.add(menuItem_link1);
			menu_link.add(menuItem_link2);
			menu_link.add(menuItem_link3);
			menu_option.add(menuItem_Preferences);
			menu_file.add(menuItem_exit);	
			menu_help.add(menuItem_Statis);
			menu_help.add(menuItem_About);
			menuItem_Preferences.addActionListener(this);
			menuItem_About.addActionListener(this);
			menuItem_exit.addActionListener(this);
			menuItem_Statis.addActionListener(this);
			menuItem_link_search.addActionListener(this);
			menuItem_link1.addActionListener(this);
			menuItem_link2.addActionListener(this);
			menuItem_link3.addActionListener(this);
			pmenuItem_lookUpChinese.addActionListener(this);
			pmenuItem_lookUpJapanese.addActionListener(this);
			pmenuItem_Copy.addActionListener(this);
			pmenuItem_SelectAll.addActionListener(this);
			pmenuItem_addWord.addActionListener(this);
			pmenuItemWord_copy.addActionListener(this);
			pmenuItemWord_paste.addActionListener(this);
			
			button_confirm.registerKeyboardAction(getIns(), 
		            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), 
		            JComponent.WHEN_IN_FOCUSED_WINDOW);
			
			button_addWord.registerKeyboardAction(getIns(), 
		            KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK), 
		            JComponent.WHEN_IN_FOCUSED_WINDOW);
			
			button_addWord.setToolTipText("Add thid word to excel.");
			
			repaint();			
			setting();		
			new UndoHandler(keyWord);			
		}

		isInitialized = true;
		
	}

	public void createComponents() {
		wordPanel = new JPanel();
		button_confirm = new ImageButton("", 0, 0, 0, 0, getIns());
		keyWordBox = new CommonComboBox();
		keyWord = (JTextField) keyWordBox.getEditor().getEditorComponent();
		pane_list = new CommonScrollPane(showList);
		label_word = new JLabel();
		label_pron = new JLabel();
		word_jp = new CommonTextField(330, 60, 540, 30);
		word_pron = new CommonTextField(330, 100, 400, 30);
		button_addWord = new ImageButton("", 750, 100, 120, 30, getIns());
		sentns = new ImageTextArea();
		pane_sentns = new CommonScrollPane(sentns);

		addMouseEvent();
		addKeyBoardEvent();
		addDocumentEvent();
	}
	
	public void setting() {

		setLanguage();
		
		pane_list.setBounds(5, 130, 235, 505);
		label_word.setBounds(250, 60, 95, 30);
		label_pron.setBounds(250, 100, 95, 30);
		menubar.setBounds(1, 18, 895, 25);
		menubar.setBorderPainted(true);
		menubar.setBorder(BorderFactory.createBevelBorder(0));	
		pane_sentns.setBounds(250, 140, 630, 495);
		wordPanel.setBounds(5, 50, 230, 70);
		keyWordBox.setBounds(10, 25, 210, 30);
		
		keyWordBox.setEditable(true);
		
		sentns.setEditable(false);
		word_jp.setEditable(false);
		word_pron.setEditable(false);
		
		keyWordBox.setMaximumRowCount(10);
		keyWordBox.getInputContext().selectInputMethod(Locale.JAPAN);
		
		Cursor corsor = new Cursor(Cursor.HAND_CURSOR);
		menuItem_link_search.setCursor(corsor);
		menuItem_link1.setCursor(corsor);
		menuItem_link2.setCursor(corsor);
		menuItem_link3.setCursor(corsor);
		
		showList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.setLayout(null);
		wordPanel.setLayout(null);
	}
	
	public void setLanguage(){
		label_word.setText(SystemConfig.getLanguageProp("DetailPanel.word"));
		label_pron.setText(SystemConfig.getLanguageProp("DetailPanel.pron"));
		button_addWord.setText(SystemConfig.getLanguageProp("DictPanel.addWord"));
		menu_file.setText(SystemConfig.getLanguageProp("DetailPanel.menu.file"));
		menu_option.setText(SystemConfig
		 .getLanguageProp("DetailPanel.menu.option"));
		menu_help.setText(SystemConfig.getLanguageProp("DetailPanel.menu.help"));
		menu_link.setText(SystemConfig.getLanguageProp("Link.menu"));
		menuItem_Preferences.setText(SystemConfig
		 .getLanguageProp("DetailPanel.menuitem.preferences"));
		menuItem_About.setText(SystemConfig
				.getLanguageProp("DetailPanel.menuitem.about"));
		menuItem_exit.setText(SystemConfig
				.getLanguageProp("NoticePanel.menuitem.exit"));
		menuItem_link_search.setText(SystemConfig
				.getLanguageProp("Link.search"));
		menuItem_link1.setText(SystemConfig
				.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig
				.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig
				.getSystemProp("link_name3"));
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		pmenuItem_Copy.setText(SystemConfig.getLanguageProp("MaxText.copy"));
		pmenuItem_lookUpChinese.setText(SystemConfig.getLanguageProp("DictPanel.C2J"));
		pmenuItem_lookUpJapanese.setText(SystemConfig.getLanguageProp("DictPanel.J2C"));
		pmenuItem_SelectAll.setText(SystemConfig.getLanguageProp("MaxText.all"));
		pmenuItem_addWord.setText(SystemConfig.getLanguageProp("DictPanel.addWord"));
		pmenuItemWord_copy.setText(SystemConfig.getLanguageProp("MaxText.copy"));
		pmenuItemWord_paste.setText(SystemConfig.getLanguageProp("MaxText.paste"));
		
		Font font = FontManager.getFont(16);
		label_word.setFont(font);
		label_pron.setFont(font);
		
		Font font_menu = FontManager.getFont(14);
		menu_file.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_option.setFont(font_menu);
		menu_link.setFont(font_menu);
		button_addWord.setFont(font_menu);
		
		TitledBorder fileBorder = new TitledBorder(SystemConfig.getLanguageProp("DictPanel.J2C"));
		fileBorder.setTitleFont(FontManager.getFont(16));
		fileBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		fileBorder.setBorder(BorderFactory.createEtchedBorder());
		wordPanel.setBorder(fileBorder);
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
		if (e.getSource() == menuItem_exit) {
			SystemConfig.setSystemProp(Keys.TOTAL_TIME, String
					.valueOf(TimeCounter.getTotal_time()));
			System.exit(0);
		}
		if (e.getSource() == menuItem_Preferences) {
			if (!OptionDialog.isInitialized ) {
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

		if (e.getSource() == pmenuItem_Copy) {
			sentns.copy();
		}
		if (e.getSource() == pmenuItem_SelectAll) {
			sentns.selectAll();
		}
		if (e.getSource() == pmenuItemWord_copy) {
			setClipBoardContents(keyWord.getSelectedText());
		}
		if (e.getSource() == pmenuItemWord_paste) {
			if(isClipboardString()){
				if(isClipboardString()){
					if(keyWord.getSelectedText()!=null){
						keyWord.setText(keyWord.getText().replace(
								keyWord.getSelectedText(), getClipBoardContents()));
					} else {
						int i = keyWord.getCaretPosition();
						if(i==0){
							keyWord.setText(getClipBoardContents()+keyWord.getText());
						}else if(i==keyWord.getText().length()){
							keyWord.setText(keyWord.getText()+ getClipBoardContents());
						}else{
							String s= keyWord.getText();
							keyWord.setText(s.substring(0,i)+ getClipBoardContents()+s.substring(i, s.length()));
						}
					}
				}
				search(keyWord.getText());
			}
		}
		if (e.getSource() == pmenuItem_lookUpChinese) {
			MainFrame.setCJDict();
			tabbedPane.setSelectedIndex(5);
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
		if (e.getSource() == pmenuItem_lookUpJapanese) {
			String str = sentns.getSelectedText();
			this.setKeyWord(str);
			boolean flag = this.search(sentns.getSelectedText());
			if(this.getModel_word().size()!=0){
				if(flag){
					this.showWord(0);
				}
			}else{
				this.clearScreen();
			}
			this.setKeyWord(str);
		}
		if (e.getSource() == button_addWord || e.getSource() == pmenuItem_addWord) {
			if (showList.getSelectedIndex() != -1) {
				if (WordPanel.getContents() == null
						|| WordPanel.getContents().size() == 0) {
					JOptionPane.showMessageDialog(this, SystemConfig
							.getLanguageProp("WordPanel.sheet")
							+ " "
							+ SystemConfig.getLanguageProp("Message.NotExist"));
				} else {
					showWord(showList.getSelectedIndex());

					WordBean wordBean = new WordBean();
					wordBean.setWord(word_jp.getText());
					wordBean.setPron(word_pron.getText());
					wordBean.setSentns(sentns.getText());

					ui = new CommonUIManager(new SheetSelector(MainFrame
							.getWordPanel(), wordBean));
					CommonUIManager.setUI();
					ui.startUp();
				}
			} else {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			}
		}
		if (e.getSource() == button_confirm) {
			if(model_word.size()!=0){
				showWord(showList.getSelectedIndex());
			}
		}
	}

	public void switchVisible() {
		if (isVisible() == false) {
			this.setVisible(true);
		} else {
			this.setVisible(false);
		}
	}

	public void addMouseEvent() {

		showList.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e){
				if ((e.getClickCount() == 1) && (e.getButton() == 1)
						&& (null != showList.getSelectedValue())) {
					showWord(showList.getSelectedIndex());
				}
				
				if ((e.getClickCount() == 1) && (e.getButton() == 3)
						&& (null != showList.getSelectedValue())) {
					pMenu_list.show(showList, e.getX(), e.getY());
				}
			}
		});
		
		keyWord.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pmenuItemWord_copy.setEnabled(isKeyWordCanCopy());
					pmenuItemWord_paste.setEnabled(isClipboardString());
					pMenu_word.show(keyWord, e.getX(), e.getY());
				}
			}
		});
		
		sentns.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)
						&& (model_word.size()>0)) {
					pmenuItem_Copy.setEnabled(isCanCopy());
					pMenu_Text.show(sentns, e.getX(), e.getY());
				}
			}
		});
	}
	public void addKeyBoardEvent() {
		keyWord.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (model_word.size() != 0) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						showList.setSelectedIndex(0);
						showWord(0);
					}
				}
			}
		});
		showList.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getSource() == showList) {					
					showWord(showList.getSelectedIndex());	
					if((showList.getSelectedIndex()==model_word.size()-1)&&showList.getSelectedIndex()<wordList.size()-1){
						int j = showList.getSelectedIndex()+1;
						model_word.addElement(wordList.get(j).getWord());
						showList.setSelectedIndex(model_word.getSize()-1);
						scrollToView();
					}
				}
			}
		});
	}
	
	public void addDocumentEvent() {
		keyWord.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				searchWord();
			}

			public void removeUpdate(DocumentEvent e) {
				searchWord();
			}

			public void changedUpdate(DocumentEvent e) {
				searchWord();
			}
		});
	}
	
	public void searchWord(){
		String keyWordString = keyWord.getText();
		if(lastSearch.equals(keyWordString)){
			return;
		}
		search(keyWordString);
		if (model_word.size() != 0) {
			if(model_word.getElementAt(0).toString().equals(
					keyWordString)) {
				showList.setSelectedIndex(0);
			}
		}
	}
	
	public boolean search(String word){
		if(!word.trim().equals("")&&isInputValid(word)){
//			long start = System.currentTimeMillis();
			boolean flag = filterWord(word);
			lastSearch  = word;
//			long end = System.currentTimeMillis();
//			System.out.println("takes "+(end-start));
			return flag;
		}else{
			return false;
		}
	}
	
	public void setKeyWord(String word){
		keyWord.setText(word);
	}
	
	public ArrayList<DictWordBean> getWordList(String keyWord){
		ArrayList<DictWordBean> beanList = new ArrayList<DictWordBean>();
		try {
			beanList = loader.searchForJ2C(keyWord);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error was found when searching word "+keyWord);
		}
		return beanList;
	}
	
	public ArrayList<DictWordBean> getFutherWordList(String keyWord,int limit,boolean isNotFound){
		ArrayList<DictWordBean> beanList = new ArrayList<DictWordBean>();
		try {
			beanList = loader.futherSearchForJ2C(keyWord,limit,isNotFound);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error was found when searching word "+keyWord);
			e.printStackTrace();
		}
		return beanList;
	}
	
	public boolean filterWord(String keyWord){	
			boolean isNeedClear = true;
			ArrayList<DictWordBean> tempList = getWordList(keyWord.toLowerCase());
		int limit = tempList.size() < MAX_SHOWED ? tempList.size() : MAX_SHOWED;
		if (tempList.size() > 0) {
			model_word.clear();
			wordList.clear();
			isNeedClear = false;
			for (int i = 0; i < limit; i++) {
				wordList.add(tempList.get(i));
				model_word.addElement(tempList.get(i).getWord());
			}
			if (model_word.size() < MAX_SHOWED && keyWord.length() > 0) {
				futherFilterWord(keyWord, isNeedClear, MAX_SHOWED
						- model_word.size(), false);
			}
			if (model_word.size() > 0) {
				if(keyWord.equals(model_word.getElementAt(0).toString())){
					showList.setSelectedIndex(0);
					scrollToView();
				}
			}
		} else {
			futherFilterWord(keyWord, isNeedClear, MAX_SHOWED
					- model_word.size(), false);
		}
		if (model_word.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public void futherFilterWord(String keyWord,boolean isNeedClear,int limit,boolean isNotFound){	
		ArrayList<DictWordBean> futherList = getFutherWordList(keyWord,limit,isNotFound);
		if(isNeedClear&&futherList.size()>0){
			model_word.clear();
			wordList.clear();
			isNeedClear = false;
		}
		for (int j = 0; j < futherList.size(); j++) {
				wordList.add(futherList.get(j));
				model_word
						.addElement(futherList.get(j).getPron() == null ? futherList
								.get(j).getWord()
								: futherList.get(j).getWord() + "("
										+ futherList.get(j).getPron() + ")");
		}
		if (model_word.size() < MAX_SHOWED && keyWord.length() > 1) {
			futherFilterWord(keyWord.substring(0,keyWord.length()-1),isNeedClear,MAX_SHOWED - model_word.size(),false);
		}
		else if(model_word.size() == MAX_SHOWED && keyWord.length() > 1&&futherList.size()==0){
			futherFilterWord(keyWord.substring(0, keyWord.length()-1),
					isNeedClear,MAX_SHOWED,true);
		}
	}
	
	public void showWord(int index) {
		DictWordBean bean = wordList.get(index);
		StringBuffer sb = new StringBuffer();
		int elementCount = 1;
		int exp_count = 1;
		boolean isStart = true;
		boolean isMultiExp = isMultiExp(bean);
		boolean isMultiElement = isMultiElement(bean);
		String str = "";
		for (int i = 0; i < bean.getSents().size(); i++) {
			if (wordList.get(index).getSents().get(i).startsWith(Keys.WORD_ELEMENT)
					|| wordList.get(index).getSents().get(i).startsWith(Keys.PROTYPE)
					|| wordList.get(index).getSents().get(i).startsWith(Keys.TYPE)
					|| wordList.get(index).getSents().get(i).startsWith(Keys.SPLIT)){
				if (isMultiElement&&wordList.get(index).getSents().get(i).startsWith(Keys.WORD_ELEMENT)&&isStart) {
					str = wordList.get(index).getSents().get(i).replaceAll(Keys.WORD_ELEMENT, "〖"+elementCount+++"〗") + "\n";
					exp_count = 1;
					sb.append(str);
					isStart = false;
				}else if(isMultiElement&&wordList.get(index).getSents().get(i).startsWith(Keys.WORD_ELEMENT)&&!isStart){
					str = "\n"+wordList.get(index).getSents().get(i).replaceAll(Keys.WORD_ELEMENT, "〖"+elementCount+++"〗") + "\n";
					exp_count = 1;
					sb.append(str);
				}else {
//					if(wordList.get(index).getSents().get(i).startsWith(Keys.PROTYPE)){
//						if(isMultiElement){
//							str="";
//						}
//						str += wordList.get(index).getSents().get(i).replaceAll(Keys.PROTYPE, "单词:") + "\n";
//					}
//					else 
						if (wordList.get(index).getSents().get(i)
							.startsWith(Keys.TYPE)) {
						str = "\n"+wordList.get(index).getSents().get(i).replaceAll(
								Keys.TYPE, "词性:")
								+ "\n";
					} else if (wordList.get(index).getSents().get(i).startsWith(Keys.SPLIT)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.SPLIT, "音节分段:") + "\n";
					}
					sb.append(str);
				}
			} else {
					if(wordList.get(index).getSents().get(i).startsWith(Keys.HABI_PROTYPE)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.HABI_PROTYPE, "惯用型:") + "\n";
						str="\n         " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.HABI_EXP)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.HABI_EXP, "") + "\n";
						str="\n                      " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.REF)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.REF, "另见:") + "\n";
						str="\n         " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.J_PRON)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.J_PRON, "日文发音:").replaceAll("</Jpron>", "") + "\n";
						str="\n" + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.PRE_EXP)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.PRE_EXP, "词性变化:") + "\n\n";
						str="\n         " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.FL_EXP)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.FL_EXP, "        ") + "\n";
						if(isMultiExp){
							str="                " + str;
						}else{
							str="      " + str;
						}
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.SENTS_PROTYPE)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.SENTS_PROTYPE, "☆例句:") + "\n";
						str="\n         " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.SENTS_EXP)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.SENTS_EXP, "          ") + "\n";
						str="            " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.EXP)){
						if(isMultiExp){
							str = wordList.get(index).getSents().get(i).replaceAll(Keys.EXP, "★【"+exp_count+++"】:") + "\n";
						}else{
							str = wordList.get(index).getSents().get(i).replaceAll(Keys.EXP, "★:") + "\n";
						}
						str="\n         " + str;
						sb.append(str);
					}else if(wordList.get(index).getSents().get(i).startsWith(Keys.SAME_MEANING)){
						str = wordList.get(index).getSents().get(i).replaceAll(Keys.SAME_MEANING, "同义词:") + "\n";
						str="\n         " + str;
						sb.append(str);
				}
			}
		}
		
		String pron = bean.getPron();

		if(pron==null || pron.equals("")){
			pron = bean.getWord();
		}
		
//		keyWord.setText(bean.getWord());
		word_jp.setText(bean.getWord());
		word_pron.setText(pron);
		sentns.setText(sb.toString());
		sentns.setCaretPosition(0);
		
		String word = keyWord.getText();
		if (!word.equals("")&&!isHistoryContained(word)) {
			addWordHistory(word);
		}
	}
	
	public boolean isMultiElement(DictWordBean bean){
		int count = 0;
		for(int i=0;i<bean.getSents().size();i++){
			if(bean.getSents().get(i).startsWith(Keys.WORD_ELEMENT)){
				count++;
				if(count==2){
					return true;
				}
			}
		}
		if(count <= 1){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean isMultiExp(DictWordBean bean){
		int count = 0;
		for(int i=0;i<bean.getSents().size();i++){
			if(bean.getSents().get(i).startsWith(Keys.EXP)){
				count++;
				if(count==2){
					return true;
				}
			}
		}
		if(count <= 1){
			return false;
		}else{
			return true;
		}
	}
	
	public void clearScreen(){
		keyWord.setText("");
		word_jp.setText("");
		word_pron.setText("");
		sentns.setText("");
		sentns.setCaretPosition(0);
		model_word.clear();
	}
	
	private void scrollToView() {

		int index = -1;
		
		if (model_word.size() != 0) {

			int last_visible = showList.getLastVisibleIndex();

			int first_visible = showList.getFirstVisibleIndex();
			
			index = showList.getSelectedIndex();

			int x = 0;
			
			Rectangle rect = null;
			if ((index + 1 > last_visible)) {
				x = last_visible - first_visible;		
				if(index+x>wordList.size()){
					x=0;
				}
			}else if(index - 1 < first_visible){
				x = 0;
			}
			else{		
				x = last_visible - first_visible;
			}
			rect = showList.getCellBounds(index + x, index + x);
			showList.scrollRectToVisible(rect);
		}
	}
	
	public JCDictPanel getIns() {
		return this;
	}

	public void run() {		
		try {
			initDictPanel();
			loader = new DictLoader(3);
		} catch (Exception e) {
			JOptionPane
			.showMessageDialog(getIns(),"Error found when initiating JC Dict.");
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
	
	 public boolean isCanCopy() {
			boolean b = false;
			int start = sentns.getSelectionStart();
			int end = sentns.getSelectionEnd();
			if (start != end)
				b = true;
			return b;
		}
	 
	 //japanese ascii range 12354(あ) - 12435(ん)  　
	 //japanese ascii range 12450(ア) - 12531(ン)   12532(ヵ) 12533(ヶ) 12534(ヴ)
	 //中文的范围:\u4e00 - \u9fa5, 日文在\u0800 - \u4e00, 韩文为\xAC00-\xD7A3
	 //不允许内容：数字，英文字母，日文字母，所有标点符号
	 public boolean isInputValid(String word) {
		for (int i = 0; i < word.length(); i++) {
				if ((word.charAt(i) >= 32 && word.charAt(i) <= 127)
						||(word.charAt(i) >= 12288 && word.charAt(i) <= 12309)
						|| (word.charAt(i) >= 12539 && word.charAt(i) <= 12542)
						|| (word.charAt(i) >= 8216 && word.charAt(i) <= 8217)
						|| (word.charAt(i) >= 65281 && word.charAt(i) <= 65381)) {
					return false;
			}
		}
		return true;
	}
	 
	 public boolean isKeyWordCanCopy() {
		boolean b = false;
		int start = keyWord.getSelectionStart();
		int end = keyWord.getSelectionEnd();
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

	public static void setClipBoardContents(String content) {
		try {

			StringSelection stringSelection = new StringSelection(content);
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getClipBoardContents() {
		String text = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				text = (String) contents
						.getTransferData(DataFlavor.stringFlavor);

				return text;
			} catch (UnsupportedFlavorException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	private boolean isHistoryContained(String keyWord){
		for(int i=0;i<keyWordBox.getItemCount();i++){
			if(keyWordBox.getItemAt(i).equals(keyWord)){
				return true;
			}
		}
		return false;
	}
	
	private void addWordHistory(String keyWord) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(keyWord);
		if (keyWordBox.getItemCount() == keyWordBox.getMaximumRowCount()) {
			keyWordBox.removeItemAt(keyWordBox.getMaximumRowCount() - 1);
		}
		for (int i = 0; i < keyWordBox.getItemCount(); i++) {
			list.add(keyWordBox.getModel().getElementAt(i).toString());
		}
		keyWordBox.removeAllItems();
		for (int i = 0; i < list.size(); i++) {
			keyWordBox.addItem(list.get(i));
		}
	}
	
	public DefaultListModel getModel_word() {
		return model_word;
	}

	public void setModel_word(DefaultListModel model_word) {
		this.model_word = model_word;
	}

}
