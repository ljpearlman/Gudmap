package gmerg.assemblers;

import gmerg.db.AdvancedQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;

public class AdvancedSearchAssembler extends OffMemoryTableAssembler {
	
	String sub;
	ArrayList options;
	int[] totals;
	
	public AdvancedSearchAssembler () {
		super();
	}
	
	public AdvancedSearchAssembler (HashMap params) {
		super(params);
	}

	public void setParams() {
		super.setParams();
		options = (ArrayList)params.get("options");
		sub = getParam("sub");
		totals = getTotalNumbers();
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
		String[] order = new String[2];
		if(true == ascending) {
			order[0] = "ASC";
		} else {
			order[0] = "DESC";
		}
		order[1] = orderResult(column);
		
		ArrayList list =  getQueryResult(options, order, String.valueOf(offset), String.valueOf(num), totals, sub);
		
        return QuickSearchAssembler.getTableDataFormatFromArrayList(list);
	}
	
	public int retrieveNumberOfRows() {
		if (totals != null)
			return totals[0];
		
        return 0;
	}	
	
	public HeaderItem[] createHeader() {
		return QuickSearchAssembler.createHeaderForSearchResultTable();
	}
	
	private int[] getTotalNumbers() {        
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		int[] list =  ishDAO.getTotalNumbers(options, null, null, null, sub);
		DBHelper.closeJDBCConnection(conn);
        return list;
	}	
	
	public ArrayList getQueryResult(ArrayList option, String[] order, String offset, String resPerPage, int[] total, String sub) {        
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		ArrayList list =  ishDAO.getQueryResult(option, order, offset, resPerPage, total, sub);
		DBHelper.closeJDBCConnection(conn);
        return list;	
	}	
	
	public int[] getColumnTotals(String type, String[] input)
	{
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		int[] list =  ishDAO.getColumnTotals(type, input);
		DBHelper.closeJDBCConnection(conn);
        return list;
	}
	
	public ArrayList getOptionInDB(String option, Hashtable lookup) {
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		ArrayList list = ishDAO.getOptionInDB(option, lookup);
		DBHelper.closeJDBCConnection(conn);
		
        return list;
	}

	public ArrayList getAllAnatomyTerms(String geneSymbol)
	{
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		ArrayList list =  ishDAO.getAllAnatomyTerms(geneSymbol);
		DBHelper.closeJDBCConnection(conn);
        return list;
	}

	public ArrayList getWholeAnatomyTree(String startStage, String endStage)
	{
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		ArrayList list =  ishDAO.getWholeAnatomyTree(startStage, endStage);
		DBHelper.closeJDBCConnection(conn);
        return list;
	}

    private String orderResult(int orderby) {
		String orderStr = null;
		String col = String.valueOf(orderby);
			if(orderby < 0)
				orderStr = "-1";
			if(col.equals("0")) {
				orderStr = "1";
			} else if(col.equals("1")) {
				orderStr = "10"; 
			} else if(col.equals("2")) {
				orderStr = "14";
			} else if(col.equals("4")) {
				orderStr = "12";
			} else if(col.equals("3")) {
				orderStr = "3";
			} else if(col.equals("5")) {
				orderStr = "2";
			} else if(col.equals("6")) {
				orderStr = "6";
			} else if(col.equals("7")) {
				orderStr = "8";
			} else if(col.equals("8")) {
				orderStr = "4";
			} else if(col.equals("9")) {
				orderStr = "5";
			} else if(col.equals("10")) {
				orderStr = "7";
			} else if(col.equals("11")) {
				orderStr = "9";
			}
		return orderStr;
    }		

}
