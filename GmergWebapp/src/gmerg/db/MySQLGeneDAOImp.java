/**
 * 
 */
package gmerg.db;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xingjun
 *
 */
public class MySQLGeneDAOImp implements GeneDAO {
	
	Connection conn;

	// default constructor
	public MySQLGeneDAOImp() {
		
	}
	
	// constructor with parameter
	public MySQLGeneDAOImp(Connection conn) {
		this.conn = conn;
	}
	

	/* (non-Javadoc)
	 * @see gmerg.db.GeneDAO#findSynonymsBySymbol(java.lang.String)
	 */
	public ArrayList<String> findSynonymsBySymbol(String symbol) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @author xingjun - 18/03/2009
	 */
	public String findSymbolBySynonym(String synonym) {
        String symbol = null;
        ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("GET_GENE_SYMBOL_BY_SYNONYM");
        PreparedStatement prepStmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, synonym);

            // execute
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
            	symbol = resSet.getString(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return symbol;
	}
	
	
	/**
	 * @author xingjun - 07/04/2009
	 * <p>modified on 01/05/2009 - split gene input before starting to search</p>
	 * <p>modified by xingjun - 12/10/2009 - should not split the input string using dot character</p>
	 * @param input
	 * @param wildcard
	 * @return
	 */
	public ArrayList<String> getSymbolsFromGeneInput(String input, String wildcard) {
//		String[] geneInput = new String[] {input};
//		String[] geneInput = input.split(":|;|,|\\.");
		String[] geneInput = input.split(":|;|,"); // 12/10/2009
//		for(int i=0;i<geneInput.length;i++) System.out.println("GeneDAO:getSymbolsFromGeneInput:geneInput(" + i + "): " + geneInput[i]);
		ArrayList<String> symbols = new ArrayList<String>();
		for(int i=0; i<geneInput.length; i++)
			if (!"".equals(geneInput[i].trim()))
				symbols.add(geneInput[i]);
		String[] symbolsArray = new String[symbols.size()];
		symbolsArray = symbols.toArray(symbolsArray);
		return this.getSymbolsFromGeneInput(symbolsArray, wildcard);
	}

