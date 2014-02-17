package gmerg.assemblers;

import gmerg.db.AdvancedQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class FocusForAllAssembler extends OffMemoryTableAssembler {
    protected boolean debug = false; 
    protected RetrieveDataCache cache = null;

	String organ;
	String[] input;
	String query;
	String sub;
	String exp;
	String[] widecard;
	String transitiveRelations;
	String archiveId;
	String batchId;

	public FocusForAllAssembler () {
	    if (debug)
	    	System.out.println("FocusForAllAssembler.constructor no argument");
	    
	}

	public FocusForAllAssembler (HashMap params) {
		super(params);
		if (debug)
		    System.out.println("FocusForAllAssembler.constructor");

	}

	public void setParams() {
	    if (debug)
	    	System.out.println("FocusForAllAssembler.setParams");
		super.setParams();
		organ = getParam("organ");
		input = getParams("geneSymbols");
		query = getParam("query");
		sub = getParam("sub");
		exp = getParam("exp");
		widecard = getParams("widecard");
		transitiveRelations = getParam("transitiveRelations");
		archiveId = getParam("archiveId");
		batchId = getParam("batchId");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (debug)
	    	System.out.println("FocusForAllAssembler.retrieveData");

	    if (null != cache && cache.isSameQuery(column, ascending, offset, num)) {
			if (debug)
			    System.out.println("FocusForAllAssembler.retriveData data not changed");
			
			return cache.getData();
	    }

		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedQDAO;
		ArrayList list = null;
		try{
			advancedQDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
			list =  advancedQDAO.getFocusQuery(query, input, column, ascending, String.valueOf(offset), String.valueOf(num), 
														organ, sub, exp, widecard, transitiveRelations, archiveId, batchId, filter);

			DataItem[][] ret = QuickSearchAssembler.getTableDataFormatFromArrayList(list);

			if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(ret);
			cache.setColumn(column);
			cache.setAscending(ascending);
			cache.setOffset(offset);
			cache.setNum(num);

			return ret;
		}
		catch(Exception e){
			System.out.println("FocusForAllAssembler::retrieveData !!!");
			list = null;

			DataItem[][] ret = QuickSearchAssembler.getTableDataFormatFromArrayList(list);

			return ret;
		}
		finally{
			/** release db resources */
			DBHelper.closeJDBCConnection(conn);
			advancedQDAO = null;
		}
	}
	
	public int retrieveNumberOfRows() {

	    // force new cache
	    cache = null;

	    if (debug)
	    	System.out.println("+++FocusForAllAssembler:retrieveNumberOfRows");
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedDAO;
		int n = 0;
		try{
			advancedDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
			n =  advancedDAO.getNumberOfRows(query, input, organ, sub, exp, widecard, transitiveRelations, archiveId, batchId, filter);

			if (debug)
				System.out.println("FocusForAllAssembler:retrieveNumberOfRows return = "+n);
			    
			return n;
		}
		catch(Exception e){
			System.out.println("FocusForAllAssembler::retrieveNumberOfRows !!!");
			return 0;
		}	
		finally{
			/** release db resources */
			DBHelper.closeJDBCConnection(conn);
			advancedDAO = null;
		}
	}
	
	
	/**
	 * @author xingjun
	 * @return
	 */
	public int[] retrieveNumberOfRowsInGroups() {
	    if (debug)
	    	System.out.println("+++FocusForAllAssembler:retrieveNumberOfRowsInGroups");
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedQueryDAO;
		int[] totals = null;
		try{
			advancedQueryDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
	
			totals =  advancedQueryDAO.getNumberOfRowsInGroups(query, input, organ, sub, exp, widecard, transitiveRelations, archiveId, batchId, filter);

			this.table.setTotals(totals);
			return totals;
		}
		catch(Exception e){
			System.out.println("FocusForAllAssembler::retrieveNumberOfRowsInGroups !!!");
			totals = new int[0];

			return totals;
		}	
		finally{
			/** release db resources */
			DBHelper.closeJDBCConnection(conn);
			advancedQueryDAO = null;
		}
	}
		
	public HeaderItem[] createHeader() {
	    if (debug)
	    	System.out.println("FocusForAllAssembler.createHeader");

		return QuickSearchAssembler.createHeaderForSearchResultTable();
	}
	
	// Bernie - 9/5/2011 - Mantis 328 - added method
	public int[] getTableviewToSqlColMap() {
	    if (debug)
	    	System.out.println("FocusForAllAssembler.getTableviewToSqlColMap");

		return QuickSearchAssembler.getTableviewToSqlColMap();
	}
	
	

}
