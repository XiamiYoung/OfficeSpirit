package com.xiami.dict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.xiami.bean.DictWordBean;
import com.xiami.core.Keys;

public class DictMaker {
	
	static int total_line = 1;
	
	public static void makeOriginalDict(){
		File[] files = new File("dict2") .listFiles();
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		
		for(int i=0;i<files.length;i++){
		int count = 0;
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(files[i])));
		fos = new FileOutputStream(new File("dict/"+files[i].getName()));
		osw = new OutputStreamWriter(fos,"utf-8");
		BufferedWriter bw = new BufferedWriter(osw);
		String line = "";
		while ((line = br.readLine()) != null) {
			if(line.contains("<root>")){
				continue;
			}
			if(line.startsWith("<单词块>")){
				continue;
			}
			if(line.startsWith("<单词>")){
				if(count>5){
					line = line.replaceAll("<单词>", "<endWord>"+"\n"+"<word>");
				}else{
					line = line.replaceAll("<单词>", "<word>");
				}
			}
			if(line.contains("<![CDATA[")){
				line = line.substring(0,line.indexOf("<![CDATA["))+line.substring(line.indexOf("<![CDATA[")+9);
			}
			if(line.contains("{")){
				line = line.replaceAll("\\{", "[");
			}
			if(line.contains("}")){
				line = line.replaceAll("\\}", "]");
			}
			if(line.contains("&")){
				line = line.replaceAll("&.", "");
			}
			if(line.contains("}]")){
				line = line.replaceAll("}]", "]");
			}
			if(line.contains("[“]")){
				line = line.replaceAll("\\[“\\]", "ˋ");
			}
			if(line.startsWith("</")){
				continue;
			}
			if(line.contains("]]></")){
				line = line.substring(0,line.indexOf("]]></"));
			}
			if(line.contains("D.J.[")){
				line = line.substring(0,line.indexOf("D.J.["))+line.substring(line.indexOf("D.J.[")+5);
			}
			if(line.contains("&I")){
				line = line.substring(0,line.indexOf("&I"))+line.substring(line.indexOf("&I")+2);
			}
			if(line.equals("")){
				continue;
			}else if(line.startsWith("<AHD音标>")){
				continue;
			}
			else if(line.startsWith("<美国音标>")){
				continue;
			}
			else if(line.startsWith("<例句>")){
				continue;
			}
			else if(line.startsWith("<注释>")){
				continue;
			}
			else if(line.startsWith("<基本词义>")){
				continue;
			}
			else if(line.startsWith("<用法>")){
				continue;
			}
			else if(line.startsWith("<单词音标>")){
				continue;
			}
			else if(line.startsWith("<习惯用语>")){
				continue;
			}
			else if(line.startsWith("<常用词组>")){
				continue;
			}
			else if(line.startsWith("<单词解释块>")){
				continue;
			}
			else if(line.startsWith("<继承用法>")){
				continue;
			}
			else if(line.startsWith("<惯用型>")){
				continue;
			}else if(line.startsWith("<语源>")){
				continue;
			}else if(line.startsWith("<参考词汇>")){
				continue;
			}
			else if(line.startsWith("<惯用型原型>")){
				line = line.replaceAll("<惯用型原型>", "<habiProtype>");
			}
			else if(line.startsWith("<另见>")){
				line = line.replaceAll("<另见>", "<ref>");
			}
			else if(line.startsWith("<惯用型解释>")){
				line = line.replaceAll("<惯用型解释>", "<habiExp>");
			}else if(line.startsWith("<日文发音>")){
				line = line.replaceAll("<日文发音>", "<Jpron>");
			}
			else if(line.startsWith("<国际音标>")){
				line = line.replaceAll("<国际音标>", "<pron>");
			}		
			else if(line.startsWith("<汉语拼音>")){
				line = line.replaceAll("<汉语拼音>", "<pinyin>");
			}
			else if(line.startsWith("<单词词性>")){
				line = line.replaceAll("<单词词性>", "<type>");
			}
			else if(line.startsWith("<预解释>")){
				line = line.replaceAll("<预解释>", "<preExp>");
			}
			else if(line.startsWith("<解释项>")){
				line = line.replaceAll("<解释项>", "<exp>");
			}
			else if(line.startsWith("<跟随注释>")){
				line = line.replaceAll("<跟随注释>", "<flExp>");
			}
			else if(line.startsWith("<例句原型>")){
				line = line.replaceAll("<例句原型>", "<sentsProtype>");
			}
			else if(line.startsWith("<例句解释>")){
				line = line.replaceAll("<例句解释>", "<sentsExp>");
			}
			else if(line.startsWith("<单词原型>")){
				line = line.replaceAll("<单词原型>", "<protype>");
			}
			else if(line.startsWith("<音节分段>")){
				line = line.replaceAll("<音节分段>", "<split>");
			}
			else if(line.startsWith("<单词项>")){
				line = line.replaceAll("<单词项>", "<wordElement>");
			}
			else if(line.startsWith("<子解释项>")){
				line = line.replaceAll("<子解释项>", "<exp>");
			}
			else if(line.startsWith("<同义词>")){
				line = line.replaceAll("<同义词>", "<SameMeaning>");
			}
			bw.write(line);
			bw.newLine();
			count++;
		}
		bw.write("<endWord>");
		br.close();
		bw.close();
		fos.close();	

		}catch (Exception e) {
			e.printStackTrace();
		}
		}
		System.out.println("done!");
	}
	
	public static void makeSplitJPDict(String[] keyWord,String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("dict/jp_hanzi.dict")));
			String line = "";
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			boolean isFind = false;
			boolean isNeed = false;
			fos = new FileOutputStream(new File(fileName));
			osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			int local_line = 0;
