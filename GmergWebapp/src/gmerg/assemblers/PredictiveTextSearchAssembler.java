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
    private boolean debug = false;

	public PredictiveTextSearchAssembler() {
	if (debug)
	    System.out.println("PredictiveTextSearchAssembler.constructor");
		
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
        PredictiveTextSearchDAO ptsDAO;
        ArrayList<String> genes = null;
        try{
	        ptsDAO = MySQLDAOFactory.getPredictiveTextSearchDAO(conn);
			
			// get data from db
	        genes = ptsDAO.getGeneSymbolsAndSynonyms(input, searchPattern, num);
	        return genes;
        }
		catch(Exception e){
			System.out.println("PredictiveTextSearchAssembler::getGenes failed !!!");
			return null;
		}
        finally{
	        // release resources
	        DBHelper.closeJDBCConnection(conn);
	        ptsDAO = null;
        }
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
        PredictiveTextSearchDAO ptsDAO;
        ArrayList<String> anatomyTerms = null;
        try{
	        ptsDAO = MySQLDAOFactory.getPredictiveTextSearchDAO(conn);
			
			// get data from db
	        anatomyTerms = ptsDAO.getAnnatomyTerms(input, searchPattern, num);
	        return anatomyTerms;
        }
		catch(Exception e){
			System.out.println("PredictiveTextSearchAssembler::getAnatomyStructures failed !!!");
			return null;
		}
        finally{
	        // release resources
	        DBHelper.closeJDBCConnection(conn);
	        ptsDAO = null;
        }
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
        PredictiveTextSearchDAO ptsDAO;
        ArrayList<String> goTerms = null;
        try{
	        ptsDAO = MySQLDAOFactory.getPredictiveTextSearchDAO(conn);
			
			// get data from db
	        goTerms = ptsDAO.getGoTerms(input, searchPattern, num);
	        // return
			return goTerms;
        }
		catch(Exception e){
			System.out.println("PredictiveTextSearchAssembler::getGeneFunctions failed !!!");
			return null;
		}
        finally{
	        // release resources
	        DBHelper.closeJDBCConnection(conn);
	        ptsDAO = null;
        }
	}
	
}
