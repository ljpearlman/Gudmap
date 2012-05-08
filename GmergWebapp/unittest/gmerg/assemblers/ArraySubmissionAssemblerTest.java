package gmerg.assemblers;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.entities.submission.array.Platform;
import gmerg.entities.submission.array.Sample;
import gmerg.entities.submission.array.Series;
import gmerg.entities.submission.array.SupplementaryFile;

import java.sql.Connection;

import junit.framework.TestCase;

public class ArraySubmissionAssemblerTest extends TestCase {

	ArrayDAO arrayDAO = null;
	ISHDAO ishDAO = null;
	Connection conn = null;
	
	GeneListBrowseSubmission[] geneList = null;
	
	public ArraySubmissionAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
		conn = DBHelper.getDBConnection();
		arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		ishDAO = MySQLDAOFactory.getISHDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		arrayDAO = null;
	}

	/*
	 * Test method for 'gmerg.assemblers.ArraySubmissionAssembler.getData(String)'
	 */
	public void testGetData() {

		// get basic submission info and create submission object
		Submission submission = ishDAO.findSubmissionById("GUDMAP:7123");
		assertEquals("GUDMAP:7123", submission.getAccID());
		
		// get supplementary file info
		SupplementaryFile supplementaryFiles = arrayDAO.findSupplementaryFileInfoBySubmissionId("GUDMAP:7123");
		assertEquals("J_244G_MOE430_2.CHP", supplementaryFiles.getChpFile());
		
		// get pi info
		Person pi = ishDAO.findPIBySubmissionId("GUDMAP:7123");
		assertEquals("TCHRF 3047 3333 Burnet Ave.", pi.getAddress());
		
		// get submitter info
		Person submitter = ishDAO.findSubmitterBySubmissionId("GUDMAP:7123");
		assertEquals("TCHRF 3049 3333 Burnet Ave", submitter.getAddress());
		
		// get sample info
		Sample sample = arrayDAO.findSampleBySubmissionId("GUDMAP:7123");
		assertEquals("2.07", sample.getA_260_280());
		assertEquals("total RNA", sample.getMolecule());
		
		// get series info
		Series series = arrayDAO.findSeriesBySubmissionId("GUDMAP:7123");
		assertEquals("7", series.getNumSamples());
		
		// get platform info
		Platform platform = arrayDAO.findPlatformBySubmissionId("GUDMAP:7123");
		assertEquals("GPL1261", platform.getGeoID());
		
		// get specimen info
		Specimen specimen = ishDAO.findSpecimenBySubmissionId("GUDMAP:7123");
//		assertEquals("male", specimen.getSex());
		assertEquals("", specimen.getFixMethod());
	}

	/*
	 * Test method for 'gmerg.assemblers.ArraySubmissionAssembler.getGeneList(String, String)'
	 */
	public void testGetGeneList() {
		
		geneList =
			arrayDAO.getGeneListBrowseSubmissionsBySubmissionId("GUDMAP:7123", null, "1");
		assertNotNull(geneList);
		assertEquals("0610005C13Rik", geneList[0].getGeneSymbol());
		assertEquals("1416980_at", geneList[1].getProbeId());
		
//		String[] order = {"byProbeID", "ASC"};
//		geneList =
//			arrayDAO.getGeneListBrowseSubmissionsById("GUDMAP:7123", order, "1");
//		assertNotNull(geneList);
//		assertEquals("P", geneList[0].getDetection());

		String[] order = {"byProbeID", "DESC"};
		geneList =
			arrayDAO.getGeneListBrowseSubmissionsBySubmissionId("GUDMAP:7123", order, "2");
		assertNotNull(geneList);
		assertEquals("SymbolNA", geneList[0].getGeneSymbol());
	}

	/*
	 * Test method for 'gmerg.assemblers.ArraySubmissionAssembler.getTotalGeneListItems(String)'
	 */
	public void testGetTotalGeneListItems() {
		String totalNumber = arrayDAO.getTotalNumberOfGeneListItemsBySubmissionId("GUDMAP:7123");
		assertEquals("46848", totalNumber);
	}

}
