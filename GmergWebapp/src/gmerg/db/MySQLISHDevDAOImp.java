/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.Probe;
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
public class MySQLISHDevDAOImp implements ISHDevDAO {

    private Connection conn;

    // default constructor
    public MySQLISHDevDAOImp() {

    }

    // constructor with connection initialisation
    public MySQLISHDevDAOImp(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * <p>modified by xingjun - 15/09/2009
     *  - added extra part into the query string to restrict to ISH and IHC assay type </p>
     */
    public ArrayList getAllSubmissionInsitu(int columnIndex, boolean ascending, int offset, int num, GenericTableFilter filter) {
    	
        ResultSet resSet = null;
        ArrayList result = null;
        ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ISH");
        PreparedStatement prepStmt = null;

        // assemble the query string
        // added extra part into query - 15/09/2009 
//        String query = parQ.getQuerySQL();
        String query = parQ.getQuerySQL() + AdvancedSearchDBQuery.getAssayType(new String[]{"ISH", "IHC"});
        String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
        String queryString =
        	assembleBrowseSubmissionQueryStringISH(1, query, defaultOrder, columnIndex, ascending, offset, num);
//        System.out.println("ISHDevDAO:getAllSubmissionInsitu:queryString: " + queryString);
        
		if(filter!=null)
			queryString = filter.addFilterSql(queryString, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
        
        // execute query and assemble result
        try {
            parQ.setQuerySQL(queryString);
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
        return result;
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
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
    	
		String queryString = null;
		
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseSubmissionOrderByColumnISH(queryType, columnIndex, ascending);
			
			queryString += column;
			
			// remove the trailing ',' character
			//int len = queryString.length();
			//queryString = queryString.substring(0, len-1);
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
//			queryString = query + " ORDER BY TRIM(RPR_SYMBOL)";
			queryString = query + defaultOrder+ ", SUB_EMBRYO_STG";
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
    			"PER_SURNAME", // 4
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
        			orderByString = "CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED) " + 
        			order +", " + geneSymbolCol;
        		} else if (queryType == 2) {
        			
        		}
        	} else if (columnIndex == 1) {
       			orderByString = geneSymbolCol + " " + order +", SUB_EMBRYO_STG "; 
        	} else if (columnIndex == 2) {
        		orderByString = "SUB_EMBRYO_STG" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 3) {
//        		orderByString = "CONCAT(SPN_STAGE,SPN_STAGE_FORMAT)" + " " + order +", " + geneSymbolCol;
        		orderByString = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" + " " + order +", " + geneSymbolCol;
        	} else if (columnIndex == 4) {
        		orderByString = "PER_SURNAME" + " " + order +", " + geneSymbolCol;
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
                ishBrowseSubmission[ 5] = resSet.getString(8); // date
                ishBrowseSubmission[ 6] = resSet.getString(7); // assay
                ishBrowseSubmission[ 7] = resSet.getString(6); // specimen
                ishBrowseSubmission[ 8] = resSet.getString(11); // sex
                ishBrowseSubmission[ 9] = resSet.getString(12); // probe name
                ishBrowseSubmission[10] = resSet.getString(13); // genotype
                ishBrowseSubmission[11] = resSet.getString(14); // probe type
                if(resSet.getString(7).trim().equalsIgnoreCase("OPT")){
                	ishBrowseSubmission[12] = resSet.getString(9).substring(0, resSet.getString(9).lastIndexOf(".")) + ".jpg"; //thumbnail for OPT data
                }
                else {
                	ishBrowseSubmission[12] = resSet.getString(9); // thumbnail
                }
                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }
    
    private ArrayList formatBrowseResultSetMixed(ResultSet resSet) throws SQLException {
    	
        if (resSet.first()) {
            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create array to store each row of results in
            ArrayList<String[]> results = new ArrayList<String[]>();
            int columnNumber = 17;

            while (resSet.next()) {
                String[] ishBrowseSubmission = new String[columnNumber];
                ishBrowseSubmission[ 0] = resSet.getString(1); // id
                ishBrowseSubmission[ 1] = resSet.getString(2); // symbol
                ishBrowseSubmission[ 2] = resSet.getString(3); // ts
                ishBrowseSubmission[ 3] = resSet.getString(4); // age
                ishBrowseSubmission[ 4] = resSet.getString(5); // lab
                ishBrowseSubmission[ 5] = resSet.getString(8); // date
                ishBrowseSubmission[ 6] = resSet.getString(7); // assay
                ishBrowseSubmission[ 7] = resSet.getString(6); // specimen
                ishBrowseSubmission[ 8] = resSet.getString(9); // sex
                ishBrowseSubmission[ 9] = resSet.getString(10); // probe name
                ishBrowseSubmission[10] = resSet.getString(11); // genotype
                ishBrowseSubmission[11] = resSet.getString(12); // probe type
                ishBrowseSubmission[12] = resSet.getString(13); // thumbnail
                ishBrowseSubmission[13] = resSet.getString(14); // tissue
                ishBrowseSubmission[14] = resSet.getString(15); // sample title
                ishBrowseSubmission[15] = resSet.getString(16); // sample description
                ishBrowseSubmission[16] = resSet.getString(17); // series id
                results.add(ishBrowseSubmission);
            }
            return results;
        }
    	return null;
    }

    /**
     * 
     * @param submissionIds
     * @return
     */
    public String getCollectionTotalsSubmissionISHQuerySection(String [] submissionIds) {
        if(submissionIds == null)
        	return "";
       	String  submissionIdsClause = " ";
   	    int submissionNumber = submissionIds.length;
    	if (submissionNumber == 1) {
    	   	submissionIdsClause += "AND SUB_ACCESSION_ID = '" + submissionIds[0] + "' ";
    	} else {
    	   	submissionIdsClause += "AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + " '";
    	   	for (int i = 1; i < submissionNumber; i++) {
    	      	submissionIdsClause += ", '" + submissionIds[i] + " '";
    	    }
    	    submissionIdsClause += ") ";
    	}
    	return submissionIdsClause;
      }	  
    
    /**
     * modified by xingjun 04/01/2008 - changed the column name to follow naming change in DB
    * <p>modified by xingjun - 28/09/2009
    * - add group by clause into the sql to merge tissue type column for the same submission</p>
     */
    public String getCollectionTotalsQueryEndingClause(String [] submissionIds) {

        ParamQuery parQIS = null;
        ParamQuery parQArray = null; 
        String queryIS = null;
        String queryArray = null;
        String endingClause = null;

        if(submissionIds == null) return "";
        
        parQIS = DBQuery.getParamQuery("COLLECTION_SUBMISSION_IN_SITU_PUBLIC");
        parQArray = DBQuery.getParamQuery("COLLECTION_SUBMISSION_ARRAY_PUBLIC");

        // assemble the query string
        queryIS = parQIS.getQuerySQL();
        queryArray = parQArray.getQuerySQL();

        // append submission id condition
        int submissionNumber = submissionIds.length;
        String groupByClause = "GROUP BY QMC_SUB_ACCESSION_ID, QMC_SUB_EMBRYO_STG, QSC_AGE, QMC_PER_NAME, QMC_SUB_SUB_DATE, QMC_SPN_SEX, QSC_SPN_WILDTYPE, SMP_TITLE, QMC_SER_GEO_ID ";
        if (submissionNumber == 1) {
//            queryIS += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
            queryIS += "AND QIC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
//            queryArray += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
            // modified by xingjun - 28/09/2009
//            queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
            queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "' " + groupByClause;
        } else {
//            queryIS += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            queryIS += "AND QIC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
//            queryArray += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            queryArray += "AND QMC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            for (int i = 1; i < submissionNumber; i++) {
                queryIS += ", '" + submissionIds[i] + "'";
                queryArray += ", '" + submissionIds[i] + "'";
            }
            queryIS += ") ";
//          queryArray += ") ";
            // modified by xingjun - 28/09/2009
            queryArray += ") " + groupByClause;
        }
        endingClause = " ((" + queryIS + ")" +  " UNION " + "(" + queryArray + " )) AS QSC_COMBINED ";
//        System.out.println("ISHDevDAO:getCollectionTotalsQueryEndingClause:sql: " + endingClause);
    	return endingClause;
    }	  
    
    public String getCollectionTotalsQueryEndingClause(int userPrivilege, String [] submissionIds) {

        ParamQuery parQIS = null;
        ParamQuery parQArray = null; 
        String queryIS = null;
        String queryArray = null;
        String endingClause = null;

        if(submissionIds == null) return "";
        
        parQIS = DBQuery.getParamQuery("COLLECTION_SUBMISSION_IN_SITU_PUBLIC");
        parQArray = DBQuery.getParamQuery("COLLECTION_SUBMISSION_ARRAY_PUBLIC");

        // assemble the query string
        queryIS = parQIS.getQuerySQL();
        queryArray = parQArray.getQuerySQL();

        // append submission id condition
        int submissionNumber = submissionIds.length;
        String groupByClause = "GROUP BY QMC_SUB_ACCESSION_ID, QMC_SUB_EMBRYO_STG, QSC_AGE, QMC_PER_NAME, QMC_SUB_SUB_DATE, QMC_SPN_SEX, QSC_SPN_WILDTYPE, SMP_TITLE, QMC_SER_GEO_ID ";
        if (submissionNumber == 1) {
//            queryIS += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
            queryIS += "AND QIC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
//            queryArray += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
            // modified by xingjun - 28/09/2009
//            queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
            queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "' " + groupByClause;
        } else {
//            queryIS += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            queryIS += "AND QIC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
//            queryArray += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            queryArray += "AND QMC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
            for (int i = 1; i < submissionNumber; i++) {
                queryIS += ", '" + submissionIds[i] + "'";
                queryArray += ", '" + submissionIds[i] + "'";
            }
            queryIS += ") ";
//          queryArray += ") ";
            // modified by xingjun - 28/09/2009
            queryArray += ") " + groupByClause;
        }
        // restrict the result to entries whose viewability depend on given privilege
        int subStatus = DBHelper.getSubStatusByPrivilege(userPrivilege);
        queryIS += "AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK <= " + subStatus;
        
        endingClause = " ((" + queryIS + ")" +  " UNION " + "(" + queryArray + " )) AS QSC_COMBINED ";
        
    	return endingClause;
    }	  
    
    /**
	 * @param param - string value to specify the criteria of the query therein
	 * @param query - query names
	 * @return query names and query results stored in a 2-dim array
	 */
    public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query, GenericTableFilter filter) {
    	return getStringArrayFromBatchQuery(param, query, "", filter);
    }

