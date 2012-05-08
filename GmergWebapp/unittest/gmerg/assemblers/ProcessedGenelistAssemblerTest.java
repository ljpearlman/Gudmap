package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.array.ProcessedGeneListHeader;
import gmerg.entities.submission.array.SearchLink;
import junit.framework.TestCase;

public class ProcessedGenelistAssemblerTest extends TestCase {

	ArrayDAO arrayDAO = null;
	Connection conn = null;

	public ProcessedGenelistAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
		conn = DBHelper.getDBConnection();
		arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// release a dao
		arrayDAO = null;
		DBHelper.closeJDBCConnection(conn);
	}

	/*
	 * Test method for 'gmerg.assemblers.ProcessedGenelistAssembler.retrieveGenelistData(int)'
	 */
	public final void testRetrieveGenelistData() {
		// TODO Auto-generated method stub
		
//		ProcessedGenelistAssembler pga = new ProcessedGenelistAssembler();
//		Object[][] genelistItems = pga.retrieveGenelistData(2);
//		assertNotNull(genelistItems);
//		assertEquals(3022, genelistItems.length);
//		assertEquals("3.24089", String.valueOf(genelistItems[0][0]));

//		ProcessedGenelistAssembler pga = new ProcessedGenelistAssembler();
//		Object[][] genelistItems = pga.retrieveGenelistData(1, 1, false, 30, 3);
//		assertNotNull(genelistItems);
//		assertEquals(3, genelistItems.length);
//		assertEquals("11.155", String.valueOf(genelistItems[0][0]));
//
//		genelistItems = pga.retrieveGenelistData(1, 1, false, 0, 3);
//		assertNotNull(genelistItems);
//		assertEquals(948, genelistItems.length);
//		assertEquals("13.1744", String.valueOf(genelistItems[0][0]));
	}

	/*
	 * Test method for 'gmerg.assemblers.ProcessedGenelistAssembler.retrieveGenelistByComponentName(String)'
	 */
	public final void testRetrieveGenelistByComponentName() {
		// TODO Auto-generated method stub
		
//		ProcessedGenelistAssembler pga = new ProcessedGenelistAssembler();
//		ArrayList genelistHeaders = pga.retrieveGenelistByComponentName("proximal tubules");
//		assertNotNull(genelistHeaders);
//		assertEquals(1, genelistHeaders.size());

	}

	/*
	 * Test method for 'gmerg.assemblers.ProcessedGenelistAssembler.retrieveGenelistByLabName(String)'
	 */
	public final void testRetrieveGenelistByLabName() {
		// TODO Auto-generated method stub
		
//		ProcessedGenelistAssembler pga = new ProcessedGenelistAssembler();
//		ArrayList genelistHeaders = pga.retrieveGenelistByLabName("16");
//		assertNotNull(genelistHeaders);
//		assertEquals(4, genelistHeaders.size());
//		assertEquals("S_shaped_vs_glomerulus.xls", ((ProcessedGeneListHeader)genelistHeaders.get(0)).getFileName());
		
	}

	/*
	 * Test method for 'gmerg.assemblers.ProcessedGenelistAssembler.retrieveSearchLinks()'
	 */
	public final void testRetrieveSearchLinks() {
		// TODO Auto-generated method stub
		
//		ProcessedGenelistAssembler pga = new ProcessedGenelistAssembler();
//		ArrayList externalLinks = pga.retrieveSearchLinks();
//		assertNotNull(externalLinks);
//		assertEquals(7, externalLinks.size());
//		assertEquals("Kanehisa Laboratories", ((SearchLink)externalLinks.get(0)).getInstitue());
//		assertEquals("Medical Research Council", ((SearchLink)externalLinks.get(3)).getInstitue());

	}
	
	/*
	 * Test method for 'gmerg.assemblers.ProcessedGenelistAssembler.getGenelistsEntryNumber()'
	 */
	public final void testGetGenelistsEntryNumber() {
		
//		ProcessedGenelistAssembler pga = new ProcessedGenelistAssembler();
//		int total = pga.getGenelistsEntryNumber(1);
//		assertEquals(943, total);
//
//		total = pga.getGenelistsEntryNumber(2);
//		assertEquals(3022, total);
	}

}
