package com.xiami.Component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import com.xiami.ui.DragListener;

public class ProgressFrame extends JFrame implements Runnable,ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressbar;
	private ImagePanel panel;
	
	private static final int INITIAL_WIDTH = 160;
	private static final int INITIAL_HEIGHT =25;
		
	public void init(){
		
		panel = new ImagePanel(0, 0, INITIAL_WIDTH , INITIAL_HEIGHT);
		this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - INITIAL_WIDTH / 2,
				screenSize.height / 2 - INITIAL_HEIGHT / 2);
		this.setUndecorated(true);
		this.getContentPane().add(panel);
		this.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2));
		progressbar = new JProgressBar();
		progressbar.setBounds(0,0,300,20);
		progressbar.setVisible(true);
		progressbar.setOrientation(JProgressBar.HORIZONTAL);
		progressbar.setMinimum(0);
		progressbar.setMaximum(300);
		progressbar.setBorderPainted(true);
//		progressbar.setBackground(Color.pink);
		progressbar.setIndeterminate(true);
		panel.add(progressbar);
		new DragListener(getIns());	
		this.setVisible(true);
	}
	
	public ProgressFrame getIns(){
		return this;
	}

	public void run() {
		init();
	}

	public void actionPerformed(ActionEvent arg0) {
		
	}
}
