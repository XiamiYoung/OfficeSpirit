package com.xiami.Component;

import java.awt.im.InputContext;

import javax.swing.JComboBox;

public class CommonComboBox extends JComboBox{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final InputContext inputContext = InputContext.getInstance(); 

	public InputContext getInputContext() { 
        return inputContext; 
      } 
}
