package com.xiami.core;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class PhoneticUtil {
	
	private static ActiveXComponent app;
	private static Dispatch excelObject;
	private static Dispatch objLangSet ;

	
	public static void initActiveXComponent(){
		
		Logger.getLogger().info("initiating excel.application");
		
		app = new ActiveXComponent("Excel.Application");
		excelObject = app.getObject();
		
		Logger.getLogger().info("initiation done");
	}
	
	/**
     * Max length for param String : 100
     */
	public static String getPhonetic(String word,int type) throws Exception{	
		
//		String[] arrayString = word.split("\\pP");
		
//		System.out.println(word);
		
		StringBuffer value = new StringBuffer();
	
		value.append(Dispatch.call((Dispatch)excelObject,  "GetPhonetic", word));
		
//		for(int i=0;i<arrayString.length;i++){
//		
//			value.append(Dispatch.call((Dispatch)excelObject,  "GetPhonetic", arrayString[i]).toString());
//		
//		}
		
		if(type == 0){
			return value.toString();
		}else{
			return KatakanaUtil.KatakanaToHiragana(value.toString());
		}
	}
	
	/** 
	 * Function from VBA.
	 * 
     * Set objLangSet = Application.LanguageSettings
     * MsgBox objLangSet.LanguageID(msoLanguageIDInstall)
     * 
     * LanguageSettings is a member of excel application.
     * msoLanguageIDInstall is a constant valued 1.   Parameter should be new Variant(1);
     */
	public static boolean getLanguageSetting() throws Exception{
		
		objLangSet = app.getProperty("LanguageSettings").toDispatch();
		
		Variant var = Dispatch.call((Dispatch)objLangSet,  "LanguageID", new Variant(1));
		
		int id = var.getInt();
		
		//1041 : Japanese
		//2052 : SimplifiedChinese
		//1033 : US
		if(id==1041){
			return true;	
		}else{
			return false;
		}
		
	}
	
	public static void quit(){
		
		Logger.getLogger().info("ready to quit excel.application");
		
		//parameter null : new Variant[] {}
		app.invoke("quit", new Variant[] {});
		ComThread.Release();
		
		Logger.getLogger().info("quit excel.application");
	}
	
//	public static void main(String args[]) {
//		initActiveXComponent();
//		try {
////			System.out.println(PhoneticUtil.getPhonetic("独立的に全ての仕事を完成できる",1));
//			getLanguageSetting();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		quit();
//	}
}
