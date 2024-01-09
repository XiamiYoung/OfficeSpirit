package com.xiami.frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.TextBean;
import com.xiami.bean.TextFileBean;
import com.xiami.core.IDGererator;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.TextFileLoader;
import com.xiami.logic.TextLoader;
import com.xiami.logic.TimeCounter;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.FontManager;

@SuppressWarnings("serial")
public class TextPanel extends ImagePanel implements Runnable, ActionListener {

	private CommonScrollPane pane_title;
	private CommonScrollPane pane_text;
	private JPanel panel_file;
	private JComboBox choice_file;
	private ImageButton button_file_add;
	private ImageButton button_file_update;
	private ImageButton button_file_delete;
	private ImageButton button_max;
	private ImageButton button_add;
	private ImageButton button_update;
	private ImageButton button_delete;
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
	private JMenuItem menuItem_Max = new JMenuItem();
	private JMenuItem menuItem_New = new JMenuItem();
	private JMenuItem menuItem_New_File = new JMenuItem();
	private JMenuItem menuItem_Update = new JMenuItem();
	private JMenuItem menuItem_Delete = new JMenuItem();
	private JMenuItem menuItem_Exit = new JMenuItem();
	private JMenuItem menuItem_Preferences = new JMenuItem();
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();
	private JPopupMenu pMenu = new JPopupMenu();
	private JMenuItem pMenuItem_Max = new JMenuItem();
	private JMenuItem pMenuItem_Update = new JMenuItem();
	private JMenuItem pMenuItem_Delete = new JMenuItem();
	private JPopupMenu pMenu_Text = new JPopupMenu();
	private JMenuItem pmenuItem_Copy = new JMenuItem();
	private JMenuItem pmenuItem_Cut = new JMenuItem();
	private JMenuItem pmenuItem_Paste = new JMenuItem();
	private JMenuItem pmenuItem_Undo = new JMenuItem();
	private JMenuItem pmenuItem_SelectAll = new JMenuItem();
	private JMenuItem pmenuItem_Delete = new JMenuItem();
	private JMenuItem pmenuItem_lookUpC2E = new JMenuItem();
	private JMenuItem pmenuItem_lookUpE2C = new JMenuItem();
	private JMenuItem pmenuItem_lookUpJ2C = new JMenuItem();
	private JMenuItem pmenuItem_lookUpC2J = new JMenuItem();
	private DefaultListModel model_title = new DefaultListModel();
	private JList list_title;
	private JTextField name;
	private ImageTextArea textArea;
	private ArrayList<TextBean> contents;
	private ArrayList<TextFileBean> textFileContent;
	private CommonUIManager ui;
	private int index_title;
	private static boolean showedUp = false;
	public static boolean isInitialzed = false;
	private JScrollBar bar;

	private MainFrame mainFrame;
	private MaxTextFrame maxTextFrame;
	private OptionDialog optionDialog;
	private TextFileModifyFrame textFileModifyFrame;

	private UndoHandler undoHandler;

	private int itemChangedType;

	private int bookMark = 0;

	public TextPanel(MainFrame obj) {
		mainFrame = obj;
	}

	public void init() {

		createComponent();
		addComponent();
		setting();
		setShowedUp(true);
		isInitialzed = true;

		loadDefaultText();

		this.repaint();
	}

	public void createComponent() {

		list_title = new JList(model_title);
		button_max = new ImageButton("", Keys.INITIAL_WIDTH_TEXT_PANEL - 90,
				50, 65, 65, getIns());
		button_add = new ImageButton("", Keys.INITIAL_WIDTH_TEXT_PANEL - 100,
				220, 80, 60, getIns());
		button_update = new ImageButton("",
				Keys.INITIAL_WIDTH_TEXT_PANEL - 100, 370, 80, 60, getIns());
		button_delete = new ImageButton("",
				Keys.INITIAL_WIDTH_TEXT_PANEL - 100, 520, 80, 60, getIns());
		button_file_add = new ImageButton("", 11, 25, 68, 35, getIns());
		button_file_update = new ImageButton("", 91, 25, 68, 35, getIns());
		button_file_delete = new ImageButton("", 171, 25, 68, 35, getIns());
		pane_title = new CommonScrollPane("", list_title);
		name = new CommonTextField(258, 50, 532, 30);
		textArea = new ImageTextArea();
		pane_text = new CommonScrollPane("", textArea);
		bar = pane_text.getVerticalScrollBar();
		panel_file = new JPanel();
		panel_file.setLayout(null);
		panel_file.setBorder(BorderFactory.createEtchedBorder());
		choice_file = new JComboBox();

		addKeyBoardEvent();
		addMouseEvent();
		addFocusEvent();
		addItemEvent();

		new UndoHandler(name);
		undoHandler = new UndoHandler(textArea);
	}

