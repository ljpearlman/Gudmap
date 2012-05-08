/**
 * 
 */
package gmerg.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gmerg.db.DBHelper;
import gmerg.entities.submission.ImageDetail;
import gmerg.entities.submission.Person;
import gmerg.entities.ChromeDetail;

/**
 * @author xingjun
 * xingjun - 06/06/2011 - replace string 'GUDMAP' with projectString (could be GUDMAP or EuReGene) 
 *
 */
public class MySQLGeneStripDAOImp implements GeneStripDAO {
    private Connection conn;
	private ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	
	// default constructor
	public MySQLGeneStripDAOImp() {
		
	}
	
    // constructor with connection initialisation
	public MySQLGeneStripDAOImp(Connection conn) {
		this.conn = conn;
	}

	/* (non-Javadoc)
	 * @see gmerg.db.GeneStripDAO#findSynonymsBySymbol(java.lang.String)
	 */
	public String findSynonymsBySymbol(String symbol) {
		if (symbol == null || symbol.equals("")) {
			return "";
		}
        String synonyms = new String("");
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GET_GENE_SYNONYM_BY_SYMBOL");
        String queryString = parQ.getQuerySQL();
//        System.out.println("query for synonyms: " + queryString);
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
	 * @author xingjun - 06/11/2008
	 */
	public String[] getInsituSubmissionNumberAndStageInfoByGeneSymbol(String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
		String[] insituSubmissionNumberAndStageInfo = null;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GENE_RELATED_SUBMISSIONS_ISH");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, symbol);
        	resSet = prepStmt.executeQuery();
        	if (resSet.first()) {
        		insituSubmissionNumberAndStageInfo = new String[2];
        		resSet.beforeFirst();
        		int counter = 0;
        		ArrayList<String> stageList = new ArrayList<String>();
        		String stageString = "";
        		while (resSet.next()) {
        			String stage = resSet.getString(3);
        			if (!stageList.contains(stage)) {
            			stageList.add(stage);
        			}
        			counter++;
        		}
        		stageString += stageList.get(0) + "-" + stageList.get(stageList.size()-1);
        		insituSubmissionNumberAndStageInfo[0] = stageString;
        		insituSubmissionNumberAndStageInfo[1] = Integer.toString(counter);
        	}
        	
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
        return insituSubmissionNumberAndStageInfo;
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
//        System.out.println("getGeneStagesQueryString: " + queryString);
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
	 * modified by xingjun - 16/01/2009 - need to go through all the daughter components 
	 * when looking for the annotation - not just those in the list
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
//		System.out.println("symbol: " + symbol);
//		System.out.println("component id number in dao: " + componentIds.length);
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
//        System.out.println("componentString: " + componentString);
        
        // assemble full component ids string including child nodes as well as parent component ids
        ParamQuery parQChildNodes = AdvancedSearchDBQuery.getParamQuery("FIND_CHILD_NODE");
        componentClause += parQChildNodes.getQuerySQL().replaceAll("WHERE ANCES_ATN.ATN_PUBLIC_ID IN", 
        			("WHERE ANCES_ATN.ATN_PUBLIC_ID IN "+ componentString)) + ")";
//        System.out.println("componentClause: " + componentClause);
        
        // use different query string according to what expressions we are looking for
        String queryString = null;
        if (expressionForGivenComponents) {
        	queryString = 
        		parQ.getQuerySQL().replace("AND EXP_COMPONENT_ID IN", componentClause);
        } else {
        	queryString = 
        		parQ.getQuerySQL().replace("AND EXP_COMPONENT_ID NOT IN", componentClause);
        }
//        System.out.println("getGeneExpressionForStructure:queryString: " + queryString);
        
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
	 * @author xingjun - 27/26/2008
	 * @return
	 */
	public ArrayList getInsituSubmissionImagesByGene(String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
        ArrayList images = null;
		ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GENE_RELATED_INSITU_SUBMISSION_IMAGES");
        String queryString = parQ.getQuerySQL();
//        System.out.println("GeneStripDAO:getInsituSubmissionImagesByGene@symbol: " + symbol);
//        System.out.println("GeneStripDAO:getInsituSubmissionImagesByGene@insituSubImageQuery: " + queryString);
        PreparedStatement prepStmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, symbol);
        	resSet = prepStmt.executeQuery();
        	images = this.formatInsituSubmissionImageResultSet(resSet);
//        	System.out.println("image number: " + images.size());
        	
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return images;
	}
	
	/**
	 * @author xingjun - 17/02/2009
	 * @param imageIds
	 * <p>xingjun - 06/06/2011 - changed the code to make it work for EuReGene app as well</p>
	 * @return
	 */
	public ArrayList<ImageDetail> getInsituSubmissionImagesByImageId(ArrayList<String> imageIds) {
		if (imageIds == null || imageIds.size() == 0) {
			return null;
		}
        ArrayList<ImageDetail> images = null;
		ResultSet resSet = null;
        ParamQuery parQ = 
        	InsituDBQuery.getParamQuery("INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID");
        String queryString = parQ.getQuerySQL();
//        System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId@insituSubImageQuery:initial: " + queryString);
        PreparedStatement prepStmt = null;
//        String imageIdString = null;
        int len = imageIds.size();
//		for (int i =0;i<len;i++) {
//			System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId:imageId: " + imageIds.get(i).toString());
//		}
        String whereClause = "WHERE CONCAT(SUB_ACCESSION_ID, '_', IMG_FILENAME) ";
        String projectString = bundle.getString("project").trim();
        String accessionPrefix = projectString.equalsIgnoreCase("GUDMAP")?"GUDMAP:":"ERG:";
        String imageIdWhereClause = null;
        if (projectString.equalsIgnoreCase("GUDMAP")) {
        	imageIdWhereClause = "WHERE CONCAT(SUB_ACCESSION_ID, '_', IMG_FILENAME) ";
        } else if (projectString.equalsIgnoreCase("EuReGene")) {
        	imageIdWhereClause = "WHERE CONCAT('ERG:', SUB_OID, '_', IMG_FILENAME) ";
        }
        if (len == 1) {
//        	imageIdString = whereClause + "= 'GUDMAP:" + imageIds.get(0).toString() + "' ";
        	imageIdWhereClause += "= '" + accessionPrefix + imageIds.get(0).toString() + "' ";
        } else {
//        	imageIdString = whereClause + "IN (";
        	imageIdWhereClause += " IN (";
        	for (int i=0;i<len;i++) {
//        		imageIdString += "'GUDMAP:" + imageIds.get(i).toString() + "', ";
        		imageIdWhereClause += "'" + accessionPrefix + imageIds.get(i).toString() + "', ";
        	}
//        	imageIdString = imageIdString.substring(0, (imageIdString.length()-2)) + ")";
        	imageIdWhereClause = imageIdWhereClause.substring(0, (imageIdWhereClause.length()-2)) + ")";
        }
//        System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId@imageIdString: " + imageIdString);
//      System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId@imageIdWhereClause: " + imageIdWhereClause);
//        queryString = queryString.replace(whereClause, imageIdString);
        queryString = queryString.replace(whereClause, imageIdWhereClause);
//        System.out.println("GeneStripDAO:getInsituSubmissionImagesByImageId@insituSubImageQuery:updated: " + queryString);
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

        	prepStmt = conn.prepareStatement(queryString);
        	resSet = prepStmt.executeQuery();
        	images = this.formatInsituSubmissionImageResultSet(resSet);
//        	System.out.println("image number: " + images.size());
        	
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
	private ArrayList<ImageDetail> formatInsituSubmissionImageResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			resSet.beforeFirst();
			ArrayList<ImageDetail> result = new ArrayList<ImageDetail>();
			// get the submission id for the first record
			String tempSubmissionId = null;
			int serialNo = 0;
			while (resSet.next()) {
				String submissionId = null;
				ImageDetail imageDetail = new ImageDetail();
				if (tempSubmissionId == null) { // its first record
					tempSubmissionId = resSet.getString(1);
					submissionId = tempSubmissionId;
					// put all data into the image detail object
					imageDetail.setAccessionId(submissionId);
//					imageDetail.setStage("TS" + resSet.getString(2));
					imageDetail.setStage(resSet.getString(2));
					imageDetail.setAssayType(resSet.getString(3));
					imageDetail.setFilePath(resSet.getString(4));
					imageDetail.setImageName(resSet.getString(5));
					imageDetail.setPyramidTileSize(resSet.getString(6));
					serialNo++;
					imageDetail.setSerialNo(Integer.toString(serialNo));
					// last column need to put in
				} else {
					submissionId = resSet.getString(1);
					imageDetail.setAccessionId(submissionId);
//					imageDetail.setStage("TS" + resSet.getString(2));
					imageDetail.setStage(resSet.getString(2));
					imageDetail.setAssayType(resSet.getString(3));
					imageDetail.setFilePath(resSet.getString(4));
					imageDetail.setImageName(resSet.getString(5));
					imageDetail.setPyramidTileSize(resSet.getString(6));
					
					if (submissionId.equals(tempSubmissionId)) { // following images of the same submission
						serialNo++;
						imageDetail.setSerialNo(Integer.toString(serialNo));
					} else { // images from a new submission
						serialNo = 1; // reset serial no
						imageDetail.setSerialNo(Integer.toString(serialNo));
						tempSubmissionId = submissionId;
					}
				}
				// put the image detail object into the result
				result.add(imageDetail);
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
        ParamQuery parQ = 
        	AdvancedSearchDBQuery.getParamQuery("TOTOAL_NUMBER_OF_DISEASE_FOR_GENE");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
//        System.out.println("geneDiseaseNumberQuery: " + queryString);
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
