/**
 * 
 */
package gmerg.utils;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author xingjun
 *
 */
public class FacesUtil {

	public static String getAnyRequestParamValue(String key) {
		String value = getFacesRequestParamValue(key);
		if (value!=null)
			return value;
		return getRequestParamValue(key);
	}
	
	public static String getRequestParamValue(String key) {
		return (String)(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key));	
	}
        
    public static String [] getRequestParamValues(String key){
        return (String[])(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap().get(key));
    }

	public static String getFacesRequestParamValue(String key) {
		Object value = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);	
		if (value==null)
			return null;
		return String.valueOf(value);	
	}
	
	public static Object getFacesRequestParamObject(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);	
	}
	
	public static void setFacesRequestParamValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, value);	
	}

	public static String removeFacesRequestParam(String key) {
		Object value = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(key);	
		if (value==null)
			return null;
		return String.valueOf(value);	
	}
	
	public static Object getSessionValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
	}
	
	public static void setSessionValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
	}
	
	public static void removeSessionValue(String key) {
		Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (sessionMap.get(key) != null)
			sessionMap.remove(key);
	}		
	
	public static Object getApplicationValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(key);
	}
    
	public static String getRequestHeader(String key) {
		return (String)FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get(key);
	}
	
	public static ExternalContext getContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public static Map<String,String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();	
	}
	
}
