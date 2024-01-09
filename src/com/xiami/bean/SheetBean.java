package com.xiami.bean;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class SheetBean {
	private String sheetName;
	private ArrayList<WordBean> wordBeanList = new ArrayList<WordBean>();
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public ArrayList<WordBean> getWordBeanList() {
		return wordBeanList;
	}
	public void setWordBeanList(ArrayList<WordBean> wordBeanList) {
		this.wordBeanList = wordBeanList;
	}


}
