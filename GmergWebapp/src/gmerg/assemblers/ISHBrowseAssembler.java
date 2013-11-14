/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.AdvancedSearchDBQuery;
import gmerg.db.DBHelper;
import gmerg.db.ISHDevDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.*;
import gmerg.utils.Utility;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;


/**
 * @author xingjun
 *
 */
public class ISHBrowseAssembler extends OffMemoryTableAssembler{
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

    public ISHBrowseAssembler() {
	if (debug)
	    System.out.println("ISHBrowseAssembler.constructor");

    }
	/**
	 * <p>modified by xingjun - 15/09/2009
	 *  - invoke method getAllSubmissionInsitu (renamed from getAllSubmissionISH) </p>
	 * @param column
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (null != cache && cache.isSameQuery(column, ascending, offset, num)) {
			if (debug)
			    System.out.println("ISHBrowseAssembler.retriveData data not changed");
			
			return cache.getData();
	    }
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		ArrayList browseSubmissions = ishDevDAO.getAllSubmissionInsitu(column, ascending, offset, num, filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDevDAO = null;
		
		/** ---return the value object---  */
		
//		System.out.println("============ BrowsIsh: Data retrieved")	;
		DataItem[][] ret = getTableDataFormatFromIshList(browseSubmissions);

		if (null == cache)
		    cache = new RetrieveDataCache();
		cache.setData(ret);
		cache.setColumn(column);
		cache.setAscending(ascending);
		cache.setOffset(offset);
		cache.setNum(num);
	
		return ret;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] retrieveTotals() {
	    // force new cache
	    cache = null;

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		String [] allColTotalsQueries = {
                "TOTAL_NUMBER_OF_GENE_SYMBOL",
                "TOTAL_NUMBER_OF_SUBMISSION",
                "TOTAL_NUMBER_OF_LAB",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE",
                "TOTAL_NUMBER_OF_ASSAY_TYPE",
                "TOTAL_NUMBER_OF_PROBE_NAME",
                "TOTAL_NUMBER_OF_THEILER_STAGE",
                "TOTAL_NUMBER_OF_GIVEN_STAGE",
                "TOTAL_NUMBER_OF_SEX",
                "TOTAL_NUMBER_OF_GENOTYPE",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
                "TOTAL_NUMBER_OF_IMAGE",
                };
		String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, filter);
		
		// convert to interger array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++) {
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
		}

		// return result
		return totalNumbers;
	}
	
	/**
	 * 
	 * @return
	 */
	public int retrieveNumberOfRows() {

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		int totalNumberOfSubmissions = ishDevDAO.getTotalNumberOfSubmissions(filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDevDAO = null;
		
		/** ---return the value---  */
		return totalNumberOfSubmissions;
	}	
	
	public HeaderItem[] createHeader() {
		return createHeaderForISHBrowseTable();
	}    

	/**
	 * @author xingjun
	 * 
	 * @param selectedSubmissions
	 * @return
	 */
	public boolean deleteSelectedSubmissions(String[] selectedSubmissions) {
		
		boolean submissionDeleted = false;
		
		// no selected submissions
		if (selectedSubmissions == null || selectedSubmissions.length == 0) {
			return submissionDeleted;
		}
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// delete
		submissionDeleted = ishEditDAO.deleteSelectedSubmission(selectedSubmissions);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionDeleted;
	}
	
	
	//************************************************************************************************
	// Utility Methods
	//************************************************************************************************

	static public void convertIshRowToDataItemFormat (DataItem[] formatedRow, String[] row) {

		// Gene
		formatedRow[ 0] = new DataItem(row[0], "", "gene.html?gene="+row[0], 10);					

		// GUDMAP entry details
		if("Microarray".equalsIgnoreCase(row[4])) 
			formatedRow[1] = new DataItem(row[1], "Click to view Samples page","mic_submission.html?id="+row[0], 10);			//sub id
		else if ("ISH".equalsIgnoreCase(row[4]) || "ISH control".equalsIgnoreCase(row[4])  || "IHC".equalsIgnoreCase(row[4]) || "OPT".equalsIgnoreCase(row[4])) 
			formatedRow[1] = new DataItem(row[1], row[1], "ish_submission.html?id="+row[1], 10);	  			// Id
		else if ("TG".equalsIgnoreCase(row[4])) // added by xingjun - 27/08/2008 - transgenic data 
			formatedRow[1] = new DataItem(row[1], row[1], "ish_submission.html?id="+row[1], 10);	  			// Id
		else
			formatedRow[1] = new DataItem(row[10]);	// Id

		// Source
		formatedRow[ 2] = new DataItem(row[2], "Source details", "lab_detail.html?id="+row[1], 6, 251, 500);	

		// Submission Date
		formatedRow[ 3] = new DataItem(Utility.convertToDisplayDate(row[3]));	

		// Assay Type
		formatedRow[ 4] = new DataItem(row[4]);

		// Probe Name
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")){
			if ("IHC".equalsIgnoreCase(row[4]))
				formatedRow[ 5] = new DataItem(row[5], "Antibody Details", "antibody.html?antibody="+row[5], 10);	
			else
				formatedRow[ 5] = new DataItem(row[5], "Probe Details", "probe.html?probe="+row[5], 10);
		}
		
		// Theiler Stage
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) 
		    formatedRow[ 6] = new DataItem(row[6], "", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+row[6]+"definition.html", 10);								// Theiler Stage
		else 
		    formatedRow[ 6] = new DataItem(row[6]);  
		
		// Age
		formatedRow[ 7] = new DataItem(row[7]);	
		
		// Sex
		formatedRow[ 8] = new DataItem(row[8]);
		
		// Genotype
		formatedRow[9] = new DataItem(row[9]);

		// In Situ Expression
