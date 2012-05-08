/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gmerg.assemblers.MasterTableAssembler;
import gmerg.entities.BrowseTableTitle;

/**
 * @author xingjun
 *
 */
public class MasterTableAssemblerTest {

	private MasterTableAssembler mta;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mta = new MasterTableAssembler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		mta = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.MasterTableAssembler#retrieveProbeListByGene(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testRetrieveProbeListByGene() {
//		fail("Not yet implemented"); // TODO
		String symbol = new String("wnt4");
		String platformId = new String("GPL1261");
//		ArrayList probeSetIds = mta.retrieveProbeListByGene(symbol, platformId);
		ArrayList probeSetIds = mta.retrieveProbeIdsBySymbol(symbol);
		assertNotNull(probeSetIds);
		assertEquals(2, probeSetIds.size());
		assertEquals("1450782_at", probeSetIds.get(1).toString());
	}

	/**
	 * Test method for {@link gmerg.assemblers.MasterTableAssembler#retrieveProbelistExpressions(java.util.ArrayList)}.
	 */
//	@Test
//	public final void testRetrieveProbelistExpressions() {
//		ArrayList<String> probeSetIds = new ArrayList<String>();
//		String probeSetId = new String("1441687_at");
//		probeSetIds.add(probeSetId);
//		probeSetId = new String("1450782_at");
//		probeSetIds.add(probeSetId);
//		double[][] expressions = mta.retrieveProbelistExpressions(probeSetIds);
//		assertNotNull(expressions);
//		assertEquals(2, expressions.length);
//		assertEquals(54, expressions[0].length);
//		assertEquals(8.360960006713867, expressions[0][5]);
//		assertEquals(9.273130416870117, expressions[1][4]);
//	}
	
	/**
	 * Test method for {@link germg.assemblers.MasterTableAssembler#retrieveAnnotationTitles()}
	 */
	@Test
	public final void testRetrieveAnnotationTitles() {
		BrowseTableTitle[] annotationTitles = MasterTableAssembler.retrieveAnnotaionTitles("GPL1261");
		assertNotNull(annotationTitles);
		assertEquals(19, annotationTitles.length);
		assertEquals("Systematic", annotationTitles[0].getTitle());
		assertEquals("GPL1261", annotationTitles[0].getType());
		assertEquals("Entrez Gene", annotationTitles[5].getTitle());
		assertEquals("Human ortholog symbol", annotationTitles[17].getTitle());
		assertEquals("Human ortholog Entrez gene ID", annotationTitles[18].getTitle());
		
		annotationTitles = MasterTableAssembler.retrieveAnnotaionTitles("GPL6246");
		assertNotNull(annotationTitles);
		assertEquals(22, annotationTitles.length);
		assertEquals("Systematic Name", annotationTitles[0].getTitle());

	}

	/**
	 * Test method for {@link gmerg.assemblers.MasterTableAssembler#retrieveExpressionTitles()}.
	 */
	@Test
	public final void testRetrieveExpressionTitles() {
		BrowseTableTitle[] expressionTitles = 
			MasterTableAssembler.retrieveExpressionTitles("1");
		assertNotNull(expressionTitles);
		assertEquals(54, expressionTitles.length);
		assertEquals("2", expressionTitles[0].getTitle());
		assertEquals("Master Expression", expressionTitles[0].getType());
		assertEquals("Met Mes", expressionTitles[0].getDescription());
		assertEquals("E11.5 Metanephric Mesenchyme", expressionTitles[0].getGroupName());
		assertEquals("4", expressionTitles[50].getTitle());
		assertEquals("Medu Interst", expressionTitles[50].getDescription());
		assertEquals("E15.5 Medullary interstitium", expressionTitles[50].getGroupName());
		assertEquals("1", expressionTitles[51].getTitle());
		assertEquals("E15.5 Cortical and nephrogenic interstitium", expressionTitles[51].getGroupName());
		assertEquals("3", expressionTitles[53].getTitle());
		assertEquals("E15.5 Cortical and nephrogenic interstitium", expressionTitles[53].getGroupName());
	}
	
	/**
	 * Test method for {@link gmerg.assemblers.MasterTableAssembler#retrieveProbelistAnnotations(ArrayList)}
	 *
	 */
	@Test
	public final void testRetrieveProbelistAnnotations() {
		/** a list of probe set ids */
		// platform GPL1261
		ArrayList<String> probeSetIds = new ArrayList<String>();
		String probeSetId = new String("1441687_at");
		probeSetIds.add(probeSetId);
		probeSetId = new String("1450782_at");
		probeSetIds.add(probeSetId);
		
		String[][] annotations = mta.retrieveProbelistAnnotations(probeSetIds);
//		if (annotations == null) System.out.println("mt annotation is null!");
		assertNotNull(annotations);
		assertEquals(2, annotations.length);
		assertEquals(19, annotations[0].length);
		assertEquals("1441687_at", annotations[0][0]);
		assertEquals("Wnt4", annotations[0][1]);
		assertEquals("wingless-related MMTV integration site 4", annotations[0][2]);
		assertEquals("AW047257", annotations[0][3]);
		assertEquals("NM_009523", annotations[0][4]);
		assertEquals("22417", annotations[0][5]);
		assertEquals("chr4", annotations[0][6]);
		assertEquals("+", annotations[0][7]);
		assertEquals("136854849", annotations[0][8]);
		assertEquals("136855414", annotations[0][9]);
		assertEquals("Wnt receptor signaling pathway; cell-cell signaling; development; female gamete generation; female gonad development; frizzled-2 signaling pathway; gametogenesis; hormone metabolism; organogenesis; sex differentiation; signal transduction", annotations[0][10]);
		assertEquals("extracellular region; extracellular space", annotations[0][11]);
		assertEquals("protein binding; receptor binding; signal transducer activity", annotations[0][12]);
		assertEquals("false", annotations[0][13]);
		assertEquals("A", annotations[0][14]);
		assertEquals("99.12", annotations[0][15]);
		assertEquals("2008-11-29", annotations[0][16]);
		assertEquals("WNT4", annotations[0][17]);
		assertEquals("54361", annotations[0][18]);
		assertEquals("1450782_at", annotations[1][0]);
		assertEquals("92.31", annotations[1][15]);
		
		// platform GPL6246
		probeSetIds = new ArrayList<String>();
		probeSetId = new String("10509267");
		probeSetIds.add(probeSetId);
		annotations = mta.retrieveProbelistAnnotations(probeSetIds);
		assertNotNull(annotations);
		assertEquals(1, annotations.length);
		assertEquals(22, annotations[0].length);
		assertEquals("10509267", annotations[0][0]);
		assertEquals("Wnt4", annotations[0][1]);
		assertEquals("wingless-related MMTV integration site 4", annotations[0][2]);
		assertEquals("ENSMUST00000045747", annotations[0][18]);


		
		/** all probe set ids */
//		probeSetIds = new ArrayList<String>();
//		String probeSetId = new String("");
//		probeSetIds.add(probeSetId);
//		probeSetId = new String("");
//		probeSetIds.add(probeSetId);
		
//		annotations = mta.retrieveProbelistAnnotations(probeSetIds);
//		assertNotNull(annotations);
//		assertEquals(100, annotations.length);
//		assertEquals(39, annotations[0].length);
//		assertEquals("1415670_at", annotations[0][0]);
//		assertEquals("22820", annotations[0][38]);

	}
	
}
