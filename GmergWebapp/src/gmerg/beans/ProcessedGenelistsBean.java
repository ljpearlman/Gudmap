package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.ProcessedGenelistAssembler;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;

/**
 * Managed Bean to diplay list of processed genelists 
 * 
 * @author Mehran Sharghi
 * 
 */

public class ProcessedGenelistsBean {
    private boolean debug = false;

	private GenericTableView tableView = null; // this should be used if using frame based wide tables

	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public ProcessedGenelistsBean() {
	if (debug)
	    System.out.println("ProcessedGenelistsBean.constructor");

		String viewName = "processedGenelists";
		
        if (TableUtil.isTableViewInSession()) {
		 	tableView = TableUtil.getTableViewFromSession(viewName);
			return;
        }
        
		String component = FacesUtil.getRequestParamValue("component");
		String lab = FacesUtil.getRequestParamValue("lab");
		tableView = populatelistOfGeneListsTableView(viewName, component, lab);
		TableUtil.saveTableView(tableView);
	}
	
	/********************************************************************************
	 * populates GenericTableView for list of processed genelists
	 *
	 */
	public GenericTableView populatelistOfGeneListsTableView(String viewName, String component, String lab) 
	{
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("component", component);
		queryParams.put("lab", lab);

	    InMemoryTableAssembler assembler = new ProcessedGenelistAssembler(queryParams);
	    GenericTable table = assembler.createTable();
	    //Create a GenericTableView object for list of Genelists
	    GenericTableView tableView = new GenericTableView(viewName, 5, 300, table);
	    boolean columnWrap[] = {false, false, false, false, true, true, true, true, true, false, true };
	    tableView.setColWrap(columnWrap);

	    return tableView;
	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public boolean getDumy() {
		return false;
	}

}
