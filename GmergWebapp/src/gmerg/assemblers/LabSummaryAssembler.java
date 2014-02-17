package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.FacesContext;

import gmerg.utils.RetrieveDataCache;
import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.FocusForAllDAO;
import gmerg.db.ISHDAO;
import gmerg.db.ISHDevDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.summary.LabSummary;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;


/**
 * @author xingjun
 *
 */
public class LabSummaryAssembler extends OffMemoryTableAssembler {
    String labId;
    String assayType;
    String submissionDate;	
    String privilege;	
    String isPublic;
    String tableType;
    boolean labIshEdit;
    String archiveId; // xingjun - 01/07/2011
    String batchId;
    
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;
    
    public LabSummaryAssembler () {
	if (debug)
	    System.out.println("LabSummaryAssembler.constructor");
	
    }
    
    public LabSummaryAssembler (HashMap params) {
	super(params);
	if (debug)
	    System.out.println("LabSummaryAssembler.constructor with param");
    }
    
    public void setParams() {
		super.setParams();
		labId = getParam("labId");
		assayType = getParam("assayType");
		submissionDate = getParam("submissionDate");
		privilege = getParam("isSubmitter");
		isPublic = getParam("isPublic");
		tableType = getParam("tableType");
		labIshEdit = (Boolean)this.params.get("labIshEdit");
		archiveId = getParam("archiveId");
		batchId = getParam("batchId");
    }
    
    /**
     * <p>modified by xingjun - 01/07/2011 - need to pass the archive id into the dao</p>
     */
    public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	if (null != cache && cache.isSameQuery(column, ascending, offset, num)) {
	    if (debug)
	    	System.out.println("LabSummaryAssembler.retriveData data not changed");
	    
	    return cache.getData();
	}
	
	DataItem[][] ret = null;
	
