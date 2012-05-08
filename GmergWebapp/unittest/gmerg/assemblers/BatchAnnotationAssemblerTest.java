package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.BatchAnnotationAssembler;
import gmerg.entities.User;
import gmerg.entities.submission.ish.ISHBatchSubmission;
import gmerg.entities.submission.ish.ISHBatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BatchAnnotationAssemblerTest {
	
	private BatchAnnotationAssembler baa;

	@Before
	public void setUp() throws Exception {
		baa = new BatchAnnotationAssembler();
	}

	@After
	public void tearDown() throws Exception {
		baa = null;
	}

	@Test
	public final void testGetLatestBatchByPI() {
//		fail("Not yet implemented"); // TODO
//		String pi = "3";
//		String userName = "kylie_g";
//		String batchId = baa.getLatestBatchByPI(pi, userName);
//		assertNotNull(batchId);
//		assertEquals("111", batchId);
	}

	@Test
	public final void testGetSubmissionByBatchId() {
//		fail("Not yet implemented"); // TODO
		String batchId = "110";
//		ISHBatchSubmission[] ibs = baa.getSubmissionByBatchId(batchId);
//		assertNotNull(ibs);
//		assertEquals(48, ibs.length);
//		assertEquals("GUDMAP:10143", ibs[2].getSubmissionID());
//		assertEquals("definer@localhost", ibs[2].getLastModifiedBy());
//		assertEquals(0, ibs[2].getLockedByPrivilege());
//		assertEquals("2008-07-15 11:36:57.0", ibs[2].getLastModifiedDate());
//		assertEquals(false, ibs[2].isLocked());

		batchId = "156";
//		ibs = baa.getSubmissionByBatchId(batchId);
//		assertNotNull(ibs);
//		assertEquals(2, ibs.length);
//		assertEquals("99999951", ibs[0].getSubmissionID());
//		assertEquals("kylie_g", ibs[0].getLastModifiedBy());
//		assertEquals(0, ibs[0].getLockedByPrivilege());
//		assertEquals("2008-07-29 17:33:44.0", ibs[0].getLastModifiedDate());
//		assertEquals(false, ibs[0].isLocked());
	}
	
	@Test
	public final void testAddSubmission() {
		String batchId = "181";
		User user = new User();
		user.setUserId(1);
		user.setUserName("derekh");
		user.setUserPi(35);
		String pi = "35";
//		int addedSubmissionNumber = baa.addSubmission(batchId, user, pi);
//		assertEquals(1, addedSubmissionNumber);
	}
	
	@Test
	public final void testDuplicateSubmission() {
		User user = new User();
		user.setUserId(7);
		user.setUserName("kylie_g");
		
		// temp sub has no expression
//		String submissionId = "9999992";
//		int duplicatedSumissionNumber = baa.duplicateSubmission(submissionId, user);
//		assertEquals(1, duplicatedSumissionNumber);
		
		// temp sub has expression but no pattern/location with note
//		String submissionId = "9999991";
//		int duplicatedSumissionNumber = baa.duplicateSubmission(submissionId, user);
//		assertEquals(1, duplicatedSumissionNumber);
		
		// temp sub has expression and pattern but no location
//		String submissionId = "99999910";
//		int duplicatedSumissionNumber = baa.duplicateSubmission(submissionId, user);
//		assertEquals(1, duplicatedSumissionNumber);
		
		// temp sub has expression and pattern and location
//		String submissionId = "9999996";
//		int duplicatedSumissionNumber = baa.duplicateSubmission(submissionId, user);
//		assertEquals(1, duplicatedSumissionNumber);
	}
	
	@Test
	public final void testDeleteTemporarySubmission() {
		User user = new User();
		user.setUserName("kylie_g");
		
		// delete empty submission id array (empty array or array with empty value)
//		String[] submissionIds = {"", ""};
//		int submissionDeleted = baa.deleteTemporarySubmission(submissionIds, user);
//		assertEquals(0, submissionDeleted);
		
		// delete single submission without expression
//		String[] submissionIds = {"9999992"};
//		int submissionDeleted = baa.deleteTemporarySubmission(submissionIds, user);
//		assertEquals(1, submissionDeleted);
		
		// delete submissions without expression
//		String[] submissionIds = {"99999914", "99999913"};
//		int submissionDeleted = baa.deleteTemporarySubmission(submissionIds, user);
//		assertEquals(1, submissionDeleted);
		
		// delete single submission with expression and note
//		String[] submissionIds = {"99999910"};
//		int submissionDeleted = baa.deleteTemporarySubmission(submissionIds, user);
//		assertEquals(1, submissionDeleted);

		// delete submissions with expression
//		String[] submissionIds = {"9999998", "9999999"};
//		int submissionDeleted = baa.deleteTemporarySubmission(submissionIds, user);
//		assertEquals(1, submissionDeleted);
		
		
		// delete submissions with expression and without expression
//		String[] submissionIds = {"9999996", "9999997"};
//		int submissionDeleted = baa.deleteTemporarySubmission(submissionIds, user);
//		assertEquals(1, submissionDeleted);
	}
	
	@Test
	public final void testCompleteBatch() {
		User user = new User();
		user.setUserName("derekh");
		String batchId = "189";
//		int batchCompleted = baa.completeBatch(batchId, user);
//		assertEquals(1, batchCompleted);
	}
	
	@Test
	public final void testGetBatchListByPI() {
		User user = new User();
		String pi = "3";
//		ISHBatch[] ishBatchList = baa.getBatchListByPI(pi, user);
//		assertNotNull(ishBatchList);
//		assertEquals(1, ishBatchList.length);
//		assertEquals("184", ishBatchList[0].getBatchID());
//		assertEquals("kylie_g", ishBatchList[0].getLastModifiedBy());

		pi = "35";
//		ishBatchList = baa.getBatchListByPI(pi, user);
//		assertNotNull(ishBatchList);
//		assertEquals(1, ishBatchList.length);
//		assertEquals("181", ishBatchList[0].getBatchID());
//		assertEquals("sara_i", ishBatchList[0].getLastModifiedBy());
	}
	
	@Test
	public final void testAddBatch() {
		User user = new User();
		user.setUserName("kylie_g");
		String pi = "3";
//		int batchAdded = baa.addBatch(pi, user);
//		assertEquals(1, batchAdded);
	}
	
	@Test
	public final void testGetUnlockedBatchList() {
		User user = new User();
		user.setUserName("kylie_g");
		user.setUserId(7);
		String pi = "3";
		String[] batchIds = {"189"};
		String[] unlockedBatches = baa.getUnlockedBatchList(batchIds, user);
		assertNotNull(unlockedBatches);
//		assertEquals(1, unlockedBatches.length);
//		assertEquals("189", unlockedBatches[0]);
	}
	
	@Test
	public final void testDeleteBatch() {
		User user = new User();
		user.setUserName("kylie_g");
		String batchId = "182";
//		int batchDeleted = baa.deleteBatch(batchId, user);
//		assertEquals(1, batchDeleted);
	}
	
	@Test
	public final void testGetBatchStatus() {
		String batchId = "212";
		int batchStatus = baa.getBatchStatus(batchId);
		assertEquals(3, batchStatus);
		
		batchId = "131";
		batchStatus = baa.getBatchStatus(batchId);
		assertEquals(2, batchStatus);

		batchId = "214";
		batchStatus = baa.getBatchStatus(batchId);
		assertEquals(2, batchStatus);
	}

}
