package gmerg.db;

import gmerg.entities.submission.ish.ISHBatchSubmission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLTrackingIPDAOImp implements TrackingIPDAO{
    private boolean debug = false;

	private Connection conn;
	
	public MySQLTrackingIPDAOImp() {
		
	}
	
	// constructor with connection initialisation
	public MySQLTrackingIPDAOImp(Connection conn) {
		this.conn = conn;
	}
	
	public boolean updateIPLog(String ip, String viewid, String browser, String platform) {
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmt2 = null;
		ParamQuery parQ =
			TrackingIPQuery.getParamQuery("GET_IP_LOG");
		
		ParamQuery parQ2 =
			TrackingIPQuery.getParamQuery("UPDATE_LOG");
		
		ParamQuery parQ3 =
			TrackingIPQuery.getParamQuery("INSERT_LOG");
		
		String query = parQ.getQuerySQL();
		String query2 = parQ2.getQuerySQL();
		String query3 = parQ3.getQuerySQL();
		int updatedOrInsertedNumber = 0;
		if(null != ip && !ip.equals("") && null != viewid && !viewid.equals("")) {
			try {
		    if (debug)
			System.out.println("MySQLTrackingIPDAOImp.sql = "+query.toLowerCase());
				prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, ip);
				prepStmt.setString(2, viewid);
				resSet = prepStmt.executeQuery();
				if (resSet.first()) {//update
		    if (debug)
			System.out.println("MySQLTrackingIPDAOImp.sql = "+query2.toLowerCase());
					prepStmt2 = conn.prepareStatement(query2);
					prepStmt2.setString(1, browser);
					prepStmt2.setString(2, platform);
					prepStmt2.setString(3, ip);
					prepStmt2.setString(4, viewid);
					updatedOrInsertedNumber = prepStmt2.executeUpdate();
				} else {//insert
		    if (debug)
			System.out.println("MySQLTrackingIPDAOImp.sql = "+query3.toLowerCase());
					prepStmt2 = conn.prepareStatement(query3);
					prepStmt2.setString(1, ip);
					prepStmt2.setString(2, viewid);
					prepStmt2.setString(3, browser);
					prepStmt2.setString(4, platform);
					updatedOrInsertedNumber = prepStmt2.executeUpdate();
				}
				// release db resource
				DBHelper.closeResultSet(resSet);
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closePreparedStatement(prepStmt2);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return true;
	}
}
