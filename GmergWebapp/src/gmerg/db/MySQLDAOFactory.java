/**
 * 
 */
package gmerg.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author xingjun
 * 
 * This class is used to create connection to MySQL database and create
 * specific DAO object to communicate with the database.
 *
 */
public final class MySQLDAOFactory {
	
	/**
	 * @author xingjun
	 * @return
	 */
	public static ISHDAO getISHDAO(Connection conn) {
		// MySQLISHDAOImp implements ISHDAO
		return new MySQLISHDAOImp(conn);
	}
	
	/**
	 * @author xingjun
	 * @return
	 */
	public static ArrayDAO getArrayDAO(Connection conn) {
		// MySQLArrayDAOImp implements ArrayDAO
		return new MySQLArrayDAOImp(conn);
	}
	
	/**
	 * @author xingjun
	 * @return
	 */
	public static AnatomyDAO getAnatomyDAO(Connection conn) {
		// MySQLAnatomyDAOImp implements AnatomyDAO
		return new MySQLAnatomyDAOImp(conn);
	}
        
    /**
     * 
     * @param conn
     * @return
     */
	public static SecurityDAO getSecurityDAO(Connection conn){
        return new MySQLSecurityDAOImp(conn);
    }

  	/**
   	 * @author xingjun
   	 * @return
   	 */
   	public static ISHDevDAO getISHDevDAO(Connection conn) {
   		// MySQLISHDevDAOImp implements ISHDevDAO
   		return new MySQLISHDevDAOImp(conn);
   	}
   	
   	/**
   	 * @author xingjun
   	 * @param conn
   	 * @return
   	 */
   	public static ISHEditDAO getISHEditDAO(Connection conn) {
   		// MySQLISHEditDAOImp implements ISHEditDAO
   		return new MySQLISHEditDAOImp(conn);
   	}
	
	/**
	 * 
	 * @return
	 */
	public static AdvancedQueryDAO getAdvancedQueryDAO(Connection conn) {
		// MySQLArrayDAOImp implements ArrayDAO
		return new MySQLAdvancedQueryDAOImp(conn);
	}	
	
	/**
	 * 
	 * @return
	 */
	public static ArrayDevDAO getArrayDevDAO(Connection conn) {
		// MySQLArrayDevDAOImp implements ArrayDevDAO
		return new MySQLArrayDevDAOImp(conn);
	}

	/**
	 * 
	 * @return
	 */
	public static FocusForAllDAO getFocusForAllDAO(Connection conn) {
	
		return new MySQLFocusForAllDAOImp(conn);
	}	

	public static FocusStageDAO getFocusStageDAO(Connection conn) {
		
		return new MySQLFocusStageDAOImp(conn);
	}		

	public static IHCDAO getIHCDAO(Connection conn) {
		
		return new MySQLIHCDAOImp(conn);
	}
	
	public static BooleanQueryDAO getBooleanQueryDAO(Connection conn) {
		
		return new MySQLBooleanQueryDAOImp(conn);
	}
	

	public static GenelistDAO getGenelistDAO(Connection conn) {
		// MySQLGenelistDAOImp implements GenelistDAO
		return new MySQLGenelistDAOImp(conn);
	}
	
	/**
	 * @author xingjun 08/05/2008
	 * @param conn
	 * @return
	 */
	public static CollectionDAO getCollectionDAO(Connection conn) {
		// MySQLCollectionDAOImp implements CollectionDAO
		return new MySQLCollectionDAOImp(conn);
	}
	
	//tmp code
	public static AnnotationTestDAO getAnnotationTestDAO(Connection conn){
		return new MySQLAnnotationTestDAOImp(conn);
	}
	
	/**
	 * @author ycheng
	 * @return
	 */
	public static TrackingIPDAO getTrackingIPDAO(Connection conn) {
		// MySQLAnatomyDAOImp implements AnatomyDAO
		return new MySQLTrackingIPDAOImp(conn);
	}
	
	/**
	 * @author xingjun - 26/08/2008
	 * @param conn
	 * @return
	 */
	public static TransgenicDAO getTransgenicDAO(Connection conn) {
		return new MySQLTransgenicDAOImp(conn);
	}
	
	/**
	 * @author xingjun - 05/11/2008
	 * @param conn
	 * @return
	 */
	public static GeneStripDAO getGeneStripDAO(Connection conn) {
		return new MySQLGeneStripDAOImp(conn);
	}
	
	/**
	 * @author xingjun - 12/03/2009
	 * @param conn
	 * @return
	 */
	public static PredictiveTextSearchDAO getPredictiveTextSearchDAO(Connection conn) {
		return new MySQLPredictiveTextSearchDAOImp(conn);
	}

	/**
	 * @author xingjun - 18/03/2009
	 * @param conn
	 * @return
	 */
	public static GeneDAO getGeneDAO(Connection conn) {
		return new MySQLGeneDAOImp(conn);
	}

}
