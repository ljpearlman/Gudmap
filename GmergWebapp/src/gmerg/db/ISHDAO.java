/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.Gene;
import gmerg.entities.submission.ImageDetail;
import gmerg.entities.submission.LockingInfo;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Probe;
import gmerg.entities.submission.Allele;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
import gmerg.entities.submission.StatusNote;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface ISHDAO {
	
	/** ---implemented--- */
	/** browse all page */
	public String getTotalNumberOfSubmission();
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query);
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, String endingClause); //for collection
    public String getCollectionTotalsSubmissionISHQuerySection(String [] submissionIds); //for collection
	
	/** used for single submission page */
	public Submission findSubmissionById(String submissionAccessionId); // q0
	public Probe findProbeBySubmissionId(String submissionAccessionId); // q1
	public Probe findMaProbeByProbeId(String probeId, String maprobeId);
	public Specimen findSpecimenBySubmissionId(String submissionAccessionId); // q2
	public Allele[] findAlleleBySubmissionId(String submissionAccessionId); 
	public String findAuthorBySubmissionId(String submissionAccessionId); // q16
	public Person[] findPIsBySubmissionId(String submissionAccessionId); // xingjun - 20/06/2011
	public Person findPersonById(String personId);
	public Person findSubmitterBySubmissionId(String submissionAccessionId); // q4
	public ArrayList findImageBySubmissionId(String submissionAccessionId); // q6
	public ArrayList findPublicationBySubmissionId(String submissionAccessionId); // q7
	public String[] findAcknowledgementBySubmissionId(String submissionAccessionId); // q8

	// added on 29th March 2007: Xingjun
	public ArrayList findLinkedSubmissionBySubmissionId(String submissionAccessionId); // q9
	
	// added to allow display antibody data - xingjun 09-July-2007
	public Antibody findAntibodyBySubmissionId(String submissionAccessionId);
	public Antibody findAntibodyByAntibodyId(String antibodyId);
	
	// added to allow display tissue data - bernie 23/09/210
	public String findTissueBySubmissionId(String submissionId);
	
    /** gene page */
	public Gene findGeneInfoBySymbol(String symbol); // q11
	public Gene findGeneInfoBySymbolId(String symbolId); // q11
	public ArrayList findRelatedSubmissionBySymbolIdInsitu(String symbol);
	public ArrayList findRelatedSubmissionBySymbolIdISH(String symbol);
	public ArrayList findRelatedSubmissionBySymbolISH(String symbol);
	public ArrayList findRelatedSubmissionBySymbolIdIHC(String symbol);
	public ArrayList findRelatedSubmissionBySymbolIdTG(String symbol);
	public ArrayList findRelatedSubmissionBySymbolArray(String symbol, int columnIndex,
                                         boolean ascending, int offset,
                                         int num);
	public ArrayList findRelatedMAProbeBySymbol(String symbol);
        public Gene findFurtherGeneInfoForMicroarray(Gene geneInfo);
        public Gene addGeneInfoIuphar(Gene geneInfo);
	
	/** query page */
	// gene query
                        
	public String getTotalNumberOfISHSubmissionsForGeneQuery(String inputType, String ignoreExpression, String inputString, String stage, String criteria);
	public String getTotalNumberOfISHSubmissionsForGeneQuery(String inputType, String ignoreExpression, String inputString, String component, String stage, String criteria);

	public String[][] getTotalNumberOfColumnsGeneQuery(String[] query, String inputType, String ignoreExpression, String inputString,
			String stage, String criteria);
	
	public String[][] getTotalNumberOfColumnsGeneQuery(String[] query, String inputType, String ignoreExpression, String inputString,
			String componentId,
			String stage, String criteria);

	// component count query
