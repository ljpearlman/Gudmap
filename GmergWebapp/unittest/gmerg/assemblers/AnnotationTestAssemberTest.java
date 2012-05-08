/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;
import gmerg.assemblers.AnnotationTestAssembler;
import gmerg.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xingjun
 *
 */
public class AnnotationTestAssemberTest {
	
	private AnnotationTestAssembler ata;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ata = new AnnotationTestAssembler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		ata = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.AnnotationTestAssembler#updateExpression(java.lang.String[], java.lang.String, java.lang.String, gmerg.entities.User)}.
	 */
	@Test
	public final void testUpdateExpression() {
//		fail("Not yet implemented"); // TODO
		User user = new User();
		user.setUserName("kylie");
		user.setUserPi(3);
		String submissionId = "9999991";
		
		// insert new expression
//		String[] compIds = {"EMAP:28496"};
		String expression = "p";
		int expressionUpdated = 0;
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// update existing expression
//		String[] compIds = {"EMAP:28496"};
//		expression = "s";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// delete existing expression
//		String[] compIds = {"EMAP:28496"};
//		expression = "ne";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// update and insert expression
//		String[] compIds = {"EMAP:27752", "EMAP:28496"};
//		expression = "s";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// update expression and delete pattern/location
//		String[] compIds = {"EMAP:27752", "EMAP:28496"};
//		expression = "nd";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// insert expression and delete pattern/location
//		String[] compIds = {"EMAP:28496", "EMAP:25788"};
//		expression = "u";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// update, delete pattern/location and insert expression
//		String[] compIds = {"EMAP:27752", "EMAP:28496", "EMAP:25788"};
//		expression = "nd";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// test for change expression from 'not detected' to 'uncertain' or 'present'
		
//		String[] compIds = {"EMAP:7436"};
//		submissionId = "9999993";
//		expression = "s";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, user);
//		assertEquals(1, expressionUpdated);
		
		// test for confliction
//		String[] compIds = {"EMAP:5721"};
//		submissionId = "9999994";
//		expression = "nd";
//		String stage = "TS22";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, stage, user);
//		assertEquals(1, expressionUpdated);
		
		// delete expression
//		submissionId = "99999914";
//		String[] compIds = {"EMAP:1435"};
//		expression = "ne";
//		String stage = "TS15";
//		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, stage, user);
//		assertEquals(1, expressionUpdated);
		
		// test for conflict
		submissionId = "9999999";
		String[] compIds = {"EMAP:6693","EMAP:6705","EMAP:6694"};
		expression = "nd";
		String stage = "TS22";
		expressionUpdated = ata.updateExpression(compIds, submissionId, expression, stage, user);
		assertEquals(1, expressionUpdated);

	}

	/**
	 * Test method for {@link gmerg.assemblers.AnnotationTestAssembler#changeStage(java.lang.String, java.lang.String, java.lang.String, gmerg.entities.User)}.
	 */
	@Test
	public final void testChangeStage() {
		String submissionId = "9999993";
		User user = new User();
		user.setUserName("kylie");
		
		// test for new stage
//		String stage = "TS23";
//		int stageUpdated = ata.changeStage(stage, submissionId, user);
//		assertEquals(1, stageUpdated);
		
		// test for do nothing - same stage
//		String stage = "TS23";
//		int stageUpdated = ata.changeStage(stage, submissionId, user);
//		assertEquals(2, stageUpdated);
		
		// test for change stage and do not remove expression (no expression info)
//		String stage = "TS22";
//		int stageUpdated = ata.changeStage(stage, submissionId, user);
//		assertEquals(3, stageUpdated);
		
		// test for change stage and remove expression
//		String stage = "TS23";
//		int stageUpdated = ata.changeStage(stage, submissionId, user);
//		assertEquals(1, stageUpdated);
	}
}
