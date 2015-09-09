/**
 * 
 */
package gmerg.beans;

import java.util.ArrayList;
import java.util.HashMap;

import gmerg.entities.Globals;
import gmerg.utils.DbUtility;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.TableUtil;

/**
 * @author xingjun
 *
 */
public class GeneQueryResultBean {
    private boolean debug = false;
	private String input;// added by xingjun - 17/06/2010 - catch what the user search for
	private String query;// added by xingjun - 17/06/2010 - catch what the user search for
	private String focusedOrgan; // added by xingjun - 17/06/2010 - catch what the user search for
//	private String searchResultOption;
	
	public GeneQueryResultBean() {
	    if (debug)
		System.out.println("GeneQueryResultBean.constructor");

		input = Visit.getRequestParam("input");
	    query = Visit.getRequestParam("query");
		if (query.equalsIgnoreCase("")) {
			focusedOrgan = null;
		} else {
			focusedOrgan = Visit.getRequestParam("focusedOrgan");  
		}
		
//		searchResultOption = Visit.getRequestParam("searchResultOption");
		if (TableUtil.isTableViewInSession()) {
			return;
		}
		TableUtil.saveTableView(populateGeneQueryResultTableView("geneQueryResult"));
	}
	
	/**
	 * @author xingjun - 07/04/2009
	 * @param viewName
	 * @return
	 */
	private GenericTableView populateGeneQueryResultTableView(String viewName) {
		
    	// check input string to decide wildcard value
    	String wildcard = "equals";
    	if (input != null) {
    		input = input.trim();
    		int stringLen = input.length();
    		String lastChar = input.trim().substring(stringLen-1, stringLen);
//			System.out.println("last char: " + lastChar);
    		if (lastChar.equals("*")) {
    			input = input.substring(0, stringLen-1);
    			wildcard = "starts with";
    			//			System.out.println("input string removed wc: " + input);
    		}
		}
		
		// get gene symbols
		ArrayList<String> geneData = DbUtility.retrieveGeneSymbolsFromGeneInput(input, wildcard);

		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(geneData, 1); // corresponds to gene symbols collection
		OffMemoryTableAssembler assembler = helper.getCollectionBrowseAssembler();
		
	    GenericTable table = assembler.createTable(0);
	    GenericTableView tableView = GeneStripBrowseBean.getDefaultGeneStripTableView(viewName, table);

	    tableView.setDisplayTotals(false);
       	tableView.setNoDataMessage(Utility.getNoDataMessageForQueryPage("", ""));

       	return  tableView;
	}

	public boolean getDumy() {
		return false;
	}
	
	/**
	 * modified by xingjun - 17/06/2010 - added code to display what the user search for
	 * @return
	 */
	public String getTitle() {
		String displayInput = input;
		String organTitle = (focusedOrgan==null || focusedOrgan=="")? "" : " in <em>" + DatabaseHomepageBean.getOrganName(focusedOrgan) + "</em>";
		return "Result of <em>" + query + "</em> query for <em> " + displayInput + "</em>" + organTitle;
//		return "Gene Query";
	}

}
