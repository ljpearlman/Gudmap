/**
 * 
 */
package gmerg.db;

import gmerg.beans.UserBean;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ish.ISHBrowseSubmission;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface AnatomyDAO {

	/** ---implemented--- */
	// used for anatomy structure page
	public ArrayList getTheilerStageRanges();
	public String getOnlogyTerms();
	public int findSequencebyStageName(String StageName);
	public ArrayList getAnatomyTreeByStages(String startStage, String endStage, boolean isForBooleanQ);
	public ArrayList findPublicIdByComponentIdAndStage(String[] components, String startStage, String endStage);
	public ISHBrowseSubmission[] getAnnotatedSubmissionByPublicIdAndStage(ArrayList publicIds,
			String startStage, String endStage, String expressionState, String[] order, String offset, String num);
	public ArrayList findAnnotationTreeBySubmissionId(String submissionAccessionId, boolean isEditor, UserBean userBean);
	public ExpressionDetail [] findAnnotatedListBySubmissionIds(String submissionAccessionId, boolean isEditor);
	public String getAnatomyTermFromPublicId(String id);
	
	/** ---to be implemented--- */
	public ArrayList executeTreeBuild(String treeType, String start, String end) throws SQLException;

	public int getStageSequence(String stage) throws SQLException;

	public ArrayList getComponentExpression(String[] nodeIds, String start, String end, String status) throws SQLException;

	public ArrayList getAnnotatedSubmissions(ArrayList results, String exprStatus, String start, String end, String order) throws SQLException;
	
	public ArrayList getAnatomyQueryResult(String[][] param) throws SQLException;

}
