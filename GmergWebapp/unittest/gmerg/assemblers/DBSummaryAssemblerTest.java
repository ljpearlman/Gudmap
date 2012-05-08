package gmerg.assemblers;

import gmerg.assemblers.DBSummaryAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.summary.DBSummary;

import java.sql.Connection;

import junit.framework.TestCase;

public class DBSummaryAssemblerTest extends TestCase {

//	private ISHDAO ishDAO = null;
//	private Connection conn = null;
	private DBSummaryAssembler dsa;
	
	
	public DBSummaryAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
//		conn = DBHelper.getDBConnection();
//		ishDAO = MySQLDAOFactory.getISHDAO(conn);
		dsa = new DBSummaryAssembler();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
//		DBHelper.closeJDBCConnection(conn);
//		ishDAO = null;
		dsa = null;
	}

	/*
	 * Test method for 'gmerg.assemblers.DBSummaryAssembler.getData()'
	 */
	public final void testGetData() {
		// TODO Auto-generated method stub

//		DBSummaryAssembler dsa = new DBSummaryAssembler();
		DBSummary dbSummary = dsa.getData();
		assertNotNull(dbSummary);
//		assertEquals("tamdhu", dbSummary.getDatabaseServer());
//		assertEquals("GUDMAP", dbSummary.getProject());
//		assertEquals("171", dbSummary.getTotAvailArraySubs());
//		assertEquals("30-Mar-2007", dbSummary.getLastEditorUpdate());
		
		// test for IHC data
		assertEquals("0", dbSummary.getTotalAvailableSubmissionsIHC());
		assertEquals("10", dbSummary.getTotalEditSubmissionsIHC());
	}

}
