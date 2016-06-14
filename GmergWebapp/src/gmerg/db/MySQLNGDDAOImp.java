/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.Allele;
import gmerg.entities.submission.array.SupplementaryFile;
import gmerg.entities.submission.nextgen.NGDSample;
import gmerg.entities.submission.nextgen.NGDSeries;
import gmerg.entities.submission.nextgen.Protocol;
import gmerg.entities.submission.nextgen.DataProcessing;
import gmerg.entities.submission.nextgen.NGDSupFiles;
import gmerg.db.MySQLArrayDAOImp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


/**
 * @author xingjun
 *
 */
public class MySQLNGDDAOImp extends MySQLArrayDAOImp implements NGDDAO {
    protected boolean debug = false;
    protected boolean performance = true;
    Connection conn;
    
    // default constructor
    public MySQLNGDDAOImp() {
	
    }
    
    // constructor with connection initialisation
    public MySQLNGDDAOImp(Connection conn) {
    	this.conn = conn;
    }
    
	public String findSpeciesBySubmissionId(String submissionAccessionId){

		
		if (submissionAccessionId == null) {
		    return null;
		}
		
		String species = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("NGD_SPECIES");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    prepStmt.setString(1, submissionAccessionId);
		    
		    if (debug)
		    	System.out.println("MySQLNGDDAOImp.findSpeciesBySubmissionId = "+prepStmt);
		    
		    resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				species = resSet.getString(1);
			}
			return species;

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
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SUPPLEMENTARY_FILES_NGD");
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLNGDDAOImp.findSupplementaryFileInfoBySubmissionId 1 arg = "+submissionAccessionId);
		    prepStmt.setString(1, submissionAccessionId);
		    //prepStmt.setString(2, submissionAccessionId);//DEREK TO FORCE COUNT IN FIRST ROW
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
				System.out.println("MySQLNGDDAOImp.findSupplementaryFileInfoBySubmissionId takes " + enter+" seconds");
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
	    List<NGDSupFiles> rFiles=new ArrayList<NGDSupFiles>();
	    List<NGDSupFiles> pFiles=new ArrayList<NGDSupFiles>();
	    NGDSupFiles ngdsupfiles=null;
	    StringBuffer filesize=new StringBuffer();
	    do
	    {
	    	filesize.setLength(0);
	    	if(resSet.getString(4)!=null)
	    		filesize.append("("+resSet.getString(4)+")");
	    		
	    	if(resSet.getString(3).equalsIgnoreCase("raw"))
	    	{
	    		ngdsupfiles=new NGDSupFiles();
	    		ngdsupfiles.setFilename(resSet.getString(2).trim());
    	    	ngdsupfiles.setFilesize(filesize.toString());
    	    	ngdsupfiles.setFiletype(resSet.getString(3).trim());
	    		rFiles.add(ngdsupfiles);//1.NGF_FILEPATH; 2.NGF_FILENAME; 3.NGF_RAW; 4.NGF_FILESIZE
	    	}
	    	else if (resSet.getString(3).equalsIgnoreCase("processed"))
	    	{
	    		ngdsupfiles=new NGDSupFiles();
	    		ngdsupfiles.setFilename(resSet.getString(2).trim());
    	    	ngdsupfiles.setFilesize(filesize.toString());
    	    	ngdsupfiles.setFiletype(resSet.getString(3).trim());
	    		pFiles.add(ngdsupfiles);//1.NGF_FILEPATH; 2.NGF_FILENAME; 3.NGF_RAW; 4.NGF_FILESIZE
	    	}
		    
	    }
	    while(resSet.next());
	    
	    if (0 == pFiles.size())
	    	supplementaryFile.setProcessedFile(null);
	    else 
	    	supplementaryFile.setProcessedFile(pFiles);
	    
	    if (0 == rFiles.size())
	    	supplementaryFile.setRawFile(null);
	    else 
	    	supplementaryFile.setRawFile(rFiles);
	    
