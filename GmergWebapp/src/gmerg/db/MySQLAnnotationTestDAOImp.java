package gmerg.db;

import gmerg.beans.UserBean;
import gmerg.entities.submission.ish.ISHBatchSubmission;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ish.ISHBatch;
import gmerg.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLAnnotationTestDAOImp implements AnnotationTestDAO {
    private boolean debug = false;

	private Connection conn;
	
	public MySQLAnnotationTestDAOImp() {
		
	}
	
	// constructor with connection initialisation
	public MySQLAnnotationTestDAOImp(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * @author xingjun - 03/07/2008
	 * @return int
	 * return incompleted submission batch id. if there's no one, return -1
	 */
	public int getIncompletedSubmissionBatchId(int pi, User user) {
		int batchId = -1;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("INCOMPLETED_SUBMISSION_BATCH_ID");
		String query = parQ.getQuerySQL();
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+query.toLowerCase());
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, pi);
			
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				batchId = resSet.getInt(1);
			}
			return batchId;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return -1;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 03/07/2008
	 * @return int
	 * <p>return id of the newly created batch</p>
	 * <p>modified by xingjun - 
	 * when create batch, batch&pi into need to be stored in the db</p>
	 */
	public int createNewBatch(int pi, User user) {
		String userName = user.getUserName();
		
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("REF_BATCH");
		String queryStringLog = parQLog.getQuerySQL();
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("MAX_SUBMISSION_BATCH");

		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("MAX_SUBMISSION_BATCH_ID");
		String queryMaxBatchId = parQ.getQuerySQL();
		parQ =
			AnnotationToolQuery.getParamQuery("UPDATE_MAX_SUBMISSION_BATCH_ID");
		String queryUpdateMaxBatchId = parQ.getQuerySQL();
		
		int maxBatchId = -1;
		int newBatchId = -1;
		int loggedRecordNumber = 0;
		
		try {
			// get max batch id
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryMaxBatchId.toLowerCase());
			prepStmt = conn.prepareStatement(queryMaxBatchId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				maxBatchId = resSet.getInt(1);
			}
			if (maxBatchId == -1) { // failed to get the max submission batch id
				return -1;
			}
			
			conn.setAutoCommit(false);
			// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	if (loggedRecordNumber == 0) { // logging failed
        		conn.rollback();
        		conn.setAutoCommit(true);
    			return -1;
        	}
        	
        	///// insert a new record into batch user table - xingjun - 12/08/2008
        	int batchPiCreated =
        		this.createNewBatchUser((maxBatchId+1), pi, userName);
        	if (batchPiCreated == 0) { // failed to insert
        		conn.rollback();
        		conn.setAutoCommit(true);
				return -1;
        	}

			// increment batch id
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryUpdateMaxBatchId.toLowerCase());
			prepStmt = conn.prepareStatement(queryUpdateMaxBatchId);
			prepStmt.setInt(1, maxBatchId+1);
			int updatedRecordNumber = prepStmt.executeUpdate();
			
			if (updatedRecordNumber == 0) { // failed to update db
        		conn.rollback();
        		conn.setAutoCommit(true);
				return -1;
			} else { // batch id updated
				newBatchId = maxBatchId+1;
			}
			// release db resource
    		conn.commit();
    		conn.setAutoCommit(true);
			return newBatchId;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return -1;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 12/08/2008
	 * <p>insert batch pi info into the db</p>
	 * @param pi
	 * @param userId
	 * @return
	 */
	private int createNewBatchUser(int batchId, int pi, String userName) {
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("BATCH_USER");;
		ParamQuery parQ = null; 
		parQ = AnnotationToolQuery.getParamQuery("INSERT_BATCH_USER_INFO");
		
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int insertedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println("createNewBatchUser: " + loggedRecordNumber + " records logged!");
        	
        	// insert
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, batchId);
        	prepStmt.setInt(2, pi);
        	prepStmt.setString(3, userName);
        	insertedRecordNumber = prepStmt.executeUpdate();
        	
			return insertedRecordNumber;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return -1;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		}
	}
	
	/**
	 * @author xingjun - 12/08/2008
	 * <p>get all batches for given pi</p>
	 */
	public ISHBatch[] getBatchListByPi(int pi) {
//		System.out.println("getBatchListByPi: pi: " + pi);
		ISHBatch[] batches = null;
		ResultSet resSetM = null;
		ResultSet resSetC = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQM =
			AnnotationToolQuery.getParamQuery("GET_ISH_BATCHES_WITH_MODIFICATION_TIME_BY_PI");
		ParamQuery parQC =
			AnnotationToolQuery.getParamQuery("GET_ISH_BATCHES_WITH_CREATION_TIME_BY_PI");
		String queryM = parQM.getQuerySQL();
		String queryC = parQC.getQuerySQL();
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryM.toLowerCase());
			prepStmt = conn.prepareStatement(queryM);
			prepStmt.setInt(1, pi);
			resSetM = prepStmt.executeQuery();
			prepStmt = null;
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryC.toLowerCase());
			prepStmt = conn.prepareStatement(queryC);
			prepStmt.setInt(1, pi);
			resSetC = prepStmt.executeQuery();
			batches = formatISHBatchResult(resSetM, resSetC);
			return batches;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSetM);
			DBHelper.closeResultSet(resSetC);
			DBHelper.closePreparedStatement(prepStmt);
		}
	}
	
	/**
	 * @author xingjun - 12/08/2008
	 * <p>modified by xingjun - 13/08/2008 - changed method to calculate last modified time</p>
	 * <p>modified by xingjun - 20/08/2008 - resSetC was copied as resSetM previously</p>
	 * <p>modified by xingjun - 19/12/2008 - have an extra column (status) for batch </p>
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ISHBatch[] formatISHBatchResult(ResultSet resSetM, ResultSet resSetC) throws SQLException {
//		System.out.println("formatISHBatchResult####");
		// get all batches with lastest modified time - has corresponding submissions
		ArrayList<ISHBatch> ishBatchListM = null;
		ArrayList<String> ishBatchIdListM = null;
		if (resSetM.first()) { //System.out.println("resSetM not null####");
			ishBatchListM = new ArrayList<ISHBatch>();
			ishBatchIdListM = new ArrayList<String>();
			resSetM.beforeFirst();
			while (resSetM.next()) {
				ISHBatch ishBatch = new ISHBatch();
				String batchId = Integer.toString(resSetM.getInt(1));
				ishBatch.setBatchID(batchId);
				ishBatch.setLastModifiedBy(resSetM.getString(2));
				ishBatch.setLastModifiedDate(resSetM.getString(3));
				ishBatch.setLastModified(resSetM.getDate(3));
				ishBatch.setStatus(resSetM.getString(4));// added by xingjun - 19/12/2008
				ishBatch.setSelected(false);
				ishBatchListM.add(ishBatch);
				ishBatchIdListM.add(batchId);
			}
		}
		// get all batches with creation time
		ArrayList<ISHBatch> ishBatchListC = null;
		ArrayList<String> ishBatchIdListC = null;
		if (resSetC.first()) { //System.out.println("resSetC not null####");
			ishBatchListC = new ArrayList<ISHBatch>();
			ishBatchIdListC = new ArrayList<String>();
			resSetC.beforeFirst();
			while (resSetC.next()) {
				ISHBatch ishBatch = new ISHBatch();
				String batchId = Integer.toString(resSetC.getInt(1));
				ishBatch.setBatchID(batchId);
				ishBatch.setLastModifiedBy(resSetC.getString(2));
				ishBatch.setLastModifiedDate(resSetC.getString(3));
				ishBatch.setLastModified(resSetC.getDate(3));
				ishBatch.setStatus(resSetC.getString(4)); // added by xingjun - 19/12/2008
				ishBatch.setSelected(false);
				ishBatchListC.add(ishBatch);
				ishBatchIdListC.add(batchId);
			}
		}

		// combine the two
		if (ishBatchListM == null) {
			if (ishBatchListC == null) {
				return null;
			} else {
				// return batches with creation time
				return (ISHBatch[])ishBatchListC.toArray(new ISHBatch[ishBatchListC.size()]);
			}
		} else {
			if (ishBatchListC == null) {
				// return batches with modified time
				return (ISHBatch[])ishBatchListM.toArray(new ISHBatch[ishBatchListM.size()]);
			} else {
//				System.out.println("COMBINE TWO BATCH LIST#####");
				// combine them all and return;
				int mlen = ishBatchListM.size();
				for (int i=0;i<mlen;i++) {
					String batchIdM = ishBatchIdListM.get(i);
					if (!ishBatchIdListC.contains(batchIdM)) { // theorically this will not happen
//						System.out.println("c does not contain m");
						ishBatchListC.add(ishBatchListM.get(i));
					} else { // update modification time
//						System.out.println("c contains m");
						int idx = ishBatchIdListC.indexOf(batchIdM);
						java.util.Date mDate = ishBatchListM.get(i).getLastModified();
						String mDateString = ishBatchListM.get(i).getLastModifiedDate();
						java.util.Date cDate = ishBatchListC.get(idx).getLastModified();
						if (mDate.after(cDate)) {
							ishBatchListC.get(idx).setLastModified(mDate);
							ishBatchListC.get(idx).setLastModifiedDate(mDateString);
						}
					}
				}
				// convert to array
//				System.out.println("ishBatchIdListC size after combination: " + ishBatchListC.size());
				return (ISHBatch[])ishBatchListC.toArray(new ISHBatch[ishBatchListC.size()]);
			}
		}
	} // end of formatISHBatchResult
	
	/**
	 * @author xingjun - 03/07/2008
	 */
	public ISHBatchSubmission[] getSubmissionByBatchId(int batchId) {
		ISHBatchSubmission[] submissions = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("GET_ISH_SUBMISSIONS_BY_BATCH_ID");
		String query = parQ.getQuerySQL();
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+query.toLowerCase());
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, batchId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				submissions = formatISHBatchSubmissions(resSet);
			}
			return submissions;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 19/08/2008
	 * @param batchId
	 * @return
	 */
	public String[] getSubmissionIdsByBatchId(int batchId) {
		String[] submissionIds = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("GET_ISH_SUBMISSION_ID_BY_BATCH_ID");
		String query = parQ.getQuerySQL();
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+query.toLowerCase());
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, batchId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				resSet.last();
				int arraySize = resSet.getRow();
				submissionIds = new String[arraySize];
				resSet.beforeFirst();
				int i = 0;
				while (resSet.next()) {
					submissionIds[i] = resSet.getString(1);
					i++;
				}
			}
			return submissionIds;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 03/07/2008
	 * <p>modified by xingjun 15/08/2008 - adjust to comply with the change of sql</p>
	 * <p>xingjun - 16/10/2009 - added column SUB_DB_STATUS_FK in the query, need to adjust accordingly</p>
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ISHBatchSubmission[] formatISHBatchSubmissions(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			resSet.last();
			int arraySize = resSet.getRow();
			ISHBatchSubmission[] submissions = new ISHBatchSubmission[arraySize];
			
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				ISHBatchSubmission submission = new ISHBatchSubmission();
				submission.setSubmissionID(resSet.getString(1));
				submission.setBatchID(Integer.toString(resSet.getInt(2)));
				submission.setLockedBy(resSet.getString(3));
				int lockedTime = resSet.getInt(4);
				if (lockedTime <1) {
					submission.setLocked(false);
				} else {
					submission.setLocked(true);
				}
				submission.setLockedByPrivilege(resSet.getInt(5));
				submission.setLastModifiedBy(resSet.getString(6));
				submission.setLastModifiedDate(resSet.getString(7));
				// xingjun - 16/10/2009 - start
				submission.setDbStatus(resSet.getInt(8)); 
				// xingjun - 16/10/2009 - start
				submission.setSelected(false);
				submissions[i] = submission;
				i++;
			}
			return submissions;
		}
		return null;
	}
	
	/**
	 * @author xingjun - 09/07/2008
	 * <p>create a new submission and insert it into database
	 * at this stage only fill in information including 
	 * submission temp id, submitter, pi, and batch id
	 * more information need to be input later</p>
	 * <p>xingjun - 11/12/2009 - integer type may not big enough to store sub id, chanegd it into String</p>
	 */
	public int createNewSubmission(int batchId, User user, int pi) {
//		System.out.println("batch: " + batchId);
//		System.out.println("user id:pi: " + user.getUserId() + ":" + user.getUserPi());
        ParamQuery parQLog =
        	DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("SUBMISSION");
		String queryStringLog = parQLog.getQuerySQL();

		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("MAX_SUBMISSION_TEMP_ID");
		String queryMaxSubmissionTempId = parQ.getQuerySQL();
		parQ =
			AnnotationToolQuery.getParamQuery("INSERT_TEMP_SUBMISSION");
		String queryInsertTempSubmission = parQ.getQuerySQL();
		parQ =
			AnnotationToolQuery.getParamQuery("UPDATE_MAX_SUBMISSION_TEMP_ID");
		String queryUpdateMaxSubmissionTempId = parQ.getQuerySQL();
		
		int maxSubmissionTempId = -1;
		int maxSubmissionTempIdPrefix = 0;
		int updatedRecordNumber = 0;
		int loggedRecordNumber = 0;
		
		try {
			// get max submission temp id
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryMaxSubmissionTempId.toLowerCase());
			prepStmt = conn.prepareStatement(queryMaxSubmissionTempId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				maxSubmissionTempIdPrefix = resSet.getInt(1);
				maxSubmissionTempId = resSet.getInt(2);
			}
//			System.out.println("maxSubmissionTempId: " + maxSubmissionTempId);
			if (maxSubmissionTempId == -1) { // failed to get the max submission batch id
				// release db resource before return
				return updatedRecordNumber;
			}
			// '999999' is the prefix of the sub temp id, when increment it, only increase the id part
			// the submissionTempId column will only store ID, need concatenate the prefix with it
			// when ask the oid
			String tempIdPrefixString = Integer.toString(maxSubmissionTempIdPrefix);
			String tempIdStrng = Integer.toString(maxSubmissionTempId+1);
			String newSubmissionTempId = tempIdPrefixString + tempIdStrng;
			
        	conn.setAutoCommit(false);

			// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, user.getUserName());
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	if (loggedRecordNumber == 0) { // logging failed
        		conn.rollback();
        		conn.setAutoCommit(true);
     			return 0;
        	}

        	//insert temporary submission
        	// if it's the editor or sysuser create submission for other lab
        	// submitter and pi's value have to be get from pi parameter
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryInsertTempSubmission.toLowerCase());
			prepStmt = conn.prepareStatement(queryInsertTempSubmission);
			prepStmt.setString(1, newSubmissionTempId); // sub temp id
			if (user.getUserPrivilege() > 4) {
				prepStmt.setInt(2, pi); // submitter
				prepStmt.setInt(3, pi); // pi 
			} else {
				prepStmt.setInt(2, user.getUserPi()); // submitter
				prepStmt.setInt(3, user.getUserPi()); // pi 
			}
			prepStmt.setInt(4, batchId); // batch
			prepStmt.setString(5, newSubmissionTempId); // sub temp acc

			// put sub temp id into sub_lab_id
			// derek's parser will use labid column in submission table to get a copy of temp sub id
			prepStmt.setString(6, newSubmissionTempId);
			///////////////////////////////////////////////////////////////
			
			updatedRecordNumber = prepStmt.executeUpdate();
