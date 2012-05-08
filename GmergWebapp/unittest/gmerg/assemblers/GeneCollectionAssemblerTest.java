//package gmerg.assemblers;
//
//import gmerg.db.ArrayDAO;
//import gmerg.db.DBHelper;
//import gmerg.db.MySQLDAOFactory;
//import gmerg.entities.submission.GeneCollectionItem;
//
//import java.sql.Connection;
//
//import junit.framework.TestCase;
//
//public class GeneCollectionAssemblerTest extends TestCase {
//
//	ArrayDAO arrayDAO = null;
//	Connection conn = null;
//
//	public GeneCollectionAssemblerTest(String arg0) {
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
//	 * Test method for 'gmerg.assemblers.GeneAssembler.getData(String, String)'
//	 */
//	public final void testGetData() {
//		// TODO Auto-generated method stub
//		
//		GeneCollectionAssembler gca = new GeneCollectionAssembler();
//		String[] geneSymbols = {"1110051M20Rik", "1110054O05Rik"};
//		GeneCollectionItem[] geneCollectionItems = gca.getData(geneSymbols, null, null, null);
//		assertNotNull(geneCollectionItems);
//		assertEquals("1110051M20Rik", geneCollectionItems[0].getGeneSymbol());
//		assertEquals("0", geneCollectionItems[0].getNumberOfRelatedSubmissionsISH());
//		assertEquals("159", geneCollectionItems[0].getNumberOfRelatedSubmissionsArray());
//
//		assertEquals(false, geneCollectionItems[1].getSelected());
//		assertEquals("1110054O05Rik", geneCollectionItems[1].getGeneSymbol());
//		assertEquals("0", geneCollectionItems[1].getNumberOfRelatedSubmissionsISH());
//		assertEquals("144", geneCollectionItems[1].getNumberOfRelatedSubmissionsArray());
//	}
//
//}
