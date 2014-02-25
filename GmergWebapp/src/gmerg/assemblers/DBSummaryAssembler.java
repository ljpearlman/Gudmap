/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ResourceBundle;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.summary.DBSummary;

/**
 * @author xingjun
 *
 */
public class DBSummaryAssembler {
    private boolean debug = false;
    public DBSummaryAssembler() {
	if (debug)
	    System.out.println("DBSummaryAssembler.constructor");
    }
	/**
	 * modified by xingjun - 02/02/2009
	 * get public genes for ish, ihc, and tg data
	 * @return
	 */
	public DBSummary getData() {
		
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);

			// get total number of ish public genes
			int numberOfPublicGenesISH = ishDAO.findNumberOfPublicGenes("ISH");
			
		    // get total number of ihc public genes
		    int numberOfPublicGenesIHC = ishDAO.findNumberOfPublicGenes("IHC");
		    
		    // get total number of tg public genes
		    int numberOfPublicGenesTG = ishDAO.findNumberOfPublicGenes("TG");
			
			// get total number of ish submissions
			int totalNumberOfSubmissionsISH = ishDAO.findTotalNumberOfSubmissionISH();
			
	        // get number of public ish sumbissions
			int numberOfPublicSubmissionsISH = ishDAO.findNumberOfPublicSubmissionISH();
			
			// get number of 'editing-in-progress' submissions of ish type
			int numberOfEditSubmissionsISH = totalNumberOfSubmissionsISH - numberOfPublicSubmissionsISH;
			
			// get total number of ihc submissions
			int totalNumberOfSubmissionsIHC = ishDAO.findTotalNumberOfSubmissionIHC();
			
			// get number of public ihc sumbissions
			int numberOfPublicSubmissionsIHC = ishDAO.findNumberOfPublicSubmissionIHC();
			
			// get number of 'editing-in-progress' submissions of ihc type
			int numberOfEditSubmissionsIHC = totalNumberOfSubmissionsIHC - numberOfPublicSubmissionsIHC;
			
			// get total number of array submissions
			int totalNumberOfSubmissionsArray = ishDAO.findTotalNumberOfSubmissionArray();
				
	        // get number of public array sumbissions
			int numberOfPublicSubmissionsArray = ishDAO.findNumberOfPublicSubmissionArray();
			
			// get number of 'editing-in-progress' submissions of array type
			int numberOfEditSubmissionsArray = totalNumberOfSubmissionsArray - numberOfPublicSubmissionsArray;
			
			// get last editorial update date
			String lastEdtorialUpdateDate = ishDAO.findLastEditorialUpdateDate();
			
			// get last software update date
			String lastSoftwareUpdateDate = ishDAO.findLastSoftwareUpdateDate();
			
			// get last database entry date
			String lastDatabaseEntryDate = ishDAO.findLastEntryDateInDB();
			
			// get application version info - xingjun 28/07/2009
			String applicationVersion = ishDAO.getApplicationVersion();
			
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
		    
		    // added by xingjun - 26/08/2008 - transgenic data - start
		    int numberOfPublicSubmissionsTG = ishDAO.findNumberOfPublicSubmissionTG();
		    // added by xingjun - 26/08/2008 - transgenic data - end
		    
			/** ---complement summary object--- */
		    DBSummary dbSummary = new DBSummary();
			dbSummary.setTotIshGenes(Integer.toString(numberOfPublicGenesISH));
			dbSummary.setTotAvailIshSubs(Integer.toString(numberOfPublicSubmissionsISH));
			dbSummary.setTotEditIshSubs(Integer.toString(numberOfEditSubmissionsISH));
			dbSummary.setTotAvailArraySubs(Integer.toString(numberOfPublicSubmissionsArray));
			dbSummary.setTotEditArraySubs(Integer.toString(numberOfEditSubmissionsArray));
			dbSummary.setLastEditorUpdate(lastEdtorialUpdateDate);
			dbSummary.setLastSoftwrUpdate(lastSoftwareUpdateDate);
			dbSummary.setLastEntryDate(lastDatabaseEntryDate);
			dbSummary.setApplicationVersion(applicationVersion); // added by xingjun - 28/07/2009
			dbSummary.setDatabaseServer(databaseHost);
			dbSummary.setProject(project);
			dbSummary.setTotalAvailableSubmissionsIHC(Integer.toString(numberOfPublicSubmissionsIHC));
			dbSummary.setTotalEditSubmissionsIHC(Integer.toString(numberOfEditSubmissionsIHC));
			// added by xingjun - 26/08/2008 - transgenic data - start
			dbSummary.setTotalAvailableSubmissionsTG(Integer.toString(numberOfPublicSubmissionsTG));
			// added by xingjun - 26/08/2008 - transgenic data - start
			
			dbSummary.setTotIhcGenes(Integer.toString(numberOfPublicGenesIHC));
			dbSummary.setTotTgGenes(Integer.toString(numberOfPublicGenesTG));

			return dbSummary;
		}
		catch(Exception e){
			System.out.println("DBSummaryAssembler::getData failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

}
