package com.xiami.dict;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* 
 * Steps for making a dictionary 
 * 1.Get a original dict from KSDrip.exe,and it is encoded by gb2312.
 * 2.Format this dict to UTF-8 and add Header&Bottom to file(eg:<root></root>) making it a parseable xml.
 * 3.Using Xml2CVS.java to transform this dict from Xml to CVS format.
 * 4.Send formated file to FilePreprocess.java to build preIndex files.
 * 5.Using Indexer.java to build the searching index of lucene3.
 * 6.begin to search!
 */


public class Xml2CVS {
	
	private static String input = "dict\\PWDECAHD_UTF-8.da3";
	private static String output = "dict\\english-chinese\\ec_cvs.dict";
	
	public static void main(String args[]){
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(input));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(output)),"utf-8"));

			 boolean isAHDKK = false;
			 boolean isPass = false;
			 boolean isProtype = false;
			 boolean isPreExp = false;
			 int event = reader.getEventType();
			 StringBuffer sb = new StringBuffer("");
			 while (true) {
			 switch (event) {
			 case XMLStreamConstants.START_ELEMENT:
				 String line = reader.getName().toString();
				 if(!line.contains("root")){
//				 System.out.println(line);
					if(isPass){
					 		break;
					 }
					if(line.contains("单词块")){
						sb = new StringBuffer("");
					}
					else if(line.contains("惯用型原型")){
						line = line.replaceAll("惯用型原型", "<habiProtype>");
						sb.append(line);
					}
					else if(line.contains("另见")){
						line = line.replaceAll("另见", "<ref>");
						sb.append(line);
					}
					else if(line.contains("惯用型解释")){
						line = line.replaceAll("惯用型解释", "<habiExp>");
						sb.append(line);
					}else if(line.contains("日文发音")){
						line = line.replaceAll("日文发音", "<Jpron>");
						sb.append(line);
					}
					else if(line.contains("AHD音标")){
						isAHDKK = true;
					}
					else if(line.contains("美国音标")){
						isAHDKK = true;
					}
					else if(line.contains("语源")){
						isPass = true;
					}
					else if(line.contains("继承用法")){
						isPass = true;
					}
					else if(line.contains("国际音标")){
						line = line.replaceAll("国际音标", "<pron>");
						sb.append(line);
					}
					else if(line.contains("汉语拼音")){
						line = line.replaceAll("汉语拼音", "<pinyin>");
						sb.append(line);
					}
					else if(line.contains("单词词性")){
						line = line.replaceAll("单词词性", "<type>");
						sb.append(line);
					}
					else if(line.contains("预解释")){
						line = line.replaceAll("预解释", "<preExp>");
						isPreExp = true;
						sb.append(line);
					}
					else if(line.contains("解释项")){
						line = line.replaceAll("解释项", "<exp>");
						sb.append(line);
					}
					else if(line.contains("跟随注释")){
						line = line.replaceAll("跟随注释", "<flExp>");
						sb.append(line);
					}
					else if(line.contains("例句原型")){
						line = line.replaceAll("例句原型", "<sentsProtype>");
						sb.append(line);
					}
					else if(line.contains("例句解释")){
						line = line.replaceAll("例句解释", "<sentsExp>");
						sb.append(line);
					}
					else if(line.contains("单词原型")){
						line = line.replaceAll("单词原型", "<protype>");
						isProtype = true;
						sb.append(line);
					}
					else if(line.contains("音节分段")){
						line = line.replaceAll("音节分段", "<split>");
						isPass = true;
//						sb.append(line);
					}
					else if(line.contains("单词项")){
						line = line.replaceAll("单词项", "<wordElement>");
						sb.append(line+"\t");
					}
					else if(line.contains("子解释项")){
						line = line.replaceAll("子解释项", "<exp>");
						sb.append(line);
					}
					else if(line.contains("同义词")){
						line = line.replaceAll("同义词", "<SameMeaning>");
						sb.append(line);
					}
				 }
				 break;

			 case XMLStreamConstants.CHARACTERS:
				 String line2 = reader.getText();
				 	if(isAHDKK){
				 		isAHDKK = false;
				 		break;
				 	}
				 	if(isPass){
				 		break;
				 	}
				 	if(isPreExp){
				 		line2 = line2.replaceAll("\\.", "");
				 	}
					if(line2.contains("<![CDATA[")){
						line2 = line2.substring(0,line2.indexOf("<![CDATA["))+line2.substring(line2.indexOf("<![CDATA[")+9);
//						sb.append(line2);
					}
					if(line2.contains("{")){
						line2 = line2.replaceAll("\\{", "[");
					}
					if(line2.contains("}")){
						line2 = line2.replaceAll("\\}", "]");
					}
					if(line2.contains("&")){
						line2 = line2.replaceAll("&.", "");
					}
					if(line2.contains("}]")){
						line2 = line2.replaceAll("}]", "]");
					}
					if(line2.contains("[“]")){
						line2 = line2.replaceAll("\\[“\\]", "ˋ");
					}
					if(line2.contains("</")){
						
					}
					if(line2.contains("]]></")){
						line2 = line2.substring(0,line2.indexOf("]]></"));
					}
					if(line2.contains("D.J.[")){
						line2 = line2.substring(0,line2.indexOf("D.J.["))+line2.substring(line2.indexOf("D.J.[")+5);
					}
					if(line2.contains("&I")){
						line2 = line2.substring(0,line2.indexOf("&I"))+line2.substring(line2.indexOf("&I")+2);
					}
					if(isProtype&&line2.contains("ˋ")){
						line2 = line2.replaceAll("ˋ", "");
					}
					if(isProtype){
						line2 = line2.replaceAll("\\[[”]\\]", "");
					}
	
					sb.append(line2.replaceAll("\n", ""));
			 break;
			 case XMLStreamConstants.END_ELEMENT:
				 String line3 = reader.getName().toString();
					if(line3.contains("单词块")){
//						sb.append("<endWord>");
						bw.write(sb.toString());
						bw.newLine();
					}
					else if(line3.contains("语源")){
						isPass = false;
					}
					else if(line3.contains("继承用法")){
						isPass = false;
					}
					else if(line3.contains("惯用型原型")){
//						line3 = line3.replaceAll("惯用型原型", "</habiProtype>");
						sb.append("\t");
					}
					else if(line3.contains("另见")){
//						line3 = line3.replaceAll("另见", "</ref>");
						sb.append("\t");
					}
					else if(line3.contains("惯用型解释")){
//						line3 = line3.replaceAll("惯用型解释", "</habiExp>");
						sb.append("\t");
					}else if(line3.contains("日文发音")){
						line3 = line3.replaceAll("日文发音", "</Jpron>");
						sb.append(line3+"\t");
					}
					else if(line3.contains("国际音标")){
						line3 = line3.replaceAll("国际音标", "</pron>");
						sb.append(line3+"\t");
					}		
					else if(line3.contains("汉语拼音")){
						line3 = line3.replaceAll("汉语拼音", "</pinyin>");
						sb.append(line3+"\t");
					}
					else if(line3.contains("单词词性")){
//						line3 = line3.replaceAll("单词词性", "</type>");
						sb.append("\t");
					}
					else if(line3.contains("预解释")){
//						line3 = line3.replaceAll("预解释", "</preExp>");
						isPreExp = false;
						sb.append("\t");
					}
					else if(line3.contains("解释项")){
//						line3 = line3.replaceAll("解释项", "</exp>");
						sb.append("\t");
					}
					else if(line3.contains("跟随注释")){
//						line3 = line3.replaceAll("跟随注释", "</flExp>");
						sb.append("\t");
					}
					else if(line3.contains("例句原型")){
//						line3 = line3.replaceAll("例句原型", "</sentsProtype>");
						sb.append("\t");
					}
					else if(line3.contains("例句解释")){
//						line3 = line3.replaceAll("例句解释", "</sentsExp>");
						sb.append("\t");
					}
					else if(line3.contains("单词原型")){
//						line3 = line3.replaceAll("单词原型", "</protype>");
						isProtype = false;
						sb.append("\t");
					}
					else if(line3.contains("音节分段")){
//						line3 = line3.replaceAll("音节分段", "</split>");
						isPass = false;
//						sb.append("\t");
					}
					else if(line3.contains("单词项")){
//						line3 = line3.replaceAll("单词项", "</wordElement>");
						sb.append("\t");
					}
					else if(line3.contains("子解释项")){
//						line3 = line3.replaceAll("子解释项", "</exp>");
						sb.append("\t");
					}
					else if(line3.contains("同义词")){
//						line3 = line3.replaceAll("同义词", "</SameMeaning>");
						sb.append("\t");
					}
			 // if(reader.getName().toString().equals("单词项")){
			 // inSingleMeaning = true;
			 // }
			 break;
			 }
			
			 if (!reader.hasNext())
			 break;
			 event = reader.next();
			 }
					reader.close();
					bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
