/**
 * 
 */
package gmerg.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class MySQLArrayDevDAOImpTest {

	private Connection conn;
	private ArrayDevDAO arrayDevDAO;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		conn = DBHelper.getDBConnection();
		arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
        DBHelper.closeJDBCConnection(conn);
        arrayDevDAO = null;
	}

	/**
	 * Test method for {@link gmerg.db.MySQLArrayDevDAOImp#getRelevantGudmapIds(java.util.ArrayList, int)}.
	 */
//	@Test
//	public final void testGetRelevantGudmapIdsArrayListInt() {
//		ArrayList<String> candidateGudmapIds = new ArrayList<String>();
//		candidateGudmapIds.add("GUDMAP:2187");
//		candidateGudmapIds.add("GUDMAP:11567");
//		candidateGudmapIds.add("GUDMAP:6151");
//		candidateGudmapIds.add("GUDMAP:5487");
//		candidateGudmapIds.add("GUDMAP:6246");
//		candidateGudmapIds.add("GUDMAP:9910");
//		candidateGudmapIds.add("GUDMAP:8206");
//		candidateGudmapIds.add("GUDMAP:10715");
//		candidateGudmapIds.add("GUDMAP:98");
//		candidateGudmapIds.add("GUDMAP:99");
//		candidateGudmapIds.add("GUDMAP:100");
//		candidateGudmapIds.add("GUDMAP:107");

//		int userPrivilege = 2;
//		ArrayList relevatGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, userPrivilege);
//		assertNotNull(relevatGudmapIds);
//		assertEquals(3, relevatGudmapIds.size());
//		assertEquals("GUDMAP:9910", relevatGudmapIds.get(relevatGudmapIds.size()-1));
//
//		userPrivilege = 3;
//		relevatGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, userPrivilege);
//		assertNotNull(relevatGudmapIds);
//		assertEquals(10, relevatGudmapIds.size());
//		assertEquals("GUDMAP:9910", relevatGudmapIds.get(relevatGudmapIds.size()-1));
//		
//		userPrivilege = 4;
//		relevatGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, userPrivilege);
//		assertNotNull(relevatGudmapIds);
//		assertEquals(11, relevatGudmapIds.size());
//		assertEquals("GUDMAP:11567", relevatGudmapIds.get(relevatGudmapIds.size()-1));
//		
//		userPrivilege = 5;
//		relevatGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, userPrivilege);
//		assertNotNull(relevatGudmapIds);
//		assertEquals(11, relevatGudmapIds.size());
//		assertEquals("GUDMAP:11567", relevatGudmapIds.get(relevatGudmapIds.size()-1));
//		
//		userPrivilege = 6;
//		relevatGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, userPrivilege);
//		assertNotNull(relevatGudmapIds);
//		assertEquals(12, relevatGudmapIds.size());
//		assertEquals("GUDMAP:11567", relevatGudmapIds.get(relevatGudmapIds.size()-1));
//		
//		userPrivilege = 7;
//		relevatGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, userPrivilege);
//		assertNotNull(relevatGudmapIds);
//		assertEquals(12, relevatGudmapIds.size());
//		assertEquals("GUDMAP:11567", relevatGudmapIds.get(relevatGudmapIds.size()-1));
//	}
	
//	@Test
//	public final void testGetRelevantGeneSymbol() {
//		ArrayList<String> candidateSymbols = new ArrayList<String>();
//		candidateSymbols.add("Aebp2");
//		candidateSymbols.add("Aff3");
//		candidateSymbols.add("BG072301");
//		candidateSymbols.add("Aqp1");
//		candidateSymbols.add("Clu");
//		
//		int userPrivilege = 2;
//		ArrayList relevantGeneSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, userPrivilege);
//		assertNotNull(relevantGeneSymbols);
//		assertEquals(4, relevantGeneSymbols.size());
//		assertEquals("Clu", relevantGeneSymbols.get(relevantGeneSymbols.size()-1));
//		
//		userPrivilege = 3;
//		relevantGeneSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, userPrivilege);
//		assertNotNull(relevantGeneSymbols);
//		assertEquals(5, relevantGeneSymbols.size());
//		assertEquals("Aebp2", relevantGeneSymbols.get(0));
//		
//		userPrivilege = 4;
//		relevantGeneSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, userPrivilege);
//		assertNotNull(relevantGeneSymbols);
//		assertEquals(5, relevantGeneSymbols.size());
//		assertEquals("Clu", relevantGeneSymbols.get(relevantGeneSymbols.size()-1));
//		
//		userPrivilege = 5;
//		relevantGeneSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, userPrivilege);
//		assertNotNull(relevantGeneSymbols);
//		assertEquals(5, relevantGeneSymbols.size());
//		assertEquals("Clu", relevantGeneSymbols.get(relevantGeneSymbols.size()-1));
//		
//		userPrivilege = 6;
//		relevantGeneSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, userPrivilege);
//		assertNotNull(relevantGeneSymbols);
//		assertEquals(5, relevantGeneSymbols.size());
//		assertEquals("Clu", relevantGeneSymbols.get(relevantGeneSymbols.size()-1));
//		
//		userPrivilege = 7;
//		relevantGeneSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, userPrivilege);
//		assertNotNull(relevantGeneSymbols);
//		assertEquals(5, relevantGeneSymbols.size());
//		assertEquals("Clu", relevantGeneSymbols.get(relevantGeneSymbols.size()-1));
//	}
	
	@Test
	public final void testGetRelevantImageIds() {
		ArrayList<String> candidateImageIds = new ArrayList<String>();
		candidateImageIds.add("7159_Wnt4_TS21B1_WMISH100706.jpg");
		
		int userPrivilege = 2;
		ArrayList relevantImageIds = arrayDevDAO.getRelevantImageIds(candidateImageIds, userPrivilege);
		assertNotNull(relevantImageIds);
		assertEquals(1, relevantImageIds.size());
		assertEquals("7159_Wnt4_TS21B1_WMISH100706.jpg", relevantImageIds.get(0));
	}

}
