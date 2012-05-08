/*
 * Created on November-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gmerg.utils.table;


/**
 * @author Mehran Sharghi
 *
 * Extended from GenericTable to represent tables with data kept off memory (session). Data is retrieved from a storage resource 
 * by referin to abstract methos retrieveData and getTotalRows 
 * 
 */
public class OffMemoryTable extends GenericTable
{
	public OffMemoryTable(OffMemoryTableAssembler assembler, HeaderItem[] header) {
		this(assembler, header, -1);
	}
	
	public OffMemoryTable(OffMemoryTableAssembler assembler, HeaderItem[] header, int sortCol) {
		super(assembler, header, sortCol);
		refreshTable();
	}
	
	public DataItem[][] getData(int offset, int rowNum){
		return ((OffMemoryTableAssembler)assembler).retrieveData(sortCol, ascending, offset, rowNum);
	}

	public boolean isInMemory() {
		return false; 
	}
	
	public void refreshTable() {
		super.refreshTable();	// refresh columns
		if (assembler == null) { 
			numRows = 0;
			numRowsInGroups = null;
		}
		else {
			numRows = ((OffMemoryTableAssembler)assembler).retrieveNumberOfRows();
			numRowsInGroups = ((OffMemoryTableAssembler)assembler).retrieveNumberOfRowsInGroups(); 
		}
		setTotals(retrieveTotals());
	}
	
	public int[] getTotals() {
		if (totals == null && header != null)
			setTotals(retrieveTotals());
		return totals;
	}
	
	// This should be override if Totals are required
	public int[] retrieveTotals() {
		int[] totals = null;
		if (header != null){
			totals = ((OffMemoryTableAssembler)assembler).retrieveTotals(); 
			if (totals != null) 
				setTotals(totals);
			else {
				totals = new int[header.length];
				for (int i=0; i<header.length; i++)
					totals[i] = 0;
			}
		}
		return totals;
	}
	
	public boolean deleteRows(String[] rowIds) { // This should be override if functionality is required
		return false;
	}

}  

