/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import gmerg.db.NGDDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.nextgen.NGDSeries;
/*import gmerg.entities.submission.array.Series;*/
import gmerg.utils.Utility;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;
import gmerg.db.NGDDevDAO;

/**
 * @author xingjun
 *
 */
public class NGDSeriesAssembler extends OffMemoryTableAssembler {
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

	String seriesId;
	boolean geoId;
	
	public NGDSeriesAssembler () {
	if (debug)
	    System.out.println("NGDSeriesAssembler.constructor");
		
	}
	
	public NGDSeriesAssembler (HashMap params) {
		super(params);
	if (debug)
	    System.out.println("NGDSeriesAssembler.constructor");
	}

	public void setParams() {
		super.setParams();
		seriesId = getParam("seriesId");
        geoId = seriesId.toUpperCase().startsWith("GSE");
        if (geoId)
        	seriesId = seriesId.toUpperCase();
	}
	
    /**
     * returns an ArrayList containing the data used to build a table of samples
     * for a particular submission
     * 
     * modified by xingjun - 06/07/2009 - changed to get samples by series oid
     * 
     * @param id - the id of the specified series
     * @param columnIndex
     * @param ascending - boolean specifying whether to sort ascending or descending
     * @param offset 
     * @param num - determines how many results per page
     * @return seriesSamples - data used to build a table of samples
     */
    public DataItem[][] retrieveData(int columnIndex, boolean ascending, int offset, int num){
	    if (null != cache &&
		cache.isSameQuery(columnIndex, ascending, offset, num)) {
			if (debug)
			    System.out.println("SeriesAssembler.retriveData data not changed");
			
			return cache.getData();
	    }	
	    
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        try{    
            ArrayList seriesSamples = null;
        	NGDDevDAO ngdDevDAO = MySQLDAOFactory.getNGDDevDAO(conn);
            // get data from database
            if (geoId)
            	seriesSamples = ngdDevDAO.getSamplesForSeries(seriesId, columnIndex, ascending, offset, num);
            else
            	seriesSamples = ngdDevDAO.getSamplesBySeriesOid(seriesId, columnIndex, ascending, offset, num);
            
            
            // return the value object
            DataItem[][] ret = getTableDataFormatFromSampleList(seriesSamples);

    		if (null == cache)
    		    cache = new RetrieveDataCache();
    		cache.setData(ret);
    		cache.setColumn(columnIndex);
    		cache.setAscending(ascending);
    		cache.setOffset(offset);
    		cache.setNum(num);

    	    return ret;
       }
		catch(Exception e){
			System.out.println("NGDSeriesAssembler::retrieveData failed !!!");
		    return null;
		}
        finally{
			DBHelper.closeJDBCConnection(conn);
        }
    }
    
    public int retrieveNumberOfRows() {
    	int n = 0;
        try {
        	NGDSeries series = getSeriesMetaData();
        	if (series != null)
        		n = Integer.parseInt(series.getNumSamples());
        }
        catch(NumberFormatException e){
        }
        
        return n;
    }

    /**
     * returns an array of integers containing the number of different values
     * found in each column of the samples table for a particular series
     * @return totalNumbers - an array of column totals for the sample table
     */
    public int[] retrieveTotals() {
	    // force new cache 
	    cache = null;

        // create a dao
        Connection conn = DBHelper.getDBConnection();
        int[] totalNumbers = null;
        try{
        	NGDDevDAO ngdDevDAO = MySQLDAOFactory.getNGDDevDAO(conn);
	
	        String[] allColTotalsQueries =
	        { "TOTAL_NGD_SAMPLES", "TOTAL_NGD_GEO_IDS", "TOTAL_NGD_SAMPLE_IDS", "TOTAL_NGD_LIBRARY_STRATEGY",
	          "TOTAL_NGD_GENOTYPE","TOTAL_NGD_SAMPLE_DESCRIPTIONS", "TOTAL_NGD_COMPONENT" };
	        String[][] param = { {seriesId}, {seriesId}, {seriesId}, {seriesId}, {seriesId}, {seriesId} };
	        String[][] columnNumbers = ngdDevDAO.getStringArrayFromBatchQuery(param, allColTotalsQueries);
	        // convert to integer array, each tuple consists of column index and the number
	        int len = columnNumbers.length;
	        totalNumbers = new int[len];
	        for (int i = 0; i < len; i++) {
	            totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
	        }
	        
	        // return result
	        return totalNumbers;
        }
		catch(Exception e){
			System.out.println("NGDSeriesAssembler::retrieveTotals failed !!!");
			totalNumbers = new int[0];
	        return totalNumbers;
		}	
        finally{
	        DBHelper.closeJDBCConnection(conn);
        }
    }

