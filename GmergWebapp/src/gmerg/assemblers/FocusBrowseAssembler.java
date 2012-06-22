package gmerg.assemblers;

import gmerg.db.AdvancedSearchDBQuery;
import gmerg.db.DBHelper;
import gmerg.db.FocusForAllDAO;
import gmerg.db.ISHDevDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.FocusStageDAO;

import gmerg.utils.Utility;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xingjun
 * modified by xingjun - 15/01/2009 - add an extra param (gene)
 *
 */
public class FocusBrowseAssembler extends OffMemoryTableAssembler{
	
	String assayType;
	String[] organs;
	String stage;
	String gene;
	
    private boolean debug = false;

	public FocusBrowseAssembler () {
	      	if (debug)
	    System.out.println("FocusBrowseAssembler.constructor");

	}
	
	public FocusBrowseAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("FocusBrowseAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
		assayType = getParam("assayType");
		organs = getParams("organ");
		stage = getParam("stage");
		gene = getParam("gene");
	}
	
	/**
	 * <p>xingjun - 17/01/2009 - add gene parameter into the invocation method of the getting data</p>
	 */
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		// get data from database
		FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
//		ArrayList submissions =
//			focusForAllDAO.getFocusBrowseList(organs, column, ascending, assayType,
//					stage, String.valueOf(offset), String.valueOf(num));
		
		ArrayList submissions =
			focusForAllDAO.getFocusBrowseList(organs, column, ascending, assayType,
					stage, gene, String.valueOf(offset), String.valueOf(num), filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		focusForAllDAO = null;
		
		/** ---return the value object---  */
		if ("array".equals(getParam("assayType")))
			return getTableDataFormatFromArrayList(submissions);
		else
			return ISHBrowseAssembler.getTableDataFormatFromIshList(submissions);
	}
	
	/**
	 * modified by xingjun - 20/01/2009
	 * add gene parameter into the invocation method of the getting data
	 */
	public int retrieveNumberOfRows() {
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
		
        // get number of public ish sumbissions
//		int n = focusForAllDAO.getQuickNumberOfRows(assayType, organs, stage);
		int n = focusForAllDAO.getQuickNumberOfRows(assayType, organs, stage, gene, filter);

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		
		return n;
	}
	
