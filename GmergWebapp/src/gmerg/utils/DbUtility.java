/**
 * 
 */
package gmerg.utils;

import gmerg.db.AdvancedSearchDBQuery;
import gmerg.db.ArrayDAO;
import gmerg.db.ArrayDevDAO;
import gmerg.db.CollectionDAO;
import gmerg.db.DBHelper;
import gmerg.db.GeneDAO;
import gmerg.db.GenelistDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.GenelistTreeInfo;
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
    
    public static ArrayList<String> retrieveGeneProbeIds(String geneId, String platformId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			
		        // get data from database
			ArrayList<String> probeIds = arrayDAO.getProbeSetIdBySymbol(geneId, platformId);
			if (probeIds == null || probeIds.size() == 0) {
			    GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
			    String alternateSymbol = geneDAO.findSymbolBySynonym(geneId);
			    probeIds = arrayDAO.getProbeSetIdBySymbol(alternateSymbol, platformId);
			}
			
			return probeIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGeneProbeIds failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }

    public static ArrayList<String> retrieveGeneProbeIdsByGeneId(String symbolId, String platformId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			
		        // get data from database
			ArrayList<String> probeIds = arrayDAO.getProbeSetIdBySymbol(symbolId, platformId);
			if (probeIds == null || probeIds.size() == 0) {
			    GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
			    String alternateSymbol = geneDAO.findSymbolBySynonym(symbolId);
			    probeIds = arrayDAO.getProbeSetIdBySymbol(alternateSymbol, platformId);
			}
			
			return probeIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGeneProbeIds failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    
    /**
     * @author xingjun 16/10/2009
     * @param symbol
     * @return
     */
    public static ArrayList<String> retrieveImageIdsByGeneSymbol(String symbol) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
			ArrayList<String> imageIds = collectionDAO.getInsituSubmissionImageIdByGene(symbol);
			return imageIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveImageIdsByGeneSymbol failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    public static ArrayList<String> retrieveImageIdsByGeneSymbolId(String symbolId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
			ArrayList<String> imageIds = collectionDAO.getInsituSubmissionImageIdByGeneId(symbolId);
			return imageIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveImageIdsByGeneSymbol failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
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
		try{
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
			    	probeSetIds = arrayDAO.getProbeSetIdBySymbols(geneSymbols, platformId);
			    }
			}
			
	
	    	return probeSetIds;
    	
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGeneProbeIds failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
		
    }
    
    /**
     * @author xingjun - 26/03/2009
     * @param genelistId
     * @return
     */
    public static String retrieveGenelistTitle(String genelistId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			String genelistTitle = arrayDAO.getAnalysisGenelistTitle(genelistId);
			return genelistTitle.replace("_", " ");
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGenelistTitle failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    
	public static String retrieveGenelist(String genelistId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			String genelist = arrayDAO.getAnalysisGenelist(genelistId);
			return genelist;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGenelist failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
	}
    
    //***************************************************************************************************
	public static ArrayList<SearchLink> retrieveSearchLinks() {
	
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
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
			return externalLinks;
		
		} catch(Exception e){
			System.out.println("DBUtility::retrieveSearchLinks failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
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
    public static ArrayList<String> retrieveGeneSymbolsFromGeneInput(String input, String wildcard) {
		if (input == null || input.equals(""))
		    return null;
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
			ArrayList<String> geneSymbols = geneDAO.getSymbolsFromGeneInput(input, wildcard);
			return geneSymbols;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGeneSymbolsFromGeneInput failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    
    /**
     * @author xingjun - 01/07/2009
     * @param mgiId
     * @return
     */
    public static String retrieveGeneSymbolByMGIId(String mgiId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
			String geneSymbol = geneDAO.getGeneSymbolByMGIId(mgiId);
			return geneSymbol;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGeneSymbolByMGIId failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    
    public static String retrieveGeneIdBySymbol(String symbol, String species) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
			String geneId = geneDAO.getGeneIdBySymbol(symbol, species);
			return geneId;
			
		} catch(Exception e){
			System.out.println("DBUtility::retrieveGeneIdBySymbol failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
   
    
    /**
     * <p>created by Mehran and implemented by xingjun on 11/08/2009</p>
     * @return
     */
    public static MasterTableInfo[] getAllMasterTablesInfo() {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			MasterTableInfo[] masterTableList = arrayDAO.getMasterTableList(); 
			return masterTableList;
			
		} catch(Exception e){
			System.out.println("DBUtility::getAllMasterTablesInfo failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    
    /**
     * <p>created by Mehran and implemented by xingjun on 11/08/2009</p>
     * @param masterTableId
     * @return
     */
    public static String getMasterTablePlatformId(String masterTableId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			String platformId = arrayDAO.getMasterTablePlatformId(masterTableId);
			return platformId;
			
		} catch(Exception e){
			System.out.println("DBUtility::getMasterTablePlatformId failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
    }
    
    /**
     * implemented by xingjun - 25/08/2009
     * @param genelistId
     * @return
     */
    public static String getGenelistPlatformId(String genelistId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
			String platformId = genelistDAO.getAnalysisGenelistPlatformId(genelistId);
			return platformId;
			
		} catch(Exception e){
			System.out.println("DBUtility::rgetGenelistPlatformId failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
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
		try{
			ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
			ArrayList<String> relevantGudmapIds = arrayDevDAO.getRelevantGudmapIds(candidateGudmapIds, Utility.getUserPriviledges());
			return relevantGudmapIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::checkGudmapIds failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

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
		try{
			ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
			ArrayList<String> relevantSymbols = arrayDevDAO.getRelevantSymbols(candidateSymbols, Utility.getUserPriviledges());
			return relevantSymbols;
			
		} catch(Exception e){
			System.out.println("DBUtility::checkGeneSymbols failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

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
		try{
			ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
			ArrayList<String> relevantProbeSetIds = arrayDevDAO.getRelevantProbeSetIds(candidateProbeSetIds, platformId);
			return relevantProbeSetIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::checkProbeSetIds failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
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
		try{
			ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
			ArrayList<String> relevantImageIds = arrayDevDAO.getRelevantImageIds(candidateImageIds, Utility.getUserPriviledges());
			return relevantImageIds;
			
		} catch(Exception e){
			System.out.println("DBUtility::checkImageIds failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

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
			if (0 == ret.size())
			    return null;
			
			return ret;
		    
		} catch (SQLException se) {
		    se.printStackTrace();
		    return null;
		}
        finally{
		    DBHelper.closeStatement(stmt);       	
		    DBHelper.closeResultSet(rs);       	
		    DBHelper.closeJDBCConnection(conn);        	
        }
    }
    
	public static ArrayList<String> getListOfSampleNames(String dataset, String stage, String sample) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			ArrayList<String> sampleListNames = null;
			sampleListNames = arrayDAO.findSampleList(dataset, stage, sample);
	    	return sampleListNames;
	    	
		} catch(Exception e){
			System.out.println("DBUtility::getListOfSampleNames failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	
	}

	public static ArrayList<GenelistTreeInfo> getRefGenelists() {
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			ArrayList<GenelistTreeInfo> result = null;
			result = arrayDAO.getRefGenelists();
	    	return result;
	    	
		} catch(Exception e){
			System.out.println("DBUtility::getRefGenelists failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

	}
	
	public static String[] getRefStages(String stage) {
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			List<String> result = arrayDAO.getRefStages(stage);
			if(stage == "Mus musculus"){
				result = result.subList(16, result.size());
			}
			
			String[] array = new String[result.size()];
			for(int i=0; i<result.size(); i++){
				array[i] = result.get(i);
			}

	    	return array;
	    	
		} catch(Exception e){
			System.out.println("DBUtility::getRefStages failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

	}

	public static String getRefStageOrder(String stage) {
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			String result = arrayDAO.getRefStageOrder(stage);

	    	return result;
	    	
		} catch(Exception e){
			System.out.println("DBUtility::getRefStageOrder failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

	}

	public static String getRefStageFromOrder(String order) {
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			String result = arrayDAO.getRefStageFromOrder(order);

	    	return result;
	    	
		} catch(Exception e){
			System.out.println("DBUtility::getRefStageFromOrder failed !!!");
			return null;
		}
		finally{
	    	DBHelper.closeJDBCConnection(conn);
		}	

	}

	
}   
    
  
