package gmerg.assemblers;

import static org.junit.Assert.*;

//import java.sql.Connection;
import gmerg.assemblers.ISHBrowseAssembler;

import java.util.ArrayList;

//import gmerg.db.DBHelper;
//import gmerg.db.ISHDevDAO;
//import gmerg.db.MySQLDAOFactory;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ISHBrowseAssemblerTest extends TestCase {
	
    private ISHBrowseAssembler ishBrowseAssembler = null;
    private ArrayList allEntries = null;
    private int[] totalNumbers = null;
    private int totalNumberOfSubmissions = 0;
	private String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION",
            "TOTAL_NUMBER_OF_GENE_SYMBOL",
            "TOTAL_NUMBER_OF_THEILER_STAGE",
            "TOTAL_NUMBER_OF_GIVEN_STAGE",
            "TOTAL_NUMBER_OF_LAB",
            "TOTAL_NUMBER_OF_SUBMISSION_DATE",
            "TOTAL_NUMBER_OF_ASSAY_TYPE",
            "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
            "TOTAL_NUMBER_OF_IMAGE",
            "TOTAL_NUMBER_OF_SEX",
//            "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL",
            "TOTAL_NUMBER_OF_PROBE_NAME",
//            "TOTAL_NUMBER_OF_ANTIBODY_NAME",
//            "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL",
            "TOTAL_NUMBER_OF_GENOTYPE",
            "TOTAL_NUMBER_OF_PROBE_TYPE"
            };
    
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		// initialise an ISHBrowseAssembler
		ishBrowseAssembler = new ISHBrowseAssembler();
	}

	@After
	public void tearDown() throws Exception {

		// release the assembler
		ishBrowseAssembler = null;
	}

	@Test
	public final void testGetData() {
//		fail("Not yet implemented"); // TODO
//		allEntries = ishBrowseAssembler.retrieveData(0, true, 0, 10);
//		assertNotNull(allEntries);
//		String[] record = (String[])allEntries.get(5);
//		String id = (String)record[0];
//		assertEquals("GUDMAP:7", id);
//		String symbol = (String)record[1];
//		assertEquals("1110034A24Rik", symbol);
//		String ts = (String)record[2];
//		assertEquals("17", ts);
//		String age = (String)record[3];
//		assertEquals("10.5dpc", age);
//		String lab = (String)record[4];
//		assertEquals("Little", lab);
//		String specimen = (String)record[5];
//		assertEquals("whole mount", specimen);
//		String assay = (String)record[6];
//		assertEquals("ISH", assay);
//		String date = (String)record[7];
//		assertEquals("2005-06-06", date);
//		String name = (String)record.[9];
//		assertEquals("GUDMAPno7", name);
//		String sex = (String)record[8];
//		assertEquals("", sex);
//		String probeName = (String)record[9];
//		assertEquals("MGI:3067724", probeName);
//		String genotype = (String)record[10];
//		assertEquals("Wild Type", genotype);
//		String probeType = (String)record[11];
//		assertEquals("RNA", probeType);
//		String thumbnail = (String)record[12];
//		assertEquals("http://genex.hgu.mrc.ac.uk/Gudmap/images/gx/uq_0605/GUDMAP_105/thumbnails/image1.jpeg", thumbnail);
	}

	@Test
	public final void testGetTotals() {
//		fail("Not yet implemented"); // TODO
//		totalNumbers = ishBrowseAssembler.getTotals();
//		assertNotNull(totalNumbers);
//		int len = totalNumbers.length;
//		assertEquals(13, len);
//		assertEquals(3012, totalNumbers[0]); // id
//		assertEquals(1806, totalNumbers[1]); // symbol
//		assertEquals(9, totalNumbers[2]); // ts
//		assertEquals(15, totalNumbers[3]); // age
//		assertEquals(3, totalNumbers[4]); // lab
//		assertEquals(17, totalNumbers[5]); // date
//		assertEquals(1, totalNumbers[6]); // assay
//		assertEquals(7, totalNumbers[7]); // specimen
//		assertEquals(4, totalNumbers[8]); // sex
//		assertEquals(1851, totalNumbers[9]); // probe name
//		assertEquals(3, totalNumbers[10]); // genotype
//		assertEquals(1, totalNumbers[11]); // probe type
//		assertEquals(3012, totalNumbers[12]); // thumbnail
	}

	@Test
	public final void testGetSubmissionNumber() {
//		fail("Not yet implemented"); // TODO
//		totalNumberOfSubmissions = ishBrowseAssembler.getSubmissionNumber();
//		assertEquals(3012, totalNumberOfSubmissions);
	}

}
