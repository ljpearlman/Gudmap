package gmerg.utils.table;

import gmerg.entities.Globals.PredefinedFilters;
import gmerg.utils.DbUtility;
import gmerg.utils.Utility;

import java.util.Date;

import javax.faces.model.SelectItem;

/**
 * @author Mehran Sharghi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */


public class FilterItem {
	public enum FilterType {SIMPLE, CHECKBOX, LIST, MULTIPLE, RADIO, DATE, SIMPLERANGE, LISTRANGE, DATERANGE}
	
	private boolean active;
	private int col;
	private String name;
	private PredefinedFilters key;
	private FilterType type;
	private String value1;
	private String value2;
	private String[] options;
	private int size;
	private boolean rangeSelect;
	private boolean rangeSwap;	// it is used for range filter and when it is true it mean to swap two range values if value1>value2
	private final static String separator = ";"; 
	private boolean numeric;
	
	public FilterItem(int col) {
		this(col, FilterType.SIMPLE, null);
	}
	
	public FilterItem(int col, FilterItem predefinedFilter) {
		this(col, predefinedFilter.type, predefinedFilter.options, predefinedFilter.getValue1().split(separator), predefinedFilter.numeric);
	}

	public FilterItem(int col, FilterItem predefinedFilter, String[] initialValues) {
		this(col, predefinedFilter.type, predefinedFilter.options, initialValues, predefinedFilter.numeric);
	}

	public FilterItem(int col, FilterType type, String[] options) {
		this(col, type, options, null, false);
	}
	
	public FilterItem(int col, FilterType type, String[] options, boolean numeric) {
		this(col, type, options, null, numeric);
	}
	
	public FilterItem(FilterType type, String[] options, String[] initialValues) {
		this(0, type, options, initialValues, false);
	}
	
	public FilterItem(FilterType type, String[] options, String[] initialValues, boolean numeric) {
		this(0, type, options, initialValues, numeric);
	}

	public FilterItem(FilterType type, String[] options, String[] initialValues, boolean numeric, String name) {
		this(0, type, options, initialValues, numeric, name);
	}
	
	public FilterItem(int col, FilterType type, String[] options, String[] initialValues, boolean numeric) {
		name = "filter-"+String.valueOf(col); //This will replace when assembler assigned to a table 
		active = false;
		this.col = col;
		this.type = type;
		this.options = options;
		if (this.options != null) {
			if(initialValues!=null) {
				value1 = "";
				for (int i=0; i<initialValues.length; i++)
					value1 += ((i==0)?"":separator) + initialValues[i];
			}
			else
				value1 = this.options[0];
			if (isRange())
				value2 = this.options[0];
		}
		else
			value1 = value2 = "";
		rangeSelect = isRange();
		this.numeric = numeric;
		rangeSwap = false;
	}
	
	public FilterItem(int col, FilterType type, String[] options, String[] initialValues, boolean numeric, String name) {
		this.name = name;
		active = false;
		this.col = col;
		this.type = type;
		this.options = options;
		if (this.options != null) {
			if(initialValues!=null) {
				value1 = "";
				for (int i=0; i<initialValues.length; i++)
					value1 += ((i==0)?"":separator) + initialValues[i];
			}
			else
				value1 = this.options[0];
			if (isRange())
				value2 = this.options[0];
		}
		else
			value1 = value2 = "";
		rangeSelect = isRange();
		this.numeric = numeric;
		rangeSwap = false;
	}

