/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.MolecularMarkerAssembler;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class MolecularMarkerAssemblerTest {

	ISHDAO ishDAO = null;
	Connection conn = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// create a dao
		conn = DBHelper.getDBConnection();
		ishDAO = MySQLDAOFactory.getISHDAO(conn);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.MolecularMarkerAssembler#getMarkerCandidates()}.
	 */
	@Test
	public final void testGetMarkerCandidates() {

		MolecularMarkerAssembler markerAssember = new MolecularMarkerAssembler();
		ArrayList markerCandidates = markerAssember.getMarkerCandidates();
		assertNotNull(markerCandidates);
		assertEquals(markerCandidates.size(), 80);
		assertEquals(((ArrayList)markerCandidates.get(3)).get(0), "late tubule");
		assertEquals(((ArrayList)markerCandidates.get(3)).get(1), "Sim1");
		assertEquals(((ArrayList)markerCandidates.get(3)).get(2), "23");
	}

}
