package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.FocusForAllDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.summary.DBSummary;

import java.sql.Connection;
import java.util.ResourceBundle;

public class FocusDBSummaryAssembler {
    private boolean debug = false;
    public FocusDBSummaryAssembler() {
	if (debug)
	    System.out.println("FocusDBSummaryAssembler.constructor");
    }
	/**
	 * 
	 * @return
	 */
	public DBSummary getData(String[] emapids) {
		
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
			
			
	        // get number of public ish sumbissions
			int numberOfPublicSubmissionsISH = focusForAllDAO.findNumberOfPublicSubmissionISH(emapids);
										
	        // get number of public array sumbissions
			int numberOfPublicSubmissionsArray = focusForAllDAO.findNumberOfPublicSubmissionArray(emapids);
			
			int numberOfPublicSubmissionsIHC = focusForAllDAO.findNumberOfPublicSubmissionIHC(emapids);
					
		    // added by xingjun - 28/08/2008 - transgenic data - start
		    int numberOfPublicSubmissionsTG = focusForAllDAO.findNumberOfPublicSubmissionTG(emapids);
		    // added by xingjun - 28/08/2008 - transgenic data - end
		    
		    int numberOfPublicSubmissionsWISH = focusForAllDAO.findNumberOfPublicSubmissionISHTypes(emapids,"wholemount");
		    int numberOfPublicSubmissionsSISH= focusForAllDAO.findNumberOfPublicSubmissionISHTypes(emapids,"section");
		    int numberOfPublicSubmissionsOPT = focusForAllDAO.findNumberOfPublicSubmissionISHTypes(emapids,"opt-wholemount");
	
		    ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	
		    // get database host name
		    String host = bundle.getString("host");
		    String databaseHost = host.substring(host.lastIndexOf("//")+2,host.length()-1);
		    
		    /*String host = "";
		    String databaseHost = "";
		    try {
		    	host = conn.getMetaData().getURL();
		    	databaseHost = host.substring(host.lastIndexOf("//")+2,host.lastIndexOf(":"));
		    } catch(Exception e) {
		    	e.printStackTrace();
		    }*/
		    
			// get project name
		    String project = bundle.getString("project");
		    
		    // get total number of ihc genes
		    int numberOfPublicGenesISH = focusForAllDAO.findNumberOfPublicGenes("ISH", emapids);
		    
		    // get total number of ihc genes
		    int numberOfPublicGenesIHC = focusForAllDAO.findNumberOfPublicGenes("IHC", emapids);
		    
		    // get total number of tg genes
		    int numberOfPublicGenesTG = focusForAllDAO.findNumberOfPublicGenes("TG", emapids);
		    
		    int numberOfPublicGenesWISH = focusForAllDAO.findNumberOfPublicGenes("wholemount", emapids);
		    int numberOfPublicGenesSISH = focusForAllDAO.findNumberOfPublicGenes("section", emapids);
		    int numberOfPublicGenesOPT = focusForAllDAO.findNumberOfPublicGenes("opt-wholemount", emapids);
//			int numberOfSequences = focusForAllDAO.findTotalNumberOfSequences();
			
			
			/** ---complement summary object--- */
		    DBSummary dbSummary = new DBSummary();
	
			dbSummary.setTotIshGenes(Integer.toString(numberOfPublicGenesISH));
			dbSummary.setTotIhcGenes(Integer.toString(numberOfPublicGenesIHC));
			dbSummary.setTotTgGenes(Integer.toString(numberOfPublicGenesTG));
			dbSummary.setTotWishGenes(Integer.toString(numberOfPublicGenesWISH));
			dbSummary.setTotSishGenes(Integer.toString(numberOfPublicGenesSISH));
			dbSummary.setTotOptGenes(Integer.toString(numberOfPublicGenesOPT));
			
			dbSummary.setTotAvailIshSubs(Integer.toString(numberOfPublicSubmissionsISH));
			dbSummary.setTotAvailArraySubs(Integer.toString(numberOfPublicSubmissionsArray));
			dbSummary.setTotalAvailableSubmissionsIHC(Integer.toString(numberOfPublicSubmissionsIHC));
			// added by xingjun - 28/08/2008
			dbSummary.setTotalAvailableSubmissionsTG(Integer.toString(numberOfPublicSubmissionsTG));
			dbSummary.setTotalAvailableSubmissionsWISH(Integer.toString(numberOfPublicSubmissionsWISH + numberOfPublicSubmissionsOPT));
			dbSummary.setTotalAvailableSubmissionsSISH(Integer.toString(numberOfPublicSubmissionsSISH));
			dbSummary.setTotalAvailableSubmissionsOPT(Integer.toString(numberOfPublicSubmissionsOPT));
//			dbSummary.setTotalAvailableSequences(Integer.toString(numberOfSequences));
	
			dbSummary.setDatabaseServer(databaseHost);
			dbSummary.setProject(project);
			/** ---return the composite value object---  */
			return dbSummary;
		}
		catch(Exception e){
			System.out.println("FocusDBSummaryAssembler::getData !!!");
			return null;
		}	
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
}
