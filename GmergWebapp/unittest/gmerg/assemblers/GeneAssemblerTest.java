package gmerg.assemblers;

import gmerg.assemblers.GeneAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.Gene;

import java.sql.Connection;
import java.util.ArrayList;

import junit.framework.TestCase;

public class GeneAssemblerTest extends TestCase {

	ISHDAO ishDAO = null;
	Connection conn = null;
	private GeneAssembler ga;
	
	public GeneAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
//		conn = DBHelper.getDBConnection();
//		ishDAO = MySQLDAOFactory.getISHDAO(conn);
//		ga = new GeneAssembler();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
//		DBHelper.closeJDBCConnection(conn);
//		ishDAO = null;
		ga = null;
	}

	/*
	 * Test method for 'gmerg.assemblers.GeneAssembler.getData(String, String)'
	 */
	public final void testGetData() {
		// TODO Auto-generated method stub
		
		// get gene info
//		Gene geneInfo = ishDAO.findGeneInfoBySymbol("Cd24a");
//		assertEquals("CD24a antigen" ,geneInfo.getName());

//		Gene geneInfo = ishDAO.findGeneInfoBySymbol("0610005C13Rik");
//		if (geneInfo == null) { // might not find the gene
//			geneInfo = new Gene();
//			geneInfo.setSymbol("0610005C13Rik");
//		}
//		if (null == geneInfo.getName() || geneInfo.getName().equals("")) {
//			String geneName = ishDAO.findGeneNameBySymbolArray("0610005C13Rik");
//			assertEquals("RIKEN cDNA 0610005C13 gene", geneName);
//		}
//		if (null == geneInfo.getMgiAccID() || geneInfo.getMgiAccID().equals("")) {
//			ArrayList mgiInfo = ishDAO.findMGIInfoByProbeSetIdArray("1434906_at");
//			assertEquals("1918911", ((String[])mgiInfo.get(0))[0]);
//		}
//		if(null == geneInfo.getEntrezID() || geneInfo.getEntrezID().equals("")) {
//			ArrayList entrezInfo = ishDAO.findEntrezInfoByProbeSetIdArray("1434906_at");
//			assertNotNull(entrezInfo);
//			assertEquals("71661", ((String[])entrezInfo.get(0))[0]);
//		}
//		
//		if(null == geneInfo.getRefSeqID() || geneInfo.getRefSeqID().equals("")) {
//			ArrayList refseqInfo = ishDAO.findRefseqInfoByProbeSetIdArray("1434906_at");
//			assertNull(refseqInfo);
////			assertEquals("", ((String[])refseqInfo.get(0))[0]);
//		}
//		
//		// get related ish submission
////		String relatedSubmissionISH = ishDAO.findRelatedSubmissionBySymbolISH("Cd24a");
////		assertEquals("related submission ish", relatedSubmissionISH);
//		
//		// get related microarray submission
//		ArrayList relatedSubmissionArray = ishDAO.findRelatedSubmissionBySymbolArray("Cd24a");
////		assertEquals("GUDMAP:2168", ((String[])relatedSubmissionArray.get(0))[0]);
////		String[] results = (String[])relatedSubmissionArray.get(0);
////		int len = results.length;
////		for (int i=0; i<len; i++) {
////			System.out.println(results[i]);
////		}
//		assertEquals("1437502_x_at", ((String[])relatedSubmissionArray.get(2))[1]);
//		
//		
//		// get associated probe
//		ArrayList associatedProbe = ishDAO.findRelatedMAProbeBySymbol("Cd24a");
//		assertEquals("maprobe:3832", ((String[])associatedProbe.get(0))[1]);

//		Gene geneInfo = ga.getData("1110002E23Rik", "");
//		assertNotNull(geneInfo);
//		
//		String xosomeName = geneInfo.getXsomeName();
//		assertEquals(xosomeName, "");
	}

}
