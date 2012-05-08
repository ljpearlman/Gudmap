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
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Bernie 10/5/2010 Mantis 328 - modified to allow generic filters
public class GenericTableFilter {
	boolean active;
	TreeMap<Integer, FilterItem> filters;
	int[] tableToSqlColMap;
	// Bernie 02/03/2012 - (mantis 620) - added QIC_SPN_SEX & QMC_SPN_SEX to filter choice
	String[] insituMap = {"QIC_RPR_SYMBOL","#","QIC_ASSAY_TYPE","QIC_EXP_STRENGTH","#","#","QIC_SUB_EMBRYO_STG","7","QIC_SPN_SEX","QIC_PER_NAME","QIC_SUB_SUB_DATE","QIC_SPN_ASSAY_TYPE","#","#","#"};
	String[] microarrayMap = {"MBC_GNF_SYMBOL","#","#","#","#","#","MBC_SUB_EMBRYO_STG","#","QMC_SPN_SEX","MBC_PER_NAME","MBC_SUB_SUB_DATE","MBC_SPN_ASSAY_TYPE","#","#","#"};
	// Bernie 08/03/2012 - (Mantis 364) - allow expression filtering
	String[] microarrayMapQuery = {"#","#","#","#","#","#","QMC_SUB_EMBRYO_STG","#","QMC_SPN_SEX","QMC_PER_NAME","QMC_SUB_SUB_DATE","QMC_SPN_ASSAY_TYPE","#","#","#"};
			
	public GenericTableFilter() {
		active = false;
		filters = new TreeMap<Integer, FilterItem>();
		tableToSqlColMap = null;
	}

	public void addFilter(FilterItem filter) {
		filters.put(filter.getCol(), filter);
	}

	public void addFilter(int col, FilterItem filter) {
		filter.setCol(col);
		filters.put(col, filter);
	}

	public void setFilterTitles(HeaderItem[] header) {
		for(FilterItem filter: filters.values()) 
			filter.setName(header[filter.getCol()].getTitle());
	}
	
	public TreeMap<Integer, FilterItem> getFilters() {
		return filters;
	}
	
	public FilterItem getFilter(int col) {
		return filters.get(col);
	}
	
	// Bernie 10/8/2010 Mantis 328 - refactor
	private String getSql(String currentSql, String[] columnNames) {
//		System.out.println("****	GenericTableFilter:getSQL(String currentSql, String[] columnNames)");	

		String sql = "";
		for(FilterItem filter: filters.values()){
			//System.out.println(filter.getCol()+"FFFFFFFFFFFFFFFFFFFF=="+filter.getName()+"~~~~~~~~~~"+filter.isActive());				
			
			if (filter.isActive()) {
				String colName = "";
				
				if (tableToSqlColMap == null){
					colName = extractSqlName(columnNames[filter.getCol()]);
				}
				else{
					int col = filter.getCol();
					if (currentSql.contains("QIC_RPR_SYMBOL")){
						colName = insituMap[col];
					}
					if (currentSql.contains("MBC_GNF_SYMBOL")){
						colName = microarrayMap[col];
					}
					// Bernie 08/03/2012 - (Mantis 364) - allow expression filtering
					if (currentSql.contains("QMC_SPN_ASSAY_TYPE")){
						colName = microarrayMapQuery[col];
					}
					
				}
				
				String filterSql = "";
				if(!colName.equalsIgnoreCase("#"))
					filterSql = filter.getSql(colName);
				
				if (filterSql != "")
					sql += ((sql.equals(""))? "" : " AND ") + filterSql;
			}
		}
		
//		System.out.println("~~~~getSql = "+sql);
		return sql;
	}

