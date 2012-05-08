package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.assemblers.AnatomyStructureAssembler;
import gmerg.db.DBHelper;
import gmerg.db.AnatomyDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
import junit.framework.TestCase;

public class AnatomyStructureAssemblerTest extends TestCase {

	ISHBrowseSubmission[] submissions;
	Connection conn = null;
	AnatomyDAO anatomyDAO = null;

	public AnatomyStructureAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
		conn = DBHelper.getDBConnection();
		anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// release the dao
		anatomyDAO = null;
		DBHelper.closeJDBCConnection(conn);
	}

	/*
	 * Test method for 'gmerg.assemblers.AnatomyStructureAssembler.getStageRanges()'
	 */
	public final void testGetStageRanges() {
		// TODO Auto-generated method stub

		// get data
		AnatomyStructureAssembler asa = new AnatomyStructureAssembler();
		ArrayList stageRanges = asa.getStageRanges();
		assertNotNull(stageRanges);
//		for (int i=0;i<stageRanges.size();i++) {
//			System.out.println("stage: " + ((String[])stageRanges.get(i))[0]);
//		}
	}

	/*
	 * Test method for 'gmerg.assemblers.AnatomyStructureAssembler.stageRangesAreValid(String, String)'
	 */
	public final void testStageRangesAreValid() {
		// TODO Auto-generated method stub

		// get data
		AnatomyStructureAssembler asa = new AnatomyStructureAssembler();
		boolean isValid = asa.stageRangesAreValid("TS17", "TS17");
		assertTrue(isValid);
	}

	/*
	 * Test method for 'gmerg.assemblers.AnatomyStructureAssembler.buildTree(String, String)'
	 */
	public final void testBuildTree() {
		// TODO Auto-generated method stub

		// get data
		AnatomyStructureAssembler asa = new AnatomyStructureAssembler();
		ArrayList tree = asa.buildTree("TS17", "TS18", false);
		assertNotNull(tree);
	}

	/*
	 * Test method for 'gmerg.assemblers.AnatomyStructureAssembler.getISHBrowseSubmission(String[], String, String, String, String[], String, String)'
	 */
	public final void testGetISHBrowseSubmission() {
		// TODO Auto-generated method stub

		// get data
		AnatomyStructureAssembler asa = new AnatomyStructureAssembler();
		String[] components = {"2054","6250"};
		String start = "TS17";
		String end = "TS18";
		String expressionState = "present";
		
		ISHBrowseSubmission[] ibs = asa.getISHBrowseSubmission(components, start, end, expressionState, null, null, null);
		assertNotNull(ibs);
		assertEquals(9, ibs.length);
	}

}
