/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.NGDDAO;
import gmerg.db.NGDDevDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.Allele;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.entities.submission.nextgen.Protocol;
import gmerg.entities.submission.nextgen.DataProcessing;
import gmerg.entities.submission.array.SupplementaryFile;
import gmerg.entities.submission.nextgen.NGDSample;
import gmerg.entities.submission.nextgen.NGDSeries;
import gmerg.entities.submission.nextgen.NGDSubmission;
import gmerg.assemblers.ISHSubmissionAssembler;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class NGDSubmissionAssembler {
    private boolean debug = false;
    public NGDSubmissionAssembler() {
	if (debug)
	    System.out.println("ArraySubmissionAssembler.constructor");
    }
	/**
	 * get array submission info excluding gene list info
	 * 
	 * @param accessionId
	 * @return
	 */
	public NGDSubmission getData(String accessionId) {

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			NGDDAO ngdDAO = MySQLDAOFactory.getNGDDAO(conn);
			NGDDevDAO ngdDevDAO = MySQLDAOFactory.getNGDDevDAO(conn);
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			// get basic submission info and create submission object
			Submission submission = ishDAO.findSubmissionById(accessionId);
			if (submission == null)
				return null;
			
			// get supplementary file info
			SupplementaryFile supplementaryFiles = ngdDAO.findSupplementaryFileInfoBySubmissionId(accessionId);
			
			// get pi info
			Person[] pis = ishDAO.findPIsBySubmissionId(accessionId);
	
			// get submitter info
			Person submitter = ishDAO.findSubmitterBySubmissionId(accessionId);
			
			// get author info
			String authors  = ishDAO.findAuthorBySubmissionId(accessionId);
			
			// get sample info
			NGDSample sample = ngdDAO.findSampleBySubmissionId(accessionId);
			
			Protocol protocol = ngdDAO.findProtocolBySubmissionId(accessionId);
			
			//DataProcessing dataProcessing = ngdDAO.findDataProcessingBySubmissionId(accessionId);
			DataProcessing[] dataProcessing = ngdDAO.findDataProcessingBySubmissionId(accessionId);
			
			// get series info
			NGDSeries series = ngdDAO.findSeriesBySubmissionId(accessionId);
			
			// get species info
			String species = ngdDAO.findSpeciesBySubmissionId(accessionId);
			
						
			// array submission does not have specimen so get allele if any
			Allele[] allele = ishDAO.findAlleleBySubmissionId(accessionId);
			
			ArrayList images= ngdDevDAO.findOriginalImagesById(accessionId);
			
			ArrayList linkedPublications = ishDAO.findPublicationBySubmissionId(accessionId);
			String[] acknowledgements = ishDAO.findAcknowledgementBySubmissionId(accessionId);
			ArrayList linkedSubmissionsRaw = ishDAO.findLinkedSubmissionBySubmissionId(accessionId);
			ArrayList linkedSubmissions = new ISHSubmissionAssembler().formatLinkedSubmissionData(linkedSubmissionsRaw);
			
			/** ---assemble array submission object---  */
			NGDSubmission ngdSubmission = new NGDSubmission();
			ngdSubmission.setOid(submission.getOid());
			ngdSubmission.setAccID(submission.getAccID());
			ngdSubmission.setStage(submission.getStage());
			ngdSubmission.setStageName(submission.getStageName());
			ngdSubmission.setArchiveId(submission.getArchiveId());
			ngdSubmission.setBatchId(submission.getBatchId());
			ngdSubmission.setOriginalImages(images);
			ngdSubmission.setSpecies(species);
			ngdSubmission.setSource(submission.getSource());
	
			if (null != supplementaryFiles) {
			    ngdSubmission.setRawFile(supplementaryFiles.getRawFile());
			    ngdSubmission.setProcessedFile(supplementaryFiles.getProcessedFile());
			}
			ngdSubmission.setPrincipalInvestigators(pis);
			ngdSubmission.setSubmitter(submitter);
			ngdSubmission.setAuthors(authors);
			ngdSubmission.setSample(sample);
			ngdSubmission.setSeries(series);
			ngdSubmission.setProtocol(protocol);
			ngdSubmission.setDataProcessing(dataProcessing);
			ngdSubmission.setAllele(allele);
			ngdSubmission.setLinkedSubmissions(linkedSubmissions);
			ngdSubmission.setLinkedPublications(linkedPublications);
			ngdSubmission.setAcknowledgements(acknowledgements);
			ngdSubmission.setResultNotes(submission.getResultNotes());
			ngdSubmission.setPublicFlag(submission.getPublicFlag());
			ngdSubmission.setDeletedFlag(submission.getDeletedFlag());

			return ngdSubmission;
		}
		catch(Exception e){
			System.out.println("NGDSubmissionAssembler::getData failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * get total number of gene items
	 * 
	 * @param accessionId
	 * @return
	 */
	public String getTotalGeneListItems(String accessionId) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			NGDDAO ngdDAO = MySQLDAOFactory.getNGDDAO(conn);
			String totalGeneListItems = new String("");
			totalGeneListItems += ngdDAO.getTotalNumberOfGeneListItemsBySubmissionId(accessionId);
			return totalGeneListItems;
		}
		catch(Exception e){
			System.out.println("ArraySubmissionAssembler::getTotalGeneListItems failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * Method to query the DAO to retrieve the row number of the first occurrence of a particular gene in a result set
	 * @param geneSymbol - a user specified gene symbol
	 * @return rowNum - the row number of the first occurrence of a specific gene symbol
	 */
	public int getRowNumberOfFirstOccurrenceOfGene(String accessionId, String geneSymbol){
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			NGDDAO ngdDAO = MySQLDAOFactory.getNGDDAO(conn);
			int pageNum = ngdDAO.getRowNumOf1stOccurrenceOfGeneInArrayGeneList(accessionId, geneSymbol);
			return pageNum;
		}
		catch(Exception e){
			System.out.println("ArraySubmissionAssembler::getRowNumberOfFirstOccurrenceOfGene failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

}
