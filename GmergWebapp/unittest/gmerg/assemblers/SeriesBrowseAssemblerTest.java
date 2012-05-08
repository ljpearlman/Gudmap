/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gmerg.assemblers.SeriesBrowseAssembler;
import gmerg.utils.table.DataItem;


/**
 * @author xingjun
 *
 */
public class SeriesBrowseAssemblerTest {

	private SeriesBrowseAssembler sba;
	
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
		sba = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesBrowseAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public void testRetrieveData() {
//		fail("Not yet implemented");
		Object[] params = {"1"};
//		sba = new SeriesBrowseAssembler(params);
//		int columnIndex = 1;
//		boolean ascending = true;
//		int offset = 0; 
		int num = 1000;
//		DataItem[][] series = sba.retrieveData(columnIndex, ascending, offset, num);
//		assertNotNull(series);
//		assertEquals(6, series.length);
//		assertEquals(5, series[0].length);
//		assertEquals("Profiling of microdissected kidney structures - MOE430 platform", series[2][0].getValue());
//		assertEquals("9", series[2][2].getValue());
		
//		params = null;
//		sba = new SeriesBrowseAssembler(params);
//		columnIndex = 1;
//		ascending = true;
//		offset = 0; 
//		num = 1000;
//		DataItem[][] series = sba.retrieveData(columnIndex, ascending, offset, num);
//		assertNotNull(series);
//		assertEquals(17, series.length);
//		assertEquals("Gene expression in whole epididymus at gd 12, 14, 16, 18 and pnd2", series[2][0].getValue());
//		assertEquals("16", series[2][2].getValue());
		
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesBrowseAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public void testRetrieveNumberOfRows() {
//		fail("Not yet implemented");
		Object[] params = {"1"};
//		sba = new SeriesBrowseAssembler(params);
//		int rows = sba.retrieveNumberOfRows();
//		assertNotNull(rows);
//		assertEquals(6, rows);
//		
//		params = null;
//		sba = new SeriesBrowseAssembler(params);
//		rows = sba.retrieveNumberOfRows();
//		assertNotNull(rows);
//		assertEquals(17, rows);
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesBrowseAssembler#retrieveTotals()}.
	 */
	@Test
	public void testRetrieveTotals() {
//		fail("Not yet implemented");
		Object[] params = {"1"};
//		sba = new SeriesBrowseAssembler(params);
//		int[] totals = sba.retrieveTotals();
//		assertNotNull(totals);
//		assertEquals(5, totals.length);
//		assertEquals(6, totals[0]);
//		for (int i=0;i<totals.length;i++) {
//			System.out.println(totals[i]);
//		}

		params = null;
//		sba = new SeriesBrowseAssembler(params);
//		int[] totals = sba.retrieveTotals();
//		assertNotNull(totals);
//		assertEquals(5, totals.length);
//		assertEquals(17, totals[0]);
//		for (int i=0;i<totals.length;i++) {
//			System.out.println(totals[i]);
//		}
	}

}
