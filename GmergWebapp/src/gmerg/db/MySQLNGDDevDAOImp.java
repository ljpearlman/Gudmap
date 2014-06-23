/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.nextgen.NGDSeries;
import gmerg.db.MySQLArrayDevDAOImp;
import gmerg.entities.submission.ImageInfo;
import gmerg.utils.Utility;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xingjun 
 *
 */
public class MySQLNGDDevDAOImp extends  MySQLArrayDevDAOImp implements NGDDevDAO  {
    private boolean debug = false;
	private Connection conn; 

	// default constructor
	public MySQLNGDDevDAOImp() {
		
	}
	
	public MySQLNGDDevDAOImp(Connection conn) {
		this.conn = conn;
	}

	/**
	 * <p>xingjun - 14/12/2009 - changed the default order (refer to the code below)</p>
	 * <p>xingjun - 03/02/2010 - changed the way to append platform criteria into the query string</p>
	 * <p>xingjun - 03/03/2010 - changed the code to assemble sql: used to append clauses at the end of sql. now replace existing one</p>
	 * <p>xingjun - 23/06/2011 - changed the query to be able to pick up multiple PIs</p>
	 */
	public ArrayList getAllSeries(int columnIndex, boolean ascending, int offset, int num) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
                //find relevant query string from DBQuery
		ParamQuery parQ = ArrayDBQuery.getParamQuery("ALL_NGD_SERIES");
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		
		String defaultOrder = DBQuery.NGD_ORDER_BY_LAB_AND_EXPERIMENT;
		String queryString = assembleBrowseSerieseQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//		System.out.println("ArrayDevDAO:getAllSeries:sql: " + queryString);
		
		// execute the query
		try {
//			parQ.setPrepStat(conn);
		    if (debug)
			System.out.println("MySQLArrayDevDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			result= Utility.formatResultSet(resSet);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);			
		}
	}


