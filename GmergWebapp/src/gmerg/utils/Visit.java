package gmerg.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class Visit {
/*	
	String[] params;
	HashMap<String, String> paramValues;

    public Visit() {
    	params = new String[] {"organ", "queryType", "queryParam", "sub", "exp", "wildcard"};
    	paramValues = new HashMap<String, String>();
    }
    
    public String getParam(String key) {
    	return paramValues.get(key);
    }
    
    public String[] getParams(String key) {
    	String value = paramValues.get(key);
    	if (value==null || value.equals(""))
    		return null;
    	
    	return value.split("\t|\n|\r|\f|,");
    }
    
    public String getRequestUrlParams() {
    	return getRequestUrlParams(paramValues);
    }
*/

	public static void setStatusParam(String name, Object value) {
//System.out.println("visittttttttttttt pass="+name+"     value="+value);		
		FacesUtil.setFacesRequestParamValue("_visit_"+name, String.valueOf(value));
	}
	
	public static String getStaticStatusParam(Set<String> excludedParams) {
		String status = getStatus(excludedParams);
		if (status==null || status.equals(""))
			return "";
		return "statusParams=" + status; 
	}
	
	public static String getAddedStatusUrl(String url) {
		String newUrl = url;
 		String[] urlParams = null;
 		Set<String> urlParamNames = new HashSet<String>(); 
 		if (url.indexOf("?")>=0) {
 			urlParams = url.substring(url.indexOf("?")).split("&");
 			for (int i=0; i<urlParams.length; i++) 
 				urlParamNames.add(urlParams[i].split("=")[0]);
 		}
 		
 		String statusParam = Visit.getStaticStatusParam(urlParamNames);
 		if (statusParam != null && !statusParam.equals("")) 
 			newUrl += ((newUrl.indexOf("?") < 0)? "?" : "&") + statusParam;
		return newUrl;
	}
	
	public String getStatusParam() {
		return getStaticStatusParam(null); 
	}
	
	public static String getStatus() {
		return getStatus(null);
	}
	
	public static String getStatus(Set<String> excludedParams) {
		String storedStatusParam = FacesUtil.getRequestParamValue("statusParams");
		HashMap<String, String>statusParams = unpackParams(storedStatusParam);
		
		if (statusParams!=null && excludedParams!=null)
			for (String key: excludedParams)
				if (statusParams.containsKey(key))
					statusParams.remove(key);
		
		HashMap<String, String>urlParams = getUrlParams();
		for(String param: urlParams.keySet()) {
			if ("statusParams".equals(param) || (excludedParams!=null && excludedParams.contains(param)))
				continue;
			String value = urlParams.get(param); //urlDecode(urlParams.get(param));		decoding is already done in getUrlParams()
			statusParams.put(param, value);
		}
		
		// retreived parameter passed for saving in the state 
		Map<String, Object> requestMap= FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
		for(String param: requestMap.keySet()) {			
			if (param.indexOf("_visit_")!=0 || (excludedParams!=null && excludedParams.contains(param.substring(7))))
				continue;
			String value = FacesUtil.getFacesRequestParamValue(param);
//System.out.println("visittttttttttttt getstatus="+name.substring(7)+"     value="+value);		
			statusParams.put(param.substring(7), value);
		}

		return packParams(statusParams);
	}
	
	public static HashMap<String, String> getUrlParams() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String queryString = request.getQueryString();
		return unpackParams(queryString);
	}

	public static String getRequestParam(String name) {
//		String param = FacesUtil.getRequestParamValue(name);
		String param = FacesUtil.getAnyRequestParamValue(name);
		if (!(param==null || param.equals("")))
			return param;

		param = FacesUtil.getFacesRequestParamValue("_visit_"+name);
		if (param!=null)
			return param;

		String statusParamsString =  FacesUtil.getRequestParamValue("statusParams");
		HashMap<String, String>statusParamsMap = unpackParams(statusParamsString);
		return statusParamsMap.get(name);
	}
	
	public static String getRequestParam(String name, String defaultValue) {
		return Utility.getValue(Visit.getRequestParam(name), defaultValue);
	}
		
		
	
	/*	
	
	public static String getAllRequestParams(String[] exceptKeys) {
		String allParams = "";
		Set<String> keys = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			boolean found = false;
			for (int i=0; i<exceptKeys.length && !found; i++)
				if (key.indexOf(exceptKeys[i])>=0)
					found = true;
			System.out.println(found+"============all request keys========="+key);
			if (!found)
			{				
				allParams = appendUrlParam(allParams, key, FacesUtil.getRequestParamValue(key));
			System.out.println(key+"--1------"+FacesUtil.getRequestParamValue(key)+"___________"+allParams);
			}			
		}
		return allParams;
	}

    public static String getRequestParams(HashMap<String, String> params) {
    	String urlParams = "";
    	Set<String> keys = params.keySet();
    	Iterator<String> iterator = keys.iterator();
    	while (iterator.hasNext()) {
    		String key = iterator.next();
    		urlParams = appendUrlParam(urlParams, key, params.get(key));
    	}
    	return urlParams;
    }
*/
    /*******************************************************************************
     * Private methods
     * 
     */
	public static String packParams(HashMap<String, String> paramsMap) {
		return packParams(paramsMap, null); 
	}
	
	public static String packParams(HashMap<String, String> paramsMap, String originalParamsString) {
		String paramsString = (originalParamsString==null)? "" : originalParamsString;
		Set<String> keys = paramsMap.keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			String encodedValue = urlEncode(paramsMap.get(name));
			if (encodedValue != null) {
				paramsString += (paramsString.equals(""))? "" : "&";
				paramsString += name + "=" + encodedValue;
			}
		}
		return paramsString;
	}
    
    /*public static String packArrayParams(HashMap<String, String []> paramsMap, String paramsString){
    	Set<String> keys = paramsMap.keySet();
    	Iterator<String> iterator = keys.iterator();
    	while(iterator.hasNext()){
    		String name = iterator.next();
    		String [] values = paramsMap.get(name);
    		if(values != null){
    			for(int i =0;i<values.length;i++){
    				if(values[i] != null && !values[i].equals("")) {
    					String encodedValue = urlEncode(values[i]);
    					paramsString += (paramsString.equals(""))? "" : "&";
    					paramsString += name + "=" + encodedValue;
    				}
    			}
    		}
    	}
    	return paramsString;
    }*/
    
	public static HashMap<String, String> unpackParams(String paramsString) {
		HashMap<String, String>paramsMap = new HashMap<String, String>(); 
		if (paramsString==null || paramsString.equals(""))
			return paramsMap;  
		String[] params = paramsString.split("&");
		for (int i=0; i<params.length; i++) {
			String[] queryParam = params[i].split("=");
			String name = queryParam[0];
			String value = (queryParam.length<2)?"" : queryParam[1];
			paramsMap.put(name, urlDecode(value));
		}
		return paramsMap;  
	}

/*    
	private static String appendUrlParam(String urlParams, String key, String value) {
		String params = urlParams;
		String encodedValue = urlEncode(value);
		if (encodedValue != null) {
			params += (urlParams.equals(""))? "" : "&";
			params += key + "=" + encodedValue;
		}
		return params;
	}
*/	
	private static String urlEncode(String param) {
    	if (param==null || param.equals(""))
    		return null;
		try {
			String encodedParam = URLEncoder.encode(param, "ISO-8859-1");
			return encodedParam.replaceAll("\\.", "%2E");	// This is added to replace all dots passed as parameter with the encoded value to avoid jsf engin confusion  
		}
		catch (UnsupportedEncodingException e) {
			System.out.println("In visit urlEncode exception.  parameter name="+param+" \n"+e.getMessage());
			System.out.println(e.getStackTrace());
			return param;
		}
	}

	private static String urlDecode(String param) {
    	if (param==null || param.equals(""))
    		return null;
		try {
			return URLDecoder.decode(param, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e) {
			System.out.println("In visit urlDecode exception.  parameter name="+param+" \n"+e.getMessage());
			System.out.println(e.getStackTrace());
			return param;
		}
	}
}
