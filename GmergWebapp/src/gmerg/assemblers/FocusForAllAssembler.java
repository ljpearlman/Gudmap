package gmerg.assemblers;

import gmerg.db.AdvancedQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class FocusForAllAssembler extends OffMemoryTableAssembler {
    private boolean debug = false;
	String organ;
	String[] input;
	String query;
	String sub;
	String exp;
	String[] widecard;
	String transitiveRelations;

	public FocusForAllAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("FocusForAllAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
		organ = getParam("organ");
		input = getParams("geneSymbols");
		query = getParam("query");
		sub = getParam("sub");
		exp = getParam("exp");
		widecard = getParams("widecard");
		transitiveRelations = getParam("transitiveRelations");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (debug)
		System.out.println("+++FocusForAllAssembler:retrieveData");
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedQDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		////////////////////////////////////////////////////////
		// Bernie - 9/5/2011 - Mantis 328 - added filter
		ArrayList list =  advancedQDAO.getFocusQuery(query, input, column, ascending, String.valueOf(offset), String.valueOf(num), 
													organ, sub, exp, widecard, transitiveRelations, filter);
		////////////////////////////////////////////////////////
		DBHelper.closeJDBCConnection(conn);
		
		return QuickSearchAssembler.getTableDataFormatFromArrayList(list);
	}
	
	public int retrieveNumberOfRows() {
	    if (debug)
		System.out.println("+++FocusForAllAssembler:retrieveNumberOfRows");
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		// Bernie - 9/5/2011 - Mantis 328 - added filter
		int n =  advancedDAO.getNumberOfRows(query, input, organ, sub, exp, widecard, transitiveRelations, filter);
		DBHelper.closeJDBCConnection(conn);

		if (debug)
		System.out.println("FocusForAllAssembler:retrieveNumberOfRows return = "+n);
		    
		return n;
	}
	
	
	/**
	 * @author xingjun
	 * @return
	 */
	public int[] retrieveNumberOfRowsInGroups() {
	    if (debug)
		System.out.println("+++FocusForAllAssembler:retrieveNumberOfRowsInGroups");
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedQueryDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		// Bernie - 9/5/2011 - Mantis 328 - added filter
		int[] totals =  advancedQueryDAO.getNumberOfRowsInGroups(query, input, organ, sub, exp, widecard, transitiveRelations, filter);
		DBHelper.closeJDBCConnection(conn);
		this.table.setTotals(totals);
		return totals;
	}
		
	public HeaderItem[] createHeader() {
		return QuickSearchAssembler.createHeaderForSearchResultTable();
	}
	
	// Bernie - 9/5/2011 - Mantis 328 - added method
	public int[] getTableviewToSqlColMap() {
		return QuickSearchAssembler.getTableviewToSqlColMap();
	}
	
	

}
