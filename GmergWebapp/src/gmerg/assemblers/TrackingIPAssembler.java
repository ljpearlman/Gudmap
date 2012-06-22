package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.TrackingIPDAO;

import java.sql.Connection;
import java.util.ArrayList;

public class TrackingIPAssembler {
    private boolean debug = false;
    public TrackingIPAssembler() {
	if (debug)
	    System.out.println("TrackingIPAssembler.constructor");

    }
	public boolean updateIPLog(String ip, String viewid, String browser, String platform) {
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		TrackingIPDAO ipDAO = MySQLDAOFactory.getTrackingIPDAO(conn);
		
		// get data
		boolean result = ipDAO.updateIPLog(ip, viewid, browser, platform);
		
		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		ipDAO = null;
		
		/** ---return the composite value object---  */
		return result;
	}
}
