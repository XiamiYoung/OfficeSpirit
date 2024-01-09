package com.xiami.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;

public class TimeCounter implements Runnable{
	
	private JLabel label_time;
	private JLabel label_total_time;
	private static int total_time;
	private static TimeCounter timeCounter;
	
	public static TimeCounter getInstance() {
		if (timeCounter == null) {
			synchronized (TimeCounter.class) {
				if (timeCounter == null) {
					timeCounter = new TimeCounter();
				}
			}
		}
		return timeCounter;
	}


	private String getFormatTotalTime(int time){
		int h;
		int m;
		int s;
		h = time/3600;
		m = (time%3600)/60;
		s = time - 3600*h - 60*m;
		
		return (h < 10 ? "0" + String.valueOf(h) : String.valueOf(h)) + ":"
				+ (m < 10 ? "0" + String.valueOf(m) : String.valueOf(m)) + ":"
				+ (s < 10 ? "0" + String.valueOf(s) : String.valueOf(s));
	}
	
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		setTotal_time(Integer.parseInt(SystemConfig.getSystemProp(Keys.TOTAL_TIME)));
		while(true){		
			try {
				Date date = new Date();
				label_time.setText(sdf.format(date));
				if(label_total_time!=null){
				label_total_time.setText("Total time runs : "
						+getFormatTotalTime(total_time));
				}
				date = null;
				Thread.sleep(1000);	
				total_time++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getTotal_time() {
		return total_time;
	}

	public static void setTotal_time(int total_time) {
		TimeCounter.total_time = total_time;
	}

	public JLabel getLabel_time() {
		return label_time;
	}

	public void setLabel_time(JLabel label_time) {
		this.label_time = label_time;
	}


	public JLabel getLabel_total_time() {
		return label_total_time;
	}

	public void setLabel_total_time(JLabel label_total_time) {
		this.label_total_time = label_total_time;
	}
}