    /**
     * <p>modified by xingjun - 06/10/2009 - modified the sql assembling part 
     *  - (refer to the comment in the code)</p>
	 * @param param - string value to specify the criteria of the query therein
	 * @param query - query names
	 * @param endingClause - a string to append at the end of all queries
	 * @return query names and query results stored in a 2-dim array
	 */
    public String[][] getStringArrayFromBatchQuery(String[][] param,
                                                   String[] query, String endingClause, GenericTableFilter filter) {

        int queryNumber = query.length;
        String[][] result = new String[queryNumber][2];
        String[] queryString = new String[queryNumber];

        // get the query sql based on query name and record query name in result string array
        for (int i = 0; i < queryNumber; i++) {
            ParamQuery parQ = DBQuery.getParamQuery(query[i]);
//            System.out.println("Qn: "  + query[i]);
//            System.out.println("Qv: "  + parQ.getQuerySQL());
//            queryString[i] = parQ.getQuerySQL()  + endingClause;
            // endingClause will only be used by EntriesCollectionBrowseAssembler and will
            // use different assembled sql
            if (endingClause == null || endingClause.equals("")) {
                queryString[i] = parQ.getQuerySQL()  + AdvancedSearchDBQuery.getAssayType(new String[]{"ISH", "IHC"}) + endingClause; 
            } else {
                queryString[i] = parQ.getQuerySQL()  + endingClause; 
            }
            if (filter!=null)
            	queryString[i] = filter.addFilterSql(queryString[i], AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
//            System.out.println("Q: "  + queryString[i]);
            result[i][0] = query[i];
        }

        // start to execute query
        //		Connection conn = DBUtil.getDBConnection();
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
            for (int i = 0; i < queryNumber; i++) {
                prepStmt = conn.prepareStatement(queryString[i]);
//                System.out.println("sql: " + queryString[i]);
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
   
   /**
    * 
    */
   public int getTotalNumberOfSubmissions(GenericTableFilter filter) {

        int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION");
        
        // Added by Mehran
//    	String queryString = parQ.getQuerySQL();
    	String queryString = parQ.getQuerySQL() + AdvancedSearchDBQuery.getAssayType(new String[]{"ISH", "IHC"});
        if(filter!=null) 
			queryString = filter.addFilterSql(queryString, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
        
        PreparedStatement prepStmt = null;
//        System.out.println("ISHDevDAO:getTotalNumberOfSubmissions:sql: " + queryString);

        try {
            prepStmt = conn.prepareStatement(queryString);

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
		return totalNumber;
	}
   
   /**
    * 
    * @return
    */
   public ArrayList getSubmissionBySubmissionId(String[] submissionIds,
		   int columnIndex, boolean ascending, int offset, int num) {
	   
       ResultSet resSet = null;
       ArrayList result = null;
       ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES");
       PreparedStatement prepStmt = null;

       // assemble the query string
       String query = parQ.getQuerySQL();

       // append submission id condition
       int submissionNumber = submissionIds.length;
       if (submissionNumber == 1) {
           query += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
       } else {
           query += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
           for (int i = 1; i < submissionNumber; i++) {
               query += ", '" + submissionIds[i] + "'";
           }
           query += ") ";
       }

       String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
       String queryString =
    	   assembleBrowseSubmissionQueryStringISH(1, query, defaultOrder, columnIndex, ascending, offset, num);
//       System.out.println("collection query String: " + queryString);

       // execute query and assemble result
       try {
           prepStmt = conn.prepareStatement(queryString);
           resSet = prepStmt.executeQuery();
           result = formatBrowseResultSet(resSet);

           // close the db object
           DBHelper.closePreparedStatement(prepStmt);

       } catch (SQLException se) {
           se.printStackTrace();
       }
       return result;
   }
   
   /**
    * only return public collection submission entries
    * <p>modified by xingjun on 04/01/2008 - modified sql to make it comply with changed DB table column names</p>
    * <p>modified by xingjun - 28/09/2009 - add group by clause into the sql to merge tissue type column for the same submission</p>
    * type: 0 all types of submissions
    *       1 ish/in situ submissions
    *       2 microarray submissions
    */
   public ArrayList getPublicCollectionSubmissionBySubmissionId(int type, String[] submissionIds,
		   int columnIndex, boolean ascending, int offset, int num) {
//	   System.out.println("ISHDevDAO:getPublicCollectionSubmissionBySubmissionId!!!!!");
//	   System.out.println("ISHDevDAO:getPublicCollectionSubmissionBySubmissionId:type" + type);
       ResultSet resSet = null;
       ArrayList result = null;
       PreparedStatement prepStmt = null;
       ParamQuery parQIS = null;
       ParamQuery parQArray = null; 
       String queryIS = null;
       String queryArray = null;
       String query = null;
       String defaultOrder = null;
       String queryString = null;
       
       if (type == 0) { // all types of submissions
           parQIS = DBQuery.getParamQuery("COLLECTION_SUBMISSION_IN_SITU_PUBLIC");
           parQArray = DBQuery.getParamQuery("COLLECTION_SUBMISSION_ARRAY_PUBLIC");

           // assemble the query string
           queryIS = parQIS.getQuerySQL();
           queryArray = parQArray.getQuerySQL();

           // append submission id condition
           int submissionNumber = submissionIds.length;
           String groupByClause = "GROUP BY QMC_SUB_ACCESSION_ID, QMC_SUB_EMBRYO_STG, QSC_AGE, QMC_PER_NAME, QMC_SUB_SUB_DATE, QMC_SPN_SEX, QSC_SPN_WILDTYPE, SMP_TITLE, QMC_SER_GEO_ID ";
           if (submissionNumber == 1) {
//               queryIS += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
               queryIS += "AND QIC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
//               queryArray += "AND QSC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
               // modified by xingjun - 28/09/2009
//               queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
               queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "' " + groupByClause;
           } else {
//               queryIS += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               queryIS += "AND QIC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
//               queryArray += "AND QSC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               queryArray += "AND QMC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               for (int i = 1; i < submissionNumber; i++) {
                   queryIS += ", '" + submissionIds[i] + "'";
                   queryArray += ", '" + submissionIds[i] + "'";
               }
               queryIS += ") ";
//               queryArray += ") ";
               // modified by xingjun - 28/09/2009
               queryArray += ") " + groupByClause;
           }
           query = "(" + queryIS + ")" +  " UNION " + "(" + queryArray + " )";

           defaultOrder = " ORDER BY natural_sort(TRIM(QSC_RPR_SYMBOL))";
           queryString =
        	   assembleBrowseSubmissionQueryString(1, query, defaultOrder, columnIndex, ascending, offset, num);
//           System.out.println("ISHDevDAO:getPublicSubmissionBySubmissionId:sql: " + queryString);
       
       } else if (type == 1) { // in situ
        StringBuffer sb = new StringBuffer(DBQuery.getISH_BROWSE_ALL_COLUMNS()+DBQuery.ISH_BROWSE_ALL_TABLES+DBQuery.PUBLIC_ENTRIES_Q);
//        String  submissionIdsClause = " ";
            int submissionNumber = submissionIds.length;
        if (submissionNumber == 1) {
                sb.append("AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'");
        } 
        else {
            sb.append("AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'");
            for (int i = 1; i < submissionNumber; i++) {
                sb.append(", '" + submissionIds[i] + "'");
            }
            sb.append(") ");
        }
        defaultOrder = " ORDER BY natural_sort(TRIM(RPR_SYMBOL))";
        queryString =
                assembleBrowseSubmissionQueryStringISH(1, sb.toString(), defaultOrder, columnIndex, ascending, offset, num);
    	   
       } else if (type == 2) {// array
    	   
       }

       // execute query and assemble result
       try {
           prepStmt = conn.prepareStatement(queryString);
           resSet = prepStmt.executeQuery();
           
           if(type == 0) { //mixed
               result = formatBrowseResultSetMixed(resSet);
           }
           else if (type == 1){ //ish
               result = this.formatBrowseResultSet(resSet);
           }
           else { //array
               
           }

           // close the db object
           DBHelper.closePreparedStatement(prepStmt);

       } catch (SQLException se) {
           se.printStackTrace();
       }
       return result;
   }
   
   /**
    * @author xingjun - 08/10/2009
    * return all collection submission entries - no matter they are public or not
    * 
    */
   public ArrayList getAllCollectionSubmissionBySubmissionId(int type, String[] submissionIds,
		   int columnIndex, boolean ascending, int offset, int num) {
//	   System.out.println("ISHDevDAO:getAllSubmissionBySubmissionId!!!!!");
//	   System.out.println("ISHDevDAO:getAllSubmissionBySubmissionId:type" + type);
       ResultSet resSet = null;
       ArrayList result = null;
       PreparedStatement prepStmt = null;
       ParamQuery parQIS = null;
       ParamQuery parQArray = null; 
       String queryIS = null;
       String queryArray = null;
       String query = null;
       String defaultOrder = null;
       String queryString = null;
       
       if (type == 0) { // all types of submissions
           parQIS = DBQuery.getParamQuery("ALL_COLLECTION_ENTRIES_ISH");
           parQArray = DBQuery.getParamQuery("COLLECTION_SUBMISSION_ARRAY_PUBLIC");

           // assemble the query string
           queryIS = parQIS.getQuerySQL();
           queryArray = parQArray.getQuerySQL();

           // append submission id condition
           int submissionNumber = submissionIds.length;
           String groupByClause = "GROUP BY QMC_SUB_ACCESSION_ID, QMC_SUB_EMBRYO_STG, QSC_AGE, QMC_PER_NAME, QMC_SUB_SUB_DATE, QMC_SPN_SEX, QSC_SPN_WILDTYPE, SMP_TITLE, QMC_SER_GEO_ID ";
           if (submissionNumber == 1) {
               queryIS += "AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
               queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "' " + groupByClause;
           } else {
               queryIS += "AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               queryArray += "AND QMC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               for (int i = 1; i < submissionNumber; i++) {
                   queryIS += ", '" + submissionIds[i] + "'";
                   queryArray += ", '" + submissionIds[i] + "'";
               }
               queryIS += ") ";
               queryArray += ") " + groupByClause;
           }
           query = "(" + queryIS + ")" +  " UNION " + "(" + queryArray + " )";

           defaultOrder = " ORDER BY natural_sort(TRIM(QSC_RPR_SYMBOL))";
           queryString =
        	   assembleBrowseSubmissionQueryString(1, query, defaultOrder, columnIndex, ascending, offset, num);
           System.out.println("ISHDevDAO:getAllSubmissionBySubmissionId:sql: " + queryString);
       
       } else if (type == 1) { // in situ
        StringBuffer sb = new StringBuffer(DBQuery.getISH_BROWSE_ALL_COLUMNS()+DBQuery.ISH_BROWSE_ALL_TABLES+DBQuery.PUBLIC_ENTRIES_Q);
            int submissionNumber = submissionIds.length;
        if (submissionNumber == 1) {
                sb.append("AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'");
        } else {
            sb.append("AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'");
            for (int i = 1; i < submissionNumber; i++) {
                sb.append(", '" + submissionIds[i] + "'");
            }
            sb.append(") ");
        }
        defaultOrder = " ORDER BY natural_sort(TRIM(RPR_SYMBOL))";
        queryString =
                assembleBrowseSubmissionQueryStringISH(1, sb.toString(), defaultOrder, columnIndex, ascending, offset, num);
    	   
       } else if (type == 2) {// array
    	   
       }

       // execute query and assemble result
       try {
           prepStmt = conn.prepareStatement(queryString);
           resSet = prepStmt.executeQuery();
           
           if(type == 0) { //mixed
               result = formatBrowseResultSetMixed(resSet);
           }
           else if (type == 1){ //ish
               result = this.formatBrowseResultSet(resSet);
           }
           else { //array
               
           }

           // close the db object
           DBHelper.closePreparedStatement(prepStmt);

       } catch (SQLException se) {
           se.printStackTrace();
       }
       return result;
   }

   /**
    * @author xingjun - 13/10/2009
    * @return
    */
   public ArrayList getCollectionSubmissionBySubmissionId(int userPrivilege, int type, String[] submissionIds,
		   int columnIndex, boolean ascending, int offset, int num) {
//	   System.out.println("ISHDevDAO:getAllSubmissionBySubmissionId!!!!!");
//	   System.out.println("ISHDevDAO:getAllSubmissionBySubmissionId:type" + type);
       ResultSet resSet = null;
       ArrayList result = null;
       PreparedStatement prepStmt = null;
       ParamQuery parQIS = null;
       ParamQuery parQArray = null; 
       String queryIS = null;
       String queryArray = null;
       String query = null;
       String defaultOrder = null;
       String queryString = null;
       
       if (type == 0) { // all types of submissions
           parQIS = DBQuery.getParamQuery("ALL_COLLECTION_ENTRIES_ISH");
           parQArray = DBQuery.getParamQuery("COLLECTION_SUBMISSION_ARRAY_PUBLIC");

           // assemble the query string
           queryIS = parQIS.getQuerySQL();
           queryArray = parQArray.getQuerySQL();

           // append submission id condition
           int submissionNumber = submissionIds.length;
           String groupByClause = "GROUP BY QMC_SUB_ACCESSION_ID, QMC_SUB_EMBRYO_STG, QSC_AGE, QMC_PER_NAME, QMC_SUB_SUB_DATE, QMC_SPN_SEX, QSC_SPN_WILDTYPE, SMP_TITLE, QMC_SER_GEO_ID ";
           if (submissionNumber == 1) {
               queryIS += "AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'";
               queryArray += "AND QMC_SUB_ACCESSION_ID = '" + submissionIds[0] + "' " + groupByClause;
           } else {
               queryIS += "AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               queryArray += "AND QMC_SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'";
               for (int i = 1; i < submissionNumber; i++) {
                   queryIS += ", '" + submissionIds[i] + "'";
                   queryArray += ", '" + submissionIds[i] + "'";
               }
               queryIS += ") ";
               queryArray += ") " + groupByClause;
           }
           // restrict the result to entries whose viewability depend on given privilege
           int subStatus = DBHelper.getSubStatusByPrivilege(userPrivilege);
           queryIS += "AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK <= " + subStatus;
           
           query = "(" + queryIS + ")" +  " UNION " + "(" + queryArray + " )";

           defaultOrder = " ORDER BY natural_sort(TRIM(QSC_RPR_SYMBOL))";
           queryString =
        	   assembleBrowseSubmissionQueryString(1, query, defaultOrder, columnIndex, ascending, offset, num);
//           System.out.println("ISHDevDAO:getAllSubmissionBySubmissionId:sql: " + queryString);
       
       } else if (type == 1) { // in situ
        StringBuffer sb = new StringBuffer(DBQuery.getISH_BROWSE_ALL_COLUMNS()+DBQuery.ISH_BROWSE_ALL_TABLES+DBQuery.PUBLIC_ENTRIES_Q);
            int submissionNumber = submissionIds.length;
        if (submissionNumber == 1) {
                sb.append("AND SUB_ACCESSION_ID = '" + submissionIds[0] + "'");
        } else {
            sb.append("AND SUB_ACCESSION_ID IN ('" + submissionIds[0] + "'");
            for (int i = 1; i < submissionNumber; i++) {
                sb.append(", '" + submissionIds[i] + "'");
            }
            sb.append(") ");
        }
        defaultOrder = " ORDER BY natural_sort(TRIM(RPR_SYMBOL))";
        queryString =
                assembleBrowseSubmissionQueryStringISH(1, sb.toString(), defaultOrder, columnIndex, ascending, offset, num);
    	   
       } else if (type == 2) {// array
    	   
       }

       // execute query and assemble result
       try {
           prepStmt = conn.prepareStatement(queryString);
           resSet = prepStmt.executeQuery();
           
           if(type == 0) { //mixed
               result = formatBrowseResultSetMixed(resSet);
           }
           else if (type == 1){ //ish
               result = this.formatBrowseResultSet(resSet);
           }
           else { //array
               
           }

           // close the db object
           DBHelper.closePreparedStatement(prepStmt);

       } catch (SQLException se) {
           se.printStackTrace();
       }
       return result;
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
   private String assembleBrowseSubmissionQueryString(int queryType, String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
	   
		String queryString = null;
		
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseSubmissionOrderByColumn(queryType, columnIndex, ascending);
			queryString += column;
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
//			queryString = query + " ORDER BY TRIM(RPR_SYMBOL)";
			queryString = query + defaultOrder + ", QSC_SUB_EMBRYO_STG";
		}
		
		// offset and retrieval number
		queryString = queryString + " LIMIT " + offset + " ," + num;
		
		// return assembled query string
		return queryString;
   }
   
   // queryType: 1- for browse and gene query; 2- for component query; 3- for component count query
   private String getBrowseSubmissionOrderByColumn(int queryType, int columnIndex, boolean ascending) {
	   
	   String orderByString = new String("");
	   String order = (ascending == true ? "ASC": "DESC");
	   String[] browseAllColumnList =
		   {"QSC_SUB_ACCESSION_ID", // 0
		   		"QSC_RPR_SYMBOL", // 1
		   		"QSC_SUB_EMBRYO_STG", // 2
		   		"QSC_AGE", // 3
		   		"QSC_PER_NAME", // 4
		   		"QSC_SPN_ASSAY_TYPE", // 7
		   		"QSC_ASSAY_TYPE", // 6
		   		"QSC_SUB_SUB_DATE", // 5
		   		"QSC_SPN_SEX", // 8
		   		"QSC_PRB_PROBE_NAME", // 9
		   		"QSC_SPN_WILDTYPE", // 10
		   		"QSC_PROBE_TYPE", // 11
		   		"QSC_SUB_THUMBNAIL", // 12
		   		"QSC_TISSUE", // 13
		   		"QSC_SMP_TITLE", // 14
		   		"QSC_SAMPLE_DESCRIPTION", // 15
		   		"QSC_SER_GEO_ID" // 16
		    };
	   
	   String geneSymbolCol;
	   String theilerStageCol = "QSC_SUB_EMBRYO_STG ";
	   
	   if(queryType == 1){
		   geneSymbolCol = " natural_sort(TRIM(QSC_RPR_SYMBOL))";
	   }
	   else if(queryType == 2){
		   geneSymbolCol = " natural_sort(TRIM(PRB_GENE_SYMBOL))";
	   }
	   else {
		   geneSymbolCol = " ";
	   }
	   
	   String defaultCol = geneSymbolCol + ", " + theilerStageCol;
	   
	   // start to translate
	   // if specify column other than gene symbol or stage, 
	   // order by the specified column and the default column (symbol & stage);
	   // if no specified column, use symbol and stage
	   if (queryType == 3) {
		   
	   } else {
		   if (columnIndex == 0) {
			   if (queryType == 1) {
				   orderByString = "natural_sort(QSC_SUB_ACCESSION_ID) " + order + ", " + defaultCol;
			   } else if (queryType == 2) {
				   
			   }
		   } else if (columnIndex == 1) {
			   orderByString = geneSymbolCol + " " + order +", " + theilerStageCol;
		   } else if (columnIndex == 2) {
			   orderByString = theilerStageCol + " " + order +", " + geneSymbolCol;
		   } else if (columnIndex == 3) {
			   orderByString = "QSC_AGE " + order + ", " + defaultCol;
		   } else if (columnIndex == 4) {
			   orderByString = "QSC_PER_NAME " + order + ", " + defaultCol;
		   } else if (columnIndex == 5) {
			   orderByString = "QSC_SUB_SUB_DATE " + order + ", " + defaultCol;
		   } else if (columnIndex == 6) {
			   orderByString = "QSC_ASSAY_TYPE " + order + ", " + defaultCol;
		   } else if (columnIndex == 7) {
			   orderByString = "QSC_SPN_ASSAY_TYPE " + order + ", " + defaultCol;
		   } else if (columnIndex == 8) {
			   orderByString = "QSC_SPN_SEX " + order + ", " + defaultCol;
		   } else if (columnIndex == 9) {
			   orderByString = "QSC_PRB_PROBE_NAME " + order + ", " + defaultCol;
		   } else if (columnIndex == 10) {
			   orderByString = "QSC_SPN_WILDTYPE " + order + ", " + defaultCol;
		   } else if (columnIndex == 11) {
			   orderByString = "QSC_PROBE_TYPE " + order + ", " + defaultCol;
		   } else if (columnIndex == 12) {
			   orderByString = "QSC_SUB_THUMBNAIL " + order + ", " + defaultCol;
		   } else if (columnIndex == 13) {
			   orderByString = "QSC_TISSUE " + order + ", " + defaultCol;
		   } else if (columnIndex == 14) {
			   orderByString = "QSC_SMP_TITLE " + order + ", " + defaultCol;
		   } else if (columnIndex == 15) {
			   orderByString = "QSC_SAMPLE_DESCRIPTION " + order + ", " + defaultCol;
		   } else if (columnIndex == 16) {
			   orderByString = "QSC_SER_GEO_ID " + order + ", " + defaultCol;
		   } else {
			   orderByString = defaultCol;
		   }
	   }
	   return orderByString;
   }
   
   /**
    * 
    */
   public Probe findProbeBySubmissionId(String submissionAccessionId) {
	   
       if (submissionAccessionId == null) {
//           throw new NullPointerException("id parameter");
			return null;
       }

       Probe probeInfo = null;
       ResultSet resSetProbe = null;
       ParamQuery parQProbe = DBQuery.getParamQuery("SUBMISSION_PROBE");
       PreparedStatement prepStmtProbe = null;

       ResultSet resSetProbeNote = null;
       ParamQuery parQProbeNote = DBQuery.getParamQuery("SUBMISSION_PRBNOTE");
       PreparedStatement prepStmtProbeNote = null;

       ResultSet resSetMaprobeNote = null;
       ParamQuery parQMaprobeNote = DBQuery.getParamQuery("SUBMISSION_MAPROBE_NOTE");
       PreparedStatement prepStmtMaprobeNote = null;

       ResultSet resSetFullSequence = null;
       ParamQuery parQFullSequence = DBQuery.getParamQuery("SUBMISSION_FULL_SEQUENCE");
       PreparedStatement prepStmtFullSequence = null;

       try {
           conn.setAutoCommit(false);

           // probe
           parQProbe.setPrepStat(conn);
           prepStmtProbe = parQProbe.getPrepStat();
           prepStmtProbe.setString(1, submissionAccessionId);
           resSetProbe = prepStmtProbe.executeQuery();

           // probe note
           parQProbeNote.setPrepStat(conn);
           prepStmtProbeNote = parQProbeNote.getPrepStat();
           prepStmtProbeNote.setString(1, submissionAccessionId);
           resSetProbeNote = prepStmtProbeNote.executeQuery();
           
           // maprobe note --- 02/05/2007
           parQMaprobeNote.setPrepStat(conn);
           prepStmtMaprobeNote = parQMaprobeNote.getPrepStat();
           prepStmtMaprobeNote.setString(1, submissionAccessionId);
           resSetMaprobeNote = prepStmtMaprobeNote.executeQuery();
	    
	    // maprobe full sequence
           parQFullSequence.setPrepStat(conn);
           prepStmtFullSequence = parQFullSequence.getPrepStat();
           prepStmtFullSequence.setString(1, submissionAccessionId);
           resSetFullSequence = prepStmtFullSequence.executeQuery();

           conn.commit();
           conn.setAutoCommit(true);

           // assemble
           probeInfo = formatProbeResultSet(resSetProbe, resSetProbeNote, resSetMaprobeNote, resSetFullSequence);

           // release db objects
           DBHelper.closePreparedStatement(prepStmtProbe);
           DBHelper.closePreparedStatement(prepStmtProbeNote);
           DBHelper.closePreparedStatement(prepStmtMaprobeNote);
           DBHelper.closePreparedStatement(prepStmtFullSequence);

       } catch (SQLException se) {
           se.printStackTrace();
       }
       return probeInfo;
   }
   
   /**
    * 
    * @param resSetProbe
    * @param resSetProbeNote
    * @param resSetMaprobeNote
    * @param resSetFullSequence
    * @return
    * @throws SQLException
    */
   private Probe formatProbeResultSet(ResultSet resSetProbe, ResultSet resSetProbeNote,
		   ResultSet resSetMaprobeNote, ResultSet resSetFullSequence) throws SQLException {
	   
       Probe probe = null;
       if (resSetProbe.first()) {
           probe = new Probe();
           probe.setGeneSymbol(resSetProbe.getString(1));
           probe.setGeneName(resSetProbe.getString(2));
           String probeName = resSetProbe.getString(3);
           probe.setProbeName(probeName);
           probe.setGeneID(resSetProbe.getString(4));
           probe.setSource(resSetProbe.getString(5));
           probe.setStrain(resSetProbe.getString(6));
           probe.setTissue(resSetProbe.getString(7));
           probe.setType(resSetProbe.getString(8));
           probe.setGeneType(resSetProbe.getString(9));
           probe.setLabelProduct(resSetProbe.getString(10));
           probe.setVisMethod(resSetProbe.getString(11));
           probe.setCloneName(resSetProbe.getString(12));
           probe.setGenbankID(resSetProbe.getString(13));
           probe.setMaprobeID(resSetProbe.getString(14));
           probe.setProbeNameURL(resSetProbe.getString(15));
           probe.setGenbankURL(resSetProbe.getString(16));
           probe.setSeqStatus(resSetProbe.getString(17));
           probe.setSeq5Loc(resSetProbe.getString(18));
           probe.setSeq3Loc(resSetProbe.getString(19));
           probe.setSeq5Primer(resSetProbe.getString(20).toUpperCase());
           probe.setSeq3Primer(resSetProbe.getString(21).toUpperCase());

           // obtain probe note
           if (resSetProbeNote.first()) {
               resSetProbeNote.beforeFirst();
               String notes = new String("");
               while (resSetProbeNote.next()) {
                   notes += resSetProbeNote.getString(1) + " ";
               }
               probe.setNotes(notes.trim());
           }
           
           // obtain maprobe note
           if (resSetMaprobeNote.first()) {
//           	ArrayList<String> maprobeNotes = new ArrayList<String>();
           	String maprobeNotes = "";
               resSetMaprobeNote.beforeFirst();
               String notes = null;
               while (resSetMaprobeNote.next()) {
            	   notes = resSetMaprobeNote.getString(1).replaceAll("\\s", " ").trim();
            	   
            	   if (notes != null && !notes.equals("")) { // dont accept null value or empty string
//                       String[] separatedNote = notes.split("\\|");
//                       if(null != separatedNote && separatedNote.length > 1) {
//                    	   for(int i = 0; i < separatedNote.length; i++) {
//                    		   maprobeNotes.add(separatedNote[i]);
//                    	   }
//                       } else {
//                    	   maprobeNotes.add(notes);
//                       }
            		   maprobeNotes += notes + " ";
            	   }
               }
//               System.out.println("mn size: " + maprobeNotes.size());
//               probe.setMaprobeNotes(maprobeNotes);
               probe.setMaprobeNoteString(maprobeNotes);
           }
           
           // obtain full sequence
           ArrayList<String> seqs = null;
           if (resSetFullSequence.first()) {
        	   resSetFullSequence.beforeFirst();
        	   String fullSeq = new String();
        	   while (resSetFullSequence.next()) {
        		   fullSeq += resSetFullSequence.getString(1);
        	   }
        	   
        	   String origin = null;
        	   int count;                
               if(null != fullSeq) {
            	   seqs = new ArrayList<String>();
            	   origin = fullSeq.trim();
            	   //System.out.println("SEQ:"+origin);
            	   count = origin.length() / 60;
            	   for(int i = 0; i < count; i++) {
            		   seqs.add(origin.substring(i*60, i*60+60));
            	   }
            	   
            	   int remainder = origin.length() / 60;
                   if (remainder > 0) {
                	   seqs.add(origin.substring(count*60));
                   }
               }     	
               probe.setFullSequence(seqs);
           } else {
        	   probe.setFullSequence(null);
           }
       }
       return probe;
   } // end of formatProbeResultSet
   
   public String findProbeNoteBySubmissionId(String submissionAccessionId) {

	   return null;
   }
   
   public String findMaProbeNoteBySubmissionId(String submissionAccessionId) {

	   return null;
   }
   
   public String findProbeFullSequenceBySubmissionId(String submissionAccessionId) {
	   return null;
   }
   
   public ArrayList getAllSubmissionsNonRenal(int columnIndex, boolean ascending, int offset, int num) {
	   
       ArrayList result = null;
       ParamQuery parQ = DBQuery.getParamQuery("ISH_NON_RENAL_SUBMISSIONS");

       // assemble the query string
       String query = parQ.getQuerySQL();
       String defaultOrder = DBQuery.ORDER_BY_REF_PROBE_SYMBOL;
       String queryString =
       	assembleBrowseSubmissionQueryStringISH(1, query, defaultOrder, columnIndex, ascending, offset, num);
//       System.out.println("browseRenalQueryString: " + queryString);
       
       result = getBrowseFormatArrayListFromDatabaseWithoutParameter(queryString);
	   return result;
   }
   
   /**
    * 
    */
   public int getTotalNumberOfNonRenalSubmissions() {
	   
	   String queryName = "ISH_NUMBER_OF_SUBMISSIONS_NON_RENAL";
	   String queryString = DBQuery.getParamQuery(queryName).getQuerySQL();
//	   System.out.println("totalNumberRenalQueryString: " + queryString);
	   int totalNumberOfNonRenalSubmissions = getIntegerFromDatabaseWithoutParameter(queryString);
	   return totalNumberOfNonRenalSubmissions;
   }
   
   /** general purpose retrievals */
   private ArrayList getBrowseFormatArrayListFromDatabaseWithoutParameter(String queryString) {

       ResultSet resSet = null;
       ArrayList result = null;
       PreparedStatement prepStmt = null;

       // execute query and assemble result
       try {
           prepStmt = conn.prepareStatement(queryString);

           // execute
           resSet = prepStmt.executeQuery();
           result = formatBrowseResultSet(resSet);

           // release the database object
           DBHelper.closePreparedStatement(prepStmt);

       } catch (SQLException se) {
           se.printStackTrace();
       }
       return result;
   }
   
   /**
    * 
    * @param queryString
    * @return
    */
   private int getIntegerFromDatabaseWithoutParameter(String queryString) {

       int result = 0;
       ResultSet resSet = null;
       PreparedStatement prepStmt = null;

       try {
           prepStmt = conn.prepareStatement(queryString);

           // execute
           resSet = prepStmt.executeQuery();

           if (resSet.first()) {
               result = resSet.getInt(1);
           }

           // close the db object
           DBHelper.closePreparedStatement(prepStmt);

       } catch (SQLException se) {
           se.printStackTrace();
       }
		return result;
   }
   
}
