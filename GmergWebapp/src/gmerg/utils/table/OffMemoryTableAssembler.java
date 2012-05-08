package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 *
 * Abstract class for generic table data assembler
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

public abstract class OffMemoryTableAssembler extends GenericTableAssembler
{
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public OffMemoryTableAssembler() {
		super();
	}
	
	public OffMemoryTableAssembler(HashMap params) {
		super(params);
	}
	
	// ********************************************************************************
	// Abstract Methods
	// ********************************************************************************
	abstract public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num);
		
    abstract public int retrieveNumberOfRows();
	
	// ********************************************************************************
	// Public Methods
	// ********************************************************************************
    public void setParams() {  // This should be override if there is any parameter locally maintained in the eassembler & super.setParams()s hould be called from there
		anyParameterChenged = true;
    }
    
	public int[] retrieveTotals() { // This should be override if functionality is required
		return null;
	}

	public int[] retrieveNumberOfRowsInGroups() { // This should be override if functionality is required
		return null;
	}
		
	public boolean deleteSelectedRows(String[] ids) {  //This should be override if functionality is required
		return false;
	}
	
	public ArrayList<Object> getColumnValues(int columnNo) {  //This should be override if functionality is required
		return null;
	}

	public GenericTable createTable(int sortCol) {
		table = new OffMemoryTable(this, createHeader(), sortCol);
		return table; 
	}

}	
	