	if ("Microarray".equals(assayType)) {
	    /** ---get data from dao---  */
	    // create a dao
	    Connection conn = DBHelper.getDBConnection();
	    ArrayDAO arrayDAO;
	    ArrayList arrayBrowseSubmissions = null;
	    try{
		    arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		    
		    arrayBrowseSubmissions =
			arrayDAO.getSubmissionsByLabId(labId, submissionDate, archiveId, column, ascending, offset, num, batchId);
		    /** ---return the composite value object---  */
		    ret = FocusBrowseAssembler.getTableDataFormatFromArrayList(arrayBrowseSubmissions); 

		    if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(ret);
			cache.setColumn(column);
			cache.setAscending(ascending);
			cache.setOffset(offset);
			cache.setNum(num);
			
			return ret;
	    }
		catch(Exception e){
			System.out.println("LabSummaryAssembler:: microarray retrieveData failed !!!");
			arrayBrowseSubmissions = null;
		    /** ---return the composite value object---  */
		    ret = FocusBrowseAssembler.getTableDataFormatFromArrayList(arrayBrowseSubmissions); 

			return ret;
		}
	    finally{
		    // release db resources
		    DBHelper.closeJDBCConnection(conn);
		    arrayDAO = null;
	    }
	} else {
	    
	    //else they are ISH, IHC, or Tg data
	    
	    /** ---get data from dao---  */
	    // create a dao
	    Connection conn = DBHelper.getDBConnection();
	    ISHDAO ishDAO;
	    ArrayList ishBrowseSubmissions = null;
	    try{
		    ishDAO = MySQLDAOFactory.getISHDAO(conn);

		    //System.out.println("WAWAWA:"+FacesContext.getCurrentInstance().getViewRoot().getViewId());
		    if(null != privilege && Integer.parseInt(privilege)>=3 && labIshEdit) {
			ishBrowseSubmissions = 
			    ishDAO.getSubmissionsForAnnotationByLabId(labId, assayType, submissionDate, archiveId, column, ascending, offset, num, isPublic);
		    } else {		
			ishBrowseSubmissions = 
			    ishDAO.getSubmissionsByLabId(labId, assayType, submissionDate, archiveId, column, ascending, offset, num, batchId);
		    }
		    
		    /** ---return the composite value object---  */
		    if(null != privilege && Integer.parseInt(privilege)>=3 && labIshEdit)
			ret = ISHBrowseAssembler.getTableDataFormatFromIshList(ishBrowseSubmissions, privilege); 
		    else
			ret = ISHBrowseAssembler.getTableDataFormatFromIshList(ishBrowseSubmissions);

		    if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(ret);
			cache.setColumn(column);
			cache.setAscending(ascending);
			cache.setOffset(offset);
			cache.setNum(num);
			
			return ret;
	    }
		catch(Exception e){
			System.out.println("LabSummaryAssembler:: ISH, IHC, or Tg retrieveData failed !!!");
			ishBrowseSubmissions = null;
			
		    /** ---return the composite value object---  */
		    if(null != privilege && Integer.parseInt(privilege)>=3 && labIshEdit)
			ret = ISHBrowseAssembler.getTableDataFormatFromIshList(ishBrowseSubmissions, privilege); 
		    else
			ret = ISHBrowseAssembler.getTableDataFormatFromIshList(ishBrowseSubmissions);

			return ret;
		}
	    finally{
		    /** release the db resources */
		    DBHelper.closeJDBCConnection(conn);
		    ishDAO = null;
	    }
	}
	
    }
    
    /**
     * @author xingjun
     * <p>xingjun - 01/07/2011 - need to pass the archive id into the dao</p>
     */
    public int retrieveNumberOfRows() {
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		FocusForAllDAO focusForAllDAO;
		int numberOfRows = 0;
		try{
			focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
			if(null != privilege && Integer.parseInt(privilege)>=3 && FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/lab_ish_edit.jsp")) {
			    numberOfRows = focusForAllDAO.getNumberOfSubmissionsForLabForAnnotation(labId, assayType, submissionDate, archiveId, isPublic);
			} else {
			    numberOfRows = focusForAllDAO.getNumberOfSubmissionsForLab(labId, assayType, submissionDate, archiveId, batchId);
			}
			return numberOfRows;
		}
		catch(Exception e){
			System.out.println("LabSummaryAssembler::retrieveNumberOfRows !!!");
			return 0;
		}
		finally{
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			focusForAllDAO = null;
		}
    }
    
    // Bernie 25/3/2011 mod to return totals 
    public int[] retrieveTotals() {
		// force new cache 
		cache = null;
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO;
		String[][] columnNumbers = null;
		try{
			ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
			
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
			String endingClause = " AND (SUB_ASSAY_TYPE = 'TG') ";
			columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);

			// convert to interger array, each tuple consists of column index and the number
			int len = columnNumbers.length;
			int[] totalNumbers = new int[len];
			for (int i=0;i<len;i++) {
			    totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
			}
			
			// return result
			return totalNumbers;
		}
		catch(Exception e){
			System.out.println("LabSummaryAssembler::retrieveTotals !!!");
			return new int[0];
		}
		finally{
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDevDAO = null;		
		}
    }
    
    
    public HeaderItem[] createHeader() {
	if ("array".equalsIgnoreCase(tableType)) 
	    return FocusBrowseAssembler.createHeaderForArrayBrowseTable();
	if ("ishEdit".equalsIgnoreCase(tableType)) 
	    return ISHBrowseAssembler.createHeaderForISHEditBrowseTable();
	if ("ish".equalsIgnoreCase(tableType)) 
	    return ISHBrowseAssembler.createHeaderForISHBrowseTable();
	return null; 
    }
    
    /**
     *  
     * @param labId - the id of the submitting lab
     * @param date - the date of the submissions
     * @return totalSubmissions - the total number of ish submissions for the specified lab
     */
    public String getTotalISHSubmissionsForLab(String date) {
        String totalSubmissions = new String("");
	
        /** ---get data from dao--- */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        ISHDAO ishDAO;
        
        try{
	        ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
	        // get the value
	        totalSubmissions += ishDAO.getTotalNumberOfISHSubmissionsForLabQuery(labId, date);
	        /** return the value  */
	        return totalSubmissions;
        }
		catch(Exception e){
			System.out.println("LabSummaryAssembler::getTotalISHSubmissionsForLab !!!");
			totalSubmissions = new String("");
	        return totalSubmissions;
		}
        finally{
	        // release db resources
	        DBHelper.closeJDBCConnection(conn);
	        ishDAO = null;
        }
    }
    
    
    public String getTotalIHCSubmissionsForLab(String date) {
        String totalSubmissions = new String("");
	
        /** ---get data from dao--- */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        ISHDAO ishDAO;
        try{
	        ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
	        // get the value
	        totalSubmissions += ishDAO.getTotalNumberOfIHCSubmissionsForLabQuery(labId, date);
	        /** return the value  */
	        return totalSubmissions;
        }
		catch(Exception e){
			System.out.println("LabSummaryAssembler::getTotalIHCSubmissionsForLab !!!");
			totalSubmissions = new String("");
	        return totalSubmissions;
		}
        finally{
	        // release db resources
	        DBHelper.closeJDBCConnection(conn);
	        ishDAO = null;
        }
    }
    
    
    public LabSummary[] getSummaryData() {
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO;
		LabSummary[] labSummaries = null;
		try{
			ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			// get data
			// get lab name and id
			String[][] pi = ishDAO.findAllPIs();
			int piNumber = pi.length;
			
			//		System.out.println("pi name size: " + pi.length);
			//		System.out.println("=====================");
			//		for (int i=0;i<pi.length;i++) {
			//			System.out.println("pi name: " + pi[i][0]);
			//		}
			
			// get last submission entry date for all labs
			String[] entryDates = new String[piNumber];
			for (int i=0;i<piNumber;i++) {
			    entryDates[i] =
				ishDAO.findLastEntryDateInDBByLabId(Integer.parseInt(pi[i][1]));
			}
			
			// get summary data for all labs
			///////////////////////////////////////////////////////////////////////////////////////
		    //
		    //  there're 4 items contained in the summary at moment
		    //  1 - submission date; 2 - number of submissions;
		    //  3 - assay type; 4 - flag to denote if it's available (1) or editing in progress/not public (0)
		    //  xingjun - 31/05/2011 - expanded to include archive id (5th item) into the summary
		    //
		    ///////////////////////////////////////////////////////////////////////////////////////
		    ArrayList SubmissionSummaryInDB = null;
		    ArrayList SubmissionSummaryInFTPRaw = null;
		    ArrayList SubmissionSummaryInFTPRefined = null;
		    ArrayList summaryInTotal = null;
		    
		    labSummaries = new LabSummary[piNumber];
		    
		    for (int i=0;i<piNumber;i++) {
			//			System.out.println("pi id: " + pi[i][1]);
			SubmissionSummaryInDB =
			    ishDAO.findSubmissionSummaryByLabId(Integer.parseInt(pi[i][1]), 1);
			SubmissionSummaryInFTPRaw =
			    ishDAO.findSubmissionSummaryByLabId(Integer.parseInt(pi[i][1]), 0);
			
			// refine the sumbission summary in the ftp
			// merge the submission entries with the same submission date and assay type
			SubmissionSummaryInFTPRefined = mergeSubmissionSummayOnFTP(SubmissionSummaryInFTPRaw);
	
			summaryInTotal =
			    getSubmissionSummary(SubmissionSummaryInFTPRefined, SubmissionSummaryInDB);
			
			/** assemble data */
			LabSummary labSummary = new LabSummary();
			labSummary.setLabName(pi[i][0]);
			labSummary.setLabId(pi[i][1]);
			labSummary.setLatestEntryDate(entryDates[i]);			
			labSummary.setSummaryResults(summaryInTotal);
			
			labSummaries[i] = labSummary;
		    }
		    /** ---return the composite value object---  */
		    return labSummaries;
		}
		catch(Exception e){
			System.out.println("LabSummaryAssembler::getSummaryData !!!");
			labSummaries = new LabSummary[0];
		    return labSummaries;
		}
		finally{
		    /** release the db resources */
		    DBHelper.closeJDBCConnection(conn);
		    ishDAO = null;
		}
    }
    
    public LabSummary[] getSummaryDataPerLab(String username, int privilege) {
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO;
		LabSummary[] labSummaries = null;
		try{
			ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			// get data
			// get lab name and id
			String[][] pi = ishDAO.findPIFromName(username, privilege);
			int piNumber = pi.length;
			
			String[] entryDates = new String[piNumber];
			for (int i=0;i<piNumber;i++) {
			    entryDates[i] =
				ishDAO.findLastEntryDateInDBByLabId(Integer.parseInt(pi[i][1]));
			}
			
			ArrayList SubmissionSummaryInFTPRaw = null;
			ArrayList SubmissionSummaryInFTPRefined = null;
			ArrayList<String[]> summaryInTotal = null;
			ArrayList[] SubmissionSummaryInDB = null;
			
			labSummaries = new LabSummary[piNumber];
			
			for (int i=0;i<piNumber;i++) {
			    //			System.out.println("pi id: " + pi[i][1]);
			    if(FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/edit_entry_page_per_lab.jsp")) {
				if(privilege >= 3) {
				    SubmissionSummaryInDB =
					ishDAO.findSubmissionSummaryByLabIdForAnnotation(Integer.parseInt(pi[i][1]), new int[]{4,5,19,20,21,22,23,24,25});
				    SubmissionSummaryInFTPRaw =
					ishDAO.findSubmissionSummaryByLabId(Integer.parseInt(pi[i][1]), 0);
				    
				    SubmissionSummaryInFTPRefined = mergeSubmissionSummayOnFTP(SubmissionSummaryInFTPRaw);
				    
				    summaryInTotal =
					getAnnotateSubmissionSummary(SubmissionSummaryInFTPRefined, SubmissionSummaryInDB, new int[]{4,5,19,20,21,22,23,24,25});
				}
			    }
			    /** assemble data */
			    LabSummary labSummary = new LabSummary();
			    labSummary.setLabName(pi[i][0]);
			    labSummary.setLabId(pi[i][1]);
			    labSummary.setLatestEntryDate(entryDates[i]);
			    labSummary.setSummaryResults(summaryInTotal);
			    
			    labSummaries[i] = labSummary;
			}
		}
		catch(Exception e){
			System.out.println("LabSummaryAssembler::getSummaryDataPerLab !!!");
			labSummaries = new LabSummary[0];
		}
	
		/** release the db resources */
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the composite value object---  */
		return labSummaries;
    }	
    
    /**
     * modified by Xingjun - 05/02/2009 - renamed from refineSubmissionSummary to current name
     * <p>merge the submission entries with the same submission date and assay type</p>
     * <p>different archives will take different lines of display</p>
     * @param submissionSummaryRaw
     * @return
     */
    private ArrayList mergeSubmissionSummayOnFTP(ArrayList submissionSummaryRaw) {
	ArrayList<String[]> result = new ArrayList<String[]>();
	if(null != submissionSummaryRaw) {		
	    int len = submissionSummaryRaw.size();
	    int step = 0;
	    for (int i = 0; i < len; i+=(1+step)) {
		step = 0;
		String[] summaryRaw = (String[])submissionSummaryRaw.get(i);
		String[] summaryRefined = new String[4];
		summaryRefined[0] = summaryRaw[0];
		summaryRefined[2] = summaryRaw[2];
		summaryRefined[3] = summaryRaw[3];// added by Bernie -24/3/2011 - mantis 445
		
		int submissionNumber = Integer.parseInt(summaryRaw[1]);
		for (int j = i+1; j < len; j++) {
		    String[] summaryRawTemp = (String[])submissionSummaryRaw.get(j);
		    if (summaryRawTemp[0].equals(summaryRaw[0])
			&& summaryRawTemp[2].equals(summaryRaw[2])
			&& summaryRawTemp[3].equals(summaryRaw[3])) {// added by xingjun - 30/05/2011 - different archives will take different lines of display
			submissionNumber += Integer.parseInt(summaryRawTemp[1]);
			step ++;
		    }
		}
		summaryRefined[1] = Integer.toString(submissionNumber);
		result.add(summaryRefined);
	    }
	}
	return result;
    }
    
    /**
     * <p>modified by xingjun - 29/08/2008
     * when display transgenic assay type, use 'Tg' instead of 'TG'</p>
     * <p>xingjun - 30/05/2011 - need to display all archives on given date</p>
     * @param SubmissionSummaryInFTP
     * @param SubmissionSummaryInDB
     * @return
     */
    private ArrayList getSubmissionSummary(ArrayList submissionSummaryInFTP,
					   ArrayList submissionSummaryInDB) {
	ArrayList<String[]> result = new ArrayList<String[]>();
	if (submissionSummaryInFTP != null) { // user submited data
	    if (submissionSummaryInDB == null) { // submited date are not put into the db yet
		int len = submissionSummaryInFTP.size();
		//System.out.println("ftp Size: " + len);
		
		// go through the ftp array list, set state to 'editing in progress/not public' - value 0 denotes 'editing in progress/not public'; 1 denote 'available'
		// and assemble the statistics info for display
		for (int i = 0; i < len; i++) {
		    String[] summaryInFTP = (String[])(submissionSummaryInFTP.get(i));
		    String[] summaryInProgress = new String[5];					
		    summaryInProgress[0] = summaryInFTP[0];
		    summaryInProgress[1] = summaryInFTP[1];
		    summaryInProgress[2] = summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2];
		    summaryInProgress[3] = Integer.toString(0);
		    summaryInProgress[4] = summaryInFTP[3];// added by Bernie - 24/3/2011 - mantis 445					
		    result.add(summaryInProgress);
		}
	    } else { // submited data already put into the db
		int lenFTP = submissionSummaryInFTP.size(); //System.out.println("ftp Size: " + lenFTP);
		int lenDB = submissionSummaryInDB.size(); //System.out.println("db Size: " + lenDB);
		
		// go through the two array list to calculate the number of submission in 'editing in progress/not public' state
		// and assemble the statistics info for display
		for (int i = 0; i < lenFTP; i++) {
		    String[] summaryInFTP = (String[])(submissionSummaryInFTP.get(i));
		    String[] summaryInProgress = new String[5];
		    summaryInProgress[0] = summaryInFTP[0];
		    summaryInProgress[2] = summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2];
		    summaryInProgress[4] = summaryInFTP[3];// added by Bernie - 24/3/2011 - mantis 445
		    
		    boolean enteredDb = false;
		    boolean allPublic = false;
		    int submissionNumberNonPublic = 0;
		    
		    // go throught the statistics info in db
		    for (int j = 0; j < lenDB; j++) {
			String[] summaryInDB =
			    (String[])(submissionSummaryInDB.get(j));
			if (summaryInDB[0].equals(summaryInFTP[0])
			    && summaryInDB[2].equals(summaryInFTP[2])
			    && summaryInDB[3].equals(summaryInFTP[3])) { // added by xingjun - 30/05/2011 - compare archive id as well
			    enteredDb = true;
			    submissionNumberNonPublic = Integer.parseInt(summaryInFTP[1])
				- Integer.parseInt(summaryInDB[1]);
			    if (submissionNumberNonPublic > 0) { // only part of data submited on this date put into the db
				String[] summaryAvailable = new String[5];
				summaryAvailable[0] = summaryInFTP[0];
				summaryAvailable[1] = new String(summaryInDB[1]);
				summaryAvailable[2] = new String(summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2]);
				summaryAvailable[3] = Integer.toString(1);
				summaryAvailable[4] = new String(summaryInDB[3]);// added by Bernie - 24/3/2011 - mantis 445
				result.add(summaryAvailable);// display available submission (part of)
			    } else { // all data submited on this date put into db
				allPublic = true;
				
				String[] summaryAvailable = new String[5];
				summaryAvailable[0] = summaryInFTP[0];
				summaryAvailable[1] = new String(summaryInDB[1]);
				summaryAvailable[2] = new String(summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2]);
				summaryAvailable[3] = Integer.toString(1);
				summaryAvailable[4] = new String(summaryInDB[3]);// added by Bernie - 24/3/2011 - mantis 445
				result.add(summaryAvailable); // display available submission (all)
			    }
			    break;
			}
		    }
		    
		    if (!enteredDb) {
			summaryInProgress[1] = summaryInFTP[1];
			summaryInProgress[3] = Integer.toString(0);
			
			result.add(summaryInProgress); // display editing in progress/not public submission (all)
		    } else {
			if (!allPublic) {
			    summaryInProgress[1] = Integer.toString(submissionNumberNonPublic);
			    summaryInProgress[3] = Integer.toString(0);
			    
			    result.add(summaryInProgress); // display editing in progress/not public submission (part of)
			}
		    }
		} // end of go through 2 arraylist
	    } // end of submited data already put into the db
	} // end of user submited data
	
	return result;
    }
    
    /**
     * @author xingjun - 01/10/2009
     * <p>xingjun - added archive ids into display</p>
     * @param submissionSummaryInFTP
     * @param submissionSummaryInDB
     * @param dbStatus
     * @return
     */
    private ArrayList<String[]> getAnnotateSubmissionSummary(ArrayList submissionSummaryInFTP,
							     ArrayList[] submissionSummaryInDB, int[] dbStatus) {
	ArrayList<String[]> result = new ArrayList<String[]>();
	if (submissionSummaryInFTP != null) { // user submited data
	    // submited data are not in db yet
	    if (submissionSummaryInDB == null || submissionSummaryInDB.length == 0) {
		int len = submissionSummaryInFTP.size();
		//System.out.println("ftp Size: " + len);
		
		// go through the ftp array list, set state to 'editing in progress/not public'
		// - value 0 denotes 'editing in progress/not public'; 1 denotes 'available'
		// and assemble the statistics info for display
		for (int i = 0; i < len; i++) {
		    String[] summaryInFTP = (String[])(submissionSummaryInFTP.get(i));
		    //					String[] summaryInfo = new String[4];
		    String[] summaryInfo = new String[5]; // xingjun - 30/05/2011 - add archive id into display
		    summaryInfo[0] = summaryInFTP[0];
		    summaryInfo[1] = summaryInFTP[1];
		    summaryInfo[2] = summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2];
		    summaryInfo[3] = Integer.toString(0);
		    summaryInfo[4] = new String(summaryInFTP[3]); // xingjun - 30/05/2011 - add archive id into display
		    result.add(summaryInfo);
		}
	    } else { // submited data already put into the db
		int lenFTP = submissionSummaryInFTP.size(); //System.out.println("ftp Size: " + lenFTP);
		int lenDbStatus = submissionSummaryInDB.length;
		
		// go through the entries in FTP and db to assemble the statistics info for display
		for (int i = 0; i < lenFTP; i++) {
		    String[] summaryInFTP = (String[])(submissionSummaryInFTP.get(i));
		    //					String[] summaryInfo = new String[4];
		    String[] summaryInfo = new String[5]; // xingjun - 30/05/2011 - add archive id into display
		    summaryInfo[0] = summaryInFTP[0];
		    summaryInfo[2] = summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2];
		    summaryInfo[4] = new String(summaryInFTP[3]); // xingjun - 30/05/2011 - add archive id into display
		    
		    boolean inDB = false;
		    boolean allInDB = false;
		    int submissionNumberNotInDB = Integer.parseInt(summaryInFTP[1]);
		    // check the number of entries against each db status
		    for(int k = 0; k < lenDbStatus; k++) {
			if(null != submissionSummaryInDB[k]) {
			    int lenDB = submissionSummaryInDB[k].size(); //System.out.println("db Size: " + lenDB);
			    // go through the statistics info in db
			    for (int j = 0; j < lenDB; j++) {
				String[] summaryInDB = (String[])(submissionSummaryInDB[k].get(j));
				if (summaryInDB[0].equals(summaryInFTP[0]) 
				    && summaryInDB[2].equals(summaryInFTP[2])
				    && summaryInDB[3].equals(summaryInFTP[3])) {
				    inDB = true;
				    submissionNumberNotInDB -= Integer.parseInt(summaryInDB[1]);
				    // only include in situ entries into the display (ISH, IHC, TG)
				    if(null != summaryInFTP[2] && (summaryInFTP[2].equals("IHC") || summaryInFTP[2].equals("ISH") || summaryInFTP[2].equals("TG"))) {
					String[] summaryAvailable =  new String[5]; // xingjun - 30/05/2011 - add archive id into display
					summaryAvailable[0] = summaryInFTP[0];
					summaryAvailable[1] = new String(summaryInDB[1]);
					summaryAvailable[2] = new String(summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2]);
					summaryAvailable[3] = Integer.toString(dbStatus[k]);//Integer.toString(1);
					summaryAvailable[4] = new String(summaryInDB[3]); // xingjun - 30/05/2011 - add archive id into display
					result.add(summaryAvailable);// display available submission (part of)
				    }
				    break;
				}
			    } // end of lenDB loop
			}
		    } // end of lenDbStatus loop
		    if (submissionNumberNotInDB == 0) { // all entries on this date are in db
			allInDB = true;
		    }
		    if (!inDB) {
			summaryInfo[1] = summaryInFTP[1];
			summaryInfo[3] = Integer.toString(0);
			// only include in situ entries into the display (ISH, IHC, TG)
			if(null != summaryInFTP[2] && (summaryInFTP[2].equals("IHC") || summaryInFTP[2].equals("ISH") || summaryInFTP[2].equals("TG"))) {
			    result.add(summaryInfo); // display editing in progress/not public submission (all)
			}
		    } else if (!allInDB) {
			summaryInfo[1] = Integer.toString(submissionNumberNotInDB);
			summaryInfo[3] = Integer.toString(0);
			// only include in situ entries into the display (ISH, IHC, TG)
			if(null != summaryInFTP[2] && (summaryInFTP[2].equals("IHC") || summaryInFTP[2].equals("ISH") || summaryInFTP[2].equals("TG"))) {
			    result.add(summaryInfo); // display editing in progress/not public submission (part of)
			}
		    }
		} // end of go through 2 arraylist
	    } // end of submited data already put into the db
	} // end of user submited data
	if(null != result && result.size() == 0)
	    result = null;
	return result;
    }
    
    /**
     * 
     * @param SubmissionSummaryInFTP
     * @param SubmissionSummaryInDB
     * @return
     */
    private ArrayList getSubmissionSummary(ArrayList submissionSummaryInFTP,
					   ArrayList[] submissionSummaryInDB,
					   int[] dbStatus, boolean onlyShowPublic) {
	ArrayList<String[]> result = new ArrayList<String[]>();
	
	if (submissionSummaryInFTP != null) { // user submited data
	    // submited data are not in db yet
	    if (submissionSummaryInDB == null || submissionSummaryInDB.length == 0) {
		int len = submissionSummaryInFTP.size();
		//				System.out.println("ftp Size: " + len);
		
		// go through the ftp array list, set state to 'editing in progress/not public' 
		// - value 0 denotes 'editing in progress/not public'; 1 denotes 'available'
		// and assemble the statistics info for display
		for (int i = 0; i < len; i++) {
		    String[] summaryInFTP =
			(String[])(submissionSummaryInFTP.get(i));
		    String[] summaryInProgress = new String[4];
		    summaryInProgress[0] = summaryInFTP[0];
		    summaryInProgress[1] = summaryInFTP[1];
		    summaryInProgress[2] = summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2];
		    summaryInProgress[3] = Integer.toString(0);
		    result.add(summaryInProgress);
		}
	    } else { // submited data already put into the db
		int lenFTP = submissionSummaryInFTP.size(); //System.out.println("ftp Size: " + lenFTP);
		int lenDbStatus = submissionSummaryInDB.length;
		// go through the entries in FTP and db to assemble the statistics info for display 
		for (int i = 0; i < lenFTP; i++) {
		    String[] summaryInFTP =
			(String[])(submissionSummaryInFTP.get(i));
		    String[] summaryInProgress = new String[4];
		    summaryInProgress[0] = summaryInFTP[0];
		    summaryInProgress[2] = summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2];
		    
		    boolean enteredDb = false;
		    boolean allPublic = false;
		    int submissionNumberNonPublic = Integer.parseInt(summaryInFTP[1]);
		    // check the number of entries against each db status
		    for(int k = 0; k < lenDbStatus; k++) {
			if(null != submissionSummaryInDB[k]) {
			    int lenDB = submissionSummaryInDB[k].size(); //System.out.println("db Size: " + lenDB);
			    // go through the statistics info in db
			    for (int j = 0; j < lenDB; j++) {
				String[] summaryInDB =
				    (String[])(submissionSummaryInDB[k].get(j));
				if (summaryInDB[0].equals(summaryInFTP[0])
				    && summaryInDB[2].equals(summaryInFTP[2])) {
				    enteredDb = true;
				    submissionNumberNonPublic = submissionNumberNonPublic
					- Integer.parseInt(summaryInDB[1]);
				    if(onlyShowPublic) {
					if(dbStatus[k] == 1) {
					    String[] summaryAvailable =  new String[4];
					    summaryAvailable[0] = summaryInFTP[0];
					    summaryAvailable[1] = new String(summaryInDB[1]);
					    summaryAvailable[2] = new String(summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2]);
					    summaryAvailable[3] = Integer.toString(dbStatus[k]);//Integer.toString(1);
					    result.add(summaryAvailable);// display available submission (part of)
					}
				    } else {
					if(null != summaryInFTP[2] && (summaryInFTP[2].equals("IHC") || summaryInFTP[2].equals("ISH") || summaryInFTP[2].equals("TG"))) {
					    String[] summaryAvailable =  new String[4];
					    summaryAvailable[0] = summaryInFTP[0];
					    summaryAvailable[1] = new String(summaryInDB[1]);
					    summaryAvailable[2] = new String(summaryInFTP[2].equals("TG")?"Tg":summaryInFTP[2]);
					    summaryAvailable[3] = Integer.toString(dbStatus[k]);//Integer.toString(1);
					    result.add(summaryAvailable);// display available submission (part of)
					}
				    }
				    break;
				}
			    } // end of lenDB loop
			}
		    } // end of lenDbStatus loop
		    
		    if (submissionNumberNonPublic > 0) { // only part of data submited on this date put into the db
		    } else { // all data submited on this date put into db
			allPublic = true;
		    }
		    
		    if (!enteredDb) {		
			summaryInProgress[1] = summaryInFTP[1];
			summaryInProgress[3] = Integer.toString(0);
			if(!onlyShowPublic && null != summaryInFTP[2] && !summaryInFTP[2].equals("IHC") && !summaryInFTP[2].equals("ISH")) {
			    //Transgenic
			} else {
			    result.add(summaryInProgress); // display editing in progress/not public submission (all)						
			}
		    } else {
			if (!allPublic) {
			    summaryInProgress[1] = Integer.toString(submissionNumberNonPublic);
			    summaryInProgress[3] = Integer.toString(0);
			    if(!onlyShowPublic && null != summaryInFTP[2] && (summaryInFTP[2].equals("IHC") || summaryInFTP[2].equals("ISH"))) {
			    } else {
				result.add(summaryInProgress); // display editing in progress/not public submission (part of)
			    }
			}
		    }
		} // end of go through 2 arraylist
	    } // end of submited data already put into the db
	} // end of user submited data
	if(null != result && result.size() == 0)
	    result = null;
	return result;
    }	
    
    /**
     * 
     * @param labId
     * @return
     */
    public String getPIName() {
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO;
		String piName;
		try{
			ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			// get data
			piName = ishDAO.findPIByLabId(labId);
			return piName;
		}
		catch(Exception e){
			System.out.println("LabSummaryAssembler::getPIName !!!");
			piName = "";
			return piName;
		}
		finally{
			/** release the db resources */
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
	    }
    }
    
}
