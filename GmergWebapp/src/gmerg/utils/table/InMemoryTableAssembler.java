package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 *
 * Abstract class for generic table data assembler
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

public abstract class InMemoryTableAssembler extends GenericTableAssembler
{
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public InMemoryTableAssembler()
	{
		super();
	}
	
	public InMemoryTableAssembler(HashMap params)
	{
		super(params);
	}
	
	// ********************************************************************************
	// Abstract Methods
	// ********************************************************************************
	abstract public DataItem[][] retrieveData();
	
	// ********************************************************************************
	// Public Methods
	// ********************************************************************************
    public void setParams() {  // This should be override if there is any parameter locally maintained in the assembler & super.setParams()s hould be called from there 
		anyParameterChenged = true;
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
		table = new InMemoryTable(this, createHeader(), sortCol);
		return table; 
	}

}	
	
