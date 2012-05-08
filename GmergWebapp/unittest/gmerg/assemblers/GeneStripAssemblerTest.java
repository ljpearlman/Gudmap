/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gmerg.assemblers.GeneStripAssembler;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class GeneStripAssemblerTest {
	private GeneStripAssembler gsa;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
//		type 1: ts23 section submissions exist
		String[] geneSymbols = new String[] {"wnt4"};
		
//		type 2: ts23 wholemount submissions exist
//		String[] geneSymbols = new String[] {"wnt4"};
		
//		type 3: ts21 section submissions exist
//		String[] geneSymbols = new String[] {"wnt4"}; 
		
//		type 4: ts21 wholemount submissions exist
//		String[] geneSymbols = new String[] {"wnt4"};
		
//		type 5: ts20 section submissions exist
//		String[] geneSymbols = new String[] {"wnt4"};
		
//		type 6: ts20 wholemount submissions exist
//		String[] geneSymbols = new String[] {"wnt4"}; 
		
//		type 7: other stages section submissions exist
//		String[] geneSymbols = new String[] {"wnt4"};
		
//		type 8: other stages wholemount submissions exist
//		String[] geneSymbols = new String[] {"wnt4"}; 
		
		gsa = new GeneStripAssembler(null, null);
//		gsa.setParams();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		gsa = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.GeneStripAssembler#getData(java.util.ArrayList)}.
	 */
	@Test
	public final void testGetData() {
//		fail("Not yet implemented"); // TODO
////////////////////////////////////////////////////////////////
//      type 1: ts23 section submissions exist
//      type 2: ts23 wholemount submissions exist
//      type 3: ts24 section submissions exist
//      type 4: ts24 wholemount submissions exist
//      type 5: ts22 section submissions exist
//      type 6: ts22 wholemount submissions exist
//      type 7: smaller than ts22 section submissions exist
//      type 8: smaller than ts22 wholemount submissions exist
//      type 9: larger than ts24 section submissions exist
//      type 10: larger than ts24 wholemount submissions exist
//      ====== below tests are based modified algorithm =======		
//      type 11: ts21 section submissions exist
//      type 12: ts21 wholemount submissions exist
//      type 13: ts20 section submissions exist
//      type 14: ts20 wholemount submissions exist
//      type 15: section submissions at other stages exist
//      type 16: wholemount submissions at other stages exist
///////////////////////////////////////////////////////////////
		/**===========================================*/
        /** type 1: ts23 section submissions exist */
		/**===========================================*/
		String symbol = "wnt4"; 
		System.out.println("type1. data for : " + symbol);
		ArrayList<String> symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		ArrayList geneStrip = gsa.getData(symbolList);
		ArrayList geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		ArrayList geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		// 1- symbol - string
		assertEquals("wnt4", geneStripItem.get(0).toString()); // type 1: ts23 section submissions exist
		
		// 2 - synonyms - string
		assertEquals("Wnt-4", geneStripItem.get(1).toString()); // type 1
		
		// 3 - developmental stages - ts17-28; number
		String[] stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS17-23", stageAndSubmissionNumber[0]);// type 1: ts23 section submissions exist
		assertEquals("9", stageAndSubmissionNumber[1]);// type 1: ts23 section submissions exist
		
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/uq_181006/GUDMAP_LOCAL_7390/thumbnails/Wnt4_EK1_IHC260106.jpg", 
				geneStripItem.get(4).toString());// type 1: ts23 section submissions exist
		
		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
        /** type 2: ts23 wholemount submissions exist */
		/**===========================================*/
		symbol = "sox10";
		System.out.println("type2. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("sox10", geneStripItem.get(0).toString()); // type 2: ts23 wholemount submissions exist
		
		// 2 - synonyms - string
		assertEquals("Sox21", geneStripItem.get(1).toString()); // type 2: ts23 wholemount submissions exist
		
		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS22-23", stageAndSubmissionNumber[0]); // type 2: ts23 wholemount submissions exist
		assertEquals("8", stageAndSubmissionNumber[1]); // type 2: ts23 wholemount submissions exist

		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/harvard_060206/GUDMAP_LOCAL_5547/thumbnails/050608iWMks_056.jpg", 
		geneStripItem.get(4).toString()); // type 2: ts23 wholemount submissions exist

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
        /** type 3: ts24 section submissions exist */
		/**===========================================*/
		symbol = "Agt";
		System.out.println("type3. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("Agt", geneStripItem.get(0).toString()); // type 3
		
		// 2 - synonyms - string
		assertEquals("Aogen, Serpina8", geneStripItem.get(1).toString()); // type 3
		
		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS20-27", stageAndSubmissionNumber[0]); // type 3
		assertEquals("5", stageAndSubmissionNumber[1]); // type 3

		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/hihs_010208/GUDMAP_LOCAL_9368/thumbnails/Agt_gd12_ovary_10x.jpg", 
		geneStripItem.get(4).toString()); // type 3

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
        /** type 4: ts24 wholemount submissions exist */
		/**===========================================*/
//		String symbol = ""; // did not find - 10/11/2008
		// 1- symbol - string
		// 2 - synonyms - string
		// 3 - developmental stages - ts17-28; number
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		// 6 - number of diseases - integer
		// 7 - microarray expression profile - ???
		// 8 - gene set - integer

		
		/**===========================================*/
        /** type 5: ts22 section submissions exist */
		/**===========================================*/
		symbol = "Ogn";
		System.out.println("type5. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("Ogn", geneStripItem.get(0).toString()); // type 5
		
		// 2 - synonyms - string
		assertEquals("OG, 3110079A16Rik, mimican, SLRR3A, mimecan", geneStripItem.get(1).toString()); // type 5
		
		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS22", stageAndSubmissionNumber[0]); // type 5
		assertEquals("1", stageAndSubmissionNumber[1]); // type 5
		
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/hihs_220607/GUDMAP_LOCAL_8413/thumbnails/Ogn_gd14_testis_10x.jpg", 
		geneStripItem.get(4).toString()); // type 5

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
        /** type 6: ts22 wholemount submissions exist */
		/**===========================================*/
//		String symbol = ""; // did not find - 11/11/2008
		// 1- symbol - string
		// 2 - synonyms - string
		// 3 - developmental stages - ts17-28; number
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		// 6 - number of diseases - integer
		// 7 - microarray expression profile - ???
		// 8 - gene set - integer
		
		
		/**===========================================*/
        /** type 7: smaller than ts22 section submissions exist */
		/**===========================================*/
//		String symbol = ""; // did not find - 11/11/2008
		// 1- symbol - string
		// 2 - synonyms - string
		// 3 - developmental stages - ts17-28; number
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		// 6 - number of diseases - integer
		// 7 - microarray expression profile - ???
		// 8 - gene set - integer
		
		
		/**===========================================*/
        /** type 8: smaller than ts22 wholemount submissions exist */
		/**===========================================*/
		symbol = "Punc";
		System.out.println("type8. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("Punc", geneStripItem.get(0).toString()); // type 8
		
		// 2 - synonyms - string
		assertEquals("WI-14920, 2810401C09Rik", geneStripItem.get(1).toString()); // type 8

		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS17-21", stageAndSubmissionNumber[0]); // type 8
		assertEquals("5", stageAndSubmissionNumber[1]); // type 8

		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/uq_250608/GUDMAP_LOCAL_10072/thumbnails/1902_TS20T_2_250907.jpg", 
		geneStripItem.get(4).toString()); // type 8

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
        /** type 9: larger than ts24 section submissions exist */
		/**===========================================*/
		symbol = "EspL1";
		System.out.println("type9. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("EspL1", geneStripItem.get(0).toString()); // type 9
		
		// 2 - synonyms - string
		assertEquals("ESP1, SSE, separase, PRCE", geneStripItem.get(1).toString()); // type 9
		
		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS27", stageAndSubmissionNumber[0]); // type 9
		assertEquals("1", stageAndSubmissionNumber[1]); // type 9
		
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/hihs_300707/GUDMAP_LOCAL_8455/thumbnails/EspL1_PND2_ovary_10x.jpg", 
		geneStripItem.get(4).toString()); // type 9

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
        /** type 10: larger than ts24 wholemount submissions exist */
		/**===========================================*/
//		String symbol = ""; // did not find - 11/11/2008
		// 1- symbol - string
		// 2 - synonyms - string
		// 3 - developmental stages - ts17-28; number
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		// 6 - number of diseases - integer
		// 7 - microarray expression profile - ???
		// 8 - gene set - integer
		
		
		/**===========================================*/
		/** type 11: ts21 section submissions exist */
		/**===========================================*/
//		String symbol = ""; // did not find - 20/11/2008
		// 1- symbol - string
		// 2 - synonyms - string
		// 3 - developmental stages - ts17-28; number
		// 4 - in situ expression profile - ?????
		// 5 - in situ expression image - image url
		// 6 - number of diseases - integer
		// 7 - microarray expression profile - ???
		// 8 - gene set - integer
		
		
		/** below are tests on modified algorithm */
		/**===========================================*/
		/** type 12: ts21 wholemount submissions exist */
		/**===========================================*/
		symbol = "Ace2";
		System.out.println("type12. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("Ace2", geneStripItem.get(0).toString()); // type 12
		
		// 2 - synonyms - string
		assertEquals("2010305L05Rik", geneStripItem.get(1).toString()); // type 12
		
		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS21-27", stageAndSubmissionNumber[0]); // type 12
		assertEquals("6", stageAndSubmissionNumber[1]); // type 12

		// 4 - in situ expression profile - ?????
		double[] expressionProfile = (double[])geneStripItem.get(3);
		assertNotNull(expressionProfile);
		assertEquals(7, expressionProfile.length);
		for (int i=0;i<expressionProfile.length;i++)
			System.out.println("profile " + i + ":" + expressionProfile[i]);
		
		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/uq_080408/GUDMAP_LOCAL_9698/thumbnails/Ace2_TS21TV_260208.jpg",
		geneStripItem.get(4).toString()); // type 12

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
		/** type 13: ts20 section submissions exist */
		/**===========================================*/
		symbol = "kit";
		System.out.println("type13. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("kit", geneStripItem.get(0).toString()); // type 13
		
		// 2 - synonyms - string
		assertEquals("CD117, c-KIT, Steel Factor Receptor, Dominant white spotting, belly-spot, Tr-kit, SCO1, SCO5, SOW3, Gsfsco1, Gsfsco5, Gsfsow3", geneStripItem.get(1).toString()); // type 13

		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS17-27", stageAndSubmissionNumber[0]); // type 13
		assertEquals("5", stageAndSubmissionNumber[1]); // type 13

		// 4 - in situ expression profile - ?????
		expressionProfile = (double[])geneStripItem.get(3);
		assertNotNull(expressionProfile);
		assertEquals(7, expressionProfile.length);
		for (int i=0;i<expressionProfile.length;i++)
			System.out.println("profile " + i + ":" + expressionProfile[i]);

		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/hihs_010208/GUDMAP_LOCAL_9377/thumbnails/Kit_gd12_ovary_10x.jpg",
		geneStripItem.get(4).toString()); // type 13

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
		/** type 14: ts20 wholemount submissions exist */
		/**===========================================*/
		symbol = "Clu";
		System.out.println("type14. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("Clu", geneStripItem.get(0).toString()); // type 14
		
		// 2 - synonyms - string
		assertEquals("Sgp-2, complement lysis inhibitor, SP-40, testosterone repressed prostate message, Apolipoprotein J, ApoJ, Cli, Sugp-2, Sgp2, sgp2, D14Ucla3", geneStripItem.get(1).toString()); // type 14
		
		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS17-20", stageAndSubmissionNumber[0]); // type 14
		assertEquals("2", stageAndSubmissionNumber[1]); // type 14

		// 4 - in situ expression profile - ?????
		expressionProfile = (double[])geneStripItem.get(3);
		assertNotNull(expressionProfile);
		assertEquals(7, expressionProfile.length);
		for (int i=0;i<expressionProfile.length;i++)
			System.out.println("profile " + i + ":" + expressionProfile[i]);

		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/uq_0605/GUDMAP_119/thumbnails/image1.jpg",
		geneStripItem.get(4).toString()); // type 14

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
		/** type 15: other section submissions exist */
		/**===========================================*/
		symbol = "ren1";
		System.out.println("type15. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("ren1", geneStripItem.get(0).toString()); // type 15
		
		// 2 - synonyms - string
		assertEquals("Ren1d, Rnr, Ren, Rn-1, Ren-A, Ren-1, Ren1c", geneStripItem.get(1).toString()); // type 15

		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS22-27", stageAndSubmissionNumber[0]); // type 15
		assertEquals("4", stageAndSubmissionNumber[1]); // type 15

		// 4 - in situ expression profile - ?????
		expressionProfile = (double[])geneStripItem.get(3);
		assertNotNull(expressionProfile);
		assertEquals(7, expressionProfile.length);
		for (int i=0;i<expressionProfile.length;i++)
			System.out.println("profile " + i + ":" + expressionProfile[i]);

		// 5 - in situ expression image - image url
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/hihs_300707/GUDMAP_LOCAL_8453/thumbnails/Ren1_gd14_testis_10x.jpg",
		geneStripItem.get(4).toString()); // type 15

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
		
		
		/**===========================================*/
		/** type 16: other wholemount submissions exist */
		/**===========================================*/
		symbol = "Sry";
		System.out.println("type16. data for : " + symbol);
		symbolList =  new ArrayList<String>();
		symbolList.add(symbol);
//		geneStrip = gsa.getData(symbolList);
		geneStrip = null;
		assertNotNull(geneStrip);
		assertEquals(1, geneStrip.size());
		geneStripItem = (ArrayList)geneStrip.get(0);
		assertNotNull(geneStripItem);
		
		// 1- symbol - string
		assertEquals("Sry", geneStripItem.get(0).toString()); // type 16
		
		// 2 - synonyms - string
		assertEquals("Tdf, Tdy", geneStripItem.get(1).toString()); // type 16

		// 3 - developmental stages - ts17-28; number
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("TS17", stageAndSubmissionNumber[0]); // type 16
		assertEquals("1", stageAndSubmissionNumber[1]); // type 16

		// 4 - in situ expression profile - ?????
		expressionProfile = (double[])geneStripItem.get(3);
		assertNotNull(expressionProfile);
		assertEquals(7, expressionProfile.length);
		for (int i=0;i<expressionProfile.length;i++)
			System.out.println("profile " + i + ":" + expressionProfile[i]);

		// 5 - in situ expression image - image url
		stageAndSubmissionNumber = (String[])(geneStripItem.get(2));
		assertNotNull(stageAndSubmissionNumber);
		assertEquals("http://gudmap.hgu.mrc.ac.uk/Gudmap/images/gx/uq_061206_061106/GUDMAP_LOCAL_7699/thumbnails/Sry_TS171_WMISH100706.jpg",
		geneStripItem.get(4).toString()); // type 16

		// 6 - number of diseases - integer
		assertEquals("number of diseases", geneStripItem.get(5).toString());
		
		// 7 - microarray expression profile - ???
		assertEquals("array expression profile", geneStripItem.get(6).toString());
		
		// 8 - gene set - integer
		assertEquals("0", geneStripItem.get(7).toString());
	}

}
