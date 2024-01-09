package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import com.xiami.Component.ImageButton;
import com.xiami.Component.ImagePanel;
import com.xiami.bean.SheetBean;
import com.xiami.bean.TipBean;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.ui.DragListener;

public class TestDialog extends JDialog implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private ImagePanel panel;
	private ImagePanel panel_buttom;
	private ImageButton button_close;
	private ImageButton button_confirm;
	private ImageButton button_next;
	private JLabel label_title;
	private JLabel labelWord;
	private JLabel labelA;
	private JLabel labelB;
	private JLabel labelC;
	private JLabel labelD;
	private JLabel label_tip;
	private JCheckBox checkA;
	private JCheckBox checkB;
	private JCheckBox checkC;
	private JCheckBox checkD;
	private ButtonGroup bg = new ButtonGroup();
	private Color bgColor;
	private JSeparator separator_header;
	private JSeparator separator_buttom;
	private int type;
	private int pos;
	private int amount;
	private float total = 0.0f;
	private float correct = 0.0f;
	private SheetBean sheetBean;
	private String word;
	private String pron;
	private String[] exp;
	private Random ran = new Random();
	private ArrayList<Integer> indexList = new ArrayList<Integer>();

	public TestDialog() {
		type = 0;//all
	}

	public TestDialog(SheetBean sheetBean) {
		this.sheetBean = sheetBean;
		type = 1;//unit
	}

	public void initialize() {

		createComponents();
		setting();
		addComponents();

		indexList = getRandomIndexList(amount);

		switchEnabled();
		showTest();

		this.setUndecorated(true);
		//		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	public void createComponents() {

		panel = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_TEST,
				Keys.INITIAL_HEIGHT_TEST);
		panel_buttom = new ImagePanel("", 0, Keys.INITIAL_HEIGHT_TEST - 37,
				Keys.INITIAL_WIDTH_TEST, 37);
		button_close = new ImageButton("", Keys.INITIAL_WIDTH_TEST - 30, 6, 20,
				20, (ActionListener) this);
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_TEST - 80,
				Keys.INITIAL_HEIGHT_TEST - 50, 60, 40, (ActionListener) this);
		button_next = new ImageButton("", Keys.INITIAL_WIDTH_TEST - 80,
				Keys.INITIAL_HEIGHT_TEST - 205, 60, 40, (ActionListener) this);
		bgColor = new Color(237, 235, 193);
		labelWord = new JLabel();
		labelA = new JLabel();
		labelB = new JLabel();
		labelC = new JLabel();
		labelD = new JLabel();
		label_tip = new JLabel();
		label_title = new JLabel();
		checkA = new JCheckBox();
		checkB = new JCheckBox();
		checkC = new JCheckBox();
		checkD = new JCheckBox();
		separator_buttom = new JSeparator();
		separator_header = new JSeparator();

		new DragListener(this);

	}

	public void setting() {

		this.setSize(Keys.INITIAL_WIDTH_TEST, Keys.INITIAL_HEIGHT_TEST);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - Keys.INITIAL_WIDTH_TEST / 2,
				screenSize.height / 2 - Keys.INITIAL_HEIGHT_TEST / 2);

		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals("Nimrod")) {
			this.setBackground(bgColor);
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else {
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}

		this.getContentPane().add(panel);
		labelWord.setBounds(30, 40, 450, 30);
		labelWord.setFont(new Font("Arial Unicode MS", Font.BOLD, 20));
		labelWord.setForeground(Color.BLUE);
		labelA.setBounds(70, 90, 450, 20);
		labelB.setBounds(70, 140, 450, 20);
		labelC.setBounds(70, 190, 450, 20);
		labelD.setBounds(70, 240, 450, 20);
		checkA.setBounds(30, 90, 25, 25);
		checkB.setBounds(30, 140, 25, 25);
		checkC.setBounds(30, 190, 25, 25);
		checkD.setBounds(30, 240, 25, 25);
		label_tip.setBounds(20, Keys.INITIAL_HEIGHT_TEST - 45, 450, 25);
		label_tip.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
		bg.add(checkA);
		bg.add(checkB);
		bg.add(checkC);
		bg.add(checkD);
		separator_header.setBounds(10, 30, Keys.INITIAL_WIDTH_TEST - 20, 22);
		separator_buttom.setBounds(10, 270, Keys.INITIAL_WIDTH_TEST - 20, 22);
		label_title.setBounds(10, 5, Keys.INITIAL_WIDTH_TEST - 100, 22);
		label_title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		labelA.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		labelB.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		labelC.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		labelD.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		label_tip.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		//		label_title.setForeground(Color.PINK);
		amount = (type == 0 ? TipBean.getWord_jp().size() : sheetBean
				.getWordBeanList().size());
		label_title.setText("Test - 1 of " + amount);
		button_confirm.setText("Statis");
		button_next.setText("Next");
		panel.setLayout(null);

		checkA.addActionListener(this);
		checkB.addActionListener(this);
		checkC.addActionListener(this);
		checkD.addActionListener(this);

	}

	public void addComponents() {

		panel.add(button_close);
		panel.add(button_confirm);
		panel.add(button_next);
		panel.add(panel_buttom);
		panel.add(separator_header);
		panel.add(separator_buttom);
		panel.add(labelWord);
		panel.add(labelA);
		panel.add(labelB);
		panel.add(labelC);
		panel.add(labelD);
		panel.add(checkA);
		panel.add(checkB);
		panel.add(checkC);
		panel.add(checkD);
		panel.add(label_title);
		panel.add(label_tip);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(false);
			this.dispose();
			this.validate();
		}
		if (e.getSource() == button_next) {
			if (bg.getSelection() != null) {
				showTest();
			}
		}
		if (e.getSource() == checkA || e.getSource() == checkB
				|| e.getSource() == checkC || e.getSource() == checkD) {

			boolean flag = isCorrect();

			label_tip.setForeground(Color.RED);
			label_tip.setVisible(true);

			if (flag) {
				label_tip.setText("Correct!");
			} else {
				label_tip.setText("The answer is :" + exp[pos]);
			}

			switchEnabled();

		}
		if (e.getSource() == button_confirm) {
			popUpResult(false);
		}
	}

	private void showTest() {

		if (indexList.size() == 0) {
			popUpResult(true);
			return;
		}
		                
		restoreDefault();

		if (type == 0) {//all
			parseData(TipBean.getWord_jp(), TipBean.getWord_cn(), TipBean
					.getWord_pron());
		} else {//unit
			ArrayList<String> wordList = new ArrayList<String>();
			ArrayList<String> expList = new ArrayList<String>();
			ArrayList<String> pronList = new ArrayList<String>();
			for (int i = 0; i < sheetBean.getWordBeanList().size(); i++) {
				wordList.add(sheetBean.getWordBeanList().get(i).getWord());
				expList.add(sheetBean.getWordBeanList().get(i).getWord_cn());
				pronList.add(sheetBean.getWordBeanList().get(i).getPron());
			}
			parseData(wordList, expList, pronList);
		}
	}

	private void parseData(ArrayList<String> wordList,
			ArrayList<String> expList, ArrayList<String> pronList) {

		exp = new String[4];

		int indexExp = ran.nextInt(expList.size());
		int size = indexList.size();
		int random = ran.nextInt(size);
		int indexWord = indexList.get(random);
		indexList.remove(random);

		pos = ran.nextInt(4);
		word = wordList.get(indexWord);
		pron = pronList.get(indexWord);
		exp[pos] = expList.get(indexWord);

		ArrayList<Integer> tempList = new ArrayList<Integer>();
		tempList.add(indexWord);

		for (int i = 0; i < exp.length; i++) {
			while(tempList.contains(indexExp)){
				indexExp = ran.nextInt(expList.size());
			}
			tempList.add(indexExp);
			if(exp[i] == null) {
				exp[i] = expList.get(indexExp);
			}
		}

		label_title.setText("Test - " + ((int) total+1) + " of " + +amount);
		labelWord.setText(word + " " + pron);
		labelA.setText("A:" + exp[0]);
		labelB.setText("B:" + exp[1]);
		labelC.setText("C:" + exp[2]);
		labelD.setText("D:" + exp[3]);
	}

	private boolean isCorrect() {
		total++;
		if (checkA.isSelected() && pos == 0 || checkB.isSelected() && pos == 1
				|| checkC.isSelected() && pos == 2 || checkD.isSelected()
				&& pos == 3) {
			correct++;
			return true;
		} else {
			return false;
		}
	}

	private void switchEnabled() {
		if (checkA.isEnabled()) {
			checkA.setEnabled(false);
			checkB.setEnabled(false);
			checkC.setEnabled(false);
			checkD.setEnabled(false);
		} else {
			checkA.setEnabled(true);
			checkB.setEnabled(true);
			checkC.setEnabled(true);
			checkD.setEnabled(true);
		}
	}

	private void restoreDefault() {
		label_tip.setVisible(false);
		bg.clearSelection();
		switchEnabled();
	}

	public void run() {
		initialize();
	}

	public TestDialog getIns() {
		return this;
	}

	private ArrayList<Integer> getRandomIndexList(int size) {

		ArrayList<Integer> result = new ArrayList<Integer>();

		int[] source = new int[size];
		for (int i = 0; i < source.length; i++) {
			source[i] = i;
		}

		HashMap<Double, Integer> map = new HashMap<Double, Integer>();

		double[] keyList = new double[source.length];

		double key = Math.random();

		for (int i = 0; i < source.length; i++) {

			while (map.containsKey(key)) {
				key = Math.random();
			}

			map.put(key, source[i]);
			keyList[i] = key;
			
		}

		Arrays.sort(keyList);

		for (int j = 0; j < keyList.length; j++) {
			result.add(map.get(keyList[j]));
		}

		return result;
	}

	private void popUpResult(boolean isShutDown){
		JOptionPane.showMessageDialog(this, "Total words : " + (int) total + "\n" 
				+ "Correct words : "+ (int) correct + "\n" 
				+ "Accuracy Rate : " + correct / total * 100+ "%");
		if(isShutDown){
			this.setVisible(false);
			this.dispose();
			this.validate();
		}
	}
	
}