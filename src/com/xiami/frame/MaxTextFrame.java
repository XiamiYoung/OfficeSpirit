package com.xiami.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.TextBean;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.TextLoader;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class MaxTextFrame extends JFrame implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_file = new JMenu();
	private JMenu menu_edit = new JMenu();
	private JMenu menu_help = new JMenu();
	private JMenuItem menuItem_Save = new JMenuItem();
	private JMenuItem menuItem_SaveAs = new JMenuItem();
	private JMenuItem menuItem_Back = new JMenuItem();
	private JMenuItem menuItem_About = new JMenuItem();
	private JMenuItem menuItem_Statis = new JMenuItem();
	private JMenuItem menuBarItem_Copy = new JMenuItem();
	private JMenuItem menuBarItem_Paste = new JMenuItem();
	private JMenuItem menuBarItem_Cut = new JMenuItem();
	private JMenuItem menuBarItem_Undo = new JMenuItem();
	private JMenuItem menuBarItem_SelectAll = new JMenuItem();
	private JMenuItem menuBarItem_Delete = new JMenuItem();
	private CommonScrollPane pane_text;
	private ImagePanel panel;
	private ImageTextArea textArea;
	private TextBean textBean;
	private JPopupMenu pMenu = new JPopupMenu();
	private JMenuItem menuItem_Copy = new JMenuItem();
	private JMenuItem menuItem_Cut = new JMenuItem();
	private JMenuItem menuItem_Paste = new JMenuItem();
	private JMenuItem menuItem_Undo = new JMenuItem();
	private JMenuItem menuItem_SelectAll = new JMenuItem();
	private JMenuItem menuItem_Delete = new JMenuItem();
	private CommonUIManager ui;
	private ImageIcon imageIcon;
	public static boolean isInitialzed = false;

	private JScrollBar bar;

	private TextPanel textPanel;
	private UndoHandler undoHandler;

	private int bookMark = 0;

	private static final int INITIAL_HEIGHT = Toolkit.getDefaultToolkit()
			.getScreenSize().height * 2 / 3;
	private static final int INITIAL_WIDTH = Toolkit.getDefaultToolkit()
			.getScreenSize().width * 2 / 3;

	public MaxTextFrame(TextPanel obj) {
		textPanel = obj;
	}

	public void init() {

		this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		this.setResizable(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - INITIAL_WIDTH / 2,
				screenSize.height / 2 - INITIAL_HEIGHT / 2);
		new DragListener(getIns());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		createComponent();
		addComponent();
		setting();
		isInitialzed = true;
	}

	public void createComponent() {

		panel = new ImagePanel();
		panel = new ImagePanel(0, 0, INITIAL_WIDTH - 5, INITIAL_HEIGHT - 5);
		textArea = new ImageTextArea();
		textArea.setLineWrap(true);
		pane_text = new CommonScrollPane("", textArea);
		imageIcon = new ImageIcon("icons/title.gif");

		bar = pane_text.getVerticalScrollBar();

		addKeyBoardEvent();
		addMouseEvent();
		addFocusEvent();
		addItemEvent();
		addWindowEvent();

		undoHandler = new UndoHandler(textArea);
	}

	public void addComponent() {

		this.setContentPane(panel);

		panel.setLayout(new BorderLayout());

		panel.add(menubar, BorderLayout.NORTH);
		panel.add(pane_text, BorderLayout.CENTER);
		menubar.add(menu_file);
		menubar.add(menu_edit);
		menubar.add(menu_help);
		menu_file.add(menuItem_Save);
		menu_file.add(menuItem_SaveAs);
		menu_file.addSeparator();
		menu_file.add(menuItem_Back);
		menu_help.add(menuItem_Statis);
		menu_help.add(menuItem_About);
		menu_edit.add(menuBarItem_Copy);
		menu_edit.add(menuBarItem_Cut);
		menu_edit.add(menuBarItem_Paste);
		menu_edit.add(menuBarItem_Undo);
		menu_edit.addSeparator();
		menu_edit.add(menuBarItem_SelectAll);
		menu_edit.add(menuBarItem_Delete);

		menuItem_Save.addActionListener(this);
		menuItem_SaveAs.addActionListener(this);
		menuItem_Back.addActionListener(this);
		menuItem_About.addActionListener(this);
		menuItem_Statis.addActionListener(this);
		menuItem_Copy.addActionListener(this);
		menuItem_Cut.addActionListener(this);
		menuItem_Paste.addActionListener(this);
		menuItem_Undo.addActionListener(this);
		menuItem_SelectAll.addActionListener(this);
		menuItem_Delete.addActionListener(this);
		menuBarItem_Copy.addActionListener(this);
		menuBarItem_Cut.addActionListener(this);
		menuBarItem_Paste.addActionListener(this);
		menuBarItem_Undo.addActionListener(this);
		menuBarItem_SelectAll.addActionListener(this);
		menuBarItem_Delete.addActionListener(this);
		

		pMenu.add(menuItem_Copy);
		pMenu.add(menuItem_Cut);
		pMenu.add(menuItem_Paste);
		pMenu.add(menuItem_Undo);
		pMenu.addSeparator();
		pMenu.add(menuItem_SelectAll);
		pMenu.add(menuItem_Delete);

		KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK);
		menuItem_Save.setAccelerator(saveKeyStroke);
		
		menuItem_Back.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_M,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
				
		menuItem_Back.setToolTipText("back to text frame");
	}

	public void setLanguage() {

		menu_file.setText(SystemConfig.getLanguageProp("TextPanel.menu.file"));
		menu_edit.setText(SystemConfig.getLanguageProp("MaxText.edit"));
		menu_help.setText(SystemConfig.getLanguageProp("TextPanel.menu.help"));
		menuItem_Back.setText(SystemConfig.getLanguageProp("MaxText.back"));
		menuItem_About.setText(SystemConfig
				.getLanguageProp("TextPanel.menuitem.about"));	
		menuItem_Statis.setText(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		menuItem_Save.setText(SystemConfig.getLanguageProp("MaxText.save"));
		menuItem_SaveAs.setText(SystemConfig.getLanguageProp("MaxText.saveas"));
		menuItem_Copy.setText(SystemConfig.getLanguageProp("MaxText.copy"));
		menuItem_Cut.setText(SystemConfig.getLanguageProp("MaxText.cut"));
		menuItem_Paste.setText(SystemConfig.getLanguageProp("MaxText.paste"));
		menuItem_Undo.setText(SystemConfig.getLanguageProp("MaxText.undo"));
		menuItem_SelectAll.setText(SystemConfig.getLanguageProp("MaxText.all"));
		menuItem_Delete.setText(SystemConfig.getLanguageProp("MaxText.delete"));
		menuBarItem_Copy.setText(SystemConfig.getLanguageProp("MaxText.copy"));
		menuBarItem_Cut.setText(SystemConfig.getLanguageProp("MaxText.cut"));
		menuBarItem_Paste
				.setText(SystemConfig.getLanguageProp("MaxText.paste"));
		menuBarItem_Undo.setText(SystemConfig.getLanguageProp("MaxText.undo"));	
		menuBarItem_SelectAll.setText(SystemConfig
				.getLanguageProp("MaxText.all"));
		menuBarItem_Delete.setText(SystemConfig
				.getLanguageProp("MaxText.delete"));
		
		Font font_menu = FontManager.getFont(15);
		menu_file.setFont(font_menu);
		menu_help.setFont(font_menu);
		menu_edit.setFont(font_menu);
	}

	public void setting() {

		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			menubar.setBackground(new Color(227, 225, 193));
		}
		panel.setBackground(new Color(237, 235, 193));

		setLanguage();

		this.setTitle(textPanel.getChoice_file().getSelectedItem().toString()+" - "+textBean.getName());
		this.setIconImage(imageIcon.getImage());
		this.setVisible(true);

		loadText();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == menuItem_Back) {
			switchVisible();
			textPanel.getMainFrame().setState(JFrame.NORMAL);
			textPanel.getMainFrame().setVisible(true);
			SysIcon.setSource(0);
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
		if (e.getSource() == menuItem_Copy || e.getSource() == menuBarItem_Copy) {
			textArea.copy();
		}
		if (e.getSource() == menuItem_Cut || e.getSource() == menuBarItem_Cut) {
			textArea.cut();
		}
		if (e.getSource() == menuItem_Paste
				|| e.getSource() == menuBarItem_Paste) {
			textArea.paste();
		}
		if (e.getSource() == menuItem_Undo || e.getSource() == menuBarItem_Undo) {
			undoHandler.undo();
		}
		if (e.getSource() == menuItem_SelectAll
				|| e.getSource() == menuBarItem_SelectAll) {
			textArea.selectAll();
		}
		if (e.getSource() == menuItem_Delete
				|| e.getSource() == menuBarItem_Delete) {
			textArea.replaceSelection("");
		}
		if (e.getSource() == menuItem_Save) {
			textBean.setContent(textArea.getText());
			TextLoader.updateTextBean(textBean);
			int index = 0;
			for (int i = 0; i < textPanel.getContents().size(); i++) {
				if (textPanel.getContents().get(i).getUUID().equals(
						textBean.getUUID())) {
					index = i;
					break;
				}
			}
			textPanel.loadText(index);
		}
		if (e.getSource() == menuItem_SaveAs) {
			fileSaveTo();
		}
	}
	
	public void loadText() {
		textArea.setText(textBean.getContent());
		bookMark = Integer.parseInt(textBean.getBookmark());
		SwingUtilities.invokeLater(scrollToBookMark);
	}

	public void addMouseEvent() {

		//double click text to make highlight
		textArea.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getButton() == 3)) {

					menuItem_Copy.setEnabled(isCanCopy());
					menuItem_Paste.setEnabled(isClipboardString());
					menuItem_Cut.setEnabled(isCanCopy());
					menuItem_Undo.setEnabled(undoHandler.canUndo());
					menuItem_Delete.setEnabled(isCanCopy());

					pMenu.show(textArea, e.getX(), e.getY());
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

	public void addKeyBoardEvent() {
		textArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (isScrollChanged(bar.getValue())) {
//					bookMark = bar.getValue();
//					textBean.setBookmark(String.valueOf(bookMark));
//					TextLoader.updateBookMark(textBean);
				}
			}
		});
	}

	public void addItemEvent() {
		menu_edit.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == menu_edit) {
					menuBarItem_Copy.setEnabled(isCanCopy());
					menuBarItem_Paste.setEnabled(isClipboardString());
					menuBarItem_Cut.setEnabled(isCanCopy());
					menuBarItem_Undo.setEnabled(undoHandler.canUndo());
					menuBarItem_Delete.setEnabled(isCanCopy());
				}
			}
		});
	}

	public void addWindowEvent() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {				
				setVisible(false);
				textPanel.getMainFrame().setState(JFrame.NORMAL);
				textPanel.getMainFrame().setVisible(true);
				SysIcon.setSource(0);
			}
		});
	}

	public void clearData() {
		this.textArea.setText("");
	}

	public void switchVisible() {
		if (this.isVisible() == false) {
			this.toFront();
			this.setAlwaysOnTop(true);
			this.setVisible(true);
			this.setExtendedState(JFrame.NORMAL);
			this.setAlwaysOnTop(false);
		} else {
			this.setVisible(false);
		}
	}

	public MaxTextFrame getIns() {
		return this;
	}

	public void run() {
		init();
	}

	public void addFocusEvent() {

		bar.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (isScrollChanged(bar.getValue())) {
//					bookMark = bar.getValue();
//					textBean.setBookmark(String.valueOf(bookMark));
//					TextLoader.updateBookMark(textBean);
				}
			}
		});
	}

	Runnable scrollToBookMark = new Runnable() {
		public void run() {
			bar.setValue(bookMark);
		}
	};

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

	private void fileSaveTo() {
		JFileChooser fileChooser = null;
		if (SystemConfig.getSystemProp("file.history.path1") != null) {
			fileChooser = new JFileChooser(SystemConfig
					.getSystemProp("file.history.path1"));
		} else {
			fileChooser = new JFileChooser();
		}
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".txt")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "*.txt";
			}
		});
		int op = fileChooser.showSaveDialog(this);
		if (op == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File file = fileChooser.getSelectedFile();
		save(new File(file.getAbsolutePath() + ".txt"));
	}

	private boolean save(File file) {
		if (file.exists()) {
			int op = JOptionPane.showConfirmDialog(this, "OverWrite the file?",
					"OverWrite", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (op) {
			case JOptionPane.YES_OPTION:
				break;
			case JOptionPane.NO_OPTION:
				return false;
			case JOptionPane.CANCEL_OPTION:
				return false;
			}
		}
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			String[] content = textArea.getText().split("\n");
			for (int i = 0; i < content.length; i++) {
				bw.write(content[i]);
				bw.newLine();
			}

			bw.close();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this,
					"Error in saving,Maybe the file is opened.");
			return false;
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}
		return true;
	}
	
	public boolean isScrollChanged(int value) {
		return bookMark != value;
	}

	public TextBean getTextBean() {
		return textBean;
	}

	public void setTextBean(TextBean textBean) {
		this.textBean = textBean;
	}

}
