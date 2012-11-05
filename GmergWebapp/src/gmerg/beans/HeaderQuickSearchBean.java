/**
 * @author xingjun - 20/07/2010
 */
package gmerg.beans;

import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import java.util.HashMap;

import gmerg.entities.Globals;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

/**
 * @author xingjun
 *
 */
public class HeaderQuickSearchBean {
    private boolean debug = false;

	private String quickSearchInput;
	private String query;
	private String quickSearchType;
	private String dis_text;

	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public HeaderQuickSearchBean() {
	    if (debug)
		System.out.println("HeaderQuickSearchBean.constructor");

		query = FacesUtil.getRequestParamValue("query"); 		// this is provided by action f:param
		quickSearchInput = "";
		quickSearchType = "Gene"; // default search option
	}

	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	// modified by xingjun - 11/01/2011 - added trim method to handle multiple whitespaces input from the user
	public String quickSearch() {
//		System.out.println("HeaderQuickSearchBean:quickSearch:quickSearchInput: " + quickSearchInput);
//		System.out.println("HeaderQuickSearchBean:quickSearch:query: " + query);
		if (quickSearchInput == null || quickSearchInput.trim().equals("")) {
//			System.out.println("return QuickNoResult");
			return "QuickNoResult";
		}
		else {
			if (query.equals("Gene")) {
//				System.out.println("return QuickGeneQuery");
	   			return "QuickGeneQuery";
			} 
		}
//		System.out.println("return QuickAdvancedQuery");
		return "QuickAdvancedQuery";
	}
	
	public String getQuickSearchParams() {
//		System.out.println("HeaderQuickSearchBean:getQuickSearchParams#########");
		HashMap<String, String> params = new HashMap<String, String>();
		String urlParams = null;
		params.put("input", quickSearchInput);
		params.put("query", query);
		urlParams = Visit.packParams(params);
		if (urlParams==null)
			return "";
//		System.out.println("HeaderQuickSearchBean:getQuickSearchParams:urlParams: " + urlParams);
		return "?"+urlParams;
	}
	
	public String getQuickSearchInput() {
		return quickSearchInput;
	}

	public void setQuickSearchInput(String quickSearchInput) {
		this.quickSearchInput = quickSearchInput;
	}

	public SelectItem[] getQuickSearchTypeItems() {
		return Globals.getQuickSearchTypeItems();
	}
	
	public String getQuickSearchType() {
		return this.quickSearchType;
	}
	
	public void setQuickSearchType(String quickSearchType) {
		this.quickSearchType = quickSearchType;
	}
	
    public String changeSearchType(ValueChangeEvent value) {
    	String searchTypeValue = (String)value.getNewValue();
    	this.quickSearchInput = searchTypeValue;
    	return null;
    }
}
