package gmerg.beans;

import java.util.*;
import gmerg.assemblers.EntriesCollectionBrowseAssembler;
import gmerg.entities.Globals;
import gmerg.model.EntriesCollectionBrowseHelper;
import gmerg.utils.CookieOperations;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;


//***************************************************
//****************  NOT USED ANY MORE ***************
//***************************************************

/**
 * Managed Bean for Collection based on a Generic tables display.
 * 
 * @author Mehran Sharghi
 *
 *
*/

public class CollectionBean {

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionBean() {
//    	System.out.println("in collection bean constructor");    	

    	if (TableUtil.isTableViewInSession()) 
			return;
        
    	String pageId = FacesUtil.getFacesRequestParamValue("collectionPage");
    	
        if (pageId != null) {
			if ("collection".equals(pageId))  
				TableUtil.saveTableView(populateCollectionTableView("collection"));
			
			if ("collectionOperation".equals(pageId)) {
				TableUtil.saveTableView(populateCollectionOperationTableView("collectionOperation"));
				TableUtil.saveTableView(populateCollectionTableView("collection"));
			}
		}
		else  
			System.out.println("Serious warnning! in CollectionBean Constructor: pageId is null");	
	}	

	// ********************************************************************************
	// Getters & Setters
	// ********************************************************************************
	public boolean getDumy() {	 
		return false;
	}
	
	public boolean getSetViewNameCollectionOperation() { // Dumy get method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 
		TableUtil.setViewName("collectionOperation"); 
		return false;
	}
	
	public boolean getSetViewNameCollection() {	// Dumy get method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2
		TableUtil.setViewName("collection");
		return false;
	}
	
	
	/***************************************************************************************************************************************
	/***************************************************************************************************************************************
	/* Collection																								
	/***************************************************************************************************************************************
	/***************************************************************************************************************************************/
	private GenericTableView populateCollectionTableView(String viewName) {
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(getCollectionIds(), 0);	// Entry Ids
		GenericTableView tableView = helper.getCollectionTableView(viewName);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(2);
		tableView.addCollection(0, 0);
		tableView.setDisplayTotals(true);
		tableView.setHeightUnlimittedFlexible();
                
		if(Utility.getProject().equals("GUDMAP"))
			tableView.setColVisible(new boolean[]{ true, true, true, false, false, false,  true, false, true, false, false, false, true,
													true,  false, true, true});
		else 
			tableView.setColVisible(new boolean[]{ true, true, true, false,  true, false, false, true, false, true, true, true, true});

		tableView.setDynamicColumnsLimits(5, 9);
	    
		tableView.setNoDataMessage("Your Collection is Empty.");
		return tableView;
	}
	
	public static ArrayList<String> getCollectionIds () {
		String operation = FacesUtil.getFacesRequestParamValue("operation");
		
		if (operation == null) 
			return  getCollectionIds("oldSelections");

		if ("add".equals(operation)) 
			return  getCollectionIds("both");
		
		if ("replace".equals(operation)) 
			return getCollectionIds("newSelections");
		
		if ("view".equals(operation)) 
			return getCollectionIds("oldSelections");

		if ("del".equals(operation)) 
			return getCollectionIds("excludeSelections");
		return null;
	}
	
	/***************************************************************************************************************************************
	/***************************************************************************************************************************************
	/* Collection operation																								
	/***************************************************************************************************************************************
	/***************************************************************************************************************************************/
	private GenericTableView populateCollectionOperationTableView(String viewName) {
	    // Create a GenericTableView object for collection operation data
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(getCollectionOperationIds(), 0);	// Entry Ids
		GenericTableView tableView = helper.getCollectionTableView(viewName);
		
		tableView.setHeightUnlimittedFixed();
		tableView.setDisplayTotals(true);
                
		if(Utility.getProject().equals("GUDMAP"))
			tableView.setColVisible(new boolean[]{ true, true, true, false, false, false,  true, false, true, false, false, false, true,
													true,  false, true, true});
		else 
			tableView.setColVisible(new boolean[]{ true, true, true, false,  true, false, false, true, false, true, true, true, true});

		tableView.setDynamicColumnsLimits(5, 9);
		tableView.setNoDataMessage("Collection operation result is empty.");
		
		return tableView;
	}
		
	// Retrieves collection operation results ids from session
	private ArrayList<String> getCollectionOperationIds() {
		String ids = (String) FacesUtil.getSessionValue("collectionOperationResultIds");
//        System.out.println("SSSSSSSSSSSSSS populate Collection operation table :   collectionOperationResultIds======"+ids)	;
		String[] newIds = null;

		if (ids != null && !"".equals(ids) ) 
	       	newIds = ids.split("/");

	    if (newIds == null)
	    	return null;
		
		ArrayList<String> collectionOpIds = new ArrayList<String>();
		for (int i=0; i<newIds.length; i++)
			collectionOpIds.add(newIds[i]);
		
		return collectionOpIds;
	}
	
	/***************************************************************************************************************************************
	/* Common Methods																							
	/***************************************************************************************************************************************
	/********************************************************************************
	 *
	 */
	private static ArrayList<String> getCollectionIds (String include) {
		// get ids in from the cookie
		int n1 = 0;
	    String[] cookieIds = null;
		if ("oldSelections".equals(include) || "both".equals(include) || "excludeSelections".equals(include)) {   
		    String subIds = CookieOperations.getCookieValue("submissionID");
//			System.out.println("retrieved coookies========"+subIds+"##");
		    
		    if (subIds != null && !"".equals(subIds) ) { 
		    	cookieIds = subIds.split("/");
		    	n1 = cookieIds.length;
		    }
		}
		
		// get newly selected ids from request map (these will show up in the cookie for the next request
		int n2 = 0;
		String[] newIds = null;
		if ("newSelections".equals(include) || "both".equals(include) || "excludeSelections".equals(include)) {   
			
	        String selectionIds = FacesUtil.getFacesRequestParamValue("selectionIds");
			if (selectionIds != null && !"".equals(selectionIds) ) {
	        	newIds = selectionIds.split("/");
	        	n2 = newIds.length;
	        }
		}

		ArrayList<String> collectionIds = new ArrayList<String>();
		
		for (int i=0; i<n1; i++) {
			if ("excludeSelections".equals(include)) {
				if (!found(cookieIds[i], newIds) )
					collectionIds.add(cookieIds[i]);
			}
			else
				collectionIds.add(cookieIds[i]);
			
//			System.out.println("coookies ["+i+"]========"+cookieIds[i])	;
		}
					
		if ("excludeSelections".equals(include))
			return collectionIds;
		for (int i=0; i<n2; i++) {			
			if ("both".equals(include)) {
				if (!found(newIds[i], cookieIds) )
					collectionIds.add(newIds[i]);
			}
			else 
				collectionIds.add(newIds[i]);
			
			System.out.println("neeewIds ["+i+"]========"+newIds[i]);
		}

		return collectionIds;
	}
	
	private static boolean found(String s, String[] a) {
		if (s == null || a == null)
			return false;
		for (int i = 0; i < a.length; i++)
			if (s.equals(a[i]))
				return true;
		return false;
	}
	
	
}
