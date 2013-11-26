package gmerg.db;

import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.Gene;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.Allele;
import gmerg.entities.submission.ImageInfo;
import gmerg.entities.submission.ImageDetail;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Probe;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.StatusNote;
import gmerg.entities.submission.LockingInfo;
import gmerg.utils.Utility;

import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import java.sql.*;

import java.text.DateFormat;

/**
 * @author xingjun
 *
 */
public class MySQLISHDAOImp implements ISHDAO {
    private boolean debug = false;
    private Connection conn;
    
    // default constructor
    public MySQLISHDAOImp() {
    }
    
    // constructor with connection initialisation
    public MySQLISHDAOImp(Connection conn) {
        this.conn = conn;
    }
    
    
    /**
     * @param resSet
     * @return
     * @throws SQLException
     */
    private ISHBrowseSubmission[] formatBrowseResultSet(ResultSet resSet) throws SQLException {
	
    	if (resSet.first()) {
            resSet.last();
            int arraySize = resSet.getRow();
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();
	    
            //create array to store each row of results in
            ISHBrowseSubmission[] results = new ISHBrowseSubmission[arraySize];
            int i = 0;
            while (resSet.next()) {
                // option 1: initialise a ish browse submission object and add it into the array
                // false is the default value of the 'selected'
                ISHBrowseSubmission ishBrowseSubmission =
                    new ISHBrowseSubmission();
                ishBrowseSubmission.setId(resSet.getString(1));
                ishBrowseSubmission.setGeneSymbol(resSet.getString(2));
                ishBrowseSubmission.setStage(resSet.getString(3));
                ishBrowseSubmission.setAge(resSet.getString(4));
                ishBrowseSubmission.setLab(resSet.getString(5));
                ishBrowseSubmission.setSpecimen(resSet.getString(6));
                ishBrowseSubmission.setAssay(resSet.getString(7));
                ishBrowseSubmission.setDate(resSet.getString(8));
                ishBrowseSubmission.setThumbnail(resSet.getString(9));
                ishBrowseSubmission.setImageName(resSet.getString(10));
                ishBrowseSubmission.setSelected(false);
                results[i] = ishBrowseSubmission;
                i++;
            }
            return results;
        }
        return null;
    }
    
    /**
     * return total number of submissions
     */
    public String getTotalNumberOfSubmission() {
        String totalNumber = new String("");
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION");
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
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * 
     */
    public String getCollectionTotalsSubmissionISHQuerySection(String [] submissionIds) {
        if(submissionIds == null)
	    return "";
       	String  submissionIdsClause = " ";
	int submissionNumber = submissionIds.length;
    	if (submissionNumber == 1) {
	    submissionIdsClause += "AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
    	} else {
	    submissionIdsClause += "AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
	    for (int i = 1; i < submissionNumber; i++) {
    	      	submissionIdsClause += ", '" + submissionIds[i] + "'";
    	    }
    	    submissionIdsClause += ") ";
    	}
    	return submissionIdsClause;
    }	  
    
    /**
     * @param param - string value to specify the criteria of the query therein
     * @param query - query names
     * @return query names and query results stored in a 2-dim array
     */
    public String[][] getStringArrayFromBatchQuery(String[][] param,
                                                   String[] query) {
    	return getStringArrayFromBatchQuery(param, query, "");
    }
    
    /**
     * @param param - string value to specify the criteria of the query therein
     * @param query - query names
     * @param endingClause - a string to append at the end of all queries
     * @return query names and query results stored in a 2-dim array
     */
    public String[][] getStringArrayFromBatchQuery(String[][] param,
                                                   String[] query, String endingClause) {
        int queryNumber = query.length;
        String[][] result = new String[queryNumber][2];
        String[] queryString = new String[queryNumber];
	
        // get the query sql based on query name and record query name in result string array
        for (int i = 0; i < queryNumber; i++) {
            ParamQuery parQ = DBQuery.getParamQuery(query[i]);
            queryString[i] = parQ.getQuerySQL() + endingClause;
            result[i][0] = query[i];
        }
	
        // start to execute query
        //		Connection conn = DBUtil.getDBConnection();
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            for (int i = 0; i < queryNumber; i++) {
				
                prepStmt = conn.prepareStatement(queryString[i]);
                if (param != null &&
                    param[i] != null) { // set query criteria if it's not null
                    int parameterNumber = param[i].length;
                    for (int j = 0; j < parameterNumber; j++) {
                        prepStmt.setString(j + 1, param[i][j]);
                    }
                }
				if (debug)
				    System.out.println("MySQLISHDAOImp:getStringArrayFromBatchQuery["+i+"] = "+prepStmt);
                resSet = prepStmt.executeQuery();
                result[i][1] = getStringValueFromIntegerResultSet(resSet);
            }
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
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
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param submissionAccessionId
     * @return submission vo
     * modified by xingjun - 15/07/2008 - need more info used to duplicate submission
     */
    public Submission findSubmissionById(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
        	return null;
        }
	
        Submission submissionInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_DETAILS");
        String queryString = parQ.getQuerySQL();
//        System.out.println("ISHDAO:findSubmissionById:sql: " + queryString);
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
            prepStmt.setString(1, submissionAccessionId);
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:findSubmissionById resSet= "+prepStmt);
	    
            // execute
            resSet = prepStmt.executeQuery();

            // get notes
            parQ = DBQuery.getParamQuery("SUBMISSION_NOTES");
            queryString = parQ.getQuerySQL();
	    
            prepStmt = conn.prepareStatement(queryString);
            prepStmt.setString(1, submissionAccessionId);
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:findSubmissionById resSetNote= "+prepStmt);
            ResultSet resSetNote = prepStmt.executeQuery();
	    
            submissionInfo = formatSubmissionResultSet(resSet, resSetNote);
	    
            // release the resource
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return submissionInfo;
    }
    
    
    /**
     * @param resSet
     * @return
     * @throws SQLException
     * modified by xingjun - 15/07/2008 - need more info used to duplicate submission
     */
    public Submission formatSubmissionResultSet(ResultSet resSet, ResultSet noteSet) throws SQLException {
        Submission submissionInfo = null;
        if (resSet.first()) {
            submissionInfo = new Submission();
            submissionInfo.setAccID(resSet.getString(1));
            submissionInfo.setStage(resSet.getString(2));
            submissionInfo.setAssayType(resSet.getString(3));
            submissionInfo.setPublicFlag(resSet.getInt(7));
            submissionInfo.setArchiveId(resSet.getString(8));
	    
            submissionInfo.setDeletedFlag(resSet.getInt(9));
            submissionInfo.setSubmitterId(resSet.getInt(10));
            submissionInfo.setPiId(resSet.getInt(11));
            submissionInfo.setEntryBy(resSet.getInt(12));
            submissionInfo.setModifierId(resSet.getInt(13));
            submissionInfo.setDbStatusId(resSet.getInt(14));
            submissionInfo.setProject(resSet.getString(15));
            submissionInfo.setBatchId(resSet.getInt(16));
            submissionInfo.setNameSpace(resSet.getString(17));
            submissionInfo.setOsAccession(resSet.getString(18));
            submissionInfo.setLoaclId(resSet.getString(19));
            submissionInfo.setSource(resSet.getString(20));
            submissionInfo.setValidation(resSet.getString(21));
            submissionInfo.setControl(resSet.getInt(22));
            submissionInfo.setAssessment(resSet.getString(23));
            submissionInfo.setConfidencLevel(resSet.getInt(24));
            submissionInfo.setLocalDbName(resSet.getString(25));
            submissionInfo.setLabId(resSet.getString(26));
            submissionInfo.setEuregeneId(resSet.getString(27));
	    
        }
        if (null != noteSet && null != submissionInfo) {
		    String str = null;
		    List list = new ArrayList<String>();
		    while (noteSet.next()) {
				str = noteSet.getString(2);
				if (null != str) {
				    str = str.trim();
				    if (str.equals(""))
				    	str = null;
				}
				if (null != str && str.equalsIgnoreCase("result")) {
				    str = noteSet.getString(1);
				    if (null != str) {
						str = str.trim();
						if (!str.equals("") && !list.contains(str))
						    list.add(str);
				    }
				}
		    }

		    if (0 == list.size())
		    	submissionInfo.setResultNotes(null);
		    else 
		    	submissionInfo.setResultNotes((String[])list.toArray(new String[0]));
		}

        return submissionInfo;
    }
    
    public Probe findMaProbeByProbeId(String probeId, String maprobeId){
    	
//    	if (debug) 
    		System.out.println("probeId = "+ probeId + " maprobeId = " + maprobeId);

    	if(probeId == null){
		    System.out.println("MySQLISHDAOImp.findMaProbeByProbeId !!!possible error: null probe id");
		    return null;
    	}
	
    	Probe maProbe = null;
	
    	PreparedStatement prepStmtProbe = null;
    	ResultSet resSetProbe = null;
    	ParamQuery parQProbe = null;
    	if (maprobeId == null || maprobeId == ""){
    		parQProbe = DBQuery.getParamQuery("MAPROBE_DETAILS");
    	}
    	else{
    		parQProbe = DBQuery.getParamQuery("MAPROBE_DETAILS_EXTRA");	    
    	}
	
    	if (debug) 
    		System.out.println("MySQLISHDAOImp.findMaProbeByProbeId sql = "+parQProbe.getQuerySQL());

        PreparedStatement prepStmtProbeNote = null;
        ResultSet resSetProbeNote = null;
        ParamQuery parQProbeNote = DBQuery.getParamQuery("MAPROBE_PRBNOTE");
	
    	//Bernie 28/06/2011 Mantis 558 Task5 added to query to get notes
        PreparedStatement prepStmtMaprobeNote = null;
        ResultSet resSetMaprobeNote = null;
        ParamQuery parQMaprobeNote = DBQuery.getParamQuery("MAPROBE_NOTE");
	
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
            parQProbe.setPrepStat(conn);
		    prepStmtProbe = parQProbe.getPrepStat();
		    prepStmtProbe.setString(1, probeId);
		    if (maprobeId != null)
		    	prepStmtProbe.setString(2, maprobeId);
		    if (debug)
		    	System.out.println("prepStmtProbe = "+ prepStmtProbe);
		    resSetProbe = prepStmtProbe.executeQuery();
	    
            // probe note -- Mantis 558 Task5
            parQProbeNote.setPrepStat(conn);
            prepStmtProbeNote = parQProbeNote.getPrepStat();
            prepStmtProbeNote.setString(1, maprobeId.replace("maprobe:", ""));
		    if (debug)
		    	System.out.println("prepStmtProbeNote = "+ prepStmtProbeNote);
            resSetProbeNote = prepStmtProbeNote.executeQuery();
	    
            // curator note -- Mantis 558 Task5
            parQMaprobeNote.setPrepStat(conn);
            prepStmtMaprobeNote = parQMaprobeNote.getPrepStat();
            prepStmtMaprobeNote.setString(1, maprobeId.replace("maprobe:", ""));
		    if (debug)
		    	System.out.println("prepStmtMaprobeNote = "+ prepStmtMaprobeNote);
            resSetMaprobeNote = prepStmtMaprobeNote.executeQuery();
	    
	    
            conn.commit();
            conn.setAutoCommit(true);
	    
            maProbe = this.formatProbeResultSet(resSetProbe, resSetProbeNote, resSetMaprobeNote, null);
	    
    	}
    	catch(SQLException e){
	    e.printStackTrace();
    	}
    	finally {
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmtProbe);
            DBHelper.closePreparedStatement(prepStmtProbeNote);
            DBHelper.closePreparedStatement(prepStmtMaprobeNote); 
    	}
	
