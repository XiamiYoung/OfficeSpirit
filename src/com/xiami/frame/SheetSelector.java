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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.WordBean;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class SheetSelector extends JDialog implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private JComboBox sheets;
	private JTextField text_word;
	private JTextField text_word_cn;
	private JTextField text_pron;
	private JLabel label_sheet;
	private JLabel label_word;
	private JLabel label_chinese;
	private JLabel label_pron;
	private ImageTextArea content;
	private CommonScrollPane pane_content;
	private ImagePanel panel;
	private ImageButton button_close;
	private ImageButton button_confirm;;

	private WordPanel wordPanel;
	private WordBean wordBean;
	
	public SheetSelector(WordPanel wordPanel,WordBean wordBean){
		this.wordPanel = wordPanel;
		this.wordBean = wordBean;
	}

	public void initialize() {

		createComponents();

		setting();

		addComponents();

		initSheets();
				
		this.setUndecorated(true);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_SHEET_SELECTOR, Keys.INITIAL_HEIGHT_SHEET_SELECTOR);
		label_word = new JLabel(SystemConfig
				.getLanguageProp("WordAddFrame.word"));
		label_chinese = new JLabel(SystemConfig
				.getLanguageProp("WordAddFrame.chinese"));
		label_pron = new JLabel(SystemConfig
				.getLanguageProp("WordAddFrame.pron"));
		label_sheet = new JLabel(SystemConfig
				.getLanguageProp("WordPanel.sheet"));
		text_word = new CommonTextField(80, 60, Keys.INITIAL_WIDTH_SHEET_SELECTOR - 190, 30);
		text_pron = new CommonTextField(80, 100, Keys.INITIAL_WIDTH_SHEET_SELECTOR - 190, 30);
		text_word_cn = new CommonTextField(80, 140, Keys.INITIAL_WIDTH_SHEET_SELECTOR - 190, 30);	
		content = new ImageTextArea();
		sheets  = new JComboBox();
		pane_content = new CommonScrollPane(content);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_SHEET_SELECTOR - 30, 6, 20, 20,
				(ActionListener) this);
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_SHEET_SELECTOR - 75, 70, 50, 50,
				(ActionListener) this);

		button_confirm.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		new DragListener(this);
		
		new UndoHandler(text_word);
		new UndoHandler(text_word_cn);
		new UndoHandler(text_pron);
		new UndoHandler(content);
	}

	public void setting() {

		this.setSize(Keys.INITIAL_WIDTH_SHEET_SELECTOR, Keys.INITIAL_HEIGHT_SHEET_SELECTOR);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_SHEET_SELECTOR / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_SHEET_SELECTOR / 2);

		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD))) {
			this.setBackground(new Color(237, 235, 193));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else {
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}
		this.getContentPane().add(panel);
		
		button_confirm.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), 
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		button_confirm.setText(SystemConfig
				.getLanguageProp("WordAddFrame.confirm"));
		panel.setLayout(null);
		sheets.setBounds(80, 10, Keys.INITIAL_WIDTH_SHEET_SELECTOR - 190, 30);
		label_sheet.setBounds(10, 10, 80, 30);
		label_word.setBounds(10, 60, 80, 30);
		label_pron.setBounds(10, 100, 80, 30);
		label_chinese.setBounds(10, 140, 80, 30);	
		pane_content.setBounds(10, 180, Keys.INITIAL_WIDTH_SHEET_SELECTOR - 20,
				Keys.INITIAL_HEIGHT_SHEET_SELECTOR - 190);
		
		String locale = SystemConfig.getSystemProp(Keys.LOCALE);
		if(locale.equals(Keys.LOCALE_EN)){
			text_word.getInputContext().selectInputMethod(Locale.US);
			text_pron.getInputContext().selectInputMethod(Locale.US);
			content.getInputContext().selectInputMethod(Locale.US);
		}else{
			text_word.getInputContext().selectInputMethod(Locale.JAPAN);
			text_pron.getInputContext().selectInputMethod(Locale.JAPAN);
			content.getInputContext().selectInputMethod(Locale.JAPAN);
		}
		
		text_word_cn.getInputContext().selectInputMethod(Locale.CHINA);
		
		Font font = FontManager.getFont();
		Insets insets = new Insets(0,0,0,0);
		button_confirm.setFont(font);
		button_confirm.setMargin(insets);

	}

	public void addComponents() {

		panel.add(sheets);
		panel.add(label_sheet);
		panel.add(label_word);
		panel.add(label_chinese);
		panel.add(label_pron);
		panel.add(text_word);
		panel.add(text_word_cn);
		panel.add(text_pron);
		panel.add(pane_content);
		panel.add(button_close);
		panel.add(button_confirm);
	}

	public void run() {
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(false);
			this.dispose();
		}
		if (e.getSource() == button_confirm) {
			if (text_word.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("WordAddFrame.word")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else if (text_pron.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("WordAddFrame.pron")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else if (text_word_cn.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("WordAddFrame.chinese")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else {
				wordBean.setWord_cn(text_word_cn.getText());
				wordBean.setWord(text_word.getText());
				wordBean.setPron(text_pron.getText());
				wordBean.setSentns(content.getText());
				int index_sheet = sheets.getSelectedIndex();
				if(wordPanel.getDetailPanel()!=null&&wordPanel.getDetailPanel().isFromDetail()){
					wordPanel.getDetailPanel().getData(0, WordPanel.getContents().get(index_sheet));
					wordPanel.loadWord(index_sheet);
					wordPanel.setSheetSelected(index_sheet);
					wordPanel.getDetailPanel().setSheet(WordPanel.getContents().get(index_sheet));
					wordPanel.getDetailPanel().setIndex_Date(index_sheet);
					wordPanel.getDetailPanel().addWordToList(wordBean,true);
				}else{
					wordPanel.setSheetSelected(index_sheet);
					wordPanel.addWordToList(sheets.getSelectedIndex(),wordBean);	
				}
				this.setVisible(false);
				this.dispose();
			}
		}
	}
	
	public SheetSelector getIns() {
		return this;
	}
	
	public void initSheets(){
		for(int i=0;i<WordPanel.getContents().size();i++){
			sheets.addItem(WordPanel.getContents().get(i).getSheetName());
		}
		sheets.setSelectedIndex(sheets.getItemCount()-1);
		text_word.setText(wordBean.getWord());
		text_pron.setText(wordBean.getPron());
		content.setText(wordBean.getSentns());
		
		content.setCaretPosition(0);
	}
}