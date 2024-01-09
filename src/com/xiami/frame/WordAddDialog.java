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

public class WordAddDialog extends JDialog implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField text_word;
	private JTextField text_word_cn;
	private JTextField text_pron;
	private JLabel label_word;
	private JLabel label_chinese;
	private JLabel label_pron;
	private ImageTextArea content;
	private CommonScrollPane pane_content;
	private ImagePanel panel;
	private ImageButton button_close;
	private ImageButton button_confirm;;

	private DetailPanel detailPanel;
	
	public WordAddDialog(DetailPanel DetailPanel) {
		this.detailPanel = DetailPanel;
	}

	public void initialize() {

		createComponents();

		setting();

		addComponents();

		this.setUndecorated(true);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_WORD_ADD, Keys.INITIAL_HEIGHT_WORD_ADD);
		label_word = new JLabel(SystemConfig
				.getLanguageProp("WordAddFrame.word"));
		label_chinese = new JLabel(SystemConfig
				.getLanguageProp("WordAddFrame.chinese"));
		label_pron = new JLabel(SystemConfig
				.getLanguageProp("WordAddFrame.pron"));
		text_word = new CommonTextField(80, 30, 150, 30);
		text_pron = new CommonTextField(80, 70, 150, 30);
		text_word_cn = new CommonTextField(80, 110, 150, 30);	
		content = new ImageTextArea();
		pane_content = new CommonScrollPane(content);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_WORD_ADD - 30, 6, 20, 20,
				(ActionListener) this);
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_WORD_ADD - 75, 60, 50, 50,
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

		this.setSize(Keys.INITIAL_WIDTH_WORD_ADD, Keys.INITIAL_HEIGHT_WORD_ADD);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_WORD_ADD / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_WORD_ADD / 2);

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
		label_word.setBounds(10, 30, 80, 30);
		label_pron.setBounds(10, 70, 80, 30);
		label_chinese.setBounds(10, 110, 80, 30);	
		pane_content.setBounds(10, 150, Keys.INITIAL_WIDTH_WORD_ADD - 20, 180);
		
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
			this.dispose();
			this.setVisible(false);
			this.validate();
		}
		if (e.getSource() == button_confirm) {
			if (text_word.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("WordAddFrame.word")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else if (text_pron.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("WordAddFrame.pron")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else if (text_word_cn.getText().equals("")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("WordAddFrame.chinese")+" "+SystemConfig.getLanguageProp("Message.input"));
			} else {
				WordBean bean = new WordBean();
				bean.setWord(text_word.getText());
				bean.setWord_cn(text_word_cn.getText());
				bean.setPron(text_pron.getText());
				bean.setSentns(content.getText());

				detailPanel.addWordToList(bean,false);

				this.setVisible(false);
				this.dispose();
			}
		}
	}
	public WordAddDialog getIns() {
		return this;
	}
}
