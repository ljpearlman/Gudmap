/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.ish.ISHBrowseSubmission;
import gmerg.utils.table.GenericTableFilter;
import gmerg.utils.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import java.sql.DriverManager;
import java.util.ResourceBundle;

/**
 * @author xingjun
 *
 */
public final class DBHelper {
    private static boolean debug = false;
    ///??????????
	private static int count = 0;
	private static HashMap<Long, Connection> connMap = new HashMap<Long, Connection>();
	private static Properties prop = null;
	///???????????
	private static String browseColumnsISH[][] = {
		{"0", ""}, {"1", ""},{"2", ""}
	};
	
	private static String browseColumnsArray[][] = {
		{"0", ""}, {"1", ""}, {"2", ""}
	};
	
	private static String browseColumnsAll[][] = {
		{"0", ""}, {"1", ""}, {"2", ""}
	};
	
	// constructor
	private DBHelper() {
		
	}
		
	/**
	 * Create database connection
	 * @return
	 */
	public static Connection getDBConnection() {
	    Connection ret = null;

	    try {
			ResourceBundle bundle = ResourceBundle.getBundle("configuration");
			Class.forName(bundle.getString("db_driver"));
			prop = new Properties();
			prop.setProperty("useCompression","false");
			String url = bundle.getString("host") + bundle.getString("database");
			String userName = bundle.getString("user");
			String passWord = bundle.getString("password");
			ret = getDBConnection(url, userName, passWord, prop);
	    } catch (Exception se) {
			se.printStackTrace();
	    }

	    return ret;

	}
	
    public static Connection getDBConnection(String url, String userName, String passWord, Properties prop) {
		
		Connection conn = null;
		try {
			Date date = new Date();
			Long connectionTime = System.currentTimeMillis();
			url = url+"?user="+userName+"&password="+passWord+"";
			conn = DriverManager.getConnection(url, prop);
			count++;
			
			connMap.put(connectionTime, conn);
//			System.out.println(date.toString() +" key: " + connectionTime + " value: " + connMap.get(connectionTime) + " DB connections open " + count);

			if (20 < count){
			    System.out.println("!!!!!!possible leaking: "+count+" DB connections open");
			    
			    Iterator<Long> keySetIterator = connMap.keySet().iterator();
			    Long key = keySetIterator.next();
			    Long oldestKey = key;
			    while(keySetIterator.hasNext()){
			    	key = keySetIterator.next();
			    	if (oldestKey > key)
			    		oldestKey = key;
			    }
			    System.out.println("!!!!!!possible leaking: removed connection " + connMap.get(oldestKey));
			    closeJDBCConnection(connMap.get(oldestKey));
			}


		} catch (Exception se) {
			se.printStackTrace();
		} 
		
		return conn;
	}

