package gmerg.db;

import java.sql.*;

public class ParamQuery{
    private boolean debug = false;

	private PreparedStatement prepStat;
	private String queryName; // descriptive name fo rquery retrieval
	private String querySQL;  // SQL for query
	
	public ParamQuery(String name, String querySQL) {
		this.queryName = name;
		this.querySQL = querySQL;
	}
	
	/*--key methods for getting SQL query--*/
	public PreparedStatement getPrepStat() {
		return prepStat;
	}
	
	public void setPrepStat(Connection conn) throws SQLException {
		if (debug) 
		    System.out.println("ParamQuery.sql = "+querySQL.toLowerCase());
		prepStat = conn.prepareStatement(querySQL);
	}
	
	/*--'get' methods for instance variables--*/
	public String getQueryName() {
		return this.queryName;
	}
	
	public String getQuerySQL() {
		    if (debug)
			System.out.println("ParamQuery.sql = "+querySQL.toLowerCase());

		return this.querySQL;
	}
	
	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}
}
