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
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			String[][] sampleIds = arrayDAO.findSampleIdsBySeriesId(seriesGEOId);
			return sampleIds;
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::retrieveSampleIds failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * 
	 * @param sampleIds
	 * @return
	 */
	public DataSet retrieveExpressionData(String[][] samplesInfo) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			DataSet analysisDataSet = arrayDAO.getAnalysisDataSetBySampleIds(samplesInfo);
			return analysisDataSet;
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::retrieveExpressionData failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList getSeriesGEOIds() {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			ArrayList seriesGEOIds = arrayDAO.getAllSeriesGEOIds();
			return seriesGEOIds;
		}
		catch(Exception e){
			System.out.println("ArrayAnalysisAssembler::getSeriesGEOIds failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

}
