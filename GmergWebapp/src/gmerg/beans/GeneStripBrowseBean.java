package gmerg.beans;

import gmerg.entities.Globals;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.table.*;

public class GeneStripBrowseBean {
    private boolean debug = false;

	public GeneStripBrowseBean() {
	    if (debug)
		System.out.println("GeneStripBrowseBean.constructor");

		String geneSymbols = Visit.getRequestParam("gene");
	    String viewName = "geneStrip_" + geneSymbols;
		if (TableUtil.isTableViewInSession())
			return;
       	TableUtil.saveTableView(populateGeneStripTableView(viewName, geneSymbols, true));		
	}

	public boolean getDummy() {
		return false;
	}
	
	// ********************************************************************************
	// Static methods
	// ********************************************************************************
	public static GenericTableView populateGeneStripTableView(String viewName, String geneSymbols, boolean displaySymbolAsLink) {
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(Utility.toArrayList(geneSymbols, ";"), 1); // corresponds to gene symbols collection
		OffMemoryTableAssembler assembler = helper.getCollectionBrowseAssembler();
		if (!displaySymbolAsLink)
			assembler.setParam("geneSymbolNoLink", "true");
		GenericTable table = assembler.createTable(0);	// set default sort column 
		GenericTableView tableView = getDefaultGeneStripTableView(viewName, table);
		tableView.setRowsSelectable();
		tableView.setDisplayTotals(false);
		
		return tableView;
	}
	
	public static GenericTableView getDefaultGeneStripTableView (String viewName, GenericTable table) {		
        GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		tableView.addCollection(1, 0);
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) 
        	tableView.setNoDataMessage(Utility.getNoDataMessageForQueryPage("", ""));
		tableView.setColMaxWidth(1, 30, true);	//limit sysnonym colum width
		return  tableView;
	}
		
}
