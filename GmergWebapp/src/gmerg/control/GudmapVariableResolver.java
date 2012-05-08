package gmerg.control;

import javax.faces.el.VariableResolver;
import javax.faces.context.FacesContext;
import gmerg.beans.*;
import gmerg.utils.FacesUtil;

/**
 * Variable resolver has been decorated to address requirement for multiple instance beans
 *  
 * @author Mehran Sharghi
 * 
 */


public class GudmapVariableResolver extends VariableResolver 
{
	private VariableResolver original;
	
	public GudmapVariableResolver(VariableResolver original){
		this.original = original;
	}
	
	public VariableResolver getOriginalVariableResolver() {
		return original;
	}

	static class MultipleInstanceInfo {
		String beanName;
		Class beanClass;
		public MultipleInstanceInfo(String name, Class c) {
			beanName = name;
			beanClass = c;
		}
	};
	
//	An entry should be added to this array for each new MultipleInstanceBean
	static MultipleInstanceInfo multipleInstanceInfoList[]= { new MultipleInstanceInfo("TableBean", TableBean.class),
															  new MultipleInstanceInfo("ISHBrowseBean", ISHBrowseBean.class),
															  new MultipleInstanceInfo("ProcessedGenelistDataBean", ProcessedGenelistDataBean.class)};

//	static Class[] multipleInstanceClasses = {TableBean.class, ISHBrowseBean.class, ProcessedGenelistDataBean.class}; // An item should be added to this array for each new MultipleInstanceBean 
	
	 synchronized public Object resolveVariable(FacesContext context, String name){

/*	This part is currently not in use	 
		// If name is refering to a multiple instance bean alias name (aliasbean) 
		for (int i=0; i<multipleInstanceInfoList.length; i++) 
			if (name.indexOf(multipleInstanceInfoList[i].beanName+"_") == 0) {
				String id = name.substring(multipleInstanceInfoList[i].beanName.length()+1);
//				System.out.println("In VariableResolver: resolving Alias Name: "+ name+ "	   id="+id);
				Object var = original.resolveVariable(context, multipleInstanceInfoList[i].beanName);
				return MultipleInstanceBean.resolveVariable(multipleInstanceInfoList[i].beanName, id, var);
			}
*/
		// If name is a normal multiple instance bean 
		Object var = original.resolveVariable(context, name);
		
		if (var == null)
			return null;
		for (int i=0; i<multipleInstanceInfoList.length; i++) {
			if (var.getClass() == multipleInstanceInfoList[i].beanClass) {
/*				
				String distinguishingId = null;
				try{
					distinguishingId = (String)multipleInstanceInfoList[i].beanClass.getMethod("getDistinguishingParam", null).invoke(null,null);
					System.out.println("dist id==================="+distinguishingId);
				} 
				catch (Exception e) {
					System.out.println("Error in calling get distingishParam");
					e.printStackTrace();
				}
*/				
				String distinguishingId = ((MultipleInstanceBean)var).getDistinguishingParam();
				
				boolean invokeAppliactionPhase = GudmapInvokeApplicationPhaseTracker.isApplicationPhase();
				String id;
				if (invokeAppliactionPhase)
					id = FacesUtil.getRequestParamValue(distinguishingId);  // First look in request parameters
				else
					id = FacesUtil.getFacesRequestParamValue(distinguishingId); // First look in request map for distinguishing id
				
//				System.out.println("**** in resolver distinguishingId="+distinguishingId+"   faces="+id+"	 rquest="+FacesUtil.getRequestParamValue(distinguishingId));		
				if ((id != null) && (id.length() > 0)) {
//					System.out.println("in resolver: found id in request map   id="+id +" name="+name);
					return MultipleInstanceBean.resolveVariable(name, id, var);
				}
				
				if (invokeAppliactionPhase)
					id = FacesUtil.getFacesRequestParamValue(distinguishingId); // If distinguishing id not found in request parameters then look for it in request map
				else
					id = FacesUtil.getRequestParamValue(distinguishingId);  // If distinguishing id not found in request map then look for it in request parameters
	   			
				if ((id != null) && (id.length() > 0)) {
//					System.out.println("in resolver: found id in request parameters   id="+id +" name="+name);
					return MultipleInstanceBean.resolveVariable(name, id, var);
				} 
				
//				System.out.println("Warning: in resolver multiple instance withoud qualifying Id for "+ name+ "  class="+var.getClass());
			}
		}

		// Any name that is not resolved as alias or normal multiple instance bean 
		if (var instanceof MultipleInstanceBean) 
			System.out.println("Serious Warning: in resolver multiple instance without qualifying Id for "+ name+ "  class="+var.getClass());
			
		return var; 
	}
	
/*	
	public Object resolveVariable(FacesContext context, String name){

		String[] ids = {"tableViewName","genelistId","browseId"}; 		//For each multiple instance bean add a qualifying id to this array

		Map requestParams = context.getExternalContext().getRequestParameterMap();

		for (int i=0; i<ids.length; i++) {
			String id = (String)requestParams.get(ids[i]);
			
			if ((id != null) && (id.length() > 0)) {
				System.out.println("^^1^^^^^=="+context.getExternalContext().getRequestParameterMap().get("browseId"));
				System.out.println("in resolver "+ids[i] + "=" + id+ "  name="+name);
				return MultipleInstanceBean.resolveVariable(name, id);
			}  	
		}
		
		Object var = original.resolveVariable(context, name);
		if (var instanceof MultipleInstanceBean) 
			System.out.println("Serious Warning: in resolver multiple instance withoud qualifying Id for "+ name);
			
		return var; 
	}
*/	
/*	
	public Object resolveVariable1(FacesContext context, String name){

		ExternalContext externalContext = context.getExternalContext();
		Map requestParams = externalContext.getRequestParameterMap();

		//For each multiple instance bean an entry sholud be added as follows:
		//-------------------------------------------------------------------------------------
		//create instance of TableBean
		String tableViewName = (String)requestParams.get("tableViewName");
		if ((tableViewName != null) && (tableViewName.length() > 0)) {
//				System.out.println("in resolver tableViewName="+ tableViewName+"  name="+name);
			return TableBean.resolveVariable(name, tableViewName);
//			return MultipleInstanceBean.resolveVariable(name, tableViewName);
		}  	

		//-------------------------------------------------------------------------------------
		//create instance of ProcessedGenelistDataBean
		String genelistId = (String)requestParams.get("genelistId");
		if ((genelistId != null) && (genelistId.length() > 0)) { 
//			 	System.out.println("in resolver id="+ genelistId+"  name="+name);
			return ProcessedGenelistDataBean.resolveVariable(name, genelistId);
		}
		
		//-------------------------------------------------------------------------------------
		//create instance of IshBrowseBean
		String browseId = (String)requestParams.get("browseId");
		if ((browseId != null) && (browseId.length() > 0)) {
//				System.out.println("in resolver browseId="+ browseId+"  name="+name);
			return ISHBrowseBean.resolveVariable(name, browseId);
		}  	

		if (name.equals("TableBean")||name.equals("ISHBrowseBean")||name.equals("columnHeader"))
			System.out.println("Serious Warning: in resolver multiple instance withoud qualifying Id for "+ name);
			
		return original.resolveVariable(context, name); 
	}
*/	
}

