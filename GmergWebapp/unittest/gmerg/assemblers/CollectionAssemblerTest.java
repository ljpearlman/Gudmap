package gmerg.assemblers;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectionAssemblerTest {

    private CollectionAssembler ca = null;
	ArrayList<String> submissionIds = null;
    
	int columnIndex = 1;
	boolean ascending = true;
	int offset = 0;
	int num = 20;

	@Before
	public void setUp() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("collectionIds", 1);
//		ca = new CollectionAssembler(params);
		ca = new CollectionAssembler();
	}

	@After
	public void tearDown() throws Exception {
		ca = null;
	}

	@Test
	public final void testGetData() {
//		fail("Not yet implemented"); // TODO
//		submissionIds = new ArrayList<String>();
//		String subId = new String("GUDMAP:5306");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:7129");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6968");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:114");
//		submissionIds.add(subId);
		
//		ArrayList collection = ca.getData(submissionIds, columnIndex, ascending, offset, num);
//		assertNotNull(collection);
//		assertEquals(4, collection.size());
//		assertEquals(17, ((String[])collection.get(0)).length);
		
//		for (int i=0;i<collection.size();i++) {
//			String subDate = ((String[])collection.get(i))[5];
//			String assay = ((String[])collection.get(i))[6];
//			System.out.println(i + ":subDate: " + subDate);
//			System.out.println(i + ":assay: " + assay);
//		}
	}

	@Test
	public final void testGetTotals() {
//		fail("Not yet implemented"); // TODO
//		submissionIds = new ArrayList<String>();
//		String subId = new String("GUDMAP:5306");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:7129");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6968");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:114");
//		submissionIds.add(subId);
		
//		int[] totalNumbers = ca.getTotals(submissionIds);
//		assertNotNull(totalNumbers);
//		assertEquals(4, totalNumbers[0]); // subid
//		assertEquals(3, totalNumbers[1]); // symbol
//		assertEquals(3, totalNumbers[2]); // stage
//		assertEquals(4, totalNumbers[3]); // age
//		assertEquals(4, totalNumbers[4]); // lab
//		assertEquals(4, totalNumbers[5]); // date
//		assertEquals(2, totalNumbers[6]); // assay
//		assertEquals(2, totalNumbers[7]); // specimen
//		assertEquals(3, totalNumbers[8]); // sex
//		assertEquals(3, totalNumbers[9]); // probe name
//		assertEquals(2, totalNumbers[10]); // geno
//		assertEquals(2, totalNumbers[11]); // probe type
//		assertEquals(3, totalNumbers[12]); // image
//		assertEquals(3, totalNumbers[13]); // tissue
//		assertEquals(3, totalNumbers[14]); // sample title
//		assertEquals(1, totalNumbers[15]); // sample desc
//		assertEquals(3, totalNumbers[16]); // series
	}

	@Test
	public final void testGetSubmissionNumber() {
//		fail("Not yet implemented"); // TODO
//		submissionIds = new ArrayList<String>();
//		String subId = new String("GUDMAP:5306");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:7129");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6968");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:114");
//		submissionIds.add(subId);

//		int submissionNumber = ca.getSubmissionNumber(submissionIds);
//		assertEquals(4, submissionNumber);
	}
	
	public final void testGetCollectionInfo() {
//		int collectionId = 3;
//		CollectionInfo collectionInfo = ca.getCollectionInfo(collectionId);
//		assertNotNull(collectionInfo);
//		assertEquals("DH_CLN_MALE", collectionInfo.getName());
//		assertEquals(3, collectionInfo.getEntries());
		
	}

}
