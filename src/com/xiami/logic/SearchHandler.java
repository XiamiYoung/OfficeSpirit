package com.xiami.logic;

import java.util.ArrayList;

import com.xiami.bean.SheetBean;

public class SearchHandler {
	
	private ArrayList<SheetBean> contents;
	
	private int index_sheetName;
	
	private int index_word;

	private boolean isFound;
	
	public SearchHandler(ArrayList<SheetBean> contents){
		this.contents = contents;
	}
	
	public boolean getSearch(int type,String word){
		if(type==0){
			return getSearchByJP(word);
		}else{
			return getSearchByCN(word);
		}
	}
	
	public boolean getSearchByJP(String word){
		for(int i=0;i<contents.size();i++){
			SheetBean bean = contents.get(i);
			for(int j=0;j<bean.getWordBeanList().size();j++){
				if(bean.getWordBeanList().get(j).getWord().toString().trim().contains(word)){		
					setFound(true);
					setIndex_word(j);
					setIndex_sheetName(i);					
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getSearchByCN(String word){
		for(int i=0;i<contents.size();i++){
			SheetBean bean = contents.get(i);
			for(int j=0;j<bean.getWordBeanList().size();j++){
				if(bean.getWordBeanList().get(j).getWord_cn().toString().trim().contains(word)){		
					setFound(true);
					setIndex_word(j);
					setIndex_sheetName(i);					
					return true;
				}
			}
		}
		return false;
	}
			
	public ArrayList<SheetBean> getContents() {
		return contents;
	}

	public void setContents(ArrayList<SheetBean> contents) {
		this.contents = contents;
	}

	public int getIndex_sheetName() {
		return index_sheetName;
	}

	public void setIndex_sheetName(int index_sheetName) {
		this.index_sheetName = index_sheetName;
	}

	public int getIndex_word() {
		return index_word;
	}

	public void setIndex_word(int index_word) {
		this.index_word = index_word;
	}
	
	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

}
