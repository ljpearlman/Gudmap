package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import gmerg.db.AdvancedSearchDBQuery;
import gmerg.utils.Utility;
import gmerg.utils.table.GenericTableFilter;

public class MySQLFocusForAllDAOImp  implements FocusForAllDAO {
    protected boolean debug = true;

    private Connection conn;
    private int MAX_COLUMNS = 12; // added extra column ALE_GENE
    private int MAX_ISH_COLUMNS = 15;//14;
    private int MAX_NGD_COLUMNS = 13;


    // default constructor
    public MySQLFocusForAllDAOImp() {  	
    }

    // constructor with connection initialisation

    public MySQLFocusForAllDAOImp(Connection conn) {
        this.conn = conn;
    }
	
	// get total number of ish submissions
	public int findTotalNumberOfSubmissionISH(String[] emapids)
	{
        int result = 0;
        return result;
	}
	
    // get number of public ish submissions
	public int findNumberOfPublicSubmissionISH(String[] emapids)
	{
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
        	new ParamQuery("NUM_ISH",AdvancedSearchDBQuery.getPublicISHNumber(emapids));
        Statement stmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
            stmt = conn.createStatement();
            
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:findNumberOfPublicSubmissionISH = "+parQ.getQuerySQL());
                        
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }
            return result;
        } catch (SQLException se) {
            se.printStackTrace();
        	return 0;
        }
        finally{
	        DBHelper.closeStatement(stmt);
	        DBHelper.closeResultSet(resSet);        	
        }
	}
	
	// get number of public WISH/SISH/OPT submissions
		public int findNumberOfPublicSubmissionISHTypes(String[] emapids, String type)
		{
	        int result = 0;
	        ResultSet resSet = null;
	        ParamQuery parQ =
	        	new ParamQuery("NUM_ISH_TYPE",AdvancedSearchDBQuery.getPublicISHTypesNumber(emapids,type));
	        Statement stmt = null;
	        try {
	        	// if disconnected from db, re-connected
	        	conn = DBHelper.reconnect2DB(conn);
	            stmt = conn.createStatement();
	            
			    if (debug)
			    	System.out.println("MySQLFocusFowAllDAOImp:findNumberOfPublicSubmissionISHTypes = "+parQ.getQuerySQL());
	                        
	            resSet = stmt.executeQuery(parQ.getQuerySQL());
	            if (resSet.first()) {
	                result = resSet.getInt(1);
	            }
	            return result;
	        } catch (SQLException se) {
	            se.printStackTrace();
	        	return 0;
	        }
	        finally{
		        DBHelper.closeStatement(stmt);
		        DBHelper.closeResultSet(resSet);        	
	        }
		}
		
	
