/**
 * 
 */
package gmerg.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.entities.GenelistInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class MySQLGenelistDAOImpTest {

	private Connection conn;
	private GenelistDAO genelistDAO;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		conn = DBHelper.getDBConnection();
		genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
        DBHelper.closeJDBCConnection(conn);
        genelistDAO = null;
	}

	/**
	 * Test method for {@link gmerg.db.MySQLGenelistDAOImp#getAnalysisGenelistPlatformId(java.lang.String)}.
	 */
	@Test
	public final void testGetAnalysisGenelistPlatformId() {
//		fail("Not yet implemented"); // TODO
		String genelistId = "1";
		String platformId = genelistDAO.getAnalysisGenelistPlatformId(genelistId);
		assertNotNull(platformId);
		assertEquals("GPL1261", platformId);

		genelistId = "2";
		assertNotNull(platformId);
		assertNotSame("GPL6246", platformId);
	}

	/**
	 * Test method for {@link gmerg.db.MySQLGenelistDAOImp#getGeneSymbolByAnalysisGenelistId(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetGeneSymbolByAnalysisGenelistId() {
//		fail("Not yet implemented"); // TODO
		String genelistId = "1";
		String platformId = "GPL1261";
		String[] symbols = 
			genelistDAO.getGeneSymbolByAnalysisGenelistId(genelistId, platformId);
		assertNotNull(symbols);
		assertEquals(5708, symbols.length);
		assertEquals("Lsamp", symbols[0]);
		assertEquals("1110002E22Rik", symbols[5707]);
		
		genelistId = "2";
		platformId = "GPL1261";
		symbols = 
			genelistDAO.getGeneSymbolByAnalysisGenelistId(genelistId, platformId);
		assertNotNull(symbols);
		assertEquals(189, symbols.length);
		assertEquals("Ivl", symbols[0]);
		assertEquals("A2bp1", symbols[188]);
		
		genelistId = "1";
		platformId = "GPL6246";
		symbols = 
			genelistDAO.getGeneSymbolByAnalysisGenelistId(genelistId, platformId);
		assertNull(symbols);

		genelistId = "2";
		platformId = "GPL6246";
		symbols = 
			genelistDAO.getGeneSymbolByAnalysisGenelistId(genelistId, platformId);
		assertNull(symbols);
	}
	
	@Test
	public final void testGetAllAnalysisGenelists() {
		ArrayList<GenelistInfo> genelists = genelistDAO.getAllAnalysisGenelistsWithFolderIds();
		assertNotNull(genelists);
		assertEquals(5, genelists.size());
		
		assertEquals("1", genelists.get(0).getGenelistId());
		assertEquals(1, genelists.get(0).getFolderIds().size());
		assertEquals("_1100", genelists.get(0).getFolderIds().get(0));
		assertEquals("aronow/november_2007/aronow_071107/data/15_7599Kclusters_splitted.xls", genelists.get(0).getFilename());
		
		assertEquals("4", genelists.get(3).getGenelistId());
		assertEquals(1, genelists.get(3).getFolderIds().size());
		assertEquals("_40", genelists.get(3).getFolderIds().get(0));
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/arrayData/aronow/november_2007/aronow_071107/data/GudmapBook2_110707.xls", genelists.get(3).getFilename());
	}
	
	@Test
	public final void testGetAnnotationByProbeSetIds() {
		ArrayList<String> probeSetList = new ArrayList<String>();
		probeSetList.add("1439878_at");
		
	}
	
	@Test
	public final void testRetrieveClustersForGenelist() {
		String genelistId = "1";
		ArrayList<GenelistInfo> genelists = genelistDAO.retrieveClustersForGenelist(genelistId);
		assertNotNull(genelists);
		assertEquals(15, genelists.size());
		assertEquals("1", genelists.get(0).getGenelistId());
		assertEquals("", genelists.get(0).getFilename());
		assertEquals(378, genelists.get(0).getNumberOfEntries());
		assertEquals("Metanephric Mesenchyme", genelists.get(0).getTitle());

		assertEquals("15", genelists.get(14).getGenelistId());
		assertEquals("", genelists.get(14).getFilename());
		assertEquals(473, genelists.get(14).getNumberOfEntries());
		assertEquals("Cortical & Nephrogenic Interstitium", genelists.get(14).getTitle());
	}

}
