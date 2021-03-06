/**
 * Composite VOA of gene details
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ResourceBundle;

import gmerg.entities.submission.Gene;
import gmerg.db.DBHelper;
import gmerg.db.ArrayDAO;
import gmerg.db.ISHDAO;
import gmerg.db.GeneDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.util.*;

/**
 * @author xingjun
 *
 */
public class GeneAssembler extends OffMemoryTableAssembler{
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

	String geneId;
	String probeset;
	Gene gene;
	private ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	
	public GeneAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("GeneAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
		geneId = getParam("geneId");
		probeset = getParam("probeset");
		gene = getData(geneId);
	}
	
    public DataItem[][] retrieveData(int columnIndex, boolean ascending, int offset, int num){
	    if (null != cache &&
		cache.isSameQuery(columnIndex, ascending, offset, num)) {
		if (debug)
		    System.out.println("GeneAssembler.retriveData data not changed");
		
		return cache.getData();
	    }

        // create a dao
        Connection conn = DBHelper.getDBConnection();
        try{
        	ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
	        
	        // get data from database
			// - use symbol from gene object instead of geneId params 
			// - sometimes geneId is actually synonym of the gene and symbol of gene object 
			//   will always be the real symbol
        	ArrayList arraysForGene = ishDAO.findRelatedSubmissionBySymbolArray(gene.getSymbol(), columnIndex, ascending, offset, num);

	        // return the value object
	        DataItem[][] ret =  getTableDataFormatFromSamplesList(arraysForGene);
		
			if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(ret);
			cache.setColumn(columnIndex);
			cache.setAscending(ascending);
			cache.setOffset(offset);
			cache.setNum(num);	
			
			return ret;
        }
		catch(Exception e){
			System.out.println("GeneAssembler::retrieveData !!!");
			return null;
		}		
        finally{
	        DBHelper.closeJDBCConnection(conn);
       }

    }

    public int retrieveNumberOfRows () {
    	int n = 0;
        try {
            n = Integer.parseInt(gene.getNumMicArrays());
        } catch (NumberFormatException e) {
        }
        return n;
    }
    
    /**
     * returns an array of integers containing the number of different values
     * found in each column of the microarray table for a particular gene
     * <p>modified by xingjun - 19/03/2009 - refer to comment in method 'retrieveData'</p>
     * @param id - the id of the series
     * @return totalNumbers - an array of column totals for the sample table
     */
    public int[] retrieveTotals() {
	// force nerw cache
	cache = null;

        // create a dao
        Connection conn = DBHelper.getDBConnection();
        int[] totalNumbers = new int[0];
        try{
        	ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
	
	        String[] allColTotalsQueries =
	        { "TOTAL_GENE_RELATED_ARRAYS", "TOTAL_GENE_PROBEIDS", "TOTAL_GENE_SIGNAL",
	          "TOTAL_GENE_DETECTION", "TOTAL_GENE_PVALUE" };
	        String[][] param = { { gene.getSymbol() }, { gene.getSymbol() }, { gene.getSymbol() }, { gene.getSymbol() }, { gene.getSymbol() }};
	        String[][] columnNumbers = ishDAO.getStringArrayFromBatchQuery(param,
	                                                     allColTotalsQueries);;
	            
	
	        // convert to interger array, each tuple consists of column index and the number
	        int len = columnNumbers.length;
	        totalNumbers = new int[len];
	        for (int i = 0; i < len; i++) {
	            totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
	        }
	        // return result
	        return totalNumbers;
		}
		catch(Exception e){
			System.out.println("GeneAssembler::retrieveTotals !!!");
			totalNumbers = new int[0];
	        // return result
	        return totalNumbers;
		}		
        finally{
	        DBHelper.closeJDBCConnection(conn);
        }
    }
    
	/**
	* <p>Initializes browse all data table for array data, setting header titles
	* and other additional parameters</p>
	* 
	*/
	public HeaderItem[] createHeader() {
		//set the titles of each of the columns in the table
		String projectString = bundle.getString("project").trim();
		String headerTitles[] = { (projectString+" ID"), "Probe ID", "Signal", "Detection", "P-Value" };
		//specify which columns are sortable
		boolean headerSortable[] = { false, false, false, false, false };
		
		int colNum = headerTitles.length;
		HeaderItem[] tableHeader = new HeaderItem[colNum];
		for (int i = 0; i < colNum; i++) 
			tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		
		return tableHeader;
	}
	
