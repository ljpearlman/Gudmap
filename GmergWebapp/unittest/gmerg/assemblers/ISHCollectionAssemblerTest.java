//package gmerg.assemblers;
//
//import gmerg.db.DBHelper;
//import gmerg.db.ISHDAO;
//import gmerg.db.MySQLDAOFactory;
////import gmerg.entities.submission.ish.ISHBrowseSubmission;
//import gmerg.entities.submission.ish.ISHBrowseSubmissionData;
//
//import java.sql.Connection;
//
//import junit.framework.TestCase;
//
//public class ISHCollectionAssemblerTest extends TestCase {
//
//	ISHDAO ishDAO = null;
//	Connection conn = null;
//	
//	public ISHCollectionAssemblerTest(String arg0) {
//		super(arg0);
//	}
//
//	protected void setUp() throws Exception {
//		super.setUp();
//		// create a dao
//		conn = DBHelper.getDBConnection();
//		ishDAO = MySQLDAOFactory.getISHDAO(conn);
//	}
//
//	protected void tearDown() throws Exception {
//		super.tearDown();
//		DBHelper.closeJDBCConnection(conn);
//		ishDAO = null;
//	}
//
//	/*
//	 * Test method for 'gmerg.assemblers.ISHCollectionAssembler.getSubmissionCollection(String[], String[], String, String)'
//	 */
//	public final void testGetSubmissionCollection() {
//		// TODO Auto-generated method stub
//		
//		ISHCollectionAssembler ica = new ISHCollectionAssembler();
//		String[] submissionIds = {"GUDMAP:1", "GUDMAP:5", "GUDMAP:3"};
////		ISHBrowseSubmission[] submissions = ica.getSubmissionCollection(submissionIds, null, null, null);
////		assertNotNull(submissionDatas);
////		assertEquals(3, submissions.length);
////		assertEquals("Olfml3", submissions[1].getGeneSymbol());
//
//		ISHBrowseSubmissionData submissionData = ica.getData(submissionIds, null, null, null);
//		assertNotNull(submissionData);
//		assertEquals(3, submissionData.getBroseSubmissionData().length);
//		assertEquals("Olfml3", submissionData.getBroseSubmissionData()[1].getGeneSymbol());
//	}
//
//}
