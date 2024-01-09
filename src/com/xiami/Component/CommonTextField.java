package com.xiami.Component;

import java.awt.im.InputContext;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CommonTextField extends JTextField {

	final InputContext inputContext = InputContext.getInstance(); 
	
	public CommonTextField() {
	}

	public CommonTextField(int x, int y, int width, int length) {
		super.setBounds(x, y, width, length);
	}

	public InputContext getInputContext() { 
        return inputContext; 
      } 
}
