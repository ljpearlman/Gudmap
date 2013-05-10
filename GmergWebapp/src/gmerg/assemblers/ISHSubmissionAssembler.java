/**
 * 
 */
package gmerg.assemblers;

import gmerg.beans.UserBean;
import gmerg.db.AnatomyDAO;
import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Probe;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Allele;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.StatusNote;
import gmerg.entities.submission.ish.ISHSubmission;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class ISHSubmissionAssembler {
    private boolean debug = false;
    public ISHSubmissionAssembler() {
	if (debug)
	    System.out.println("ISHSubmissionAssembler.constructor");

    }
	/**
	 * @param accessionId
	 * @param isEditor
	 * @return
	 */
	public ISHSubmission getData(String accessionId, boolean isEditor, 
			boolean displayAnnotationAsTree, UserBean userBean, boolean onlyRetrieveTree) {
		if (accessionId == null) {
			return null;
		}
				
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
		
		// get basic submission info and create submission object
		Submission submission = ishDAO.findSubmissionById(accessionId);
		
		if(submission == null){
			return null;
		}
		
		ArrayList annotationTree = null;
		ExpressionDetail [] annotComp = null;
		
		if(displayAnnotationAsTree) {
		// get expression info -- discuss with chris
			annotationTree = anatomyDAO.findAnnotationTreeBySubmissionId(accessionId, isEditor, userBean);
		}
		else {
		annotComp = anatomyDAO.findAnnotatedListBySubmissionIds(accessionId, isEditor);
		}		
		
		// get probe info or antibody -- modified by xingjun 09-July-2007
		String assayType = submission.getAssayType();
		/** ---composite a ish submission object---  */
		ISHSubmission ishSubmission = new ISHSubmission();
		ishSubmission.setAccID(submission.getAccID());
		ishSubmission.setPublic(submission.getPublicFlag());
		ishSubmission.setDeleted(submission.getDeleteFlag());
		ishSubmission.setStage(submission.getStage());
		ishSubmission.setAssayType(assayType);
		ishSubmission.setArchiveId(submission.getArchiveId());
		ishSubmission.setAnnotationTree(annotationTree);
		ishSubmission.setAnnotatedComponents(annotComp);
		ishSubmission.setLabId(submission.getLabId());// added by xingjun - 12/08/2010
		ishSubmission.setProject(submission.getProject());
		ishSubmission.setEuregeneId(submission.getEuregeneId());
		ishSubmission.setResultNotes(submission.getResultNotes());
		
		if(onlyRetrieveTree) {
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
		anatomyDAO = null;
		return ishSubmission;
		}
	
		Probe probe = null;
		Antibody antibody = null;
		if (assayType.indexOf("ISH") >= 0) {
			probe = ishDAO.findProbeBySubmissionId(accessionId);
		} else if (assayType.indexOf("IHC") >= 0) { // assay type is IHC
			antibody = ishDAO.findAntibodyBySubmissionId(accessionId);
		}
		
		// get specimen info
		Specimen specimen = ishDAO.findSpecimenBySubmissionId(accessionId);

		// get allel info
		Allele[] allele = ishDAO.findAlleleBySubmissionId(accessionId);
		
		// get image info
		ArrayList images = ishDAO.findImageBySubmissionId(accessionId);
		
		// get author info
		String author = ishDAO.findAuthorBySubmissionId(accessionId);
		
		// get pi info
		Person[] pi = ishDAO.findPIsBySubmissionId(accessionId);
		
		// get submitter info
		Person submitter = ishDAO.findSubmitterBySubmissionId(accessionId);
		
		// get publication info
		ArrayList publication = ishDAO.findPublicationBySubmissionId(accessionId);
		
		// get acknowledgement 
		String[] acknowledgement = ishDAO.findAcknowledgementBySubmissionId(accessionId);

		ArrayList linkedSubmissionsRaw = ishDAO.findLinkedSubmissionBySubmissionId(accessionId);
		
		// format the linked submission raw data into appropriate data structure
		ArrayList linkedSubmission = formatLinkedSubmissionData(linkedSubmissionsRaw);
		
        if (assayType.indexOf("ISH") >=0) {
//        	System.out.println("add probe into submission");
    		ishSubmission.setProbe(probe);
        } else if (assayType.indexOf("IHC") >=0) {
        	ishSubmission.setAntibody(antibody);
        }
		
		ishSubmission.setSpecimen(specimen);
		ishSubmission.setAllele(allele);
		ishSubmission.setOriginalImages(images);
		ishSubmission.setAuthors(author);
		ishSubmission.setPrincipalInvestigators(pi);
		ishSubmission.setSubmitter(submitter);

		ishSubmission.setLinkedPublications(publication);
		ishSubmission.setAcknowledgements(acknowledgement);
        ishSubmission.setLinkedSubmissions(linkedSubmission);
        
        // added by Bernie - 23/09/2010
        String tissue = ishDAO.findTissueBySubmissionId(accessionId);
        ishSubmission.setTissue(tissue);

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
                anatomyDAO = null;
		
		/** ---return the composite value object---  */
		return ishSubmission;
	}
	
	/**
	 * Linked Submissions(ArrayList)-- resource (e.g. GUDMAP)
	 *                              -- submissions(ArrayList)-- serial no
	 *                                                       -- link type (e.g. control) 
	 *                                                       -- accessionId(ArrayList)-- accId1, ..., accIdN
	 *                              -- URL
	 * 
	 * @param linkedSubmissionsRaw
	 * @return an array list of LS, within every element of this array list, it's an 3-unit object array,
	 *         the first element is resource string, the second element is an array list of LS type and
	 *         accession ids, the third one is URL string;
	 *         The first element of the array list of LS type and accesion ids is the serial no,
	 *         the second element is the type string,
	 *         the third element is an array list of accession ids.
	 *         
	 */
	private ArrayList formatLinkedSubmissionDataBackup(ArrayList linkedSubmissionsRaw) {
    	
		if (linkedSubmissionsRaw == null || linkedSubmissionsRaw.isEmpty()) {
			return null;
		}
		
		int len = linkedSubmissionsRaw.size();

		// go through the linked submission list raw data and assemble them into desired data structure
		ArrayList<Object> result = new ArrayList<Object>();
		
        // linked submissions from one resource
		ArrayList<Object> linkedSubmission = null;
		
        // list of linked submission type and accession ids
		ArrayList<Object> typeAndAccessionIDsList = null;
		
		// linked submission type and accession ids
		ArrayList<Object> typeAndAccessionIDs = null;
		
		ArrayList<String> accessionIDs = null;
		String tempResource = null;
		String tempSubmissiontype = null;
//		int typeAndAccessionIDSerialNo = 1; // used for display purpose
		
		for (int i=0;i<len;i++) {
    		
			// get the data
			String resource = ((String[])linkedSubmissionsRaw.get(i))[0];
    		String submissionType = ((String[])linkedSubmissionsRaw.get(i))[1];
    		String accessionId = ((String[])linkedSubmissionsRaw.get(i))[2];
    		String url = ((String[])linkedSubmissionsRaw.get(i))[3];
    		
    		// assemble
    		if (i == 0) { // first row
    			linkedSubmission = new ArrayList<Object>();
        		linkedSubmission.add(0, resource);
        		linkedSubmission.add(1, url);
        		
        		tempResource = resource;
        		tempSubmissiontype = submissionType;
        		
        		typeAndAccessionIDsList = new ArrayList<Object>();
        		
        		typeAndAccessionIDs = new ArrayList<Object>();
        		typeAndAccessionIDs.add(0, Integer.toString(1));
        		typeAndAccessionIDs.add(1, submissionType);
        		
        		accessionIDs = new ArrayList<String>();
        		accessionIDs.add(accessionId);
        		
    		} else { // if it's not the first row, compare the resource, type, and assemble the LS accordingly
    			
    			if (resource.equals(tempResource)) {
    				if (submissionType.equals(tempSubmissiontype)) {
    					accessionIDs.add(accessionId);
    				} else {
    					typeAndAccessionIDs.add(2, accessionIDs);
    					typeAndAccessionIDsList.add(typeAndAccessionIDs);
    					
    					typeAndAccessionIDs = new ArrayList<Object>();
    					int serialNo = typeAndAccessionIDsList.size() + 1;
    	        		typeAndAccessionIDs.add(0, Integer.toString(serialNo));
    	        		typeAndAccessionIDs.add(1, submissionType);
    					tempSubmissiontype = submissionType;

    					accessionIDs = new ArrayList<String>();
    					accessionIDs.add(accessionId);
    				}
    				
    			} else { // not the same resource
    				
    				// add the accession id, type, into LS data structure (ArrayList)
					typeAndAccessionIDs.add(2, accessionIDs);
					typeAndAccessionIDsList.add(typeAndAccessionIDs);
					linkedSubmission.add(1, typeAndAccessionIDsList);
    				
					// convert the data structure for display
					Object[] linkedSubmissionObj = 
    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
    				result.add(linkedSubmissionObj);
    				
        			linkedSubmission = new ArrayList<Object>();
            		linkedSubmission.add(0, resource);
            		linkedSubmission.add(1, url);
            		
            		tempResource = resource;
            		tempSubmissiontype = submissionType;
            		
            		typeAndAccessionIDsList = new ArrayList<Object>();
            		
            		typeAndAccessionIDs = new ArrayList<Object>();
	        		typeAndAccessionIDs.add(0, Integer.toString(1));
	        		typeAndAccessionIDs.add(1, submissionType);
            		
            		accessionIDs = new ArrayList<String>();
            		accessionIDs.add(accessionId);
    			}
    			
    			// put the last row of data into the result data structure
    			if (i == len-1) {
    				typeAndAccessionIDs.add(2, accessionIDs);
    				typeAndAccessionIDsList.add(typeAndAccessionIDs);
    				linkedSubmission.add(1, typeAndAccessionIDsList);
    				Object[] linkedSubmissionObj = 
    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
    				result.add(linkedSubmissionObj);
    			}
    		}
    		
		} // end of going through the linked submission list raw data
		return result;
    }
	
	/**
	 * Linked Submissions(ArrayList)-- resource (e.g. GUDMAP)
	 *                              -- submissions(ArrayList)-- accessionId
	 *                                                                  -- link type(ArrayList) 
	 *                              -- URL
	 * 
	 * @param linkedSubmissionsRaw
	 * @return an array list of LS, within every element of this array list, it's an 3-unit object array,
	 *         the first element is resource string, the second element is an array list of accession id and
	 *         link types, the third one is URL string;
	 *         The first element of the array list of accesion id and link types is the accession id,
	 *         the second element is an array list of link types.
	 *         
	 */
	private ArrayList formatLinkedSubmissionData(ArrayList linkedSubmissionsRaw) {
    	
		if (linkedSubmissionsRaw == null || linkedSubmissionsRaw.isEmpty()) {
			return null;
		}
		
		int len = linkedSubmissionsRaw.size();
//		System.out.println("len: " + len);

		// go through the linked submission list raw data and assemble them into desired data structure
		ArrayList<Object> result = new ArrayList<Object>();
		
        // linked submissions from one resource
		ArrayList<Object> linkedSubmission = null;
		
        // list of linked submission and accession id and their link types
		ArrayList<Object> accessionIDAndTypesList = null;
		
		// linked submission accession id and types
		ArrayList<Object> accessionIDAndTypes = null;
		
		ArrayList<String> linkTypes = null;
		String tempResource = null;
		String tempAccessionId = null;
		
		for (int i=0;i<len;i++) {
    		
			// get the data
			String resource = ((String[])linkedSubmissionsRaw.get(i))[0].trim();
//			System.out.println("resource len: " + resource.length());
    		String accessionId = ((String[])linkedSubmissionsRaw.get(i))[1].trim();
//    		System.out.println("accessionid: " + accessionId);
    		String linkType = ((String[])linkedSubmissionsRaw.get(i))[2].trim();
    		String url = ((String[])linkedSubmissionsRaw.get(i))[3].trim();
//    		System.out.println("this is no " + i);
    		
    		// assemble
    		if (i == 0) { // first row
    			linkedSubmission = new ArrayList<Object>();
        		linkedSubmission.add(0, resource);
        		linkedSubmission.add(1, url);
        		
        		tempResource = resource;
        		tempAccessionId = accessionId;
        		
        		accessionIDAndTypesList = new ArrayList<Object>();
        		
        		accessionIDAndTypes = new ArrayList<Object>();
        		accessionIDAndTypes.add(0, accessionId);
        		
        		linkTypes = new ArrayList<String>();
        		linkTypes.add(linkType);
        		
    		} else { // if it's not the first row, compare the resource, accession id, and assemble the link type accordingly
    			
    			if (resource.equals(tempResource)) {
    				if (accessionId.equals(tempAccessionId)) {
    					linkTypes.add(linkType);
    				} else {
    					accessionIDAndTypes.add(1, linkTypes);
    					accessionIDAndTypesList.add(accessionIDAndTypes);
    					
    					accessionIDAndTypes = new ArrayList<Object>();
    	        		accessionIDAndTypes.add(0, accessionId);
    	        		tempAccessionId = accessionId;

    					linkTypes = new ArrayList<String>();
    					linkTypes.add(linkType);
    				}
    				
    			} else { // not the same resource
    				
    				// add the type, accession id, into LS data structure (ArrayList)
					accessionIDAndTypes.add(1, linkTypes);
					accessionIDAndTypesList.add(accessionIDAndTypes);
					linkedSubmission.add(1, accessionIDAndTypesList);
    				
					// convert the data structure for display
					Object[] linkedSubmissionObj = 
    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
    				result.add(linkedSubmissionObj);
    				
        			linkedSubmission = new ArrayList<Object>();
            		linkedSubmission.add(0, resource);
            		linkedSubmission.add(1, url);
            		
            		tempResource = resource;
            		tempAccessionId = accessionId;
            		
            		accessionIDAndTypesList = new ArrayList<Object>();
            		
            		accessionIDAndTypes = new ArrayList<Object>();
	        		accessionIDAndTypes.add(0, accessionId);
            		
            		linkTypes = new ArrayList<String>();
            		linkTypes.add(linkType);
    			}
    		}
			// put the last row of data into the result data structure
//			System.out.println("len: " + len);
			if (i == len-1) {
				accessionIDAndTypes.add(1, linkTypes);
				accessionIDAndTypesList.add(accessionIDAndTypes);
				linkedSubmission.add(1, accessionIDAndTypesList);
				Object[] linkedSubmissionObj = 
					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
				result.add(linkedSubmissionObj);
			}
    		
		} // end of going through the linked submission list raw data
		return result;
    }
	
	public Person getPersonById(String personId) {
		
		if (personId == null) {
			return null;
		}
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get pi info
		Person pi = ishDAO.findPersonById(personId);
		return pi;
	}	

	/**
	 * @author xingjun
	 * @param accessionId
	 * @return
	 */
	public StatusNote[] getStatusNotes(String accessionId) {
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get status notes
		StatusNote[] statusNotes = ishDAO.getStatusNotesBySubmissionId(accessionId);
		return statusNotes;
	}
	
	/**
	 * @author xingjun - 18/07/2011
	 * @param accessionId
	 * @return
	 */
	public Person[] getPeopleBySubmissionId(String submissionId) {
		
		if (submissionId == null) {
			return null;
		}
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get pi info
		Person[] pis = ishDAO.findPIsBySubmissionId(submissionId);
		return pis;
	}
}
