package com.xiami.Component;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	ImageIcon icon;

	@SuppressWarnings("serial")
	public ImagePanel(String path, int x, int y, int width, int height) {
		icon = new ImageIcon(path);
		this.setBounds(x, y, width, height);
		this.setOpaque(false);
		this.setVisible(true);

	}

	public ImagePanel(int x, int y, int width, int height) {
		this.setBounds(x, y, width, height);
		this.setOpaque(false);
		this.setVisible(true);

	}
	
	public ImagePanel() {
		this.setOpaque(false);
		this.setVisible(true);
	}

	protected void paintComponent(Graphics g) {
		if (null != icon) {
			g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this
					.getHeight(), this);
		}
		super.paintComponent(g);
	}
}
