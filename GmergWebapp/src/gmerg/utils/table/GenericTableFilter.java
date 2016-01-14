package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

import gmerg.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
//import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericTableFilter {
    protected boolean debug = false;

	boolean active;
//	TreeMap<Integer, FilterItem> filters;
	LinkedHashMap<String, FilterItem> filters;
	int[] tableToSqlColMap;
	String[] insituMap = {"QIC_RPR_SYMBOL","#","QIC_SUB_SOURCE","QIC_SUB_SUB_DATE","QIC_SUB_ASSAY_TYPE","QIC_PRB_PROBE_NAME","QIC_STG_STAGE_DISPLAY","7","QIC_SPN_SEX","QIC_SPN_WILDTYPE","#","QIC_EXP_STRENGTH","#","QIC_SPN_ASSAY_TYPE","#","#","#"};
//	String[] insituMap = {"QIC_RPR_SYMBOL","#","QIC_ASSAY_TYPE","QIC_EXP_STRENGTH","#","#","QIC_SUB_EMBRYO_STG","7","QIC_SPN_SEX","QIC_SUB_SOURCE","QIC_SUB_SUB_DATE","QIC_SPN_ASSAY_TYPE","#","#","#"};
	String[] microarrayMap = {"MBC_GNF_SYMBOL","#","QMC_SUB_SOURCE","#","#","#","MBC_STG_STAGE_DISPLAY","#","QMC_SPN_SEX","MBC_SUB_SOURCE","MBC_SUB_SUB_DATE","MBC_SPN_ASSAY_TYPE","#","#","#"};
