/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;

import java.sql.Connection;
import java.util.ArrayList;
import analysis.DataSet;

/**
 * @author xingjun
 *
 */
public class ArrayAnalysisAssembler {
    private boolean debug = false;
    public ArrayAnalysisAssembler() {
	if (debug)
	    System.out.println("ArrayAnalysisAssembler.constructor");
    }

	/**
	 * 
	 * @param seriesGEOId
	 * @return
	 */
	public String[][] retrieveSampleIds(String seriesGEOId) {
		
		String[][] sampleIds = null;

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		try{
			 arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			// get data
			sampleIds = arrayDAO.findSampleIdsBySeriesId(seriesGEOId);
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::retrieveSampleIds failed !!!");
			sampleIds = null;
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the value object---  */
		return sampleIds;
	}
	
	/**
	 * 
	 * @param sampleIds
	 * @return
	 */
	public DataSet retrieveExpressionData(String[][] samplesInfo) {
		
		DataSet analysisDataSet =null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO;
		try{
			arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	        // get data
			analysisDataSet = arrayDAO.getAnalysisDataSetBySampleIds(samplesInfo);
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::retrieveExpressionData failed !!!");
			analysisDataSet = null;
		}
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the value object---  */
		return analysisDataSet;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList getSeriesGEOIds() {
		
		ArrayList seriesGEOIds = null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO;
		try{
			arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			// get data
			seriesGEOIds = arrayDAO.getAllSeriesGEOIds();
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::getSeriesGEOIds failed !!!");
			seriesGEOIds = new ArrayList();
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the value object---  */
		return seriesGEOIds;
	}

}
