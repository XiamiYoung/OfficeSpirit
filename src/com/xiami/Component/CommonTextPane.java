package com.xiami.Component;

import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

public class CommonTextPane extends JTextPane{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommonTextPane(HTMLDocument document){
		 super(document);
	 }
	
	 public boolean getScrollableTracksViewportWidth() {
	        return false;
	    }

	    public void setSize(Dimension d) {
	        int parentWidth = this.getParent().getWidth();
	        if(parentWidth>d.width){
	            d.width = parentWidth;
	        }
	        super.setSize(d);
	    }
}
