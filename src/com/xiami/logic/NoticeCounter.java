package com.xiami.logic;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.xiami.bean.NoticeBean;
import com.xiami.bean.NoticeList;
import com.xiami.core.Logger;
import com.xiami.frame.NoticeWindow;
import com.xiami.ui.CommonUIManager;

public class NoticeCounter implements Runnable {

	private ArrayList<NoticeBean> noticeList = new ArrayList<NoticeBean>();

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private int INITIAL_HEIGHT = 340;
	private int INITIAL_WIDTH = 250;
	private int x;
	private int y;

	public NoticeCounter(ArrayList<NoticeBean> noticeList) {
		this.noticeList = noticeList;
		x = screenSize.width - INITIAL_WIDTH - 10;
		y = screenSize.height - INITIAL_HEIGHT - 400;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1);
				noticeList = NoticeList.getNoticelist();
				for (int i = 0; i < noticeList.size(); i++) {
					if (noticeList.get(i).getIsNoticed().equals("false")
							&& getCompareTime(noticeList.get(i),
									Long.parseLong(noticeList.get(i).getNoticetime()))) {
						NoticeBean bean = noticeList.get(i);

						bean.setIsNoticed("true");
						
						NoticeLoader.updateNoticeBean(bean);

						CommonUIManager ui = new CommonUIManager(
								new NoticeWindow(bean, x, y));

						x = x - 20;

						y = y + 20;

						CommonUIManager.setUI();

						ui.startUp();

						Logger.getLogger().info("Show Notice : "+bean.getName());

					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("deprecation")
	public static boolean getCompareTime(NoticeBean bean, long compareTime) {
		String setTime = bean.getFromtime();
		String isCommon = bean.getIsCommon();
		SimpleDateFormat formatter ;
		long currentTime ;
		int h = 0;
		int m = 0;
		int h_now;
		int m_now;
		if(isCommon.equals("false")){
			Date date_set = null;
			formatter= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
			currentTime = new Date().getTime();
			try {
				date_set = formatter.parse(setTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long l = date_set.getTime() - currentTime;
			long d = l / (60 * 1000);
			return d < compareTime;
		}else{			
			formatter= new SimpleDateFormat("HH:mm");
			h_now = new Date().getHours();
			m_now = new Date().getMinutes();
			try {
			h = formatter.parse(setTime).getHours();
			m = formatter.parse(setTime).getMinutes();			
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 60*(h-h_now)+m-m_now <= compareTime;
		}

	}
}