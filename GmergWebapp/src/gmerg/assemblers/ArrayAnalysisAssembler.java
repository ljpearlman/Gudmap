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
	
	/**
	 * 
	 * @param seriesGEOId
	 * @return
	 */
	public String[][] retrieveSampleIds(String seriesGEOId) {
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get data
		String[][] sampleIds = arrayDAO.findSampleIdsBySeriesId(seriesGEOId);
		
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
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
        // get data
		DataSet analysisDataSet = arrayDAO.getAnalysisDataSetBySampleIds(samplesInfo);
		
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
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get data
		ArrayList seriesGEOIds = arrayDAO.getAllSeriesGEOIds();
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the value object---  */
		return seriesGEOIds;
	}

}
