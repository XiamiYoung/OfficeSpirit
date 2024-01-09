package com.xiami.logic;

import java.io.File;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;

import com.xiami.bean.DictWordBean;
import com.xiami.core.Keys;
import com.xiami.core.Logger;
import com.xiami.core.SystemConfig;

public class DictLoader{

	private static final int MAX_SHOWED  = Integer.parseInt(SystemConfig.getSystemProp(Keys.MAX_SHOWED));
	
	ArrayList<DictWordBean> eclist = new ArrayList<DictWordBean>();
	ArrayList<DictWordBean> celist = new ArrayList<DictWordBean>();
	ArrayList<DictWordBean> jclist = new ArrayList<DictWordBean>();
	ArrayList<DictWordBean> cjlist = new ArrayList<DictWordBean>();
	
	private static String INDEX_DIR_JC = "dict\\japanese-chinese\\index";
	private static String INDEX_DIR_CJ = "dict\\chinese-japanese\\index";
	private static String INDEX_DIR_CE = "dict\\chinese-english\\index";
	private static String INDEX_DIR_EC = "dict\\english-chinese\\index";
	private static IndexSearcher searcherEC;
	private static IndexSearcher searcherCE;
	private static IndexSearcher searcherJC;
	private static IndexSearcher searcherCJ;
	
	public DictLoader(int index) throws Exception{
		
		if(index==1){
			searcherEC = new IndexSearcher(FSDirectory.open(new File(INDEX_DIR_EC)), true);
			buildCache(1);
		}else if(index==2){
			searcherCE = new IndexSearcher(FSDirectory.open(new File(INDEX_DIR_CE)), true);
			buildCache(2);
		}else if(index==3){
			searcherJC = new IndexSearcher(FSDirectory.open(new File(INDEX_DIR_JC)), true);
			buildCache(3);
		}else if(index==4){
			searcherCJ = new IndexSearcher(FSDirectory.open(new File(INDEX_DIR_CJ)), true);
			buildCache(4);
		}
	}

	public void buildCache(int index) throws Exception{
		if(index==1){
			futherSearchForE2C("abstract",1,false);
		}else if(index==2){
			futherSearchForC2E("信息",1,false);
		}else if(index==3){
			futherSearchForJ2C("には",1,false);
		}else if(index==4){
			futherSearchForC2J("信息",1,false);
		}
	}
	
	public ArrayList<DictWordBean> searchForE2C(String keyWord) throws Exception {
		
//		System.out.println(keyWord);
		
		eclist.clear();
		
		long start = System.currentTimeMillis();
		
		Query queryWord = new PrefixQuery(new Term("word",keyWord)); 

		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWord, BooleanClause.Occur.SHOULD); 
		
		Sort sort = new Sort();
		sort.setSort(new SortField("word", SortField.STRING, false));
		ScoreDoc[] hits = searcherEC.search(booleanQuery, null,MAX_SHOWED, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherEC.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			eclist.add(bean);
		}
		
		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search ec takes time:"+(end - start));
		
		return eclist;
	}
	
	public ArrayList<DictWordBean> futherSearchForE2C(String keyWord,int limit,boolean isNotFound) throws Exception {
		
		eclist.clear();
		
		if(limit<=0){
			return eclist;
		}
		
		long start = System.currentTimeMillis();
		
		Query queryWord; 
		
		Query queryFutherWord;
		
		Query queryFutherSubWord;
		
		if(keyWord.length()>1){
			queryFutherWord = new PrefixQuery(new Term("word",keyWord)); 
			if(isNotFound){
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord));
			}else{
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord.substring(0,keyWord.length()-1)));
			}
		}else{
			queryFutherWord = new PrefixQuery(new Term("word",keyWord)); 
			queryFutherSubWord = new PrefixQuery(new Term("word",keyWord)); 
		}

		queryWord = new TermQuery(new Term("word",keyWord));
		
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWord, BooleanClause.Occur.MUST_NOT); 
//		booleanQuery.add(queryFutherWord, BooleanClause.Occur.MUST_NOT); 
		if(!isNotFound){
			booleanQuery.add(queryFutherWord, BooleanClause.Occur.MUST_NOT); 
		}
		booleanQuery.add(queryFutherSubWord, BooleanClause.Occur.SHOULD); 

		Sort sort = new Sort();
		sort.setSort(new SortField("word", SortField.STRING, false));
		ScoreDoc[] hits = searcherEC.search(booleanQuery, null, limit, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherEC.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			eclist.add(bean);
		}
		
		long end = System.currentTimeMillis();

		Logger.getLogger().info("search futher ec takes time:"+(end - start));
