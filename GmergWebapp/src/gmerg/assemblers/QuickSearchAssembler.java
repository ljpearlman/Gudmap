package gmerg.assemblers;

import gmerg.db.AdvancedQueryDAO;
import gmerg.db.AdvancedSearchDBQuery;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;

import gmerg.utils.Utility;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Arrays;

public class QuickSearchAssembler extends OffMemoryTableAssembler {
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

	String[] input;
	String query;
	
	public QuickSearchAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("QuickSearchAssembler.constructor");

	}

	public void setParams() {
	if (debug)
	    System.out.println("QuickSearchAssembler.setParams");

		super.setParams();
		input = getParams("input");
		query = getParam("query");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	if (debug)
	    System.out.println("QuickSearchAssembler.retrieveData");

		if(input == null || input[0] == null || input[0].equals("")) 
			return null;

		if (null != cache &&
		    cache.isSameQuery(column, ascending, offset, num)) {
		    if (debug)
			System.out.println("QuickSearchAssembler.retriveData data not changed");
		    
		    return cache.getData();
		}		

		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedQDAO;
		ArrayList list = new ArrayList();
		try{
			advancedQDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
			
			String type = "All";
			int[] total = null;
	   	
			list = advancedQDAO.getQuickSearch(type, input, column, ascending, String.valueOf(offset), String.valueOf(num), total);
		}
		catch(Exception e){
			System.out.println("QuickSearchAssembler::retrieveData failed !!!");
			list = new ArrayList();
		}		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		advancedQDAO = null;
		
		DataItem[][] ret = getTableDataFormatFromArrayList(list);

		if (null == cache)
		    cache = new RetrieveDataCache();
		cache.setData(ret);
		cache.setColumn(column);
		cache.setAscending(ascending);
		cache.setOffset(offset);
		cache.setNum(num);

		return ret;
	}
	
	public int retrieveNumberOfRows() {
	if (debug)
	    System.out.println("QuickSearchAssembler.retrieveNumberOfRows");

		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO;
		int[] n = null;
		try{
			ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
			n = ishDAO.getQuickNumberOfRows(query, input);
		}
		catch(Exception e){
			System.out.println("QuickSearchAssembler::retrieveNumberOfRows failed !!!");
			n = null;
		}		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		if(null != n)
			return n[0];
        return 0;
	}
		
	/**
	 * @author xingjun
	 * @return
	 */
	public int[] retrieveNumberOfRowsInGroups() {
	if (debug)
	    System.out.println("QuickSearchAssembler.retrieveNumberOfRowsInGroups");

		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO ishDAO;
		int[] n = null;
		try{
			ishDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
			n = ishDAO.getQuickNumberOfRows(query, input);
		}
		catch(Exception e){
			System.out.println("QuickSearchAssembler::retrieveNumberOfRowsInGroups failed !!!");
			n = null;
		}		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		if(null != n) {
			return new int[]{n[1], n[2]};
		} 
        return new int[]{0,0};
	}
	
	public HeaderItem[] createHeader() {
		return createHeaderForSearchResultTable();
	}
	
	public static HeaderItem[] createHeaderForSearchResultTable() {

	    System.out.println("QuickSearchAssembler.createHeaderForSearchResultTable");

		 String headerTitles[] = AdvancedSearchDBQuery.getBothDefaultTitle();
		 boolean headerSortable[] = {true, true, true, true, true, true, true, true, true, true, true, true, false};
		 int colNum = headerTitles.length;
		 HeaderItem[] tableHeader = new HeaderItem[colNum];
		 for(int i=0; i<colNum; i++)
			 tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
 		 return tableHeader;
	}
	
	//Bernie 10/5/2011 - Mantis 328 - added method
	//Bernie 01/03/2012 - Mantis 620 - added SEX to filter choice
	public static int[] getTableviewToSqlColMap() {
		int[] map = {0, 9, 13, 2, 11, 1, 5, 7, 14, 3, 4, 6, 8};
		return map;
	}

	/**
	 * <p>xingjun 30/09/2010 - reduced the size for lab details popup window</p>
	 * <p>xingjun - 08/07/2011 - changed the link to the EMAP database: need to change to get the link from the database in the future </p>
	 */
	public static DataItem[][] getTableDataFormatFromArrayList(ArrayList list) {

	    System.out.println("QuickSearchAssembler.getTableDataFormatFromArrayList");

	    if (list == null){
			System.out.println("No data is retrieved");
			return null;
		}

	    Hashtable<String, String> geneExpressions = new Hashtable<String, String>();
	    geneExpressions.put("M", "Marginal");
	    geneExpressions.put("m", "Marginal");
	    geneExpressions.put("P", "Present");
	    geneExpressions.put("p", "Present");
	    geneExpressions.put("A", "Absent");
	    geneExpressions.put("a", "Absent");
		int colNum = ((String[])list.get(0)).length;
		
		int rowNum = list.size();
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		String[] row = null;
		String[] strArr = null;
		String str = null;
		int j = 0;
		for(int i=0; i<rowNum; i++) {	
			row = (String[])list.get(i); 
		
			tableData[i][0] = new DataItem(row[0], "Click to view gene page","gene.html?gene="+row[0], 10);	//gene		  	
			if(null != row[13] && row[13].equals("Microarray")) {
				tableData[i][1] = new DataItem(row[9], "Click to view Samples page","mic_submission.html?id="+row[9], 10);			//sub id
			} else if(null != row[13] && (row[13].equals("ISH")||row[13].equals("IHC") || row[13].equals("OPT") || row[13].equalsIgnoreCase("TG"))) {
				tableData[i][1] = new DataItem(row[9], "Click to view submission page","ish_submission.html?id="+row[9], 10);		//sub id
			}
			tableData[i][2] = new DataItem(row[13]);	//assay type
			tableData[i][3] = new DataItem(row[2]);		//ish exp
			tableData[i][4] = new DataItem(geneExpressions.get(row[11]));	//mic exp
			// to allow description popup window, and mouse over text
			if (null != row[1]) {
			    strArr = row[1].split(";");
			    if (3 < strArr.length) {
				Arrays.sort(strArr);
				str = strArr[0];
				for (j = 1; j < 3; j++)
				    str += "; "+strArr[j];
				str += " ...";
			    } else 
				str = row[1];
			}
			tableData[i][5] = new DataItem(str,row[1],30);		//tissue	
            if(Utility.getProject().equalsIgnoreCase("GUDMAP")) {
//                tableData[i][6] = new DataItem(row[5], "", "http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts"+row[5]+"/", 10);         //stage
                tableData[i][6] = new DataItem(row[5], "", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+row[5]+"definition.html", 10);         //stage
            }
            else {
                tableData[i][6] = new DataItem(row[5]);         //stage
            }
			
			tableData[i][7] = new DataItem(row[7]);		//age
			//Bernie - 01/03/2012 - (Mantis 619) added new column for sex
			tableData[i][8] = new DataItem(row[14]);	//sex	

			tableData[i][9] = new DataItem(row[3], "Source details", "lab_detail.html?id="+row[9], 6, 251, 500);		//source

			tableData[i][10] = new DataItem(row[4]);		//date
			tableData[i][11] = new DataItem(row[6]);	//spe type
			//			ycheng bug reported 20070723
			if(null != row[13] && row[13].equals("Microarray")) {
				tableData[i][12] = new DataItem("");
			} else if(null != row[13] && (row[13].equalsIgnoreCase("ISH")||row[13].equalsIgnoreCase("IHC") || row[13].equalsIgnoreCase("OPT") || row[13].equalsIgnoreCase("TG"))) {
				if(row[13].equals("OPT")) {
					tableData[i][12] = new DataItem(row[8].substring(0,row[8].lastIndexOf("."))+".jpg", "Click to see submission details for "+ row[9], "ish_submission.html?id="+row[9], 13); // thumbnail (only for OPT)
				}
				else {
					tableData[i][12] = new DataItem(row[8], "Click to see submission details for "+ row[9], "ish_submission.html?id="+row[9], 13); // thumbnail (only for ISH)
				}
				
			}
		}
		
		return tableData;
	}
	
}
