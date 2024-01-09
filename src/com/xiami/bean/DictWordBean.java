package com.xiami.bean;

import java.util.ArrayList;

public class DictWordBean {
	private String word;
	private String pron;
	private ArrayList<String> sents = new ArrayList<String>();
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the pron
	 */
	public String getPron() {
		return pron;
	}
	/**
	 * @param pron the pron to set
	 */
	public void setPron(String pron) {
		this.pron = pron;
	}

	public ArrayList<String> getSents() {
		return sents;
	}
	public void setSents(ArrayList<String> sents) {
		this.sents = sents;
	}
}
