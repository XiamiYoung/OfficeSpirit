package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.CommonTextField;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.Component.ImageTextArea;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.TiPCounter;
import com.xiami.logic.TipDataLoader;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class TipWindow extends JWindow implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField word_jp;
	private JTextField word_cn;
	private JTextField pron;
	private JLabel lable_jp;
	private JLabel lable_cn;
	private JLabel lable_pron;
	private ImageTextArea sentns;
	private CommonScrollPane pane_sentns;
	private ImagePanel panel;
	private ImageButton button_close;
	private ImageButton button_pre;
	private ImageButton button_next;
	private TipDataLoader tiploader = new TipDataLoader();
	private static TipWindow tipWindow;
	public static boolean isInitialized = false;
	public static boolean isShowUp = false;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Color bgColor;

	// type 0:random 1:order
	private int type = 0;

	public static TipWindow getInstance() {
		if (null == tipWindow) {
			synchronized (TipWindow.class) {
				if (null == tipWindow) {
					tipWindow = new TipWindow();
				}
			}
		}
		return tipWindow;
	}

	public void initialize() {

		createComponents();

		setting();

		addComponents();

		isInitialized = true;

		isShowUp = true;

		getTipsData(type, 1, true);
	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_TIP_WINDOW, Keys.INITIAL_HEIGHT_TIP_WINDOW);
		lable_jp = new JLabel();
		lable_cn = new JLabel();
		lable_pron = new JLabel();
		word_jp = new CommonTextField(100, 40, 140, 30);
		word_cn = new CommonTextField(100, 80, 140, 30);
		pron = new CommonTextField(100, 120, 140, 30);
		sentns = new ImageTextArea();
		pane_sentns = new CommonScrollPane(sentns);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_TIP_WINDOW - 30, 6, 20, 20,
				(ActionListener) this);
		button_pre = new ImageButton("", 10, 6, 60, 25, (ActionListener) this);
		button_next = new ImageButton("", 80, 6, 60, 25, (ActionListener) this);
		bgColor = new Color(237, 235, 193);
		new DragListener(this);
	}

	public void setting() {

		tipWindow.setSize(Keys.INITIAL_WIDTH_TIP_WINDOW, Keys.INITIAL_HEIGHT_TIP_WINDOW);
		tipWindow.setLocation(screenSize.width - Keys.INITIAL_WIDTH_TIP_WINDOW - 10,
				screenSize.height - Keys.INITIAL_HEIGHT_TIP_WINDOW - 100);
		tipWindow.setVisible(true);
		tipWindow.setAlwaysOnTop(true);
		tipWindow.setContentPane(panel);
		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD))) {
			bgColor = new Color(237,235,193);
			tipWindow.setBackground(bgColor);
		}else if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD_SILVER))) {
			bgColor = new Color(234,239,240);
			tipWindow.setBackground(bgColor);
		}else{
			bgColor = new Color(241,240,227);
			tipWindow.setBackground(bgColor);
		}
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));		
		panel.setLayout(null);
		lable_jp.setBounds(15, 40, 80, 30);
		lable_jp.setText(SystemConfig.getLanguageProp("TipWindow.word"));
		lable_cn.setBounds(15, 80, 80, 30);
		lable_cn.setText(SystemConfig.getLanguageProp("TipWindow.chinese"));
		lable_pron.setBounds(15, 120, 80, 30);
		lable_pron.setText(SystemConfig.getLanguageProp("TipWindow.pron"));
		button_pre.setText(SystemConfig.getLanguageProp("TipWindow.pre"));
		button_next.setText(SystemConfig.getLanguageProp("TipWindow.next"));
		pane_sentns.setBounds(10, 160, Keys.INITIAL_WIDTH_TIP_WINDOW - 20, 130);
		
		Font font_label = FontManager.getFont(16);
		lable_jp.setFont(font_label);
		lable_cn.setFont(font_label);
		lable_pron.setFont(font_label);
		
		Font font = FontManager.getFont(15);
		button_pre.setFont(font);
		button_next.setFont(font);
	}

	public void addComponents() {
		panel.add(lable_jp);
		panel.add(lable_cn);
		panel.add(lable_pron);
		panel.add(pron);
		panel.add(word_jp);
		panel.add(word_cn);
		panel.add(pane_sentns);
		panel.add(button_close);
		panel.add(button_pre);
		panel.add(button_next);
	}

	public void getTipsData(int type, int button,boolean isFirst) {
		type = Integer.parseInt(SystemConfig.getSystemProp("tip_type"));
		tiploader.ShowData(word_jp, word_cn, pron, sentns, type, button,isFirst);
	}

	public void run() {
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			setUnvisible();
		} else if (e.getSource() == button_pre) {
			getTipsData(type, 0, false);
		} else if (e.getSource() == button_next) {
			getTipsData(type, 1, false);
		}
	}

	public void showUp() {
		this.setVisible(false);
		setting();
		getTipsData(type, 1, false);
		tipWindow.setAlwaysOnTop(true);
		tipWindow.setVisible(true);
		//tipWindow.setAlwaysOnTop(false);
		isShowUp = true;
	}
	public void setUnvisible() {
		tipWindow.dispose();
		tipWindow.setVisible(false);
		isShowUp = false;
		if (SysIcon.getIsTipAllowed() == 0) {
			TiPCounter.restartCounter = true;
			synchronized (TiPCounter.getInstance()) {
				TiPCounter.getInstance().notify();
			}
		}
	}

}
