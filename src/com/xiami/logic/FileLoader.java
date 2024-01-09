package com.xiami.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xiami.bean.SheetBean;
import com.xiami.bean.WordBean;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;

public class FileLoader {

	private static File file;

	private static HSSFWorkbook wb;

	public static boolean ShowFile() {
		JFileChooser chooser = null;

		if (SystemConfig.getSystemProp("file.history.path1") != null) {
			chooser = new JFileChooser(SystemConfig
					.getSystemProp("file.history.path1"));
		} else {
			chooser = new JFileChooser();
		}

		chooser.setDragEnabled(true);
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".xls")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "FILES_AND_DIRECTORIES";
			}
		});

		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = chooser.showOpenDialog(new JFrame());
		if (result == JFileChooser.CANCEL_OPTION) {
			return false;
		}
		File dir = chooser.getSelectedFile();

		if (dir == null) {
			return false;
		}else if(!dir.isFile()){
			return false;
		}

		file = dir;
		
		Logger.getLogger().info("file:"+file+"loaded.");

		updateHistory(dir);

		return true;
	}

	public static boolean newFile() throws IOException {

		JFileChooser chooser = null;

		if (SystemConfig.getSystemProp("file.history.path1") != null) {
			chooser = new JFileChooser(SystemConfig
					.getSystemProp("file.history.path1"));
		} else {
			chooser = new JFileChooser();
		}

		chooser.setDragEnabled(true);
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".xls")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "*.xls(No suffix input needed)";
			}
		});

		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = chooser.showSaveDialog(new JFrame());
		if (result == JFileChooser.CANCEL_OPTION) {
			return false;
		}
		File dir = chooser.getSelectedFile();

		String fileName = dir.getAbsolutePath();

		if (!fileName.contains(".xls")) {
			fileName = fileName + ".xls";
		}

		FileOutputStream fop = null;

		File newFile = new File(fileName);

		if (dir.exists()||isExist(dir.getCanonicalPath().toString().substring(0,dir.getCanonicalPath().toString().lastIndexOf("\\")),dir.getName()+".xls")) {
			int i = JOptionPane.showConfirmDialog(new JFrame(),
					"File is already exists,overwrite?");
			if (i == JOptionPane.YES_OPTION){
				fop = getFileOutputStream(newFile);
				fop.close();
			}else{
				return false;
			}
		} else {
			fop = getFileOutputStream(newFile);
			fop.close();
		}
		if (dir == null) {
			return false;
		}

		wb = new HSSFWorkbook();

		wb.createSheet("sheet1");

		FileOutputStream fileOut = new FileOutputStream(newFile);

		wb.write(fileOut);

		fileOut.close();

		file = newFile;

		Logger.getLogger().info("file:"+file+"created.");
		
		updateHistory(file);

		return true;
	}

	public static boolean isExist(String path,String name){
		File filePath = new File(path);
		for(int i =0;i<filePath.listFiles().length;i++){
			if(filePath.listFiles()[i].getAbsolutePath().substring(filePath.listFiles()[i].getAbsolutePath().lastIndexOf("\\")+1).equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public static int getSheetCount(){
		return wb.getNumberOfSheets();
	}
	
	public static ArrayList<SheetBean> ParseExcel()
			throws FileNotFoundException, IOException, NullPointerException,
			RecordFormatException {

		SheetBean sheetbean = null;

		if (null != file) {
			
			wb = new HSSFWorkbook(new FileInputStream(file));

			ArrayList<SheetBean> list = new ArrayList<SheetBean>();
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {

				HSSFSheet sheet = wb.getSheetAt(i);

				sheetbean = new SheetBean();

				sheetbean.setSheetName(wb.getSheetName(i));

				for (int j = 0; j <= sheet.getLastRowNum(); j++) {
					if (null != sheet.getRow(j)
							&& null != sheet.getRow(j).getCell(0)
							&& !"".equals(sheet.getRow(j).getCell(0).toString()
									.trim())) {
						WordBean bean = new WordBean();
						bean.setWord(null != sheet.getRow(j).getCell(0) ? (sheet
										.getRow(j).getCell(0)).toString()
										: "");
						bean.setWord_cn(null != sheet.getRow(j).getCell(1) ? sheet
										.getRow(j).getCell(1).toString()
										: "");
						bean.setPron(null != sheet.getRow(j).getCell(2) ? (sheet
										.getRow(j).getCell(2).toString())
										: "");
						bean.setSentns(null != sheet.getRow(j).getCell(3) ? (sheet
										.getRow(j).getCell(3).toString())
										: "");
						bean.setPhysicalNum(sheet.getRow(j).getRowNum());

						sheetbean.getWordBeanList().add(bean);
						
					}
				}
				list.add(sheetbean);
			}
			return list;
		} else {
			return null;
		}
	}

	public static void updateWord(int index_sheet, int index_row, WordBean bean)
			throws IOException {

		FileOutputStream fileOut = getFileOutputStream(file);

		HSSFSheet sheet = wb.getSheetAt(index_sheet);

		Logger.getLogger().info("update file " + file.getAbsolutePath()+File.separator+file.getName());
		
		Logger.getLogger().info("update word for " + wb.getSheetName(index_sheet));

		HSSFRow row = sheet.getRow(index_row);

		System.out.println("word:" + row.getCell(0).toString());

		row.getCell(0).setCellValue(
				bean.getWord() == null ? "" : bean.getWord().toString());

		row.getCell(1).setCellValue(
				bean.getWord_cn() == null ? "" : bean.getWord_cn().toString());

		row.getCell(2).setCellValue(
				bean.getPron() == null ? "" : bean.getPron().toString());

		row.getCell(3).setCellValue(
				bean.getSentns() == null ? "" : bean.getSentns().toString());

		

		wb.write(fileOut);

		fileOut.close();

	}

	public static void deleteWord(int index_sheet, int index_word,
			int physicalNum, int model_size, SheetBean sheetBean)
			throws IOException {

		FileOutputStream fileOut = getFileOutputStream(file);

		HSSFSheet sheet = wb.getSheetAt(index_sheet);

		//			System.out.println("sheetName:"+wb.getSheetName(index_sheet));			
		//			
		//			System.out.println("physicalNum:"+physicalNum);
		//			
		//			System.out.println("index_word:"+index_word);

		HSSFRow row = sheet.getRow(physicalNum);

		//			System.out.println("word:"+row.getCell(0).toString());

		//			System.out.println("model_size:"+model_size);
		//			
		//			System.out.println("row==null:"+String.valueOf(row==null));
		//			
		//			System.out.println("rowNum:"+row.getRowNum());

		System.out.println("deleting word:"
				+ sheetBean.getWordBeanList().get(index_word).getWord()
				+ "  num is:"
				+ sheetBean.getWordBeanList().get(index_word).getPhysicalNum());

		sheet.removeRow(row);

		sheetBean.getWordBeanList().remove(index_word);

		if (index_word != model_size - 1) {

			sheet.shiftRows(physicalNum + 1, sheet.getLastRowNum(), -1);

			for (int i = 0; i < sheetBean.getWordBeanList().size(); i++) {
				int num = sheetBean.getWordBeanList().get(i).getPhysicalNum();
				if (num > physicalNum) {
					sheetBean.getWordBeanList().get(i).setPhysicalNum(--num);
				}
			}

		}
//		for (int i = 0; i < sheetBean.getWordBeanList().size(); i++) {
//			System.out.println("word "
//					+ sheetBean.getWordBeanList().get(i).getWord() + " num:"
//					+ sheetBean.getWordBeanList().get(i).getPhysicalNum());
//		}

		wb.write(fileOut);

		fileOut.close();

	}

	public static void addWord(int index_sheet, int index_row, int physicalNum,
			int model_size, SheetBean sheetBean, WordBean wordBean)
			throws IOException {

		FileOutputStream fileOut = getFileOutputStream(file);

		HSSFSheet sheet = wb.getSheetAt(index_sheet);

		HSSFRow rowNew = null;

		System.out.println("sheetName:" + wb.getSheetName(index_sheet));
		
		System.out.println("current index:" + index_row);
		
		System.out.println("model_size:" + model_size);
		
		System.out.println("physicalNum:" + physicalNum);
		
		System.out.println("word:" + wordBean.getWord());

		if (index_row == -1) {
			rowNew = sheet.createRow(0);

			rowNew.createCell(0).setCellValue(wordBean.getWord());

			rowNew.createCell(1).setCellValue(wordBean.getWord_cn());

			rowNew.createCell(2).setCellValue(wordBean.getPron());

			rowNew.createCell(3).setCellValue(wordBean.getSentns());
			
			wordBean.setPhysicalNum(0);

			sheetBean.getWordBeanList().add(wordBean);
			
			
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 35000);

		} else if (index_row == model_size - 1) {

			rowNew = sheet.createRow(physicalNum + 1);

			rowNew.createCell(0).setCellValue(wordBean.getWord());

			rowNew.createCell(1).setCellValue(wordBean.getWord_cn());

			rowNew.createCell(2).setCellValue(wordBean.getPron());
			
			rowNew.createCell(3).setCellValue(wordBean.getSentns());		

			wordBean.setPhysicalNum(physicalNum + 1);

			sheetBean.getWordBeanList().add(wordBean);
			
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 25000);

		} else {

			sheet.shiftRows(physicalNum + 1, sheet.getLastRowNum(), 1);

			for (int i = 0; i < sheetBean.getWordBeanList().size(); i++) {
				int num = sheetBean.getWordBeanList().get(i).getPhysicalNum();

				if (num > physicalNum) {
					sheetBean.getWordBeanList().get(i).setPhysicalNum(num + 1);
				}
			}

			if (null != sheet.getRow(physicalNum + 1)) {
				rowNew = sheet.getRow(physicalNum + 1);
			} else {
				rowNew = sheet.createRow(physicalNum + 1);
			}

			rowNew.createCell(0).setCellValue(wordBean.getWord());

			rowNew.createCell(1).setCellValue(wordBean.getWord_cn());

			rowNew.createCell(2).setCellValue(wordBean.getPron());

			rowNew.createCell(3).setCellValue(wordBean.getSentns());			

			wordBean.setPhysicalNum(physicalNum + 1);

			sheetBean.getWordBeanList().add(wordBean);
			
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 25000);
			
			for (int i = sheetBean.getWordBeanList().size() - 1; i > index_row; i--) {
				sheetBean.getWordBeanList().set(i,
						sheetBean.getWordBeanList().get(i - 1));
			}
			sheetBean.getWordBeanList().set(index_row + 1, wordBean);

		}

		//		for (int i = 0; i < sheetBean.getWordBeanList().size(); i++) {
		//			System.out.println("word "
		//					+ sheetBean.getWordBeanList().get(i).getWord() + " num:"
		//					+ sheetBean.getWordBeanList().get(i).getPhysicalNum());
		//
		//		}

		
		wb.write(fileOut);

		fileOut.close();

		Logger.getLogger().info("add word " + wordBean.getWord() +" to file "+file.getAbsolutePath()+File.separator+file.getName());
		
	}

	public static void addSheet(String name) throws IOException {

		FileOutputStream fileOut = getFileOutputStream(file);

		wb.createSheet(name);

		wb.write(fileOut);

		fileOut.close();
		
		Logger.getLogger().info("add sheet " + name +" to file "+file.getAbsolutePath()+File.separator+file.getName());

	}

	public static void updateSheetName(int index_sheet, String name)
			throws IOException {

		FileOutputStream fileOut = getFileOutputStream(file);

		wb.setSheetName(index_sheet, name);

		wb.write(fileOut);

		fileOut.close();
		
		Logger.getLogger().info("update sheet " + name +" for file "+file.getAbsolutePath()+File.separator+file.getName());
	}

	public static void deleteSheet(int index_sheet) throws IOException {

		FileOutputStream fileOut = getFileOutputStream(file);

		wb.removeSheetAt(index_sheet);

		wb.write(fileOut);

		fileOut.close();
		
	}

	private static FileOutputStream getFileOutputStream(File file)
			throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	public static File getFile() {
		return file;
	}

	public static void setFile(File file) {
		FileLoader.file = file;
	}

	public static void updateHistory(File dir) {

		if (SystemConfig.getSystemProp("file.history.file1").equals("")) {
			SystemConfig.setSystemProp("file.history.file1", dir
					.getAbsolutePath());
			SystemConfig.setSystemProp("file.history.path1", dir
					.getAbsolutePath().substring(0,
							dir.getAbsolutePath().lastIndexOf("\\")));
		} else {
			if (SystemConfig.getSystemProp("file.history.file1").equals(
					dir.getAbsolutePath().toString())) {
				return;
			} else if (SystemConfig.getSystemProp("file.history.file2").equals(
					dir.getAbsolutePath().toString())) {
				String fileTemp = SystemConfig.getSystemProp("file.history.file1");
				String pathTemp = SystemConfig.getSystemProp("file.history.path1");
				SystemConfig.setSystemProp("file.history.file1", dir
						.getAbsolutePath());
				SystemConfig.setSystemProp("file.history.path1", dir
						.getAbsolutePath().substring(0,
								dir.getAbsolutePath().lastIndexOf("\\")));
				SystemConfig.setSystemProp("file.history.file2", fileTemp);
				SystemConfig.setSystemProp("file.history.path2", pathTemp);
			} else {
				if (!SystemConfig.getSystemProp("file.history.file2")
						.equals("")) {

					SystemConfig.setSystemProp("file.history.file3",
							SystemConfig.getSystemProp("file.history.file2"));
					SystemConfig.setSystemProp("file.history.path3",
							SystemConfig.getSystemProp("file.history.path2"));

					SystemConfig.setSystemProp("file.history.file2",
							SystemConfig.getSystemProp("file.history.file1"));
					SystemConfig.setSystemProp("file.history.path2",
							SystemConfig.getSystemProp("file.history.path1"));

					SystemConfig.setSystemProp("file.history.file1", dir
							.getAbsolutePath());
					SystemConfig.setSystemProp("file.history.path1", dir
							.getAbsolutePath().substring(0,
									dir.getAbsolutePath().lastIndexOf("\\")));
				} else {
					SystemConfig.setSystemProp("file.history.file2",
							SystemConfig.getSystemProp("file.history.file1"));
					SystemConfig.setSystemProp("file.history.path2",
							SystemConfig.getSystemProp("file.history.path1"));

					SystemConfig.setSystemProp("file.history.file1", dir
							.getAbsolutePath());
					SystemConfig.setSystemProp("file.history.path1", dir
							.getAbsolutePath().substring(0,
									dir.getAbsolutePath().lastIndexOf("\\")));
				}
			}
		}
	}
	
	public static void updateHistoryByIndex(int index) {
		if(index == 2){
			String fileTemp = SystemConfig.getSystemProp("file.history.file2");
			String pathTemp = SystemConfig.getSystemProp("file.history.path2");
			SystemConfig.setSystemProp("file.history.file2", SystemConfig.getSystemProp("file.history.file1"));
			SystemConfig.setSystemProp("file.history.path2", SystemConfig.getSystemProp("file.history.path1"));
			SystemConfig.setSystemProp("file.history.file1", fileTemp);
			SystemConfig.setSystemProp("file.history.path1", pathTemp);
		}else if (index == 3){
			String fileTemp = SystemConfig.getSystemProp("file.history.file3");
			String pathTemp = SystemConfig.getSystemProp("file.history.path3");
			
			SystemConfig.setSystemProp("file.history.file3", SystemConfig.getSystemProp("file.history.file2"));
			SystemConfig.setSystemProp("file.history.path3", SystemConfig.getSystemProp("file.history.path2"));
			
			SystemConfig.setSystemProp("file.history.file2", SystemConfig.getSystemProp("file.history.file1"));
			SystemConfig.setSystemProp("file.history.path2", SystemConfig.getSystemProp("file.history.path1"));
			
			SystemConfig.setSystemProp("file.history.file1", fileTemp);
			SystemConfig.setSystemProp("file.history.path1", pathTemp);
		}
	}
	
	public static void restoreSystemResource() throws IOException {
			File[] fileList = new File(".//backup").listFiles();
			for(int i=0;i<fileList.length;i++){
				FileInputStream fis = new FileInputStream(".//backup//"+fileList[i].getName());
				FileOutputStream fos = new FileOutputStream(
						".//resources//"+fileList[i].getName());
				byte[] buff = new byte[1024];
				int readed = -1;
				while ((readed = fis.read(buff)) > 0)
					fos.write(buff, 0, readed);
				fis.close();
				fos.close();
			}
	}
	
}