public ArrayList getComponentCountInfoByGeneInfo(String inputType, String inputString,
			String stage, String criteria, int columnIndex, boolean ascending, int offset, int num);
                                                                 
	public String getTotalISHComponentsExpressingGeneQuery(String inputType, String ignoreExpression, String inputString, String stage, String criteria);

	// component query
	public ISHBrowseSubmission[] getSubmissionByComponentId(String component, String stage,
			                                                String[] order, String offset, String num);
                                                                        
    //number of results from 'component query'
    public String getTotalNumberOfISHSubmissionsForComponentQuery(String component);
                                                                        
	public ISHBrowseSubmission[] getSubmissionByComponentIds(String[] component, String start,String end, String [] annotationTypes, String criteria,
			                                                 String[] order, String offset, String num);

	public ArrayList getSubmissionByComponentIds(String[] components, int columnIndex, boolean ascending, int offset, int num);
	public ArrayList getSubmissionByComponentIds(String[] components, String startStage, String endStage, String [] annotationTypes, String criteria, int columnIndex, boolean ascending, int offset, int num);
	public ArrayList getSubmissionByComponentId(String component, String stage, int columnIndex, boolean ascending, int offset, int num);
	public ArrayList getSubmissionByGeneInfo(String inputType, String ignoreExpression,
            String inputString, String stage, String criteria, int columnIndex, boolean ascending, int offset, int num);
	
    public String getTotalNumberOfSubmissionsForComponentsQuery(String [] components, String start, String end, String [] annotationTypes, String criteria);
        
    public String getTotalNumberOfSubmissionsForComponentsQuery(String [] components);
                                                                         
    public ISHBrowseSubmission[] getSubmissionByComponentIds(String[] component, 
                                                                     String[] order, String offset, String num);
	
	// submission query
	public String findAssayTypeBySubmissionId(String accessionId);

	/** lab summary page */
	// find all lab name and id
	public String[][] findAllPIs(); // pINamesQuery
	public String[][] findPIFromName(String username, int privilege);
	public String findLastEntryDateInDBByLabId(int labId);
	public ArrayList findSubmissionSummaryByLabId(int labId, int location); // location: 1-db; 2-ftp
	public ArrayList[] findSubmissionSummaryByLabIdForAnnotation(int labId, int[] location);
	
	//////////////////////////////// need refactoring - redundant ///////////////////////////////////////////
	public ISHBrowseSubmission[] getSubmissionsByLabId(String labId, String assayType, String submissionDate,
			                                           String[] order, String offset, String num);
	public ArrayList getSubmissionsByLabId(String labId, String assayType,
            String submissionDate, int columnIndex, boolean ascending, int offset, int num);
	public ArrayList getSubmissionsForAnnotationByLabId(String labId, String assayType,
            String submissionDate, int columnIndex, boolean ascending, int offset, int num, String isPublic);

	// xingjun - 01/07/2011
	public ArrayList getSubmissionsByLabId(String labId, String assayType, String submissionDate, 
			String archiveId, int columnIndex, boolean ascending, int offset, int num, String batchId);
	public ArrayList getSubmissionsForAnnotationByLabId(String labId, String assayType, String submissionDate, 
			String archiveId, int columnIndex, boolean ascending, int offset, int num, String isPublic);
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String findPIByLabId(String labId);
	public String getTotalNumberOfISHSubmissionsForLabQuery(String labId, String date);
	public String getTotalNumberOfIHCSubmissionsForLabQuery(String labId, String date);
	
	/** --db summary page--- */
	// find the number of total submissions users sent to GUDMAP DB
	public int findTotalNumberOfSubmissionISH(); // numTotalIshSubmissionsQuery
	public int findTotalNumberOfSubmissionArray(); // numTotalArraySubmissionsQuery
	public int findTotalNumberOfSubmissionIHC();
	public int findTotalNumberOfSequences();	
	
	// find the number of public submissions
	public int findNumberOfPublicSubmissionISH(); // numPubISHSubmissionsQuery
	public int findNumberOfPublicSubmissionArray(); // numPubArraySubmissionsQuery
	public int findNumberOfPublicSubmissionIHC();
	public int findNumberOfPublicSubmissionTG();
	public int findNumberOfPublicSubmissionWISH();
	public int findNumberOfPublicSubmissionSISH();
	public int findNumberOfPublicSubmissionOPT();
	
	// find the number of public gene entries related to given assay type
	public int findNumberOfPublicGenes(String assayType); // numPubGenesQuery
	
	public String findLastSoftwareUpdateDate(); // lastSoftwareUpdat
	public String findLastEditorialUpdateDate(); // lastEditorialUpdateQuery
	public String findLastEntryDateInDB(); // lastEntryDateQuery
	public String getApplicationVersion(); // application version info
	
	// image detail popup window
	public ImageDetail findImageDetailBySubmissionId(String submissionAccessionId, int serialNum);
	public ImageDetail findWlzImageDetailBySubmissionId(String submissionAccessionId); // q6
	
	// submission collection
	public ISHBrowseSubmission[] getSubmissionBySubmissionId(String[] submissionIds,
			String order[], String offset, String num);
	
	/** ---expression detail--- */
	public ExpressionDetail findExpressionDetailBySubmissionIdAndComponentId(String submissionAccessionId, String componentId);
	public String findAnnotationNote(String submissionAccessionId, String componentId); // q27
	public String findDensityNote(String submissionAccessionId, String componentId); // q27
	public ArrayList<String> findDensityDetail(String submissionAccessionId, String componentId); // q27
	public String findStageBySubmissionId(String submissionAccessionId); // q33
	public ExpressionPattern [] findPatternsAndLocations(String expressionId);
	public ExpressionPattern [] findPatternsAndLocations(boolean forEditing, String expressionId);

	public ArrayList findComponentDetailById(String componentId); // q35
	public boolean hasParentNode(String componentId, String stageName, String submissionAccessionId); // q31
	public boolean hasChildenNode(String componentId, String stageName, String submissionAccessionId); // q32

	// added by xingjun - 13/06/2008
	public ArrayList getPatternList();
	public ArrayList getLocationList();
	
	// added by xingjun - 16/06/2008
	public ArrayList getComponentListAtGivenStage(String stage);
	
	// status notes - only display when editor logged in
	public StatusNote[] getStatusNotesBySubmissionId(String submissionAccessionId);
	
	// locking info
	public LockingInfo getLockingInfo(String accessionId);
	
	
}
