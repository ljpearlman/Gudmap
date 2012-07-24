package gmerg.db;

import java.sql.*;

public class ParamQuery{
    private boolean debug = false;

	private PreparedStatement prepStat;
	private String queryName; // descriptive name fo rquery retrieval
	private String querySQL;  // SQL for query
	
	public ParamQuery(String name, String SQL) {
		this.queryName = name;
		this.querySQL = SQL;

		if (debug) {
		    if (null != querySQL) 
			System.out.println("ParamQuery.sql = "+querySQL.toLowerCase());
		}
	}
	
	/*--key methods for getting SQL query--*/
	public PreparedStatement getPrepStat() {

		return prepStat;
	}
	
	public void setPrepStat(Connection conn) throws SQLException {
		prepStat = conn.prepareStatement(querySQL);
	}
	
	/*--'get' methods for instance variables--*/
	public String getQueryName() {
		return this.queryName;
	}
	
	public String getQuerySQL() {
		return this.querySQL;
	}
	
	public void setQuerySQL(String SQL) {
	    if (debug) {
		if (null != SQL) {
		    if (null == querySQL ||  !querySQL.equals(SQL)) {
			System.out.println("ParamQuery.sql = "+SQL.toLowerCase());
		    }
		}
	    }

	    this.querySQL = SQL;
	}
}
