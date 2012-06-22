package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.IHCDAO;
import gmerg.db.ISHDevDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class FocusBrowseIHCAssembler extends OffMemoryTableAssembler{
	
    private boolean debug = false;

	String[] organs;
	
	public FocusBrowseIHCAssembler () {
		super();
	if (debug)
	    System.out.println("FocusBrowseIHCAssembler.constructor");

	}
	
	public FocusBrowseIHCAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("FocusBrowseIHCAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
		organs = getParams("organs");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		IHCDAO ihcDAO = MySQLDAOFactory.getIHCDAO(conn);

		// get data from database
		ArrayList submissions = ihcDAO.getAllSubmissionISH(column, ascending, offset, num, "IHC", organs, filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ihcDAO = null;
		
		/** ---return the value object---  */
		return ISHBrowseAssembler.getTableDataFormatFromIshList(submissions);
	}
	
	public int retrieveNumberOfRows() {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		IHCDAO ihcDAO = MySQLDAOFactory.getIHCDAO(conn);

		// get data from database
		int totalNumberOfSubmissions = ihcDAO.getTotalNumberOfSubmissions("IHC", organs, filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ihcDAO = null;
		
		//* ---return the value---  * /
		return totalNumberOfSubmissions;
	}
	
	public HeaderItem[] createHeader() {
		return ISHBrowseAssembler.createHeaderForISHBrowseTable();
	}    	

	/**
	 * 
	 * @return
	 */
	public int[] retrieveTotals() {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION",
                "TOTAL_NUMBER_OF_GENE_SYMBOL",
                "TOTAL_NUMBER_OF_THEILER_STAGE",
                "TOTAL_NUMBER_OF_GIVEN_STAGE",
                "TOTAL_NUMBER_OF_LAB",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE",
                "TOTAL_NUMBER_OF_ASSAY_TYPE",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
                "TOTAL_NUMBER_OF_SEX",
//                "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL",
                "TOTAL_NUMBER_OF_PROBE_NAME",
//                "TOTAL_NUMBER_OF_ANTIBODY_NAME",
//                "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL",
                "TOTAL_NUMBER_OF_GENOTYPE",
                "TOTAL_NUMBER_OF_PROBE_TYPE",
                "TOTAL_NUMBER_OF_IMAGE",
                };
		
		String endingClause = " AND (SUB_ASSAY_TYPE = 'IHC') "; // Bernie 04/11/2010 - added endingClause to get correct totals
		String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);
		// convert to integer array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++)
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);

		// return result
		return totalNumbers;
	}
	
	
	
}
