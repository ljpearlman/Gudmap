/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.ArrayDAO;
import gmerg.db.ArrayDevDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.Transgenic;
import gmerg.entities.submission.array.ArraySubmission;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.entities.submission.array.Platform;
import gmerg.entities.submission.array.Sample;
import gmerg.entities.submission.array.Series;
import gmerg.entities.submission.array.SupplementaryFile;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class ArraySubmissionAssembler {
    private boolean debug = false;
    public ArraySubmissionAssembler() {
	if (debug)
	    System.out.println("ArraySubmissionAssembler.constructor");
    }
	/**
	 * get array submission info excluding gene list info
	 * 
	 * <p>modifiec by xingjun - 09/11/2009 - put transgenic data into array data structure</p>
	 * @param accessionId
	 * @return
	 */
	public ArraySubmission getData(String accessionId) {
        /** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get basic submission info and create submission object
		Submission submission = ishDAO.findSubmissionById(accessionId);
		
		// get supplementary file info
		SupplementaryFile supplementaryFiles = arrayDAO.findSupplementaryFileInfoBySubmissionId(accessionId);
		
		// get pi info
		Person pi = ishDAO.findPIBySubmissionId(accessionId);
		
		// xingjun - 20/06/2011 - some of the submission entries might have multiple pis
		Person[] pis = ishDAO.findPIsBySubmissionId(accessionId);
		
		// get submitter info
		Person submitter = ishDAO.findSubmitterBySubmissionId(accessionId);
		
		// get sample info
		Sample sample = arrayDAO.findSampleBySubmissionId(accessionId);
		
		// get series info
		Series series = arrayDAO.findSeriesBySubmissionId(accessionId);
		
		// get platform info
		Platform platform = arrayDAO.findPlatformBySubmissionId(accessionId);
		
		// get specimen info
		Specimen specimen = ishDAO.findSpecimenBySubmissionId(accessionId);
		
		ArrayList images = arrayDevDAO.findOriginalImagesById(accessionId);
		
		// get transgenic info - xingjun - 22/07/2009
//		Transgenic transgenic = arrayDAO.getTransgenicInfoBySubmissionId(accessionId);
		Transgenic[] transgenics = arrayDAO.getTransgenicInfoBySubmissionIdBak(accessionId);
		
		/** ---assemble array submission object---  */
		ArraySubmission arraySubmission = new ArraySubmission();
		arraySubmission.setAccID(submission.getAccID());
		arraySubmission.setStage(submission.getStage());
		arraySubmission.setArchiveId(submission.getArchiveId()); // added by xingjun - 03/11/2010
		arraySubmission.setOriginalImages(images);
//		arraySubmission.setFilesLocation((String)supplementaryFiles.get(0));
//		arraySubmission.setCelFile((String)supplementaryFiles.get(1));
//		arraySubmission.setChpFile((String)supplementaryFiles.get(2));
//		arraySubmission.setRptFile((String)supplementaryFiles.get(3));
//		arraySubmission.setExpFile((String)supplementaryFiles.get(4));
//		arraySubmission.setTxtFile((String)supplementaryFiles.get(5));

		arraySubmission.setFilesLocation(supplementaryFiles.getFilesLocation());
		arraySubmission.setCelFile(supplementaryFiles.getCelFile());
		arraySubmission.setChpFile(supplementaryFiles.getChpFile());
		arraySubmission.setRptFile(supplementaryFiles.getRptFile());
		arraySubmission.setExpFile(supplementaryFiles.getExpFile());
		arraySubmission.setTxtFile(supplementaryFiles.getTxtFile());
		arraySubmission.setPrincipalInvestigator(pi);
		arraySubmission.setPrincipalInvestigators(pis);
		arraySubmission.setSubmitter(submitter);
		arraySubmission.setSample(sample);
		arraySubmission.setSeries(series);
		arraySubmission.setPlatform(platform);
		arraySubmission.setSpecimen(specimen);
		
		// added by xingjun - 22/07/2009
//		arraySubmission.setTransgenic(transgenic);
		// xingjun - 09/11/2009
		arraySubmission.setTransgenics(transgenics);
		if (transgenics != null && transgenics.length > 1) {
			arraySubmission.setMultipleTransgenics(true);
		} else {
			arraySubmission.setMultipleTransgenics(false);
		}

        // added by Bernie - 23/09/2010
        String tissue = arrayDAO.findTissueBySubmissionId(accessionId);
        arraySubmission.setTissue(tissue);
        
		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		ishDAO = null;
		
		/** ---return the array submission  */
		return arraySubmission;
	}
	
	/**
	 * get total number of gene items
	 * 
	 * @param accessionId
	 * @return
	 */
	public String getTotalGeneListItems(String accessionId) {
		
		String totalGeneListItems = new String("");
		
		/** ---get the data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get the value
        totalGeneListItems += arrayDAO.getTotalNumberOfGeneListItemsBySubmissionId(accessionId);
		
		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the gene list info---  */
		return totalGeneListItems;
	}
	
	/**
	 * Method to query the DAO to retrieve the row number of the first occurrence of a particular gene in a result set
	 * @param geneSymbol - a user specified gene symbol
	 * @return rowNum - the row number of the first occurrence of a specific gene symbol
	 */
	public int getRowNumberOfFirstOccurrenceOfGene(String accessionId, String geneSymbol){
		
		/** ---get the data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get page number from DAO
		int pageNum = arrayDAO.getRowNumOf1stOccurrenceOfGeneInArrayGeneList(accessionId, geneSymbol);
		
		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the gene list info---  */
		return pageNum;
	}

}
