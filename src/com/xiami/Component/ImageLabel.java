package com.xiami.Component;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;

	public ImageLabel(String path, int x, int y, int width, int height) {
		imageIcon = new ImageIcon(path);
		this.setIcon(imageIcon);
		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.setOpaque(true);
	}

	public ImageLabel(int x, int y, int width, int height, String title) {

		this.setText(title);
		this.setIcon(imageIcon);
		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.setOpaque(false);
	}

	protected void paintComponent(Graphics g) {
		if (null != imageIcon) {
			g.drawImage(imageIcon.getImage(), 0, 0, this.getWidth(), this
					.getHeight(), this);
		}
		super.paintComponent(g);
	}

	public void drawIcon(Graphics g,String path) {
		g.drawImage(new ImageIcon(path).getImage(), 0, 0, this.getWidth(), this
					.getHeight(), this);
		super.paintComponent(g);
	}
}
