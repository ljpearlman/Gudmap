/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.AdvancedQueryDAO;
import gmerg.db.DBHelper;
import gmerg.db.FocusForAllDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.DbUtility;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xingjun
 *
 */
public class BridgePageAssembler {
	
	String[] inputs;
	String[] queryCriteria;
	String query;
	
	String organ = null;
	String sub = null;
	String exp = null ;
	String transitiveRelations = null;
	
	
	public BridgePageAssembler(HashMap params) {
		inputs = (String[])params.get("inputs");
		query = (String)params.get("query");
		queryCriteria = (String[])params.get("wildcards");
	}

	public int getNumberOfInsituByGeneByEpression(String exp) {
		this.organ = null;
		this.exp = exp;
		this.queryCriteria[1] = "ALL";
		return retrieveNumberOfRowsInGroups()[0];
	}
	
	public int getNumberOfInsituByGeneByStage(String stage) {
		this.organ = null;
		this.exp = null;
		this.queryCriteria[1] = stage;
		return retrieveNumberOfRowsInGroups()[0];
	}

	public int getNumberOfInsituByGeneByAnatomy(String organ) {
		this.organ = organ;
		this.exp = null;
		this.queryCriteria[1] = "ALL";
		return retrieveNumberOfRowsInGroups()[0];
	}

	public int getNumberOfGenesQueryByGene(String input) {
		return 0;
	}
	
	public int getNumberOfEntriesQueryByGene(String inputString) {
		return 0;
	}
	
	public int getNumberOfEntriesQueryByAnatomy(String inputString) {
		return 0;
	}
	
	public int getNumberOfEntriesQueryByAccession(String inputString) {
		return 0;
	}

	public int getNumberOfEntriesQueryByGeneFunction(String inputString) {
		return 0;
	}

	public int getNumberOfGenesQueryByGeneFunction(String inputString) {
		return 0;
	}
	
	public int retrieveNumberOfRows() {
//		System.out.println("retrieveNumberOfRows::query: " + query);
//		System.out.println("input: " + inputs.length + ":" + inputs[0]);
//		System.out.println("sub: " + sub);
//		System.out.println("organ: " + organ);
//		System.out.println("wildcard: " + queryCriteria.length + ":" + queryCriteria[0]);

		
		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		int n =  advancedDAO.getNumberOfRowsOneQuery(query, inputs, organ, sub, exp, queryCriteria, transitiveRelations);
		DBHelper.closeJDBCConnection(conn);
		
		System.out.println("rowcount: " + n);
		return n;
	}
	
	public int[] retrieveNumberOfRowsInGroups() {
//		System.out.println("retrieveNumberOfRowsInGroup::query: " + query);
//		System.out.println("input: " + inputs.length + ":" + inputs[0]);
//		System.out.println("sub: " + sub);		
//		System.out.println("exp: " + exp);	
//		System.out.println("organ: " + organ);
//		System.out.println("wildcard: " + queryCriteria.length + ":" + queryCriteria[0]);
//		System.out.println("stage: " + queryCriteria.length + ":" + queryCriteria[1]);
//		System.out.println("ano: " + queryCriteria.length + ":" + queryCriteria[2]);

		Connection conn = DBHelper.getDBConnection();
		AdvancedQueryDAO advancedQueryDAO = MySQLDAOFactory.getAdvancedQueryDAO(conn);
		int[] totals =  advancedQueryDAO.getNumberOfRowsInGroups(query, inputs, organ, sub, exp, queryCriteria, transitiveRelations, null);
		DBHelper.closeJDBCConnection(conn);
		
		return totals;
	}

}