	// Bernie 10/5/2010 Mantis 328 - added function
	public String addFilterCountSql(String sql) {
//		System.out.println("GenericTableFilter:addFilterCountSql(String sql)");
		
		// strip out the count part of the sql statement
		
		Pattern patternCountStar = Pattern.compile("^(?i)\\s*select\\s* count\\s*\\(\\s*\\*\\s*\\)\\s*from\\s*\\(\\s*(.*)\\)");
		Pattern patternTrimBrackets = Pattern.compile("^(?i)\\s*\\(\\s*(.*)\\s*\\)\\s*");

//		Matcher m = patternTrimBrackets.matcher(sql);
//		if (m.find()){
//			System.out.println("GenericTableFilter:addFilterSql matcher.group(1) trimmed = "+m.group(1));
//		}

		String CountSql = "";
		
		Matcher matchcount = patternCountStar.matcher(sql);
		if (matchcount.find()) {
			String newsql = matchcount.group(1);
			
			CountSql = "select count(*) from (" + addFilterSql(newsql) + ") as tablea";
			return CountSql;
		}
		else{
			return addFilterSql(sql);
		}
		
		
	}
	
	// Bernie 10/5/2010 Mantis 328 - added function
	// xingjun - 30/08/2011 - for sql with count(*) clause there is no 'order by' clause so need to check before append 'ORDER BY' clause to the sql
	public String addFilterSql(String sql) {
//		System.out.println("GenericTableFilter:addFilterSql(String sql)");
		if (filters == null || filters.size()==0)
			return sql;
		
		Boolean counter = false;

		Pattern patternComplexQuery = Pattern.compile("^(?i)\\s*\\(\\s*(select\\s*.*)\\s*\\)\\s*(union)\\s*(\\(\\s*select\\s*.*\\s*\\))(.*)");
		Pattern patternTrimBrackets = Pattern.compile("^(?i)\\s*\\(\\s*(.*)\\s*\\)\\s*");
		
		Matcher matcher = patternComplexQuery.matcher(sql);
		if (matcher.find()) {
//			System.out.println("^^^^^^GenericTableFilter:addFilterSql matcher succeeded");
//			System.out.println("GenericTableFilter:addFilterSql matcher.group(1) = "+matcher.group(1));
//			System.out.println("GenericTableFilter:addFilterSql matcher.group(2) = "+matcher.group(2));
//			System.out.println("GenericTableFilter:addFilterSql matcher.group(3) = "+matcher.group(3));
//			System.out.println("GenericTableFilter:addFilterSql matcher.group(4) = "+matcher.group(4));
			
			// split the sql up into manageable groups via the union keyword
//			Pattern p1 = Pattern.compile("order");
			Pattern p1 = Pattern.compile("order by");
			Pattern p2 = Pattern.compile("union");
			int i;
			String[] items1 = p1.split(sql);
//			String lastPart = "ORDER" + items1[items1.length-1];// get the last portion of the split
			// xingjun - 30/08/2011 - start
			String lastPart = "";
			matcher = p1.matcher(sql);
			if (matcher.find()) {
				lastPart = "ORDER BY" + items1[items1.length-1];// get the last portion of the split
			}
			// xingjun - 30/08/2011 - end
			
			//String lastPart = matcher.group(4); //"ORDER " + items1[1];
			String[] items2 = p2.split(items1[0]);


			String newSql = "";
			
			for(i=0; i<items2.length; i++){
				matcher = patternTrimBrackets.matcher(items2[i]);
				if (matcher.find()) { 				
					sql = matcher.group(1);	
					newSql +=  "("+ addFilterSql1(sql)+ ")";
					if (i < items2.length-1 )
						newSql += " union ";
					else
						newSql += " " +lastPart;
				}				
			}			
			return newSql;								
		}
		else{
			matcher = patternTrimBrackets.matcher(sql);
			
			//Bernie 12/03/2012 - (mantis 625) fix for sort feature
			Pattern p1 = Pattern.compile("order by");
			int i;
			String lastPart = "";
			String[] items1 = p1.split(sql);
			if (matcher.find()){
				if (items1.length > 1){
					lastPart = "ORDER BY " + items1[items1.length-1];
					sql = items1[0];
				}
			}
//			System.out.println("addFilterSql:firstPart = " + items1[0]);
//			System.out.println("addFilterSql:lastPart = " + lastPart);

			
			if (matcher.find()){
				sql = matcher.group(1);	
				String r1 = "(" + addFilterSql1(sql) + ") ";
//				System.out.println("addFilterSql:r1 = "+r1 + lastPart);
				return r1 + lastPart;
			}
			else{
				// Bernie 13/03/2012 - (mantis 637 & mantis 626) fix for filter on anatomy query
				matcher = patternTrimBrackets.matcher(sql);
				if (matcher.find()){
					sql = matcher.group(1);
//					System.out.println("addFilterSql:r21 = "+sql);
					//sql += " " + lastPart;
				}
				
//				System.out.println("addFilterSql:r22 = "+sql);
				String r3 = addFilterSql1(sql) + " " + lastPart;
//				System.out.println("addFilterSql:r3 = "+r3);
				return r3;
			}
		}
	}

//	public String addFilterSql(String sql) {
//		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\nGenericTableFilter:addFilterSql sql = "+sql);
//		if (filters == null || filters.size()==0)
//			return sql;
//
//		Pattern patternComplexQuery = Pattern.compile("^(?i)\\s*\\(\\s*(select\\s*.*)\\s*\\)\\s*(union)\\s*(\\(\\s*select\\s*.*\\s*\\))(.*)");
//		Pattern patternTrimBrackets = Pattern.compile("^(?i)\\s*\\(\\s*(.*)\\s*\\)\\s*");
//
//		
//		Matcher matcher = patternComplexQuery.matcher(sql);
//		if (matcher.find()) {
////			System.out.println("GenericTableFilter:addFilterSql matcher.group(1) = "+matcher.group(1));
////			System.out.println("GenericTableFilter:addFilterSql matcher.group(2) = "+matcher.group(2));
////			System.out.println("GenericTableFilter:addFilterSql matcher.group(3) = "+matcher.group(3));
////			System.out.println("GenericTableFilter:addFilterSql matcher.group(4) = "+matcher.group(4));
//
//			String newSql = "";
//			String lastPart = matcher.group(4);
//			System.out.println("GenericTableFilter:addFilterSql lastPart = "+ lastPart);
//			while (matcher.find(0)) {
//				newSql +=  "("+ addFilterSql1(matcher.group(1))+ ") " + matcher.group(2) + " ";
////				System.out.println("GenericTableFilter:addFilterSql newSql0 = "+ newSql);
//				sql = matcher.group(3);
////				System.out.println("GenericTableFilter:addFilterSql sql0 = "+ sql);
//				matcher = patternComplexQuery.matcher(sql);
//			}
//			matcher = patternTrimBrackets.matcher(sql);
//			if (matcher.find()) { 
//				sql = matcher.group(1);	
////				System.out.println("GenericTableFilter:addFilterSql sql1 = "+ sql);
//				String r1 = newSql + " (" + addFilterSql1(sql) + ") " + lastPart;
////				System.out.println("GenericTableFilter:addFilterSql sql out1 = r1");
//				return r1;
//			}
//			else{
//				String r2 = newSql + addFilterSql1(sql) + lastPart;
////				System.out.println("GenericTableFilter:addFilterSql sql out2 = r2");
//				return r2;
//			}
//		}
//		else{
//			String r3 = addFilterSql1(sql);
////			System.out.println("GenericTableFilter:addFilterSql sql out3 = r3");
//			return r3;
//		}
//	}

	
	// Bernie 10/8/2010 Mantis 328 - refactor
	private String addFilterSql1(String currentSql) {
//		System.out.println("GenericTableFilter:addFilterSql1(String currentSql)");
		Pattern patternCountStar = Pattern.compile("^(?i)\\s*select\\s* count\\s*\\(\\s*\\*\\s*\\)\\s*from\\s*\\(\\s*(.*)\\)");
//		Pattern pattern = Pattern.compile("^(?i)\\(*\\s*select\\s*(count\\s*\\()?\\s*(distinct)?\\s*(.*?)from.*");
		Pattern pattern = Pattern.compile("^(?i)\\(*\\s*select\\s*(count\\s*\\()?\\s*(distinct)?\\s*(.*)");
		Pattern patternAliasColName = Pattern.compile("^(?i).+?\\s+(.+)");
		Pattern patternAliasColNameComplex = Pattern.compile("^(?i).+\\)\\s+([^\\)]+)$");
		
		Matcher matcher = patternCountStar.matcher(currentSql);
		String sql;
		String countStarSql = null;
		if (matcher.find()) {
			String coreSql = matcher.group(1);
//			System.out.println("** addFilterSql1 --- coreSql = " + coreSql);
			countStarSql = currentSql.replace(coreSql, "#@#@#"); 
			sql = coreSql.trim();
			if (sql.indexOf("(") == 0) {
				sql = sql.substring(1, sql.length()-1);
				countStarSql = currentSql.replace(coreSql, "("+"#@#@#"+")");
			}
		}
		else
			sql = currentSql;
				
		if (countStarSql != null) {
			sql = addFilterSql(sql, null);
			countStarSql = countStarSql.replace("#@#@#", sql);
			return countStarSql;
		}
		return addFilterSql(currentSql, null);	
	}

