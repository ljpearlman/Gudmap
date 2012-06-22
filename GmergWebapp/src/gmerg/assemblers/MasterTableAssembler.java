/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.GenelistDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.BrowseTableTitle;
import gmerg.entities.HeatmapData;
import gmerg.model.HeatmapDisplayTransform;
import gmerg.utils.DbUtility;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class MasterTableAssembler {
	
    private boolean debug = false;

	private String masterTableId;
	private String platformId;

	public MasterTableAssembler() {
	    this((String)null);
	}
	
	public MasterTableAssembler(String masterTableId) {
	if (debug)
	    System.out.println("MasterTableAssembler.constructor");

		this.masterTableId = masterTableId;
		if (masterTableId != null)
			platformId = DbUtility.getMasterTablePlatformId(masterTableId);
		else {
			platformId = "GPL1261";
			masterTableId = "1";
		}
	}
	
	//************************************* Retrieve DataSet ******************************************
/*	
	public DataSet retrieveGeneExpressionDataSet(String geneSymbol) {
		ArrayList<String> probeIds = retrieveProbeIdsBySymbol(geneSymbol);
		return retrieveProbelistExpressionDataSet(probeIds);
	}
	
	public DataSet retrieveGenelistExpressionDataSet(String genelistId) {
		ArrayList<String> probeIds = DbUtility.retrieveGenelistProbeIds(genelistId, masterTableId);
		return retrieveProbelistExpressionDataSet(probeIds);
	}
	
	public DataSet retrieveProbelistExpressionDataSet1(ArrayList<String> probeIds) {
		double[][] data = retrieveProbelistExpressions(probeIds);
		BrowseTableTitle[] expressionHeaders = retrieveExpressionTitles(masterTableId);
		String[][] arrayAnnotation = new String[expressionHeaders.length][1];
		for(int i=0; i<expressionHeaders.length; i++)
			arrayAnnotation[i][0] = expressionHeaders[i].getGroupName()+ "_" + expressionHeaders[i].getTitle();
		String[] expressionIds = new String[probeIds.size()];
		expressionIds = probeIds.toArray(expressionIds);
		String[][] geneAnnotations = new String[expressionIds.length][2];
		
		String[][] allOntologies = retrieveProbelistAnnotations(probeIds);
		for(int i=0; i<expressionIds.length; i++) { 
			geneAnnotations[i][0] = expressionIds[i];
			String geneSymbols = allOntologies[i][2]; //should be retreived from database (now it is taken from ontology columns)
			if (geneSymbols==null || geneSymbols=="")
				geneSymbols = "-";
			else
				geneSymbols = geneSymbols.replace("///", ",");
			geneAnnotations[i][1] = geneSymbols;  
		}
		DataSet dataSet = new DataSet(data, null, data.length, data[0].length, geneAnnotations, arrayAnnotation);

		String[] arrayHeader = {"Sample ID"};
		String[] geneHeader =  {"ProbeID", "GeneSymbol"};
		boolean[] geneHeadersClickable = {false, true};
		dataSet.setGeneHeaders(geneHeader);
		dataSet.setArrayHeaders(arrayHeader);
		dataSet.setGeneHeadersClickable(geneHeadersClickable);

		return dataSet;
	}
*/
	
	//************************************ Retrieve Expressions ****************************************
	public HeatmapData retrieveGeneExpressions(String geneSymbol) {
		ArrayList<String> probeIds = new ArrayList<String>();
		probeIds = retrieveProbeIdsBySymbol(geneSymbol);
		return retrieveProbelistExpressions(probeIds, null);
	}

	public HeatmapData retrieveGenelistExpressions(String genelistId) {
		ArrayList<String> probeIds = new ArrayList<String>();
		probeIds = DbUtility.retrieveGenelistProbeIds(genelistId, masterTableId);
		return retrieveProbelistExpressions(probeIds, genelistId);
	}
	
	/**
	 * @author xingjun - 21/11/2008
	 * @param probeSetIds
	 * @return expression info in a two-dimension double array
	 */
	public HeatmapData retrieveProbelistExpressions(ArrayList<String> probeSetIds, String genelistId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
        // get data from database
		HeatmapData expressions = arrayDAO.getExpressionByGivenProbeSetIds(probeSetIds, masterTableId, genelistId);
		
        // release db resources
        DBHelper.closeJDBCConnection(conn);
        arrayDAO = null;

        // return the value object
		return expressions;
	}

	//************************************ Retrieve Annotations ****************************************
	public String[][] retrieveGeneAnnotations(String geneSymbol) {
		ArrayList probeIds = retrieveProbeIdsBySymbol(geneSymbol);
		return retrieveProbelistAnnotations(probeIds);
	}
	
	public String[][] retrieveGenelistAnnotations(String genelistId) {
		ArrayList<String> probeIds = new ArrayList<String>();
		probeIds = DbUtility.retrieveGenelistProbeIds(genelistId, masterTableId);
		return retrieveProbelistAnnotations(probeIds);
	}
	
	/**
	 * @author xingjun - 09/12/2008
	 * @param probeSetIds
	 * @return
	 */
	public String[][] retrieveProbelistAnnotations(ArrayList probeSetIds) {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		String[][] annotations = 
			genelistDAO.getAnnotationByProbeSetIds(probeSetIds);
		
		return annotations;
	}

	//*************************************** Retrieve Titles ******************************************
	/**
	 * @author xingjun - 03/23/3008
	 * @return
	 */
	public static BrowseTableTitle[] retrieveExpressionTitles(String masterTableId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);

        // get data from database
		BrowseTableTitle[] expressionTitles = genelistDAO.getMasterTableExpressionTitles(masterTableId);
		
        // release db resources
        DBHelper.closeJDBCConnection(conn);

        // return the value object
        return expressionTitles;
	}

	/**
	 * @author xingjun - 03/12/2008
	 * @return
	 */
	public static BrowseTableTitle[] retrieveAnnotaionTitles(String platformId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);

        // get data from database
		BrowseTableTitle[] annotationTitles = genelistDAO.getMasterTableAnnotationTitles(platformId);
		
        // release db resources
        DBHelper.closeJDBCConnection(conn);

        // return the value object
		return annotationTitles;
	}


	//************************************* Retrieve Probe IDs *****************************************
	public ArrayList<String> retrieveProbeIdsBySymbol(String geneSymbol) {
		return DbUtility.retrieveGeneProbeIds(geneSymbol, platformId);
	}
		
	//**************************************************************************************************
	public static HeatmapDisplayTransform getDisplayTransform() {
//		return new HeatmapDisplayTransform(10.0, 1.0, 1.0, 1.0);
//		return new HeatmapDisplayTransform(2.5, 10.0, 10.0, 10.0, 8.5);
		return new HeatmapDisplayTransform(2.4, 2.4, 2.4, 0);
	}
	
}
