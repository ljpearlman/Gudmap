/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import gmerg.assemblers.FocusBrowseAssembler;
import gmerg.utils.table.DataItem;

/**
 * @author xingjun
 *
 */
public class FocusBrowseAssemblerTest {
	
	private FocusBrowseAssembler fba = null;

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
		fba = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.FocusBrowseAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public final void testRetrieveData() {
//		fail("Not yet implemented"); // TODO
//		String assayType = "array";
//		String[] organs = {"1"};
		String[] organs = null;
//		String stage = "19";
		String stage = null;
		String gene = "Cd24a";
//		Object[] params = {assayType, organ, stage, gene};
//		fba = new FocusBrowseAssembler(params);
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("assayType", "insitu");
		queryParams.put("organ", organs);
		queryParams.put("stage", stage);
		queryParams.put("gene", gene);
		fba = new FocusBrowseAssembler(queryParams);
		int column = 1;
		boolean ascending = true;
		int offset = 0; 
		int num = 1000;
		DataItem[][] result = fba.retrieveData(column, ascending, offset, num);
		assertNotNull(result);
	}


}
