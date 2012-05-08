/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import gmerg.utils.table.DataItem;
/**
 * @author xingjun
 *
 */
public class ImageMatrixAssemblerTest {
//	ImageMatrixAssembler ima;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		String gene = "wnt4";
		param.put("gene", gene);
//		ima = new ImageMatrixAssembler(param);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
//		ima = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.ImageMatrixAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public final void testRetrieveData() {
//		fail("Not yet implemented"); // TODO
//		ima.getParam("gene");
//		System.out.println("gene: " + ima.getParam("gene"));
		String symbol = "wnt4";
//		ArrayList imageInfo = ima.getData(symbol);
//		assertNotNull(imageInfo);
//		assertEquals(32, imageInfo.size());
//		assertEquals(12, ((Object[])imageInfo.get(0)).length);
	}

	/**
	 * Test method for {@link gmerg.assemblers.ImageMatrixAssembler#getInsituSubmissionImagesByGene(java.lang.String)}.
	 */
	@Test
	public final void testGetInsituSubmissionImagesByGene() {
//		fail("Not yet implemented"); // TODO
		String symbol = "wnt4";
//		ArrayList rawImageInfo = ima.getInsituSubmissionImagesByGene(symbol);
//		assertNotNull(rawImageInfo);
//		assertEquals(54, rawImageInfo.size());
//		assertEquals("GUDMAP:7155", ((Object[])rawImageInfo.get(0))[0]);
//		assertEquals("17", ((Object[])rawImageInfo.get(0))[1]);
//		assertEquals("wholemount", ((Object[])rawImageInfo.get(0))[2]);
//		assertEquals("GUDMAP:8209", ((Object[])rawImageInfo.get(53))[0]);
//		assertEquals("23", ((Object[])rawImageInfo.get(53))[1]);
//		assertEquals("section", ((Object[])rawImageInfo.get(53))[2]);
		
//		String symbol = "cd24a";
//		ArrayList rawImageInfo = ima.getInsituSubmissionImagesByGene(symbol);
//		assertNotNull(rawImageInfo);
//		assertEquals(54, rawImageInfo.size());
//		assertEquals("GUDMAP:7155", ((Object[])rawImageInfo.get(0))[0]);
//		assertEquals("17", ((Object[])rawImageInfo.get(0))[1]);
//		assertEquals("wholemount", ((Object[])rawImageInfo.get(0))[2]);
//		assertEquals("GUDMAP:8209", ((Object[])rawImageInfo.get(53))[0]);
//		assertEquals("23", ((Object[])rawImageInfo.get(53))[1]);
//		assertEquals("section", ((Object[])rawImageInfo.get(53))[2]);
	}

	/**
	 * Test method for {@link gmerg.assemblers.ImageMatrixAssembler#reconstructInsituSubmissionImageInfo(java.util.ArrayList)}.
	 */
	@Test
	public final void testReconstructInsituSubmissionImageInfo() {
//		fail("Not yet implemented"); // TODO
		String symbol = "wnt4";	
//		ArrayList rawImageInfo = ima.getInsituSubmissionImagesByGene(symbol);
//		Object[][][] imageInfo = ima.reconstructInsituSubmissionImageInfo(rawImageInfo);
//		assertNotNull(imageInfo);
//		assertEquals(32, imageInfo.length);
//		assertEquals(12, imageInfo[0].length);
//		assertEquals(6, imageInfo[0][0].length);
//		assertEquals(true, imageInfo[0][0][0]);
//		assertEquals("http://www.gudmap.org/fcgi-bin/iipsrv.fcgi?FIF=/opt/MAWWW/Public/html/mrciip/projects/gudmap/uq_181006/GUDMAP_LOCAL_7389/images/Wnt4_TS171_WMISH100706.jpg.256.pyr.tif&QLT=100&RGN=0,0,1,1&HEI=240&CVT=jpeg", 
//				imageInfo[0][0][1]);
//		assertEquals("GUDMAP:7155", imageInfo[0][0][2]);
//		assertEquals("TS17", imageInfo[0][0][3]);
//		assertEquals(false, imageInfo[0][0][4]);
//		assertEquals("Wnt4_TS171_WMISH100706.jpg", imageInfo[0][0][5]);
//
//		assertNotNull(imageInfo[0][1]);
//		assertEquals(false, imageInfo[0][1][0]);
//		assertEquals("", imageInfo[0][1][2]);
//		assertEquals("", imageInfo[0][1][2]);
//		assertNotNull(imageInfo[0][5]);
//		assertEquals(false, imageInfo[0][5][0]);
//		assertEquals("", imageInfo[0][5][2]);
//		assertEquals("", imageInfo[0][5][2]);
//		assertNotNull(imageInfo[0][8]);
//		assertEquals(false, imageInfo[0][8][0]);
//		assertEquals("", imageInfo[0][8][2]);
//		assertEquals("", imageInfo[0][8][2]);
//		
//		assertNotNull(imageInfo[0][6]);
//		assertEquals(true, imageInfo[0][6][0]);
//		assertEquals("GUDMAP:7156", imageInfo[0][6][2]);
//		assertEquals("TS23", imageInfo[0][6][3]);
//		assertEquals(false, imageInfo[0][6][4]);
//		assertEquals("Wnt4_EK3_IHC260106.jpg", imageInfo[0][6][5]);
//		assertEquals(true, imageInfo[31][6][0]);
//		assertEquals("GUDMAP:8209", imageInfo[31][6][2]);
//		assertEquals("TS23", imageInfo[31][6][3]);
//		assertEquals(false, imageInfo[31][6][4]);
//		assertEquals("Wnt4_Wt1_EK4_IHC260906.jpg", imageInfo[31][6][5]);
//
//		symbol = "Sigirr";	
//		rawImageInfo = ima.getInsituSubmissionImagesByGene(symbol);
//		imageInfo = ima.reconstructInsituSubmissionImageInfo(rawImageInfo);
//		assertNotNull(imageInfo);
//		assertEquals(13, imageInfo.length);
//		assertEquals(12, imageInfo[0].length);
//		assertEquals(6, imageInfo[0][6].length);
//		assertEquals(true, imageInfo[0][6][0]);
//		assertEquals("http://www.gudmap.org/fcgi-bin/iipsrv.fcgi?FIF=/opt/MAWWW/Public/html/mrciip/projects/gudmap/uq_281107/GUDMAP_LOCAL_9239/images/1753_E15_1a_280807.jpg.256.pyr.tif&QLT=100&RGN=0,0,1,1&HEI=240&CVT=jpeg", 
//				imageInfo[0][6][1]);
//		assertEquals("GUDMAP:9057", imageInfo[0][6][2]);
//		assertEquals("TS23", imageInfo[0][6][3]);
//		assertEquals(false, imageInfo[0][6][4]);
//		assertEquals("1753_E15_1a_280807.jpg", imageInfo[0][6][5]);
	}

}
