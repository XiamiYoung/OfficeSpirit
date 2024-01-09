package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import com.xiami.Component.CommonDefaultTableModel;
import com.xiami.Component.CommonScrollPane;
import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.bean.NoticeList;
import com.xiami.bean.TipBean;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.FileLoader;
import com.xiami.logic.TextFileLoader;
import com.xiami.logic.TextLoader;
import com.xiami.logic.TimeCounter;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class StatisDialog extends JDialog implements Runnable, ActionListener {

	
	private static final long serialVersionUID = 1L;
	private ImagePanel panel;
	private ImagePanel panel_word;
	private ImagePanel panel_buttom;
	private CommonScrollPane pane_table;
	private ImageButton button_close;
	private ImageButton button_confirm;
	private JLabel label_title;
	private JLabel label_time;
	private Color bgColor;
	private JTable table = new JTable();
	private CommonDefaultTableModel tableModel;
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
		
		try {
			loadStatis();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to get text statistics," +
					"please check text files.");
		}
		
		this.setVisible(true);
		
	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_STATIS, Keys.INITIAL_HEIGHT_STATIS);
		panel_word = new ImagePanel(10, 35, 500, 288);
		panel_buttom = new ImagePanel("", 0, Keys.INITIAL_HEIGHT_STATIS - 37,
				Keys.INITIAL_WIDTH_STATIS, 37);
		tableModel = new CommonDefaultTableModel();
		table.setModel(tableModel);
		pane_table = new CommonScrollPane(table);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_STATIS - 30, 6, 20, 20,
				(ActionListener) this);
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_STATIS - 70,
				Keys.INITIAL_HEIGHT_STATIS - 35, 50, 25, (ActionListener) this);
		bgColor = new Color(237, 235, 193);
		label_time = new JLabel("");
		label_title = new JLabel(SystemConfig
				.getLanguageProp("MainFrame.menuitem.statis"));
		separator_buttom = new JSeparator();
		separator_header = new JSeparator();
		
		new DragListener(this);

	}

	public void setting() {

		this.setSize(Keys.INITIAL_WIDTH_STATIS, Keys.INITIAL_HEIGHT_STATIS);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_STATIS / 2,
				screenSize.height / 2 -Keys.INITIAL_HEIGHT_STATIS / 2);

		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			this.setBackground(bgColor);
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else {
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}
		
		this.getContentPane().add(panel);
		label_title.setFont(FontManager.getFont(25,true));
