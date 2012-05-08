package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import gmerg.assemblers.AdvancedSearchAssembler;

import java.util.ArrayList;

/**
 * 
 */

/**
 * @author xingjun
 *
 */
public class AdvancedSearchAssemblerTest {
	
	private AdvancedSearchAssembler asa = null;
//	private FocusForAllAssembler ffa = null;

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
	}

	/**
	 * Test method for {@link gmerg.assemblers.AdvancedSearchAssembler#getFocusQuery(java.lang.String, java.lang.String[], 
	 * int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetFocusQuery() {
//		fail("Not yet implemented"); // TODO
		asa = new AdvancedSearchAssembler();
		String type = "Gene";
//		String[] input = {"cd24a"};
		String[] input = {"cd24a"};
//		String[] input = {"cd24"};
		int orderby = 0;
		boolean asc = true;
		String offset = "0";
		String resperpage = "20";
		String organ = "";
//		String sub = "ish";
		String sub = "mic";
		String exp = "";
//		String[] queryCriteria = {"equals", "All", "0"};
		String[] queryCriteria = {"equals", "23", "1"};
//		String[] queryCriteria = {"contains", "23", "1"};
//		String[] queryCriteria = {"starts with", "ALL", "0"};
//		ArrayList result = asa.getFocusQuery(type, input, orderby, asc, offset, resperpage, organ, sub, exp, queryCriteria);
//		int total = asa.getNumberOfRows(type, input, organ, sub, exp, queryCriteria);
//		assertNotNull(result);
//		assertEquals(2, result.size());
	}

}