//	 get total number of ihc submissions
	public int findTotalNumberOfSubmissionArray(String[] emapids)
	{
        int result = 0;
        return result;
	}
	
	/**
	 * @author xingjun - 25/09/2009
	 */
	public int findNumberOfPublicSubmissionArray(String[] emapids) {
        int result = 0;
        ResultSet resultSet = null;
		Statement stmt = null;
        ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ARRAY_FOCUS");
        
		// emap ids
		String queryString = parQ.getQuerySQL();
		String ids = "";
		for(int i = 0; i < emapids.length; i++) {
			ids += "'"+emapids[i] + "',";
		}
		if(emapids.length >= 1) {
			ids = ids.substring(0, ids.length()-1);
		}
		queryString += AdvancedSearchDBQuery.findDescendant.replaceAll("COMPONENT_IDS", ids);
		
		// group by clause
		queryString += AdvancedSearchDBQuery.groupBySubmissionArray;
        try {
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:findNumberOfPublicSubmissionArray Sql = "+queryString.toLowerCase());

		    stmt = conn.prepareStatement(queryString);
            resultSet = stmt.executeQuery(queryString);
            
            ArrayList arraySubmissions = this.formatResultSet(resultSet, this.MAX_COLUMNS);
            if (arraySubmissions == null) {
            	result = 0;
            } else {
            	result = arraySubmissions.size();
            }

            return result;
        } catch (SQLException se) {
            se.printStackTrace();
            return 0;
        }
        finally{
	        DBHelper.closeStatement(stmt);
	        DBHelper.closeResultSet(resultSet);        	
        }
	}

	// get number of public ihc sumbissions
	public int findNumberOfPublicSubmissionIHC(String[] emapids)
	{
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
        	new ParamQuery("NUM_IHC",AdvancedSearchDBQuery.getPublicIHCNumber(emapids));
        Statement stmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
            stmt = conn.createStatement();
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:findNumberOfPublicSubmissionIHC sql = "+parQ.getQuerySQL());

		    resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }            
            return result;

        } catch (SQLException se) {
            se.printStackTrace();
            return 0;
        }
        finally{
	        DBHelper.closeStatement(stmt);
	        DBHelper.closeResultSet(resSet);        	
        }
	}
	
	/**
	 * @author xingjun - 28/08/2008
	 * @param emapids
	 * @return
	 */
	public int findNumberOfPublicSubmissionTG(String[] emapids) {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ = new ParamQuery("NUM_TG",AdvancedSearchDBQuery.getPublicTransgenicNumber(emapids));
        Statement stmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
            stmt = conn.createStatement();
            
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:findNumberOfPublicSubmissionTG sql = "+parQ.getQuerySQL());
            
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }

            return result;
        } catch (SQLException se) {
            se.printStackTrace();
            return 0;
        }
        finally{
	        DBHelper.closeStatement(stmt);
	        DBHelper.closeResultSet(resSet);        	
        }
	}
	
	/**
	 * @author xingjun - 02/02/2009
	 * <p>xingjun - 22/09/2011 - transgenic data need different sql to get the number</p>
	 */
	public int findNumberOfPublicGenes(String assayType, String[] emapIds) {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (assayType.equalsIgnoreCase("TG")) {
            parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE_TG");
        } 
     // ISH or IHC 
        else if (assayType.equalsIgnoreCase("ISH") || assayType.equalsIgnoreCase("IHC"))
        { 
            parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE");
	    }
        //section, wholemount, opt-wholemount
        else
        	parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE_ISH_TYPES");

        Statement stmt = null;
        String componentString = "AND EXP_COMPONENT_ID IN " + DBHelper.assembleComponentString(emapIds);
        
        String queryString = null;
        if (assayType.equalsIgnoreCase("TG")) {
        	queryString = parQ.getQuerySQL().replace("JOIN ISH_SUBMISSION ON SAL_SUBMISSION_FK = SUB_OID", 
        			"JOIN ISH_SUBMISSION ON SAL_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID ")
			+ componentString;
        } else {
        	queryString = parQ.getQuerySQL().replace("JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID", 
        			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID ")
			+ componentString;
        }
        
        queryString = queryString.replace("?", "'"+assayType+"'");
            
        try {
	      	// if disconnected from db, re-connected
	      	conn = DBHelper.reconnect2DB(conn);
	        stmt = conn.createStatement(); 
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:findNumberOfPublicGenes sql = "+queryString);

		    resSet = stmt.executeQuery(queryString);
	          
			if (resSet.first()) {
				result = resSet.getInt(1);
			}
	
	        return result;
	    } catch (SQLException se) {
	        se.printStackTrace();
	        return 0;
	    }
        finally{
	        DBHelper.closeStatement(stmt);
	        DBHelper.closeResultSet(resSet);        	
        }

	}
	
	/**
	 * @param orderby
	 * @param asc
	 * @param query
	 * @return
	 */
	public String orderResult(int orderby, boolean asc, String query) {
		String orderStr = null;
		
		String order = (asc == true ? " ASC ": " DESC ");

			if(query.equals("array")) {
				if(orderby < 0) {
					orderStr = " order by " + AdvancedSearchDBQuery.getMICDefaultSort(); 
				} else if(0 == orderby) {
					orderStr = " order by " + AdvancedSearchDBQuery.getMICDefaultSort() + " " + order; 
				} else if(1 == orderby){
					orderStr = " order by natural_sort(SMP_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(2 == orderby){
					orderStr = " order by natural_sort(SER_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(3 == orderby){
					orderStr = " order by SUB_SOURCE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(4 == orderby){
					orderStr = " order by SMP_THEILER_STAGE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(5 == orderby){
					orderStr = " order by TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(6 == orderby){
					orderStr = " order by SUB_SUB_DATE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(7 == orderby){
					orderStr = " order by SMP_SEX " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(8 == orderby){
					orderStr = " order by SRM_SAMPLE_DESCRIPTION " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(9 == orderby){
					orderStr = " order by SMP_TITLE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(10 == orderby){
					orderStr = " order by natural_sort(GENOTYPE) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				} else if(11 == orderby){
					orderStr = " order by GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', ') " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  
			} else if(query.equals("ngd")) {
				if(orderby < 0) {
					orderStr = " order by " + AdvancedSearchDBQuery.getMICDefaultSort(); 
				} else if(0 == orderby) {
					orderStr = " order by " + AdvancedSearchDBQuery.getMICDefaultSort() + " " + order; 
				} else if(1 == orderby){
					orderStr = " order by natural_sort(NGS_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(2 == orderby){
					orderStr = " order by natural_sort(NGR_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();	
				}  else if(3 == orderby){
					orderStr = " order by SUB_SOURCE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(4 == orderby){
					orderStr = " order by NGP_LIBRARY_STRATEGY " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(5 == orderby){
					orderStr = " order by STG_STAGE_DISPLAY " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();	
				}  else if(6 == orderby){
					orderStr = " order by TRIM(CASE NGS_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(NGS_DEV_STAGE,' ',NGS_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',NGS_DEV_STAGE) ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(7 == orderby){
					orderStr = " order by SUB_SUB_DATE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();	
				}  else if(8 == orderby){
					orderStr = " order by NGS_SEX " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();		
				}  else if(9 == orderby){
					orderStr = " order by NGS_DESCRIPTION " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();	
				} else if(10 == orderby){
					orderStr = " order by NGS_SAMPLE_NAME " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				} else if(11 == orderby){
					orderStr = " order by natural_sort(GENOTYPE) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				} else if(12 == orderby){
					orderStr = " order by GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', ') " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}
			}
			else if(query.equals("ish") || query.equals("insitu") || query.equals("insitu_all")) {
				if(orderby < 0) {
					orderStr = " order by " + AdvancedSearchDBQuery.getISHDefaultSort(); 
				} else if(0 == orderby){
					orderStr = " order by NATURAL_SORT(RPR_SYMBOL) " + order + "," + "STG_STAGE_DISPLAY, SPN_SEX";
				} else if(1 == orderby) {
					orderStr = " order by CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort(); 
				}  else if(2 == orderby){
					orderStr = " order by SUB_SOURCE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(3 == orderby){
					orderStr = " order by SUB_SUB_DATE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(4 == orderby){
					orderStr = " order by SUB_ASSAY_TYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(5 == orderby){
					orderStr = " order by NATURAL_SORT(TRIM(RPR_JAX_ACC)) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(6 == orderby){
					orderStr = " order by STG_STAGE_DISPLAY " + order + "," + "NATURAL_SORT(RPR_SYMBOL), SPN_SEX";
				}  else if(7 == orderby){
					orderStr = " order by TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) " 
						+ order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(8 == orderby){
					orderStr = " order by SPN_SEX " + order + "," + "NATURAL_SORT(RPR_SYMBOL), STG_STAGE_DISPLAY, SPN_SEX";
				}  else if(9 == orderby){
					//orderStr = " order by SPN_WILDTYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
					orderStr = " order by natural_sort(GENOTYPE) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(10 == orderby){
					orderStr = " order by ANO_COMPONENT_NAME " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(11 == orderby){
					orderStr = " order by EXP_STRENGTH " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(12 == orderby){
					orderStr = " order by SPN_ASSAY_TYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}   else if(13 == orderby){
					orderStr = " order by CONCAT(IMG_URL.URL_URL, I.IMG_FILEPATH, IMG_URL.URL_SUFFIX, I.IMG_SML_FILENAME) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}   				
			}
		return orderStr;
    }
	

	
	
	/**
	 * overloaded version - xingjun - 15/01/2009
	 * added an extra param - gene
	 * @return
	 */
	public ArrayList getFocusBrowseList(String[] organ, int column, boolean ascending, 
			String query, String stage, String gene, String geneid, String archiveId, String batchId, String specimenType, String offset, String resPerPage, GenericTableFilter filter) {
//		System.out.println("FocusForAll:getFocusBrowseList:column: " + column);
//		System.out.println("query type: " + query);

		final long startTime = System.currentTimeMillis();
		ResultSet resSet = null;
		ArrayList result = null;
		ArrayList result2 = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		if(query.equals("array")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ARRAY_FOCUS");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			// focus group
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
			////////////////// gene -------- to be implemented
			
			// archiveId & batchId
	        if (null != archiveId && !archiveId.equals("")){
	        	sql += " and (SUB_ARCHIVE_ID = " + archiveId + " ) ";
	        }
	        
	        if (null != batchId && !batchId.equals("")){
	        	sql += " and (SUB_BATCH = " + batchId + " ) ";
	        }			
			
			// stage group by and order by - xingjun - 25/09/2009 - added group by clause
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += AdvancedSearchDBQuery.groupBySubmissionArray + orderResult(column, ascending, query);
			} else {
				sql += " and STG_STAGE_DISPLAY='"+stage+"' " + AdvancedSearchDBQuery.groupBySubmissionArray + orderResult(column, ascending, query);
			}
			
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			parQ = null;
			parQ = new ParamQuery("FOCUS_ARRAY_BROWSE",sql);

			if (debug)
			    System.out.println("FocusForAllDAO:getFocusBrowseList:Arraybrowseall:"+parQ.getQuerySQL());
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				if (debug)
					System.out.println("MySQLFocusForAllDAO:getFocusBrowseList - prepStmt" + prepStmt);
				
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_COLUMNS);
				return result;
				/*result = Utility.formatResultSet(resSet);
	    		result2 = Utility.formatGenotypeResultSet(result,getArrayGenotypeBySeriesOid(oid),3);*/
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			}	
			finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				final long endTime = System.currentTimeMillis();
				System.out.println("getFocusBrowseList array Total execution time: " + (endTime - startTime) );
			}
		}
		else if(query.equals("ngd")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_NGD_FOCUS");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			// focus group
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND IST_COMPONENT in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
			////////////////// gene -------- to be implemented
			
			// archiveId & batchId
	        if (null != archiveId && !archiveId.equals("")){
	        	sql += " and (SUB_ARCHIVE_ID = " + archiveId + " ) ";
	        }
	        
	        if (null != batchId && !batchId.equals("")){
	        	sql += " and (SUB_BATCH = " + batchId + " ) ";
	        }			
			
			// stage group by and order by - xingjun - 25/09/2009 - added group by clause
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += AdvancedSearchDBQuery.groupBySubmissionNGD + orderResult(column, ascending, query);
			} else {
				sql += " and STG_STAGE_DISPLAY='"+stage+"' " + AdvancedSearchDBQuery.groupBySubmissionNGD + orderResult(column, ascending, query);
			}
			
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			parQ = null;
			parQ = new ParamQuery("FOCUS_NGD_BROWSE",sql);

			if (debug)
			    System.out.println("FocusForAllDAO:getFocusBrowseList:Arraybrowseall:"+parQ.getQuerySQL());
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				if (debug)
					System.out.println("MySQLFocusForAllDAO:getFocusBrowseList - prepStmt" + prepStmt);
				
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_NGD_COLUMNS);
				
				
				return result;
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			}	
			finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				final long endTime = System.currentTimeMillis();
				System.out.println("getFocusBrowseList array Total execution time: " + (endTime - startTime) );
			}
		}
		else if(query.equals("ish")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ISH");
			// assemble the query string
			String sql = parQ.getQuerySQL();
			if(specimenType!=null){
			if(specimenType.equals("WISH"))
				sql+=" AND (SPN_ASSAY_TYPE='wholemount' OR SPN_ASSAY_TYPE='opt-wholemount') ";
			else if(specimenType.equals("SISH"))
				sql+=" AND SPN_ASSAY_TYPE='section' ";
			else if(specimenType.equals("OPT"))
				sql+=" AND SPN_ASSAY_TYPE='opt-wholemount' ";
			}
			// focus group
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			} else { // remove redundant join to speed up query - 03/09/2009
//				sql = sql.replace("LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
				sql = sql.replace("JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID", "");
			}
			// gene --------- to be implemented
			// archiveId & batchId
	        if (null != archiveId && !archiveId.equals("")){
	        	sql += " and (SUB_ARCHIVE_ID = " + archiveId + " ) ";
	        }
	        
	        if (null != batchId && !batchId.equals("")){
	        	sql += " and (SUB_BATCH = " + batchId + " ) ";
	        }			
			// stage and order
	        String group = " GROUP BY SUB_ACCESSION_ID ";
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += group + orderResult(column, ascending, query);
			} else {
				sql += " and STG_STAGE_DISPLAY='"+stage+"' " + group + orderResult(column, ascending, query);
			}
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
		
			if(filter!=null) 
				sql = filter.addFilterSql(sql, AdvancedSearchDBQuery.ISH_BROWSE_ALL_SQL_COLUMNS);
			parQ = null;
			parQ = new ParamQuery("FOCUS_ISH_BROWSE",sql);

			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				if (debug)
					System.out.println("MySQLFocusForAllDAO:getFocusBrowseList - prepStmt" + prepStmt);
				// execute
				resSet = prepStmt.executeQuery();
				
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				return result;
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			}	
			finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				final long endTime = System.currentTimeMillis();
				System.out.println("getFocusBrowseList ish Total execution time: " + (endTime - startTime) );
			}
		} else if(query.equals("insitu")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_INSITU");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			// organ
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			} else { // remove redunant join to speed up query
				sql = sql.replace(" LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
			}
			// gene ---------------- to be implemented
			if (gene != null && !gene.equals("")) {
				sql += " and RPR_SYMBOL = '" + gene + "' ";
			}
			// archiveId & batchId
	        if (null != archiveId && !archiveId.equals("")){
	        	sql += " and (SUB_ARCHIVE_ID = " + archiveId + " ) ";
	        }
	        
	        if (null != batchId && !batchId.equals("")){
	        	sql += " and (SUB_BATCH = " + batchId + " ) ";
	        }			
			// stage and order
	        String group = " GROUP BY SUB_ACCESSION_ID ";
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += group + orderResult(column, ascending, query);
			} else {
				sql += " and STG_STAGE_DISPLAY='"+stage+"' " + group + orderResult(column, ascending, query);
			}
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			if(filter!=null) 
				sql = filter.addFilterSql(sql, AdvancedSearchDBQuery.ISH_BROWSE_ALL_SQL_COLUMNS);
			parQ = null;
			parQ = new ParamQuery("FOCUS_INSITU_BROWSE",sql);

