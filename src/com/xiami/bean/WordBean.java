package com.xiami.bean;

public class WordBean {
	private String word;
	private String word_cn;
	private String pron;
	private String sentns;
	private int physicalNum;
	
	public int getPhysicalNum() {
		return physicalNum;
	}
	public void setPhysicalNum(int physicalNum) {
		this.physicalNum = physicalNum;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getWord_cn() {
		return word_cn;
	}
	public void setWord_cn(String word_cn) {
		this.word_cn = word_cn;
	}
	public String getPron() {
		return pron;
	}
	public void setPron(String pron) {
		this.pron = pron;
	}
	public String getSentns() {
		return sentns;
	}
	public void setSentns(String sentns) {
		this.sentns = sentns;
	}
}
