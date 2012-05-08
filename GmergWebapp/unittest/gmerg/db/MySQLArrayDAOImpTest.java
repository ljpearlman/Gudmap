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

import gmerg.entities.submission.Transgenic;
import gmerg.entities.HeatmapData;

/**
 * @author xingjun
 *
 */
public class MySQLArrayDAOImpTest {
	//test
	private Connection conn;
	private ArrayDAO arrayDAO;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		conn = DBHelper.getDBConnection();
		arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
        DBHelper.closeJDBCConnection(conn);
        arrayDAO = null;
	}

	/**
	 * Test method for {@link gmerg.db.MySQLArrayDAOImp#getExpressionSortedByGivenProbeSetIds(java.util.ArrayList, int, boolean, int, int)}.
	 */
	@Test
	public final void testGetExpressionSortedByGivenProbeSetIds() {
//		fail("Not yet implemented"); // TODO
		ArrayList<String> probeSetIds = new ArrayList<String>();
		String probeSetId = "1418771_a_at";
		probeSetIds.add(probeSetId);
		probeSetId = "1427001_s_at";
		probeSetIds.add(probeSetId);
		String masterTableId = "4_1";
		String genelistId = null;
//		double[][] expressionInfo = 
//			arrayDAO.getExpressionByGivenProbeSetIds(probeSetIds, 
//					masterTableId, 
//					genelistId);
//		assertNotNull(expressionInfo);
//		assertEquals(2, expressionInfo.length);

//		genelistId = "4";
//		expressionInfo = 
//			arrayDAO.getExpressionByGivenProbeSetIds(probeSetIds, 
//					masterTableId, 
//					genelistId);
//		assertNotNull(expressionInfo);
//		assertEquals(2, expressionInfo.length);
		HeatmapData expressionInfo = 
			arrayDAO.getExpressionByGivenProbeSetIds(probeSetIds, 
					masterTableId, 
					genelistId);
		assertNotNull(expressionInfo);
		assertNotNull(expressionInfo.getExpression());
		assertNotNull(expressionInfo.getMedian());
		assertNotNull(expressionInfo.getStdDev());
		
		assertEquals(2, expressionInfo.getExpression().length);
		assertEquals(2, expressionInfo.getMedian().length);
		assertEquals(2, expressionInfo.getStdDev().length);
	}
	
	@Test
	public final void testGetProbeSetIdBySymbols() {
		String[] symbols = {"Lsamp", "SymbolNA", "A830082K12Rik"};
		String platformId = "GPL1261";
		ArrayList probeSetIds = arrayDAO.getProbeSetIdBySymbols(symbols, platformId);
		assertNotNull(probeSetIds);
		assertEquals(6093, probeSetIds.size());
		assertEquals("1419990_at", probeSetIds.get(0).toString());
		assertEquals("AFFX-MUR_b2_at", probeSetIds.get(6092).toString());

		symbols = new String[]{"Lsamp", "A830082K12Rik"};
		platformId = "GPL1261";
		probeSetIds = arrayDAO.getProbeSetIdBySymbols(symbols, platformId);
		assertNotNull(probeSetIds);
		assertEquals(4, probeSetIds.size());
		assertEquals("1419990_at", probeSetIds.get(0).toString());
		assertEquals("1455636_at", probeSetIds.get(3).toString());

		symbols = new String[]{"Lsamp", "A830082K12Rik"};
		platformId = "GPL6246";
		probeSetIds = arrayDAO.getProbeSetIdBySymbols(symbols, platformId);
		assertNotNull(probeSetIds);
		assertEquals(1, probeSetIds.size());
		assertEquals("10435752", probeSetIds.get(0).toString());
	}
	
