/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.entities.submission.LockingInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class EditAssemblerUtilTest {
	
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
	 * Test method for {@link gmerg.assemblers.EditAssemblerUtil#getLockingInfo(java.lang.String)}.
	 */
	@Test
	public final void testGetLockingInfo() {
//		fail("Not yet implemented"); // TODO
		String submissionId = "9999997";
		LockingInfo lockingInfo = EditAssemblerUtil.getLockingInfo(submissionId);
		assertNotNull(lockingInfo);
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditAssemblerUtil#lockSubmission(java.lang.String, gmerg.entities.User)}.
	 */
	@Test
	public final void testLockSubmissionStringUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditAssemblerUtil#relockSubmission(java.lang.String, gmerg.entities.User)}.
	 */
	@Test
	public final void testRelockSubmissionStringUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link gmerg.assemblers.EditAssemblerUtil#unlockSubmissions(gmerg.entities.User)}.
	 */
	@Test
	public final void testUnlockSubmissionsUser() {
		fail("Not yet implemented"); // TODO
	}

}
