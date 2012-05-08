package gmerg.beans;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.util.*;

/**
 * Managed Bean abstract class for multiple instance beans
 * 
 * @author Mehran Sharghi
 * 
 */

public abstract class MultipleInstanceBean {

	//Abstract method to initialize the newly instantiated bean
	public abstract void initInstance(String id);
	public abstract String getDistinguishingParam();

   	//-------------------------------------------------------------------------------------
	static synchronized public Object resolveVariable(String name, String id, Object newInstance) {
		
    	String newName = name + "_" + id; 

		FacesContext context = FacesContext.getCurrentInstance();
	    ExternalContext externalContext = context.getExternalContext();
	    Map<String, Object> requestMap = externalContext.getRequestMap();
    	Map<String, Object> sessionMap = externalContext.getSessionMap();
    	Map<String, Object> applicationMap = externalContext.getApplicationMap();
		
    	Object value = null; 
    	// See if the bean instance already esists.
        if ((value=requestMap.get(newName))==null && (value=sessionMap.get(newName))==null && (value=applicationMap.get(newName))==null )  {
            // We could not find the bean instance in scope so create the bean using the standard variable resolver.
            // Now check if the bean implements that page interface. If it is a page then we want to rename the key to access this bean
            if (newInstance instanceof MultipleInstanceBean) { 
//            	System.out.println("==========> IN Resolver: couldn't find "+newName+" creating one");        	
                // Initialize the bean using the id 
                ((MultipleInstanceBean)newInstance).initInstance(id); 
                // Remove the created instance with reference "name" and put it again with reference "newName" 
                if (null != (value = requestMap.get(name))) { 
                  requestMap.remove(name); 
                  requestMap.put(newName, value); 
//                  System.out.println("==========> IN Resolver: remove from request "+name+"--- put in request" + newName);        	
                } 
                else if (null != (value = sessionMap.get(name))) { 
                	  sessionMap.remove(name); 
                	  sessionMap.put(newName, value); 
//                	  System.out.println("==========> IN Resolver: remove from session "+name+"--- put in session" + newName);        	
                } 
                else if (null != (value = applicationMap.get(name))) { 
              	  	applicationMap.remove(name); 
              	  	applicationMap.put(newName, value);
//                	System.out.println("==========> IN Resolver: remove from app "+name+"--- put in app" + newName);        	
              	} 
            }
            return newInstance;
        }
        
        return value; 
    }  	
}
