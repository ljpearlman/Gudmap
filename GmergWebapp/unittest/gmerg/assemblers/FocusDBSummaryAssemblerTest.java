/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gmerg.assemblers.FocusDBSummaryAssembler;
import gmerg.entities.summary.DBSummary;

/**
 * @author xingjun
 *
 */
public class FocusDBSummaryAssemblerTest {
	
	private FocusDBSummaryAssembler fdsa;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		fdsa = new FocusDBSummaryAssembler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		fdsa = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.FocusDBSummaryAssembler#getData(java.lang.String[])}.
	 */
	@Test
	public void testGetData() {
//		fail("Not yet implemented");

		// metanephros
//		String[] emapids = {"EMAP:3233","EMAP:3841","EMAP:4587",
//				"EMAP:5504","EMAP:6674","EMAP:8226",
//				"EMAP:9536","EMAP:10896","EMAP:12256",
//				"EMAP:29491","EMAP:30247"};
		// lower urinary tract
		String[] emapids = {"EMAP:6668", 
				"EMAP:8219","EMAP:9528",
				"EMAP:10888","EMAP:12248","EMAP:29457",
				"EMAP:30374","EMAP:28749","EMAP:28750","EMAP:28751",
				"EMAP:28752","EMAP:29443","EMAP:30418","EMAP:28556",
				"EMAP:30898","EMAP:27572","EMAP:3237","EMAP:3846",
				"EMAP:4593","EMAP:5516"};
		
		DBSummary dbSummary = fdsa.getData(emapids);
		assertNotNull(dbSummary);
		assertEquals(1, dbSummary.getTotAvailArraySubs());
	}

}
