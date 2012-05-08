/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.Probe;
import gmerg.utils.table.GenericTableFilter;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface ISHDevDAO {

	/** browse all page */
	public ArrayList getAllSubmissionInsitu(int columnIndex, boolean ascending, int offset, int num, GenericTableFilter filter); // allEntriesQuery
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, GenericTableFilter filter);
	public int getTotalNumberOfSubmissions(GenericTableFilter filter);
	
	/**  collection query */
	public ArrayList getSubmissionBySubmissionId(String[] submissionIds, int column, boolean ascending, int offset, int num);
	public String getCollectionTotalsSubmissionISHQuerySection(String[] submissionIds);
	public String getCollectionTotalsQueryEndingClause(String[] submissionIds);
	public String getCollectionTotalsQueryEndingClause(int userPrivilege, String[] submissionIds);
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, String endingClause, GenericTableFilter filter);

	public ArrayList getPublicCollectionSubmissionBySubmissionId(int type, String[] submissionIds, int column, boolean ascending, int offset, int num);
	public ArrayList getAllCollectionSubmissionBySubmissionId(int type, String[] submissionIds, int columnIndex, boolean ascending, int offset, int num);
	public ArrayList getCollectionSubmissionBySubmissionId(int userPrivilege, int type, String[] submissionIds, int columnIndex, boolean ascending, int offset, int num);

	/** edit pages */
	// probe
    public Probe findProbeBySubmissionId(String submissionAccessionId);
    public String findProbeNoteBySubmissionId(String submissionAccessionId);
    public String findMaProbeNoteBySubmissionId(String submissionAccessionId);
    public String findProbeFullSequenceBySubmissionId(String submissionAccessionId);
    
    /**  browse all - non-renal */
    public ArrayList getAllSubmissionsNonRenal(int columnIndex, boolean ascending, int offset, int num);
    public int getTotalNumberOfNonRenalSubmissions();
}
