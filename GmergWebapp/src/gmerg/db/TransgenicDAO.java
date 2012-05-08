/**
 * 
 */
package gmerg.db;

import gmerg.utils.table.GenericTableFilter;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface TransgenicDAO {
	public ArrayList getAllSubmission(int columnIndex, boolean ascending, int offset, int num, String[] organ, GenericTableFilter filter);
	public int getTotalNumberOfSubmissions(String[] organ, GenericTableFilter filter);
    public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, String endingClause, GenericTableFilter filter);
}
