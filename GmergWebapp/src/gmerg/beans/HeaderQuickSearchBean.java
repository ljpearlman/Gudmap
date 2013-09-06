/**
 * @author xingjun - 20/07/2010
 */
package gmerg.beans;

import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import gmerg.entities.Globals;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;
import gmerg.utils.Utility;

/**
 * @author xingjun
 *
 */
public class HeaderQuickSearchBean  implements Serializable {
    private boolean debug = false;

	private String quickSearchInput;
	private String quickSearchType;
	private String dis_text;

	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public HeaderQuickSearchBean() {
	    if (debug)
		System.out.println("HeaderQuickSearchBean.constructor");

		quickSearchInput = "";
		quickSearchType = "Gene"; 
	}

	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	public String quickSearch() {
		if (quickSearchInput == null || quickSearchInput.trim().equals("")) {
			return "QuickNoResult";
		}
		else {
		    if (null != quickSearchType && quickSearchType.equals("Gene"))
			return "QuickGeneQuery";
		}
		return "QuickAdvancedQuery";
	}
	
	public String getQuickSearchParams() {
		HashMap<String, String> params = new HashMap<String, String>();
		String urlParams = null;

		if (null != quickSearchType && quickSearchType.equals("Accession ID"))
		    params.put("input", Utility.checkAccessionInput(quickSearchInput));
		else
		    params.put("input", quickSearchInput);
		params.put("query", quickSearchType);
		urlParams = Visit.packParams(params);
		if (urlParams==null)
			return "";

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
    	quickSearchType = (String)value.getNewValue();

    	return quickSearchType;
    }

    public String getAppUrl() {
	return gmerg.utils.Utility.appUrl;
    }
}
