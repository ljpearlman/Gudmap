package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ArrayList;

import gmerg.assemblers.AllComponentsGenelistAssembler;
import gmerg.entities.BrowseTableTitle;
import gmerg.entities.GenelistInfo;
import gmerg.utils.Utility;

/**
 * @author xingjun
 *
 */
public class MySQLGenelistDAOImp implements GenelistDAO {
    private boolean debug = false;

	private Connection conn;
	

	// default constructor
	public MySQLGenelistDAOImp() {
		
	}
	
	// constructor with connection initialisation
	public MySQLGenelistDAOImp(Connection conn) {
		this.conn = conn;
	}
	
	public String[] getAnalysisTitles(int ref) {
		final String[][] expressionTitles = {
				//----------------------223 all--------------------------------
				{"Urothelium", "centr_UretBud", "Ureteral Smooth Muscle", "medull stroma", "Cortical Stromal Cells",	
			 	 "E11.5 MetMes", "S-shaped body", "renal vesicles",	"ren corpusc stage3-4 glomer", "distTub-LoopHenle", "prox tubules", 
			 	 "E11.5_UretBud", "cort collct duct", "Ureteric tip with flanking...", "cap mesnchy" },
			 	 //----------------------102 SS---------------------------------
				{"Cortical Stromal Cells",	"E11.5 MetMes",	"E11.5_UretBud",	"S-shaped body",	"Ureteral Smooth Muscle",	
			 	"Ureteric tip with flanking cortical collecting duc",	"Urothelium",	"cap mesnchy",	"centr_UretBud",	"cort collct duct",	
			 	"distTub-LoopHenle",	"medull stroma",	"prox tubules",	"ren corpusc stage3-4 glomer",	"renal vesicles" },
			 	//----------------------110 RV---------------------------------
			 	{"Cortical Stromal Cells",	"E11.5 MetMes",	"E11.5_UretBud",	"S-shaped body",	"Ureteral Smooth Muscle",
			 	"Ureteric tip with flanking cortical collecting duc",	"Urothelium",	"cap mesnchy",	"centr_UretBud",	"cort collct duct",
			 	"distTub-LoopHenle",	"medull stroma",	"prox tubules",	"ren corpusc stage3-4 glomer",	"renal vesicles" }
		};

		return expressionTitles[ref];
	}


//********************************************************************************************************	
	private class MyArrayComparator implements Comparator<Object> {
		int col;
		boolean ascending;

		public MyArrayComparator(int col) {
			this.col = col;
			ascending = true;
		}  
		public MyArrayComparator(int col, boolean ascending)	{
			this.col = col;
			this.ascending = ascending;
		}  
		public int compare(Object o1, Object o2) {
			Object[] r1 = (Object[])o1;
			Object[] r2 = (Object[])o2;
			String s = "";
			int result = 0;
			if (s.getClass() == r1[col].getClass() ) 
				result = ((String)r1[col]).compareTo((String)r2[col]);
			else
				result = ((Double)r1[col] > (Double)r2[col])? 1 : ((r1==r2)?0:-1);
			if (!ascending)
				result = -result;
			return result;
		}
	}	
	
	public double[][] getAllAnalysisData(int ref) {
	    return new double[0][0];
	}
	
	
	public String[] getAllIds(int ref) {
	    return new String[0];
	}
	
	public String[][] getAllOntologies(int ref) {
	    return new String[0][0];

	}
	
	/**
	 * @author xingjun - 02/12/2008
	 * <p>xingjun - 11/08/2009 - added master table id parameter</p>
	 * 
	 * <p>xingjun - 04/02/2010 
	 * - added extra criteria (section)
	 * - return median value along with the expression
	 * </p>
	 */
	public BrowseTableTitle[] getMasterTableExpressionTitles(String masterTableId) {
		BrowseTableTitle[] expressionTitles = null;
        ResultSet resSet = null;
        ParamQuery parQ = 
        	ArrayDBQuery.getParamQuery("MASTER_TABLE_EXPRESSION_TITLES");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	String masterTableAndSectionId[] = Utility.parseMasterTableId(masterTableId);
        	
//        	prepStmt.setInt(1, Integer.parseInt(masterTableId));
			prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
			prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
        	
        	resSet = prepStmt.executeQuery();
        	expressionTitles = 
        		this.formatBrowseTableTitleResultSet(resSet);
        	
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		// return value
        return expressionTitles;
	}

