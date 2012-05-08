package gmerg.assemblers;

import gmerg.assemblers.QueryAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.array.ArraySubmission;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
import gmerg.entities.submission.ish.ISHSubmission;
import gmerg.entities.submission.ish.ISHBrowseSubmissionData;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.SubmissionData;

import java.sql.Connection;
import java.util.ArrayList;

import junit.framework.TestCase;

public class QueryAssemblerTest extends TestCase {

	ISHBrowseSubmission[] submissions;
	private String[][] columnNumbers;
	private ISHDAO ishDAO = null;
	Connection conn = null;

	public QueryAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
		conn = DBHelper.getDBConnection();
		ishDAO = MySQLDAOFactory.getISHDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// release a dao
		ishDAO = null;
		DBHelper.closeJDBCConnection(conn);
	}

	/*
	 * Test method for 'gmerg.assemblers.QueryAssembler.getDataByGene(String, String, String, String, String, String, String[], String, String)'
	 */
	public final void testgetDataByGene() {
		// TODO Auto-generated method stub

		// get data
		/* @param inputType - ignoreExpression -  
		 * @param inputString -
		 * @param stage -
		 * @param criteria -
		 * @param order -
		 * @param offset -
		 * @param rowNum
		 * @return array of ISHBrowseSubmission
		 */
//		submissions = ishDAO.getSubmissionByGeneInfo("", "", "", "", "", null, "", "");
//		submissions = ishDAO.getSubmissionByGeneInfo("symbol", "false", "Cd24a", null, "equals", null, null, null);
//		assertNotNull(submissions);
//		assertEquals("26", submissions[1].getStage());

//		submissions = ishDAO.getSubmissionByGeneInfo("symbol", "false", "Cd24a,1500005I02Rik", null, "equals", null, null, null);
//		assertNotNull(submissions);

//		submissions = ishDAO.getSubmissionByGeneInfo("all", "false", "Cd24a,1500005I02Rik", null, "equals", null, null, null);
//		assertNotNull(submissions);
		
//		submissions = ishDAO.getSubmissionByGeneInfo("all", "true", "Cd24a,,  1500005I02Rik", null, "begins", null, null, null);
//		assertNotNull(submissions);
		
//		submissions = ishDAO.getSubmissionByGeneInfo("symbol", "false", "Cd24a", null, "wildcard", null, null, null);
//		assertNotNull(submissions);

//		submissions = ishDAO.getSubmissionByGeneInfo("symbol", "false", "Cd24a , ,1500005I02Rik", null, "wildcard", null, null, null);
//		assertNotNull(submissions);
		
//		submissions = ishDAO.getSubmissionByGeneInfo("all", "false", "Cd24a", null, "wildcard", null, null, null);
//		assertNotNull(submissions);

//		submissions = ishDAO.getSubmissionByGeneInfo("all", "false", "Cd24a,1500005I02Rik", null, "begins", null, null, null);
//		assertNotNull(submissions);
		
//		String[] order = {"byGene", "ASC"};
//		submissions = ishDAO.getSubmissionByGeneInfo("symbol", "true", "cd24a", "17", "equals", order, "1", "20");
//		assertNotNull(submissions);
//		assertEquals("GUDMAP:114", submissions[0].getId());
		
//		QueryAssembler qa = new QueryAssembler();
//		ISHBrowseSubmissionData isd = qa.getDataByGene("gene", "symbol", "true", "cd24a", "17", "equals", order, "1", "20");
//		ISHBrowseSubmissionData isd = qa.getDataByGene("gene", "true", "symbol", "cd24a", "17", "equals", order, "1", "20");
//		assertNotNull(isd);

		// get total number of columns
//		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_QENE_QUERY",
//                "TOTAL_NUMBER_OF_GENE_SYMBOL_QENE_QUERY",
//                "TOTAL_NUMBER_OF_THEILER_STAGE_QENE_QUERY",
//                "TOTAL_NUMBER_OF_GIVEN_STAGE_QENE_QUERY",
//                "TOTAL_NUMBER_OF_LAB_QENE_QUERY",
//                "TOTAL_NUMBER_OF_SUBMISSION_DATE_QENE_QUERY",
//                "TOTAL_NUMBER_OF_ASSAY_TYPE_QENE_QUERY",
//                "TOTAL_NUMBER_OF_SPECIMEN_TYPE_QENE_QUERY",
//                "TOTAL_NUMBER_OF_IMAGE_QENE_QUERY"};
		
		/* @param query
		 * @param inputType
		 * @param ignoreExpression
		 * @param inputString
		 * @param stage
		 * @param criteria
		 * @return array of name and numbers
		 */
//		columnNumbers = ishDAO.getTotalNumberOfColumnsGeneQuery(allColTotalsQueries, "", "", ", ,", "", "equals");
//		columnNumbers = ishDAO.getTotalNumberOfColumnsGeneQuery(allColTotalsQueries, "symbol", "", "Cd24a", "", "equals");
//		assertNotNull(columnNumbers);
//		for (int i=0;i<columnNumbers.length;i++) {
//			System.out.print("name: " + columnNumbers[i][0] + "---");
//			System.out.println("numb: " + columnNumbers[i][1]);
//		}
//		assertEquals("4", columnNumbers[0][1]);
		
		QueryAssembler qa = new QueryAssembler();
		
//		ArrayList componentCountInfo = (ArrayList)qa.getDataByGene("anatomy", "false", "symbol", "Cd24a", null, "equals", null, null, null);
//		assertNotNull(componentCountInfo);
//		assertEquals(2, componentCountInfo.size());
//		assertEquals("mouse.limb", ((String[])componentCountInfo.get(0))[0]);
//		assertEquals("EMAP:2220", ((String[])componentCountInfo.get(1))[1]);
		
//		componentCountInfo = (ArrayList)qa.getDataByGene("anatomy", "false", "symbol", "r", "", "contains", new String[]{"byGene","ASC"}, "1", "20");
//		assertNotNull(componentCountInfo);
//		assertEquals(2, componentCountInfo.size());
//		assertEquals("mouse.limb", ((String[])componentCountInfo.get(0))[0]);
//		assertEquals("EMAP:2220", ((String[])componentCountInfo.get(1))[1]);
		
//		ISHBrowseSubmissionData ibsData =
//			(ISHBrowseSubmissionData)qa.getDataByGene("gene", "false", "symbol", "Cd24a", null, "equals", null, null, null);
//		assertNotNull(ibsData);
//		assertEquals(4, ibsData.getBroseSubmissionData().length);
//		assertEquals("GUDMAP:113", ibsData.getBroseSubmissionData()[2].getId());

		ISHBrowseSubmissionData ibsData =
			(ISHBrowseSubmissionData)qa.getDataByGene("gene", null, "symbol", "Arid3a", "20", "equals", null, null, null);
		assertNotNull(ibsData);
		assertEquals(3, ibsData.getBroseSubmissionData().length);

//		ISHBrowseSubmissionData ibsData =
//			(ISHBrowseSubmissionData)qa.getDataByGene("gene", "false", "symbol", "Arid3a", "EMAP:4587", "20", "equals", null, null, null);
//		assertNotNull(ibsData);
//		assertEquals(1, ibsData.getBroseSubmissionData().length);
//		assertEquals("GUDMAP:7035", ibsData.getBroseSubmissionData()[0].getId());

		ibsData =
			(ISHBrowseSubmissionData)qa.getDataByGene("gene", "false", "symbol", "8030497I03Rik", "EMAP:4580", "20", "equals", null, null, null);
		assertNotNull(ibsData);
		assertEquals(2, ibsData.getBroseSubmissionData().length);
//		assertEquals("GUDMAP:7035", ibsData.getBroseSubmissionData()[0].getId());

		ArrayList componentCountInfo = (ArrayList)qa.getDataByGene("anatomy", "false", "symbol", "8030497I03Rik", "20", "equals", null, null, null);
		assertNotNull(componentCountInfo);
		assertEquals(9, componentCountInfo.size());
//		System.out.println(((String[])componentCountInfo.get(1))[4]);
		assertEquals("mouse.organ system.visceral organ.reproductive system.female reproductive system.mesonephros of female", ((String[])componentCountInfo.get(1))[0]);
		assertEquals("EMAP:28910", ((String[])componentCountInfo.get(1))[1]);
		assertEquals("EMAP:28872", ((String[])componentCountInfo.get(0))[1]);
		assertEquals("1", ((String[])componentCountInfo.get(0))[4]);
//		assertEquals("", ((String[])componentCountInfo.get(1))[4]);
	}

	/*
	 * Test method for 'gmerg.assemblers.QueryAssembler.getComponentQueryData(String, String, String[], String, String)'
	 */
	public final void testGetComponentQueryData() {
		// TODO Auto-generated method stub
//		submissions = ishDAO.getSubmissionByComponentId("2095", "", null, null, null);
//		submissions = ishDAO.getSubmissionByGeneInfo("symbol", "false", "Cd24a", null, "equals", null, null, null);
//		assertNotNull(submissions);
//		assertEquals("26", submissions[1].getStage());
		
		submissions = ishDAO.getSubmissionByComponentId("EMAP:2095", "17", null, null, null);
		assertNotNull(submissions);
		assertEquals(5, submissions.length);
	}

	/*
	 * Test method for 'gmerg.assemblers.QueryAssembler.getComponentsQueryData(String[], String[], String, String)'
	 */
	public final void testGetComponentsQueryData() {
		QueryAssembler qa = new QueryAssembler();
//		String[] components ={"6668", "8219"};
//		ISHBrowseSubmission[] ibs = qa.getComponentsQueryData(components, null, null, null);
//		assertNotNull(ibs);
//		assertEquals(973, ibs.length);
		String[] components ={"4580"};
		ISHBrowseSubmission[] ibs = qa.getDataByComponentIdList(components, null, null, null);
		assertNotNull(ibs);
		assertEquals(296, ibs.length);
	}

	/*
	 * Test method for 'gmerg.assemblers.QueryAssembler.getSubmissionQueryData(String)'
	 */
	public final void testGetSubmissionQueryData() {
		// TODO Auto-generated method stub

		QueryAssembler qa = new QueryAssembler();

		//##########################
		SubmissionData submissionData = qa.getSubmissionQueryData("123",null);
		assertNotNull(submissionData);
		assertEquals(0, submissionData.getSubmissionType());

//		submissionData = qa.getSubmissionQueryData("123D");
//		assertNotNull(submissionData);
//		assertEquals(1, submissionData.getSubmissionType());
		
//		submissionData = qa.getSubmissionQueryData("d:123");
//		assertNotNull(submissionData);
//		assertEquals(1, submissionData.getSubmissionType());
		
//		submissionData = qa.getSubmissionQueryData("gudmap:123");
//		assertNotNull(submissionData);
//		assertEquals(1, submissionData.getSubmissionType());
		
		submissionData = qa.getSubmissionQueryData("GUDMAP:7125", null);
		assertNotNull(submissionData);
		assertEquals(1, submissionData.getSubmissionType());
		assertNotNull(submissionData.getSubmission());
		assertEquals("GSM148652", ((ArraySubmission)(submissionData.getSubmission())).getSample().getGeoID());
	}
	
}
