package gmerg.db;

import gmerg.utils.table.GenericTableFilter;

import java.util.ArrayList;

public interface IHCDAO {
	public ArrayList getAllSubmissionISH(int columnIndex, boolean ascending, int offset, int num, String assayType, String[] organ, String[] archiveId, String[] batchId, GenericTableFilter filter); // allEntriesQuery
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, String assayType);
	public int getTotalNumberOfSubmissions(String assayType, String[] organ, String[] archiveId, String[] batchId, GenericTableFilter filter);

}
