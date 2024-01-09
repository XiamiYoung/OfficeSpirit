package com.xiami.frame;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.xiami.Component.CommonDefaultTableModel;
import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.DroppableList;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.bean.SheetBean;
import com.xiami.bean.TipBean;
import com.xiami.bean.WordBean;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;
import com.xiami.logic.FileLoader;
import com.xiami.logic.LinkHandler;
import com.xiami.logic.SearchHandler;
import com.xiami.logic.TiPCounter;
import com.xiami.logic.TimeCounter;
import com.xiami.logic.TipDataLoader;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.FontManager;

@SuppressWarnings("serial")
public class WordPanel extends ImagePanel implements Runnable, ActionListener {

	private ImagePanel sheetPanel;
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_file = new JMenu();
	private JMenu menu_edit = new JMenu();
	private JMenu menu_option = new JMenu();
	private JMenu menu_help = new JMenu();
	private JMenu menu_link = new JMenu();
	private JMenuItem menuItem_New = new JMenuItem();
	private JMenuItem menuItem_OPEN = new JMenuItem();
	private JMenuItem menuItem_Exit = new JMenuItem();
	private JMenuItem menuItem_Update = new JMenuItem();
	private JMenuItem menuItem_Delete = new JMenuItem();
	private JMenuItem menuItem_history1 = new JMenuItem();
	private JMenuItem menuItem_history2 = new JMenuItem();
	private JMenuItem menuItem_history3 = new JMenuItem();
	private JMenuItem menuItem_Preferences = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JMenuItem menuItem_Search = new JMenuItem();
	private JMenuItem menuItem_link_search = new JMenuItem();
	private JMenuItem menuItem_link1 = new JMenuItem();
	private JMenuItem menuItem_link2 = new JMenuItem();
	private JMenuItem menuItem_link3 = new JMenuItem();
	private CommonScrollPane pane_title;
	private CommonScrollPane pane_date;
	private CommonScrollPane pane_word;
	private JTextField text_title;
	private ImageButton button_search;
	private ImageButton button_add;
	private ImageButton button_test;
	private ImageButton button_puzzle;
	private ImageButton button_update;
	private ImageButton button_delete;
	private ImageButton button_detail;
	private ImageButton button_export;
	private DefaultListModel model_word = new DefaultListModel();
	private DroppableList list_word = new DroppableList(model_word);
	private JTable table_word = new JTable();
	private CommonDefaultTableModel tableModel;
	private static ArrayList<SheetBean> contents;
	private JPopupMenu pMenu_date = new JPopupMenu();
	private JPopupMenu pMenu_word = new JPopupMenu();
	private JMenuItem openDetail_date = new JMenuItem();
	private JMenuItem openDetail_word = new JMenuItem();
	private JMenuItem pMenuItem_update = new JMenuItem();
	private JMenuItem pMenuItem_delete = new JMenuItem();
	private CommonUIManager ui;
	private File file;
	private int index_date;
	private int max_index_date;
	private static boolean isHasContent = false;
	private static boolean showedUp = false;
	public static boolean isInitialzed = false;

	private DetailPanel detailPanel = null;
	private OptionDialog optionDialog;
	private SearchHandler search;
	private SearchFrame searchFrame;
	private MainFrame mainFrame;
	private SheetModifyFrame sheetModifyFrame;

	public WordPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void init() {

		createComponent();
		addComponent();
		setting();
		setShowedUp(true);
		isInitialzed = true;
		this.setVisible(true);

		initWordTable();
		
		if (SystemConfig.getSystemProp(Keys.IS_LOAD_WORD).equals(Keys.YES)) {
			loadHistory1();
		}
	}

	public void createComponent() {
		this.setLayout(null);
		sheetPanel = new ImagePanel(220, 45, 155,
				Keys.INITIAL_HEIGHT_WORD_PANEL - 55);
		sheetPanel.setLayout(null);
		button_detail = new ImageButton("",
				Keys.INITIAL_WIDTH_WORD_PANEL - 100, 70, 70, 50, getIns());
		button_test = new ImageButton("",
				Keys.INITIAL_WIDTH_WORD_PANEL - 100, 220, 70, 50, getIns());
		button_puzzle = new ImageButton("",0, 0, 0, 0, getIns());
		button_search = new ImageButton("",
				Keys.INITIAL_WIDTH_WORD_PANEL - 100, 370, 70, 50, getIns());
		button_export = new ImageButton("",
				Keys.INITIAL_WIDTH_WORD_PANEL - 100,
				520, 70, 50, getIns());
		button_add = new ImageButton("", 40, 70, 80, 60, getIns());
		button_update = new ImageButton("", 40, 260, 80, 60, getIns());
		button_delete = new ImageButton("", 40, 450, 80, 60, getIns());
		text_title = new JTextField();
		pane_date = new CommonScrollPane("", list_word);
		pane_word = new CommonScrollPane("", table_word);
		pane_title = new CommonScrollPane("", text_title);
		tableModel = new CommonDefaultTableModel();
		table_word.setModel(tableModel);

		addKeyBoardEvent();
		addMouseEvent();
	}

