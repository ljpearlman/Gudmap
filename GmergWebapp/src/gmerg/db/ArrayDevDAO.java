/**
 * Prepare data for new array pages
 */
package gmerg.db;

import gmerg.entities.submission.array.Series;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface ArrayDevDAO {
	
	// browse all page
	public ArrayList getAllSeries(int columnIndex, boolean ascending, int offset, int num, String platform);
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query);
	public int getTotalNumberOfSeries(String platform);
        
    //series page
    public Series findSeriesById(String id);
    public Series findSeriesByOid(String oid);
    public ArrayList getSamplesForSeries(String id, int columnIndex, boolean ascending, int offset, int num);
    public ArrayList getSamplesBySeriesOid(String id, int columnIndex, boolean ascending, int offset, int num);
    
    // series browse page
    public String[][] getSeriesBrowseTotals(String[][] param, String[] query, String organ);
    public ArrayList findOriginalImagesById(String submissionAccessionId);
    
    // checking method
    public ArrayList<String> getRelevantGudmapIds(ArrayList candidateGudmapIds);
    public ArrayList<String> getRelevantGudmapIds(ArrayList candidateGudmapIds, int userPrivilege); // 13/10/2009
    public ArrayList<String> getRelevantSymbols(ArrayList candidateSymbols);
    public ArrayList<String> getRelevantSymbols(ArrayList candidateSymbols, int userPrivilege); // 29/10/2009
    public ArrayList<String> getRelevantProbeSetIds(ArrayList candidateProbeSetIds, String platformId);
    public ArrayList<String> getRelevantImageIds(ArrayList candidateImageIds, int userPrivilege);
}
