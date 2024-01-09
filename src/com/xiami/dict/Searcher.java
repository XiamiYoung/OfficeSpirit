package com.xiami.dict;
import java.io.File;
import java.util.Date;

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
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	private static String INDEX_DIR = "dict\\english-chinese\\index";//索引所在的路径
	private static String KEYWORD = "人才";//关键词

	public static void main(String[] args) throws Exception {
		File indexDir = new File(INDEX_DIR);
		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new Exception(indexDir
					+ " does not exist or is not a directory.");
		}
		search(indexDir, KEYWORD);//调用search方法进行查询
	}
	
	public static void search(File indexDir, String keyword) throws Exception {
		
		long start = new Date().getTime();// start time
		
		IndexSearcher searcher = new IndexSearcher(FSDirectory.open(indexDir), true);//read-only

		Query queryWord = new PrefixQuery(new Term("word",keyword)); 
		Query queryPron = new PrefixQuery(new Term("pron",keyword));

		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(queryWord, BooleanClause.Occur.SHOULD); 
		booleanQuery.add(queryPron, BooleanClause.Occur.SHOULD); 

		Sort sort = new Sort();
		sort.setSort(new SortField("pron", SortField.STRING, false));
		ScoreDoc[] hits = searcher.search(booleanQuery, null, 1000, sort).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("word") + ":" + hitDoc.get("pron"));

		}
		
		long end = new Date().getTime();//end time

		System.out.println("Found " + hits.length
				+ " document(s) (in " + (end - start)
				+ " milliseconds) that matched query '" + booleanQuery + "':");
	}
}
