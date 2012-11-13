/**
 * 
 */
package gmerg.db;

import gmerg.utils.table.GenericTableFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class MySQLTransgenicDAOImp implements TransgenicDAO {
    private boolean debug = false;

    private Connection conn;

    // default constructor
    public MySQLTransgenicDAOImp() {

    }

    // constructor with connection initialisation
    public MySQLTransgenicDAOImp(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * @author xingjun - 26/08/2008
     * <p>xingjun - now get sql from InsituDBQuery</p>
     */
    public ArrayList getAllSubmission(int columnIndex, boolean ascending, int offset, int num, 
    		String[] organ, GenericTableFilter filter) {
	if (debug)
	    System.out.println("TransgenicDAOImp:getAllSubmission entered#####");
        ResultSet resSet = null;
        ArrayList result = null;
        ParamQuery parQ = null;
    	parQ = InsituDBQuery.getParamQuery("ALL_ENTRIES_TG");
        PreparedStatement prepStmt = null;

        // assemble the query string
        String query = parQ.getQuerySQL() + "AND SUB_ASSAY_TYPE = 'TG'";
        String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
        String queryString =
        	assembleBrowseSubmissionQueryString(1, query, defaultOrder, columnIndex, ascending, offset, num, organ);
        
	if (debug)
	    System.out.println("TransgenicDAO:getAllSubmission:sql (pre filter): " + queryString);
        
        if(filter!=null)
	  	  	queryString = filter.addFilterSql(queryString, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
        
	if (debug)
	    System.out.println("TransgenicDAOImp:getAllSubmission:sql (post filter): " + queryString);
        
        try {
	 ///////!!!!! poor databse table for different type of submissions
	    ////////!!!! do not know why replaceAll does not work
	    int index = queryString.lastIndexOf("MUT_GENE AS RPR_SYMBOL");
	    String str = null;
	    if (-1 != index) {
		str = queryString;
		queryString = str.substring(0, index) + "MUT_GENE "+str.substring(index + (new String("MUT_GENE AS RPR_SYMBOL")).length());
	    }
	    
	    index = queryString.lastIndexOf("RPR_SYMBOL");
	    while (-1 != index) {
		str = queryString;
		queryString = str.substring(0, index) + "MUT_GENE "+str.substring(index + (new String("RPR_SYMBOL")).length());
		index = queryString.lastIndexOf("RPR_SYMBOL");
	    }
	    ////////!!!!!!

	    if (debug)
		System.out.println("MySQLTransgenicDAOImp.sql = "+queryString);
        	prepStmt = conn.prepareStatement(queryString);
            resSet = prepStmt.executeQuery();
            result = formatBrowseResultSet(resSet);

            // release db resource
            DBHelper.closePreparedStatement(prepStmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }

    /**
     * @author xingjun - 26/08/2008
     */
    private String assembleBrowseSubmissionQueryString(int queryType, String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num, String[] organ) {
		String queryString = null;
		String organsql = "";
		if(null != organ) {
			String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
			String ids = "";
			  for(int i = 0; i < emapids.length; i++) {
				  ids += "'"+emapids[i] + "',";
			  }
			  if(emapids.length >= 1) {
				  ids = ids.substring(0, ids.length()-1);
			  }
			
			organsql =" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
		         " ANAD_RELATIONSHIP_TRANSITIVE, "+
		         " ANA_TIMED_NODE DESCEND_ATN, "+
		         " ANA_NODE, "+
		         " ANAD_PART_OF "+
		    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
		      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
		      //" and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+
		      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
		      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
		      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
		      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
		} else { // remove redundant join to speed up query
			query = query.replace("LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
		}
		
		// order by
		if (columnIndex != -1) {
			queryString = query + organsql + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseSubmissionOrderByColumn(queryType, columnIndex, ascending);
			queryString += column;
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
			queryString = query + organsql + defaultOrder+ ", SUB_EMBRYO_STG";
		}
		
		// offset and retrieval number
		queryString = queryString + " LIMIT " + offset + " ," + num;
		
		// return assembled query string
		return queryString;
    }

    /**
     * @author xingjun - 26/08/2008
	 * @param queryType: 2 - for component query; 1 - for browse and gene query
     * <p>xingjun - 23/10/2009 - changed concatenation of column 3 (Age) </p>
     * <p></p>
     */
    private String getBrowseSubmissionOrderByColumn(int queryType, int columnIndex, boolean ascending) {
	if (debug)
	    System.out.println("TransgenicDAO:getBrowseSubmissionOrderByColumn:columnIndex: " + columnIndex);
    	String orderByString = new String("");
    	String order = (ascending == true ? "ASC": "DESC");
    	String[] ISHBrowseAllColumnList = {
    			"natural_sort(SUB_ACCESSION_ID)", // 0
    			"natural_sort(TRIM(RPR_SYMBOL))", // 1
    			"SUB_EMBRYO_STG", // 2
    			"TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)", // 3
    			"SUB_SOURCE", // 4
    			"SUB_SUB_DATE", // 5
    			"SUB_ASSAY_TYPE", // 6
    			"SPN_ASSAY_TYPE", // 7
    			"SPN_SEX", // 8
    			"RPR_JAX_ACC", // 9
    			"SPN_WILDTYPE", // 10
    			"PRB_PROBE_TYPE" //11
    	};
    	String geneSymbolCol;
    	
        if(queryType == 1){
            geneSymbolCol = "natural_sort(TRIM(RPR_SYMBOL))";
        }
        else if(queryType == 2){
            geneSymbolCol = "natural_sort(TRIM(PRB_GENE_SYMBOL))";
        }
        else {
            geneSymbolCol = "";
        }
        
        // start to translate
        if (queryType == 3) {
        
        } else {
        	if(columnIndex == 0) {
        		if (queryType == 1) {
        			orderByString = "natural_sort(SUB_ACCESSION_ID) " + 
        			order +", " + geneSymbolCol;
        		} else if (queryType == 2) {
        			
        		}
        	} else if (columnIndex == 1) {
       			orderByString = geneSymbolCol + " " + order +", SUB_EMBRYO_STG "; 
        	} else if (columnIndex == 2) {
        		orderByString = "SUB_EMBRYO_STG" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 3) {
//        		orderByString = "CONCAT(SPN_STAGE,SPN_STAGE_FORMAT)" + " " + order +", " + geneSymbolCol;
//        		orderByString = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" + " " + order +", " + geneSymbolCol;
        		orderByString = "AGE" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 4) {
        		orderByString = "SUB_SOURCE" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 5) {
        		orderByString = "SUB_SUB_DATE" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 6) {
        		orderByString = "SUB_ASSAY_TYPE" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 7) {
        		orderByString = "SPN_ASSAY_TYPE" + " " + order +", " + geneSymbolCol;
        	}else if (columnIndex == 8) {
        		orderByString = "SPN_SEX" + " " + order + ", " + geneSymbolCol;
        	}else if (columnIndex == 9) {
        		orderByString = "NATURAL_SORT(TRIM(RPR_JAX_ACC))" + " " + order + ", " + geneSymbolCol;
        	}else if (columnIndex == 10) {
        		orderByString = "SPN_WILDTYPE" + " " + order + ", " + geneSymbolCol;
        	}else if (columnIndex == 11) {
        		orderByString = "PRB_PROBE_TYPE" + " " + order + ", " + geneSymbolCol;
        	} else {
       			orderByString = geneSymbolCol + ", SUB_EMBRYO_STG ";
        	}
        }

    	return orderByString;
    }
    
    /**
     * xingjun - 08/09/2011 - copied from ISHDevDAO and modified - should combined two of them into one
     */
    public String[][] getStringArrayFromBatchQuery(String[][] param,
            String[] query, String endingClause, GenericTableFilter filter) {
    	int queryNumber = query.length;
    	String[][] result = new String[queryNumber][2];
    	String[] queryString = new String[queryNumber];
    	
    	// get the query sql based on query name and record query name in result string array
    	for (int i = 0; i < queryNumber; i++) {
    		ParamQuery parQ = InsituDBQuery.getParamQuery(query[i]);
    		
    		queryString[i] = parQ.getQuerySQL();
    		if (endingClause != null && !endingClause.equals("")) {
    			queryString[i] += endingClause;
    		}
		if (debug)
		    System.out.println("Q (preFilter): "  + queryString[i]);
    		if (filter!=null)
    			queryString[i] = filter.addFilterSql(queryString[i], InsituDBQuery.getTG_BROWSE_ALL_SQL_COLUMNS());
		if (debug)
		    System.out.println("Q (postFilter): "  + queryString[i]);
    		result[i][0] = query[i];
    	}
    	
    	// start to execute query
    	// Connection conn = DBUtil.getDBConnection();
    	ResultSet resSet = null;
    	PreparedStatement prepStmt = null;
	int index = 0;
	String str = null;
    	try {
    		for (int i = 0; i < queryNumber; i++) {
			if (debug)
			    System.out.println("MySQLTransgenicDAOImp.sql = "+queryString[i]);

			///////!!!!! poor databse table for different type of submissions
			    ////////!!!! do not know why replaceAll does not work
			    index = queryString[i].lastIndexOf("MUT_GENE AS RPR_SYMBOL");
			    if (-1 != index) {
				str = queryString[i];
				queryString[i] = str.substring(0, index) + "MUT_GENE "+str.substring(index + (new String("MUT_GENE AS RPR_SYMBOL")).length());
			    }
			    
			    index = queryString[i].lastIndexOf("RPR_SYMBOL");
			    while (-1 != index) {
				str = queryString[i];
				queryString[i] = str.substring(0, index) + "MUT_GENE "+str.substring(index + (new String("RPR_SYMBOL")).length());
				index = queryString[i].lastIndexOf("RPR_SYMBOL");
			    }
			    ////////!!!!!!

    			prepStmt = conn.prepareStatement(queryString[i]);

    			if (param != null && param[i] != null) { // set query criteria if it's not null
    				int parameterNumber = param[i].length;
    				for (int j = 0; j < parameterNumber; j++) {
    					prepStmt.setString(j + 1, param[i][j]);
    				}
    			}

    			resSet = prepStmt.executeQuery();
    			result[i][1] = getStringValueFromIntegerResultSet(resSet);
    		}
    		// close the connection
    		DBHelper.closePreparedStatement(prepStmt);
//			DBUtil.closeJDBCConnection(conn);
    	} catch (SQLException se) {
    		se.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * xingjun - 08/09/2011 - copied from ISHDevDAO
     */
    private String getStringValueFromIntegerResultSet(ResultSet resSet) {
        try {
            if (resSet.first()) {
                int integerResult = resSet.getInt(1);
                return Integer.toString(integerResult);
            } else {
                return null;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * @author xingjun - 26/08/2008
     * <p>xingjun - 08/09/2011 - modified to get correct sequence of columns</p>
     */
    private ArrayList formatBrowseResultSet(ResultSet resSet) throws SQLException {
        if (resSet.first()) {
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create array to store each row of results in
            ArrayList<String[]> results = new ArrayList<String[]>();
            while (resSet.next()) {
                String[] ishBrowseSubmission = new String[13];
                ishBrowseSubmission[0] = resSet.getString(1); // id
                ishBrowseSubmission[1] = resSet.getString(2); // symbol
                ishBrowseSubmission[2] = resSet.getString(3); // ts
                ishBrowseSubmission[3] = resSet.getString(4); // age
                ishBrowseSubmission[4] = resSet.getString(5); // lab
//                ishBrowseSubmission[5] = resSet.getString(6); // date
                ishBrowseSubmission[5] = resSet.getString(8); // date
                String assay = resSet.getString(7);
                ishBrowseSubmission[6] = (assay.equals("TG")? "Tg":assay); // assay
//                ishBrowseSubmission[7] = resSet.getString(8); // specimen
                ishBrowseSubmission[7] = resSet.getString(6); // specimen
//                ishBrowseSubmission[8] = resSet.getString(9); // sex
                ishBrowseSubmission[8] = resSet.getString(11); // sex
//                ishBrowseSubmission[9] = resSet.getString(10); // probe name
                ishBrowseSubmission[9] = resSet.getString(12); // probe name
//                ishBrowseSubmission[10] = resSet.getString(11); // genotype
                ishBrowseSubmission[10] = resSet.getString(13); // genotype
//                ishBrowseSubmission[11] = resSet.getString(12); // probe type
                ishBrowseSubmission[11] = resSet.getString(14); // no probe type for tg data
//                ishBrowseSubmission[12] = resSet.getString(13); // thumbnail
                ishBrowseSubmission[12] = resSet.getString(9); // thumbnail
                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }
    
    // xingjun - 08/09/2011 - now use the sql from InsituDBQuery
    public int getTotalNumberOfSubmissions(String[] organ, GenericTableFilter filter) {
        int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
//    	parQ = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_TG");
    	parQ = InsituDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_TG");
        String query = parQ.getQuerySQL();
 		String organsql = "";
		if(null != organ) {
			String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
			String ids = "";
			  for(int i = 0; i < emapids.length; i++) {
				  ids += "'"+emapids[i] + "',";
			  }
			  if(emapids.length >= 1) {
				  ids = ids.substring(0, ids.length()-1);
			  }
			
			organsql =" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
		         " ANAD_RELATIONSHIP_TRANSITIVE, "+
		         " ANA_TIMED_NODE DESCEND_ATN, "+
		         " ANA_NODE, "+
		         " ANAD_PART_OF "+
		    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
		      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
		      //" and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+
		      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
		      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
		      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
		      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
		} else { // remove redundant join to speed up query
			query = query.replace("LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
		}
         PreparedStatement prepStmt = null;
         query += organsql;
	 if (debug)
	     System.out.println("TransgenicDAO:getTotalNumberOfSubmissions:sql (pre filter): " + query);
         
         if(filter!=null)
 	  	  	query = filter.addFilterSql(query, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
         
	 if (debug)
	     System.out.println("TransgenicDAO:getTotalNumberOfSubmissions:sql (post filter): " + query);
         
	 ///////!!!!! poor databse table for different type of submissions
	    ////////!!!! do not know why replaceAll does not work
	    int index = query.lastIndexOf("MUT_GENE AS RPR_SYMBOL");
	    String str = null;
	    if (-1 != index) {
		str = query;
		query = str.substring(0, index) + "MUT_GENE "+str.substring(index + (new String("MUT_GENE AS RPR_SYMBOL")).length());
	    }
	    
	    index = query.lastIndexOf("RPR_SYMBOL");
	    while (-1 != index) {
		str = query;
		query = str.substring(0, index) + "MUT_GENE "+str.substring(index + (new String("RPR_SYMBOL")).length());
		index = query.lastIndexOf("RPR_SYMBOL");
	    }
	    ////////!!!!!!

         parQ = null;
         parQ = new ParamQuery("TOTAL_COUNT", query);
         try {
             parQ.setPrepStat(conn);
             prepStmt = parQ.getPrepStat();
             resSet = prepStmt.executeQuery();

             if (resSet.first()) {
                 totalNumber = resSet.getInt(1);
             }
             DBHelper.closePreparedStatement(prepStmt);

         } catch (SQLException se) {
             se.printStackTrace();
         }
 		return totalNumber;
    }    
   
}