    /**
     * queries database and formats results to be used to build table of 
     * sample data for a particular series
     * @param id - the identifier of the specified series
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return result - an ArrayList of data to be used to build a table fo sample data
     */
    public ArrayList getSamplesForSeries(String id, int columnIndex,
                                         boolean ascending, int offset,
                                         int num) {
        ArrayList result = null;
        ArrayList result2 = null;
        ResultSet resSet = null;
        //find relevant query string from db query
        ParamQuery parQ = DBQuery.getParamQuery("NGD_SERIES_SAMPLES");
        PreparedStatement prepStat = null;

        String query = parQ.getQuerySQL();
        String defaultOrder = " CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,':')+1) AS UNSIGNED) ";
        String queryString =
            assembleSeriesSamplesQString(query, defaultOrder, columnIndex,
                                         ascending, offset, num);
//        System.out.println("ArrayDevDAO:getSamplesForSeries:sql: " + queryString);

        //try to execute the query
        try {
	    if (debug)
		System.out.println("MySQLNGDDevDAOImp.sql = "+queryString.toLowerCase());
            prepStat = conn.prepareStatement(queryString);
            prepStat.setString(1, id);

            resSet = prepStat.executeQuery();
            result = Utility.formatResultSet(resSet);
            return result;
            
           /* result = Utility.formatResultSet(resSet);
    		result2 = Utility.formatGenotypeResultSet(result,getGenotypeBySeriesOid(id,"GENOTYPE_LIST_NGD_GEO"),4);
    		return result2;*/
    		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStat);			
		}
    }
    
    /**
     * @author xingjun - 06/07/2009
     */
    public ArrayList getSamplesBySeriesOid(String oid, int columnIndex,
            boolean ascending, int offset, int num) {
    	ArrayList result = null;
    	ArrayList result2 = null;
    	ResultSet resSet = null;
    	
    	//find relevant query string from db query
    	ParamQuery parQ = ArrayDBQuery.getParamQuery("NGD_SERIES_SAMPLES_BY_OID");
    	PreparedStatement prepStat = null;
    	
    	String query = parQ.getQuerySQL();
    	String defaultOrder = " CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,':')+1) AS UNSIGNED) ";
    	String queryString = assembleSeriesSamplesQString(query, defaultOrder, 
    			columnIndex, ascending, offset, num);
//    	System.out.println("ArrayDevDAO:getSamplesBySeriesOid:sql: " + queryString);
//    	System.out.println("ArrayDevDAO:getSamplesBySeriesOid:seriesOid: " + oid);
    	
    	//try to execute the query
    	try {
		    if (debug)
		    	System.out.println("MySQLNDGDevDAOImp.sql = "+queryString.toLowerCase());
    		prepStat = conn.prepareStatement(queryString);
    		prepStat.setInt(1, Integer.parseInt(oid));
    		
    		resSet = prepStat.executeQuery();
    		/*result = formatBrowseSeriesResultSet(resSet);
    		return result;*/
    		result = Utility.formatResultSet(resSet);
    		result2 = Utility.formatGenotypeResultSet(result,getGenotypeBySeriesOid(oid,"GENOTYPE_LIST_NGD"),4);
    		return result2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStat);			
		}
    }
    
    public ArrayList getGenotypeBySeriesOid(String oid, String dbquery) {
    	ArrayList result = null;
    	ResultSet resSet = null;
    	
    	//find relevant query string from db query
    	ParamQuery parQ = ArrayDBQuery.getParamQuery(dbquery);
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
    
    protected String assembleSeriesSamplesQString(String query,
        String defaultOrder,
        int columnIndex,
        boolean ascending, int offset,
        int num) {
		StringBuffer queryString = new StringBuffer("");
		
		//orderBy
		if (columnIndex != -1) {
		//add code here to make table columns sortable
		queryString.append(query + " ORDER BY ");
		String column = this.getSeriesSampleOrderByColumn(columnIndex, ascending, defaultOrder);
		queryString.append(column);
		} else {
		queryString.append(query + " ORDER BY " + defaultOrder);
		}
		
		queryString.append(" LIMIT " + offset + ", " + num);
		
		return queryString.toString();
	}

    
    /**
     * <p>xingjun - 11/05/2010 - appended the natural_sort to the sql to make the sorting more 'natural'</p>
     * @param columnIndex
     * @param ascending
     * @param defaultOrder
     * @return
     */
	private String getSeriesSampleOrderByColumn(int columnIndex, boolean ascending, String defaultOrder) {
    	
		String orderByString = new String("");
		String order = (ascending == true ? "ASC": "DESC");		
		// start to translate
		if (columnIndex == 1) {
			orderByString = "NGS_GEO_ID " + order + ", " + defaultOrder;
		} else if (columnIndex == 2) {
			orderByString = "natural_sort(NGS_SAMPLE_NAME) " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
			orderByString = "natural_sort(NGP_LIBRARY_STRATEGY) " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) {
			orderByString = "natural_sort(GENOTYPE) " + order + ", " + defaultOrder;
		} 
		else {
			orderByString = defaultOrder + order;
		}
		return orderByString;
    }
	
	/**
	 * 
	 * @param query
	 * @param defaultOrder
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */	
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
			queryString = query + defaultOrder+ ", NATURAL_SORT(TRIM(NGR_GEO_ID))";
		}
		
		// offset and retrieval number
		queryString = queryString + " LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	/**
	 * @param columnIndex
	 * @param ascending
	 * @return
	 */
	private String getBrowseSeriesOrderByColumn(int columnIndex, boolean ascending) {
		
		String orderByString = new String("");
		String defaultOrder = " NATURAL_SORT(TRIM(NGR_TITLE)) ";
		String order = (ascending == true ? "ASC": "DESC");

		// start to translate
		if (columnIndex == 0) {
			orderByString = defaultOrder + order;
		} else if (columnIndex == 1) {
			orderByString = "NATURAL_SORT(TRIM(NGR_GEO_ID)) " + order + ", " + defaultOrder;
		} else if (columnIndex == 2) {
			orderByString = "SAMPLE_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
			orderByString = "SUB_SOURCE " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) {
			orderByString = "NATURAL_SORT(TRIM(PLT_GEO_ID)) " + order + ", " + defaultOrder;
		}
		return orderByString;
	}
	
	
	/**
	 * based on the specified organ value to append join clause and component related criteria
	 * @author xingjun
	 * @param param
	 * @param query - query name
	 * @param organ 
	 */
	public String[][] getSeriesBrowseTotals(String[][] param, String[] query, String organ) {
		String joinClause = "JOIN NGD_SAMPLE_SERIES ON NGL_SERIES_FK = NGR_OID " +
	  		"JOIN NGD_SAMPLE ON NGL_SAMPLE_FK= NGS_OID " +
	  		"JOIN ISH_SUBMISSION ON NGS_SUBMISSION_FK = SUB_OID " +
	  		"JOIN ISH_PERSON ON SUB_PI_FK = PER_OID ";
	  
		String componentClause = "";
		if(null != organ && !organ.equals("")) {
			String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
			String ids = "";
			if(null != emapids) {
			  for(int i = 0; i < emapids.length; i++) {
				  ids += "'" + emapids[i] + "',";
			  }
			  if(emapids.length >= 1) {
				  ids = ids.substring(0, ids.length()-1);
			  }
			  componentClause += "JOIN ISH_SP_TISSUE ON SUB_OID = IST_SUBMISSION_FK " +
			  		"AND IST_COMPONENT in (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
			  "from ANA_TIMED_NODE ANCES_ATN, " +
			  "ANAD_RELATIONSHIP_TRANSITIVE, " +
			  "ANA_TIMED_NODE DESCEND_ATN, " +
			  "ANA_NODE, " +
			  "ANAD_PART_OF " +
			  "where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") " +
			  "and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK " +
			  "and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK " +
			  "and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK " +
			  "and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
			  "and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
			  "and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
		}
		return getStringArrayFromBatchQuery(param, query, (joinClause + componentClause));
	}
  

	/**
	 */
	public int getTotalNumberOfSeries() {
        int totalNumber = 0;
        ResultSet resSet = null;
	ParamQuery parQ = ArrayDBQuery.getParamQuery("ALL_NGD_SERIES");

        PreparedStatement prepStmt = null;
        String query = parQ.getQuerySQL();
        
        try {
            
            query = "SELECT COUNT(*) FROM (" + query + ") AS S_TABLE";
//    		System.out.println("ArrayDevDAO:getTotalNumberOfSeries:sql: " + query);
		    if (debug)
		    	System.out.println("MySQLArrayDevDAOImp.sql = "+query.toLowerCase());
            prepStmt = conn.prepareStatement(query);
            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
                totalNumber = resSet.getInt(1);
            }
    		return totalNumber;

        } catch (SQLException se) {
            se.printStackTrace();
    		return 0;
        }
        finally{
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        }
	}
	
	/**
	 * @author ying
	 * <p>added extra column description into the result</p>
	 * <p>xingjun - 12/08/2010 - added archive id column</p>
	 */
	public NGDSeries findSeriesById(String id) {
		if (id == null) {
			return null;
		}
		NGDSeries series = null;
		ResultSet resSetSeries = null;
		ParamQuery parQSeries = DBQuery.getParamQuery("NGD_SERIES_DATA");
		PreparedStatement prepStmtSeries = null;
		try {
			conn.setAutoCommit(false);
			
			// series
			parQSeries.setPrepStat(conn);
			prepStmtSeries = parQSeries.getPrepStat();
			prepStmtSeries.setString(1, id);
			resSetSeries = prepStmtSeries.executeQuery();
			
			// assemble
			if(resSetSeries.first()){
				series = new NGDSeries();
				series.setGeoID(resSetSeries.getString(1));
				series.setNumSamples(resSetSeries.getString(2));
				series.setTitle(resSetSeries.getString(3));
				series.setSummary(resSetSeries.getString(4));
				//series.setType(resSetSeries.getString(5));
				series.SetDesign(resSetSeries.getString(5));
				//series.SetDescription(resSetSeries.getString(7));
				series.setArchiveId(resSetSeries.getInt(6));
				series.setBatchId(resSetSeries.getInt(7));
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			
			return series;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSetSeries);
			DBHelper.closePreparedStatement(prepStmtSeries);			
		}
	}
	
	/**
	 * @author xingjun - 06/07/2009
	 * <p>modified by xingjun 30/09/2009 - if non-integer param passed in, return null</p>
	 * <p>xingjun  - 04/03/2010 - added property series description into result</p>
	 */
	public NGDSeries findSeriesByOid(String oid) {
		if (oid == null) {
			return null;
		}
//		System.out.println("ArrayDevDAO:findSeriesByOid:oid: " + oid);
		int seriesOid = -1;
		try {
			seriesOid = Integer.parseInt(oid);
		} catch (NumberFormatException nfe) {
			seriesOid = 0;
		}
		NGDSeries series = null;
		ResultSet resSetSeries = null;
		ParamQuery parQSeries = ArrayDBQuery.getParamQuery("NGD_SERIES_DATA_BY_OID");
		PreparedStatement prepStmtSeries = null;
//		System.out.println("ArrayDevDAO:findSeriesByOid:sql: " + parQSeries.getQuerySQL());
//		System.out.println("ArrayDevDAO:findSeriesByOid:seriesOid: " + oid);
		try {
			// series
			parQSeries.setPrepStat(conn);
			prepStmtSeries = parQSeries.getPrepStat();
			prepStmtSeries.setInt(1, seriesOid);
			resSetSeries = prepStmtSeries.executeQuery();
			
			// assemble
			if(resSetSeries.first()){
				series = new NGDSeries();
				series.setGeoID(resSetSeries.getString(1));
				series.setNumSamples(resSetSeries.getString(2));
				series.setTitle(resSetSeries.getString(3));
				series.setSummary(resSetSeries.getString(4));
				series.SetDesign(resSetSeries.getString(5));
				series.setArchiveId(resSetSeries.getInt(6));
				series.setBatchId(resSetSeries.getInt(7));
				 
			}
			return series;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closeResultSet(resSetSeries);
			DBHelper.closePreparedStatement(prepStmtSeries);			
		}
	}
	
	public ArrayList findOriginalImagesById(String submissionAccessionId) {
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return null;
		}
		ArrayList ImageInfo = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("NGD_IMAGES");
		PreparedStatement prepStmt = null;
		try {
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			ImageInfo = formatImageResultSet(resSet);
			return ImageInfo;
			
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		}
		finally{
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		}
	}
	
	/**
   	 *
   	 * @param resSetImage
   	 * @return
   	 * @throws SQLException
   	 */
	private ArrayList formatImageResultSet(ResultSet resSetImage) throws SQLException {
	    if (resSetImage.first()) {
			resSetImage.beforeFirst();
			int serialNo = 1;
			ArrayList results = new ArrayList();
			int dotPosition = 0;
			String fileExtension = null;
			String str = null;
			ImageInfo img = null;
			
			while (resSetImage.next()) {
			    img = new ImageInfo();
			    str = Utility.netTrim(resSetImage.getString(1));
			    if (null != str && !str.equals("")) 
			    	img.setAccessionId(str);
			    str = Utility.netTrim(resSetImage.getString(2));
			    if (null != str && !str.equals("")) 
			    	img.setFilePath(str);
			    str = Utility.netTrim(resSetImage.getString(3));
			    if (null != str && !str.equals("")) 
			    	img.setClickFilePath(str);
			    
			    // do not know the reason zoom_viewer do not work for micr array
			    // even though microarray images have tif. so
			    // use speciement type to mark microarray image
			    img.setSpecimenType("nextgen");
			    img.setSerialNo(""+serialNo);
	
			    serialNo++;
			    results.add(img);
			}
			return results;
	    }
	    return null;
	}
	

    
}
