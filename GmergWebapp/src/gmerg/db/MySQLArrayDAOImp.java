/**
 * 
 */
package gmerg.db;

import gmerg.assemblers.AllComponentsGenelistAssembler;
import gmerg.entities.submission.Gene;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.entities.submission.array.MasterTableInfo;
import gmerg.entities.submission.array.Platform;
import gmerg.entities.submission.array.ProcessedGeneListHeader;
import gmerg.entities.submission.array.Sample;
import gmerg.entities.submission.array.SearchLink;
import gmerg.entities.submission.array.Series;
import gmerg.entities.submission.array.SupplementaryFile;
import gmerg.entities.GenelistTreeInfo;
import gmerg.entities.HeatmapData;
import gmerg.utils.Utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import analysis.DataSet;

/**
 * @author xingjun
 *
 */
public class MySQLArrayDAOImp implements ArrayDAO {
    protected boolean debug = false;
    protected boolean performance = true;
    Connection conn;
    
    // default constructor
    public MySQLArrayDAOImp() {
	
    }
    
    // constructor with connection initialisation
    public MySQLArrayDAOImp(Connection conn) {
    	this.conn = conn;
    }
    
    /**
     * @return total number of submissions
     */
    public String getTotalNumberOfSubmission() {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		String totalNumber = new String("");
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_ARRAY");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    
		    if (resSet.first()) {
			totalNumber = Integer.toString(resSet.getInt(1));
		    }
		    
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
			    	System.out.println("MySQLArrayDAOImp.getTotalNumberOfSubmission takes " + enter+" seconds");
			}
			
