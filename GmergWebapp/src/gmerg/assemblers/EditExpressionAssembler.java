/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.db.AnnotationTestDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.LockingInfo;
import gmerg.entities.submission.Submission;

/**
 * @author xingjun
 *
 */
public class EditExpressionAssembler {

	public ExpressionDetail getData(String submissionAccessionId, String componentId) {
        if (submissionAccessionId == null || submissionAccessionId.equals("")) {
        	return null;
        }
        if (componentId == null || componentId.equals("")) {
        	return null;
        }
        
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		ExpressionDetail expressionDetail = null;
		
		// get expressionDetail
		// if expression is null
		//    get stage
		//    get component details
		//    set strength as 'not examined'
		// else
		//    get notes
		//    assemble and return
		expressionDetail =
			ishDAO.findExpressionDetailBySubmissionIdAndComponentId(submissionAccessionId, componentId);
		if (expressionDetail != null) {
//			System.out.println("strength: " + expressionDetail.getPrimaryStrength());
			
			expressionDetail.setAnnotated(true);
			String expressionNotes = ishDAO.findAnnotationNote(submissionAccessionId, componentId);
			ExpressionPattern [] patterns = ishDAO.findPatternsAndLocations(String.valueOf(expressionDetail.getExpressionId()));
			expressionDetail.setPattern(patterns);
                        
			// fill out the expression detail object
			expressionDetail.setSubmissionId(submissionAccessionId);
			expressionDetail.setExpressionNote(expressionNotes);
		} else {
			try {
				expressionDetail = new ExpressionDetail();
				expressionDetail.setAnnotated(false);
				
				String stage = ishDAO.findStageBySubmissionId(submissionAccessionId);
				ArrayList componentDetail = ishDAO.findComponentDetailById(componentId);
				
				expressionDetail.setSubmissionId(submissionAccessionId);
				expressionDetail.setStage(stage);
				expressionDetail.setComponentId(((String)componentDetail.get(0)));
				expressionDetail.setComponentName(((String)componentDetail.get(1)));
				expressionDetail.setComponentDescription(((ArrayList)componentDetail.get(2)));
				expressionDetail.setPrimaryStrength("");
				
//				expressionDetail.setSecondaryStrength("");
//				ExpressionPattern[] patterns = new ExpressionPattern[1];
//				ExpressionPattern pattern = new ExpressionPattern();
//				pattern.setPattern("");
//				patterns[0] = pattern;
//				expressionDetail.setPattern(patterns);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the value object---  */
		return expressionDetail;
	} // getData
	
	/**
	 * <p>modified by xingjun - 24/11/2009 - added code to get dbStatus for given submission if there's no linked expression</p>
	 * @param submissionAccessionId
	 * @param componentId
	 * @return
	 */
	public ExpressionDetail getExpression(String submissionAccessionId, String componentId) {
        if (submissionAccessionId == null || submissionAccessionId.equals("")) {
        	return null;
        }
        if (componentId == null || componentId.equals("")) {
        	return null;
        }
        
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		AnnotationTestDAO annotationTestDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
		ExpressionDetail expressionDetail = null;
		
		// get expressionDetail
		// if expression is null
		//    get stage
		//    get component details
		//    set strength as 'not examined'
		// else
		//    get notes
		//    assemble and return
		expressionDetail =
			ishDAO.findExpressionDetailBySubmissionIdAndComponentId(submissionAccessionId, componentId);
		if (expressionDetail != null) {
//			System.out.println("##############edit expression detail is not null############");
//			System.out.println("strength: " + expressionDetail.getPrimaryStrength());
			
			String expressionNotes = ishDAO.findAnnotationNote(submissionAccessionId, componentId);
			
//			ExpressionPattern [] patterns = ishDAO.findPatternsAndLocations(String.valueOf(expressionDetail.getExpressionId()));
//			expressionDetail.setPattern(patterns);
                        
			// fill out the expression detail object
			expressionDetail.setSubmissionId(submissionAccessionId);
			expressionDetail.setExpressionNote(expressionNotes);
			expressionDetail.setAnnotated(true);
			
		} else {
//			System.out.println("##############edit expression detail is null############");
			try {
				// xingjun - 24/11/2009 - start
				Submission submission = annotationTestDAO.getSubmissionSummary(submissionAccessionId);
//				String stage = ishDAO.findStageBySubmissionId(submissionAccessionId);
				String stage = submission.getStage();
				int dbStatus = submission.getDbStatusId();
				// xingjun - 24/11/2009 - end
				ArrayList componentDetail = ishDAO.findComponentDetailById(componentId);
				expressionDetail = new ExpressionDetail();
				expressionDetail.setSubmissionId(submissionAccessionId);
				expressionDetail.setStage(stage);
				expressionDetail.setSubmissionDbStatus(dbStatus);// xingjun - 24/11/2009
				
				expressionDetail.setComponentId(((String)componentDetail.get(0)));
				expressionDetail.setComponentName(((String)componentDetail.get(1)));
				expressionDetail.setComponentDescription(((ArrayList)componentDetail.get(2)));
				
				expressionDetail.setPrimaryStrength("");
				expressionDetail.setAnnotated(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the value object---  */
//		System.out.println("editExpressionAssembler:getExpression: " + expressionDetail == null?"not expressed":"expressed");
		return expressionDetail;
	} // getExpression
	
	/**
	 * 
	 * @param expressionId
	 * @return
	 */
	public ExpressionPattern[] getPatterns(String expressionId) {
//		System.out.println("getPatterns method in assembler########");
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// retrieve data
		ExpressionPattern [] patterns = ishDAO.findPatternsAndLocations(true, expressionId);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the value object---  */
		return patterns;
	}
	
	/**
	 * @author xingjun - 28/07/2008
	 * @param submissionId
	 * @return
	 */
	public LockingInfo getLockingInfo(String submissionId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// retrieve data
		LockingInfo lockingInfo = ishDAO.getLockingInfo(submissionId);

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the value object---  */
		return lockingInfo;
	}
	
	/** editing methods **/
	// update primary strength in database to the specific value: present/not detected/uncertain
	// update additional strength to db if there's a value
	// log it
//	public boolean addAnnotation(String submissionId, String componentId, 
//			String secondaryStrength, ExpressionPattern[] patterns, String userName) {
//		
//		/** create dao */
//		Connection conn = DBHelper.getDBConnection();
//		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
//		
//		/** add & log */
//		int addedStrengthRowNumber = 0;
//
//		// insert new expression record
//		int addedStrengthRecordNumber = ishEditDAO.insertExpression(submissionId, componentId, secondaryStrength, userName);
//		
//		if (addedStrengthRecordNumber == 0) return false;
//		
//		// primary strength
//		addedStrengthRowNumber = ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, "present", userName);
//		
//		if (addedStrengthRowNumber == 0) return false;
//		
//		// secondary strength
//		if (secondaryStrength != null && !secondaryStrength.equals("")) {
//			addedStrengthRowNumber = 
//				ishEditDAO.updateExpressionStrengh(submissionId, componentId, 1, secondaryStrength, userName);
//		}
//		
//		// patterns
//		if (patterns != null) {
//			int addedPatternRowNumber = 
//				this.insertMultiplePatternAndLocation(ishEditDAO, patterns, submissionId, componentId, userName);
//			if (addedPatternRowNumber == 0) return false;
//		}
//		
//		/** release db resources */
//		DBHelper.closeJDBCConnection(conn);
//		ishEditDAO = null;
//		
//		/** return */
//		return true;
//	} // end of addAnnotation
	
	/**
	 * amended version - changed the type of the return value from boolean to integer
	 *                   to return more information to bean class
	 * <p>modified by xingjun - 02/03/2009 
	 * - modified null judgement statement for patterns: in case an empty array passed in</p>
	 * @return errorCode - refer to the description in EditExpressionBean class (errorCode)
	 * 
	 */
	public int addAnnotation(String submissionId, String componentId, String stage, String primaryStrength, 
			String secondaryStrength, ExpressionPattern[] patterns, String userName) {
//		System.out.println("addAnnotation#######");
//		System.out.println("primaryStrength: " + primaryStrength);
//		System.out.println("stage: " + stage);
//		System.out.println("componentId: " + componentId);
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		int errorCode = 0;
		
		/** add & log */
		// insert new expression record
		if (primaryStrength.equals("present") 
				|| primaryStrength.equals("not detected") 
				|| primaryStrength.equals("uncertain")) {
			errorCode = 
				ishEditDAO.existConflict(submissionId, stage, componentId, primaryStrength);
//			System.out.println("conflictCode: " + errorCode);
			if (errorCode != 0) {
//				System.out.println("confliction exists#######");
//				return false;
				return errorCode;
			}
		}
		
		// insert expression
		int addedStrengthRecordNumber =
			ishEditDAO.insertExpression(submissionId, componentId, primaryStrength, secondaryStrength, userName);
		if (addedStrengthRecordNumber == 0) {
			DBHelper.closeJDBCConnection(conn);
			ishEditDAO = null;
			return 7; // failed to add data into database
		}

		// insert pattern info
		if (patterns != null && patterns.length > 0 && patterns[0].getPattern() != null && !patterns[0].getPattern().equals("")) {
//			System.out.println("pattern is not null, now is adding...");
			int addedPatternRowNumber = 
				this.insertMultiplePatternAndLocation(ishEditDAO, patterns, submissionId, componentId, userName);
//			System.out.println("addedPatternRowNumber: " + addedPatternRowNumber);
			if (addedPatternRowNumber == 0) {
//				return false;
				DBHelper.closeJDBCConnection(conn);
				ishEditDAO = null;
				return 7; // failed to insert data into database
			}
		}
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		return 0;
	} // end of addAnnotation
	
	
	/**
	 * @author xingjun
	 * added code to check if expression record has been deleted - 17/07/2008
	 * <p>modified by xingjun - 02/03/2009 
	 * - modified null judgement statement for patterns: in case an empty array passed in</p>
	 * @param submissionId
	 * @param componentId
	 * @param patterns
	 * @param userName
	 * @return
	 */
	public boolean deleteAnnotation(String submissionId, String componentId, 
			ExpressionPattern[] patterns, String userName) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** delete & log */
		// delete pattern and location
		if (patterns != null && patterns.length > 0 && patterns[0].getPattern() != null && !patterns[0].getPattern().equals("")) {
//			System.out.println("pattern is not null#########");
			int deletedPatternNumber = 
				this.deleteAllPatternAndLocation(submissionId, componentId, ishEditDAO, patterns, userName);
			if (deletedPatternNumber == 0) {
				DBHelper.closeJDBCConnection(conn);
				ishEditDAO = null;
				return false;
			}
		}
//		System.out.println("pattern is null#########");
		
		// delete expression
		int deletedExpressionNumber = ishEditDAO.deleteExpression(submissionId, componentId, userName);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (deletedExpressionNumber == 0) return false;
		else return true;
	} // end of deleteAnnotation
	

	/**
	 * update secondary strength as appropriate
	 * update pattern info
	 * log it
	 * if the values are identical and not modified, still return true
	 * amended version - changed the type of the return value from boolean to integer
	 *                   to return more information to bean class
	 * 
	 * <p>modified by xingjun - 21/10/2008 
	 * - modified code to assemble location string: when involed into the '+' operation, 
	 *   null string will be allocated a value of 'null' </p>
	 * 
	 * <p>modified by xingjun - 02/03/2009 
	 * - modified null judgement statement for patterns: in case an empty array passed in</p>
     *
	 * @return errorCode - refer to the description in EditExpressionBean class (errorCode)
	 * 
	 */
	public int updateAnnotation(String submissionId, String componentId, String stage,
			String newPrimaryStrength, String oldPrimaryStrength,
			String newSecondaryStrength, String oldSecondaryStrength,
			ExpressionPattern[] patternOnPage, ExpressionPattern[] patternInDB, String userName) {
		// check if pattern info is null
		boolean patternOnPageIsNull = false;
		boolean patternInDBIsNull = false;
		if (patternOnPage == null || patternOnPage.length == 0 
				|| patternOnPage[0] == null || patternOnPage[0].getPattern() == null 
				|| patternOnPage[0].getPattern().equals("")) {
//			System.out.println("patternOnPage is null");
			patternOnPageIsNull = true;
		}
		if (patternInDB == null || patternInDB.length == 0 
				|| patternInDB[0] == null || patternInDB[0].getPattern() == null 
				|| patternInDB[0].getPattern().equals("")) {
//			System.out.println("patternInDB is null");
			patternInDBIsNull = true;
		}

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		int errorCode = 0;
		
//		if (patternInDB == null)
//			System.out.println("patternInDB is null#########");

		// update
		int updatedStrengthRowNumber = 0;
		int updatedPatternNumber = 0;
		if (oldPrimaryStrength.equals("present") || oldPrimaryStrength.equals("uncertain")) {
			if (newPrimaryStrength.equals(oldPrimaryStrength)) {
				// update secondary strength if not identical
				if (newSecondaryStrength != null && !newSecondaryStrength.equals("")) {
//					System.out.println("newSecondaryStrength is not null#######");
					if (oldSecondaryStrength == null 
							|| !oldSecondaryStrength.equals(newSecondaryStrength)) {
						updatedStrengthRowNumber = ishEditDAO.updateExpressionStrengh(submissionId, 
								componentId, 1, newSecondaryStrength, userName);
						if (updatedStrengthRowNumber == 0) {
//							 return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
					}
				} else {
					if (oldSecondaryStrength != null && !oldSecondaryStrength.equals("")) {
						updatedStrengthRowNumber = ishEditDAO.updateExpressionStrengh(submissionId, 
								componentId, 1, "", userName);
						if (updatedStrengthRowNumber == 0) {
//							return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
					}
				}
				// pattern updation
//				if (patternInDB == null) { // pattern info not exists in database
				if (patternInDBIsNull) {	
//					if (patternOnPage != null) {
					if (!patternOnPageIsNull) {
						// record by record, insert pattern and/or location value 
						// into the database & log it
						updatedPatternNumber = 
							this.insertMultiplePatternAndLocation(ishEditDAO, patternOnPage, submissionId, componentId, userName);
						if (updatedPatternNumber == 0) {
//							return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
					}
				} else { // pattern in database is not null
//					System.out.println("pattern in database is not null########");
//					if (patternOnPage == null) { // pattern on page is null
					if (patternOnPageIsNull) {
						// delete pattern and location in database & log it
//						System.out.println("pattern in db is not null: deleteAllPatternAndLocation####");
						updatedPatternNumber = 
							this.deleteAllPatternAndLocation(submissionId, componentId, ishEditDAO, patternInDB, userName);
						if (updatedPatternNumber == 0){
//							return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
						
					} else {
						int patternNumberInDB = patternInDB.length;
						int patternNumberOnPage = patternOnPage.length;
						if (patternNumberInDB == patternNumberOnPage) { // pattern numbers in db and on page are the same
//							System.out.println("patternNumberInDB == patternNumberOnPage");
							// check if only modified location
							if (patternValuesAreEqual(patternInDB, patternOnPage, patternNumberInDB)) {
//								System.out.print("patternValuesAreEqual");
								// record by record, update pattern/location value where appropriate & log it
								for (int i=0;i<patternNumberInDB;i++) {
									String ld = patternInDB[i].getLocations();
//									String lp = patternOnPage[i].getLocations();
									String la = patternOnPage[i].getLocationAPart();
									String lpa = (la==null || la.equals("")) ? "" : la;
									String ln = patternOnPage[i].getLocationNPart();
									String lpn = (ln==null || ln.equals("")) ? "" : ln;
									String lp = (lpa + " " + lpn).trim();
									if (ld != null && !ld.equals("")) { // locaiton in database is not null
										if (lp != null && !lp.equals("")) { // location on page is not null
											if (!lp.equals(ld)) { // location on page is different from the one in DB
//												System.out.println("location on page is different from the one in DB");
												updatedPatternNumber = 
													ishEditDAO.updateLocation(patternInDB[i].getPatternId(), patternInDB[i].getLocationId(), lp, userName);
												if (updatedPatternNumber == 0) {
//													return false;
													DBHelper.closeJDBCConnection(conn);
													ishEditDAO = null;
													return 7; // failed to update database
												}
											}
										} else { // location on page is null
											// delete location
//											System.out.println("delete location");
											updatedPatternNumber =  ishEditDAO.deleteLocation(patternInDB[i].getLocationId(), userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									} else { // location in database is null
										if (lp != null && !lp.equals("")) { // location on page is not null, insert where appropriate
											updatedPatternNumber = 
												ishEditDAO.insertLocation(submissionId, componentId, patternInDB[i].getPattern(), lp, userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									}
								}
							} else { // pattern values are not the same
								// delete original pattern and location and insert new values
//								System.out.println("pattern values are not the same: deleteAllPatternAndLocation####");
								updatedPatternNumber = 
									this.deleteAllPatternAndLocation(submissionId, componentId, ishEditDAO, patternInDB, userName);
								if (updatedPatternNumber == 0) {
//									return false;
									DBHelper.closeJDBCConnection(conn);
									ishEditDAO = null;
									return 7; // failed to update database
								}
								// insert
								updatedPatternNumber = 
									this.insertMultiplePatternAndLocation(ishEditDAO, patternOnPage, submissionId, componentId, userName);
								if (updatedPatternNumber == 0) {
//									return false;
									DBHelper.closeJDBCConnection(conn);
									ishEditDAO = null;
									return 7; // failed to update database
								}
							}
							
						} else if (patternNumberInDB > patternNumberOnPage) { // pattern numbers in db and on page are not identical
//							System.out.println("patternNumberInDB > patternNumberOnPage");
//							System.out.println("patternNumberInDB: " + patternNumberInDB);
//							System.out.println("patternNumberOnPage: " + patternNumberOnPage);
							if (patternValuesAreEqual(patternInDB, patternOnPage, patternNumberOnPage)) {
								for (int i=0;i<patternNumberOnPage;i++) {
									// compare locations, update values in database where appropriate & log it
									String ld = patternInDB[i].getLocations();
//									String lp = patternOnPage[i].getLocations();
									String la = patternOnPage[i].getLocationAPart();
									String lpa = (la==null || la.equals("")) ? "" : la;
									String ln = patternOnPage[i].getLocationNPart();
									String lpn = (ln==null || ln.equals("")) ? "" : ln;
									String lp = (lpa + " " + lpn).trim();
									int pid = patternInDB[i].getPatternId();
									int lid = patternInDB[i].getLocationId();
									if (ld != null && !ld.equals("")) { // locaiton in database is not null
										if (lp != null && !lp.equals("")) { // location on page is not null
											if (!lp.equals(ld)) { // location on page is different from the one in DB, update
												updatedPatternNumber = 
													ishEditDAO.updateLocation(pid, lid, lp, userName);
												if (updatedPatternNumber == 0) {
//													return false;
													DBHelper.closeJDBCConnection(conn);
													ishEditDAO = null;
													return 7; // failed to update database
												}
											}
										} else {
											// delete location
											updatedPatternNumber =  ishEditDAO.deleteLocation(lid, userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									} else { // location in database is null
										if (lp != null && !lp.equals("")) { // location on page is not null, insert where appropriate
											// add location
											updatedPatternNumber = 
												ishEditDAO.insertLocation(submissionId, componentId, patternInDB[i].getPattern(), lp, userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									}
								} // end of for loop
								// delete other pattern and location in database & log it
								int startIndexForDeletion = patternNumberOnPage;
								for (int i=startIndexForDeletion;i<patternNumberInDB;i++) {
									if (patternInDB[i].getPatternId() == patternInDB[startIndexForDeletion-1].getPatternId()) {
										// pattern values are equal, delete location only
										if (patternInDB[i].getLocations() != null) {
											updatedPatternNumber = ishEditDAO.deleteLocation(patternInDB[i].getLocationId(), userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									} else { // delete pattern AND location info
										// delete location from database
										if (patternInDB[i].getLocations() != null) {
											updatedPatternNumber = 
												ishEditDAO.deleteLocationByPattern(submissionId, componentId, patternInDB[i].getPattern(), userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
										
										// delete pattern from database
										updatedPatternNumber =
											ishEditDAO.deletePatternById(patternInDB[i].getPatternId(), userName);
										if (updatedPatternNumber == 0) {
//											return false;
											DBHelper.closeJDBCConnection(conn);
											ishEditDAO = null;
											return 7; // failed to update database
										}
									}
								}
							} else { // pattern values in db and on page are not identical
								// delete all pattern and location values and insert new ones
//								System.out.println("pattern values in db and on page are not identical: deleteAllPatternAndLocation####");
								updatedPatternNumber = 
									this.deleteAllPatternAndLocation(submissionId, componentId, ishEditDAO, patternInDB, userName);
								if (updatedPatternNumber == 0) {
//									return false;
									DBHelper.closeJDBCConnection(conn);
									ishEditDAO = null;
									return 7; // failed to update database
								}
								// insert
								updatedPatternNumber = 
									this.insertMultiplePatternAndLocation(ishEditDAO, patternOnPage, submissionId, componentId, userName);
								if (updatedPatternNumber == 0) {
//									return false;
									DBHelper.closeJDBCConnection(conn);
									ishEditDAO = null;
									return 7; // failed to update database
								}
							}
							
						} else { // patternNumberInDB < patternNumberOnPage
//							System.out.println("patternNumberInDB < patternNumberOnPage");
//							System.out.println("patternNumberInDB: " + patternNumberInDB);
//							System.out.println("patternNumberOnPage: " + patternNumberOnPage);
							if (patternValuesAreEqual(patternInDB, patternOnPage, patternNumberInDB)) {
//								System.out.println("patternValuesAreEqual#######");
								for (int i=0;i<patternNumberInDB;i++) {
									// compare pattern and location, update value in database where appropriate & log it
									String ld = patternInDB[i].getLocations();
//									String lp = patternOnPage[i].getLocations();
									String la = patternOnPage[i].getLocationAPart();
									String lpa = (la==null || la.equals("")) ? "" : la;
									String ln = patternOnPage[i].getLocationNPart();
									String lpn = (ln==null || ln.equals("")) ? "" : ln;
									String lp = (lpa + " " + lpn).trim();
									int pid = patternInDB[i].getPatternId();
									int lid = patternInDB[i].getLocationId();
									if (ld != null && !ld.equals("")) { // location in database is not null
										if (lp != null && !lp.equals("")) { // location on page is not null
											if (!lp.equals(ld)) { // location on page is different from the one in DB, update
												updatedPatternNumber = 
													ishEditDAO.updateLocation(pid, lid, lp, userName);
												if (updatedPatternNumber == 0) {
//													return false;
													DBHelper.closeJDBCConnection(conn);
													ishEditDAO = null;
													return 7; // failed to update database
												}
											}
										} else { // location on page is null
											// delete location from database
											updatedPatternNumber =  ishEditDAO.deleteLocation(lid, userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									} else { // location in database is null
										if (lp != null && !lp.equals("")) { // location on page is not null, insert where appropriate
											// add location
											updatedPatternNumber = 
												ishEditDAO.insertLocation(submissionId, componentId, patternInDB[i].getPattern(), lp, userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
										}
									}
								}
								// insert other pattern and/or location value into the database & log it
								int startIndexForAdding = patternNumberInDB;
//								System.out.println("startIndexForAdding: " + startIndexForAdding);
								ArrayList patternList = getAllPatternsInPatternArray(patternInDB);
//								for (int i=0;i<patternList.size();i++)
//									System.out.println("pattern list: " + i + ": " + patternList.get(i).toString());
								ArrayList patternModified = 
									getPatternAndLocationListFromPatternArray(patternInDB);
								for (int i=startIndexForAdding;i<patternNumberOnPage;i++) {
									String pattern = patternOnPage[i].getPattern();
//									String location = patternOnPage[i].getLocations();
									String la = patternOnPage[i].getLocationAPart();
									String locationAPart = (la==null || la.equals("")) ? "" : la;
									String ln = patternOnPage[i].getLocationNPart();
									String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
									String location = (locationAPart + " " + locationNPart).trim();
//									System.out.println("pattern value: " + pattern);
//									System.out.println("location value: " + location);
									int patternId = this.getIdForPattern(patternInDB, patternOnPage[i].getPattern());
									if (patternList.contains(pattern)) { // pattern value already existed
//										System.out.println("pattern value alread existed######");
										// insert location where appropriate
										if (location != null && !location.equals("") && !specifiedLocationForPatternExists(patternModified, pattern, location)) {
											updatedPatternNumber =
												ishEditDAO.insertLocation(submissionId, componentId, pattern, location,
														userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
											String[] patternAndLocation = new String[2];
											patternAndLocation[0] = pattern;
											patternAndLocation[1] = location;
											patternModified.add(patternAndLocation);
										}
										
									} else { // insert pattern AND location info
										// pattern
										updatedPatternNumber =
											ishEditDAO.insertPattern(submissionId, componentId,
													pattern, userName);
										if (updatedPatternNumber == 0) {
//											return false;
											DBHelper.closeJDBCConnection(conn);
											ishEditDAO = null;
											return 7; // failed to update database
										}
										// location
										if (updatedPatternNumber != 0 && location != null) {
											updatedPatternNumber =
												ishEditDAO.insertLocation(submissionId, componentId,
														pattern, location, userName);
											if (updatedPatternNumber == 0) {
//												return false;
												DBHelper.closeJDBCConnection(conn);
												ishEditDAO = null;
												return 7; // failed to update database
											}
											String[] patternAndLocation = new String[2];
											patternAndLocation[0] = pattern;
											patternAndLocation[1] = location;
											patternModified.add(patternAndLocation);
										}
									}
								} // end of for loop
							} else { // patterns on page are totally different from the ones in DB
								// delete all pattern and location values and insert new ones
//								System.out.println("pattern on page are totally different from the ones in db: deleteAllPatternAndLocation####");
								updatedPatternNumber = 
									this.deleteAllPatternAndLocation(submissionId, componentId, ishEditDAO, patternInDB, userName);
								if (updatedPatternNumber == 0) {
//									return false;
									DBHelper.closeJDBCConnection(conn);
									ishEditDAO = null;
									return 7; // failed to update database
								}
								// insert
								updatedPatternNumber = 
									this.insertMultiplePatternAndLocation(ishEditDAO, patternOnPage, submissionId, componentId, userName);
								if (updatedPatternNumber == 0) {
//									return false;
									DBHelper.closeJDBCConnection(conn);
									ishEditDAO = null;
									return 7; // failed to update database
								}
							}
						}
					}
				} // end of pattern updation				
			} else { // expression (primary strength) are not identical
				
				// should judge if the change will bring about confliction for the annotation
				errorCode = ishEditDAO.existConflict(submissionId, stage, componentId, newPrimaryStrength);
				if (errorCode != 0) { // confliction exists, updating denied
					// for time being only deny updating. in the future could return more informative info###########
//					return false;
					DBHelper.closeJDBCConnection(conn);
					ishEditDAO = null;
					return errorCode;
				} else { // there's no confliction

					// update primary strength
					updatedStrengthRowNumber =
						ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, newPrimaryStrength, userName);
					if (updatedStrengthRowNumber == 0) {
//						return false;
						DBHelper.closeJDBCConnection(conn);
						ishEditDAO = null;
						return 7; // failed to update database
					}
					
					// delete secondary strength
					updatedStrengthRowNumber =
						ishEditDAO.updateExpressionStrengh(submissionId, componentId, 1, "", userName);
					if (updatedStrengthRowNumber == 0) {
//						return false;
						DBHelper.closeJDBCConnection(conn);
						ishEditDAO = null;
						return 7; // failed to update database
					}
					
					// delete pattern if there's any in DB
//					System.out.println("strength on page are not present: deleteAllPatternAndLocation####");
//					if (patternInDB != null) {
					if (!patternInDBIsNull) {
						updatedPatternNumber = 
							this.deleteAllPatternAndLocation(submissionId, componentId, ishEditDAO, patternInDB, userName);
						if (updatedPatternNumber == 0) {
//							return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
					}
				}
			}
		} else { // not detected in DB
			if (newPrimaryStrength.equals("present") || newPrimaryStrength.equals("uncertain")) { // present/uncertain on page
				
				// should judge if the change will bring about confliction for the annotation
				errorCode = ishEditDAO.existConflict(submissionId, stage, componentId, newPrimaryStrength);
				if (errorCode != 0) { // confliction exists, updating denied
					// for time being only deny updating. in the future could return more informative info###########
//					return false;
					DBHelper.closeJDBCConnection(conn);
					ishEditDAO = null;
					return errorCode;
				} else { // there's no confliction
					// update primary strength
					updatedStrengthRowNumber =
						ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, newPrimaryStrength, userName);
					if (updatedStrengthRowNumber == 0) {
//						return false;
						DBHelper.closeJDBCConnection(conn);
						ishEditDAO = null;
						return 7; // failed to update database
					}
					
					// set secondary strength if there's one
					if (newSecondaryStrength != null && !newSecondaryStrength.equals("")) {
						updatedStrengthRowNumber =
							ishEditDAO.updateExpressionStrengh(submissionId, componentId, 1, newSecondaryStrength, userName);
						if (updatedStrengthRowNumber == 0) {
//							return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
					}
						
					// add pattern info if there are any
//					if (patternOnPage != null) {
					if (!patternOnPageIsNull) {
						// record by record, insert pattern and/or location value into the database & log it
						updatedPatternNumber = 
							this.insertMultiplePatternAndLocation(ishEditDAO, patternOnPage, submissionId, componentId, userName);
						if (updatedPatternNumber == 0) {
//							return false;
							DBHelper.closeJDBCConnection(conn);
							ishEditDAO = null;
							return 7; // failed to update database
						}
					}
				}
			} else if(!newPrimaryStrength.equals(oldPrimaryStrength)) {
				// update primary strength
				updatedStrengthRowNumber =
					ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, newPrimaryStrength, userName);
				if (updatedStrengthRowNumber == 0) {
//					return false;
					DBHelper.closeJDBCConnection(conn);
					ishEditDAO = null;
					return 7; // failed to update database
				}
			}
		}
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		return 0;
	} // end of updateAnnotation
	
	/**
	 * check if pattern values in pattern1 and pattern2 are identical. return false if not
	 * @param pattern1
	 * @param pattern2
	 * @param comparisonNumber
	 * @return
	 */
	private boolean patternValuesAreEqual(ExpressionPattern[] pattern1, ExpressionPattern[] pattern2, int comparisonNumber) {
		for (int i=0;i<comparisonNumber;i++) {
//			System.out.println("pattern1: "  + pattern1[i].getPattern());
//			System.out.println("pattern2: "  + pattern2[i].getPattern());
//			System.out.println("pattern1:location: "  + pattern1[i].getLocations());
//			System.out.println("pattern2:location: "  + pattern2[i].getLocations());
			if (!pattern1[i].getPattern().equals(pattern2[i].getPattern())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * insert specified pattern info into database
	 * @param ishEditDAO
	 * @param patterns
	 * @param submissionId
	 * @param componentId
	 * <p>modified by xingjun - 21/10/2008 
	 * - modified code to assemble location string: when involed into the '+' operation, 
	 *   null string will be allocated a value of 'null' </p>
	 * @return
	 */
	private int insertMultiplePatternAndLocation(ISHEditDAO ishEditDAO, ExpressionPattern[] patterns, 
			String submissionId, String componentId, String userName) {
		// record by record, insert pattern and/or location value into the database & log it
		ArrayList<String> patternList = new ArrayList<String>();
		patternList.add(patterns[0].getPattern());
		ArrayList<String> insertedLocationList = new ArrayList<String>();
		
		// pattern
		int insertPatternNumber =
			ishEditDAO.insertPattern(submissionId, componentId, patterns[0].getPattern(), userName);
		// location
		if (insertPatternNumber != 0) {
//			String location = patterns[0].getLocations();
			////////////////////////////
			// modified by xingjun - 20/06/2008 - assemble location string
			// for 'adjacent to' location, locationAPart is 'adjacent to', 
			//                             locationNPart should be numerical data (e.g. 1234) 
			//                             location is the concatenate of la and ln (e.g. adjacent to 1234)
			// for non-'adjacent to' location, locationAPart is not null (e.g. rostral)
			//                                 locationNPart is null
			//                                 location is the concatenate of la and ln (e.g. rostral)
			String la = patterns[0].getLocationAPart();
			String locationAPart = (la==null || la.equals("")) ? "" : la;
			String ln = patterns[0].getLocationNPart();
			String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
			String location = (locationAPart + " " + locationNPart).trim();
			///////////////////////////////
			if (location != null && !location.equals("")) {
				insertPatternNumber = ishEditDAO.insertLocation(submissionId, componentId, 
						patterns[0].getPattern(), location, userName);
				// keep record of insertion of location of specified pattern
				if (insertPatternNumber > 0) insertedLocationList.add(location);
			}
		}
		int len = patterns.length;
		for (int i=1;i<len;i++) {
			if (patternList.contains(patterns[i].getPattern())) { // one pattern presents in more than one locations
//				System.out.println("pattern duplicated#####");
				// insert every location string for the specified pattern if there's one
				////////////////////////////
				// modified by xingjun - 20/06/2008 - assemble location string
//				String location = patterns[i].getLocations();
				String la = patterns[i].getLocationAPart();
				String locationAPart = (la==null || la.equals("")) ? "" : la;
				String ln = patterns[i].getLocationNPart();
				String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
				String location = (locationAPart + " " + locationNPart).trim();
				////////////////////////////
				if (location != null && !location.equals("") && !insertedLocationList.contains(location)) {
					/** due to the database redundancy problem this method need to be implemented in some strange way */
					insertPatternNumber = ishEditDAO.insertLocation(submissionId, componentId,
							patterns[i].getPattern(), location, userName);
					// keep record of insertion of location of specified pattern
					if (insertPatternNumber > 0) insertedLocationList.add(location);
				}
			} else { // different pattern
//				System.out.println("different pattern#####");
				// pattern
				String pattern = patterns[i].getPattern();
				insertPatternNumber =
					ishEditDAO.insertPattern(submissionId, componentId, pattern, userName);
				// location
				if (insertPatternNumber != 0) {
					//////////////////////////
					// modified by xingjun - 20/06/2008 - assemble location string
//					String location = patterns[i].getLocations();
					String la = patterns[i].getLocationAPart();
					String locationAPart = (la==null || la.equals("")) ? "" : la;
					String ln = patterns[i].getLocationNPart();
					String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
					String location = (locationAPart + " " + locationNPart).trim();
					//////////////////////////
					if (location != null && !location.equals("")) {
						insertPatternNumber =
							ishEditDAO.insertLocation(submissionId, componentId, pattern, location, userName);
						if (insertPatternNumber > 0) { // keep record of insertion of location of specified pattern
							insertedLocationList = new ArrayList<String>();
							insertedLocationList.add(location);
						}
					}
				}
				patternList.add(patterns[i].getPattern());
			}
		}
		
		return insertPatternNumber;
	} // end of insertMultiplePatternAndLocation
	
	// delete pattern and location in database & log it
	private int deleteAllPatternAndLocation(String submissionId, String componentId, 
			ISHEditDAO ishEditDAO, ExpressionPattern[] patterns, String userName) {

		if (patterns == null) {
			return 0;
		}
		
		// get pattern value list
		int len = patterns.length;
		ArrayList<String> patternList = new ArrayList<String>();
		patternList.add(patterns[0].getPattern());
		for (int i=1;i<len;i++) {
			if (!patternList.contains(patterns[i].getPattern())) {
				patternList.add(patterns[i].getPattern());
			}
		}
		String[] patternArray = patternList.toArray(new String[patternList.size()]);
		len = patternArray.length;
		
		// one by one, delete all pattern info
		int deleteRecordNumber = 0;
		for (int i=0;i<len;i++) {
			// delete location
			deleteRecordNumber = 
				ishEditDAO.deleteLocationByPattern(submissionId, componentId, patternArray[i], userName);
			// delete pattern
			deleteRecordNumber = 
				ishEditDAO.deletePatternByIds(submissionId, componentId, patternArray[i], userName);
		}
		return deleteRecordNumber;
	}
	
	/**
	 * 
	 * @param patternArray
	 * @param pattern
	 * @return
	 */
	private int getIdForPattern(ExpressionPattern[] patternArray, String pattern) {
		
		int len = patternArray.length;
		for (int i=0;i<len;i++) {
			if (pattern.equals(patternArray[i].getPattern())) {
				return patternArray[i].getPatternId();
			}
		}
		return -1;
	}
	
	private boolean specifiedLocationForPatternExists(ExpressionPattern[] patternArray, String pattern, String location) {
		int len = patternArray.length;
		for (int i=0;i<len;i++) {
			if (pattern.equals(patternArray[i].getPattern())) {
				String locationInArray = patternArray[i].getLocations();
				if (locationInArray != null
						&& !locationInArray.equals("")
						&& locationInArray.equals(location)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean specifiedLocationForPatternExists(ArrayList patternList, String pattern, String location) {
		
		int len = patternList.size();
		for (int i=0;i<len;i++) {
			String[] patternAndLocation = new String[2];
			patternAndLocation[0] = pattern;
			patternAndLocation[1] = location;
			if (patternList.contains(patternAndLocation))
				return true;
		}
		return false;
	}
	
	private ArrayList getPatternAndLocationListFromPatternArray(ExpressionPattern[] patternArray) {
		
		ArrayList<String[]> patternAndLocationList = new ArrayList<String[]>();
		String[] patternAndLocation;
		int len = patternArray.length;
		for (int i=0;i<len;i++) {
			patternAndLocation = new String[2];
			patternAndLocation[0] = patternArray[i].getPattern();
			patternAndLocation[1] = patternArray[i].getLocations();
			patternAndLocationList.add(patternAndLocation);
		}
		return patternAndLocationList;
	}
	
	private ArrayList getAllPatternsInPatternArray(ExpressionPattern[] patternArray) {
		
		int len = patternArray.length;
		ArrayList<String> patternList = new ArrayList<String>();
		for (int i=0;i<len;i++) {
			if (!patternList.contains(patternArray[i].getPattern())) {
				patternList.add(patternArray[i].getPattern());
			}
		}
		return patternList;
	}
	
	/**
	 * 
	 * @param submissionId
	 * @param componentId
	 * @param note
	 * @return
	 */
	public boolean addNote(String submissionId, String componentId, String note, String userName) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** add & log */
		int addedRowNumber = 
			ishEditDAO.addExpressionNote(submissionId, componentId, note, 1, userName);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (addedRowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * @param submissionId
	 * @param componentId
	 * @return
	 */
	public boolean deleteNote(String submissionId, String componentId, String userName) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** delete & log */
		int deletedRowNumber = ishEditDAO.deleteExpressionNotes(submissionId, componentId, userName);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (deletedRowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * @param submissionId
	 * @param componentId
	 * @param notesOnpage
	 * @param notesInDB
	 * @return
	 */
	public boolean updateNote(String submissionId, String componentId, String notesOnpage, String userName) {
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** update & log */
		int updatedRowNumber = ishEditDAO.updateExpressionNotes(submissionId, componentId, notesOnpage, userName);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (updatedRowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean signOffAnnotation(String submissionId, String status) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** update & log */
		boolean result = ishEditDAO.signOffAnnotation(submissionId, status);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		return result;
	}
	
	public boolean signOffOrEditAnnotation(String submissionId, String oldStatus, String newStatus) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** update & log */
		boolean result = ishEditDAO.signOffAnnotation(submissionId, oldStatus, newStatus);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		return result;
	}
	
	public boolean setPublicSubmission(String submissionId, String status) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** update & log */
		boolean result = ishEditDAO.setPublicSubmission(submissionId, status);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		return result;
	}
	
	public boolean signOffAnnotationAndSetPublicSubmission(String submissionId, String dbstatus, String substatus) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** update & log */
		boolean result = ishEditDAO.signOffAnnotationAndSetPublicSubmission(submissionId, dbstatus, substatus);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		return result;
	}
	
	/**
	 * @author ying
	 * @param submissionId
	 * @return
	 */
	public ArrayList getPIBySubID(String submissionId) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** update & log */
		ArrayList result = ishEditDAO.getPIBySubID(submissionId);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		return result;
	}
	
	/**
	 * @author xingjun - 13/06/2008
	 * @return
	 */
	public ArrayList getPatternList() {
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get data
		ArrayList patternList = ishDAO.getPatternList();
		
		// return
		return patternList;
	}
	
	/**
	 * @author xingjun - 13/06/2008
	 * @return
	 */
	public ArrayList getLocationList() {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get data
		ArrayList locationList = ishDAO.getLocationList();
		
		// return
		return locationList;
	}
	
	/**
	 * @author xingjun - 19/06/2008
	 * @param stage
	 * @return
	 */
	public ArrayList getComponentListAtTheStage(String stage) {
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get data
		ArrayList componentListAtTheStage = ishDAO.getComponentListAtGivenStage(stage);
		
		// return
		return componentListAtTheStage;
	}
		
}
