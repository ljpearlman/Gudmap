//package gmerg.assemblers;
//
//import gmerg.entities.submission.array.ArrayBrowseSubmission;
//import gmerg.db.ArrayDAO;
//import gmerg.db.DBHelper;
//import gmerg.db.MySQLDAOFactory;
//
//import java.sql.Connection;
////import java.util.ArrayList;
//
//import junit.framework.TestCase;
//
//public class ArrayBrowseSubmissionAssemblerTest extends TestCase {
//
//	private ArrayBrowseSubmission[] allEntries;
//	private String[][] columnNumbers;
//	ArrayDAO arrayDAO = null;
//	Connection conn = null;
//
//	public ArrayBrowseSubmissionAssemblerTest(String arg0) {
//		super(arg0);
//	}
//
//	protected void setUp() throws Exception {
//		super.setUp();
//		// create a dao
//		conn = DBHelper.getDBConnection();
//		arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
//	}
//
//	protected void tearDown() throws Exception {
//		super.tearDown();
//		// release a dao
//		arrayDAO = null;
//		DBHelper.closeJDBCConnection(conn);
//	}
//
//	/*
//	 * Test method for 'db.ArrayBrowseAssembler.getData(String, String, String, String)'
//	 */
//	public final void testGetData() {
//		// TODO Auto-generated method stub
//
//		// get all submission entries
////		allEntries = arrayDAO.getAllSubmissionArray(null, null, null);
////		assertNotNull(allEntries);
//		
////		allEntries = arrayDAO.getAllSubmissionArray(null, "2", "100");
////		assertNotNull(allEntries);
//
////		allEntries = arrayDAO.getAllSubmissionArray(null, "A", "h");
////		assertEquals(164, allEntries.size());
//		
////		allEntries = arrayDAO.getAllSubmissionArray(null, "A", null);
////		assertEquals(164, allEntries.size());
//
////		String[] orderBy = {"bySeries", "DESC"};
////		allEntries = arrayDAO.getAllSubmissionArray(orderBy, "A", null);
////		assertNotNull(allEntries);
//		
//		String[] orderBy = {"bySeries", "DESC"};
//		allEntries = arrayDAO.getAllSubmissionArray(orderBy, "A", "50");
//		assertEquals(50, allEntries.length);
////		assertEquals("not equal", 100, allEntries.size());
//		
//		// get total number of columns
//		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_ARRAY",
//				                         "TOTAL_NUMBER_OF_THEILER_STAGE_ARRAY",
//				                         "TOTAL_NUMBER_OF_AGE_ARRAY",
//				                         "TOTAL_NUMBER_OF_LAB_ARRAY",
//				                         "TOTAL_NUMBER_OF_SUBMISSION_DATE_ARRAY",
//				                         "TOTAL_NUMBER_OF_SAMPLE_ARRAY",
//				                         "TOTAL_NUMBER_OF_SAMPLE_DESCRIPTION_ARRAY",
//				                         "TOTAL_NUMBER_OF_SAMPLE_TITLE_ARRAY",
//				                         "TOTAL_NUMBER_OF_SAMPLE_ARRAY",
//				                         "TOTAL_NUMBER_OF_SERIES_ARRAY"};
//
//		columnNumbers = arrayDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries);
//		assertNotNull(columnNumbers);
//		assertEquals(163, Integer.parseInt(columnNumbers[0][1]));
//		assertEquals(14, Integer.parseInt(columnNumbers[2][1]));
//		assertEquals(11, Integer.parseInt(columnNumbers[4][1]));
//	}
//	
//	/*
//	 * Test method for 'db.ArrayBrowseSubmissionAssembler.getLabNameByPersonId(String)'
//	 */
////	public final void testgetLabNameByPersonId() {
////		
////		ArrayBrowseSubmissionAssembler absa = new ArrayBrowseSubmissionAssembler();
////		String labName = absa.getLabNameByPersonId("15");
////		assertNotNull(labName);
////		assertEquals("Cincinnati Children's Hospital Research Foundation", labName);
////	}
//
//}
