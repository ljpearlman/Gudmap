package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.FocusStageDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.Connection;

public class FocusGeneIndexAssembler {
	public Object[][] getGeneIndex(String prefix, String organ) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		FocusStageDAO focusStageDAO = MySQLDAOFactory.getFocusStageDAO(conn);
		
		// get data from database
		Object[][] browseSeries = focusStageDAO.getGeneIndex(prefix, organ);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		focusStageDAO = null;
		
		// return the value object
		return browseSeries;
	}
}
