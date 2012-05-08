package gmerg.beans;


import java.util.HashMap;

import gmerg.assemblers.AllComponentsGenelistAssembler;
import gmerg.utils.table.*;


/**
 * Managed Bean to diplay list of processed genelists 
 * 
 * @author Mehran Sharghi
 * 
 */

public class AllComponentsGenelistsBean {

	private GenericTableView tableView = null; // this should be used if using frame based wide tables

	private String cdtFile;
	private String gtrFile;
	private String genelistId;
	private boolean displayTreeView;
	
	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public AllComponentsGenelistsBean() {
		String viewName = "processedGenelists";
		
        if (TableUtil.isTableViewInSession()) {
		 	tableView = TableUtil.getTableViewFromSession(viewName);
			return;
        }
        
		tableView = populatelistOfGeneListsTableView(viewName);
		TableUtil.saveTableView(tableView);
	}
	
	/********************************************************************************
	 * populates GenericTableView for list of processed genelists
	 *
	 */
	public GenericTableView populatelistOfGeneListsTableView(String viewName) 
	{
	    InMemoryTableAssembler assembler = new AllComponentsGenelistAssembler();
	    GenericTable table = assembler.createTable();
	    GenericTableView tableView = new GenericTableView(viewName, 20, 300, table);
	    tableView.setHeightUnlimittedFlexible();

	    return tableView;
	}


	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String launchTreeView() {
		HashMap<String, String> params = TableUtil.getClickedLinkParams();
		genelistId = params.get("genelistId");
		cdtFile = params.get("cdtFile");
		gtrFile = params.get("gtrFile");
		if ((genelistId!=null && !genelistId.equals("")) || (cdtFile!=null && !cdtFile.equals("")))
			displayTreeView = true;
		return null;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public boolean getDumy() {
		return false;
	}

	public boolean isDisplayTreeView() {
		return displayTreeView;
	}

	public void setDisplayTreeView(boolean displayTreeView) {
		this.displayTreeView = displayTreeView;
	}

	public String getCdtFile() {
		return cdtFile;
	}

	public String getGtrFile() {
		return gtrFile;
	}

	public String getGenelistId() {
		return genelistId;
	}
}
