package gmerg.assemblers;

import gmerg.db.AdvancedQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;

import java.sql.Connection;
import java.util.ArrayList;

public class SuggestionBoxAssembler {
    private boolean debug = false;
    public SuggestionBoxAssembler () {
	if (debug)
	    System.out.println("SuggestionBoxAssembler.constructor");

    }
	public ArrayList getAllAnatomyTerms(String geneSymbol)
	{
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		ArrayList list =  ishDAO.getAllAnatomyTerms(geneSymbol);
		DBHelper.closeJDBCConnection(conn);
        return list;
	}
	
	public ArrayList getWholeAnatomyTree(String startStage, String endStage)
	{
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		ArrayList list =  ishDAO.getWholeAnatomyTree(startStage, endStage);
		DBHelper.closeJDBCConnection(conn);
        return list;
	}
}