//			System.out.println("updatedSubNumber: " + updatedRecordNumber);
			
			if (updatedRecordNumber == 0) { // failed to insert submission
				conn.rollback();
    			return 0;
			} else {
				// increment submission temp id
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryUpdateMaxSubmissionTempId.toLowerCase());
				prepStmt = conn.prepareStatement(queryUpdateMaxSubmissionTempId);
//				prepStmt.setInt(1, Integer.parseInt(tempIdStrng)); // sub temp id - xingjun - 11/12/2009
				prepStmt.setString(1, tempIdStrng); // sub temp id
				updatedRecordNumber = prepStmt.executeUpdate();
//				System.out.println("updatedSeqNumber: " + updatedRecordNumber);
				if (updatedRecordNumber == 0) { // failed to increment sub temp id
	    			return 0;
				} else {
					conn.commit();
				}
			}
			conn.setAutoCommit(true);

			return 1;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return 0;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	} // end of createNewSubmission(batch, userName)
	
	/**
	 * @author xingjun - 16/07/2008
	 * <p>create a new submission based on template submission; 
	 * return temporary submission id - type int</p>
	 * <p>modified by xingjun - 11/11/2008
	 * <p>modified return type to String - xingjun - 14/08/2009</p>
	 * <p>modified newSubmissionTempId from type int to string - xingjun - 14/08/2009</p>
	 * <p>xingjun 06/11/2009 - removed localDbStatusId column from the setting statement</p>
	 * removed column setting for lab id</p>
	 */
	public String createNewSubmission(Submission templateSubmission, String userName) {
        ParamQuery parQLog =
        	DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = null;
		parMTable = DBUpdateSQL.getTableMap("SUBMISSION");
		String queryStringLog = parQLog.getQuerySQL();

		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("MAX_SUBMISSION_TEMP_ID");
		String queryMaxSubmissionTempId = parQ.getQuerySQL();
		parQ =
			AnnotationToolQuery.getParamQuery("INSERT_SUBMISSION");
		String queryInsertTempSubmission = parQ.getQuerySQL();
		parQ =
			AnnotationToolQuery.getParamQuery("UPDATE_MAX_SUBMISSION_TEMP_ID");
		String queryUpdateMaxSubmissionTempId = parQ.getQuerySQL();
		
		int maxSubmissionTempId = -1;
		int maxSubmissionTempIdPrefix = 0;
		String newSubmissionTempId = "0";
		int updatedRecordNumber = 0;
		int loggedRecordNumber = 0;
		
		try {
			// get max submission temp id
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryMaxSubmissionTempId.toLowerCase());
			prepStmt = conn.prepareStatement(queryMaxSubmissionTempId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				maxSubmissionTempIdPrefix = resSet.getInt(1);
				maxSubmissionTempId = resSet.getInt(2);
			}
//			System.out.println("maxSubmissionTempId: " + maxSubmissionTempId);
			if (maxSubmissionTempId == -1) { // failed to get the max submission batch id
				// release db resource before return
//				return updatedRecordNumber;
				return Integer.toString(updatedRecordNumber);
			}
			// '999999' is the prefix of the sub temp id, when increment it, only increase the id part
			// the submissionTempId column will only store ID, need concatenate the prefix with it
			// when ask the oid
			String tempIdPrefixString = Integer.toString(maxSubmissionTempIdPrefix);
			String tempIdStrng = Integer.toString(maxSubmissionTempId+1);
			newSubmissionTempId = tempIdPrefixString + tempIdStrng;
			
        	conn.setAutoCommit(false);

			// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "insert");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	if (loggedRecordNumber == 0) { // logging failed
        		conn.rollback();
        		conn.setAutoCommit(true);
    			return "0";
        	}

        	//insert temporary submission
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryInsertTempSubmission.toLowerCase());
			prepStmt = conn.prepareStatement(queryInsertTempSubmission);
			prepStmt.setString(1, newSubmissionTempId); // sub temp id
			prepStmt.setString(2, newSubmissionTempId); // acc
			prepStmt.setString(3, templateSubmission.getStage());
			prepStmt.setString(4, templateSubmission.getAssayType());
			prepStmt.setInt(5, templateSubmission.getPublicFlag());
			prepStmt.setInt(6, Integer.parseInt(templateSubmission.getArchiveId()));
			prepStmt.setInt(7, templateSubmission.getDeletedFlag());
			prepStmt.setInt(8, templateSubmission.getSubmitterId());
			prepStmt.setInt(9, templateSubmission.getPiId());
			prepStmt.setInt(10, templateSubmission.getEntryBy());
			prepStmt.setInt(11, templateSubmission.getModifierId());
			prepStmt.setInt(12, templateSubmission.getDbStatusId());
			prepStmt.setString(13, templateSubmission.getProject());
			prepStmt.setInt(14, templateSubmission.getBatchId());
			prepStmt.setString(15, templateSubmission.getNameSpace());
			prepStmt.setString(16, templateSubmission.getOsAccession());
			prepStmt.setString(17, templateSubmission.getLoaclId());
			prepStmt.setString(18, templateSubmission.getSource());
			prepStmt.setString(19, templateSubmission.getValidation());
			prepStmt.setInt(20, templateSubmission.getControl());
			prepStmt.setString(21, templateSubmission.getAssessment());
			prepStmt.setInt(22, templateSubmission.getConfidencLevel());
			prepStmt.setString(23, templateSubmission.getLocalDbName());
			
			updatedRecordNumber = prepStmt.executeUpdate();