	/**
	 * @author xingjun - 03/12/2008
	 * <p>modified by xingjun - 22/12/2008 - renamed variable from 'value' to 'titleValue'</p>
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private BrowseTableTitle[] formatBrowseTableTitleResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
    		ArrayList<BrowseTableTitle> titles = 
    			new ArrayList<BrowseTableTitle>();
    		resSet.beforeFirst();
    		while (resSet.next()) {
    			BrowseTableTitle title = new BrowseTableTitle();
    			String titleValue = resSet.getString(1);
    			String type = resSet.getString(2);
    			String desc = resSet.getString(3);
    			String group = resSet.getString(4);

    			// set "" for empty properties
    			title.setTitle((titleValue==null||titleValue.length()==0)?"":titleValue);
    			title.setType(type==null?"":type);
    			title.setDescription(desc==null?"":desc);
    			title.setGroupName(group==null?"":group);
    			titles.add(title);
    		}
        	return titles.toArray(new BrowseTableTitle[titles.size()]);
		}
		return null;
	}
	
	/**
	 * @author xingjun - 09/12/2008
	 * <p>overload of getAnnotationByProbeSetIds</p>
	 * @param probeSetIds
	 * @return
	 */
	public String[][] getAnnotationByProbeSetIds(ArrayList probeSetIds) {
		String[][] annotations = null;
        ResultSet resSet = null;
        ParamQuery parQ = 
        	ArrayDBQuery.getParamQuery("MIC_ANALYSIS_ANNOTATION");
        String querySQL = parQ.getQuerySQL();
//        System.out.println("MTAnnotation query before add criteria: " + querySQL);

        String queryString = this.assembleAnnotationQueryString(probeSetIds, querySQL);
        // System.out.println("MTAnnotation query string (full): " + queryString);
        PreparedStatement prepStmt = null;
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	resSet = prepStmt.executeQuery();
        	annotations = this.formatAnnotationResultSet(resSet);
        	
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		// return value
		return annotations;
	}
	
