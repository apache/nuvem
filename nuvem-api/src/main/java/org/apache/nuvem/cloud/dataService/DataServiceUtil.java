package org.apache.nuvem.cloud.dataService;

public class DataServiceUtil {
	
	public static String createKey(String kind , String id) {		
		return kind.concat("(").concat(id).concat(")");			
	}
	
	public static String getKind(String key){
		if(key != null){
			return key.substring(0, key.indexOf("("));
		}
		return null;
	}
	
	public static String getId(String key){
		if(key != null){
			return key.substring(key.indexOf("(")+1, key.indexOf(")"));
		}
		return null;
	}

}
