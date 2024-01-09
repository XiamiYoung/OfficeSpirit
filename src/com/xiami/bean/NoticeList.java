package com.xiami.bean;

import java.util.ArrayList;

public class NoticeList {
	private static ArrayList<NoticeBean> Noticelist;

	public static ArrayList<NoticeBean> getNoticelist() {
		return Noticelist;
	}

	public static void setNoticelist(ArrayList<NoticeBean> noticelist) {
		Noticelist = noticelist;
	}
}
