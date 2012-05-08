/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gmerg.assemblers.FocusForAllAssembler;
import gmerg.utils.table.DataItem;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class FocusForAllAssemblerTest {

	FocusForAllAssembler ffaa;
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
		ffaa = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.FocusForAllAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public void testRetrieveData() {
//		fail("Not yet implemented");
		String organ = "1";
		String[] input = {"wnt4"};
		String query = "Gene";
		String sub = "ISH";
		String exp = "present";
		String[] wildcard = {"equals", "All", "1"};
		
		Object[] params = {organ, input, query, sub, exp, wildcard};
//		ffaa = new FocusForAllAssembler(params);
//		int column = 1;
//		boolean ascending = true;
//		int offset = 0; 
//		int num = 1000;
//		DataItem[][] result = ffaa.retrieveData(column, ascending, offset, num);
//		assertNotNull(result);
	}

	/**
	 * Test method for {@link gmerg.assemblers.FocusForAllAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public void testRetrieveNumberOfRows() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gmerg.assemblers.FocusForAllAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public void testRetrieveNumberOfRowsInGroups() {
//		fail("Not yet implemented");
//		String organ = "1";
//		String[] input = {"wnt4"};
//		String query = "Gene";
//		String sub = "ish";
		String sub = null;
		String exp = "";
		String[] wildcard = {"equals", "All", "1"};
		
//		Object[] params = {organ, input, query, sub, exp, wildcard};
//		ffaa = new FocusForAllAssembler(params);
//		int[] totals = ffaa.retrieveNumberOfRowsInGroups();
//		assertNotNull(totals);
//		for (int i=0;i<totals.length;i++) {
//			System.out.println(i + " : " + totals[i]);
//		}
		
	}
	
}