//			System.out.println("INSITUbrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				if (debug)
					System.out.println("MySQLFocusForAllDAO:getFocusBrowseList - prepStmt" + prepStmt);
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				return result;
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			}	
			finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);

				final long endTime = System.currentTimeMillis();
				System.out.println("getFocusBrowseList insitu Total execution time: " + (endTime - startTime) );
			}
		} else if(query.equals("insitu_all")) {

			if(debug)
				System.out.println("MySQLFocusForAllDAO:getFocusBrowseList - insitu_all" + prepStmt);


			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ISH_IHC_TG");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			// organ
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			} else { // remove redunant join to speed up query
				sql = sql.replace(" LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
			}
			// gene ---------------- to be implemented
			if (gene != null && !gene.equals("")) {
				sql += " and RPR_SYMBOL = '" + gene + "' ";
			}
			// geneid 
			if (geneid != null && !geneid.equals("")) {
				sql += " and RPR_LOCUS_TAG = '" + geneid + "' ";
			}
			// archiveId & batchId
	        if (null != archiveId && !archiveId.equals("")){
	        	sql += " and (SUB_ARCHIVE_ID = " + archiveId + " ) ";
	        }
	        
	        if (null != batchId && !batchId.equals("")){
	        	sql += " and (SUB_BATCH = " + batchId + " ) ";
	        }			
			// stage and order

	        String group = " GROUP BY SUB_ACCESSION_ID ";
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += group + orderResult(column, ascending, query);
			} else {
				sql += " and STG_STAGE_DISPLAY='"+stage+"' " + group + orderResult(column, ascending, query);
			}
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			if(filter!=null) 
				sql = filter.addFilterSql(sql, AdvancedSearchDBQuery.ISH_BROWSE_ALL_SQL_COLUMNS);
			parQ = null;
			parQ = new ParamQuery("FOCUS_INSITU_BROWSE",sql);


			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				if (debug)
					System.out.println("MySQLFocusForAllDAO:getFocusBrowseList insitu_all- prepStmt" + prepStmt);


				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				return result;
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			}	
			finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				final long endTime = System.currentTimeMillis();
				System.out.println("getFocusBrowseList insitu all Total execution time: " + (endTime - startTime) );
			}

		}
		return result;

	} // end of getFocusBrowseList()
	
    private ArrayList formatResultSet(ResultSet resSetImage,int length) throws SQLException {     
        if (resSetImage.first()) {
            resSetImage.beforeFirst();           
            ArrayList<String[]> results = new ArrayList<String[]>();
            while (resSetImage.next()) {
                String[] columns = new String[length];
                for(int i = 1; i <= length; i++) {
                	columns[i-1] = resSetImage.getString(i);
                	//System.out.println("SOURCE:"+columns[i-1]);
                }
                if(null != columns)
                results.add(columns);
            }
            return results;
        }
        return null;
    }
    

    
    /**
     * @author xingjun
     * overload version 
     * @return
     */
    public int getQuickNumberOfRows(String query, String[] inputs, String stage, String symbol, String geneid, String archiveId, String batchId, String specimenType, GenericTableFilter filter) {
    	ArrayList list = getFocusBrowseList(inputs, 1, true, query, stage, symbol, geneid, archiveId, batchId, specimenType, null, null,filter);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
//   	return getTotalNumberOfSubmissions(inputs, 1, true, query, stage, symbol, archiveId, batchId, null, null,filter); 	    	
    }
    
	/**
	 * <p>xingjun - 14/12/2009 - changed the default order (refer to the code below)</p>
	 * <p>xingjun - 03/02/2010 - changed the way to append platform criteria into the query string</p>
	 */
    public ArrayList getSeriesList(int columnIndex, boolean ascending, int offset, int num, String organ, String platform) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_SERIES");
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
		if(null != emapids) {
			String ids = "";
			for(int i = 0; i < emapids.length; i++) {
				ids += "'"+emapids[i] + "',";
			}
			if(emapids.length >= 1) {
				ids = ids.substring(0, ids.length()-1);
			}
			String componentString = "AND EXP_COMPONENT_ID IN (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
			" from ANA_TIMED_NODE ANCES_ATN, " +
			" ANAD_RELATIONSHIP_TRANSITIVE, " +
			" ANA_TIMED_NODE DESCEND_ATN, " +
			" ANA_NODE, " +
			" ANAD_PART_OF " +
			" where ANCES_ATN.ATN_PUBLIC_ID in ("+ids+") " +
			" and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			//     " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			" and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			" and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
			" and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
			" and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) ";
			query = query.replaceAll("AND EXP_COMPONENT_ID IN", componentString); 
		}
		if (null != platform && !platform.equals("")) {
			String platformString = " and SER_PLATFORM_FK=PLT_OID and PLT_GEO_ID=? ";
			query = query.replaceAll("AND SER_PLATFORM_FK", platformString);
		} else {
			query.replaceAll("AND SER_PLATFORM_FK", "");
		}
		
		// xingjun - 14/12/2009 - changed the default order as editors requested
