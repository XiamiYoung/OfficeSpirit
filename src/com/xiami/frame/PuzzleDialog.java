package com.xiami.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.logic.PuzzleCounter;
import com.xiami.ui.DragListener;

public class PuzzleDialog extends JDialog implements Runnable, ActionListener {

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
	private JLabel label_tip;
	private JCheckBox checkA;
	private JCheckBox checkB;
	private JCheckBox checkC;
	private ButtonGroup bg = new ButtonGroup();
	private Color bgColor;
	private JSeparator separator_header;
	private JSeparator separator_buttom;
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
	private Thread counterThread;
	private PuzzleCounter counter;
	private boolean isAlive = true;

	public PuzzleDialog(SheetBean sheetBean) {
		this.sheetBean = sheetBean;
	}

	public void initialize() {

		createComponents();
		setting();
		addComponents();

		indexList = getRandomIndexList(amount);

		switchEnabled();
		showTest();

		counter = new PuzzleCounter(getIns());

		counterThread = new Thread(counter);

		counterThread.start();

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
		label_tip = new JLabel();
		label_title = new JLabel();
		checkA = new JCheckBox();
		checkB = new JCheckBox();
		checkC = new JCheckBox();
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
		labelA.setBounds(70, 110, 450, 20);
		labelB.setBounds(70, 170, 450, 20);
		labelC.setBounds(70, 230, 450, 20);
		checkA.setBounds(30, 110, 25, 25);
		checkB.setBounds(30, 170, 25, 25);
		checkC.setBounds(30, 230, 25, 25);
		label_tip.setBounds(20, Keys.INITIAL_HEIGHT_TEST - 45, 450, 25);
		label_tip.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
		bg.add(checkA);
		bg.add(checkB);
		bg.add(checkC);
		separator_header.setBounds(10, 30, Keys.INITIAL_WIDTH_TEST - 20, 22);
		separator_buttom.setBounds(10, 270, Keys.INITIAL_WIDTH_TEST - 20, 22);
		label_title.setBounds(10, 5, Keys.INITIAL_WIDTH_TEST - 50, 22);
		label_title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		labelA.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		labelB.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		labelC.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		label_tip.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		//		label_title.setForeground(Color.PINK);
		amount = (sheetBean.getWordBeanList().size());
		label_title.setText("Test - 1 of " + amount + " ");
		button_confirm.setText("Statis");
		button_next.setText("Next");
		panel.setLayout(null);

		checkA.addActionListener(this);
		checkB.addActionListener(this);
		checkC.addActionListener(this);

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
		panel.add(checkA);
		panel.add(checkB);
		panel.add(checkC);
		panel.add(label_title);
		panel.add(label_tip);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(false);
			this.dispose();
			this.validate();
			setAlive(false);
		}
		if (e.getSource() == button_next) {
			if (bg.getSelection() != null) {
				showTest();
			}
		}
		if (e.getSource() == checkA || e.getSource() == checkB
				|| e.getSource() == checkC) {

			boolean flag = isCorrect();

			label_tip.setForeground(Color.RED);
			label_tip.setVisible(true);

			if (flag) {
				label_tip.setText("Correct!");
			} else {
				label_tip.setText("The answer is :" + exp[pos]);
			}

			switchEnabled();

			if (indexList.size() == 0) {
				popUpResult(true);
				return;
			}
			
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

	private void parseData(ArrayList<String> wordList,
			ArrayList<String> expList, ArrayList<String> pronList) {

		int size = indexList.size();
		int random = ran.nextInt(size);
		int indexWord = indexList.get(random);
		indexList.remove(random);

		word = wordList.get(indexWord);
		pron = pronList.get(indexWord);
		exp = expList.get(indexWord).split("@");

		pos = detectPos(exp);

		String str = label_title.getText();

		str = str.substring(0, 7) + ((int) total + 1)
				+ str.substring(str.indexOf(" of"), str.length());

		label_title.setText(str);
		labelWord.setText(word + " " + pron);
		labelA.setText("A:" + exp[0]);
		labelB.setText("B:" + exp[1]);
		labelC.setText("C:" + exp[2]);
	}

	private int detectPos(String[] exp) {
		for (int i = 0; i < exp.length; i++) {
			if (exp[i].indexOf("\"") != -1) {
				pos = i;
				exp[i] = exp[i].replaceAll("\"", "");
				break;
			}
		}
		return pos;
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

	private boolean isCorrect() {
		total++;
		if (checkA.isSelected() && pos == 0 || checkB.isSelected() && pos == 1
				|| checkC.isSelected() && pos == 2) {
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
		} else {
			checkA.setEnabled(true);
			checkB.setEnabled(true);
			checkC.setEnabled(true);
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

	public PuzzleDialog getIns() {
		return this;
	}

	public void popUpResult(boolean isShutDown) {

		String str = "Answered words : " + (int) total + "\n" + "Correct words : "
				+ (int) correct + "\n" + "Accuracy Rate : " + correct / total
				* 100 + "%\n";

		if (isShutDown) {

			setAlive(false);

			double timeWeight = ((((double)counter.getTime()+ 1.00)/25.00 ) < 0 ? 0 : (((double)counter.getTime()+ 1.00)/25.00 ));
			
			str += "\nYour final point is : \n= (Accuracy Rate) x 10 + (Correct words) x 1.5 + (Time bonus - Time Remains / 25)"
					+ "\n= "
					+ (correct / total * 10)
					+ " + "
					+ (int)correct * 1.5
					+ " + "
					+ timeWeight
					+ "\n= "
					+new BigDecimal((correct / total * 10) + correct*1.5 + timeWeight).setScale(4, BigDecimal.ROUND_CEILING);
		}

		JOptionPane.showMessageDialog(this, str);

		if (isShutDown) {
			this.setVisible(false);
			this.dispose();
			this.validate();
		}
	}

	public JLabel getLabel_title() {
		return label_title;
	}

	public void setLabel_title(JLabel label_title) {
		this.label_title = label_title;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public ArrayList<Integer> getIndexList() {
		return indexList;
	}

	public void setIndexList(ArrayList<Integer> indexList) {
		this.indexList = indexList;
	}

}