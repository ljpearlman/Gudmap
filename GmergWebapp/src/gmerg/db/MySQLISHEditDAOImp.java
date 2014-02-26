/**
 * 
 */
package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gmerg.entities.submission.Probe;
import gmerg.utils.Utility;

/**
 * @author xingjun
 *
 */
public class MySQLISHEditDAOImp implements ISHEditDAO {

    private Connection conn;

    // default constructor
	public MySQLISHEditDAOImp() {
		
	}
	
	public MySQLISHEditDAOImp(Connection conn) {
		this.conn = conn;
	}
	
	public boolean signOffAnnotation(String submissionId, String status) {
		// initialising
		ParamQuery parQ = DBUpdateSQL.getParamQuery("SIGN_OFF_ANNOTATION");
        PreparedStatement prepStmt = null;
        int deletedSubmissionNumber;
        // assemble sql string
        String queryString = parQ.getQuerySQL();
        
        try {
        	conn = DBHelper.reconnect2DB(conn); // in case lost the connection
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, status);
        	prepStmt.setString(2, submissionId);
        	deletedSubmissionNumber = prepStmt.executeUpdate();
        	
    		return true;
        	
        } catch(SQLException se) {
        	se.printStackTrace();
        	return false;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);
        }

	}
	
	public boolean signOffAnnotation(String submissionId, String oldStatus, String newStatus) {
		// initialising
		ParamQuery parQ = DBUpdateSQL.getParamQuery("SIGN_OFF_ANNOTATION_STATUS");
        PreparedStatement prepStmt = null;
        int deletedSubmissionNumber;
        // assemble sql string
        String queryString = parQ.getQuerySQL();
        
        try {
        	conn = DBHelper.reconnect2DB(conn); // in case lost the connection
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, newStatus);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, oldStatus);
        	deletedSubmissionNumber = prepStmt.executeUpdate();
        	
    		return true;	
    		
        } catch(SQLException se) {
        	se.printStackTrace();
        	return false;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);        	
        }
	}
	
	public boolean setPublicSubmission(String submissionId, String status) {
		// initialising
		ParamQuery parQ = DBUpdateSQL.getParamQuery("SET_PUBLIC_SUBMISSION");
        PreparedStatement prepStmt = null;
        int updatedSubmissionNumber;
        // assemble sql string
        String queryString = parQ.getQuerySQL();
        
        try {
        	conn = DBHelper.reconnect2DB(conn); // in case lost the connection
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, status);
        	prepStmt.setString(2, submissionId);
        	updatedSubmissionNumber = prepStmt.executeUpdate();
    		return true;	
    		
        } catch(SQLException se) {
        	se.printStackTrace();
        	return false;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);        	
        }
	}
	
	public boolean signOffAnnotationAndSetPublicSubmission(String submissionId, String dbstatus, String substatus) {

        PreparedStatement prepStmt2 = null;
        PreparedStatement prepStmt = null;
		
		try {        	
        	conn = DBHelper.reconnect2DB(conn); // in case lost the connection
        	conn.setAutoCommit(false);
        	
        	//initialising
    		ParamQuery parQ2 = DBUpdateSQL.getParamQuery("SIGN_OFF_ANNOTATION");
            int deletedSubmissionNumber2;
            // assemble sql string
            String queryString2 = parQ2.getQuerySQL();
            
            prepStmt2 = conn.prepareStatement(queryString2);
        	prepStmt2.setString(1, dbstatus);
        	prepStmt2.setString(2, submissionId);
        	deletedSubmissionNumber2 = prepStmt2.executeUpdate();
        	
    		ParamQuery parQ = DBUpdateSQL.getParamQuery("SET_PUBLIC_SUBMISSION");
            int deletedSubmissionNumber;
            // assemble sql string
            String queryString = parQ.getQuerySQL();
            
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, substatus);
        	prepStmt.setString(2, submissionId);
        	deletedSubmissionNumber = prepStmt.executeUpdate();
        	    	        	            
            conn.commit();
    		return true;
            
        } catch(Exception se) {
        	se.printStackTrace();
    		return false;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(Exception se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closePreparedStatement(prepStmt2);
        }
	}	
	
	/**
	 * @author xingjun
	 * 
	 */
	public boolean deleteSelectedSubmission(String[] selectedSubmissions) {
		if (selectedSubmissions == null || selectedSubmissions.length == 0) {
			return false;
		}

		// if there's no selected submission
		int submissionNumber = selectedSubmissions.length;
//		System.out.println("submission no.: " + submissionNumber);
        if (submissionNumber < 1) {
        	return false;
        }
        
		// initialising
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_SELECTED_SUBMISSIONS");
        PreparedStatement prepStmt = null;
        int deletedSubmissionNumber = 0;
        
        // assemble sql string
        String queryString = parQ.getQuerySQL();
        
        // append submission id condition
        if (submissionNumber == 1) {
        	queryString += "SUB_ACCESSION_ID = '" + selectedSubmissions[0] + "'";
        } else {
        	queryString += "SUB_ACCESSION_ID IN ('" + selectedSubmissions[0] + "'";
        	for (int i=1;i<submissionNumber;i++) {
        		queryString += ", '" + selectedSubmissions[i] + "'";
        	}
        	queryString += ") ";
        }
//        System.out.println("delete selected submission: " + queryString);
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	deletedSubmissionNumber = prepStmt.executeUpdate();

            if (deletedSubmissionNumber != submissionNumber) {
            	return false;
            }
    		return true;
        	
        } catch(SQLException se) {
        	se.printStackTrace();
        	return false;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);      	
        }
	} // end of deleteSelectedSubmission
	
	/** methods linked to annotation editing **/
	/**
	 * @author xingjun
	 * insert expression
	 * modified on 16/05/2008 to make it update sequence number after inserting expression info
	 */
	public int insertExpression(String submissionId, String componentId, 
			String secondaryStrength, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("EXPRESSION");
		
		ParamQuery parQExpressionId = DBUpdateSQL.getParamQuery("GET_MAX_EXPRESSION_ID");
        PreparedStatement prepStmtExpressionId = null;
        String queryStringExpressionId = parQExpressionId.getQuerySQL();
        ResultSet resSetExpressionId = null;

		ParamQuery parQExpressionValue = DBUpdateSQL.getParamQuery("INSERT_EXPRESSION");
        PreparedStatement prepStmtExpressionValue = null;
        String queryStringExpressionValue = parQExpressionValue.getQuerySQL();
        
		ParamQuery parQExpressionSeq = DBUpdateSQL.getParamQuery("UPDATE_SEQUENCE_NUMBER_FOR_EXPRESSION");
        PreparedStatement prepStmtExpressionSeq = null;
        String queryStringExpressionSeq = parQExpressionSeq.getQuerySQL();
        
        int insertedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// get max id
        	prepStmtExpressionId = conn.prepareStatement(queryStringExpressionId);
        	resSetExpressionId = prepStmtExpressionId.executeQuery();
        	int maxExpressionId = 0;
        	if (resSetExpressionId.first()) {
        		maxExpressionId = resSetExpressionId.getInt(1);
        	}
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// insert expression
        	// modified by xingjun - 08/07/2008 - able to deal with temp sub id
        	prepStmtExpressionValue = conn.prepareStatement(queryStringExpressionValue);
        	prepStmtExpressionValue.setInt(1, maxExpressionId+1);
//        	prepStmtExpressionValue.setInt(2, Integer.parseInt(submissionId.substring(7)));
        	prepStmtExpressionValue.setString(2, submissionId);
        	prepStmtExpressionValue.setString(3, submissionId);
        	prepStmtExpressionValue.setString(4, submissionId);
        	prepStmtExpressionValue.setString(5, "present");
        	prepStmtExpressionValue.setString(6, secondaryStrength);
        	prepStmtExpressionValue.setString(7, componentId);
        	insertedRecordNumber = prepStmtExpressionValue.executeUpdate();
//        	System.out.println(insertedRecordNumber  + " expression inserted!");
         	
        	if (insertedRecordNumber == 0) {
        		conn.rollback();
        	} else { // update sequence number
            	prepStmtExpressionSeq = conn.prepareStatement(queryStringExpressionSeq);
            	prepStmtExpressionSeq.setInt(1, maxExpressionId+1);
            	insertedRecordNumber = prepStmtExpressionSeq.executeUpdate();
//            	System.out.println(insertedRecordNumber  + " sequence number expression updated!");
        	}
            conn.commit();
    		return insertedRecordNumber ;

        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0 ;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtExpressionId);
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmtExpressionValue);
        	DBHelper.closePreparedStatement(prepStmtExpressionSeq);
        	DBHelper.closeResultSet(resSetExpressionId);
       }
	}
	
	/**
	 * @author xingjun
	 * modified on 16/05/2008 to make it update sequence number after inserting expression info
	 * modified on 07/07/2008 to enalble it to deal with temp subid as well as permament subid
	 */
	public int insertExpression(String submissionId, String componentId,
			String primaryStrength, String secondaryStrength, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("EXPRESSION");
		
		ParamQuery parQExpressionId = DBUpdateSQL.getParamQuery("GET_MAX_EXPRESSION_ID");
        PreparedStatement prepStmtExpressionId = null;
        String queryStringExpressionId = parQExpressionId.getQuerySQL();
        ResultSet resSetExpressionId = null;

		ParamQuery parQExpressionNum = DBUpdateSQL.getParamQuery("FIND_EXPRESSION");
        PreparedStatement prepStmtExpressionNum = null;
        String queryExpressionNum = parQExpressionNum.getQuerySQL();
        ResultSet resSetExpressionNum = null;

		ParamQuery parQExpressionValue = DBUpdateSQL.getParamQuery("INSERT_EXPRESSION");
        PreparedStatement prepStmtExpressionValue = null;
        String queryStringExpressionValue = parQExpressionValue.getQuerySQL();
        
		ParamQuery parQExpressionSeq = DBUpdateSQL.getParamQuery("UPDATE_SEQUENCE_NUMBER_FOR_EXPRESSION");
        PreparedStatement prepStmtExpressionSeq = null;
        String queryStringExpressionSeq = parQExpressionSeq.getQuerySQL();
        
        int insertedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// get max id
        	prepStmtExpressionId = conn.prepareStatement(queryStringExpressionId);
        	resSetExpressionId = prepStmtExpressionId.executeQuery();
        	int maxExpressionId = 0;
        	if (resSetExpressionId.first()) {
        		maxExpressionId = resSetExpressionId.getInt(1);
        	}
        	
        	// if the expression is already exist, do not insert
        	// 07/07/2008 - xingjun - enable to deal with temp sub id
        	prepStmtExpressionNum = conn.prepareStatement(queryExpressionNum);
//        	prepStmtExpressionNum.setInt(1, Integer.parseInt(submissionId.substring(7)));
        	prepStmtExpressionNum.setString(1, submissionId);
        	prepStmtExpressionNum.setString(2, submissionId);
        	prepStmtExpressionNum.setString(3, submissionId);
        	prepStmtExpressionNum.setString(4, componentId);
        	resSetExpressionNum = prepStmtExpressionNum.executeQuery();
        	
        	int expressionNum = 0;
        	if (resSetExpressionNum.first()) {
        		expressionNum = resSetExpressionNum.getInt(1);
        	}
        	if (expressionNum > 0) {
        		return 0;
        	}
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// insert expression
        	// 07/07/2008 - xingjun
        	prepStmtExpressionValue = conn.prepareStatement(queryStringExpressionValue);
        	prepStmtExpressionValue.setInt(1, maxExpressionId+1); // oid
//        	prepStmtExpressionValue.setInt(2, Integer.parseInt(submissionId.substring(7))); // submission fk
        	prepStmtExpressionValue.setString(2, submissionId);
        	prepStmtExpressionValue.setString(3, submissionId);
        	prepStmtExpressionValue.setString(4, submissionId);
        	prepStmtExpressionValue.setString(5, primaryStrength); // strength
        	
            // additional strength
        	// modified by xingjun - 15/01/2008 - begin: sometimes the user will not provide additional strength
        	if (secondaryStrength == null || secondaryStrength.equals("")) {
        		prepStmtExpressionValue.setString(6, "");
        	} else {
            	prepStmtExpressionValue.setString(6, secondaryStrength);
        	}
        	// modified by xingjun - 15/01/2008 - end
        	
        	prepStmtExpressionValue.setString(7, componentId); // component id
//        	System.out.println("ISHEditDAO@insertExpression@maxExpressionId" + maxExpressionId);
//        	System.out.println("ISHEditDAO@insertExpression@submissionId" + submissionId);
//        	System.out.println("ISHEditDAO@insertExpression@componentId" + componentId);
        	insertedRecordNumber = prepStmtExpressionValue.executeUpdate();
//        	System.out.println(insertedRecordNumber  + " expression inserted!");
        	
        	if (insertedRecordNumber == 0) {
        		conn.rollback();
        	} else { // update sequence number
            	prepStmtExpressionSeq = conn.prepareStatement(queryStringExpressionSeq);
            	prepStmtExpressionSeq.setInt(1, maxExpressionId+1);
            	insertedRecordNumber = prepStmtExpressionSeq.executeUpdate();
//            	System.out.println(insertedRecordNumber  + " sequence number for expression updated!");
        	}
        	
            conn.commit();
    		return insertedRecordNumber ;

        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0 ;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closeResultSet(resSetExpressionId);
        	DBHelper.closePreparedStatement(prepStmtExpressionId);
        	DBHelper.closePreparedStatement(prepStmtExpressionNum);
        	DBHelper.closeResultSet(resSetExpressionNum);
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmtExpressionValue);
    		DBHelper.closePreparedStatement(prepStmtExpressionSeq);
        }
	}
	
	/**
	 * @author xingjun
	 * update expression strength: 0 - primary strength
	 *                             1 - secondary strength
	 * modified to enable it to deal with temp subid as well as permanent subid
	 */
	public int updateExpressionStrengh(String submissionId, String componentId, int type, String strength, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("EXPRESSION");;
		ParamMap parMColumn = null;

		ParamQuery parQ = null; 
		if (type == 0) { // expression
			parMColumn = DBUpdateSQL.getColumnMap("EXPRESSION_EXPRESSION");
			parQ = DBUpdateSQL.getParamQuery("UPDATE_EXPRESSION");
		} else if (type == 1) { // strength
			parMColumn = DBUpdateSQL.getColumnMap("EXPRESSION_STRENGTH");
			parQ = DBUpdateSQL.getParamQuery("UPDATE_EXPRESSION_STRENGTH");
		}
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
        	// 07/07/2008 - xingjun
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, strength);
//        	prepStmt.setInt(2, Integer.parseInt(submissionId.substring(7)));
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, submissionId);
        	prepStmt.setString(5, componentId);
        	updatedRecordNumber = prepStmt.executeUpdate();