	public void addComponent() {

		this.add(button_max);
		this.add(button_add);
		this.add(button_update);
		this.add(button_delete);
		this.add(panel_file);
		this.add(pane_title);
		this.add(pane_text);
		this.add(name);
		this.add(menubar);

		panel_file.add(button_file_add);
		panel_file.add(button_file_update);
		panel_file.add(button_file_delete);
		panel_file.add(choice_file);

		menubar.add(menu_file);
		menubar.add(menu_edit);
		menubar.add(menu_option);
		menubar.add(menu_link);
		menubar.add(menu_help);

		menu_link.add(menuItem_link_search);
		menu_link.addSeparator();
		menu_link.add(menuItem_link1);
		menu_link.add(menuItem_link2);
		menu_link.add(menuItem_link3);

		menu_file.add(menuItem_New_File);
		menu_file.addSeparator();
		menu_file.add(menuItem_New);
		menu_file.addSeparator();
		menu_file.add(menuItem_Exit);

		menu_edit.add(menuItem_Max);
		menu_edit.addSeparator();
		menu_edit.add(menuItem_Update);
		menu_edit.add(menuItem_Delete);
		menu_option.add(menuItem_Preferences);
		menu_help.add(menuItem_Statis);
		menu_help.add(menuItem_About);

		pMenu.add(pMenuItem_Max);
		pMenu.add(pMenuItem_Update);
		pMenu.add(pMenuItem_Delete);

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

		pmenuItem_Copy.addActionListener(this);
		pmenuItem_Cut.addActionListener(this);
		pmenuItem_Paste.addActionListener(this);
		pmenuItem_Undo.addActionListener(this);
		pmenuItem_SelectAll.addActionListener(this);
		pmenuItem_Delete.addActionListener(this);
		menuItem_Delete.addActionListener(this);
		menuItem_New_File.addActionListener(this);
		menuItem_New.addActionListener(this);
		menuItem_Update.addActionListener(this);
		menuItem_Max.addActionListener(this);
		menuItem_Exit.addActionListener(this);
		menuItem_Preferences.addActionListener(this);
		menuItem_Statis.addActionListener(this);
		menuItem_About.addActionListener(this);
		menuItem_link_search.addActionListener(this);
		menuItem_link1.addActionListener(this);
		menuItem_link2.addActionListener(this);
		menuItem_link3.addActionListener(this);
		pMenuItem_Max.addActionListener(this);
		pMenuItem_Update.addActionListener(this);
		pMenuItem_Delete.addActionListener(this);
		pmenuItem_lookUpE2C.addActionListener(this);
		pmenuItem_lookUpC2E.addActionListener(this);
		pmenuItem_lookUpJ2C.addActionListener(this);
		pmenuItem_lookUpC2J.addActionListener(this);
	}

