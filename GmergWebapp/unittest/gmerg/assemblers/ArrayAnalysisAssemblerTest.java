package gmerg.assemblers;

import gmerg.assemblers.ArrayAnalysisAssembler;
import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import analysis.DataSet;

import java.sql.Connection;
import java.util.ArrayList;

import junit.framework.TestCase;

public class ArrayAnalysisAssemblerTest extends TestCase {

	ArrayDAO arrayDAO = null;
	Connection conn = null;

	public ArrayAnalysisAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
		conn = DBHelper.getDBConnection();
		arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// release a dao
		arrayDAO = null;
		DBHelper.closeJDBCConnection(conn);
	}

	/*
	 * Test method for 'gmerg.assemblers.ArrayAnalysisAssembler.retrieveSampleIds(String)'
	 */
	public final void testRetrieveSampleIds() {
		// TODO Auto-generated method stub
		
		ArrayAnalysisAssembler aaa = new ArrayAnalysisAssembler();
		String[][] sampleIds = aaa.retrieveSampleIds("GSE6589");
		assertNotNull(sampleIds);
		assertEquals(12, sampleIds.length);
//		for (int i=0;i<sampleIds.length;i++) {
//			System.out.println("sample id " + i + ": " +sampleIds[i]);
//		}
//		assertEquals(1, sampleIds[0]);

	}

	/*
	 * Test method for 'gmerg.assemblers.ArrayAnalysisAssembler.retrieveExpressionData(int[])'
	 */
	public final void testRetrieveExpressionData() {
		// TODO Auto-generated method stub

		ArrayAnalysisAssembler aaa = new ArrayAnalysisAssembler();
//		int[] sampleIds = {154, 155, 157, 158, 156, 161, 159, 160, 163, 164, 162, 162};
		int[] sampleIds = {22, 23, 28, 31, 32, 33, 29, 30, 24, 25, 26, 27};
//		DataSet dataSet = aaa.retrieveExpressionData(sampleIds);
//		assertNotNull(dataSet);
		
	}

	/*
	 * Test method for 'gmerg.assemblers.ArrayAnalysisAssembler.getSeriesGEOIds()'
	 */
	public final void testGetSeriesGEOIds() {
		// TODO Auto-generated method stub
		
		ArrayAnalysisAssembler aaa = new ArrayAnalysisAssembler();
		ArrayList seriesIds = aaa.getSeriesGEOIds();
		assertNotNull(seriesIds);
		assertEquals(17, seriesIds.size());
//		for (int i=0;i<seriesIds.size();i++) {
//			System.out.println("series " + i + ": " + ((String)seriesIds.get(i)));
//		}

	}

}
