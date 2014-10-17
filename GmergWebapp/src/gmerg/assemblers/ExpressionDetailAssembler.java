/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;

/**
 * @author xingjun
 *
 */
public class ExpressionDetailAssembler {
    private boolean debug = false;
    public ExpressionDetailAssembler() {
	if (debug)
	    System.out.println("ExpressionDetailAssembler.constructor");
    }

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
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			// get expressionDetail
			// if expression is null
			//    get stage
			//    get component details
			//    get parent node
			//    if has parent node: inferred not detected
			//    else get children
			//      if child node: inferred present
			//      else: not examined
			// else
			//    get notes
			//    assemble and return
			ExpressionDetail expressionDetail =
				ishDAO.findExpressionDetailBySubmissionIdAndComponentId(submissionAccessionId, componentId);
			if (expressionDetail != null) {
				String expressionNotes = ishDAO.findAnnotationNote(submissionAccessionId, componentId);
				String densityNotes = ishDAO.findDensityNote(submissionAccessionId, componentId);
				
				ExpressionPattern [] patterns = 
					ishDAO.findPatternsAndLocations(String.valueOf(expressionDetail.getExpressionId()));
				expressionDetail.setPattern(patterns);
	                        
				// fill out the expression detail object
				expressionDetail.setSubmissionId(submissionAccessionId);
				expressionDetail.setExpressionNote(expressionNotes);
				expressionDetail.setDensityNote(densityNotes);
				
				ArrayList<String> densityDetail = ishDAO.findDensityDetail(submissionAccessionId, componentId);
				expressionDetail.setDensityRelativeToTotal(densityDetail.get(0));
				expressionDetail.setDensityDirectionalChange(densityDetail.get(1));
				expressionDetail.setDensityMagnitudeChange(densityDetail.get(2));

				
			} else {
	                
				String stage = ishDAO.findStageBySubmissionId(submissionAccessionId);
				ArrayList componentDetail = ishDAO.findComponentDetailById(componentId);
				expressionDetail = new ExpressionDetail();
				expressionDetail.setSubmissionId(submissionAccessionId);
				expressionDetail.setStage(stage);
				expressionDetail.setComponentId(((String)componentDetail.get(0)));
				expressionDetail.setComponentName(((String)componentDetail.get(1)));
				expressionDetail.setComponentDescription(((ArrayList)componentDetail.get(2)));
				
	//			expressionDetail.setPattern("not applicable");
				ExpressionPattern[] patterns = new ExpressionPattern[1];
				ExpressionPattern pattern = new ExpressionPattern();
				pattern.setPattern("not applicable");
				patterns[0] = pattern;
				expressionDetail.setPattern(patterns);
				
				boolean hasParent = ishDAO.hasParentNode(componentId, stage, submissionAccessionId);
				if (hasParent) {
					// inferred not detected
					expressionDetail.setPrimaryStrength("inferred not detected");
					
				} else {
					boolean hasChild = ishDAO.hasChildenNode(componentId, stage, submissionAccessionId);
					if (hasChild) {
						// inferred present
						expressionDetail.setPrimaryStrength("inferred present");
						
					} else {
						// not examined
						expressionDetail.setPrimaryStrength("not examined");
					}
				}
			}
			return expressionDetail;
		}
		catch(Exception e){
			System.out.println("ExpressionDetailAssembler::getData !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
}
