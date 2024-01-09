package com.xiami.Component;

import java.awt.Graphics;
import java.awt.im.InputContext;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ImageTextArea extends JTextArea {

	private ImageIcon imageIcon;
	final InputContext inputContext = InputContext.getInstance(); 

	public ImageTextArea(String path) {
		imageIcon = new ImageIcon(path);
		this.setVisible(true);
		this.setLineWrap(true);
		this.setFont(new java.awt.Font("SansSerif", 0, 16));
	}

	public ImageTextArea() {
		this.setVisible(true);
		this.setLineWrap(true);
		this.setFont(new java.awt.Font("SansSerif", 0, 16));
	}

	protected void paintComponent(Graphics g) {
		if (null != imageIcon) {
			g.drawImage(imageIcon.getImage(), 0, 0, this.getWidth(), this
					.getHeight(), this);
		}
		super.paintComponent(g);
	}
	
	public InputContext getInputContext() { 
        return inputContext; 
      } 
}
