/**
 * 
 */
package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class MySQLPredictiveTextSearchDAOImp implements PredictiveTextSearchDAO {
    private boolean debug = false;
    private Connection conn;

    // default constructor
    public MySQLPredictiveTextSearchDAOImp() {
    	
    }

    // constructor with connection initialisation
    public MySQLPredictiveTextSearchDAOImp(Connection conn) {
    	this.conn = conn;
    }

    
	/**
	 * @author xingjun - 12/03/2009
	 * <p>xingjun - 24/09/2009 - added an extra parameter to pass to the sql to include microarray related gene into the result</p>
	 * <p>xingjun - 12/10/2009 - added extra param to pass to the sql to include array related gene synonyms into the result</p>
	 * @param input - string input by user
	 * @param searchPattern - refer to code
	 * @param num - number of records needed to retrieve
	 */
    public ArrayList<String> getGeneSymbolsAndSynonyms(String input, int searchPattern, int num) {
        ArrayList<String> result = null;
        ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("GENE_SYMBOLS_AND_SYNONYMS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        String geneString = "";
        if (searchPattern == 1) { // start with
        	geneString = input + "%";
        } else if (searchPattern == 2) { // start string of constituent word(s) of the symbol or synonym
        	
        }
        try {
		    if (debug)
			 System.out.println("MySQLPredictiveTextSearchDAOImp.sql = "+queryString.toLowerCase());
		    
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, geneString);
        	prepStmt.setString(2, geneString);
        	prepStmt.setString(3, geneString);
        	prepStmt.setString(4, geneString);
        	prepStmt.setInt(5, num);
        	resSet = prepStmt.executeQuery();
        	if (resSet.first()) {
        		result = new ArrayList<String>();
        		resSet.beforeFirst();
        		while (resSet.next()) {
        			String gene = this.filterNoiseCharacters(resSet.getString(1));
        			result.add(gene);
        		}
        	}
    		return result;
        	
        } catch (SQLException se) {
        	se.printStackTrace();
    		return null;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);        	
        }
	}
    
    /**
     * @author xingjun - 16/03/2009
     */
    public ArrayList<String> getAnnatomyTerms(String input, int searchPattern, int num) {
        ArrayList<String> result = null;
        ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("ANNATOMY_TERMS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        String annatomyTermString = "";
        if (searchPattern == 1) { // start with
        	annatomyTermString = input + "%";
        } else if (searchPattern == 2) { // start string of constituent word(s) of the term
        	
        }
        try {
		    if (debug)
		    	System.out.println("MySQLPredictiveTextSearchDAOImp.sql = "+queryString.toLowerCase());
		    
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, annatomyTermString);
        	prepStmt.setInt(2, num);
        	resSet = prepStmt.executeQuery();
        	if (resSet.first()) {
        		result = new ArrayList<String>();
        		resSet.beforeFirst();
        		while (resSet.next()) {
        			String annatomyTerm = this.filterNoiseCharacters(resSet.getString(1));
        			result.add(annatomyTerm);
        		}
        	}
    		return result;
        	
        } catch (SQLException se) {
        	se.printStackTrace();
    		return null;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);        	
        }
    }
    
    /**
     * @author xingjun - 16/03/2009
     */
    public ArrayList<String> getGoTerms(String input, int searchPattern, int num) {
        ArrayList<String> result = null;
        ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("GO_TERMS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        String goTermString = "";
        if (searchPattern == 1) { // start with
        	goTermString = input + "%";
        } else if (searchPattern == 2) { // start string of constituent word(s) of the term
        	
        }
        try {
		    if (debug)
		    	System.out.println("MySQLPredictiveTextSearchDAOImp.sql = "+queryString.toLowerCase());
		    
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, goTermString);
        	prepStmt.setInt(2, num);
        	resSet = prepStmt.executeQuery();
        	if (resSet.first()) {
        		result = new ArrayList<String>();
        		resSet.beforeFirst();
        		while (resSet.next()) {
        			String goTerm = this.filterNoiseCharacters(resSet.getString(1));
        			result.add(goTerm);
        		}
        	}
    		return result;
        	
        } catch (SQLException se) {
        	se.printStackTrace();
    		return null;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);        	
        }
    	
    }
    
    // may need further refactoring
    /**
     * @author xingjun - 16/03/2009
     */
    private String filterNoiseCharacters(String targetString) {
    	final String[][] specialChars = {	{"<", "("}, 
    										{">", ")"}
    									};
    	if (targetString == null || targetString.equals("")) 
    		return targetString;
    	String result = targetString;
       	for (int i=0; i< specialChars.length; i++)
       		result = result.replaceAll(specialChars[i][0], specialChars[i][1]);
       	return result;
   	}

}