//		
//		System.out.println("Found " + hits.length
//			+ " document(s) (in " + (end - start)
//			+ " milliseconds) that matched query '" + booleanQuery + "':");
		
		return eclist;
	}
	
	public ArrayList<DictWordBean> searchForJ2C(String keyWord) throws Exception {
		
		jclist.clear();
		
		long start = System.currentTimeMillis();
		
		Query queryWordTerm = new TermQuery(new Term("word",keyWord)); 
		Query queryPronTerm = new TermQuery(new Term("pron",keyWord));
		
		Query queryWord = new PrefixQuery(new Term("word",keyWord)); 
		Query queryPron = new PrefixQuery(new Term("pron",keyWord));
		
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWordTerm, BooleanClause.Occur.SHOULD);
		booleanQuery.add(queryPronTerm, BooleanClause.Occur.SHOULD);

		Sort sort = new Sort();
		sort.setSort(new SortField("pron", SortField.STRING, false));
		ScoreDoc[] hits = searcherJC.search(booleanQuery, null,MAX_SHOWED).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherJC.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			jclist.add(bean);
		}
		
		booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWord, BooleanClause.Occur.SHOULD);
		booleanQuery.add(queryPron, BooleanClause.Occur.SHOULD); 
		booleanQuery.add(queryWordTerm, BooleanClause.Occur.MUST_NOT);
		booleanQuery.add(queryPronTerm, BooleanClause.Occur.MUST_NOT); 
		
		int limit = MAX_SHOWED - hits.length;
		
		hits = searcherJC.search(booleanQuery, null,limit,sort).scoreDocs;
		
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherJC.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			jclist.add(bean);
		}
		
		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search jc takes time:"+(end - start));
		
//		System.out.println("Found " + hits.length
//				+ " document(s) (in " + (end - start)
//				+ " milliseconds) that matched query '" + booleanQuery + "':");
		
		return jclist;
	}
	
	public ArrayList<DictWordBean> futherSearchForJ2C(String keyWord,int limit,boolean isNotFound) throws Exception {

		jclist.clear();
		
		if(limit<=0){
			return jclist;
		}
		
		long start = System.currentTimeMillis();
		
		Query queryWord; 
		Query queryPron; 
		
		Query queryFutherSubWord;
		Query queryFutherSubPron;
		
		Query queryFutherWord;
		Query queryFutherPron;
		
		if(keyWord.length()>1){
			queryWord = new PrefixQuery(new Term("word",keyWord));
			queryPron = new PrefixQuery(new Term("pron",keyWord));
			if(isNotFound){
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord));
				queryFutherSubPron = new PrefixQuery(new Term("pron",keyWord)); 
			}else{
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord.substring(0,keyWord.length()-1)));
				queryFutherSubPron = new PrefixQuery(new Term("pron",keyWord.substring(0,keyWord.length()-1))); 
			}
		}else{
			queryFutherSubWord = new PrefixQuery(new Term("word",keyWord)); 
			queryFutherSubPron = new PrefixQuery(new Term("pron",keyWord)); 
			queryWord = new TermQuery(new Term("word",keyWord));
			queryPron = new TermQuery(new Term("pron",keyWord));
		}

		queryFutherWord = new PrefixQuery(new Term("word",keyWord));
		queryFutherPron = new PrefixQuery(new Term("pron",keyWord));
		
		BooleanQuery booleanQuery = new BooleanQuery();
		if(!isNotFound){
			booleanQuery.add(queryWord, BooleanClause.Occur.MUST_NOT); 
			booleanQuery.add(queryPron, BooleanClause.Occur.MUST_NOT); 
		}
	
		booleanQuery.add(queryFutherWord, BooleanClause.Occur.SHOULD); 
		booleanQuery.add(queryFutherPron, BooleanClause.Occur.SHOULD); 
	
		booleanQuery.add(queryFutherSubWord, BooleanClause.Occur.SHOULD); 
		booleanQuery.add(queryFutherSubPron, BooleanClause.Occur.SHOULD); 

		Sort sort = new Sort();
		sort.setSort(new SortField("pron", SortField.STRING, false));
		ScoreDoc[] hits = searcherJC.search(booleanQuery, null, limit).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherJC.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			jclist.add(bean);
		}

		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search futher jc takes time:"+(end - start));
		
