package com.xiami.bean;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class TipBean {
	private static ArrayList<String> word_jp = new ArrayList<String>();
	private static ArrayList<String> word_cn = new ArrayList<String>();
	private static ArrayList<String> word_pron = new ArrayList<String>();
	private static ArrayList<String> word_sentns = new ArrayList<String>();



	/**
	 * @return the word_jp
	 */
	public static ArrayList<String> getWord_jp() {
		return word_jp;
	}

	/**
	 * @param word_jp the word_jp to set
	 */
	public static void setWord_jp(ArrayList<String> word_jp) {
		TipBean.word_jp = word_jp;
	}

	/**
	 * @return the word_cn
	 */
	public static ArrayList<String> getWord_cn() {
		return word_cn;
	}

	/**
	 * @param word_cn the word_cn to set
	 */
	public static void setWord_cn(ArrayList<String> word_cn) {
		TipBean.word_cn = word_cn;
	}

	/**
	 * @return the word_pron
	 */
	public static ArrayList<String> getWord_pron() {
		return word_pron;
	}

	/**
	 * @param word_pron the word_pron to set
	 */
	public static void setWord_pron(ArrayList<String> word_pron) {
		TipBean.word_pron = word_pron;
	}

	/**
	 * @return the word_sentns
	 */
	public static ArrayList<String> getWord_sentns() {
		return word_sentns;
	}

	/**
	 * @param word_sentns the word_sentns to set
	 */
	public static void setWord_sentns(ArrayList<String> word_sentns) {
		TipBean.word_sentns = word_sentns;
	}

	public static void clear(){
		word_jp.clear();
		word_cn.clear();
		word_pron.clear();
		word_sentns.clear();
	}
	
}