//        	String message = (type == 0) ? " expression primary strength updated!" : " expression secondary strength updated!"; 
//        	System.out.println(updatedRecordNumber + message);
         	
        	conn.commit();
    		return updatedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * modified to enable it to deal with temp subid as well as permanent subid
	 */
	public int deleteExpression(String submissionId, String componentId, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("EXPRESSION");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_EXPRESSION");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("deleteExpression sql: " + queryString);
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
        	// 07/07/2008 - xingjun - able to deal with temp sub id
        	prepStmt = conn.prepareStatement(queryString);
//        	prepStmt.setInt(1, Integer.parseInt(submissionId.substring(7)));
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " expression deleted!");
        	
        	conn.commit();
    		return deletedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
       }
	}

	/**
	 * @author xingjun
	 * 
	 */
	public int deletePatternByIds(String submissionId, String componentId, String pattern, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_PATTERN_BY_IDS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
        	// xingjun - 09/07/2008 - able to deal with temp sub id
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
        	prepStmt.setString(5, pattern);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " expression pattern deleted!");
        	
        	conn.commit();
    		return deletedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
       } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * 
	 */
	public int deleteLocationByPattern(String submissionId, String componentId, String pattern, String userName) {
//		System.out.println("deleteLocationByPattern########");
		// initialising
		ResultSet resSet = null;
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN_LOCATION");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("GET_LOCATION_ID_BY_PATTERN");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// get location ids
        	// xingjun - 09/07/2008 - able to deal with temp sub id
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
        	prepStmt.setString(5, pattern);
        	resSet = prepStmt.executeQuery();
        	ArrayList<Integer>  locationIds = null;
        	if (resSet.first()) {
        		resSet.beforeFirst();
        		locationIds = new ArrayList<Integer>();
        		while (resSet.next()) {
        			locationIds.add(new Integer(resSet.getInt(1)));
        		}
        	}
        	
        	if (locationIds == null) { // there's no linked locations for specified pattern
        		return 0;
        	}
        	int len = locationIds.size();
        	
        	// delete locations if exist
        	if (len > 0) {
        		// log prepared statement initialisation
        		prepStmtLog = conn.prepareStatement(queryStringLog);
            	prepStmtLog.setString(1, "delete");
            	prepStmtLog.setString(2, userName);
            	prepStmtLog.setString(3, parMTable.getParamName());
            	prepStmtLog.setString(4, "");
            	
            	// deletion initialisation
            	parQ = DBUpdateSQL.getParamQuery("DELETE_LOCATION_BY_ID");
            	queryString = parQ.getQuerySQL();
            	
        		for (int i=0;i<len;i++) {
                	// log
                	loggedRecordNumber = prepStmtLog.executeUpdate();
//                	System.out.println(loggedRecordNumber + " records logged!");
                	
                	// delete
                	prepStmt = conn.prepareStatement(queryString);
                	prepStmt.setInt(1, locationIds.get(i).intValue());
                	deletedRecordNumber = prepStmt.executeUpdate();
//                	System.out.println(deletedRecordNumber + " expression pattern deleted!");
        		}
        	}
        	conn.commit();
    		return deletedRecordNumber;
        	
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closeResultSet(resSet);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * modified on 16/05/2008 to make it update sequence number after inserting pattern info
	 */
	public int insertPattern(String submissionId, String componentId, String pattern, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("PATTERN");
		
		ParamQuery parQPatternId = DBUpdateSQL.getParamQuery("GET_MAX_PATTERN_ID");
        PreparedStatement prepStmtPatternId = null;
        String queryStringPatternId = parQPatternId.getQuerySQL();
        ResultSet resSetPatternId = null;

		ParamQuery parQPatternValue = DBUpdateSQL.getParamQuery("INSERT_PATTERN");
        PreparedStatement prepStmtPatternValue = null;
        String queryStringPatternValue = parQPatternValue.getQuerySQL();
        
		ParamQuery parQPatternSeq = DBUpdateSQL.getParamQuery("UPDATE_SEQUENCE_NUMBER_FOR_PATTERN");
        PreparedStatement prepStmtPatternSeq = null;
        String queryStringPatternSeq = parQPatternSeq.getQuerySQL();
        
        int insertedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// get max id
        	prepStmtPatternId = conn.prepareStatement(queryStringPatternId);
        	resSetPatternId = prepStmtPatternId.executeQuery();
        	int maxPatternId = 0;
        	if (resSetPatternId.first()) {
        		maxPatternId = resSetPatternId.getInt(1);
        	}
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
        	// insert pattern
        	// xingjun - 09/07/2008 - able to deal with temp sub id
        	prepStmtPatternValue = conn.prepareStatement(queryStringPatternValue);
        	prepStmtPatternValue.setInt(1, maxPatternId+1);
        	prepStmtPatternValue.setString(2, submissionId);
        	prepStmtPatternValue.setString(3, submissionId);
        	prepStmtPatternValue.setString(4, submissionId);
        	prepStmtPatternValue.setString(5, componentId);
        	prepStmtPatternValue.setString(6, pattern);
        	insertedRecordNumber = prepStmtPatternValue.executeUpdate();
//        	System.out.println(insertedRecordNumber  + " expression pattern inserted!");
        	
        	if (insertedRecordNumber == 0) {
        		conn.rollback();
        	} else { // update sequence number
        		prepStmtPatternSeq = conn.prepareStatement(queryStringPatternSeq);
        		prepStmtPatternSeq.setInt(1, maxPatternId+1);
            	insertedRecordNumber = prepStmtPatternSeq.executeUpdate();
//            	System.out.println(insertedRecordNumber  + " sequence number for pattern updated!");
        	}
        	
            conn.commit();
    		return insertedRecordNumber ;

        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0 ;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmtPatternId);
        	DBHelper.closePreparedStatement(prepStmtPatternValue);
        	DBHelper.closePreparedStatement(prepStmtPatternSeq);
        	DBHelper.closeResultSet(resSetPatternId);
        }
 	}
	
	/**
	 * @author xingjun
	 * 
	 */
	public int insertLocation(String submissionId, String componentId, String pattern, String location, String userName) {
		
//		System.out.println("submissionId: " + submissionId);
//		System.out.println("componentId: " + componentId);
//		System.out.println("pattern: " + pattern);
//		System.out.println("location: " + location);
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN_LOCATION");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("INSERT_LOCATION");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int insertedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);

        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// insert
        	// xingjun - 09/07/2008 - able to deal with temp sub id
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
        	prepStmt.setString(5, pattern);
        	prepStmt.setString(6, location);
        	insertedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(insertedRecordNumber  + " expression location inserted!");
        	
            conn.commit();
    		return insertedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	public int insertLocation(int patternId, String location, String userName) {
		return 0;
	}
	
	/**
	 * @author xingjun
	 * 
	 */
	public int updateLocation(int patternId, int locationId, String location, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN_LOCATION");
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("LOCATION");

		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("UPDATE_LOCATION");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, location);
        	prepStmt.setInt(2, locationId);
//        	prepStmt.setInt(3, patternId); NOT SURE WE NEED THIS PARAMETER
        	updatedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(updatedRecordNumber  + " expression location updated!");
        	
             conn.commit();
     		return updatedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
           	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * 
	 */
	public int deleteLocation(int locationId, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN_LOCATION");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_LOCATION_BY_ID");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, locationId);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " expression location deleted!");
        	
            conn.commit();
    		return deletedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * 
	 */
	public int deletePatternById(int patternId, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_PATTERN_BY_ID");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");

        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, patternId);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " expression pattern deleted!");
        	
            conn.commit();
    		return deletedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	  /**
	   * @author xingjun
	   * start from current node, go up (if current node's to-be-set strength is 'present'),
	   * or go down (if current node's to-be-set strength is 'not detected') to find if there exists any
	   * parent node or kid node's strength is 'not detected' or 'present'. if found, the confliction exist
	   * 
	   * @param stage
	   * @param strength
	   * @return error code
	   * 0 - no confilction
	   * 1 - conflict with ancestor node - present
	   * 2 - conflict with decendant node - not detected
	   * 3 - conflict with ancestor node - uncertain
	   */
	  public int existConflict(String submissionId, String stage, String componentId, String strength) {
		  
//		  System.out.println("entered confliction checking routine!!!");
		  ParamQuery componentPQ = null;
		  PreparedStatement componentPS = null;
//		  System.out.println("submissionId: " + submissionId);
//		  System.out.println("componentId: " + componentId);
//		  System.out.println("stage: " + stage);
//		  System.out.println("strength: " + strength);
		  ResultSet resSet = null;
		  int errorCode = 0;
		  if (strength.equals("present")) {
			  componentPQ = DBQuery.getParamQuery("PARENT_NODES");
		  } else if (strength.equals("not detected")) {
			  componentPQ = DBQuery.getParamQuery("CHILD_NODES");
		  } else if (strength.equals("uncertain")) {
			  // 10/06/2008 - xingjun
			  componentPQ = DBQuery.getParamQuery("PARENT_NODES");
		  } else {
			  // may need to change: dont know what's the logic behind if the strength is blank
			  // 18/06/2008 - xingjun6
			  return 0; // no need to check confliction
		  }
		  String queryString = componentPQ.getQuerySQL();
		  
		  try {
//			  componentPQ.setPrepStat(conn);
//			  componentPS = componentPQ.getPrepStat();
			  conn = DBHelper.reconnect2DB(conn);
			  componentPS = conn.prepareStatement(queryString);
			  
			  componentPS.setString(1, componentId);
			  componentPS.setString(2, stage);
			  componentPS.setString(3, submissionId);
			  
			  resSet = componentPS.executeQuery();
			  if (resSet.first()) {
				  if (strength.equals("present")) {
					  errorCode = 1;
				  } else if (strength.equals("not detected")){ // modified by xingjun - 18/06/2008
					  errorCode = 2;
				  } else if (strength.equals("uncertain")){ // modified by xingjun - 18/06/2008
					  errorCode = 3;
				  }
			  }
			  return errorCode;
			  
		  } catch(SQLException se) {
			  se.printStackTrace();
			  return errorCode;
		  } finally {
			  DBHelper.closePreparedStatement(componentPS);
			  DBHelper.closeResultSet(resSet);
		  }
	  }
	
	/**
	 * @author xingjun
	 * insert expression note: notes less than 200 character length
	 */
	public int addExpressionNote(String submissionId, String componentId, String note, int serialNo, String userName) {
//		System.out.println("ISHEditDAO@addExpressionNote@submissionid: " + submissionId);
//		System.out.println("ISHEditDAO@addExpressionNote@componentId: " + componentId);
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("EXPRESSION_NOTE");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("INSERT_EXPRESSION_NOTE");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("insert exp note sql: " + queryString);
        int insertedNoteNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// modified by xingjun - 07/07/2008
        	// enable to deal with temporary subid as well as permanent subid
//    		System.out.println("submissionid: " + submissionId);
//    		System.out.println("componentId: " + componentId);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
        	prepStmt.setString(5, note);
        	prepStmt.setInt(6, serialNo);
        	insertedNoteNumber = prepStmt.executeUpdate();
//        	System.out.println(insertedSubmissionNumber + " expression note record inserted!");
        	
            conn.commit();
    		return insertedNoteNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * delete expression note
	 */
	public int deleteExpressionNotes(String submissionId, String componentId, String userName) {
//		System.out.println("ISHEditDAO@deleteExpressionNotes@submissionid: " + submissionId);
//		System.out.println("ISHEditDAO@deleteExpressionNotes@componentId: " + componentId);
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("EXPRESSION_NOTE");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_EXPRESSION_NOTE");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedNoteNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
        	// modified by xingjun - 07/07/2008
        	// enable to deal with temporary subid as well as permanent subid
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
        	deletedNoteNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedSubmissionNumber + " expression note record inserted!");
        	
            conn.commit();
    		return deletedNoteNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun
	 * update expression note
	 */
	public int updateExpressionNotes(String submissionId, String componentId, String newNote, String userName) {
//		System.out.println("ISHEditDAO@updateExpressionNotes@submissionid: " + submissionId);
//		System.out.println("ISHEditDAO@updateExpressionNotes@componentId: " + componentId);
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("EXPRESSION_NOTE");
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("EXPRESSION_NOTE");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("UPDATE_EXPRESSION_NOTE");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedNoteNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// modified by xingjun - 07/07/2008
        	// enable to deal with temporary subid as well as permanent subid
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, newNote);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, submissionId);
        	prepStmt.setString(5, componentId);
        	updatedNoteNumber = prepStmt.executeUpdate();
