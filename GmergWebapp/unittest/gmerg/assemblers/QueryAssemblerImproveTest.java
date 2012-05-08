/**
 * 
 */
package gmerg.assemblers;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gmerg.assemblers.QueryAssembler;
import gmerg.utils.table.DataItem;

/**
 * @author xingjun
 *
 */
public class QueryAssemblerImproveTest {

	QueryAssembler qa = null;
	String queryType;
    String component;    
    String startStage;
    String endStage;
    String criteria;
    String output;
    String ignoreExpression;
    String inputType;
    String geneSymbol;
    String stage;
    String componentID;

    /**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		qa = null;
	}

	/**
	 * Test method for {@link gmerg.assemblers.QueryAssembler#retrieveData(int, boolean, int, int)}.
	 */
//	@Test
//	public void testRetrieveData() {
////		fail("Not yet implemented");
////		queryType = "genesInAnatomyISH";
//	    String components = "30103";
////	    String expressionTypes = "present_not detected_uncertain";
////	    startStage = "TS17";
////	    endStage = "TS28";
////	    criteria = "all";
////	    criteria = "any";
//
//		queryType = "geneQueryISH";
//	    String expressionTypes = "present";
//	    criteria = "equals";
//	    output = "gene";
//	    ignoreExpression = null;
//	    inputType = "symbol";
////	    geneSymbol = "cd24a,wnt40";
//	    geneSymbol = "";
//	    stage = "20";
//
////		queryType = "componentGeneSubsISH";
////		component = "EMAP:4580";
////	    geneSymbol = "cd24a,wnt40";
////	    stage = "20";
//		
////		queryType = "componentSubsISH";
////	    componentID = null;
//	    
//	    
////	    output = "anatomy";
//	    HashMap<String,Object> params = new HashMap<String,Object>();
//	    params.put("queryType", queryType);
//	    params.put("component", component);
//	    params.put("components", components);
//	    params.put("startStage", startStage);
//	    params.put("endStage", endStage);
//	    params.put("expressionTypes", expressionTypes);
//	    params.put("criteria", criteria);
//	    params.put("output", output);
//	    params.put("ignoreExpression", ignoreExpression);
//	    params.put("inputType", inputType);
//	    params.put("geneSymbol", geneSymbol);
//	    params.put("stage", stage);
//	    params.put("componentID", componentID);
//	    qa = new QueryAssembler(params);
//	    
//	    int column = 0;
//	    boolean ascending = true;
//	    int offset = 0; 
//	    int num = 100000;
//	    DataItem[][] result = qa.retrieveData(column, ascending, offset, num);
//	    assertNotNull(result);
//	    
//	    // when output = gene
//	    assertEquals(2,result.length);
//	    assertEquals(13, result[0].length);
//	    
//	    // when output = anatomy
////	    assertEquals(3,result.length);
////	    assertEquals(13, result[0].length);
//	}

	/**
	 * Test method for {@link gmerg.assemblers.QueryAssembler#retrieveNumberOfRows()}.
	 */
	@Test
	public void testRetrieveNumberOfRows() {
//		fail("Not yet implemented");
//		queryType = "genesInAnatomyISH";
		queryType = "geneQueryISH";
		String components = null;
	    String expressionTypes = null;
//		queryType = "componentGeneSubsISH";
//		queryType = "componentSubsISH";
	    component = "EMAP:4580";    
	    startStage = null;
	    endStage = null;
//	    expressionTypes = null;
	    criteria = "equals";
	    
//	    output = "anatomy";
	    output = "gene";
	    
	    ignoreExpression = null;
	    inputType = "symbol";
//	    geneSymbol = "cd24a,wint40";
	    geneSymbol = "";
	    stage = "20";
	    componentID = null;
	    HashMap<String,Object> params = new HashMap<String,Object>();
	    params.put("queryType", queryType);
	    params.put("component", component);
	    params.put("components", components);
	    params.put("startStage", startStage);
	    params.put("endStage", endStage);
	    params.put("expressionTypes", expressionTypes);
	    params.put("criteria", criteria);
	    params.put("output", output);
	    params.put("ignoreExpression", ignoreExpression);
	    params.put("inputType", inputType);
	    params.put("geneSymbol", geneSymbol);
	    params.put("stage", stage);
	    params.put("componentID", componentID);
	    qa = new QueryAssembler(params);
	    
	    int numberOfRows = qa.retrieveNumberOfRows();
	    assertEquals(5, numberOfRows);
	}

	/**
	 * Test method for {@link gmerg.assemblers.QueryAssembler#retrieveTotals()}.
	 */
	@Test
	public void testRetrieveTotals() {
//		fail("Not yet implemented");
//		queryType = "geneQueryISH";
		queryType = "componentGeneSubsISH";
		String components = null;
	    String expressionTypes = null;
	    component = "EMAP:4580";    
	    startStage = null;
	    endStage = null;
//	    expressionTypes = null;
	    criteria = "equals";
	    
//	    output = "anatomy";
	    output = "gene";
	    
	    ignoreExpression = null;
	    inputType = "gene";
	    geneSymbol = "cd24a,wnt40";
	    stage = "20";
	    componentID = null;
	    HashMap<String,Object> params = new HashMap<String,Object>();
	    params.put("queryType", queryType);
	    params.put("component", component);
	    params.put("components", components);
	    params.put("startStage", startStage);
	    params.put("endStage", endStage);
	    params.put("expressionTypes", expressionTypes);
	    params.put("criteria", criteria);
	    params.put("output", output);
	    params.put("ignoreExpression", ignoreExpression);
	    params.put("inputType", inputType);
	    params.put("geneSymbol", geneSymbol);
	    params.put("stage", stage);
	    params.put("componentID", componentID);
	    qa = new QueryAssembler(params);
	    
//	    int[] totals = qa.retrieveTotals();
//	    assertNotNull(totals);
//	    assertEquals(9, totals.length);
	}

}
