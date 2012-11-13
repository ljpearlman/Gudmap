package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.FocusForAllAssembler;
import gmerg.assemblers.AnatomyStructureAssembler;
import gmerg.entities.Globals;
import gmerg.entities.Globals.PredefinedFilters;
import gmerg.utils.Visit;
import gmerg.utils.Utility;
import gmerg.utils.table.FilterItem;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableFilter;
import gmerg.utils.table.GenericTableView;

import gmerg.utils.table.TableUtil;

public class FocusGeneBrowseBean {
    private boolean debug = false;
    private String query;
    private String input;
    private String focusedOrgan;
	
	public FocusGeneBrowseBean() {
	    if (debug)
		System.out.println("FocusGeneBrowseBean constructor:");
	    query = Visit.getRequestParam("query");
		input = Visit.getRequestParam("input");
//		focusedOrgan = Visit.getRequestParam("focusedOrgan");		// changed by Mehran (23/11/09) - Anatomy search ignores any focus group
		// modified by xingjun - 14/01/2010 - start
		// - should not simply assign null value to focusdOrgan 
		// - other query will be affected when specifying focus organ (e.g. gene query)
//		focusedOrgan = null;
		if (query == null || query.trim().equalsIgnoreCase("")) {
			focusedOrgan = null;
		} else {
			focusedOrgan = Visit.getRequestParam("focusedOrgan");  
		}
		// modified by xingjun - 14/01/2010 - end
		String viewName = "focusGene";
		if (TableUtil.isTableViewInSession())
			return;
   
       	TableUtil.saveTableView(populateFocusGeneBrowseTableView(viewName));		
	}
	
	/**
	 * method to construct a String containing a summary of the query a user has made
	 * @return string literal containing summary of query
	 */
	public String getTitle() {
		
		AnatomyStructureAssembler assembler = new AnatomyStructureAssembler();
		String displayInput = "";
		String anatomyIdPrefix = Utility.getAnatomyIdPrefix().substring(0,Utility.getAnatomyIdPrefix().length()-1);
		
		if(input != null && ! input.equals("") && input.toUpperCase().indexOf(anatomyIdPrefix.toUpperCase())==0){
			displayInput = assembler.getComponentNameFromId(input);
		}
		if(displayInput != null && !displayInput.equals("")){
			displayInput =  input+" (" + displayInput + ")";
		}
		else {
			displayInput = input;
		}
		
		String organTitle = (focusedOrgan==null || focusedOrgan=="")? "" : " in <em>" + DatabaseHomepageBean.getOrganName(focusedOrgan) + "</em>";
		return "Result of <em>" + query + "</em> query for <em> " + displayInput + "</em>" + organTitle;
	}
	
