package gmerg.beans;

import gmerg.assemblers.AdvancedSearchAssembler;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;

import java.util.ArrayList;
import java.util.HashMap;


public class AdvancedQueryResultBean {

    private boolean debug = false;

    protected Object[][] columns;
    protected Object[][] subs;

    protected ArrayList ibsd;

	public AdvancedQueryResultBean() {
	    if (debug)
		System.out.println("AdvancedQueryResultBean.constructor");

    	String viewName = "AdvancedQueryResult";
    	if (TableUtil.isTableViewInSession())
			return;
    	ArrayList list = (ArrayList)FacesUtil.getSessionValue("ad_query_option");
    	String sub = (String)FacesUtil.getSessionValue("ad_query_sub");
    	if (list != null) 
    		TableUtil.saveTableView(populateAdvancedQueryResultBrowseTableView(viewName, list, sub));
    	
    }
    
	public boolean getDumy() {
		return false;
	}
	
	private GenericTableView populateAdvancedQueryResultBrowseTableView(String viewName, ArrayList options, String sub) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("options", options);
		queryParams.put("sub", sub);
		AdvancedSearchAssembler assembler = new AdvancedSearchAssembler(queryParams);
		GenericTable table = assembler.createTable();
		GenericTableView tableView = QuickSearchBean.getDefaultSearchResultBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);

		return  tableView;
	}	

}