	    return supplementaryFile;
	}
	return null;
  }
    
 
    
    /**
     * <p> modified by xingjun - 18/11/2009 - relevant sample info might be null</p>
     * @param submissionAccessionId
     * @return Sample
     */
    public NGDSample findSampleBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		//		System.out.println("ArrayDAO:findSampleBySubmissionId:submissionAccessionId: " + submissionAccessionId);
		NGDSample sample = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SAMPLE_NGD");
		String queryString = parQ.getQuerySQL();
		//		System.out.println("ArrayDAO:findSampleBySubmissionId:sql: " + queryString);
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLNGDDAOImp.sql = "+queryString.toLowerCase() + " 1 arg = "+submissionAccessionId);
	
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
				System.out.println("MySQLNGDDAOImp.findSampleBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
    /**
     * <p>modified by xingjun - 18/06/2008 - we might got more than one source record</p>
     * <p>xingjun - 18/11/2009 - sampleAnatomy might be empty</p> 
     * @param sample
     * @param id
     */
    public void formatSampleAnatomy(NGDSample sample, String id) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("NGD_SAMPLE_SOURCE");
		PreparedStatement prepStmt = null;
		//		System.out.println("ArrayDAO:formatSampleAnatomy:sql: " + parQ.getQuerySQL());
		//		System.out.println("ArrayDAO:formatSampleAnatomy:id: " + id);
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLNGDDAOImp.formatSampleAnatomy 1 arg = "+id);
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
				System.out.println("MySQLNGDDAOImp.formatSampleAnatomy takes " + enter+" seconds");
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
    private NGDSample formatSampleResultSet(ResultSet resSet) throws SQLException {
	if (resSet.first()) {
	    NGDSample sample = new NGDSample();
	    sample.setGeoID(resSet.getString(1));
	    sample.setDescription(resSet.getString(2));
	    sample.setTitle(resSet.getString(3));
	    sample.setOrganism(resSet.getString(4));
	    sample.setStrain(resSet.getString(5));
	    sample.setGenotype(resSet.getString(6));
	    sample.setSex(resSet.getString(7));
	    sample.setDevAge(resSet.getString(8));
	    sample.setTheilerStage(resSet.getString(9));
	    sample.setPooledSample(resSet.getString(10));
	    sample.setPoolSize(resSet.getString(11));
	    sample.setExperimentalDesign(resSet.getString(12));
	    sample.setSampleNotes(resSet.getString(13));
	    sample.setLibraryPoolSize(resSet.getString(14));
	    sample.setLibraryReads(resSet.getString(15));
	    sample.setReadLength(resSet.getString(16));
	    sample.setMeanInsertSize(resSet.getString(17));
	    
	    return sample;
	}
	return null;
    }
    
    /**
     * 
     * @param submissionAccessionId
     * @return Series
     */
    public NGDSeries findSeriesBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		
		NGDSeries series = null;
		ResultSet resSetSeries = null;
		ResultSet resSetSampleNumber = null;
		ParamQuery parQSeries = DBQuery.getParamQuery("NGD_SUBMISSION_SERIES");
		ParamQuery parQSampleNumber = DBQuery.getParamQuery("NGD_SAMPLE_NUMBER_OF_SERIES");
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
			System.out.println("MySQLNGDDAOImp 1 arg = "+submissionAccessionId);
		    prepStmtSeries.setString(1, submissionAccessionId);
		    resSetSeries = prepStmtSeries.executeQuery();
		    
		    // sample number
		    parQSampleNumber.setPrepStat(conn);
		    prepStmtSampleNumber = parQSampleNumber.getPrepStat();
		    if (debug)
			System.out.println("MySQLNGDDAOImp 1 arg = "+submissionAccessionId);
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
    private NGDSeries formatSeriesResultSet(ResultSet resSetSeries, ResultSet resSetSampleNumber) throws SQLException {
	if (resSetSeries.first()) {
	    // series
	    NGDSeries series = new NGDSeries();
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
     */
    public ArrayList findSamplesInCertainSeriesBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		ArrayList relatedSamples = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("NGD_SERIES_SAMPLE");
		PreparedStatement prepStmt = null;
		//		System.out.println("series sample query: " + parQ.getQuerySQL());
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    if (debug)
			System.out.println("MySQLNGDDAOImp 1 arg = "+submissionAccessionId);
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
				System.out.println("MySQLNGDDAOImp.findSamplesInCertainSeriesBySubmissionId takes " + enter+" seconds");
			}
		}
    }
    
 
	
	public Protocol findProtocolBySubmissionId(String submissionAccessionId) {
		long enter = 0;
		if (performance)
		    enter = System.currentTimeMillis();
		
		
		if (submissionAccessionId == null) {
		    //			throw new NullPointerException("id parameter");
		    return null;
		}
		Protocol protocol = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROTOCOL_SAMPLE_NGD");
		String queryString = parQ.getQuerySQL();
		//		System.out.println("ArrayDAO:findSampleBySubmissionId:sql: " + queryString);
		PreparedStatement prepStmt = null;
		
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    if (debug)
			System.out.println("MySQLNGDDAOImp.sql = "+queryString.toLowerCase() + " 1 arg = "+submissionAccessionId);
	
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, submissionAccessionId);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    protocol = formatProtocolResultSet(resSet); 
		   
		    
			return protocol;
			
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
				System.out.println("MySQLNGDDAOImp.findSampleBySubmissionId takes " + enter+" seconds");
			}
		}
    }
	
	 private Protocol formatProtocolResultSet(ResultSet resSet) throws SQLException {
			if (resSet.first()) {
				Protocol protocol = new Protocol();
				protocol.setName(resSet.getString(1));
				protocol.setLibraryConProt(resSet.getString(2));
				protocol.setLibraryStrategy(resSet.getString(3));
				protocol.setExtractedMolecule(resSet.getString(4));
				protocol.setRnaIsolationMethod(resSet.getString(5));
				protocol.setSequencingMethod(resSet.getString(6));
				protocol.setLabelMethod(resSet.getString(7));
				protocol.setInstrumentModel(resSet.getString(8));
				protocol.setPairedEnd(resSet.getString(9));
				protocol.setProtAdditionalNotes(resSet.getString(10));
				protocol.setDnaExtractMethod(resSet.getString(11));
				protocol.setAntibody(resSet.getString(12));
				protocol.setCreatedBy(resSet.getString(13));
			    
			    return protocol;
			}
			return null;
	}
	 
