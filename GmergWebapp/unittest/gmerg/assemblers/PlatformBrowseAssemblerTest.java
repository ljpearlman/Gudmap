/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.PlatformBrowseAssembler;
import gmerg.utils.table.DataItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class PlatformBrowseAssemblerTest {

	PlatformBrowseAssembler pba = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
//		pba = new PlatformBrowseAssembler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		pba = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.PlatformBrowseAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public void testRetrieveData() {
//		fail("Not yet implemented");
		Object[] params = {"1"};
//		pba = new PlatformBrowseAssembler(params);
//		int columnIndex = 0;
//		boolean ascending = true;
//		int offset = 0;
//		int num = 1000;
//		DataItem[][] platforms = pba.retrieveData(columnIndex, ascending, offset, num);
//		assertNotNull(platforms);
//		assertEquals(5, platforms[0].length);
//		assertEquals("14", platforms[0][4].getValue());
//		assertEquals("in situ oligonucleotide", platforms[0][2].getValue());
//		System.out.println(platforms[0][0].getValue() + "#" + platforms[0][4].getValue());
//		System.out.println(platforms[1][0].getValue() + "#" + platforms[1][4].getValue());
//		System.out.println(platforms[2][0].getValue() + "#" + platforms[2][4].getValue());
		
//		columnIndex = 2;
//		ascending = true;
//		offset = 0;
//		num = 1000;
//		DataItem[][] platforms = pba.retrieveData(columnIndex, ascending, offset, num);
//		assertNotNull(platforms);
//		assertEquals(3, platforms.length);
//		System.out.println(platforms[0][0].getValue() + "#" + platforms[0][4].getValue());
//		System.out.println(platforms[1][0].getValue() + "#" + platforms[1][4].getValue());
//		System.out.println(platforms[2][0].getValue() + "#" + platforms[2][4].getValue());
//		assertEquals("2", platforms[1][4].getValue());
//		assertEquals("Affymetrix GeneChip Mouse Genome 430 2.0 Array", platforms[0][1].getValue());
		
	}

	/**
	 * Test method for {@link gmerg.assemblers.PlatformBrowseAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public void testRetrieveNumberOfRows() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gmerg.assemblers.PlatformBrowseAssembler#getTableDataFormatFromPlatformsList(java.util.ArrayList)}.
	 */
	@Test
	public void testGetTableDataFormatFromPlatformsList() {
//		fail("Not yet implemented");
	}

}
