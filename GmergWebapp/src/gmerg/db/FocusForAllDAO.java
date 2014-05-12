package gmerg.db;

import gmerg.utils.table.GenericTableFilter;

import java.util.ArrayList;

public interface FocusForAllDAO {
	// get total number of ish submissions
	public int findTotalNumberOfSubmissionISH(String[] emapids);
	
    // get number of public ish sumbissions
	public int findNumberOfPublicSubmissionISH(String[] emapids);
//	 get total number of ihc submissions
	public int findTotalNumberOfSubmissionArray(String[] emapids);
	
	public int findNumberOfPublicSubmissionArray(String[] emapids);
	
	// get number of public ihc sumbissions
	public int findNumberOfPublicSubmissionIHC(String[] emapids);
	
	// get number of public transgenic sumbissions
	public int findNumberOfPublicSubmissionTG(String[] emapids);
	
	// find the number of public gene entries related to given assay type
	public int findNumberOfPublicGenes(String assayType, String[] emapids);
	
	public ArrayList getFocusBrowseList(String[] organ, int column, boolean ascending, String query, String stage, String gene, String archiveId, String batchId, String offset, String resPerPage, GenericTableFilter filter);

	public int getQuickNumberOfRows(String query, String[] inputs, String stage, String symbol, String archiveId, String batchId, GenericTableFilter filter);

	public ArrayList getSeriesList(int columnIndex, boolean ascending, int offset, int num, String organ, String platform);

	public int getNumberOfSeries(String organ, String platform);
	
	public ArrayList getNGDSeriesList(int columnIndex, boolean ascending, int offset, int num, String organ, String protocol);

	public int getNumberOfNGDSeries(String organ, String protocol);
	
	public ArrayList getPlatformList(int columnIndex, boolean ascending, int offset, int num, String organ);

	public int getNumberOfPlatform(String organ);
	
	// added by Xingjun - 09/11/2007
	public int getNumberOfSubmissionsForLab(String labId, String assayType, String submissionDate);
	public int getNumberOfSubmissionsForLabForAnnotation(String labId, String assayType, String submissionDate, String isPublic);

	// xingjun - 01/07/2011
	public int getNumberOfSubmissionsForLab(String labId, String assayType, String submissionDate, String archiveId, String batchId);
	public int getNumberOfSubmissionsForLabForAnnotation(String labId, String assayType, String submissionDate, String archiveId, String isPublic);
}
