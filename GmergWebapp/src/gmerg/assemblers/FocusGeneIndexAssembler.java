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
		try{
			FocusStageDAO focusStageDAO = MySQLDAOFactory.getFocusStageDAO(conn);
			Object[][] browseSeries = focusStageDAO.getGeneIndex(prefix, organ);
			return browseSeries;
		}
		catch(Exception e){
			System.out.println("FocusGeneIndexAssembler::getGeneIndex !!!");
			return null;
		}		
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
}
