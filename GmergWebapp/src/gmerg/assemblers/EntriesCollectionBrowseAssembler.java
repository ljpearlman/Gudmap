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
	    System.out.println("EntriesCollectionBrowseAssembler::constructor");

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
		if (ids == null || ids.size() == 0) {
			if (debug)
				System.out.println("EntriesCollectionBrowseAssembler::retrieveData | ids=null");
			return null;
		}

		if (null != cache &&
		    cache.isSameQuery(columnIndex, ascending, offset, num)) {
		    if (debug)
		    	System.out.println("EntriesCollectionBrowseAssembler::retrieveData | Cache used. Data not changed");
		    
		    return cache.getData();
		}

		if (debug)
	    	System.out.println("EntriesCollectionBrowseAssembler::retrieveData | ids==" + ids.toString());

		// create dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
	
			String[] subIds = (String[]) ids.toArray(new String[ids.size()]);
	
			int userPrivilege = Utility.getUserPriviledges();
			
			ArrayList viewableSubmissions = ishDevDAO.getCollectionSubmissionBySubmissionId(userPrivilege, 0, subIds, columnIndex, ascending, offset, num);
			if (debug)
		    	System.out.println("EntriesCollectionBrowseAssembler::retrieveData | viewableSubmissions=" + viewableSubmissions);

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
		catch(Exception e){
			if (debug)
		    	System.out.println("EntriesCollectionBrowseAssembler::retrieveData | catch");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
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
	    // force new cache
	    cache = null;

		if (ids == null || ids.size() == 0) 
			return null;
		
		if (debug)
	    	System.out.println("EntriesCollectionBrowseAssembler::retrieveTotals");
        
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		int[] totalNumbers = null;
		try{
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
					"TOTAL_NUMBER_OF_THUMBNAIL_MIX", 
					"TOTAL_NUMBER_OF_TISSUE_MIX",
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
	
			String[] subIds = (String[]) ids.toArray(new String[ids.size()]);
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
			if (debug)
		    	System.out.println("EntriesCollectionBrowseAssembler::retrieveTotals | columnNumbers length="+len);
			totalNumbers = new int[len];
			
			for (int i = 0; i < len; i++) 
				totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
			if (debug)
		    	System.out.println("EntriesCollectionBrowseAssembler::retrieveTotals | totalNumbers="+totalNumbers.toString());
			
			// return result
			return totalNumbers;
		}
		catch(Exception e){
			System.out.println("EntriesCollectionBrowseAssembler::retrieveTotals !!!");
			totalNumbers = new int[0];
			return totalNumbers;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public HeaderItem[] createHeader()	{
		//HeaderItem[] ishHeader = ISHBrowseAssembler.createHeaderForISHBrowseTable();
		String ishHeaderTitles[]={"Gene", Utility.getProject()+" Entry Details", "Source", "Submission Date", 
   		  	 "Assay Type", "Probe Name", Utility.getStageSeriesMed()+" Stage", 
			 "Age", "Sex", "Genotype", "Tissue", "Specimen Type", "Images"};
		boolean ishHeaderSortable[] = {true, true, true, true, true, true, true, true, true, true, true, true, false};
		int ishColNum=ishHeaderTitles.length;
		HeaderItem[] ishHeader = new HeaderItem[ishColNum];
		for(int i=0; i<ishColNum; i++)
			ishHeader[i] = new HeaderItem(ishHeaderTitles[i], ishHeaderSortable[i]);
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
	
	/*public HeaderItem[] createHeader()	{
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
	}*/
	
	
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
			
		/*for(int i=0; i<rowNum; i++) {
			String[] row =  (String[])collectionList.get(i);
			ISHBrowseAssembler.convertIshRowToDataItemFormat(tableData[i], row); 	// Fill the first part with ISH data
			 if(!Utility.getProject().equals("GUDMAP"))
			     continue;
			tableData[i][13] = new DataItem(row[13]);								// Tissue Type
			tableData[i][14] = new DataItem(row[14]);								// Title
			tableData[i][15] = new DataItem(row[15]);								// Description
			tableData[i][16] = new DataItem(row[16], "Click to view GEO entry", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[16], 10);	// Series ID
		}*/
		
		for(int i=0; i<rowNum; i++) {
			String[] row =  (String[])collectionList.get(i);
			tableData[i][0] = new DataItem(row[1], "", "gene.html?gene="+row[1], 10);//gene
			if("Microarray".equalsIgnoreCase(row[6])) 
				tableData[i][1] = new DataItem(row[0], "Click to view Samples page","mic_submission.html?id="+row[0], 10);			//sub id
			else if ("ISH".equalsIgnoreCase(row[6]) || "ISH control".equalsIgnoreCase(row[6])  || "IHC".equalsIgnoreCase(row[6]) || "OPT".equalsIgnoreCase(row[6])) 
				tableData[i][1] = new DataItem(row[0], row[0], "ish_submission.html?id="+row[10], 10);	  			
			else if ("TG".equalsIgnoreCase(row[6]))  
				tableData[i][1] = new DataItem(row[0], row[0], "ish_submission.html?id="+row[0], 10);
			else if ("NextGen".equalsIgnoreCase(row[6]))  
				tableData[i][1] = new DataItem(row[0], row[0], "ngd_submission.html?id="+row[0], 10);//entry details	
			//tableData[i][1] = new DataItem(row[0]);//entry details
			tableData[i][2] = new DataItem(row[4], "Source details", "lab_detail.html?id="+row[4], 6, 251, 500);//source
			tableData[i][3] = new DataItem(Utility.convertToDisplayDate(row[5]));//sub date
			tableData[i][4] = new DataItem(row[6]);//assay type
			if(Utility.getProject().equalsIgnoreCase("GUDMAP")){
				
				if ("IHC".equalsIgnoreCase(row[6]))
					tableData[i][5] = new DataItem(row[9], "Antibody Details", "antibody.html?antibody="+row[9], 10);	
				else
					tableData[i][5] = new DataItem(row[9], "Probe Details", "probe.html?probe="+row[9], 10);
			}
			//tableData[i][5] = new DataItem(row[9]);//probe name
			if(Utility.getProject().equalsIgnoreCase("GUDMAP")) 
				tableData[i][6] = new DataItem(row[2], "", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+row[2]+"definition.html", 10);								// Theiler Stage
			else 
				tableData[i][6] = new DataItem(row[2]);
			//tableData[i][6] = new DataItem(row[2]);//stage
			tableData[i][7] = new DataItem(row[3]);//age
			tableData[i][8] = new DataItem(row[8]);//sex
			tableData[i][9] = new DataItem(Utility.superscriptAllele(row[10]),50);//genotype
			tableData[i][10] = new DataItem(row[11]);//tissue (probe type)
			tableData[i][11] = new DataItem(row[7]);//specimen type
			if(row[12] == null || row[12].trim().equals(""))
				tableData[i][12] = new DataItem("");	// microarrays don't have thumbnails to display
			else if(row[6].equals("OPT")) {
				tableData[i][12] = new DataItem(row[12].substring(0,row[12].lastIndexOf("."))+".jpg", "Click to see submission details for "+ row[0], "ish_submission.html?id="+row[0], 13); // thumbnail (only for OPT)
			}
			else
				tableData[i][12] = new DataItem(row[12], "Click to see submission details for "+ row[0], "ish_submission.html?id="+row[0], 13);
			//tableData[i][12] = new DataItem(row[12]);//images
			 if(!Utility.getProject().equals("GUDMAP"))
			     continue;
			tableData[i][13] = new DataItem(row[13]);								// Tissue Type
			tableData[i][14] = new DataItem(row[14]);								// Title
			tableData[i][15] = new DataItem(row[15]);								// Description
			tableData[i][16] = new DataItem(row[16], "Click to view GEO entry", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[16], 10);	// Series ID
		}
		return tableData;
	 }
	
	static public void convertNewIshRowToDataItemFormat (DataItem[] formatedRow, String[] row) {

		// Gene
		formatedRow[ 0] = new DataItem(row[0], "", "gene.html?gene="+row[0], 10);					

		// GUDMAP entry details
		if("Microarray".equalsIgnoreCase(row[4])) 
			formatedRow[1] = new DataItem(row[1], "Click to view Samples page","mic_submission.html?id="+row[0], 10);			//sub id
		else if ("ISH".equalsIgnoreCase(row[4]) || "ISH control".equalsIgnoreCase(row[4])  || "IHC".equalsIgnoreCase(row[4]) || "OPT".equalsIgnoreCase(row[4])) 
			formatedRow[1] = new DataItem(row[1], row[1], "ish_submission.html?id="+row[1], 10);	  			// Id
		else if ("TG".equalsIgnoreCase(row[4])) // added by xingjun - 27/08/2008 - transgenic data 
			formatedRow[1] = new DataItem(row[1], row[1], "ish_submission.html?id="+row[1], 10);	  			// Id
		else
			formatedRow[1] = new DataItem(row[10]);	// Id

		// Source
		formatedRow[ 2] = new DataItem(row[2], "Source details", "lab_detail.html?id="+row[1], 6, 251, 500);	

		// Submission Date
		formatedRow[ 3] = new DataItem(Utility.convertToDisplayDate(row[3]));	

		// Assay Type
		formatedRow[ 4] = new DataItem(row[4]);

		// Probe Name
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")){
			
			if ("IHC".equalsIgnoreCase(row[4]))
				formatedRow[ 5] = new DataItem(row[5], "Antibody Details", "antibody.html?antibody="+row[5], 10);	
			else
				formatedRow[ 5] = new DataItem(row[5], "Probe Details", "probe.html?probe="+row[5], 10);
		}
		
		// Theiler Stage
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")) 
		    formatedRow[ 6] = new DataItem(row[6], "", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+row[6]+"definition.html", 10);								// Theiler Stage
		else 
		    formatedRow[ 6] = new DataItem(row[6]);  
		
		// Age
		formatedRow[ 7] = new DataItem(row[7]);	
		
		// Sex
		formatedRow[ 8] = new DataItem(row[8]);
		
		// Genotype
		/*formatedRow[ 9] = new DataItem(row[9]);*/
		formatedRow[ 9] = new DataItem(Utility.superscriptAllele(row[9]),50);

		// Tissue
		formatedRow[ 10] = new DataItem(row[10]);
		
		// In Situ Expression
		if(row[11]==null){
			formatedRow[11] = new DataItem("");
		}
		else
		{
			String expression = row[11];
			if (expression.contains("present"))
				formatedRow[11] = new DataItem("present");
			else if (expression.contains("uncertain"))
				formatedRow[11] = new DataItem("uncertain");
			else if (expression.contains("not detected"))
				formatedRow[11] = new DataItem("not detected");
			else
				formatedRow[11] = new DataItem("");
		}
		// Specimen Type
		formatedRow[ 12] = new DataItem(row[12]);
		
		// Image
		if(row[13] == null || row[13].trim().equals(""))
			formatedRow[13] = new DataItem("");	// microarrays don't have thumbnails to display
		else if(row[4].equals("OPT")) {
			formatedRow[13] = new DataItem(row[13].substring(0,row[13].lastIndexOf("."))+".jpg", "Click to see submission details for "+ row[1], "ish_submission.html?id="+row[1], 13); // thumbnail (only for OPT)
		}
		else
			formatedRow[13] = new DataItem(row[13], "Click to see submission details for "+ row[1], "ish_submission.html?id="+row[1], 13);	// thumbnail
		
	}

}