//			while ((line = br.readLine()) != null) {
//				for(int i=0;i<keyWord.length;i++){		
//					if ((line.startsWith(Keys.WORD_START + keyWord[i])
////							  ||line.startsWith(Keys.J_PRON + keyWord[i])
//							)) {
//							if(line.startsWith(Keys.WORD_START + "ご")){
//								System.out.println("total:"+line);
//								total_line++;
//							}
//								isWordStart = true;
//								break;
//							}
//					}
//					if (isWordStart&&line.startsWith(Keys.WORD_END)) {
//						isWordStart = false;
//						isWordEnd = true;
//					}else{
//						isWordEnd = false;
//					}
//					if(isWordStart||isWordEnd){
//						if(line.startsWith(Keys.WORD_START + "ご")){
//							System.out.println("local:"+line);
//							local_line++;
//						}
//						bw.write(line);
//						bw.newLine();
////						total_line++;
//					}
//					
//			}
			DictWordBean bean = new DictWordBean();
		while ((line = br.readLine()) != null) {
				if (line.startsWith(Keys.WORD_START)) {
					isFind = true;
					bean.setWord(line);
				}else if (isFind && line.startsWith(Keys.WORD_END)) {
					isFind = false;
					isNeed = isMatch(bean,keyWord);
					if (isNeed) {
						if(bean.getPron()!=null&&!bean.getPron().equals("")){
							bw.write(bean.getWord()+"("+bean.getPron().substring(Keys.J_PRON.length())+")");
							bw.newLine();
						}else{
							bw.write(bean.getWord());
							bw.newLine();
						}
						local_line++;
						if(bean.getPron()!=null){
							bw.write(bean.getPron());
							bw.newLine();
							local_line++;
						}
						for(int j=0;j<bean.getSents().size();j++){
							bw.write(bean.getSents().get(j));
							bw.newLine();
							local_line++;
						}
						bw.write("<endWord>");
						bw.newLine();
						local_line++;
						// total_line++;
					}
					bean = new DictWordBean();
				} else if (isFind && line.startsWith(Keys.J_PRON)) {
					bean.setPron(line);
				} else {
					if (isFind) {
						bean.getSents().add(line);
					}
				}
			}
			System.out.println("localLine:"+local_line);
			br.close();
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isMatch(DictWordBean bean, String[] keyWord) {
		boolean flag = false;
		for (int i = 0; i < keyWord.length; i++) {
			if ((bean.getWord().startsWith(Keys.WORD_START + keyWord[i]))) {
				flag = true;
				return flag;
			}
			if (bean.getPron() == null) {
				return flag;
			}
			if (bean.getPron().startsWith(Keys.J_PRON + keyWord[i])) {
				flag = true;
				return flag;
			}
		}
		return flag;
	}
	
	public static void main(String[] args) {
//		DictMaker.makeSplitJPDict(new String[]{"あ","い","う","え","お","ア","イ","ウ","エ","オ"},"dict/jp_a_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"か","き","く","け","こ","カ","キ","ク","ヶ","コ","が","ぎ","ぐ","げ","ご","ガ","ギ","グ","ゲ","ゴ"},"dict/jp_k_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"さ","し","す","せ","そ","サ","シ","ス","セ","ソ","ザ","じ","ず","ぜ","ぞ","ザ","ジ","ズ","ゼ","ゾ"},"dict/jp_s_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"た","ち","つ","て","と","タ","チ","ツ","テ","ト","だ","ぢ","づ","で","ど","ダ","ヂ","ヅ","デ","ド"},"dict/jp_t_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"な","に","ぬ","ね","の","ナ","ニ","ヌ","ネ","ノ"},"dict/jp_n_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"は","ひ","ふ","へ","ほ","ハ","ヒ","フ","ヘ","ホ","ば","び","ぶ","べ","ぼ","バ","ビ","ブ","ベ","ボ","ぱ","ぴ","ぷ","ぺ","ぽ","パ","ピ","プ","ペ","ポ"},"dict/jp_h_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"ま","み","む","め","も","マ","ミ","ム","メ","モ"},"dict/jp_m_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"や","ゆ","よ","ヤ","ユ","ヨ"},"dict/jp_y_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"ら","り","る","れ","ろ","ラ","リ","ル","レ","ロ","わ","を","ん","ワ","ヲ","ン"},"dict/jp_r_new.dict");
//		DictMaker.makeSplitJPDict(new String[]{"あ","い","う","え","お","ア","イ","ウ","エ","オ","か","き","く","け","こ","カ","キ","ク","ヶ","コ","が","ぎ","ぐ","げ","ご","ガ","ギ","グ","ゲ","ゴ","さ","し","す","せ","そ","サ","シ","ス","セ","ソ","ザ","じ","ず","ぜ","ぞ","ザ","ジ","ズ","ゼ","ゾ",
//				"た","ち","つ","て","と","タ","チ","ツ","テ","ト","だ","ぢ","づ","で","ど","ダ","ヂ","ヅ","デ","ド","な","に","ぬ","ね","の","ナ","ニ","ヌ","ネ","ノ",
//				"は","ひ","ふ","へ","ほ","ハ","ヒ","フ","ヘ","ホ","ば","び","ぶ","べ","ぼ","バ","ビ","ブ","ベ","ボ","ぱ","ぴ","ぷ","ぺ","ぽ","パ","ピ","プ","ペ","ポ",
//				"ま","み","む","め","も","マ","ミ","ム","メ","モ","や","ゆ","よ","ヤ","ユ","ヨ","ら","り","る","れ","ろ","ラ","リ","ル","レ","ロ","わ","を","ん","ワ","ヲ","ン"},"dict/jp_hanzi_new.dict");
//		System.out.println("total_line:"+total_line);
		DictMaker.makeOriginalDict();
	}
}
