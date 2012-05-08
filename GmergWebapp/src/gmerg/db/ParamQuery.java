package gmerg.db;

import java.sql.*;

public class ParamQuery{
	
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
		prepStat = conn.prepareStatement(querySQL);
	}
	
	/*--'get' methods for instance variables--*/
	public String getQueryName() {
		return this.queryName;
	}
	
	public String getQuerySQL() {
		return this.querySQL;
	}
	
	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}
}
