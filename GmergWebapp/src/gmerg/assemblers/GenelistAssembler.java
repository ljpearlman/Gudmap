/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.ArrayDAO;
import gmerg.db.GenelistDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.GenelistInfo;
import gmerg.entities.submission.array.SearchLink;
import gmerg.utils.table.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class GenelistAssembler  {/* extends OffMemoryTableAssembler {
	* 
	 *
	 *
	 *
	 *	Is not being used
	 * 
	 * 
	 * 
	 * 
	 * 
	
    private ArrayList genelistSearchLinks;
	private int genelistId;
	private GenelistInfo genelistInfo;
    
	public GenelistAssembler (HashMap params) {
    	super(params);
	}

	public void setParams() {
		super.setParams();
    	Integer id = (Integer)params.get("genelistId");
    	genelistId = 0;
		if (id != null) 
			genelistId = id.intValue();
    	
        genelistSearchLinks = retrieveSearchLinks();
        genelistInfo = retrieveGenelistInfo();
	}

	public DataItem[][] retrieveData(int columnId, boolean ascending, int offset, int num) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		
		// get data
//		Object[][] genelistItems = arrayDAO.getGenelistItemsByGenelistId(genelistId, columnId+1, ascending, offset+1, num);
		Object[][] allData = genelistDAO.getData(columnId, ascending, offset, num, genelistId);
		int analysisDataColNum = genelistDAO.getAnalysisTitles(genelistId).length;
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		genelistDAO = null;

		// return value
//		GenelistDAO genelistDAO = new GenelistDAO();
		return getTableDataFormatFromGenelistData(allData, analysisDataColNum);
		
	}

	public int retrieveNumberOfRows () {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		int numRows = genelistDAO.getTotalNumberOfRows(genelistId);
		return numRows;
	}
		
	public HeaderItem[] createHeader() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		String[] analysisTitles = genelistDAO.getAnalysisTitles(genelistId);
		String[] ontologyTitles = genelistDAO.getOntologyTitles();
		String[] searchLinkTitles = getSearchLinkTitles();
		String[] analysisTitlesImageNames = genelistDAO.getAnalysisTitlesImageNames(genelistId);
		
		int colNum = 1 + analysisTitles.length + ontologyTitles.length + searchLinkTitles.length;
		HeaderItem[] tableHeader = new HeaderItem[colNum];
		
		tableHeader[0] = new HeaderItem(genelistDAO.getIdTitle(genelistId), true); // Affy ID
		
		for(int i=0; i<analysisTitles.length; i++)
			tableHeader[i+1] = new HeaderItem(analysisTitles[i], true, 0, "../images/titles/"+analysisTitlesImageNames[i]);
		
		for(int i=0; i<ontologyTitles.length; i++)
			tableHeader[i+analysisTitles.length+1] = new HeaderItem(ontologyTitles[i], true);
		
		for(int i=0; i<searchLinkTitles.length; i++)
			tableHeader[i+analysisTitles.length+ontologyTitles.length+1] = new HeaderItem(searchLinkTitles[i], (i==0));
		
		return tableHeader;
	}
	
	public String[] getAnalysisTitles() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		String[] analysisTitles = genelistDAO.getAnalysisTitles(genelistId);
		return analysisTitles;
	}
	
	public String[] getOntologyTitles() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		String[] ontologyTitles = genelistDAO.getOntologyTitles();
		return ontologyTitles;
	}
	
	public int[][] getOntologyColsWidth() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		return  genelistDAO.getOntologyColsWidth();
	}
	
	public int[] getDefaultOntologyCols() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		return genelistDAO.getDefaultOntologyCols();
	}
	
	public int[] getHeatmapCols() {
		int[] heatmapCols = genelistInfo.getHeatmapCols().clone();
		for(int i=0; i<heatmapCols.length; i++)
			heatmapCols[i]++;			// This a shift in column number because id column is the first  
		
		return heatmapCols;
	}
	
	public GenelistInfo getGenelistInfo() {
		return genelistInfo;
	}

	//***************************************************************************************************************
	private GenelistInfo retrieveGenelistInfo() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
   		
		return genelistDAO.getGenelistInfo(genelistId);
	}

	
	private ArrayList retrieveSearchLinks() {

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get data
		ArrayList externalLinks = arrayDAO.getGenelistExternalLinks();
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		return externalLinks;
	}

	private String[] getSearchLinkTitles() {
		final String[] searchLinkTitles={"KEGG", "MGI", "UCSC", "GUDMAP-ISH"}; //GO & OMIM are removed for the time being
		
		return searchLinkTitles;
	}

	private DataItem[][] getTableDataFormatFromGenelistData(Object[][] data, int analysisDataColNum) {
		if (data == null){
			System.out.println("No data is retrieved");
			return null;
		}
//		String[]ids, double[][]analysisData, String[][]ontologies;		
	    
		int colNum1 = data[0].length;
		int colNum2 = genelistSearchLinks.size() - 3;	 //searchlinks for GO & OMIM & MRC2 are removed for the time being
		int colNum = colNum1 + colNum2;
		int rowNum = data.length;
	    
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		
		
		for(int row=0; row<rowNum; row++) {
			
			int col=0;
		
			tableData[row][col++] = new DataItem(data[row][0]); // Affy ID
		
			//------- analysis data ----------
			for (int i=0; i<analysisDataColNum; i++) {
				String value = String.format("%.2f", data[row][col]);
				tableData[row][col++] = new DataItem(value);
			}

			//------- ontologies ----------
			String geneSymbol = (String)data[row][analysisDataColNum+1];
			if (!geneSymbol.equals("-"))  //Description 
				tableData[row][col++] = new DataItem(geneSymbol, geneSymbol, "../pages/gene.jsf?gene="+geneSymbol, 4); //url for Gudmap ISH
			tableData[row][col] = new DataItem(data[row][col]);	//Synonym
			col++;
			if (!geneSymbol.equals("-"))  //Gene symbol 
				tableData[row][col++] = new DataItem(geneSymbol, geneSymbol, "../pages/gene.jsf?gene="+geneSymbol, 4); //url for Gudmap ISH
			for (int i=col; i<colNum1; i++) //rest of the ontologies
				tableData[row][i] = new DataItem(data[row][i]);
			col=colNum1;
			
			//------- search links ----------
			for(int i=0; i<colNum2; i++) {   //search links -- not including GO & OMIM  & MRC2
				SearchLink link = (SearchLink)genelistSearchLinks.get(i);
				/*	these ontologies doesn't contain an omim id 				
				if ("OMIM".equals(link.getName())) {
					String omimId = (String)ontologies[row][9]; //OMIM ID
					if(omimId != null)
						tableData[row][col+colOffset] = new DataItem(link.getName(), link.getName() , link.getUrl(omimId), 4);  //Pass omim id
					else
						tableData[row][col+colOffset] = new DataItem("-");
				}
				else          
* /
				if (i == 3) 
					//Gumap ISH link DAO needs to provide with ontology number of gudmap entries & omim id
					/*			
								int entriesNum = ((Integer)data[row][colNum1]).intValue();
								String suffix = " entr" + ((entriesNum==1)? "y" : "ies" );
								String title = ((entriesNum==0)?"no" : String.valueOf(entriesNum)) + suffix;
								if(entriesNum > 0)
//									tableData[row][colNum1] = new DataItem(title, title, "../pages/ish_selected_submissions.jsf?output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
//									tableData[row][colNum1] = new DataItem(title, title, "../pages/ish_submissions.jsf?queryType=geneQueryISH&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
									tableData[row][colNum1] = new DataItem(title, title, "../pages/ish_gene_submissions.jsf?queryType=geneQueryISH&ignoreExpression=true&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
								else
									tableData[row][colNum1] = new DataItem(title);
					* /			
					tableData[row][col] = new DataItem("MRC", "MRC", "ish_gene_submissions.jsf?queryType=geneQueryISH&ignoreExpression=true&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
				else 
					tableData[row][col] = new DataItem(link.getName(), link.getName() , link.getUrl(geneSymbol), 4);  //Pass gene symbol
				col++;
			}
		}
		
		return tableData;
	}
*/
}	
	
	

