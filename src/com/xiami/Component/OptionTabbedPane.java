package com.xiami.Component;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xiami.core.SystemConfig;
import com.xiami.frame.OptionDialog;
import com.xiami.ui.FontManager;

public class OptionTabbedPane extends JTabbedPane implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1, panel2, panel3,panel4,panel5;
	private OptionDialog ins;

	public OptionTabbedPane(Object ins, int x, int y, int width, int height) {
		this.ins = (OptionDialog) ins;
		//super (a, b);
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();

		this.add(panel1, 0);	
		this.add(panel2, 1);
		this.add(panel3, 2);
		this.add(panel4, 3);
		this.add(panel5, 4);
		
		this.setSelectedIndex(0);
		
		this.setBounds(x, y, width, height);
		setPreferredSize(new Dimension(width, height));
		setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

		this.getModel().addChangeListener(this);
		
		setLanguage();
	}

	public void setLanguage(){
		setTitleAt(3, SystemConfig
				.getLanguageProp("option.header.theme"));
		setToolTipTextAt(3, SystemConfig
				.getLanguageProp("option.header.theme"));
		setTitleAt(2, SystemConfig
				.getLanguageProp("option.header.tips"));
		setToolTipTextAt(2, SystemConfig
				.getLanguageProp("option.header.tips"));
		setTitleAt(1, SystemConfig
				.getLanguageProp("option.header.language"));
		setToolTipTextAt(1, SystemConfig
				.getLanguageProp("option.header.language"));
		setTitleAt(0, SystemConfig
				.getLanguageProp("option.header.general"));
		setToolTipTextAt(0, SystemConfig
				.getLanguageProp("option.header.general"));
		setTitleAt(4, SystemConfig
				.getLanguageProp("Link.menu"));
		setToolTipTextAt(4, SystemConfig
				.getLanguageProp("Link.menu"));
		this.setFont(FontManager.getFont(16));
	}
	
	public void stateChanged(ChangeEvent e) {
		if (this.getSelectedIndex() == 1) {
			if(ins.isPageLink()){
				ins.switch_Page_Link(false);
				ins.setPageLink(false);
			}
			if(ins.isPageGeneral()){
				ins.switch_Page_General(false);
				ins.setPageGeneral(false);
			}
			if(ins.isPageTheme()){
				ins.switch_Page_Theme(false);
				ins.setPageTheme(false);
			}
			if(ins.isPageTip()){
				ins.switch_Page_Tip(false);
				ins.setPageTip(false);
			}
			ins.switch_Page_Language(true);
			ins.setPageLanguage(true);
		} else if (this.getSelectedIndex() == 3) {
			if(ins.isPageLink()){
				ins.switch_Page_Link(false);
				ins.setPageLink(false);
			}
			if(ins.isPageGeneral()){
				ins.switch_Page_General(false);
				ins.setPageGeneral(false);
			}
			if(ins.isPageLanguage()){
				ins.switch_Page_Language(false);
				ins.setPageLanguage(false);
			}
			if(ins.isPageTip()){
				ins.switch_Page_Tip(false);
				ins.setPageTip(false);
			}
			ins.switch_Page_Theme(true);
			ins.setPageTheme(true);
		}else if (this.getSelectedIndex() == 2) {
			if(ins.isPageLink()){
				ins.switch_Page_Link(false);
				ins.setPageLink(false);
			}
			if(ins.isPageGeneral()){
				ins.switch_Page_General(false);
				ins.setPageGeneral(false);
			}
			if(ins.isPageTheme()){
				ins.switch_Page_Theme(false);
				ins.setPageTheme(false);
			}
			if(ins.isPageLanguage()){
				ins.switch_Page_Language(false);
				ins.setPageLanguage(false);
			}
			ins.switch_Page_Tip(true);
			ins.setPageTip(true);
		}
		else if (this.getSelectedIndex() == 0) {
			if(ins.isPageLink()){
				ins.switch_Page_Link(false);
				ins.setPageLink(false);
			}
			if(ins.isPageTheme()){
				ins.switch_Page_Theme(false);
				ins.setPageTheme(false);
			}
			if(ins.isPageLanguage()){
				ins.switch_Page_Language(false);
				ins.setPageLanguage(false);
			}
			if(ins.isPageTip()){
				ins.switch_Page_Tip(false);
				ins.setPageTip(false);
			}
			ins.switch_Page_General(true);
			ins.setPageGeneral(true);
		}
		else if (this.getSelectedIndex() == 4) {		
			if(ins.isPageGeneral()){
				ins.switch_Page_General(false);
				ins.setPageGeneral(false);
			}
			if(ins.isPageLanguage()){
				ins.switch_Page_Language(false);
				ins.setPageLanguage(false);
			}
			if(ins.isPageTip()){
				ins.switch_Page_Tip(false);
				ins.setPageTip(false);
			}
			if(ins.isPageTheme()){
				ins.switch_Page_Theme(false);
				ins.setPageTheme(false);
			}
			ins.switch_Page_Link(true);
			ins.setPageLink(true);
		}
	}
}
