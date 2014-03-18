package gmerg.assemblers;

import gmerg.db.AnnotationTestDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ish.ISHBatchSubmission;
import gmerg.entities.User;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.ish.ISHBatch;

import java.sql.Connection;
import java.util.ArrayList;

public class BatchAnnotationAssembler {
    private boolean debug = false;
	public BatchAnnotationAssembler() {
	    if (debug)
	    System.out.println("BatchAnnotationAssembler.constructor");

	}
	
	/**
	 * @author xingjun - 03/07/2008
	 * if there's any incompleted batch, return; or create a new one and return it
	 * @param pi
	 * @return
	 */
	public String getLatestBatchByPI(String pi, User user) {
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			
			// get data
			// find incomplete batch - if exist
			int batchId =
				testDAO.getIncompletedSubmissionBatchId(Integer.parseInt(pi), user);
			if (batchId == -1) { // there's no incompleted batch, need create a new one
				batchId = testDAO.createNewBatch(Integer.parseInt(pi), user);
			}
			// release resources
			testDAO = null;
			conn = null;
	
			// return
			if (batchId == -1) {
				return null;
			} else {
				return Integer.toString(batchId);
			}
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::getLatestBatchByPI failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun - 12/08/2008
	 * @param pi
	 * @param user
	 * @return
	 */
	public ISHBatch[] getBatchListByPI(String pi, User user) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			
			// get data
			ISHBatch[] batches = testDAO.getBatchListByPi(Integer.parseInt(pi));
			return batches;
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::getBatchListByPI failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

	/**
	 * @author xingjun - 12/08/2008
	 * @param pi
	 * @param user
	 * @return
	 */
	public int addBatch(String pi, User user) {
		// get dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			int batchId = testDAO.createNewBatch(Integer.parseInt(pi), user);
			if (batchId < 0) {
				return 0;
			} else {
				return 1;
			}
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::addBatch failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * Although could be used to get submissions in any status, it's most used
	 * to retrieve incompleted submission by ISHBatchSubmissionBean
	 * 
	 * @author xingjun - 03/07/2008
	 * @param batchId
	 * @return
	 */
	public ISHBatchSubmission[] getSubmissionByBatchId(String batchId) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			
			// get data
			ISHBatchSubmission[] submissions = testDAO.getSubmissionByBatchId(Integer.parseInt(batchId));
			
			// return
			return submissions;
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::getSubmissionByBatchId failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun 09/07/2008
	 * create a new temporary submission
	 * @param batchId
	 * @param user
	 * @return
	 */
	public int addSubmission(String batchId, User user, String pi) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			int addedSubmissionNumber = 
				testDAO.createNewSubmission(Integer.parseInt(batchId), user, Integer.parseInt(pi));
			
			return addedSubmissionNumber;
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::addBatch failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
		
	}
	
	/**
	 * @author xingjun - 17/07/2008
	 * <p>delete all information linked to specified submission ids</p>
	 * <p>modified by xingjun - 19/08/2008 - 
	 * moved the deleting single submission code into deleteSubmission method</p>
	 * @param submissionIds
	 * @param user
	 * @return
	 */
	public int deleteTemporarySubmission(String[] submissionIds, User user) {
		// validate submission ids
		if (submissionIds == null) {
			return 0;
		}
		int len = submissionIds.length;
		if (len == 0) {
			return 0;
		}
		for (int i=0;i<len;i++) {
			if (submissionIds[i] == null || submissionIds[i].equals("")) {
				return 0;
			}
		}
		
		// go through the submission id to delete submission, expression, pattern, locaiton, note
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			String userName = user.getUserName();
			for (int i=0;i<len;i++) {
				int submissionDeleted = this.deleteSubmission(submissionIds[i],
						testDAO, ishEditDAO, ishDAO, userName);
				if (submissionDeleted == 0) { // failed to delete
					return 0;
				}
			}
			return 1;
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::addBatch failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	} // end of deleteTemporarySubmission
	
	/**
	 * @author xingjun - 19/08/2008
	 * <p>delete specified submission, including expression info</p>
	 * @param submissionId
	 * @param testDAO
	 * @param ishEditDAO
	 * @param ishDAO
	 * @param userName
	 * @return
	 */
	public int deleteSubmission(String submissionId, AnnotationTestDAO testDAO, 
			ISHEditDAO ishEditDAO, ISHDAO ishDAO, String userName) {
		// get expression
		ArrayList expressionInDB = testDAO.getComponentAndExpression(submissionId);
		// only delete if there's expression
		if (expressionInDB != null && expressionInDB.size() != 0) {
			int size = expressionInDB.size();
			for (int j=0;j<size;j++) {
				String componentId = ((String[])expressionInDB.get(j))[0];
//				String expression = ((String[])expressionInDB.get(j))[1];
				String pattern = ((String[])expressionInDB.get(j))[3];
				String note = ((String[])expressionInDB.get(j))[4];
				String expressionId = ((String[])expressionInDB.get(j))[5];
				// only delete pattern if they/it exist(s)
				if (pattern != null && !pattern.equals("")) {
					ExpressionPattern [] patterns =
						ishDAO.findPatternsAndLocations(expressionId);
					int deletedRecordNumber = 
						EditAssemblerUtil.deleteAllPatternAndLocation(submissionId, 
								componentId, ishEditDAO, patterns, userName);
					if (deletedRecordNumber == 0) {
//						System.out.println("failed to delete pattern and location!!!!!");
						return 0;
					}
				}
				// only delete note if it exists
				if (note != null && !note.equals("")) {
					int deletedRecordNumber =
						ishEditDAO.deleteExpressionNotes(submissionId, componentId, userName);
					if (deletedRecordNumber == 0) {
//						System.out.println("failed to delete note!!!!!");
						return 0;
					}
				}
				// delete expression
				int deletedRecordNumber =
					ishEditDAO.deleteExpression(submissionId, componentId, userName);
				if (deletedRecordNumber == 0) {
//					System.out.println("failed to delete expression!!!!!");
					return 0;
				}
			}
		}
		// delete submission detail
		int deletedRecordNumber = testDAO.deleteSubmission(submissionId, userName);
		if (deletedRecordNumber == 0) {
//			System.out.println("failed to delete submission detail!!!!!");
			return 0;
		}
		return 1; // successful
	} // end of deleteSubmission
	
	/**
	 * @author xingjun - 16/07/2008
	 * <p>create a new temporary submission based on specific submission</p>
	 * <p>14/08/2009 - modified to receive new temporary submission as a string</p>
	 * <p>xingjun - 26/02/2010 - added 'uncertain' along with 'present' when check the expression value</p>
	 */
	public int duplicateSubmission(String submissionId, User user) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
			
			// get template submission info from database
			Submission templateSubmission = ishDAO.findSubmissionById(submissionId);
			
			// get expression info
			ArrayList expressionInDB = testDAO.getComponentAndExpression(submissionId);
	
			////////////////////////////
			// need to implement later
			// other info includes: probe/antibody, specimen, image, author,
			// publication, acknowledgement, linked submission etc
			///////////////////////////
			
			String userName = user.getUserName();
	
			// insert new submission based on the existing submission
	//		int newSubmissionTempId = testDAO.createNewSubmission(templateSubmission, userName);
			String newSubmissionTempId = testDAO.createNewSubmission(templateSubmission, userName);
	//		System.out.println("newSubmissionTempId: " + newSubmissionTempId);
	//		if (newSubmissionTempId == 0) {
			if (newSubmissionTempId.equals("0")) {
				return 0;
			}
			
			int insertedNewRecordNumber = 0;
			// insert expression
			if (expressionInDB != null && expressionInDB.size() != 0) {
				int len = expressionInDB.size();
				for (int i=0;i<len;i++) {
					String componentId = ((String[])expressionInDB.get(i))[0];
					String expression = ((String[])expressionInDB.get(i))[1];
					String strength = ((String[])expressionInDB.get(i))[2];
					String pattern = ((String[])expressionInDB.get(i))[3];
					String note = ((String[])expressionInDB.get(i))[4];
					String expressionId = ((String[])expressionInDB.get(i))[5];
					if (strength == null) { // in case there's no strength
						strength = "";
					}
					insertedNewRecordNumber =
	//					ishEditDAO.insertExpression(Integer.toString(newSubmissionTempId), 
						ishEditDAO.insertExpression(newSubmissionTempId, 
								componentId, expression, strength, userName);
					if (insertedNewRecordNumber == 0) {
						return 0;
					}
					// insert expression note
					if (note != null && !note.equals("")) {
						insertedNewRecordNumber =
	//						ishEditDAO.addExpressionNote(Integer.toString(newSubmissionTempId),
							ishEditDAO.addExpressionNote(newSubmissionTempId,
									componentId, note, 1, userName);
						if (insertedNewRecordNumber == 0) {
							return 0;
						}
					}
					// insert pattern/location 
					// xingjun - 26/02/2010 - should include 'uncertain' into the judgement
					if ((expression.equals("present") || expression.equals("uncertain")) && pattern != null && !pattern.equals("")) {
						ExpressionPattern[] patterns =
							ishDAO.findPatternsAndLocations(true, expressionId);
	//					System.out.println("locationString: " + patterns[0].getLocations());
	//					System.out.println("locationAPart:" + patterns[0].getLocationAPart());
	//					System.out.println("locationNPart" + patterns[0].getLocationNPart());
						if (patterns != null) {
							insertedNewRecordNumber =
								EditAssemblerUtil.insertMultiplePatternAndLocation(ishEditDAO, patterns,
	//									Integer.toString(newSubmissionTempId), componentId, userName);
										newSubmissionTempId, componentId, userName);
							if (insertedNewRecordNumber == 0) {
								return 0;
							}
						}
					} // end of insert pattern/location
				} // end of for loop
			} // end of insert expression
			return 1; // success
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::addBatch failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	} // end of duplicateSubmission()
	