//		String defaultOrder = AdvancedSearchDBQuery.ORDER_BY_EXPERIMENT_NAME;
		String defaultOrder = DBQuery.ORDER_BY_LAB_AND_EXPERIMENT;
		String queryString = assembleBrowseSerieseQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//	    System.out.println("FocusForAllDAO:getSeriesList:sql: "+queryString);
		// execute the query
		try {
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:getSeriesList sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			if(null != platform && !platform.equals("")) {
				prepStmt.setString(1, platform);
			}
			if (debug)
				System.out.println("MySQLFocusForAllDAO:getFocusBrowseList - prepStmt" + prepStmt);
			resSet = prepStmt.executeQuery();
			result = formatBrowseSeriesResultSet(resSet);
			
			return result;
		} catch(SQLException se) {
			se.printStackTrace();
			return null;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}

	public int getNumberOfSeries(String organ, String platform) {
		ArrayList list = getSeriesList(1, true, 0, 0, organ, platform);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
	}
	
	public ArrayList getNGDSeriesList(int columnIndex, boolean ascending, int offset, int num, String organ, String protocol) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_NGD_SERIES");
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
		if(null != emapids) {
			String ids = "";
			for(int i = 0; i < emapids.length; i++) {
				ids += "'"+emapids[i] + "',";
			}
			if(emapids.length >= 1) {
				ids = ids.substring(0, ids.length()-1);
			}
			String componentString = "AND IST_COMPONENT IN (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
			" from ANA_TIMED_NODE ANCES_ATN, " +
			" ANAD_RELATIONSHIP_TRANSITIVE, " +
			" ANA_TIMED_NODE DESCEND_ATN, " +
			" ANA_NODE, " +
			" ANAD_PART_OF " +
			" where ANCES_ATN.ATN_PUBLIC_ID in ("+ids+") " +
			" and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			//     " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			" and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			" and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
			" and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
			" and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) ";
			query = query.replaceAll("AND IST_COMPONENT IN", componentString); 
		}
		/*
		if (null != protocol && !protocol.equals("")) {
			String protocolString = " and NGS_PROTOCOL_FK=NGP_OID and PLT_GEO_ID=? ";
			query = query.replaceAll("AND NGS_PROTOCOL_FK", protocolString);
		} else {
			query.replaceAll("AND NGS_PROTOCOL_FK", "");
		}
		*/
		// xingjun - 14/12/2009 - changed the default order as editors requested