	public void setting() {

		setLanguage();
		panel_file.setBounds(5, 48, 250, 110);
		choice_file.setBounds(5, 70, 240, 30);
		choice_file.setEnabled(false);
		pane_title.setBounds(5, 165, 250, Keys.INITIAL_HEIGHT_TEXT_PANEL - 175);
		pane_text.setBounds(258, 85, 532, Keys.INITIAL_HEIGHT_TEXT_PANEL - 95);
		menubar.setBounds(1, 18, Keys.INITIAL_WIDTH_TEXT_PANEL - 5, 25);
		menubar.setBorderPainted(true);
		menubar.setBorder(BorderFactory.createBevelBorder(0));
		list_title.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Cursor corsor = new Cursor(Cursor.HAND_CURSOR);
		menuItem_link_search.setCursor(corsor);
		menuItem_link1.setCursor(corsor);
		menuItem_link2.setCursor(corsor);
		menuItem_link3.setCursor(corsor);
		this.setLayout(null);
		this.setBackground(new Color(237, 235, 193));

		button_update.registerKeyboardAction(getIns(), KeyStroke.getKeyStroke(
				KeyEvent.VK_S, InputEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		button_update.setToolTipText("update a text");

		button_add.registerKeyboardAction(getIns(), KeyStroke.getKeyStroke(
				KeyEvent.VK_N, InputEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		button_add.setToolTipText("add a text");

		button_delete.registerKeyboardAction(getIns(), KeyStroke.getKeyStroke(
				KeyEvent.VK_D, InputEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		button_delete.setToolTipText("delete a text");

		button_max.registerKeyboardAction(getIns(), KeyStroke.getKeyStroke(
				KeyEvent.VK_M, InputEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		button_max.setToolTipText("to max mode");
	}

	public void setLanguage() {
		button_max.setText(SystemConfig.getLanguageProp("TextPanel.max"));
		button_add.setText(SystemConfig.getLanguageProp("TextPanel.add"));
		button_update.setText(SystemConfig.getLanguageProp("TextPanel.update"));
		button_delete.setText(SystemConfig.getLanguageProp("TextPanel.delete"));
		button_file_add.setText(SystemConfig.getLanguageProp("TextPanel.add"));
		button_file_update.setText(SystemConfig
				.getLanguageProp("TextPanel.update"));
		button_file_delete.setText(SystemConfig
				.getLanguageProp("TextPanel.delete"));
		menu_file.setText(SystemConfig.getLanguageProp("TextPanel.menu.file"));
		menu_edit.setText(SystemConfig.getLanguageProp("TextPanel.menu.edit"));
		menu_option.setText(SystemConfig
				.getLanguageProp("TextPanel.menu.option"));
		menu_help.setText(SystemConfig.getLanguageProp("TextPanel.menu.help"));
		menu_link.setText(SystemConfig.getLanguageProp("Link.menu"));
		menuItem_New_File.setText(SystemConfig
				.getLanguageProp("TextPanel.menu.add.file"));
		menuItem_New.setText(SystemConfig
				.getLanguageProp("TextPanel.menu.add.text"));
		menuItem_Max.setText(SystemConfig.getLanguageProp("TextPanel.max"));
		menuItem_Update.setText(SystemConfig
				.getLanguageProp("TextPanel.update.text"));
		menuItem_Delete.setText(SystemConfig
				.getLanguageProp("TextPanel.delete.text"));
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		pMenuItem_Max.setText(SystemConfig.getLanguageProp("TextPanel.max"));
		pMenuItem_Update.setText(SystemConfig
				.getLanguageProp("TextPanel.update"));
		pMenuItem_Delete.setText(SystemConfig
				.getLanguageProp("TextPanel.delete"));
		pmenuItem_Copy.setText(SystemConfig.getLanguageProp("MaxText.copy"));
		pmenuItem_Cut.setText(SystemConfig.getLanguageProp("MaxText.cut"));
		pmenuItem_Paste.setText(SystemConfig.getLanguageProp("MaxText.paste"));
		pmenuItem_Undo.setText(SystemConfig.getLanguageProp("MaxText.undo"));
		pmenuItem_SelectAll
				.setText(SystemConfig.getLanguageProp("MaxText.all"));
		pmenuItem_Delete
				.setText(SystemConfig.getLanguageProp("MaxText.delete"));
		pmenuItem_lookUpE2C.setText(SystemConfig.getLanguageProp("DictPanel.E2C"));
		pmenuItem_lookUpC2E.setText(SystemConfig.getLanguageProp("DictPanel.C2E"));
		pmenuItem_lookUpJ2C.setText(SystemConfig.getLanguageProp("DictPanel.J2C"));
		pmenuItem_lookUpC2J.setText(SystemConfig.getLanguageProp("DictPanel.C2J"));
		menuItem_Preferences.setText(SystemConfig
				.getLanguageProp("TextPanel.menuitem.preferences"));
		menuItem_About.setText(SystemConfig
				.getLanguageProp("TextPanel.menuitem.about"));
		menuItem_Exit.setText(SystemConfig
				.getLanguageProp("TextPanel.menuitem.exit"));
		menuItem_link_search.setText(SystemConfig
				.getLanguageProp("Link.search"));
		menuItem_link1.setText(SystemConfig.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig.getSystemProp("link_name3"));

		Font font = FontManager.getFont(16);
		Insets insets = new Insets(0, 0, 0, 0);
		button_add.setFont(font);
		button_add.setMargin(insets);
		button_update.setFont(font);
		button_update.setMargin(insets);
		button_delete.setFont(font);
		button_delete.setMargin(insets);
		button_max.setFont(font);
		button_max.setMargin(insets);

		Font font_file = FontManager.getFont(16);

		button_file_add.setFont(font_file);
		button_file_add.setMargin(insets);
		button_file_update.setFont(font_file);
		button_file_update.setMargin(insets);
		button_file_delete.setFont(font_file);
		button_file_delete.setMargin(insets);

		Font font_menu = FontManager.getFont(14);
		menu_file.setFont(font_menu);
		menu_edit.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_option.setFont(font_menu);
		menu_link.setFont(font_menu);

		TitledBorder fileBorder = new TitledBorder(SystemConfig
				.getLanguageProp("TextPanel.category"));
		fileBorder.setTitleFont(FontManager.getFont(16));
		fileBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		fileBorder.setBorder(BorderFactory.createEtchedBorder());
		panel_file.setBorder(fileBorder);
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
			SystemConfig.setSystemProp(Keys.TOTAL_TIME, String
					.valueOf(TimeCounter.getTotal_time()));
			System.exit(0);
		}
		if (e.getSource() == menuItem_Max || e.getSource() == pMenuItem_Max
				|| e.getSource() == button_max) {
			if (model_title.size() == 0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
				toMaxMode();
			}
		}
		if (e.getSource() == pmenuItem_Copy) {
			textArea.copy();
		}
		if (e.getSource() == pmenuItem_Cut) {
			textArea.cut();
		}
		if (e.getSource() == pmenuItem_Paste) {
			textArea.paste();
		}
		if (e.getSource() == pmenuItem_Undo) {
			undoHandler.undo();
		}
		if (e.getSource() == pmenuItem_SelectAll) {
			textArea.selectAll();
		}
		if (e.getSource() == pmenuItem_Delete) {
			textArea.replaceSelection("");
		}
		if (e.getSource() == menuItem_link1) {
			LinkHandler.toUrl(SystemConfig.getSystemProp("link_url1"));
		}
		if (e.getSource() == menuItem_link2) {
			LinkHandler.toUrl(SystemConfig.getSystemProp("link_url2"));
		}
		if (e.getSource() == menuItem_link3) {
			LinkHandler.toUrl(SystemConfig.getSystemProp("link_url3"));
		}
		if (e.getSource() == menuItem_link_search) {
			Random ran = new Random();
			int i = ran.nextInt(100);
			String type = SystemConfig.getSystemProp(Keys.SEARCH_ENGINE_TYPE);
			if (type.equals("0")) {
				i -= 51;
			} else if (type.equals("1")) {
				i += 51;
			}
			if (i <= 50) {
				LinkHandler.toUrl(SystemConfig.getSystemProp("link_baidu"));
			} else {
				LinkHandler.toUrl(SystemConfig.getSystemProp("link_google"));
			}
		}
		if (e.getSource() == pmenuItem_lookUpE2C) {
			MainFrame.setECDict();
			MainFrame.getTabbedPane().setSelectedIndex(2);
			MainFrame.getEcDictPanel().clearScreen();
			String str = textArea.getSelectedText();
			MainFrame.getEcDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getEcDictPanel().search(textArea.getSelectedText());
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
			String str = textArea.getSelectedText();
			MainFrame.getCeDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getCeDictPanel().search(textArea.getSelectedText());
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
			String str = textArea.getSelectedText();
			MainFrame.getJcDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getJcDictPanel().search(textArea.getSelectedText());
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
			String str = textArea.getSelectedText();
			MainFrame.getCjDictPanel().setKeyWord(str);
			boolean flag = MainFrame.getCjDictPanel().search(textArea.getSelectedText());
			if(MainFrame.getCjDictPanel().getModel_word().size()!=0){
				if(flag){
					MainFrame.getCjDictPanel().showWord(0);
				}
			}else{
				MainFrame.getCjDictPanel().clearScreen();
				MainFrame.getCjDictPanel().setKeyWord(str);
			}
		}
		if (e.getSource() == button_add || e.getSource() == menuItem_New) {
			if (choice_file.getModel().getSize() == 0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
				ui = new CommonUIManager(new TextAddDialog(getIns()));
				CommonUIManager.setUI();
				ui.startUp();
			}
		}
		if (e.getSource() == button_delete || e.getSource() == pMenuItem_Delete
				|| e.getSource() == menuItem_Delete) {
			if (model_title.size() == 0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
				int result = JOptionPane.showConfirmDialog(getIns(),
						SystemConfig.getLanguageProp("Message.Delete"));
				if (result == 0) {
					String uuid = contents.get(list_title.getSelectedIndex())
							.getUUID();
					TextLoader.removeText(uuid);
					loadText(0);
				}
			}
		}
		if (e.getSource() == button_update || e.getSource() == pMenuItem_Update
				|| e.getSource() == menuItem_Update) {
			if (model_title.size() == 0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(this, SystemConfig
							.getLanguageProp("Message.name")
							+ " "
							+ SystemConfig.getLanguageProp("Message.input"));
				} else if (textArea.getText().equals("")) {
					JOptionPane.showMessageDialog(this, SystemConfig
							.getLanguageProp("Message.content")
							+ " "
							+ SystemConfig.getLanguageProp("Message.input"));
				} else {
					int result = JOptionPane.showConfirmDialog(getIns(),
							SystemConfig.getLanguageProp("Message.Update"));
					if (result == 0) {
						int index = list_title.getSelectedIndex();
						TextBean bean = contents.get(index);
						bean.setName(name.getText());
						bean.setContent(textArea.getText());
						TextLoader.updateTextBean(bean);
						loadText(index);
					}
				}
			}
		}
		if (e.getSource() == button_file_add
				|| e.getSource() == menuItem_New_File) {
			textFileModifyFrame = new TextFileModifyFrame(getIns());
			ui = new CommonUIManager(textFileModifyFrame);
			CommonUIManager.setUI();
			ui.startUp();
		}
		if (e.getSource() == button_file_update) {
			if (null == textFileContent
					|| choice_file.getModel().getSize() == 0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
				textFileModifyFrame = new TextFileModifyFrame(choice_file
						.getSelectedItem().toString(), getIns());
				ui = new CommonUIManager(textFileModifyFrame);
				CommonUIManager.setUI();
				ui.startUp();
			}
		}
		if (e.getSource() == button_file_delete) {
			if (null == textFileContent
					|| choice_file.getModel().getSize() == 0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
				deleteFile(true);
			}
		}
	}

	public void run() {
		init();
	}

	public void loadText(int index) {
		ArrayList<TextBean> contents_temp = TextLoader.getText();
//		if (index >= contents_temp.size()) {
//			index = 0;
//		} else {
			if (null != contents_temp) {
				clearData();
				contents = contents_temp;
				for (int i = 0; i < contents.size(); i++) {
					model_title.addElement(" " + contents.get(i).getName()
							+ " ");
				}
				list_title.setSelectedIndex(index);
				if (contents_temp.size() != 0) {
					textArea.setEnabled(true);
					name.setEnabled(true);
					TextBean bean = contents.get(index);
					textArea.setText(null);
					textArea.append(bean.getContent());
					if (null != bean.getBookmark()
							&& !"".equals(bean.getBookmark().toString())) {
						bookMark = Integer.parseInt(bean.getBookmark());
					}
					SwingUtilities.invokeLater(scrollToBookMark);
					name.setText(bean.getName());
					index_title = index;
					textFileContent.get(choice_file.getSelectedIndex()).setLastOpened(list_title.getSelectedIndex());
					TextFileLoader.updateTexFileBean(textFileContent.get(choice_file.getSelectedIndex()));
					scrollToView();
				} else {
					textArea.setEnabled(false);
					name.setEnabled(false);
					clearData();
				}
			}
//		}
	}

	public void addFocusEvent() {

		bar.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (isScrollChanged(bar.getValue())) {
					TextBean bean = contents.get(index_title);
					bookMark = bar.getValue();
					bean.setBookmark(String.valueOf(bookMark));
					contents.get(index_title).setBookmark(
							String.valueOf(bookMark));
					TextLoader.updateBookMark(bean);
				}
			}
		});
	}

	public void addMouseEvent() {
		list_title.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
					if ((e.getClickCount() == 2) && (e.getButton() == 1)
						&& (null != contents)) {
					toMaxMode();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 1)
						&& (null != contents)
						&& (index_title != list_title.getSelectedIndex())) {
					loadSelectedText();
				} else if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pMenu.show(list_title, e.getX(), e.getY());
				}
			}
		});

		//double click text to make highlight
		textArea.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pmenuItem_lookUpE2C.setEnabled(isCanCopy());
					pmenuItem_lookUpC2E.setEnabled(isCanCopy());
					pmenuItem_lookUpJ2C.setEnabled(isCanCopy());
					pmenuItem_lookUpC2J.setEnabled(isCanCopy());
					pmenuItem_Copy.setEnabled(isCanCopy());
					pmenuItem_Paste.setEnabled(isClipboardString());
					pmenuItem_Cut.setEnabled(isCanCopy());
					pmenuItem_Undo.setEnabled(undoHandler.canUndo());
					pmenuItem_Delete.setEnabled(isCanCopy());
					pMenu_Text.show(textArea, e.getX(), e.getY());
				}
			}
		});

		bar.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if ((e.getButton() == 1) && (null != textArea.getText())
						&& (!"".equals(textArea.getText()))) {
					bar.requestFocus();
				}
			}
		});

		pane_text.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				bar.requestFocus();
			}
		});

	}

	public void addItemEvent() {
		choice_file.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED
						&& itemChangedType != 0) {
					
					configText();
					
					int index = textFileContent.get(
							choice_file.getSelectedIndex()).getLastOpened();
					
					if(index>=0){
						loadText(index);
					}else{
						loadText(0);
					}
				}
			}
		});
	}

	public void addKeyBoardEvent() {

		list_title.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				try {
					if (null != contents && e.getSource() == list_title) {
						TextBean bean = contents.get(list_title
								.getSelectedIndex());
						index_title = list_title.getSelectedIndex();

						textArea.setText(bean.getContent());
						if (null != bean.getBookmark()
								&& !"".equals(bean.getBookmark().toString())) {
							bookMark = Integer.parseInt(bean.getBookmark());
						}
						SwingUtilities.invokeLater(scrollToBookMark);
						name.setText(bean.getName());
						scrollToView();
					}
				} catch (OutOfMemoryError ex) {
					JOptionPane
							.showMessageDialog(
									mainFrame,
									"We are sorry but OutOfMemoryError was found,this is due to a known jvm bug,please restart");
					System.exit(1);
				}
			}
		});

		textArea.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (isScrollChanged(bar.getValue())) {
					TextBean bean = contents.get(index_title);
					bookMark = bar.getValue();
					bean.setBookmark(String.valueOf(bookMark));
					contents.get(index_title).setBookmark(
							String.valueOf(bookMark));
					TextLoader.updateBookMark(bean);
				}
			}
		});
	}

	public void clearData() {
		this.model_title.clear();
		this.textArea.setText(null);
		this.name.setText(null);
		this.contents = null;
	}

	public void switchVisible() {
		if (this.isVisible() == false) {
			setShowedUp(true);
			this.setVisible(true);
		} else {
			setShowedUp(false);
			this.setVisible(false);
			this.requestFocus();
		}
	}

	public void loadSelectedText() {
		try {
			TextBean bean = contents.get(list_title.getSelectedIndex());
			index_title = list_title.getSelectedIndex();
			textArea.setText(bean.getContent());
			name.setText(bean.getName());
			if (null != bean.getBookmark()
					&& !"".equals(bean.getBookmark().toString())) {
				bookMark = Integer.parseInt(bean.getBookmark());
			}
			textFileContent.get(choice_file.getSelectedIndex()).setLastOpened(list_title.getSelectedIndex());
			TextFileLoader.updateTexFileBean(textFileContent.get(choice_file.getSelectedIndex()));
			SwingUtilities.invokeLater(scrollToBookMark);
		} catch (OutOfMemoryError ex) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"We are sorry but OutOfMemoryError was found,this is due to a known jvm problem（try not to switch text too many times,cause this will eat lots of memories.）,and please restart");
			System.exit(1);
		}
	}

	public void configText() {
		try {
			TextLoader.configuration(textFileContent.get(
					choice_file.getSelectedIndex()).getPath());
			SystemConfig.setSystemProp(Keys.LAST_OPENED_TEXT_CATEGORY,
					choice_file.getSelectedItem().toString());
		} catch (Exception e1) {
			int result = JOptionPane.showConfirmDialog(getIns(), SystemConfig
					.getLanguageProp("TextPanel.menu.file")
					+ " "
					+ choice_file.getSelectedItem()
					+ " "
					+ SystemConfig.getLanguageProp("Message.TextError"));
			if (result == 0) {
				deleteFile(false);
				loadTextFile(0);
			} else {
				return;
			}
		}
	}

	public void loadTextFile(int index) {
		choice_file.removeAllItems();
		for (int i = 0; i < textFileContent.size(); i++) {
			choice_file.addItem(textFileContent.get(i).getName());
		}
		if (textFileContent.size() != 0) {
			itemChangedType = 0;
			choice_file.setSelectedIndex(index);
			itemChangedType = 1;
			configText();
			choice_file.setEnabled(true);
		} else {
			textArea.setEnabled(false);
			name.setEnabled(false);
		}
	}

	public void addTextFile(String name) {
		if (isHasSameName(name)) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Same"));
			return;
		} else {
			TextFileLoader.createXMLFile(new File(".\\textResource\\" + name + ".xml"));
			
			TextFileBean bean = new TextFileBean();
			bean.setUUID(IDGererator.getUUID());
			bean.setName(name);
			bean.setPath(".\\textResource\\" + name + ".xml");
			bean.setLastOpened(0);
			
			TextFileLoader.addTextFile(bean);
			
			textFileContent.add(bean);

			itemChangedType = 0;
			loadTextFile(textFileContent.size() - 1);
			loadText(0);
		}
	}

	public void updateTextFile(String name) {
		if (name.toLowerCase().equals(
				choice_file.getSelectedItem().toString().toLowerCase())) {
			return;
		} else if (isHasSameName(name)) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Same"));
			return;
		} else {
			int result = JOptionPane.showConfirmDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Update"));
			if (result == 0) {
				TextFileBean bean = new TextFileBean();
				bean.setUUID(textFileContent
						.get(choice_file.getSelectedIndex()).getUUID());
				bean.setName(name);
				bean.setPath(".\\textResource\\" + name + ".xml");
				bean.setLastOpened(list_title.getSelectedIndex());

				TextFileLoader.updateTexFileBean(bean);
				TextFileLoader.updateXMLFileName(new File(".\\textResource\\"
						+ choice_file.getSelectedItem().toString() + ".xml"),
						new File(".\\textResource\\" + name + ".xml"));
				textFileContent.get(choice_file.getSelectedIndex()).setName(
						name);
				textFileContent.get(choice_file.getSelectedIndex()).setPath(
						".\\textResource\\" + name + ".xml");
				textFileContent.get(choice_file.getSelectedIndex()).setLastOpened(
						list_title.getSelectedIndex());
				

				loadTextFile(choice_file.getSelectedIndex());
				loadText(bean.getLastOpened());
			}
		}
	}

	private boolean isHasSameName(String name) {
		boolean flag = false;
		for (int i = 0; i < choice_file.getModel().getSize(); i++) {
			if (choice_file.getModel().getElementAt(i).toString().toLowerCase()
					.equals(name.toLowerCase())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public void deleteFile(boolean isConfirm) {
		int result = 0;
		if (isConfirm) {
			result = JOptionPane.showConfirmDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Delete"));
		}
		if (result == 0) {
			TextFileLoader.removeTextFile(textFileContent.get(
					choice_file.getSelectedIndex()).getUUID());
			TextFileLoader.deleteXMLFile(new File(".\\textResource\\"
					+ choice_file.getSelectedItem().toString() + ".xml"));

			textFileContent.remove(textFileContent.get(choice_file
					.getSelectedIndex()));
			if(choice_file.getItemCount()==1){
				choice_file.removeAllItems();
			}
			clearData();
			if(choice_file.getItemCount()>0){
				int index = choice_file.getSelectedIndex() - 1 > 0 ? choice_file
						.getSelectedIndex() - 1 : 0;
				loadTextFile(index);
				loadText(textFileContent.get(index).getLastOpened());
			}
			else{
				choice_file.setEnabled(false);
				name.setEnabled(false);
				textArea.setEnabled(false);
				SystemConfig
				.setSystemProp(Keys.LAST_OPENED_TEXT_CATEGORY,"");
			}
		}
	}

	public void toMaxMode() {
		if (!MaxTextFrame.isInitialzed) {
			Logger.getLogger().info("Initializing maxTextFrame");
			maxTextFrame = new MaxTextFrame(getIns());
			maxTextFrame.setTextBean(contents
					.get(list_title.getSelectedIndex()));
			ui = new CommonUIManager(maxTextFrame);
			CommonUIManager.setUI();
			ui.startUp();
			Logger.getLogger().info("Initialization done");
		} else {
			Logger.getLogger().info("maxTextFrame show up");
			maxTextFrame.setTextBean(contents
					.get(list_title.getSelectedIndex()));
			maxTextFrame.setting();
		}
		mainFrame.setVisible(false);
		SysIcon.setSource(5);
	}

	public void loadDefaultText() {
		try {
			int index_category = 0;
			int index_text = 0;
			textFileContent = TextFileLoader.getTextFile();
			if(textFileContent.size()==0){
				name.setEnabled(false);
				textArea.setEnabled(false);
				return;
			}
			for (int i = 0; i < textFileContent.size(); i++) {
				if (textFileContent.get(i).getName().equals(
						SystemConfig
								.getSystemProp(Keys.LAST_OPENED_TEXT_CATEGORY))) {
					index_category = i;
					break;
				}
			}
			index_text = textFileContent.get(index_category).getLastOpened();
			loadTextFile(index_category);
			loadText(index_text);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, SystemConfig
					.getLanguageProp("Message.TextFileError"));
			e.printStackTrace();
			button_file_add.setEnabled(false);
			return;
		}
	}

	public TextPanel getIns() {
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

	public static boolean isShowedUp() {
		return showedUp;
	}

	public static void setShowedUp(boolean showedUp) {
		TextPanel.showedUp = showedUp;
	}

	public int getTextCount() {
		return model_title.getSize();
	}

	Runnable scrollToBookMark = new Runnable() {
		public void run() {
			bar.setValue(bookMark);
		}
	};

	public boolean isScrollChanged(int value) {
		return bookMark != value;
	}

	public boolean isCanCopy() {
		boolean b = false;
		int start = textArea.getSelectionStart();
		int end = textArea.getSelectionEnd();
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

	public void updateLink() {
		menuItem_link1.setText(SystemConfig.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig.getSystemProp("link_name3"));
	}

	public MaxTextFrame getMaxTextFrame() {
		return maxTextFrame;
	}

	public void setMaxTextFrame(MaxTextFrame maxTextFrame) {
		this.maxTextFrame = maxTextFrame;
	}

	public ArrayList<TextBean> getContents() {
		return contents;
	}

	public void setContents(ArrayList<TextBean> contents) {
		this.contents = contents;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JComboBox getChoice_file() {
		return choice_file;
	}

}
