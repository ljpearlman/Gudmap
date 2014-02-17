package gmerg.assemblers;

import gmerg.db.BooleanQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class BooleanQueryAssembler extends OffMemoryTableAssembler  {
    private boolean debug = false;
    protected RetrieveDataCache cache = null;

	String input;
	public BooleanQueryAssembler () {
	    if (debug)
		System.out.println("BooleanQueryAssembler.constructor no param");
	    
	}

	public BooleanQueryAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("BooleanQueryAssembler.constructor");

	}

	public void setParams() {
		
		super.setParams();
		input = getParam("input");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (null != cache && cache.isSameQuery(column, ascending, offset, num)) {
			if (debug)
			    System.out.println("BooleanQueryAssembler.retriveData data not changed");
			
			return cache.getData();
	    }

		Connection conn = DBHelper.getDBConnection();
		BooleanQueryDAO booleanQueryDAO;
		ArrayList list = new ArrayList();
		try{
			booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);
			list =  booleanQueryDAO.getAllSubmissions(input, column, ascending, offset, num, filter);

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
			System.out.println("BooleanQueryAssembler::retrieveData failed !!!");
			list = null;

	        DataItem[][] ret = QuickSearchAssembler.getTableDataFormatFromArrayList(list);

			return ret;
		}
		finally{
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			booleanQueryDAO = null;
		}
	}
	
	public int retrieveNumberOfRows() {
		if (debug)
		    System.out.println("BooleanQueryAssembler.retrieveNumberOfRows");

		// force new cache due to filter
		cache = null;

		Connection conn = DBHelper.getDBConnection();
		BooleanQueryDAO booleanQueryDAO;
		int n = 0;
		try{
			booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);
			n =  booleanQueryDAO.getTotalNumberOfSubmissions(input, filter);
	        return n;
		}
		catch(Exception e){
			System.out.println("BooleanQueryAssembler::retrieveNumberOfRows failed !!!");
	        return 0;
		}
		finally{
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			booleanQueryDAO = null;
		}
	}
	
	public HeaderItem[] createHeader() {
		return QuickSearchAssembler.createHeaderForSearchResultTable();
	}
	
	/**
	 * @author xingjun
	 * @return
	 */
/*	commented by Mehran (20/11/09) -  because it always return 0 foir micro array and it is total number of rows for ISH  
	public int[] retrieveNumberOfRowsInGroups() {
		Connection conn = DBHelper.getDBConnection();
		BooleanQueryDAO booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);
		int[] totals =  booleanQueryDAO.getNumberOfRowsInGroups(input);
		DBHelper.closeJDBCConnection(conn);
		
        return totals;
	}
*/
	
	/**
	 * @author xingjun - 06/03/2009
	 */
	public static ArrayList<String> retrieveGenes(String queryString){
//		System.out.println("booleanQuery@geneType: " + queryString);
		
		Connection conn = DBHelper.getDBConnection();
		BooleanQueryDAO booleanQueryDAO;
		ArrayList<String> genes = new ArrayList<String>();
		try{
//			String[] genes = {"cd24a", "wnt4"}; //fake results
			booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);
			genes = booleanQueryDAO.getGeneSymbols(queryString);
//			System.out.println("booleanQuery@gene numbers: " + genes.size());
			return genes;
		}
		catch(Exception e){
			System.out.println("BooleanQueryAssembler::retrieveGenes failed !!!");
			genes = new ArrayList<String>();
			return genes;
		}
		finally{
			// release the db resources		
			DBHelper.closeJDBCConnection(conn);
			booleanQueryDAO = null;
		}
	}
	 
}