    	return maProbe;
    }
    
    /**
     * @param submissionAccessionId
     */
    public Probe findProbeBySubmissionId(String submissionAccessionId) {
	
        if (submissionAccessionId == null) {
	    System.out.println("MySQLISHDAOImp.findProbeBySubmissionId  !!!possible error: try to get probe of a submission whose accession is null");
	    return null;
        }
	
        Probe probeInfo = null;
        ResultSet resSetProbe = null;
        ResultSet resSetProbeNote = null;
        ParamQuery parQProbe = DBQuery.getParamQuery("SUBMISSION_PROBE");
        ParamQuery parQProbeNote = DBQuery.getParamQuery("SUBMISSION_PRBNOTE");
        PreparedStatement prepStmtProbe = null;
        PreparedStatement prepStmtProbeNote = null;
	
        // added by xingjun --- 02.05/2007
        // add maprobe notes into the probe info object
        ResultSet resSetMaprobeNote = null;
        ParamQuery parQMaprobeNote = DBQuery.getParamQuery("SUBMISSION_MAPROBE_NOTE");
        PreparedStatement prepStmtMaprobeNote = null;
	
	//added by ying probe full sequence
        ResultSet resSetFullSequence = null;
        ParamQuery parQFullSequence = DBQuery.getParamQuery("SUBMISSION_FULL_SEQUENCE");
        PreparedStatement prepStmtFullSequence = null;
	
	if (debug) 
	    System.out.println(" to do sql for rna probe: " + parQProbe.getQuerySQL());

        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
            // probe
            parQProbe.setPrepStat(conn);
            prepStmtProbe = parQProbe.getPrepStat();
	    if (-1 == submissionAccessionId.indexOf("GUDMAP:"))
		prepStmtProbe.setString(1, "GUDMAP:"+submissionAccessionId);
	    else
		prepStmtProbe.setString(1, submissionAccessionId);
            resSetProbe = prepStmtProbe.executeQuery();
	    
            // probe note
            parQProbeNote.setPrepStat(conn);
            prepStmtProbeNote = parQProbeNote.getPrepStat();
            prepStmtProbeNote.setString(1, submissionAccessionId);
            resSetProbeNote = prepStmtProbeNote.executeQuery();
	    
            // maprobe note --- 02/05/2007
            parQMaprobeNote.setPrepStat(conn);
            prepStmtMaprobeNote = parQMaprobeNote.getPrepStat();
            prepStmtMaprobeNote.setString(1, submissionAccessionId);
            resSetMaprobeNote = prepStmtMaprobeNote.executeQuery();
	    
	    // maprobe full sequence
            parQFullSequence.setPrepStat(conn);
            prepStmtFullSequence = parQFullSequence.getPrepStat();
            prepStmtFullSequence.setString(1, submissionAccessionId);
            resSetFullSequence = prepStmtFullSequence.executeQuery();
	    
            conn.commit();
            conn.setAutoCommit(true);
	    
            // assemble
            probeInfo = formatProbeResultSet(resSetProbe, resSetProbeNote, resSetMaprobeNote, resSetFullSequence);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmtProbe);
            DBHelper.closePreparedStatement(prepStmtProbeNote);
            DBHelper.closePreparedStatement(prepStmtMaprobeNote); // --- 02/05/2007
            DBHelper.closePreparedStatement(prepStmtFullSequence);
        } catch (SQLException se) {
            se.printStackTrace();
        }

	if (null == probeInfo)
	    System.out.println("MySQLISHDAOImp.findProbeBySubmissionId !!!possible error: submission "+submissionAccessionId+" has no probe");
        return probeInfo;
    }
    
    /**
     * <p>modified by xingjun - 14/04/2010 - 
     * modified not to put maprobenotes into the probe object if its an empty string</p>
     */
    private Probe formatProbeResultSet(ResultSet resSetProbe,
                                       ResultSet resSetProbeNote,
                                       ResultSet resSetMaprobeNote,
                                       ResultSet resSetFullSequence) throws SQLException {
        Probe probe = null;
        if (resSetProbe!= null && resSetProbe.first()) {
            probe = new Probe();
            probe.setGeneSymbol(resSetProbe.getString(1));
            probe.setGeneName(resSetProbe.getString(2));
            String probeName = resSetProbe.getString(3);
            probe.setProbeName(probeName);
            probe.setGeneID(resSetProbe.getString(4));
            probe.setSource(resSetProbe.getString(5));
            probe.setStrain(resSetProbe.getString(6));
            probe.setTissue(resSetProbe.getString(7));
            probe.setType(resSetProbe.getString(8));
            probe.setGeneType(resSetProbe.getString(9));
            probe.setLabelProduct(resSetProbe.getString(10));
            probe.setVisMethod(resSetProbe.getString(11));
            probe.setCloneName(resSetProbe.getString(12));
            probe.setGenbankID(resSetProbe.getString(13));
            probe.setMaprobeID(resSetProbe.getString(14));
            probe.setProbeNameURL(resSetProbe.getString(15));
            probe.setGenbankURL(resSetProbe.getString(16));
            probe.setSeqStatus(resSetProbe.getString(17));
            probe.setSeq5Loc(resSetProbe.getString(18));
            probe.setSeq3Loc(resSetProbe.getString(19));
	    
		    String str = null;
	            str = Utility.netTrim(resSetProbe.getString(20));
		    if (null == str)
		    	probe.setSeq5Primer(null);
		    else
		    	probe.setSeq5Primer(str.toUpperCase());
		    
            str = Utility.netTrim(resSetProbe.getString(21));
		    if (null == str)
		    	probe.setSeq3Primer(null);
		    else
		    	probe.setSeq3Primer(str.toUpperCase());
		    
            probe.setGeneIdUrl(resSetProbe.getString(22));	    
            probe.setAdditionalCloneName(resSetProbe.getString(23));
	    
            // xingjun - 19/01/2011
            probe.setLabProbeId(resSetProbe.getString(24));
	    
            // obtain probe note
            if (resSetProbeNote!= null && resSetProbeNote.first()) {
                resSetProbeNote.beforeFirst();
                String notes = new String("");
                while (resSetProbeNote.next()) {
                    str = Utility.netTrim(resSetProbeNote.getString(1));
		    if (null != str)
                    notes += str + " ";
                }
                probe.setNotes(notes.trim());
            }
	    
            if (resSetMaprobeNote!= null && resSetMaprobeNote.first()) {
            	ArrayList<String> maprobeNotes = new ArrayList<String>();
                resSetMaprobeNote.beforeFirst();
                String notes = null;
                String[] separatedNote = null;
                while (resSetMaprobeNote.next()) {
				    notes = Utility.netTrim(resSetMaprobeNote.getString(1));
				    if (null != notes) {
						notes = notes.replaceAll("\\s", " ").trim();
						if (notes.equals(""))
						    notes = null;
				    }
				    if (null != notes) {
                        separatedNote = notes.split("\\|");
                    	if(null != separatedNote && separatedNote.length > 1) {
						    for(int i = 0; i < separatedNote.length; i++) {
						    	maprobeNotes.add(separatedNote[i]);
						    }
                    	} 
                    	else {
						    //                    		System.out.println("note added: " + notes);
						    maprobeNotes.add(notes);
                    	}
                    }
                }
                if (maprobeNotes.size() == 0) {
            		probe.setMaprobeNotes(null);
                } else {
                    probe.setMaprobeNotes(maprobeNotes);
                }
            }
	    
	    // obtain full sequence
            ArrayList <String>seqs = null;
            if (resSetFullSequence!= null && resSetFullSequence.first()) {
            	resSetFullSequence.beforeFirst();
            	String fullSeq = new String();
                while (resSetFullSequence.next()) {
				    fullSeq += resSetFullSequence.getString(1);
                }
                String origin = null;
                int count;                
                if(null != fullSeq) {               
				    seqs = new ArrayList<String>();
				    origin = fullSeq.trim();
				    count = origin.length() / 60;
				    for(int i = 0; i < count; i++) {
				    	seqs.add(origin.substring(i*60, i*60+60));
				    }
	                int remainder = origin.length() / 60;
		    
	                if (remainder > 0) {
	                	seqs.add(origin.substring(count*60));
	                }               	
                }     	
                probe.setFullSequence(seqs);
            } else {
            	probe.setFullSequence(null);
            }
        }

		if (null == probe) 
		    System.out.println("MySQLISHDAOImp.formatProbeResultSet   !!!possible error: null probe");

        return probe;
    }
    
    public Antibody findAntibodyByAntibodyId(String antibodyId) {
        Antibody antibody = null;
        ResultSet resSetAntibody = null;
        ResultSet resSetAntibodyNote = null;
        ResultSet resSetSpeciesSpecificity = null;
        ResultSet resSetAntibodyVariant = null;
        ParamQuery parQAntibody = DBQuery.getParamQuery("ANTIBODY_DETAILS");
        ParamQuery parQAntibodyNote = DBQuery.getParamQuery("MAPROBE_NOTE");
        ParamQuery parQSpeciesSpecificity = DBQuery.getParamQuery("ANTIBODY_SPECIES_SPECIFICITY");
        ParamQuery parQAntibodyVariant = DBQuery.getParamQuery("ANTIBODY_VARIANTS");
        PreparedStatement prepStmtAntibody = null;
        PreparedStatement prepStmtAntibodyNote = null;
        PreparedStatement prepStmtSpeciesSpecificity = null;
        PreparedStatement prepStmtAntibodyVariant = null;
	//        System.out.println("sql for antibody: " + parQAntibody.getQuerySQL());
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
            // antibody
            parQAntibody.setPrepStat(conn);
            prepStmtAntibody = parQAntibody.getPrepStat();
            prepStmtAntibody.setString(1, antibodyId);
            if (debug)
            	System.out.println("MySQLISHDAOImp:findAntibodyByAntibodyId prepStmtAntibody =" + prepStmtAntibody);
            resSetAntibody = prepStmtAntibody.executeQuery();
	    
            // antibody note
            parQAntibodyNote.setPrepStat(conn);
            prepStmtAntibodyNote = parQAntibodyNote.getPrepStat();
            prepStmtAntibodyNote.setString(1, antibodyId);
            if (debug)
            	System.out.println("MySQLISHDAOImp:findAntibodyByAntibodyId prepStmtAntibodyNote =" + prepStmtAntibodyNote);
            resSetAntibodyNote = prepStmtAntibodyNote.executeQuery();

            // species specificity 
            parQSpeciesSpecificity.setPrepStat(conn);
            prepStmtSpeciesSpecificity = parQSpeciesSpecificity.getPrepStat();
            prepStmtSpeciesSpecificity.setString(1, antibodyId);
            if (debug)
            	System.out.println("MySQLISHDAOImp:findAntibodyByAntibodyId prepStmtSpeciesSpecificity =" + prepStmtSpeciesSpecificity);
            resSetSpeciesSpecificity = prepStmtSpeciesSpecificity.executeQuery();
	    
            // antibody variant
            parQAntibodyVariant.setPrepStat(conn);
            prepStmtAntibodyVariant = parQAntibodyVariant.getPrepStat();
            prepStmtAntibodyVariant.setString(1, antibodyId);
            if (debug)
            	System.out.println("MySQLISHDAOImp:findAntibodyByAntibodyId prepStmtAntibodyVariant =" + prepStmtAntibodyVariant);
            resSetAntibodyVariant = prepStmtAntibodyVariant.executeQuery();
	    
            conn.commit();
            conn.setAutoCommit(true);
	    
            // assemble
            antibody = formatAntibodyResultSet(resSetAntibody, null, resSetSpeciesSpecificity, resSetAntibodyVariant);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmtAntibody);
            DBHelper.closePreparedStatement(prepStmtAntibodyNote);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
        return antibody;
    }
    
    /**
     * @return
     */
    public Antibody findAntibodyBySubmissionId(String submissionAccessionId) {
	
        Antibody antibody = null;
        ResultSet resSetAntibody = null;
        ResultSet resSetAntibodyNote = null;
        ResultSet resSetSpeciesSpecificity = null;
        ResultSet resSetAntibodyVariant = null;
        ParamQuery parQAntibody = DBQuery.getParamQuery("SUBMISSION_ANTIBODY");
        ParamQuery parQAntibodyNote = DBQuery.getParamQuery("SUBMISSION_PRBNOTE");
        ParamQuery parQSpeciesSpecificity = DBQuery.getParamQuery("ANTIBODY_SPECIES_SPECIFICITY");
        ParamQuery parQAntibodyVariant = DBQuery.getParamQuery("ANTIBODY_VARIANTS");
        PreparedStatement prepStmtAntibody = null;
        PreparedStatement prepStmtAntibodyNote = null;
        PreparedStatement prepStmtSpeciesSpecificity = null;
        PreparedStatement prepStmtAntibodyVariant = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
            // antibody
            parQAntibody.setPrepStat(conn);
            prepStmtAntibody = parQAntibody.getPrepStat();
            prepStmtAntibody.setString(1, submissionAccessionId);
        	if (debug) 
        	    System.out.println(" to do sql for antibody: " + prepStmtAntibody);
            resSetAntibody = prepStmtAntibody.executeQuery();
	    
            // antibody note
            parQAntibodyNote.setPrepStat(conn);
            prepStmtAntibodyNote = parQAntibodyNote.getPrepStat();
            prepStmtAntibodyNote.setString(1, submissionAccessionId);
        	if (debug) 
        	    System.out.println("to do sql for antibody note: " + prepStmtAntibodyNote);
            resSetAntibodyNote = prepStmtAntibodyNote.executeQuery();
	    
            // species specificity 
            parQSpeciesSpecificity.setPrepStat(conn);
            prepStmtSpeciesSpecificity = parQSpeciesSpecificity.getPrepStat();
            prepStmtSpeciesSpecificity.setString(1, submissionAccessionId);
        	if (debug) 
        	    System.out.println("to sql for antibody specificity: " + prepStmtSpeciesSpecificity);
            resSetSpeciesSpecificity = prepStmtSpeciesSpecificity.executeQuery();
	    
            // antibody variant
            parQAntibodyVariant.setPrepStat(conn);
            prepStmtAntibodyVariant = parQAntibodyVariant.getPrepStat();
            prepStmtAntibodyVariant.setString(1, submissionAccessionId);
        	if (debug) 
        	    System.out.println("sql for antibody variant: " + prepStmtAntibodyVariant);
            resSetAntibodyVariant = prepStmtAntibodyVariant.executeQuery();
	    
            conn.commit();
            conn.setAutoCommit(true);
	    
            // assemble
            antibody = formatAntibodyResultSet(resSetAntibody, resSetAntibodyNote, resSetSpeciesSpecificity, resSetAntibodyVariant);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmtAntibody);
            DBHelper.closePreparedStatement(prepStmtAntibodyNote);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return antibody;
    }
    
    /**
     * @param resSetAntibody
     * @param resSetAntibodyNote
     * @param resSetSpeciesSpecificity
     * @return
     * @throws SQLException
     */
    private Antibody formatAntibodyResultSet(ResultSet resSetAntibody, 
					     ResultSet resSetAntibodyNote, ResultSet resSetSpeciesSpecificity, ResultSet resSetAntibodyVariant) throws SQLException {
    	Antibody antibody = null;
    	if (resSetAntibody.first()) {
	    antibody = new Antibody();
	    // set properties
	    antibody.setMaProbeId(resSetAntibody.getString(1));
	    antibody.setName(resSetAntibody.getString(2));
	    antibody.setAccessionId(resSetAntibody.getString(3));
	    antibody.setGeneSymbol(resSetAntibody.getString(4));
	    antibody.setGeneName(resSetAntibody.getString(5));
	    antibody.setLocusTag(resSetAntibody.getString(6));
	    antibody.setUniprotId(resSetAntibody.getString(7));
	    antibody.setseqStartLocation(resSetAntibody.getInt(8));
	    antibody.setSeqEndLocation(resSetAntibody.getInt(9));
	    
	    String antibodyType = resSetAntibody.getString(10);
	    antibody.setSubtype(antibodyType);
	    // obtain host based on antibody type
	    if (antibodyType.trim().equalsIgnoreCase("monoclonal")) {
		String productionMethod = resSetAntibody.getString(11);
		String cloneId = resSetAntibody.getString(12);
		if (cloneId == null)
		    antibody.setHost(productionMethod);
		else
		    antibody.setHost(productionMethod + " " +cloneId);
	    } else { // antibody type = polyclonal
		antibody.setHost(resSetAntibody.getString(13));
	    }
	    
	    antibody.setPurificationMethod(resSetAntibody.getString(14));
	    antibody.setImmunoglobulinIsotype(resSetAntibody.getString(15));
	    antibody.setChainType(resSetAntibody.getString(16));
	    antibody.setDirectLabel(resSetAntibody.getString(17));
	    antibody.setDetectionNotes(resSetAntibody.getString(18));
	    antibody.setDilution(resSetAntibody.getString(19));
	    antibody.setLabProbeId(resSetAntibody.getString(20));
	    
	    // supplier
	    antibody.setSupplier(resSetAntibody.getString(21));
	    antibody.setCatalogueNumber(resSetAntibody.getString(22));
	    antibody.setLotNumber(resSetAntibody.getString(23));
	    
	    antibody.setSecondaryAntibody(resSetAntibody.getString(24));
	    antibody.setSignalDetectionMethod(resSetAntibody.getString(25));
	    
	    // note
	    if (resSetAntibodyNote != null && resSetAntibodyNote.first()) 
		antibody.setNotes(resSetAntibodyNote.getString(1));
	    
	    String antibodyVariants = null;
	    if (resSetAntibodyVariant.first()) {
		resSetAntibodyVariant.beforeFirst();
		antibodyVariants = new String("");
		while (resSetAntibodyVariant.next()) {
		    antibodyVariants += resSetAntibodyVariant.getString(2) + ", ";
		}
		// remove trailing ',' character
		antibodyVariants = antibodyVariants.substring(0, antibodyVariants.length()-2);
	    }
	    antibody.setDetectedVariantValue(antibodyVariants);
	    
	    String speciesSpecificities = null;
	    if (resSetSpeciesSpecificity.first()) {
		resSetSpeciesSpecificity.beforeFirst();
		speciesSpecificities = new String("");
		while (resSetSpeciesSpecificity.next()) {
		    speciesSpecificities += resSetSpeciesSpecificity.getString(2) + ", ";
		}
		// remove trailing ',' character
		speciesSpecificities = speciesSpecificities.substring(0, speciesSpecificities.length()-2);
	    }
	    antibody.setSpeciesSpecificity(speciesSpecificities);
	    
	    
    	}
	
    	return antibody;
    }
    
    /**
     * @param submissionAccessionId
     */
    public Specimen findSpecimenBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }
        Specimen specimenInfo = null;
        ResultSet resSet = null;
        ResultSet resSetSpecimenNote = null;
        ParamQuery parQSpecimen = DBQuery.getParamQuery("SUBMISSION_SPECIMEN");
        ParamQuery parQSpecimenNote = DBQuery.getParamQuery("SPECIMEN_NOTE");
        PreparedStatement prepStmtSpecimen = null;
        PreparedStatement prepStmtSpecimenNote = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
            // specimen
            parQSpecimen.setPrepStat(conn);
            prepStmtSpecimen = parQSpecimen.getPrepStat();
            prepStmtSpecimen.setString(1, submissionAccessionId);
            resSet = prepStmtSpecimen.executeQuery();
	    
            // specimen note
            parQSpecimenNote.setPrepStat(conn);
            prepStmtSpecimenNote = parQSpecimenNote.getPrepStat();
            prepStmtSpecimenNote.setString(1, submissionAccessionId);
            resSetSpecimenNote = prepStmtSpecimenNote.executeQuery();
            specimenInfo = formatSpecimenResultSet(resSet, resSetSpecimenNote);
	    
            conn.commit();
            conn.setAutoCommit(true);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmtSpecimen);
            DBHelper.closePreparedStatement(prepStmtSpecimenNote);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return specimenInfo;
    }

    /**
     * @param submissionAccessionId
     */
    public Allele[] findAlleleBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }

        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("ALLELE_INFO_BY_SUBMISSION_ID");
        PreparedStatement prepStmt = null;
	Allele[] ret = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
	    // get allele if any
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
            resSet = prepStmt.executeQuery();
	    
	    ret = formatAlleleResultSet(resSet);

            conn.commit();
            conn.setAutoCommit(true);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return ret;
    }
    
    /**
     * @param resSetSpecimen
     * @param resSetSpecimenNote
     * @return
     * @throws SQLException
     */
    private Specimen formatSpecimenResultSet(ResultSet resSetSpecimen,
                                             ResultSet resSetSpecimenNote) throws SQLException {
        Specimen specimen = null;
        if (resSetSpecimen.first()) {
            specimen = new Specimen();
            specimen.setGenotype(resSetSpecimen.getString(1));
            specimen.setAssayType(resSetSpecimen.getString(2));
            specimen.setFixMethod(resSetSpecimen.getString(3));
            specimen.setEmbedding(resSetSpecimen.getString(4));
            specimen.setStrain(resSetSpecimen.getString(5));
            specimen.setStageFormat(resSetSpecimen.getString(6));
            specimen.setOtherStageValue(resSetSpecimen.getString(7));
            specimen.setSex(resSetSpecimen.getString(9));
	    
            List<String> notes = new ArrayList<String>();
            String str = null;
            if (resSetSpecimenNote.first()) {
                resSetSpecimenNote.beforeFirst();
                while (resSetSpecimenNote.next()) {
                    str = Utility.netTrim(resSetSpecimenNote.getString(1));
		    if (null != str)
			notes.add(str);
                }
		
		if (0 == notes.size())
		    specimen.setNotes(null);
		else
		    specimen.setNotes((String[])notes.toArray(new String[0]));
            }
        }
        return specimen;
    }
    
    private Allele[] formatAlleleResultSet(ResultSet resSet)
    throws SQLException {
	if (debug)
	    System.out.println("MySQLISHDAOImp.formatAlleleResultSet");
 	ArrayList<Allele> alleleList = new ArrayList<Allele>();
 	String str = null;
	Allele allele = null;

 	if (resSet.first()) {
		if (debug)
		    System.out.println("MySQLISHDAOImp.formatAlleleResultSet has allele");
 	    resSet.beforeFirst();
 	    while (resSet.next()) {
	 		allele = new Allele();
	 		str = Utility.netTrim(resSet.getString(1));
			allele.setGeneSymbol(str);
	
	 		str = Utility.netTrim(resSet.getString(2));
			allele.setGeneId(str);
	
	 		str = Utility.netTrim(resSet.getString(3));
			allele.setAlleleId(str);
			
			// name first, then lab name
	 		str = Utility.netTrim(resSet.getString(5));
			if (null == str)
			    str = Utility.netTrim(resSet.getString(4));
			allele.setAlleleName(str);
	
	 		str = Utility.netTrim(resSet.getString(7));
			allele.setType(str);
	
	 		str = Utility.netTrim(resSet.getString(8));
			allele.setAlleleFirstChrom(str);
	
	 		str = Utility.netTrim(resSet.getString(9));
			allele.setAlleleSecondChrom(str);
	
	 		str = Utility.netTrim(resSet.getString(10));
			allele.setReporter(str);
	
	 		str = Utility.netTrim(resSet.getString(11));
			allele.setVisMethod(str);
	
			str = Utility.netTrim(resSet.getString(12));
			allele.setNotes(str);
	
	 		alleleList.add(allele);
 	    }
	}
	if (debug)
	    System.out.println("number of allele = "+alleleList.size());

	if (0 == alleleList.size())
	    return null;

     	return (Allele[])alleleList.toArray(new Allele[0]);
     } // end of formatAlleleResultSet

    /**
     * @param submissionAccessionId
     */
    public ArrayList findImageBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
		    //            throw new NullPointerException("id parameter");
		    return null;
        }
        ArrayList ImageInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_IMAGES");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(queryString);
            prepStmt.setString(1, submissionAccessionId);
	    
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:findImageBySubmissionId = "+prepStmt);
            // execute
            resSet = prepStmt.executeQuery();
            ImageInfo = formatImageResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return ImageInfo;
    }
    
    /**
     * - this is a temporay solution
     * - for sharing purpose (with Euregene) we have OPT related process and file extensions of OPT images are different
     * - I presume the file extensions are wlz so modified the code accordingly - filter out wlz files
     * - some images we have are .jpeg files so need to keep the file extension (dont change them to jpg)</p>
     * 
     * 
     * <p>xingjun - 03/06/2011 - 
     * - the fix will cause null pointer exception so keep the original code 
     * - but try to keep file extensions for non-jpg and non-wlz images</p>
     * 
     * <p>xingjun - 01/08/2011 - refer to the comment in the code </p>
     * @param resSetImage
     * @return
     * @throws SQLException
     */
    private ArrayList formatImageResultSet(ResultSet resSetImage) throws SQLException {
        if (resSetImage.first()) {
            resSetImage.beforeFirst();
            int serialNo = 1;
            ArrayList results = new ArrayList();
		    int dotPosition = 0;
		    String fileExtension = null;
		    String str = null;
		    ImageInfo img = null;

            while (resSetImage.next()) {
                img = new ImageInfo();
				str = Utility.netTrim(resSetImage.getString(1));
				if (null != str && !str.equals("")) 
				    img.setAccessionId(str);
				str = Utility.netTrim(resSetImage.getString(2));
				if (null != str && !str.equals("")) 
				    img.setFilePath(str);
				str = Utility.netTrim(resSetImage.getString(3));
				if (null != str && !str.equals("")) 
				    img.setNote(str);
				str = Utility.netTrim(resSetImage.getString(4));
				if (null != str && !str.equals("")) 
				    img.setSpecimenType(str);
				str = Utility.netTrim(resSetImage.getString(5));
				if (null != str && !str.equals("")) 
				    img.setClickFilePath(str);
			
				img.setSerialNo(""+serialNo);
                serialNo++;
                results.add(img);
            }
            return results;
        }
        return null;
    }
    
    /**
     * @param submissionAccessionId
     */
    public String findAuthorBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }
        String authorInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_AUTHOR");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            authorInfo = formatAuthorResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return authorInfo;
    }
    
    /**
     * @param resSet
     * @return
     * @throws SQLException
     */
    private String formatAuthorResultSet(ResultSet resSet) throws SQLException {
        if (resSet.first()) {
            resSet.beforeFirst();
            String authors = new String("");
            while (resSet.next()) {
                authors += resSet.getString(1) + " ";
            }
            return authors.trim();
        }
        return null;
    }
    
    /**
     * @param submissionAccessionId
     */
    /*
    public Person findPIBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }
        Person piInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_PI");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            piInfo = formatPersonResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return piInfo;
    }
    */

    /**
     * @author xingjun - 17/06/2011
     * @param submissionAccessionId
     * @return
     */

    public Person[] findPIsBySubmissionId(String submissionAccessionId) {
    	if (submissionAccessionId == null) {
	    return null;
    	}
        Person[] piInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_PIS");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setInt(1, Integer.parseInt(submissionAccessionId.substring(7)));
	    
            // execute
            resSet = prepStmt.executeQuery();
            piInfo = formatPeopleResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);

	    if (null == piInfo || 0 == piInfo.length) {
		parQ = DBQuery.getParamQuery("SUBMISSION_PI");
		parQ.setPrepStat(conn);
		prepStmt = parQ.getPrepStat();
		prepStmt.setString(1, submissionAccessionId);
		
		// execute
		resSet = prepStmt.executeQuery();
		piInfo = new Person[1];
		piInfo[0] = formatPersonResultSet(resSet);
		if (null == piInfo[0])
		    piInfo = null;
	    
		// close the connection
		DBHelper.closePreparedStatement(prepStmt);
	    }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    	return piInfo;
    }
    
    /**
     * @author xingjun - 17/06/2011
     * @param resSet
     * @return
     * @throws SQLException
     */
    private Person[] formatPeopleResultSet(ResultSet resSet) throws SQLException {
    	ArrayList<Person> people = null;
        if (resSet.first()) {
	    resSet.beforeFirst();
	    people = new ArrayList<Person>();
	    while (resSet.next()) {
                Person person = new Person();
                person.setName(resSet.getString(1));
                person.setLab(resSet.getString(2));
                person.setAddress(resSet.getString(3));
                person.setAddress2(resSet.getString(4));
                person.setEmail(resSet.getString(5));
                person.setCity(resSet.getString(6));
                person.setPostcode(resSet.getString(7));
                person.setCountry(resSet.getString(8));
                person.setPhone(resSet.getString(9));
                person.setFax(resSet.getString(10));
                person.setId(resSet.getString(11));
                people.add(person);
	    }
        }
        if (people != null) {
            Person[] result = people.toArray(new Person[0]);
            return result;
        }
        return null;
    }
    
    /**
     * @param personId
     */
    public Person findPersonById(String personId) {
        if (personId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }
        Person personInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("PERSON_BY_ID");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, personId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            personInfo = formatPersonResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return personInfo;
    }
    
    /**
     *
     * @param resSet
     * @return
     * @throws SQLException
     */
    protected Person formatPersonResultSet(ResultSet resSet) throws SQLException {
        Person pi = new Person();
        if (resSet.first()) {
            pi.setName(resSet.getString(1));
            pi.setLab(resSet.getString(2));
            pi.setAddress(resSet.getString(3));
            pi.setAddress2(resSet.getString(4));
            pi.setEmail(resSet.getString(5));
            pi.setCity(resSet.getString(6));
            pi.setPostcode(resSet.getString(7));
            pi.setCountry(resSet.getString(8));
            pi.setPhone(resSet.getString(9));
            pi.setFax(resSet.getString(10));
            pi.setId(resSet.getString(11));
        } else {
            pi.setName("n/a");
            pi.setLab("");
            pi.setAddress("");
            pi.setAddress2("");
            pi.setEmail("");
            pi.setCity("");
            pi.setPostcode("");
            pi.setCountry("");
            pi.setPhone("");
            pi.setFax("");
            pi.setId("");
        }
        return pi;
    }
    
    /**
     * @param submissionAccessionId
     */
    public Person findSubmitterBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }
        Person submitterInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SUBMITTER");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            submitterInfo = formatPersonResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return submitterInfo;
    }
    
    /**
     * @param submissionAccessionId
     */
    public ArrayList findPublicationBySubmissionId(String submissionAccessionId) {
        if (submissionAccessionId == null) {
	    //            throw new NullPointerException("id parameter");
	    return null;
        }
        ArrayList publicationInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("LINKED_PUBLICATIONS");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            publicationInfo = DBHelper.formatResultSetToArrayList(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return publicationInfo;
    }
    
    /**
     * @param submissionAccessionId
     */
    public String[] findAcknowledgementBySubmissionId(String submissionAccessionId) {
        String[] AcknowledgementInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUB_ACKNOWLEDGEMENTS");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            AcknowledgementInfo = DBHelper.formatResultSetToStringArray(resSet);
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return AcknowledgementInfo;
    }
    
    /**
     * 
     * @param submissionAccessionId
     * @return
     */
    public ArrayList findLinkedSubmissionBySubmissionId(String submissionAccessionId) {
        ArrayList linkedSubmissionInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_OID");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            String subOid = "";
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
            resSet = prepStmt.executeQuery();
            if(resSet.first()) {
                subOid = resSet.getString(1);
            } else {
                return null;
            }
            parQ = DBQuery.getParamQuery("MUS_SUB_LINKED_SUBMISSIONS");
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
	    prepStmt.setString(1, submissionAccessionId);
	    prepStmt.setString(2, subOid);
	    
            // execute
            resSet = prepStmt.executeQuery();
            linkedSubmissionInfo = DBHelper.formatResultSetToArrayList(resSet);
        } catch (SQLException se) {
            se.printStackTrace();
        } 
        finally {
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        }
        return linkedSubmissionInfo;
    }
    
    /**
     * get relevant data on a particular gene
     * @param symbol - the gene symbol to base the search on
     * @param probeSetId - affy probeset id
     * @return Gene - object containing info on the gene
     */
    public Gene findGeneInfoBySymbol(String symbol) {
        if (symbol == null) {
	    //            throw new NullPointerException("gene symbol needed");
	    return null;
        }
        Gene geneInfo = null;
        ResultSet resSet = null;
        ResultSet resSet2 = null;
        ParamQuery parQ = DBQuery.getParamQuery("GENE_INFO");
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmt2 = null; 
	//        System.out.println("gene query:" + parQ.getQuerySQL());
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            // gene info
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, symbol);
            resSet = prepStmt.executeQuery();
	    
            parQ = DBQuery.getParamQuery("TOTAL_GENE_RELATED_ARRAYS");
            parQ.setPrepStat(conn);
            prepStmt2 = parQ.getPrepStat();
            prepStmt2.setString(1, symbol);
            resSet2 = prepStmt2.executeQuery();
	    
            // assemble
            geneInfo = formatGeneInfoResultSet(resSet, resSet2);
            return geneInfo;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
            // release db resources
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closePreparedStatement(prepStmt2);
        }
        return null;
    }
    
    /**
     * @param resSet
     * @return
     * @throws SQLException
     */
    private Gene formatGeneInfoResultSet(ResultSet resSet, ResultSet resSet2) throws SQLException {
        if (resSet.first()) {
            Gene geneInfo = new Gene();
            geneInfo.setSymbol(resSet.getString(1));
            geneInfo.setName(resSet.getString(2));
            geneInfo.setMgiAccID(resSet.getString(3));
	    
            // synonyms string is '|' delimited, need change it to ','
            String synonymString = resSet.getString(4);
            if (synonymString != null  && !synonymString.trim().equals("")) {
            	String[] snm = synonymString.split("\\|");
            	String synonyms = new String("");
            	int len = snm.length;
            	for (int i = 0; i < len; i++) {
		    //                	System.out.println(snm[i]);
		    if(i+1 == len) {
			synonyms += snm[i];
		    } else {
			synonyms += snm[i] + ", ";
		    }
		    //                System.out.println(synonyms);
                }
            	geneInfo.setSynonyms(synonyms);
            } else {
            	geneInfo.setSynonyms("");
            }
            geneInfo.setMgiURL(resSet.getString(5));
            geneInfo.setEnsemblID(resSet.getString(6));
            geneInfo.setEnsemblURL(resSet.getString(7));
            geneInfo.setGoURL(resSet.getString(8));
            geneInfo.setOmimURL(resSet.getString(9));
            geneInfo.setEntrezURL(resSet.getString(10));
            geneInfo.setXsomeStart(resSet.getString(11));
            geneInfo.setXsomeEnd(resSet.getString(12));
            geneInfo.setXsomeName(resSet.getString(13));
            geneInfo.setGenomeBuild(resSet.getString(14));
            geneInfo.setGeneCardURL(resSet.getString(15));
            geneInfo.setHgncSearchSymbolURL(resSet.getString(16));
            geneInfo.setUcscURL(resSet.getString(17)); // added by xingjun - 30/04/2009
            if(resSet2.first()){
                geneInfo.setNumMicArrays(resSet2.getString(1));
            }
            return geneInfo;
        }
        return null;
    }
    
    /**
     * @param symbol
     * @return
     */
    public ArrayList findRelatedSubmissionBySymbolISH(String symbol) {
	if (symbol == null || symbol.equals("")) {
	    return null;
	}
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("GENE_RELATED_SUBMISSIONS_ISH");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
            prepStmt.setString(1, symbol);
            resSet = prepStmt.executeQuery();
            ArrayList<String[]> relatedSubmissionISH =
		DBHelper.formatResultSetToArrayList(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
            return relatedSubmissionISH;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param symbol
     * @return
     */
    public ArrayList findRelatedSubmissionBySymbolArray(String symbol, int columnIndex,
							boolean ascending, int offset,
							int num) {
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("GENE_RELATED_SUBMISSIONS_ARRAY");
        PreparedStatement prepStmt = null;
        String query = parQ.getQuerySQL();
        String defaultOrder = DBQuery.ORDER_BY_PROJECT_ID;
        String queryString =
            assembleSeriesQStringForGene(query, defaultOrder, columnIndex,
                                         ascending, offset, num);
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(queryString);
            prepStmt.setString(1, symbol);
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:findRelatedSubmissionBySymbolArray = "+prepStmt);
            resSet = prepStmt.executeQuery();
            ArrayList relatedSubmissionArray =
                DBHelper.formatResultSetToArrayList(resSet);
            // release db resources
            DBHelper.closePreparedStatement(prepStmt);
            return relatedSubmissionArray;
	    
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     * assembles the query string used to query the db to find data on samples
     * for a particular series
     * @param query
     * @param defaultOrder
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return queryString - the String used to query the db 
     */
    private String assembleSeriesQStringForGene(String query,
                                                String defaultOrder,
                                                int columnIndex,
                                                boolean ascending, int offset,
                                                int num) {
        StringBuffer queryString = new StringBuffer("");
        // orderBy
        if (columnIndex != -1) {
            //add code here to make table columns sortable
        } else {
            queryString.append(query + defaultOrder);
        }
        queryString.append(" LIMIT " + offset + ", " + num);
        return queryString.toString();
    }
    
    /**
     * @param symbol
     * @return
     */
    public ArrayList findRelatedMAProbeBySymbol(String symbol) {
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GENE_RELATED_MAPROBE");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, symbol);
            resSet = prepStmt.executeQuery();
            ArrayList relatedMAProbe =
                DBHelper.formatResultSetToArrayList(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
            return relatedMAProbe;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     * method to get additional info on the gene when it only exists in microarray
     * @param geneInfo - a gene object containing partial data on a gene 
     * @return gene - object containing more complete info on a gene
     */
    public Gene findFurtherGeneInfoForMicroarray(Gene geneInfo) {
        Gene gene = geneInfo;
        if (gene == null) {
            return null;
        }

        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GENE_INFO_FOR_ARRAY");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, gene.getSymbol());
            resSet = prepStmt.executeQuery();
            if(resSet.first()){
		gene.setMgiAccID(resSet.getString(3)); 
		gene.setMgiURL(resSet.getString(4));
		gene.setEnsemblID(resSet.getString(5));
		gene.setEnsemblURL(resSet.getString(6));
		gene.setGoURL(resSet.getString(7));
		gene.setOmimURL(resSet.getString(8));
		gene.setEntrezURL(resSet.getString(9));
		gene.setXsomeStart(resSet.getString(10));
		gene.setXsomeEnd(resSet.getString(11));
		gene.setXsomeName(resSet.getString(12));
		gene.setGenomeBuild(resSet.getString(13));
		gene.setGeneCardURL(resSet.getString(14));
		gene.setHgncSearchSymbolURL(resSet.getString(15));
		
		String syn = resSet.getString(2);
		parQ = DBQuery.getParamQuery("GENE_SYNONYM_INFO_ARRAY");
		parQ.setPrepStat(conn);
		prepStmt = parQ.getPrepStat();
		prepStmt.setString(1, syn);
		//               System.out.println("\n\nsyn is: "+syn);
		resSet = prepStmt.executeQuery();
		if(resSet.first()){
		    resSet.beforeFirst();
		    syn = "";
		    while(resSet.next()){
			if(resSet.isLast()){
			    syn = syn + resSet.getString(1);
			}
			else {
			    syn = syn + resSet.getString(1) + ", ";
			}
		    }
		    //                   System.out.println(syn);
		    gene.setSynonyms(syn);
		}
		parQ = DBQuery.getParamQuery("TOTAL_GENE_RELATED_ARRAYS");
		parQ.setPrepStat(conn);
		prepStmt = parQ.getPrepStat();
		prepStmt.setString(1, gene.getSymbol());
		resSet = prepStmt.executeQuery();
		if(resSet.first()) {
		    geneInfo.setNumMicArrays(resSet.getString(1));
		}
            }
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
            return gene;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
 
    /**
     * method to get additional translational info on the gene
     * @param geneInfo - a gene object containing partial data on a gene 
     * @return gene - object containing more complete info on a gene
     */
    public Gene addGeneInfoIuphar(Gene geneInfo) {
	if (null == geneInfo)
	    return null;
	String str = geneInfo.getMgiAccID();
	if (null == str || str.trim().equals(""))
	    str = geneInfo.getSymbol();
	if (null == str || str.trim().equals(""))
	    return geneInfo;
	
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GENE_INFO_IUPHAR");
        PreparedStatement prepStmt = null;
	int type = -1;
	Gene gene = geneInfo;

	gene.setIuphar_db_URL(null); 
	gene.setIuphar_guide_URL(null); 

        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, str);
            prepStmt.setString(2, str);
            resSet = prepStmt.executeQuery();
            if(resSet.first()){
		type = resSet.getInt(3);
		switch (type) {
		case 2:
		    gene.setIuphar_db_URL(resSet.getString(1)); 
		    gene.setIuphar_guide_URL(resSet.getString(2)); 
		    break;
		case 1:
		    gene.setIuphar_guide_URL(resSet.getString(2)); 
		break;
		case 0:
		default:
		    gene.setIuphar_db_URL(resSet.getString(1)); 
		    break;
		}
            }
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
		gene.setIuphar_db_URL(null); 
		gene.setIuphar_guide_URL(null); 
            se.printStackTrace();
        }
        return gene;
    }
 
    /**
     * @author xingjun
     * <p>used for new format of browse style page</p>
     * 
     * @param inputType
     * @param ignoreExpression
     * @param inputString
     * @param stage
     * @param criteria
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param rowNum
     * @return
     */
    public ArrayList getSubmissionByGeneInfo(String inputType, String ignoreExpression,
					     String inputString, String stage, String criteria,
					     int columnIndex, boolean ascending, int offset, int rowNum) {
	//        System.out.println("inputType: " + inputType);
	
    	// initialise
    	ResultSet resSet = null;
    	ArrayList result = null;
    	PreparedStatement prepStmt = null;
    	ParamQuery parQ = null;
    	String query = null;
	
    	// get gene info condition
	//		System.out.println("inputType: " + inputType);
	//		System.out.println("ignoreExpression: " + ignoreExpression);
    	String geneInfoString = getGeneInfoString(inputType, criteria, inputString);
	
    	// get stage condition
    	String stageString = null;
    	if (stage != null && !stage.equals("")) {
	    stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
    	} else {
	    stageString = "";
    	}
	
    	// get where condition
    	ParamQuery parQWhere = DBQuery.getParamQuery("WHERE_CONDITION_GENE_QUERY");
    	String whereCondition = parQWhere.getQuerySQL();
	
    	// get select clause
    	if (ignoreExpression == null || ignoreExpression.equals("true")) { // dont care expression
	    parQ = DBQuery.getParamQuery("SUBMISSION_GENE_QUERY_IGNORE_EXPRESSION");
    	} else {
	    parQ = DBQuery.getParamQuery("SUBMISSION_GENE_QUERY_CONCERN_EXPRESSION");
    	}
    	query = parQ.getQuerySQL();
	
    	// assemble query string
    	String queryString =
	    assembleGeneQueryString(query, geneInfoString, stageString, whereCondition,
				    columnIndex, ascending, offset, rowNum);
	//    	System.out.println("gene query: " + queryString);
	
    	// execute query
    	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    resSet = prepStmt.executeQuery();
	    result = DBHelper.formatBrowseResultSetISH(resSet);
	    
	    // close database object
	    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * @param ignoreExpression
     * @param inputType
     * @param inputString
     * @param stage
     * @param criteria
     * @return
     */
    public String getTotalNumberOfISHSubmissionsForGeneQuery(String inputType,
							     String ignoreExpression, String inputString, String stage, String criteria) {
	
	//        System.out.println("getTotalNumberOfISHSubmissionsForGeneQuery:inputType: " + inputType);
	//        System.out.println("getTotalNumberOfISHSubmissionsForGeneQuery:ignoreExpression: " + ignoreExpression);
	//        System.out.println("getTotalNumberOfISHSubmissionsForGeneQuery:inputString: " + inputString);
	//        System.out.println("getTotalNumberOfISHSubmissionsForGeneQuery:stage: " + stage);
	//        System.out.println("getTotalNumberOfISHSubmissionsForGeneQuery:criteria: " + criteria);
	
    	ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        String query = null;
        String geneInfoString =
	    getGeneInfoString(inputType, criteria, inputString);
	
        String stageString = null;
        if (stage != null && !stage.equals("")) {
            stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
        } else {
            stageString = "";
        }
	
        ParamQuery parQWhere =
	    DBQuery.getParamQuery("WHERE_CONDITION_GENE_QUERY");
        String whereCondition = parQWhere.getQuerySQL();
        query = DBQuery.NUMBER_OF_SUBMISSION; // select
        query+= DBQuery.join_expression_gene_query; // from clause and join
	
        // if care expression, append expression joint condition
        if (ignoreExpression != null && !ignoreExpression.equals("true")) { // care expression
            query += DBQuery.JOIN_EXPRESSION_TABLE;
        }
	
        query+= " WHERE "; // where clause start
        query+= geneInfoString; // gene criteria
        query+= whereCondition; // other conditions (public?, deleted?, etc.)
	//        System.out.println("getTotalNumberOfISHSubmissionsForGeneQuery:query: " + query);
        String totalNumber = new String("0");
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(query);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                totalNumber = Integer.toString(resSet.getInt(1));
            }
	    
            // close database object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * 
     */
    public String getTotalNumberOfISHSubmissionsForGeneQuery(String inputType,
							     String ignoreExpression, String inputString, String component, String stage, String criteria){
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        String query = null;
        String geneInfoString =
	    getGeneInfoString(inputType, criteria, inputString);
        String stageString = null;
        if (stage != null && !stage.equals("")) {
            stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
        } else {
            stageString = "";
        }
        ParamQuery parQWhere =
	    DBQuery.getParamQuery("WHERE_CONDITION_GENE_QUERY");
        String whereCondition = parQWhere.getQuerySQL();
        query = DBQuery.NUMBER_OF_SUBMISSION;
        query += DBQuery.join_expression_gene_query;
        if (ignoreExpression != null && !ignoreExpression.equals("true")) { // care expression
            query += DBQuery.JOIN_EXPRESSION_TABLE;
            whereCondition += " AND E.EXP_COMPONENT_ID = '" + component + "' ";
        }
	
        query += " WHERE ";
        query+= geneInfoString + whereCondition;
	//        System.out.println("QUURRYRYRYUUYUYUYUY    "+query);
        String totalNumber = new String("");
	
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(query);
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:getTotalNumberOfISHSubmissionsForGeneQuery = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                totalNumber = Integer.toString(resSet.getInt(1));
            }
	    
            // close database object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * @param inputType
     * @param criteria
     * @param inputString
     * @return
     */
    private String getGeneInfoString(String inputType, String criteria,
                                     String inputString) {
        String result = null;
        if (inputType.equals("symbol")) {
            result = assembleGeneInfoString("RPR_SYMBOL", criteria, inputString);
        } else if (inputType.equals("name")) {
            result = assembleGeneInfoString("RPR_NAME", criteria, inputString);
        } else if (inputType.equals("synonym")) {
            result = assembleGeneInfoString("RPR_SYNONYMS", criteria, inputString);
        } else { // inputType.equals("all")
            result = "(" + assembleGeneInfoString("RPR_SYMBOL", criteria, inputString) +
		" OR " + assembleGeneInfoString("RPR_NAME", criteria, inputString) +
		" OR " + assembleGeneInfoString("RPR_SYNONYMS", criteria, inputString) + ")";
        }
        return result;
    }
    
    /**
     * @param columnName
     * @param criteria
     * @param inputString
     * @return
     */
    private String assembleGeneInfoString(String columnName, String criteria,
                                          String inputString) {
        String result = new String("");
	
        // white space and comma as the delimiters
        String[] columnValues =
            inputString.split("(\\s+,+){1,}|(\\s+){1,}|(,+\\s+){1,}|(,+){1,}");
	
        // assemble
        // if inputString is a empty string, return '(columnName is null)' as the gene info string,
        // else assemble the  gene info string and return it
        int columnNumber = columnValues.length;
	//        System.out.println("columnNumber: " + columnNumber);
        if (columnNumber == 0) {
            result += "(" + columnName + " IS NULL)";
        } else {
            if (criteria.equals("equals")) {
                result += "(" + columnName + " IN (";
                if (columnNumber > 1) {
                    result += "'" + columnValues[0] + "'";
                    for (int i = 1; i < columnNumber; i++) {
                        result += ", '" + columnValues[i] + "'";
                    }
                } else {
                    result += "'" + columnValues[0] + "'";
                }
                result += ")) ";
		
            } else if (criteria.equals("begins")) {
                result += "(";
                if (columnNumber > 1) {
                    result += columnName + " LIKE '" + columnValues[0] + "%' ";
                    for (int i = 1; i < columnNumber; i++) {
                        result += "OR " + columnName + " LIKE '" + columnValues[i] + "%' ";
                    }
                } else {
                    result += columnName + " LIKE '" + columnValues[0] + "%'";
                }
                result += ") ";
		
            } else { // criteria.equals("wildcard")
                result += "(";
                if (columnNumber > 1) {
                    result += columnName + " LIKE '%" + columnValues[0] + "%' ";
                    for (int i = 1; i < columnNumber; i++) {
                        if (columnValues[i] != null && !columnValues[i].equals("")) {
                            result += "OR " + columnName + " LIKE '%" + columnValues[i] + "%' ";
                        }
                    }
                } else {
                    result += columnName + " LIKE '%" + columnValues[0] + "%'";
                }
                result += ") ";
            }
        }
        //		System.out.println("gene info string: " + result);
        return result;
    }
    
    /**
     * @author xingjun
     * <p>used for new format of browse style page</p>
     * @param query
     * @param geneInfoString
     * @param stageString
     * @param whereCondition
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    
    private String assembleGeneQueryString(String query, String geneInfoString,
					   String stageString,
					   String whereCondition,
					   int columnIndex, boolean ascending, int offset, int num) {
    	// query string excluding order by and retrieval row number info
    	String queryString = query + geneInfoString + whereCondition + stageString;
	//	    System.out.println("queryString in assembleGeneQueryString method: " + queryString);
	
    	// append order by string and retrieval row number info
    	String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
    	String result =
	    DBHelper.assembleBrowseSubmissionQueryStringISH(2, queryString, defaultOrder, columnIndex, ascending, offset, num);
    	return result;
    }
    
    /**
     * @param query
     * @param inputType
     * @param ignoreExpression
     * @param inputString
     * @param stage
     * @param criteria
     * @return
     */
    public String[][] getTotalNumberOfColumnsGeneQuery(String[] query,
                                                       String inputType,
                                                       String ignoreExpression,
                                                       String inputString,
                                                       String stage,
                                                       String criteria) {
        int queryNumber = query.length;
        String[][] result = new String[queryNumber][2];
        String[] queryString = new String[queryNumber];
	
        /** ---assemble query string--- */
        // expression string
        // if ignore expression, the expression string will be empty
        String expressionString = null;
        if (ignoreExpression == "true") {
            expressionString = new String("");
        } else {
            ParamQuery parQIE =
                DBQuery.getParamQuery("WITH_EXPRESSION_GENE_QUERY");
            expressionString = parQIE.getQuerySQL();
        }
	
        // join expression
        ParamQuery parQJE =
            DBQuery.getParamQuery("JOIN_EXPRESSION_GENE_QUERY");
        String joinExpression = parQJE.getQuerySQL();
	
        // get gene info condition
        String geneInfoString =
            " WHERE "+getGeneInfoString(inputType, criteria, inputString);
	
        // get where condition
        ParamQuery parQWhere =
            DBQuery.getParamQuery("WHERE_CONDITION_GENE_QUERY");
        String whereCondition = parQWhere.getQuerySQL();
	
        // get stage condition
        String stageString = null;
        if (stage != null && !stage.equals("")) {
            stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
        } else {
            stageString = "";
        }
        //		System.out.println("expr: " + expressionString);
        //		System.out.println("gene: " + geneInfoString);
        //		System.out.println("wher: " + whereCondition);
        //		System.out.println("stag: " + stageString);
        //		System.out.println("join: " + joinExpression);
	
        // get the query sql based on query name and record query name in result string array
        for (int i = 0; i < queryNumber; i++) {
            ParamQuery parQ = DBQuery.getParamQuery(query[i]);
            // select exp + expression info + join expression + gene info + where condition + stage condition
            queryString[i] =
		//                    parQ.getQuerySQL() + expressionString + joinExpression +
		//                    geneInfoString + whereCondition + stageString;
            	parQ.getQuerySQL() + joinExpression + expressionString +
            	geneInfoString + whereCondition + stageString;
	    //            System.out.println("TOTAL NUMBER query: " + queryString[i]);
            result[i][0] = query[i];
        }
	
        /** --start to execute query--- */
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            for (int i = 0; i < queryNumber; i++) {
                prepStmt = conn.prepareStatement(queryString[i]);
    		    if (debug)
    		    	System.out.println("MySQLISHDAOImp:getTotalNumberOfColumnsGeneQuery["+i+"] = "+prepStmt);
                resSet = prepStmt.executeQuery();
                result[i][1] = getStringValueFromIntegerResultSet(resSet);
            }
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * <p>another format of getTotalNumberOfColumnsGeneQuery, with an extra parameter: componentId</p>
     * @param query
     * @param inputType
     * @param ignoreExpression
     * @param inputString
     * @param componentId
     * @param stage
     * @param criteria
     * @return
     */
    public String[][] getTotalNumberOfColumnsGeneQuery(String[] query,
                                                       String inputType,
                                                       String ignoreExpression,
                                                       String inputString,
                                                       String componentId,
                                                       String stage,
                                                       String criteria) {
        int queryNumber = query.length;
        String[][] result = new String[queryNumber][2];
        String[] queryString = new String[queryNumber];
	
        /** ---assemble query string--- */
        // expression string
        // if ignore expression, the expression string will be empty
        String expressionString = null;
        if (ignoreExpression == null || ignoreExpression.equals("false")) {
            ParamQuery parQIE =
                DBQuery.getParamQuery("WITH_EXPRESSION_GENE_QUERY");
            expressionString = parQIE.getQuerySQL();
        } else {
            expressionString = new String("");
        }
	
        // join expression
        ParamQuery parQJE =
            DBQuery.getParamQuery("JOIN_EXPRESSION_GENE_QUERY");
        String joinExpression = parQJE.getQuerySQL();
	
        // get gene info condition
        String geneInfoString =
            " WHERE " + getGeneInfoString(inputType, criteria, inputString);
	
        // get where condition
        ParamQuery parQWhere =
            DBQuery.getParamQuery("WHERE_CONDITION_GENE_QUERY");
        String whereCondition = parQWhere.getQuerySQL();
        if (ignoreExpression == null || ignoreExpression.equals("false")) {
            whereCondition +=
		" AND E.EXP_COMPONENT_ID = '" + componentId + "' ";
        }
	
        // get stage condition
        String stageString = null;
        if (stage != null && !stage.equals("")) {
            stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
        } else {
            stageString = "";
        }
	//        System.out.println("expr: " + expressionString);
	//        System.out.println("gene: " + geneInfoString);
	//        System.out.println("wher: " + whereCondition);
	//        System.out.println("stag: " + stageString);
	//        System.out.println("join: " + joinExpression);
	
        // get the query sql based on query name and record query name in result string array
        for (int i = 0; i < queryNumber; i++) {
            ParamQuery parQ = DBQuery.getParamQuery(query[i]);
            // select exp + expression info + join expression + gene info + where condition + stage condition
            queryString[i] =
		//                parQ.getQuerySQL() + expressionString + joinExpression +
		//                geneInfoString + whereCondition + stageString;
                parQ.getQuerySQL() + joinExpression + expressionString +
                geneInfoString + whereCondition + stageString;
	    //            System.out.println("TOTAL num of query WITH COMPONENT: " + queryString[i]);
            result[i][0] = query[i];
        }
	
        /** --start to execute query--- */
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            for (int i = 0; i < queryNumber; i++) {
                prepStmt = conn.prepareStatement(queryString[i]);
        		if (debug)
        		    System.out.println("MySQLISHDAOImp:getTotalNumberOfColumnsGeneQuery["+i+"] = "+prepStmt);
                resSet = prepStmt.executeQuery();
                result[i][1] = getStringValueFromIntegerResultSet(resSet);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @author xingjun
     * <p>comply with format of new browse style page</p>
     * @param inputType
     * @param inputString
     * @param stage
     * @param criteria
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param rowNum
     * @return
     */
    public ArrayList getComponentCountInfoByGeneInfo(String inputType, String inputString,
						     String stage, String criteria,
						     int columnIndex, boolean ascending, int offset, int num) {
    	// initialise
    	ResultSet resSet = null;
    	ArrayList result = null;
    	PreparedStatement prepStmt = null;
    	ParamQuery parQ = null;
    	String query = null;
	
    	// get gene info condition
	//		System.out.println("inputType: " + inputType);
    	String geneInfoString = getGeneInfoString(inputType, criteria, inputString);
	
    	// get stage condition
    	String stageString = null;
    	if (stage != null && !stage.equals("")) {
	    stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
    	} else {
	    stageString = "";
    	}
	
    	// get where condition
	//		ParamQuery parQWhere = DBQuery.getParamQuery("WHERE_CONDITION_GENE_QUERY");
	//		String whereCondition = parQWhere.getQuerySQL();
	
    	// group by clause
    	ParamQuery parQGroupBy = DBQuery.getParamQuery("GROUP_BY_CLAUSE_COMPONENT_COUNT_QUERY");
    	String groupBy = parQGroupBy.getQuerySQL();
	
    	// get select clause
    	parQ = DBQuery.getParamQuery("COMPONENT_COUNT_QUERY");
    	query = parQ.getQuerySQL();
	
    	// assemble the query string
    	String queryString = assembleComponentCountQueryString(query, geneInfoString,
							       stageString, groupBy, columnIndex, ascending, offset, num);
	//    	System.out.println("\n\n\n\n"+queryString+"\n\n\n\n");
	
    	// execute query
    	try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getComponentCountInfoByGeneInfo = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    result = DBHelper.formatResultSetToArrayList(resSet);
		    
		    // close database object
		    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * @param ignoreExpression
     * @param inputType
     * @param inputString
     * @param stage
     * @param criteria
     * @return
     */
    public String getTotalISHComponentsExpressingGeneQuery(String inputType,
							   String ignoreExpression, String inputString, String stage, String criteria) {
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        String query = null;
        String geneInfoString =
	    getGeneInfoString(inputType, criteria, inputString);
        String stageString = null;
        if (stage != null && !stage.equals("")) {
            stageString = " AND SUB_EMBRYO_STG = '" + stage + "' ";
        } else {
            stageString = "";
        }
	
        query = DBQuery.TOTAL_GENES_IN_COMP_COLS + DBQuery.GENES_IN_COMPONENT_TABLES
	    + stageString + " AND " + geneInfoString;
	//        System.out.println(query);
        String totalNumber = new String("");
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
	        prepStmt = conn.prepareStatement(query);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getTotalISHComponentsExpressingGeneQuery = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                totalNumber = Integer.toString(resSet.getInt(1));
            }
	    
            // close database object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * @param query
     * @param geneInfoString
     * @param stageString
     * @param whereCondition
     * @param groupBy
     * @param order
     * @param offset
     * @param rowNum
     * @return
     */
    private String assembleComponentCountQueryString(String query,
                                                     String geneInfoString,
                                                     String stageString,
                                                     String groupBy,
                                                     String[] order,
                                                     String offset,
                                                     String rowNum) {
        // query string excluding order by and retrieval row number info
        String queryString =
            query + " AND " + geneInfoString + stageString + groupBy;
	//        	    System.out.println("queryString in assembleComponentCountQueryString method: " + queryString);
	
        // append order by string and retrieval row number info
        String defaultOrder = " ORDER BY APO_FULL_PATH";
        String result =
            DBHelper.assembleBrowseSubmissionQueryStringISH(3, queryString,
                                                            defaultOrder,
                                                            order, offset,
                                                            rowNum);
        return result;
    }
    
    /**
     * @author xingjun
     * <p>comply with the format of new browse style</p>
     * @param query
     * @param geneInfoString
     * @param stageString
     * @param groupBy
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param rowNum
     * @return
     */
    private String assembleComponentCountQueryString(String query, String geneInfoString,
						     String stageString, String groupBy,
						     int columnIndex, boolean ascending, int offset, int rowNum) {
    	// query string excluding order by and retrieval row number info
    	String queryString = query + " AND " + geneInfoString + stageString + groupBy;
	//	    System.out.println("queryString in assembleComponentCountQueryString method: " + queryString);
	
    	// append order by string and retrieval row number info
    	String defaultOrder = "APO_FULL_PATH";
    	String result =
	    DBHelper.assembleBrowseSubmissionQueryStringISH(3, queryString, defaultOrder, 
							    columnIndex, ascending, offset, rowNum);
    	return result;
    }
    
    /**
     * @param component id
     * @param stage value
     * @return
     */
    public ISHBrowseSubmission[] getSubmissionByComponentId(String component,
                                                            String stage,
                                                            String[] order,
                                                            String offset,
                                                            String num) {
        ResultSet resSet = null;
        ISHBrowseSubmission[] result = null;
        ParamQuery parQStart = null;
        ParamQuery parQEnd = null;
        PreparedStatement prepStmt = null;
        String componentId = null;
        if (component.indexOf(":") == -1) {
            componentId = "EMAP:" + component;
        } else {
            componentId = component;
        }
        String stageValue = null;
        String start = "";
        String end = "";
        String query = null;
        if (stage == null || stage.equals("")) {
            parQStart =
		DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_START");
            start = parQStart.getQuerySQL();
            parQEnd =
		DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_END");
            end = parQEnd.getQuerySQL();
            query = start + " ? " + end;
        } else {
            parQStart = DBQuery.getParamQuery("COMPONENT_QUERY_WITH_STAGE");
            start = parQStart.getQuerySQL();
            stageValue = "TS" + stage;
            query = start;
        }
	
        // assemble the query string
        String defaultOrder = DBQuery.ORDER_BY_ISH_PROBE_SYMBOL;
        String queryString =
            DBHelper.assembleBrowseSubmissionQueryStringISH(2, query,
                                                            defaultOrder,
                                                            order, offset,
                                                            num);
        //		System.out.println("component queryString: " + queryString);
	
        // execute query and assemble result
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(queryString);
	    
            if (stage == null || stage.equals("")) {
                prepStmt.setString(1, componentId);
            } else {
                prepStmt.setString(1, componentId);
                prepStmt.setString(2, stageValue);
            }
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:getSubmissionByComponentId = "+prepStmt);
	    
            // execute
            resSet = prepStmt.executeQuery();
            result = formatBrowseResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @author xingjun
     * <p>used for new format of browse style page</p>
     * @param component
     * @param stage
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    public ArrayList getSubmissionByComponentId(String component, String stage,
						int columnIndex, boolean ascending, int offset, int num) {
    	ResultSet resSet = null;
    	ArrayList result = null;
    	ParamQuery parQStart = null;
    	ParamQuery parQEnd = null;
    	PreparedStatement prepStmt = null;
    	String componentId = null;
    	if (component.indexOf(":") == -1) {
	    componentId = "EMAP:" + component;
    	} else {
	    componentId = component;
    	}
    	String stageValue = null;
    	String start = "";
    	String end = "";
    	String query = null;
    	if (stage == null || stage.equals("")) {
	    parQStart = DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_START");
	    start = parQStart.getQuerySQL();
	    parQEnd = DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_END");
	    end = parQEnd.getQuerySQL();
	    query = start + " ? " + end;
    	} else {
	    parQStart = DBQuery.getParamQuery("COMPONENT_QUERY_WITH_STAGE");
	    start = parQStart.getQuerySQL();
	    stageValue = "TS" + stage;
	    query = start;
    	}
	
    	// assemble the query string
    	String defaultOrder = DBQuery.ORDER_BY_ISH_PROBE_SYMBOL;
    	String queryString =
	    DBHelper.assembleBrowseSubmissionQueryStringISH(2, query,
							    defaultOrder, columnIndex, ascending, offset, num);
	//    	System.out.println("component queryString: " + queryString);
	
    	// execute query and assemble result
    	try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    if (stage == null || stage.equals("")) {
		    	prepStmt.setString(1, componentId);
		    } else {
				prepStmt.setString(1, componentId);
				prepStmt.setString(2, stageValue);
		    }
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionByComponentId = "+prepStmt);
		    
		    // execute
		    resSet = prepStmt.executeQuery();
		    result = DBHelper.formatBrowseResultSetISH(resSet);
		    
		    // close the connection
		    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * 
     */
    public ISHBrowseSubmission[] getSubmissionByComponentIds(String[] components,
                                                             String[] order,
                                                             String offset,
                                                             String num) {
        ResultSet resSet = null;
        ISHBrowseSubmission[] result = null;
        ParamQuery parQStart = null;
        ParamQuery parQEnd = null;
        PreparedStatement prepStmt = null;
        String componentIds = "";
        int componentNumber = components.length;
        if (componentNumber == 1) {
            if (components[0].indexOf(":") == -1) {
                componentIds += "'EMAP:" + components[0] + "'";
            } else {
                componentIds += "'" + components[0] + "'";
            }
        } else {
            // add the first component id into the component string
            if (components[0].indexOf(":") == -1) {
                componentIds += "'EMAP:" + components[0] + "'";
            } else {
                componentIds += "'" + components[0] + "'";
            }
	    
            // add other component ids into the component string
            for (int i = 1; i < componentNumber; i++) {
                if (components[i].indexOf(":") == -1) {
                    componentIds += ", 'EMAP:" + components[i] + "'";
                } else {
                    componentIds += ", '" + components[i] + "'";
                }
            }
        }
        //              System.out.println("component list: " + componentIds);
	
        // add the component string into the query string
        parQStart =
	    DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_START");
        parQEnd = DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_END");
	
        // assemble the query string
        String query =
            parQStart.getQuerySQL() + componentIds + parQEnd.getQuerySQL();
        String defaultOrder = DBQuery.ORDER_BY_ISH_PROBE_SYMBOL;
        String queryString =
            DBHelper.assembleBrowseSubmissionQueryStringISH(2, query,
                                                            defaultOrder,
                                                            order, offset,
                                                            num);
        //              System.out.println("components queryString: " + queryString);
	
        // execute query and assemble result
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
	        prepStmt = conn.prepareStatement(queryString);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionByComponentIds = "+prepStmt);
            resSet = prepStmt.executeQuery();
            result = formatBrowseResultSet(resSet);
	    
            // close the database object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @author xingjun
     * <p>used for new format of browse style page</p>
     * @param components
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    public ArrayList getSubmissionByComponentIds(String[] components, 
						 int columnIndex, boolean ascending, int offset, int num) {
    	ResultSet resSet = null;
    	ArrayList result = null;
    	ParamQuery parQStart = null;
    	ParamQuery parQEnd = null;
    	PreparedStatement prepStmt = null;
    	String componentIds = "";
    	int componentNumber = components.length;
    	if (componentNumber == 1) {
		    if (components[0].indexOf(":") == -1) {
		    	componentIds += "'EMAP:" + components[0] + "'";
		    } else {
		    	componentIds += "'" + components[0] + "'";
		    }
    	} else {
	    // add the first component id into the component string
		    if (components[0].indexOf(":") == -1) {
				componentIds += "'EMAP:" + components[0] + "'";
		    } else {
		    	componentIds += "'" + components[0] + "'";
		    }
		    
		    // add other component ids into the component string
		    for (int i = 1; i < componentNumber; i++) {
				if (components[i].indexOf(":") == -1) {
				    componentIds += ", 'EMAP:" + components[i] + "'";
				} else {
				    componentIds += ", '" + components[i] + "'";
				}
		    }
    	}
	//    	System.out.println("component list: " + componentIds);
	
    	// add the component string into the query string
    	parQStart = DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_START");
    	parQEnd = DBQuery.getParamQuery("COMPONENT_QUERY_WITHOUT_STAGE_END");
	
    	// assemble the query string
    	String query = parQStart.getQuerySQL() + componentIds + parQEnd.getQuerySQL();
    	String defaultOrder = DBQuery.ORDER_BY_ISH_PROBE_SYMBOL;
    	String queryString =
	    DBHelper.assembleBrowseSubmissionQueryStringISH(2, query,
							    defaultOrder, columnIndex, ascending, offset, num);
	//    	System.out.println("components queryString: " + queryString);
	
    	// execute query and assemble result
    	try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionByComponentIds = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    result = DBHelper.formatBrowseResultSetISH(resSet);
		    
		    // close the database object
		    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * @param components - list of ids corresponding to ANO_OID in ANA_NODE tbl
     * @param start - user specified developmental start stage
     * @param end - user specified developmental end stage
     * @param order - sql param
     * @param offset - sql param
     * @param num - sql param
     * @return
     */
    public ISHBrowseSubmission[] getSubmissionByComponentIds(String[] components,
							     String start,
							     String end,
							     String [] annotationTypes,
							     String criteria,
							     String[] order,
							     String offset,
							     String num) {
	String [] componentList = this.getComponentList(components, start, end, criteria);
	StringBuffer annotatedGenesInComponentsQ = new StringBuffer(DBQuery.getISH_BROWSE_ALL_COLUMNS() + DBQuery.ISH_BROWSE_ALL_TABLES);
	if(this.appendSubsWithExprInCompsQ(criteria, componentList, annotationTypes) == null){
	    return null;
	}
	annotatedGenesInComponentsQ.append(this.appendSubsWithExprInCompsQ(criteria, componentList, annotationTypes));
	String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
	String queryString =
	    DBHelper.assembleBrowseSubmissionQueryStringISH(1, annotatedGenesInComponentsQ.toString(),
							    defaultOrder,
							    order, offset,
							    num);
	
	//determine what types of expression user has selected - this will shape SQL
	int index = this.getAnnotationTypeIndex(annotationTypes, "present");
	boolean presentSelected = false;
	if(index >= 0){
	    presentSelected = true;
	}
	index = this.getAnnotationTypeIndex(annotationTypes, "not detected");
	boolean notDetSelected = false;
	if(index >=0) {
	    notDetSelected = true;
	}
	index = this.getAnnotationTypeIndex(annotationTypes, "possibleuncertain");
	boolean possSelected = false;
	if(index >=0) {
	    possSelected = true;
	}
	//         System.out.println("\n\n");
	//         System.out.println(queryString);
	//         System.out.println("\n\n");
	ResultSet resSet = null;
	ParamQuery parQ = null;
	PreparedStatement prepStat = null;
	
	parQ = new ParamQuery("",queryString);
	ISHBrowseSubmission[] result = null;        
	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    parQ.setPrepStat(conn);
	    prepStat = parQ.getPrepStat();
	    
	    //need to set appropriate parameters in SQL based on user specs
	    if(criteria.equals("any")){
		if((presentSelected || possSelected) && notDetSelected){
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+1, componentList[i]);
		    }
		    for(int i =0;i<componentList.length;i++){
			prepStat.setString(i+1+componentList.length, componentList[i]);
		    }
		}
		//if user has selected 'possible/uncertain' or 'present' expression
		else {
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+1, componentList[i]);
		    }
		}
	    }
	    else if(criteria.equals("all")){
		prepStat.setInt(1, componentList.length);
		if((presentSelected || possSelected) && notDetSelected){
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+2, componentList[i]);
		    }
		    prepStat.setString(componentList.length+2, start);
		    prepStat.setString(componentList.length+3, end);
		    
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+4+componentList.length, componentList[i]);
		    }
		    prepStat.setString(componentList.length*2+4, start);
		    prepStat.setString(componentList.length*2+5, end);
		}
		//if user has selected 'possible/uncertain' or 'present' or 'not detected' expression
		else {
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+2, componentList[i]);
		    }
		    prepStat.setString(componentList.length+2, start);
		    prepStat.setString(componentList.length+3, end);
		}
	    }
	    if (debug)
	    	System.out.println("MySQLISHDAOImp:getSubmissionByComponentIds = "+prepStat);
	    
	    resSet = prepStat.executeQuery();
	    //System.out.println("query executed");
	    result = formatBrowseResultSet(resSet);
	    DBHelper.closeResultSet(resSet);
	    DBHelper.closePreparedStatement(prepStat);
	}
	catch (SQLException se) {
	    se.printStackTrace();
	}
	return result;
    }
    
    /**
     * @author xingjun
     * <p>used for new format of browse style page</p>
     * @param components - list of ids corresponding to ANO_OID in ANA_NODE tbl
     * @param start - user specified developmental start stage
     * @param end - user specified developmental end stage
     * @param order - sql param
     * @param offset - sql param
     * @param num - sql param
     * @return
     */
    public ArrayList getSubmissionByComponentIds(String[] components,
						 String startStage,
						 String endStage,
						 String [] annotationTypes,
						 String criteria,
						 int columnIndex,
						 boolean ascending,
						 int offset,
						 int num) {
	String [] componentList = this.getComponentList(components, startStage, endStage, criteria);
	//         System.out.println("componentList len: "  + componentList.length);
	//         for (int i=0;i<componentList.length;i++)
	//        	 System.out.println("component value:" + i + ":" + componentList[i]);
	StringBuffer annotatedGenesInComponentsQ = new StringBuffer(DBQuery.getISH_BROWSE_ALL_COLUMNS() + DBQuery.ISH_BROWSE_ALL_TABLES);
	//         System.out.println("annotatedGenesInComponentsQ: before appendSubsWithExprInCompsQ: " + annotatedGenesInComponentsQ);
	
	if(this.appendSubsWithExprInCompsQ(criteria, componentList, annotationTypes) == null){
	    return null;
	}
	annotatedGenesInComponentsQ.append(this.appendSubsWithExprInCompsQ(criteria, componentList, annotationTypes));
	//         System.out.println("annotatedGenesInComponentsQ: after appendSubsWithExprInCompsQ: " + annotatedGenesInComponentsQ);
	
	String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
	String queryString =
	    DBHelper.assembleBrowseSubmissionQueryStringISH(1, annotatedGenesInComponentsQ.toString(),
							    defaultOrder, columnIndex, ascending, offset, num);
	//         System.out.println("getSubmissionByComponentIds sql: " + queryString);
	
	//determine what types of expression user has selected - this will shape SQL
	int index = this.getAnnotationTypeIndex(annotationTypes, "present");
	boolean presentSelected = false;
	if(index >= 0){
	    presentSelected = true;
	}
	index = this.getAnnotationTypeIndex(annotationTypes, "not detected");
	boolean notDetSelected = false;
	if(index >=0) {
	    notDetSelected = true;
	}
	index = this.getAnnotationTypeIndex(annotationTypes, "possibleuncertain");
	boolean possSelected = false;
	if(index >=0) {
	    possSelected = true;
	}
	ResultSet resSet = null;
	ParamQuery parQ = null;
	PreparedStatement prepStat = null;
	parQ = new ParamQuery("",queryString);
	ArrayList result = null;        
	//         System.out.println("getSubmissionByComponentIds sql: " + parQ.getQuerySQL());
	
	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    parQ.setPrepStat(conn);
	    prepStat = parQ.getPrepStat();
	    
	    //need to set appropriate parameters in SQL based on user specs
	    if(criteria.equals("any")){
		if((presentSelected || possSelected) && notDetSelected){
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+1, componentList[i]);
		    }
		    for(int i =0;i<componentList.length;i++){
			prepStat.setString(i+1+componentList.length, componentList[i]);
		    }
		}
		//if user has selected 'possible/uncertain' or 'present' expression
		else {
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+1, componentList[i]);
		    }
		}
	    }
	    else if(criteria.equals("all")){
		prepStat.setInt(1, componentList.length);
		if((presentSelected || possSelected) && notDetSelected){
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+2, componentList[i]);
		    }
		    prepStat.setString(componentList.length+2, startStage);
		    prepStat.setString(componentList.length+3, endStage);
		    
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+4+componentList.length, componentList[i]);
		    }
		    prepStat.setString(componentList.length*2+4, startStage);
		    prepStat.setString(componentList.length*2+5, endStage);
		}
		//if user has selected 'possible/uncertain' or 'present' or 'not detected' expression
		else {
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+2, componentList[i]);
		    }
		    prepStat.setString(componentList.length+2, startStage);
		    prepStat.setString(componentList.length+3, endStage);
		}
	    }
	    //             System.out.println("\n\n"+prepStat.toString()+"\n\n");
	    
	    resSet = prepStat.executeQuery();
	    result = DBHelper.formatBrowseResultSetISH(resSet);
	    
	    DBHelper.closeResultSet(resSet);
	    DBHelper.closePreparedStatement(prepStat);
	}
	catch (SQLException se) {
	    se.printStackTrace();
	}
	return result;
    }
    
    private String [] getComponentList (String[] components, 
					String start, String end, String criteria) {
	
	if (criteria != null && criteria.equals("all")) {
	    return components;
	} else {
	    String [] results = null;
	    String publicIdsQ = DBQuery.getAnatPubIdsFromNodeIdsQuery(components);
	    ParamQuery paramQ = new ParamQuery("",publicIdsQ);
	    PreparedStatement prepStat = null;
	    try {
             	// if disconnected from db, re-connected
             	conn = DBHelper.reconnect2DB(conn);
		
		paramQ.setPrepStat(conn);
		prepStat = paramQ.getPrepStat();
		prepStat.setString(1, start);
		prepStat.setString(2, end);
		for (int i = 0; i < components.length; i++) {
		    prepStat.setString(i + 3, components[i]);
		}
		ResultSet resSet = prepStat.executeQuery();
		if(resSet.first()) {
		    //                    System.out.println("there is a first value in the result set");
                    resSet.last();
                    int totalRows = resSet.getRow();
                    results = new String[totalRows];
                    int i = 0;
                    resSet.beforeFirst();
                    while (resSet.next()) {
                        results[i] = resSet.getString(1);
                        i++;
                    }
                }
                DBHelper.closePreparedStatement(prepStat);
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
            return results;
        }
    }
    
    /**
     * 
     */
    public String getTotalNumberOfISHSubmissionsForComponentQuery(String component){
    	ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        String totalNumber = new String("");
        String componentId = null;
        if (component.indexOf(":") == -1) {
            componentId = "EMAP:" + component;
        } else {
            componentId = component;
        }
        String query = DBQuery.NUMBER_OF_SUBMISSION + DBQuery.ISH_BROWSE_ALL_TABLES + DBQuery.endsComponentQueryWithoutStagePart1 +" ? "+ DBQuery.endsComponentQueryWithoutStagePart2;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
            prepStmt = conn.prepareStatement(query);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getTotalNumberOfISHSubmissionsForComponentQuery = "+prepStmt);
            prepStmt.setString(1, componentId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                totalNumber = Integer.toString(resSet.getInt(1));
            }
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * 
     */
    public String getTotalNumberOfSubmissionsForComponentsQuery(String [] components){
		String totalNumber = new String("");
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		String componentIds = "";
		int componentNumber = components.length;
		if (componentNumber == 1) {
		    if (components[0].indexOf(":") == -1) {
		    	componentIds += "'EMAP:" + components[0] + "'";
		    } else {
		    	componentIds += "'" + components[0] + "'";
		    }
		    
		} else {
	    // add the first component id into the component string
		    if (components[0].indexOf(":") == -1) {
		    	componentIds += "'EMAP:" + components[0] + "'";
		    } else {
		    	componentIds += "'" + components[0] + "'";
		    }
	    
		    // add other component ids into the component string
		    for (int i = 1; i < componentNumber; i++) {
				if (components[i].indexOf(":") == -1) {
				    componentIds += ", 'EMAP:" + components[i] + "'";
				} else {
				    componentIds += ", '" + components[i] + "'";
				}
		    }
		}
		String queryString = DBQuery.NUMBER_OF_SUBMISSION + DBQuery.ISH_BROWSE_ALL_TABLES + DBQuery.endsComponentQueryWithoutStagePart1;
		queryString += componentIds;
		queryString += DBQuery.endsComponentQueryWithoutStagePart2;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getTotalNumberOfSubmissionsForComponentsQuery = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
		    	totalNumber = Integer.toString(resSet.getInt(1));
		    }
		    
		    // close the database object
		    DBHelper.closePreparedStatement(prepStmt);
		} catch (SQLException se) {
		    se.printStackTrace();
		}
		return totalNumber;
    }
    
    /**
     * @param annotationTypes
     * @param annotationValue
     * @return
     */
    private int getAnnotationTypeIndex(String [] annotationTypes, String annotationValue){
        int index = -1;
        for(int i=0;i<annotationTypes.length;i++){
	    //            System.out.println("index of value for "+annotationValue+": "+annotationTypes[i].indexOf(annotationValue));
            if(annotationTypes[i] != null && annotationValue.indexOf(annotationTypes[i]) >=0) {
                index = i;
		//                System.out.println("\n\n"+annotationValue+":"+i+"\n\n");
            }
        }
        return index;
    }
    
    /**
     * @param criteria
     * @param componentList
     * @param annotationTypes
     * @return
     */
    private String appendSubsWithExprInCompsQ (String criteria, String [] componentList, String [] annotationTypes){
        StringBuffer annotatedGenesInComponentsQ = new StringBuffer("");
	//        System.out.println("criteria: " + criteria);
	//        System.out.println("componentList len: " + componentList.length);
	//        System.out.println("annotationTypes len: " + annotationTypes.length);
	
        //count to determine whether one, both or none of 'possible' or 'present' have been selected
        int presentOrPossibleCount = 0;
	
        //determine what types of expression user has selected - this will shape SQL
        int index = this.getAnnotationTypeIndex(annotationTypes, "present");
        String present = "";
        boolean presentSelected = false;
        if(index >= 0){
            present = annotationTypes[index];
            presentSelected = true;
            presentOrPossibleCount ++;
        }
        index = this.getAnnotationTypeIndex(annotationTypes, "not detected");
        String notDetected = "";
        boolean notDetSelected = false;
        if(index >=0) {
            notDetected = annotationTypes[index];
            notDetSelected = true;
        }
        index = this.getAnnotationTypeIndex(annotationTypes, "possibleuncertain");
        String possible = "";
        boolean possSelected = false;
        if(index >=0) {
            possible = annotationTypes[index];
            possSelected = true;
            presentOrPossibleCount ++;
        }
	
        //store present or possible values in array for incorporation into SQL 
        String [] presentOrPossible = new String [presentOrPossibleCount];
	
        //if user want to search for submissions with expression in any of their selected components 
        if(criteria != null && criteria.equals("any")){
            annotatedGenesInComponentsQ.append(DBQuery.JOIN_EXPRESSION_START);
            /*-- SQL will be different depending on what expression parameters user has selected --*/
            //if user has selected all expression conditions
            if((presentSelected || possSelected) && notDetSelected){
                annotatedGenesInComponentsQ.append(" ((");
                //store appropriate values in array
                if(presentSelected && possSelected){
                    presentOrPossible[0] = present;
                    presentOrPossible[1] = possible;
                }
                else if(presentSelected && !possSelected){
                    presentOrPossible[0] = present;
                }
                else if(!presentSelected && possSelected){
                    presentOrPossible[0] = possible;
                }
                annotatedGenesInComponentsQ.append(DBQuery.getExprInAnySelComponentsQ(componentList, presentOrPossible));
                annotatedGenesInComponentsQ.append(") OR (");
                annotatedGenesInComponentsQ.append(DBQuery.getNotDetExprInAnySelComponentsQ(componentList));
                annotatedGenesInComponentsQ.append(")) ");
            }
            //if user has selected 'possible/uncertain' or 'present' expression
            else if((presentSelected || possSelected) && !notDetSelected){
                //store approipriate values in array
                if(presentSelected && possSelected){
                    presentOrPossible[0] = present;
                    presentOrPossible[1] = possible;
                }
                else if(presentSelected && !possSelected){
                    presentOrPossible[0] = present;
                }
                else if(!presentSelected && possSelected){
                    presentOrPossible[0] = possible;
                }
                annotatedGenesInComponentsQ.append(DBQuery.getExprInAnySelComponentsQ(componentList, presentOrPossible));
            }
            //if user has only selected 'not detected' expression
            else if(!(presentSelected || possSelected) && notDetSelected){
                annotatedGenesInComponentsQ.append(DBQuery.getNotDetExprInAnySelComponentsQ(componentList));
            }
        }
        //else user has selected all
        else if (criteria != null && criteria.equals("all")){
            annotatedGenesInComponentsQ.append(" AND ? = (");
            //if user has selected all expression conditions
            if((presentSelected || possSelected) && notDetSelected){
                annotatedGenesInComponentsQ.append("SELECT (");
                //store approipriate values in array
                if(presentSelected && possSelected){
                    presentOrPossible[0] = present;
                    presentOrPossible[1] = possible;
                }
                else if(presentSelected && !possSelected){
                    presentOrPossible[0] = present;
                }
                else if(!presentSelected && possSelected){
                    presentOrPossible[0] = possible;
                }
                annotatedGenesInComponentsQ.append(DBQuery.getExprInAllSelComponentsQ(componentList, presentOrPossible));
                annotatedGenesInComponentsQ.append(") + (");
                annotatedGenesInComponentsQ.append(DBQuery.getNotDetExprInAllSelComponentsQ(componentList));
            }
            //if user has selected 'possible/uncertain' or 'present' expression
            else if((presentSelected || possSelected) && !notDetSelected) {
		
                //store approipriate values in array
                if(presentSelected && possSelected){
                    presentOrPossible[0] = present;
                    presentOrPossible[1] = possible;
                }
                else if(presentSelected && !possSelected){
                    presentOrPossible[0] = present;
                }
                else if(!presentSelected && possSelected){
                    presentOrPossible[0] = possible;
                }
                annotatedGenesInComponentsQ.append(DBQuery.getExprInAllSelComponentsQ(componentList, presentOrPossible));
            }
            //if user has only selected 'not detected' expression
            else if(!(presentSelected || possSelected) && notDetSelected){
                annotatedGenesInComponentsQ.append(DBQuery.getNotDetExprInAllSelComponentsQ(componentList));
            }
        } else {
            return null;
        }
        annotatedGenesInComponentsQ.append(DBQuery.PUBLIC_ENTRIES_Q);
        return annotatedGenesInComponentsQ.toString();
    }
    
    /**
     * return total no of submissions when searching expression in selected components
     * @param components
     * @return
     */
    public String getTotalNumberOfSubmissionsForComponentsQuery(String [] components, String start, String end, String [] annotationTypes, String criteria) {
	String totalNumber = new String("");
	String [] componentList = this.getComponentList(components, start, end, criteria);
	
	//annotatedGenesIncomponentsQ contains SQL to find annotated submissions matchin users parameters
	StringBuffer annotatedGenesInComponentsQ = new StringBuffer(DBQuery.NUMBER_OF_SUBMISSION + DBQuery.ISH_BROWSE_ALL_TABLES);
	if(this.appendSubsWithExprInCompsQ(criteria, componentList, annotationTypes) == null){
	    return "0";
	}
	annotatedGenesInComponentsQ.append(this.appendSubsWithExprInCompsQ(criteria, componentList, annotationTypes));
	
	//determine what types of expression user has selected - this will shape SQL
	int index = this.getAnnotationTypeIndex(annotationTypes, "present");
	boolean presentSelected = false;
	if(index >= 0){
	    presentSelected = true;
	}
	index = this.getAnnotationTypeIndex(annotationTypes, "not detected");
	boolean notDetSelected = false;
	if(index >=0) {
	    notDetSelected = true;
	}
	index = this.getAnnotationTypeIndex(annotationTypes, "possibleuncertain");
	boolean possSelected = false;
	if(index >=0) {
	    possSelected = true;
	}
	ResultSet resSet = null;
	ParamQuery parQ = null;
	PreparedStatement prepStat = null;
	parQ = new ParamQuery("",annotatedGenesInComponentsQ.toString());
	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    parQ.setPrepStat(conn);
	    prepStat = parQ.getPrepStat();
	    
	    //need to set appropriate parameters in SQL based on user specs
	    if(criteria.equals("any")){
		if((presentSelected || possSelected) && notDetSelected){
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+1, componentList[i]);
		    }
		    for(int i =0;i<componentList.length;i++){
			prepStat.setString(i+1+componentList.length, componentList[i]);
		    }
		}
		//if user has selected 'possible/uncertain' or 'present' or 'not detected' expression
		else {
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+1, componentList[i]);
		    }
		}
	    }
	    else if(criteria.equals("all")){
		prepStat.setInt(1, componentList.length);
		if((presentSelected || possSelected) && notDetSelected){
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+2, componentList[i]);
		    }
		    prepStat.setString(componentList.length+2, start);
		    prepStat.setString(componentList.length+3, end);
		    
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+4+componentList.length, componentList[i]);
		    }
		    prepStat.setString(componentList.length*2+4, start);
		    prepStat.setString(componentList.length*2+5, end);
		}
		//if user has selected 'possible/uncertain' or 'present' or 'not detected' expression
		else {
		    for(int i=0;i<componentList.length;i++){
			prepStat.setString(i+2, componentList[i]);
		    }
		    prepStat.setString(componentList.length+2, start);
		    prepStat.setString(componentList.length+3, end);
		}
	    }
	    //             System.out.println("\n\n"+prepStat.toString()+"\n\n");
	    
	    resSet = prepStat.executeQuery();
	    //             System.out.println("query executed");
	    
	    if(resSet.first()){
		totalNumber = resSet.getString(1);
	    } else {
		totalNumber = "0";
	    }
	    DBHelper.closePreparedStatement(prepStat);
	} catch (SQLException se) {
	    se.printStackTrace();
	}
	return totalNumber;
    }
    
    /**
     * @param accessionId
     * @return
     */
    public String findAssayTypeBySubmissionId(String accessionId) {
        String assayType = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_DETAILS");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, accessionId);
	    
            // execute
            resSet = prepStmt.executeQuery();
	    
            if (resSet.first()) {
                assayType = resSet.getString(3);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return assayType;
    }
    
    /**
     *
     */
    public String[][] findAllPIs() {
        String[][] pi = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("ALL_PIS");
        //		System.out.println("all pis query: " + parQ.getQuerySQL());
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            resSet = prepStmt.executeQuery();
            pi = formatResultSetToStringArray(resSet);
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return pi;
    }
    
    /**
     * 
     */
    public String[][] findPIFromName(String PI, int privilege) {
        String[][] pi = null;
        ResultSet resSet = null;
        if(privilege == 3 || privilege == 4) {
	    if(null != PI && !PI.equals("0")) {
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("PI_FROM_NAME");
		//		System.out.println("all pis query: " + parQ.getQuerySQL());
		PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    prepStmt.setString(1, PI);
		    resSet = prepStmt.executeQuery();
		    pi = formatResultSetToStringArray(resSet);
		    
		    // close the db object
		    DBHelper.closePreparedStatement(prepStmt);
		} catch (SQLException se) {
		    se.printStackTrace();
		}
	    } 
    	} else if(privilege >= 5) {
	    ParamQuery parQ = DBQuery.getParamQuery("ALL_PIS");
	    //		System.out.println("all pis query: " + parQ.getQuerySQL());
	    PreparedStatement prepStmt = null;
	    try {
            	// if disconnected from db, re-connected
            	conn = DBHelper.reconnect2DB(conn);
		
		parQ.setPrepStat(conn);
		prepStmt = parQ.getPrepStat();
		resSet = prepStmt.executeQuery();
		pi = formatResultSetToStringArray(resSet);
		
		// close the db object
		DBHelper.closePreparedStatement(prepStmt);
	    } catch (SQLException se) {
		se.printStackTrace();
	    }    	
    	}
        return pi;
    }    
    
    /**
     * @param resSet
     * @return
     * @throws SQLException
     */
    private String[][] formatResultSetToStringArray(ResultSet resSet) throws SQLException {
        ResultSetMetaData resSetData = resSet.getMetaData();
        int columnCount = resSetData.getColumnCount();
        if (resSet.first()) {
            resSet.last();
            int arraySize = resSet.getRow();
	    
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();
	    
            //create array to store each row of results in
            String[][] results = new String[arraySize][columnCount];
            int counter = 0;
            while (resSet.next()) {
                String[] columns = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columns[i] = resSet.getString(i + 1);
                }
                results[counter] = columns;
                counter++;
            }
            return results;
        }
        return null;
    }
    
    /**
     * <p>modified by xingjun - 28/01/2009
     * - leave the result as empty string if it's '1000-01-01'
     * when user create their new submission through the annotation interface, a default 
     * value of '1000-01-01 will be allocated by the application; it will be modified by
     * derek's code when the other meta data are loaded in'</p>
     * @param labId
     */
    public String findLastEntryDateInDBByLabId(int labId) {
        //		System.out.println("labId: " + labId);
        String result = "";
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("LAB_LATEST_ENTRY");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setInt(1, labId);
            resSet = prepStmt.executeQuery();
	    
            // added check on resSet to avoid crash - Mantis 609
            if (resSet != null){
		if (resSet.first()) {
		    java.util.Date lastEntryDate = resSet.getDate(1);
		    if(null != lastEntryDate) {
			DateFormat df =
			    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
			result = df.format(lastEntryDate);
			if (result.equals("01-Jan-1000")) {
			    result = "";
			}
		    }
		}
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @param labId
     * @param location: 1-db; 0-ftp
     */
    public ArrayList findSubmissionSummaryByLabId(int labId, int location) {
        ArrayList submissionSummary = null;
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        ParamQuery parQ = null;
        if (location == 1) { // summary in db wanted
            parQ = DBQuery.getParamQuery("TOTOAL_SUBMISSION_OF_LAB_DB");
        } else if (location == 0) { // summary in ftp wanted
            parQ = DBQuery.getParamQuery("TOTOAL_SUBMISSION_OF_LAB_FTP");
        }
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setInt(1, labId);
	    
            // execute
            resSet = prepStmt.executeQuery();
            submissionSummary = DBHelper.formatResultSetToArrayList(resSet);
	    
            // close the database object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return submissionSummary;
    }    
    
    /**
     * <p>modified by xingjun - 01/10/2009 - rename the second param from location to dbStatus</p>
     * @param labId
     * @param dbStatus
     */
    public ArrayList[] findSubmissionSummaryByLabIdForAnnotation(int labId, int[] dbStatus) {
        ArrayList[] submissionSummary = null;        
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        ParamQuery parQ = null;
        if(null != dbStatus) {
	    submissionSummary = new ArrayList[dbStatus.length];
	    parQ = DBQuery.getParamQuery("TOTAL_SUBMISSION_OF_LAB_DB_ANNOTATION");
	    for(int i = 0; i < submissionSummary.length; i++) {
		submissionSummary[i] = new ArrayList();
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    parQ.setPrepStat(conn);
		    prepStmt = parQ.getPrepStat();
		    prepStmt.setInt(1, labId);
		    prepStmt.setInt(2, dbStatus[i]);
		    // execute
		    resSet = prepStmt.executeQuery();
		    submissionSummary[i] = DBHelper.formatResultSetToArrayList(resSet);
		    //		        	if (labId == 35) {
		    //		        		System.out.println("findSubmissionSummaryByLabIdForAnnotation:" + labId + ":i: " + i + " dbStatus: " + dbStatus[i]);
		    //			            if (submissionSummary[i] != null && submissionSummary[i].get(0) != null) {
		    //			            	for (int j=0;j<submissionSummary[i].size();j++) {
		    //			            		for (int m=0;m<((String[])submissionSummary[i].get(j)).length;m++)
		    //			            		System.out.println("m: " + m + ":" + ((String[])submissionSummary[i].get(j))[m]);
		    //			            	}
		    //			            }
		    //		        	}
		    
		    // close the database object
		    DBHelper.closePreparedStatement(prepStmt);
		} catch (SQLException se) {
		    se.printStackTrace();
		}
	    }
        }
        return submissionSummary;
    }
    
    /**
     * modified by xingjun --- 02/08/2007 -- allow to deal with data with different assay type
     * 1. added an extra parameter: assayType
     * 2. modified the sql statement
     * 
     */
    public ISHBrowseSubmission[] getSubmissionsByLabId(String labId, String assayType,
                                                       String submissionDate,
                                                       String[] order,
                                                       String offset,
                                                       String num) {
	//    	System.out.println("GET SUBMISSION BY LAB ID");
        ResultSet resSet = null;
        ISHBrowseSubmission[] result = null;
        ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH");
        PreparedStatement prepStmt = null;
	
        // assemble the query string
        String query = parQ.getQuerySQL();
        if (submissionDate == null || submissionDate.equals("")) {
            query += " AND SUB_PI_FK = ? AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
        } else {
            query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? AND SUB_ASSAY_TYPE = '" + assayType + "' ";
        }
        String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
        String queryString =
            DBHelper.assembleBrowseSubmissionQueryStringISH(1, query,
                                                            defaultOrder,
                                                            order, offset,
                                                            num);
	//        		System.out.println("getSubmissionsByLabId:queryString: " + queryString);
	
        int lab = -1;
        try {
            lab = Integer.parseInt(labId);
	    
        } catch (NumberFormatException nfe) {
            lab = 0;
        }
        //		System.out.println("lab id: " + lab);
        //		System.out.println("submissionDate: " + submissionDate);
	
        // execute query and assemble result
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
            prepStmt = conn.prepareStatement(queryString);
	    
            if (submissionDate == null || submissionDate.equals("")) {
                prepStmt.setInt(1, lab);
            } else {
                prepStmt.setInt(1, Integer.parseInt(labId));
                prepStmt.setString(2, submissionDate);
            }
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionsByLabId = "+prepStmt);
            resSet = prepStmt.executeQuery();
            result = formatBrowseResultSet(resSet);
	    
            // close the connection
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * separated query based on assay type - ish, ihc, array, transgenic</p>
     * 
     */
    public ArrayList getSubmissionsByLabId(String labId, String assayType,
					   String submissionDate, int columnIndex, boolean ascending, int offset, int num) {
    	//System.out.println("GET SUBMISSION BY LAB ID");
		try { // return null value if lab id is not valid
		    Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
		    return null;
		}
		ResultSet resSet = null;
    	ArrayList result = null;
    	ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH");
    	PreparedStatement prepStmt = null;
	
    	// assemble the query string
    	// modified by xingjun - 28/08/2008
    	String query = parQ.getQuerySQL();
    	if (submissionDate == null || submissionDate.equals("")) {
	    query += " AND SUB_PI_FK = ? ";
    	} else {
	    query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? ";
    	}
	
    	// added by xingjun - 28/08/2008
    	if (assayType.equals("insitu")) {
	    query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
    	} else {
	    query += "AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
    	}
	
    	String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
    	String queryString = DBHelper.assembleBrowseSubmissionQueryStringISH(1, query,
									     defaultOrder, columnIndex, ascending, offset, num);
	//		System.out.println("getSubmissionsByLabId:queryString: " + queryString);
	
    	int lab = -1;
    	try {
	    lab = Integer.parseInt(labId);
    	} catch (NumberFormatException nfe) {
	    lab = 0;
    	}
	//    	System.out.println("lab id: " + lab);
	//    	System.out.println("submissionDate: " + submissionDate);
	
    	// execute query and assemble result
    	try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    
		    if (submissionDate == null || submissionDate.equals("")) {
		    	prepStmt.setInt(1, lab);
		    } else {
				prepStmt.setInt(1, Integer.parseInt(labId));
				prepStmt.setString(2, submissionDate);
		    }
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionsByLabId = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    //    		result = formatBrowseResultSet2ArrayList(resSet);
		    result = DBHelper.formatBrowseResultSetISH(resSet);
		    
		    // close the connection
		    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * @author xingjun - 01/07/2011 - overloading version
     * <p>xingjun - TG query should use different query to get data (gene symbol stored in mutant table instead of probe table)</p>
     */
    public ArrayList getSubmissionsByLabId(String labId, String assayType, String submissionDate, 
					   String archiveId, int columnIndex, boolean ascending, int offset, int num, String batchId) {
    	if (debug)
    		System.out.println("GET SUBMISSION BY LAB ID");
    	

		ResultSet resSet = null;
    	ArrayList result = null;
    	ParamQuery parQ;
    	if(assayType != null){
	    	if (assayType.equalsIgnoreCase("TG")) {
			    parQ = InsituDBQuery.getParamQuery("ALL_ENTRIES_TG");
	    	} else {
			    parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH");
	    	}
    	}
    	else
    		parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH");
	
    	PreparedStatement prepStmt = null;
	
    	// assemble the query string
    	String query = parQ.getQuerySQL();
    	
    	if (labId != null && !labId.trim().equals("")) {
    		query += " AND SUB_PI_FK = ? ";
    	}
   	
    	if (submissionDate != null && !submissionDate.equals("")) {
    		query += " AND SUB_SUB_DATE = ? ";
    	} 
    	
    	if (archiveId != null && !archiveId.trim().equals("")) {
    		query += " AND SUB_ARCHIVE_ID = ? ";
    	}
	
    	if (assayType != null && !assayType.trim().equals("")) {
	    	if (assayType.equals("insitu")) {
	    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
	    	} else {
	    		query += "AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
	    	}
    	}
    	else
    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
    	
    	if (batchId != null && !batchId.trim().equals("")){
    		if (!batchId.trim().equals(""))
    			query += " AND SUB_BATCH = ? ";
    	}
    	
    	String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
    	String queryString = DBHelper.assembleBrowseSubmissionQueryStringISH(1, query,
									     defaultOrder, columnIndex, ascending, offset, num);
    	
    	if (debug){
			System.out.println("getSubmissionsByLabId:queryString: " + queryString);
	    	System.out.println("lab id: " + labId);
	    	System.out.println("assayType: " + assayType);
	    	System.out.println("submissionDate: " + submissionDate);
	    	System.out.println("archiveId: " + archiveId);
    	}
	
    	// execute query and assemble result
    	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    int paramNum = 1;
	    
	    if (labId != null && !labId.trim().equals("")) {
	    	prepStmt.setInt(1, Integer.parseInt(labId));
			paramNum ++;
	    }
	    
	    if (submissionDate != null && !submissionDate.equals("")) {
			prepStmt.setString(paramNum, submissionDate);
			paramNum ++;
	    }
	    
	    if (archiveId != null && !archiveId.trim().equals("")) {
	    	prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
	    	paramNum ++;
	    }

	    if (batchId != null && !batchId.trim().equals("")) {
	    		prepStmt.setInt(paramNum, Integer.parseInt(batchId));
	    }
	    
	    if (debug)
	    	System.out.println("MySQLISHDAOImp:getSubmissionsByLabId = "+prepStmt);
	    
	    resSet = prepStmt.executeQuery();
	    result = DBHelper.formatBrowseResultSetISH(resSet);
	    
	    // close the connection
	    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    public ArrayList getSubmissionsForAnnotationByLabId(String labId, String assayType,
							String submissionDate, int columnIndex, boolean ascending, int offset, int num, String isPublic) {
    	//System.out.println("GET SUBMISSION BY LAB ID");
		try { // return null value if lab id is not valid
		    Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
		    return null;
		}
		ResultSet resSet = null;
    	ArrayList result = null;
    	ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH_FOR_ANNOTATION");
    	PreparedStatement prepStmt = null;
	
    	// assemble the query string
    	String query = parQ.getQuerySQL();
    	if (submissionDate == null || submissionDate.equals("")) {
    		query += " AND SUB_PI_FK = ? AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
    	} else {
    		query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? AND SUB_ASSAY_TYPE = '" + assayType + "' ";
    	}
    	String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
    	String queryString = DBHelper.assembleBrowseSubmissionQueryStringISH(1, query,
									     defaultOrder, columnIndex, ascending, offset, num);
	
    	int lab = -1;
    	try {
    		lab = Integer.parseInt(labId);
    	} catch (NumberFormatException nfe) {
    		lab = 0;
    	}

    	// execute query and assemble result
    	try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    if (submissionDate == null || submissionDate.equals("")) {
				prepStmt.setString(1, isPublic);
				prepStmt.setInt(2, lab);
		    } else {
				prepStmt.setString(1, isPublic);
				prepStmt.setInt(2, Integer.parseInt(labId));
				prepStmt.setString(3, submissionDate);
		    }
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionsForAnnotationByLabId = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    //    		result = formatBrowseResultSet2ArrayList(resSet);
		    result = DBHelper.formatBrowseResultSetISHForAnnotation(resSet);
		    
		    // close the connection
		    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * @author xingjun - 01/07/2011 - overloading version
     */
    public ArrayList getSubmissionsForAnnotationByLabId(String labId, String assayType, String submissionDate, 
							String archiveId, int columnIndex, boolean ascending, int offset, int num, String isPublic) {

		try { // return null value if lab id is not valid
		    Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
		    return null;
		}
		ResultSet resSet = null;
    	ArrayList result = null;
    	ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH_FOR_ANNOTATION");
    	PreparedStatement prepStmt = null;
	
    	// assemble the query string
    	String query = parQ.getQuerySQL();
    	if (submissionDate == null || submissionDate.equals("")) {
	    query += " AND SUB_PI_FK = ? AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
    	} else {
	    query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? AND SUB_ASSAY_TYPE = '" + assayType + "' ";
    	}
	
		if (archiveId != null && !archiveId.trim().equals("")) {
		    query += " AND SUB_ARCHIVE_ID = ? ";
		}
		
		String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
    	String queryString = DBHelper.assembleBrowseSubmissionQueryStringISH(1, query,
									     defaultOrder, columnIndex, ascending, offset, num);

	
    	// execute query and assemble result
    	try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, isPublic);
		    prepStmt.setInt(2, Integer.parseInt(labId));
		    int paramNum = 3;
		    
		    if (submissionDate != null && !submissionDate.equals("")) {
				prepStmt.setString(paramNum, submissionDate);
				paramNum ++;
		    }
		    
		    if (archiveId != null && !archiveId.trim().equals("")) {
		    	prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
		    }
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getSubmissionsForAnnotationByLabId = "+prepStmt);
		    
		    resSet = prepStmt.executeQuery();
		    result = DBHelper.formatBrowseResultSetISHForAnnotation(resSet);
		    
		    // close the connection
		    DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
	    se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * modified by xingjun -- 02/08/2007 -- allow to accept IHC data
     * 1. modified sql
     */
    public String getTotalNumberOfISHSubmissionsForLabQuery(String labId, String submissionDate) {
        String totalNumber = new String("");
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION");
        PreparedStatement prepStmt = null;
        String query = parQ.getQuerySQL();
        if (submissionDate == null || submissionDate.equals("")) {
            query += " AND SUB_PI_FK = ? AND SUB_ASSAY_TYPE = 'ISH' ";
        } else {
            query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? AND SUB_ASSAY_TYPE = 'ISH' ";
        }
        int lab = -1;
        try {
            lab = Integer.parseInt(labId);
	    
        } catch (NumberFormatException nfe) {
            lab = 0;
        }
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(query);
            if (submissionDate == null || submissionDate.equals("")) {
                prepStmt.setInt(1, lab);
            } else {
                prepStmt.setInt(1, Integer.parseInt(labId));
                prepStmt.setString(2, submissionDate);
            }
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:getTotalNumberOfISHSubmissionsForLabQuery = "+prepStmt);
	    
            // execute
            resSet = prepStmt.executeQuery();
	    
            if (resSet.first()) {
                totalNumber = Integer.toString(resSet.getInt(1));
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * find total number of ihc submissions from specific lab on specific date 
     */
    public String getTotalNumberOfIHCSubmissionsForLabQuery(String labId, String submissionDate) {
        String totalNumber = new String("");
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION");
        PreparedStatement prepStmt = null;
        String query = parQ.getQuerySQL();
        if (submissionDate == null || submissionDate.equals("")) {
            query += " AND SUB_PI_FK = ? AND SUB_ASSAY_TYPE = 'IHC' ";
        } else {
            query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? AND SUB_ASSAY_TYPE = 'IHC' ";
        }
        int lab = -1;
        try {
            lab = Integer.parseInt(labId);
	    
        } catch (NumberFormatException nfe) {
            lab = 0;
        }
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(query);
            if (submissionDate == null || submissionDate.equals("")) {
                prepStmt.setInt(1, lab);
            } else {
                prepStmt.setInt(1, Integer.parseInt(labId));
                prepStmt.setString(2, submissionDate);
            }
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:getTotalNumberOfIHCSubmissionsForLabQuery = "+prepStmt);
	    
            // execute
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                totalNumber = Integer.toString(resSet.getInt(1));
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
    }
    
    /**
     * @param labId
     */
    public String findPIByLabId(String labId) {
        String result = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("PI_NAME");
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setInt(1, Integer.parseInt(labId));
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                result = resSet.getString(1);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * find the number of total ISH submissions users sent to GUDMAP DB
     */
    public int findTotalNumberOfSubmissionISH() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_ISH");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     *
     */
    public int findNumberOfPublicSubmissionISH() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("NUMBER_OF_PUBLIC_SUBMISSIONS_ISH");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * 
     */
    public int findTotalNumberOfSubmissionIHC() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_IHC");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * 
     */
    public int findNumberOfPublicSubmissionIHC() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("NUMBER_OF_PUBLIC_SUBMISSIONS_IHC");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * find the number of total Microarray submissions users sent to GUDMAP DB
     */
    public int findTotalNumberOfSubmissionArray() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_ARRAY");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     *
     */
    public int findNumberOfPublicSubmissionArray() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("NUMBER_OF_PUBLIC_SUBMISSIONS_ARRAY");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @author xingjun - 26/08/2008
     * @return
     */
    public int findNumberOfPublicSubmissionTG() {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("NUMBER_OF_PUBLIC_SUBMISSIONS_TG");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * find the number of public gene entries related to given assay type
     * <p>xingjun - 02/02/2009 - modified sql</p>
     * <p>xingjun - 15/09/2011 - retrieving TG data need different sql</p>
     */
    public int findNumberOfPublicGenes(String assayType) {
	
	// Modified to use statement insteda of prepared statement (23/11/09) - but this should be undone when moving to the most recent MySql     	
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (assayType.equalsIgnoreCase("TG")) {
            parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE_TG");
        } else { // ISH or IHC 
            parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE");
	    
        }
        PreparedStatement prepStmt = null;
	
        String queryString = parQ.getQuerySQL().replace("?", "'"+assayType+"'");
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
            prepStmt = conn.prepareStatement(queryString);
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:findNumberOfPublicGenes = "+prepStmt);
            resSet = prepStmt.executeQuery();
	    
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
	    //            DBHelper.closeStatement(stmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     *
     */
    public String findLastEditorialUpdateDate() {
        String result = "Not Available";
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("LAST_ENTRY_DATE_EDITORIAL");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                java.util.Date lastEditorialUpdate = resSet.getDate(1);
                DateFormat df =
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                result = df.format(lastEditorialUpdate);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     *
     */
    public String findLastSoftwareUpdateDate() {
        String result = "Not Available";
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("LAST_SOFTWARE_UPDATE_DATE");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                java.util.Date lastEditorialUpdate = resSet.getDate(1);
                DateFormat df =
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                result = df.format(lastEditorialUpdate);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     *
     */
    public String findLastEntryDateInDB() {
        String result = "Not Available";
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("LAST_ENTRY_DATE_DB");
	//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    //            parQ.setPrepStat(conn);
	    //            prepStmt = parQ.getPrepStat();
	    //            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                java.util.Date lastEditorialUpdate = resSet.getDate(1);
                DateFormat df =
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                result = df.format(lastEditorialUpdate);
            }
	    
            // close the db object
	    //            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }

    /**
     * @param submissionAccessionId
     * @param serialNum
     * return
     */
    public ImageDetail findWlzImageDetailBySubmissionId(String submissionAccessionId) {
        ImageDetail result = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_WLZ_DETAIL");// in situ
        PreparedStatement prepStmt = null;
	
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    
		    // image detail
		    parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
            
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:findWlzImageDetailBySubmissionId:sql: " + prepStmt);
            
            resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				result = new ImageDetail();
				result.setAccessionId(resSet.getString(1));
				result.setGeneSymbol(resSet.getString(2));
				result.setGeneName(resSet.getString(3));
				result.setStage(resSet.getString(4));
				result.setSpecimenType(resSet.getString(7));
				result.setFilePath(resSet.getString(8));
				result.setClickFilePath(resSet.getString(9));
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @param submissionAccessionId
     * @param serialNum
     * return
     */
    public ImageDetail findImageDetailBySubmissionId(String submissionAccessionId,
                                                     int serialNum) {
        ImageDetail result = null;
        ResultSet resSet = null;
        ResultSet resSetAllImageNotesInSameSubmission = null;
        ResultSet resSetPublicImgs = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_IMAGE_DETAIL");// in situ
        ParamQuery parQTG = InsituDBQuery.getParamQuery("SUBMISSION_IMAGE_DETAIL_TG"); // tg
        ParamQuery parQAllImageNotesInSameSubmission = DBQuery.getParamQuery("ISH_SUBMISSION_ALL_IMAGE_NOTES");
        ParamQuery parQPublicImgs = DBQuery.getParamQuery("ISH_SUBMISSION_PUBLIC_IMGS");
        PreparedStatement prepStmt = null;
	//        String queryStringInSitu = parQ.getQuerySQL();
        String queryStringTG = parQTG.getQuerySQL();
        PreparedStatement prepStmtTG = null;
        PreparedStatement prepStmtAllImageNotesInSameSubmission = null;
        PreparedStatement prepStmtPublicImgs = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    conn.setAutoCommit(false);
		    //        	System.out.println("findImageDetailBySubmissionId:sql: " + parQ.getQuerySQL());
            // image detail
		    parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
			if (debug)
			    System.out.println("MySQLISHDAOImpfindImageDetailBySubmissionId prepStmtTG= "+prepStmt);
            resSet = prepStmt.executeQuery();
	    
            // xingjun - 14/09/2011
            if (!resSet.first()) {// not a insitu submission. go to get the image from tg table
		//            	System.out.println("findImageDetailBySubmissionId:it's a tg submission");
                prepStmtTG = conn.prepareStatement(queryStringTG);
                prepStmtTG.setString(1, submissionAccessionId);
                prepStmtTG.setInt(2, serialNum);
				if (debug)
				    System.out.println("MySQLISHDAOImpfindImageDetailBySubmissionId prepStmtTG= "+prepStmtTG);
                resSet = prepStmtTG.executeQuery();
            }
            resSet.beforeFirst();// reset the pointer for resSet
	    
	    
            // all image notes in the same submission
            parQAllImageNotesInSameSubmission.setPrepStat(conn);
            prepStmtAllImageNotesInSameSubmission = parQAllImageNotesInSameSubmission.getPrepStat();
            prepStmtAllImageNotesInSameSubmission.setString(1, submissionAccessionId);
			if (debug)
			    System.out.println("MySQLISHDAOImpfindImageDetailBySubmissionId prepStmtAllImageNotesInSameSubmission= "+prepStmtAllImageNotesInSameSubmission);
            resSetAllImageNotesInSameSubmission = prepStmtAllImageNotesInSameSubmission.executeQuery();
	    
            //all public images
            parQPublicImgs.setPrepStat(conn);
            prepStmtPublicImgs =parQPublicImgs.getPrepStat();
            prepStmtPublicImgs.setString(1, submissionAccessionId);
			if (debug)
			    System.out.println("MySQLISHDAOImpfindImageDetailBySubmissionId prepStmtPublicImgs= "+prepStmtPublicImgs);
            resSetPublicImgs = prepStmtPublicImgs.executeQuery();
            if (resSet.first()) {
                result = formatImageDetailResultSet(resSet, resSetAllImageNotesInSameSubmission,resSetPublicImgs, serialNum);
            }
	    
            conn.commit();
            conn.setAutoCommit(true);
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @param imageResSet
     * @param serialNo
     * @throws SQLException
     * @author xingjun 17th May 2006
     */
    public ImageDetail formatImageDetailResultSet(ResultSet resSet,
			  ResultSet resSetAllImageNotesInSameSubmission,
			  ResultSet resSetPublicImgs, int serialNum) throws SQLException {
        //		  System.out.println("obtained the image detail!!!!!!!!!!!");
        if (resSet.first()) {
            ImageDetail imageDetail = new ImageDetail();
            imageDetail.setAccessionId(resSet.getString(1));
            imageDetail.setGeneSymbol(resSet.getString(2));
            imageDetail.setGeneName(resSet.getString(3));
            imageDetail.setStage(resSet.getString(4));
            imageDetail.setAge(resSet.getString(5));
            imageDetail.setAssayType(resSet.getString(6));
            imageDetail.setSpecimenType(resSet.getString(7));
            imageDetail.setFilePath(resSet.getString(8));
            imageDetail.setSerialNo(Integer.toString(serialNum + 1));
	    
            // get all image notes in the same submission: for image viewer disposal
            // currently assume one image was linked to one note entry. may change in the future
            if (resSetAllImageNotesInSameSubmission.first()) {
            	ArrayList<String[]> allImageNotes = new ArrayList<String[]>();
            	resSetAllImageNotesInSameSubmission.beforeFirst();
            	String[] filenamenNote = null;
            	while (resSetAllImageNotesInSameSubmission.next()) {
				    filenamenNote = new String[2];
				    filenamenNote[0] = Utility.netTrim(resSetAllImageNotesInSameSubmission.getString(1));
				    filenamenNote[1] = Utility.netTrim(resSetAllImageNotesInSameSubmission.getString(2));
				    if (null != filenamenNote[0] || null !=  filenamenNote[1])
				    allImageNotes.add(filenamenNote);
            	}
            	imageDetail.setAllImageNotesInSameSubmission(allImageNotes);
            }
            //get list of the image file names for this submission which are marked as public
            if(resSetPublicImgs.first()){
            	ArrayList<String> publicImgs = new ArrayList<String>();
            	resSetPublicImgs.beforeFirst();
            	while(resSetPublicImgs.next()){
            		publicImgs.add(resSetPublicImgs.getString(1));
            	}
            	imageDetail.setAllPublicImagesInSameSubmission(publicImgs);
            }
            return imageDetail;
        }
        return null;
    }
    
    /**
     * used for collection operation
     * @param sumbission ids
     * @param
     * @return
     */
    public ISHBrowseSubmission[] getSubmissionBySubmissionId(String[] submissionIds,
                                                             String[] order,
                                                             String offset,
                                                             String num) {
        ResultSet resSet = null;
        ISHBrowseSubmission[] result = null;
        ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH");
        PreparedStatement prepStmt = null;
	
        // assemble the query string
        String query = parQ.getQuerySQL();
	
        // append submission id condition
        int submissionNumber = submissionIds.length;
        if (submissionNumber == 1) {
            query += " AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
        } else {
            query += " AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            for (int i = 1; i < submissionNumber; i++) {
                query += ", '" + submissionIds[i] + "'";
            }
            query += ") ";
        }
	
        String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
        String queryString =
            DBHelper.assembleBrowseSubmissionQueryStringISH(1, query,
                                                            defaultOrder,
                                                            order, offset,
                                                            num);
        //System.out.println("collection query String: " + queryString);
	
        // execute query and assemble result
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            prepStmt = conn.prepareStatement(queryString);
    	    if (debug)
    	    	System.out.println("MySQLISHDAOImp:getSubmissionBySubmissionId = "+prepStmt);
            resSet = prepStmt.executeQuery();
            result = formatBrowseResultSet(resSet);
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    
    /**
     * @param submissionAccessionId
     * @param componentId
     *
     */
    public ExpressionDetail findExpressionDetailBySubmissionIdAndComponentId(String submissionAccessionId,
                                                                             String componentId) {
        ExpressionDetail expression = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("EXPRESSION_DETAIL");
        PreparedStatement prepStmt = null;
	//        		  System.out.println("expression query: " + parQ.getQuerySQL());
	//        		  System.out.println("submissionAccessionId: " + submissionAccessionId);
	//        		  System.out.println("componentId: " + componentId);
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
            prepStmt.setString(2, componentId);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {          
                expression = formatExpressionDetailResultSet(resSet);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            return expression;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     *
     * @param resSet
     * @return
     * @throws SQLException
     */
    private ExpressionDetail formatExpressionDetailResultSet(ResultSet resSet) throws SQLException {
        ExpressionDetail expression = null;
	String str = null;
	String [] components = null;
	ArrayList componentList = null;
        if (resSet.first()) {
            expression = new ExpressionDetail();
            expression.setComponentId(resSet.getString(1));
            expression.setComponentName(resSet.getString(2));
	    str = Utility.netTrim(resSet.getString(3));
	    if (null == str) {
		expression.setComponentDescription(null);
	    } else {
		
		components = str.split("\\.");
		componentList = reformatComponentFullPath(components);
		expression.setComponentDescription(componentList);
	    }
            expression.setPrimaryStrength(resSet.getString(4));
            expression.setSecondaryStrength(resSet.getString(5));
            expression.setExpressionId(resSet.getInt(6));
            expression.setStage("TS" + resSet.getString(7));
            expression.setSubmissionDbStatus(resSet.getInt(9));

            return expression;
        }
        return null;
    }
    
    /**
     * returns an array of components reformatted for display in 
     * expressoin detail pages of application
     * @return componentList - list of reformatted components
     * @param components - an array of components making up the full path of to a component
     */
    private ArrayList reformatComponentFullPath(String [] components) {
        ArrayList<String> componentList = new ArrayList<String>();
        for(int i=0;i< components.length;i++){
            StringBuffer component = new StringBuffer("");
            for(int j=0;j<i;j++){
                component.append(". . ");
            }
            component.append(components[i]);
	    //            System.out.println(components[i]);
            componentList.add(component.toString());
        }
        return componentList;
    }
    
    /**
     *
     * @param submissionAccessionId
     * @param componentId
     *
     */
    public String findAnnotationNote(String submissionAccessionId,
                                     String componentId) {
        String expressionNote = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("EXPRESSION_NOTE");
        PreparedStatement prepStmt = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
            prepStmt.setString(2, componentId);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:findAnnotationNote = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                expressionNote = DBHelper.formatResultSetToString(resSet);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            return expressionNote;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    //created by chris - may need to be changed by Xingjun.
    public ExpressionPattern[] findPatternsAndLocations(String expressionId) {
    	if (expressionId == null) {
	    //            throw new NullPointerException("id parameter or componentId parameter");
	    return null;
        }
        ExpressionPattern[] pattern = null;
        ResultSet resSetPattern = null;
        ResultSet resSetLocations = null;
        ParamQuery parQ = DBQuery.getParamQuery("EXPRESSION_PATTERNS");
        //System.out.println(parQPatterns.getQuerySQL());
	
        PreparedStatement prepStmtPatterns = null;
        PreparedStatement prepStmtLocations = null;
        PreparedStatement compNmeStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
            // execute
            parQ.setPrepStat(conn);
            prepStmtPatterns = parQ.getPrepStat();
            prepStmtPatterns.setString(1, expressionId);
            resSetPattern = prepStmtPatterns.executeQuery();
            if (resSetPattern.first()) {
                //need to get number of rows to determine size of array
                resSetPattern.last();
                int numPatterns = resSetPattern.getRow();
		
                //set size of patterns array based on num rows
                pattern = new ExpressionPattern[numPatterns];
		
                //return the result set to the start
                resSetPattern.beforeFirst();
		
                //integer to put data into the correct element in the pattern array object
                int index = 0;
		
                //need to fill each item in array with correct data
                while (resSetPattern.next()) {
		    
                    pattern[index] = new ExpressionPattern();
                    pattern[index].setPattern(resSetPattern.getString(2));
                    parQ = DBQuery.getParamQuery("EXP_PATTERN_LOCATIONS");
		    
                    //create statements need to find expression locations
                    parQ.setPrepStat(conn);
                    //System.out.println("prepstat set");
                    //System.out.println(parQLocations.getQuerySQL());
                    prepStmtLocations = parQ.getPrepStat();
                    prepStmtLocations.setString(1, resSetPattern.getString(1));
                    resSetLocations = prepStmtLocations.executeQuery();
                    StringBuffer locations = null;
		    String str = null;
		    String atnPubIdVal = null;
		    ResourceBundle  bundle = ResourceBundle.getBundle("configuration");
		    String anatIdPrefix = bundle.getString("anatomy_id_prefix");
		    ResultSet compNmeSet = null;
                                
                    if (resSetLocations.first()) {
			//                        System.out.println("resSetLocations has results");
                        locations = new StringBuffer("");
                        resSetLocations.beforeFirst();
			String adjacentTxt = "adjacent to ";
                        while (resSetLocations.next()) {
                            //if the location string begins with 'adjacent to ', further query is required
                            str = Utility.netTrim(resSetLocations.getString(1));
                            if(null != str && str.indexOf(adjacentTxt) >= 0) {
                                //the components public id is a substring of the location string 
                                atnPubIdVal = str.substring(adjacentTxt.length());
				
                                /*need to get the anatomy prefix and attach it to the id obtained previously
				  then query the database to get the mane of the component*/
                                parQ = DBQuery.getParamQuery("COMPONENT_NAME_FROM_ATN_PUBLIC_ID");
                                parQ.setPrepStat(conn);
                                compNmeStmt = parQ.getPrepStat();
                                compNmeStmt.setString(1, anatIdPrefix+atnPubIdVal);
                                compNmeSet = compNmeStmt.executeQuery();
                                if(compNmeSet.first()){
                                    locations.append(adjacentTxt+ compNmeSet.getString(1));
                                }
                            }
                            else {
                                locations.append(resSetLocations.getString(1));
                            }
			    
                            if(!resSetLocations.isLast()){
                                locations.append(", ");
                            }
                        }
                        if(locations != null){
                            pattern[index].setLocations(locations.toString());
                        }
                    }
                    index++;
                }
            }
	    
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
            //close the statements
            DBHelper.closePreparedStatement(prepStmtPatterns);
            DBHelper.closePreparedStatement(prepStmtLocations);
            DBHelper.closePreparedStatement(compNmeStmt);
        }
        return pattern;
    }
    
    /**
     * 
     */
    public ExpressionPattern[] findPatternsAndLocations(boolean forEditing, String expressionId) {
	//    	System.out.println("getPatterns method in dao########");
        if (expressionId == null) {
	    //            throw new NullPointerException("id parameter or componentId parameter");
	    return null;
        }
        ExpressionPattern[] patterns = null;
        ArrayList<ExpressionPattern> patternList = null;
        ResultSet resSetPattern = null;
        ResultSet resSetLocations = null;
        ParamQuery parQ = DBQuery.getParamQuery("EXPRESSION_PATTERNS");
        PreparedStatement prepStmtPatterns = null;
        PreparedStatement prepStmtLocations = null;
        PreparedStatement compNmeStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
            conn.setAutoCommit(false);
	    
            // execute
            parQ.setPrepStat(conn);
            prepStmtPatterns = parQ.getPrepStat();
            prepStmtPatterns.setInt(1, Integer.parseInt(expressionId));
            resSetPattern = prepStmtPatterns.executeQuery();
            if (resSetPattern.first()) {
            	patternList = new ArrayList<ExpressionPattern>();
                resSetPattern.beforeFirst();
		
                //need to fill each item in array with correct data
                while (resSetPattern.next()) {
		    ExpressionPattern pattern = new ExpressionPattern();
		    int expId = Integer.parseInt(expressionId);
                    pattern.setExpressionId(expId);
                    int patternId = resSetPattern.getInt(1);
                    pattern.setPatternId(patternId);
                    String patternValue = resSetPattern.getString(2);
                    pattern.setPattern(patternValue);
                    parQ = DBQuery.getParamQuery("EXP_PATTERN_LOCATIONS");
		    
                    //create statements need to find expression locations
                    parQ.setPrepStat(conn);
		    //                    System.out.println("prepstat set");
                    //System.out.println(parQLocations.getQuerySQL());
                    prepStmtLocations = parQ.getPrepStat();
                    prepStmtLocations.setInt(1, resSetPattern.getInt(1));
                    resSetLocations = prepStmtLocations.executeQuery();
                    StringBuffer locations = null;
		    String str = null;
		    String adjacentTxt = "adjacent to ";
		    String atnPubIdVal = null;
		    ResourceBundle bundle = ResourceBundle.getBundle("configuration");
		    String  anatIdPrefix = bundle.getString("anatomy_id_prefix");
		    ResultSet compNmeSet = null;
		    
                    if (resSetLocations.first()) {
			//                        System.out.println("resSetLocations has results");
                    	if (!forEditing) {
                            locations = new StringBuffer("");
                            resSetLocations.beforeFirst();
                            while (resSetLocations.next()) {
                                //if the location string begins with 'adjacent to ', further query is required
                                str = Utility.netTrim(resSetLocations.getString(1));
                                if(null != str && str.indexOf(adjacentTxt) >= 0) {
				    
                                    //the components public id is a substring of the location string 
                                    atnPubIdVal = str.substring(adjacentTxt.length());
				    
                                    /*need to get the anatomy prefix and attach it to the id obtained previously
				      then query the database to get the mane of the component*/
                                    
                                    parQ = DBQuery.getParamQuery("COMPONENT_NAME_FROM_ATN_PUBLIC_ID");
                                    parQ.setPrepStat(conn);
                                    compNmeStmt = parQ.getPrepStat();
                                    compNmeStmt.setString(1, anatIdPrefix+atnPubIdVal);
                                    compNmeSet = compNmeStmt.executeQuery();
                                    if(compNmeSet.first()){
                                        locations.append(adjacentTxt+ compNmeSet.getString(1));
                                    }
                                }
                                else {
                                    locations.append(resSetLocations.getString(1));
                                }
				
                                if(!resSetLocations.isLast()){
                                    locations.append(", ");
                                }
                            }
                            if(locations != null){
                                pattern.setLocations(locations.toString());
                            }
                            patternList.add(pattern);
			    
                    	} else { // for editing
			    //                    		System.out.println("for editing########");
			    // get all related location info
                            ArrayList<String[]> locationList = new ArrayList<String[]>();
                            resSetLocations.beforeFirst();
                            while (resSetLocations.next()) {
                            	String[] locationInfo = new String[2];
                            	locationInfo[0] = resSetLocations.getString(1);
                            	locationInfo[1] = resSetLocations.getString(3);
				//                            	System.out.println("location value: " + loc);
                            	if (locationInfo[0] != null && locationInfo[0].length() > 0) {
				    locationList.add(locationInfo);
                            	}
                            }
                            int len = locationList.size();

                            if (len > 0) {
                                pattern.setLocationId(Integer.parseInt(locationList.get(0)[1]));
                                /////////////////////////////
				    // modified by xingjun - 16/06/2008 - need to split location value if
				    // it's in 'adjacent to xxxxx' format
				    String locationString = locationList.get(0)[0];
				    pattern.setLocations(locationString);
				    if(locationString.indexOf("adjacent to") >= 0) {
                                	pattern.setLocationAPart("adjacent to");
                                	str = Utility.netTrim(locationString.substring(11));
					if (null == str)
                                	pattern.setLocationNPart(null);
					else
                                	pattern.setLocationNPart(str);
				    } else {
                                	pattern.setLocationAPart(locationString);
                                	pattern.setLocationNPart("");
				    }
				    /////////////////////////////////
					patternList.add(pattern);
					
					if (len > 1) {
					    for (int i=1;i<len;i++) {
						pattern = new ExpressionPattern();
						pattern.setExpressionId(expId);
						pattern.setPatternId(patternId);
						pattern.setPattern(patternValue);
						pattern.setLocationId(Integer.parseInt(locationList.get(i)[1]));
						/////////////////////////////
						    // it's in 'adjacent to xxxxx' format
						    locationString = locationList.get(i)[0];
						    pattern.setLocations(locationString);
						    if(locationString.indexOf("adjacent to") >= 0) {
							pattern.setLocationAPart("adjacent to");
							str = Utility.netTrim(locationString.substring(11));
							if (null == str)
							    pattern.setLocationNPart(null);
							else
							    pattern.setLocationNPart(str);
						    } else {
							pattern.setLocationAPart(locationString);
							pattern.setLocationNPart("");
						    }
						    /////////////////////////////
							patternList.add(pattern);
					    }
					}
                            } else {
                            	patternList.add(pattern);
                            }
                    	}
                    } else { // location info is not available
                        patternList.add(pattern);
                    }
                } // end of pattern loop
            }
            conn.commit();
            conn.setAutoCommit(true);
	    
        } catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
            //close the statements
            DBHelper.closePreparedStatement(prepStmtPatterns);
            DBHelper.closePreparedStatement(prepStmtLocations);
            DBHelper.closePreparedStatement(compNmeStmt);
        }
	//        System.out.println("pattern number: " + patternList.size());
        if (patternList != null && patternList.size() > 0) {
            patterns = patternList.toArray(new ExpressionPattern[0]);
        }
        return patterns;
    }
    
    /**
     * @param componentId
     * @return
     */
    public ArrayList findComponentDetailById(String componentId) {
        ArrayList<Object> componentDetail = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("COMPONENT_DETAIL");
        PreparedStatement prepStmt = null;
		String [] components = null;
		ArrayList componentList = null;
		String str = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, componentId);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:findComponentDetailById = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                componentDetail = new ArrayList<Object>();
                componentDetail.add(resSet.getString(1));
                componentDetail.add(resSet.getString(2));
                str = Utility.netTrim(resSet.getString(3));
				if (null != str) {
		                components = str.split("\\.");
		                componentList = reformatComponentFullPath(components);
		                componentDetail.add(componentList);
				}
            }
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            return componentDetail;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     *
     * @param submissionAccessionId
     * @return
     */
    public String findStageBySubmissionId(String submissionAccessionId) {
        String stage = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_STAGE");
        PreparedStatement prepStmt = null;
        //		  System.out.println("submissionAccessionId: " + submissionAccessionId);
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:findStageBySubmissionId = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                stage = "TS" + resSet.getString(2);
                //				  System.out.println("stage: " + stage);
            }
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            return stage;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param submissionAccessionId
     * @param stageName
     * @param componentId
     */
    public boolean hasParentNode(String componentId, String stageName,
                                 String submissionAccessionId) {
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("PARENT_NODES");
        PreparedStatement prepStmt = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, componentId);
            prepStmt.setString(2, stageName);
            prepStmt.setString(3, submissionAccessionId);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:hasParentNode = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                // close the db object
                DBHelper.closePreparedStatement(prepStmt);
                return true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }
    
    /**
     * @param submissionAccessionId
     * @param stageName
     * @param componentId
     */
    public boolean hasChildenNode(String componentId, String stageName,
                                  String submissionAccessionId) {
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("CHILD_NODES");
        PreparedStatement prepStmt = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, componentId);
            prepStmt.setString(2, stageName);
            prepStmt.setString(3, submissionAccessionId);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:hasChildenNode = "+prepStmt);
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
                // close the db object
                DBHelper.closePreparedStatement(prepStmt);
                return true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }
    
    /**
     * <p>xingjun - 11/12/2009 - return String type for the submission id</p>
     */
    public StatusNote[] getStatusNotesBySubmissionId(String accessionId) {
    	StatusNote[] statusNotes = null;
        ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_STATUS_NOTES_BY_ID");
        String query = parQ.getQuerySQL();
	//        System.out.println("status notes query: " + query);
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(query);
		    prepStmt.setInt(1, Integer.parseInt(accessionId.substring(7)));
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getStatusNotesBySubmissionId = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				ArrayList<StatusNote> sns = new ArrayList<StatusNote>();
				StatusNote statusNote = null;
				while (resSet.next()) {
				    statusNote = new StatusNote();
				    statusNote.setStatusNoteId(resSet.getInt(1));
				    statusNote.setSubmissionId(resSet.getString(2));
				    statusNote.setStatusNote(resSet.getString(3));
				    statusNote.setSelected(false);
				    sns.add(statusNote);
				}
				statusNotes = sns.toArray(new StatusNote[0]);
		    }
	    
            // close the db object
		    DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
    	return statusNotes;
    }
    
    /**
     * @author xingjun - 13/06/2008
     */
    public ArrayList getPatternList() {
    	ArrayList<String> patternList = null;
        ParamQuery parQ = DBQuery.getParamQuery("PATTERN_LIST");
        String query = parQ.getQuerySQL();
	//        System.out.println("pattern list query: " + query);
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        String str = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(query);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getPatternList = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				patternList = new ArrayList<String>();
				while (resSet.next()) {
				    str = Utility.netTrim(resSet.getString(1));
				    if (null != str)
				    	patternList.add(str);
				}
		    }
	    
            // close the db object
		    DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
	    
        } catch (SQLException se) {
	    se.printStackTrace();
        }
    	return patternList;
    }
    
    /**
     * @author xingjun - 13/06/2008
     */
    public ArrayList getLocationList() {
	ArrayList<String> locationList = null;
        ParamQuery parQ = DBQuery.getParamQuery("LOCATION_LIST");
        String query = parQ.getQuerySQL();

        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        String str = null;
        try {
	    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(query);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getLocationList = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				resSet.beforeFirst();
				locationList = new ArrayList<String>();
				while (resSet.next()) {
				    str = Utility.netTrim(resSet.getString(1));
				    if (null != str)
				    	locationList.add(str);
				}
		    }
	    
            // close the db object
		    DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
    	return locationList;
    }
    
    /**
     * @author xingjun -17/06/2008
     * 
     */
    public ArrayList getComponentListAtGivenStage(String stage) {
    	ArrayList<String[]> componentList = null;
        ParamQuery parQ = DBQuery.getParamQuery("COMPONENT_LIST_AT_GIVEN_STAGE");
        String query = parQ.getQuerySQL();
	//        System.out.println("component list query: " + query);
	//        System.out.println("stage: " + stage);
	
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(query);
		    prepStmt.setString(1, stage);
		    if (debug)
		    	System.out.println("MySQLISHDAOImp:getComponentListAtGivenStage = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    String str = null;
		    String[] component = null;
		    if (resSet.first()) {
				resSet.beforeFirst();
				componentList = new ArrayList<String[]>();
				while (resSet.next()) {
				    component = new String[2];
				    str = Utility.netTrim(resSet.getString(1));
				    component[0] = str;
				    str = Utility.netTrim(resSet.getString(2));
				    component[1] = str;
				    if (null != component[0] || str != component[1])
					componentList.add(component);
				}
		    }
            // close the db object
	    	DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
	//        System.out.println("component list size: " + componentList.size());
    	return componentList;
    }
    
    /**
     * @author xingjun - 28/07/2008
     * get locking info by submission id
     * @param accessionId
     * @return
     */
    public LockingInfo getLockingInfo(String accessionId) {
		LockingInfo lockingInfo = null;
		ParamQuery parQ = DBQuery.getParamQuery("GET_LOCKING_INFO_BY_SUBMISSION_ID");
		String queryString = parQ.getQuerySQL();
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
		try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
		    prepStmt = conn.prepareStatement(queryString);
		    prepStmt.setString(1, accessionId);
		    if (debug)
				System.out.println("MySQLISHDAOImp:getLockingInfo = "+prepStmt);
		    resSet = prepStmt.executeQuery();
		    if (resSet.first()) {
				lockingInfo = new LockingInfo();
				lockingInfo.setsubmissionId(resSet.getString(1));
				lockingInfo.setLockedBy(resSet.getString(2));
				lockingInfo.setLockTime(resSet.getInt(3));
		    }
		    // release db resources
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(prepStmt);
		} catch(SQLException se) {
		    se.printStackTrace();
		}
		// return
		return lockingInfo;
    }
    
    /**
     * @author xingjun - 28/07/2009
     * @return
     */
    public String getApplicationVersion() {
        String result = "";
        ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("APPLICATION_VERSION");
        PreparedStatement prepStmt = null;

        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
		    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:getApplicationVersion = "+prepStmt);
    	    
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
            	result = resSet.getString(1);
            }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
	//        System.out.println("application version: " + result);
        return result;
	
    }
    
    // added by Bernie - 23/09/2010
    public String findTissueBySubmissionId(String submissionId)
    {
		//		System.out.println("MySQLISHDAOImp-findTissueBySubmissionId for " + submissionId);
		String tissue = null;
        ResultSet resultSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GET_SUBMISSION_TISSUE");
        PreparedStatement prepStmt = null;        
        try {
		    // if disconnected from db, re-connected
		    conn = DBHelper.reconnect2DB(conn);
	    
            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionId);
    	    if (debug)
    			System.out.println("MySQLISHDAOImp:findTissueBySubmissionId = "+prepStmt);
    	    
            resultSet = prepStmt.executeQuery();
            
            if (resultSet.first()) 
                tissue = resultSet.getString(1);
            
	        if (debug) 	    
            	System.out.println("tissue: " + tissue);

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            return tissue;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
}
