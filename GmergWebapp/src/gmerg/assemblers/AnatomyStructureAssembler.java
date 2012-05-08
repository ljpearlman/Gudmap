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
	
	/**
	 * 
	 * @return
	 */
	public ArrayList getStageRanges() {
		
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get data
		ArrayList stageRanges = anatomyDAO.getTheilerStageRanges();
		
		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		anatomyDAO = null;
		
		/** ---return the composite value object---  */
		return stageRanges;
	}
	
	public String getComponentNameFromId(String id){
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get data
		String componentName = anatomyDAO.getAnatomyTermFromPublicId(id);
		
		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		anatomyDAO = null;
		
		/** ---return the composite value object---  */
		return componentName;
	}
	
	public String getOntologyTerms() {
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get data
		String ontologyTerms = anatomyDAO.getOnlogyTerms();
		
		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		anatomyDAO = null;
		
		/** ---return the composite value object---  */
		return ontologyTerms;
	}
	
	/**
	 * 
	 * @param startStage
	 * @param endStage
	 * @return
	 */
	public boolean stageRangesAreValid(String startStage, String endStage) {

        if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
        	return false;
        }
        
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get data
		int startStageSequence = anatomyDAO.findSequencebyStageName(startStage);
		int endStageSequence = anatomyDAO.findSequencebyStageName(endStage);
			
		// make the judgement
		boolean isValid = true;
		if (startStageSequence == -1 || endStageSequence == -1 || startStageSequence > endStageSequence) {
			isValid = false;
		}

		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		anatomyDAO = null;
		
		/** ---return the composite value object---  */
		return isValid;
	}
	
	/**
	 * 
	 * @param startStage
	 * @param endStage
	 * @return
	 */
	public ArrayList buildTree(String startStage, String endStage, boolean isForBooleanQ) {

        if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
        	return null;
        }
        
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get data
		ArrayList anatomyTree = anatomyDAO.getAnatomyTreeByStages(startStage, endStage, isForBooleanQ);

		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		anatomyDAO = null;
		
		/** ---return the composite value object---  */
		return anatomyTree;
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

        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get data
		ArrayList publicIds = anatomyDAO.findPublicIdByComponentIdAndStage(components, startStage, endStage);
		ISHBrowseSubmission[] annotatedSubmissions = null;
		if (publicIds == null || publicIds.size() == 0) {
//			System.out.println("bad public id!!!!!!!");
			return null;
		} else {
			annotatedSubmissions =
				anatomyDAO.getAnnotatedSubmissionByPublicIdAndStage(publicIds, startStage, endStage, expressionState,
						order, offset, num);
		}

		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		anatomyDAO = null;
		
		/** ---return the composite value object---  */
//		System.out.println("got the value!!!!!!!");
		return annotatedSubmissions;
	}

}
