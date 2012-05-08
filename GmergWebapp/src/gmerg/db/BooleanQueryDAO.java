package gmerg.db;

import java.util.ArrayList;
import gmerg.utils.table.*;

public interface BooleanQueryDAO {
	public ArrayList getAllSubmissions(String input, int columnIndex, boolean ascending, int offset, int num, GenericTableFilter filter);
	public int getTotalNumberOfSubmissions(String input, GenericTableFilter filter);
	public int[] getNumberOfRowsInGroups(String input, GenericTableFilter filter);
	public ArrayList getGeneSymbols(String input);
}
