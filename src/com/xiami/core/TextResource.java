package com.xiami.core;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

public class TextResource {
	
	private Document document_text;
	private Document document_text_file;

	public Document getTextResource(String path) throws Exception {
		SAXReader saxReader = new SAXReader();
		document_text = saxReader.read(new File(path));
		return document_text;
	}
	
	public Document getTextFileResource() throws Exception {
		SAXReader saxReader = new SAXReader();
		document_text_file = saxReader.read(new File(SystemConfig
				.getSystemProp(Keys.XML_TEXT_FILE_PATH)));
		return document_text_file;
	}
}

