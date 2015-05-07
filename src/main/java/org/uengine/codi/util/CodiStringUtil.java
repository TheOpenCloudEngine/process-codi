package org.uengine.codi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodiStringUtil {

	public final static String URL_SEPARATOR = "/";
	
	public static String firstUpperCase(String value){
		return (new StringBuilder()).append(Character.toUpperCase(value.charAt(0))).append(value.substring(1)).toString();
	}
	 
	public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }
	
	public static String lastLastFileSeparatorChar(String value){
		if(!value.endsWith(URL_SEPARATOR))
			value += CodiStringUtil.URL_SEPARATOR;
		
		return value;
	}
	
	public static boolean isNumeric(String value){
		Pattern pattern = Pattern.compile("[+-]?\\d+"); 
	    return pattern.matcher(value).matches(); 
	}
}
