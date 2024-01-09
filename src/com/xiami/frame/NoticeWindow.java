package com.xiami.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.NoticeBean;
import com.xiami.bean.NoticeList;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.NoticeLoader;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class NoticeWindow extends JDialog implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField text_name;
	private JTextField text_time;
	private JTextField text_user;
	private JLabel label_name;
	private JLabel label_time;
	private JLabel label_user;
	private ImageTextArea content;
	private CommonScrollPane pane_content;
	private ImagePanel panel;
	private ImageButton button_close;
	private Color bgColor;
	private int x;
	private int y;

	private NoticeBean bean;


	public NoticeWindow(NoticeBean bean, int x, int y) {
		this.bean = bean;
		this.x = x;
		this.y = y;
	}

	public void initialize() {

		createComponents();

		setting();

		addComponents();

		getNoticeData();
		
		this.setUndecorated(true);
		this.setModal(true);
		this.setVisible(true);
	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_NOTICE_WINDOW, Keys.INITIAL_HEIGHT_NOTICE_WINDOW);
		label_name = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.eventname"));
		label_user = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.user"));
		label_time = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.fromtime"));
		text_name = new CommonTextField(90, 30, 150, 30);
		text_user = new CommonTextField(90, 70, 150, 30);
		text_time = new CommonTextField(90, 110, 150, 30);
		content = new ImageTextArea();
		pane_content = new CommonScrollPane(content);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_NOTICE_WINDOW - 30, 6, 20, 20,
				(ActionListener) this);
		new DragListener(this);
	}

	public void setting() {

		this.setSize(Keys.INITIAL_WIDTH_NOTICE_WINDOW, Keys.INITIAL_HEIGHT_NOTICE_WINDOW);
		this.setLocation(x, y);
		
//		this.setAlwaysOnTop(true);
		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			bgColor = new Color(237, 239, 193);
			this.setBackground(bgColor);
		} else if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod_Silver")) {
			bgColor = new Color(234,239,240);
			this.setBackground(bgColor);
		}else{
			bgColor = new Color(241,240,227);
			this.setBackground(bgColor);
		}
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.setContentPane(panel);
		panel.setLayout(null);
		label_name.setBounds(10, 30, 80, 30);
		label_user.setBounds(10, 70, 80, 30);
		label_time.setBounds(10, 110, 80, 30);
		pane_content.setBounds(10, 160, Keys.INITIAL_WIDTH_NOTICE_WINDOW - 20,90);
		
		Font font_label = FontManager.getFont(13);
		label_name.setFont(font_label);
		label_user.setFont(font_label);
		label_time.setFont(font_label);
		
		text_name.setEditable(false);
		text_time.setEditable(false);
		text_user.setEditable(false);
		content.setEditable(false);

	}

	public void addComponents() {
		panel.add(label_name);
		panel.add(label_time);
		panel.add(label_user);
		panel.add(text_name);
		panel.add(text_time);
		panel.add(text_user);
		panel.add(pane_content);
		panel.add(button_close);
	}

	public void getNoticeData() {
		text_name.setText(bean.getName());
		text_time.setText(bean.getFromtime());
		text_user.setText(bean.getUser());
		content.setText(bean.getContent());
		content.setCaretPosition(0);
	}

	public void run() {
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			if(bean.getIsCommon().equals("false")){
				int result = JOptionPane.showConfirmDialog(this,
						SystemConfig.getLanguageProp("NoticePanel.deleteonshow"));
				if (result == 0) {
					NoticeLoader.removeNotice(bean.getUUID());
					NoticeList.setNoticelist(NoticeLoader.getNotice());
					if(MainFrame.getNoticePanel()!=null){
						MainFrame.getNoticePanel().loadNotice(0);
					}
				}else if(result == JOptionPane.CANCEL_OPTION){
					this.toFront();
					return;
				}
			}
			this.dispose();
			this.setVisible(false);
			this.validate();
		}
	}

}