	// Bernie - 09/11/2010 - added to provide Totals
	public int[] retrieveTotals() {
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
                "TOTAL_NUMBER_OF_PROBE_NAME",
                "TOTAL_NUMBER_OF_GENOTYPE",
                "TOTAL_NUMBER_OF_PROBE_TYPE",
                "TOTAL_NUMBER_OF_IMAGE",
                };
		String endingClause = " AND (SUB_ASSAY_TYPE = 'ISH') "; // Bernie 17/11/2010 - added endingClause to get correct totals
		String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);
		//String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, filter);
		
		// convert to integer array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++) {
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
		}

		// return result
		return totalNumbers;
	}
	

	public HeaderItem[] createHeader() {
		HeaderItem[] header = null;
		if ("ish".equalsIgnoreCase(assayType) || "insitu".equalsIgnoreCase(assayType)) {
			header = ISHBrowseAssembler.createHeaderForISHBrowseTable();
			if (stage!=null)
				header[2].setSortable(false);
		}
		if ("array".equalsIgnoreCase(assayType)) {
			header = createHeaderForArrayBrowseTable();
			if (stage!=null)
				header[2].setSortable(false);
		}
		return header;
	}	

	/**
	 * <p>xingjun - 07/12/2009 - added extra column Gene Reported</p>
	 * @return
	 */
	public static HeaderItem[] createHeaderForArrayBrowseTable() {
		 String[] headerTitles = { Utility.getProject()+" Entry Details", "Sample", 
				 				   Utility.getStageSeriesMed()+" Stage", "Age", 
				 				   "Lab", "Date", "Sex",
				 				   "Sample Description", "Title", "Gene Reported", "Series", "Component(s) sampled" };
		 
		int colNum = headerTitles.length;
		HeaderItem[] tableHeader = new HeaderItem[colNum];
		for(int i=0; i<colNum; i++)
			tableHeader[i] = new HeaderItem(headerTitles[i], true);
		return tableHeader;
	}

	public ArrayList[][] getStageList(String[] stage) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		FocusStageDAO focusStageDAO = MySQLDAOFactory.getFocusStageDAO(conn);
		
		// get data from database
		ArrayList[][] browseSeries = focusStageDAO.getStageList(stage);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		focusStageDAO = null;
		
		// return the value object
		return browseSeries;
	}
	
	/**
	 * @author xingjun - 10-09-2007
	 * <p>return submission summary information (in situ & microarray) categorised by stages</p>
	 */
	public String[][] getStageList(String[] stage, String organ) {
		
		/** create dao */
		Connection conn = DBHelper.getDBConnection();
		FocusStageDAO focusStageDAO = MySQLDAOFactory.getFocusStageDAO(conn);
		
		/** get data from database */
		// get insitu stage list
		String[][] stageList = focusStageDAO.getStageList(stage, organ);
		
		// get microarray stage list
		
		// get age (dpc) stage list
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		focusStageDAO = null;
		
		/** return the value object */
		return stageList;
	}
	
	/**
	 * @author xingjun - 28/01/2009
	 * 1. use separate dao methods to get stage list rather than using one single
	 *    dao method to get all data;
	 * 2. add gene criteria;
	 * 3. add age (dpc) column into result -----not finished yet. </p>
	 * @param stage
	 * @param organ
	 * @param symbol
	 * @return
	 */
	public String[][] getStageList(String[] stage, String organ, String symbol) {
		/** create dao */
		Connection conn = DBHelper.getDBConnection();
		FocusStageDAO focusStageDAO = MySQLDAOFactory.getFocusStageDAO(conn);
		
		/** get data from database */
		// get insitu stage list
		String[] insituStageList = 
			focusStageDAO.getStageList("insitu", stage, organ, symbol);
		
		// get microarray stage list
		String[] arrayStageList = 
			focusStageDAO.getStageList("Microarray", stage, organ, symbol);
		
		// get age (dpc) stage list
		int len = stage.length;
		String[] dpcStageList = new String[len];
		for (int i=0;i<len;i++) {
			dpcStageList[i] = focusStageDAO.getDpcStageValue(stage[i]);
		}
		
		// put them together
		String[][] stageLists = new String[len][3];
		for (int i=0;i<len;i++) {
			stageLists[i][0] = dpcStageList[i];
			stageLists[i][1] = insituStageList[i];
			stageLists[i][2] = arrayStageList[i];
		}
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		focusStageDAO = null;
		
		/** return the value object */
		return stageLists;
	}

	/**
	 * <p>xingjun - 07/12/2009 - added extra column Gene Reported and adjusted the display order</p>
	 * <p>xingjun - 30/09/2010 - reduced the size for lab details popup window</p>
	 * <p>xingjun - 08/07/2011 - changed the link to the EMAP database: need to change to get the link from the database in the future </p>
	 */
	public static DataItem[][] getTableDataFormatFromArrayList(ArrayList list) {
		if (list == null){
			System.out.println("No data is retrieved");
			return null;
		}
	
		int colNum = ((String[])list.get(0)).length;
		int rowNum = list.size();
		
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++) {
			String[] row = (String[])list.get(i); 
			tableData[i][0] = new DataItem(row[0], "Click to view Samples page","mic_submission.html?id="+row[0], 10 );	
			tableData[i][1] = new DataItem(row[1], "Click to view GEO page", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[1], 2);
			if(Utility.getProject().equalsIgnoreCase("GUDMAP")){
//				tableData[i][2] = new DataItem(row[2], "", "http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts"+row[2]+"/", 10);
				tableData[i][2] = new DataItem(row[2], "", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+row[2]+"definition.html", 10);
			}
			else {
				tableData[i][2] = new DataItem(row[2]);
			}
			tableData[i][3] = new DataItem(row[3]);
//			tableData[i][4] = new DataItem(row[4], "Lab details", "lab_detail.html?id="+row[0], 6, 300, 500);
			tableData[i][4] = new DataItem(row[4], "Lab details", "lab_detail.html?id="+row[0], 6, 251, 500);
			tableData[i][5] = new DataItem(row[5]);
			tableData[i][6] = new DataItem(row[6]);
			tableData[i][7] = new DataItem(row[7]);
			tableData[i][8] = new DataItem(row[8]);
//			tableData[i][9] = new DataItem(row[9], "Click to view GEO page", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[9], 2);
//			tableData[i][10] = new DataItem(row[10]);
			tableData[i][9] = new DataItem(row[11]); // Gene Reported
			tableData[i][10] = new DataItem(row[9], "Click to view GEO page", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[9], 2);
			tableData[i][11] = new DataItem(row[10]);
		}					   
		return tableData;
	}	
}
