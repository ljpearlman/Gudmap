package gmerg.assemblers;

import junit.framework.TestCase;

import java.util.ArrayList;

import gmerg.assemblers.ISHBrowseSubmissionNonRenalAssembler;

public class ISHBrowseSubmissionNonRenalAssemblerTest extends TestCase {

    private ISHBrowseSubmissionNonRenalAssembler assembler = null;
    ArrayList submissions = null;
	private int[] totalNumbers = null;
    private int totalNumberOfSubmissions = 0;
	String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_NON_RENAL",
            "TOTAL_NUMBER_OF_GENE_SYMBOL_NON_RENAL",
            "TOTAL_NUMBER_OF_THEILER_STAGE_NON_RENAL",
            "TOTAL_NUMBER_OF_GIVEN_STAGE_NON_RENAL",
            "TOTAL_NUMBER_OF_LAB_NON_RENAL",
            "TOTAL_NUMBER_OF_SUBMISSION_DATE_NON_RENAL",
            "TOTAL_NUMBER_OF_ASSAY_TYPE_NON_RENAL",
            "TOTAL_NUMBER_OF_SPECIMEN_TYPE_NON_RENAL",
            "TOTAL_NUMBER_OF_SEX_NON_RENAL",
//            "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL_NON_RENAL",
            "TOTAL_NUMBER_OF_PROBE_NAME_NON_RENAL",
//            "TOTAL_NUMBER_OF_ANTIBODY_NAME_NON_RENAL",
//            "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL_NON_RENAL",
            "TOTAL_NUMBER_OF_GENOTYPE_NON_RENAL",
            "TOTAL_NUMBER_OF_PROBE_TYPE_NON_RENAL",
            "TOTAL_NUMBER_OF_IMAGE_NON_RENAL",
            };
    
	public ISHBrowseSubmissionNonRenalAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		assembler = new ISHBrowseSubmissionNonRenalAssembler();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		assembler = null;
	}

	public final void testGetData() {
//		fail("Not yet implemented"); // TODO
//		submissions = assembler.getData(1, true, 0, 1000);
//		assertNotNull(submissions);
//		int size = submissions.size();
//		assertEquals(249, size);
	}

	public final void testGetTotals() {
//		fail("Not yet implemented"); // TODO
//		totalNumbers = assembler.getTotals();
//		assertNotNull(totalNumbers);
//		int len = totalNumbers.length;
//		assertEquals(13, len);
//		assertEquals(249, totalNumbers[0]); // id
//		assertEquals(147, totalNumbers[1]); // symbol
//		assertEquals(5, totalNumbers[2]); // ts
//		assertEquals(8, totalNumbers[3]); // age
//		assertEquals(3, totalNumbers[4]); // lab
//		assertEquals(18, totalNumbers[5]); // date
//		assertEquals(1, totalNumbers[6]); // assay
//		assertEquals(6, totalNumbers[7]); // specimen
//		assertEquals(2, totalNumbers[8]); // sex
//		assertEquals(153, totalNumbers[9]); // probe name
//		assertEquals(2, totalNumbers[10]); // genotype
//		assertEquals(1, totalNumbers[11]); // probe type
//		assertEquals(249, totalNumbers[12]); // thumbnail
	}

	public final void testGetSubmissionNumber() {
//		fail("Not yet implemented"); // TODO
//		totalNumberOfSubmissions = assembler.getSubmissionNumber();
//		assertEquals(249, totalNumberOfSubmissions);
	}

}
