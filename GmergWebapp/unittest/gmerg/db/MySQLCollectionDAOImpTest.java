/**
 * 
 */
package gmerg.db;

import static org.junit.Assert.*;

import gmerg.entities.CollectionInfo;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class MySQLCollectionDAOImpTest {

	private Connection conn;
	private CollectionDAO collectionDAO;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		conn = DBHelper.getDBConnection();
		collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
        DBHelper.closeJDBCConnection(conn);
        collectionDAO = null;
	}

	/**
	 * Test method for {@link gmerg.db.MySQLCollectionDAOImp#updateCollectionSummary(gmerg.entities.CollectionInfo)}.
	 */
	@Test
	public final void testUpdateCollectionSummary() {
//		fail("Not yet implemented"); // TODO
		CollectionInfo collectionInfo = new CollectionInfo();
		collectionInfo.setId(17);
		collectionInfo.setDescription("collection 17");
		collectionInfo.setFocusGroup(1);
		collectionInfo.setName("C17-test");
		collectionInfo.setStatus(1);
		collectionInfo.setLastUpdate("2009-08-31");
		int updatedRecordNumber = collectionDAO.updateCollectionSummary(collectionInfo);
		assertEquals(0, updatedRecordNumber);
		
	}

}