	public String addFilterSql(String currentSql, String[] colNames) {
//		System.out.println("GenericTableFilter:addFilterSql = " +currentSql);		
		String filterSql = getSql(currentSql,colNames);
		if (filterSql==null || filterSql.equals(""))
			return currentSql;
	
		filterSql = " AND " + filterSql;
		
		// Bernie 28/03/2012 Mantis 640 - a hack to get filter working for WHERE clause with lots of OR statements when adding an AND statement
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

		//Bernie 26/05/2011 
		String sql1 = null;
		if (currentSql.matches("(?i).*\\)\\s*\\)\\s*GROUP\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
			sql1 = currentSql.replaceFirst("(?i)\\)\\s*GROUP\\sBY\\s",  filterSql + ") GROUP BY ");
//			System.out.println("+++sql1====="+sql1);
			return sql1;
		}
		else if (currentSql.matches("(?i).*\\sGROUP\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
//			System.out.println("+++Filter sql2====="+filterSql);			
			sql1 = currentSql.replaceFirst("(?i)\\sGROUP\\sBY\\s",  filterSql + " GROUP BY ");
//			System.out.println("+++sql1====="+sql1);
			return sql1;
		}		
		else if (currentSql.matches("(?i).*\\)\\s*\\)\\s*\\)\\s*ORDER\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
//			System.out.println("+++Filter sql1====="+filterSql);			
			sql1 = currentSql.replaceFirst("(?i)\\)\\s*ORDER\\sBY\\s",  ") " + filterSql + " ORDER BY ");
//			System.out.println("+++sql1====="+sql1);
			return sql1;
		}
		else if (currentSql.matches("(?i).*\\)\\s*\\)\\s*ORDER\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
			sql1 = currentSql.replaceFirst("(?i)\\)\\s*ORDER\\sBY\\s",  filterSql + ") ORDER BY ");
//			System.out.println("+++sql1====="+sql1);
			return sql1;
		}
		else if (currentSql.matches("(?i).*\\sORDER\\sBY\\s.*")) {
			filterSql=filterSql.replaceAll("\\$","\\\\\\$");	// This is to avoid exception being thrown by java Matcher
			sql1 = currentSql.replaceFirst("(?i)\\sORDER\\sBY\\s",  filterSql + " ORDER BY ");
//			System.out.println("+++sql1====="+sql1);
			return sql1;
		}

		
		String newSql = currentSql + filterSql;
//		System.out.println("newSql ====="+newSql);
		return  newSql;
	}
	
	private String extractSqlName(String name) {	
		// this is to ignore any existing IF in the column name and extract the alias name 
		if (name.matches("\\s*IF\\s*\\(.*")) 
			return name.substring(name.lastIndexOf(')')+1).trim();
		
		return name;
	}

	// Bernie 10/5/2010 Mantis 328 - added function
	public int[] getTableToSqlColMap() {
		return tableToSqlColMap;
	}

	// Bernie 10/5/2010 Mantis 328 - added function
	public void setTableToSqlColMap(int[] tableToSqlColMap) {
		this.tableToSqlColMap = tableToSqlColMap;
	}
	
	// Bernie 10/5/2010 Mantis 328 - added function
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
	

	
/*	
	public ParamQuery addFilterSql(ParamQuery query, String[] colNames) {
        String queryString = query.getQuerySQL();
		queryString = addFilterSql(queryString, DBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
		query.setQuerySQL(queryString);
		return query;
	}
*/	
}
