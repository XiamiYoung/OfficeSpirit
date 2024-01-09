package com.xiami.core;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

public class NoticeResource {

	private String XML_PATH;
	private static final String XML_NOTICE_PATH = "xml_notice_path";
	private Document document;

	public Document getXMLResource() throws Exception {
		XML_PATH = SystemConfig.getSystemProp(XML_NOTICE_PATH);
		SAXReader saxReader = new SAXReader();
		document = saxReader.read(new File(XML_PATH));
		return document;
	}
}