//		String defaultOrder = AdvancedSearchDBQuery.ORDER_BY_EXPERIMENT_NAME;
		String defaultOrder = DBQuery.ORDER_BY_LAB_AND_EXPERIMENT;
		String queryString = assembleBrowseNGDSeriesQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//	    System.out.println("FocusForAllDAO:getSeriesList:sql: "+queryString);
		// execute the query
		try {
		    if (debug)
		    	System.out.println("MySQLFocusFowAllDAOImp:getNGDSeriesList sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			/*if(null != protocol && !protocol.equals("")) {
				prepStmt.setString(1, protocol);
			}
			*/
			if (debug)
				System.out.println("MySQLFocusForAllDAO:getNGDSeriesList - prepStmt" + prepStmt);
			resSet = prepStmt.executeQuery();
			result = formatBrowseSeriesResultSet(resSet);
			
			return result;
		} catch(SQLException se) {
			se.printStackTrace();
			return null;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}

	public int getNumberOfNGDSeries(String organ, String protocol) {
		ArrayList list = getNGDSeriesList(1, true, 0, 0, organ, protocol);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
	}
	
	private String assembleBrowseSerieseQueryString(String query, String defaultOrder, 
			int columnIndex, boolean ascending, int offset, int num) {

		String queryString = null;
		
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseSeriesOrderByColumn(columnIndex, ascending);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by experiment name and geo id by default
			queryString = query + defaultOrder+ ", NATURAL_SORT(TRIM(SER_GEO_ID))";
		}
		
		// offset and retrieval number
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	private String assembleBrowseNGDSeriesQueryString(String query, String defaultOrder, 
			int columnIndex, boolean ascending, int offset, int num) {

		String queryString = null;
		
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseNGDSeriesOrderByColumn(columnIndex, ascending);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by experiment name and geo id by default
			queryString = query + defaultOrder+ ", NATURAL_SORT(TRIM(NGR_GEO_ID))";
		}
		
		// offset and retrieval number
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	/**
	 * 
	 * @param columnIndex
	 * @param ascending
	 * @return
	 */
	private String getBrowseSeriesOrderByColumn(int columnIndex, boolean ascending) {
		
		String orderByString = new String("");
		String defaultOrder = " NATURAL_SORT(TRIM(SER_TITLE)) ";
		String order = (ascending == true ? "ASC": "DESC");
		String[] browseSeriesColumnList = {
				"NATURAL_SORT(TRIM(SER_TITLE))", "SAMPLE_NUMBER", "NATURAL_SORT(TRIM(SER_GEO_ID))", 
				"SUB_SOURCE", "PLT_GEO_ID"};
		
		// start to translate
		if (columnIndex == 0) {
			orderByString = defaultOrder + order;
		} else if (columnIndex == 2) {
			orderByString = "SAMPLE_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 1) {
			orderByString = "NATURAL_SORT(TRIM(SER_GEO_ID)) " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
			orderByString = "SUB_SOURCE " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) {
			orderByString = "PLT_GEO_ID " + order + ", " + defaultOrder;
		}
		return orderByString;
	}
	
