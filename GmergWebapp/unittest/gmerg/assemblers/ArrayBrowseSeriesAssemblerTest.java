///**
// * 
// */
//package gmerg.assemblers;
//
//import junit.framework.TestCase;
//
//import java.util.ArrayList;
//
///**
// * @author xingjun
// *
// */
//public class ArrayBrowseSeriesAssemblerTest extends TestCase {
//	
//	private ArrayBrowseSeriesAssembler arrayBrowseSeriesAssembler= null;
//
//	/**
//	 * @param arg0
//	 */
//	public ArrayBrowseSeriesAssemblerTest(String arg0) {
//		super(arg0);
//	}
//
//	/* (non-Javadoc)
//	 * @see junit.framework.TestCase#setUp()
//	 */
//	protected void setUp() throws Exception {
//		super.setUp();
//		arrayBrowseSeriesAssembler = new ArrayBrowseSeriesAssembler();
//	}
//
//	/* (non-Javadoc)
//	 * @see junit.framework.TestCase#tearDown()
//	 */
//	protected void tearDown() throws Exception {
//		super.tearDown();
//		arrayBrowseSeriesAssembler = null;
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ArrayBrowseSeriesAssembler#ArrayBrowseSeriesAssembler()}.
//	 */
//	public final void testArrayBrowseSeriesAssembler() {
////		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ArrayBrowseSeriesAssembler#getData(int, boolean, int, int)}.
//	 */
//	public final void testGetData() {
////		fail("Not yet implemented"); // TODO
//		ArrayList allEntries = arrayBrowseSeriesAssembler.getData(0, true, 0, 200);
////		System.out.println("series no.: " + allEntries.size());
//		assertNotNull(allEntries);
//		assertEquals(17, allEntries.size());
//		
//		String[] record = (String[])allEntries.get(0);
//		assertEquals(4, record.length);
//		assertEquals("Gene expression analysis of embryonic day 14 gential tubercle of the male. (GUDMAP Series_id: 16)", record[0]);
//		assertEquals("GSE7903", record[2]);
//		
//		record = (String[])allEntries.get(15);
//		assertEquals("kidney component comparison", record[1]);
//		assertEquals("GSE6309", record[2]);
//		
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ArrayBrowseSeriesAssembler#getColumnTotals()}.
//	 */
//	public final void testGetColumnTotals() {
////		fail("Not yet implemented"); // TODO
//		int[] totalNumbers = arrayBrowseSeriesAssembler.getColumnTotals();
//		assertNotNull(totalNumbers);
//		int len = totalNumbers.length;
//		assertEquals(4, len);
//		assertEquals(17, totalNumbers[0]);
//		assertEquals(10, totalNumbers[1]);
//		assertEquals(11, totalNumbers[2]);
//		assertEquals(17, totalNumbers[3]);
//	}
//
//	/**
//	 * Test method for {@link gmerg.assemblers.ArrayBrowseSeriesAssembler#getNumberOfSeries()}.
//	 */
//	public final void testGetNumberOfSeries() {
////		fail("Not yet implemented"); // TODO
//		int num = arrayBrowseSeriesAssembler.getNumberOfSeries();
//		assertEquals(17, num);
//	}
//
//}
