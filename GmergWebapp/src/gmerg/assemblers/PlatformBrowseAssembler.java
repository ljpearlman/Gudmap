package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.FocusForAllDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class PlatformBrowseAssembler extends OffMemoryTableAssembler{
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;
	String organ;
	
	public PlatformBrowseAssembler() {
	if (debug)
	    System.out.println("PlatformBrowseAssembler.constructor");
	       
	}
	
	public PlatformBrowseAssembler(HashMap params) {
		super(params);
	if (debug)
	    System.out.println("PlatformBrowseAssembler.constructor");
	}

	public void setParams() {
		super.setParams();
		organ = getParam("organ");
	}
	
	public DataItem[][] retrieveData(int columnIndex, boolean ascending, int offset, int num) {
	    if (null != cache && cache.isSameQuery(columnIndex, ascending, offset, num)) {
			if (debug)
			    System.out.println("PlatformBrowseAssembler.retriveData data not changed");
		
			return cache.getData();
	    }
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
			
			// get data from database
			ArrayList platformsList = focusForAllDAO.getPlatformList(columnIndex, ascending, offset, num, organ);
//			System.out.println("ser num: " + ((String[])platformsList.get(0))[4]);
//			System.out.println("ser num: " + ((String[])platformsList.get(1))[4]);
//			System.out.println("ser num: " + ((String[])platformsList.get(2))[4]);
			// return the value object
			DataItem[][] ret = getTableDataFormatFromPlatformsList(platformsList);

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
			System.out.println("PlatformBrowseAssembler::retrieveData failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public int retrieveNumberOfRows() {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
			int numberOfPlatforms = focusForAllDAO.getNumberOfPlatform(organ);
			return numberOfPlatforms;
		}
		catch(Exception e){
			System.out.println("PlatformBrowseAssembler::retrieveNumberOfRows failed !!!");
			return 0;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public HeaderItem[] createHeader()	{
		 String headerTitles[] = { "GEO ID", "Name", "Technology", "Manufacturer", "Number of Series" };
		 boolean headerSortable[] = { true, true, true, true, true };

		 int colNum = headerTitles.length;
		 HeaderItem[] tableHeader = new HeaderItem[colNum];
		 for(int i=0; i<colNum; i++)
			 tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		 return tableHeader;
	}
	
	/********************************************************************************
	 * Returns a 2D array of DataItems populated by data in a list of platforms 
	 *
	 */
	public static DataItem[][] getTableDataFormatFromPlatformsList(ArrayList platformsList)
	{
		if (platformsList==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])platformsList.get(0)).length;
		int rowNum = platformsList.size();
		
        //build the data item containing the actual platform data from the ArrayList 
        DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++) {
				String[] row = (String[])platformsList.get(i); 
				tableData[i][0] = new DataItem(row[0], "Click to view GEO page", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[0], 2);
				tableData[i][1] = new DataItem(row[1]); // platform name
				tableData[i][2] = new DataItem(row[2]); // technology
				tableData[i][3] = new DataItem(row[3]); // manufacturer
				tableData[i][4] = new DataItem(row[4], "Click to view Series list page", "series_browse.html?seriesId="+row[1]+"&platform="+row[0], 10); // linked series number
			}
			return tableData;
		 }
	
}
	