private String getBrowseNGDSeriesOrderByColumn(int columnIndex, boolean ascending) {
		
		String orderByString = new String("");
		String defaultOrder = " NATURAL_SORT(TRIM(NGR_TITLE)) ";
		String order = (ascending == true ? "ASC": "DESC");
		String[] browseSeriesColumnList = {
				"NATURAL_SORT(TRIM(NGR_TITLE))", "NATURAL_SORT(TRIM(NGR_GEO_ID))", "SUB_SOURCE", "SAMPLE_NUMBER",  
				 "NGP_LIBRARY_STRATEGY"};
		
		// start to translate
		if (columnIndex == 0) {
			orderByString = defaultOrder + order;
		} else if (columnIndex == 1) {
			orderByString = "NATURAL_SORT(TRIM(NGR_GEO_ID)) " + order + ", " + defaultOrder;
		} else if (columnIndex == 2) {
			orderByString = "SUB_SOURCE " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
			orderByString = "SAMPLE_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) {
			orderByString = "NGP_LIBRARY_STRATEGY " + order + ", " + defaultOrder;
		}
		return orderByString;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList formatBrowseSeriesResultSet(ResultSet resSet) throws SQLException {
		
   		ArrayList<String[]> results = null;
		ResultSetMetaData resSetMetaData = resSet.getMetaData();
   		int columnCount = resSetMetaData.getColumnCount();

		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			results = new ArrayList<String[]>();
			
			while(resSet.next()) {
				String[] columns = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
		        }
		        results.add(columns);
			}
		}
		return results;
	}	

	/**
	 * modified by xingjun - 18/08/2009 
	 * - use natural sort instead of just normal sort for geo id 
	 * 
	 */
	public ArrayList getPlatformList(int columnIndex, boolean ascending, int offset, int num, String organ) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_PLATFORM");
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		if(null != organ && !organ.equals("")) {
			String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
			String ids = "";
			  for(int i = 0; i < emapids.length; i++) {
				  ids += "'"+emapids[i] + "',";
			  }
			  if(emapids.length >= 1) {
				  ids = ids.substring(0, ids.length()-1);
			  }
			query += " AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
	        " ANAD_RELATIONSHIP_TRANSITIVE, "+
	        " ANA_TIMED_NODE DESCEND_ATN, "+
	        " ANA_NODE, "+
	        " ANAD_PART_OF "+
			   " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			     " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
//			     " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			     " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			     " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			     " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			     " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
		}
