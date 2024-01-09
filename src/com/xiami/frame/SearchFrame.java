package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class SearchFrame extends JDialog implements ActionListener, Runnable {


	private static final long serialVersionUID = 1L;
	private ImageButton button_close;
	private ImageButton button_jp;
	private ImageButton button_cn;
	private JTextField text_jp;
	private JTextField text_cn;
	private ImagePanel panel;
	private JLabel label_jp;
	private JLabel label_cn;
	private JLabel label_title;
	private int type;
	private String word;
	private Color bgColor;

	private WordPanel wordPanel;
	private static SearchFrame searchFrame;

	private static boolean isInitialized;

	public static SearchFrame getInstance(WordPanel wordPanel) {
		if (searchFrame == null) {
			synchronized (SearchFrame.class) {
				if (searchFrame == null) {
					searchFrame = new SearchFrame(wordPanel);
				}
			}
		}
		return searchFrame;
	}
	
	public SearchFrame(WordPanel wordPanel) {
		this.wordPanel = wordPanel;

	}

	public void init() {
		this.setSize(Keys.INITIAL_WIDTH_SEARCH, Keys.INITIAL_HEIGHT_SEARCH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_SEARCH / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_SEARCH / 2);
		this.setUndecorated(true);
		// this.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));
		new DragListener(getIns());

		createComponent();
		addComponent();
		setting();

		setInitialized(true);
	}

	public void createComponent() {

		label_title = new JLabel();
		label_title.setBounds(5, 3, Keys.INITIAL_WIDTH_SEARCH - 100, 22);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_SEARCH - 30, 3, 20, 20,
				getIns());
		button_cn = new ImageButton("", 180, 70, 50, 30, getIns());
		button_jp = new ImageButton("", 180, 150, 50, 30, getIns());
		
		text_jp = new JTextField();
		text_cn = new JTextField();
		label_jp = new JLabel();
		label_cn = new JLabel();
		label_cn.setBounds(10, 30, 250, 30);
		label_jp.setBounds(10, 110, 250, 30);
		text_cn.setBounds(10, 70, 150, 30);
		text_jp.setBounds(10, 150, 150, 30);
		
		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_SEARCH - 5, Keys.INITIAL_HEIGHT_SEARCH - 5);
		panel.setLayout(null);
		this.getContentPane().add(panel);
		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD))) {
			bgColor = new Color(237,235,193);
			label_title.setBackground(bgColor);
			panel.getRootPane().setBackground(bgColor);
		}else if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(Keys.THEME_NIMROD_SILVER)) {
			bgColor = new Color(234,239,240);
			label_title.setBackground(bgColor);
			panel.getRootPane().setBackground(bgColor);
		}
		
		new UndoHandler(text_cn);
		new UndoHandler(text_jp);
	}

	public void addComponent() {

		this.setContentPane(panel);

		panel.add(button_close);
		panel.add(button_cn);
		panel.add(button_jp);
		panel.add(text_cn);
		panel.add(text_jp);
		panel.add(label_cn);
		panel.add(label_jp);
		panel.add(label_title);

	}

	public void setting() {

		label_cn.setText(SystemConfig.getLanguageProp("SearchFrame.cn"));
		label_jp.setText(SystemConfig.getLanguageProp("SearchFrame.jp"));
		label_title.setText(SystemConfig
				.getLanguageProp("SearchFrame.labeltitle"));
		button_cn.setText(SystemConfig.getLanguageProp("SearchFrame.confirm"));
		button_jp.setText(SystemConfig.getLanguageProp("SearchFrame.confirm"));
		this.setTitle(SystemConfig.getLanguageProp("SearchFrame.title"));
		
		button_cn.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), 
	            JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		button_jp.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);

		String locale = SystemConfig.getSystemProp(Keys.LOCALE);
		if(locale.equals(Keys.LOCALE_EN)){
			text_jp.getInputContext().selectInputMethod(Locale.US);
		}else{
			text_jp.getInputContext().selectInputMethod(Locale.JAPAN);
		}
		
		text_cn.getInputContext().selectInputMethod(Locale.CHINA);
		
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		
		Font font = FontManager.getFont();
		Insets insets = new Insets(0,0,0,0);
		button_jp.setFont(font);
		button_jp.setMargin(insets);
		button_cn.setFont(font);
		button_cn.setMargin(insets);
		label_cn.setFont(font);
		label_jp.setFont(font);
		label_title.setFont(font);
		
	}
	
	public void run() {
		init();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_jp) {
			if (text_jp.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("SearchFrame.keyword")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else {
				setType(0);
				setWord(text_jp.getText());
				wordPanel.setSearchFrame(getIns());
				if(!wordPanel.getSearch()){
					JOptionPane.showMessageDialog(getIns(),
							SystemConfig.getLanguageProp("Message.NoWord"));
				}
			}
		}
		if (e.getSource() == button_cn) {
			if (text_cn.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("SearchFrame.keyword")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else {
				setType(1);
				setWord(text_cn.getText());
				wordPanel.setSearchFrame(getIns());
				if(!wordPanel.getSearch()){
					JOptionPane.showMessageDialog(getIns(),
							SystemConfig.getLanguageProp("Message.NoWord"));
				}
			}
		}
		if (e.getSource() == button_close) {
			this.switchVisible();
		}
	}

	public void switchVisible() {
		if (this.isVisible()) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
		}
	}
	
	public SearchFrame getIns() {
		return this;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public static boolean isInitialized() {
		return isInitialized;
	}

	public static void setInitialized(boolean isInitialized) {
		SearchFrame.isInitialized = isInitialized;
	}

	public JTextField getText_jp() {
		return text_jp;
	}

	public void setText_jp(JTextField text_jp) {
		this.text_jp = text_jp;
	}

	public JTextField getText_cn() {
		return text_cn;
	}

	public void setText_cn(JTextField text_cn) {
		this.text_cn = text_cn;
	}

}