//			System.out.println("updatedSubNumber: " + updatedRecordNumber);
			
			if (updatedRecordNumber == 0) { // failed to insert submission
				conn.rollback();
   			return "0";
			} else {
				// increment submission temp id
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryUpdateMaxSubmissionTempId.toLowerCase());
				prepStmt = conn.prepareStatement(queryUpdateMaxSubmissionTempId);
//				prepStmt.setInt(1, Integer.parseInt(tempIdStrng));
				prepStmt.setString(1, tempIdStrng);
				updatedRecordNumber = prepStmt.executeUpdate();
//				System.out.println("updatedSeqNumber: " + updatedRecordNumber);
				if (updatedRecordNumber == 0) { // failed to increment sub temp id
					conn.rollback();
	    			return "0";
				} else {
					conn.commit();
				}
			}
			conn.setAutoCommit(true);

			return newSubmissionTempId;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	} // end of createNewSubmission(submission, userName)
	
	/**
	 * @author xingjun
	 * 
	 */
	public int updateSubmissionDbStatusByBatch(int batchId, int status, String userName) {
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("SUBMISSION");;
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("SUBMISSION_MULTI");
		ParamQuery parQ = null; 
		parQ = AnnotationToolQuery.getParamQuery("UPDATE_SUBMISSION_DB_STATUS_BY_BATCH");
		
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, status);
        	prepStmt.setInt(2, batchId);
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
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
	 * @author xingjun - 24/12/2008
	 * @return
	 */
	public int updateSubmissionDbStatusByLabAndSubDateAndState(int labId, String subDate, 
			int status, String userName, int subState, int isPublicValue) {
//		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState!!!!!");
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("SUBMISSION");;
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("SUBMISSION_MULTI");
		ParamQuery parQ = null; 
		parQ = DBUpdateSQL.getParamQuery("UPDATE_SUBMISSION_STATUS_BY_PI_N_DATE_N_STATE");
		
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState:sql: " + queryString);
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
//    		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState:status: " + status);
//    		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState:subDate: " + subDate);
//    		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState:labId: " + labId);
//    		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState:subState: " + subState);
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, status);
        	prepStmt.setInt(2, isPublicValue);
        	prepStmt.setString(3, subDate);
        	prepStmt.setInt(4, labId);
        	prepStmt.setInt(5, subState);
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
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
	 * @author xingjun - 01/07/2011 - overloading version
	 */
	public int updateSubmissionDbStatusByLabAndSubDateAndState(int labId, String subDate, String archiveId, 
			int status, String userName, int subState, int isPublicValue) {
//		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState!!!!!");
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("SUBMISSION");;
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("SUBMISSION_MULTI");
		ParamQuery parQ = null; 
		parQ = DBUpdateSQL.getParamQuery("UPDATE_SUBMISSION_STATUS_BY_PI_N_DATE_N_STATE");
		
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//		System.out.println("AnnotationTestDAO:updateSubmissionDbStatusByLabAndSubDateAndState:sql: " + queryString);
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, status);
        	prepStmt.setInt(2, isPublicValue);
        	prepStmt.setString(3, subDate);
        	prepStmt.setInt(4, labId);
        	prepStmt.setInt(5, subState);
        	prepStmt.setInt(6, Integer.parseInt(archiveId)); // xingjun - 01/07/2011
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
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
	 * @author xingjun - 20/08/2008
	 * @param batchId
	 * @param status
	 * @param userName
	 * @return
	 */
	public int updateBatchUserStatusByBatch(int batchId, int status, String userName) {
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("BATCH_USER");;
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("BATCH_USER_MULTI");
		ParamQuery parQ = null; 
		parQ = AnnotationToolQuery.getParamQuery("UPDATE_BATCH_USER_STATUS_BY_BATCH");
		
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, status);
        	prepStmt.setInt(2, batchId);
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
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

	public String getStageFromSubmission(String submissionId) {
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = AnnotationToolQuery.getParamQuery("GET_STAGE_FROM_SUBMISSION");
		String stage  = null;
		try {
			
			parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionId);

            // execute
            resSet = prepStmt.executeQuery();
            
            if(resSet.first()) {
            	stage = resSet.getString(1);
            }
            return stage;
        } catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		finally {
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
		}
	}
	
	/**
	 * @author xingjun - 20/11/2009
	 * <p>xingjun - 26/02/2010 - stage value should be set as TSxxx</p>
	 * @param submissionId
	 * @return
	 */
	public Submission getSubmissionSummary(String submissionId) {
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = AnnotationToolQuery.getParamQuery("SUBMISSION_SUMMARY");
		String queryString = parQ.getQuerySQL();
//		System.out.println("AnnotationTestDAO:getSubmissionSummary:sql: " + queryString);
		Submission submission  = null;
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
            prepStmt.setString(1, submissionId);

            // execute
            resSet = prepStmt.executeQuery();
            
            if(resSet.first()) {
            	submission = new Submission();
            	submission.setAccID(resSet.getString(2));
            	submission.setStage("TS" + resSet.getString(3));// xingjun - 26/02/2010
            	submission.setDbStatusId(resSet.getInt(4));
            }
            return submission;
            
        } catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		finally {
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
		}
	}

	/**
	 * @author xingjun - 11/07/2008
	 */
	public ArrayList<String[]> getComponentAndExpression(String submissionId) {
		ParamQuery parQ =  AnnotationToolQuery.getParamQuery("COMPONENT_AND_EXPRESSION_INFO");
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        ArrayList<String[]> result = null;
        String queryString = parQ.getQuerySQL();
        
        try {
		    if (debug)
		    	System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
		    
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	resSet = prepStmt.executeQuery();
        	result = this.formatComponentAndExpressionResultSet(resSet);
        	
        	return result;
        } catch(Exception se) {
        	se.printStackTrace();  
        	return null;
        }	
		finally {
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 11/07/2008
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<String[]> formatComponentAndExpressionResultSet(ResultSet resSet) throws SQLException {
		
		ResultSetMetaData resSetData = resSet.getMetaData();
		int columnCount = resSetData.getColumnCount();
		
		if (resSet.first()) {
			
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<String[]>();
			
			int counter = 0;
			while (resSet.next()) {
				String[] columns = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
				}
				// expression could have more than one corresponding patterns
				if (counter > 0) { // only check pattern duplication from the second record - if applicable
					int last = results.size()-1;
					String componentId = results.get(last)[0];
					String expression = results.get(last)[1];
					String strength = results.get(last)[2];
					if (!columns[0].equals(componentId)
							&& !columns[0].equals(expression) && !columns[0].equals(strength)) {
						results.add(columns);
					}
				} else {
					results.add(columns);
				}
				counter++;
			}
			return results;
		}
		return null;
	}
	
	/**
	 * @author xingjun - 01/10/2008
	 * <p>xingjun - 11/12/2009 - temporary sub id will exceed the Integer data type range if its too big, change it into String </p>
	 * @return
	 */
	public ArrayList<String[]> getExpressionAndPattern(String submissionId, String componentId) {
		ParamQuery parQ = 
			AnnotationToolQuery.getParamQuery("EXPRESSION_AND_PATTERN_INFO");
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        ArrayList<String[]> result = null;
        String queryString = parQ.getQuerySQL();
        
        // if it's not a temporary submission, the alphabetic part of the id should be removed 
        String subId = "";
        if (submissionId.substring(0, 6).equals("999999")) {
        	subId = submissionId;
        } else {
        	subId = submissionId.substring(7);
        }
        
        try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
//        	prepStmt.setInt(1, Integer.parseInt(subId)); // submission fk
        	prepStmt.setString(1, subId); // submission fk
        	prepStmt.setString(2, componentId); // component id
        	resSet = prepStmt.executeQuery();
        	result = DBHelper.formatResultSetToArrayList(resSet);
            return result;
        } catch(Exception se) {
        	se.printStackTrace();       
            return null;
        }	
        finally{
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);
        }
	}
	
	/**
	 * @author xingjun - 11/07/2008
	 * return 1: success
	 * return 0: unsuccess
	 */
	public int deleteLocation(String submissionId, String componentId, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN_LOCATION");
		
		ParamQuery parQ = AnnotationToolQuery.getParamQuery("DELETE_LOCATION_BY_SUB_COMP_ID");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
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
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun - 11/07/2008
	 */
	public int deletePattern(String submissionId, String componentId, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("PATTERN");
		
		ParamQuery parQ = AnnotationToolQuery.getParamQuery("DELETE_PATTERN_BY_SUB_COMP_ID");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	prepStmt.setString(2, submissionId);
        	prepStmt.setString(3, submissionId);
        	prepStmt.setString(4, componentId);
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
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * @author xingjun - 15/07/2008
	 * update submission embryo stage
	 * 
	 */
	public int updateStage(String submissionId, String stage, String userName) {
		// initialising
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("SUBMISSION");;
		ParamMap parMColumn = DBUpdateSQL.getColumnMap("SUBMISSION_MULTI");
		ParamQuery parQ = null; 
		parQ = AnnotationToolQuery.getParamQuery("UPDATE_SUBMISSION_STAGE");
		
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int updatedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;

        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "update");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, parMColumn.getParamName());
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// update
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, stage.substring(2));
        	prepStmt.setString(2, submissionId);
        	updatedRecordNumber = prepStmt.executeUpdate();
        	
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
	 * @author xingjun - 17/07/2008
	 * delete specified submission
	 */
	public int deleteSubmission(String submissionId, String userName) {
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("SUBMISSION");
		
		ParamQuery parQ = AnnotationToolQuery.getParamQuery("DELETE_SUBMISSION");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("deleteSubmission sql: " + queryString);
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, submissionId);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " submission deleted!");
        	
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
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}// end of deleteSubmission
	
	/**
	 * @author xingjun - 19/08/2008
	 * <p>delete specified batch</p>
	 * @param submissionId
	 * @param userName
	 * @return
	 */
	public int deleteBatch(int batchId, String userName) {
        ParamQuery parQLog = DBUpdateSQL.getParamQuery("WRITE_USER_INTO_LOG_TABLE");
		PreparedStatement prepStmtLog = null;
		ParamMap parMTable = DBUpdateSQL.getTableMap("BATCH_USER");
		
		ParamQuery parQ = AnnotationToolQuery.getParamQuery("DELETE_BATCH");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("deleteBatch sql: " + queryString);
        int deletedRecordNumber = 0;
        String queryStringLog = parQLog.getQuerySQL();
        int loggedRecordNumber = 0;
        
        // perform the query
        try {
        	conn.setAutoCommit(false);
        	
        	// log
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryStringLog.toLowerCase());
        	prepStmtLog = conn.prepareStatement(queryStringLog);
        	prepStmtLog.setString(1, "delete");
        	prepStmtLog.setString(2, userName);
        	prepStmtLog.setString(3, parMTable.getParamName());
        	prepStmtLog.setString(4, "");
        	loggedRecordNumber = prepStmtLog.executeUpdate();
//        	System.out.println(loggedRecordNumber + " records logged!");
        	
        	// delete
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, batchId);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " batch deleted!");
        	
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
        	DBHelper.closePreparedStatement(prepStmt);
        }
	}
	
	/**
	 * <p>expression should be either 'present', 'not detected', or 'uncertain'</p>
	 * <p>0 - no confilction</p>
	 * <p>1 - conflict with ancestor node - present</p>
	 * <p>2 - conflict with decendant node - not detected</p>
	 * <p>3 - conflict with ancestor node - uncertain</p>
	 */
	public int checkConflict(String componentId, ArrayList components, String stage, String expression) {
		ParamQuery componentPQ = null;
		PreparedStatement componentPS = null;
		ResultSet resSet = null;
		// get list of component ids
		int len = components.size();
		if (len == 0) {
			return 0; // no conflict when user annotate the expression at the first time
		}
		String componentList = "(";
		if (len == 1) {
			componentList += "'" + components.get(0) + "')";
		} else {
			for (int i=0;i<len;i++) {
				componentList += "'" + components.get(i) + "', ";
			}
			componentList = componentList.substring(0, componentList.lastIndexOf(',')) + ")";
		}
//		System.out.println("componentList: " + componentList);
		// assemble sql
		int errorCode = 0;
		if (expression.equals("present")) {
			componentPQ = AnnotationToolQuery.getParamQuery("PARENT_NODES");
		} else if (expression.equals("not detected")) {
			componentPQ = AnnotationToolQuery.getParamQuery("CHILD_NODES");
		} else if (expression.equals("uncertain")) {
			componentPQ = AnnotationToolQuery.getParamQuery("PARENT_NODES");
		}
		String queryString = componentPQ.getQuerySQL() + componentList;
//		System.out.println("query: " +  queryString);
		// query
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
			componentPS = conn.prepareStatement(queryString);
			componentPS.setString(1, componentId);
			componentPS.setString(2, stage);
			resSet = componentPS.executeQuery();
			if (resSet.first()) {
				if (expression.equals("present")) {
					errorCode = 1;
				} else if (expression.equals("not detected")) {
					errorCode = 2;
				} else if (expression.equals("uncertain")) {
					errorCode = 3;
				}
			}
			return errorCode;
		} catch(SQLException se) {
			se.printStackTrace();
			return errorCode;
		} finally {
			try {
				if (componentPS != null) {
					componentPS.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
        	DBHelper.closePreparedStatement(componentPS);
        	DBHelper.closeResultSet(resSet);
		}
	} // end of checkConflict
	
	
	public int addSubmission(String batch, UserBean ub) {
		/* 1. create a new row in ISH_SUBMISSION, if it is brand new batch, create a new batch as well
		 * update REF_BATCH table?
		 * 2. wirte SUB_SUBMITTER_FK, SUB_PI_FK, 
		 * SUB_DB_STATUS_FK=2, SUB_PROJECT_FK, SUB_BATCH, 
		 * SUB_ASSAY_TYPE, SUB_SUB_DATE, SUB_IS_PUBLIC=0, SUB_IS_DELETED=0,
		 * SUB_VERSION_NO, SUB_ACCESSION_ID
		 * 
		 */
		return 0;
	}
	
	public int completeBatch(String batch, UserBean ub) {
		//set SUB_DB_STATUS_FK=3
		return 0;
	}
	
	/**
	 * @author xingjun - 14/05/2010
	 * <p>return theiler stages and dpc stages as well</p>
	 * @return
	 */
	public ArrayList<String[]> getStageRanges() {
		ResultSet resSet = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		parQ = DBQuery.getParamQuery("STAGES_IN_PERSPECTIVE_FROM_TS17");
		String queryString = parQ.getQuerySQL();
//		System.out.println("AnnotationTestDAO:getStageRanges:sql " + queryString);
		
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			ArrayList<String[]> stages = this.formatStageRangeResultSet(resSet);
//			System.out.println("AnnotationTestDAO:getStageRanges:stage#" + stages.size());
			return stages;
			
		} catch(SQLException se) {
			se.printStackTrace();
			return null;
		}
		finally {
			//close the statement and result set
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
		}
	}
	
	/**
	 * @author xingjun - 14/05/2010
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<String[]> formatStageRangeResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			ArrayList<String[]> results = new ArrayList<String[]>();
			resSet.beforeFirst();
			while (resSet.next()) {
				String[] stages = new String[2];
				stages[0] = resSet.getString(1);// theiler stage
				stages[1] = resSet.getString(2);// dpc stage
				results.add(stages);
			}
			return results;
		}
		return null;
	}
	
	public ArrayList<String> getTreeContent(String stage, String id) {
		
		ResultSet resSet = null;
		
		PreparedStatement prepStmt = null;
		ArrayList<String> treeContent = null;
		
		ParamQuery parQ = DBQuery.getParamQuery("SUB_HAS_ANNOTATION");

		try {
			
			parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, id);

            // execute
            resSet = prepStmt.executeQuery();
            
            boolean hasAnnot = true;
            resSet.first();
            if(resSet.getInt(1) == 0) {
            	hasAnnot = false;
            }
			
            parQ = DBQuery.getParamQuery("ANNOT_TREE_CONTENT");
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, stage);
            prepStmt.setString(2, id);
			resSet = prepStmt.executeQuery();
			treeContent = formatTreeContentForJavascript(resSet, hasAnnot, id);
			return treeContent;
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		finally {
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
		}
	}
	
	private ArrayList<String> formatTreeContentForJavascript(ResultSet resSet, boolean hasAnnot, String accNo) throws SQLException {
		
		
		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create ArrayList to store each row of results in
            ArrayList<String> results = new ArrayList<String>();
            
            String javascriptFunc = "javascript:toggleComponent";
            String additionalInfo = "";
            
            if (!hasAnnot) {
            	results.add("ANNOTATEDTREE = 0");
                results.add("OPENATVISCERAL = 1");
            }
            else {
            	results.add("ANNOTATEDTREE = 1");
                results.add("OPENATVISCERAL = 0");
            }
            results.add("ANNOTATABLE = 1");
            results.add("HIGHLIGHT = 1");
            results.add("USETEXTLINKS = 1");
            results.add("STARTALLOPEN = 0");
            results.add("USEFRAMES = 0");
            results.add("USEICONS = 1");
            results.add("PRESERVESTATE = 0");
            results.add("SUBMISSION_ID = \"" + accNo + "\"");
            
            while (resSet.next()) {
            	
            	String patterns = "";
            	additionalInfo = "("+resSet.getString(3)+")";
            	
            	if (hasAnnot) {
            		patterns = this.getPatternsForAnnotatedComponent(resSet.getString(10));
                }
            	
            	int row = resSet.getRow() - 1;
                String componentName = resSet.getString(4);
                String id = resSet.getString(3);
                String expression = resSet.getString(8);
            
                String strength = resSet.getString(9);
            
                if(expression == null){
                    expression = "not examined";
                }
                if(strength == null){
                    strength = "";
                }
            	
                if (resSet.getInt(1) == 0) {
                    results.add("foldersTree = gFld(\"" + componentName + " " +
                            additionalInfo + "\", \"" + javascriptFunc +
                            "(&quot;" + id + "&quot;,&quot;" + componentName +
                            "&quot;,&quot;" + row + "&quot;)\"," +
                            resSet.getInt(7) + ",\"" + expression + "\",\"" +strength + "\",\""+patterns+"\","+resSet.getInt(11)+")");

                } else if (resSet.getInt(1) > 0) {

                    //if statement to determine whether the component has children. If it does,
                    //it will be displayed as a folder in the tree.
                    if (resSet.getInt(6) > 0) {
                        //if the parent of the component is at depth 0, add 'foldersTree' to the code
                        if (resSet.getInt(1) == 1) {
                            results.add("aux1 = insFld(foldersTree, gFld(\"" +
                                    componentName + " " + additionalInfo +
                                    "\", \"" + javascriptFunc + "(&quot;" +
                                    id + "&quot;,&quot;" + componentName +
                                    "&quot;,&quot;" + row + "&quot;)\"," +
                                    resSet.getInt(7) + ",\"" +
                                    expression + "\",\"" +
                                    strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        } else {
                            results.add("aux" + resSet.getInt(1) +
                                    " = insFld (aux" +
                                    (resSet.getInt(1) - 1) + ", gFld(\"" +
                                     componentName + " " + additionalInfo +
                                     "\", \"" + javascriptFunc + "(&quot;" +
                                     id + "&quot;,&quot;" + componentName +
                                     "&quot;,&quot;" + row + "&quot;)\"," +
                                     resSet.getInt(7) + ",\"" +
                                     expression + "\",\"" +
                                     strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        }
                    } else {
                        if (resSet.getInt(1) == 1) {
                            results.add("insDoc(foldersTree, gLnk(\"S\", \"" +
                                    componentName + " " + additionalInfo +
                                    "\", \"" + javascriptFunc + "(&quot;" +
                                    id + "&quot;,&quot;" + componentName +
                                    "&quot;,&quot;" + row + "&quot;)\"," +
                                    resSet.getInt(7) + ",\"" +
                                    expression + "\",\"" +
                                    strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        } else {
                            results.add("insDoc(aux" + (resSet.getInt(1) - 1) +
                                    ", gLnk(\"S\", \"" + componentName +
                                    " " + additionalInfo + "\", \"" +
                                    javascriptFunc + "(&quot;" + id +
                                    "&quot;,&quot;" + componentName +
                                    "&quot;,&quot;" + row + "&quot;)\"," +
                                    resSet.getInt(7) + ",\"" +
                                    expression + "\",\"" +
                                    strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        }
                    }
                }       	
            }
            return results; 
		}
		return null;
	}
	
	private String getPatternsForAnnotatedComponent(String expressionOID) throws SQLException{
        ParamQuery parQ = DBQuery.getParamQuery("EXPRESSION_PATTERNS");
        PreparedStatement prepStat = null;
        ResultSet resSet = null;
        try {
          parQ.setPrepStat(conn);
          prepStat = parQ.getPrepStat();
          prepStat.setString(1, expressionOID);
          resSet = prepStat.executeQuery();
          if(resSet.first()){
              resSet.beforeFirst();
              StringBuffer patterns = new StringBuffer("");
              while(resSet.next()){
                if(resSet.isLast()) {
                    patterns.append(resSet.getString(2));
                }
                else {
                    patterns.append(resSet.getString(2)+",");
                }
              }
              return patterns.toString();
          }
          return "";
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
        finally {
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStat);
        }
    }
	
	/**
	 * @author xingjun - 01/09/2009
	 */
	public int getBatchStatus(String batchId) {
		int batchStatus = 0;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ =
			AnnotationToolQuery.getParamQuery("GET_BATCH_STATUS");
		String queryString = parQ.getQuerySQL();
//		System.out.println("AnnotationTestDAO:getBatchStatus:SQL: " + queryString);
		try {
		    if (debug)
			System.out.println("MySQLAnnotationTestDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setInt(1, Integer.parseInt(batchId));
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				batchStatus = resSet.getInt(1);
			}
			// release db resource
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
			return batchStatus;
		} catch (SQLException se) {
			se.printStackTrace();
			return 0;
		}
        finally {
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
        }
	}
}
