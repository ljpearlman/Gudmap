/*
 * Created on November-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gmerg.utils.table;

import java.util.*;

/**
 * @author Mehran Sharghi
 *
 * Extended from GenericTable to represent tables with data kept in memory (session)
 * 
 */
public class InMemoryTable extends GenericTable
{
	protected DataItem[][] data;

	class RowComparator implements Comparator
	{
		int col;
		boolean ascending;

		public RowComparator(int col)
		{
			this.col = col;
			ascending = true;
		}  

		public RowComparator(int col, boolean ascending)
		{
			this.col = col;
			this.ascending = ascending;
		}  

		public int compare(Object o1,Object o2)
		{
			DataItem[] r1 = (DataItem[])o1;
			DataItem[] r2 = (DataItem[])o2;
			Comparable ref = (Comparable)r1[col].value;
			if (ascending)
				return ref.compareTo(r2[col].value);
			else
				return -(ref.compareTo(r2[col].value));
		}
	}

	public InMemoryTable(InMemoryTableAssembler assembler, HeaderItem[] header)	{
		this(assembler, header, -1);
	}
	
	public InMemoryTable(InMemoryTableAssembler assembler, HeaderItem[] header, int sortCol) {
		super(assembler, header, sortCol);
		loadData();
	}
	
	public void sort(int col) {
		super.sort(col);
		if (col==-1)
			loadData();
		else
			Arrays.sort(data, new RowComparator(col, ascending));
	}
	
	private void loadData() {
		data = ((InMemoryTableAssembler)assembler).retrieveData();
		if (data != null)
			numRows = data.length;
		else
			numRows = 0;
		numRowsInGroups = ((InMemoryTableAssembler)assembler).retrieveNumberOfRowsInGroups();
		
	}

/*	
	public void setData(DataItem[][] data)
	{
		this.data = data;
		if (data!=null)
			numRows = data.length;
		else
			numRows = 0;

		ascending = true;
		sortCol = -1;
	}
*/
	
	public DataItem[][] getData(int offset, int rowNum){
/*		the offset & rowNum are taken care of in TableBean LoadData model 
		int n = Math.min(offset+rowNum, data.length) - offset;
		DataItem[][] pageData = new DataItem[n][]; 
		System.arraycopy(data, offset, pageData, 0, n);
		return pageData;
*/		
		return data;
	}

	public boolean isInMemory() {
		return true; 
	}

	public int[] getTotals() {
		if (totals == null && header != null) {
			totals = new int[header.length];
			for (int j=0; j<header.length; j++) {
				HashSet column = new HashSet();
				for (int i=0; i<data.length; i++) 
					column.add(data[i][j].value);
				totals[j] = column.size();
			}
		}
		return totals;
	}
	
	public boolean deleteRows(String[] rowIds) {
		 // Code should be added later needs to do same as constructor and assembler delete is required 
		return false;
	}
	
}  

