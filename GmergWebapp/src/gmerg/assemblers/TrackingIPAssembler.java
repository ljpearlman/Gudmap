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
		
		boolean result = false;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			TrackingIPDAO ipDAO = MySQLDAOFactory.getTrackingIPDAO(conn);
			result = ipDAO.updateIPLog(ip, viewid, browser, platform);
			return result;
		} 
		catch(Exception e){
			System.out.println("TrackingIPAssembler::updateIPLog failed !!!");
			return false;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
}
