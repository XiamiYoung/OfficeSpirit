package com.xiami.core;

import java.util.UUID;

public class IDGererator{
	
	public static String getUUID(){
		
		return UUID.randomUUID().toString().replace("-","");
	}
	
}