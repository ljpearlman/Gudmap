package gmerg.control;

import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIViewRoot;

import java.util.Locale;
import java.io.IOException;


/**
 * Managed Bean for Table JSF page
 * 
 * @author Mehran Sharghi
 * 
 */

public class GudmapViewHandler extends ViewHandler
{
	private ViewHandler originalViewHandler;
	
	public GudmapViewHandler(ViewHandler handler) 
	{
		super();
		originalViewHandler = handler;
	}

	public UIViewRoot createView(javax.faces.context.FacesContext context, java.lang.String viewId)
	{
// 		System.out.println("in View Handler ================create view===="+viewId);		
		if(UIComponentTag.isValueReference(viewId)) { 
			ValueBinding vb = context.getApplication().createValueBinding(viewId); 
			viewId = vb.getValue(context).toString();
		}
		
		return originalViewHandler.createView(context, viewId);
	}

	  
	//--------------------------------------------------------------------------------------
	
	public String getActionURL(FacesContext context, String viewId) { 
            
	    String paramString = "";

	    if(viewId.indexOf("#") >= 0) {
	        ValueBinding vb = context.getApplication().createValueBinding(viewId);
	        viewId = vb.getValue(context).toString();
	    }     
	           
	    if(viewId.indexOf("?") >=0) {
	        int paramIndex = viewId.indexOf("?");
	        paramString = viewId.substring(paramIndex);
	    }
//		System.out.println("in View Handler=========get action url ========"+viewId);
            
	    return originalViewHandler.getActionURL(context, viewId)+paramString; 
	}

	public void renderView (FacesContext context, UIViewRoot view) throws IOException
	{
		originalViewHandler.renderView(context, view);
	}
	
	public Locale calculateLocale(javax.faces.context.FacesContext context) {
	   	return originalViewHandler.calculateLocale(context);
	}
	
    public String calculateRenderKitId(javax.faces.context.FacesContext context)
    {
    	return originalViewHandler.calculateRenderKitId(context);
    }

	public String getResourceURL(javax.faces.context.FacesContext context, java.lang.String path){
//		System.out.println("in View Handler ================resource pathcreate view===="+path);		
		return originalViewHandler.getResourceURL(context, path);
	}
	
	public UIViewRoot restoreView(javax.faces.context.FacesContext context, java.lang.String viewId){
//		System.out.println("in View Handler ================restore view===="+viewId);		
		return originalViewHandler.restoreView(context, viewId);
	}
	
	public void writeState(javax.faces.context.FacesContext context) throws java.io.IOException
	{
		originalViewHandler.writeState(context);
	}

}
