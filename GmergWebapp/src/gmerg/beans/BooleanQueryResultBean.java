package gmerg.beans;

import gmerg.assemblers.BooleanQueryAssembler;
import gmerg.assemblers.QuickSearchAssembler;
import gmerg.entities.Globals;
import gmerg.entities.Globals.PredefinedFilters;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.FilterItem;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableFilter;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.TableUtil;

import java.util.HashMap;
import java.util.ArrayList;

public class BooleanQueryResultBean {
    private boolean debug = false;
	private String searchResultOption; 
	
	public BooleanQueryResultBean() {
	    if (debug)
		System.out.println("BooleanQueryResultBean.constructor");

		if (TableUtil.isTableViewInSession())
			return;
		searchResultOption ="entry";
		String input = (String)FacesUtil.getSessionValue("input");
		if(null != input && !input.equals("") && input.indexOf(":")>=0 ) {
			//System.out.println("input not null and searchResultOption: " + searchResultOption);
			String prefix = input.substring(0, input.indexOf(":"));
			if (prefix != null) {
				input = input.substring(input.indexOf(":")+1);
				if (prefix.equalsIgnoreCase("gene") || prefix.equalsIgnoreCase("genes"))
					searchResultOption = "gene";
				if (prefix.equalsIgnoreCase("TF") || prefix.equalsIgnoreCase("transcription factor"))
					searchResultOption = "TF";
			}
        	TableUtil.saveTableView(populateBooleanTableView("booleanResult", input));		
        }  
	}

	private GenericTableView populateBooleanTableView(String viewName, String input) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("input", input);
		OffMemoryTableAssembler assembler;
	    if ("gene".equals(searchResultOption)) {
	    	ArrayList<String> geneSymbols = BooleanQueryAssembler.retrieveGenes(input);
//	    	System.out.println("booleanQueryResultBean@gene numbers: " + geneSymbols.size()+"   genes="+geneSymbols.toString());
			CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(geneSymbols, 1); // corresponds to gene symbols collection
			assembler = helper.getCollectionBrowseAssembler();
	    	
	    }
	    else{ 
	    	// TF type should also be checked
	    	assembler = new BooleanQueryAssembler(queryParams);
	    	//Derek. 110714 filter not working. revert to generic.
	    	assembler.setFilter(getNewFilter(QuickSearchAssembler.getTableviewToSqlColMap()));

			//Bernie 01/02/2012 - Mantis 328 - added method 
		    /*int[] map = {0, 9, 13, 2, 11, 1, 5, 7, 14, 3, 4, 6, 8};
		    assembler.setFilter(getFilter(map));*/
	    }
	    
	    GenericTable table = assembler.createTable();

	    GenericTableView tableView;
	    if ("gene".equals(searchResultOption))
	    	tableView = GeneStripBrowseBean.getDefaultGeneStripTableView(viewName, table);
	    else
	    	tableView = QuickSearchBean.getDefaultSearchResultBrowseTableView(viewName, table);

	    tableView.setDisplayTotals(false);
       	tableView.setNoDataMessage(Utility.getNoDataMessageForQueryPage("", ""));

       	return  tableView;
	}	
	
	
	//Bernie 06/03/2012 - Mantis 328 - added method
	private GenericTableFilter getFilter(int[] colMap) { 
//		System.out.println("FocusGeneBrowseBean:getFilter(in)");
		GenericTableFilter filter = new GenericTableFilter();
		filter.setTableToSqlColMap(colMap);
		filter.addFilter(new FilterItem(0));
		filter.addFilter(2, Globals.getPredefinedFilter(PredefinedFilters.ASSAY));
//		filter.addFilter(new FilterItem(5));// bernie temp remove tissue option
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.THEILER_STAGE));
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.HUMAN_STAGE));
		filter.addFilter(8, Globals.getPredefinedFilter(PredefinedFilters.SEX));
		if(Utility.getProject().equalsIgnoreCase("gudmap")) 
			filter.addFilter(9, Globals.getPredefinedFilter(PredefinedFilters.LAB));
		filter.addFilter(10, Globals.getPredefinedFilter(PredefinedFilters.DATE));
		filter.addFilter(11, Globals.getPredefinedFilter(PredefinedFilters.SPECIMEN));
		filter.addFilter(3, Globals.getPredefinedFilter(PredefinedFilters.EXPRESSION));
//		System.out.println("FocusGeneBrowseBean:getFilter(out)");
		return filter;
	}
	
	private GenericTableFilter getNewFilter(int[] colMap) { 
	    if (debug)
	    	System.out.println("FocusGeneBrowseBean:getFilter(in)");
		GenericTableFilter filter = new GenericTableFilter();
		filter.setTableToSqlColMap(colMap);
		filter.addFilter(new FilterItem(0));
		if(Utility.getProject().equalsIgnoreCase("gudmap")) 
			filter.addFilter(2, Globals.getPredefinedFilter(PredefinedFilters.LAB));
		filter.addFilter(3, Globals.getPredefinedFilter(PredefinedFilters.DATE));		
		filter.addFilter(4, Globals.getPredefinedFilter(PredefinedFilters.ASSAY));
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.THEILER_STAGE));
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.HUMAN_STAGE));
		filter.addFilter(8, Globals.getPredefinedFilter(PredefinedFilters.SEX));
		filter.addFilter(11, Globals.getPredefinedFilter(PredefinedFilters.EXPRESSION));
		filter.addFilter(13, Globals.getPredefinedFilter(PredefinedFilters.SPECIMEN));
	    if (debug)
	    	System.out.println("FocusGeneBrowseBean:getNewFilter(out)");
		return filter;
	}

	
	public boolean getDumy() {
		return false;
	}
	
	public String getTitle() {
		return "Boolean Query";
	}

}
