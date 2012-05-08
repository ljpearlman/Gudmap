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

	private GenericTableView tableView = null; // this should be used if using frame based wide tables

	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public ProcessedGenelistsBean() {
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
//	    ProcessedGenelistAssembler assembler = new ProcessedGenelistAssembler(queryParams);
	    InMemoryTableAssembler assembler = new ProcessedGenelistAssembler(queryParams);
	    GenericTable table = assembler.createTable();
	    //Create a GenericTableView object for list of Genelists
//	    GenericTableView tableView = new GenericTableView(viewName, 3, "", 400, 1100, table);
	    GenericTableView tableView = new GenericTableView(viewName, 5, 300, table);
	    tableView.setHeightUnlimittedFlexible();
	    boolean columnWrap[] = {false, false, false, false, true, true, true, true, true, false, true };
	    tableView.setColWrap(columnWrap);
//	    tableView.setSelectable(false);
//		tableView.setDisplayInFrame(true);

	    return tableView;
	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public boolean getDumy() {
		return false;
	}

/*	
	public GenericTableView getTableView() {
		return tableView;
	}

	public void setTableView(GenericTableView tableView) {
		this.tableView = tableView;
	}

	public String getWidth() {
		return String.valueOf(tableView.getWidth());
	}

	public String getTableViewName() {
		return tableView.getName();
	}
*/
	
}
