package gmerg.beans;

import gmerg.assemblers.ISHBrowseAssembler;
import gmerg.assemblers.ISHBrowseSubmissionNonRenalAssembler;
import gmerg.entities.Globals;
import gmerg.entities.Globals.PredefinedFilters;
import gmerg.utils.Utility;
import gmerg.utils.table.FilterItem;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableFilter;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.TableUtil;

/**
 * Managed Bean for display of different ISH submissions based on a Generic tables display. Because there 
 * might be multiple ISH browse tables at the same time it extends from MultipleInstanceBean.
 * 
 * @author Mehran Sharghi
 *
 *
*/

public class ISHBrowseBean extends MultipleInstanceBean {
    private boolean debug = false;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public void initInstance (String browseId) {
	    if (debug)
      		System.out.println("initInstance ISHBrowseBean; browseId="+browseId);
		
		if (TableUtil.isTableViewInSession()) 
			return;

        if (browseId != null) {
			if ("browseAll".equals(browseId)) 
				TableUtil.saveTableView(populateBrowsAllTableView("browseAll"), getDistinguishingParam(), browseId);
			
			if ("noneRenal".equals(browseId))  
				TableUtil.saveTableView(populateNoneRenalTableView("noneRenal"), getDistinguishingParam(), browseId);
		}
		else  
			System.out.println("Serious warnning! in ISHBrowseBean initInstance: browseId is null");	
	}	

    // ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getDistinguishingParam() {
		return "browseId";
	}
	
	/***************************************************************************************************************************************
	/* BrwseAll																							
	/***************************************************************************************************************************************/
	/********************************************************************************
	 * populates GenericTableView for data table of a BrowseAll page 
	 *
	 */
	private GenericTableView populateBrowsAllTableView(String viewName) {
		OffMemoryTableAssembler assembler = new ISHBrowseAssembler();
	    GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(getDefaultIshFilter());
		GenericTableView tableView = getDefaultIshBrowseTableView(viewName, table);
//		tableView.setNavigationPanelMessage("Totals: ISH(<b>238</b>) &nbsp&nbsp&nbsp Microarray(<b>120</b>)");
		return  tableView;
	}

	/***************************************************************************************************************************************
	/* None Renal																							
	/***************************************************************************************************************************************
	/********************************************************************************
	 * populates GenericTableView for data table of a NoneRenal page 
	 *
	 */
	private GenericTableView populateNoneRenalTableView(String viewName) {
		OffMemoryTableAssembler noneRenalAssembler = new ISHBrowseSubmissionNonRenalAssembler();
	    GenericTable table = noneRenalAssembler.createTable();
		table.getAssembler().setFilter(getDefaultIshFilter());
		GenericTableView tableView = getDefaultIshBrowseTableView(viewName, table);
		return  tableView;
	}

	
	/***************************************************************************************************************************************
	/* Common Methods																							
	/***************************************************************************************************************************************
	/********************************************************************************

	/********************************************************************************
	 * Returns a default GenericTableView for ISH browse table 
	 *
	 */
	public static GenericTableView getDefaultIshBrowseTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, 650, table);
//		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		/*tableView.addCollection(0, 0);
		tableView.addCollection(1, 1);*/
		tableView.addCollection(0, 1);//'selected' id from choice menu, col no to take value from - Entries
		tableView.addCollection(1, 0);//genes
		tableView.setDisplayTotals(true);
//		tableView.setDisplayTotals(false);
		tableView.setDefaultColVisible(new boolean[]{true, true, true, false, false, true, true, false, false, true, false, false, false, true});
		
		return tableView;
	}

	public static GenericTableFilter getDefaultIshFilter() { //this is compatible with AdvancedSearchQuery version
		GenericTableFilter filter = new GenericTableFilter();
//		filter.addFilter(new FilterItem(0));
		filter.addFilter(0, Globals.getPredefinedFilter(PredefinedFilters.GENE));
		if(Utility.getProject().equalsIgnoreCase("gudmap")) 
			filter.addFilter(2, Globals.getPredefinedFilter(PredefinedFilters.LAB));
		filter.addFilter(3, Globals.getPredefinedFilter(PredefinedFilters.DATE));
		filter.addFilter(4, Globals.getPredefinedFilter(PredefinedFilters.ASSAY));
//		filter.addFilter(new FilterItem(5));
		filter.addFilter(5, Globals.getPredefinedFilter(PredefinedFilters.PROBE_NAME));
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.THEILER_STAGE));
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.HUMAN_STAGE));
		filter.addFilter(8, Globals.getPredefinedFilter(PredefinedFilters.SEX));
//		filter.addFilter(11, Globals.getPredefinedFilter(PredefinedFilters.EXPRESSION));
		filter.addFilter(12, Globals.getPredefinedFilter(PredefinedFilters.SPECIMEN));
		
		return filter;
	}
}