//	@Test
//	public final void testGetTransgenicInfoBySubmissionIdBak() {
//		// submission with more than one trans info rows
//		String submissionId = "GUDMAP:9424";
//		Transgenic[] transgenicInfo = arrayDAO.getTransgenicInfoBySubmissionIdBak(submissionId);
//		assertNotNull(transgenicInfo);
//		assertEquals(2, transgenicInfo.length);
//		assertEquals(43, transgenicInfo[0].getMutantOid());
//		assertEquals("", transgenicInfo[0].getGeneId());
//		assertEquals("", transgenicInfo[0].getMutatedAlleleId());
//		assertEquals("Tg(Acta2-EYFP)Jll", transgenicInfo[0].getMutatedAlleleName());
//		assertEquals("transgenic insertion", transgenicInfo[0].getMutantType());
//		assertEquals("Transgenic mouse carrying both Actg2 promoter driving EGFP expression and Aca2 promoter driving YGFP expression. PMID: 14713859", 
//				transgenicInfo[0].getNotes());
//		assertEquals("Transgenic mouse carrying both Actg2 promoter driving EGFP expression and Aca2 promoter driving YGFP expression. PMID: 14713859", 
//				transgenicInfo[0].getNotePrefix());
//		assertEquals("", transgenicInfo[0].getNoteSuffix());
//		assertEquals("", transgenicInfo[0].getNoteUrl());
//		assertEquals("", transgenicInfo[0].getNoteUrlText());
//		assertEquals(80, transgenicInfo[1].getMutantOid());
//		
//		// submission with only one trans info
//		submissionId = "GUDMAP:10715";
//		transgenicInfo = arrayDAO.getTransgenicInfoBySubmissionIdBak(submissionId);
//		assertNotNull(transgenicInfo);
//		assertEquals(1, transgenicInfo.length);
//		assertEquals(60, transgenicInfo[0].getMutantOid());
//		assertEquals("", transgenicInfo[0].getGeneId());
//		assertEquals("MGI:3769269 (provisional)", transgenicInfo[0].getMutatedAlleleId());
//		assertEquals("Tg(Sox10-HIST2H2BE/YFP*)1Sout", transgenicInfo[0].getMutatedAlleleName());
//		assertEquals("transgenic insertion", transgenicInfo[0].getMutantType());
//		assertNull(transgenicInfo[0].getNotes());
//		assertNull(transgenicInfo[0].getNotePrefix());
//		assertNull(transgenicInfo[0].getNoteSuffix());
//		assertNull(transgenicInfo[0].getNoteUrl());
//		assertNull(transgenicInfo[0].getNoteUrlText());
//		
//		// submission with gene
//		submissionId = "GUDMAP:12312";
//		transgenicInfo = arrayDAO.getTransgenicInfoBySubmissionIdBak(submissionId);
//		assertNotNull(transgenicInfo);
//		assertEquals(1, transgenicInfo.length);
//		assertEquals(65, transgenicInfo[0].getMutantOid());
//		assertEquals("", transgenicInfo[0].getGeneId());
//		assertEquals("MMRRC:012003-UCD", transgenicInfo[0].getMutatedAlleleId());
//		assertEquals("Tg(Crym-EGFP)82Gsat", transgenicInfo[0].getMutatedAlleleName());
//		assertEquals("transgenic insertion", transgenicInfo[0].getMutantType());
//		assertEquals("Crym BAC-transgenic mice were received from the GENSAT Project <a href = \"http://www.ncbi.nlm.nih.gov/projects/gensat/\"> (Gene Expression Nervous System Atlas)</a>, a NIH-funded consortium that generates transgenic BAC-EGFP reporter lines.", transgenicInfo[0].getNotes());
//		assertEquals("Crym BAC-transgenic mice were received from the GENSAT Project ", transgenicInfo[0].getNotePrefix());
//		assertEquals(", a NIH-funded consortium that generates transgenic BAC-EGFP reporter lines.", transgenicInfo[0].getNoteSuffix());
//		assertEquals("http://www.ncbi.nlm.nih.gov/projects/gensat/", transgenicInfo[0].getNoteUrl());
//		assertEquals(" (Gene Expression Nervous System Atlas)", transgenicInfo[0].getNoteUrlText());
//		
//	}
	
	@Test
	public final void testGetgetMasterTablePlatformId() {
		String masterTableId = "4_1";
		String platformId = arrayDAO.getMasterTablePlatformId(masterTableId);
		assertNotNull(platformId);
		assertEquals("GPL1261", platformId);
		
		masterTableId = "3";
		platformId = arrayDAO.getMasterTablePlatformId(masterTableId);
		assertNotNull(platformId);
		assertEquals("GPL6246", platformId);
	}
	
	@Test
	public final void testGetExpressionByGivenProbeSetIds() {
		ArrayList<String> probeSetIds = new ArrayList<String>();
		String probeSetId = "1418771_a_at";
		probeSetIds.add(probeSetId);
		probeSetId = "1427001_s_at";
		probeSetIds.add(probeSetId);
		String masterTableId = "4_1";
		String genelistId = "3";
		
		HeatmapData expressionInfo = 
			arrayDAO.getExpressionByGivenProbeSetIds(probeSetIds, masterTableId, genelistId);
		
		assertNotNull(expressionInfo);
		assertEquals(2, expressionInfo.getExpression().length);
		assertEquals(2, expressionInfo.getMedian().length);
		assertEquals(2, expressionInfo.getStdDev().length);
		assertEquals(4.128927230834961, expressionInfo.getMedian()[0]);
		
	}
}
