package gmerg.assemblers;

import gmerg.assemblers.ImageDetailAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ImageDetail;

import java.sql.Connection;

import junit.framework.TestCase;

public class ImageDetailAssemblerTest extends TestCase {

	ISHDAO ishDAO = null;
	Connection conn = null;
	
	public ImageDetailAssemblerTest(String arg0) {
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
		ishDAO = null;
	}

	/*
	 * Test method for 'gmerg.assemblers.ImageDetailAssembler.getData(String, String)'
	 */
	public final void testGetData() {
		// TODO Auto-generated method stub
		
		ImageDetailAssembler ida = new ImageDetailAssembler();
		ImageDetail imageDetail = ida.getData("GUDMAP:5306", "1");
        assertNotNull(imageDetail);
        assertEquals("GUDMAP:5306", imageDetail.getAccessionId());
        assertEquals("1", imageDetail.getSerialNo());
        assertEquals("RIKEN cDNA 1110002E23 gene", imageDetail.getGeneName());
        
		imageDetail = ida.getData("GUDMAP:10", "1");
        assertNotNull(imageDetail);
        assertEquals("GUDMAP:10", imageDetail.getAccessionId());
        assertEquals(2, imageDetail.getAllImageNotesInSameSubmission().size());
        assertEquals("image2.jpeg", ((String[])imageDetail.getAllImageNotesInSameSubmission().get(1))[0]);
        assertEquals("10x  Caudal end of embryo on Right, hindlimb bud visible.  Medium-strong expression is visible dorsal to the metanephric mesenchyme.", ((String[])imageDetail.getAllImageNotesInSameSubmission().get(1))[1]);
	}

}
