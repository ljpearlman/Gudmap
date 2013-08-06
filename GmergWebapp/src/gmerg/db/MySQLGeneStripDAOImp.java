/**
 * 
 */
package gmerg.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gmerg.db.DBHelper;
import gmerg.entities.submission.ImageInfo;
import gmerg.entities.submission.Person;
import gmerg.entities.ChromeDetail;

/**
 * @author xingjun
 */
public class MySQLGeneStripDAOImp implements GeneStripDAO {
    protected boolean debug = false;
    private Connection conn;
    private ResourceBundle bundle = ResourceBundle.getBundle("configuration");
    
    // default constructor
    public MySQLGeneStripDAOImp() {
	
    }
    
    // constructor with connection initialisation
    public MySQLGeneStripDAOImp(Connection conn) {
	this.conn = conn;
    }
    
    public String findSynonymsBySymbol(String symbol) {
	if (symbol == null || symbol.equals("")) {
	    return "";
	}
        String synonyms = new String("");
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GET_GENE_SYNONYM_BY_SYMBOL");
        String queryString = parQ.getQuerySQL();
	if (debug)
	    System.out.println("query for synonyms: " + queryString);
        PreparedStatement prepStmt = null;
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    prepStmt.setString(1, symbol);
	    resSet = prepStmt.executeQuery();
	    if (resSet.first()) {
		resSet.beforeFirst();
		while (resSet.next()) {
		    synonyms += resSet.getString(1) + ", ";
		}
		synonyms = synonyms.substring(0, synonyms.lastIndexOf(","));
	    }
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
	return synonyms;
    }
    
