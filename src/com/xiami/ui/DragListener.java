package com.xiami.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;

public class DragListener extends MouseAdapter implements MouseMotionListener {

	private Point offset;
	private Component host;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static String isDragToBorderAllowed = SystemConfig.getSystemProp(Keys.IS_WINDOW_ATTACHED);

	public DragListener(Component ins) {
		install(ins);
	}

	public synchronized void install(Component comp) {
		uninstall();
		host = comp;
		host.addMouseListener(this);
		host.addMouseMotionListener(this);
	}

	public synchronized void uninstall() {
		if (host != null) {
			host.removeMouseListener(this);
			host.removeMouseMotionListener(this);
			host = null;
		}
	}

	public void mousePressed(MouseEvent e) {
		try {
			if (e.getSource() == host)
				offset = e.getPoint();
		} catch (Exception ex) {
			//ignore NullPointer Exception
		}
	}

	public void mouseDragged(MouseEvent e) {
		try {
			if (e.getSource() != host)
				return;
			final int x = host.getX();
			final int y = host.getY();
			final Point lastAt = e.getPoint();

			host.setLocation(x + lastAt.x - offset.x, y + lastAt.y - offset.y);
		} catch (Exception ex) {
			//ignore NullPointer Exception
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		try {
			if (e.getSource() != host)
				return;
			int width = screenSize.width;
			final int x = host.getX();
			final int y = host.getY();
			final Point lastAt = e.getPoint();
			int location_x = x;
			int location_y = y;
			if (isDragToBorderAllowed.equals("0")) {
				if (x < 50 && x > 0 && y < 50 && y > 0) {
					location_x = 2;
					location_y = 2;
					host.setLocation(location_x, location_y);
				} else if ((x + host.getWidth() > width - 50)
						&& (x + host.getWidth() < width && y < 50 && y > 0)) {
					location_x = width - host.getWidth() - 2;
					host.setLocation(location_x, 2);
				} else if ((x + host.getWidth() > width - 50)
						&& (x + host.getWidth() < width)) {
					location_x = width - host.getWidth() - 2;
					host.setLocation(location_x, y + lastAt.y - offset.y);
				} else if (x < 50 && x > 0) {
					location_x = 2;
					host.setLocation(location_x, y + lastAt.y - offset.y);
				} else if (y < 50 && y > 0) {
					location_y = 2;
					host.setLocation(x + lastAt.x - offset.x, location_y);
				}
			} else {
				host.setLocation(x + lastAt.x - offset.x, y + lastAt.y
						- offset.y);
			}
		} catch (Exception ex) {
			//ignore NullPointer Exception
		}
	}

	public static String getIsDragToBorderAllowed() {
		return isDragToBorderAllowed;
	}

	public static void setIsDragToBorderAllowed(String isDragToBorderAllowed) {
		DragListener.isDragToBorderAllowed = isDragToBorderAllowed;
	}
}