//		System.out.println("Futher Found " + hits.length
//				+ " document(s) (in " + (end - start)
//				+ " milliseconds) that matched query '" + booleanQuery + "':");
		
		return jclist;
	}
	
	public ArrayList<DictWordBean> searchForC2J(String keyWord) throws Exception {
		
		cjlist.clear();
		
		long start = System.currentTimeMillis();
		
		Query queryWord = new PrefixQuery(new Term("word",keyWord)); 

		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWord, BooleanClause.Occur.SHOULD); 

		Sort sort = new Sort();
		sort.setSort(new SortField("word", SortField.STRING, false));
		ScoreDoc[] hits = searcherCJ.search(booleanQuery, null,MAX_SHOWED, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherCJ.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			cjlist.add(bean);
		}
		
		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search cj takes time:"+(end - start));
		
		//System.out.println("Found " + hits.length
//				+ " document(s) (in " + (end - start)
//				+ " milliseconds) that matched query '" + booleanQuery + "':");
		
		return cjlist;
	}
	
	
	public ArrayList<DictWordBean> futherSearchForC2J(String keyWord,int limit,boolean isNotFound) throws Exception {
		
		cjlist.clear();
		
		if(limit<=0){
			return cjlist;
		}
		
		long start = System.currentTimeMillis();
		
		Query queryWord; 
		Query queryFutherSubWord;
		Query queryFutherWord;
		
		if(keyWord.length()>1){
			if(isNotFound){
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord));
			}else{
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord.substring(0,keyWord.length()-1)));
			}
			queryWord = new PrefixQuery(new Term("word",keyWord));
		}else{
			queryFutherSubWord = new PrefixQuery(new Term("word",keyWord)); 
			queryWord = new TermQuery(new Term("word",keyWord));
		}

		queryFutherWord = new PrefixQuery(new Term("word",keyWord));
		
		BooleanQuery booleanQuery = new BooleanQuery();
		if(!isNotFound){
			booleanQuery.add(queryWord, BooleanClause.Occur.MUST_NOT); 
		}
		booleanQuery.add(queryFutherSubWord, BooleanClause.Occur.SHOULD); 
		booleanQuery.add(queryFutherWord, BooleanClause.Occur.MUST_NOT); 

//		System.out.println("futher:"+(keyWord.substring(0,keyWord.length()-1)));
		
		
		Sort sort = new Sort();
		sort.setSort(new SortField("word", SortField.STRING, false));
		ScoreDoc[] hits = searcherCJ.search(booleanQuery, null, limit, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherCJ.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			cjlist.add(bean);
		}
		
		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search futher cj takes time:"+(end - start));
		
//		System.out.println("Found " + hits.length
//				+ " document(s) (in " + (end - start)
//				+ " milliseconds) that matched query '" + booleanQuery + "':");

		return cjlist;
	}
	public ArrayList<DictWordBean> searchForC2E(String keyWord) throws Exception {
		
		celist.clear();
		
		long start = System.currentTimeMillis();
		
		Query queryWord = new PrefixQuery(new Term("word",keyWord)); 

		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWord, BooleanClause.Occur.SHOULD); 

		Sort sort = new Sort();
		sort.setSort(new SortField("word", SortField.STRING, false));
		ScoreDoc[] hits = searcherCE.search(booleanQuery, null,MAX_SHOWED, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherCE.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			celist.add(bean);
		}
		
		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search ce takes time:"+(end - start));
		
		//System.out.println("Found " + hits.length
