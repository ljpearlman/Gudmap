package gmerg.utils;

import static org.junit.Assert.*;

import gmerg.entities.submission.array.MasterTableInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 
 */

/**
 * @author xingjun
 *
 */
public class DbUtilTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link gmerg.utils.DbUtility#retrieveGeneSymbolByMGIId(java.lang.String)}.
	 */
	@Test
	public final void testRetrieveGeneSymbolByMGIId() {
//		fail("Not yet implemented"); // TODO
		String mgiId = "";
		String symbol = DbUtility.retrieveGeneSymbolByMGIId(mgiId);
		assertNotNull(symbol);
		
		mgiId = "MGI:1890077";
		symbol = DbUtility.retrieveGeneSymbolByMGIId(mgiId);
		assertNotNull(symbol);
		assertEquals("Foxo1", symbol);
	}
	
	@Test
	public final void testGetAllMasterTablesInfo() {
		MasterTableInfo[] masterTableList =  DbUtility.getAllMasterTablesInfo();
		
		assertNotNull(masterTableList);
		assertEquals(4, masterTableList.length);
		
		String id = masterTableList[0].getId();
		assertEquals("3_0", id);
		
		String masterId = masterTableList[0].getMasterId();
		assertEquals("3", masterId);
		
		String masterTableTitle = masterTableList[0].getTitle();
		assertEquals("Developing_kidney (ST_1)", masterTableTitle);

		id = masterTableList[2].getId();
		assertEquals("4_2", id);
		
		String desc = masterTableList[0].getDescription();
		assertEquals("50 samples Developing_kidney ST 1.0", desc);
		desc = masterTableList[1].getDescription();
		assertEquals("Developing_kidney MOE 430", desc);
		desc = masterTableList[2].getDescription();
		assertEquals("Lower Urinary tract MOE 430", desc);
		desc = masterTableList[3].getDescription();
		assertEquals("Reproductive system MOE 430", desc);
	}

	@Test
	public final void testGetMasterTablePlatformId() {
		String masterTableId = "3";
		String platformId = DbUtility.getMasterTablePlatformId(masterTableId);
		
		assertNotNull(platformId);
		assertEquals("GPL6246", platformId);
	}
	
	@Test
	public final void testRetrieveGenelistProbeIds() {
		String genelistId = "2";
		String platformId = "GPL1261";
		ArrayList probeIds = DbUtility.retrieveGenelistProbeIds(genelistId, platformId);
		assertNotNull(probeIds);
		assertEquals(223, probeIds.size());
		
		genelistId = "2";
		platformId = "GPL6246";
		probeIds = DbUtility.retrieveGenelistProbeIds(genelistId, platformId);
		assertNotNull(probeIds);
		assertEquals(178, probeIds.size());
	}
	
	@Test
	public final void testGetGenelistPlatformId() {
		String genelistId = "2";
		String platformId = DbUtility.getGenelistPlatformId(genelistId);
		assertNotNull(platformId);
		assertEquals("GPL1261", platformId);
		
		genelistId = "3";
		platformId = DbUtility.getGenelistPlatformId(genelistId);
		assertNotNull(platformId);
		assertEquals("GPL1261", platformId);
	}
	
//	@Test
//	public final void testCheckGudmapIds() {
//		// test if all ids exist
//		int userPrivilege = 2;
//		String gudmapId = "GUDMAP:10200";
//		ArrayList<String> candidateGudmapIds = new ArrayList<String>();
//		candidateGudmapIds.add(gudmapId);
////		HashSet checkResultHashSet = DbUtility.checkGudmapIds(candidateGudmapIds, userPrivilege);
//		ArrayList checkResult = DbUtility.checkGudmapIds(candidateGudmapIds);
//		assertNotNull(checkResult);
//		
//		// test if no param passed in
//		candidateGudmapIds = null;
//		checkResult = DbUtility.checkGudmapIds(candidateGudmapIds);
//		assertNull(checkResult);
//
//		// test if some of the ids not exists
//		candidateGudmapIds = new ArrayList<String>();
//		gudmapId = "GUDMAP:10200";
//		candidateGudmapIds.add(gudmapId);
//		gudmapId = "GUDMAP:5";
//		candidateGudmapIds.add(gudmapId);
//		checkResult = DbUtility.checkGudmapIds(candidateGudmapIds);
//		assertNotNull(checkResult);
//		assertEquals(1, checkResult.size());
//		assertEquals(true, checkResult.contains("gudmap:5"));
//	}
	
	@Test
	public final void testCheckProbeSetIds() {
		// test if all ids exist
		String probeSetId = "1415671_at";
		String platformId = "GPL1261";
		ArrayList<String> candidateProbeSetIds = new ArrayList<String>();
		candidateProbeSetIds.add(probeSetId);
		ArrayList checkResult = DbUtility.checkProbeSetIds(candidateProbeSetIds, platformId);
		assertNull(checkResult);
		
		// test if no param passed in
		candidateProbeSetIds = null;
		checkResult = DbUtility.checkProbeSetIds(candidateProbeSetIds, platformId);
		assertNull(checkResult);

		// test if some of the ids not exists
		probeSetId = "1415671_at";
		candidateProbeSetIds = new ArrayList<String>();
		candidateProbeSetIds.add(probeSetId);
		probeSetId = "22";
		candidateProbeSetIds.add(probeSetId);
		checkResult = DbUtility.checkProbeSetIds(candidateProbeSetIds, platformId);
		assertNotNull(checkResult);
		assertEquals(1, checkResult.size());
		assertEquals(true, checkResult.contains("22"));
	}
	
//	@Test
//	public final void testGetRelevantSymbols() {
//		// test if all ids exist
//		String symbol = "cd24a";
//		ArrayList<String> candidateSymbols = new ArrayList<String>();
//		candidateSymbols.add(symbol);
//		ArrayList checkResult = DbUtility.checkGeneSymbols(candidateSymbols);
//		assertNull(checkResult);
//		
//		// test if no param passed in
//		candidateSymbols = null;
//		checkResult = DbUtility.checkGeneSymbols(candidateSymbols);
//		assertNull(checkResult);
//
//		// test if some of the ids not exists
//		symbol = "wnt4";
//		candidateSymbols = new ArrayList<String>();
//		candidateSymbols.add(symbol);
//		symbol = "22";
//		candidateSymbols.add(symbol);
//		checkResult = DbUtility.checkGeneSymbols(candidateSymbols);
//		assertNotNull(checkResult);
//		assertEquals(1, checkResult.size());
//		assertEquals(true, checkResult.contains("22"));
//		assertEquals(false, checkResult.contains("wnt4"));
//	}

	@Test
	public final void testRetrieveImageIdsByGeneSymbol() {
		String symbol = "cd24a";
		ArrayList imageIds = DbUtility.retrieveImageIdsByGeneSymbol(symbol);
		assertNotNull(imageIds);
		assertEquals(16, imageIds.size());
		assertEquals("CD24_E115_10x.jpg", imageIds.get(0));
	}
	
	@Test
	public final void testCheckImageIds() {
		String imageId = "7159_Wnt4_TS21B1_WMISH100706.jpg";
		ArrayList<String> candidateImageIds = new ArrayList<String>();
		candidateImageIds.add(imageId);
		ArrayList validImageIds = DbUtility.checkImageIds(candidateImageIds);
		assertNotNull(validImageIds);
		assertEquals(1, validImageIds.size());
		assertEquals("", validImageIds.get(0));
	}
}
