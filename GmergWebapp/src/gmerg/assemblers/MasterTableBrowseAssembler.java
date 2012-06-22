/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.GenelistDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.BrowseTableTitle;
import gmerg.entities.HeatmapData;
import gmerg.entities.submission.array.SearchLink;
import gmerg.utils.DbUtility;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryCollectionAssembler;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 * xingjun - 06/06/2011 - replace string 'GUDMAP' with projectString (could be GUDMAP or EuReGene) 
 *
 */
public class MasterTableBrowseAssembler extends OffMemoryCollectionAssembler{
    private boolean debug = false;

    private ArrayList<SearchLink> genelistSearchLinks;
	private BrowseTableTitle[] expressionTitles;
	private BrowseTableTitle[] annotationTitles;
	private String masterTableId;
	private String genelistId;
	private String projectString = ResourceBundle.getBundle("configuration").getString("project").trim();

	
	//********************************* Generic Assembler Methods *************************************
	public MasterTableBrowseAssembler (HashMap params, CollectionBrowseHelper helper) {
    	super(params, helper);
	if (debug)
	    System.out.println("MasterTableBrowseAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
		expressionTitles = (BrowseTableTitle[])getDataRetrivalParams().get("expressionTitles");
		annotationTitles = (BrowseTableTitle[])getDataRetrivalParams().get("annotationTitles");
		genelistSearchLinks = DbUtility.retrieveSearchLinks();
		masterTableId = getParam("masterTableId");
		genelistId = getParam("genelistId");
	}

	/**
	 * @author xingjun - 04/12/2008
	 */
	public DataItem[][] retrieveData(int columnId, boolean ascending, int offset, int num) {
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		
        // get data from database
		ArrayList<String> onePageIds = new ArrayList<String>();
		if (columnId == 0) {
			Collections.sort(ids);
			if (!ascending) 
				Collections.reverse(ids);
		}
		
//		if (columnId > 0) 
//			onePageIds = arrayDAO.getExpressionSortedByGivenProbeSetIds(ids, columnId, ascending, offset, num);
//		else
			
		for(int i=0; i<num && i+offset<ids.size(); i++)
			onePageIds.add(ids.get(i+offset));
		
//		double[][] expressions = arrayDAO.getExpressionByGivenProbeSetIds(ids, columnId, ascending, offset, num);
//		String[][] annotations = genelistDAO.getAnnotationByProbeSetIds(ids, columnId, ascending, offset, num);
		HeatmapData expressions = arrayDAO.getExpressionByGivenProbeSetIds(onePageIds, masterTableId, genelistId);

		String[][] annotations = genelistDAO.getAnnotationByProbeSetIds(onePageIds);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
        arrayDAO = null;
        genelistDAO = null;
        
        
		// return value
		return getTableDataFormatFromMastertableData(onePageIds, expressions ,annotations);
	}
	
	/**
	 * modified by xingjun - 15/05/2009
	 * - separate screen tips from titles - they used to be the same
	 */
	public HeaderItem[] createHeader() {
	    if (debug)
		System.out.println("MasterTableBrowseAssembler - createHeader");
		HeaderItem[] headers = new HeaderItem[expressionTitles.length + annotationTitles.length + genelistSearchLinks.size() + 4];
		headers[0] = new HeaderItem("Probe Id", false);
		headers[1] = new HeaderItem("Median", false);
		headers[2] = new HeaderItem("StdDev", false);
		// Bernie 12/4/2011 - Mantis 540 - add header for Gene Sybbol and change offset from 3 to 4
		headers[3] = new HeaderItem(annotationTitles[0].getTitle(), false);
		int offset = 4; //3;
		HeaderItem item = null;
		String screenTip = null;
		String title = null;

		for (int i=0; i<expressionTitles.length; i++) {
			screenTip = expressionTitles[i].getGroupName()+expressionTitles[i].getTitle(); // used for screen tips
			title = (expressionTitles[i].getDescription() + "_" + expressionTitles[i].getTitle()).replaceAll("\\s", ""); // used for vertical image title display
			item  = new HeaderItem(screenTip, false, 1, "../dynamicImages/title_"+title + ".jpg?masterTable=");
			if (debug)
			    System.out.println("MasterTableBrowseAssembler.createHeader title = "+item.getTitle()+" image name = "+item.getImageName());

			headers[i+offset] = item;
		}
		offset += expressionTitles.length;
		
		// Bernie 12/4/2011 - Mantis 540 - mod loop to start at 1 rather than 0 to take account of moving Gene Symbol
		for(int i=1; i<annotationTitles.length; i++)
			headers[i+offset-1] = new HeaderItem(annotationTitles[i].getTitle(), false);
		offset += annotationTitles.length-1;
		
//		headers[offset] = new HeaderItem("GUDMAP-ISH", false);
		headers[offset] = new HeaderItem(projectString+"-ISH", false);
		offset++;
		
		for(int i=0; i<genelistSearchLinks.size(); i++)
			headers[i+offset] = new HeaderItem(genelistSearchLinks.get(i).getName(), false);

		return headers;
	}
	
	public DataItem[][] getTableDataFormatFromMastertableData(ArrayList<String> onePageIds, HeatmapData data, String[][] annotations) {
	    if (debug)
		System.out.println("MasterTableBrowseAssembler - getTableDataFormatFromMastertableData");
		
		double[][] expressions = (data!=null)? data.getExpression() : null;
		if (data==null || expressions == null || annotations == null){
			System.out.println("No data is retrieved");
			return null;
		}
		
		double[] median = data.getMedian();
		double[] stdDev = data.getStdDev();
		int colNum1 = expressions[0].length + annotations[0].length + 2; // +2 is for median and stdDev columns 
		int colNum2 = genelistSearchLinks.size();
		int colNum = colNum1 + colNum2 + 2;
		int rowNum = onePageIds.size();
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		
		for(int row=0; row<rowNum; row++) {
			int col=0;			
			tableData[row][col++] = new DataItem(onePageIds.get(row)); //Probe ID
		
			tableData[row][col++] = new DataItem(median[row]); // median
			
			tableData[row][col++] = new DataItem(stdDev[row]); // standard deviation
			
			// Bernie 12/4/2011 - Mantis 540
			String geneSymbol = annotations[row][0];
			tableData[row][col++] = new DataItem(geneSymbol);	// Gene Symbol
						
			//------- analysis data ----------
			for (int i=0; i<expressions[0].length; i++) {
				String value = String.format("%.2f", expressions[row][i]);
//				System.out.println("===tableData["+row+"]["+col+"] = "+value);
				tableData[row][col++] = new DataItem(value);				
			}

			//------- ontologies ----------
			// Bernie 12/4/2011 - Mantis 540 - mod annotations[row][0] to annotations[row][1]
			String geneDescription = (String)annotations[row][1];
			if (geneDescription.equals("-") || geneDescription.equals(""))  // Gene Description 
				tableData[row][col++] = new DataItem(geneDescription);	
			else
				tableData[row][col++] = new DataItem(geneDescription, geneDescription, "../pages/gene.jsf?gene="+geneDescription, 4); //url for Gudmap ISH
			
			for (int i=2; i<annotations[0].length; i++) //rest of the ontologies 
				tableData[row][col++] = new DataItem(annotations[row][i]);
			

			//------- search links ----------
			// Bernie 12/4/2011 - Mantis 540 - changed geneSymbol="+geneDescription to geneSymbol="+geneSymbol
//			tableData[row][col++] = new DataItem("GUDMAP", "Click to view GUDMAP ISH", "ish_gene_submissions.jsf?queryType=geneQueryISH&ignoreExpression=true&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
			tableData[row][col++] = new DataItem(projectString, "Click to view "+projectString+" ISH", "ish_gene_submissions.jsf?queryType=geneQueryISH&ignoreExpression=true&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
			for(int i=0; i<colNum2; i++) {   //search links -- not including GO & OMIM  & MRC2
				SearchLink link = (SearchLink)genelistSearchLinks.get(i);
//				if (i == 3) 
//					tableData[row][col++] = new DataItem("MRC", "MRC", "ish_gene_submissions.jsf?queryType=geneQueryISH&ignoreExpression=true&output=gene&inputType=symbol&criteria=equals&geneSymbol="+geneSymbol, 4); //url for Gudmap ISH
				// Bernie 12/4/2011 - Mantis 540 - changed link.getUrl(geneDescription) to link.getUrl(geneSymbol)
				tableData[row][col++] = new DataItem(link.getName(), link.getName() , link.getUrl(geneSymbol), 4);  //Pass gene symbol
			}
		}
		
		return tableData;
	}


}
