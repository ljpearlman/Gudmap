/**
 * 
 */
package gmerg.db;

import java.util.ArrayList;

import gmerg.entities.submission.ImageDetail;
import gmerg.entities.ChromeDetail;

/**
 * @author xingjun
 *
 */
public interface GeneStripDAO {

	public String findSynonymsBySymbol(String symbol);
	public ArrayList getGeneExpressionForStructure(String symbol, String[] componentIds, 
			boolean expressionForGivenComponents);
	public ArrayList<ImageDetail> getInsituSubmissionImagesByImageId(ArrayList<String> imageIds);
	// get stages for specific gene (insitu data or microarray data)
	public String[] getGeneStages(String symbol, String assayType);
	public int getGeneDiseaseNumber(String symbol);
	public ChromeDetail getChromeDetailBySymbol(String symbol);
}