    /**
     * @author xingjun - 27/01/2009
     * @param symbol: gene symbol
     * @param assayType: insitu or microarray
     * @return
     */
    public String[] getGeneStages(String symbol, String assayType) {
	
	if (symbol == null || symbol.equals("")) {
	    return null;
	}
	String[] stages = null;
	ResultSet resSet = null;
	ParamQuery parQ = null;
	if (assayType.equals("Microarray")) {
	    parQ = ArrayDBQuery.getParamQuery("GENE_THEILER_STAGES_ARRAY");
	} else if (assayType.equals("insitu")) {
	    parQ = InsituDBQuery.getParamQuery("GENE_THEILER_STAGES_INSITU");
	}
	String queryString = parQ.getQuerySQL();
	if (debug)
	    System.out.println("getGeneStagesQueryString: " + queryString);
	PreparedStatement prepStmt = null;
	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    prepStmt.setString(1, symbol);
	    resSet = prepStmt.executeQuery();
	    stages = DBHelper.formatResultSetToStringArray(resSet);
	    
	    // close the db object
	    DBHelper.closePreparedStatement(prepStmt);
	    DBHelper.closeResultSet(resSet);
	} catch (SQLException se) {
	    se.printStackTrace();
	}
	return stages;
    }
    
    /**
     * @author xingjun - 19/11/2008
     * 
     * @param componentIds - emap ids for given structure
     * @param expressionForGivenComponents 
     * - flag to specify find expression linked to given strucutre
     *   or expression linked to non-given structures
     */
    public ArrayList getGeneExpressionForStructure(String symbol, String[] componentIds, 
						   boolean expressionForGivenComponents) {
	
	if (symbol == null || symbol.equals("")) {
	    return null;
	}
	
	if (debug) {
	    System.out.println("symbol: " + symbol);
	    System.out.println("component id number in dao: " + componentIds.length);
	}
	
	ResultSet resSet = null;
        ArrayList expressions = null;
        ParamQuery parQ = null;
        PreparedStatement prepStmt = null;
        String componentClause = null;
        String componentString = " (";
        if (expressionForGivenComponents) {
            parQ = AdvancedSearchDBQuery.getParamQuery("GENE_EXPRESSION_FOR_GIVEN_STRUCTURE");
            componentClause = "AND EXP_COMPONENT_ID IN (";
        } else {
	    parQ = AdvancedSearchDBQuery.getParamQuery("GENE_EXPRESSION_FOR_NONGIVEN_STRUCTURE");
	    componentClause = "AND EXP_COMPONENT_ID NOT IN (";
        }
        // assemble parent component ids string
        int len = componentIds.length;
        for (int i=0;i<len;i++) {
	    componentString += "'" + componentIds[i] + "', ";
        }
        componentString = componentString.substring(0, (componentString.length()-2)) + ")";
	if (debug)
	    System.out.println("componentString: " + componentString);
	
        // assemble full component ids string including child nodes as well as parent component ids
        ParamQuery parQChildNodes = AdvancedSearchDBQuery.getParamQuery("FIND_CHILD_NODE");
        componentClause += parQChildNodes.getQuerySQL().replaceAll("WHERE ANCES_ATN.ATN_PUBLIC_ID IN", 
								   ("WHERE ANCES_ATN.ATN_PUBLIC_ID IN "+ componentString)) + ")";
	if (debug)
	    System.out.println("componentClause: " + componentClause);
	
        // use different query string according to what expressions we are looking for
        String queryString = null;
        if (expressionForGivenComponents) {
	    queryString = 
		parQ.getQuerySQL().replace("AND EXP_COMPONENT_ID IN", componentClause);
        } else {
	    queryString = 
		parQ.getQuerySQL().replace("AND EXP_COMPONENT_ID NOT IN", componentClause);
        }
	if (debug)
	    System.out.println("getGeneExpressionForStructure:queryString: " + queryString);
	
        // get the expression
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    prepStmt.setString(1, symbol);
	    resSet = prepStmt.executeQuery();
	    expressions = DBHelper.formatResultSetToArrayList(resSet);
	    //        	if (expressions == null) System.out.println("no expression info for this structure!");
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
	return expressions;
    }
    
    /**
     * @author xingjun - 17/02/2009
     * @param imageIds
     * @return
     */
    public ArrayList getInsituSubmissionImagesByImageId(ArrayList<String> imageIds) {
	
	if (imageIds == null || imageIds.size() == 0) {
	    return null;
	}
        ArrayList images = null;
	ResultSet resSet = null;
        ParamQuery parQ = 
	    InsituDBQuery.getParamQuery("INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID");
        String queryString = parQ.getQuerySQL();
	
        PreparedStatement prepStmt = null;
        int len = imageIds.size();
	if (debug)
	    for (int i =0;i<len;i++) {
		System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId:imageId: " + imageIds.get(i).toString());
	    }
        String whereClause = "FILENAME) IN";
	String imageIdWhereClause = "FILENAME) IN ('" + imageIds.get(0).toString() + "'";
	
	for (int i=1;i<len;i++) {
	    imageIdWhereClause += ", '" + imageIds.get(i).toString()+"'";
	}
	imageIdWhereClause += ") ";
	
        queryString = queryString.replace(whereClause, imageIdWhereClause);
	if (debug)
	    System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId@insituSubImageQuery:updated: " + queryString);
        try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    resSet = prepStmt.executeQuery();
	    images = formatInsituSubmissionImageResultSet(resSet);
	    if (debug) {
		if (null == images)
		    System.out.println("!!!possible error: no image");
		else
		    System.out.println("image number: " + images.size());
	    }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
	return images;
    }
    
    /**
     * @author xingjun - 12/02/2009
     * @param resSet
     * @return an array list of image detail objects
     * @throws SQLException
     */
    private ArrayList formatInsituSubmissionImageResultSet(ResultSet resSet) throws SQLException {
	if (resSet.first()) {
	    resSet.beforeFirst();
	    ArrayList result = new ArrayList();
	    // get the submission id for the first record
	    String tempSubmissionId = null;
	    int serialNo = 0;
	    String submissionId = null;
	    ImageInfo img = null;
	    while (resSet.next()) {
		submissionId = null;
		img = new ImageInfo();

		    submissionId = resSet.getString(1);
		    img.setAccessionId(submissionId);
		    img.setStage("TS"+resSet.getString(2));
		    img.setSpecimenType(resSet.getString(3));
		    img.setFilePath(resSet.getString(4));
		    img.setClickFilePath(resSet.getString(5));


		if (tempSubmissionId == null || 
		    !submissionId.equals(tempSubmissionId)) { // its first record or a new submission
		    tempSubmissionId = submissionId;
		    serialNo = 1;
		    img.setSerialNo(""+serialNo);
		} else {
		    serialNo++;
		    img.setSerialNo(Integer.toString(serialNo));
		} 
		// put the image detail object into the result
		result.add(img);
	    }
	    // return result
	    return result;
	}
	return null;
    } // end of formatInsituSubmissionImageResultSet
    
    /**
     * @author xingjun - 29/01/2009
     */
    public int getGeneDiseaseNumber(String symbol) {
	if (symbol == null || symbol.equals("")) {
	    return 0;
	}

	int diseaseNumber = 0;
	ResultSet resSet = null;

	ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("TOTOAL_NUMBER_OF_DISEASE_FOR_GENE");
	String queryString = parQ.getQuerySQL();
	PreparedStatement prepStmt = null;

	if (debug)
	    System.out.println("geneDiseaseNumberQuery: " + queryString);
	
               try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    prepStmt = conn.prepareStatement(queryString);
	    prepStmt.setString(1, symbol);
	    resSet = prepStmt.executeQuery();
	    if (resSet.first()) {
		diseaseNumber = resSet.getInt(1);
	    }
	    
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
	    se.printStackTrace();
        }
	return diseaseNumber;
    }
    
    /**
     * @author xingjun - 21/07/2011
     */
    public ChromeDetail getChromeDetailBySymbol(String symbol) {
	if (symbol == null) {
	    return null;
	}
	ChromeDetail chromeDetail = null;
	ResultSet resSet = null;
	ParamQuery parQ = InsituDBQuery.getParamQuery("CHROME_DETAIL_BY_SYMBOL");
	PreparedStatement prepStmt = null;
	try {
	    // if disconnected from db, re-connected
	    conn = DBHelper.reconnect2DB(conn);
	    
	    parQ.setPrepStat(conn);
	    prepStmt = parQ.getPrepStat();
	    prepStmt.setString(1, symbol);
	    
	    // execute
	    resSet = prepStmt.executeQuery();
	    chromeDetail = formatChromeDetailResultSet(resSet);
	    
	    // close the connection
	    DBHelper.closePreparedStatement(prepStmt);
	} catch (SQLException se) {
	    se.printStackTrace();
	}
	return chromeDetail;
    }
    
    /**
     * @author xingjun - 21/07/2011
     */
    private ChromeDetail formatChromeDetailResultSet(ResultSet resSet) throws SQLException {
    	ChromeDetail chromeDetail = null;
	if (resSet.first()) {
	    chromeDetail = new ChromeDetail();
	    chromeDetail.setChromeName(resSet.getString(2));
	    chromeDetail.setChromeStart(resSet.getString(3));
	    chromeDetail.setChromeEnd(resSet.getString(4));
    	}
    	return chromeDetail;
    }
    
}
