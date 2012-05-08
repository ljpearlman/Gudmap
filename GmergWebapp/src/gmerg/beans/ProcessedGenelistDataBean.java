
package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.ProcessedGenelistDataAssembler;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;

/**
 * Managed Bean to display Processed Genelist data table
 * 
 * @author Mehran Sharghi
 * 
*/
public class ProcessedGenelistDataBean extends MultipleInstanceBean{

	private String genelistName = null;
    private GenericTableView tableView = null; // this should be used if using frame based wide tables

	// ********************************************************************************
	// Constructors & initializers
	// ********************************************************************************
	public void initInstance (String genelistId){
	System.out.println("in init instance ProcessedGenelistDataBean id="+genelistId);    

		//get genelist name from request 
		genelistName = FacesUtil.getRequestParamValue("genelistName");
//		System.out.println("genelistName===="+genelistName);	    
		
		String viewName = "genelistData" + genelistId;
		
//		if (TableBean.isTableViewInSession(genelistId)) {
		if (TableUtil.isTableViewInSession()) {
		 	tableView = TableUtil.getTableViewFromSession(viewName);
			return;
		}
		
        if (genelistId != null) {
        	tableView = populateGenelistTableView(viewName, genelistId);
			TableUtil.saveTableView(tableView, getDistinguishingParam(), genelistId);
        }
		else  
			System.out.println("Serious warnning! in ProcessedGenelistDataBean initInstance: genelistId is null");	
	    
	}

	public String getDistinguishingParam() {
		return "genelistId";
	}

	/********************************************************************************
	 * populates GenericTableView for data table of a genelist 
	 *
	 */
	public GenericTableView populateGenelistTableView(String viewName, String genelistId)
	{
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("genelistId", new Integer(genelistId));
	    ProcessedGenelistDataAssembler assembler = new ProcessedGenelistDataAssembler(queryParams);
	    GenericTable table = assembler.createTable();
	    // Create a GenericTableView object for genelist data
//		GenericTableView tableView = new GenericTableView(viewName, 20, "", 500, 1500, table);
		GenericTableView tableView = new GenericTableView(viewName, 20, 570, table);
		tableView.setHeightUnlimittedFlexible();
	    tableView.setColWrap(false);
//		tableView.setDisplayInFrame(true);
		
		tableView.setColVisible(5, false);
		tableView.setColVisible(7, false);
		
		tableView.setColMaxWidth(9, 15, true);
		tableView.setColMaxWidth(14, 20, true);

		return  tableView;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
/*	
	public GenericTableView getTableView() {
		return tableView;
	}

	public void setTableView(GenericTableView tableView) {
		this.tableView = tableView;
	}   
*/
	
	public String getGenelistName() {
		return genelistName;
	}

	public void setGenelistName(String genelistName) {
		this.genelistName = genelistName;
	}
	
}
