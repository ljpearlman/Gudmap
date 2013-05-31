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
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.Allele;
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
		if (submission == null)
			return null;
		
		// get supplementary file info
		SupplementaryFile supplementaryFiles = arrayDAO.findSupplementaryFileInfoBySubmissionId(accessionId);
		
		// get pi info
		Person[] pis = ishDAO.findPIsBySubmissionId(accessionId);

		// get submitter info
		Person submitter = ishDAO.findSubmitterBySubmissionId(accessionId);
		
		// get sample info
		Sample sample = arrayDAO.findSampleBySubmissionId(accessionId);
		
		// get series info
		Series series = arrayDAO.findSeriesBySubmissionId(accessionId);
		
		// get platform info
		Platform platform = arrayDAO.findPlatformBySubmissionId(accessionId);
		
		// array submission does not have specimen so get allele if any
		Allele[] allele = ishDAO.findAlleleBySubmissionId(accessionId);
		
		ArrayList images = arrayDevDAO.findOriginalImagesById(accessionId);
		
		/** ---assemble array submission object---  */
		ArraySubmission arraySubmission = new ArraySubmission();
		arraySubmission.setAccID(submission.getAccID());
		arraySubmission.setStage(submission.getStage());
		arraySubmission.setArchiveId(submission.getArchiveId()); // added by xingjun - 03/11/2010
		arraySubmission.setOriginalImages(images);

		arraySubmission.setFilesLocation(supplementaryFiles.getFilesLocation());
		arraySubmission.setCelFile(supplementaryFiles.getCelFile());
		arraySubmission.setChpFile(supplementaryFiles.getChpFile());
		arraySubmission.setRptFile(supplementaryFiles.getRptFile());
		arraySubmission.setExpFile(supplementaryFiles.getExpFile());
		arraySubmission.setTxtFile(supplementaryFiles.getTxtFile());
		arraySubmission.setPrincipalInvestigators(pis);
		arraySubmission.setSubmitter(submitter);
		arraySubmission.setSample(sample);
		arraySubmission.setSeries(series);
		arraySubmission.setPlatform(platform);
		arraySubmission.setAllele(allele);
		
		arraySubmission.setPublicFlag(submission.getPublicFlag());
		arraySubmission.setDeletedFlag(submission.getDeletedFlag());
		

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
