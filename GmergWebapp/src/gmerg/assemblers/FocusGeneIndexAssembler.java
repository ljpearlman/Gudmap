package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.FocusStageDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.Connection;

public class FocusGeneIndexAssembler {
    private boolean debug = false;
    public FocusGeneIndexAssembler() {
	if (debug)
	    System.out.println("FocusGeneIndexAssembler.constructor");

    }
	public Object[][] getGeneIndex(String prefix, String organ) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		FocusStageDAO focusStageDAO;
		Object[][] browseSeries = null;
		try{
			focusStageDAO = MySQLDAOFactory.getFocusStageDAO(conn);
			
			// get data from database
			browseSeries = focusStageDAO.getGeneIndex(prefix, organ);
		}
		catch(Exception e){
			System.out.println("FocusGeneIndexAssembler::getGeneIndex !!!");
			browseSeries = null;
		}		

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		focusStageDAO = null;
		
		// return the value object
		return browseSeries;
	}
}
