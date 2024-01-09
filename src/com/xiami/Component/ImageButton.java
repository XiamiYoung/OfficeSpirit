package com.xiami.Component;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

public class ImageButton extends JButton {

	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;
	private Insets insets;

	public ImageButton(String path, int x, int y, int width, int height,
			ActionListener ins) {
		imageIcon = new ImageIcon(path);
		insets = new Insets(0, 0, 0, 0);
		this.setMargin(insets);
		this.setIcon(imageIcon);
		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.addActionListener(ins);
		this.setOpaque(true);
	}

	public ImageButton(int x, int y, int width, int height, ActionListener ins) {

		this.setIcon(imageIcon);
		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.addActionListener(ins);
		this.setOpaque(false);
	}
	
	public ImageButton(ActionListener ins) {

		this.setIcon(imageIcon);
		this.setVisible(true);
		this.addActionListener(ins);
		this.setOpaque(false);
	}

	public void registerKeyboardAction(ActionListener ins, 
            KeyStroke stroke,int condition){
		super.registerKeyboardAction(ins, stroke, condition);
	}
	
	protected void paintComponent(Graphics g) {
		if (null != imageIcon) {
			g.drawImage(imageIcon.getImage(), 0, 0, this.getWidth(), this
					.getHeight(), this);
		}
		super.paintComponent(g);
	}
}