//	String[] microarrayMap = {"MBC_GNF_SYMBOL","#","#","#","#","#","MBC_SUB_EMBRYO_STG","#","QMC_SPN_SEX","MBC_SUB_SOURCE","MBC_SUB_SUB_DATE","MBC_SPN_ASSAY_TYPE","#","#","#"};
	String[] microarrayMapQuery = {"#","#","#","#","#","#","QMC_STG_STAGE_DISPLAY","#","QMC_SPN_SEX","QMC_SUB_SOURCE","QMC_SUB_SUB_DATE","QMC_SPN_ASSAY_TYPE","#","#","#"};
			
	public GenericTableFilter() {
		active = false;
//		filters = new TreeMap<Integer, FilterItem>();
		filters = new LinkedHashMap<String, FilterItem>();
		tableToSqlColMap = null;
	}

	public void addFilter(FilterItem filter) {
//		filters.put(filter.getCol(), filter);
		filters.put(filter.getName(), filter);
	}

	public void addFilter(int col, FilterItem filter) {
		filter.setCol(col);
		filters.put(filter.getName(), filter);
	}

	public void setFilterTitles(HeaderItem[] header) {
		for(FilterItem filter: filters.values()) {
			
			String name = filter.getName();
			System.out.println("filter name = "+name);
			
			if (name.contains("filter"))
				filter.setName(header[filter.getCol()].getTitle());
		}
	}
	
	public LinkedHashMap<String, FilterItem> getFilters() {
		return filters;
	}
	
	public FilterItem getFilter(int col) {
		return filters.get(col);
	}
	
                private boolean hasFilter() {
		if (filters == null || filters.size()==0)
			return false;

		for(FilterItem filter: filters.values()){
			if (filter.isActive()) 
			    return true;
		}

		return false;
              }

	private String getFilterSql(String currentSql, String[] columnNames) {
	    if (!hasFilter())
		return null;

	    if (debug) {
		System.out.println("enter GenericTableFilter:getFilterSql(String currentSql, String[] columnNames) currentSql = "+currentSql);
		int iSize = 0;
		if (null != columnNames)
		    iSize = columnNames.length;
		int i = 0;
		for (i = 0; i < iSize; i++)
		    System.out.println(i+" col = "+columnNames[i]);
	    }
		
		String sql = "";
		for(FilterItem filter: filters.values()){
		    if (debug)
			System.out.println(filter.getCol()+"FFFFFFFFFFFFFFFFFFFF=="+filter.getName()+"~~~~~~~~~~"+filter.isActive());				
			
			if (filter.isActive()) {
				String colName = "";
				
				if (tableToSqlColMap == null){
					colName = extractSqlName(columnNames[filter.getCol()]);
				} else {
					int col = filter.getCol();
					if (currentSql.contains("QIC_RPR_SYMBOL")){
						colName = insituMap[col];
					}
					if (currentSql.contains("MBC_GNF_SYMBOL")){
						colName = microarrayMap[col];
					}
					if (currentSql.contains("QMC_SPN_ASSAY_TYPE")){
						colName = microarrayMapQuery[col];
					}
					//Derek modify filter to accept sequence data from accession id search. below will only work if its ish
					/*if (currentSql.contains("QIC_SUB_ACCESSION_ID")){
						colName = insituMap[col];
					}*/
					
				}
						
				String filterSql = "";
				if(!colName.equalsIgnoreCase("#"))
					filterSql = filter.getSql(colName);

				// Bernie - a bit of a hack to fix filter until we sort the query - see derek
				if (filterSql.contains("(SELECT GROUP_CONCAT(DISTINCT EXP_STRENGTH) FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID)")){
					filterSql = filterSql.replace("(SELECT GROUP_CONCAT(DISTINCT EXP_STRENGTH) FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID)", "");
				}
//				if (filterSql.contains("GROUP_CONCAT(DISTINCT EXP_STRENGTH)")){
//					filterSql = filterSql.replace("GROUP_CONCAT(DISTINCT EXP_STRENGTH)", "EXP_STRENGTH");
//				}
				
				if (debug)
				    System.out.println("colName = "+colName+" filterSql = "+filterSql+" current sql = "+currentSql);

				// modified to allow both human and theiler stages to be selected in the filters
				String predef = filter.getKey().toString();
				String speciessql = "(STG_SPECIES = ";
				if (predef == "THEILER_STAGE")
					speciessql += "'Mus musculus')";
				else
					speciessql += "'Homo sapiens')";
					
				if (filterSql != ""){
					if (filterSql.contains("STG_ORDER")){
						if (sql.equals(""))
							sql +=  filterSql + "AND " + speciessql;
						else
							sql +=  " AND " + filterSql + "AND " + speciessql;					
//							sql = "(" + sql + " OR " + filterSql + ")" + "AND " + speciessql;
					}
					else
						sql += ((sql.equals(""))? "" : " AND ") + filterSql;
				}
			}
		}
		
		if (debug)
		    System.out.println("exit GenericTableFilter.getFilterSql = "+sql);

		return sql;
	}

	public String addFilterCountSql(String sql) {
	    if (debug)
		System.out.println("enter GenericTableFilter.addFilterCountSql sql = "+sql);
	    // to check whether it has filter
	    if (!hasFilter())
		return sql;

		// strip out the count part of the sql statement
		Pattern patternCountStar = Pattern.compile("^(?i)\\s*select\\s* count\\s*\\(\\s*\\*\\s*\\)\\s*from\\s*\\(\\s*(.*)\\)");

		String ret = sql;
		
		Matcher matchcount = patternCountStar.matcher(sql);
		if (matchcount.find()) {
		    ret = processFilterSql(matchcount.group(1), null);

		    ret = "select count(*) from (" + ret+ ") as tablea";
		} else {
			ret = processFilterSql(sql, (String[])null);
		}
		
		if (debug)
		    System.out.println("exit GenericTableFilter.addFilterCountSql getSql = "+ret);

		return ret;
		
	}
	
    private String processFilterSql(String inputSql, String[] colName) {
	    if (!hasFilter())
		return inputSql;

	    if (debug)
		System.out.println("enter GenericTableFilter.processFilterSql sql = "+inputSql);

		String currentSql = inputSql.toLowerCase();
		String before = "";
		String after = "";
		// get part inside ... () as tablea
		if (currentSql.endsWith(") as tablea")) {
		    after = ") as tablea";

		    int index = currentSql.indexOf("from");
		    if (-1 == index)
			return null;
		    before = inputSql.substring(0, index+4)+" (";
		    currentSql = inputSql.substring(index+4);
		    index = currentSql.indexOf("(");
		    currentSql = currentSql.substring(index+1);
		    currentSql = currentSql.replace(after, "");
		} else
		    currentSql = inputSql;

		Pattern p1 = Pattern.compile("order by");
		String[] items = p1.split(currentSql);
		int iSize = items.length;
		Matcher matcher = p1.matcher(currentSql);
		if (matcher.find()) {
		    after = " ORDER BY " + items[iSize-1] + after;
		}

		p1 = Pattern.compile("union");
		items = p1.split(items[0]);
		iSize = items.length;
		int i = 0;
		String str = null;
		String sql = null;

		if (1 == iSize) {
		    // remove most out ()
		    currentSql = items[0].trim();
		    sql = currentSql.trim();
		    int num = 0;
		    while (0 == sql.indexOf("(") && sql.endsWith(")")) { 
			// within brackets
			num++;
			sql = sql.substring(1);
			sql = sql.substring(0, sql.length() - 1);
		    }
		    sql = addFilterSql1(sql, colName);
		    if (0 < num)
			sql = "("+sql+")";
		} else {
		    sql = processFilterSql(items[0], colName);
		    for (i = 1; i < iSize; i++) {
			str = processFilterSql(items[i], colName);
			sql = sql + " UNION "+str;
		    }
		}
		
		String ret = before + sql + after;
		
		if (debug)
		    System.out.println("exit GenericTableFilter.processFilterSql getSql = "+ret);

		return ret;
	}

