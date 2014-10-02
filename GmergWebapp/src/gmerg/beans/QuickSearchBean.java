package gmerg.beans;

import java.io.Serializable;
import java.util.HashMap;

import gmerg.assemblers.QuickSearchAssembler;
import gmerg.utils.table.*;
import gmerg.utils.Utility;
import gmerg.utils.Visit;

	public class QuickSearchBean implements Serializable {
    private boolean debug = false;

	private String input;
	
	public QuickSearchBean() {
	    	if (debug)
	    System.out.println("QuickSearchBean.constructor");

		input = Visit.getRequestParam("input");
        String viewName = "focusQuick";
		if (TableUtil.isTableViewInSession())
			return;
       	TableUtil.saveTableView(populateSearchResultBrowseTableView(viewName));
	}
	
	public boolean getDumy() {
		return false;
	}
	
	public String getInput() {		
        return input;
	}
	
	private GenericTableView populateSearchResultBrowseTableView(String viewName) {
		
		String[] inputs = null;
		if(input != null && !input.trim().equals("")){
			inputs = new String [] {input};
		}
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("input", inputs);
		queryParams.put("query", "All");
		OffMemoryTableAssembler assembler = new QuickSearchAssembler(queryParams);
		GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultSearchResultBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);
		
		return  tableView;
	}	

	public static GenericTableView getDefaultSearchResultBrowseTableView (String viewName, GenericTable table) {		
        GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		tableView.addCollection(0, 1);
		tableView.addCollection(1, 0);
		tableView.setColVisible(new boolean[]{true, true, true, true, true, true, true, false, true, false, false, true});
		// Bernie 09/03/2012 - (Mantis 328) - mod to make sex column visible by default
//		tableView.setDynamicColumnsLimits(5, 10);
		int[] groupedNumRows = table.getNumRowsInGroups();
		if (groupedNumRows != null) {
			String ishNo = String.valueOf(groupedNumRows[0]);
			String arrayNo = String.valueOf(groupedNumRows[1]);
			//sequence data not included
			if(groupedNumRows.length<3)
				tableView.setNavigationPanelMessage("Totals: In Situ(<b>" + ishNo + "</b>) &nbsp&nbsp&nbsp Microarray(<b>" + arrayNo + "</b>)");
			else {// include the Sequence (ngd) data
			String ngdNo = String.valueOf(groupedNumRows[2]);
			tableView.setNavigationPanelMessage("Totals: In Situ(<b>" + ishNo + "</b>) &nbsp&nbsp&nbsp Microarray(<b>" + arrayNo + "</b>)  &nbsp&nbsp&nbsp Sequence(<b>" + ngdNo + "</b>)");
			}
		}
		if((tableView == null || tableView.getTable() == null || tableView.getTable().getNumRows() == 0) && Utility.getProject().equalsIgnoreCase("GUDMAP")) {
        	tableView.setNoDataMessage(Utility.getNoDataMessageForQueryPage("", ""));
        }
//    	tableView.setColVisible(2, false);	// source
		tableView.setColVisible(3, false);	// submission date
		tableView.setColVisible(4, false);	// assay type		
//		tableView.setColVisible(5, false);	// probe name		
		tableView.setColVisible(7, false);	// age
		tableView.setColVisible(8, false);	// sex
		tableView.setColVisible(9, true);	// genotype
		tableView.setColVisible(11, false);	// ish expression
		tableView.setColVisible(13, false);	// specimen type
		
//    	tableView.setColHidden(2, true);	// source
//		tableView.setColHidden(3, true);	// submission date
//		tableView.setColHidden(6, true);	// age
//		tableView.setColHidden(5, true);	// probe name
		tableView.setColHidden(12, true);	// microarray expression

		return  tableView;
	}	

}
