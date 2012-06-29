/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.ISHDevDAO;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryCollectionAssembler;
import gmerg.utils.Utility;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xingjun
 *
 */
public class EntriesCollectionBrowseAssembler extends OffMemoryCollectionAssembler {
    private boolean debug = false;
    protected RetrieveDataCache cache = null;


	public EntriesCollectionBrowseAssembler (HashMap params, CollectionBrowseHelper helper) {
		super(params, helper);
	if (debug)
	    System.out.println("EntriesCollectionBrowseAssembler.constructor");

	}
	
	/**
	 * 
	 * check the db status and user's privilege
	 * if lower than editor, only return public entries
	 * if higher than (Sr.) annotator, return all entries (private & public)
	 * <p>modified by xingjun - 21/10/2009 - changed logic as the same as method retrieveNumberOfRows</p>
	 * get user privilege
	 * @author xingjun 13/10/2009
	 */
	public DataItem[][] retrieveData(int columnIndex, boolean ascending, int offset, int num) {
//		System.out.println("entered EntriesCollectionBrowseAssembler:retrieveData!!!!!!!!");
		if (ids == null || ids.size() == 0) {
//			System.out.println("entered EntriesCollectionBrowseAssembler:retrieveData:ids is null!!!!!!!!");
			return null;
		}

		if (null != cache &&
		    cache.isSameQuery(columnIndex, ascending, offset, num)) {
		    if (debug)
			System.out.println("EntriesCollectionBrowseAssembler.retriveData data not changed");
		    
		    return cache.getData();
		}

		// System.out.println("EntriesCollectionBrowseAssembler    ids==" + ids.toString());

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		String[] subIds = (String[]) ids.toArray(new String[ids.size()]);

		int userPrivilege = Utility.getUserPriviledges();
//		int askedSubmissionNumber = ids.size();
//		System.out.println("EntriesCollectionAssembler:retrieveData:askedSubNum: " + askedSubmissionNumber);
//		System.out.println("EntriesCollectionAssembler:retrieveData:userPrivilege: " + userPrivilege);
		
		ArrayList viewableSubmissions = ishDevDAO.getCollectionSubmissionBySubmissionId(userPrivilege, 0, subIds, columnIndex, ascending, offset, num);
//		System.out.println("EntriesCollectionBrowseAssembler:retrieveData:entries: " + viewableSubmissions);

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDevDAO = null;
		
		/** ---return the value object--- */
		DataItem[][] ret = getTableDataFormatFromCollectionList(viewableSubmissions);

		if (null == cache)
		    cache = new RetrieveDataCache();
		cache.setData(ret);
		cache.setColumn(columnIndex);
		cache.setAscending(ascending);
		cache.setOffset(offset);
		cache.setNum(num);

		return ret;
	}
	
	//Note: this is exceptionally overwrites the super class method; because multiple rows can be displayed for the same entry ID (due to different tissue types)
	
	/**
	 * @author xingjun - 13/10/2009
	 */
/*	
	public int retrieveNumberOfRows() {
		if (ids == null || ids.size() == 0) {
			return 0;
		}
		Connection conn = DBHelper.getDBConnection();
		ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
		
		int userPrivilege = Utility.getUserPriviledges();
		int askedSubmissionNumber = ids.size();
//		System.out.println("EntriesCollectionAssembler:retrieveNumberOfRows:askedSubNum: " + askedSubmissionNumber);
//		System.out.println("EntriesCollectionAssembler:retrieveNumberOfRows:userPrivilege: " + userPrivilege);

		ArrayList viewableSubmissions = arrayDevDAO.getRelevantGudmapIds(ids, userPrivilege);
		int viewableSubmissionNumber = 0;
		if (viewableSubmissions != null) {
			viewableSubmissionNumber = viewableSubmissions.size();
		}
//		System.out.println("EntriesCollectionBrowseAssembler:retrieveNumberOfRows:NoOfRows: " + viewableSubmissionNumber);
		return viewableSubmissionNumber;
	}
*/	

	/**
	 * <p>modified by xingjun - 14/10/2009 - invoke overloading method to get endingClause value</p>
	 */
	public int[] retrieveTotals() {
		if (ids == null || ids.size() == 0) 
			return null;
        
		/** ---get data from dao--- */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);

