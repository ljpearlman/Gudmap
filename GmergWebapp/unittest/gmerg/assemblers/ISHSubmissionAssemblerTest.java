package gmerg.assemblers;

import junit.framework.TestCase;

import gmerg.assemblers.ISHSubmissionAssembler;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.DBHelper;
//import gmerg.entities.submission.Acknowledgement;
//import gmerg.entities.submission.Person;
import gmerg.entities.submission.Antibody;
//import gmerg.entities.submission.Probe;
//import gmerg.entities.submission.Specimen;
//import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ish.ISHSubmission;
import gmerg.entities.submission.StatusNote;

import java.util.*;
import java.sql.*;

public class ISHSubmissionAssemblerTest extends TestCase {

	ISHDAO ishDAO = null;
	Connection conn = null;
	
	public ISHSubmissionAssemblerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a dao
		conn = DBHelper.getDBConnection();
		ishDAO = MySQLDAOFactory.getISHDAO(conn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		DBHelper.closeJDBCConnection(conn);
	}

	/*
	 * Test method for 'db.SubmissionDetailsAssembler.getData(String)'
	 */
	public void testGetData() {

//		// get submission info
//		Submission submission = ishDAO.findSubmissionById("GUDMAP:5000");
//		assertNull(submission);
//		
//		// get probe info
//		Probe probe = ishDAO.findProbeBySubmissionId("GUDMAP:5306");
//		assertNotNull(probe);
//		assertEquals("NM_025365.1", probe.getGenbankID());
//		
//		// get specimen info
//		Specimen specimen = ishDAO.findSpecimenBySubmissionId("GUDMAP:5306");
//		assertNotNull(specimen);
//		assertEquals("4% PFA", specimen.getFixMethod());
//		
//		// get image info
//		ArrayList images = ishDAO.findImageBySubmissionId("GUDMAP:5306");
//		assertNotNull(images);
//		assertEquals(2, images.size());
//		
//		// get pi info
//		Person pi = ishDAO.findPIBySubmissionId("GUDMAP:5306");
//		assertNotNull(pi);
//		assertEquals("Andrew P. McMahon", pi.getName());
//		
//		// get submitter info
//		Person submitter = ishDAO.findSubmitterBySubmissionId("GUDMAP:5306");
//		assertNotNull(submitter);
//		assertEquals("M. Todd Valerius", submitter.getName());
//		
//		// get author info
//		String author = ishDAO.findAuthorBySubmissionId("GUDMAP:5306");
////		assertNotNull(author);
//		assertEquals("J. Yu, M.T. Valerius, A.P. McMahon", author);
//		
//		// get expression info -- discuss with chris
//		ArrayList annotationTree = ishDAO.findAnnotationTreeBySubmissionId("GUDMAP:5306", false);
//		assertNotNull(annotationTree);
//		
//		// get publication info
//		ArrayList publication = ishDAO.findPublicationBySubmissionId("GUDMAP:5306");
//		assertNotNull(publication);
//		assertEquals("2004", ((String[])publication.get(0))[1]);
//		
//		// get acknowledgement info
//		ArrayList acknowledgement = ishDAO.findAcknowledgementBySubmissionId("GUDMAP:5306");
//		assertNotNull(acknowledgement);
//		assertEquals("GUDMAP", ((String[])acknowledgement.get(0))[0]);
		
		ISHSubmissionAssembler isa = new ISHSubmissionAssembler();
		ArrayList linkedSubmission;
		ISHSubmission ishSubmission;
//		ISHSubmission ishSubmission = isa.getData("GUDMAP:7230", false);
//		assertNotNull(ishSubmission);
//		String peopleAcknowledged = ishSubmission.getPeopleAcknowleged();
//		assertNotNull(peopleAcknowledged);
//		assertEquals("Jenn Hansard", ishSubmission.getPeopleAcknowleged());
		
		// test for linked submission -- group by resource, link type
//		ishSubmission = isa.getData("GUDMAP:7837", false);
//		assertNotNull(ishSubmission);
//		ArrayList linkedSubmission = ishSubmission.getLinkedSubmissions();
//		String resource = ((Object[])linkedSubmission.get(0))[0].toString();
//		assertEquals("GUDMAP", resource);
//		String serialNo = ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(0).toString();
//		assertEquals("1", serialNo);
//		String type = ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(1).toString();
//		assertEquals("control", type);
//		String accessionId = 
//			((ArrayList)((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(2)).get(0).toString();
//		assertEquals("GUDMAP:7825", accessionId);
//		accessionId = 
//			((ArrayList)((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(2)).get(2).toString();
//		assertEquals("GUDMAP:7827", accessionId);
//
//		ishSubmission = isa.getData("GUDMAP:7825", false);
//		assertNotNull(ishSubmission);
//		linkedSubmission = ishSubmission.getLinkedSubmissions();
//		assertNotNull(linkedSubmission);
//		resource = ((Object[])linkedSubmission.get(0))[0].toString();
//		assertEquals("GUDMAP", resource);

//		System.out.println("number of LS: " + linkedSubmission.size());
//		System.out.println("number of LS type: " + ((ArrayList)((Object[])linkedSubmission.get(0))[1]).size());
//		System.out.println("LS type1 size:  " + ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).size());
		
//		serialNo = ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(0).toString();
//		assertEquals("1", serialNo);
//		type = ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(1).toString();
//		assertEquals("control", type);
//		accessionId = 
//			((ArrayList)((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(0)).get(2)).get(0).toString();
//		assertEquals("GUDMAP:7927", accessionId);
//		
//		serialNo = ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(1)).get(0).toString();
//		assertEquals("2", serialNo);
//		type = ((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(1)).get(1).toString();
//		assertEquals("same probe", type);
//		accessionId = 
//			((ArrayList)((ArrayList)((ArrayList)((Object[])linkedSubmission.get(0))[1]).get(1)).get(2)).get(0).toString();
//		assertEquals("GUDMAP:7828", accessionId);
		
		// test for maprobe notes
//		ishSubmission = isa.getData("GUDMAP:5166", false, false);
//		assertNotNull(ishSubmission);
//		Probe probe = ishSubmission.getProbe();
//		assertNotNull(probe.getMaprobeNotes());
//		assertEquals(1, ishSubmission.getProbe().getMaprobeNotes().size());
//		System.out.println("ma probe notes: " + ishSubmission.getProbe().getMaprobeNotes().get(0));
//		assertEquals("", ishSubmission.getProbe().getMaprobeNotes().get(0));
//		for (int i=0;i<ishSubmission.getProbe().getMaprobeNotes().size();i++) {
//			System.out.println("maprobe note" + i + ": " + ishSubmission.getProbe().getMaprobeNotes().get(i));
//		}
		
		// test for probe name source
//		ishSubmission = isa.getData("GUDMAP:5306", false);
//		assertNotNull(ishSubmission);
//		assertEquals("MGI", ishSubmission.getProbe().getProbeNameSource());
		
		// test for linked submission -- group by resource, accession id
//		ishSubmission = isa.getData("GUDMAP:7825", false, false);
//		assertNotNull(ishSubmission);
//
//		ArrayList linkedSubmission = ishSubmission.getLinkedSubmissions();
//		assertEquals(1, linkedSubmission.size()); // arrayList
//		assertEquals(3, ((Object[])linkedSubmission.get(0)).length); // object array: [resourse, accessionIDAndTypes, URL]
//		ArrayList accessionIDs = ((ArrayList)((Object[])linkedSubmission.get(0))[1]);  // arrayList
//		assertEquals(11, accessionIDs.size()); 
//		
//		String resource = ((Object[])linkedSubmission.get(0))[0].toString();
//		assertEquals("GUDMAP", resource);
//		
//		String url = ((Object[])linkedSubmission.get(0))[2].toString();
//		assertEquals("", url);
//		assertEquals("GUDMAP:7828", ((ArrayList)accessionIDs.get(0)).get(0).toString());
//		
//		ArrayList linkTypes = (ArrayList)((ArrayList)accessionIDs.get(0)).get(1);
//		assertEquals("same probe", linkTypes.get(0).toString());
//		
//		linkTypes = (ArrayList)((ArrayList)accessionIDs.get(1)).get(1);
//		assertEquals("control", linkTypes.get(0).toString());

		// another test for LS
//		ishSubmission = isa.getData("GUDMAP:7828", false, false);
//		assertNotNull(ishSubmission);
//
//		linkedSubmission = ishSubmission.getLinkedSubmissions();
//		assertEquals(1, linkedSubmission.size()); // arrayList
		
		// test for acknowledgement people
//		ishSubmission = isa.getData("GUDMAP:7986", false, false);
//		ArrayList acknowledgement = ishSubmission.getAcknowledgements();
//		assertNotNull(acknowledgement);
//		assertEquals(2, acknowledgement.size());
//		assertEquals("Kamin Johnson", ((String[])acknowledgement.get(0))[1]);
//		assertEquals("Michael Kelso", ((String[])acknowledgement.get(1))[1]);
		
		// test for antibody
//		ishSubmission = isa.getData("GUDMAP:8199", false, false, null);
		ishSubmission = isa.getData("GUDMAP:8199", false, false, null, false);
		assertNotNull(ishSubmission);
		
		Antibody antibody = ishSubmission.getAntibody();
		assertNotNull(antibody);
//		assertEquals("maprobe:4426", antibody.getAccessionId());
//		assertEquals("MGI:88248", antibody.getGeneId());
//		assertEquals("hybridoma mouse clone CB-955", antibody.getProductionMethod());
//        assertEquals("bovine, mouse, human, goat, sheep, porcine, rabbit, dog, cat, guinea pig, ra",  antibody.getSpeciesSpecificity());
//        assertEquals("Antigen=purified bovine kidney Calbindin-28K CALB1_BOVIN.  The UniProt accession for the mouse protein is P12658.", antibody.getNotes());
//        assertEquals("Unsequenced", antibody.getSeqStatus());
        assertEquals("bovine, mouse, human, goat, sheep, porcine, rabbit, dog, cat, guinea pig, rat", antibody.getSpeciesSpecificity());
		
//        ishSubmission = isa.getData("GUDMAP:8200", false, false, null);
        ishSubmission = isa.getData("GUDMAP:8200", false, false, null, false);
		assertNotNull(ishSubmission);
		antibody = ishSubmission.getAntibody();
		assertNotNull(antibody);
        assertEquals("mouse, human", antibody.getSpeciesSpecificity());
        
        
		// TEST for ls for ihc data
//		ishSubmission = isa.getData("GUDMAP:8199", false, false);
//		assertNotNull(ishSubmission);
//
//		linkedSubmission = ishSubmission.getLinkedSubmissions();
//		assertEquals(1, linkedSubmission.size()); // arrayList
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8200", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8201", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8202", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8203", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8204", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8205", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8206", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
//
//		ishSubmission = isa.getData("GUDMAP:8207", false, false);
//		assertNotNull(ishSubmission);
//		assertEquals("Unsequenced", ishSubmission.getAntibody().getSeqStatus());
	}
	
	public final void testGetStatusNotes() {
		ISHSubmissionAssembler isa = new ISHSubmissionAssembler();
//		ArrayList statusNotes = isa.getStatusNotes("GUDMAP:8400");
//		ArrayList statusNotes = isa.getStatusNotes("GUDMAP:5536");
		StatusNote[] statusNotes = isa.getStatusNotes("GUDMAP:5536");
		assertNotNull(statusNotes);
		assertEquals(1, statusNotes.length);
		assertEquals("WITHDRAWN", statusNotes[0].getStatusNote());
	}

}
