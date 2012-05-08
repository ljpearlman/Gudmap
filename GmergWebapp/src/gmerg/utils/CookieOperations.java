package gmerg.utils;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieOperations {
    public CookieOperations() {
    }

    private static String encodeCookie(String s) {
    	String t = new String(s);
    	
    	String[][] cookieEncodeChars = {{"\\%", "%25"},
    									{"\\+", "%2B"},
    									{"\\;", "%3B"},
    									{"\\,", "%2C"},
    									{"\\=", "%3D"},
    									{"\\&", "%26"},
    									{"\\:", "%3A"},
    									{"\\s", "+"}};

    	for (int i=0; i<cookieEncodeChars.length; i++) {
    		t=t.replaceAll(cookieEncodeChars[i][0], cookieEncodeChars[i][1]);
    	}    		
    	return t; 
    }
    
    private static String decodeCookie(String s) {
    	String t = new String(s);
    	
    	String[][] cookieDecodeChars = {{"\\+", " "},
    								    {"\\%3A", ":"},
    									{"\\%26", "&"},
    									{"\\%3D", "="},
    									{"\\%2C", ","},
    									{"\\%3B", ";"},
    									{"\\%2B", "+"},
    									{"\\%25", "%"}};    	
    	
    	for (int i=0; i<cookieDecodeChars.length; i++) {
    		t=t.replaceAll(cookieDecodeChars[i][0], cookieDecodeChars[i][1]);
    	}    		
    	return t; 
    }
    
//    public static int setValuesInCookie(String name, String[] values) {
    public static ArrayList<String> setValuesInCookie(String name, String[] values) {
        return setValuesInCookie(name, values, false);
    }
    
    public static ArrayList<String> setValuesInCookie(String name, String[] values, boolean ignoreCase) {
        String cookieString = getCookieValue(name);
//        int addedItems = 0; 
        if (values != null) {
            if (cookieString == null) 
            	cookieString = "";
            for (int i=0; i<values.length; i++) 
                if (!checkValue(cookieString, values[i], ignoreCase)) 
                    cookieString += "/" + values[i];
            if (cookieString.length()>=1 && cookieString.substring(0, 1).equals("/")) 
                cookieString = cookieString.substring(1);
            addLongLivedCookie(name, cookieString);
        }
//        return addedItems;
        
    	ArrayList<String> newCookieValues = new ArrayList<String>();
	    if (cookieString != null && !"".equals(cookieString) ) {  
	    	String[] ids = cookieString.split("/");
	    	for(int i=0; i<ids.length; i++)
	    		newCookieValues.add(ids[i]);
	    }
	    
        return newCookieValues; 
    }
    
    public static void setValueInCookie(String name, String value,int cookieLife,boolean rootPath){
        Cookie cookie = new Cookie(name, encodeCookie(value));
        if(rootPath){
            cookie.setPath("/");
        }
        cookie.setMaxAge(cookieLife);
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(cookie);
    }

    public static ArrayList<String> removeSelectedValuesFromCookie(String name, String[] values) {
        return removeSelectedValuesFromCookie(name, values, false);
    }
        
    public static ArrayList<String> removeSelectedValuesFromCookie(String name, String[] values, boolean ignoreCase) {
    	ArrayList<String> newCookieValues = new ArrayList<String>();
        String cookieString = getCookieValue(name);
        
        if (!(cookieString==null || cookieString.equals("") || values == null)) {
            String[] subsInCookie = cookieString.split("/");
            String newString = "";
            String delimiter = "";
            for (int i=0; i<subsInCookie.length; i++)
            	if (Utility.stringArraySearch(values, subsInCookie[i], ignoreCase)<0) {
            		newCookieValues.add(subsInCookie[i]);
            		newString += delimiter + subsInCookie[i];
            		delimiter = "/";
            	}
            addLongLivedCookie(name, newString);
        }
        return newCookieValues;
    }

    public static void deleteCookie(String cookieName) {

    	FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest)context.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        Cookie candidateCookie = null;
        for (int i=0;i<cookies.length;i++) {
        	if (cookies[i].getName().equals(cookieName)) {
        		candidateCookie = cookies[i];
        		candidateCookie.setMaxAge(0);
        		HttpServletResponse response =
        			(HttpServletResponse)context.getExternalContext().getResponse();
        		response.addCookie(candidateCookie);
//        		System.out.println("cookie deleted!");
        		break;
        	}
        }
    }

    public static void replaceCookieValues(String name, String[] values) {
        replaceCookieValues(name, values, false);
    }
    
    public static void replaceCookieValues(String name, String[] values, boolean ignoreCase) {
        if (values != null) {
            String cookieString = "";
            for (int i = 0; i < values.length; i++) {
                if (!checkValue(cookieString, values[i], ignoreCase))
                    cookieString += "/" + values[i];
            }
            if (cookieString.length() >= 1 && cookieString.substring(0, 1).equals("/"))
                cookieString = cookieString.substring(1);
            addLongLivedCookie(name, cookieString);
        }
    }

    public static void removeAllValuesFromCookie(String name) {
        LongLivedCookie userCookie = new LongLivedCookie(name, "");
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(userCookie);
    }

    private static void addLongLivedCookie(String name, String cookieString) {
        try {
        	String value = encodeCookie(cookieString);
            LongLivedCookie userCookie = new LongLivedCookie(name, value);
            ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(userCookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCookieValue(String cookieName) {
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (int i = 0; i<cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName()))
                    return (decodeCookie(cookie.getValue()));
            }
        return (null);
    }

    public static String getCookieValue(String cookieName, String defaultValue, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName()))
                    return (decodeCookie(cookie.getValue()));
            }
        }
        return (defaultValue);
    }    
    
    public static boolean checkValue(String cookie, String value, boolean ignoreCase) {
        String[] valueList = null;
        if (null != cookie && cookie.length() > 0) {
            valueList = cookie.split("/");
            for (int i = 0; i < valueList.length; i++) 
                if (!ignoreCase && valueList[i].equals(value) || ignoreCase&& valueList[i].equalsIgnoreCase(value))
                    return true;
        }
        return false;
    }

}