	/**
	 * Release database connection
	 * @param conn
	 */
	public static void closeJDBCConnection(final Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				connMap.remove(conn);
				
				count--;
//		    	System.out.println("DBHelper:closeJDBCConnection "+conn + " DB connections open " + count);

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Release the prepared statement
	 * @param prepStmt
	 */
	public static void closePreparedStatement(final PreparedStatement prepStmt) {
		if (prepStmt != null) {
			try {
				prepStmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	/**
	 * Release the  statement
	 * @param stmt
	 */
	public static void closeStatement(final Statement stmt){
		if(stmt != null){
			try {
				stmt.close();
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Release the result set
	 * @param rs
	 */
	public static void closeResultSet(final ResultSet resSet) {
		if (resSet != null) {
			try {
				resSet.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<String[]> formatResultSetToArrayList(ResultSet resSet) throws SQLException {
		
		ResultSetMetaData resSetData = resSet.getMetaData();
		int columnCount = resSetData.getColumnCount();
		
		if (resSet.first()) {
			
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<String[]>();
			int i = 0;
			String[] columns = null;
			String str = null;
			while (resSet.next()) {
				columns = new String[columnCount];
				for (i = 0; i < columnCount; i++) {
					str = resSet.getString(i + 1);
					if (null == str)
					    str = "";
					else {
					    str = str.trim();
					    if (str.equalsIgnoreCase("null"))
						str = "";
					}

					columns[i] = str;
				}
				results.add(columns);
			}
			return results;
			
		}
		return null;
	}
	
    /**
     * <p>modified by xingjun - 03/09/2008</p>
     * <p>rename from formatResultSet to formatResultSetToArrayList</p>
     * @param resSet
     * @param length
     * @return
     * @throws SQLException
     */
    public static ArrayList<String[]> formatResultSetToArrayList(ResultSet resSet,int length) throws SQLException {     
        if (resSet.first()) {
            resSet.beforeFirst();           
            ArrayList<String[]> results = new ArrayList<String[]>();
            String str = null;
            String[] columns = null;
            int i = 0;
            while (resSet.next()) {
                columns = new String[length];
                for(i = 1; i <= length; i++) {
		    str = resSet.getString(i);
		    if (null == str)
			str = "";
		    else {
			str = str.trim();
			if (str.equalsIgnoreCase("null"))
			    str = "";
			else if (str.equalsIgnoreCase("itg"))
			    str = "Tg";
		    }

		    columns[i - 1] = str;
                }
                if(null != columns)
		    results.add(columns);
            }

            return results;
        }
        return null;
    }
	
	/**
	 * @author xingjun - 03/12/2008
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
    public static String[] formatResultSetToStringArray(ResultSet resSet) throws SQLException {
	String str = null;
    	if (resSet.first()) {
    		resSet.beforeFirst();
			ArrayList<String> results = new ArrayList<String>();
    		while (resSet.next()) {
		    str = Utility.netTrim(resSet.getString(1));
			if (null != str)
			    results.add(str);
    		}
    		if (results != null && results.size() > 0) {
    			return results.toArray(new String[0]);
    		}
    	}
		return null;
	}
	
	/**
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	public static String formatResultSetToString(ResultSet resSet) throws SQLException {
		
		String result = null;
		if (resSet.first()) {
			resSet.beforeFirst();
			result = new String("");
			while (resSet.next()) {
				result += resSet.getString(1) + " ";
			}
			return result;
		}
		return null;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	public static ISHBrowseSubmission[] formatISHBrowseResultSet(ResultSet resSet) throws SQLException {
		
		if (resSet.first()) {
			resSet.last();
			int arraySize = resSet.getRow();
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create array to store each row of results in
			ISHBrowseSubmission [] results = new ISHBrowseSubmission[arraySize];
			
			int i = 0;
			while (resSet.next()) {
				
				// option 1: initialise a ish browse submission object and add it into the array
				// false is the default value of the 'selected'
				ISHBrowseSubmission ishBrowseSubmission = new ISHBrowseSubmission(); 
				ishBrowseSubmission.setId(resSet.getString(1));
				ishBrowseSubmission.setGeneSymbol(resSet.getString(2));
				ishBrowseSubmission.setStage(resSet.getString(3));
				ishBrowseSubmission.setAge(resSet.getString(4));
				ishBrowseSubmission.setLab(resSet.getString(5));
				ishBrowseSubmission.setAssay(resSet.getString(6));
				ishBrowseSubmission.setSpecimen(resSet.getString(7));
				ishBrowseSubmission.setDate(resSet.getString(8));
				ishBrowseSubmission.setThumbnail(resSet.getString(9));
				ishBrowseSubmission.setSelected(false);
				results[i] = ishBrowseSubmission;
				i++;
				 
		    }
		    return results;
		}
	    return null;
    }
	
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    public static ArrayList formatBrowseResultSetISH(ResultSet resSet) throws SQLException {
    	
        if (resSet.first()) {
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create array to store each row of results in
            ArrayList<String[]> results = new ArrayList<String[]>();                        
            
            while (resSet.next()) {
            	String[] ishBrowseSubmission = new String[13];
         	
                ishBrowseSubmission[ 0] = resSet.getString(1); // symbol
                ishBrowseSubmission[ 1] = resSet.getString(2); // id
                ishBrowseSubmission[ 2] = resSet.getString(3); // source
                ishBrowseSubmission[ 3] = resSet.getString(4); // date
                String assay = resSet.getString(5);
                ishBrowseSubmission[ 4] = (assay.equals("TG")? "Tg":assay); // assay
                ishBrowseSubmission[ 5] = resSet.getString(6); // probe name
                ishBrowseSubmission[ 6] = resSet.getString(7); // ts
                ishBrowseSubmission[ 7] = resSet.getString(8); // age
                ishBrowseSubmission[ 8] = resSet.getString(9); // sex name
                ishBrowseSubmission[ 9] = resSet.getString(10); // genotype
                ishBrowseSubmission[10] = resSet.getString(11); // insitu strength
        		String expression = resSet.getString(11); // insitu strength
        		if (expression.contains("present"))
        			ishBrowseSubmission[10] = "present";
        		else if (expression.contains("uncertain"))
        			ishBrowseSubmission[10] = "uncertain";
        		else if (expression.contains("not detected"))
        			ishBrowseSubmission[10] = "not detected";
        		else
        			ishBrowseSubmission[10] = "";
                
                ishBrowseSubmission[11] = resSet.getString(12); // speciman
                ishBrowseSubmission[12] = resSet.getString(13); // thumbnail
                
                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }
    
 public static ArrayList formatLabBrowseResultSetISH(ResultSet resSet) throws SQLException {
    	
        if (resSet.first()) {
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create array to store each row of results in
            ArrayList<String[]> results = new ArrayList<String[]>();                        
            
            while (resSet.next()) {
            	String[] ishBrowseSubmission = new String[14];
         	
                ishBrowseSubmission[ 0] = resSet.getString(1); // symbol
                ishBrowseSubmission[ 1] = resSet.getString(2); // id
                ishBrowseSubmission[ 2] = resSet.getString(3); // source
                ishBrowseSubmission[ 3] = resSet.getString(4); // date
                String assay = resSet.getString(5);
                ishBrowseSubmission[ 4] = (assay.equals("TG")? "Tg":assay); // assay
                ishBrowseSubmission[ 5] = resSet.getString(6); // probe name
                ishBrowseSubmission[ 6] = resSet.getString(7); // ts
                ishBrowseSubmission[ 7] = resSet.getString(8); // age
                ishBrowseSubmission[ 8] = resSet.getString(9); // sex name
                ishBrowseSubmission[ 9] = resSet.getString(10); // genotype
                ishBrowseSubmission[10] = resSet.getString(11); // tissue
        		String expression = resSet.getString(12); // insitu strength
        		if (expression.contains("present"))
        			ishBrowseSubmission[11] = "present";
        		else if (expression.contains("uncertain"))
        			ishBrowseSubmission[11] = "uncertain";
        		else if (expression.contains("not detected"))
        			ishBrowseSubmission[11] = "not detected";
        		else
        			ishBrowseSubmission[11] = "";
                
                ishBrowseSubmission[12] = resSet.getString(13); // speciman
                ishBrowseSubmission[13] = resSet.getString(14); // thumbnail
                
                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }
    
    public static ArrayList formatBrowseResultSetISHForAnnotation(ResultSet resSet) throws SQLException {
    	
        if (resSet.first()) {
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create array to store each row of results in
            ArrayList<String[]> results = new ArrayList<String[]>();
            
            while (resSet.next()) {
            	String[] ishBrowseSubmission = null;
            		ishBrowseSubmission = new String[14];

                ishBrowseSubmission[ 0] = resSet.getString(1); // id
                ishBrowseSubmission[ 1] = resSet.getString(2); // symbol
                ishBrowseSubmission[ 2] = resSet.getString(3); // ts
                ishBrowseSubmission[ 3] = resSet.getString(4); // age
                ishBrowseSubmission[ 4] = resSet.getString(5); // source
                ishBrowseSubmission[ 5] = resSet.getString(8); // date
                ishBrowseSubmission[ 6] = resSet.getString(7); // assay
                ishBrowseSubmission[ 7] = resSet.getString(6); // specimen
                ishBrowseSubmission[ 8] = resSet.getString(11); // sex
                ishBrowseSubmission[ 9] = resSet.getString(12); // probe name
                ishBrowseSubmission[10] = resSet.getString(13); // genotype
                ishBrowseSubmission[11] = resSet.getString(14); // probe type
                ishBrowseSubmission[12] = resSet.getString(9); // thumbnail
                ishBrowseSubmission[13] = resSet.getString(15); //SUB_IS_PUBLIC

                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }
    
    /**
	 * 1. mapping the order by tuples
	 * 2. find the offset and number of records wanted
	 * 
	 * @param queryType: 2 - for component query; 1 - for browse and gene query
	 * @param query: sql string
	 * @param defaultOrder: if no 'order-by' specified, use it
	 * @param order: a 1-dimension array stores the to-be-ordered item and its order
	 * @param offset: start position of the rows to be retrieved
	 * @param num: number of the rows to be retrieved
	 * @return
	 */
	public static String assembleBrowseSubmissionQueryStringISH(int queryType, String query,
			String defaultOrder, String[] order, String offset, String num) {
		
		String queryString = null;
		
		// order by
		if (order != null) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseSubmissionOrderByColumnISH(queryType, order);
			
			queryString += column;
			
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
			queryString = query + defaultOrder+ ", STG_STAGE_DISPLAY";
		}
		
		// offset and retrieval number
		if (offset != null) {
			if (!offset.equalsIgnoreCase("ALL")) {
				
				if (isValidInteger(offset)) {
					int os = Integer.parseInt(offset) - 1;
					queryString = queryString + " LIMIT " + Integer.toString(os);
				} else {
					queryString = queryString + " LIMIT 0";
				}
				if (isValidInteger(num)) {
					queryString = queryString + " ," + num;
				} else {
					queryString = queryString + " ," + "100000000";
				}
			}
		}
		
		// return assembled query string
		return queryString;
	}
	
	public static String assembleBrowseSubmissionQueryStringISH(int queryType, String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
		
		String queryString = null;
		// order by
		if (columnIndex != -1) {
			queryString = query + " GROUP BY SUB_ACCESSION_ID ORDER BY ";
			
			// translate parameters into database column names
			String column = getLabOrderByColumnISH(columnIndex, ascending, defaultOrder);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
			queryString = query + defaultOrder + ", STG_STAGE_DISPLAY";
		}
		
		// offset and retrieval number
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	/**
	 * @author xingjun - 11/08/2008
	 * @param queryType
	 * @param query
	 * @param defaultOrder
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @param filter
	 * @return
	 */
	public static String assembleBrowseSubmissionQueryStringISH(int queryType, String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num,
			GenericTableFilter filter) {
		
		String queryString = null;
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// add extra query criteria based on filter
			String filterString = "";
			if (filter != null) {
				int len = filter.getFilters().size();
				for (int i=0;i<len;i++) {
					
				}
			}
			
			// translate parameters into database column names
			String column = getBrowseSubmissionOrderByColumnISH(queryType, columnIndex, ascending, defaultOrder);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
			queryString = query + defaultOrder + ", STG_STAGE_DISPLAY";
		}
		
		// offset and retrieval number
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	/**
	 * 
	 * @param order
	 * @param queryType: 1- for browse and gene query; 2- for component query; 3- for component count query
	 * @return
	 */
	private static String getBrowseSubmissionOrderByColumnISH(int queryType, String[] order) {
		
		String column = new String("");
		String geneSymbolCol;
                
                if(queryType == 1 || queryType == 2){
                    geneSymbolCol = "natural_sort(TRIM(RPR_SYMBOL))";
                }
                else {
                    geneSymbolCol = "";
                }
		
		// start to translate
		if (queryType == 3) {
			if (order[0].equals("byPath")) {
				column = "APO_FULL_PATH " + order[1];
			} else if (order[0].equals("byGene")) {
				column = "TRIM(RPR_SYMBOL) " + order[1];
			} else if (order[0].equals("byCompID")) {
				column = "CAST(SUBSTRING(EXP_COMPONENT_ID, INSTR(EXP_COMPONENT_ID,':')+1) AS SIGNED) " + order[1];
			} else if (order[0].equals("byTS")) {
				column = "STG_STAGE_DISPLAY " + order[1];
			} else if (order[0].equals("byGroup")) {
				column = "COUNT(SUB_ACCESSION_ID) " + order[1];
			} else {
				column = "APO_FULL_PATH" + order[1];
			}
		} else {
			if(order[0].equals("byID")) {
				if (queryType == 1 || queryType == 2) {
					column = "CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS SIGNED) " + order[1] +", " + geneSymbolCol; 
				}
			} else if (order[0].equals("byGene")) {
				if (queryType == 1 || queryType == 2) {
					column = geneSymbolCol + " " + order[1] +", STG_STAGE_DISPLAY "; 
				}
			} else if (order[0].equals("byTS")) {
				column = "STG_STAGE_DISPLAY" + " " + order[1] +", " + geneSymbolCol; 
			} else if (order[0].equals("byStage")) {
				column = "STG_STAGE_DISPLAY" + " " + order[1] +", " + geneSymbolCol; 
			} else if (order[0].equals("byAge")) {
				column = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" + " " + order[1] +", " + geneSymbolCol; 
			} else if (order[0].equals("byLab")) {
				column = "SUB_SOURCE" + " " + order[1] +", " + geneSymbolCol; 
			} else if (order[0].equals("byDate")) {
				column = "SUB_SUB_DATE" + " " + order[1] +", " + geneSymbolCol; 
			} else if (order[0].equals("byAssay")) {
				column = "SUB_ASSAY_TYPE" + " " + order[1] +", " + geneSymbolCol; 
			} else if (order[0].equals("bySpecimen")) {
				column = "SPN_ASSAY_TYPE" + " " + order[1] +", " + geneSymbolCol; 
			} else {
				if (queryType == 1 || queryType == 2) {
					column = geneSymbolCol + ", STG_STAGE_DISPLAY "; 
				}
			}
		}
		return column;
	}
		
	/**
	 * @param queryType: 1- for browse and gene query; 2- for component query; 3- for component count query
	 * @param columnIndex
	 * @param ascending
	 * @param defaultOrder
	 * @return
	 */
	private static String getBrowseSubmissionOrderByColumnISH(int queryType, 
			int columnIndex, boolean ascending, String defaultOrder) {
		
		String column = new String("");
		String order = (ascending == true ? "ASC" : "DESC");
		String geneSymbolCol;
		
		if(queryType == 1 || queryType == 2){
			geneSymbolCol = "natural_sort(TRIM(RPR_SYMBOL))";
		} else {
			geneSymbolCol = "";
		}
		
		// start to translate
		if (queryType == 3) { // component count query
			if (columnIndex == 0) { // path
				column += defaultOrder;
			} else if (columnIndex == 1) { // gene
				column = "TRIM(RPR_SYMBOL) " + order + ", " + defaultOrder;
			} else if (columnIndex == 2) { // component id
				column += "CAST(SUBSTRING(EXP_COMPONENT_ID, INSTR(EXP_COMPONENT_ID,':')+1) AS SIGNED) " + 
				order + ", " + defaultOrder;
			} else if (columnIndex == 3) { // stage
				column += "STG_STAGE_DISPLAY " + order + ", " + defaultOrder;
			} else if (columnIndex == 4) { // group
				column += "COUNT(SUB_ACCESSION_ID) " + order + ", " + defaultOrder;
			} else { // path by default
				column = defaultOrder;
			}
		} else { // gene or component query
			if(columnIndex == 0) { // gudmap id
				if (queryType == 1) {
					column = "CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS SIGNED) "
					+ order +", " + geneSymbolCol; 
				} else if (queryType == 2) {
					column = "CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS SIGNED) " + order + ", " + geneSymbolCol;
				}
			} else if (columnIndex == 1) { // gene
				if (queryType == 1 || queryType == 2) {
					column = geneSymbolCol + " " + order +", STG_STAGE_DISPLAY "; 
				} 
			} else if (columnIndex == 2) { // stage 
				column = "STG_STAGE_DISPLAY" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 3) { // age
				column = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 4) { // source
				column = "SUB_SOURCE" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 5) { // submission date
				column = "SUB_SUB_DATE" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 6) { // assay type
				column = "SUB_ASSAY_TYPE" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 7) { // specimen
				column = "SPN_ASSAY_TYPE" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 9) { // probe name
				column = "NATURAL_SORT(TRIM(RPR_JAX_ACC))" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 8) { // sex
				column = "SPN_SEX" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 10) { // genotype
				column = "SPN_WILDTYPE" + " " + order +", " + geneSymbolCol; 
			} else if (columnIndex == 11) { // probe type
				column = "PRB_PROBE_TYPE" + " " + order +", " + geneSymbolCol; 
			} else {
				if (queryType == 1 || queryType == 2) {
					column = geneSymbolCol + ", STG_STAGE_DISPLAY "; 
				} 
			}
		}
		return column;
	} // end of getBrowseSubmissionOrderByColumnISH

	private static String getLabOrderByColumnISH(int columnIndex, boolean ascending, String defaultOrder) {
		
		String column = new String("");
		String order = (ascending == true ? "ASC" : "DESC");
		String geneSymbolCol = "natural_sort(TRIM(RPR_SYMBOL))";
		
		
		// start to translate
		if(columnIndex == 0) // symbol
			column = geneSymbolCol + " " + order +", STG_STAGE_DISPLAY ";
		else if (columnIndex == 1)  // gudmap id
			column = "CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS SIGNED) " + order + ", " + geneSymbolCol;
		else if (columnIndex == 2)  // source
			column = "SUB_SOURCE" + " " + order +", " + geneSymbolCol; 
		else if (columnIndex == 3) // submission date
			column = "SUB_SUB_DATE" + " " + order +", " + geneSymbolCol; 
		else if (columnIndex == 4)  // assay type
			column = "SUB_ASSAY_TYPE" + " " + order +", " + geneSymbolCol;
		else if (columnIndex == 5)  // probe name
			column = "NATURAL_SORT(TRIM(RPR_JAX_ACC))" + " " + order +", " + geneSymbolCol;
		else if (columnIndex == 6)  // stage 
			column = "STG_STAGE_DISPLAY" + " " + order +", " + geneSymbolCol; 
		else if (columnIndex == 7)  // age
			column = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" + " " + order +", " + geneSymbolCol; 
		else if (columnIndex == 8)  // sex
			column = "SPN_SEX" + " " + order +", " + geneSymbolCol; 
		else if (columnIndex == 9)  // genotype
			column = "SPN_WILDTYPE" + " " + order +", " + geneSymbolCol;
		else if (columnIndex == 10)  // ish expression
			column = "EXP_STRENGTH" + " " + order +", " + geneSymbolCol;
		else if (columnIndex == 11)  // specimen
			column = "SPN_ASSAY_TYPE" + " " + order +", " + geneSymbolCol;
		else
			column = geneSymbolCol + " " + order +", STG_STAGE_DISPLAY ";
			
		return column;
	} 
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isValidInteger(String value) {
		
		boolean result = true;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			result = false;
		}
		return result;
	}
	
	public static ArrayList buildTreeStructure(ResultSet resSet, int hasAnnot, String accno, boolean isEditor) throws SQLException {
		return buildTreeStructure(resSet, hasAnnot, accno, isEditor, "showComponentID");
	}
	
	/**
	 * 
	 * @param resSet - query result for the tree
	 * @param hasAnnot - if annotated
	 * @param accno - submission id
	 * @param isEditor - if used for editing
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList buildTreeStructure(ResultSet resSet, int hasAnnot, String accno, boolean isEditor, String method) throws SQLException {
		
		if (resSet.first()) {
			
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			ArrayList<String> results = new ArrayList<String>();
			
			//contains the javascript function for the tree
			String javascriptFunc = "";
			String additionalInfo = "";
			
			//javascript function changes depending on whether tree is annotated or not
			if(hasAnnot == 0) {
				results.add("USETEXTLINKS =1");
				results.add("ANNOTATEDTREE = 0");
				results.add("OPENATVISCERAL = 1");
				//javascriptFunc = "javascript:showComponentID";
				javascriptFunc = "javascript:" + method;
			} else {
				results.add("USETEXTLINKS =0");
				results.add("ANNOTATEDTREE = 1");
				results.add("OPENATVISCERAL = 0");
				results.add("SUBMISSION_ID = \""+accno+"\"");
				javascriptFunc = "javascript:showExprInfo";
			}
			
			results.add("STARTALLOPEN = 0");
			results.add("USEFRAMES = 0");
			results.add("USEICONS = 1");
			results.add("HIGHLIGHT = 1");
			results.add("PRESERVESTATE = 0");
			
			while (resSet.next()) {
				
				if(hasAnnot == 0) {
					additionalInfo = resSet.getString(5);
				} else {
					additionalInfo = "("+resSet.getString(3)+")";
				}
				
				int row = resSet.getRow() - 1;
				String componentName = resSet.getString(4);
				String id = resSet.getString(3);
				
				//resSet.getInt(1) tells you the depth of the component in the tree so
		        //each line of javasciprt code produced is determined by this
				if (resSet.getInt(1) == 0) {
					results.add("foldersTree = gFld(\"" + componentName + " " +
							additionalInfo +
							"\", \""+javascriptFunc+"(&#39;" + id +
							"&#39;,&#39;" + componentName + "&#39;,&#39;" + row +
							"&#39;)\","+resSet.getInt(7)+"," + resSet.getInt(8) + "," + resSet.getInt(9) +
		                    ")");
				
				} else if (resSet.getInt(1) > 0) {
					
					//if statement to determine whether the component has children. If it does,
					//it will be displayed as a folder in the tree.
					if (resSet.getInt(6) > 0) {
						//if the parent of the component is at depth 0, add 'foldersTree' to the code
						if (resSet.getInt(1) == 1) {
							results.add("aux1 = insFld(foldersTree, gFld(\"" + componentName +
									" " + additionalInfo +
									"\", \""+javascriptFunc+"(&#39;" + id +
									"&#39;,&#39;" +
									componentName + "&#39;,&#39;" + row + "&#39;)\"," +
									resSet.getInt(7) + "," + resSet.getString(8) + "," + resSet.getString(9) +
									"))");
						} else {
							results.add("aux" + resSet.getInt(1) + " = insFld (aux" +
									(resSet.getInt(1) - 1) + ", gFld(\"" + componentName +
									" " + additionalInfo +
									"\", \""+javascriptFunc+"(&#39;" + id +
									"&#39;,&#39;" +
									componentName + "&#39;,&#39;" + row + "&#39;)\"," +
									resSet.getInt(7)+","+resSet.getString(8) + "," + resSet.getString(9) +
									"))");
						}
					} else {
						if (resSet.getInt(1) == 1) {
							results.add("insDoc(foldersTree, gLnk(\"S\", \"" + componentName +
									" " + additionalInfo +
									"\", \""+javascriptFunc+"(&#39;" + id +
									"&#39;,&#39;" + componentName + "&#39;,&#39;" + row +
									"&#39;)\"," +resSet.getInt(7)+"," + resSet.getString(8) + "," +
									resSet.getString(9) + "))");
						} else {
							results.add("insDoc(aux" + (resSet.getInt(1) - 1) +
									", gLnk(\"S\", \"" + componentName + " " +
									additionalInfo +
									"\", \""+javascriptFunc+"(&#39;" + id +
									"&#39;,&#39;" + componentName + "&#39;,&#39;" + row +
									"&#39;)\"," +resSet.getInt(7)+"," + resSet.getString(8) + "," +
									resSet.getString(9) + "))");
						}
					}
				}
			}
			return results;
		}
	    return null;
    }
	/**
	 * modified by xingjun on 01/11/2007
	 * need add another column, namely sex. speak to ying
	 * @param orderby
	 * @param asc
	 * @return
	 */	
    public static String orderResult(String[] order) {
		String orderStr = null;
		if(null != order) {
			String col = order[1];
//			String orderBy = " " + order[0] + " ";
			if(Integer.parseInt(col) < 0) {
				orderStr = " order by " + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("0")) { // by gene
				orderStr = " order by col1 " + order + ", col14, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6, col2 ";
			} else if(col.equals("1")) { // by gudmap id
				orderStr = " order by NATURAL_SORT(col10) " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort(); 
			} else if(col.equals("2")) { // by source
				orderStr = " order by col4 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("3")) { // by submission date
				orderStr = " order by col5 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("4")) { // by assay type
				orderStr = " order by col14 " + order + ", col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6, col2 ";
			} else if(col.equals("5")) { // by probe name
				orderStr = " order by col16 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col2 ";
			} else if(col.equals("6")) { // by stage
				orderStr = " order by col6 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col2 ";
			} else if(col.equals("7")) { // by age
				orderStr = " order by col8 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("8")) { // by sex
				orderStr = " order by col15 " + order + ", col14, col1, CASE col3 WHEN 'present' THEN 1 WHEN 'uncertain' THEN 2 WHEN 'not detect' THEN 3 ELSE 4 END, col6";
			} else if(col.equals("9")) { // by genotype
				orderStr = " order by col17 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6 ";
			} else if(col.equals("10")) { // by tissue
				orderStr = " order by col2 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6 ";
			} else if(col.equals("11")) { // by expression ish
				orderStr = " order by col3 " + order + ", col14, col1, col6, col2 ";
			} else if(col.equals("12")) { // by expression microarray
				orderStr = " order by col12 " + order + ", col14, col1, col6, col2 ";
			} else if(col.equals("13")) {// by specimen type (wholemount, e.g.)
				orderStr = " order by col7 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("14")) { // by image
				orderStr = " order by col9 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			}
		}
		return orderStr;
    }	
    
    /**
     * rewitten by xingjun - 15/11/2007
     * Bernie 06/03/2012 - (Mantis 327) mod to allow sort by sex column
     * @param order
     * @return
     */
    public static String orderResult(int orderby, boolean asc) {
		String orderStr = null;
		//System.out.println("ORDERBY:"+orderby);
		String col = String.valueOf(orderby);
		String order = (asc == true ? " ASC ": " DESC ");
			if(orderby < 0) {
				orderStr = " order by " + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("0")) { // by gene
				orderStr = " order by col1 " + order + ", col14, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6, col2 ";
			} else if(col.equals("1")) { // by gudmap id
				orderStr = " order by NATURAL_SORT(col10) " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort(); 
			} else if(col.equals("2")) { // by source
				orderStr = " order by col4 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("3")) { // by submission date
				orderStr = " order by col5 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("4")) { // by assay type
				orderStr = " order by col14 " + order + ", col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6, col2 ";
			} else if(col.equals("5")) { // by probe name
				orderStr = " order by col16 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col2 ";
			} else if(col.equals("6")) { // by stage
				orderStr = " order by col6 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col2 ";
			} else if(col.equals("7")) { // by age
				orderStr = " order by col8 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("8")) { // by sex
				orderStr = " order by col15 " + order + ", col14, col1, CASE col3 WHEN 'present' THEN 1 WHEN 'uncertain' THEN 2 WHEN 'not detect' THEN 3 ELSE 4 END, col6";
			} else if(col.equals("9")) { // by genotype
				orderStr = " order by col17 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6 ";
			} else if(col.equals("10")) { // by tissue
				orderStr = " order by col2 " + order + ", col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6 ";
			} else if(col.equals("11")) { // by expression ish
				orderStr = " order by col3 " + order + ", col14, col1, col6, col2 ";
			} else if(col.equals("12")) { // by expression microarray
				orderStr = " order by col12 " + order + ", col14, col1, col6, col2 ";
			} else if(col.equals("13")) {// by specimen type (wholemount, e.g.)
				orderStr = " order by col7 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			} else if(col.equals("14")) { // by image
				orderStr = " order by col9 " + order + "," + AdvancedSearchDBQuery.getBothDefaultSort();
			}
		return orderStr;
    }	
    
	/**
	 * @author xingjun - 02/02/2009
	 * @param emapIds
	 * @return
	 */
    public static String assembleComponentString(String[] emapIds) {
    	String ids = "";
    	String componentString = null;
    	for(int j = 0; j < emapIds.length; j++) {
    		ids += "'"+emapIds[j] + "',";
    	}
    	if(emapIds.length >= 1) {
    		ids = ids.substring(0, ids.length()-1);
    	}
    	componentString = " (SELECT DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
    	" FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
    	" ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
    	" WHERE ANCES_ATN.ATN_PUBLIC_ID IN (" + ids + ") " +
    	" AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
    	" AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
    	" AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
    	" AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
    	" AND APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = TRUE) ";
    	return componentString;
    }
    
    /**
     * @author xingjun - 11/03/2009
     * @param conn
     * @return
     */
    public static Connection reconnect2DB(Connection conn) {
	Connection ret = conn;

	try {
	    if (conn == null || conn.isClosed()) {
	    	System.out.println("DBHelper:reconnect2DB reconnect "+conn);
	    	
		ret = getDBConnection();
	    } 
	} catch (Exception e) {ret = null;}

	return ret;
    }
    
	/**
	 * @author xingjun - 08/10/2009
	 * @param itemList
	 * @return
	 */
    public static String convertItemsFromArrayListToString(ArrayList itemList) {
		if (itemList == null || itemList.size() == 0) {
			return null;
		}
		int len = itemList.size();
		String result = "'";
		for (int i=0;i<len;i++) {
			result += itemList.get(i) + "', '";
		}
		result = result.substring(0, result.length()-3);
		return result;
	}

	/**
	 * @author xingjun - 13/10/2009
	 * users with different privileges will be able to see entries in different status
	 * pre = 2 can view entries with status <= 4
	 * pre = 3 can view entries with status <= 19
	 * pre = 4 can view entries with status <= 21
	 * pre = 5 can view entries with status <= 23
	 * pre = 6/7 can view entries with status <= 25
	 * @param userPrivilege
	 * @return
	 */
	public static int getSubStatusByPrivilege(int userPrivilege) {
		int subStatus = 0;
		if (userPrivilege == 2) {
			subStatus = 4;
		} else if (userPrivilege == 3) {
			subStatus = 19;
		} else if (userPrivilege == 4) {
			subStatus = 21;
		} else if (userPrivilege == 5) {
			subStatus = 23;
		} else if (userPrivilege == 6 || userPrivilege == 7) {
			subStatus = 25;
		} else {
			subStatus = 4;
		}
		return subStatus;
	}
	
	
	
}
