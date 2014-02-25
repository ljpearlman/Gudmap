package gmerg.assemblers;

import gmerg.db.AnnotationTestDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.ISHEditDAO;
import gmerg.db.ISHDAO;
import gmerg.entities.User;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ExpressionDetail;

import java.sql.Connection;
import java.util.ArrayList;

public class AnnotationTestAssembler {
    private boolean debug = false;

    public AnnotationTestAssembler() {
	if (debug)
	    System.out.println("AnnotationTestAssembler.constructor");
    }

	public ArrayList<String> getAnatomyTree() {
		return null;
	}
	
	/**
	 * <p>xingjun - 14/05/2010 - use different method to add dpc stage value into the labels</p>
	 * @return
	 */
	public ArrayList getStagesInPerspective() {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ArrayList stageRanges = testDAO.getStageRanges();
			return stageRanges;
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::getStagesInPerspective failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public ArrayList<String> getTreeContent(String stage, String id) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ArrayList <String> treeContent = testDAO.getTreeContent(stage, id);
			return treeContent;
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::getTreeContent failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public String getStageFromSubmission(String id) {

		// create a dao
		Connection conn = DBHelper.getDBConnection();

		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			String stage = testDAO.getStageFromSubmission(id);
			return stage;
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::getStageFromSubmission failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public Submission getSubmissionSummary(String submissionId) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO annotationTestDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			Submission submission = annotationTestDAO.getSubmissionSummary(submissionId);
			return submission;
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::getSubmissionSummary failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun - 15/07/2008
	 * return 0: failed
	 * 1: success
	 * 2: no need to change
	 * 3: no annotation to delete
	 */
	public int changeStage(String stage, String submissionId, User user) {
		
		// create daos
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			ISHEditDAO editDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
			// get stage value in db
			String stageInDB = ishDAO.findStageBySubmissionId(submissionId);
			
			/** compare */
			if (stageInDB.equals(stage)) {
				return 2;
			} else {
				String userName = user.getUserName();
				// change stage;
				int updatedRecordNumber = testDAO.updateStage(submissionId, stage, userName);
				if (updatedRecordNumber == 0) {
					return 0;
				}
				
				// get corresponding expression in db (ed)
				// - include component id, expression, strength, pattern, and note
				ArrayList<String[]> expressionInDB = testDAO.getComponentAndExpression(submissionId);
				if (expressionInDB == null) {
					return 3;
				} else {
					int len = expressionInDB.size();
					for (int i=0;i<len;i++) {
						// if note in db is not null, delete it
						String componentId = ((String[])expressionInDB.get(i))[0];
						String expressionNote = ((String[])expressionInDB.get(i))[4];
						if (expressionNote != null && !expressionNote.equals("")) {
	//						System.out.println("start to delete expression note@changeStage!");
							if (editDAO.deleteExpressionNotes(submissionId, componentId, userName)== 0) {
								return 0;
							}
						}
						// if pattern/location exists, delete it
						String pattern = ((String[])expressionInDB.get(i))[3];
						if (pattern != null && !pattern.equals("")) {
	//						System.out.println("start to delete location@changeStage!");
							testDAO.deleteLocation(submissionId, componentId, userName);
	//						System.out.println("start to delete pattern@changeStage!");
							if (testDAO.deletePattern(submissionId, componentId, userName) == 0) {
								return 0;
							}
						}
						// delete expression;
	//					System.out.println("start to delete expression@changeStage!");
						if (editDAO.deleteExpression(submissionId, componentId, userName) == 0) {
							return 0;
						}
					} // end of for loop
				} // end of expression is not null 
			}
			
			return 1; // success
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::changeStage failed !!!");
			return 0; // failed
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun - 29/09/2008
	 * @param compIds
	 * @param submissionId
	 * @param pattern
	 * @param user
	 * @return 1: success
	 *         0: fail
	 *         2: expression value is 'not examined' or 'not detected'
	 */
	public int updatePatterns(String[] compIds, String submissionId, 
			String pattern, String stage, User user) {
		// validate component ids
		if (compIds == null) {
			return 0;
		}
		int cLen = compIds.length;
		if (cLen < 1) {
			return 0;
		}
		for (int i=0;i<cLen;i++) {
			if (compIds[i] == null || compIds[i].equals("")) {
				return 0;
			}
		}
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			// declare a counter to count conflict or data updating error
			int errorCounter = 0;
			String userName = user.getUserName();
			AnnotationTestDAO annotationTestDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
			// go through all the components, if the expression value is present, update pattern
			// if the expression value is not present, flag pluses one
			for (int i=0;i<cLen;i++) {
				// get expression value for the component
				ArrayList expressionAndPattern = 
					annotationTestDAO.getExpressionAndPattern(submissionId, compIds[i]);
				String expressionInDB = "";
				if (expressionAndPattern == null || expressionAndPattern.size() == 0) {// not examined, conflict exists
					errorCounter ++;
				} else { // not detected or present
					expressionInDB = ((String[])expressionAndPattern.get(0))[1];
					if (expressionInDB.equals("present")) { // expressed
						// check if pattern exists
						int eapLen = expressionAndPattern.size();
						boolean patternExists = false;
						for (int j=0;j<eapLen;j++) {
							String patternInDB = ((String[])expressionAndPattern.get(j))[2];
							if (patternInDB != null && patternInDB.equals(pattern)) {
								patternExists = true;
								break;
							}
						}
						// insert pattern value if pattern not exists
						if (!patternExists) {
							int updatedPatternNumber = 
								ishEditDAO.insertPattern(submissionId, compIds[i], pattern, userName);
							if (updatedPatternNumber == 0) { // updating failed, return 0
								return 0;
							}
						}
					} else { // not detected, conflict exists
						errorCounter ++;
					}
				}
			} // end of for loop
			
			// if the counter > 0 return 2
			// else return 1;
			// during the modification, if db connection failed, return 0
			if (errorCounter > 0) {
				return 2;
			} else {
				return 1;
			}
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::updatePatterns failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun - 14/07/2008
	 * update expression for a set of component. abort if confliction exists
	 * modified on 17/07/2008
	 * - confliction checking should be made on the compIds instead of componentsInCommon
	 * @param compIds
	 * @param submissionId
	 * @param expression
	 * @param user
	 * @return 1: success
	 *         0: fail
	 *         2: confliction exists
	 */
	public int updateExpression(String[] compIds, String submissionId, 
			String expression, String stage, User user) {
		// validate component ids
		if (compIds == null) {
			return 0;
		}
		int cLen = compIds.length;
		if (cLen < 1) {
			return 0;
		}
		for (int i=0;i<cLen;i++) {
			if (compIds[i] == null || compIds[i].equals("")) {
				return 0;
			}
		}
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnnotationTestDAO testDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
			ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);

			/** reorganise expression data on page and in db */
			String expressionOnPage = null;
			String strengthOnPage = "";
			if (expression.equals("nd")) {
				expressionOnPage = "not detected";
			} else if (expression.equals("ne")) {
				expressionOnPage  = "not examined";
			} else if (expression.equals("u")) {
				expressionOnPage  = "uncertain";
			} else {
				expressionOnPage = "present";
				strengthOnPage = this.getStrengthFromExpression(expression);
			}
	//		System.out.println("expressionOnPage: " + expressionOnPage);
	//		System.out.println("strengthOnPage: " + strengthOnPage);
			// list of components on page (cp)
			ArrayList<String> componentsOnPage = new ArrayList<String>();
			for (int i=0;i<cLen;i++) {
				componentsOnPage.add(compIds[i]);
			}
			// corresponding expression in db (ed)
			// - include component id, expression, strength, pattern, and note
			ArrayList<String[]> expressionInDB = testDAO.getComponentAndExpression(submissionId);
			String userName = user.getUserName();
	//		System.out.println("HERE:"+submissionId+compIds.length+expressionOnPage+strengthOnPage+userName);
			/** start */
			if (expressionInDB != null && expressionInDB.size() > 0) {
				// get components in common
				ArrayList componentsInCommon = null;
				int numberOfComponentsInCommon = 0;
				componentsInCommon =
					this.getComponentsInCommon(componentsOnPage, expressionInDB);
				if (componentsInCommon != null && componentsInCommon.size() > 0) {
					numberOfComponentsInCommon = componentsInCommon.size();
				}
	//			System.out.println("numberOfComponentsInCommon: " + numberOfComponentsInCommon);
				
				// modify
				if (expressionOnPage.equals("not examined")) { // no expression info on page
					for (int i=0;i<numberOfComponentsInCommon;i++) {
						// if edn not null, delete note
						String componentId = ((String[])componentsInCommon.get(i))[0];
						String expressionNote = ((String[])componentsInCommon.get(i))[4];
						if (expressionNote != null && !expressionNote.equals("")) {
							// delete note
	//						System.out.println("start to delete expression note@not examined!");
							if (ishEditDAO.deleteExpressionNotes(submissionId, componentId, userName)== 0) {
								return 0;
							}
						}
						// if ed == present & have pattern, delete pattern;
						String pattern = ((String[])componentsInCommon.get(i))[3];
						if (pattern != null && !pattern.equals("")) {
							// delete pattern (location)
	//						System.out.println("start to delete location@not examined!");
							testDAO.deleteLocation(submissionId, componentId, userName);
	//						System.out.println("start to delete pattern@not examined!");
							if (testDAO.deletePattern(submissionId, componentId, userName) == 0) {
								return 0;
							}
						}
						// delete expression;
	//					System.out.println("start to delete expression@not examined!");
						if (ishEditDAO.deleteExpression(submissionId, componentId, userName) == 0) {
							return 0;
						}
					}
				} else { // has expression info on page
					/** check confliction */
					// get all components in db with contrary expression to those on page
					ArrayList componentsWithOpposedExpressionInDB =
						this.getComponentsWithOpposedExpression(expressionInDB, expressionOnPage);
					// remove components whose expression would be changed
					ArrayList<String> componentsWithOpposedExpression = new ArrayList<String>();
					for (int i=0;i<componentsWithOpposedExpressionInDB.size();i++) {
						String component = (String)componentsWithOpposedExpressionInDB.get(i);
						if (!componentsOnPage.contains(component)) {
							componentsWithOpposedExpression.add(component);
						}
					}
	//				for (int i=0;i<componentsWithOpposedExpressionInDB.size();i++) {
	//					System.out.println("component after: " + componentsWithOpposedExpressionInDB.get(i));
	//				}
					// get would-be component list
	//				ArrayList newComponentList =
	//					this.getUnionOfStringArrayLists(componentsOnPage, componentsInDB);
					// check conflict
					for (int i=0;i<cLen;i++) {
						int conflictCode =
							testDAO.checkConflict(compIds[i], componentsWithOpposedExpression, stage, expressionOnPage);
	//					System.out.println(i + compIds[i] + ":conflictCode: " + conflictCode);
						if (conflictCode != 0) {
							return 2;
						}
					}
					if (expressionOnPage.equals("present")) {
						// if strength different, update
						for (int i=0;i<numberOfComponentsInCommon;i++) {
							String componentId = ((String[])componentsInCommon.get(i))[0];
							String ed = ((String[])componentsInCommon.get(i))[1]; // expression in db
							String strengthInDB = ((String[])componentsInCommon.get(i))[2];
							if (ed.equals("present")) {
								if (!strengthOnPage.equals(strengthInDB)) {
									// update strength
	//								System.out.println("start to update strength@p on page!");
									if (ishEditDAO.updateExpressionStrengh(submissionId, componentId, 1, strengthOnPage, userName) == 0) {
										return 0;
									}
								}
							} else { // 'not detected' or 'uncertain' in db
								// update expression
	//							System.out.println("start to update expression@ne/p on page and nd/u in db");
								if (ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, expressionOnPage, userName) == 0) {
									return 0;
								}
								// update strength
	//							System.out.println("start to update strength@ne/u on page and p in db");
								if (ishEditDAO.updateExpressionStrengh(submissionId, componentId, 1, strengthOnPage, userName) == 0) {
									return 0;
								}
							}
						}
						
					} else if (expressionOnPage.equals("not detected")
							|| expressionOnPage.equals("uncertain")) {
						// if ed == 'present'
						//                   if edn not null, delete note
						//                   if have pattern, delete pattern
						//                   update expression
						for (int i=0;i<numberOfComponentsInCommon;i++) {
							String ed = ((String[])componentsInCommon.get(i))[1]; // expression in db
							String componentId = ((String[])componentsInCommon.get(i))[0];
							if (ed.equals("present")) {
								String strengthInDB = ((String[])componentsInCommon.get(i))[2];
	//							String expressionNote = ((String[])componentsInCommon.get(i))[4];
	//							if (expressionNote != null && !expressionNote.equals("")) {
	//								// delete note
	////								System.out.println("start to delete expression note@ne/u on page and p in db");
	//								if (ishEditDAO.deleteExpressionNotes(submissionId, componentId, userName)== 0) {
	//									return 0;
	//								}
	//							}
								String pattern = ((String[])componentsInCommon.get(i))[3];
								if (pattern != null && !pattern.equals("")) {
									// delete pattern (location)
	//								System.out.println("start to delete location@ne/u on page and p in db");
									testDAO.deleteLocation(submissionId, componentId, userName);
									if (testDAO.deletePattern(submissionId, componentId, userName) == 0) {
										return 0;
									}
								}
								// update expression
	//							System.out.println("start to update expression@ne/u on page and p in db");
								if (ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, expressionOnPage, userName) == 0) {
									return 0;
								}
								// update strength - set to null
	//							System.out.println("start to update strength@ne/u on page and p in db");
								if (strengthInDB != null && !strengthInDB.equals("")) {
									if (ishEditDAO.updateExpressionStrengh(submissionId, componentId, 1, strengthOnPage, userName) == 0) {
										return 0;
									}
								}
							} else { // expression on page is 'not detected' or 'uncertain'
								// update expression
	//							System.out.println("start to update expression@ne/u on page and ne/u in db");
								if (ishEditDAO.updateExpressionStrengh(submissionId, componentId, 0, expressionOnPage, userName) == 0) {
									return 0;
								}
							}
						}
					}
					// remove components in common
					for (int i=0;i<numberOfComponentsInCommon;i++) {
						String componentId = ((String[])componentsInCommon.get(i))[0];
						if (componentsOnPage.contains(componentId)) {
							componentsOnPage.remove(componentId);
						}
					}
					// insert residue components and expression into the database //
					// for residue components (could be the same as original cp if expressionInDB is null)
					int len = componentsOnPage.size(); // re-calculate the length of the component list
					for (int i=0;i<len;i++) {
						String componentId = componentsOnPage.get(i);
						// insert expression
	//					System.out.println("start to insert expression@residue!");
						if (ishEditDAO.insertExpression(submissionId, componentId, expressionOnPage, strengthOnPage, userName) == 0) {
							return 0;
						}
					}
				} // end of has expression info
			} else { // expression in db is null
				// when insert expression, it impossible causes the confliction 
				// insert expression
				
				for (int i=0;i<cLen;i++) {
					if (ishEditDAO.insertExpression(submissionId, compIds[i], expressionOnPage, strengthOnPage, userName) == 0) {
						return 0;
					}
				}
			}
			/** success */
			return 1;
		}
		catch(Exception e){
			System.out.println("AnnotationTestAssembler::updateExpression failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	} // end of updateExpression
	
	/**
	 * @author xingjun - 14/07/2008
	 * translate abbreviation of expression value into its full name
	 * p, s, m, w, u and nd
	 */
	private String getStrengthFromExpression(String expression) {
		
		if (expression.equals("s")) {
			return "strong";
		} else if (expression.equals("m")) {
			return "moderate";
		} else if (expression.equals("w")) {
			return "weak";
		}
		return "";
	}
	
	/**
	 * @author xingjun - 14/07/2008
	 * go through the comp list and expr list, find compId in common 
	 * and initilise a new list of compId and corresponding expression
	 * @param componentList
	 * @param expressionList
	 * @return
	 */
	private ArrayList getComponentsInCommon(ArrayList componentList, ArrayList expressionList) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		int len = expressionList.size();
		for (int i=0;i<len;i++) {
			String compId = ((String[])expressionList.get(i))[0];
//			System.out.println("component: " + compId);
			if (componentList.contains(compId)) {
				result.add(((String[])expressionList.get(i)));
			}
		}
		return result;
	}
	
	/**
	 * @author xingjun - 21/07/2008
	 * get union of two string arraylist
	 * 
	 * @param componentsOnPage
	 * @param componentsInDB
	 * @return
	 */
//	private ArrayList getUnionOfStringArrayLists(ArrayList componentsOnPage, ArrayList componentsInDB) {
//		ArrayList<String> result = new ArrayList<String>();
//		if (componentsOnPage.size() == 0 && componentsInDB.size() == 0) {
//			result.add("");
//		} else {
//			for (int i=0;i<componentsOnPage.size();i++) {
//				result.add((String)componentsOnPage.get(i));
//			}
//			for (int i=0;i<componentsInDB.size();i++) {
//				String component = (String)componentsInDB.get(i);
//				if (!result.contains(component)) {
//					result.add(component);
//				}
//			}
//		}
//		return result;
//	}
	
	private ArrayList getComponentsWithOpposedExpression(ArrayList expressions, String expression) {
		ArrayList<String> result =  new ArrayList<String>();
		int size = expressions.size();
//		System.out.println("expression on page: " + expression);
		for (int i=0;i<size;i++) {
			String componentId = ((String[])expressions.get(i))[0];
			String exp = ((String[])expressions.get(i))[1];
//			System.out.println(componentId + ": expression in db: " +  exp);
			if (exp.equals("not detected") && (expression.equals("present") || expression.equals("uncertain"))) {
				result.add(componentId);
			} else if ((exp.equals("uncertain") || exp.equals("present")) && expression.equals("not detected")) {
				result.add(componentId);
			}
		}
//		for (int i=0;i<result.size();i++)
//			System.out.println("component initial: " +  result.get(i));
		return result;
	}
	
}
