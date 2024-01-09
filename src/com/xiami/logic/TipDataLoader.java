package com.xiami.logic;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JTextField;

import com.xiami.Component.ImageTextArea;
import com.xiami.bean.SheetBean;
import com.xiami.bean.TipBean;

public class TipDataLoader {

	private Random random = new Random();

	private static ArrayList<SheetBean> contents_list;

	private int i;

	public static void setData(ArrayList<SheetBean> contents) {
		contents_list = contents;
		TipBean.clear();
		for (int i = 0; i < contents.size(); i++) {
			for (int j = 0; j < contents.get(i).getWordBeanList().size(); j++) {
				TipBean.getWord_jp()
						.add(
								contents_list.get(i).getWordBeanList().get(j)
										.getWord());
				TipBean.getWord_cn().add(
						contents_list.get(i).getWordBeanList().get(j)
								.getWord_cn());
				TipBean.getWord_pron()
						.add(
								contents_list.get(i).getWordBeanList().get(j)
										.getPron());
				TipBean.getWord_sentns().add(
						contents_list.get(i).getWordBeanList().get(j)
								.getSentns());
			}
		}
	}

	public void ShowData(JTextField word_jp, JTextField word_cn,
			JTextField pron, ImageTextArea sentns, int type, int button,
			boolean isFirst) {

		if (type == 0) {
			if (TipBean.getWord_jp().size() == 1) {
				i = 0;
			} else {
				i = random.nextInt(TipBean.getWord_jp().size() - 1);
			}
		} else {
			if (isFirst) {
				i = 0;
			} else {
				if (button == 0) {
					if (i >= 1) {
						i--;
					} else {
						i = TipBean.getWord_jp().size() - 1;
					}
				} else {
					if (i < TipBean.getWord_jp().size() - 1) {
						i++;
					} else {
						i = 0;
					}
				}
			}
		}
		if ((null != TipBean.getWord_jp().get(i).toString())
				&& !("".equals(TipBean.getWord_jp().get(i).toString()))) {
			word_jp.setText(TipBean.getWord_jp().get(i).toString());
			word_jp.setCaretPosition(0);
		} else {
			word_jp.setText("");
		}
		if ((null != TipBean.getWord_cn().get(i))
				&& !("".equals(TipBean.getWord_cn().get(i)))) {
			word_cn.setText(TipBean.getWord_cn().get(i).toString());
			word_cn.setCaretPosition(0);
		} else {
			word_cn.setText("");
		}
		if ((null != TipBean.getWord_pron().get(i).toString())
				&& !("".equals(TipBean.getWord_pron().get(i).toString()))) {
			pron.setText(TipBean.getWord_pron().get(i).toString());
			pron.setCaretPosition(0);
		} else {
			pron.setText("");
		}
		if ((null != TipBean.getWord_sentns().get(i))
				&& !("".equals(TipBean.getWord_sentns().get(i)))) {
			sentns.setText(TipBean.getWord_sentns().get(i).toString());
			sentns.setCaretPosition(0);
		} else {
			sentns.setText("");
		}
	}
}