/*	 public DataProcessing findDataProcessingBySubmissionId(String submissionAccessionId) {
			long enter = 0;
			if (performance)
			    enter = System.currentTimeMillis();
			
			
			if (submissionAccessionId == null) {
			    //			throw new NullPointerException("id parameter");
			    return null;
			}
			DataProcessing dataProcessing = null;
			ResultSet resSet = null;
			ParamQuery parQ = DBQuery.getParamQuery("DATA_PROCESSING_SAMPLE_NGD");
			String queryString = parQ.getQuerySQL();
			//		System.out.println("ArrayDAO:findSampleBySubmissionId:sql: " + queryString);
			PreparedStatement prepStmt = null;
			
			try {
			    // if disconnected from db, re-connected
			    conn = DBHelper.reconnect2DB(conn);
			    
			    if (debug)
				System.out.println("MySQLNGDDAOImp.sql = "+queryString.toLowerCase() + " 1 arg = "+submissionAccessionId);
		
			    prepStmt = conn.prepareStatement(queryString);
			    prepStmt.setString(1, submissionAccessionId);
			    
			    // execute
			    resSet = prepStmt.executeQuery();
			    dataProcessing = formatDataProcessingResultSet(resSet); 
			   
			    
				return dataProcessing;
				
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
					System.out.println("MySQLNGDDAOImp.findSampleBySubmissionId takes " + enter+" seconds");
				}
			}
	    }*/
	 
	 public DataProcessing[] findDataProcessingBySubmissionId(String submissionAccessionId) {
			long enter = 0;
			if (performance)
			    enter = System.currentTimeMillis();
			
			
			if (submissionAccessionId == null) {
			    //			throw new NullPointerException("id parameter");
			    return null;
			}
			DataProcessing dataProcessing []= null;
			ResultSet resSet = null;
			ParamQuery parQ = DBQuery.getParamQuery("DATA_PROCESSING_SAMPLE_NGD");
			String queryString = parQ.getQuerySQL();
			//		System.out.println("ArrayDAO:findSampleBySubmissionId:sql: " + queryString);
			PreparedStatement prepStmt = null;
			
			try {
			    // if disconnected from db, re-connected
			    conn = DBHelper.reconnect2DB(conn);
			    
			    if (debug)
				System.out.println("MySQLNGDDAOImp.sql = "+queryString.toLowerCase() + " 1 arg = "+submissionAccessionId);
		
			    prepStmt = conn.prepareStatement(queryString);
			    prepStmt.setString(1, submissionAccessionId);
			    
			    // execute
			    resSet = prepStmt.executeQuery();
			    dataProcessing = formatDataProcessingResultSet(resSet); 
			   
			    
				return dataProcessing;
				
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
					System.out.println("MySQLNGDDAOImp.findSampleBySubmissionId takes " + enter+" seconds");
				}
			}
	    }
	 
	 private DataProcessing[] formatDataProcessingResultSet(ResultSet resSet) throws SQLException {
		 	ArrayList<DataProcessing> dataProcessingList = new ArrayList<DataProcessing>();
		 	String str = null;
		 	DataProcessing dataProcessing = null;
			if (resSet.first()) {
				resSet.beforeFirst();
		 	    while (resSet.next()) {
					dataProcessing = new DataProcessing();
					dataProcessing.setProStep(resSet.getString(1));
					dataProcessing.setBuild(resSet.getString(2));
					dataProcessing.setAlignedGenome(resSet.getString(3));
					dataProcessing.setUnalignedGenome(resSet.getString(4));
					dataProcessing.setRnaReads(resSet.getString(5));
					dataProcessing.setFiveThreeRatio(resSet.getString(6));
					dataProcessing.setFormatContent(resSet.getString(7));				
					dataProcessing.setNumberOfReads(resSet.getString(8));
					dataProcessing.setBeforeCleanUpReads(resSet.getString(9));
					dataProcessing.setPairedEnd(resSet.getString(10));
					dataProcessing.setFilename(resSet.getString(11));
					dataProcessing.setFiletype(resSet.getString(12));
					dataProcessing.setRawOrProcessed(resSet.getString(13));
					
					dataProcessingList.add(dataProcessing);
		 	    }
			}
			if (0 == dataProcessingList.size())
			    return null;

	     	return (DataProcessing[])dataProcessingList.toArray(new DataProcessing[0]);
			//return dataProcessing;
	}
	 
	/* private DataProcessing formatDataProcessingResultSet(ResultSet resSet) throws SQLException {
			if (resSet.first()) {
				DataProcessing dataProcessing = new DataProcessing();
				dataProcessing.setProStep(resSet.getString(1));
				dataProcessing.setBuild(resSet.getString(2));
				dataProcessing.setAlignedGenome(resSet.getString(3));
				dataProcessing.setUnalignedGenome(resSet.getString(4));
				dataProcessing.setRnaReads(resSet.getString(5));
				dataProcessing.setFiveThreeRatio(resSet.getString(6));
				dataProcessing.setFormatContent(resSet.getString(7));				
				dataProcessing.setNumberOfReads(resSet.getString(8));
				dataProcessing.setBeforeCleanUpReads(resSet.getString(9));
				dataProcessing.setPairedEnd(resSet.getString(10));
			    
			    return dataProcessing;
			}
			return null;
	}*/
    
}
