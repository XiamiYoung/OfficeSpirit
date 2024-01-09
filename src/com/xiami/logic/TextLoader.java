package com.xiami.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.xiami.bean.TextBean;
import com.xiami.core.IDGererator;
import com.xiami.core.TextResource;

@SuppressWarnings("unchecked")
public class TextLoader {
	private static String XML_PATH;
	private static List<Element> beanList;
	private static List<Element> Nodes;
	private static Document document;
	private static Element root;
	
	public static void configuration(String path) throws Exception{
		XML_PATH = path;
		document = new TextResource().getTextResource(path);
		root = document.getRootElement();
	}
	
	public static HashMap getTotalTextCounts() throws IOException, Exception {
		
		Element root_temp = root;
		
		HashMap map = new HashMap();
		int max = 0;
		int min = 10000;
		String name_max = "";
		String name_min = "";
		int counts = 0;
		File dir = new File(".\\textResource");
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".xml")) {
					configuration(files[i].getCanonicalPath());
					counts += getSingleTextCount();
					if(getSingleTextCount()>=max){
						max = getSingleTextCount();
						name_max = files[i].getName().substring(0, files[i].getName().indexOf("."));
					}
					if(getSingleTextCount()<=min){
						min = getSingleTextCount();
						name_min = files[i].getName().substring(0, files[i].getName().indexOf("."));
					}
				}
			}
		}
		
		root = root_temp;
		
		map.put("name_max", name_max);
		map.put("name_min", name_min);
		map.put("max", max);
		map.put("min", min);
		map.put("total", counts);
		
		return map;
	}
	
	public static int getSingleTextCount(){
		beanList = root.selectNodes("//TextList/TextBean");
		return beanList.size();
	}
	
	public static ArrayList<TextBean> getText() {
		ArrayList<TextBean> textList = new ArrayList<TextBean>();
		beanList = root.selectNodes("//TextList/TextBean");
		for (int i = 0; i < beanList.size(); i++) {
			Nodes = (beanList.get(i)).elements();
			TextBean textBean = new TextBean();
			textBean.setUUID(beanList.get(i).attributeValue("UUID"));
			textBean.setName(Nodes.get(0).getText());
			textBean.setContent(Nodes.get(1).getText());
			textBean.setBookmark(Nodes.get(2).getText());
			textList.add(textBean);
		}
		
		return textList;
	}

	public static TextBean getTextBeanById(String uid) {
		ArrayList<TextBean> textList = new ArrayList<TextBean>();
		beanList = root.selectNodes("//TextList/TextBean");
		TextBean textBean = new TextBean();
		for (int i = 0; i < beanList.size(); i++) {
			if (beanList.get(i).attributeValue("UUID").equals(uid)) {
				Nodes = (beanList.get(i)).elements();
				textBean.setName(Nodes.get(0).getText());
				textBean.setContent(Nodes.get(1).getText());
				textBean.setBookmark(Nodes.get(2).getText());
				textList.add(textBean);
				break;
			}
		}
		return textBean;
	}

	@SuppressWarnings("deprecation")
	public static void addText(TextBean bean) {
		try {

			Element textElement = (Element) root.selectSingleNode(
					"//TextList/TextTemplate").clone();

			textElement.setName("TextBean");

			textElement.setAttributeValue("UUID", IDGererator.getUUID());

			List<Element> tempNodes = textElement.elements();

			tempNodes.get(0).setText(bean.getName());

			tempNodes.get(1).setText(bean.getContent());
			
			tempNodes.get(2).setText("0");

			root.add(textElement);

			XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));

			writer.write(document);

			writer.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not add text!");
		}
	}

	public static void removeText(String uid) {
		try {
			beanList = root.selectNodes("//TextList/TextBean");
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
			JOptionPane.showMessageDialog(new JFrame(), "Can not remove text!");
		}
	}
	
	public static void updateTextBean(TextBean textBean) {
		try {
			beanList = root.selectNodes("//TextList/TextBean");
			for (int i = 0; i < beanList.size(); i++) {
				if (beanList.get(i).attributeValue("UUID").equals(textBean.getUUID())) {
					
					List<Element> tempNodes = beanList.get(i).elements();
					
					tempNodes.get(0).setText(textBean.getName());

					tempNodes.get(1).setText(textBean.getContent());
					
					tempNodes.get(2).setText(textBean.getBookmark());
					
					XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));
					writer.write(document);
					writer.close();
					break;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not update text!");
		}
	}
	public static void updateBookMark(TextBean textBean) {
		try {
			beanList = root.selectNodes("//TextList/TextBean");
			for (int i = 0; i < beanList.size(); i++) {
				if (beanList.get(i).attributeValue("UUID").equals(textBean.getUUID())) {
					
					List<Element> tempNodes = beanList.get(i).elements();
										
					tempNodes.get(2).setText(textBean.getBookmark());
					
					XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));
					writer.write(document);
					writer.close();
					break;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not update bookmark!");
		}
	}
}
