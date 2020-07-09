package com.mvu.lottery.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StringUtils {
	public static class StringTemplate {
		//private List<String> markerList = new LinkedList<>();
		
		
		private Map<String, String> markerAndValueMap = new LinkedHashMap<String, String>();
		
		/**
		 * 
		 * @param marker
		 * @param value
		 * @return
		 */
		public StringTemplate markerAndValue (String marker, String value) {
			
			markerAndValueMap.put(marker, value);
			
			
			return this;
		}
		
		/**
		 * 
		 * @param templateString
		 * @return
		 */
		public String format(String templateString) {
			final String[] result = new String[] {templateString};
			
			markerAndValueMap.forEach((marker, value) -> {
				result[0] = result[0].replaceAll("\\{"+marker+"\\}", value);
			});
		
			return result[0];
		}
		
	}
	
	
	
	public StringUtils() {
		
	}
	
	public static StringTemplate createStringTeamplate() {
		
		return new StringTemplate();
	}
	
	/**
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String filterTopCommentFromJsonString(String jsonString) {
		//Locate the first {
		
		int pos = jsonString.indexOf("{");
		
		if (pos > 0) {
			return jsonString.substring(pos);
		} else {
			return jsonString;
		}
	}
	
}
