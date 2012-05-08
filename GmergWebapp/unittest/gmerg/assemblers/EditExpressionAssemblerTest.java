/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.EditExpressionAssembler;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.ExpressionDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class EditExpressionAssemblerTest {

    private EditExpressionAssembler eea = null;
    private ExpressionPattern[] patterns = null;
    
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		eea = new EditExpressionAssembler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		eea = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#getData(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetData() {
//		fail("Not yet implemented"); // TODO
		ExpressionDetail ed = eea.getData("GUDMAP:7386", "EMAP:29144");
		assertNotNull(ed);
		
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#getExpression(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetExpression() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#getPatterns(java.lang.String)}.
	 */
	@Test
	public final void testGetPatterns() {
//		fail("Not yet implemented"); // TODO
//		patterns = eea.getPatterns("48014");
//		assertNotNull(patterns);
//		assertEquals(2, patterns.length);
//		for (int i=0;i<patterns.length;i++) {
//			System.out.println(i + " :pattern: " + patterns[i].getPattern());
//			System.out.println(i + " :location: " + patterns[i].getLocations());
//			System.out.println(i + " :expressionId: " + patterns[i].getExpressionId());
//			System.out.println(i + " :patternId: " + patterns[i].getPatternId());
//			System.out.println(i + " :locationId: " + patterns[i].getLocationId());
//		}

//		patterns = eea.getPatterns("44509");
//		assertNotNull(patterns);
//		assertEquals(2, patterns.length);
//		for (int i=0;i<patterns.length;i++) {
//			System.out.println(i + " :pattern: " + patterns[i].getPattern());
//			System.out.println(i + " :location: " + patterns[i].getLocations());
//			System.out.println(i + " :expressionId: " + patterns[i].getExpressionId());
//			System.out.println(i + " :patternId: " + patterns[i].getPatternId());
//			System.out.println(i + " :locationId: " + patterns[i].getLocationId());
//		}

//		patterns = eea.getPatterns("46638");
//		assertNotNull(patterns);
//		assertEquals(3, patterns.length);
//		for (int i=0;i<patterns.length;i++) {
//			System.out.println(i + " :pattern: " + patterns[i].getPattern());
//			System.out.println(i + " :location: " + patterns[i].getLocations());
//			System.out.println(i + " :expressionId: " + patterns[i].getExpressionId());
//			System.out.println(i + " :patternId: " + patterns[i].getPatternId());
//			System.out.println(i + " :locationId: " + patterns[i].getLocationId());
//		}
//		patterns = eea.getPatterns("49616");
//		assertNotNull(patterns);
//		assertEquals(3, patterns.length);
//		for (int i=0;i<patterns.length;i++) {
//			System.out.println(i + " :pattern: " + patterns[i].getPattern());
//			System.out.println(i + " :location: " + patterns[i].getLocations());
//			System.out.println(i + " :expressionId: " + patterns[i].getExpressionId());
//			System.out.println(i + " :patternId: " + patterns[i].getPatternId());
//			System.out.println(i + " :locationId: " + patterns[i].getLocationId());
//		}
		
		// to find out location information of the pattern (adjacent to xxxx location)
		patterns = eea.getPatterns("69471");
		assertNotNull(patterns);
		assertEquals(1, patterns.length);
		for (int i=0;i<patterns.length;i++) {
		System.out.println(i + " :pattern: " + patterns[i].getPattern());
		System.out.println(i + " :location: " + patterns[i].getLocations());
		System.out.println(i + " :expressionId: " + patterns[i].getExpressionId());
		System.out.println(i + " :patternId: " + patterns[i].getPatternId());
		System.out.println(i + " :locationId: " + patterns[i].getLocationId());
		System.out.println(i + " :locationAPart: " + patterns[i].getLocationAPart());
		System.out.println(i + " :locationNPart: " + patterns[i].getLocationNPart());
	}
		
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#updateNote(String, String, String, String)}.
	 */
	@Test
	public final void testUpdateNote() {
	    String submissionId = "GUDMAP:7583";
	    String componentId = "EMAP:4587";
		String notesOnpage = "the signal in the vascular";
//		String notesOnpage = "the signal in the vasc";
//		String userName = "derekh";
//		boolean updated = eea.updateNote(submissionId, componentId, notesOnpage, userName);
//		assertEquals(updated, true);
		
	}
	
	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#addNote(String, String, String, String)}.
	 */
	@Test
	public final void testAddNote() {
	    String submissionId = "GUDMAP:7386";
	    String componentId = "EMAP:28873";
		String note = "this is a test record";
		String userName = "derekh";
//		boolean added = eea.addNote(submissionId, componentId, note, userName);
//		assertEquals(added, true);
	}
	
	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#deleteNote(String, String, String)}.
	 */
	@Test
	public final void testDeleteNote() {
	    String submissionId = "GUDMAP:7386";
	    String componentId = "EMAP:28873";
		String userName = "derekh";
//		boolean deleted = eea.deleteNote(submissionId, componentId, userName);
//		assertEquals(deleted, true);
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#updateAnnotation(String submissionId, String componentId, 
			String newSecondaryStrength, String oldSecondaryStrength,
			ExpressionPattern[] patternOnPage, ExpressionPattern[] patternInDB, String userName)}.
	 */
	@Test
	public final void testUpdateAnnotation() {
	    String submissionId = "GUDMAP:7386";
	    String componentId = "EMAP:29154";
	    String stage = "20";
		String userName = "derekh";
		
		///////////////////////////////   test case    ///////////////////////////////
		// NOTE: the value specified below should be consistent with data in database
		//
		// test updating annotation - pattern in db is not null & on page is not null, 
		//                                              pattern numbers are identical, 
		//                                              pattern and location values are identical
		//                                              - update second strength only - OK
		String newPrimaryStrength = "present";
		String oldPrimaryStrength = "present";
		String newSecondaryStrength = "weak";
//		String oldSecondaryStrength = "moderate";
		String oldSecondaryStrength = "weak";
		ExpressionPattern[] patternOnPage = new ExpressionPattern[3];
		ExpressionPattern[] patternInDB = new ExpressionPattern[3];

		ExpressionPattern ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(141);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(143);
		ptp.setLocations("rostral");
		patternOnPage[1] = ptp;
		patternInDB[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(142);
		ptp.setLocations("rostral");
		patternOnPage[2] = ptp;
		patternInDB[2] = ptp;

		// test updating annotation - pattern in db is not null & on page is not null,
		//                                              pattern numbers are identical, 
		//                                              pattern values are identical, location is not null in db and on page, but different
		//                                              - update location - OK
		patternOnPage = new ExpressionPattern[3];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(141);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(143);
//		ptp.setLocations("rostral");
		ptp.setLocations("dorsal");
		patternOnPage[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(142);
		ptp.setLocations("rostral");
		patternOnPage[2] = ptp;
		
		// test updating annotation - pattern in db is not null & on page is not null,
		//                                              pattern numbers are identical, 
		//                                              pattern values are identical, location in db is not null and on page is null
		//                                              -- delete location - OK
		patternOnPage = new ExpressionPattern[3];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(141);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(143);
		patternOnPage[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(142);
		ptp.setLocations("rostral");
		patternOnPage[2] = ptp;
		
		// test updating annotation - pattern in db is not null & on page is not null, 
		//                                              pattern numbers are identical, 
		//                                              pattern values are identical, location in db is null and on page is not null
		//                                              - add location - OK
		patternOnPage = new ExpressionPattern[3];
		patternInDB = new ExpressionPattern[3];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(141);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(143);
		ptp.setLocations("rostral");
		patternOnPage[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(143);
		patternInDB[1] = ptp;

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(142);
		ptp.setLocations("rostral");
		patternOnPage[2] = ptp;
		patternInDB[2] = ptp;
		
		// test updating annotation - pattern in db is not null & on page is not null, 
		//                                              pattern numbers are not identical, there are more on page
		//                                              first part of pattern values are identical (pattern values are the same)
		//                                              - add location - OK
		patternOnPage = new ExpressionPattern[3];
		patternInDB = new ExpressionPattern[2];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(141);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(142);
		ptp.setLocations("rostral");
		patternOnPage[1] = ptp;
		patternInDB[1] = ptp;

		ptp = new ExpressionPattern();
//		ptp.setExpressionId(49616);
//		ptp.setPatternId(536);
		ptp.setPattern("regional");
//		ptp.setLocationId(143);
		ptp.setLocations("rostral");
		patternOnPage[2] = ptp;
		
		// test updating annotation - pattern in db is not null & on page is not null, 
		//                                              pattern numbers are not identical, there are more in db
		//                                              first part of pattern values are identical
		//                                              - delete location (two tests) - OK
//		patternOnPage = new ExpressionPattern[2];
//		patternInDB = new ExpressionPattern[3];
//
//		ptp = new ExpressionPattern();
//		ptp.setExpressionId(49616);
//		ptp.setPatternId(536);
//		ptp.setPattern("regional");
//		ptp.setLocationId(141);
//		ptp.setLocations("medial");
//		patternOnPage[0] = ptp;
//		patternInDB[0] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setExpressionId(49616);
//		ptp.setPatternId(536);
//		ptp.setPattern("regional");
//		ptp.setLocationId(143);
//		ptp.setLocations("rostral");
//		patternOnPage[1] = ptp;
//		patternInDB[1] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setExpressionId(49616);
//		ptp.setPatternId(537);
//		ptp.setPattern("graded");
//		ptp.setLocationId(142);
//		ptp.setLocations("rostral");
//		patternInDB[2] = ptp;

		patternOnPage = new ExpressionPattern[2];
		patternInDB = new ExpressionPattern[3];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(141);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(143);
		ptp.setLocations("rostral");
		patternInDB[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(142);
		ptp.setLocations("rostral");
		patternOnPage[1] = ptp;
		patternInDB[2] = ptp;

		// test updating annotation - pattern in db is not null & on page is not null, 
		//                                              pattern numbers are not identical, there are more on page
		//                                              first part of pattern values are not identical
		//                                              -- delete and insert patterns - OK
		patternOnPage = new ExpressionPattern[3];
		patternInDB = new ExpressionPattern[2];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(144);
		ptp.setLocations("medial");
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(145);
		ptp.setLocations("rostral");
		patternInDB[1] = ptp;

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("spotted");
		ptp.setLocations("rostral");
		patternOnPage[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("regional");
		ptp.setLocations("medial");
		patternOnPage[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("regional");
		ptp.setLocations("rostral");
		patternOnPage[2] = ptp;
		
		// test updating annotation - pattern in db is not null & on page is not null, 
		//                                              pattern numbers are not identical, there are more in db
		//                                              first part of pattern values are not identical
		//                                              - delete and insert patterns - OK
		patternOnPage = new ExpressionPattern[2];
		patternInDB = new ExpressionPattern[3];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(144);
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(145);
		ptp.setLocations("rostral");
		patternOnPage[1] = ptp;

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("spotted");
		ptp.setLocations("rostral");
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("regional");
		ptp.setLocations("medial");
		patternInDB[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("regional");
		ptp.setLocations("rostral");
		patternInDB[2] = ptp;
		
		// test updating annotation - pattern in db is not null & on page is null - delete pattern - OK
		patternOnPage = null;
		patternInDB = new ExpressionPattern[2];

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(536);
		ptp.setPattern("regional");
		ptp.setLocationId(144);
		ptp.setLocations("medial");
		patternInDB[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPatternId(537);
		ptp.setPattern("graded");
		ptp.setLocationId(145);
		ptp.setLocations("rostral");
		patternInDB[1] = ptp;

		// test updating annotation - pattern in db is null & on page is not null - add pattern - OK
		patternOnPage = new ExpressionPattern[2];
		patternInDB = null;

		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("regional");
		ptp.setLocations("medial");
		patternOnPage[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setExpressionId(49616);
		ptp.setPattern("graded");
//		ptp.setLocations("rostral");
		patternOnPage[1] = ptp;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		boolean updated =
//			eea.updateAnnotation(submissionId, componentId, stage,
//					newPrimaryStrength, oldPrimaryStrength,
//					newSecondaryStrength, oldSecondaryStrength,
//					patternOnPage, patternInDB, userName);
//		assertEquals(updated, true);
		
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#addAnnotation(String submissionId, String componentId, 
			String secondaryStrength, ExpressionPattern[] patterns, String userName)}.
	 */
	@Test
	public final void testAddAnnotation() {
	    String stage = "TS20";
		String submissionId = "GUDMAP:7386";
		String userName = "derekh";
		
		/////////////////////////////////   test case  ////////////////////////////////
		// test adding annotation without pattern and with/out secondary strength - ok
	    String componentId = "EMAP:29154";
		String primaryStrength = "present";
//		secondaryStrength = "";
		String secondaryStrength = "moderate";
		
		// test adding annotation with confliction - OK
		componentId = "EMAP:29071";
		primaryStrength = "present";
//		or
//	    componentId = "EMAP:4596";
//	    primaryStrength = "not detected";
		secondaryStrength = "";
		ExpressionPattern[] patterns = null;
		
		// test adding annotation with two patterns and with secondary strength, without location - ok
//		primaryStrength = "present";
//		secondaryStrength = "weak";
//		ExpressionPattern ptp = new ExpressionPattern();
//		patterns = new ExpressionPattern[2];
//
//		ptp.setPattern("regional");
//		patterns[0] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setPattern("graded");
//		patterns[1] = ptp;
		
		// test adding annotation with one pattern and with one location - ok
//		ptp = new ExpressionPattern();
//		patterns = new ExpressionPattern[1];
//
//		ptp.setPattern("regional");
//		ptp.setLocations("medial");
//		patterns[0] = ptp;
				
		// test adding annotation with one pattern and with two locations - ok
//		ptp = new ExpressionPattern();
//		patterns = new ExpressionPattern[2];
//
//		ptp.setPattern("regional");
//		ptp.setLocations("medial");
//		patterns[0] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setPattern("regional");
//		ptp.setLocations("rostral");
//		patterns[1] = ptp;

		// test adding annotation with two patterns and each with one location - ok
//		ptp = new ExpressionPattern();
//		patterns = new ExpressionPattern[2];
//
//		ptp.setPattern("regional");
//		ptp.setLocations("medial");
//		patterns[0] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setPattern("graded");
//		ptp.setLocations("rostral");
//		patterns[1] = ptp;
		
		// test adding annotation with two patterns, one has one location and another has none - ok
//		patterns = new ExpressionPattern[2];
//
//		ptp = new ExpressionPattern();
//		ptp.setPattern("regional");
////		ptp.setLocations("medial");
//		patterns[0] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setPattern("graded");
//		ptp.setLocations("rostral");
//		patterns[1] = ptp;
		
		// test adding annotation with two patterns, one has two locations and another has one - ok
//		patterns = new ExpressionPattern[3];
//
//		ptp = new ExpressionPattern();
//		ptp.setPattern("regional");
//		ptp.setLocations("medial");
//		patterns[0] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setPattern("graded");
//		ptp.setLocations("rostral");
//		patterns[1] = ptp;
//		
//		ptp = new ExpressionPattern();
//		ptp.setPattern("regional");
//		ptp.setLocations("rostral");
//		patterns[2] = ptp;
		
//		boolean annotationAdded =
//			eea.addAnnotation(submissionId, componentId, stage, primaryStrength, secondaryStrength, patterns, userName);
//		assertEquals(annotationAdded, false);
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditExpressionAssembler#deleteAnnotation(String submissionId, String componentId, 
			String strength, ExpressionPattern[] patterns, String userName)}.
	 */
	@Test
	public final void testDeleteAnnotation() {
	    String submissionId = "GUDMAP:7386";
	    String componentId = "EMAP:29154";
		String userName = "derekh";
//		String strength = "";
		ExpressionPattern[] patterns = new ExpressionPattern[3];
//		patterns = new ExpressionPattern[2];

		ExpressionPattern ptp = new ExpressionPattern();
		ptp.setPattern("regional");
		ptp.setLocations("medial");
		patterns[0] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setPattern("graded");
		ptp.setLocations("rostral");
		patterns[1] = ptp;
		
		ptp = new ExpressionPattern();
		ptp.setPattern("regional");
		ptp.setLocations("rostral");
		patterns[2] = ptp;
		
		////////////////////////////////////////////////   test case    ////////////////////////////////////////////////////////
		// test deleting annotation without pattern - ok
		
		// test deleting annotation with pattern - ok

		// the deleting statements are the same; performed with testAddAnnotation
//		boolean annotationDeleted = eea.deleteAnnotation(submissionId, componentId, patterns, userName);
//		assertEquals(annotationDeleted, false);
		
	}
	
	@Test
	public final void testGetPatternList() {
		ArrayList patternList = eea.getPatternList();
		assertNotNull(patternList);
		assertEquals(6, patternList.size());
		assertEquals("regional", patternList.get(1));
	}
	
	@Test
	public final void testGetLocationList() {
		ArrayList locationList = eea.getLocationList();
		assertNotNull(locationList);
		assertEquals(12, locationList.size());
		assertEquals("ventral", locationList.get(11));
	}
	
}
