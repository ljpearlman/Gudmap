///**
// * 
// */
//package gmerg.assemblers;
//
//import static org.junit.Assert.*;
//
//import java.sql.Connection;
//import java.util.ArrayList;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import gmerg.db.DBHelper;
//import gmerg.db.ISHDevDAO;
//import gmerg.db.MySQLDAOFactory;
//
///**
// * @author xingjun
// *
// */
//public class ISHCollectionAssembler1Test {
//
//	ISHCollectionAssembler1 collectionAssembler = null;
//	ArrayList<String> submissionIds = null;
//	
//	int columnIndex = 1;
//	boolean ascending = true;
//	int offset = 0;
//	int num = 10;
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		collectionAssembler = new ISHCollectionAssembler1();
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//		collectionAssembler = null;
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ISHCollectionAssembler1#getData(java.util.ArrayList, int, boolean, int, int)}.
//	 */
//	@Test
//	public final void testGetData() {
////		fail("Not yet implemented"); // TODO
//		submissionIds = new ArrayList<String>();
//		String subId = new String("GUDMAP:5306");
//		submissionIds.add(subId);
////		subId = new String("GUDMAP:6");
////		submissionIds.add(subId);
////		subId = new String("GUDMAP:6457");
////		submissionIds.add(subId);
////		subId = new String("GUDMAP:7");
////		submissionIds.add(subId);
//		
//		
//		ArrayList collection = collectionAssembler.getData(submissionIds, columnIndex, ascending, offset, num);
//		assertNotNull(collection);
////		assertEquals(4, collection.size());
//		assertEquals(1, collection.size());
////		for (int i=0;i<collection.size();i++) {
////			String[] record = (String[])collection.get(i);
////			System.out.println("id: " + record[0]);
////		}
//		
//		String[] record = (String[])collection.get(0);
//		String id = (String)record[0];
//		assertEquals("GUDMAP:5306", id);
//		String symbol = (String)record[1];
//		assertEquals("1110002E23Rik", symbol);
//		String ts = (String)record[2];
//		assertEquals("23", ts);
//
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ISHCollectionAssembler1#getTotals(java.util.ArrayList)}.
//	 */
//	@Test
//	public final void testGetTotals() {
////		fail("Not yet implemented"); // TODO
//		submissionIds = new ArrayList<String>();
//		String subId = new String("GUDMAP:5306");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6457");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:7");
//		submissionIds.add(subId);
//
//		int[] totalNumbers = collectionAssembler.getTotals(submissionIds);
//		assertNotNull(totalNumbers);
//		assertEquals(4, totalNumbers[0]);
//		assertEquals(3, totalNumbers[1]);
//		assertEquals(3, totalNumbers[2]);
//		assertEquals(3, totalNumbers[3]);
//		assertEquals(2, totalNumbers[4]);
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ISHCollectionAssembler1#getSubmissionNumber(java.util.ArrayList)}.
//	 */
//	@Test
//	public final void testGetSubmissionNumber() {
////		fail("Not yet implemented"); // TODO
//		submissionIds = new ArrayList<String>();
//		String subId = new String("GUDMAP:5306");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:6457");
//		submissionIds.add(subId);
//		subId = new String("GUDMAP:7");
//		submissionIds.add(subId);
//
//		int submissionNumber = collectionAssembler.getSubmissionNumber(submissionIds);
//		assertEquals(4, submissionNumber);
//	}
//
//}
