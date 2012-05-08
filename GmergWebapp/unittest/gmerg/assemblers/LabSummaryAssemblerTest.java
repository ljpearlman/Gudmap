package gmerg.assemblers;

import gmerg.assemblers.LabSummaryAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
//import gmerg.entities.submission.array.ArrayBrowseSubmission;
import gmerg.entities.summary.LabSummary;
import gmerg.utils.table.DataItem;

import java.sql.Connection;
import java.util.ArrayList;

import junit.framework.TestCase;

public class LabSummaryAssemblerTest extends TestCase {

	LabSummaryAssembler lsa;
	
	public LabSummaryAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		lsa = null;
	}
	
	public void testRetrieveData() {
//		String labId = "3"; // LITTLE
//		String assayType = "ISH";
//		String submissionDate = "";
//		Object[] params = {labId, assayType, submissionDate};
//		lsa = new LabSummaryAssembler(params);
		int column = -1;
		boolean ascending = true;
		int offset = 0; 
		int num = 20;
//		DataItem[][] results = lsa.retrieveData(column, ascending, offset, num);
//		assertNotNull(results);
//		assertEquals(1144, results.length);
//		assertEquals("GUDMAP:8130", results[5][0].getValue());
//		assertEquals("1500016L03Rik", results[3][1].getValue());

//		String labId = "3"; // LITTLE
//		String assayType = "IHC";
//		String submissionDate = "";
//		Object[] params = {labId, assayType, submissionDate};
//		lsa = new LabSummaryAssembler(params);
//		DataItem[][] results = lsa.retrieveData(column, ascending, offset, num);
//		assertNotNull(results);
//		assertEquals(9, results.length);

//		String labId = "3"; // LITTLE
//		String assayType = "ISH";
//		String submissionDate = "2006-02-02";
//		Object[] params = {labId, assayType, submissionDate};
//		lsa = new LabSummaryAssembler(params);
//		DataItem[][] results = lsa.retrieveData(column, ascending, offset, num);
//		assertNotNull(results);
//		assertEquals(77, results.length);
		
//		String labId = "16"; // POTTER
//		String assayType = "array";
//		String submissionDate = "";
//		Object[] params = {labId, assayType, submissionDate};
//		lsa = new LabSummaryAssembler(params);
//		DataItem[][] results = lsa.retrieveData(column, ascending, offset, num);
//		assertNotNull(results);
//		assertEquals(55, results.length);

		String labId = "8"; // mcmahon
		String assayType = "array";
		String submissionDate = "2006-12-15";
		Object[] params = {labId, assayType, submissionDate};
//		lsa = new LabSummaryAssembler(params);
//		DataItem[][] results = lsa.retrieveData(column, ascending, offset, num);
//		assertNotNull(results);
//		assertEquals(4, results.length);
	}
	
	public void testRetrieveNumberOfRows() {
//		String labId = "8"; // mcmahon
////		String assayType = "array";
//		String assayType = "ish";
////		String submissionDate = "2006-12-15";
//		String submissionDate = "2006-07-10";
//		Object[] params = {labId, assayType, submissionDate};
//		lsa = new LabSummaryAssembler(params);
//		int result = lsa.retrieveNumberOfRows();
////		assertEquals(4, result);
//		assertEquals(271, result);
	}

}