    /**
     * <p>method to retrieve list of gene symbols from user input.</p>
     * <p>most of the code are based on method getSymbolsFromGeneInputParams in AdvancedQueryDAO class</p>
     * <p>modified by xingjun - 09/10/2009 - added code to find symbol from REF_GENE_INFO using gene synonym</p>
     * @param input - list of user inputs. Could be any one of gene name, gene symbol, gene synonym
     * @param wildcard - string to determine whether the user wants to search for an exact string,
     * @return geneSymbols - a list of gene symbols determined from user input
     */
    private ArrayList<String> getSymbolsFromGeneInput(String[] input, String wildcard) {
    	ArrayList<String> geneSymbols = null;
    	//array to contain components to build a specific query
		String[] symbolsQParts;
		//string to contain sql to find gene symbols from REF_PROBE using gene symbol
		String symbolsFromRefProbeSymbolQ;
		//string to contain sql to find gene symbol from REF_PROBE using gene name
		String symbolsFromRefProbeNameQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene symbol
		String symbolsFromrefGeneInfoSymbolQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene name
		String symbolsFromrefGeneInfoNameQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene synonym
		String symbolsFromrefGeneInfoSynonymQ;
		
		// this qury will return a list of syns to be used as input in another genefinding query - symbolsFromSynListQ
		String synonymListQ;
		
		PreparedStatement stmt = null;
		ResultSet resSet = null;

		try {			
			//if user wants to do a wild card search
			if (wildcard.equalsIgnoreCase("contains") || wildcard.equalsIgnoreCase("starts with")) {
				//get components to build query to find symbols from REF_PROBE using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Symbol");
				//create sql from components and user input
				symbolsFromRefProbeSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_PROBE using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Name");
				//create sql from components and user input
				symbolsFromRefProbeNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_GENE_INFO using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Symbol");
				//create sql from components and user input
				symbolsFromrefGeneInfoSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_GENE_INFO using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Name");
				//create sql from components and user input
				symbolsFromrefGeneInfoNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				// 09/10/2009 - START
				//get components to build query to find symbol from REF_GENE_INFO using gene synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_synonym");
				//create sql from components and user input
				symbolsFromrefGeneInfoSynonymQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				// 09/10/2009 - END
				//get components to build query to find synonymns from REF_SYNONYM using synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefSyn_Synonym");
				//create sql from components and user input
				synonymListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			}
			//search for an exact string
			else {
				//get components to build query to find symbols from REF_PROBE using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Symbol");
				//create sql from components and user input
				symbolsFromRefProbeSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbols from REF_PROBE using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Name");
				//create sql from components and user input
				symbolsFromRefProbeNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbols from REF_GENE_INFO using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Symbol");
				//create sql from components and user input
				symbolsFromrefGeneInfoSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbols from REF_GENE_INFO using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Name");
				//create sql from components and user input
				symbolsFromrefGeneInfoNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				// 09/10/2009 - START
				//get components to build query to find symbol from REF_GENE_INFO using gene synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_synonym");
				//create sql from components and user input
				symbolsFromrefGeneInfoSynonymQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				// 09/10/2009 - END
				//get components to build query to find synonymns from REF_SYNONYM using synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefSyn_Synonym");
				//create sql from components and user input
				synonymListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
			}

			// need to execute query to get syn list here
			String[] synList = null;
			stmt = conn.prepareStatement(synonymListQ);
			for(int i=0;i<input.length;i++){
				if(wildcard.equalsIgnoreCase("contains")) {
					stmt.setString(i+1, "%"+input[i].trim()+"%");
				}
				else if(wildcard.equalsIgnoreCase("starts with")){
					stmt.setString(i+1, input[i].trim()+"%");
				}
				else {
					stmt.setString(i+1, input[i].trim());
				}
			}
			
			resSet = stmt.executeQuery();
			if (resSet.first()) {
				resSet.last();
				int rowCount = resSet.getRow();
				resSet.beforeFirst();
				synList = new String[rowCount];
				int i = 0;
				while (resSet.next()) {
					synList[i] = resSet.getString(1);
					i++;
				}
			}

			symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefMgiMrk_MGIAcc");
			String symbolsFromMGiAccQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
			
			symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefEnsGene_EnsemblId");
			String symbolsFromEnsemblIdQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
			
			// sligtly different query - had to get list of relevant synonyms
			// from db to use as input for this query
			symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefMgiMrkRefSyn_Synonym");
			String symbolsFromSynListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(synList,symbolsQParts[0], symbolsQParts[1], 1);

			String union = AdvancedSearchDBQuery.getUnion();
			//use 'union' to incorporate all queryies into a single query
			String allQueriesQ = symbolsFromRefProbeSymbolQ + union
					+ symbolsFromRefProbeNameQ + union
					+ symbolsFromrefGeneInfoSymbolQ + union
					+ symbolsFromrefGeneInfoNameQ + union
					+ symbolsFromrefGeneInfoSynonymQ + union // 09/10/2009
					+ symbolsFromMGiAccQ + union 
					+ symbolsFromEnsemblIdQ;
			if(!symbolsFromSynListQ.equals("")){
				allQueriesQ += union + symbolsFromSynListQ;
			}
//			System.out.println("geneDAO:getSymbolsFromGeneInput:symbolsFromrefGeneInfoSynonymQ: " + symbolsFromrefGeneInfoSynonymQ);
			stmt = conn.prepareStatement(allQueriesQ);
			
			//for the first 4 in 'union' query, set the parameters
			for(int i=0;i<5;i++){// xingjun - 09/10/2009 - change from 4 to 5
				for(int j=0;j<input.length;j++){
					if(wildcard.equalsIgnoreCase("contains")) {
						stmt.setString((i*input.length)+j+1, "%"+input[j].trim()+"%");
					}
					else if(wildcard.equalsIgnoreCase("starts with")) {
						stmt.setString((i*input.length)+j+1, input[j].trim()+"%");
					}
					else {
						stmt.setString((i*input.length)+j+1, input[j].trim());
					}
				}
			}
			//start the loop at 4 since we have already set params for the first four queries.
			//set the params for the remaining 'union' queries
			for(int i=5;i<7;i++){// xingjun - 09/10/2009 - change from 4 to 5 and 6 to 7 respectively
				for(int j=0;j<input.length;j++){
					stmt.setString((i*input.length)+j+1, input[j].trim());
				}
			}
			//need to set additional params based on result of synonym search
			if(synList != null) {
			    for(int i = 0;i< synList.length;i++){
			    	//as there are 6 previous queries, need to start setting params from 6 onwards.
			    	stmt.setString((7*input.length+1+i), synList[i].trim());// xingjun - 09/10/2009 - change from 6 to 7
			    }
			}

			resSet = stmt.executeQuery();
			if(resSet.first()){
				resSet.last();
				geneSymbols = new ArrayList<String>();
				resSet.beforeFirst();
				while (resSet.next()) {
					if(!resSet.getString(1).trim().equals("")){
						geneSymbols.add(resSet.getString(1));
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(stmt);
		}

		return geneSymbols;
	} // end of getSymbolsFromGeneInputParams
    
    /**
     * @author xingjun - 01/07/2009
     */
    public String getGeneSymbolByMGIId(String mgiId) {
        String symbol = null;
        ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("GET_GENE_SYMBOL_BY_MGI_ID");
        PreparedStatement prepStmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, mgiId);

            // execute
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
            	symbol = resSet.getString(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return symbol;
    }
    
}