//				+ " document(s) (in " + (end - start)
//				+ " milliseconds) that matched query '" + booleanQuery + "':");
		
		return celist;
	}
	
	public ArrayList<DictWordBean> futherSearchForC2E(String keyWord,int limit,boolean isNotFound) throws Exception {
		
		celist.clear();
		
		if(limit<=0){
			return celist;
		}
		
		long start = System.currentTimeMillis();
		
		Query queryWord; 
		Query queryFutherSubWord;
		Query queryFutherWord;
		
		if(keyWord.length()>1){
			if(isNotFound){
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord));
			}else{
				queryFutherSubWord = new PrefixQuery(new Term("word",keyWord.substring(0,keyWord.length()-1)));
			}
			queryWord = new PrefixQuery(new Term("word",keyWord));
		}else{
			queryFutherSubWord = new PrefixQuery(new Term("word",keyWord)); 
			queryWord = new TermQuery(new Term("word",keyWord));
		}

		queryFutherWord = new PrefixQuery(new Term("word",keyWord));
		
		BooleanQuery booleanQuery = new BooleanQuery();
		if(!isNotFound){
			booleanQuery.add(queryWord, BooleanClause.Occur.MUST_NOT); 
		}
		booleanQuery.add(queryFutherSubWord, BooleanClause.Occur.SHOULD); 
		booleanQuery.add(queryFutherWord, BooleanClause.Occur.MUST_NOT); 
		
		//System.out.println("futher:"+(keyWord.substring(0,keyWord.length()-1)));
		
		
		Sort sort = new Sort();
		sort.setSort(new SortField("word", SortField.STRING, false));
		ScoreDoc[] hits = searcherCE.search(booleanQuery, null, limit, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcherCE.doc(hits[i].doc);
			DictWordBean bean = new DictWordBean();
			bean.setWord(hitDoc.get("word"));
			bean.setPron(hitDoc.get("pron"));
			bean.setSents(getWordContents(hitDoc.get("contents")));
			celist.add(bean);
		}
		
		long end = System.currentTimeMillis();
		
		Logger.getLogger().info("search futher ce takes time:"+(end - start));
		
//		System.out.println("Found " + hits.length
//				+ " document(s) (in " + (end - start)
//				+ " milliseconds) that matched query '" + booleanQuery + "':");

		return celist;
	}
	
	public ArrayList<String> getWordContents(String content){
		ArrayList<String> list = new ArrayList<String>();
		String[] contents = content.split("\t");
		for(int i=0;i<contents.length;i++){
			list.add(contents[i]);
		}
		return list;
	}
	
	
