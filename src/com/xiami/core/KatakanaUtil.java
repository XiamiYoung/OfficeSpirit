package com.xiami.core;

public class KatakanaUtil { 
	  /* 
	   * ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞ 
	   * ただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽ 
	   * まみむめもゃやゅゆょよらりるれろゎわゐゑをん 
	   *  
	   * ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾ 
	   * タダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポ 
	   * マミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶ 
	   */ 
	  public static String KatakanaToHiragana(String s) { 
	    StringBuffer sb = new StringBuffer(s); 
	    for (int i = 0; i  < sb.length(); i++) { 
	      char c = sb.charAt(i); 
	      if (c >= 'ァ' && c  <= 'ン') { 
	        sb.setCharAt(i, (char)(c - 'ァ' + 'ぁ')); 
	      } else if (c == 'ヵ') { 
	        sb.setCharAt(i, 'か'); 
	      } else if (c == 'ヶ') { 
	        sb.setCharAt(i, 'け'); 
	      } else if (c == 'ヴ') { 
	        sb.setCharAt(i, 'う'); 
	        sb.insert(i + 1, '゛'); 
	        i++; 
	      } 
	    } 
	    return sb.toString();     
	  } 
	  public static void main(String[] args) { 
		  
//	    System.out.println(KatakanaToHiragana("ジャヴァ・プログラミング")); 
	  } 
	}
