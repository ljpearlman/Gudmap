/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.array.SearchLink;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class ProcessedGenelistDataAssembler extends OffMemoryTableAssembler {
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

    private ArrayList geneListSearchLinks;
	private int genelistId;
	private String projectString = ResourceBundle.getBundle("configuration").getString("project").trim();
    
	public ProcessedGenelistDataAssembler (HashMap params) {
    	super(params);
	if (debug)
	    System.out.println("ProcessedGenelistDataAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
    	Integer id = (Integer)params.get("genelistId");
    	genelistId = 0;
		if (id != null) 
			genelistId = id.intValue();
    	
        geneListSearchLinks = retrieveSearchLinks();
	}
	
	/**
	 * 
	 * @param genelistId
	 * @return
	 */
	public DataItem[][] retrieveData(int columnId, boolean ascending, int offset, int num) {
	    if (null != cache &&
		cache.isSameQuery(columnId, ascending, offset, num)) {
		if (debug)
		    System.out.println("ProcessedGenelistDataAssembler.retriveData data not changed");
		
		return cache.getData();
	    }
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			Object[][] genelistItems = null;;
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			
			// get data
			genelistItems = arrayDAO.getGenelistItemsByGenelistId(genelistId, columnId+1, ascending, offset+1, num );

			/** ---return the composite value object---  */
			DataItem[][] ret = getTableDataFormatFromGenelistData(genelistItems);

			if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(ret);
			cache.setColumn(columnId);
			cache.setAscending(ascending);
			cache.setOffset(offset);
			cache.setNum(num);

			return ret;
		}
		catch(Exception e){
			System.out.println("ProcessedGenelistDataAssembler::retrieveData failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

	/**
	 * 
	 * @param genelistId
	 * @return
	 */
	public int retrieveNumberOfRows () {

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			int total = arrayDAO.getGenelistsEntryNumber(genelistId);
			return total;
		}
		catch(Exception e){
			System.out.println("ProcessedGenelistDataAssembler::retrieveNumberOfRows failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
		
	public HeaderItem[] createHeader() {
		String headerTitles[] = {"Grp1 Mean" , "Grp2 Mean" , "Grp1 Sem"       , "Grp2 Sem"  , "Ratio"    , 
	                             "Direction" , "P-Value"   , "Gene Identifier", "Affy ID"  , "Gene Name", 
	                             "UG Cluster", "Locus Link", "Gene Symbol"    , "Chromosome", "Ontologies",
//	                             "GUDMAP-ISH", "KEGG"      , "MGI"       	   , "UCSC"      , "OMIM"      }; //"GUDMAP-GENE", "GO"        , 
	                             projectString+"-ISH", "KEGG"      , "MGI"       	   , "UCSC"      , "OMIM"      }; //projectString+"-GENE", "GO"        , 
	     
		boolean headerSortable[] = {true, true, true, true, true,  false, true, true, true, true,  true, true, true, true, true, 
	                                true, false, false, false, false }; //false, false };

		int colNum = headerTitles.length;
		HeaderItem[] tableHeader = new HeaderItem[colNum];
		for(int i=0; i<colNum; i++)
			tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		return tableHeader;
	}

	/**
	 * 
	 * @return
	 */
	private ArrayList retrieveSearchLinks() {

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayList externalLinks	 = new ArrayList();	
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			
			// get data
			externalLinks = arrayDAO.getGenelistExternalLinks();

			return externalLinks;
		}
		catch(Exception e){
			System.out.println("ProcessedGenelistDataAssembler::retrieveSearchLinks failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

	/********************************************************************************
	 * populates processed gene list data table (only one page for display gets populated 
	 *
	 */
	private DataItem[][] getTableDataFormatFromGenelistData(Object[][] data)
	{
		if (data == null){
			System.out.println("No data is retrieved");
			return null;
		}
	    
		int colNum1 = data[0].length - 2;
		int colNum2 = geneListSearchLinks.size() -2;  // -2 because -- GO & MRC2 are removed
		int colNum = colNum1 + colNum2;
		int rowNum = data.length;
	    
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int row=0; row<rowNum; row++){
			String geneSymbol = (String)data[row][12];
			for(int col=0; col<colNum1; col++){   //gene list data 
				if (col==12 && !geneSymbol.equals("-"))  //Gene ID
					tableData[row][col] = new DataItem(geneSymbol, geneSymbol, "../pages/gene.html?gene="+geneSymbol, 4); //url for Gudmap ISH
				else
					tableData[row][col] = new DataItem(data[row][col]);
			}
			//Gumap ISH link
			int entriesNum = ((Integer)data[row][colNum1]).intValue();
			String suffix = " entr" + ((entriesNum==1)? "y" : "ies" );
			String title = ((entriesNum==0)?"no" : String.valueOf(entriesNum)) + suffix;
			if(entriesNum > 0)
				tableData[row][colNum1] = new DataItem(title, title, "../pages/ish_gene_submissions.html?queryType=geneQueryISH&ignoreExpression=true&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
			else
				tableData[row][colNum1] = new DataItem(title);
			
			for(int i=1, col=colNum1+1; i<colNum2; i++, col++){   //search links -- GO & MRC2 are removed
				SearchLink link = (SearchLink)geneListSearchLinks.get(i-1);
				if(i == colNum2-1){ //OMIM replacing MRC2 
					link = (SearchLink)geneListSearchLinks.get(5); //index for OMIM : (should be replaced by a hashmap)
					String omimId = (String)data[row][colNum1+1]; //OMIM ID
					if (omimId != null)
						tableData[row][col] = new DataItem(link.getName(), link.getName() , link.getUrl(omimId), 4);  //Pass gene symbol
					else
						tableData[row][col] = new DataItem("-");
				}
				else          
					tableData[row][col] = new DataItem(link.getName(), link.getName() , link.getUrl(geneSymbol), 4);  //Pass gene symbol
			}
		}
		
		return tableData;
	}  
	
}
