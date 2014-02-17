/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.AnatomyDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
//import gmerg.utils.Helper;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class AnatomyStructureAssembler {
    private boolean debug = false;

    public AnatomyStructureAssembler() {
	if (debug)
	    System.out.println("AnatomyStructureAssembler.constructor");
    }

	/**
	 * 
	 * @return
	 */
	public ArrayList getStageRanges() {
	if (debug)
	    System.out.println("AnatomyStructureAssembler.getStageRanges");
		
		ArrayList stageRanges = null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO;
		try{
			anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			stageRanges = anatomyDAO.getTheilerStageRanges();
			
			/** ---return the composite value object---  */
			return stageRanges;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getStageRanges failed !!!");
			stageRanges = null;
			
			/** ---return the composite value object---  */
			return stageRanges;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			anatomyDAO = null;
		}
	}
	
	public String getComponentNameFromId(String id){
	if (debug)
	    System.out.println("AnatomyStructureAssembler.getComponentNameFromId");
		
		String componentName = null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO;
		try{
			anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			componentName = anatomyDAO.getAnatomyTermFromPublicId(id);
			/** ---return the composite value object---  */
			return componentName;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getComponentNameFromId failed !!!");
			componentName = null;				
			/** ---return the composite value object---  */
			return componentName;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			anatomyDAO = null;
		}
	}
	
	public String getOntologyTerms() {
	if (debug)
	    System.out.println("AnatomyStructureAssembler.getOntologyTerms");
		
		String ontologyTerms = null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO;
		try{
			anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			ontologyTerms = anatomyDAO.getOnlogyTerms();
			/** ---return the composite value object---  */
			return ontologyTerms;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getOntologyTerms failed !!!");
			ontologyTerms = null;				
			/** ---return the composite value object---  */
			return ontologyTerms;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			anatomyDAO = null;
		}
	}
	
	/**
	 * 
	 * @param startStage
	 * @param endStage
	 * @return
	 */
	public boolean stageRangesAreValid(String startStage, String endStage) {
	if (debug)
	    System.out.println("AnatomyStructureAssembler.stageRangesAreValid");

        if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
        	return false;
        }
        
        boolean isValid = true;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO;
		try{
			anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			int startStageSequence = anatomyDAO.findSequencebyStageName(startStage);
			int endStageSequence = anatomyDAO.findSequencebyStageName(endStage);
				
			// make the judgement
			if (startStageSequence == -1 || endStageSequence == -1 || startStageSequence > endStageSequence) {
				isValid = false;
			}
			/** ---return the composite value object---  */
			return isValid;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::stageRangesAreValid failed !!!");
			isValid = false;				
			/** ---return the composite value object---  */
			return isValid;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			anatomyDAO = null;
		}
	}
	
	/**
	 * 
	 * @param startStage
	 * @param endStage
	 * @return
	 */
	public ArrayList buildTree(String startStage, String endStage, boolean isForBooleanQ) {
	if (debug)
	    System.out.println("AnatomyStructureAssembler.buildTree");

        if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
        	return null;
        }
        
        ArrayList anatomyTree = null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO;
		try{
			anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			anatomyTree = anatomyDAO.getAnatomyTreeByStages(startStage, endStage, isForBooleanQ);
			/** ---return the composite value object---  */
			return anatomyTree;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::buildTree failed !!!");
			anatomyTree = null;				
			/** ---return the composite value object---  */
			return anatomyTree;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			anatomyDAO = null;
		}
	}
	
	/**
	 * 
	 * @param components
	 * @param startStage
	 * @param endStage
	 * @param expressionState
	 * @param order
	 * @param offset
	 * @param num
	 * @return
	 */
	public ISHBrowseSubmission[] getISHBrowseSubmission(String[] components,
			String startStage, String endStage, String expressionState, 
			String[] order, String offset, String num) {
	if (debug)
	    System.out.println("AnatomyStructureAssembler.getISHBrowseSubmission");

        if (components == null || components.length == 0) {
//        	System.out.println("bad components!!!!!!!");
        	return null;
        }
        
		if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
//			System.out.println("bad stage!!!!!!!");
//        	return null;
        }
		
		if (expressionState == null ||
				(!expressionState.equalsIgnoreCase("present") && !expressionState.equalsIgnoreCase("not detected"))) {
//			System.out.println("bad expression state!!!!!!!");
			return null;
		}

		ISHBrowseSubmission[] annotatedSubmissions = null;
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO;
		try{
			anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			ArrayList publicIds = anatomyDAO.findPublicIdByComponentIdAndStage(components, startStage, endStage);
			if (publicIds == null || publicIds.size() == 0) {
	//			System.out.println("bad public id!!!!!!!");
				DBHelper.closeJDBCConnection(conn);
				return null;
			} else {
				annotatedSubmissions =
					anatomyDAO.getAnnotatedSubmissionByPublicIdAndStage(publicIds, startStage, endStage, expressionState,
							order, offset, num);
			}
			/** ---return the composite value object---  */
//			System.out.println("got the value!!!!!!!");
			return annotatedSubmissions;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getISHBrowseSubmission failed !!!");
			annotatedSubmissions = null;				
			/** ---return the composite value object---  */
//			System.out.println("got the value!!!!!!!");
			return annotatedSubmissions;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			anatomyDAO = null;
		}
	}

}
