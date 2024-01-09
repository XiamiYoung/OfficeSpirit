package com.xiami.Component;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xiami.core.SystemConfig;
import com.xiami.frame.MainFrame;
import com.xiami.ui.FontManager;

public class MainTabbedPane extends JTabbedPane implements ChangeListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel1, panel2, panel3,panel4,panel5,panel6,panel7,panel8;
	private MainFrame mainFrame;

	public MainTabbedPane(MainFrame mainFrame, int x, int y, int width, int height) {
		this.mainFrame =  mainFrame;
		//super (a, b);
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		panel6 = new JPanel();
		panel7 = new JPanel();
		panel8 = new JPanel();

		this.add(panel1, 0);		
		this.add(panel2, 1);	
		this.add(panel3, 2);
		this.add(panel4, 3);	
		this.add(panel5, 4);	
		this.add(panel6, 5);	
		this.add(panel7, 6);	
		this.add(panel8, 6);	
				
		this.setSelectedIndex(0);
		
		this.setBounds(x, y, width, height);	
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);	
		this.getModel().addChangeListener(this);
		
	}

	public void setLanguage(){
		setTitleAt(0, SystemConfig.getLanguageProp("MainFrame.word"));
		setToolTipTextAt(0, SystemConfig.getLanguageProp("MainFrame.word"));
		setTitleAt(1, SystemConfig.getLanguageProp("MainFrame.text"));
		setToolTipTextAt(1, SystemConfig.getLanguageProp("MainFrame.text"));
		setTitleAt(2, SystemConfig.getLanguageProp("Dict.E2C"));
		setToolTipTextAt(2, SystemConfig.getLanguageProp("Dict.E2C"));
		setTitleAt(3, SystemConfig.getLanguageProp("Dict.C2E"));
		setToolTipTextAt(3, SystemConfig.getLanguageProp("Dict.C2E"));
		setTitleAt(4, SystemConfig.getLanguageProp("Dict.J2C"));
		setToolTipTextAt(4, SystemConfig.getLanguageProp("Dict.J2C"));
		setTitleAt(5, SystemConfig.getLanguageProp("Dict.C2J"));
		setToolTipTextAt(5, SystemConfig.getLanguageProp("Dict.C2J"));
		setTitleAt(6, SystemConfig.getLanguageProp("MainFrame.notice"));
		setToolTipTextAt(6, SystemConfig.getLanguageProp("MainFrame.notice"));
		setTitleAt(7, SystemConfig.getLanguageProp("MainFrame.phonetic"));
		setToolTipTextAt(7, SystemConfig.getLanguageProp("MainFrame.phonetic"));
		
		this.setFont(FontManager.getFont(20,true));
	}
	
	
	public void stateChanged(ChangeEvent e) {
		
		if (this.getSelectedIndex() == 0) {
			mainFrame.setWord();
		}else if (this.getSelectedIndex() == 1) {
			mainFrame.setText();
		}else if (this.getSelectedIndex() == 2) {
			MainFrame.setECDict();
		}else if (this.getSelectedIndex() == 3) {
			MainFrame.setCEDict();
		}else if (this.getSelectedIndex() == 4) {
			MainFrame.setJCDict();
		}else if (this.getSelectedIndex() == 5) {
			MainFrame.setCJDict();
		}else if (this.getSelectedIndex() == 6) {
			mainFrame.setNotice();
		}else if (this.getSelectedIndex() == 7) {
			mainFrame.setPhonetic();
		}

	}
}