	public void addComponent() {

		this.add(sheetPanel);
		this.add(button_search);
		this.add(button_test);
		this.add(button_puzzle);
		this.add(button_detail);
		this.add(button_export);
		this.add(menubar);
		this.add(pane_date);
		this.add(pane_word);
		this.add(pane_title);
		sheetPanel.add(button_add);
		sheetPanel.add(button_update);
		sheetPanel.add(button_delete);

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

		menu_option.add(menuItem_Preferences);

		menu_file.add(menuItem_New);
		menu_file.add(menuItem_OPEN);
		menu_file.addSeparator();
		//update History
		updateItemHistory();

		menu_file.addSeparator();
		menu_file.add(menuItem_Search);
		menu_file.addSeparator();
		menu_file.add(menuItem_Exit);

		menu_edit.add(menuItem_Update);
		menu_edit.add(menuItem_Delete);

		menu_help.add(menuItem_Statis);
		menu_help.add(menuItem_About);

		pMenu_date.add(openDetail_date);
		pMenu_date.addSeparator();
		pMenu_date.add(pMenuItem_update);
		pMenu_date.add(pMenuItem_delete);
		
		pMenu_word.add(openDetail_word);

		menuItem_Search.addActionListener(this);
		menuItem_Exit.addActionListener(this);
		menuItem_New.addActionListener(this);
		menuItem_OPEN.addActionListener(this);
		menuItem_Update.addActionListener(this);
		menuItem_Delete.addActionListener(this);
		menuItem_history1.addActionListener(this);
		menuItem_history2.addActionListener(this);
		menuItem_history3.addActionListener(this);
		menuItem_Preferences.addActionListener(this);
		menuItem_Statis.addActionListener(this);
		menuItem_About.addActionListener(this);
		menuItem_link_search.addActionListener(this);
		menuItem_link1.addActionListener(this);
		menuItem_link2.addActionListener(this);
		menuItem_link3.addActionListener(this);
		openDetail_date.addActionListener(this);
		openDetail_word.addActionListener(this);
		pMenuItem_update.addActionListener(this);
		pMenuItem_delete.addActionListener(this);
	}