			return totalNumber;
			
		} catch (SQLException se) {
		    se.printStackTrace();
		    return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
    }
    
    /**
     * 
     * @param queryType: 1-array browse; 2-gene list item browse
     * @param query: sql string
     * @param defaultOrder: if no 'order-by' specified, use it
     * @param order: a 1-dimension array stores the to-be-ordered item and its order
     * @param offset: start position of the rows to be retrieved
     * @param num: number of the rows to be retrieved
     * @return
     */
    private String assembleBrowseSubmissionQueryStringArray(int queryType, String query,
							    String defaultOrder, String[] order, String offset, String num) {
	String queryString = null;
	
	// order by
	if (order != null) {
	    
	    queryString = query + " ORDER BY ";
	    
	    // translate parameters into database column names
	    String column = null;
	    if (queryType == 1) { // array browse
		column = getArrayBrowseOrderByColumn(order);
	    } else if (queryType == 2) { // gene list item browse
		column = getGeneListBrowseOrderByColumn(order);
	    }
	    queryString += column + " " + order[1] + ",";
	    
	    // remove the trailing ',' character
	    int len = queryString.length();
	    queryString = queryString.substring(0, len-1);
	    
	} else { // if don't specify order by column, order by submission id ascend by default
	    //			System.out.println("order is nulll");
	    queryString = query + defaultOrder;
	    //			System.out.println("queryString: " + queryString);
	}
	
	// offset and retrieval number
	if (offset != null) {
	    if (!offset.equalsIgnoreCase("ALL")) {
		if (isValidInteger(offset)) {
		    int os = Integer.parseInt(offset) - 1;
		    if (0 > os)
			queryString = queryString + " LIMIT 0";
		    else
			queryString = queryString + " LIMIT " + os;
		} else {
		    queryString = queryString + " LIMIT 0";
		}
		if (isValidInteger(num)) {
		    queryString = queryString + ", " + num;
		} else {
		    queryString = queryString + ", " + "100000000";
		}
	    }
	}
	
	// return assembled query string
	return queryString;
	
    }
    
    private String assembleBrowseSubmissionQueryStringArray(int queryType, String query,
							    String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
	String queryString = null;
	//System.out.println("QueryType:"+columnIndex+":"+queryType);
	// order by
	
	String group = " GROUP BY SUB_ACCESSION_ID,SMP_GEO_ID,SMP_THEILER_STAGE,TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END), SUB_SOURCE, SUB_SUB_DATE,SMP_SEX,SRM_SAMPLE_DESCRIPTION,SMP_TITLE, SER_GEO_ID, SPN_ASSAY_TYPE  ";
	if (columnIndex != -1) {
	    queryString = query + group + " ORDER BY ";
	    
	    // translate parameters into database column names
	    String column = null;
	    if (queryType == 1) { // array browse
		column = getArrayBrowseOrderByColumn(columnIndex, ascending, defaultOrder);
	    } else if (queryType == 2) { // gene list item browse
		column = getGeneListBrowseOrderByColumn(columnIndex, ascending, defaultOrder);
	    }
	    queryString += column;
	    
	} else { // if don't specify order by column, order by submission date & stage descending by default
	    queryString = query + group + " ORDER BY " + defaultOrder;
	    //			System.out.println("queryString: " + queryString);
	}
	
	// offset and retrieval number
	//		System.out.println("offset: " + offset);
	queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
	
	// return assembled query string
	return queryString;
    } // end of assembleBrowseSubmissionQueryStringArray
    
    /**
     * 
     * @param order
     * @return
     */
    private String getArrayBrowseOrderByColumn(String[] order) {
	//		int columnNumber = order.length;
	//		String[][] columns = new String[columnNumber][2];
	
	String column = new String("");
	//		System.out.println("LAB:"+order[0]+":"+order[1]);
	// start to translate
	if(order[0].equals("byID")) {
	    column = "CAST(SUBSTRING(SUB_ACCESSION_ID,8) AS UNSIGNED)"; 
	} else if (order[0].equals("bySample")) {
	    column = "SMP_GEO_ID"; 
	} else if (order[0].equals("byTheilerStage")) {
	    column = "SMP_THEILER_STAGE"; 
	} else if (order[0].equals("byAge")) {
	    column = "SPN_STAGE"; 
	} else if (order[0].equals("byLab")) {
	    column = "SUB_SOURCE"; 
	} else if (order[0].equals("byDate")) {
	    column = "SUB_SUB_DATE"; 
	} else if (order[0].equals("byTissueType")) {
	    column = "SMP_SOURCE"; 
	} else if (order[0].equals("byDescription")) {
	    column = "SRM_SAMPLE_DESCRIPTION"; 
	} else if (order[0].equals("byTitle")) {
	    column = "SMP_TITLE"; 
	} else if (order[0].equals("bySeries")) {
	    column = "SER_GEO_ID"; 
	} else {
	    column = "SUB_SUB_DATE";
	}
	return column;
    }
    
    private String getArrayBrowseOrderByColumn(int columnIndex, boolean ascending, String defaultOrder) {
	
	String column = new String("");
	String order = (ascending == true ? "ASC": "DESC");
	
	// start to translate
	if(columnIndex == 0) { // gudmap id
	    column = "CAST(SUBSTRING(SUB_ACCESSION_ID,8) AS UNSIGNED) " + order + ", " + defaultOrder;
	} else if (columnIndex == 1) { // sample
	    column = "SMP_GEO_ID " + order + ", " + defaultOrder;
	} else if (columnIndex == 2) { // stage
	    column = "SMP_THEILER_STAGE " + order + ", " + "SUB_SUB_DATE DESC";
	} else if (columnIndex == 3) { // age
	    //			column = "concat(SPN_STAGE_FORMAT,SPN_STAGE) " + order + ", " + defaultOrder;
	    column = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) " + order + ", " + defaultOrder;
	} else if (columnIndex == 4) { // lab
	    column = "SUB_SOURCE " + order + ", " + defaultOrder;
	} else if (columnIndex == 5) { // submission date
	    column = "SUB_SUB_DATE " + order + ", " + "SMP_THEILER_STAGE DESC";
	} else if (columnIndex == 6) {// tissue
	    column = "SMP_SOURCE " + order + ", " + defaultOrder;
	} else if (columnIndex == 7) { // description
	    column = "SRM_SAMPLE_DESCRIPTION " + order + ", " + defaultOrder;
	} else if (columnIndex == 8) { // title
	    column = "SMP_TITLE " + order + ", " + defaultOrder;
	} else if (columnIndex == 9) { // series
	    column = "SER_GEO_ID " + order + ", " + defaultOrder;
	} else if (columnIndex == 10) { // series
	    column = "concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') " + order + ", " + defaultOrder;
	} else { // default
	    column = defaultOrder;
	}
	
	return column;
    }
    
    /**
     * 
     * @param order
     * @return
     */
    private String getGeneListBrowseOrderByColumn(String[] order) {
	//		int columnNumber = order.length;
	//		String[][] columns = new String[columnNumber][2];
	
	String column = new String("");
	
	// start to translate
	if(order[0].equals("byGene")) {
	    column = "MBC_GNF_SYMBOL"; 
	} else if (order[0].equals("byProbeID")) {
	    column = "MBC_GLI_PROBE_SET_NAME"; 
	} else if (order[0].equals("bySignal")) {
	    column = "MBC_GLI_SIGNAL"; 
	} else if (order[0].equals("byDetection")) {
	    column = "MBC_GLI_DETECTION"; 
	} else if (order[0].equals("byPValue")) {
	    column = "MBC_GLI_P_VALUE"; 
	}
	else {
	    column = "MBC_GNF_SYMBOL";
	}
	return column;
    }
    
    // need change when find default order
    /**
     * @author xingjun
     * 
     */
    private String getGeneListBrowseOrderByColumn(int columnIndex, boolean ascending, String defaultOrder) {
	
	String column = new String("");
	String order = (ascending == true ? "ASC":"DESC");
	
	// start to translate
	if(columnIndex == 0) { // gene
	    column = "MBC_GNF_SYMBOL " + order + ", " + defaultOrder; 
	} else if (columnIndex == 1) { // probe id
	    column = "MBC_GLI_PROBE_SET_NAME " + order + ", " + defaultOrder; 
	} else if (columnIndex == 2) { // signal
	    column = "MBC_GLI_SIGNAL " + order + ", " + defaultOrder; 
	} else if (columnIndex == 3) { // detection
	    column = "MBC_GLI_DETECTION " + order + ", " + defaultOrder; 
	} else if (columnIndex == 4) { // p-value
	    column = "MBC_GLI_P_VALUE " + order + ", " + defaultOrder; 
	} else {
	    column = defaultOrder;
	}
	return column;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    private boolean isValidInteger(String value) {
	
	boolean result = true;
	try {
	    Integer.parseInt(value);
	} catch (NumberFormatException nfe) {
	    result = false;
	}
	return result;
    }
    
    
    /**
     * @param param - string value to specify the criteria of the query therein
     * @param query - query string
     * @return query name and query result stored in a 2-dim array
     */
    public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		int queryNumber = query.length;
		String[][] result = new String[queryNumber][2];
		String[] queryString = new String[queryNumber];
		
		// get the query sql based on query name and record query name in result string array
		for (int i=0;i<queryNumber;i++) {
		    //			System.out.println("query Name: " + query[i]);
		    ParamQuery parQ = DBQuery.getParamQuery(query[i]);
		    queryString[i] = parQ.getQuerySQL();
		    result[i][0] = query[i];
		}
		
		// start to execute query
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    for (int i=0;i<queryNumber;i++) {
			if (debug)
			    System.out.println("MySQLArrayDAOImp.sql = "+queryString[i]);
			prepStmt = conn.prepareStatement(queryString[i]);
			if ( param != null && param[i] != null) { // set query criteria if it's not null
			    int parameterNumber = param[i].length;
			    for (int j=0;j<parameterNumber;j++) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp. "+(j+1)+" arg = "+param[i][j]);
				prepStmt.setString(j+1, param[i][j]);
			    }
			}
			resSet = prepStmt.executeQuery();
			result[i][1] = getStringValueFromIntegerResultSet(resSet);
		    }
			return result;
			
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getStringArrayFromBatchQuery takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param resSet
     * @return
     */
    private String getStringValueFromIntegerResultSet(ResultSet resSet) {
	try {
	    if (resSet.first()) {
		int integerResult = resSet.getInt(1);
		return Integer.toString(integerResult);
	    } else {
		return null;
	    }
	} catch(SQLException se) {
	    se.printStackTrace();
	}
	return null;
    }
    
    /**
     * 
     * @param submissionAccessionId
     * @return SupplementaryFile
     */
    public SupplementaryFile findSupplementaryFileInfoBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		
		SupplementaryFile supplementaryFiles = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SUPPLIMENTARY_FILES_ARRAY");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp.findSupplementaryFileInfoBySubmissionId 1 arg = "+submissionAccessionId);
		    prepStmt.setString(1, submissionAccessionId);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    supplementaryFiles = formatSupplementaryFileResultSet(resSet); 
			return supplementaryFiles;

		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.findSupplementaryFileInfoBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private SupplementaryFile formatSupplementaryFileResultSet(ResultSet resSet) throws SQLException {
	
	if (resSet.first()) {
	    SupplementaryFile supplementaryFile = new SupplementaryFile();
	    supplementaryFile.setFilesLocation(resSet.getString(1));
	    supplementaryFile.setCelFile(resSet.getString(2));
	    supplementaryFile.setChpFile(resSet.getString(3));
	    supplementaryFile.setRptFile(resSet.getString(4));
	    supplementaryFile.setExpFile(resSet.getString(5));
	    supplementaryFile.setTxtFile(resSet.getString(6));
	    
	    return supplementaryFile;
	}
	return null;
    }
    
    /**
     * <p> modified by xingjun - 18/11/2009 - relevant sample info might be null</p>
     * @param submissionAccessionId
     * @return Sample
     */
    public Sample findSampleBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		//		System.out.println("ArrayDAO:findSampleBySubmissionId:submissionAccessionId: " + submissionAccessionId);
		Sample sample = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SAMPLE");
		String queryString = parQ.getQuerySQL();
		//		System.out.println("ArrayDAO:findSampleBySubmissionId:sql: " + queryString);
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLArrayDAOImp.sql = "+queryString + " 1 arg = "+submissionAccessionId);
	
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, submissionAccessionId);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    sample = formatSampleResultSet(resSet); 
		    if (sample != null) { // xingjun - 18/11/2009 - in case there's no sample info
		    	formatSampleAnatomy(sample, submissionAccessionId);
		    }
		    
			return sample;
			
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.findSampleBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * <p>modified by xingjun - 18/06/2008 - we might got more than one source record</p>
     * <p>xingjun - 18/11/2009 - sampleAnatomy might be empty</p> 
     * @param sample
     * @param id
     */
    public void formatSampleAnatomy(Sample sample, String id) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("SAMPLE_SOURCE");
		PreparedStatement prepStmt = null;
		//		System.out.println("ArrayDAO:formatSampleAnatomy:sql: " + parQ.getQuerySQL());
		//		System.out.println("ArrayDAO:formatSampleAnatomy:id: " + id);
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp.formatSampleAnatomy 1 arg = "+id);
		    prepStmt.setString(1, id);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				String anatomySource = "";
				while (resSet.next()) { // it's possible it's expressed in more than one component 
				    anatomySource += resSet.getString(1) + "; ";
				}
				anatomySource = anatomySource.substring(0, anatomySource.length()-2);
				//				System.out.println("ArrayDAO:formatSampleAnatomy:anatomySource: " + anatomySource);
				if (anatomySource.trim().length() != 0) { // xingjun - 18/11/2009
				    sample.setSource(anatomySource);
				}
		    }
		    
		} catch(SQLException se) {
		    se.printStackTrace();
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.formatSampleAnatomy takes " + enter+" seconds");
			}
		}
	
    }	
    
    /**
     * <p>xingjun - 14/05/2010 
     * - added 3 extra properties: experimentalDesign, labelProtocol, scanProtocol</p>
     * @param resSet
     * @return
     * @throws SQLException
     */
    private Sample formatSampleResultSet(ResultSet resSet) throws SQLException {
	if (resSet.first()) {
	    Sample sample = new Sample();
	    sample.setGeoID(resSet.getString(1));
	    sample.setTitle(resSet.getString(2));
	    sample.setSource(resSet.getString(3));
	    sample.setOrganism(resSet.getString(4));
	    sample.setStrain(resSet.getString(5));
	    sample.setMutation(resSet.getString(6));
	    sample.setSex(resSet.getString(7));
	    sample.setDevAge(resSet.getString(8));
	    sample.setTheilerStage(resSet.getString(9));
	    sample.setdissectionMethod(resSet.getString(10));
	    sample.setMolecule(resSet.getString(11));
	    sample.setA_260_280(resSet.getString(12));
	    sample.setExtractionProtocol(resSet.getString(13));
	    sample.setAmplificationKit(resSet.getString(14));
	    sample.setAmplificationProtocol(resSet.getString(15));
	    sample.setAmplificationRounds(resSet.getString(16));
	    sample.setVolTargHybrid(resSet.getString(17));
	    sample.setLabel(resSet.getString(18));
	    sample.setWashScanHybProtocol(resSet.getString(19));
	    sample.setGcosTgtVal(resSet.getString(20));
	    sample.setDataAnalProtocol(resSet.getString(21));
	    sample.setReference(resSet.getString(22));
	    sample.setDescription(resSet.getString(23));
	    sample.setExperimentalDesign(resSet.getString(24));
	    sample.setLabelProtocol(resSet.getString(25));
	    sample.setScanProtocol(resSet.getString(26));
	    sample.setPoolSize(resSet.getString(27));
	    sample.setPooledSample(resSet.getString(28));
	    sample.setDevelopmentalLandmarks(resSet.getString(29));// Bernie 22/11/2010 - Mantis 505
	    
	    return sample;
	}
	return null;
    }
    
    /**
     * 
     * @param submissionAccessionId
     * @return Series
     */
    public Series findSeriesBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		
		Series series = null;
		ResultSet resSetSeries = null;
		ResultSet resSetSampleNumber = null;
		ParamQuery parQSeries = DBQuery.getParamQuery("SUBMISSION_SERIES");
		ParamQuery parQSampleNumber = DBQuery.getParamQuery("SAMPLE_NUMBER_OF_SERIES");
		PreparedStatement prepStmtSeries = null;
		PreparedStatement prepStmtSampleNumber = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    conn.setAutoCommit(false);
		    
		    // series
		    parQSeries.setPrepStat(conn);
		    prepStmtSeries = parQSeries.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+submissionAccessionId);
		    prepStmtSeries.setString(1, submissionAccessionId);
		    resSetSeries = prepStmtSeries.executeQuery();
		    
		    // sample number
		    parQSampleNumber.setPrepStat(conn);
		    prepStmtSampleNumber = parQSampleNumber.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+submissionAccessionId);
		    prepStmtSampleNumber.setString(1, submissionAccessionId);
		    resSetSampleNumber = prepStmtSampleNumber.executeQuery();
		    
		    // assemble
		    series = formatSeriesResultSet(resSetSeries, resSetSampleNumber); 
		    
		    conn.commit();
		    conn.setAutoCommit(true);
		    
			return series;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmtSeries);
		    DBHelper.closePreparedStatement(prepStmtSampleNumber);	
		    DBHelper.closeResultSet(resSetSeries);
		    DBHelper.closeResultSet(resSetSampleNumber);
		}
    }
    
    /**DBHelper
     * modified by xingjun - 06/07/2009 - added oid into result series
     * 
     * @param resSetSeries
     * @param resSetSampleNumber
     * @return
     * @throws SQLException
     */
    private Series formatSeriesResultSet(ResultSet resSetSeries, ResultSet resSetSampleNumber) throws SQLException {
	if (resSetSeries.first()) {
	    // series
	    Series series = new Series();
	    series.setGeoID(resSetSeries.getString(1));
	    series.setTitle(resSetSeries.getString(2));
	    series.setSummary(resSetSeries.getString(3));
	    series.setType(resSetSeries.getString(4));
	    series.SetDesign(resSetSeries.getString(5));
	    series.setOid(resSetSeries.getInt(6));
	    
	    // get the sample number
	    if (resSetSampleNumber.first()) {
		series.setNumSamples(Integer.toString(resSetSampleNumber.getInt(1)));
	    }
	    return series;
	}
	return null;
    }
    
    /**
     * 
     * @param submissionAccessionId
     * @return Platform
     */
    public Platform findPlatformBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		
		Platform platform = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_PLATFORM");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+submissionAccessionId);
		    prepStmt.setString(1, submissionAccessionId);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    platform = formatPlatformResultSet(resSet); 
		    
			return platform;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet)	;		
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.findPlatformBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private Platform formatPlatformResultSet(ResultSet resSet) throws SQLException {
	if (resSet.first()) {
	    Platform platform = new Platform();
	    platform.setGeoID(resSet.getString(1));
	    platform.setTitle(resSet.getString(2));
	    platform.setName(resSet.getString(3));
	    platform.setDistribution(resSet.getString(4));
	    platform.setTechnology(resSet.getString(5));
	    platform.setOrganism(resSet.getString(6));
	    platform.setManufacturer(resSet.getString(7));
	    platform.setManufactureProtocol(resSet.getString(8));
	    platform.setCatNo(resSet.getString(9));
	    
	    return platform;
	}
	return null;
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private GeneListBrowseSubmission[] formatGeneListBrowseResultSet(ResultSet resSet) throws SQLException {
	//		ResultSetMetaData resSetMetaData = resSet.getMetaData();
	//		int columnCount = resSetMetaData.getColumnCount();
	
	if (resSet.first()) {
	    resSet.last();
	    int arraySize = resSet.getRow();
	    //need to reset cursor as 'if' move it on a place
	    resSet.beforeFirst();
	    
	    //create ArrayList to store each row of results in
	    GeneListBrowseSubmission[] results = new GeneListBrowseSubmission[arraySize];
	    int i = 0;
	    
	    while (resSet.next()) {
		
		// option 1: initialise a gene list browse submission object and add it into the array
		// false is the default value of the 'selected'
		GeneListBrowseSubmission geneListBrowseSubmission = new GeneListBrowseSubmission();
		geneListBrowseSubmission.setGeneSymbol(resSet.getString(1));
		geneListBrowseSubmission.setSignal(resSet.getString(2));
		geneListBrowseSubmission.setDetection(resSet.getString(3));
		geneListBrowseSubmission.setPvalue(resSet.getString(4));
		geneListBrowseSubmission.setProbeId(resSet.getString(5));
		geneListBrowseSubmission.setSelected(false);
		results[i] = geneListBrowseSubmission;
		i++;
		
                // option 2: initialise an array and add it into the arraylist
		// first cell used to indicate checkbox status: false is the default value
		//				String[] columns = new String[columnCount + 1];
		//				columns[0] = "false";
		//				for (int i = 1; i < columnCount; i++) {
		//					columns[i] = resSet.getString(i + 1);
		//		        }
		//		        results.add(columns);
	    }
	    return results;
	}
	return null;
    }
    
    /**
     * 
     * @param submissionAccessionId
     * @return String
     */
    public String getTotalNumberOfGeneListItemsBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return "0";
		}
		
		String totalNumber = new String("");
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_GENE_LIST_ITEM");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+submissionAccessionId);
		    prepStmt.setString(1, submissionAccessionId);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
			totalNumber = Integer.toString(resSet.getInt(1));
		    }
			return totalNumber;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getTotalNumberOfGeneListItemsBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * method to query database to return an entire gene list for a single microarray sample and find the location
     * of a specific gene symbol in the data set
     * @param accessionId the accession id of the microarray sample
     * @param geneSymbol the specified gene symbol
     * @return the row number of the entry matching the geneSymbol string
     */
    public int getRowNumOf1stOccurrenceOfGeneInArrayGeneList(String accessionId, String geneSymbol) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if(accessionId == null || geneSymbol == null){
		    return -1;
		}
		
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_GENE_LIST_ITEM");
		PreparedStatement prepStmt = null;
		int ret = -1;
		
		// assemble the query string
		String query = parQ.getQuerySQL();
		String defaultOrder = new String(" ORDER BY MBC_GNF_SYMBOL");
		String queryString = assembleBrowseSubmissionQueryStringArray(0, query, defaultOrder, null, null, null);
		parQ.setQuerySQL(queryString);
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
		    	System.out.println("MySQLArrayDAOImp 1 arg = "+accessionId);
		    prepStmt.setString(1, accessionId);
		    resSet = prepStmt.executeQuery();
		    
		    // reset the static query string to its original value
		    parQ.setQuerySQL(query);
		    if(resSet.first()){
				resSet.beforeFirst();
				boolean notFound = true;
				int rowNum = -1;
				String str = null;
				//starting at 0 go through the result set until a match is found
				while(resSet.next() && notFound){
				    str = Utility.netTrim(resSet.getString(1));
				    if(null != str && str.equalsIgnoreCase(geneSymbol)){
				    	notFound = false;
				    	rowNum = resSet.getRow();
				    }	
				}
				
				//if a match was found return the row number of the matching entry
				if(!notFound){
				    ret = rowNum;
				}
		    }	
		    return ret;
		}
		catch(SQLException e){
		    e.printStackTrace();
		    return -1;
		}
		finally {
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(prepStmt);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getRowNumOf1stOccurrenceOfGeneInArrayGeneList takes " + enter+" seconds");
			}
		}
    }
    
    
    /**
     * @author xingjun
     */
    public ArrayList getSubmissionsByLabId(String labId, String submissionDate,
					   int columnIndex, boolean ascending, int offset, int num) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		//		System.out.println("labId: " + labId);
		//		System.out.println("submissionDate: " + submissionDate);
		//		System.out.println("labId: " + labId);
		try { // return null value if lab id is not valid
		    Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
		    return null;
		}
		
		ResultSet resSet = null;
		ArrayList result = null;
		ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ARRAY");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();
		
		if (submissionDate == null || submissionDate.equals("")) {
		    query += " AND SUB_PI_FK = ? ";
		} else {
		    query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? ";
		}
		
		String defaultOrder = new String("SUB_SUB_DATE DESC, SMP_THEILER_STAGE DESC");
		String queryString =
		    assembleBrowseSubmissionQueryStringArray(1, query, defaultOrder, columnIndex, ascending, offset, num);
		//		System.out.println("array browse query: " + queryString);
		
	        int lab = -1;
		try {
		    lab = Integer.parseInt(labId);
		    
	        } catch(NumberFormatException nfe) {
		    lab = 0;
	        }
		
		// execute query and assemble result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
		    	System.out.println("MySQLArrayDAOImp.sql = "+queryString);
		    prepStmt = conn.prepareStatement(queryString);
		    
		    if (submissionDate == null || submissionDate.equals("")) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp 1 arg = "+lab);
				prepStmt.setInt(1, lab);
		    } else {
				if (debug)
				    System.out.println("MySQLArrayDAOImp 1 arg = "+ labId+" 2 arg = "+submissionDate);
		
				prepStmt.setInt(1, Integer.parseInt(labId));
				prepStmt.setString(2, submissionDate);
		    }
		    
			if (debug)
			    System.out.println("MySQLArrayDAOImp prepStmt = "+ prepStmt);
	
		    // execute
		    resSet = prepStmt.executeQuery();
		    //			result = formatBrowseResultSet2ArrayList(resSet);
		    result = DBHelper.formatResultSetToArrayList(resSet);
		    return result;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
		    return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp. getSubmissionsByLabId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * @author xingjun - 01/07/2011 - overloading version
     */
    public ArrayList getSubmissionsByLabId(String labId, String submissionDate, String archiveId,
					   int columnIndex, boolean ascending, int offset, int num, String batchId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (debug)
			System.out.println("GET SUBMISSION BY LAB ID");
	
		ResultSet resSet = null;
		ArrayList result = null;
		ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ARRAY");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();
		
		if (debug)
			System.out.println("query1: " + query);
		
		if (labId != null && !labId.trim().equals("")) {
			query += " AND SUB_PI_FK = ? ";
		}
		
		if (submissionDate != null && !submissionDate.equals("")) {
		    query += " AND SUB_SUB_DATE = ? ";
		}
		
		if (archiveId != null && !archiveId.trim().equals("")) {
		    query += " AND SUB_ARCHIVE_ID = ? ";
		}

		if (batchId != null && !batchId.trim().equals("")) {
			query += " AND SUB_BATCH = ? ";
		}
		
		String defaultOrder = new String("SUB_SUB_DATE DESC, SMP_THEILER_STAGE DESC");
		String queryString =
		    assembleBrowseSubmissionQueryStringArray(1, query, defaultOrder, columnIndex, ascending, offset, num);
		
		if (debug)
			System.out.println("array browse query: " + queryString);
		
		// execute query and assemble result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
            int paramNum = 1;

		    if (labId != null && !labId.trim().equals("")) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp  arg = "+labId);
				prepStmt.setInt(paramNum, Integer.parseInt(labId));
				paramNum++;
		    }
		    
		    if (submissionDate != null && !submissionDate.equals("")) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp  arg = "+submissionDate);
				prepStmt.setString(paramNum, submissionDate);
				paramNum++;
		    }
		    
		    if (archiveId != null && !archiveId.trim().equals("")) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp  arg = "+archiveId);
				prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
				paramNum++;
		    }

		    if (batchId != null && !batchId.trim().equals("")) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp  arg = "+batchId);
				prepStmt.setInt(paramNum, Integer.parseInt(batchId));
				paramNum++;
		    }
		    
		    if (debug)
		    	System.out.println("query = " + prepStmt);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    result = DBHelper.formatResultSetToArrayList(resSet);
			return result;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getSubmissionsByLabId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param submissionAccessionId
     */
    public ArrayList findSamplesInCertainSeriesBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList relatedSamples = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SERIES_SAMPLE");
		PreparedStatement prepStmt = null;
		//		System.out.println("series sample query: " + parQ.getQuerySQL());
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+submissionAccessionId);
		    prepStmt.setString(1, submissionAccessionId);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
			relatedSamples = DBHelper.formatResultSetToArrayList(resSet);
		    }
			return relatedSamples;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.findSamplesInCertainSeriesBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
    // used for analysis
    /**
     * 
     * @param seriesId
     */
    public String[][] findSampleIdsBySeriesId(String seriesId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		String[][] samplesInfo = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SAMPLE_INFO_BY_SERIES_ID");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+seriesId);
		    prepStmt.setString(1, seriesId);
		    resSet = prepStmt.executeQuery();
		    
		    samplesInfo = formatSampleIdResultSet(resSet);
			return samplesInfo;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.findSampleIdsBySeriesId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private String[][] formatSampleIdResultSet(ResultSet resSet) throws SQLException {
	
	if (resSet.first()) {
	    resSet.last();
	    int sampleNumber = resSet.getRow();
	    resSet.beforeFirst();
	    String[][] samplesInfo = new String[sampleNumber][2];
	    int i = 0;
	    while (resSet.next()) {
		samplesInfo[i][0] = resSet.getString(1);
		samplesInfo[i++][1] = resSet.getString(2);
	    }
	    return samplesInfo;
	}
	return null;
    }
    
    /**
     * this method need to be rewritten: it's not comply to the design!!!!!!!!!!!!!!!!
     * @param sampleIds
     * @return
     */
    public DataSet getAnalysisDataSetBySampleIds(String[][] samplesInfo) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		double[][] data = null;
		char[][] mask = null;
		String[][]geneId = null;
		int geneNo=0;
		int sampleNo = samplesInfo.length; 
		
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("GENELIST_INFO_RAW");
		PreparedStatement prepStmt = null;
		
		int row;
		String str = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    for(int col=0; col<sampleNo; col++) {
				if (debug)
				    System.out.println("MySQLArrayDAOImp 1 arg = "+samplesInfo[col][0]);
				prepStmt.setString(1, samplesInfo[col][0]);
				resSet = prepStmt.executeQuery();
				resSet.last();
				geneNo = resSet.getRow();
				resSet.beforeFirst();
				if (col==0){
				    data = new double[geneNo][sampleNo];
				    mask = new char[geneNo][sampleNo];
				}
				while(resSet.next()) {
				    row = resSet.getInt(1) - 1;
				    data[row][col] = resSet.getFloat(2);
				    str = Utility.netTrim(resSet.getString(3));
				    if (null != str)
				    	mask[row][col] = str .charAt(0);
				}
		    }
		    
		    // retrieve probe names and gene symbols
		    geneId = new String[geneNo][2];
		    parQ = DBQuery.getParamQuery("GENELIST_INFO_WITH_SYMBOL_RAW");
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp 1 arg = "+samplesInfo[0][0]);
		    prepStmt.setString(1, samplesInfo[0][0]);
		    resSet = prepStmt.executeQuery();
		    
		    String prevProbeName = null;
		    String geneSymbol = null;
		    while (resSet.next()){
				row = resSet.getInt("GLI_SERIAL_NO")-1;
				geneId[row][0] = resSet.getString("GLI_PROBE_SET_NAME");  //Get probe name
				String gs = resSet.getString("GNF_SYMBOL");
				geneSymbol = (gs != null) ? gs : "";
				//				System.out.print("row: " + row + "####");
				//				if (geneSymbol == null) System.out.print("NULL###");
				//				System.out.println("geneSymbol: " + geneSymbol);
				
				// Set gene Symbol in a comma separated list
				if (geneId[row][0].equals(prevProbeName)) {
				    geneId[row][1] += ", " + geneSymbol;
				} else {
				    geneId[row][1] = geneSymbol;
				}
				
				prevProbeName = geneId[row][0];
		    }
		    
			String[][] sampleNames = new String[sampleNo][1];
			for (int i=0; i<sampleNo; i++) {
			    //			sampleNames[i][0] = "Sample_"+samplesInfo[i][0];
			    sampleNames[i][0] = samplesInfo[i][1];
			}
			
			DataSet dataSet = new DataSet(data, mask, geneNo, sampleNo, geneId, sampleNames);
			String[] arrayHeader = {"Sample ID"};
			String[] geneHeader =  {"ProbeID", "GeneSymbol"};
			boolean[] geneHeadersClickable = {false, true};
			dataSet.setGeneHeaders(geneHeader);
			dataSet.setArrayHeaders(arrayHeader);
			dataSet.setGeneHeadersClickable(geneHeadersClickable);
			return dataSet;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getAnalysisDataSetBySampleIds takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     */
    public ArrayList getAllSeriesGEOIds() {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList<String> seriesGEOIds = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SERIES_INFO");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    resSet = prepStmt.executeQuery();
		    
		    if (resSet.first()) {
				seriesGEOIds = new ArrayList<String>();
				resSet.beforeFirst();
				while(resSet.next()) {
				    String seriesGEOId = resSet.getString(1);
				    if (seriesGEOId != null && !seriesGEOId.equals("")) {
					seriesGEOIds.add(seriesGEOId);
				    }
				}
		    }
			return seriesGEOIds;
		    
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getAllSeriesGEOIds takes " + enter+" seconds");
			}
		}
    }
    
    // used for microarray focus, gene list
    /**
     * 
     * @param componentName
     * @return
     */
    public ArrayList getComponentListByName(String componentName) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList<String> componentNames = new ArrayList<String>();
		componentNames.add(componentName);
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_COMPONENT_NAME");
		
		// add criteria for the quer string
		String queryString = parQ.getQuerySQL();
		queryString += "'%" + componentName + "%'";
		//		System.out.println("component list query: " + queryString);
		
		// execute query
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLArrayDAOImp.sql = "+queryString.toLowerCase());
		    prepStmt = conn.prepareStatement(queryString);
		    resSet = prepStmt.executeQuery();
		    
		    if (resSet.first()) {
				resSet.beforeFirst();
				while(resSet.next()) {
				    String component = resSet.getString(1);
				    if (component != null && !component.equals("")) {
					componentNames.add(component);
				    }
				}
		    }
			return componentNames;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getComponentListByName takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param componentNames
     * @return
     */
    public ArrayList getGenelistHeadersByComponentNames(ArrayList componentNames) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList genelistHeaders = null;
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_HEADER");
		
		// add where clause for the quer string
		String queryString = parQ.getQuerySQL();
		int componentNumber = componentNames.size();
		String whereClause = "";
		if (componentNumber == 1) {
		    whereClause += " WHERE IGL_COMPONENT LIKE '%" + (String)componentNames.get(0) + "%' ";
		} else {
		    whereClause += " WHERE (IGL_COMPONENT LIKE '%" + (String)componentNames.get(0) + "%' ";
		    for (int i=1;i<componentNumber;i++) {
	    		whereClause += " OR IGL_COMPONENT LIKE '%" + (String)componentNames.get(i) + "%' ";
		    }
		    whereClause += ")";
		}
		
		queryString += (whereClause + "AND PER_OID = IGL_LAB_NAME");
		//		System.out.println("genelist header by component query: " + queryString);
		
		// execute query and format the result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLArrayDAOImp.sql = "+queryString.toLowerCase());
		    prepStmt = conn.prepareStatement(queryString);
		    resSet = prepStmt.executeQuery();
		    genelistHeaders = formatGenelistHeaderResultSet(resSet);
		    
			return genelistHeaders;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getGenelistHeadersByComponentNames takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private ArrayList formatGenelistHeaderResultSet(ResultSet resSet) throws SQLException {
	
	if (resSet.first()) {
	    ArrayList<ProcessedGeneListHeader> geneListHeaders = new ArrayList<ProcessedGeneListHeader>();
	    // reset the resultSet cursor
	    resSet.beforeFirst();
	    while (resSet.next()) {
		ProcessedGeneListHeader geneListHeader = new ProcessedGeneListHeader();
		geneListHeader.setSurname(resSet.getString("PER_SURNAME"));
		geneListHeader.setLabName(Integer.toString(resSet.getInt("IGL_LAB_NAME")));
		geneListHeader.setComponents(resSet.getString("IGL_COMPONENT"));
		geneListHeader.setStatistics(resSet.getString("IGL_STATISTICS"));
		geneListHeader.setDataTransformation (resSet.getString("IGL_DATA_TRANSFORMATION"));
		geneListHeader.setTests(resSet.getInt("IGL_TESTS"));
		geneListHeader.setSerialNo(resSet.getInt("IGL_SERIAL_NUMBER"));
		geneListHeader.setUpRegulated(resSet.getInt("IGL_UP_REGULATED"));
		geneListHeader.setDownRegulated(resSet.getInt("IGL_DOWN_REGULATED"));
		geneListHeader.setFileName(resSet.getString("IGL_FILE_NAME"));
		geneListHeader.setId(resSet.getInt("IGL_OID"));
		geneListHeaders.add(geneListHeader);
	    }
	    //			System.out.println("geneListHeaders size: " + geneListHeaders.size());
	    return geneListHeaders;
	}
	return null;
    }
    
    /**
     * 
     * @param labId
     * @return
     */
    public ArrayList getGenelistHeadersByLabId(String labId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList genelistHeaders = null;
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_HEADER");
		
		// add where clause for the quer string
		String queryString = parQ.getQuerySQL();
		String whereClause = " where IGL_LAB_NAME = " + labId;
		
		queryString += (whereClause + "AND PER_OID = IGL_LAB_NAME");
		//		System.out.println("genelist header by lab id query: " + queryString);
		
		// execute query and format the result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLArrayDAOImp.sql = "+queryString.toLowerCase());
		    prepStmt = conn.prepareStatement(queryString);
		    resSet = prepStmt.executeQuery();
		    genelistHeaders = formatGenelistHeaderResultSet(resSet);
		    
			return genelistHeaders;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getGenelistHeadersByLabId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     */
    public ArrayList<SearchLink> getGenelistExternalLinks() {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList<SearchLink> externalLinks = null;
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_EXTERNAL_LINK");
		//		System.out.println("external link query: " + parQ.getQuerySQL());
		
		// execute query and format the result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLArrayDAOImp:getGenelistExternalLinks sql = "+prepStmt);

		    resSet = prepStmt.executeQuery();
		    externalLinks = formatGenelistExternalLinkResultSet(resSet);
		    
			return externalLinks;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getGenelistExternalLinks takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private ArrayList<SearchLink> formatGenelistExternalLinkResultSet(ResultSet resSet) throws SQLException {
	
	if (resSet.first()) {
	    ArrayList<SearchLink> externalLinks = new ArrayList<SearchLink>();
	    
	    // reset the resultSet cursor
	    resSet.beforeFirst();
	    
	    while (resSet.next()){
		SearchLink externalLink = new SearchLink();
		externalLink.setUrlPrefix(resSet.getString("URL_URL"));
		externalLink.setUrlSuffix(resSet.getString("URL_SUFFIX"));
		externalLink.setInstitue(resSet.getString("URL_INSTITUTE"));
		externalLink.setName(resSet.getString("URL_SHORT_NAME"));
		externalLinks.add(externalLink);
	    }
	    return externalLinks;
	}
	return null;
    }
    
    /**
     * 
     * @param genelistId
     * @return
     */
    public Object[][] getGenelistItemsByGenelistId(int genelistId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		Object[][] genelistItems = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_ITEM");
		
		// execute query and format the result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    prepStmt.setInt(1, genelistId);
		    resSet = prepStmt.executeQuery();
		    
		    genelistItems = formatGenelistItemResultSet(resSet);
			return genelistItems;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getGenelistItemsByGenelistId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param genelistId
     * @param columnId
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    public Object[][] getGenelistItemsByGenelistId(int genelistId, int columnId, boolean ascending, int offset, int num) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		Object[][] genelistItems = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_ITEM");
		String query = parQ.getQuerySQL();
		String queryString = assembleQueryString(query, columnId, ascending, offset, num);
		
		// execute query and format the result
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLArrayDAOImp.sql = "+queryString.toLowerCase()+" 1 arg = "+genelistId);
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setInt(1, genelistId);
		    resSet = prepStmt.executeQuery();
		    
		    genelistItems = formatGenelistItemResultSet(resSet);
		    
			return genelistItems;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getGenelistItemsByGenelistId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * 
     * @param query
     * @param columnId
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    private String assembleQueryString(String query, int columnId, boolean ascending, int offset, int num) {
	
	String queryString = null;
	
	// append order by clause is exists
	if (columnId != 0) { // there is an 'order by' criteria
	    queryString = query + " ORDER BY ";
	    
	    // translate parameter into database column name
	    String columnName = getGeneListColumnName(columnId);
	    
	    queryString += columnName + " ";
	    
	    if (!ascending) {
		queryString += "DESC ";
	    }
	    
	} else { // there's no 'order by' criteria
	    queryString = query + " ";
	}
	
	// append row number restriction clause
	if (offset != 0) {
	    queryString += " LIMIT " + Integer.toString(offset-1) + " ," + Integer.toString(num);
	}
	
	return queryString;
    }
    
    /**
     * 
     * @param columnId
     * @return
     */
    private String getGeneListColumnName(int columnId) {
	
	String columnName = null;
	
	// start to translate
	if (columnId == 1) {
	    columnName = "IGI_GRP1_MEAN";
	} else if (columnId == 2) {
	    columnName = "IGI_GRP2_MEAN";
	} else if (columnId == 3) {
	    columnName = "IGI_GRP1_SEM";
	} else if (columnId == 4) {
	    columnName = "IGI_GRP2_SEM";
	} else if (columnId == 5) {
	    columnName = "IGI_RATIO";
	} else if (columnId == 6) {
	    columnName = "IGI_DIRECTION";
	} else if (columnId == 7) {
	    columnName = "IGI_P_VALUE";
	} else if (columnId == 8) {
	    columnName = "IGI_GENE_IDENTIFIER";
	} else if (columnId == 9) {
	    columnName = "IGI_OTHER_ID";
	} else if (columnId == 10) {
	    columnName = "IGI_GENE_NAME";
	} else if (columnId == 11) {
	    columnName = "IGI_UG_CLUSTER";
	} else if (columnId == 12) {
	    columnName = "IGI_LOCUSLINK";
	} else if (columnId == 13) {
	    columnName = "IGI_GENE_ID";
	} else if (columnId == 14) {
	    columnName = "IGI_CHROMOSOME";
	} else if (columnId == 15) {
	    columnName = "IGI_ONTOLOGIES";
	} else if (columnId == 16) {
	    columnName = "IGI_ISH_ENTRY_COUNT";
	}
	return columnName;
    }
    
    /**
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private Object[][] formatGenelistItemResultSet(ResultSet resSet) throws SQLException {
	
	if (resSet.first()) {
	    // obtain the column number and row number of the gene list item result
	    ResultSetMetaData resSetMetaData = resSet.getMetaData();
	    int columnCount = resSetMetaData.getColumnCount();
	    resSet.last();
	    int rowCount = resSet.getRow();
	    
	    // reset resultSet cursor and initilise object array to format genelist item result
	    resSet.beforeFirst();
	    Object[][] genelistItems = new Object[rowCount][columnCount];
	    
	    // format
	    int row = 0;
	    while (resSet.next()) {
		for (int col=0;col<columnCount;col++) {
		    genelistItems[row][col] = resSet.getObject(col+1);
		}
		row++;
	    }
	    return genelistItems;
	}
	return null;
    }
    
    
    /**
     * 
     * @param
     * @return
     */
    public int getGenelistsEntryNumber(int genelistId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
	
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("TOTAL_NUMBER_OF_pROCESSED_GENE_LIST_ITEMS");
        PreparedStatement prepStmt = null;
	
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setInt(1,genelistId);
            resSet = prepStmt.executeQuery();
	    
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
			return result;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return 0;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getGenelistsEntryNumber takes " + enter+" seconds");
			}
		}
    }
    
    
    /**
     * @author xingjun - 21/11/2008
     * @param symbol - gene symbol
     * @param platformId - GEO platform id
     * @return a list of probe set ids
     */
    public ArrayList<String> getProbeSetIdBySymbol(String geneId, String platformId) {
    	long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList<String> probeSetIds = null;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOL");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, geneId);
		    prepStmt.setString(2, platformId);
		    
		    if (debug) System.out.println("MySQLArrayDAOImp.sql = "+ prepStmt);

		    
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
				    probeSetIds.add(resSet.getString(1));
				}
		    }
		    //			probeSetIds = DBHelper.formatResultSetToArrayList(resSet);
		    
			return probeSetIds;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getProbeSetIdBySymbol takes " + enter+" seconds");
			}
		}
    }

    public ArrayList<String> getProbeSetIdBySymbolId(String symbolId, String platformId) {
 		
		ArrayList<String> probeSetIds = null;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOLID");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLArrayDAOImp.sql = "+queryString.toLowerCase() + "1 arg = "+symbolId+" 2 arg = "+platformId);
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, symbolId);
		    prepStmt.setString(2, platformId);
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
				    probeSetIds.add(resSet.getString(1));
				}
		    }
		    //			probeSetIds = DBHelper.formatResultSetToArrayList(resSet);
		    
			return probeSetIds;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
    }
   
    /**
     * @author xingjun - 19/03/2009
     * <p>modified by xingjun - 25/03/2009 - modified sql, added assemble query string method</p>
     * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
     * the genelist id and cluster id<p> 
     */
    public ArrayList<String> getProbeSetIdByAnalysisGenelistId(String genelistId, 
							       boolean ascending, int offset, int num) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (genelistId == null || genelistId.equals("")) {
		    return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
		
		ArrayList<String> probeSetIds = null;
	        ResultSet resSet = null;
	        ParamQuery parQ = null;
		
	        if (clstId == null) { // only genelist id passed in
	            parQ =
	                ArrayDBQuery.getParamQuery("GET_PROBE_SET_ID_BY_ANALYSIS_GENELIST_ID");
	        } else {
	            parQ =
	                ArrayDBQuery.getParamQuery("GET_PROBE_SET_ID_BY_ANALYSIS_GENELIST_CLUSTER_ID");
	        }
	        String queryString = parQ.getQuerySQL();
		//        System.out.println("getProbeSet query raw: " + queryString);
	        queryString = this.assembleProbeSetIdByAnaGenelistIdQueryString(queryString, 
										ascending, offset, num);
		//        System.out.println("getProbeSet query extra: " + queryString);
		//        System.out.println("getProbeSet genelistId: " + genelistId);
	        PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
	
		    prepStmt = conn.prepareStatement(queryString);
		    if (clstId == null) {
		    	prepStmt.setInt(1, Integer.parseInt(glstId));
		    } else {	
			prepStmt.setInt(1, Integer.parseInt(glstId));
			prepStmt.setInt(2, Integer.parseInt(clstId));
		    }
		    if (debug) System.out.println("MySQLArrayDAOImp.getProbeSetIdByAnalysisGenelistId sql = "+prepStmt);
		    
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
				    probeSetIds.add(resSet.getString(1));
				}
		    }
			return probeSetIds;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getProbeSetIdByAnalysisGenelistId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * @author xingjun - 25/03/2009
     * @param query
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    private String assembleProbeSetIdByAnaGenelistIdQueryString(String query, 
								boolean ascending, int offset, int num) {
	String queryString = query;
	
	// append order by clause
	if (!ascending) {
	    queryString += " DESC";
	}
	
	// append row number restriction clause
	if (offset != 0) {
	    queryString += " LIMIT " + Integer.toString(offset-1) + " ," + Integer.toString(num);
	}
	return queryString;
    }
    
    /**
     * @author xingjun - 26/03/2009
     * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
     * the genelist id and cluster id<p> 
     */
    public String getAnalysisGenelistTitle(String genelistId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (genelistId == null || genelistId.equals("")) {
		    return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
		
		String genelistTitle = null;
	        ResultSet resSet = null;
	        ParamQuery parQ = null;
	        if (clstId == null) { // only genelist id passed in
	            parQ =
	                ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST_TITLE");
	        } else {
	            parQ =
	                ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST_CLUSTER_TITLE");
	        }
		
	        String queryString = parQ.getQuerySQL();
	        PreparedStatement prepStmt = null;
		try {
		    prepStmt = conn.prepareStatement(queryString);
		    if (clstId == null) {
			prepStmt.setInt(1, Integer.parseInt(glstId));
		    } else {
			prepStmt.setInt(1, Integer.parseInt(clstId));
		    }
		    if (debug) System.out.println("MySQLArrayDAOImp.getAnalysisGenelistTitle sql = "+prepStmt);
		    
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
			genelistTitle = resSet.getString(1);
		    }
			return genelistTitle;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
    }

    public String getAnalysisGenelist(String genelistId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (genelistId == null || genelistId.equals("")) {
		    return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
		
		String genelist = "";
	    ResultSet resSet = null;
	    ParamQuery parQ = null;
	    if (clstId == null) { // only genelist id passed in
	    	parQ = ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST");
	    } 
		
	    String queryString = parQ.getQuerySQL();
	    PreparedStatement prepStmt = null;
		try {
		    prepStmt = conn.prepareStatement(queryString);
		    if (clstId == null) {
			 prepStmt.setInt(1, Integer.parseInt(glstId));
		    } else {
			 prepStmt.setInt(1, Integer.parseInt(clstId));
		    }
		    if (debug) System.out.println("MySQLArrayDAOImp.getAnalysisGenelist sql = "+prepStmt);
		    
		    resSet = prepStmt.executeQuery();
		    
			while (resSet.next()) { // it's possible it's expressed in more than one component 
				genelist += resSet.getString(1) + ",";
			}
			genelist = genelist.substring(0, genelist.length()-1);
			if (debug)
				System.out.println("genelist = "+genelist);
			
			return genelist;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
    }
    
    /**
     * @author xingjun - 21/11/2008
     * <p>modified by xingjun - 11/08/2009
     * - added master table id into the query</p>
     * 
     * <p>modified by xingjun - 26/08/2009 
     * - added an extra parameter of genelist id
     * - if the value is provided, the result should be based on the order of 
     *   probe set id in that genelist; if the value is not provided, ignore it</p>
     *   
     * <p>xingjun - 04/02/2010 
     * - added extra criteria (section). now the masterTableId consisted of masterTableId plus sectionId with underscore in between them
     * - return median value along with the expression
     * </p>
     * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
     * the genelist id and cluster id<p> 
     */
    public HeatmapData getExpressionByGivenProbeSetIds(ArrayList probeSetIds, 
						       String masterTableId, String genelistId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (debug)
		    System.out.println("MySQLArrayDAOImp.getExpressionByGivenProbeSetIds masterTableId = "+masterTableId+" genelistId = "+genelistId);
		
		// only go to find the expression if probe set ids are provided
		HeatmapData expressions = null;
		if (probeSetIds != null && probeSetIds.size() != 0 
		    && masterTableId != null && !masterTableId.equals("")) {
		    ResultSet resSet = null;
		    PreparedStatement prepStmt = null;
		    
		    // use specific sql based on genelistId parameter
		    ParamQuery parQ = null;
		    boolean genelistIdProvided = false; 
		    String glstId = "";
		    String clstId = "";
		    if (genelistId == null || genelistId.equals("")) {
			parQ = DBQuery.getParamQuery("ARRAY_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
		    } else {
				genelistIdProvided = true;
				glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
				clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
				if (clstId == null) { // only genelist id passed in
				    parQ = ArrayDBQuery.getParamQuery("GET_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
				} else { // cluster id passed in as well
				    parQ = ArrayDBQuery.getParamQuery("GET_EXPRESSION_OF_GIVEN_PROBE_SET_IDS_CLUSTER");
				}
				
		    }
		    String querySQL = parQ.getQuerySQL();
		    
		    // assembler query string
		    // assemble probe set id string
		    String queryString = 
			this.assembleExpressionByGivenProbeSetIdsQueryString(probeSetIds, querySQL);
		    
		    if (debug) {
	 	        System.out.println("getExpressionByGivenProbeSetIds master table id: " + masterTableId);
		        System.out.println("getExpressionByGivenProbeSetIds genelist id: " + genelistId);
		        System.out.println("getExpressionByGivenProbeSetIds query: " + queryString.toLowerCase());
		    }
		    
		    // get data
		    try {
				// if disconnected from db, re-connected
				conn = DBHelper.reconnect2DB(conn);
				
				prepStmt = conn.prepareStatement(queryString);
				// get master table id and section id
				String[] masterTableAndSectionId = Utility.parseMasterTableId(masterTableId);
				
				if (genelistIdProvided) {
				    if (clstId == null) { // only genelist id passed in
					prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
					prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
					prepStmt.setInt(3, Integer.parseInt(glstId));
				    } else {
					prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
					prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
					prepStmt.setInt(3, Integer.parseInt(glstId));
					prepStmt.setInt(4, Integer.parseInt(clstId));
				    }
				} else {
				    prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
				    prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
				}
				if (debug)
				    System.out.println("MySQLArrayDAOImp.getExpressionByGivenProbeSetIds sql = "+prepStmt);
				
				resSet = prepStmt.executeQuery();
				int probeSetNumber = probeSetIds.size();
				expressions = this.formatExpressionByProbeSetIdsResultSet(resSet, probeSetNumber);
				
				return expressions;
			
			} catch(SQLException se) {
			    se.printStackTrace();
				return null;
			}
			finally{
			    DBHelper.closePreparedStatement(prepStmt);
			    DBHelper.closeResultSet(resSet);
				if (performance) {
				    enter = (System.currentTimeMillis() - enter)/1000;
				    if (2 < enter)
					System.out.println("MySQLArrayDAOImp.getExpressionByGivenProbeSetIds takes " + enter+" seconds");
				}
			}
		}
		return expressions;

    } // end of getExpressionByGivenProbeSetIds
    
    /**
     * @author xingjun - 21/11/2008
     * @param resSet
     * @param probeSetNumber
     * <p>xingjun - 04/02/2010 - added median value along with the expression</p>
     */
    private HeatmapData formatExpressionByProbeSetIdsResultSet(ResultSet resSet, 
		       int probeSetNumber) throws SQLException {
    	
		if (debug)
			System.out.println("MySQLArrayDAOImp.formatExpressionByProbeSetIdsResultSet probeSetNumber: " + probeSetNumber);

		double[][] expressions = null;
		double[] medianValues = null;
		double[] stdValues = null;
		HeatmapData heatmapData = null;
		if (resSet.first()) {
			// obtain the row number of the expression result
			// and calculate column number for each row
			resSet.last();
			int recordCount = resSet.getRow();
			int columnNumber = recordCount/probeSetNumber;
			
			if (debug)
				System.out.println("recordCount: " + recordCount+" columnNumber: " + columnNumber);
			
			// reset resultSet cursor and initilise object array 
			// to format expression result
			expressions = new double[probeSetNumber][columnNumber];
			medianValues = new double[probeSetNumber];
			stdValues = new double[probeSetNumber];
			int rowCounter = 0;
			int rowCounterM = 0;
			int columnCounter = 0;
			resSet.beforeFirst();
			// the sequence of putting data into the double array
			// (in the case of 2 probe set ids and 15 expression groups):
			// expressions[0][0] -> expressions[1][0] ->
			// expressions[0][1] -> expressions[1][1] ->
			// ... ...
			// expressions[0][14] -> expressions[1][14]
			while (resSet.next()) {
				// put expression value into the array (start from the 2nd pos)
				double expression = resSet.getDouble(3);
				expressions[rowCounter][columnCounter] = expression;
				if (rowCounter == rowCounterM) {
					double medianVal = resSet.getDouble(4);
					double stdVal = resSet.getDouble(5);
					medianValues[rowCounterM] = medianVal;
					stdValues[rowCounterM] = stdVal;
					rowCounterM++;
				}
				/*
				if (debug) {
				System.out.println("rowCounter: " + rowCounter);
				System.out.println("columnCounter: " + columnCounter);
				System.out.println("expression value: " + expression);
				}
				*/
				rowCounter++;
				if (rowCounter == probeSetNumber) {
					columnCounter++;
					rowCounter = 0;
				}
			}
			// put the median value into the expression array
			//			for (int i=0;i<probeSetNumber;i++) {
			//				expressions[i][0] = medianValues[i];
			//			}
			// put the expression, median, and std value into the HeatmapData object
			heatmapData =  new HeatmapData(expressions, medianValues, stdValues);
			
			if (debug) 
				System.out.println("MySQLArrayDAOImp.formatExpressionByProbeSetIdsResultSet expressions size = "+expressions.length+" medianValues size = "+medianValues.length+" stdValues size = "+stdValues.length);
			
			//			System.out.println("===== expression =====");
			//			for (int i=0;i<expressions.length;i++) {
			//				for (int j=0;j<expressions[0].length;j++) {
			//					System.out.println(i+":"+j+": "+expressions[i][j]);
			//				}
			//			}
			//			System.out.println("===== median =====");
			//			for (int i=0;i<medianValues.length;i++) {
			//				System.out.println(i+":"+medianValues[i]);
			//			}
			//			System.out.println("===== std =====");
			//			for (int i=0;i<stdValues.length;i++) {
			//				System.out.println(i+":"+stdValues[i]);
			//			}
		}
		
		return heatmapData;
    }
    
    /**
     * @author xingjun - 01/05/2009
     * @param resSet
     * @return
     * @throws SQLException
     */
    private ArrayList<String> formatExpressionByProbeSetIdsResultSet(ResultSet resSet) throws SQLException {
	ArrayList<String> expressions = null;
	if (resSet.first()) {
	    resSet.beforeFirst();
	    expressions = new ArrayList<String>();
	    while (resSet.next()) {
		String expression = Double.toString(resSet.getDouble(3));
		//				System.out.println("expression value: " + expression);
		expressions.add(expression);
	    }
	}
	return expressions;
    }
    
    /**
     * @author xingjun - 05/12/2008
     * this method is used to retrieve expression value for only one component
     * @param probeSetIds
     * @param columnId
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    public ArrayList<String> getExpressionSortedByGivenProbeSetIds(ArrayList probeSetIds, 
								   int columnId, boolean ascending, int offset, int num) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		// only go to find the expression if probe set ids are provided
		ArrayList<String> expressions = null;
		if (probeSetIds != null && probeSetIds.size() != 0) {
		    ResultSet resSet = null;
		    PreparedStatement prepStmt = null;
		    ParamQuery parQ =
			ArrayDBQuery.getParamQuery("COMPONENT_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
		    String querySQL = parQ.getQuerySQL();
		    
		    // assemble query string
		    // assemble probe set id string
		    String queryString = 
			this.assembleExpressionByGivenProbeSetIdsQueryString(probeSetIds, 
									     querySQL, columnId, ascending, offset, num);
		    //	        System.out.println("getExpressionSortedByGivenProbeSetIds query: " + queryString);
		    
		    // get data
		    try {
	            	// if disconnected from db, re-connected
	            	conn = DBHelper.reconnect2DB(conn);
			
				prepStmt = conn.prepareStatement(queryString);
				prepStmt.setInt(1, columnId);
				if (debug) System.out.println("MySQLArrayDAOImp.getExpressionSortedByGivenProbeSetIds sql = "+prepStmt);
				resSet = prepStmt.executeQuery();
				expressions = 
				    this.formatExpressionByProbeSetIdsResultSet(resSet);
				
				return expressions;
			
			} catch(SQLException se) {
			    se.printStackTrace();
				return null;
			}
			finally{
			    DBHelper.closePreparedStatement(prepStmt);
			    DBHelper.closeResultSet(resSet);
				if (performance) {
				    enter = (System.currentTimeMillis() - enter)/1000;
				    if (2 < enter)
					System.out.println("MySQLArrayDAOImp.getExpressionSortedByGivenProbeSetIds takes " + enter+" seconds");
				}
			}
		}
		return expressions;
    } // end of getExpressionByGivenProbeSetIds
    
    // backup
    public HeatmapData getExpressionByGivenProbeSetIds(ArrayList probeSetIds, 
						       int columnId, boolean ascending, int offset, int num) {
    	long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		// only go to find the expression if probe set ids are provided
		HeatmapData ret = null;
		if (probeSetIds != null && probeSetIds.size() != 0) {
		    HeatmapData expressions = null;
		    ResultSet resSet = null;
		    PreparedStatement prepStmt = null;
		    ParamQuery parQ =
			DBQuery.getParamQuery("ARRAY_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
		    String querySQL = parQ.getQuerySQL();
		    
		    // assemble query string
		    // assemble probe set id string
		    String queryString = 
			this.assembleExpressionByGivenProbeSetIdsQueryString(probeSetIds, 
									     querySQL, columnId, ascending, offset, num);
		    //	        int probeSetNumber = probeSetIds.size();
		    //	        String probeSetString = "WHERE PRS_PROBE_SET_ID IN (";
		    //	        for (int i=0;i<probeSetNumber;i++) {
		    //	        	probeSetString += "'" + probeSetIds.get(i) + "', ";
		    //	        }
		    //	        probeSetString = 
		    //	        	probeSetString.substring(0, (probeSetString.length()-2)) + ")";
		    //	        
		    //	        // assemble sql string
		    //	        String queryString = 
		    //	        	parQ.getQuerySQL().replace("WHERE PRS_PROBE_SET_ID IN", probeSetString);
		    //	        System.out.println("expressionForProbeSet query:" + queryString);
		    
		    // get data
		    try {
	            	// if disconnected from db, re-connected
	            	conn = DBHelper.reconnect2DB(conn);
			
				prepStmt = conn.prepareStatement(queryString);
				if (debug) System.out.println("MySQLArrayDAOImp.getExpressionByGivenProbeSetIds sql = "+prepStmt);
				resSet = prepStmt.executeQuery();
				int probeSetNumber = probeSetIds.size();
				expressions = this.formatExpressionByProbeSetIdsResultSet(resSet, probeSetNumber);
				
				return expressions;
			
			} catch(SQLException se) {
			    se.printStackTrace();
				return null;
			}
			finally{
			    DBHelper.closePreparedStatement(prepStmt);
			    DBHelper.closeResultSet(resSet);
				if (performance) {
				    enter = (System.currentTimeMillis() - enter)/1000;
				    if (2 < enter)
					System.out.println("MySQLArrayDAOImp.getExpressionByGivenProbeSetIds takes " + enter+" seconds");
				}
			}
		}
		return null;
	} // end of getExpressionByGivenProbeSetIds
    
    /**
     * @author xingjun - 09/12/2008
     * <p>modified on 01/05/2009 - only retrieve expression for one component</p>
     * <p>xingjun - 27/10/2009 - modified order clause to make the result shows as the order of given probe set ids</p>
     * @param probeSetIds
     * @param querySQL
     * @param columnId
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    private String assembleExpressionByGivenProbeSetIdsQueryString(ArrayList probeSetIds, String querySQL,
								   int columnId, boolean ascending, int offset, int num) {
		String queryString = null;
		
		// add probe set id criteria
        if (probeSetIds != null && probeSetIds.size() != 0) {
	    String probeSetIdString = "WHERE PRS_PROBE_SET_ID IN (";
	    String probeSetIdList = "";
	    int len = probeSetIds.size();
	    for (int i=0;i<len;i++) {
		probeSetIdString += "'" + probeSetIds.get(i).toString() + "', ";
		probeSetIdList += "'" + probeSetIds.get(i).toString() + "', ";
	    }
	    probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2) + ") ";
	    probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
	    //         	System.out.println("MTExpression probeset string: " + probeSetIdString);
	    queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", probeSetIdString);
	    queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
	    queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", "");
	    queryString = queryString.replace(", FIELD(PRS_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }
	
        // append ascending condition
        if (!ascending) {
            queryString += " DESC ";
        }
	
	// append row number restriction clause
	//		if (offset != 0) {
	//			queryString += " LIMIT " + Integer.toString(offset-1) + ", " + Integer.toString(num);
	//		}
	
	// return value
	if (debug)
	    System.out.println("ArrayDAO:assembleExpressionByGivenProbeSetIdsQueryString:sql: " + queryString);
	
	return queryString;
    }
    
    /**
     * @author xingjun - 09/12/2008
     * <p>overloaded version</p>
     * <p>xingjun - 27/10/2009 - modified order clause to make the result shows as the order of given probe set ids</p>
     * @param probeSetIds
     * @param querySQL
     * @return
     */
    private String assembleExpressionByGivenProbeSetIdsQueryString(ArrayList probeSetIds, String querySQL) {
	String queryString = null;
	
	// add probe set id criteria
        if (probeSetIds != null && probeSetIds.size() != 0) {
	    String probeSetIdString = "WHERE PRS_PROBE_SET_ID IN (";
	    String probeSetIdList = "";
	    int len = probeSetIds.size();
	    for (int i=0;i<len;i++) {
		probeSetIdString += "'" + probeSetIds.get(i).toString() + "', ";
		probeSetIdList += "'" + probeSetIds.get(i).toString() + "', ";
	    }
	    probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2) + ") ";
	    probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
	    //        	System.out.println("MTExpression probeset string: " + probeSetIdString);
	    queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", probeSetIdString);
	    queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
	    queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", "");
	    queryString = queryString.replace(", FIELD(PRS_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }
	
	// return value
	//         System.out.println("ArrayDAO:assembleExpressionByGivenProbeSetIdsQueryString:sql: " + queryString);
	return queryString;
    }
    
    
    /**
     * @author xingjun - 18/03/2009
     */
    public Gene findGeneInfoBySymbol(ArrayList genes) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		if (genes == null || genes.size() == 0) {
		    return null;
		}
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_GENE_BY_SYMBOLID");
        String queryString = parQ.getQuerySQL();
        int len = genes.size();
        String symbolCriteria = "";
        String appendString = " OR GNF_ID = ";
        for (int i=0;i<len;i++) {
		    symbolCriteria += "'" + genes.get(i) + "'" +  appendString;
        }
        symbolCriteria = symbolCriteria.substring(0, (symbolCriteria.length()-appendString.length()));
        queryString += symbolCriteria;
	//        System.out.println("array:findGeneInfoBySymbol query: " + queryString);
	
        PreparedStatement prepStmt = null;
        Gene gene = null;
        try {
            prepStmt = conn.prepareStatement(queryString);
		    if (debug) System.out.println("MySQLArrayDAOImp.findGeneInfoBySymbol sql = "+prepStmt);
            resSet = prepStmt.executeQuery();
	    
            if (resSet.first()) {
                gene = new Gene();
                gene.setSymbol(resSet.getString(1));  
                gene.setName(resSet.getString(2));
            }
			return gene;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.findGeneInfoBySymbol takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * @author xingjun - 11/08/2009
     */
    public MasterTableInfo[] getMasterTableList(boolean isMaster) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		MasterTableInfo[] masterTableList = null;
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("MASTER_TABLE_LIST");
		PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    resSet = prepStmt.executeQuery();
		    
		    masterTableList = formatMasterTableResultSet(resSet);
		    
			return masterTableList;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp. getMasterTableListtakes " + enter+" seconds");
			}
		}
    }
    
    /**
     * @author xingjun - 11/08/2009
     * <p>xingjun - 03/02/2010 - added an extra property for master table entity</p>
     */
    private MasterTableInfo[] formatMasterTableResultSet(ResultSet resSet) throws SQLException {
	if (resSet.first()) {
	    resSet.beforeFirst();
	    ArrayList<MasterTableInfo> masterTableList = new ArrayList<MasterTableInfo>();
	    while (resSet.next()) {
		MasterTableInfo masterTableInfo = new MasterTableInfo();
		masterTableInfo.setId(resSet.getString(1));
		masterTableInfo.setMasterId(resSet.getString(1));// xingjun - 02/03/2010
		masterTableInfo.setPlatform(resSet.getString(2));
		masterTableInfo.setTitle(resSet.getString(3));
		masterTableInfo.setDescription(resSet.getString(4));
		masterTableList.add(masterTableInfo);
	    }
	    return masterTableList.toArray(new MasterTableInfo[masterTableList.size()]);
	}
	return null;
    }
    
    /**
     * @author xingjun - 03/02/2010
     * @return
     */
    public MasterTableInfo[] getMasterTableList() {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		MasterTableInfo[] masterSectionList = null;
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("MASTER_SECTION_LIST");
		PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    resSet = prepStmt.executeQuery();
		    
		    masterSectionList = formatMasterSectionResultSet(resSet);
		    
			return masterSectionList;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getMasterTableList takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * @author xingjun - 03/02/2010
     */
    private MasterTableInfo[] formatMasterSectionResultSet(ResultSet resSet) throws SQLException {
	if (resSet.first()) {
	    resSet.beforeFirst();
	    ArrayList<MasterTableInfo> masterTableList = new ArrayList<MasterTableInfo>();
	    while (resSet.next()) {
		MasterTableInfo masterTableInfo = new MasterTableInfo();
		masterTableInfo.setId(resSet.getString(1) + "_" + resSet.getString(2));
		masterTableInfo.setMasterId(resSet.getString(1));
		masterTableInfo.setSectionId(resSet.getString(2));
		masterTableInfo.setTitle(resSet.getString(3));
		masterTableInfo.setDescription(resSet.getString(4));
		masterTableInfo.setPlatform(resSet.getString(5));
		masterTableList.add(masterTableInfo);
	    }
	    return masterTableList.toArray(new MasterTableInfo[masterTableList.size()]);
	}
	return null;
    }
    
    /**
     * <p>xingjun - 03/02/2010 
     * - need parse input masterTableId before proceeding to retrieve platform id</p>
     */
    public String getMasterTablePlatformId(String masterTableId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		String platformId = null;
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("MASTER_TABLE_PLATFORM_ID");
		PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    // xingjun - 03/02/2010 - start
		    String masterId = Utility.parseMasterTableId(masterTableId)[0];
		    prepStmt.setInt(1, Integer.parseInt(masterId));
		    // xingjun - 03/02/2010 - finish
		    
		    
		    System.out.println("MySQLArrayDAOImp.getMasterTablePlatformId platformId query = " + prepStmt);
		    resSet = prepStmt.executeQuery();
		    
		    if (resSet.first()) {
			platformId = resSet.getString(1);
		    }
		    
			return platformId;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getMasterTablePlatformId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * @author xingjun - 24/08/2009
     */
    public ArrayList<String> getProbeSetIdBySymbols(String[] symbols, String platformId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList<String> probeSetIds = null;
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOL");
        String queryString = parQ.getQuerySQL();
	
        // put symbol criteria into the query string
        String symbolString = "('";
        for (int i=0;i<symbols.length;i++) {
        	symbolString += symbols[i] + "', '";
        }
        symbolString = symbolString.substring(0, symbolString.length()-3) + ")";
		//        System.out.println("getProbeSetIdBySymbols:symbolString: " + symbolString);
        queryString = queryString.replaceAll("WHERE GNF_SYMBOL = \\?", "WHERE GNF_SYMBOL IN"+symbolString );
		//        System.out.println("getProbeSetIdBySymbols:queryString: " + queryString);
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
	
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, platformId);
		    
		    if (debug) System.out.println("MySQLArrayDAOImp.getProbeSetIdBySymbols sql = "+prepStmt);

		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
				    probeSetIds.add(resSet.getString(1));
				}
		    }
		    
			return probeSetIds;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
			if (performance) {
			    enter = (System.currentTimeMillis() - enter)/1000;
			    if (2 < enter)
				System.out.println("MySQLArrayDAOImp.getProbeSetIdBySymbols takes " + enter+" seconds");
			}
		}
    }

    // added by Bernie - 23/09/2010
    public String findTissueBySubmissionId(String submissionId){
//		System.out.println("MySQLArrayDAOImp-findTissueBySubmissionId for " + submissionId);
		String tissue = null;
        ResultSet resultSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GET_SUBMISSION_TISSUE");
        PreparedStatement prepStmt = null;        
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
		    if (debug)
		    	System.out.println("MySQLArrayDAOImp 1 arg = "+submissionId);
            prepStmt.setString(1, submissionId);
            resultSet = prepStmt.executeQuery();
            if (resultSet.first()) {
                tissue = resultSet.getString(1);
		//                System.out.println("tissue: " + tissue);
            }
			return tissue;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resultSet);
		}
    }

 
    public ArrayList findSampleList(String dataset, String stage, String sample)
    {
        ResultSet resultSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_SAMPLE_LIST");
        PreparedStatement prepStmt = null;  
        
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
	        parQ.setPrepStat(conn);
	        prepStmt = parQ.getPrepStat();
	        prepStmt.setString(1, dataset);
	        prepStmt.setString(2, stage);
	        prepStmt.setString(3, sample);
	        resultSet = prepStmt.executeQuery();
	        
			if (resultSet.first()) {
				
				//need to reset cursor as 'if' move it on a place
				resultSet.beforeFirst();
				
				//create ArrayList to store each row of results in
				ArrayList<String[]> results = new ArrayList<String[]>();
				
				while (resultSet.next()) {
					String[] data = new String[2];
					for (int i = 0; i < 2; i++) {
						data[i] = resultSet.getString(i + 1);
					}
					results.add(data);
				}
				return results;
			}
	    
        }
        catch (SQLException se) {
        se.printStackTrace();
        }

        return null;
    }
    
	public ArrayList<GenelistTreeInfo> getRefGenelists() {
		ArrayList<GenelistTreeInfo> result = null;
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_ALL_REF_GENELISTS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
        	prepStmt = conn.prepareStatement(queryString);
        	resSet = prepStmt.executeQuery();
        	
        	if (resSet.first()) {
        		resSet.beforeFirst();
        		
        		result = new ArrayList<GenelistTreeInfo>();
        		while (resSet.next()) {
        			GenelistTreeInfo genelistTreeInfo = new GenelistTreeInfo();
        			
        			genelistTreeInfo.setGenelistOID(resSet.getString(1));
        			genelistTreeInfo.setGenelistUID(resSet.getString(2));
        			genelistTreeInfo.setName(resSet.getString(3));
        			genelistTreeInfo.setDescription(resSet.getString(4));
        			genelistTreeInfo.setSeriesPlatform(resSet.getString(5));
        			genelistTreeInfo.setPlatformGeoId(resSet.getString(6));
        			genelistTreeInfo.setSample(resSet.getString(7));
        			genelistTreeInfo.setEmapId(resSet.getString(8));
        			genelistTreeInfo.setDataset(resSet.getString(9));
        			genelistTreeInfo.setDatasetId(resSet.getString(10));
        			genelistTreeInfo.setMethod(resSet.getString(11));
        			genelistTreeInfo.setEntityType(resSet.getString(12));
        			genelistTreeInfo.setEntityCount(resSet.getString(13));
        			genelistTreeInfo.setGeneCount(resSet.getString(14));
        			genelistTreeInfo.setAuthor(resSet.getString(15));
        			genelistTreeInfo.setDate(resSet.getString(16));
        			genelistTreeInfo.setVersion(resSet.getString(17));
        			genelistTreeInfo.setReference(resSet.getString(18));
        			genelistTreeInfo.setPublished(Integer.toBinaryString(resSet.getInt(19)));
        			genelistTreeInfo.setOtherRefs(resSet.getString(20));
        			genelistTreeInfo.setStage(resSet.getString(21));
        			genelistTreeInfo.setGenelistType(resSet.getString(22));
        			genelistTreeInfo.setSex(resSet.getString(23));
        			genelistTreeInfo.setSubset1(resSet.getString(24));
        			genelistTreeInfo.setSubset2(resSet.getString(25));
        			genelistTreeInfo.setSubset3(resSet.getString(26));
        			genelistTreeInfo.setAmgId(resSet.getString(27));
        			genelistTreeInfo.setLpuRef(resSet.getString(28));
        			
        			
        			result.add(genelistTreeInfo);
        		}
        	}
        	
			return result;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
	}

	public ArrayList<String> getRefStages(String stage) {
		ArrayList<String> stages = null;
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_ALL_REF_STAGES");
        PreparedStatement prepStmt = null;
        
        
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
	        parQ.setPrepStat(conn);
	        prepStmt = parQ.getPrepStat();
	        prepStmt.setString(1, stage);

	        resSet = prepStmt.executeQuery();
        	
		    if (resSet.first()) {
				resSet.beforeFirst();
				stages = new ArrayList<String>();
				while (resSet.next()) {
				    stages.add(resSet.getString(1));
				}
		    }
        	
			return stages;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
	}
	
	public String getRefStageOrder(String stage) {
		String stageOrder = null;
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_REF_STAGE_ORDER");
        PreparedStatement prepStmt = null;
        
        
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
	        parQ.setPrepStat(conn);
	        prepStmt = parQ.getPrepStat();
	        prepStmt.setString(1, stage);

	        resSet = prepStmt.executeQuery();
        	
		    if (resSet.first()) {
				stageOrder = resSet.getString(1);
		    }
        	
			return stageOrder;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
	}

	public String getRefStageFromOrder(String order) {
		String stageDisplay = null;
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_REF_STAGE_FROM_ORDER");
        PreparedStatement prepStmt = null;
        
        
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
	        parQ.setPrepStat(conn);
	        prepStmt = parQ.getPrepStat();
	        prepStmt.setString(1, order);

	        resSet = prepStmt.executeQuery();
        	
		    if (resSet.first()) {
		    	stageDisplay = resSet.getString(1);
		    }
        	
			return stageDisplay;
			
		} catch(SQLException se) {
		    se.printStackTrace();
			return null;
		}
		finally{
		    DBHelper.closePreparedStatement(prepStmt);
		    DBHelper.closeResultSet(resSet);
		}
	}
	
}
