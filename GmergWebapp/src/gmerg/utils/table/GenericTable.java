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
 * Abstract class for generic table data representation
 * 
 */
public abstract class GenericTable
{
	protected HeaderItem[] header;
	protected int numCols;
	protected int numRows;  
	protected int[] numRowsInGroups;  // This is optional for number of rows in specific groups of data within the table
	protected boolean ascending;
	protected int sortCol;
	protected int[] totals;
	protected boolean deletable;
	protected GenericTableAssembler assembler;

	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public GenericTable(GenericTableAssembler assembler, HeaderItem[] header) {
		this(assembler, header, -1);
	}
	
	public GenericTable(GenericTableAssembler assembler, HeaderItem[] header, int sortCol) {
		this.header = header;
		numCols = 0;
		totals = null;
		if (header!=null) 
			numCols = header.length;
		ascending = true;
		this.sortCol = sortCol;

		setAssembler(assembler);
		
		deletable = false;
		
		numRowsInGroups = null;
	}
	
	// ********************************************************************************
	// Abstract Methods
	// ********************************************************************************
	public abstract DataItem[][] getData(int offset, int rowNum);

	public abstract boolean isInMemory();
	
	public abstract int[] getTotals();
	
	public abstract boolean deleteRows(String[] rowIds);
	
	// ********************************************************************************
	// Public Methods
	// ********************************************************************************
	public void sort(int col)
	{
		if (col == sortCol)
			ascending = !ascending;
		else 
			ascending = true;
		sortCol = col;
	}

	public void refreshTable() {	// this method only refresh columns if required. Offmemory table also refereshes other values
		if (assembler == null)
			return;
		HeaderItem[] newHeader = assembler.createHeader();
		if (header.length == newHeader.length)
			return;
		if (sortCol >= 0) {
			int newSortCol = -1;
			for (int j=0; j<header.length; j++)
				if (header[sortCol].getTitle().equals(newHeader[j].getTitle())) {
					newSortCol = j;
					break;
				}
			sortCol = newSortCol;
		}
		header = newHeader;
		if (header != null) 
			numCols = header.length;
		else
			numCols = 0;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public HeaderItem[] getHeader(){
		return header;
	}
	
	public int getNumCols() {
		return numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setTotals(int[] totals) {
		this.totals = totals;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public GenericTableAssembler getAssembler() {
		return assembler;
	}

	public void setAssembler(GenericTableAssembler assembler) {
		this.assembler = assembler;
		if (assembler.getFilter() != null)
			this.assembler.filter.setFilterTitles(header);
		this.assembler.setTable(this);
	}

	public int[] getNumRowsInGroups() {
		return numRowsInGroups;
	}

	public void setNumRowsInGroups(int[] numRowsInGroups) {
		this.numRowsInGroups = numRowsInGroups;
	}

	public int getSortCol() {
		return sortCol;
	}

	public void setSortCol(int sortCol) {
		sort(sortCol);
	}
	
}
