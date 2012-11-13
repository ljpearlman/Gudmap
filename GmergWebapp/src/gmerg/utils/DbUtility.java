/**
 * 
 */
package gmerg.utils;

import gmerg.db.ArrayDAO;
import gmerg.db.ArrayDevDAO;
import gmerg.db.CollectionDAO;
import gmerg.db.DBHelper;
import gmerg.db.GeneDAO;
import gmerg.db.GenelistDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.array.MasterTableInfo;
import gmerg.entities.submission.array.SearchLink;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */

public class DbUtility {
    
    public static ArrayList<String> retrieveGeneProbeIds(String geneSymbol, String platformId) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	
        // get data from database
	ArrayList<String> probeIds = arrayDAO.getProbeSetIdBySymbol(geneSymbol, platformId);
	if (probeIds == null || probeIds.size() == 0) {
	    GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
	    String alternateSymbol = geneDAO.findSymbolBySynonym(geneSymbol);
	    probeIds = arrayDAO.getProbeSetIdBySymbol(alternateSymbol, platformId);
	}
	
        // release db resources
        DBHelper.closeJDBCConnection(conn);
        arrayDAO = null;
	
        // return the value object
	return probeIds;
    }
    
    /**
     * @author xingjun 16/10/2009
     * @param symbol
     * @return
     */
    public static ArrayList<String> retrieveImageIdsByGeneSymbol(String symbol) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
	
        // get data from database
	ArrayList<String> imageIds = collectionDAO.getInsituSubmissionImageIdByGene(symbol);
	
        // release db resources
        DBHelper.closeJDBCConnection(conn);
        collectionDAO = null;
	
        // return the value object
	return imageIds;
    }
    
    /**
     * @author xingjun
     * - if given genelist is linked to the given platform, get relevant probe ids
     * - if given genelist is linked to different platform, get probe ids through
     *   gene symbol mappings</p>
     */
    public static ArrayList<String> retrieveGenelistProbeIds(String genelistId, String platformId) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
	
	// check if given genelist is linked to the given platform
	// if it is, return relevant probe ids;
	// if not, get probe ids on the given platform
	String geneListPlatformId = genelistDAO.getAnalysisGenelistPlatformId(genelistId);
	
	// given genelist is not linked to any platform in GUDMAP database
	if (geneListPlatformId == null && geneListPlatformId.equals("")) {
	    return null;
	}
	
	ArrayList<String> probeSetIds = null;
	if (geneListPlatformId.equalsIgnoreCase(platformId)) {
	    probeSetIds = 
		arrayDAO.getProbeSetIdByAnalysisGenelistId(genelistId, true, 0, 20);
	} else {
	    // get relevant gene symbols
	    String[] geneSymbols = 
		genelistDAO.getGeneSymbolByAnalysisGenelistId(genelistId, geneListPlatformId);
	    
	    // get probe ids on the given platform
	    if (geneSymbols != null && geneSymbols.length > 0) {				
		probeSetIds = 
		    arrayDAO.getProbeSetIdBySymbols(geneSymbols, platformId);
	    }
	}
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	arrayDAO = null;
	genelistDAO = null;
	
	// return value
    	return probeSetIds;
    }
    
    /**
     * @author xingjun - 26/03/2009
     * @param genelistId
     * @return
     */
    public static String retrieveGenelistTitle(String genelistId) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	
        // get data from database
	String genelistTitle = arrayDAO.getAnalysisGenelistTitle(genelistId);
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	arrayDAO = null;
	
	// return value
	return genelistTitle;
    }
    
    
    //***************************************************************************************************
	public static ArrayList<SearchLink> retrieveSearchLinks() {
	
	/** ---get data from dao---  */
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	
	// get data
	ArrayList<SearchLink> links = arrayDAO.getGenelistExternalLinks();
	ArrayList<SearchLink> externalLinks = new ArrayList<SearchLink>(); 
	for(SearchLink link: links) {
	    if(link.getName().equalsIgnoreCase("GO") || 
	       link.getName().equalsIgnoreCase("OMIM") || 
	       link.getName().indexOf("MRC")>=0 || 
	       link.getName().equalsIgnoreCase("KEGG_OLD"))
		continue;
	    externalLinks.add(link);
	}
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	arrayDAO = null;
	
	/** ---return the composite value object---  */
	return externalLinks;
    }
    
    public static String[] retrieveSearchLinkTitles() {
	final String[] searchLinkTitles={"KEGG", "MGI", "UCSC", "GUDMAP-ISH"}; //GO & OMIM are removed for the time being
	
	return searchLinkTitles;
    }
    
    /**
     * @author xingjun - 07/04/2009
     * @param input
     * @param wildcard
     * @return
     */
    public static ArrayList retrieveGeneSymbolsFromGeneInput(String input, String wildcard) {
	if (input == null || input.equals(""))
	    return null;
	
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
	
	// get data
	ArrayList geneSymbols = geneDAO.getSymbolsFromGeneInput(input, wildcard);
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	geneDAO = null;
	
	/** ---return the value object---  */
	return geneSymbols;
    }
    
    /**
     * @author xingjun - 01/07/2009
     * @param mgiId
     * @return
     */
    public static String retrieveGeneSymbolByMGIId(String mgiId) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
	
	// get data
	String geneSymbol = geneDAO.getGeneSymbolByMGIId(mgiId);
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	geneDAO = null;
	
	/** ---return the value object---  */
	return geneSymbol;
    }
    
    /**
     * <p>created by Mehran and implemented by xingjun on 11/08/2009</p>
     * @return
     */
    public static MasterTableInfo[] getAllMasterTablesInfo() {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	
	// get data
	MasterTableInfo[] masterTableList = arrayDAO.getMasterTableList(); 
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	arrayDAO = null;
	
	// return value object
	return masterTableList;
    }
    
    /**
     * <p>created by Mehran and implemented by xingjun on 11/08/2009</p>
     * @param masterTableId
     * @return
     */
    public static String getMasterTablePlatformId(String masterTableId) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
	
	// get data
	String platformId = arrayDAO.getMasterTablePlatformId(masterTableId);
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	arrayDAO = null;
	
	// return value object
	return platformId;
    }
    
    /**
     * implemented by xingjun - 25/08/2009
     * @param genelistId
     * @return
     */
    public static String getGenelistPlatformId(String genelistId) {
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
	
	// get data
	String platformId = genelistDAO.getAnalysisGenelistPlatformId(genelistId);
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	
	// return value object
	return platformId;
    }
    
    /**
     * @author xingjun - 02/09/2009
     * <p>modified by xingjun - 23/09/2009
     * - make it able to deal with the situation when no relevant gudmap id has been found at all</p>
     * @param candidateGudmapIds
     * @return
     */
    public static ArrayList<String> checkGudmapIds(ArrayList candidateGudmapIds) {
	if (candidateGudmapIds == null || candidateGudmapIds.size() == 0) {
	    return null;
	}
	
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
	// get data
	ArrayList<String> relevantGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, Utility.getUserPriviledges());
	
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	
	return relevantGudmapIds;
    }
    
    
    /**
     * @author xingjun - 02/09/2009
     * <p>xingjun - 29/10/2009 - modified to make the return relevant to the user's privilege</p>
     * @param candidateSymbols
     * @return
     */
    public static ArrayList<String> checkGeneSymbols(ArrayList candidateSymbols) {
	if (candidateSymbols == null || candidateSymbols.size() == 0) {
	    return null;
	}
	
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
	
	// get data
	//		ArrayList<String> relevantSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols);
	ArrayList<String> relevantSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, Utility.getUserPriviledges());
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	return relevantSymbols;
    }
    
    /**
     * @author xingjun - 02/09/2009
     * @param candidateProbeSetIds
     * @param platformId
     * @return
     */
    public static ArrayList<String> checkProbeSetIds(ArrayList candidateProbeSetIds, String platformId) {
	if (candidateProbeSetIds == null || candidateProbeSetIds.size() == 0) {
	    return null;
	}
	
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
	
	// get data
	ArrayList<String> relevantProbeSetIds = arrayDevDAO.getRelevantProbeSetIds(candidateProbeSetIds, platformId);
	
	DBHelper.closeJDBCConnection(conn);
	return relevantProbeSetIds;
    }

    /**
     * @author xingjun - 11/12/2009
     */
    public static ArrayList<String> checkImageIds(ArrayList<String> candidateImageIds) {
	if (candidateImageIds == null || candidateImageIds.size() == 0) {
	    return null;
	}
	// create a dao
	Connection conn = DBHelper.getDBConnection();
	ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
	
	// get data
	ArrayList<String> relevantImageIds = arrayDevDAO.getRelevantImageIds(candidateImageIds, Utility.getUserPriviledges());
	// release db resources
	DBHelper.closeJDBCConnection(conn);
	return relevantImageIds;
    }
    
    /** DB query result is returned as a ArrayList of Object for 1 column query or Object[]
     *  for 1+ columns query
     */
    public static List query(String statement) {
	
	if (null == statement)
	    return null;
	
        Statement stmt = null;
	ResultSet rs = null;
	List ret = new ArrayList();
	Connection conn = DBHelper.getDBConnection();
        try {
	    conn = DBHelper.reconnect2DB(conn);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(statement);
	    if (null != rs) {
		ResultSetMetaData md = rs.getMetaData();
		int iSize = md.getColumnCount();
		Object[] row = null;
		int i = 0;
		while (rs.next()) {
		    if (1 == iSize)
			ret.add(rs.getObject(1));
		    else {
			row = new Object[iSize];
			for (i = 0; i < iSize; i++)
			    row[i] = rs.getObject(i+1);
			ret.add(row);
		    }
		}
	    }
	    
	    // close the db object
	    DBHelper.closeJDBCConnection(conn);
	} catch (SQLException se) {
	    se.printStackTrace();
	}
	
	if (0 == ret.size())
	    return null;
	
	return ret;
    }
    }   
    
  
