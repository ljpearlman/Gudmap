/**
 * 
 */
package gmerg.assemblers;

//import java.util.ArrayList;
import java.sql.*;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ish.ISHBrowseSubmissionData;
//import gmerg.entities.submission.BrowseSubmission;
import gmerg.entities.submission.ish.ISHBrowseSubmission;

/**
 * @author xingjun
 *
 */
public class ISHBrowseSubmissionAssembler {
    private boolean debug = false;
    public ISHBrowseSubmissionAssembler() {
	if (debug)
	    System.out.println("ISHBrowseSubmissionAssembler.constructor");

    }
	/**
	 * return a composite vo including all ish submissions info and total
	 * number of listed columns
	 * 
	 * @param order
	 * @param offset
	 * @param num
	 * @return
	 */
//	public ISHBrowseSubmissionData getData(String[] order,
//			                               String offset, String num) {
//		
//		ISHBrowseSubmissionData ibsData = new ISHBrowseSubmissionData();
//		
//        /** ---get data from dao---  */
//		// create a dao
//		Connection conn = DBHelper.getDBConnection();
//		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
//		
//		// get all submission entries
////		ArrayList allEntries = ishDAO.getAllSubmissionISH(order, offset, num);
//		ISHBrowseSubmission[] browseSubmissions = ishDAO.getAllSubmissionISH(order, offset, num);
////		String[][] browseSubmissions = 
////			(String [][])allEntries.toArray(new String[allEntries.size()][0]);
//		
////		ISHBrowseSubmission[] browseSubmissions =
////			(ISHBrowseSubmission[])allEntries.toArray(new ISHBrowseSubmission[allEntries.size()]);
//		
//		// get all column numbers, store the col totals in a 2D array
//		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION",
//				                         "TOTAL_NUMBER_OF_GENE_SYMBOL",
//				                         "TOTAL_NUMBER_OF_THEILER_STAGE",
//				                         "TOTAL_NUMBER_OF_GIVEN_STAGE",
//				                         "TOTAL_NUMBER_OF_LAB",
//				                         "TOTAL_NUMBER_OF_SUBMISSION_DATE",
//				                         "TOTAL_NUMBER_OF_ASSAY_TYPE",
//				                         "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
//				                         "TOTAL_NUMBER_OF_IMAGE"};
//
//		String[][] columnNumbers = 
//			ishDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries);
//				
//		/** ---construct the composite value object---  */
//		ibsData.setBroseSubmissionData(browseSubmissions);
//		ibsData.setAllColumnTotalNumbers(columnNumbers);
//		
//		// release db resources
//		DBHelper.closeJDBCConnection(conn);
//		ishDAO = null;
//		
//		/** ---return the composite value object---  */
//		return ibsData;
//	}
	
	
	/**
	 * return total number of ish submissions
	 * @return
	 */
	public String getTotalSubmissions() {
		
		String totalSubmissions = new String("");
		
		/** ---get data from dao--- */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get the value
		totalSubmissions += ishDAO.getTotalNumberOfSubmission();
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;

		/** return the value  */
		return totalSubmissions;
	}
	
	/**
	 * @author xingjun
	 * 
	 * @param selectedSubmissions
	 * @return
	 */
	public boolean deleteSelectedSubmissions(String[] selectedSubmissions) {
		
		boolean submissionDeleted = false;
		
		// no selected submissions
		if (selectedSubmissions == null || selectedSubmissions.length == 0) {
			return submissionDeleted;
		}
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// delete
		submissionDeleted = ishEditDAO.deleteSelectedSubmission(selectedSubmissions);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionDeleted;
	}

}
