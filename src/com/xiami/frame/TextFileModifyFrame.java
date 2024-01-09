package com.xiami.frame;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class TextFileModifyFrame extends JDialog implements Runnable,
		ActionListener {

	private static final long serialVersionUID = 1L;
	private int type;
	private String originName;
	private ImagePanel panel;
	private JTextField text_name;
	private JLabel label;
	private ImageButton button_close;
	private ImageButton button_confirm;

	private TextPanel textPanel;

	public TextFileModifyFrame(TextPanel textPanel) {
		type = 0;
		this.textPanel = textPanel;
	}

	public TextFileModifyFrame(String name, TextPanel textPanel) {
		type = 1;
		this.originName = name;
		this.textPanel = textPanel;
	}

	public void init() {

		this.setSize(Keys.INITIAL_WIDTH_SHEET_MODIFY, Keys.INITIAL_HEIGHT_SHEET_MODIFY);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_SHEET_MODIFY / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_SHEET_MODIFY / 2);
		this.setUndecorated(true);
		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));
		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_SHEET_MODIFY - 5, Keys.INITIAL_HEIGHT_SHEET_MODIFY - 5);
		panel.setLayout(null);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_SHEET_MODIFY - 30, 3, 20, 20,
				getIns());
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_SHEET_MODIFY - 70, 100, 50, 30,
				getIns());
		text_name = new CommonTextField(10, 50, 220, 30);

		label = new JLabel();

		label.setBounds(10, 10, 100, 30);

		label.setText(SystemConfig.getLanguageProp("SheetModifyFrame.label"));
		button_confirm.setText(SystemConfig
				.getLanguageProp("SheetModifyFrame.confirm"));
		
		Font font = FontManager.getFont(16);
		label.setFont(font);
		button_confirm.setFont(font);
		
		if (null != originName) {
			text_name.setText(originName);
		}

		panel.add(button_close);
		panel.add(text_name);
		panel.add(button_confirm);
		panel.add(label);
		
		button_confirm.setFont(FontManager.getFont(16));
		
		button_confirm.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), 
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		new UndoHandler(text_name);

		this.getContentPane().add(panel);
		this.setModal(true);
		new DragListener(this);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	public void run() {
		init();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(false);
			this.dispose();
		}
		if (e.getSource() == button_confirm) {
			if (text_name.getText().equals("")||text_name.getText().toLowerCase().equals("textfileresource")) {
				JOptionPane.showMessageDialog(this, SystemConfig.getLanguageProp("SheetModifyFrame.label")+" "+SystemConfig.getLanguageProp("Message.input"));
			}else{
				this.setVisible(false);
				this.dispose();
				if (type == 0) {
					textPanel.addTextFile(text_name.getText().trim());
				} else {
					textPanel.updateTextFile(text_name.getText().trim());
				}				
			}
		}
	}

	public TextFileModifyFrame getIns() {
		return this;
	}
}