    public HeaderItem [] createHeader() {
        
        //set the titles of each of the columns in the table
        String headerTitles[] = {Utility.getProject()+" ID", "GEO Sample ID", "Sample ID", "Library Strategy", "Genotype", "Sample Description", "Component(s) Sampled" };
        //specify which columns are sortable
//        boolean headerSortable [] = {false, false, false, false};
        boolean headerSortable [] = {true, true, true, true, true, false, false};
        
        int colNum = headerTitles.length;
        HeaderItem[] tableHeader = new HeaderItem[colNum];
        for(int i=0; i<colNum; i++) {
            tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
        }
                
        return tableHeader;
    }
    
	public NGDSeries getData(String submissionAccessionId) {
		
		if (submissionAccessionId == null || submissionAccessionId.equals("")) {
			return null;
		}
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			NGDDAO ngdDAO = MySQLDAOFactory.getNGDDAO(conn);
			
			// get series details
			NGDSeries series = ngdDAO.findSeriesBySubmissionId(submissionAccessionId);
			
			// get related sample info
			ArrayList relatedSamples = ngdDAO.findSamplesInCertainSeriesBySubmissionId(submissionAccessionId);
			
			// convert to meet the display demand
			ArrayList<String[]> samples = new ArrayList<String[]>();
			int sampleNumber = relatedSamples.size();
			for (int i=0;i<sampleNumber;i++) {
				String[] source = (String[])relatedSamples.get(i);
				String[] target = this.addExtraColumnFromStartOfStringArray(source, ("Sample"+ Integer.toString(i+1)));
				samples.add(target);
			}

			if (relatedSamples != null && relatedSamples.size() > 0) {
				series.setSummaryResults(samples);
			}
			return series;
		}
		catch(Exception e){
			System.out.println("NGDSeriesAssembler::retrieveTotals failed !!!");
			return null;
		}		
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	/**
	 * 
	 * @param source
	 * @param columnValue
	 * @return
	 */
	private String[] addExtraColumnFromStartOfStringArray(String[] source, String columnValue) {
		
		if (source == null || source.length < 1) {
			return null;
		}
		int sourceSize = source.length;
		int resultSize = sourceSize + 1;
		String[] result = new String[resultSize];
		result[0] = columnValue;
		
		for (int i=1;i<resultSize;i++) {
			result[i] = source[i-1];
		}
		return result;
	}
        
        /**
         * returns a Series object containing all data for a particular series,
         * except for associated samples, using the given id parameter
         * @param id - the parameter used to find a particular series
         * @return series - a specific series
         */
        public NGDSeries getSeriesMetaData(){
            if (seriesId == null || seriesId.equals("")) {
                    return null;
            }
            
            /** ---get data from dao---  */
            // create a dao
            Connection conn = DBHelper.getDBConnection();
            try{
                NGDSeries series = null;
            	NGDDevDAO ngdDevDAO = MySQLDAOFactory.getNGDDevDAO(conn);
	            
	            // get series details
	            if (geoId)
	            	series = ngdDevDAO.findSeriesById(seriesId);
	            else
	            	series = ngdDevDAO.findSeriesByOid(seriesId);

	            return series;
            }
    		catch(Exception e){
    			System.out.println("NGDSeriesAssembler::getSeriesMetaData failed !!!");
    			return null;
    		}		
            finally{
	            DBHelper.closeJDBCConnection(conn);
            }
        }



	/********************************************************************************
	 * Returns a 2D array of DataItems populated by data in a list of Series submissions 
	 *
	 */
	public static DataItem[][] getTableDataFormatFromSampleList(ArrayList sampleList)
	{
		if (sampleList==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])sampleList.get(0)).length;
		int rowNum = sampleList.size();
		
        //build the data item containing the actual sample data from the ArrayList 
        DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++) {
			String[] row = (String[])sampleList.get(i); 
//            System.out.println(row[0]+" "+ row[1]+ " "+row[2]+" "+row[3]);
            tableData[i][0] = new DataItem(row[0], "View Sample", "ngd_submission.html?id="+row[0], 10);   // Project Id
            tableData[i][1] = new DataItem(row[1], "View Sample in GEO", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[1], 9);   // Sample GEO Id
            tableData[i][2] = new DataItem(row[2]);   // NGS_SAMPLE_NAME
            tableData[i][3] = new DataItem(row[3]);   // NGP_LIBRARY_STRATEGY
            tableData[i][4] = new DataItem(row[4]);   // GENOTYPE
            tableData[i][5] = new DataItem(row[5]);   // NGS_DESCRIPTION
            tableData[i][6] = new DataItem(row[6]);   // IST_COMPONENT
        }
                
        return tableData;
    }
    
}