//	public String findJpPrefix(String prefix){
//		if(prefix.equals("あ")
//				||prefix.equals("い")
//				||prefix.equals("う")
//				||prefix.equals("え")
//				||prefix.equals("ア")
//				||prefix.equals("イ")
//				||prefix.equals("ウ")
//				||prefix.equals("エ")
//				||prefix.equals("オ")){
//			return "a";
//		}else if(prefix.equals("か")
//				||prefix.equals("き")
//				||prefix.equals("く")
//				||prefix.equals("け")
//				||prefix.equals("こ")
//				||prefix.equals("カ")
//				||prefix.equals("キ")
//				||prefix.equals("ク")
//				||prefix.equals("ヶ")
//				||prefix.equals("コ")
//				||prefix.equals("が")
//				||prefix.equals("ぎ")
//				||prefix.equals("ぐ")
//				||prefix.equals("げ")
//				||prefix.equals("ご")
//				||prefix.equals("ガ")
//				||prefix.equals("ギ")
//				||prefix.equals("グ")
//				||prefix.equals("ゲ")
//				||prefix.equals("ゴ")){
//			return "k";
//		}else if(prefix.equals("さ")
//				||prefix.equals("し")
//				||prefix.equals("す")
//				||prefix.equals("せ")
//				||prefix.equals("そ")
//				||prefix.equals("サ")
//				||prefix.equals("シ")
//				||prefix.equals("ス")
//				||prefix.equals("セ")
//				||prefix.equals("ソ")
//				||prefix.equals("ざ")
//				||prefix.equals("じ")
//				||prefix.equals("ず")
//				||prefix.equals("ぜ")
//				||prefix.equals("ぞ")
//				||prefix.equals("ザ")
//				||prefix.equals("ジ")
//				||prefix.equals("ズ")
//				||prefix.equals("ゼ")
//				||prefix.equals("ゾ")){
//			return "s";
//		}else if(prefix.equals("た")
//				||prefix.equals("ち")
//				||prefix.equals("つ")
//				||prefix.equals("て")
//				||prefix.equals("と")
//				||prefix.equals("タ")
//				||prefix.equals("チ")
//				||prefix.equals("ツ")
//				||prefix.equals("テ")
//				||prefix.equals("ト")
//				||prefix.equals("だ")
//				||prefix.equals("ぢ")
//				||prefix.equals("づ")
//				||prefix.equals("で")
//				||prefix.equals("ど")
//				||prefix.equals("ダ")
//				||prefix.equals("ヂ")
//				||prefix.equals("ヅ")
//				||prefix.equals("デ")
//				||prefix.equals("ド")){
//			return "t";
//		}else if(prefix.equals("な")
//				||prefix.equals("に")
//				||prefix.equals("ぬ")
//				||prefix.equals("ね")
//				||prefix.equals("の")
//				||prefix.equals("ナ")
//				||prefix.equals("ニ")
//				||prefix.equals("ヌ")
//				||prefix.equals("ネ")
//				||prefix.equals("ノ")){
//			return "n";
//		}else if(prefix.equals("は")
//				||prefix.equals("ひ")
//				||prefix.equals("ふ")
//				||prefix.equals("へ")
//				||prefix.equals("ほ")
//				||prefix.equals("ハ")
//				||prefix.equals("ヒ")
//				||prefix.equals("フ")
//				||prefix.equals("ヘ")
//				||prefix.equals("ホ")
//				||prefix.equals("ば")
//				||prefix.equals("び")
//				||prefix.equals("ぶ")
//				||prefix.equals("べ")
//				||prefix.equals("ぼ")
//				||prefix.equals("バ")
//				||prefix.equals("ビ")
//				||prefix.equals("ブ")
//				||prefix.equals("ベ")
//				||prefix.equals("ボ")
//				||prefix.equals("ぱ")
//				||prefix.equals("ぴ")
//				||prefix.equals("ぷ")
//				||prefix.equals("ぺ")
//				||prefix.equals("ぽ")
//				||prefix.equals("パ")
//				||prefix.equals("ピ")
//				||prefix.equals("プ")
//				||prefix.equals("ペ")
//				||prefix.equals("ポ")){
//			return "h";
//		}
//		else if(prefix.equals("ま")
//				||prefix.equals("み")
//				||prefix.equals("む")
//				||prefix.equals("め")
//				||prefix.equals("も")
//				||prefix.equals("マ")
//				||prefix.equals("ミ")
//				||prefix.equals("ム")
//				||prefix.equals("メ")
//				||prefix.equals("モ")){
//			return "m";
//		}
//		else if(prefix.equals("や")
//				||prefix.equals("ゆ")
//				||prefix.equals("よ")
//				||prefix.equals("ヤ")
//				||prefix.equals("ユ")
//				||prefix.equals("ヨ")){
//			return "y";
//		}
//		else if(prefix.equals("ら")
//				||prefix.equals("り")
//				||prefix.equals("る")
//				||prefix.equals("れ")
//				||prefix.equals("ろ")
//				||prefix.equals("ラ")
//				||prefix.equals("リ")
//				||prefix.equals("ル")
//				||prefix.equals("レ")
//				||prefix.equals("ロ")
//				||prefix.equals("わ")
//				||prefix.equals("を")
//				||prefix.equals("ん")
//				||prefix.equals("ワ")
//				||prefix.equals("ヲ")
//				||prefix.equals("ン")){
//			return "r";
//		}
//		return null;
//	}
//	
	// public void getFromDict() throws Exception {
	//
	// inputFactory = XMLInputFactory.newInstance();
	// reader = inputFactory.createXMLStreamReader(new FileInputStream(
	// "dict/e_a.dict"));
	//
	// boolean isWordElement = false;
	// boolean inWordRange = false;
	// boolean inSingleMeaning = false;
	// boolean inJpPron = false;
	// String item = "";
	// String wordElemet = "";
	// ArrayList<String> sents = null;
	// int item_index = 1;
	//
	// int event = reader.getEventType();
	//
	// DictWordBean bean = null;
	// while (true) {
	// switch (event) {
	// case XMLStreamConstants.START_ELEMENT:
	// if (reader.isWhiteSpace())
	// break;
	// if (reader.getName().toString().equals("单词块")) {
	// inWordRange = true;
	// item_index = 1;
	// }
	// if (reader.getName().toString().equals("单词")) {
	// isWordElement = true;
	// } else {
	// isWordElement = false;
	// }
	// if (reader.getName().toString().equals("日文发音")) {
	// inJpPron = true;
	// } else {
	// inJpPron = false;
	// }
	// if ((!reader.getName().toString().equals("单词解释块")
	// && !reader.getName().toString().equals("基本词义")
	// && !reader.getName().toString().equals("例句")
	// && !reader.getName().toString().equals("单词块")
	// && !reader.getName().toString().equals("单词")
	// && !reader.getName().toString().equals("单词音标")
	// && !reader.getName().toString().equals("root"))) {
	// // item = reader.getName().toString().replaceAll("&I","");
	// // if(reader.getName().toString().equals("单词项")){
	// // wordElemet = "词义"+item_index+++":";
	// // inSingleMeaning = true;
	// // }
	// // if(reader.getName().toString().equals("预解释")){
	// // item = " 词形变化";
	// // }
	// // if(reader.getName().toString().equals("解释项")){
	// // item = " 解释";
	// // }
	// //
	// if(reader.getName().toString().equals("例句原型")||reader.getName().toString().equals("例句解释")){
	// // item = " "+reader.getName().toString();
	// // }
	// // if(reader.getName().toString().equals("例句原型")){
	// // item = " 例句";
	// // }
	// // if(reader.getName().toString().equals("例句解释")){
	// // item = " 例句翻译";
	// // }
	// }
	// break;
	// case XMLStreamConstants.CHARACTERS:
	// // if (reader.isWhiteSpace())
	// // break;
	// if (isWordElement) {
	// bean = new DictWordBean();
	// // sents = new ArrayList<String>();
	// bean.setWord(reader.getText());
	// }
	// // if(inJpPron){
	// // bean.setPron(reader.getText());
	// // }
	// // if (inWordRange && !isWordElement) {
	// // if(inSingleMeaning){
	// // inSingleMeaning = false;
	// // if(item_index==2){
	// // wordElemet = "词义:";
	// // }
	// // sents.add(wordElemet);
	// // }
	// // sents.add(item+":"+reader.getText());
	// // }
	// break;
	// case XMLStreamConstants.END_ELEMENT:
	// if (reader.getName().toString().equals("单词块")) {
	// inWordRange = false;
	// // bean.setSents(sents);
	// wordList.add(bean);
	// }
	// // if(reader.getName().toString().equals("单词项")){
	// // inSingleMeaning = true;
	// // }
	// break;
	// }
	//
	// if (!reader.hasNext())
	// break;
	// event = reader.next();
	// }
	//		reader.close();
	//	}
}
