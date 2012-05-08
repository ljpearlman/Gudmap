/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.EditSubmissionAssembler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class EditSubmissionAssemblerTest {
	
	private EditSubmissionAssembler esa = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		esa =  new EditSubmissionAssembler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		esa = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditSubmissionAssembler#addStatusNotes(java.lang.String, gmerg.entities.submission.StatusNote[], java.lang.String)}.
	 */
//	@Test
//	public final void testAddStatusNotes() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.EditSubmissionAssembler#deleteAllStatusNotes(java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public final void testDeleteAllStatusNotes() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.EditSubmissionAssembler#updateStatusNotes(java.lang.String, gmerg.entities.submission.StatusNote[], gmerg.entities.submission.StatusNote[], java.lang.String)}.
//	 */
//	@Test
//	public final void testUpdateStatusNotes() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.EditSubmissionAssembler#deleteSelectedSubmissions(java.lang.String[])}.
//	 */
//	@Test
//	public final void testDeleteSelectedSubmissions() {
//		fail("Not yet implemented"); // TODO
//	}

	/**
	 * Test method for {@link gmerg.assemblers.EditSubmissionAssembler#updateSubmissionStatusByLabAndDate(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testUpdateSubmissionStatusByLabAndDate() {
//		fail("Not yet implemented"); // TODO
		String labId = "8";
		String submissionDate = "2008-11-25";
		String userName = "derekh";
		int status = 4;
//		boolean submissionStatusUpdated = 
//			esa.updateSubmissionStatusByLabAndDate(labId, submissionDate, status, userName);
//		assertEquals(true, submissionStatusUpdated);
	}

}