//		String defaultOrder = " ORDER BY TRIM(PLT_GEO_ID) ";
		String defaultOrder = " ORDER BY natural_sort(TRIM(PLT_GEO_ID)) ";
		String queryString = assemblePlatformQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//	    System.out.println("FocusPlatform:"+queryString);
		// execute the query
		try {
			prepStmt = conn.prepareStatement(queryString);
			
			if (debug)
				System.out.println("MySQLFocusForAllDAO:getPlatformList - prepStmt" + prepStmt);
			resSet = prepStmt.executeQuery();
			result = formatBrowseSeriesResultSet(resSet);
			
			return result;
		} catch(SQLException se) {
			se.printStackTrace();
			return null;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}

	public int getNumberOfPlatform(String organ) {
		ArrayList list = getPlatformList(1, true, 0, 0, organ);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
	}	

	/**
	 * modified by xingjun - 18/08/2009 
	 * - use natural sort instead of just normal sort for geo id and geo name 
	 * @param query
	 * @param defaultOrder
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	private String assemblePlatformQueryString(String query, String defaultOrder, 
			int columnIndex, boolean ascending, int offset, int num) {
		String queryString = null;
		String order = (ascending == true ? " ASC ": " DESC ");
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			String column = "";
			if(0 == columnIndex) {
				column = "natural_sort(TRIM(PLT_GEO_ID))" + order;
			} else if(1 == columnIndex) {
				column = "natural_sort(TRIM(PLT_NAME))" + order;
			} else if(2 == columnIndex) {
				column = "PLT_TECHNOLOGY" + order;
			} else if(3 == columnIndex) {
				column = "PLT_MANUFACTURER" + order;
			} else if (columnIndex == 4) {
				column = "SERIES_NUMBER" + order;
			}
			
			queryString += column;
			
		} else { 
			queryString = query + defaultOrder;
		}
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		return queryString;
	}
	
	/**
	 * @author xingjun
	 * <p>modified by xingjun - 28/08/2008
	 * separated query based on assay type - ish, ihc, array, transgenic</p>
	 * @param labId
	 * @param assayType
	 * @param submissionDate
	 * @return
	 */
	public int getNumberOfSubmissionsForLab(String labId, String assayType, String submissionDate) {
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
//        System.out.println("getNumberOfSubmissionsForLab:assayType: " + assayType);
        if (assayType.equals("ISH") || assayType.equals("IHC")) {
            parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        } else if (assayType.equals("Microarray")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_ARRAY");
        } else if (assayType.equals("TG")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_TG");
        } else if (assayType.equals("insitu")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        }
        String query = parQ.getQuerySQL();

        if (labId != null && !labId.equals("")) {
        	query += " AND SUB_PI_FK  = ? ";
        }
        
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }
    	// added by xingjun - 28/08/2008
    	if (assayType.equals("insitu")) {
    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
    	} else {
    		query += "AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
    	}
    	
