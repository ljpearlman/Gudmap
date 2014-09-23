/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.CollectionDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.TableUtil;
import gmerg.entities.Globals;

import java.util.ArrayList;
import java.sql.Connection;
import java.util.HashMap;


/**
 * @author xingjun
 *
 */
public class CollectionListAssembler extends OffMemoryTableAssembler{
    int userId;
    int collectionType; // 0 - submission collections; 1 - gene collections; 2 - query collections
    int status; // 0 - own collections; 1 - others' collections; 2 - both
    private boolean debug = false;

    public CollectionListAssembler (HashMap params) {
        super(params);
	if (debug)
	    System.out.println("CollectionListAssembler.constructor");

    }

    public void setParams() {
    	super.setParams();
        userId = (Integer)params.get("userId");
        collectionType = (Integer)params.get("collectionType");
        status = (Integer)params.get("status");
    }
    
    /**
     * if it's submission collection, get submission ids;
     * 
     * if it's gene collection, get gene symbols
     */
    public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
    	 
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        try{
        	CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
        	ArrayList collectionData = collectionDAO.getCollections(userId, collectionType, status, column, ascending, offset, num);
	        return getTableDataFormatFromIshList(collectionData);
        }
		catch(Exception e){
			System.out.println("CollectionListAssembler::retrieveData failed !!!");
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
        	CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
        	int totalNumber = collectionDAO.getTotalNumberOfCollections(userId, collectionType, status);
	        return totalNumber;
        }
		catch(Exception e){
			System.out.println("CollectionListAssembler::retrieveNumberOfRows failed !!!");
			return 0;
		}
        finally{
	        DBHelper.closeJDBCConnection(conn);
        }
    }

	public HeaderItem[] createHeader() {
		String headerTitles[] = {"Name", "Id", "Description", "Owner Id", "Owner", "Focus group", "Entity Count", "Status", "Last modifed", "Download", "Share" };
		boolean headerSortable[] = {true, true, true, true, true, true, true, true, true, false, false };
		int colNum = headerTitles.length;
		HeaderItem[] tableHeader = new HeaderItem[colNum];
		for(int i=0; i<colNum; i++)
			tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		
 		return tableHeader;
	}
	
    
     /********************************************************************************
     * Returns a 2D array of DataItems populated by data in a list of ISH submissions          
     */
    public DataItem[][] getTableDataFormatFromIshList(ArrayList collectionList) {
    	
    	if (collectionList == null) {
    		return null;
    	}
    	
		int rowNum = collectionList.size();
        if(rowNum == 0) {
            return null;
        }
        
		// there are two non-database columns need to be added into the array: link for downloading & sending
        int colNum = ((String[])collectionList.get(0)).length + 2;

        DataItem[][] tableData = new DataItem[rowNum][colNum];
        for(int i=0; i<rowNum; i++) {
        	String[] collection = (String[])collectionList.get(i);
            convertCollectionsRowToDataItemFormat(tableData[i], collection);
        }
        return tableData;
     }
   
    //////////////////////////////////////////
    // collection column definition
    // 0 - collection id
    // 1 - name
    // 2 - description
    // 3 - owner id
    // 4 - owner name
    // 5 - focus group id
    // 6 - entry number
    // 7 - status id
    // 9 - last modified date
    // 10 - type
    // 11 - focus group name - not used for display at moment - 22/09/2009
    // 12 - download (not an database column)
    // 13 - share (not an database column)
    //////////////////////////////////////////
    public void convertCollectionsRowToDataItemFormat (DataItem[] formatedRow, String[] row) {
    	//make the ID col =1
    	formatedRow[0] = new DataItem(row[1], "Click to view collection", "collection_browse.html?collectionId="+row[0]+"&collectionType="+String.valueOf(collectionType)+"&focusGroup="+row[10], 10);
    	//formatedRow[0] = new DataItem(row[1], "Click to view collection", "collection_browse.html?collectionId="+row[0]+"&collectionType="+String.valueOf(collectionType)+"&focusGroup="+row[10]+"&cleartabs=true", 10);
    	formatedRow[1] = new DataItem(row[0]);
        /*formatedRow[0] = new DataItem(row[0]);
    	formatedRow[1] = new DataItem(row[1], "Click to view collection", "collection_browse.html?collectionId="+row[0]+"&collectionType="+String.valueOf(collectionType)+"&focusGroup="+row[10], 10);*/
        formatedRow[2] = new DataItem(row[2]);
        formatedRow[3] = new DataItem(row[3]);
        formatedRow[4] = (Integer.parseInt(row[3])==userId) ? new DataItem("own"):new DataItem(row[4]);
        formatedRow[5] = new DataItem(Globals.getFocusGroup(Integer.parseInt(row[5])));
        formatedRow[6] = new DataItem(row[6], "Click to view collection", "collection_browse.html?collectionId="+row[0]+"&collectionType="+String.valueOf(collectionType), 10);   
        formatedRow[7] = new DataItem((row[7].equals("0"))?"private":"public");
        formatedRow[8] = new DataItem(row[8]);
        String script = TableUtil.getActionLinkScript("collectionListButtonsForm", "downloadCollectionLink", "collectionId="+row[0]);
        formatedRow[9] = new DataItem("download", "Click to download this collection" , script, 40);
//        formatedRow[10] = new DataItem("Send", "Click to send collection to another user", "empty_query_result.html", 10);
        formatedRow[10] = new DataItem("Send");
    }
    
} 
