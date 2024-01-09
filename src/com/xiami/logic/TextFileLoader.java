package com.xiami.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.xiami.bean.TextFileBean;
import com.xiami.core.Keys;
import com.xiami.core.SystemConfig;
import com.xiami.core.TextResource;

@SuppressWarnings("unchecked")
public class TextFileLoader {
	private static String XML_PATH;
	private static List<Element> beanList;
	private static List<Element> Nodes;
	private static Document document;
	private static Element root;
	
	public static void configuration() throws Exception{
		XML_PATH = SystemConfig.getSystemProp(Keys.XML_TEXT_FILE_PATH);
		document = new TextResource().getTextFileResource();
		root = document.getRootElement();
	}
	
	public static int getTextFileCount() {
		
		beanList = root.selectNodes("//TextFileList/TextFileBean");
		
		return beanList.size();
	}
	
	public static ArrayList<TextFileBean> getTextFile() {
		ArrayList<TextFileBean> textFileList = new ArrayList<TextFileBean>();
		beanList = root.selectNodes("//TextFileList/TextFileBean");
		for (int i = 0; i < beanList.size(); i++) {
			Nodes = (beanList.get(i)).elements();
			TextFileBean textFileBean = new TextFileBean();
			textFileBean.setUUID(beanList.get(i).attributeValue("UUID"));
			textFileBean.setName(Nodes.get(0).getText());
			textFileBean.setPath(Nodes.get(1).getText());
			textFileBean.setLastOpened(Integer.parseInt(Nodes.get(2).getText()));
			textFileList.add(textFileBean);
		}
		
		return textFileList;
	}

	public static TextFileBean getTextFileById(String uid) {
		ArrayList<TextFileBean> textList = new ArrayList<TextFileBean>();
		beanList = root.selectNodes("//TextFileList/TextFileBean");
		TextFileBean textFileBean = new TextFileBean();
		for (int i = 0; i < beanList.size(); i++) {
			if (beanList.get(i).attributeValue("UUID").equals(uid)) {
				Nodes = (beanList.get(i)).elements();
				textFileBean.setName(Nodes.get(0).getText());
				textFileBean.setPath(Nodes.get(1).getText());
				textFileBean.setLastOpened(Integer.parseInt(Nodes.get(2).getText()));
				textList.add(textFileBean);
				break;
			}
		}
		return textFileBean;
	}

	@SuppressWarnings("deprecation")
	public static void addTextFile(TextFileBean bean) {
		try {

			Element textFileElement = (Element) root.selectSingleNode(
					"//TextFileList/TextFileTemplate").clone();

			textFileElement.setName("TextFileBean");

			textFileElement.setAttributeValue("UUID", bean.getUUID());

			List<Element> tempNodes = textFileElement.elements();

			tempNodes.get(0).setText(bean.getName());

			tempNodes.get(1).setText(bean.getPath());
			
			tempNodes.get(2).setText(String.valueOf(bean.getLastOpened()));
			
			root.add(textFileElement);

			XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));

			writer.write(document);

			writer.close();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not read text config resource!");
		}
	}

	
	public static void removeTextFile(String uid) {
		try {
			beanList = root.selectNodes("//TextFileList/TextFileBean");
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
			JOptionPane.showMessageDialog(new JFrame(), "Can not remove text config resource!");
		}
	}
	
	public static void updateTexFileBean(TextFileBean textFileBean) {
		try {
			beanList = root.selectNodes("//TextFileList/TextFileBean");
			for (int i = 0; i < beanList.size(); i++) {
				if (beanList.get(i).attributeValue("UUID").equals(textFileBean.getUUID())) {
					
					List<Element> tempNodes = beanList.get(i).elements();
					
					tempNodes.get(0).setText(textFileBean.getName());

					tempNodes.get(1).setText(textFileBean.getPath());
					
					tempNodes.get(2).setText(String.valueOf(textFileBean.getLastOpened()));
					
					XMLWriter writer = new XMLWriter(new FileOutputStream(XML_PATH));
					writer.write(document);
					writer.close();
					break;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Can not write text config resource!");
		}
	}
	
	public static boolean createXMLFile(File file) {
		if (file.exists()) {
			int op = JOptionPane.showConfirmDialog(new JFrame(), "OverWrite the file?",
					"OverWrite", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (op) {
			case JOptionPane.YES_OPTION:
				break;
			case JOptionPane.NO_OPTION:
				return false;
			case JOptionPane.CANCEL_OPTION:
				return false;
			}
		}
		
		PrintWriter output = null;
		try {
			 output =new PrintWriter(new FileWriter(file));			 
		     output.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");	 
		     output.print("<TextList>\n");
		     output.print("<TextTemplate>\n");
		     output.print("<name/>\n");
		     output.print("<content/>\n");
		     output.print("<bookmark/>\n");
		     output.print("</TextTemplate>\n");
		     output.print("</TextList>\n");
		     output.close();

		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Error in saving,Maybe the file is opened.");
			return false;
		} finally {
			if (output != null){
				output.close();
				}
		}
		return true;
	}
	
	public static void updateXMLFileName(File oldFile,File newFile) {
		oldFile.renameTo(newFile);		
	}
	
	public static void deleteXMLFile(File file) {
		file.delete();	
	}
	
}
