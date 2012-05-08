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

import java.sql.Connection;
import java.util.ArrayList;


/**
 * @author xingjun
 *
 */
public class ISHBrowseAssembler extends OffMemoryTableAssembler{
	
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
		return getTableDataFormatFromIshList(browseSubmissions);
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] retrieveTotals() {

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION",
                "TOTAL_NUMBER_OF_GENE_SYMBOL",
                "TOTAL_NUMBER_OF_THEILER_STAGE",
                "TOTAL_NUMBER_OF_GIVEN_STAGE",
                "TOTAL_NUMBER_OF_LAB",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE",
                "TOTAL_NUMBER_OF_ASSAY_TYPE",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
                "TOTAL_NUMBER_OF_SEX",
//                "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL",
                "TOTAL_NUMBER_OF_PROBE_NAME",
//                "TOTAL_NUMBER_OF_ANTIBODY_NAME",
//                "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL",
                "TOTAL_NUMBER_OF_GENOTYPE",
                "TOTAL_NUMBER_OF_PROBE_TYPE",
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
	/**
	 * <p>xingjun - 06/10/2009 - added 'ISH control' criteria into the comparison of row[0]</p>
	 * <p>xingjun 30/09/2010 - reduced the size for lab details popup window</p>
	 * <p>xingjun - 08/07/2011 - changed the link to the EMAP database: need to change to get the link from the database in the future </p>
	 */

	static public void convertIshRowToDataItemFormat (DataItem[] formatedRow, String[] row) {
		
		if("Microarray".equalsIgnoreCase(row[6])) 
			formatedRow[0] = new DataItem(row[0], "Click to view Samples page","mic_submission.html?id="+row[0], 10);			//sub id
		else if ("ISH".equalsIgnoreCase(row[6]) || "ISH control".equalsIgnoreCase(row[6])  || "IHC".equalsIgnoreCase(row[6]) || "OPT".equalsIgnoreCase(row[6])) 
			formatedRow[0] = new DataItem(row[0], row[0], "ish_submission.html?id="+row[0], 10);	  			// Id
		else if ("TG".equalsIgnoreCase(row[6])) // added by xingjun - 27/08/2008 - transgenic data 
			formatedRow[0] = new DataItem(row[0], row[0], "ish_submission.html?id="+row[0], 10);	  			// Id
		else
			formatedRow[0] = new DataItem(row[0]);	  			// Id
				
		formatedRow[ 1] = new DataItem(row[1], "", "gene.html?gene="+row[1], 10);					// Gene Symbol
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) 
//		    formatedRow[ 2] = new DataItem(row[2], "", "http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts"+row[2]+"/", 10);								// Theiler Stage
		    formatedRow[ 2] = new DataItem(row[2], "", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+row[2]+"definition.html", 10);								// Theiler Stage
		else 
		    formatedRow[ 2] = new DataItem(row[2]);                                                                       // Theiler Stage
		formatedRow[ 3] = new DataItem(row[3]);										// Age
//		formatedRow[ 4] = new DataItem(row[4], "Lab details", "lab_detail.html?id="+row[0], 6, 300, 500);	        // Lab               
		formatedRow[ 4] = new DataItem(row[4], "Lab details", "lab_detail.html?id="+row[0], 6, 251, 500);	        // Lab               
		formatedRow[ 5] = new DataItem(Utility.convertToDisplayDate(row[5]));						// submission Date
		formatedRow[ 6] = new DataItem(row[6]);										// Assay Type
		formatedRow[ 7] = new DataItem(row[7]);										// Specimen Type
		formatedRow[ 8] = new DataItem(row[8]);															// Sex
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) 
			formatedRow[ 9] = new DataItem(row[9], "Probe Details", "probe.html?probe="+row[9], 10);																// Probe Name
		else 
			formatedRow[ 9] = new DataItem(row[9]);																// Probe Name
		formatedRow[10] = new DataItem(row[10]);															// genotype
		formatedRow[11] = new DataItem(row[11]);															// Probe Type
//		formatedRow[12] = new DataItem(row[12], row[0], row[0], 14);							// Thumbnail using openZoomViewer
//		formatedRow[12] = new DataItem(row[12], row[0].replace(':', '_'), "zoom_viewer.html?id="+row[0]+"&serialNo=1", 12, 835, 1150);	// Thumbnail
		if(row[12] == null || row[12].trim().equals(""))
			formatedRow[12] = new DataItem("");	// microarrays don't have thumbnails to display
		else if(row[6].equals("OPT")) {
			formatedRow[12] = new DataItem(row[12].substring(0,row[12].lastIndexOf("."))+".jpg", "Click to see submission details for "+ row[0], "ish_submission.html?id="+row[0], 13); // thumbnail (only for OPT)
		}
		else
			formatedRow[12] = new DataItem(row[12], "Click to see submission details for "+ row[0], "ish_submission.html?id="+row[0], 13);	// thumbnail
		
		
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
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("19")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("20")) {
				//formatedRow[13] = new DataItem("Completely Annotated by Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("21")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Sr. Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("22")) {
				//formatedRow[13] = new DataItem("Completely Annotated by Sr. Annotator", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("23")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Editor", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("24")) {
				//formatedRow[13] = new DataItem("Completely Annotated by Editor", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("25")) {
				//formatedRow[13] = new DataItem("Partially Annotated by Sr. Editor", "Click to open annotation window for "+ row[0], "openZoomViewer('"+row[0]+"', 'desktop1', '1');var w=window.open('ish_edit_expression.html?id="+row[0]+"','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');w.focus(); return false;", 40);	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail
			} else if(row[13].equalsIgnoreCase("4")) {
				//formatedRow[13] = new DataItem("Public");	// thumbnail
				formatedRow[13] = new DataItem("View/Edit Annotation", "Click to open annotation window for "+ row[0], "openDesktop1('"+row[0]+"');return false;", 40);	// thumbnail

			}/* else if(row[13].equalsIgnoreCase("0")) {
				formatedRow[13] = new DataItem("Editing in Progress");	// thumbnail

			}*/
		}		
	}
	
}
