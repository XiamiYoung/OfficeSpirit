package com.xiami.core;

public class FileLocator {
	 @SuppressWarnings("unchecked")
	public static String getAppPath(Class cls){
	        if(cls==null) 
	         throw new java.lang.IllegalArgumentException("Param can not be null");
	        ClassLoader loader=cls.getClassLoader();

	        String clsName=cls.getName()+".class";

	        Package pack=cls.getPackage();
	        String path="";

	        if(pack!=null){
	            String packName=pack.getName();

	           if(packName.startsWith("java.")||packName.startsWith("javax.")) 
	              throw new java.lang.IllegalArgumentException("Do not send a JDK class");

	            clsName=clsName.substring(packName.length()+1);

	            if(packName.indexOf(".")<0) {	            	
	            	path=packName+"/";	            
	            }else{
	                int start=0,end=0;
	                end=packName.indexOf(".");
	                while(end!=-1){
	                    path=path+packName.substring(start,end)+"/";
	                    start=end+1;
	                    end=packName.indexOf(".",start);
	                }
	                path=path+packName.substring(start)+"/";
	            }
	        }
	        java.net.URL url =loader.getResource(path+clsName);

	        String realPath=url.getPath();

	        int pos=realPath.indexOf("file:");
	        if(pos>-1) realPath=realPath.substring(pos+5);

	        pos=realPath.indexOf(path+clsName);
	        realPath=realPath.substring(0,pos-1);

	        if(realPath.endsWith("!"))
	            realPath=realPath.substring(0,realPath.lastIndexOf("/"));

	      try{
	        realPath=java.net.URLDecoder.decode(realPath,"utf-8");
	       }catch(Exception e){throw new RuntimeException(e);}
	       realPath = realPath.substring(1);
	       realPath=realPath.replace("/", "\\");
	       return realPath;
	    }
	 
//	 public static void main(String args[]){
//		 System.out.println(FileLocator.getAppPath(com.xiami.app.starter.class));
//	 }
}
