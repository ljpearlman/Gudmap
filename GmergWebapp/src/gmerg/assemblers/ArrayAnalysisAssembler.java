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
			/** ---return the value object---  */
			return sampleIds;
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::retrieveSampleIds failed !!!");
			return null;
		}
		finally{
			// release db resources
			DBHelper.closeJDBCConnection(conn);
			arrayDAO = null;
		}
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
			/** ---return the value object---  */
			return analysisDataSet;
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::retrieveExpressionData failed !!!");
			return null;
		}
		finally{
			// release db resources
			DBHelper.closeJDBCConnection(conn);
			arrayDAO = null;
		}
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
			/** ---return the value object---  */
			return seriesGEOIds;
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::getSeriesGEOIds failed !!!");
			seriesGEOIds = new ArrayList();
			/** ---return the value object---  */
			return seriesGEOIds;
		}
		finally{
			// release db resources
			DBHelper.closeJDBCConnection(conn);
			arrayDAO = null;
		}
	}

}
