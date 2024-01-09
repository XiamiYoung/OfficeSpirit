package com.xiami.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.ui.DragListener;
import com.xiami.ui.FontManager;

public class DateSelector extends JDialog implements ActionListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int year = 0;
	private int month = 0;
	private int day = 0;

	private ImagePanel panel;

	private ImagePanel panel_header;

	private ImageButton button_close;

	private JPanel Main = new JPanel();

	private JPanel jPanelDay = new JPanel();

	private JPanel jPanelMonth = new JPanel();

	private JTextField Year = new JTextField();

	private JTextField Month = new JTextField();

	private JButton MonthDown = new JButton();

	private JButton MonthUp = new JButton();

	private JPanel jPanelButton = new JPanel();

	private JButton YearDown = new JButton();

	private JButton YearUp = new JButton();

	private JLabel Out = new JLabel();

	private Locale l = Locale.CHINESE;

	private GregorianCalendar cal = new GregorianCalendar(l);

	private JPanel weekPanel = new JPanel();

	private JToggleButton[] days = new JToggleButton[42];

	private JPanel Days = new JPanel();
	
	private JComboBox choice = new JComboBox();
	
	private JButton button_confirm;
	
	public String result;
		
	private Color bgColor;

	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel4 = new JLabel();
	private JLabel jLabel5 = new JLabel();
	private JLabel jLabel6 = new JLabel();
	private JLabel jLabel7 = new JLabel();

	private JTextField fromTime;
	
	// 1  2  3  4  5  6  7  8  9  10 11 12
	private int[] mm = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public DateSelector(JTextField fromTime) {
		this.fromTime = fromTime;
		if(fromTime.getText().indexOf("-")!=-1){
			String[] dates = fromTime.getText().split("-");
			cal.set(Integer.parseInt(dates[0]), Integer.parseInt(dates[1])-1, Integer.parseInt(dates[2]));
		}
	}

	public DateSelector(int year, int month, int day) {
		cal.set(year, month, day);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DateSelector(GregorianCalendar calendar) {
		cal = calendar;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DateSelector(Date date) {
		cal.setTime(date);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {

		iniCalender();

		new DragListener(this);
		
		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));
		
		this.setLayout(null);
		
		this.setModal(true);
		
		this.setUndecorated(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setBounds(screenSize.width / 2 - Keys.INITIAL_WIDTH_DATOR/ 2,
				screenSize.height / 2 -Keys.INITIAL_HEIGHT_DATOR / 2, Keys.INITIAL_WIDTH_DATOR, Keys.INITIAL_HEIGHT_DATOR);
		
		if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD))) {
			bgColor = new Color(237, 235, 193);
			this.getContentPane().setBackground(bgColor);
		}else if (SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_NIMROD_SILVER))||SystemConfig.getSystemProp(Keys.THEME_DEFAULT).equals(SystemConfig.getSystemProp(Keys.THEME_PGS))) {
			bgColor = new Color(234,239,240);
			this.getContentPane().setBackground(bgColor);
		}else{
			bgColor = new Color(255,255,255);
		}
		panel_header = new ImagePanel(0, 0, Keys.INITIAL_WIDTH_DATOR - 5, 30);

		panel_header.setLayout(null);

		panel = new ImagePanel(3, 33, Keys.INITIAL_WIDTH_DATOR - 5, Keys.INITIAL_HEIGHT_DATOR - 35);
		
		choice.setBounds(10, 5, 80, 25);
		
		initChoice();
		
		button_confirm = new ImageButton("", Keys.INITIAL_WIDTH_DATOR - 85, 3, 40, 25,
				getIns());
		
		button_confirm.setText(SystemConfig
				.getLanguageProp("DateSelector.confirm"));
		
		panel_header.add(choice);
		
		panel_header.add(button_confirm);

		button_confirm.setFont(FontManager.getFont(16));
		
		this.getContentPane().add(panel_header);

		this.getContentPane().add(panel);
		
		if(!fromTime.getText().equals("")){
			if(fromTime.getText().indexOf("-")==-1){
				choice.setSelectedItem(fromTime.getText());
			}else{
				choice.setSelectedItem(fromTime.getText().split("-")[3]);
			}
		}
		
		panel.setLayout(new BorderLayout());
		//    this.setBorder(BorderFactory.createRaisedBevelBorder());
		//    panel.setMaximumSize(new Dimension(200, 200));
		//    panel.setMinimumSize(new Dimension(200, 200));
		//    panel.setPreferredSize(new Dimension(200, 200));

		Main.setLayout(new BorderLayout());
		Main.setBackground(bgColor);
		Main.setBorder(null);

		Out.setBackground(bgColor);
		Out.setHorizontalAlignment(SwingConstants.CENTER);
		Out.setMaximumSize(new Dimension(100, 19));
		Out.setMinimumSize(new Dimension(100, 19));
		Out.setPreferredSize(new Dimension(100, 19));

		button_close = new ImageButton("", Keys.INITIAL_WIDTH_DATOR - 35, 3, 30,25,
				getIns());

		button_close.setText(SystemConfig
				.getLanguageProp("Frame.close"));
		
		jLabel1.setBackground(bgColor);
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel1.setText(SystemConfig
				.getLanguageProp("DateSelector.son"));
		jLabel2.setBackground(bgColor);
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel2.setText(SystemConfig
				.getLanguageProp("DateSelector.sat"));
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel3.setText(SystemConfig
				.getLanguageProp("DateSelector.fri"));
		jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel4.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel4.setText(SystemConfig
				.getLanguageProp("DateSelector.thu"));
		jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel5.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel5.setText(SystemConfig
				.getLanguageProp("DateSelector.wed"));
		jLabel6.setBorder(null);
		jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel6.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel6.setText(SystemConfig
				.getLanguageProp("DateSelector.tue"));
		jLabel7.setBackground(bgColor);
		jLabel7.setBackground(bgColor);
		jLabel7.setBorder(null);
		jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel7.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel7.setText(SystemConfig
				.getLanguageProp("DateSelector.mon"));

		weekPanel.setBackground(bgColor);
		weekPanel.setBorder(BorderFactory.createEtchedBorder());
		weekPanel.setLayout(new GridLayout(1, 7));
		weekPanel.add(jLabel1, null);
		weekPanel.add(jLabel7, null);
		weekPanel.add(jLabel6, null);
		weekPanel.add(jLabel5, null);
		weekPanel.add(jLabel4, null);
		weekPanel.add(jLabel3, null);
		weekPanel.add(jLabel2, null);

		MonthUp.setAlignmentX((float) 0.0);
		MonthUp.setActionMap(null);

		jPanelMonth.setBackground(bgColor);
		jPanelMonth.setLayout(new BorderLayout());
		jPanelMonth.setBorder(BorderFactory.createEtchedBorder());

		Month.setBorder(null);
		Month.setHorizontalAlignment(SwingConstants.CENTER);
		Month.setEditable(false);
		Month.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Month_mouseClicked(e);
			}
		});
		Month.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Month_keyPressed(e);
			}
		});

		MonthDown.setBorder(null);
		MonthDown.setText("\u25C4");
		MonthDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MonthDown_actionPerformed(e);
			}
		});
		MonthUp.setBorder(null);
		MonthUp.setText("\u25BA");
		MonthUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MonthUp_actionPerformed(e);
			}
		});

		jPanelButton.setLayout(null);
		jPanelButton.setBorder(null);
		jPanelButton
				.addComponentListener(new java.awt.event.ComponentAdapter() {
					public void componentResized(
							java.awt.event.ComponentEvent evt) {
						jPanelButtonComponentResized(evt);
					}
				});

		Year.setBorder(BorderFactory.createEtchedBorder());
		Year.setMaximumSize(new Dimension(80, 25));
		Year.setMinimumSize(new Dimension(80, 25));
		Year.setPreferredSize(new Dimension(80, 25));
		Year.setHorizontalAlignment(SwingConstants.CENTER);
		Year.setEditable(false);
		Year.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Year_mouseClicked(e);
			}
		});
		Year.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Year_keyPressed(e);
			}
		});

		YearDown.setBorder(null);
		YearDown.setMaximumSize(new Dimension(16, 16));
		YearDown.setMinimumSize(new Dimension(16, 16));
		YearDown.setPreferredSize(new Dimension(16, 16));
		YearDown.setSize(new Dimension(16, 16));
		YearDown.setText(SystemConfig
				.getLanguageProp("DateSelector.yeardown"));
		YearDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				YearDown_actionPerformed(e);
			}
		});
		YearUp.setBorder(null);
		YearUp.setMaximumSize(new Dimension(16, 16));
		YearUp.setMinimumSize(new Dimension(16, 16));
		YearUp.setPreferredSize(new Dimension(16, 16));
		YearUp.setSize(new Dimension(16, 16));
		YearUp.setText(SystemConfig
				.getLanguageProp("DateSelector.yearup"));
		YearUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				YearUp_actionPerformed(e);
			}
		});

		jPanelDay.setLayout(new BorderLayout());

		Days.setLayout(new GridLayout(6, 7));
		Days.setBackground(bgColor);

		for (int i = 0; i < 42; i++) {
			days[i] = new JToggleButton();
			days[i].setBorder(null);
			days[i].setBackground(bgColor);
			days[i].setHorizontalAlignment(SwingConstants.CENTER);
			days[i].setHorizontalTextPosition(SwingConstants.CENTER);
			//days[i].setSize(l,l);
			days[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String day =((JToggleButton) e.getSource())
							.getText();
					if(day.length()<2){
						day = "0"+day;
					}
					
					setResult(Year.getText().substring(0,4)+"-"+getResultMonth()+"-"+day+"-"+choice.getSelectedItem());
					fromTime.setText(getResult());
					showDate();
					showDays();
					getIns().dispose();
				}
			});
			Days.add(days[i]);
		}

		panel.add(Main, BorderLayout.NORTH);
		panel.add(jPanelDay, BorderLayout.CENTER);
		panel.add(jPanelMonth, BorderLayout.SOUTH);

		panel_header.add(button_close);

		Main.add(Year, BorderLayout.CENTER);
		Main.add(Out, BorderLayout.WEST);
		Main.add(jPanelButton, BorderLayout.EAST);

		jPanelButton.add(YearUp);
		jPanelButton.add(YearDown);

		jPanelDay.add(weekPanel, BorderLayout.NORTH);
		jPanelDay.add(Days, BorderLayout.CENTER);

		jPanelMonth.add(Month, BorderLayout.CENTER);
		jPanelMonth.add(MonthDown, BorderLayout.WEST);
		jPanelMonth.add(MonthUp, BorderLayout.EAST);

		showMonth();
		showYear();
		showDate();
		showDays();

		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	void jPanelButtonComponentResized(java.awt.event.ComponentEvent evt) {
		YearUp.setLocation(0, 0);
		YearDown.setLocation(0, YearUp.getHeight());
		jPanelButton.setSize(YearUp.getWidth(), YearUp.getHeight() * 2);
		jPanelButton.setPreferredSize(new Dimension(YearUp.getWidth(), YearUp
				.getHeight() * 2));
		jPanelButton.updateUI();
	}

	void YearUp_actionPerformed(ActionEvent e) {
		year++;
		showYear();
		showDate();
		showDays();
	}

	void YearDown_actionPerformed(ActionEvent e) {
		year--;
		showYear();
		showDate();
		showDays();
	}

	void MonthDown_actionPerformed(ActionEvent e) {
		month--;
		if (month < 0) {
			month = 11;
			year--;
			showYear();
		}
		showMonth();
		showDate();
		showDays();
	}

	void MonthUp_actionPerformed(ActionEvent e) {
		month++;
		if (month == 12) {
			month = 0;
			year++;
			showYear();
		}
		showMonth();
		showDate();
		showDays();
	}

	void iniCalender() {
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
	}

	void showMonth() {
		Month.setText(Integer.toString(month + 1) + SystemConfig
				.getLanguageProp("DateSelector.month"));
	}

	void showYear() {
		Year.setText(Integer.toString(year) + SystemConfig
				.getLanguageProp("DateSelector.year"));
	}

	void showDate() {
		Out.setText(Integer.toString(year) + "-" + Integer.toString(month + 1)
				+ "-" + Integer.toString(day));
	}

	void showDays() {
		cal.set(year, month, 1);
		int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int n = mm[month];
		if (cal.isLeapYear(year) && month == 1)
			n++;
		int i = 0;
		for (; i < firstDayOfWeek - 1; i++) {
			days[i].setEnabled(false);
			days[i].setSelected(false);
			days[i].setText("");
		}
		int d = 1;
		for (; d <= n; d++) {
			days[i].setText(Integer.toString(d));
			days[i].setEnabled(true);
			if (d == day)
				days[i].setSelected(true);
			else
				days[i].setSelected(false);
			;
			i++;
		}
		for (; i < 42; i++) {
			days[i].setEnabled(false);
			days[i].setSelected(false);
			days[i].setText("");
		}
	}

	void SelectionYear() {
		Year.setSelectionStart(0);
		Year.setSelectionEnd(Year.getText().length());
	}

	void SelectionMonth() {
		Month.setSelectionStart(0);
		Month.setSelectionEnd(Month.getText().length());
	}

	void Month_mouseClicked(MouseEvent e) {
		//SelectionMonth();
		inputMonth();
	}

	void inputMonth() {
		String s;
		if (Month.getText().endsWith(SystemConfig
				.getLanguageProp("DateSelector.month"))) {
			s = Month.getText().substring(0, Month.getText().length() - 1);
		} else
			s = Month.getText();
		month = Integer.parseInt(s) - 1;
		this.showMe();
	}

	void Month_keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 10)
			inputMonth();
	}

	void Year_mouseClicked(MouseEvent e) {
		//SelectionYear();
		inputYear();
	}

	void Year_keyPressed(KeyEvent e) {
		//System.out.print(new Integer(e.getKeyChar()).byteValue());
		if (e.getKeyChar() == 10)
			inputYear();
	}

	void inputYear() {
		String s;
		if (Year.getText().endsWith(SystemConfig
				.getLanguageProp("DateSelector.year"))) {
			s = Year.getText().substring(0, Year.getText().length() - 1);
		} else
			s = Year.getText();
		year = Integer.parseInt(s);
		this.showMe();
	}

	public String getDate() {
		return Out.getText();
	}

	public void setDate(String date) {
		if (date != null) {
			StringTokenizer f = new StringTokenizer(date, "-");
			if (f.hasMoreTokens())
				year = Integer.parseInt(f.nextToken());
			if (f.hasMoreTokens())
				month = Integer.parseInt(f.nextToken());
			if (f.hasMoreTokens())
				day = Integer.parseInt(f.nextToken());
			cal.set(year, month, day);
		}
		this.showMe();
	}

	public void setTime(Date date) {
		cal.setTime(date);
		this.iniCalender();
		this.showMe();
	}

	public Date getTime() {
		return cal.getTime();
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
		cal.set(this.year, this.month, this.day);
		this.showMe();
	}

	public void setYear(int year) {
		this.year = year;
		cal.set(this.year, this.month, this.day);
		this.showMe();
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		cal.set(this.year, this.month, this.day);
		this.showMe();
	}

	public void showMe() {
		this.showDays();
		this.showMonth();
		this.showYear();
		this.showDate();
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_close) {
			this.setVisible(true);
			this.dispose();
			try {
				this.finalize();
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == button_confirm) {
			setResult(Year.getText().substring(0,4)+"-"+getResultMonth()+"-"+day+"-"+choice.getSelectedItem());
			fromTime.setText(getResult());
			showDate();
			showDays();
			getIns().dispose();
		}
	}

	public void run() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DateSelector getIns() {
		return this;
	}


	public String getResultMonth() {
		String resultMonth = Month.getText();
		if(Integer.parseInt(resultMonth) < 10){
			resultMonth = "0"+resultMonth;
		}
		resultMonth = resultMonth.substring(0,2);
		return resultMonth;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
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
}
