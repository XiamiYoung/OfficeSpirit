package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.TimeCounter;
import com.xiami.ui.DragListener;

public class AboutDialog extends JDialog implements Runnable, ActionListener {

	
	private static final long serialVersionUID = 1L;
	private ImagePanel panel;
	private ImagePanel panel_pic;
	private ImagePanel panel_word;
	private ImagePanel panel_buttom;
	private ImageButton button_close;
	private ImageButton button_confirm;
	private JLabel label_title;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label_time;
	private Color bgColor;
	private JSeparator separator_header;
	private JSeparator separator_buttom;

	public void initialize() {

		createComponents();
		setting();
		addComponents();

		TimeCounter.getInstance().setLabel_total_time(label_time);
		
		this.setUndecorated(true);
		this.setModal(true);
//		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_ABOUT, Keys.INITIAL_HEIGHT_ABOUT);
		panel_pic = new ImagePanel("pic/about.jpg", 10, 40, 216, 143);
		panel_word = new ImagePanel(235, 30, 275, 198);
		panel_buttom = new ImagePanel("", 0, Keys.INITIAL_HEIGHT_ABOUT - 37,
				Keys.INITIAL_WIDTH_ABOUT, 37);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_ABOUT - 30, 6, 20, 20,
				(ActionListener) this);
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_ABOUT - 70,
				Keys.INITIAL_HEIGHT_ABOUT - 35, 50, 25, (ActionListener) this);
		bgColor = new Color(237, 235, 193);
		label1 = new JLabel("Office Language Spirit V1.0");
		label2 = new JLabel("Created By Xue Yuyang");
		label3 = new JLabel("Copyleft No Rights Reserved.");
		label_time = new JLabel("");
		label_title = new JLabel("About");
		separator_buttom = new JSeparator();
		separator_header = new JSeparator();
		
		new DragListener(this);

	}

	public void setting() {

		this.setSize(Keys.INITIAL_WIDTH_ABOUT, Keys.INITIAL_HEIGHT_ABOUT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_ABOUT / 2,
				screenSize.height / 2 -Keys.INITIAL_HEIGHT_ABOUT / 2);

		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			this.setBackground(bgColor);
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else {
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}
		
		this.getContentPane().add(panel);
		label1.setBounds(10, 20, 250, 30);
		label1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		label1.setForeground(Color.DARK_GRAY);
		label2.setBounds(10, 70, 250, 30);
		label2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		label2.setForeground(new Color(78,58,58));
		label3.setBounds(10, 120, 250, 30);
		label3.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		label3.setForeground(new Color(51,102,255));
		label_title.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		label_title.setForeground(new Color(255,151,51));
		label_time.setBounds(20,Keys.INITIAL_HEIGHT_ABOUT - 35, 350, 25);
		label_time.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		separator_header.setBounds(10, 30, Keys.INITIAL_WIDTH_ABOUT-20, 22);
		separator_buttom.setBounds(10, 190, Keys.INITIAL_WIDTH_ABOUT-20, 22);	
		label_title.setBounds(10, 5, Keys.INITIAL_WIDTH_ABOUT - 100, 22);
		button_confirm.setText("OK");
		panel.setLayout(null);
		panel_word.setLayout(null);
		panel_pic.setLayout(null);
		
	}

	public void addComponents() {

		panel_word.add(label1);
		panel_word.add(label2);
		panel_word.add(label3);
		panel.add(button_close);
		panel.add(button_confirm);
		panel.add(panel_pic);
		panel.add(panel_word);
		panel.add(panel_buttom);
		panel.add(separator_header);
		panel.add(separator_buttom);
		panel.add(label_title);
		panel.add(label_time);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(false);
			this.dispose();
			this.validate();
		}
		if (e.getSource() == button_confirm) {
			this.setVisible(false);
			this.dispose();
			this.validate();
		}

	}

	public void run() {
		initialize();
	}
	
	public AboutDialog getIns() {
		return this;
	}

}