		// get data from database
		String[] allColTotalsQueries = {
				"TOTAL_NUMBER_OF_SUBMISSION_MIX",
				"TOTAL_NUMBER_OF_GENE_SYMBOL_MIX",
				"TOTAL_NUMBER_OF_THEILER_STAGE_MIX",
				"TOTAL_NUMBER_OF_AGE_MIX",
				"TOTAL_NUMBER_OF_LAB_MIX",
				"TOTAL_NUMBER_OF_SUBMISSION_DATE_MIX",
				"TOTAL_NUMBER_OF_ASSAY_TYPE_MIX",
				"TOTAL_NUMBER_OF_SPECIMEN_TYPE_MIX",
				"TOTAL_NUMBER_OF_SEX_MIX",
				// "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL_MIX",
				"TOTAL_NUMBER_OF_PROBE_NAME_MIX",
				// "TOTAL_NUMBER_OF_ANTIBODY_NAME_MIX",
				// "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL_MIX",
				"TOTAL_NUMBER_OF_GENOTYPE_MIX",
				"TOTAL_NUMBER_OF_PROBE_TYPE_MIX",
				"TOTAL_NUMBER_OF_THUMBNAIL_MIX", "TOTAL_NUMBER_OF_TISSUE_MIX",
				"TOTAL_NUMBER_OF_SAMPLE_TITLE_MIX",
				"TOTAL_NUMBER_OF_SAMPLE_DESCRIPTION_MIX",
				"TOTAL_NUMBER_OF_SERIES_MIX" };

		// use for EuReGene Projects
		String[] allColTotalsQueriesISH = { "TOTAL_NUMBER_OF_SUBMISSION",
				"TOTAL_NUMBER_OF_GENE_SYMBOL", "TOTAL_NUMBER_OF_THEILER_STAGE",
				"TOTAL_NUMBER_OF_GIVEN_STAGE", "TOTAL_NUMBER_OF_LAB",
				"TOTAL_NUMBER_OF_SUBMISSION_DATE",
				"TOTAL_NUMBER_OF_ASSAY_TYPE", "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
				"TOTAL_NUMBER_OF_SEX", "TOTAL_NUMBER_OF_PROBE_NAME",
				"TOTAL_NUMBER_OF_GENOTYPE", "TOTAL_NUMBER_OF_PROBE_TYPE",
				"TOTAL_NUMBER_OF_IMAGE" };

		String[] subIds = (String[]) ids
				.toArray(new String[ids.size()]);
		String endingClause = "";
		String[][] columnNumbers;
		if (Utility.getProject().equals("GUDMAP")) {
			int userPrivilege = Utility.getUserPriviledges();
//			endingClause = ishDevDAO.getCollectionTotalsQueryEndingClause(subIds);
			endingClause = ishDevDAO.getCollectionTotalsQueryEndingClause(userPrivilege, subIds);
			columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);
		} 
		else {
			endingClause = ishDAO.getCollectionTotalsSubmissionISHQuerySection(subIds);
			columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueriesISH, endingClause, filter);
		}

		// convert to interger array, each tuple consists of column index and
		// the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		
		for (int i = 0; i < len; i++) 
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);

		// return result
		return totalNumbers;
	}
	
	public HeaderItem[] createHeader()	{
		HeaderItem[] ishHeader = ISHBrowseAssembler.createHeaderForISHBrowseTable();
		String arraySampleHeaderTitles[] = {"Tissue Type", "Title", "Description", "Series ID"};
		boolean arraySampleHeaderSortable[] = {true, true, true, true};
		int colNum = arraySampleHeaderTitles.length;
		HeaderItem[] arraySampleHeader = new HeaderItem[colNum];
		for(int i=0; i<colNum; i++)
			arraySampleHeader[i] = new HeaderItem(arraySampleHeaderTitles[i], arraySampleHeaderSortable[i]);
		if(!Utility.getProject().equals("GUDMAP"))
			return ishHeader;
		
        colNum = ishHeader.length + arraySampleHeader.length;
		HeaderItem[] collectionHeader = new HeaderItem[colNum];
		for(int i=0; i<ishHeader.length; i++)
			collectionHeader[i] = ishHeader[i];
		for(int i=0, j=ishHeader.length; i<arraySampleHeader.length; i++)
			collectionHeader[j+i] = arraySampleHeader[i];
                
 		return collectionHeader;
	}    
	
	
	/***************************************************************************
	 * populates collection[operation] data table
	 * 
	 */
	public static DataItem[][] getTableDataFormatFromCollectionList(ArrayList collectionList) {
		if (collectionList==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])collectionList.get(0)).length;
		int rowNum = collectionList.size();
		
//		System.out.println("collection Assembler retrieved collection rows="+rowNum+  " col="+colNum);		
	    
		DataItem[][] tableData = new DataItem[rowNum][colNum];
			
		for(int i=0; i<rowNum; i++) {
			String[] row =  (String[])collectionList.get(i);
			ISHBrowseAssembler.convertIshRowToDataItemFormat(tableData[i], row); 	// Fill the first part with ISH data
			 if(!Utility.getProject().equals("GUDMAP"))
			     continue;
			tableData[i][13] = new DataItem(row[13]);								// Tissue Type
			tableData[i][14] = new DataItem(row[14]);								// Title
			tableData[i][15] = new DataItem(row[15]);								// Description
			tableData[i][16] = new DataItem(row[16], "Click to view GEO entry", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[16], 10);	// Series ID
		}
		return tableData;
	 }

}