	/**
	 * @author xingjun - 04/12/2008
	 */
	private String assembleAnnotationQueryString(ArrayList probeSetIds, String querySQL,
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
        	probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2);
        	probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
//        	System.out.println("MTAnnotation probeset string: " + queryString);
        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", probeSetIdString) + ") ";
        	// xingjun - 25/02/2010
        	queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", "");
        	// xingjun - 25/02/2010
        	queryString = queryString.replaceAll("ORDER BY FIELD(MAN_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }
        
		// append row number restriction clause
		if (offset != 0) {
			queryString += "LIMIT " + Integer.toString(offset-1) + ", " + Integer.toString(num);
		}
        
		// return value
		return queryString;
	}
	
	/**
	 * @author xingjun - 09/12/2008
	 * <p>overload of assembleAnnotationQueryString</p>
	 * <p>xingjun - 25/02/2010 - since relevant sql modified to preserve the 
	 * order of the result as the order in WHERE IN() clause, the java code need to change accordingly  </p>
	 */
	private String assembleAnnotationQueryString(ArrayList probeSetIds, String querySQL) {
		String queryString = null;

		// add probe set id criteria
        if (probeSetIds != null && probeSetIds.size() != 0) {
//        	String probeSetIdString = "WHERE PRS_PROBE_SET_ID IN (";
        	String probeSetIdString = "WHERE MAN_PROBE_SET_ID IN (";
        	// xingjun - 25/02/2010
        	String probeSetIdList = "";
        	int len = probeSetIds.size();
        	for (int i=0;i<len;i++) {
        		probeSetIdString += "'" + probeSetIds.get(i).toString() + "', ";
            	// xingjun - 25/02/2010
        		probeSetIdList += "'" + probeSetIds.get(i).toString() + "', ";
        	}
        	probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2) + ") ";
        	// xingjun - 25/02/2010
        	probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
//        	System.out.println("MTAnnotation probeset string: " + probeSetIdString);
//        	System.out.println("MTAnnotation query string raw: " + querySQL);
//        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", probeSetIdString) + ") ";
        	queryString = querySQL.replace("WHERE MAN_PROBE_SET_ID", probeSetIdString);
        	// xingjun - 25/02/2010
//        	System.out.println("MTAnnotation probeset string after replacement of where: " + queryString);
        	queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
//        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", "");
        	queryString = querySQL.replace("WHERE MAN_PROBE_SET_ID", "");
        	// xingjun - 25/02/2010
        	queryString = queryString.replaceAll("ORDER BY FIELD(MAN_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }
        
		// return value
		return queryString;
	}
	
	/**
	 * @author xingjun - 04/12/2008
	 * <p>modified by xingjun 
	 * - now have to make it be able to perform in the situation with multiple platforms</p>
	 * <p>modified by xingjun
	 * - see ##### in the code</p>
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private String[][] formatAnnotationResultSet(ResultSet resSet) throws SQLException {
		ResultSetMetaData resSetData = resSet.getMetaData();
		int columnCount = resSetData.getColumnCount();
		
		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<String[]>();
			while (resSet.next()) {
				// the first column (platform id) will not be included
				String platformId = resSet.getString(1);
//				System.out.println("formatAnnotationResultSet:platformId: " + platformId);
				int columnNumber = ArrayDBQuery.ANNOTATION_COLUMN_NUMBER;
				String[] columns = new String[columnNumber];
				
				for (int i = 0; i < columnNumber; i++) {
					columns[i] = resSet.getString(i + 2);
				}
				
				results.add(columns);
			}
			// convert to 2-dimension array
			int len = results.size();
			String[][] annotations = new String[len][columnCount];
			for (int i=0;i<len;i++) {
				annotations[i] = results.get(i);
			}
			return annotations;
		}
		return null;
	}
	
	/**
	 * @author xingjun - 19/03/2009
	 */
	public ArrayList<GenelistInfo> getAllAnalysisGeneLists() {
		ArrayList<GenelistInfo> genelists = null;
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_ALL_ANALYSIS_GENELISTS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	resSet = prepStmt.executeQuery();
        	genelists = this.formatAllAnalysisGeneListsResultSet(resSet);
//        	ArrayList<String[]> genelist = DBHelper.formatResultSetToArrayList(resSet);
//        	result = (String[][])genelist.toArray(new Object[genelist.size()]);
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return genelists;
	}
	
	/**
	 * @author xingjun - 19/03/2009
	 * <p>xingjun 24/03/2009 - modified param in setFilename statement</p>
	 * <p>xingjun 26/03/2010 - added setClustered</p>
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<GenelistInfo> formatAllAnalysisGeneListsResultSet(ResultSet resSet) throws SQLException {
    	if (resSet.first()) {
    		resSet.beforeFirst();
    		ArrayList<GenelistInfo> result = new ArrayList<GenelistInfo>();
    		while (resSet.next()) {
    			GenelistInfo genelist = new GenelistInfo();
    			genelist.setGenelistId(resSet.getString(1));
    			genelist.setTitle(resSet.getString(2));
    			genelist.setSubmitter(resSet.getString(3));
    			genelist.setSummary(resSet.getString(4));
    			genelist.setFilename(resSet.getString(5)+resSet.getString(6)+resSet.getString(7));
    			genelist.setCdtFileName(resSet.getString(8));
    			genelist.setClustered(resSet.getInt(11)==1?true:false);
    			genelist.setPlatformId(resSet.getString(13));
    			result.add(genelist);
//    			System.out.println("GenelistDAO:formatAllAnalysisGeneListsResultSet:isClustered: " + Boolean.toString(genelist.isClustered()));
    		}
            return result;
    	}
		return null;
	}
	
	/**
	 * @author xingjun - 24/08/2009
	 * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
	 * the genelist id and cluster id<p> 
	 */
	public String getAnalysisGenelistPlatformId(String genelistId) {
		if (genelistId == null || genelistId.equals("")) {
			return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
		
		String platformId = null;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (clstId == null) { // only genelist id passed in
            parQ = ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST_PLATFORM");
        } else { // both ids passed in
            parQ = ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST_PLATFORM_BY_CLUSTER_ID");
        }
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	if (clstId == null) {
            	prepStmt.setString(1, glstId);
        	} else {
            	prepStmt.setInt(1, Integer.parseInt(glstId));
            	prepStmt.setInt(2, Integer.parseInt(clstId));
        	}
        	resSet = prepStmt.executeQuery();
        	if (resSet.first()) {
        		platformId = resSet.getString(1);
        	}
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return platformId;
	}
	
	/**
	 * @author xingjun - 24/08/2009
	 * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
	 * the genelist id and cluster id<p> 
	 */
	public String[] getGeneSymbolByAnalysisGenelistId(String genelistId, String platformId) {
		if (genelistId == null || genelistId.equals("")) {
			return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
		
		String[] geneSymbols = null;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (clstId == null) { // only genelist id passed in
            parQ = ArrayDBQuery.getParamQuery("GET_GENE_SYMBOL_BY_ANALYSIS_GENELIST_ID");
        } else {
        	parQ = ArrayDBQuery.getParamQuery("GET_GENE_SYMBOL_BY_ANALYSIS_GENELIST_CLUSTER_ID");
        }
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGeneLists sql: " + queryString);
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
            if (clstId == null) {
            	prepStmt.setInt(1, Integer.parseInt(glstId));
            	prepStmt.setString(2, platformId);
            } else {
            	prepStmt.setInt(1, Integer.parseInt(glstId));
            	prepStmt.setString(2, platformId);
            	prepStmt.setInt(3, Integer.parseInt(clstId));
            }
        	resSet = prepStmt.executeQuery();
        	geneSymbols = this.formatGeneSymbolByAnalysisGenelistIdResultSet(resSet);
        	
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return geneSymbols;
	}
	
    /**
     * @author xingjun - 25/08/2009
     * empty string gene symbol will not be included into the result
     * @param resSet
     * @return
     * @throws SQLException
     */
	private String[] formatGeneSymbolByAnalysisGenelistIdResultSet(ResultSet resSet) throws SQLException {
    	if (resSet.first()) {
    		resSet.beforeFirst();
			ArrayList<String> results = new ArrayList<String>();
    		while (resSet.next()) {
    			String geneSymbol = resSet.getString(1);
    			if (geneSymbol != null && !geneSymbol.equals("") 
    					&& !geneSymbol.equalsIgnoreCase("SymbolNA")) {
        			results.add(geneSymbol);
    			}
    		}
    		if (results != null && results.size() != 0) {
    			return results.toArray(new String[results.size()]);
    		}
    	}
		return null;
	}
	
	/**
	 * @author xingjun - 05/02/2010
	 */
	public ArrayList<GenelistInfo> getAllAnalysisGenelistsWithFolderIds() {
		ArrayList<GenelistInfo> genelists = null;
        ResultSet resSet = null;
        ParamQuery parQ = 
        	ArrayDBQuery.getParamQuery("ALL_ANALYSIS_GENELISTS_WITH_FOLDER_IDS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("getAllAnalysisGenelistsWithFolderIds sql: " + queryString);
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	resSet = prepStmt.executeQuery();
        	genelists = this.formatGenelistResultSet(resSet);
        	
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return genelists;
	}
	
	/**
	 * @author xingjun - 08/02/2010
	 * <p>xingjun - 13/05/2010 - added description into the genelist result</p>
	 */
	private ArrayList<GenelistInfo> formatGenelistResultSet(ResultSet resSet) throws SQLException {
    	if (resSet.first()) {
    		resSet.beforeFirst();
    		ArrayList<GenelistInfo> result = new ArrayList<GenelistInfo>();
    		int tempGenelistId = 0;
    		ArrayList<String> folderIdList = null;
			GenelistInfo genelist = null;
    		while (resSet.next()) {
    			int genelistId = resSet.getInt(1);
    			if (tempGenelistId == 0 || genelistId != tempGenelistId) {
	    			if (tempGenelistId != 0 ) { 
	    				genelist.setFolderIds(folderIdList);
	    				result.add(genelist);
	    			}
        			genelist = new GenelistInfo();
        			genelist.setGenelistId(resSet.getString(1));
        			genelist.setTitle(resSet.getString(2));
        			genelist.setSubmitter(resSet.getString(3));
        			genelist.setSummary(resSet.getString(4));
        			genelist.setDescription(resSet.getString(4));// added by xingjun - 13/05/2010
//        			genelist.setFilename(resSet.getString(5)+resSet.getString(6)+resSet.getString(7));
//        			genelist.setCdtFileName(resSet.getString(8));
        			String cdtFilename = resSet.getString(8);
        			String cdtFilename1 = cdtFilename.indexOf('/')<0 ?  cdtFilename : cdtFilename.substring(cdtFilename.lastIndexOf("/")+1);
        			String filename = resSet.getString(7);
        			String url = resSet.getString(5);
        			if (cdtFilename==null || "".equals(cdtFilename) || !cdtFilename1.equalsIgnoreCase(filename))
        				filename = url + resSet.getString(6) + filename;
        			else 
        				if (cdtFilename==null || "".equals(cdtFilename)) 
            				filename = url + resSet.getString(6) + filename;
        				else
        					filename = cdtFilename;
    				genelist.setFilename(filename);
    				genelist.setCdtFileName(cdtFilename);
        			genelist.setClustered(resSet.getInt(11)==1?true:false);        			
        			genelist.setNumberOfEntries(Integer.parseInt(resSet.getString(12)));        			
        			genelist.setPlatformId(resSet.getString(14));
    				folderIdList = new ArrayList<String>();
        			folderIdList.add(resSet.getString(13));
        			tempGenelistId = genelistId;
    			}
    			else 
    				folderIdList.add(resSet.getString(13));
    		}
    		// put the last genelist entity into the result
			genelist.setFolderIds(folderIdList);
			result.add(genelist);
            return result;
    	}
		return null;
	} // end of formatGenelistResultSet
	
	/**
	 * @author xingjun - 26/03/2010
	 * @param genelistId
	 * @return
	 */
	public ArrayList<GenelistInfo> retrieveClustersForGenelist(String genelistId) {
		ArrayList<GenelistInfo> genelists = null;
        ResultSet resSet = null;
        ParamQuery parQ = 
        	ArrayDBQuery.getParamQuery("GET_CLUSTERS_FOR_GIVEN_GENELISTS");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
//        System.out.println("retrieveClustersForGenelist:sql: " + queryString);
        try {
		    if (debug)
			System.out.println("MySQLGenelistDAOImp.sql = "+queryString.toLowerCase());
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, Integer.parseInt(genelistId));
        	resSet = prepStmt.executeQuery();
        	genelists = this.formatClusteredGenelistResultSet(resSet);
        	
        	DBHelper.closePreparedStatement(prepStmt);
        	DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return genelists;
	}
	
	/**
	 * @author xingjun - 26/03/2010
	 * xingjun - 27/05/2010 - added extra property displayOrder
	 */
	private ArrayList<GenelistInfo> formatClusteredGenelistResultSet(ResultSet resSet) throws SQLException {
    	if (resSet.first()) {
    		resSet.beforeFirst();
    		ArrayList<GenelistInfo> result = new ArrayList<GenelistInfo>();
    		while (resSet.next()) {
    			GenelistInfo genelist = new GenelistInfo();
    			genelist.setGenelistId(resSet.getString(1));
    			genelist.setFilename(resSet.getString(2));
    			genelist.setNumberOfEntries(resSet.getInt(3));
    			genelist.setTitle(resSet.getString(4));
    			genelist.setDisplayOrder(resSet.getInt(5));
    			result.add(genelist);
    		}
            return result;
    	}
		return null;
	}
	
}
