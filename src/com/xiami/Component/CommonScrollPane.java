package com.xiami.Component;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class CommonScrollPane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;

	public CommonScrollPane(Component view) {
		this.setViewportView(view);
		//		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}

	public CommonScrollPane(String path, Component view) {
		this.setViewportView(view);
		//		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		imageIcon = new ImageIcon(path);
		this.setOpaque(false);
	}

	public void paint(Graphics g) {
		if (null != imageIcon) {
			g.drawImage(imageIcon.getImage(), 0, 0, this.getWidth(), this
					.getHeight(), this);
		}
		super.paint(g);
	}
}
