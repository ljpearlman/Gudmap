package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.SeriesBrowseAssembler;
import gmerg.utils.Visit;
import gmerg.utils.table.*;
import gmerg.utils.Utility;

/**
 * @author Mehran Sharghi
 *
*/

public class SeriesBrowseBean  {

	private String organ;
	private String platform;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public SeriesBrowseBean() {
		organ = Visit.getRequestParam("focusedOrgan");
		platform = Visit.getRequestParam("platform");
		String viewName = "seriesBrowse";
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
		queryParams.put("platform", platform);
		SeriesBrowseAssembler assembler = new SeriesBrowseAssembler(queryParams);
	    GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultSeriesBrowseTableView(viewName, table);
		return  tableView;
	}
	
	public static GenericTableView getDefaultSeriesBrowseTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setDisplayTotals(false);
		tableView.setSelectable(0);
		tableView.setColAlignment(0, 0);	// set Title column to left alignment
		tableView.setColAlignment(5, 0);	// set Description column to left alignment
 		return  tableView;
	}

	public String getTitle() {
		
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) {
			if (organ==null || "".equals(organ))
				return "Gudmap microarray Series submissions";
		
			return "Gudmap microarray Series submissions in <em>" + DatabaseHomepageBean.getOrganName(organ) + "</em>";
		}
		else if(Utility.getProject().equalsIgnoreCase("EuReGene")) {
			return "Microarray Experiments";
		}
		else {
			return "";
		}
	}
	
}
