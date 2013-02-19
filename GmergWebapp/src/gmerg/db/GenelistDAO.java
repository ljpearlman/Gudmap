/**
 * 
 */
package gmerg.db;

//import java.util.Comparator;
//import java.util.Arrays;

import java.util.ArrayList;

import gmerg.entities.GenelistInfo;
import gmerg.entities.BrowseTableTitle;

/**
 * @author xingjun
 *
 */
public interface GenelistDAO {

	public String[] getAnalysisTitles(int ref);	
	
	public double[][] getAllAnalysisData(int ref);
	
	public String[] getAllIds(int ref);
	
	// DAO should also provide number of gudmap entries & omim id		
	public String[][] getAllOntologies(int ref);
	
	public BrowseTableTitle[] getMasterTableExpressionTitles(String masterTableId);
	public String[][] getAnnotationByProbeSetIds(ArrayList probeSetIds);
	public ArrayList<GenelistInfo> getAllAnalysisGeneLists();
	
	public String getAnalysisGenelistPlatformId(String genelistId);
	public String[] getGeneSymbolByAnalysisGenelistId(String genelistId, String platformId);
	
	 
	public ArrayList<GenelistInfo> getAllAnalysisGenelistsWithFolderIds();
	public ArrayList<GenelistInfo> retrieveClustersForGenelist(String genelistId);
}
