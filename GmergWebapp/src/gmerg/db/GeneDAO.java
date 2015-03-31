/**
 * 
 */
package gmerg.db;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface GeneDAO {

	public ArrayList<String> findSynonymsBySymbol(String symbol);
	
	public String findSymbolBySynonym(String synonym);
	
	public ArrayList<String> getSymbolsFromGeneInput(String input, String wildcard);
	
	public String getGeneSymbolByMGIId(String mgiId);
}