	/**
	 * method to retrieve all relevant info (except microarray data) on a particular gene based on gene symbol and probeSetId
	 * <p>modified by xingjun - 18/03/2009 
	 * - sometimes for array data gene symbols are actually their synonyms. 
	 * that means when make query on array-linked gene info, input should includes both synonyms and symbol </p>
	 * @param symbol - the symbol of the gene
	 * @param probeSetId - the affy probeset id
	 * @return geneInfo - a info on a particular gene in the db 
	 */
	public Gene getData(String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
		String geneSymbol = symbol;
		String alternateSymbol = null;
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			// get gene info
			Gene geneInfo = ishDAO.findGeneInfoBySymbolId(geneSymbol);
			
			// might not find the gene
			if (geneInfo == null) {
	//			System.out.println("GeneAssembler:getData:geneInfo not found by ish#####: ");
				ArrayList<String> synonymsAndSymbol = new ArrayList<String>();
				synonymsAndSymbol.add(geneSymbol);
				GeneDAO geneDAO = MySQLDAOFactory.getGeneDAO(conn);
				alternateSymbol = geneDAO.findSymbolBySynonym(symbol);
	//			System.out.println("alternateSymbol: " + alternateSymbol);
				if (alternateSymbol != null && !alternateSymbol.equals("")) {
					synonymsAndSymbol.add(alternateSymbol);
				}
				
				ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
				
				// find gene info
				geneInfo = arrayDAO.findGeneInfoBySymbol(synonymsAndSymbol);
				if(geneInfo == null){
	//				System.out.println("geneInfo is null############### ");
			    	return null;
				} else {
	//		    	System.out.println("GeneAssembler:geneInfo by array:synonym: " + geneInfo.getSynonyms());
	//		    	System.out.println("geneInfo is not null############### ");
	//		    	System.out.println("gene symbol: " + geneInfo.getSymbol());
			    	geneInfo = ishDAO.findFurtherGeneInfoForMicroarray(geneInfo);
			    	// should use alternate symbol to make query
			    	geneSymbol = alternateSymbol;
	//		    	System.out.println("GeneAssembler:geneInfo by further array search:synonym: " + geneInfo.getSynonyms());
			    }
			}
	
			// get associated probe
			ArrayList associatedProbe = ishDAO.findRelatedMAProbeBySymbol(geneSymbol);
			
			//get related ish submissions
			ArrayList relatedSubmissionISH = ishDAO.findRelatedSubmissionBySymbolIdISH(geneSymbol);

			//get related ish submissions
			ArrayList relatedSubmissionIHC = ishDAO.findRelatedSubmissionBySymbolIdIHC(geneSymbol);

			//get related ish submissions
			ArrayList relatedSubmissionTG = ishDAO.findRelatedSubmissionBySymbolIdTG(geneSymbol);
			
			/** ---complete gene object---  */
			if (null != relatedSubmissionISH) {
				geneInfo.setIshSubmissions(relatedSubmissionISH);
			}
			if (null != relatedSubmissionIHC) {
				geneInfo.setIhcSubmissions(relatedSubmissionIHC);
			}
			if (null != relatedSubmissionTG) {
				geneInfo.setTgSubmissions(relatedSubmissionTG);
			}
			if (null != associatedProbe) {
				geneInfo.setAssocProbes(associatedProbe);
			}
			
			// get translational links info
			geneInfo = ishDAO.addGeneInfoIuphar(geneInfo);
			/** ---return the composite value object---  */
			return geneInfo;
		}
		catch(Exception e){
			System.out.println("GeneAssembler::getData !!!");
			return null;
		}		
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
        
	public static DataItem[][] getTableDataFormatFromSamplesList(ArrayList samples) {
	    //if no samples are retrieved, return a null value
	    if (samples == null) {
	        return null;
	    }
	
	    int colNum = ((String[])samples.get(0)).length;
	    int rowNum = samples.size();
	
	    //build the data item containing the actual sample data from the ArrayList
	    DataItem[][] tableData = new DataItem[rowNum][colNum];
	    for (int i = 0; i < rowNum; i++) {
	        String[] row = (String[])samples.get(i);
	        tableData[i][0] = new DataItem(row[0], "View Sample", "mic_submission.html?id=" + row[0], 10); // Project Id
	        tableData[i][1] = new DataItem(row[1]); // Probe Id
	        tableData[i][2] = new DataItem(row[2]); // Signal
	        tableData[i][3] = new DataItem(row[3]); // Detection
	        tableData[i][4] = new DataItem(row[4]); // p-value
	    }
	
	    return tableData;
	}

	public Gene getGene() {
		return gene;
	}
}
