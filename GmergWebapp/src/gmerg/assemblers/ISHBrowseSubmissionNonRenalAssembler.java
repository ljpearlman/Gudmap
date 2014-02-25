/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.ISHDevDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.DataItem;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class ISHBrowseSubmissionNonRenalAssembler extends OffMemoryTableAssembler{
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

    public ISHBrowseSubmissionNonRenalAssembler() {
	if (debug)
	    System.out.println("ISHBrowseSubmissionNonRenalAssembler.constructor");

    }
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (null != cache && cache.isSameQuery(column, ascending, offset, num)) {
			if (debug)
			    System.out.println("ISHBrowseSubmissionNonRenalAssembler.retriveData data not changed");
			
			return cache.getData();
	    }

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
	

			ArrayList browseSubmissionsNonRenal = ishDevDAO.getAllSubmissionsNonRenal(column, ascending, offset, num);

			DataItem[][] ret = ISHBrowseAssembler.getTableDataFormatFromIshList(browseSubmissionsNonRenal);

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
			System.out.println("ISHBrowseSubmissionNonRenalAssembler::retrieveData !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] retrieveTotals() {
	    // force new cache
	    cache = null;

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		int[] totalNumbers = new int[0];
		try{
			ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
	
			// get data from database
			String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_NON_RENAL",
	                "TOTAL_NUMBER_OF_GENE_SYMBOL_NON_RENAL",
	                "TOTAL_NUMBER_OF_THEILER_STAGE_NON_RENAL",
	                "TOTAL_NUMBER_OF_GIVEN_STAGE_NON_RENAL",
	                "TOTAL_NUMBER_OF_LAB_NON_RENAL",
	                "TOTAL_NUMBER_OF_SUBMISSION_DATE_NON_RENAL",
	                "TOTAL_NUMBER_OF_ASSAY_TYPE_NON_RENAL",
	                "TOTAL_NUMBER_OF_SPECIMEN_TYPE_NON_RENAL",
	                "TOTAL_NUMBER_OF_SEX_NON_RENAL",
	//                "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL_NON_RENAL",
	                "TOTAL_NUMBER_OF_PROBE_NAME_NON_RENAL",
	//                "TOTAL_NUMBER_OF_ANTIBODY_NAME_NON_RENAL",
	//                "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL_NON_RENAL",
	                "TOTAL_NUMBER_OF_GENOTYPE_NON_RENAL",
	                "TOTAL_NUMBER_OF_PROBE_TYPE_NON_RENAL",
	                "TOTAL_NUMBER_OF_IMAGE_NON_RENAL",
	                };
			String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, filter);
			
			// convert to interger array, each tuple consists of column index and the number
			int len = columnNumbers.length;
			totalNumbers = new int[len];
			for (int i=0;i<len;i++) {
				totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
			}

			// return result
			return totalNumbers;
		}
		catch(Exception e){
			System.out.println("ISHBrowseSubmissionNonRenalAssembler::retrieveTotals !!!");
			totalNumbers = new int[0];
			return totalNumbers;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public int retrieveNumberOfRows() {

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
			int totalNumberOfSubmissions = ishDevDAO.getTotalNumberOfNonRenalSubmissions();
			return totalNumberOfSubmissions;
		}
		catch(Exception e){
			System.out.println("ISHBrowseSubmissionNonRenalAssembler::retrieveNumberOfRows !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	
	public HeaderItem[] createHeader() {
		return ISHBrowseAssembler.createHeaderForISHBrowseTable();
	}    	

}
