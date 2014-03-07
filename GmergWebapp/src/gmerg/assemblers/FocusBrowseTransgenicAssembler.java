/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.TransgenicDAO;
import gmerg.db.ISHDevDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xingjun
 *
 */
public class FocusBrowseTransgenicAssembler extends OffMemoryTableAssembler {
	String[] organs;
	String[] archiveIds;
	String[] batchIds;
    protected boolean debug = true;
	protected RetrieveDataCache cache = null;

	public FocusBrowseTransgenicAssembler () {
	if (debug)
	    System.out.println("FocusBrowseTransgenicAssembler.constructor");
	       
	}
	
	public FocusBrowseTransgenicAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("FocusBrowseTransgenicAssembler.constructor");
	}

	public void setParams() {
		super.setParams();
		organs = getParams("organs");
		archiveIds = getParams("archiveIds");
		batchIds = getParams("batchIds");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (null != cache &&
		cache.isSameQuery(column, ascending, offset, num)) {
		if (debug)
		    System.out.println("FocusBrowseTransgenicAssembler.retriveData data not changed");
		
		return cache.getData();
	    }

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			TransgenicDAO transgenicDAO = MySQLDAOFactory.getTransgenicDAO(conn);

			ArrayList submissions = transgenicDAO.getAllSubmission(column, ascending, offset, num, organs, archiveIds, batchIds, filter);

			DataItem[][] ret = ISHBrowseAssembler.getTableDataFormatFromIshList(submissions);

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
			System.out.println("FocusBrowseTransgenicAssembler::retrieveData !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public int retrieveNumberOfRows() {
	if (debug)
	    System.out.println("FocusBrowseTransgenicAssembler.retrieveNumberOfRows");

	    // force new cache
	    cache = null;

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			TransgenicDAO transgenicDAO = MySQLDAOFactory.getTransgenicDAO(conn);
			int totalNumberOfSubmissions = transgenicDAO.getTotalNumberOfSubmissions(organs, archiveIds, batchIds, filter);
			return totalNumberOfSubmissions;
		}
		catch(Exception e){
			System.out.println("FocusBrowseTransgenicAssembler::retrieveNumberOfRows !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public int[] retrieveTotals() {
	if (debug)
	    System.out.println("FocusBrowseTransgenicAssembler.retrieveTotals");

	    // force new cache
	    cache = null;

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		int[] totalNumbers = null;
		try{
			TransgenicDAO transgenicDAO = MySQLDAOFactory.getTransgenicDAO(conn);
			
			// get data from database
			String [] allColTotalsQueries = {
					"TOTAL_NUMBER_OF_GENE_SYMBOL_TG",
					"TOTAL_NUMBER_OF_SUBMISSION_TG",
					"TOTAL_NUMBER_OF_LAB_TG",
					"TOTAL_NUMBER_OF_SUBMISSION_DATE_TG",
					"TOTAL_NUMBER_OF_ASSAY_TYPE_TG",
					"TOTAL_NUMBER_OF_PROBE_NAME_TG",
					"TOTAL_NUMBER_OF_THEILER_STAGE_TG",
					"TOTAL_NUMBER_OF_GIVEN_STAGE_TG",
					"TOTAL_NUMBER_OF_SEX_TG",
					"TOTAL_NUMBER_OF_GENOTYPE_TG",
	                "TOTAL_NUMBER_OF_ISH_EXPRESSION_TG",
					"TOTAL_NUMBER_OF_SPECIMEN_TYPE_TG",
					"TOTAL_NUMBER_OF_IMAGE_TG"
			};
	//		String endingClause = " AND SUB_ASSAY_TYPE = 'TG' ";	// Bernie 16/11/2010 mod to ensure correct totals are returned
	//		String[][] columnNumbers = transgenicDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);
			String[][] columnNumbers = transgenicDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, "", filter);
			
			// convert to interger array, each tuple consists of column index and the number
			int len = columnNumbers.length;
			totalNumbers = new int[len];
			for (int i=0;i<len;i++) {
				if (columnNumbers[i][1] != null) {
					totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
				} else {
					totalNumbers[i] = 0;
				}
			}
			// return result
			return totalNumbers;
		}
		catch(Exception e){
			System.out.println("FocusBrowseTransgenicAssembler::retrieveTotals !!!");
			totalNumbers = new int[0];
			return totalNumbers;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

	public HeaderItem[] createHeader() {
		return ISHBrowseAssembler.createHeaderForISHBrowseTable();
	}    	

}
