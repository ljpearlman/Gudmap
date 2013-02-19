/**
 * 
 */
package gmerg.db;

//import gmerg.entities.submission.ExpressionPattern;
import java.util.ArrayList;

import gmerg.entities.submission.Probe;

/**
 * @author xingjun
 *
 */
public interface ISHEditDAO {
	// linked to ish browse all page
	public boolean deleteSelectedSubmission(String[] selectedSubmissions);
	
	// linked to expression editing page
	public int insertExpression(String submissionId, String componentId, String secondaryStrength, String userName);
	public int insertExpression(String submissionId, String componentId, String primaryStrength, String secondaryStrength, String userName);
	public int updateExpressionStrengh(String submissionId, String componentId, int type, String strength, String userName);
	public int insertPattern(String submissionId, String componentId, String pattern, String userName);
	public int insertLocation(String submissionId, String componentId, String pattern, String location, String userName);
	public int insertLocation(int patternId, String location, String userName);
	public int updateLocation(int patternId, int locationId, String location, String userName);
	public int deleteExpression(String submissionId, String componentId, String userName);
	public int deletePatternById(int patternId, String userName);
	public int deletePatternByIds(String submissionId, String componentId, String pattern, String userName);
	public int deleteLocation(int locationId, String userName);
	public int deleteLocationByPattern(String submissionId, String componentId, String pattern, String userName);
	public int addExpressionNote(String submissionId, String componentId, String note, int serialNo, String userName);
	public int deleteExpressionNotes(String submissionId, String componentId, String userName);
	public int updateExpressionNotes(String submissionId, String componentId, String newNote, String userName);
	public int existConflict(String submissionId, String stage, String componentId, String strength);
	public boolean signOffAnnotation(String submissionId, String status);
	public boolean signOffAnnotation(String submissionId, String oldStatus, String newStatus);
	public boolean setPublicSubmission(String submissionId, String status);
	public boolean signOffAnnotationAndSetPublicSubmission(String submissionId, String dbstatus, String substatus);
	public ArrayList getPIBySubID(String submissionId);
	
	// linked to submisison status note
	public int insertStatusNote(String submissionId, String statusNote, String userName);
	public int deleteStatusNotesBySubmissionId(String submissionId, String userName);
	public int updateStatusNoteById(int statusNoteId, String statusNote, String userName);
	public int deleteStatusNotesByStatusNoteId(int statusNoteId, String userName);
	
	// methods linked to locking function
	public int insertSubmissionLockingInfo(String submissionId, String userName);
	public int insertSubmissionLockingInfo(String submissionId, int userId);
	public int updateSubmissionLockingInfo(String submissionId, String userName);
	public int updateSubmissionLockingInfo(String submissionId, int userId);
	public int deleteSubmissionLockingInfo(String submissionId);
	public int deleteUserLockingInfo(String userName);
	public int deleteUserLockingInfo(int userId);
	public boolean isBatchLocked(int batchId, int userId);
}
