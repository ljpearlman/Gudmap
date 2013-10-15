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
	public ArrayList getAllSubmission(int columnIndex, boolean ascending, int offset, int num, String[] organ, String[] archiveId, String[] batchId, GenericTableFilter filter);
	public int getTotalNumberOfSubmissions(String[] organ, String[] archiveId, String[] batchId, GenericTableFilter filter);
    public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, String endingClause, GenericTableFilter filter);
}
