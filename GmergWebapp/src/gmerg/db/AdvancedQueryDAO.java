package gmerg.db;

import java.util.ArrayList;
import java.util.Hashtable;

import gmerg.utils.table.*;

public interface AdvancedQueryDAO {
	
	/** --- Quick Search --- */
	public ArrayList getQuickSearch(String type, String[] input, int orderby, boolean asc, String offset, String resPerPage, int[] total);

	public ArrayList<String []> getFocusQuery(String type, String[] input, int orderby, 
			boolean asc, String offset, String resperpage, String organ,
			String sub, String exp, String[] queryCriteria, String transitiveRelations, GenericTableFilter filter);
	// Bernie - 9/5/2011 - Mantis 328 - added filter
    public int getNumberOfRows(String type, String[] input, String organ, String sub, String exp, String[] queryCriteria, String transitiveRelations, GenericTableFilter filter);
 
    public int[] getNumberOfRowsInGroups(String type, String[] input, String organ,
    		String sub, String exp, String[] queryCriteria, String transitiveRelations, GenericTableFilter filter);
    //////////////////////////////////
    public int[] getQuickNumberOfRows(String type, String[] input);
}
