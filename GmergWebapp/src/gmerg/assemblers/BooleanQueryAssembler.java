package gmerg.assemblers;

import gmerg.db.BooleanQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class BooleanQueryAssembler extends OffMemoryTableAssembler  {
	String input;
	
	public BooleanQueryAssembler (HashMap params) {
		super(params);
	}

	public void setParams() {
		
		super.setParams();
		input = getParam("input");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
		Connection conn = DBHelper.getDBConnection();
		BooleanQueryDAO booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);
		////////////////////////////////////////////////////////
		ArrayList list =  booleanQueryDAO.getAllSubmissions(input, column, ascending, offset, num, filter);
		////////////////////////////////////////////////////////
		DBHelper.closeJDBCConnection(conn);
		booleanQueryDAO = null;
		
        return QuickSearchAssembler.getTableDataFormatFromArrayList(list);
	}
	
	public int retrieveNumberOfRows() {
		Connection conn = DBHelper.getDBConnection();
		BooleanQueryDAO booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);
		int n =  booleanQueryDAO.getTotalNumberOfSubmissions(input, filter);
		DBHelper.closeJDBCConnection(conn);
		booleanQueryDAO = null;
        return n;
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
		BooleanQueryDAO booleanQueryDAO = MySQLDAOFactory.getBooleanQueryDAO(conn);

//		String[] genes = {"cd24a", "wnt4"}; //fake results
		ArrayList<String> genes = booleanQueryDAO.getGeneSymbols(queryString);
//		System.out.println("booleanQuery@gene numbers: " + genes.size());
		
		DBHelper.closeJDBCConnection(conn);
		booleanQueryDAO = null;

		return genes;
	}
	 
}
