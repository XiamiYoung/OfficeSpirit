package com.xiami.frame;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.PhoneticUtil;
import com.xiami.core.SystemConfig;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.TimeCounter;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.FontManager;

@SuppressWarnings("serial")
public class PhoneticPanel extends ImagePanel implements Runnable,
		ActionListener{

	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_file = new JMenu();
	private JMenu menu_option = new JMenu();
	private JMenu menu_help = new JMenu();
	private JMenu menu_link = new JMenu();
	private JMenuItem menuItem_link_search = new JMenuItem();
	private JMenuItem menuItem_link1 = new JMenuItem();
	private JMenuItem menuItem_link2 = new JMenuItem();
	private JMenuItem menuItem_link3 = new JMenuItem();
	private JMenuItem menuItem_Exit = new JMenuItem();
	private JMenuItem menuItem_Preferences = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JPopupMenu pMenu = new JPopupMenu();
	private JMenuItem pmenuItem_lookUpJ2C = new JMenuItem();
	private JCheckBox check_katakana;
	private JCheckBox check_hirakana;
	private JLabel label_katakana;
	private JLabel label_hiragana;
	private ButtonGroup bg_check = new ButtonGroup();
	private JSplitPane splitPane;
	private ImageButton button_confirm;
	private ImageTextArea textArea_sentens;
	private JEditorPane textPane_phonetic;
	private CommonScrollPane pane_sentens;
	private CommonScrollPane pane_phonetic;
	private SimpleAttributeSet attr;
	private JTextComponent source;

	private CommonUIManager ui;
	public static boolean isInitialzed = false;
	private boolean isUp = true;

	private MainFrame mainFrame;
	private OptionDialog optionDialog;

	public PhoneticPanel(MainFrame obj) {
		mainFrame = obj;
	}

	public void init() {	
		
		createComponent();
		addComponent();
		setting();
		
		addMouseEvent();
		
		isInitialzed = true;
		this.repaint();

		checkIsUsable();
	}

	public void createComponent() {
	
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_NOTICEPANEL-130, 46, 115, 35,
				getIns());
		textArea_sentens = new ImageTextArea();
		textPane_phonetic = new JEditorPane();
		pane_sentens = new CommonScrollPane("", textArea_sentens);
		pane_phonetic = new CommonScrollPane("", textPane_phonetic);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,false,pane_sentens,pane_phonetic);
		check_katakana = new JCheckBox();
		check_hirakana = new JCheckBox();
		bg_check.add(check_katakana);
		bg_check.add(check_hirakana);
		label_katakana = new JLabel();
		label_hiragana = new JLabel();
		
	}
	
	public void addComponent() {

		this.add(menubar);
		this.add(splitPane);
		this.add(button_confirm);
		this.add(label_hiragana);
		this.add(label_katakana);
		this.add(check_hirakana);
		this.add(check_katakana);
		
		menubar.add(menu_file);
		menubar.add(menu_option);
		menubar.add(menu_link);
		menubar.add(menu_help);
		
		pMenu.add(pmenuItem_lookUpJ2C);
		
		menu_file.addSeparator();
		menu_file.add(menuItem_Exit);
		menu_option.add(menuItem_Preferences);
		menu_help.add(menuItem_Statis);
		menu_help.add(menuItem_About);		
		
		menu_link.add(menuItem_link_search);
		menu_link.addSeparator();
		menu_link.add(menuItem_link1);
		menu_link.add(menuItem_link2);
		menu_link.add(menuItem_link3);

		menuItem_Statis.addActionListener(this);
		menuItem_Exit.addActionListener(this);
		menuItem_Preferences.addActionListener(this);
		menuItem_About.addActionListener(this);
		menuItem_link_search.addActionListener(this);
		menuItem_link1.addActionListener(this);
		menuItem_link2.addActionListener(this);
		menuItem_link3.addActionListener(this);
		pmenuItem_lookUpJ2C.addActionListener(this);

	}

	public void setting() {

		setLanguage();

		this.setLayout(null);
		
		menubar.setBounds(1, 18, Keys.INITIAL_WIDTH_NOTICEPANEL-5, 25);
		menubar.setBorderPainted(true);
		menubar.setBorder(BorderFactory.createBevelBorder(0));
		
		splitPane.setBounds(5, 85, Keys.INITIAL_WIDTH_NOTICEPANEL - 10, Keys.INITIAL_HEIGHT_NOTICEPANEL - 95);
		splitPane.setDividerLocation(0.5);
		splitPane.setOneTouchExpandable(true);
	    splitPane.setDividerSize(10);
	    
	    label_hiragana.setBounds(10, 50, 80, 25);		
	    label_katakana.setBounds(110, 50, 80, 25);		
	   
		check_hirakana.setBounds(75, 50, 25, 25);
		check_katakana.setBounds(175, 50, 25, 25);
		
		check_hirakana.setSelected(true);
		
		attr = new SimpleAttributeSet();
	    
		textPane_phonetic.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,16));
	    
		textArea_sentens.setText(SystemConfig
				.getLanguageProp("PhoneticPanel.input"));
		
		Cursor corsor = new Cursor(Cursor.HAND_CURSOR);
		menuItem_link_search.setCursor(corsor);
		menuItem_link1.setCursor(corsor);
		menuItem_link2.setCursor(corsor);
		menuItem_link3.setCursor(corsor);
		
		button_confirm.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		new UndoHandler(textArea_sentens);	
		new UndoHandler(textPane_phonetic);	
		
		this.setVisible(true);
		
	}
	
	public void setLanguage() {

		button_confirm.setText(SystemConfig
				.getLanguageProp("PhoneticPanel.confim"));
		label_hiragana.setText(SystemConfig
				.getLanguageProp("PhoneticPanel.hiragana"));
		label_katakana.setText(SystemConfig
				.getLanguageProp("PhoneticPanel.katakana"));
		menu_option.setText(SystemConfig
				.getLanguageProp("NoticePanel.menu.option"));
		menu_file
				.setText(SystemConfig.getLanguageProp("NoticePanel.menu.file"));
		menu_help
				.setText(SystemConfig.getLanguageProp("NoticePanel.menu.help"));
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		menu_link.setText(SystemConfig.getLanguageProp("Link.menu"));
		
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
		pmenuItem_lookUpJ2C.setText(SystemConfig.getLanguageProp("DictPanel.J2C"));
		
		Font font = FontManager.getFont(16);
		
		Insets insets = new Insets(0,0,0,0);
		
		button_confirm.setFont(font);
		button_confirm.setMargin(insets);
		label_hiragana.setFont(font);
		label_katakana.setFont(font);
		
		Font font_menu = FontManager.getFont(14);
		menu_file.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_option.setFont(font_menu);
		menu_link.setFont(font_menu);

	}

	public void addMouseEvent(){
		Component divider = getSplitDivider(splitPane);
		divider.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (splitPane.getDividerLocation()==0.0) {
						splitPane.setDividerLocation(0.5);
					}else if (splitPane.getDividerLocation()==1.0) {
						splitPane.setDividerLocation(0.5);
					}else{
						if(isUp){
							System.out.println(isUp);
							splitPane.setDividerLocation(0.0);
							isUp = false;
						}else{
							System.out.println(isUp);
							splitPane.setDividerLocation(1.0);
							isUp = true;
						}
					}
				}
			}
		});
		
		textArea_sentens.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pmenuItem_lookUpJ2C.setEnabled(isCanCopy(textArea_sentens));
					getIns().setSource(textArea_sentens);
					pMenu.show(textArea_sentens, e.getX(), e.getY());
				}
			}
		});
		
		textPane_phonetic.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pmenuItem_lookUpJ2C.setEnabled(isCanCopy(textPane_phonetic));
					getIns().setSource(textPane_phonetic);
					pMenu.show(textPane_phonetic, e.getX(), e.getY());
				}
			}
		});
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
		if (e.getSource() == pmenuItem_lookUpJ2C) {
			searchForJ2c(this.getSource());
		}
		if (e.getSource() == button_confirm) {
			if (textArea_sentens.getText().length() > 50000) {
				JOptionPane.showMessageDialog(getIns(), SystemConfig
						.getLanguageProp("Message.Toolong"));
				return;
			}else if(textArea_sentens.getText().trim().length()==0){
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("Message.content")
					+" "+SystemConfig.getLanguageProp("Message.input"));
				return;
			}
			getPhonetic();
		}
	}

	public void run() {
		init();
	}

	public void searchForJ2c(JTextComponent component){
		MainFrame.setJCDict();
		MainFrame.getTabbedPane().setSelectedIndex(4);
		MainFrame.getJcDictPanel().clearScreen();
		String str = component.getSelectedText();
		MainFrame.getJcDictPanel().setKeyWord(str);
		boolean flag = MainFrame.getJcDictPanel().search(component.getSelectedText());
		if(MainFrame.getJcDictPanel().getModel_word().size()!=0){
			if(flag){
				MainFrame.getJcDictPanel().showWord(0);
			}
		}else{
			MainFrame.getJcDictPanel().clearScreen();
			MainFrame.getJcDictPanel().setKeyWord(str);
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
	
	private static Component getSplitDivider(JSplitPane splitPane) {
		Component top = splitPane.getTopComponent();
		Component bottom = splitPane.getBottomComponent();
		for (int i = 0; i < splitPane.getComponentCount(); i++) {
			Component component = splitPane.getComponent(i);
			if (component != top && component != bottom) {
				return component;
			}
		}
		return null;
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

	public PhoneticPanel getIns() {
		return this;
	}
	
	public void getPhonetic(){
		
		new Thread() {
			public void run() {
				
				PhoneticUtil.initActiveXComponent();
				
				int type = -1;
				if (check_hirakana.isSelected()) {
					type = 1;
				} else {
					type = 0;
				}
				try {
					textPane_phonetic.setText("");
					Document doc = textPane_phonetic.getDocument();
					int index = -1;
					int start = 0;
					String str = textArea_sentens.getText().trim();
					if ((!String.valueOf(str.charAt(str.length() - 1))
							.matches("\\pP")&&!String.valueOf(str.charAt(str.length() - 1))
							.matches("\n"))) {
//						str = str.substring(0, str.length() - 1);
						str += "。";
					}
					int length = str.length();
					for (int i = 0; i < length; i++) {
						index++;
						if (String.valueOf(str.charAt(i)).matches("\\pP")
								&&!String.valueOf(str.charAt(i)).matches("／")){
							doc.insertString(doc.getLength(), PhoneticUtil
									.getPhonetic(str
											.substring(start, index), type)
									+ str.charAt(i), attr);
							start = index + 1;
						}
					}
					PhoneticUtil.quit();
					textArea_sentens.setCaretPosition(0);
					textPane_phonetic.setCaretPosition(0);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(getIns(), SystemConfig
							.getLanguageProp("PhoneticPanel.install"));
				}
			}
		}.start();
	}
	
	public void checkIsUsable(){
		try {
			PhoneticUtil.initActiveXComponent();
			boolean flag = PhoneticUtil.getLanguageSetting();
			if(flag == false){
				JOptionPane.showMessageDialog(getIns(), SystemConfig
						.getLanguageProp("PhoneticPanel.check"));
				button_confirm.setEnabled(false);
			}
			PhoneticUtil.quit();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("PhoneticPanel.check"));
			button_confirm.setEnabled(false);
		}
	}
	
	public boolean isCanCopy(JTextComponent component) {
		boolean b = false;
		int start = component.getSelectionStart();
		int end = component.getSelectionEnd();
		if (start != end)
			b = true;
		return b;
	}

	public JTextComponent getSource() {
		return source;
	}

	public void setSource(JTextComponent source) {
		this.source = source;
	}
	
}
