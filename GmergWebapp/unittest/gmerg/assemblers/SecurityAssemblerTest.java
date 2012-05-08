package gmerg.assemblers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gmerg.assemblers.SecurityAssembler;
import gmerg.entities.User;

public class SecurityAssemblerTest {

	private SecurityAssembler sa;
	
	@Before
	public void setUp() throws Exception {
		sa = new SecurityAssembler(); 
	}

	@After
	public void tearDown() throws Exception {
		sa = null;
	}

	@Test
	public final void testGetData() {
//		fail("Not yet implemented"); // TODO
		String username = "kylie_g";
		String password = "imb_water";
		User usr = sa.getData(username, password);
		assertEquals(username, usr.getUserName());
		assertEquals("ANNOTATOR", usr.getUserType());
		assertEquals("ANNOTATOR", usr.getUserRole());
		assertEquals("APPROVE OWN ANNOTATION", usr.getUserPrivilege());
		assertEquals(4, usr.getUserId());
		assertEquals(3, usr.getUserPi());
	}

}
