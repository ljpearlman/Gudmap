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
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			ArrayList stageRanges = anatomyDAO.getTheilerStageRanges();
			return stageRanges;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getStageRanges failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public String getComponentNameFromId(String id){
		if (debug)
		    System.out.println("AnatomyStructureAssembler.getComponentNameFromId");
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			String componentName = anatomyDAO.getAnatomyTermFromPublicId(id);
			return componentName;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getComponentNameFromId failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public String getOntologyTerms() {
		if (debug)
		    System.out.println("AnatomyStructureAssembler.getOntologyTerms");
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			String ontologyTerms = anatomyDAO.getOnlogyTerms();
			return ontologyTerms;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getOntologyTerms failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
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
        
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
	        boolean isValid = true;
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			int startStageSequence = anatomyDAO.findSequencebyStageName(startStage);
			int endStageSequence = anatomyDAO.findSequencebyStageName(endStage);

			if (startStageSequence == -1 || endStageSequence == -1 || startStageSequence > endStageSequence) {
				isValid = false;
			}

			return isValid;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::stageRangesAreValid failed !!!");
			return false;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
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
        
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			ArrayList anatomyTree = anatomyDAO.getAnatomyTreeByStages(startStage, endStage, isForBooleanQ);
			return anatomyTree;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::buildTree failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
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
			return null;
		}

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHBrowseSubmission[] annotatedSubmissions;
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			ArrayList publicIds = anatomyDAO.findPublicIdByComponentIdAndStage(components, startStage, endStage);
			if (publicIds == null || publicIds.size() == 0) {
				return null;
			} else {
				annotatedSubmissions =
					anatomyDAO.getAnnotatedSubmissionByPublicIdAndStage(publicIds, startStage, endStage, expressionState,
							order, offset, num);
			}
			return annotatedSubmissions;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getISHBrowseSubmission failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

}
