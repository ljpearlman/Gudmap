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
	
    protected boolean debug = false;

	protected String masterTableId;
	protected String platformId;

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
		try{
			ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
			HeatmapData expressions = arrayDAO.getExpressionByGivenProbeSetIds(probeSetIds, masterTableId, genelistId);
			return expressions;
		}
		catch(Exception e){
			System.out.println("MasterTableAssembler::retrieveProbelistExpressions failed !!!");
			return null;
		}
		finally{
	        DBHelper.closeJDBCConnection(conn);
		}
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
		try{
			GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
			String[][] annotations = genelistDAO.getAnnotationByProbeSetIds(probeSetIds);
			return annotations;
		}
		catch(Exception e){
			System.out.println("MasterTableAssembler::retrieveProbelistExpressions failed !!!");
			return null;
		}
		finally{
	        DBHelper.closeJDBCConnection(conn);
		}
	}

	//*************************************** Retrieve Titles ******************************************
	/**
	 * @author xingjun - 03/23/3008
	 * @return
	 */
	public static BrowseTableTitle[] retrieveExpressionTitles(String masterTableId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
			BrowseTableTitle[] expressionTitles = genelistDAO.getMasterTableExpressionTitles(masterTableId);
	        return expressionTitles;
		}
		catch(Exception e){
			System.out.println("MasterTableAssembler::retrieveExpressionTitles failed !!!");
			return null;
		}
		finally{
	        DBHelper.closeJDBCConnection(conn);
		}
	}

	/**
	 * @author xingjun - 03/12/2008
	 * @return
	 */
	public static BrowseTableTitle[] retrieveAnnotaionTitles(String platformId) {
	    BrowseTableTitle[] ret = new BrowseTableTitle[6];

	    ret[0] = new BrowseTableTitle();
	    ret[0].setTitle("Gene Symbol");
	    ret[1] = new BrowseTableTitle();
	    ret[1].setTitle("Probe Seq ID");
	    ret[2] = new BrowseTableTitle();
	    ret[2].setTitle("MGI Gene ID");
	    ret[3] = new BrowseTableTitle();
	    ret[3].setTitle("Entrez Gene ID");
	    ret[4] = new BrowseTableTitle();
	    ret[4].setTitle("Human Ortholog Symbol");
	    ret[5] = new BrowseTableTitle();
	    ret[5].setTitle("Human Ortholog Entez ID");

	    return ret;
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
