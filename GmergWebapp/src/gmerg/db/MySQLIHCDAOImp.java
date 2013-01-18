package gmerg.db;

import gmerg.utils.table.FilterItem;
import gmerg.utils.table.GenericTableFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class MySQLIHCDAOImp implements IHCDAO {

    private boolean debug = false;
    private Connection conn;

    // default constructor
    public MySQLIHCDAOImp() {

    }

    // constructor with connection initialisation
    public MySQLIHCDAOImp(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * 
     */
    public ArrayList getAllSubmissionISH(int columnIndex, boolean ascending, int offset, int num, String assayType, String[] organ, GenericTableFilter filter) {
        ResultSet resSet = null;
        ArrayList result = null;
        if(null != assayType) {
        	
	        ParamQuery parQ = null;
	        if(assayType.equals("IHC")) {
	        	parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_IHC");
	        } else {
	        	parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ISH");
	        }
	        PreparedStatement prepStmt = null;
	
	        // assemble the query string
	        String query = parQ.getQuerySQL();
	        String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
	        String queryString =
	        	assembleBrowseSubmissionQueryStringISH(1, query, defaultOrder, columnIndex, ascending, offset, num, organ);
	        
	        //*******************************************************************************************************************
	        // Temporarily added by Mehran to implement Filter functionality 
	        if (filter!=null) {
				queryString = filter.addFilterSql(queryString, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
	        }	    		
	        //*******************************************************************************************************************

	        
//	        System.out.println("queryString IHC: " + queryString);
	        
	        parQ.setQuerySQL(queryString);
	
	        // execute query and assemble result
	        try {
	            parQ.setPrepStat(conn);
	            prepStmt = parQ.getPrepStat();
	
	            // execute
	            resSet = prepStmt.executeQuery();
	            result = formatBrowseResultSet(resSet);
	
	            // close the connection
	            DBHelper.closePreparedStatement(prepStmt);
	            //			DBUtil.closeJDBCConnection(conn);
	
	            // reset the static query string to its original value
	            parQ.setQuerySQL(query);
	
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
        }
        return result;
    }

    /**
     * 
     */
    public int getTotalNumberOfSubmissions(String assayType, String[] organ, GenericTableFilter filter) {
         int totalNumber = 0;
         ResultSet resSet = null;
         ParamQuery parQ = null;
         if(null != assayType) {
	         
	         if(assayType.equals("IHC")) {
	        	 parQ = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_IHC");
	         } else {
	        	 parQ = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_ISH");
	         }
	 		String organsql = "";
	        String query = parQ.getQuerySQL();
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
			      " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			} else { // remove redundant join to speed up query
				query = query.replace("LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
			}
	        PreparedStatement prepStmt = null;
	        query += organsql;
	         

	        //*******************************************************************************************************************
	        // Temporarily added by Mehran to implement Filter functionality 
			if (filter!=null) {
		  	  	query = filter.addFilterSql(query, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
			}     
	        //*******************************************************************************************************************

//			System.out.println("query Total rows: "+query);     
			parQ = null;
			parQ = new ParamQuery("TOTAL_COUNT", query);

			try {
	        	parQ.setPrepStat(conn);
	            prepStmt = parQ.getPrepStat();
	
	             // execute
	             resSet = prepStmt.executeQuery();
	
	             if (resSet.first()) {
	                 totalNumber = resSet.getInt(1);
	             }
	
	             // close the db object
	             DBHelper.closePreparedStatement(prepStmt);
	
	         } catch (SQLException se) {
	             se.printStackTrace();
	         }
         }
  		return totalNumber;
     }    
    
    /**
     * 
     * @param queryType
     * @param query
     * @param defaultOrder
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    private String assembleBrowseSubmissionQueryStringISH(int queryType, String query,
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
		      " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+
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
			String column = getBrowseSubmissionOrderByColumnISH(queryType, columnIndex, ascending);
			
			queryString += column;
			
			// remove the trailing ',' character
			//int len = queryString.length();
			//queryString = queryString.substring(0, len-1);
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
//			queryString = query + " ORDER BY TRIM(RPR_SYMBOL)";
			queryString = query + organsql + defaultOrder+ ", SUB_EMBRYO_STG";
		}
		
		// offset and retrieval number
		queryString = queryString + " LIMIT " + offset + " ," + num;
		
		// return assembled query string
		return queryString;
    }
    
	/**
	 * 
	 * @param order
	 * @param queryType: 1- for browse and gene query; 2- for component query; 3- for component count query
	 * @return
	 */
    private String getBrowseSubmissionOrderByColumnISH(int queryType, int columnIndex, boolean ascending) {
    	
    	String orderByString = new String("");
    	String order = (ascending == true ? "ASC": "DESC"); 
    	String[] ISHBrowseAllColumnList =
    	{"CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED)", // 0
    			"natural_sort(TRIM(RPR_SYMBOL))", // 1
    			"SUB_EMBRYO_STG", // 2
//    			"CONCAT(SPN_STAGE,SPN_STAGE_FORMAT)", // 3
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
    	
        if(queryType == 1 || queryType == 2){
            geneSymbolCol = "natural_sort(TRIM(RPR_SYMBOL))";
        }
        else {
            geneSymbolCol = "";
        }
        
        // start to translate
        if (queryType == 3) {
        
        } else {
        	if(columnIndex == 0) {
        		if (queryType == 1) {
        			orderByString = "CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED) " + 
        			order +", " + geneSymbolCol;
        		} 
        	} else if (columnIndex == 1) {
       			orderByString = geneSymbolCol + " " + order +", SUB_EMBRYO_STG "; 
        	} else if (columnIndex == 2) {
        		orderByString = "SUB_EMBRYO_STG" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 3) {
//        		orderByString = "CONCAT(SPN_STAGE,SPN_STAGE_FORMAT)" + " " + order +", " + geneSymbolCol;
        		orderByString = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" + " " + order +", " + geneSymbolCol;
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
     * 
     * @param resSet
     * @return
     * @throws SQLException
     */
    private ArrayList formatBrowseResultSet(ResultSet resSet) throws SQLException {
    	
        if (resSet.first()) {
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create array to store each row of results in
            ArrayList<String[]> results = new ArrayList<String[]>();

            while (resSet.next()) {
                String[] ishBrowseSubmission = new String[13];
                ishBrowseSubmission[ 0] = resSet.getString(1); // id
                ishBrowseSubmission[ 1] = resSet.getString(2); // symbol
                ishBrowseSubmission[ 2] = resSet.getString(3); // ts
                ishBrowseSubmission[ 3] = resSet.getString(4); // age
                ishBrowseSubmission[ 4] = resSet.getString(5); // lab
                ishBrowseSubmission[ 5] = resSet.getString(6); // date
                ishBrowseSubmission[ 6] = resSet.getString(7); // assay
                ishBrowseSubmission[ 7] = resSet.getString(8); // specimen
                ishBrowseSubmission[ 8] = resSet.getString(9); // sex
                ishBrowseSubmission[ 9] = resSet.getString(10); // probe name
                ishBrowseSubmission[10] = resSet.getString(11); // genotype
                ishBrowseSubmission[11] = resSet.getString(12); // probe type
                ishBrowseSubmission[12] = resSet.getString(13); // thumbnail
                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }
    

    
    /**
	 * @param param - string value to specify the criteria of the query therein
	 * @param query - query names
	 * @return query names and query results stored in a 2-dim array
	 */
    public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, String assayType) {
    	return getStringArrayFromBatchQuery(param, query, "", assayType);
    }

    /**
	 * @param param - string value to specify the criteria of the query therein
	 * @param query - query names
	 * @param endingClause - a string to append at the end of all queries
	 * @return query names and query results stored in a 2-dim array
	 */
    public String[][] getStringArrayFromBatchQuery(String[][] param,
                                                   String[] query, String endingClause, String assayType) {

        int queryNumber = query.length;
        String[][] result = new String[queryNumber][2];
        String[] queryString = new String[queryNumber];

        // get the query sql based on query name and record query name in result string array
        for (int i = 0; i < queryNumber; i++) {
            ParamQuery parQ = DBQuery.getParamQuery(query[i]);
            queryString[i] = parQ.getQuerySQL() + endingClause; 
//            System.out.println("Q: "  + queryString[i]);
            result[i][0] = query[i];
        }

        // start to execute query
        //		Connection conn = DBUtil.getDBConnection();
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
            for (int i = 0; i < queryNumber; i++) {
		    if (debug)
			System.out.println("MySQLIHCDAOImp.sql = "+queryString[i].toLowerCase());
                prepStmt = conn.prepareStatement(queryString[i]);
                if (param != null &&
                    param[i] != null) { // set query criteria if it's not null
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
	 *
	 * @param resSet
	 * @return
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
  
    
}
