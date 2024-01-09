package com.xiami.logic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.xiami.bean.NoticeBean;
import com.xiami.core.IDGererator;
import com.xiami.core.NoticeResource;
import com.xiami.core.SystemConfig;
@SuppressWarnings("unchecked")
public class NoticeLoader {
	
	private static String XML_PATH;
	private static final String XML_NOTICE_PATH = "xml_notice_path";
	private static List<Element> beanList;
	private static List<Element> Nodes;
	private static Document document;
	private static Element root;
	private static boolean isReNotice = false;
	
	public static void configuration() throws Exception{
		XML_PATH = SystemConfig.getSystemProp(XML_NOTICE_PATH);
		document = new NoticeResource().getXMLResource();
		root = document.getRootElement();
	}
	
	
	public static ArrayList<NoticeBean> getNotice() {
		ArrayList<NoticeBean> noticeList = new ArrayList<NoticeBean>();
		beanList = root.selectNodes("//NoticeList/NoticeBean");
		for (int i = 0; i < beanList.size(); i++) {
			Nodes = (beanList.get(i)).elements();
			NoticeBean noticeBean = new NoticeBean();
			noticeBean.setUUID(beanList.get(i).attributeValue("UUID"));
			noticeBean.setName(Nodes.get(0).getText());
			noticeBean.setUser(Nodes.get(1).getText());
			noticeBean.setFromtime(Nodes.get(2).getText());
			noticeBean.setNoticetime(Nodes.get(3).getText());
			noticeBean.setContent(Nodes.get(4).getText());
			noticeBean.setIsCommon(Nodes.get(5).getText());
			noticeBean.setIsNoticed(Nodes.get(6).getText());
			noticeList.add(noticeBean);
		}

		return noticeList;
	}

	public static NoticeBean getNoticeBeanById(String uid) {
		ArrayList<NoticeBean> noticeList = new ArrayList<NoticeBean>();
		beanList = root.selectNodes("//NoticeList/NoticeBean");
		NoticeBean noticeBean = new NoticeBean();
		for (int i = 0; i < beanList.size(); i++) {
			if (beanList.get(i).attributeValue("UUID").equals(uid)) {
				Nodes = (beanList.get(i)).elements();
				noticeBean.setName(Nodes.get(0).getText());
				noticeBean.setUser(Nodes.get(1).getText());
				noticeBean.setFromtime(Nodes.get(2).getText());
				noticeBean.setNoticetime(Nodes.get(3).getText());
				noticeBean.setContent(Nodes.get(4).getText());
				noticeBean.setIsCommon(Nodes.get(5).getText());
				noticeBean.setIsNoticed(Nodes.get(6).getText());
				noticeList.add(noticeBean);
				break;
			}
		}
		return noticeBean;
	}

	@SuppressWarnings("deprecation")
	public static void addNotice(NoticeBean bean) {
		try {

			Element noticeElement = (Element) root.selectSingleNode(
					"//NoticeList/NoticeTemplate").clone();

			noticeElement.setName("NoticeBean");

			noticeElement.setAttributeValue("UUID", IDGererator.getUUID());

			List<Element> tempNodes = noticeElement.elements();

			tempNodes.get(0).setText(bean.getName());

			tempNodes.get(1).setText(bean.getUser());

			tempNodes.get(2).setText(bean.getFromtime());

			tempNodes.get(3).setText(bean.getNoticetime());

			tempNodes.get(4).setText(bean.getContent());

			tempNodes.get(5).setText(bean.getIsCommon());

			tempNodes.get(6).setText("false");

			root.add(noticeElement);

			XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));

			writer.write(document);

			writer.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not add notice config resource!");
		}
	}

	public static void removeNotice(String uid) {
		try {
			beanList = root.selectNodes("//NoticeList/NoticeBean");
			for (int i = 0; i < beanList.size(); i++) {
				if (beanList.get(i).attributeValue("UUID").equals(uid)) {
					
					root.remove(beanList.get(i));

					XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));
					
					writer.write(document);

					writer.close();

					break;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not remove notice config resource!");
		}
	}
	
	public static void updateNoticeBean(NoticeBean noticeBean) {
		try {

			beanList = root.selectNodes("//NoticeList/NoticeBean");
			for (int i = 0; i < beanList.size(); i++) {
				if (beanList.get(i).attributeValue("UUID").equals(noticeBean.getUUID())) {
					
					List<Element> tempNodes = beanList.get(i).elements();
					
					tempNodes.get(0).setText(noticeBean.getName());

					tempNodes.get(1).setText(noticeBean.getUser());

					tempNodes.get(2).setText(noticeBean.getFromtime());

					tempNodes.get(3).setText(noticeBean.getNoticetime());

					tempNodes.get(4).setText(noticeBean.getContent());

					tempNodes.get(5).setText(noticeBean.getIsCommon());
					
					if(isReNotice()){
											
						tempNodes.get(6).setText("false");
						
					}else{
						
					tempNodes.get(6).setText(noticeBean.getIsNoticed());
					
					}
					XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));
					
					writer.write(document);
					writer.close();
					
					setReNotice(false);
					
					break;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not update notice config resource!");
		}
	}

	public static void updateCommon() {
		try {

			beanList = root.selectNodes("//NoticeList/NoticeBean");
			for (int i = 0; i < beanList.size(); i++) {
					
					List<Element> tempNodes = beanList.get(i).elements();
					
					if(tempNodes.get(5).getText().equals("true")){
						tempNodes.get(6).setText("false");
					}													
			}
			
			XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));
			
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not update notice config resource!");
		}
	}
	
	public static boolean isReNotice() {
		return isReNotice;
	}

	public static void setReNotice(boolean isReNotice) {
		NoticeLoader.isReNotice = isReNotice;
	}
	
}