	public String getSql(String colName) {
		String value1 = this.value1;
		String value2 = this.value2;
		
		//System.out.println("value1====="+value1);
		//System.out.println("value2====="+value2);
		String predef = this.getKey().toString();
		if (predef == "THEILER_STAGE"|| predef == "HUMAN_STAGE"){
			colName = "STG_ORDER";
			// converts stage to its associated number value
			value1 = DbUtility.getRefStageOrder(value1);
			value2 = DbUtility.getRefStageOrder(value2);
		}
		
//		if (colName == "STG_STAGE_DISPLAY" || colName == "QIC_STG_STAGE_DISPLAY" || colName == "QMC_STG_STAGE_DISPLAY" || colName == "MBC_STG_STAGE_DISPLAY"){
//			colName = "STG_ORDER";
//				
//			// converts stage to its associated number value
//			value1 = DbUtility.getRefStageOrder(value1);
//			value2 = DbUtility.getRefStageOrder(value2);
//		}

		
		if(type == FilterType.DATE || type == FilterType.DATERANGE) {
			value1 = Utility.convertToDatabaseDate(value1);
			if (value2!=null)
				value2 = Utility.convertToDatabaseDate(value2);
		}
				
		String q = numeric? "" : "'";
		if (isRange() & rangeSelect) {
			if (rangeSwap && value2!=null && !value2.trim().equals(""))
				if (type==FilterType.DATERANGE && Utility.compareDates(value1, value2)>0 || type!=FilterType.DATERANGE && Integer.parseInt(value2) < Integer.parseInt(value1)) {
					String temp = value1;
					value1 = value2;
					value2 = temp;
					//System.out.println("new value1====="+value1);
					//System.out.println("new value2====="+value2);
				}
			if (value1 == null || value1.equals(""))
				if (value2 == null || value2.equals(""))
					return "";
				else
					return "(" + colName + " <= " + q + value2 +  q + ")";
			else
				if (value2 == null || value2.equals(""))
					return "(" + colName + " >= " + q + value1 +  q + ")";
				else
					return "(" + colName + " >= " + q + value1 +  q + " AND " + colName + " <= " + q + value2 + q + ")";
		}
		else
			if (value1 == null || value1.equals(""))
				return "";

		String[] values = value1.split(separator);
		if (values.length == 0)
			return "";
		String sql = "(";
		for (int i=0; i<values.length; i++) {
			if (values[i].indexOf("*")<0)
				sql += ((i==0)?"" : " OR ") + colName + "=" + q + values[i]  + q;
			else
				sql += ((i==0)?"" : " OR ") + "(" + colName + " LIKE " + q + values[i].replaceAll("\\*", "%") + q + ")";
		}
		sql += ")";

		return sql;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean isRange() {
		return ( type == FilterType.SIMPLERANGE || type == FilterType.LISTRANGE || type == FilterType.DATERANGE);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PredefinedFilters getKey() {
		return key;
	}

	public void setKey(PredefinedFilters filter) {
		this.key = filter;
	}
	
	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	public String getTypeString() {
		return type.toString();
	}
	
	public SelectItem[] getSelectOptions() {
		SelectItem[] selectOptions = new SelectItem[options.length];
		for (int i=0; i<options.length; i++)
			selectOptions[i] = new SelectItem(options[i], options[i]);
		return selectOptions;
	}

	public boolean isRangeSelect() {
		return rangeSelect;
	}

	public void setRangeSelect(boolean rangeSelect) {
		this.rangeSelect = rangeSelect;
	}

	//*********************************************
	public String[] getMultipleValue() {
		return value1.split(separator); 
	}
	
	public void setMultipleValue(String[] value) {
		value1 = "";
		for (int i=0; i<value.length; i++)
			value1 += ((i==0)?"":separator) + value[i];
	}

	public Date getDate1() {
		if ("".equals(value1))
			return null;
		String dateValue = Utility.convertToDatabaseDate(value1);  
		return new Date(java.sql.Date.valueOf(dateValue).getTime());
	}
	
	public void setDate1(Date date) {
		if (date==null) {
			value1 = ""; 
			return;
		}
		value1 = Utility.convertToDisplayDate(date);  
	}
	
	public Date getDate2() {
		if ("".equals(value2))
			return null;
		String dateValue = Utility.convertToDatabaseDate(value2);  
		return new Date(java.sql.Date.valueOf(dateValue).getTime());
	}
	
	public void setDate2(Date date) {
		if (date==null) {
			value2 = ""; 
			return;
		}
		value2 = Utility.convertToDisplayDate(date);  
	}

	//*******************************
	public String getOriginalValue1() {
		return value1;
	}

	public void setOriginalValue1(String value1) {
	}

	public String getOriginalValue2() {
		return value2;
	}

	public void setOriginalValue2(String value2) {
	}

	public boolean isNumeric() {
		return numeric;
	}

	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
	}

	public boolean isRangeSwap() {
		return rangeSwap;
	}

	public void setRangeSwap(boolean rangeSwap) {
		this.rangeSwap = rangeSwap;
	}

	
}
