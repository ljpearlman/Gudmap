/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.PredictiveTextSearchDAO;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun - 12/03/2009
 *
 */
public class PredictiveTextSearchAssembler {
	
	public PredictiveTextSearchAssembler() {
		
	}
	
	/**
	 * based on user's input, return possible gene symbol(s) or synonym(s)
	 * @param keyPress
	 * @param type
	 * @return
	 */
	public ArrayList<String> getGenes(String input, int searchPattern, int num) {
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        PredictiveTextSearchDAO ptsDAO = MySQLDAOFactory.getPredictiveTextSearchDAO(conn);
		
		// get data from db
        ArrayList<String> genes = ptsDAO.getGeneSymbolsAndSynonyms(input, searchPattern, num);
        
        // release resources
        DBHelper.closeJDBCConnection(conn);
        ptsDAO = null;

        return genes;
	}
	
	/**
	 * @author xingjun - 16/03/2009
	 * based on user's input, return possible anatomy terms
	 * @param input
	 * @param searchPattern
	 * @param num
	 * @return
	 */
	public ArrayList<String> getAnatomyStructures(String input, int searchPattern, int num) {
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        PredictiveTextSearchDAO ptsDAO = MySQLDAOFactory.getPredictiveTextSearchDAO(conn);
		
		// get data from db
        ArrayList<String> anatomyTerms = ptsDAO.getAnnatomyTerms(input, searchPattern, num);
        
        // release resources
        DBHelper.closeJDBCConnection(conn);
        ptsDAO = null;

        return anatomyTerms;
	}
	
	/**
	 * @author xingjun - 16/03/2009
	 * @param input
	 * @param searchPattern
	 * @param num
	 * @return
	 */
	public ArrayList<String> getGeneFunctions(String input, int searchPattern, int num) {
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        PredictiveTextSearchDAO ptsDAO = MySQLDAOFactory.getPredictiveTextSearchDAO(conn);
		
		// get data from db
        ArrayList<String> goTerms = ptsDAO.getGoTerms(input, searchPattern, num);
        
        // release resources
        DBHelper.closeJDBCConnection(conn);
        ptsDAO = null;
        
        // return
		return goTerms;
	}
	
}