//		formatedRow[ 10] = new DataItem(row[10]);

		// Specimen Type
		formatedRow[ 10] = new DataItem(row[10]);
		
		// Image
		if(row[11] == null || row[11].trim().equals(""))
			formatedRow[11] = new DataItem("");	// microarrays don't have thumbnails to display
		else if(row[4].equals("OPT")) {
			formatedRow[11] = new DataItem(row[11].substring(0,row[11].lastIndexOf("."))+".jpg", "Click to see submission details for "+ row[1], "ish_submission.html?id="+row[1], 13); // thumbnail (only for OPT)
		}
		else
			formatedRow[11] = new DataItem(row[11], "Click to see submission details for "+ row[1], "ish_submission.html?id="+row[1], 13);	// thumbnail
		
	}

	/********************************************************************************
	 * Returns a 2D array of DataItems populated by data in a list of ISH submissions 
	 *
	 */
	public static DataItem[][] getTableDataFormatFromIshList(ArrayList subs) {
		if (subs==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])subs.get(0)).length;
		int rowNum = subs.size();
		
//		System.out.println("ISH Assembler retrieved rows="+rowNum);		
		    
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++)
			convertIshRowToDataItemFormat(tableData[i], (String[])subs.get(i));
			
		return tableData;
	 }

	static public HeaderItem[] createHeaderForISHBrowseTable() {	
		 String headerTitles[] = AdvancedSearchDBQuery.getISHDefaultTitle();
		 
	     boolean[] headerSortable = null;

	     headerSortable = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, false};
	     
		 int colNum = headerTitles.length;

		 HeaderItem[] tableHeader = new HeaderItem[colNum];
		 for(int i=0; i<colNum; i++)
			 tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		 
		 return tableHeader;
	}    

	static public HeaderItem[] createHeaderForISHEditBrowseTable() {	
		 String headerTitles[] = AdvancedSearchDBQuery.getISHEditDefaultTitle();
		 
	     boolean[] headerSortable = null;

	     headerSortable = new boolean[]{true, true, true, true, true, true, true, true, false, true, true, true, false, true};
	     
		 int colNum = headerTitles.length;
		 
		 
		 HeaderItem[] tableHeader = new HeaderItem[colNum];
		 for(int i=0; i<colNum; i++)
			 tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		 
 		 return tableHeader;
	} 
	

	public static DataItem[][] getTableDataFormatFromIshList(ArrayList subs, String privilege) {
		if (subs==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])subs.get(0)).length;
		int rowNum = subs.size();
	    if(null != privilege && Integer.parseInt(privilege)>=3) {
	    	colNum = colNum + 1;
	    }
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++)
			convertIshRowToDataItemFormat(tableData[i], (String[])subs.get(i), privilege);
			
		return tableData;
	}
	
	/**
	 * <p>modified by xingjun - 20/11/2009 - renamed argument name from userRole to userPrivilege</p>
	 */
	static public void convertIshRowToDataItemFormat (DataItem[] formatedRow, String[] row, String userPrivilege) {
		convertIshRowToDataItemFormat(formatedRow, row);
		if(null != row[13]) {
			if(row[13].equalsIgnoreCase("5")) {
				//formatedRow[13] = new DataItem("Awaiting Annotation", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("19")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("20")) {
				//formatedRow[13] = new DataItem("Completely Annotated by Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("21")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Sr. Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("22")) {
				//formatedRow[13] = new DataItem("Completely Annotated by Sr. Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("23")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Editor", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("24")) {
				//formatedRow[13] = new DataItem("Completely Annotated by Editor", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("25")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Sr. Editor", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("4")) {
				//formatedRow[13] = new DataItem("Public");	// thumbnail
				formatedRow[13] = new DataItem("View Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			}
		}		
	}
	
}
