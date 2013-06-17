/**
 * 
 */
package gmerg.db;

import java.util.ArrayList;

import gmerg.entities.submission.array.*;
import gmerg.entities.submission.Gene;
import gmerg.entities.GenelistTreeInfo;
import gmerg.entities.HeatmapData;

import analysis.DataSet;

/**
 * @author xingjun
 *
 */
public interface ArrayDAO {

	/** ---implemented--- */
	public String getTotalNumberOfSubmission();
        
	public SupplementaryFile findSupplementaryFileInfoBySubmissionId(String submissionAccessionId);
	
	public Sample findSampleBySubmissionId(String submissionAccessionId);
	
	public Series findSeriesBySubmissionId(String submissionAccessionId);
	
	public Platform findPlatformBySubmissionId(String submissionAccessionId);
	
	public String getTotalNumberOfGeneListItemsBySubmissionId(String submissionAccessionId);
	
	public int getRowNumOf1stOccurrenceOfGeneInArrayGeneList(String accessionId, String geneSymbol);
	
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query);
	
	// added to allow display tissue data - bernie 23/09/210
	public String findTissueBySubmissionId(String submissionId);
	
	//////////////////////// need refactoring ///////////////////////////////////////////////
	public ArrayList getSubmissionsByLabId(String labId, String submissionDate,
            int columnIndex, boolean ascending, int offset, int num);

	// xingjun - 01/07/2011
	public ArrayList getSubmissionsByLabId(String labId, String submissionDate, String archiveId,
            int columnIndex, boolean ascending, int offset, int num);
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public ArrayList findSamplesInCertainSeriesBySubmissionId(String submissionAccessionId);
	
	// used for analysis
	public String[][] findSampleIdsBySeriesId(String seriesId);
	public DataSet getAnalysisDataSetBySampleIds(String[][] samplesInfo);
	public ArrayList getAllSeriesGEOIds();
	
	// used for microarray focus, gene list
	public ArrayList getComponentListByName(String componentName);
	public ArrayList getGenelistHeadersByComponentNames(ArrayList componentNames);
	public ArrayList getGenelistHeadersByLabId(String labId);
	public ArrayList<SearchLink> getGenelistExternalLinks();
	public Object[][] getGenelistItemsByGenelistId(int genelistId);
	public Object[][] getGenelistItemsByGenelistId(int genelistId, int columnId, boolean ascending, int offset, int num);
	public int getGenelistsEntryNumber(int genelistId);

	// used for heatmap display
	public ArrayList<String> getProbeSetIdBySymbol(String symbol, String platformId);
	public HeatmapData getExpressionByGivenProbeSetIds(ArrayList probeSetIds, 
			String masterTableId, String genelistId);
	public HeatmapData getExpressionByGivenProbeSetIds(ArrayList probeSetIds, 
			int columnId, boolean ascending, int offset, int num);
	public ArrayList<String> getExpressionSortedByGivenProbeSetIds(ArrayList probeSetIds, 
			int columnId, boolean ascending, int offset, int num);
	
	public Gene findGeneInfoBySymbol(ArrayList genes);
	
	public ArrayList<String> getProbeSetIdByAnalysisGenelistId(String genelistId, boolean ascending, int offset, int num);
	public String getAnalysisGenelistTitle(String genelistId);
	public String getAnalysisGenelist(String genelistId);
	
	// linked to master table
	public MasterTableInfo[] getMasterTableList();
	public String getMasterTablePlatformId(String masterTableId);
	public MasterTableInfo[] getMasterTableList(boolean isMaster); // keep it for time being in case required later - xingjun - 03/02/2010
	
	public ArrayList<String> getProbeSetIdBySymbols(String[] symbols, String platformId);
	
	public ArrayList<String> findSampleList(String dataset, String stage, String sample);
	
	public ArrayList<GenelistTreeInfo> getRefGenelists();
	
}