//        System.out.println("getNumberOfSubmissionsForLab:query: " + query);
        PreparedStatement prepStmt = null;

        try {
		    if (debug)
		    	System.out.println("MySQLFocusForAllDAO:getNumberOfSubmissionsForLab sql = "+query.toLowerCase());
		    
            prepStmt = conn.prepareStatement(query);
            int paramNum = 1;

            if (labId != null && !labId.equals("")) {
            	prepStmt.setInt(paramNum, Integer.parseInt(labId));
            	paramNum++;
            }
                        
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(paramNum, submissionDate);
            }
            
			if (debug)
				System.out.println("MySQLFocusForAllDAO:getNumberOfSubmissionsForLab - prepStmt" + prepStmt);

            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

			return totalNumber;
		} catch(SQLException se) {
			se.printStackTrace();
			return 0;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 01/07/2011 - overloading version
	 * 
	 */
	public int getNumberOfSubmissionsForLab(String labId, String assayType, String submissionDate, String archiveId, String batchId) {

		if (debug){
			System.out.println("getNumberOfSubmissionsForLab:labId: " + labId);
			System.out.println("getNumberOfSubmissionsForLab:assayType: " + assayType);
			System.out.println("getNumberOfSubmissionsForLab:submissionDate: " + submissionDate);
			System.out.println("getNumberOfSubmissionsForLab:archiveId: " + archiveId);
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (assayType == null){
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        } 
        else if (assayType.equals("ISH") || assayType.equals("IHC")) {
            parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        } 
        else if (assayType.equals("Microarray")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_ARRAY");
        } 
        else if (assayType.equals("TG")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_TG");
        }
        else if (assayType.equals("insitu")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        }
        
        String query = parQ.getQuerySQL();

        if (labId != null && !labId.equals("")) {
        	query += " AND SUB_PI_FK  = ? ";
        }
               
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }

    	if (assayType != null && !assayType.trim().equals("")) {
	    	if (assayType.equals("insitu")) {
	    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
	    	} else {
	    		query += "AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
	    	}
    	}
    	else{
    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
    	}
    	
    	if (archiveId != null && !archiveId.trim().equals("")) {
    		query += "AND SUB_ARCHIVE_ID = ? ";
    	}
    	
    	if (batchId != null && !batchId.trim().equals("")) {
    		query += "AND SUB_BATCH = ? ";
    	}
   	
        PreparedStatement prepStmt = null;

        try {
		    
            prepStmt = conn.prepareStatement(query);
            int paramNum = 1;
            
			if (labId != null && !labId.trim().equals("")) {
				prepStmt.setInt(paramNum, Integer.parseInt(labId));
				paramNum++;
			}
            
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(paramNum, submissionDate);
            	paramNum ++;
            }
            
			if (archiveId != null && !archiveId.trim().equals("")) {
				prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
				paramNum ++;
			}
			
			if (batchId != null && !batchId.trim().equals("")) {
				prepStmt.setInt(paramNum, Integer.parseInt(batchId));
			}

			if (debug)
				System.out.println("MySQLFocusForAllDAO:getNumberOfSubmissionsForLab - prepStmt" + prepStmt);
            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

			return totalNumber;
		} catch(SQLException se) {
			se.printStackTrace();
			return 0;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	
	public int getNumberOfSubmissionsForLabForAnnotation(String labId, String assayType, String submissionDate, String isPublic) {
        
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_ANNOTATION_SUBMISSIONS_FOR_SPECIFIC_LAB");
        String query = parQ.getQuerySQL();
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }
//        System.out.println("number of submission for lab: " + query);
        PreparedStatement prepStmt = null;

        try {
            prepStmt = conn.prepareStatement(query);
            prepStmt.setInt(1, Integer.parseInt(labId));
            prepStmt.setString(2, assayType);
            prepStmt.setString(3, isPublic);
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(4, submissionDate);
            }
			if (debug)
				System.out.println("MySQLFocusForAllDAO:getNumberOfSubmissionsForLabForAnnotation - prepStmt" + prepStmt);

            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

			return totalNumber;
		} catch(SQLException se) {
			se.printStackTrace();
			return 0;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
	 * @author xingjun - 01/07/2011 - overloading version
	 */
	public int getNumberOfSubmissionsForLabForAnnotation(String labId, String assayType, String submissionDate, String archiveId, String isPublic) {
        
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_ANNOTATION_SUBMISSIONS_FOR_SPECIFIC_LAB");
        String query = parQ.getQuerySQL();
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }

		if (archiveId != null && !archiveId.trim().equals("")) {
			query += " AND SUB_ARCHIVE_ID = ? ";
		}
		
//        System.out.println("number of submission for lab: " + query);
        PreparedStatement prepStmt = null;

        try {
            prepStmt = conn.prepareStatement(query);
            prepStmt.setInt(1, Integer.parseInt(labId));
            prepStmt.setString(2, assayType);
            prepStmt.setString(3, isPublic);
            int paramNum = 4;
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(paramNum, submissionDate);
            	paramNum ++;
            }

			if (archiveId != null && !archiveId.trim().equals("")) {
				prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
			}
			
			if (debug)
				System.out.println("MySQLFocusForAllDAO:getNumberOfSubmissionsForLabForAnnotation - prepStmt" + prepStmt);
			
            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

			return totalNumber;
		} catch(SQLException se) {
			se.printStackTrace();
			return 0;
		}	
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
    public ArrayList getArrayGenotypeBySeriesOid(String oid, int columnIndex,
            boolean ascending, int offset, int num) {
    	ArrayList result = null;
    	ResultSet resSet = null;
    	
    	//find relevant query string from db query
    	ParamQuery parQ = ArrayDBQuery.getParamQuery("GENOTYPE_LIST_ARRAY");
    	PreparedStatement prepStat = null;
    	
    	String query = parQ.getQuerySQL();
    	
    	//try to execute the query
    	try {
		    if (debug)
		    	System.out.println("MySQLArrayDevDAOImp.sql = "+query.toLowerCase());
    		prepStat = conn.prepareStatement(query);
    		prepStat.setInt(1, Integer.parseInt(oid));
    		prepStat.setInt(2, Integer.parseInt(oid));
    		
    		resSet = prepStat.executeQuery();
    		result = Utility.formatResultSet(resSet);
    		
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStat);			
		}
    }
}
