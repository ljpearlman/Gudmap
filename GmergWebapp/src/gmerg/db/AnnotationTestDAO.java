package gmerg.db;

import gmerg.beans.UserBean;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ish.ISHBatch;
import gmerg.entities.submission.ish.ISHBatchSubmission;
import gmerg.entities.User;

import java.util.ArrayList;

public interface AnnotationTestDAO {
	
	public ArrayList getTheilerStageRanges();
	public ArrayList<String[]> getStageRanges(); // xingjun - 14/05/2010
	public ArrayList<String> getTreeContent(String stage, String id);
	public String getStageFromSubmission(String id);
	public int deleteAnnotationWithDifferentStage(String stage, String id);
	public int addSubmission(String batch, UserBean ub);
	
	// get batch id of incompleted submission for given pi
	public int getIncompletedSubmissionBatchId(int pi, User user);
	public int createNewBatch(int pi, User user);
	
	// get batch list for given pi
	public ISHBatch[] getBatchListByPi(int pi);
	
	// get submission by batch
	public ISHBatchSubmission[] getSubmissionByBatchId(int batchId);
	public String[] getSubmissionIdsByBatchId(int batchId);
	
	// create a new temporary submission
	public int createNewSubmission(int batchId, User user, int pi);
	// create a new temporary submission based on template submission
	// modified return type to String - xingjun - 14/08/2009
	public String createNewSubmission(Submission templateSubmission, String userName);
	
	// get component and expression info
	public ArrayList<String[]> getComponentAndExpression(String submissionId);
	public int deleteLocation(String submissionId, String componentId, String userName);
	public int deletePattern(String submissionId, String componentId, String userName);
	
	// change stage
	public int updateStage(String submissionId, String stage, String userName);
	
	public int deleteSubmission(String submissionId, String userName);
	public int updateSubmissionDbStatusByBatch(int batchId, int status, String userName);
	public int updateBatchUserStatusByBatch(int batchId, int status, String userName);
	
	// check conflict for the would-be expression
	public int checkConflict(String componentId, ArrayList components, String stage, String expression);
	
	// delete batch
	public int deleteBatch(int batchId, String userName);
	
	// get expression and pattern info for given component node
	public ArrayList<String[]> getExpressionAndPattern(String submissionId, String componentId);
	
	// update submission db status by lab and submission date and state
	public int updateSubmissionDbStatusByLabAndSubDateAndState(int labId, String subDate, int status, String userName, int subState, int isPublicValue);
	
	// xingjun - 01/07/2011
	public int updateSubmissionDbStatusByLabAndSubDateAndState(int labId, String subDate, String archiveId, int status, String userName, int subState, int isPublicValue);

	// get batch status
	public int getBatchStatus(String batchId);
	public Submission getSubmissionSummary(String submissionId);
}
