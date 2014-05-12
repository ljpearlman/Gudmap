/**
 * Prepare data for new array pages
 */
package gmerg.db;

import gmerg.entities.submission.nextgen.NGDSeries;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface NGDDevDAO {
	
	// browse all page
	public ArrayList getAllSeries(int columnIndex, boolean ascending, int offset, int num);
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query);
	public int getTotalNumberOfSeries();
        
    //series page
    public NGDSeries findSeriesById(String id);
    public NGDSeries findSeriesByOid(String oid);
    public ArrayList getSamplesForSeries(String id, int columnIndex, boolean ascending, int offset, int num);
    public ArrayList getSamplesBySeriesOid(String id, int columnIndex, boolean ascending, int offset, int num);
    
    // series browse page
    public String[][] getSeriesBrowseTotals(String[][] param, String[] query, String organ);
    public ArrayList findOriginalImagesById(String submissionAccessionId);
    

}