	public void setting() {

		menuItem_Search.setText(SystemConfig
				.getLanguageProp("WordPanel.button.search"));
		button_search.setText(SystemConfig
				.getLanguageProp("WordPanel.button.search"));
		button_test.setText(SystemConfig
				.getLanguageProp("WordPanel.button.test"));
		button_detail.setText(SystemConfig
				.getLanguageProp("WordPanel.button.detail"));
		button_export.setText(SystemConfig
				.getLanguageProp("WordPanel.button.export"));
		openDetail_date.setText(SystemConfig
				.getLanguageProp("WordPanel.button.detail"));
		button_add.setText(SystemConfig.getLanguageProp("WordPanel.add"));
		openDetail_word.setText(SystemConfig
				.getLanguageProp("WordPanel.button.detail"));
		button_add.setText(SystemConfig.getLanguageProp("WordPanel.add"));
		button_update.setText(SystemConfig.getLanguageProp("WordPanel.update"));
		button_delete.setText(SystemConfig.getLanguageProp("WordPanel.delete"));
		menubar.setBounds(1, 18, Keys.INITIAL_WIDTH_WORD_PANEL - 5, 25);
		menubar.setBorderPainted(true);
		menubar.setBorder(BorderFactory.createBevelBorder(0));

		pane_title.setBounds(5, 48, 210, 30);
		pane_date.setBounds(5, 83, 210, Keys.INITIAL_HEIGHT_WORD_PANEL - 93);
		pane_word.setBounds(385, 45, 385, Keys.INITIAL_HEIGHT_WORD_PANEL - 55);

		pane_title.setHorizontalScrollBarPolicy(CommonScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane_title.setVerticalScrollBarPolicy(CommonScrollPane.VERTICAL_SCROLLBAR_NEVER);
		text_title.setHorizontalAlignment(JTextField.CENTER);
		text_title.setEditable(false);

		menu_file.setText(SystemConfig.getLanguageProp("WordPanel.menu.file"));
		menu_edit.setText(SystemConfig.getLanguageProp("WordPanel.menu.edit"));
		menu_option.setText(SystemConfig
				.getLanguageProp("WordPanel.menu.option"));
		menu_help.setText(SystemConfig.getLanguageProp("WordPanel.menu.help"));
		menu_link.setText(SystemConfig.getLanguageProp("Link.menu"));

		menuItem_New.setText(SystemConfig
				.getLanguageProp("WordPanel.menuitem.new"));
		menuItem_Update.setText(SystemConfig
				.getLanguageProp("WordPanel.update"));
		pMenuItem_update.setText(SystemConfig
				.getLanguageProp("WordPanel.update"));
		menuItem_Delete.setText(SystemConfig
				.getLanguageProp("WordPanel.delete"));
		pMenuItem_delete.setText(SystemConfig
				.getLanguageProp("WordPanel.delete"));
		menuItem_OPEN.setText(SystemConfig
				.getLanguageProp("WordPanel.menuitem.open"));
		menuItem_Preferences.setText(SystemConfig
				.getLanguageProp("WordPanel.menuitem.preferences"));
		menuItem_About.setText(SystemConfig
				.getLanguageProp("WordPanel.menuitem.about"));
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		menuItem_Exit.setText(SystemConfig
				.getLanguageProp("WordPanel.menuitem.exit"));
		menuItem_link_search.setText(SystemConfig
				.getLanguageProp("Link.search"));
		menuItem_link1.setText(SystemConfig.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig.getSystemProp("link_name3"));

		if (tableModel.getColumnCount() < 1) {
			tableModel.addColumn(SystemConfig
					.getLanguageProp("DetailPanel.word"));
			tableModel.addColumn(SystemConfig
					.getLanguageProp("DetailPanel.pron"));
			tableModel.addColumn(SystemConfig
					.getLanguageProp("DetailPanel.chinese"));
			table_word.getColumnModel().getColumn(0).setPreferredWidth(140);
			table_word.getColumnModel().getColumn(1).setPreferredWidth(120);
			table_word.getColumnModel().getColumn(2).setPreferredWidth(102);
		} else {
			table_word.getColumnModel().getColumn(0).setHeaderValue(
					SystemConfig.getLanguageProp("DetailPanel.word"));
			table_word.getColumnModel().getColumn(1).setHeaderValue(
					SystemConfig.getLanguageProp("DetailPanel.pron"));
			table_word.getColumnModel().getColumn(2).setHeaderValue(
					SystemConfig.getLanguageProp("DetailPanel.chinese"));
		}

		if (!text_title.getText().equals("")) {
			text_title.setText(SystemConfig
					.getLanguageProp("WordPanel.menu.file")
					+ ":" + file.getName());
		}

		table_word.getTableHeader().setReorderingAllowed(false);
		list_word.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//		table_word.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_word.setRowSelectionAllowed(true);
		table_word.setShowHorizontalLines(true);
		table_word.setShowVerticalLines(true);
		table_word.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//		table_word.setGridColor (new Color(147,128,40));
		//		table_word.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tableModel.isCellEditable(0, 0);

		Font font = FontManager.getFont(16);
		Insets insets = new Insets(0, 0, 0, 0);
		button_detail.setFont(font);
		button_detail.setMargin(insets);
		button_search.setFont(font);
		button_search.setMargin(insets);
		button_test.setFont(font);
		button_test.setMargin(insets);
		button_add.setFont(font);
		button_add.setMargin(insets);
		button_update.setFont(font);
		button_update.setMargin(insets);
		button_delete.setFont(font);
		button_delete.setMargin(insets);

		if (SystemConfig.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT).equals(
				Keys.LANGUAGE_CONFIG_JP)) {
			button_export.setFont(FontManager.getFont(10));
		} else {
			button_export.setFont(font);
		}
		button_export.setMargin(insets);

		TitledBorder sheetBorder = new TitledBorder(SystemConfig
				.getLanguageProp("WordPanel.sheet"));
		sheetBorder.setTitleFont(FontManager.getFont(18));
		sheetBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		sheetBorder.setBorder(BorderFactory.createEtchedBorder());

		Font font_menu = FontManager.getFont(14);
		menu_file.setFont(font_menu);
		menu_edit.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_option.setFont(font_menu);
		menu_link.setFont(font_menu);

		sheetPanel.setBorder(sheetBorder);

		Cursor corsor = new Cursor(Cursor.HAND_CURSOR);
		menuItem_link_search.setCursor(corsor);
		menuItem_link1.setCursor(corsor);
		menuItem_link2.setCursor(corsor);
		menuItem_link3.setCursor(corsor);

		SysIcon.setWordPanel(getIns());
		
		list_word.setWordPanel(getIns());
		
		button_update.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_update.setToolTipText("update a sheet");
		
		button_add.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		button_add.setToolTipText("add a sheet");
		
		button_delete.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_delete.setToolTipText("delete a sheet");
		
		button_detail.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_G,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_detail.setToolTipText("go to detail page");
		
		button_search.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_search.setToolTipText("search a word");
		
		button_test.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_test.setToolTipText("Total words test");
		
		button_export.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		button_export.setToolTipText("export selected rows to a excel or txt file");
		
		button_puzzle.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == menuItem_New) {
			Logger.getLogger().info("Creating new Excel");
			boolean isNotNull = false;
			try {
				isNotNull = FileLoader.newFile();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.Opened"));
				Logger.getLogger().info(e1.getMessage());
			}			
			if (isNotNull) {
				loadContent();
				if (null != table_word) {
					if (table_word.getSelectedRow() == -1) {
						table_word.setRowSelectionInterval(0, 0);
					}
					showDetailPanel();
				}
				updateItemHistory();
			}
		}

