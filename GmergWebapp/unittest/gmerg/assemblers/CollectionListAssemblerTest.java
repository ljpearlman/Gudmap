/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import gmerg.assemblers.CollectionListAssembler;
import gmerg.utils.table.DataItem;

/**
 * @author xingjun
 *
 */
public class CollectionListAssemblerTest {
	
	private CollectionListAssembler cla;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("userId", 1);
		params.put("collectionType", 1);
		params.put("status", 0);
		params.put("focusGroup", 0);
		
		cla = new CollectionListAssembler(params);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		cla = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.CollectionListAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public final void testRetrieveData() {
//		fail("Not yet implemented"); // TODO
		DataItem[][] result = cla.retrieveData(8, false, 0, 1000);
		assertNotNull(result);
		for (int i=0;i<result.length;i++) {
			assertEquals(12, result[i].length);
//			System.out.println("collection name: " + result[i][1].getValue().toString());
//			System.out.print("owner: " + result[i][4].getValue().toString() + "####");
//			System.out.println("focus group: " + result[i][5].getValue().toString());
		}
//		assertEquals("DH_CLN_MALE", result[1][1].getValue().toString());
		assertEquals(6, result.length);
	}

	/**
	 * Test method for {@link gmerg.assemblers.CollectionListAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public final void testRetrieveNumberOfRows() {
//		fail("Not yet implemented"); // TODO
		int result = cla.retrieveNumberOfRows();
		assertEquals(6, result);
	}

}
