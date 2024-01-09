package com.xiami.logic;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;

import com.xiami.Component.ImageTextArea;
import com.xiami.bean.SheetBean;

public class DetailDataLoader {

	public void LoadData(int i, SheetBean sheet, JTextField word_jp,
			JTextField word_cn, JTextField pron, JTextField sheetName,
			ImageTextArea sentns, DefaultListModel model_word) {

		boolean isEnabled = true;
		
		model_word.clear();
		
		if (model_word.getSize() == 0) {
			for (int index = 0; index < sheet.getWordBeanList().size(); index++) {
				if (null != sheet.getWordBeanList().get(index).getWord()) {
					model_word.addElement(sheet.getWordBeanList().get(index)
							.getWord().toString());
				}
			}
			sheetName.setText(sheet.getSheetName().toLowerCase().trim());
		}

		word_jp.setText(sheet.getWordBeanList().size()==0?"":sheet.getWordBeanList().get(i).getWord());

		word_cn.setText(sheet.getWordBeanList().size()==0?"":sheet.getWordBeanList().get(i).getWord_cn());

		pron.setText(sheet.getWordBeanList().size()==0?"":sheet.getWordBeanList().get(i).getPron());

		sentns.setText(sheet.getWordBeanList().size()==0?"":sheet.getWordBeanList().get(i).getSentns());
		
		if(model_word.getSize() == 0){
			isEnabled = false;
		}
			word_jp.setEnabled(isEnabled);
			word_cn.setEnabled(isEnabled);
			pron.setEnabled(isEnabled);
			sentns.setEnabled(isEnabled);		
	}
	
	
}
