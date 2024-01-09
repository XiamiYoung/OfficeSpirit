package com.xiami.core;


public class AsciiUtil {

	public static String Ascii2String(int hashCode) {
		return String.valueOf((char)hashCode);
	}

	public static int[] String2Ascii(String s) {
		char[]chars=s.toCharArray(); 
		int[] result = new int[chars.length];
		for(int i=0;i<chars.length;i++){
			result[i] = (int)chars[i];
		}
		return result;
	}
	public static void main(String args[]){
		System.out.println(String2Ascii("ア")[0]);
	}
}