//		label_title.setForeground(new Color(255,151,51));
		label_time.setBounds(20,Keys.INITIAL_HEIGHT_STATIS - 35, 350, 25);
		label_time.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		separator_header.setBounds(10, 30, Keys.INITIAL_WIDTH_STATIS-20, 22);
		separator_buttom.setBounds(10, 190, Keys.INITIAL_WIDTH_STATIS-20, 22);	
		label_title.setBounds(10, 5, Keys.INITIAL_WIDTH_STATIS - 100, 22);
		pane_table.setBounds(0,0, 480, 250);
		button_confirm.setText("OK");
		panel.setLayout(null);
		panel_word.setLayout(null);
		
		tableModel.addColumn("Item");
		tableModel.addColumn("Result");
		table.getColumnModel().getColumn(0).setPreferredWidth(272);
		table.getColumnModel().getColumn(1).setPreferredWidth(185);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableModel.isCellEditable(0, 0);
		
	}

	public void addComponents() {

		panel_word.add(pane_table);
		panel.add(button_close);
		panel.add(button_confirm);
		panel.add(panel_word);
		panel.add(panel_buttom);
		panel.add(separator_header);
		panel.add(separator_buttom);
		panel.add(label_title);
		panel.add(label_time);

	}
	
	@SuppressWarnings("unchecked")
	public void loadStatis() throws Exception {
		if(FileLoader.getFile()!=null){
			tableModel.addRow(new String[] { "Excel file : ",
					FileLoader.getFile().getName() });
			tableModel.addRow(new String[] { "Total words of excel file : ",
					String.valueOf(TipBean.getWord_jp().size()) });
			tableModel.addRow(new String[] { "Total sheets of excel file: ",
					String.valueOf(WordPanel.getContents().size()) });
			tableModel.addRow(new String[] {
					"Sheet with most words : ",
					String.valueOf(getMaxWordsSheet().get("name")) + "("
							+ String.valueOf(getMaxWordsSheet().get("max"))
							+ " words)" });
			tableModel.addRow(new String[] {
					"Sheet with least words : ",
					String.valueOf(getMinWordsSheet().get("name")) + "("
							+ String.valueOf(getMinWordsSheet().get("min"))
							+ " words)" });
			double average = 0;
			if(WordPanel.getContents().size()!=0){
				average = Math.ceil(((double)TipBean.getWord_jp().size()/(double)WordPanel.getContents().size()));
			}
			tableModel.addRow(new String[] { "Average word number of sheets: ",
					String.valueOf(new Double(average).intValue()) });
		}
		
		HashMap map = TextLoader.getTotalTextCounts();
		
		tableModel.addRow(new String[] { "Total text categories : ",
				String.valueOf(TextFileLoader.getTextFileCount()) });
		tableModel.addRow(new String[] { "Total texts collected : ",
				String.valueOf(map.get("total")) });
		tableModel.addRow(new String[] { "Text category with most texts: ",
				String.valueOf(map.get("name_max"))+"("+map.get("max")+" texts)" });
		tableModel.addRow(new String[] { "Text category with least texts: ",
				String.valueOf(map.get("name_min"))+"("+map.get("min")+" texts)" });
		tableModel.addRow(new String[] { "Total notices : ",
				String.valueOf(NoticeList.getNoticelist().size()) });
		tableModel.addRow(new String[] { "Current Tips time :",
				SystemConfig.getSystemProp("tip_time") +" minutes"});
		tableModel.addRow(new String[] { "Current Theme : ",
				SystemConfig.getSystemProp(Keys.THEME_DEFAULT) });
		
		String lang = "";
		String default_language = SystemConfig
		.getSystemProp(Keys.LANGUAGE_CONFIG_DEFAULT);
		if (default_language.equals(Keys.LANGUAGE_CONFIG_EN)) {
			lang = "English";
		} else if (default_language.equals(Keys.LANGUAGE_CONFIG_JP)) {
			lang = "日本語";
		}
		if (default_language.equals(Keys.LANGUAGE_CONFIG_CN)) {
			lang = "中文";
		}
		tableModel.addRow(new String[] { "Current Language : ",lang});
		tableModel.addRow(new String[] {
						"Startup with Windows : ",
						SystemConfig.getSystemProp(Keys.START_WITH_WINDOWS)
								.equals("0") ? "On" : "Off" });
		tableModel.addRow(new String[] {
				"Iconfied on close : ",
				SystemConfig.getSystemProp(Keys.IS_ICONFIED).equals("0") ? "On"
						: "Off" });
		tableModel.addRow(new String[] {
						"Load recent excel on start : ",
						SystemConfig.getSystemProp(Keys.IS_LOAD_WORD).equals(
								"0") ? "On" : "Off" });
		tableModel.addRow(new String[] {
						"Frames always attach to the side of screen : ",
						SystemConfig.getSystemProp(Keys.IS_WINDOW_ATTACHED)
								.equals("0") ? "On" : "Off" });
		tableModel.addRow(new String[] {
						"Icon animated : ",
						SystemConfig.getSystemProp(Keys.IS_ICON_DYNAMIC)
								.equals("0") ? "On" : "Off" });
		
		String link = SystemConfig.getSystemProp(Keys.SEARCH_ENGINE_TYPE);
		if(link.equals("0")){
			link = "Baidu";
		}else if(link.equals("1")){
			link = "Google";
		}else{
			link = "Random:Baidu,Google";
		}
		tableModel.addRow(new String[] {"Search Engine : ",link});
		
		int fill = 14 - tableModel.getRowCount();
		
		if(fill>0){
			for(int i=0;i<fill;i++){
				tableModel.addRow(new String[] {"",""});
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getMaxWordsSheet(){
		HashMap map = new HashMap();
		int max = 0;
		int index = -1;
		for(int i=0;i<WordPanel.getContents().size();i++){
			if(WordPanel.getContents().get(i).getWordBeanList().size()>=max){
				max = WordPanel.getContents().get(i).getWordBeanList().size();
				index = i;
			}
		}
		map.put("max",max);
		map.put("name",WordPanel.getContents().get(index).getSheetName());
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getMinWordsSheet(){
		HashMap map = new HashMap();
		int min = 10000;
		int index = -1;
		for(int i=0;i<WordPanel.getContents().size();i++){
			if(WordPanel.getContents().get(i).getWordBeanList().size()<=min){
				min = WordPanel.getContents().get(i).getWordBeanList().size();
				index = i;
			}
		}
		map.put("min",min);
		map.put("name",WordPanel.getContents().get(index).getSheetName());
		return map;
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
	
	public StatisDialog getIns() {
		return this;
	}

}