	/**
	 * method to query the database and store the result of the query in a table object which will be rendered for the user
	 * <p>modified by xingjun - 01/05/2009
	 *    - solved the problem when user input string like ';gene' an exception will occur </p>
	 * @param viewName - the name of the table being viewed
	 * @return 
	 */
	private GenericTableView populateFocusGeneBrowseTableView(String viewName) {
	    if (debug)
		System.out.println("FocusGeneBrowseBean:populateFocusGeneBrowseTableView");
		String sub = Visit.getRequestParam("sub");
		String exp = Visit.getRequestParam("exp");
		String ttvRels = Visit.getRequestParam("ttvRels");
		String[] wildcards = new String[]{ Visit.getRequestParam("geneWildcard"), Visit.getRequestParam("geneStage"), Visit.getRequestParam("geneAnnotation") };
		String[] inputs = null;
		String [] tmp = null;
		if (!(input==null || input.equals(""))) {
		    inputs = input.split("\t|\n|\r|\f|;");
		    if (debug)
			System.out.println("number of symbols: " + inputs.length);
		    //			geneSymbols = input.split("[[\\s|,]&&[^ ]]");
		    /////// added by xingjun - 06/04/2009 - if the input by user comes like 'string*', it means wildcard search (start with) //////////////
	    if (debug)
			System.out.println("check input string###########: ");
			int numberOfInputItems = inputs.length;
			for (int i=0;i<numberOfInputItems;i++) {
	    if (debug)
				System.out.println("input string: " + inputs[i]);
				int stringLen = inputs[i].length();
				if (stringLen > 0) {
					// got last character of the input string
					String lastChar = inputs[i].substring(stringLen-1, stringLen);
	    if (debug)
					System.out.println("last char: " + lastChar);
					if (lastChar.equals("*")) {
						inputs[i] = inputs[i].substring(0, stringLen-1);
						wildcards[0] = "starts with";
	    if (debug)
						System.out.println("input string removed wc: " + inputs[i]);
					} else {
	    if (debug)
						System.out.println("not wild~~~~~~~~ ");
						wildcards[0] = "equals";
					}
				}
			}
			///////////////////////////////////////////////////////////////////
		}

		if("GeneSymbol".equals(query) && (wildcards[0]==null || wildcards[0]=="") ) { //This is used for queries from focused_gene_index_browse page
	    if (debug)
			System.out.println("query gene symbol");
			wildcards[0] = "equals";
			wildcards[1] = "ALL";
			wildcards[2] = "0";
		}
    	
		if (debug) {
System.out.println("FocusGeneBrowseBean:populateFocusGeneBrowseTableView:organ="+ focusedOrgan);        
System.out.println("input="+input);        	
System.out.println("query="+ query);        	
System.out.println("sub="+ sub);        	
System.out.println("exp="+ exp);        	
System.out.println("widecard0="+ wildcards[0]);
System.out.println("widecard1="+ wildcards[1]);
System.out.println("widecard2="+ wildcards[2]);
		}
		
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("organ", focusedOrgan);
		queryParams.put("geneSymbols", inputs);
		queryParams.put("query", query);
		queryParams.put("sub", sub);
		queryParams.put("exp", exp);
		queryParams.put("widecard", wildcards);
		queryParams.put("transitiveRelations", ttvRels);
		FocusForAllAssembler assembler = new FocusForAllAssembler(queryParams);
		//Bernie 10/5/2011 - Mantis 328 - added call to set filter
		assembler.setFilter(getFilter(assembler.getTableviewToSqlColMap()));
		
	    GenericTable table = assembler.createTable();
        GenericTableView tableView = QuickSearchBean.getDefaultSearchResultBrowseTableView(viewName, table);

        // Bernie 27/10/2010 - added to reduce length of string when displayed
		tableView.setColMaxWidth(5, 30, true);  // Trim value if it is more than 30 characters (adds ... at the end)

        if ("anatomy".equalsIgnoreCase(query))
        	tableView.setColHidden(3, false);
        if((tableView == null || tableView.getTable() == null || tableView.getTable().getNumRows() == 0) && Utility.getProject().equalsIgnoreCase("GUDMAP")) {
        	tableView.setNoDataMessage(Utility.getNoDataMessageForQueryPage(query, input));
        }
		tableView.setDisplayTotals(false);
		return  tableView;
	}
	
	//Bernie 10/5/2011 - Mantis 328 - added method
	//Bernie 01/03/2010 - Mantis 620 - added SEX to the filter options
	private GenericTableFilter getFilter(int[] colMap) { 
	    if (debug)
		System.out.println("FocusGeneBrowseBean:getFilter(in)");
		GenericTableFilter filter = new GenericTableFilter();
		filter.setTableToSqlColMap(colMap);
		filter.addFilter(new FilterItem(0));
		filter.addFilter(2, Globals.getPredefinedFilter(PredefinedFilters.ASSAY));
//		filter.addFilter(new FilterItem(5));// bernie temp remove tissue option
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.STAGE));
		filter.addFilter(8, Globals.getPredefinedFilter(PredefinedFilters.SEX));
		if(Utility.getProject().equalsIgnoreCase("gudmap")) 
			filter.addFilter(9, Globals.getPredefinedFilter(PredefinedFilters.LAB));
		filter.addFilter(10, Globals.getPredefinedFilter(PredefinedFilters.DATE));
		filter.addFilter(11, Globals.getPredefinedFilter(PredefinedFilters.SPECIMEN));
		filter.addFilter(3, Globals.getPredefinedFilter(PredefinedFilters.EXPRESSION));
	    if (debug)
		System.out.println("FocusGeneBrowseBean:getFilter(out)");
		return filter;
	}
	
}
