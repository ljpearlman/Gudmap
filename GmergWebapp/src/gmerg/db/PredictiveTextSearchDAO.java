/**
 * 
 */
package gmerg.db;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface PredictiveTextSearchDAO {

	public ArrayList<String> getGeneSymbolsAndSynonyms(String input, int searchPattern, int num);
	public ArrayList<String> getAnnatomyTerms(String input, int searchPattern, int num);
	public ArrayList<String> getGoTerms(String input, int searchPattern, int num);
}
