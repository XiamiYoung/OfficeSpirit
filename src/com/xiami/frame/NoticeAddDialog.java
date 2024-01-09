package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.DateSelector;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.bean.NoticeBean;
import com.xiami.bean.NoticeList;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.NoticeLoader;
import com.xiami.logic.UndoHandler;
import com.xiami.ui.CommonUIManager;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class NoticeAddDialog extends JDialog implements Runnable, ActionListener , ItemListener{

	private static final long serialVersionUID = 1L;
	private JTextField text_name;
	private JTextField text_time;
	private JComboBox noticeTime;
	private JTextField text_user;
	private JLabel label_name;
	private JLabel label_lasting_time;
	private JLabel label_notice_time;
	private JLabel label_time;
	private JLabel label_user;
	private ImageTextArea content;
	private CommonScrollPane pane_content;
	private ImagePanel panel;
	private ImageButton button_close;
	private ImageButton button_confirm;
	private ImageButton button_date;
	private JComboBox choice ;
	private JLabel label_isCommon;
	private JCheckBox check_isCommon;
	private CommonUIManager ui;

	private NoticePanel noticePanel;

	private int INITIAL_HEIGHT = 340;
	private int INITIAL_WIDTH = 320;

	public NoticeAddDialog(NoticePanel noticePanel) {
		this.noticePanel = noticePanel;
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

		panel = new ImagePanel(0, 0, INITIAL_WIDTH, INITIAL_HEIGHT);
		label_name = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.eventname"));
		label_user = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.user"));
		label_time = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.fromtime"));
		label_lasting_time = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.noticetime"));
		label_notice_time = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.minutes"));
		text_name = new CommonTextField(90, 30, 150, 30);
		text_user = new CommonTextField(90, 70, 150, 30);
		text_time = new CommonTextField(90, 110, 110, 30);
		button_date = new ImageButton("", 210, 110, 80, 30,
				getIns());
		noticeTime = new JComboBox ();
		content = new ImageTextArea();
		pane_content = new CommonScrollPane(content);
		button_close = new ImageButton("", INITIAL_WIDTH - 30, 6, 20, 20,
				(ActionListener) this);
		button_confirm = new ImageButton("", INITIAL_WIDTH - 65, 40, 50, 50,
				(ActionListener) this);
		check_isCommon = new JCheckBox();
		choice = new JComboBox();
		
		new DragListener(this);
		initChoice();
		initNoticeTime();
		
		new UndoHandler(text_name);
		new UndoHandler(text_time);
		new UndoHandler(text_user);
		new UndoHandler(content);
	}

	public void setting() {

		this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - INITIAL_WIDTH / 2,
				screenSize.height / 2 - INITIAL_HEIGHT / 2);
	
		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			this.setBackground(new Color(237, 235, 193));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else {
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}
		this.getContentPane().add(panel);
		label_isCommon = new JLabel(SystemConfig
				.getLanguageProp("NoticePanel.iscommon"));
		button_confirm.setText(SystemConfig
		.getLanguageProp("DateSelector.confirm"));
		button_date.setText(SystemConfig.getLanguageProp("NoticePanel.dator"));
		panel.setLayout(null);
		label_name.setBounds(10, 30, 80, 30);
		label_user.setBounds(10, 70, 80, 30);
		label_time.setBounds(10, 110, 80, 30);
		label_lasting_time.setBounds(10, 150, 150, 30);
		label_isCommon.setBounds(10, 190, 100, 30);
		check_isCommon.setBounds(110, 192,25, 25);
		noticeTime.setBounds(160, 150, 80, 30);
		label_notice_time.setBounds(250, 150, 150, 30);
		choice.setBounds(160, 190, 100, 30);
		pane_content.setBounds(10, 230, INITIAL_WIDTH - 20, 100);
		
		Insets insets = new Insets(0,0,0,0);
		button_confirm.setFont(FontManager.getFont());
		button_confirm.setMargin(insets);
		button_date.setFont(FontManager.getFont(13));
		button_date.setMargin(insets);
		
		String locale = SystemConfig.getSystemProp(Keys.LOCALE);
		if(locale.equals(Keys.LOCALE_EN)){
			text_name.getInputContext().selectInputMethod(Locale.US);
			text_user.getInputContext().selectInputMethod(Locale.US);
			content.getInputContext().selectInputMethod(Locale.US);
		}else{
			text_name.getInputContext().selectInputMethod(Locale.JAPAN);
			text_user.getInputContext().selectInputMethod(Locale.JAPAN);
			content.getInputContext().selectInputMethod(Locale.JAPAN);
		}
		
		button_confirm.registerKeyboardAction(getIns(), 
	            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), 
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void addComponents() {
		
		text_time.setEnabled(false);
		choice.setEnabled(false);
		check_isCommon.addActionListener(this);
		choice.addItemListener(this);
		
		panel.add(label_name);
		panel.add(label_lasting_time);
		panel.add(label_notice_time);
		panel.add(label_time);
		panel.add(label_user);
		panel.add(text_name);
		panel.add(noticeTime);
		panel.add(text_time);
		panel.add(text_user);
		panel.add(pane_content);
		panel.add(button_close);
		panel.add(button_confirm);
		panel.add(label_isCommon);
		panel.add(check_isCommon);
		panel.add(choice);
		panel.add(button_date);
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
		if(e.getSource()==button_confirm){	
			if(text_name.getText().equals("")){
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("NoticePanel.eventname")+" "+SystemConfig.getLanguageProp("Message.input"));
			}
//			else if(text_user.getText().equals("")){
//				JOptionPane.showMessageDialog(this,
//						SystemConfig.getLanguageProp("NoticePanel.user")+" "+SystemConfig.getLanguageProp("Message.input"));
//			}
			else if(text_time.getText().equals("")){
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("NoticePanel.fromtime")+" "+SystemConfig.getLanguageProp("Message.input"));
			}else if (noticeTime.getSelectedItem().toString().equals("")) {
				JOptionPane
				.showMessageDialog(this, SystemConfig.getLanguageProp("NoticePanel.noticetime")+" "+SystemConfig.getLanguageProp("Message.input"));
			}else if(content.getText().equals("")){
				JOptionPane.showMessageDialog(this,
						SystemConfig.getLanguageProp("Message.content")+" "+SystemConfig.getLanguageProp("Message.input"));
			}else{
				this.setVisible(false);
				this.dispose();
				addToNoticeXML();
				NoticeList.setNoticelist(NoticeLoader.getNotice());
				noticePanel.loadNotice(noticePanel.getTextCount());
			}
		}
		if (e.getSource() == check_isCommon) {
			if(check_isCommon.isSelected()){
				choice.setEnabled(true);
				button_date.setEnabled(false);
				text_time.setText(choice.getSelectedItem().toString());
			}else{
				text_time.setText("");
				choice.setEnabled(false);
				button_date.setEnabled(true);
			}
		}
		if (e.getSource() == button_date) {
			DateSelector dator = new DateSelector(getFromTime());
			ui = new CommonUIManager(dator);
			CommonUIManager.setUI();
			ui.startUp();
		}
	}

	
	public void addToNoticeXML(){
		
		NoticeBean bean = new NoticeBean();

		bean.setName(text_name.getText());
		bean.setFromtime(text_time.getText());
		bean.setNoticetime(noticeTime.getSelectedItem().toString());
		bean.setUser(text_user.getText());
		bean.setContent(content.getText());
		if (check_isCommon.isSelected()) {
			bean.setIsCommon("true");
		} else {
			bean.setIsCommon("false");
		}
		bean.setIsCommon(String.valueOf(check_isCommon.isSelected()));
		
		NoticeLoader.addNotice(bean);

	}
	
	public NoticeAddDialog getIns(){
		return this;
	}
	
	public void initChoice(){
		int h = 0;
		int m = 0;
		for(int i=0;i<96;i++){
			if(m==60){
				h++;
				m = 0;
			}
			choice.addItem((h<10?"0"+String.valueOf(h):String.valueOf(h))+":"+(String.valueOf(m).equals("0")?String.valueOf(m)+"0":String.valueOf(m)));
			m+=15;
		}
	}
	
	public void initNoticeTime(){
		noticeTime.addItem("");
		noticeTime.addItem("1");
		noticeTime.addItem("5");
		noticeTime.addItem("10");
		noticeTime.addItem("15");
		noticeTime.addItem("25");
		noticeTime.addItem("30");
		noticeTime.addItem("45");
		noticeTime.addItem("60");
		noticeTime.addItem("90");
		noticeTime.addItem("120");
		noticeTime.addItem("180");
		noticeTime.addItem("240");
		noticeTime.addItem("300");
		noticeTime.setSelectedItem("15");
	}
	
	public JTextField getFromTime(){
		return this.text_time;
	}

	public void itemStateChanged(ItemEvent arg0) {
		text_time.setText(choice.getSelectedItem().toString());
	}
}
