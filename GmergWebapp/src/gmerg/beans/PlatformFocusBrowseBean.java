package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.PlatformBrowseAssembler;
import gmerg.utils.table.*;
import gmerg.utils.Visit;

public class PlatformFocusBrowseBean {
    private boolean debug = false;

	private String organ;
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public PlatformFocusBrowseBean() {
	if (debug)
	    System.out.println("PlatformFocusBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		String viewName = "platformFocusBrowse";
		if (TableUtil.isTableViewInSession())
			return;
		TableUtil.saveTableView(populatePlatformFocusBrowseTableView(viewName));
	}
	
	private GenericTableView populatePlatformFocusBrowseTableView(String viewName) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("organ", organ);
		PlatformBrowseAssembler assembler = new PlatformBrowseAssembler(queryParams); 
		GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultPlatformBrowseTableView(viewName, table);
		return  tableView;
	}

	public static GenericTableView getDefaultPlatformBrowseTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setDisplayTotals(false);
		tableView.setColAlignment(1, 0);	// set Name column to left alignment

		return  tableView;
	}

	public String getOrganTitle() {
		if (organ==null || "".equals(organ))
			return "";
		
		return " in <em>" + DatabaseHomepageBean.getOrganName(organ) + "</em>"; 
	}
	
}