	/**
	 * @author xingjun 17/07/2008
	 * <p>set db status to 3 - Awaiting Spreadsheet
	 * for time being we set it to 3, in future may need to change it to 5 - Awaiting QA</p>
	 * <p>modified by xingjun - 20/08/2008 changed the code to update submission table
	 * and status user table separately - it's a one-step approach before</p>
	 * @param batchId
	 * @param user
	 * @return 0 - application error
	 *         1 - successful
	 */
	public int completeBatch(String batchId, User user) {
		// get dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			
			// update database
			// set the db status to 3 - awaiting spreadsheet
			// update submission table if there's any submission
			String[] submissionIds = 
				testDAO.getSubmissionIdsByBatchId(Integer.parseInt(batchId));
			int batchUpdated = 0; 
			if (submissionIds != null && submissionIds.length > 0) {
				batchUpdated = 
					testDAO.updateSubmissionDbStatusByBatch(Integer.parseInt(batchId), 3, user.getUserName());
				if (batchUpdated == 0) {
					return 0;
				}
			}
			// update batch user table
			batchUpdated =
				testDAO.updateBatchUserStatusByBatch(Integer.parseInt(batchId), 3, user.getUserName()); 
			
			if (batchUpdated == 0) {
				return 0;
			} else {
				return 1;
			}
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::completeBatch failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun - 19/08/2008
	 * <p>batch version of complteBatch</p>
	 * @param batchIds
	 * @param user
	 * @return 0 - application error; 1 - successful
	 */
	public int completeBatches(String[] batchIds, User user) {
		if (batchIds == null) {
			return 0;
		}
		int len = batchIds.length;
		if (len == 0) {
			return 0;
		}
		for (int i=0;i<len;i++) {
			if (batchIds[i] == null || batchIds[i].equals("")) {
				return 0;
			}
		}
		// go through the batch id array, change submission status for all batch ids
		for (int i=0;i<len;i++) {
			int batchUpdated = this.completeBatch(batchIds[i], user);
			if (batchUpdated == 0) { // failed to complete
				return 0;
			}
		}
		return 1; // successful
	}

	/**
	 * @author xingjun - 15/08/2008
	 * <p>check the locking status for the given batches</p>
	 * @param batchIds
	 * @param user
	 * @return
	 */
	public String[] getUnlockedBatchList(String[] batchIds, User user) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
			
			// get data
			int len = batchIds.length;
			ArrayList<String> unlockedBatches = new ArrayList<String>();
			for (int i=0;i<len;i++) {
				if (!ishEditDAO.isBatchLocked(Integer.parseInt(batchIds[i]), user.getUserId())) {
					unlockedBatches.add(batchIds[i]);
				}
			}
			// release resources
			ishEditDAO = null;
			conn = null;
			// return
			if (unlockedBatches.size() > 0) { // at least one batch unlocked
				return (String[])unlockedBatches.toArray(new String[unlockedBatches.size()]);
			} else {
				return null;
			}
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::getUnlockedBatchList failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public int unlockSubmission(String submissionId, User user) {
		return 0;
	}
	
	/**
	 * @author xingjun - 19/08/2008
	 * <p>delete all information related to the given batches</p>
	 * @param batchIds
	 * @param user
	 * @return
	 */
	public int deleteBatch(String[] batchIds, User user) {
		if (batchIds == null) {
			return 0;
		}
		int len = batchIds.length;
		if (len == 0) {
			return 0;
		}
		// go through the batch id array, delete submission and batch id
		for (int i=0;i<len;i++) {
			int batchDeleted = this.deleteBatch(batchIds[i], user);
			if (batchDeleted == 0) { // failed to delete given batch
				return 0;
			}
		}
		return 1; // successful
	} // end of deleteBatch(String[] batchIds, User user)
	
	/**
	 * @author xingjun - 19/08/2008
	 * <p>delete the given batch and all related submissions</p>
	 * @param batchId
	 * @param user
	 * @return
	 */
	public int deleteBatch(String batchId, User user) {
		// validate the batch id
		if (batchId == null || batchId.equals("")) {
			return 0;
		}
		try {
			int batch = Integer.parseInt(batchId);
		} catch(NumberFormatException nfe) {
			return 0;
		}
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			String userName = user.getUserName();
			// get submissions
			String[] submissionIds = 
				testDAO.getSubmissionIdsByBatchId(Integer.parseInt(batchId));
			// delete submissions
			if (submissionIds != null && submissionIds.length > 0) {
				int submissionIdNum = submissionIds.length;
				for (int j=0;j<submissionIdNum;j++) {
					int submissionDeleted = this.deleteSubmission(submissionIds[j],
							testDAO, ishEditDAO, ishDAO, userName);
					if (submissionDeleted == 0) { // failed to delete submission
						return 0;
					}
				}
			}
			// delete batch
			int batchDeleted = testDAO.deleteBatch(Integer.parseInt(batchId), userName);
			if (batchDeleted == 0) { // failed to delete batch
				return 0;
			}
			return 1; // successful
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::deleteBatch failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}

	} // end of deleteBatch(String batchId, User user)
	
	/**
	 * @author xingjun - 01/09/2009
	 * @param batchId
	 * @return
	 */
	public int getBatchStatus(String batchId) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			
			// get data
			int batchStatus = testDAO.getBatchStatus(batchId);
	
			// return
			return batchStatus;
		}
		catch(Exception e){
			System.out.println("BatchAnnotationAssembler::getBatchStatus failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	

	public int updateSubmissionStatusByPIandDate(int pi, String date) {
		return 0;
	}
		
}
