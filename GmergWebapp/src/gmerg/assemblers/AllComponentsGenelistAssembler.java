/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.GenelistDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.InMemoryTableAssembler;
import gmerg.utils.table.TableUtil;
import gmerg.utils.RetrieveDataCache;

import gmerg.entities.GenelistInfo;
import gmerg.entities.Globals;


/**
 * @author Mehran
 * 
 * used for list of genelists display page
 *
 */
public class AllComponentsGenelistAssembler extends InMemoryTableAssembler{
    private boolean debug = false;
    protected RetrieveDataCache cache = null;

    public AllComponentsGenelistAssembler () {
	
	if (debug)
	    System.out.println("AllComponentsGenelistAssembler.constructor");

	}

    public DataItem[][] retrieveData() {
    	if (null != cache) {
			if (debug)
			    System.out.println("AllComponentGenelistAssembler.retriveData data not changed");
		
			return cache.getData();
	    }
    	
    	String[][] data = null;
  	    	
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
					
	    	ArrayList<GenelistInfo> allAnalysisGenelists = genelistDAO.getAllAnalysisGeneLists();
	    	
	    	// convert to desired data structure
	    	if (allAnalysisGenelists != null && allAnalysisGenelists.size() != 0) {
	    		int row = allAnalysisGenelists.size();
				data = new String[row][7];
	    		for (int i= 0;i<row;i++) {
	    			data[i][0] = allAnalysisGenelists.get(i).getGenelistId();
	    			data[i][1] = allAnalysisGenelists.get(i).getTitle();
	       			data[i][2] = allAnalysisGenelists.get(i).getSubmitter();
	        		data[i][3] = allAnalysisGenelists.get(i).getSummary();
	                data[i][4] = allAnalysisGenelists.get(i).getFilename();
	//                System.out.println("ass:filename: " + allAnalysisGenelists.get(i).getFilename());
	                String cdtFileName = allAnalysisGenelists.get(i).getCdtFileName();
	                if (cdtFileName == null || cdtFileName.equals("")) {
	                    data[i][5] = null;
	                } else {
	                    data[i][5] = cdtFileName;
	                }
	                data[i][6] = null;
	    		}
	    	}
	    	
	    	DataItem[][] dataItems = getTableDataFormatFromListOfgenelists(data);

			if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(dataItems);	
			
			return dataItems;
    	
		} catch(Exception e){
			System.out.println("AllComponentsGenelistAssembler::retrieveData failed !!!");
			return null;
		}
    	finally{
	    	DBHelper.closeJDBCConnection(conn);
    	}	
	}
    
    public ArrayList getProbeSetIdsByGenelistId(String genelistId) {
    			
		ArrayList<String> probeSetIds = new ArrayList<String>();

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		try{
	
	        // get data from database
			// return value
	    	return probeSetIds;
					
		}
		catch(Exception e){
			System.out.println("AllComponentsGenelistAssembler::getProbeSetIdsByGenelistId failed !!!");
	    	return null;
		}	
		finally{
			DBHelper.closeJDBCConnection(conn);
			arrayDAO = null;
		}
    }
    
	/********************************************************************************
	 * Create Headers for list of processed genelists
	 *
	 */
	public HeaderItem[] createHeader()
	{
		String headerTitles[] = {"ID (temporary)", "Gene List", "JavaTreeView", "Source", "Summary", "Download" };
	      
	    boolean headerSortable[] = {true, true, false, true, true, false};

	    int colNum = headerTitles.length; 
	    
	    HeaderItem[] tableHeader = new HeaderItem[colNum];
	    for(int i=0; i<colNum; i++)
	    	tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);

	    return tableHeader;
	}
    

    /********************************************************************************
	 * populates GenericTable for list of processed genelists
	 * <p>modified by xingjun - 24/03/2009 - need to adjust to comply with changes in retrieveData method</p>
	 *
	 */
	private DataItem[][] getTableDataFormatFromListOfgenelists(String[][] genelistsData)
	{
		if(genelistsData == null) 
	    	return null;

		int colNum = genelistsData[0].length;
	    int rowNum = genelistsData.length;
//	    String archivePath = "https://www.gudmap.org/Gudmap/archive/hgu_private/microarrays/";
	    String archivePath = Globals.getArchiveSiteArrayURL();

	    DataItem[][] tableData = new DataItem[rowNum][colNum];
	    for(int i=0; i<rowNum; i++){
	    	if (genelistsData[i][5]==null) {
		    	tableData[i][0] = new DataItem(genelistsData[i][0], "click to view gene list", "mastertable_browse.html?genelistId="+genelistsData[i][0], 10);
		    	tableData[i][1] = new DataItem(genelistsData[i][1], "click to view gene list", "mastertable_browse.html?genelistId="+genelistsData[i][0], 10);
		    	tableData[i][5] = new DataItem("download", "click to download original data", genelistsData[i][4], 10);
	    	}
	    	else {
		    	tableData[i][0] = new DataItem(genelistsData[i][0]);
		    	tableData[i][1] = new DataItem(genelistsData[i][1]);
		    	tableData[i][5] = new DataItem("download", "click to download original data", archivePath+genelistsData[i][4], 10);
//		    	tableData[i][5] = new DataItem("download", "click to download original data", "../genelist_data/cdt_files/"+genelistsData[i][5], 10);
//		    	System.out.println("tableData[i][4]=========="+tableData[i][5].getLink());
	    	}
	    	
	    	HashMap<String, String> params = new HashMap<String, String>();
	    	params.put("genelistId", genelistsData[i][0]);
	    	if (genelistsData[i][5]!=null)
		    	params.put("cdtFile", archivePath+genelistsData[i][4]);
//	    		params.put("cdtFile", genelistsData[i][5]);
	    	if (genelistsData[i][5]!=null && genelistsData[i][6]!=null)
		    	params.put("gtrFile", archivePath+genelistsData[i][4]);
//	    		params.put("gtrFile", genelistsData[i][6]);
	    	
	        String script = TableUtil.getActionLinkScript("componentGenelistForm", "treeviewLink", params);
	    	tableData[i][2] = new DataItem("tree view", "Click to launch JavaTreeView to visualise this gene list" , script, 40);
	    	tableData[i][3] = new DataItem(genelistsData[i][2]);
//	    	tableData[i][3] = new DataItem(genelistsData[i][2], genelistsData[i][2], "lab_detail.html?personId="+genelistsData[i][3], 4);
	    	tableData[i][4] = new DataItem(genelistsData[i][3]);
	    }        

    	return tableData;
	}
	
	/**
	 * @author xingjun - 05/02/2010
	 * @return
	 */
	public static ArrayList<GenelistInfo> retrieveAllAnalysisGenelists() {
		
		ArrayList<GenelistInfo> allGenelists = new ArrayList<GenelistInfo>();
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
			allGenelists = genelistDAO.getAllAnalysisGenelistsWithFolderIds();
			return allGenelists;
		}
		catch(Exception e){
			System.out.println("AllComponentsGenelistAssembler::retrieveAllAnalysisGenelists !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * @author xingjun - 05/02/2010
	 * @return
	 */
	public static ArrayList<GenelistInfo> retrieveAllClusters(String genelistId) {
		
		ArrayList<GenelistInfo> allGenelists = new ArrayList<GenelistInfo>();
	
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
			allGenelists = genelistDAO.retrieveClustersForGenelist(genelistId);
			return allGenelists;
		}
		catch(Exception e){
			System.out.println("AllComponentsGenelistAssembler::retrieveAllClusters !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	final static String clusterIdMark = "_@_!_"; 
	public static String createClusterId(String genelistId, String id) {
		if (id == null || id.equals(""))
			return genelistId;
		return genelistId + clusterIdMark + id;
	}
	
	public static String getIdFromClusterId(String id) {
		int i = id.indexOf(clusterIdMark);
		if (i<0) 
			return null;
		return id.substring(i+clusterIdMark.length());
	}
	
	public static String getGenelistIdFromClusterId(String id) {
		int i = id.indexOf(clusterIdMark);
		if (i<0) 
			return id;
		return id.substring(0, i);
	}
	
	public static boolean isClusterId(String id) {
		return id.indexOf(clusterIdMark) >= 0;
	}

}
