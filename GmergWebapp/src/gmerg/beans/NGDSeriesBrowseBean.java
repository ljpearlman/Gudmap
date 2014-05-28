package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.NGDSeriesBrowseAssembler;
import gmerg.utils.Visit;
import gmerg.utils.table.*;
import gmerg.utils.Utility;

/**
 * @author Mehran Sharghi
 *
*/

public class NGDSeriesBrowseBean  {
    private boolean debug = true;

	private String organ;
	private String protocol;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public NGDSeriesBrowseBean() {
	if (debug)
	    System.out.println("NGDSeriesBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		protocol = Visit.getRequestParam("protocol");
		String viewName = "NGDSeriesBrowse";
        if (TableUtil.isTableViewInSession())
			return;
        
		GenericTableView tableView = populateSeriesBrowseTableView(viewName);
		TableUtil.saveTableView(tableView);
	}
	
	/********************************************************************************
	 * populates GenericTableView for data table of a series browse  page 
	 *
	 */
	private GenericTableView populateSeriesBrowseTableView(String viewName) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("organ", organ);
		queryParams.put("protocol", protocol);
		NGDSeriesBrowseAssembler assembler = new NGDSeriesBrowseAssembler(queryParams);
	    GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultSeriesBrowseTableView(viewName, table);
		return  tableView;
	}
	
	public static GenericTableView getDefaultSeriesBrowseTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setDisplayTotals(false);
		tableView.setSelectable(0);
		tableView.setColAlignment(0, 0);	// set Title column to left alignment
		tableView.setColAlignment(5, 0);	// set Component(s) Sampled column to left alignment
 		return  tableView;
	}

	public String getTitle() {
		
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) {
			if (organ==null || "".equals(organ))
				return "Gudmap Next Gen Series submissions";
		
			return "Gudmap Next Gen Series submissions in <em>" + DatabaseHomepageBean.getOrganName(organ) + "</em>";
		}
		return "";
	}
	
}