		if (e.getSource() == menuItem_OPEN) {
			boolean isNotNull = true;
			Logger.getLogger().info("Open Excel");
			isNotNull = FileLoader.ShowFile();
			if (isNotNull && loadContent()) {
				updateItemHistory();
				Logger.getLogger().info("Load Content");
			}
		}
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
		if (e.getSource() == button_search || e.getSource() == menuItem_Search) {
			Logger.getLogger().info("Initializing SearchFrame");
			if (!isHasContent() || list_word.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No word,search exited.");
			} else {
				if (!SearchFrame.isInitialized()) {
					searchFrame = SearchFrame.getInstance(getIns());
					ui = new CommonUIManager(searchFrame);
					CommonUIManager.setUI();
					ui.startUp();
					Logger.getLogger().info("Initializationg done");
				} else {
					Logger.getLogger().info("SearchFrame show up");
					searchFrame = SearchFrame.getInstance(getIns());
					searchFrame.setting();
				}
			}
		}

		if (e.getSource() == button_test) {
			if (TipBean.getWord_jp().size()<5) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.TestValid"));
			} else {
				ui = new CommonUIManager(new TestDialog());
				CommonUIManager.setUI();
				ui.startUp();
			}
		}
		
		if (e.getSource() == button_puzzle) {
			if (TipBean.getWord_jp().size()<4) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.TestValid"));
			} else {
				ui = new CommonUIManager(new PuzzleDialog(contents.get(list_word.getSelectedIndex())));
				CommonUIManager.setUI();
				ui.startUp();
			}
		}
		
		if (e.getSource() == button_detail || e.getSource() == openDetail_date || e.getSource() == openDetail_word) {
			Logger.getLogger().info("Open DetailPanel");
			if (null != table_word) {
				if (table_word.getSelectedRow() == -1) {
					table_word.setRowSelectionInterval(0, 0);
				}
				showDetailPanel();
			}
		}

		if (e.getSource() == button_export) {
			if (!isHasContent() || list_word.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No word,search exited.");
			} else if (table_word.getModel().getValueAt(0, 0).equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No word,search exited.");
			} else if (table_word.getSelectedRows().length==0) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
			} else {
//				int start = table_word.getSelectedRows()[0];
//				int end = start + table_word.getSelectedRows().length;
//
//				if (start >= contents.get(index_date).getWordBeanList().size()) {
//					JOptionPane.showMessageDialog(this, SystemConfig
//							.getLanguageProp("Message.OpenFirst"));
//					return;
//				}
//				
//				if (end > contents.get(index_date).getWordBeanList().size()) {
//					end = contents.get(index_date).getWordBeanList().size();
//				}
//				for (int i = start; i < end; i++) {
//					sb.append(contents.get(index_date).getWordBeanList().get(i)
//							.getWord()
//							+ "\t"
//							+ contents.get(index_date).getWordBeanList().get(i)
//									.getPron()
//							+ "\t"
//							+ contents.get(index_date).getWordBeanList().get(i)
//									.getWord_cn() + "\n");
//				}
				
				StringBuffer sb = new StringBuffer();
				
				for(int i=0;i<TipBean.getWord_jp().size();i++){
					sb.append(TipBean.getWord_jp().get(i)+"\t"
							+TipBean.getWord_pron().get(i)+"\t"
							+TipBean.getWord_cn().get(i)+"\t\n");
				}
				setClipboardText(sb.toString());
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.export"));
			}
		}

		if (e.getSource() == button_add) {
			Logger.getLogger().info("Creating new sheet");
			if (null == contents) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No excel,can not create new sheet");
			} else {
				Logger.getLogger().info("Initializing SheetModifyFrame");
				sheetModifyFrame = new SheetModifyFrame(getIns());
				ui = new CommonUIManager(sheetModifyFrame);
				CommonUIManager.setUI();
				ui.startUp();
				Logger.getLogger().info("SheetModifyFrame show up");
			}
		}
		if (e.getSource() == button_update || e.getSource() == menuItem_Update || e.getSource() == pMenuItem_update) {
			Logger.getLogger().info("Updating sheet name");
			if (null == contents) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No excel,can not update sheet");
			} else if (-1 == list_word.getSelectedIndex()) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info(
						"No excel selected,can not update sheet");
			} else {
				Logger.getLogger().info("Initializing SheetModifyFrame");
				sheetModifyFrame = new SheetModifyFrame(list_word
						.getSelectedValue().toString(), getIns());
				ui = new CommonUIManager(sheetModifyFrame);
				CommonUIManager.setUI();
				ui.startUp();
				Logger.getLogger().info("SheetModifyFrame show up");
			}
		}
		if (e.getSource() == button_delete || e.getSource() == menuItem_Delete || e.getSource() == pMenuItem_delete) {
			Logger.getLogger().info("Deleting sheet name");
			if (null == contents) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No excel,nothing to delete");
			} else if (-1 == list_word.getSelectedIndex()) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.OpenFirst"));
				Logger.getLogger().info("No excel selected,can not delete");
			} else if (1 == FileLoader.getSheetCount()) {
				JOptionPane.showMessageDialog(this, SystemConfig
						.getLanguageProp("Message.cantDeleteAll"));
			}else {
				int result = JOptionPane.showConfirmDialog(getIns(),
						SystemConfig.getLanguageProp("Message.Delete"));
				if (result == 0) {
					int index = list_word.getSelectedIndex();
					try {
						FileLoader.deleteSheet(index);
						Logger.getLogger().info("Delete successfully");
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(getIns(), SystemConfig
								.getLanguageProp("Message.Opened"));
						Logger.getLogger().info(
								"Delete failed,file is opening.");
						return;
					}
					Logger.getLogger().info("Deleting SheetBean");
					contents.remove(index);
					TipDataLoader.setData(contents);
					model_word.remove(index);
					if(contents.size()>0){
						if (index > 1) {
							list_word.setSelectedIndex(index - 1);
						} else {
							list_word.setSelectedIndex(0);
						}
						max_index_date--;
						loadWord(list_word.getSelectedIndex());
					}else{
						for(int i=tableModel.getRowCount()-1;i>=0;i--){
							tableModel.removeRow(i);
						}
						initWordTable();
					}
				}
			}
		}
		if (e.getSource() == menuItem_history1) {
			loadHistory1();
		}

		if (e.getSource() == menuItem_history2) {
			loadHistory2();
		}

		if (e.getSource() == menuItem_history3) {
			loadHistory3();
		}
	}

	public boolean loadContent() {
		Logger.getLogger().info("Loading Content");
		ArrayList<SheetBean> contents_temp = null;
		try {
			contents_temp = FileLoader.ParseExcel();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, SystemConfig
					.getLanguageProp("Message.LoadError"));
			Logger.getLogger().info("Error in loading the file");
			return false;
		}
		if (null != contents_temp) {
			Logger.getLogger().info("Load file sucessfully");
			file = FileLoader.getFile();
			max_index_date = contents_temp.size() - 1;
			clearData();
			contents = contents_temp;
			TipDataLoader.setData(contents);
			for (int i = 0; i < contents.size(); i++) {
				model_word.addElement(contents.get(i).getSheetName());
			}
			setHasContent(true);
			TiPCounter.restartCounter = true;
			synchronized (TiPCounter.getInstance()) {
				TiPCounter.getInstance().notifyAll();
			}
			list_word.setSelectedIndex(0);
			loadWord(list_word.getSelectedIndex());
			//			table_word.setSelectedIndex(0);
			text_title.setText(SystemConfig
					.getLanguageProp("WordPanel.menu.file")
					+ ":"
					+ file.getName().substring(0,
							file.getName().lastIndexOf(".")));
			Logger.getLogger().info("Load file done");
			if (contents_temp.size() == 0) {
				contents = new ArrayList<SheetBean>();
			}
		}
		if (TipWindow.isInitialized && TipWindow.isShowUp) {
			TipWindow.getInstance().setUnvisible();
		}
		return true;
	}

	public void addMouseEvent() {
		list_word.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 2) && (e.getButton() == 1)
						&& (null != contents)) {
					showDetailPanel();
				}
				if ((e.getClickCount() == 1) && (e.getButton() == 3)
						&& (null != list_word.getSelectedValue())) {
					//int index = list_word.locationToIndex(e.getPoint());
					//list_word.setSelectedIndex(index);
					pMenu_date.show(list_word, e.getX(), e.getY());
					Logger.getLogger().info("Show Popup Menu");
				}
			}

			public void mouseReleased(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 1) && (null != contents)
						&& (contents.size() != 0)&&list_word.getSelectedIndex()!=index_date) {
					Logger.getLogger().info(
							"Load sheet :"
									+ list_word.getSelectedValue().toString());
					loadWord(list_word.getSelectedIndex());
					if(table_word.getSelectedRow()==-1){
						table_word.setRowSelectionInterval(0, 0);
					}
				}
			}
		});

		table_word.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {
					pMenu_word.show(table_word, e.getX(), e.getY());
					Logger.getLogger().info("Show Popup Menu");
				}
				if ((e.getClickCount() == 2) && (e.getButton() == 1)
						&& (null != contents)) {
					showDetailPanel();
				}
			}
		});

	}

	public void addKeyBoardEvent() {

		list_word.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (null != contents && e.getSource() == list_word) {
					loadWord(list_word.getSelectedIndex());
					table_word.setRowSelectionInterval(0, 0);
				}
			}
		});
	}

	public void clearData() {
		this.model_word.clear();
		this.text_title.setText("");
		removeAllTableData();
		contents = null;
	}

	public void switchVisible() {
		if (this.isVisible() == false) {
			Logger.getLogger().info("WordPanel showUp");
			setShowedUp(true);
			this.setVisible(true);
		} else {
			Logger.getLogger().info("WordPanel shut down");
			setShowedUp(false);
			this.setVisible(false);
		}
	}

	public void showDetailPanel() {
		if (null == contents || list_word.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.OpenFirst"));
			Logger.getLogger().info("Message Popup,no excel now.");
			return;
		} else {
			SheetBean sheet = contents.get(list_word.getSelectedIndex());
			if (tableModel.getValueAt(table_word.getSelectedRow(), 0)
					.toString().equals("")
					&& !tableModel.getValueAt(0, 0).toString().equals("")) {
				table_word.setRowSelectionInterval(contents.get(index_date)
						.getWordBeanList().size() - 1, contents.get(index_date)
						.getWordBeanList().size() - 1);
			}
			if (null == detailPanel) {
				Logger.getLogger().info("Initializing DetailPanel");
				detailPanel = new DetailPanel(max_index_date, index_date,
						table_word, getIns(), sheet);

				ui = new CommonUIManager(detailPanel);
				CommonUIManager.setUI();
				ui.startUp();

				Logger.getLogger().info("Initialization Done");
			} else {
				Logger.getLogger().info("DetailPanel show up");
				detailPanel.setIndex_Date(index_date);
				detailPanel.setMax_index_date(max_index_date);
				detailPanel.setSheet(sheet);
				mainFrame.setDetailPanel(detailPanel.getIns());
				detailPanel.switchVisible();
				
				detailPanel.getData(table_word.getSelectedRow(), sheet);
				
			}

		}
	}

	public void addWordToList(int sheetIndex,WordBean bean) {

		SheetBean sheet = contents.get(sheetIndex);
		
		ArrayList<WordBean> wordlist = sheet.getWordBeanList();

		int index_row = -1;

		int physicalNum = -1;
		
		try {
			if (sheet.getWordBeanList().size() != 0) {
				index_row = wordlist.size()-1;
				physicalNum = wordlist.get(index_row).getPhysicalNum();
				FileLoader.addWord(sheetIndex, index_row, physicalNum,
						sheet.getWordBeanList().size(), sheet, bean);
			} else {
				FileLoader.addWord(sheetIndex, index_row, physicalNum,
						sheet.getWordBeanList().size(), sheet, bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(getIns(),
							SystemConfig.getLanguageProp("Message.Opened"));
			return;
		}
		
		if(index_date==sheetIndex){
			if(index_row==tableModel.getRowCount()-1){
				tableModel.addRow(new String[] { "", "", "" });
			}
			tableModel.setValueAt(bean.getWord(), index_row+1, 0);
			tableModel.setValueAt(bean.getPron(), index_row+1, 1);
			tableModel.setValueAt(bean.getWord_cn(), index_row+1, 2);
		}
		
		loadWord(sheetIndex);
		table_word.setRowSelectionInterval(index_row+1, index_row+1);
	}
	
	public boolean getSearch() {

		search = new SearchHandler(contents);
		boolean result = search.getSearch(searchFrame.getType(), searchFrame
				.getWord());
		searchFrame.switchVisible();
		if (result) {
			searchUpdateTable();
		}
		if (!mainFrame.isVisible()) {
			mainFrame.setting();
			mainFrame.setState(JFrame.NORMAL);
			SysIcon.switchMainFrameEnabled();
			SysIcon.changeIcon();
		}
		searchFrame.getText_cn().setText(null);
		searchFrame.getText_jp().setText(null);
		Logger.getLogger().info("Searh Successfully");
		return result;
	}

	public void searchUpdateTable() {
		list_word.setSelectedIndex(search.getIndex_sheetName());
		loadWord(list_word.getSelectedIndex());
		table_word.setRowSelectionInterval(search.getIndex_word(), search
				.getIndex_word());
	}

	public void UpdateTabledata() {
		loadWord(list_word.getSelectedIndex());
		TipDataLoader.setData(contents);
	}

	public WordPanel getIns() {
		return this;
	}

	public static ArrayList<SheetBean> getContents() {
		return contents;
	}

	public static void setContents(ArrayList<SheetBean> contents) {
		WordPanel.contents = contents;
	}

	public DetailPanel getDetailPanel() {
		return this.detailPanel;
	}

	public void run() {

		init();

	}

	public void loadWord(int index) {
		index_date = index;
		if (index_date != -1) {
			removeAllTableData();
			for (int i = 0; i < contents.get(index_date).getWordBeanList()
					.size(); i++) {
				Vector<String> rowVector = new Vector<String>();
				rowVector.addElement(contents.get(index_date).getWordBeanList()
						.get(i).getWord());
				rowVector.addElement(contents.get(index_date).getWordBeanList()
						.get(i).getPron());
				rowVector.addElement(contents.get(index_date).getWordBeanList()
						.get(i).getWord_cn());
				tableModel.addRow(rowVector);
			}
			if (contents.get(index_date).getWordBeanList().size() < 35) {
				for (int i = contents.get(index_date).getWordBeanList().size(); i < 35; i++) {
					tableModel.addRow(new String[] { "", "", "" });
				}
			}
		}
		if(table_word.getSelectedRow()==-1){
			table_word.setRowSelectionInterval(0, 0);
		}
	}

	public void initWordTable() {
		for (int i = tableModel.getRowCount(); i < 35; i++) {
			tableModel.addRow(new String[] { "", "", "" });
		}
	}

	public void setSheetSelected(int index) {
		list_word.setSelectedIndex(index);
	}

	public void removeAllTableData() {
		tableModel.getDataVector().removeAllElements();
		tableModel.fireTableDataChanged();
	}

	public static boolean isHasContent() {
		return isHasContent;
	}

	public static void setHasContent(boolean isHasContent) {
		WordPanel.isHasContent = isHasContent;
	}

	public static boolean isShowedUp() {
		return showedUp;
	}

	public static void setShowedUp(boolean showedUp) {
		WordPanel.showedUp = showedUp;
	}

	public void setSearchFrame(SearchFrame frame) {
		this.searchFrame = frame;
	}

	private void scrollToView() {

		Logger.getLogger().info("scrollToView");

		if (model_word.size() != 0 && list_word.getSelectedIndex() != -1) {

			int last_visible = list_word.getLastVisibleIndex();

			int first_visible = list_word.getFirstVisibleIndex();

			int index = list_word.getSelectedIndex();

			Rectangle rect = list_word.getCellBounds(index, index);

			if ((index + 1 > last_visible) || (index - 1 < first_visible)) {

				list_word.scrollRectToVisible(rect);
			}
		}
	}

	public void updateSheetName(String name) {
		Logger.getLogger().info("Update SheetName.");
		if (name.toLowerCase().equals(
				list_word.getSelectedValue().toString().toLowerCase())) {
			return;
		} else if (isHasSameSheetName(name)) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Same"));
			return;
		}
		int result = JOptionPane.showConfirmDialog(getIns(), SystemConfig
				.getLanguageProp("Message.Update"));
		if (result == 0) {
			int index = list_word.getSelectedIndex();
			try {
				FileLoader.updateSheetName(index, name);
			} catch (IOException e) {
				sheetModifyFrame.setVisible(false);
				sheetModifyFrame.dispose();
				JOptionPane.showMessageDialog(getIns(), SystemConfig
						.getLanguageProp("Message.Opened"));
				Logger.getLogger().info(
						SystemConfig.getLanguageProp("Message.Opened"));
				return;
			}
			model_word.set(index, name);
			contents.get(index).setSheetName(name);
		}
	}

	public void addNewSheet(String name) {
		Logger.getLogger().info("add New Sheet");
		if (isHasSameSheetName(name)) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Same"));
			return;
		}
		try {
			FileLoader.addSheet(name);
		} catch (IOException e) {
			sheetModifyFrame.setVisible(false);
			sheetModifyFrame.dispose();
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.Opened"));
			Logger.getLogger().info(
					SystemConfig.getLanguageProp("Message.Opened"));
			return;
		}
		model_word.addElement(name);
		SheetBean sheetBean = new SheetBean();
		sheetBean.setSheetName(name);
		contents.add(sheetBean);
		max_index_date = model_word.getSize() - 1;
		list_word.setSelectedIndex(max_index_date);
		scrollToView();
		removeAllTableData();
		loadWord(list_word.getSelectedIndex());
		if (null != table_word) {
			if (table_word.getSelectedRow() == -1) {
				table_word.setRowSelectionInterval(0, 0);
			}
			showDetailPanel();
		}
	}

	public void updateItemHistory() {
		Logger.getLogger().info("Updating File History");
		menu_file.remove(menuItem_history1);
		menu_file.remove(menuItem_history2);
		menu_file.remove(menuItem_history3);

		if (!SystemConfig.getSystemProp("file.history.file1").equals("")) {
			menuItem_history1.setText("1 "
					+ SystemConfig.getSystemProp("file.history.file1"));
			menu_file.add(menuItem_history1, 3);
		}
		if (!SystemConfig.getSystemProp("file.history.file2").equals("")) {
			menuItem_history2.setText("2 "
					+ SystemConfig.getSystemProp("file.history.file2"));
			menu_file.add(menuItem_history2, 4);
		}
		if (!SystemConfig.getSystemProp("file.history.file3").equals("")) {
			menuItem_history3.setText("3 "
					+ SystemConfig.getSystemProp("file.history.file3"));
			menu_file.add(menuItem_history3, 5);
		}
		Logger.getLogger().info("Update File History Successfully.");
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void updateLink() {
		menuItem_link1.setText(SystemConfig.getSystemProp("link_name1"));
		menuItem_link2.setText(SystemConfig.getSystemProp("link_name2"));
		menuItem_link3.setText(SystemConfig.getSystemProp("link_name3"));
	}

	public void loadHistory1() {
		Logger.getLogger().info("Open history1");
		String filePath = SystemConfig.getSystemProp("file.history.file1");
		if(filePath==null||filePath.equals("")){
			return;
		}
		File file = new File(filePath);

		//file does not exist.
		if (!file.exists()) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.file")
					+ " "
					+ SystemConfig.getSystemProp("file.history.file1")
					+ " " + SystemConfig.getLanguageProp("Message.NotExist"));
			Logger.getLogger().info("File history1 does not exist.");
			return;
		}

		// if historyFile is opened now.
		else if (null != FileLoader.getFile()
				&& SystemConfig.getSystemProp("file.history.file1").equals(
						FileLoader.getFile().getAbsoluteFile().toString())) {
			return;
		} else {
			Logger.getLogger().info(
					"File " + SystemConfig.getSystemProp("file.history.file1")
							+ " loaded successfully");
			FileLoader.setFile(file);
			loadContent();
		}
	}

	public void loadHistory2() {
		Logger.getLogger().info("Open history2");
		File file = new File(SystemConfig.getSystemProp("file.history.file2"));

		//file does not exist.
		if (!file.exists()) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.file")
					+ " "
					+ SystemConfig.getSystemProp("file.history.file2")
					+ " " + SystemConfig.getLanguageProp("Message.NotExist"));
			Logger.getLogger().info("File history1 does not exist.");
			return;
		} else {
			Logger.getLogger().info("File history2 loaded successfully");
			FileLoader.setFile(file);
			if (loadContent()) {
				FileLoader.updateHistoryByIndex(2);
				updateItemHistory();
			}
		}
	}

	public void loadHistory3() {
		Logger.getLogger().info("Open history3");
		File file = new File(SystemConfig.getSystemProp("file.history.file3"));

		//file does not exist.
		if (!file.exists()) {
			JOptionPane.showMessageDialog(getIns(), SystemConfig
					.getLanguageProp("Message.file")
					+ " "
					+ SystemConfig.getSystemProp("file.history.file3")
					+ " " + SystemConfig.getLanguageProp("Message.NotExist"));
			Logger.getLogger().info("File history3 does not exist.");
			return;
		} else {
			Logger.getLogger().info("File history3 loaded successfully");
			FileLoader.setFile(file);
			if (loadContent()) {
				FileLoader.updateHistoryByIndex(3);
				updateItemHistory();
			}
		}
	}

	protected void setClipboardText(String text) {

		Clipboard clip = this.getToolkit().getSystemClipboard();

		Transferable tText = new StringSelection(text);

		clip.setContents(tText, null);

	}

	private boolean isHasSameSheetName(String name) {
		boolean flag = false;
		for (int i = 0; i < list_word.getModel().getSize(); i++) {
			if (list_word.getModel().getElementAt(i).toString().toLowerCase()
					.equals(name.toLowerCase())) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