//        	System.out.println(updatedSubmissionNumber + " expression note record updated!");
        	
            conn.commit();
    		return updatedNoteNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtLog);
        	DBHelper.closePreparedStatement(prepStmt);
       }
	}
	
	/**
	 * @author xingjun
	 * 
	 */
	public int insertStatusNote(String submissionId, String statusNote, String userName) {
		// initialising
//        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
//		PreparedStatement prepStmtLog = null;
//		ParamMap parMTable = null;
//		parMTable = DBUpdateSQL.getTableMap("STATUS_NOTE");
		
		ParamQuery parQStatusNoteValue = DBUpdateSQL.getParamQuery("INSERT_SUBMISSION_STATUS_NOTE");
        PreparedStatement prepStmtStatusNoteValue = null;
        String queryStringStatusNoteValue = parQStatusNoteValue.getQuerySQL();
        
        int insertedRecordNumber = 0;
//        String queryStringLog = parQLog.getQuerySQL();
//        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
//        	prepStmtLog = conn.prepareStatement(queryStringLog);
//        	prepStmtLog.setString(1, "insert");
//        	prepStmtLog.setString(2, userName);
//        	prepStmtLog.setString(3, parMTable.getParamName());
//        	prepStmtLog.setString(4, "");
//        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
        	// insert status notes
        	prepStmtStatusNoteValue = conn.prepareStatement(queryStringStatusNoteValue);
        	prepStmtStatusNoteValue.setInt(1, Integer.parseInt(submissionId.substring(7)));
        	prepStmtStatusNoteValue.setString(2, statusNote);
        	insertedRecordNumber = prepStmtStatusNoteValue.executeUpdate();
//        	System.out.println(insertedRecordNumber  + " status notes inserted!");
        	
        	if (insertedRecordNumber == 0) {
        		conn.rollback();
        	}
            conn.commit();
    		return insertedRecordNumber ;

        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0 ;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmtStatusNoteValue);
        }
	}
	
	public int deleteStatusNotesBySubmissionId(String submissionId, String userName) {
		
		// initialising
//        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
//		PreparedStatement prepStmtLog = null;
//		ParamMap parMTable = DBUpdateSQL.getTableMap("STATUS_NOTE");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_STATUS_NOTE_BY_SUBMISSION_ID");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
//        String queryStringLog = parQLog.getQuerySQL();
//        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
//        	conn.setAutoCommit(false);
        	
        	// log
//        	prepStmtLog = conn.prepareStatement(queryStringLog);
//        	prepStmtLog.setString(1, "delete");
//        	prepStmtLog.setString(2, userName);
//        	prepStmtLog.setString(3, parMTable.getParamName());
//        	prepStmtLog.setString(4, "");
//        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, Integer.parseInt(submissionId.substring(7))); // use digital part of the id
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedStatusNoteNumber + " status note record deleted!");
        	
    		return deletedRecordNumber;
         } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
         } 
        finally {
        	DBHelper.closePreparedStatement(prepStmt);
       }
	}
	
	public int deleteStatusNotesByStatusNoteId(int statusNoteId, String userName) {
		// initialising
//      ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
//		PreparedStatement prepStmtLog = null;
//		ParamMap parMTable = DBUpdateSQL.getTableMap("STATUS_NOTE");
		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_STATUS_NOTE_BY_STATUS_NOTE_ID");
		PreparedStatement prepStmt = null;
		String queryString = parQ.getQuerySQL();
		int deletedRecordNumber = 0;
//		String queryStringLog = parQLog.getQuerySQL();
//		int loggedRecordNumber = 0;
		
		// perform the query
		try {
        	conn = DBHelper.reconnect2DB(conn);
//			conn.setAutoCommit(false);
      	
      	// log
//      	prepStmtLog = conn.prepareStatement(queryStringLog);
//      	prepStmtLog.setString(1, "delete");
//      	prepStmtLog.setString(2, userName);
//      	prepStmtLog.setString(3, parMTable.getParamName());
//      	prepStmtLog.setString(4, "");
//      	loggedRecordNumber = prepStmtLog.executeUpdate();
//      	System.out.println(loggedRecordNumber + " records logged!");

			// delete
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setInt(1, statusNoteId);
			deletedRecordNumber = prepStmt.executeUpdate();
//			System.out.println(deletedStatusNoteNumber + " status note record deleted!");
			
			return deletedRecordNumber;
		} catch(SQLException se) {
			se.printStackTrace();
			return 0;
		} 
      finally {
      	DBHelper.closePreparedStatement(prepStmt);
       }
	}

	public int updateStatusNoteById(int statusNoteId, String statusNotes, String userName) {
		// initialising
//        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
//		PreparedStatement prepStmtLog = null;
//		ParamMap parMTable = DBUpdateSQL.getTableMap("STATUS_NOTE");
//		ParamMap parMColumn = DBUpdateSQL.getColumnMap("STATUS_NOTE");

		
		ParamQuery parQ = DBUpdateSQL.getParamQuery("UPDATE_SUBMISSION_STATUS_NOTE");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
//        String queryStringLog = parQLog.getQuerySQL();
//        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	conn.setAutoCommit(false);
        	
        	// log
//        	prepStmtLog = conn.prepareStatement(queryStringLog);
//        	prepStmtLog.setString(1, "update");
//        	prepStmtLog.setString(2, userName);
//        	prepStmtLog.setString(3, parMTable.getParamName());
//        	prepStmtLog.setString(4, parMColumn.getParamName());
//        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, statusNotes);
        	prepStmt.setInt(2, statusNoteId);
        	updatedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(updatedRecordNumber  + " status notes updated!");
        	
            conn.commit();
    		return updatedRecordNumber;
        } catch(SQLException se) {
        	se.printStackTrace();
    		return 0;
        } finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun - 29/07/2008
	 * @param submissionId
	 * @param userName
	 * @return
	 */
	public int updateSubmissionLockingInfo(String submissionId, String userName) {
//		System.out.println("update submission locking info!!");
		ParamQuery parQ = DBUpdateSQL.getParamQuery("UPDATE_SUBMISSION_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, userName);
        	prepStmt.setString(2, submissionId);
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
    		return updatedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	public int updateSubmissionLockingInfo(String submissionId, int userId) {
//		System.out.println("update submission locking info!!");
		ParamQuery parQ = DBUpdateSQL.getParamQuery("UPDATE_SUBMISSION_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, userId);
        	prepStmt.setString(2, submissionId);
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
    		return updatedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	/**
	 * @author xingjun - 29/07/2008
	 * @param submissionId
	 * @param userName
	 * @return
	 */
	public int insertSubmissionLockingInfo(String submissionId, String userName) {
		ParamQuery parQ = DBUpdateSQL.getParamQuery("INSERT_SUBMISSION_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int insertedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, userName);
        	insertedRecordNumber = prepStmt.executeUpdate();
        	
    		return insertedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	public int insertSubmissionLockingInfo(String submissionId, int userId) {
		ParamQuery parQ = DBUpdateSQL.getParamQuery("INSERT_SUBMISSION_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int insertedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setInt(2, userId);
        	insertedRecordNumber = prepStmt.executeUpdate();
        	
    		return insertedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	/**
	 * @author xingjun - 29/07/2008
	 * @param submissionId
	 * @param userName
	 * @return
	 */
	public int deleteSubmissionLockingInfo(String submissionId) {
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_SUBMISSION_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	deletedRecordNumber = prepStmt.executeUpdate();
        	
    		return deletedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	/**
	 * @author xingjun - 06/08/2008
	 * <p>delete all corresponding locking info linked to given user</p>
	 * @param userName
	 * @return
	 */
	public int deleteUserLockingInfo(String userName) {
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_USER_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("deleteUserLockingInfo:"+ userName + ":" + queryString);
        int deletedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, userName);
        	deletedRecordNumber = prepStmt.executeUpdate();
        	
    		return deletedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	public int deleteUserLockingInfo(int userId) {
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_USER_LOCKING_INFO");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("deleteUserLockingInfo:"+ userName + ":" + queryString);
        int deletedRecordNumber = 0;
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, userId);
        	deletedRecordNumber = prepStmt.executeUpdate();
        	
    		return deletedRecordNumber;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return 0;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        }
	}
	
	/**
	 * @author xingjun - 15/08/2008
	 * <p>check if the given batches for given pi are locked </p>
	 * @param batchIds
	 * @param pi
	 * @return
	 */
	public boolean isBatchLocked(int batchId, int userId) {
//		System.out.println("isBatchLocked:batchId: " + batchId);
//		System.out.println("isBatchLocked:userId: " + userId);
		boolean batchLocked = false;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			DBQuery.getParamQuery("GET_LOCKING_INFO_BY_BATCH_ID");
		String query = parQ.getQuerySQL();
		try {
        	conn = DBHelper.reconnect2DB(conn);
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, batchId);
			resSet = prepStmt.executeQuery();
			if (this.isSubmissionLocked(resSet, userId)) { // if get result, some submissions in this batch have looked
				batchLocked = true;
			}
    		return batchLocked;
        } catch (SQLException se) {
        	se.printStackTrace();
    		return false;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);       	
        	DBHelper.closeResultSet(resSet);     	
        }
	}
	
	/**
	 * @author xingjun - 15/08/2008
	 * 
	 * @param resSet
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	private boolean isSubmissionLocked(ResultSet resSet, int userId) throws SQLException {
		if (resSet.first()) { // if get result, some submissions in this batch have looked
			resSet.beforeFirst();
			while (resSet.next()) {
				int lockedByUserId = resSet.getInt(2);
				if (userId != lockedByUserId) {
					int definedLockTimOut = Integer.parseInt(Utility.getLockTimeOut());
					int lockedTime = resSet.getInt(3);
					if (lockedTime<= definedLockTimOut) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * @author ying
	 */
	public ArrayList getPIBySubID(String submissionId) {
		ParamQuery parQ = DBUpdateSQL.getParamQuery("GET_PI_FROM_SUB_ID");
        PreparedStatement prepStmt = null;
        ResultSet pi = null;
        ArrayList piArray = null;
        // assemble sql string
        String queryString = parQ.getQuerySQL();
        
        try {
        	conn = DBHelper.reconnect2DB(conn);
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	pi = prepStmt.executeQuery();
        	piArray = convertToArray(pi);
    		return piArray;
    		
        } catch (SQLException se) {
        	se.printStackTrace();
    		return null;
        }
        finally{
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(pi);
        }
	}

	public ArrayList<String> convertToArray(ResultSet resSetImage) throws SQLException {     
        if (resSetImage.first()) {
            resSetImage.beforeFirst();           
            ArrayList<String> results = new ArrayList<String>();
            while (resSetImage.next()) { 
                for(int i = 1; i <= 4; i++) {
                	results.add(resSetImage.getString(i));
                }
            }
            return results;
        }
        return null;
    }
}
