/**
 * 
 */
package gmerg.db;

import java.util.ArrayList;

import gmerg.entities.submission.array.*;
import gmerg.entities.submission.nextgen.NGDSample;
import gmerg.entities.submission.nextgen.NGDSeries;
import gmerg.entities.submission.nextgen.Protocol;
import gmerg.entities.submission.nextgen.DataProcessing;


/**
 * @author xingjun
 *
 */
public interface NGDDAO {

	/** ---implemented--- */
	public String getTotalNumberOfSubmission();
        
	public SupplementaryFile findSupplementaryFileInfoBySubmissionId(String submissionAccessionId);
	
	public NGDSample findSampleBySubmissionId(String submissionAccessionId);
	
	public NGDSeries findSeriesBySubmissionId(String submissionAccessionId);
	
	public Protocol findProtocolBySubmissionId(String submissionAccessionId);
	
	public DataProcessing findDataProcessingBySubmissionId(String submissionAccessionId);
	
	public String getTotalNumberOfGeneListItemsBySubmissionId(String submissionAccessionId);
	
	public int getRowNumOf1stOccurrenceOfGeneInArrayGeneList(String accessionId, String geneSymbol);
	
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query);	
	
	public ArrayList findSamplesInCertainSeriesBySubmissionId(String submissionAccessionId);
	
	
	
}
