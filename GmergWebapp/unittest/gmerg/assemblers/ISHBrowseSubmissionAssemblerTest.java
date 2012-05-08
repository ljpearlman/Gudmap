package gmerg.assemblers;

import junit.framework.TestCase;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ish.ISHBrowseSubmission;

//import java.util.ArrayList;
import java.sql.*;

public class ISHBrowseSubmissionAssemblerTest extends TestCase {
	
//	private ArrayList allEntries;
	ISHBrowseSubmission[] allEntries;
	private String[][] columnNumbers;
	private ISHDAO ishDAO = null;
	private ISHEditDAO ishEditDAO = null;
	private String totalNumberOfSubmissions;
	Connection conn = null;

	public ISHBrowseSubmissionAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		// create a dao
		conn = DBHelper.getDBConnection();
		ishDAO = MySQLDAOFactory.getISHDAO(conn);
		ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// release a dao
		ishDAO = null;
		ishEditDAO = null;
		DBHelper.closeJDBCConnection(conn);
	}

	/**
	 * Test method for 'db.ISHBrowseAssembler.getData(String, String, String, String)'
	 */
	public final void testGetData() {
		// TODO Auto-generated method stub

		// get all submission entries
//		allEntries = ishDAO.getAllSubmissionISH(null, "1", "10");
//		assertNotNull(allEntries);
//		assertEquals("GUDMAP:5306",allEntries[0].getId());
//		allEntries = ishDAO.getAllSubmissionISH(null, "2", "100");
//		allEntries = ishDAO.getAllSubmissionISH(null, "A", "h");
//		allEntries = ishDAO.getAllSubmissionISH(null, "A", null);

//		String[] orderBy = {"byGene", "DESC"};
//		allEntries = ishDAO.getAllSubmissionISH(orderBy, "A", null);
//		assertNotNull(allEntries);
		
//		allEntries = ishDAO.getAllSubmissionISH(orderBy, "A", "50");
//		assertEquals(50, allEntries.size());
//		assertEquals("not equal", 100, allEntries.size());
		
		// get total number of columns
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION",
                "TOTAL_NUMBER_OF_GENE_SYMBOL",
                "TOTAL_NUMBER_OF_THEILER_STAGE",
                "TOTAL_NUMBER_OF_GIVEN_STAGE",
                "TOTAL_NUMBER_OF_LAB",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE",
                "TOTAL_NUMBER_OF_ASSAY_TYPE",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
                "TOTAL_NUMBER_OF_IMAGE"};
		columnNumbers = ishDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries);
		assertNotNull(columnNumbers);

	}
	
	/**
	 * Test method for 'db.ISHBrowseAssembler.getData(String, String, String, String)'
	 */
	public final void testGetTotalSubmissions() {
		// get the data
		totalNumberOfSubmissions = ishDAO.getTotalNumberOfSubmission();
		assertNotNull(totalNumberOfSubmissions);
		assertEquals("3229", totalNumberOfSubmissions);
	}
	
	/**
	 * Test method for 'db.ISHBrowseSubmissionAssembler.deleteSelectedSubmissions(String[])''
	 */
	public final void testDeleteSelectedSubmissions() {
		
		// initialise
//		String[] selectedSubmissions = {"GUDMAP:5306", "GUDMAP:7", "GUDMAP:6"};
//		String[] selectedSubmissions = {"GUDMAP:5306"};
//		String[] selectedSubmissions = {""};
		String[] selectedSubmissions = null;
		
		boolean submissionDeleted = ishEditDAO.deleteSelectedSubmission(selectedSubmissions);
		assertEquals(false, submissionDeleted);
	}

}
