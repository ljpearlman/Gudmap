package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 *
 * Abstract class for generic table data assembler
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GenericTableAssembler
{
	protected HashMap params;
	protected GenericTableFilter filter;
	protected GenericTable table;
	protected boolean anyParameterChenged;
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public GenericTableAssembler() {
		params = null;
		filter = null;
		table = null;
		anyParameterChenged = false;
	}
	
	public GenericTableAssembler(HashMap params) {
		setDataRetrivalParams(params);
		filter = null;
		table = null;
	}
	
	// ********************************************************************************
	// Abstract Methods
	// ********************************************************************************
	public abstract int[] retrieveNumberOfRowsInGroups();
	public abstract boolean deleteSelectedRows(String[] ids);
	public abstract void setParams();
	public abstract HeaderItem[] createHeader();
	public abstract GenericTable createTable(int sortCol);
	public abstract ArrayList<Object> getColumnValues(int columnNo);	// return all values in a column if it is a ccollection ids if there is a c

	public GenericTable createTable() {
		return createTable(-1);
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public HashMap getDataRetrivalParams() {
		return params;
	}

	public void setDataRetrivalParams(HashMap params) {
		if (params!=null) { 
			this.params = params;
			setParams();
		}
		else 
			resetDataRetrivalParams();
		anyParameterChenged = true;
	}
	
	public void resetDataRetrivalParams() {
		anyParameterChenged = true;
		params.clear();
		setParams();
	}
	
	public String getParam(Object key) {
		return (String)params.get(key);
	}
	
	public String[] getParams(Object key) {
		return (String[])params.get(key);
	}
	
	public void setParam(Object key, Object value) {
		if (params==null)
			params = new HashMap();
		params.put(key, value);
		anyParameterChenged = true;
		setParams();
	}
	
	public GenericTableFilter getFilter() {
		return filter;
	}

	public void setFilter(GenericTableFilter filter) {
		this.filter = filter;
		if (table != null) 
			filter.setFilterTitles(table.getHeader());
	}

	public GenericTable getTable() {
		return table;
	}

	public void setTable(GenericTable table) {
		this.table = table;
	}

	public boolean isAnyParameterChenged() {
		return anyParameterChenged;
	}
}	
