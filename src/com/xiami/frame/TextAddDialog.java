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
import com.xiami.bean.TextBean;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.TextLoader;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class TextAddDialog extends JDialog implements Runnable,ActionListener{
	

	private static final long serialVersionUID = 1L;
	
	private ImagePanel panel;
	private CommonScrollPane pane_content;
	private JTextField name;
	private ImageTextArea content;
	private ImageButton button_close;
	private ImageButton button_confirm;
	private JLabel label_name;
	private JLabel label_text;
			
	private TextPanel frame;
	
	public TextAddDialog(TextPanel frame){
		this.frame = frame;
	}
	
	public void init(){
		
		this.setSize(Keys.INITIAL_WIDTH_TEXT_ADD, Keys.INITIAL_HEIGHT_TEXT_ADD);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_TEXT_ADD / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_TEXT_ADD / 2);
		this.setUndecorated(true);
		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));
		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_TEXT_ADD - 5, Keys.INITIAL_HEIGHT_TEXT_ADD - 5);
		panel.setLayout(null);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_TEXT_ADD - 30, 3, 20, 20,
				getIns());
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_TEXT_ADD - 80, Keys.INITIAL_HEIGHT_TEXT_ADD/2, 60, 60,
				getIns());
		name = new CommonTextField(10, 30, Keys.INITIAL_WIDTH_TEXT_ADD-100, 30);
		content = new ImageTextArea();
		pane_content = new CommonScrollPane("", content);
		pane_content.setBounds(10, 90, Keys.INITIAL_WIDTH_TEXT_ADD-100, Keys.INITIAL_HEIGHT_TEXT_ADD - 100);
		
		label_name = new JLabel();
		label_text = new JLabel();
		label_name.setBounds(10, 0, 100, 30);
		label_text.setBounds(10, 60, 100, 30);
		
		button_confirm.setText(SystemConfig.getLanguageProp("TextAdd.add"));
		label_name.setText(SystemConfig.getLanguageProp("TextAdd.name"));
		label_text.setText(SystemConfig.getLanguageProp("TextAdd.text"));
		
		panel.add(button_close);
		panel.add(name);
		panel.add(pane_content);
		panel.add(button_confirm);
		panel.add(label_name);
		panel.add(label_text);
		
		button_confirm.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		Font font = FontManager.getFont();
		Insets insets = new Insets(0,0,0,0);
		button_confirm.setFont(font);
		button_confirm.setMargin(insets);
				
		new UndoHandler(name);
		new UndoHandler(content);
		
		this.getContentPane().add(panel);
		this.setModal(true);
		new DragListener(this);
		this.setAlwaysOnTop(true);
		this.setVisible(true);		

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button_close){
			this.setVisible(false);
			this.dispose();
		}
		if(e.getSource()==button_confirm){	
			if(name.getText().equals("")){
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("TextAdd.name")+" "+SystemConfig.getLanguageProp("Message.input"));
			}else if(content.getText().equals("")){
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("Message.content")+" "+SystemConfig.getLanguageProp("Message.input"));
			}else if(content.getText().length()>50000){
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("Message.Length")+" "+content.getText().length()+" "+SystemConfig.getLanguageProp("Message.Toolong"));
			}else{
				this.setVisible(false);
				this.dispose();
				addToTextXML();
				frame.loadText(frame.getTextCount());
			}
		}
	}
	
	public void addToTextXML(){
		
		TextBean bean = new TextBean();
		bean.setName(name.getText());
		bean.setContent(content.getText());
		TextLoader.addText(bean);
	}
	
	public void run() {
		init();
	}
	
	public TextAddDialog getIns(){
		return this;
	}

}
