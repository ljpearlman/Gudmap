/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import java.util.HashMap;
import gmerg.utils.table.DataItem;
import gmerg.assemblers.SeriesAssembler;
import gmerg.entities.submission.array.Series;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class SeriesAssemblerTest {

	SeriesAssembler sa;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("seriesId", "1");
		params.put("seriesId", "2");
		sa = new SeriesAssembler(params);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		sa = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesAssembler#retrieveData(int, boolean, int, int)}.
	 */
	@Test
	public void testRetrieveData() {
//		fail("Not yet implemented");
		Object[] params = {"GSE7903"};
//		sa = new SeriesAssembler(params);
//		int columnIndex = 0; 
//		boolean ascending = true; 
//		int offset = 0; 
//		int num = 1000;
//		DataItem[][] series = sa.retrieveData(columnIndex, ascending, offset, num);
//		assertNotNull(series);
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public void testRetrieveNumberOfRows() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesAssembler#retrieveTotals()}.
	 */
	@Test
	public void testRetrieveTotals() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gmerg.assemblers.SeriesAssembler#getData(java.lang.String)}.
	 */
	@Test
	public void testGetData() {
//		fail("Not yet implemented");
//		SeriesAssembler sa = new SeriesAssembler();
//		Series series = sa.getData("GUDMAP:7136");
//		assertNotNull(series);
//		assertEquals("12", series.getNumSamples());
//		assertEquals("GSE6589", series.getGeoID());
//		assertNotNull(series.getSummaryResults());
//		assertEquals("Sample1", ((String[])series.getSummaryResults().get(0))[0]);
//		assertEquals("CCD_3_6_13_06", ((String[])series.getSummaryResults().get(1))[3]);
//		for (int i=0;i<series.getSummaryResults().size();i++) {
//			for (int j=0;j<((String[])series.getSummaryResults().get(1)).length;j++) {
//				System.out.println(((String[])series.getSummaryResults().get(i))[j]);
//			}
//		}
	}
	
	@Test
	public void testGetSeriesMetaData() {
		Series series = sa.getSeriesMetaData();
		assertNotNull(series);
		assertEquals("GSE4816", series.getGeoID());
	}
}
