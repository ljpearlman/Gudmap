package gmerg.assemblers;

import gmerg.assemblers.ExpressionDetailAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ExpressionDetail;

import java.sql.Connection;

import junit.framework.TestCase;

public class ExpressionDetailAssemblerTest extends TestCase {

	ISHDAO ishDAO = null;
	Connection conn = null;
	
	public ExpressionDetailAssemblerTest(String arg0) {
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
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
	}

	/*
	 * Test method for 'gmerg.assemblers.ExpressionDetailAssembler.getData(String, String)'
	 */
	public final void testGetData() {
		// TODO Auto-generated method stub
		ExpressionDetailAssembler eda = new ExpressionDetailAssembler();
		
//		ExpressionDetail expressionDetail = eda.getData("GUDMAP:5306", "EMAP:8234");
//		assertNotNull(expressionDetail);
//		assertEquals("mouse.organ system.visceral organ.renal/urinary system.ureter", expressionDetail.getComponentDescription());
//		assertEquals("GUDMAP:5306", expressionDetail.getSubmissionId());
//		assertEquals("TS23", expressionDetail.getStage());
//		assertEquals("not detected", expressionDetail.getStrength());
//		assertEquals("not applicable", expressionDetail.getPattern());

		ExpressionDetail expressionDetail = eda.getData("GUDMAP:5306", "EMAP:8219");
		assertNotNull(expressionDetail);
		assertEquals("mouse.organ system.visceral organ.renal/urinary system.bladder", expressionDetail.getComponentDescription());
		assertEquals("GUDMAP:5306", expressionDetail.getSubmissionId());
		assertEquals("bladder", expressionDetail.getComponentName());
		assertEquals("TS23", expressionDetail.getStage());
		assertEquals("present", expressionDetail.getPrimaryStrength());
		assertEquals("not applicable", expressionDetail.getPattern()[0].getPattern());

	}

}
