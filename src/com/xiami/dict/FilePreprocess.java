package com.xiami.dict;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FilePreprocess {
	
	private static String outputpath = "dict\\english-chinese\\dicts";//小文件存放路径
	private static String source = "dict\\english-chinese\\ec_cvs.dict";//原文件存放路径
	private static String filename = "ec";//小文件的文件名前缀
	
	public static void main(String[] arg) {
		if (!new File(outputpath).exists()) {
			new File(outputpath).mkdirs();
		}

		splitToSmallFiles(new File(source), outputpath);
	}

	/**大文件切割为小的
	* @param file
	* @param outputpath
	*/
	public static void splitToSmallFiles(File file, String outputpath) {
		int filePointer = 0;
		int MAX_SIZE = 10240;//小文件大小
		
		BufferedWriter writer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				buffer.append(line).append("\r\n");
				if (buffer.toString().getBytes().length >= MAX_SIZE) {
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputpath
							+"\\"+ filename + filePointer)),"UTF-8"));
					writer.write(buffer.toString());
					writer.close();
					filePointer++;
					buffer = new StringBuffer();
				}
				line = reader.readLine();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputpath
					+"\\"+ filename + filePointer)),"UTF-8"));
			writer.write(buffer.toString());
			writer.close();
			System.out.println("The file hava splited to small files !");
		} catch (FileNotFoundException e) {
			System.out.println("file not found !");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}