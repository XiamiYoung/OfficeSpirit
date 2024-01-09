package com.xiami.dict;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
* @author ht
* 索引生成
*
*/
public class Indexer {
	
	private static String INDEX_DIR = "dict\\english-chinese\\index";//索引存放目录
	private static String DATA_DIR = "dict\\english-chinese\\dicts";//小文件存放的目录

	public static void main(String[] args) throws IOException { 
		IndexWriter indexWriter = null; 
		Directory dir = new SimpleFSDirectory(new File(INDEX_DIR)); 
		indexWriter = new IndexWriter(dir,new StandardAnalyzer(Version.LUCENE_30),true,IndexWriter.MaxFieldLength.UNLIMITED); 
		
		File[] files = new File(DATA_DIR).listFiles(); 
		for (int i = 0; i < files.length; i++) { 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[i]),"utf-8"));
			String line="";
			while((line=br.readLine())!=null){
			Document doc = new Document(); 
			//Must not ANALYZED if sort is needed.
			doc.add(new Field("word",line.substring(0,line.indexOf("<wordElement>")), Field.Store.YES, Field.Index.NOT_ANALYZED));
			//			System.out.println("word:"+line.substring(0,line.indexOf("<wordElement>")));
//			if(line.indexOf("<Jpron>")!=-1){
//				doc.add(new Field("pron",line.substring(line.indexOf("<Jpron>")+7,line.indexOf("</Jpron>")), Field.Store.YES, Field.Index.NOT_ANALYZED));
////				System.out.println("pron:"+line.substring(line.indexOf("<Jpron>")+7,line.indexOf("</Jpron>")));
//			}
			if(line.indexOf("<pron>")!=-1){
				doc.add(new Field("pron",line.substring(line.indexOf("<pron>")+6,line.indexOf("</pron>")), Field.Store.YES, Field.Index.NOT_ANALYZED));
//				System.out.println("pron:"+line.substring(line.indexOf("<pinyin>")+7,line.indexOf("</pinyin>")));
			}
//			if(line.indexOf("<pinyin>")!=-1){
//				doc.add(new Field("pron",line.substring(line.indexOf("<pinyin>")+8,line.indexOf("</pinyin>")), Field.Store.YES, Field.Index.NOT_ANALYZED));
////				System.out.println("pron:"+line.substring(line.indexOf("<pinyin>")+7,line.indexOf("</pinyin>")));
//			}
			doc.add(new Field("contents",line.substring(line.indexOf("<wordElement>")), Field.Store.YES, Field.Index.ANALYZED));
//			System.out.println("contents:"+line.substring(line.indexOf("<wordElement>")));
	//		doc.add(new Field("contents", getFileContent(files[i]),  Field.Store.YES,   
	//                Field.Index.ANALYZED,   
	//                Field.TermVector.WITH_POSITIONS_OFFSETS)); 
			indexWriter.addDocument(doc); 
		}
		} 
		indexWriter.optimize();
		System.out.println("numDocs"+indexWriter.numDocs()); 
		indexWriter.close(); 

		} 
	public static String getFileContent(File file){
		StringBuffer sb = new StringBuffer("");
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		String line = "";
		while((line=br.readLine())!=null){
			sb.append(line);
		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