private String addFilterSql1(String currentSql, String[] colName) {
	    if (debug)
		System.out.println("enter GenericTableFilter.addFilterSql1 sql = "+currentSql);

	    if (null == currentSql || currentSql.trim().equals(""))
		return null;

		Pattern patternCountStar = Pattern.compile("^(?i)\\s*select\\s* count\\s*\\(\\s*\\*\\s*\\)\\s*from\\s*\\(\\s*(.*)\\)");
		/*
		Pattern pattern = Pattern.compile("^(?i)\\(*\\s*select\\s*(count\\s*\\()?\\s*(distinct)?\\s*(.*?)from.*");
		Pattern pattern = Pattern.compile("^(?i)\\(*\\s*select\\s*(count\\s*\\()?\\s*(distinct)?\\s*(.*)");
		Pattern patternAliasColName = Pattern.compile("^(?i).+?\\s+(.+)");
		Pattern patternAliasColNameComplex = Pattern.compile("^(?i).+\\)\\s+([^\\)]+)$");
		*/

		    Matcher matcher = patternCountStar.matcher(currentSql);
		    String countStarSql = null;
		    String sql = null;

		    if (matcher.find()) {
			String coreSql = matcher.group(1);
			if (debug)
			    System.out.println("GenericTableFilter.addFilterSql1 find match select count coreSql = " + coreSql);
			countStarSql = currentSql.replace(coreSql, "#@#@#"); 
			sql = coreSql.trim();
			if (sql.indexOf("(") == 0) {
			    sql = sql.substring(1, sql.length()-1);
			    countStarSql = currentSql.replace(coreSql, "("+"#@#@#"+")");
			}
		    } else {
			sql = currentSql;
		    }
		
		    String worker = getFilterSql(sql, colName);
		    sql = primaryFilterSql(sql, worker);
		    if (countStarSql != null) {
			countStarSql = countStarSql.replace("#@#@#", sql);
			sql = countStarSql;
		    }

		if (debug)
		    System.out.println("exit GenericTableFilter.addFilterSql1 getSql = "+sql);

		return sql;
	}

    public String addFilterSql(String currentSql, String[] colNames) {
	    if (!hasFilter())
		return currentSql;

	    return processFilterSql(currentSql, colNames);
    }

    private String primaryFilterSql(String currentSql, String inputFilterSql) {
	if (null == currentSql  || currentSql.trim().equals(""))
	    return null;

	if (null == inputFilterSql  || inputFilterSql.trim().equals(""))
		return currentSql;

		String filterSql = " AND " + inputFilterSql;

		if (debug)
		System.out.println("enter GenericTableFilter.primaryFilterSql sql = "+currentSql+" filterSql = "+filterSql);
		
		// use regex to build filter is not the simple way to do things - too many variations
		// should create new statement with placeholder for filters
		Pattern p1 = Pattern.compile("WHERE");
		String[] items1 = p1.split(currentSql);
		if (items1.length > 1)
		{
			String wclause = items1[1]; // the last where clause split
			if (wclause.contains("OR"))
			{
				Pattern p2 = Pattern.compile("ORDER BY");				
				String[] items2 = p2.split(wclause);
				if (items2.length > 1){
					wclause = "(" + items2[0] + filterSql + ") ORDER BY " + items2[1];
					currentSql = items1[0] + " WHERE " + wclause;
//					System.out.println("++++++++++current sql = "+currentSql);
				}
				
				Pattern p4 = Pattern.compile("order by");				
				String[] items4 = p4.split(wclause);
				if (items2.length > 1){
					wclause = "(" + items4[0] + filterSql + ") ORDER BY " + items4[1];
					currentSql = items4[0] + " WHERE " + wclause;
//					System.out.println("++++++++++current sql = "+currentSql);
				}
				
				Pattern p3 = Pattern.compile("GROUP BY");
				String[] items3 = p3.split(wclause);
				if (items3.length > 1){
					wclause = "(" + items3[0] + filterSql + ") GROUP BY " + items3[1];
					currentSql = items1[0] + " WHERE " + wclause;
//					System.out.println("++++++++++current sql = "+currentSql);
				}

			}
		}

		String ret = "";
		if (currentSql.matches("(?i).*\\)\\s*\\)\\s*GROUP\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
			ret = currentSql.replaceFirst("(?i)\\)\\s*GROUP\\sBY\\s",  filterSql + ") GROUP BY ");
		}
		else if (currentSql.matches("(?i).*\\sGROUP\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
//			System.out.println("+++Filter sql2====="+filterSql);			
			ret = currentSql.replaceFirst("(?i)\\sGROUP\\sBY\\s",  filterSql + " GROUP BY ");
		}		
		else if (currentSql.matches("(?i).*\\)\\s*\\)\\s*\\)\\s*ORDER\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
//			System.out.println("+++Filter sql1====="+filterSql);			
			ret = currentSql.replaceFirst("(?i)\\)\\s*ORDER\\sBY\\s",  ") " + filterSql + " ORDER BY ");
		}
		else if (currentSql.matches("(?i).*\\)\\s*\\)\\s*ORDER\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
			ret = currentSql.replaceFirst("(?i)\\)\\s*ORDER\\sBY\\s",  filterSql + ") ORDER BY ");
		}
		else if (currentSql.matches("(?i).*\\sORDER\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
			ret = currentSql.replaceFirst("(?i)\\sORDER\\sBY\\s",  filterSql + " ORDER BY ");
		}

		if (ret.equals("")) {
			    ret = currentSql +" "+filterSql;
		}

		if (debug) 
		    System.out.println("exit GenericTableFilter.primaryFilterSql  getSql = "+ret);

		return  ret;
	}
	
	private String extractSqlName(String name) {	
		// this is to ignore any existing IF in the column name and extract the alias name 
		if (name.matches("\\s*IF\\s*\\(.*")) 
			return name.substring(name.lastIndexOf(')')+1).trim();
		
		return name;
	}

	public int[] getTableToSqlColMap() {
		return tableToSqlColMap;
	}

	public void setTableToSqlColMap(int[] tableToSqlColMap) {
		this.tableToSqlColMap = tableToSqlColMap;
	}
	
	public String getActiveAssay()
	{
		String result = null;
		for(FilterItem filter: filters.values()){
			if (filter.getName() == "Assay Type" && filter.isActive() == true)
				result = filter.getValue1();
		}
		
//		System.out.println("~~~getAssayActive = "+ result);
		return result;
	}
}
