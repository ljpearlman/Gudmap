/**
 * 
 */
package gmerg.assemblers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.sql.Connection;

import gmerg.utils.RetrieveDataCache;
import gmerg.db.AdvancedSearchDBQuery;
import gmerg.db.DBHelper;
import gmerg.db.GeneStripDAO;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.DbUtility;
import gmerg.utils.Utility;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryCollectionAssembler;
import gmerg.entities.Globals;
import gmerg.entities.submission.array.MasterTableInfo;
import gmerg.entities.submission.ImageInfo;
import gmerg.entities.ChromeDetail;
/**
 * @author xingjun
 *
 */
public class GeneStripAssembler extends OffMemoryCollectionAssembler {
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

	static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	public GeneStripAssembler (HashMap params, CollectionBrowseHelper helper) {
		super(params, helper);
	if (debug)
	    System.out.println("GeneStripAssembler.constructor");

	}

	/**
	 * @author xingjun - 05/11/2008
	 * <p>modified by xingjun - 27/01/2009 
	 * 1. display stage range (including insitu & array) in the stage column rather than 
	 * insitu stage range and number of insitu submissions; 
	 * 2. add a link to stage summary page </p>
	 * 30/01/2009 - add link to disease page; calculate relevant disease number
	 * @param symbolList
	 * @return
	 */

	/**
	 * modified by xingjun - 18/03/2009 - modified way to get stage range
	 * <p>modified by xingjun - 22/04/2009 
	 *    - added link on gene symbol to the gene page.
	 *    - modified hint for image matrix link
	 * </p>
	 * 
	 */
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (null != cache && cache.isSameQuery(column, ascending, offset, num)) {
		if (debug)
		    System.out.println("GeneStripAssembler.retriveData data not changed");
		
		return cache.getData();
	    }


		if (ascending || column <0) 
			Collections.sort(ids);// natural sort the gene symbols
		else
			Collections.sort(ids, Collections.reverseOrder());// natural sort the gene symbols
		
	    if (debug) {
		System.out.println("geneStripAssembler:retrieveData:original symbol number: " + ids.size());
		System.out.println("geneStripAssembler:retrieveData:original symbols: " + ids.toString());
	    }

		int len = ids.size();
		
		// calculate to display
		// - get required symbols to assemble gene strip(s)
		// - ignore parameter 'column': only symbol column is sortable
		ArrayList<String> requiredSymbols = new ArrayList<String>();
		for (int i=0, j=0;j<num; i++) {
			int position = offset + i;
			if (position == len) 
				break; // number of available symbols might be less than num
			String symbol = ids.get(position); 
			if (symbol==null || symbol.equals(""))
				continue;
			requiredSymbols.add(symbol);
			j++;
		}
		
		int geneStripArraySize = requiredSymbols.size();
//		System.out.println("geneStripAssembler:retrieveData:required symbol number: " + requiredSymbols.size());
//		System.out.println("geneStripAssembler:retrieveData:required symbols : " + requiredSymbols.toString());
		
		DataItem[][] data = new DataItem[geneStripArraySize][9];

		// connect to database
		Connection conn = DBHelper.getDBConnection();
		try{
			DataItem element = null;
			for (int i=0;i<geneStripArraySize;i++) {
				/** 1 - symbol */
				String symbol = requiredSymbols.get(i);
				if (getParam("geneSymbolNoLink")!=null)	// this is used for display of genestrip in the gene details page inwhich symbol shouldn't be a link
					data[i][0] = new DataItem(symbol);
				else
					data[i][0] = new DataItem(symbol, "Click to see detailed information for "+symbol, "gene.html?gene="+symbol, 10);
	
				/** 2 - synonyms */
				String synonyms = this.getSynonyms(conn, symbol);
				data[i][1] = new DataItem(synonyms);
	
				// get relevant submissions and their ts, specimen type info
				ArrayList relatedInsituSubmissions = this.getRelatedInsituSubmissions(conn, symbol);
	
				/** 3 - number of diseases */
				int diseaseNumber = this.getNumberOfDisease(conn, symbol);
				String diseaseString = "OMIM(" + Integer.toString(diseaseNumber) + ")";
				// not display link if there's no relevant disease
				if (diseaseNumber ==0) {
					data[i][2] = new DataItem(diseaseString);
				} else {
					data[i][2] = 
						new DataItem(diseaseString, "Click to see disease detail for "+symbol, 
								gmerg.utils.Utility.domainUrl+"gudmap_dis/Gene_Result.jsp?gene="+symbol+"&gene_text="+symbol, 10);
				}
	
				/** 4 - developmental stage */
				// insitu stages
				String[] insituGeneStages = this.getGeneStagesInsitu(conn, symbol);
				// array stages
				String[] arrayGeneStages = this.getGeneStagesArray(conn, symbol);
				String[] geneStageRange = this.getGeneStages(insituGeneStages, arrayGeneStages);
				// stage string
				String stage = "";
				if (geneStageRange[0] != "-1" || geneStageRange[1] != "-1") {
					stage = "TS" + geneStageRange[0] + "-" + geneStageRange[1];
	//				System.out.println("stage range: " + stage);
					data[i][3] = 
						new DataItem(stage, "Click to see stage summary for "+symbol, 
								"focus_stage_browse.html?gene="+symbol, 10);
				} else {
					stage = "N/A";
					data[i][3] = 
						new DataItem(stage);
				}
	
				/** 5 - in situe expression profile */
				double[] insituExprofile = this.getExpressionProfile(symbol);
	//			for (int j=0;j<insituExprofile.length;j++) System.out.println("insituEF-" +i+ "-" + j + ": " + insituExprofile[j]);
				String[] interestedAnatomyStructures = 
					AdvancedSearchDBQuery.getInterestedAnatomyStructureIds();
				data[i][4] = 
					new DataItem(getExpressionHtmlCode(insituExprofile, interestedAnatomyStructures, symbol), 50);
	
				/** 6 - representative image */
				// choose the 'right' one based on the discussion with DD
				String candidateSubmission = 
					this.chooseRepresentativeInsituSubmission(relatedInsituSubmissions);
				String thumbnail = null;
				// get the image and put the url into the string
				if (candidateSubmission != null) {
					thumbnail = this.getThumbnailURL(conn, candidateSubmission);
	//				data[i][5] = new DataItem(thumbnail, "Click to see submission details for "+ candidateSubmission, "image_matrix_browse.html?gene="+symbol, 13);
					data[i][5] = new DataItem(thumbnail, "Click to see image matrix for "+symbol, "image_matrix_browse.html?gene="+symbol, 13);
				} else {
					thumbnail = "N/A";
					data[i][5] = new DataItem(thumbnail);
				}
	
				/** 7 - array expression profile */
				ArrayList<DataItem> complexValue = new ArrayList<DataItem>();
				MasterTableInfo[] masterTableInfo = DbUtility.getAllMasterTablesInfo();
				String geneSymbol = requiredSymbols.get(i);
				for (MasterTableInfo item : masterTableInfo) 
				    if (DbUtility.retrieveGeneProbeIds(geneSymbol, item.getPlatform()) != null) {//check to see if there is possible data for this symbol (it is to avoid refering to null images which display as a crsss icon in IE) 
					element = new DataItem("../dynamicimages/heatmap_" + geneSymbol + ".jpg?tile=5&masterTableId="+item.getId(), 
							       "Click to see " + item.getTitle() + " microarray expression profile for "+ symbol, 
							       "mastertable_browse.html?gene="+symbol+"&masterTableId="+item.getId()+"&cleartabs=true", 15);
					if (debug) 
					    System.out.println("GeneStripAssembler.retrieveData value = "+element.getValue()+" title = "+element.getTitle()+" link = "+element.getLink());
					complexValue.add(element);
				    }
				
				data[i][6] = new DataItem(complexValue, 81);	// 81 is complex & centre aligned
				
				/** 8 - RNA_SEQ */
				// Bernie 5/7/2011 - added new column for RNA_SEQ
				// requires link to USCS Browser
				String ucscUrlPrefix = "http://genome.ucsc.edu/cgi-bin/hgTracks?position=chr";
				
				// used when accessed by session id
				String ucscUrlSuffix = "&hgsid=";
				String nextGenSeqString = "View on UCSC Browser";
				String ucscSessionId = "203342519";
				
				// used when accessed by user name and session name
				String ucscUrlHgS_doOtherUser = "hgS_doOtherUser=" + "submit";
				//String ucscUrlHgS_OtherUserName= "hgS_doOtherUser" + "R.thiagarajan26";
				String ucscUrlHgS_OtherUserName= "hgS_otherUserName=" + "Simon%20Harding";
				//String ucscUrlHgs_OtherUserSessionName = "hgS_otherUserSessionName=" + "UQGUDMAP";
				String ucscUrlHgs_OtherUserSessionName = "hgS_otherUserSessionName=" + "gudmap_1";
				boolean ucscUrlWithSessionId = false;
	
				// get chrome info from the database
				ChromeDetail chromeDetail = this.getChromeDetail(conn, symbol);
				if (chromeDetail == null) {
					data[i][7] = new DataItem(nextGenSeqString);
				} else {
//					String ucscUrl = "";
//					if (ucscUrlWithSessionId) { // access by session id
//						ucscUrl = ucscUrlPrefix + chromeDetail.getChromeName() + ":" + 
//						chromeDetail.getChromeStart() + "-" + chromeDetail.getChromeEnd() + ucscUrlSuffix + ucscSessionId;
//					} else { // access by user name and session name
//						ucscUrl = ucscUrlPrefix + chromeDetail.getChromeName() + ":" + 
//						chromeDetail.getChromeStart() + "-" + chromeDetail.getChromeEnd() + "&" +
//						ucscUrlHgS_doOtherUser + "&" + ucscUrlHgS_OtherUserName + "&" + ucscUrlHgs_OtherUserSessionName;
//					}
					//Bernie 28 Aug 2014 - mantis983
					String ucscUrl = "http://genome.ucsc.edu/cgi-bin/hgTracks?db=mm9&hubUrl=http://www.gudmap.org/Gudmap/ngsData/gudmap_ucsc_hub/hub.txt&position="+symbol;
					data[i][7] = 
						new DataItem(nextGenSeqString, "", ucscUrl, 10);
						//new DataItem(nextGenSeqString, "Click to see RNA-SEQ data on UCSC genome browser for " + symbol, ucscUrl, 10);
				}
	
				/** 9 - geneset */
				// not decided yet, check with ED & duncan
				// leave it for time being ///////////////
	//			String geneSetNumber = "0";
				data[i][8] = new DataItem("Genesets(n)");
			}
			
			if (null == cache)
			    cache = new RetrieveDataCache();
			cache.setData(data);
			cache.setColumn(column);
			cache.setAscending(ascending);
			cache.setOffset(offset);
			cache.setNum(num);	

			return data;
		}
		catch(Exception e){
			System.out.println("GeneStripAssembler::retrieveData !!!");
			data = new DataItem[0][0];
			return data;
		}	
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public HeaderItem[] createHeader()	{
		String headerTitles[] = {"Gene", "Synonyms", "Disease", "Theiler Stage", 
								 "Expression Profile",  
								 "Expression Images", "Microarray expression profile", "RNA-SEQ", "Genesets"};
		boolean headerSortable[] = {true, false, false, false, false, false, false, false, false, false};
		int colNum = headerTitles.length;
		HeaderItem[] header = new HeaderItem[colNum];
		for(int i=0; i<colNum; i++)
			if (i==4)
				header[i] = new HeaderItem(headerTitles[i]+getExprssionHeaderHtmlCode(), headerTitles[i], headerSortable[i]);
			else
				header[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		
 		return header;
	}    
	
	/**
	 * modified by xingjun - 03/03/2009 
	 * - there might be more than one geneStrips at the same page, the div id should be identical in the
	 *   document: in this case symbols were selected 
	 * @param values
	 * @param focusGroups
	 * @param symbol
	 * @return
	 */
	public static String getExpressionHtmlCode(double[] values, String[] focusGroups, String symbol) {
		// added by xingjun - 08/05/2009 - its possible values is null
		if (values == null || values.length == 0) {
			return "";
		}
		
//		String code = "<div id='exprLevelsGraph_focus_group' style='text-align:center'></div>";
		String code = "<div id='exprLevelsGraph_" + symbol + "' style='text-align:center'></div>";
		// get focus group string list
		String focusGroupString = "";
		for (int i=0;i<focusGroups.length;i++) {
			focusGroupString += focusGroups[i] + "+','+";
		}
		focusGroupString = focusGroupString.substring(0, (focusGroupString.length()-1));
//		focusGroupString += "0";
//		System.out.println("focusGroupString: " + focusGroupString);
		
		// url
		String browseLink = "'focus_insitu_browse.html?gene=" + symbol + "&focusedOrgan='";
		
		// concatenate script string
    	code += "<script>var val=";
    	for (int i=0; i<values.length; i++)
    		code += ((i==0)? "" : "+','+") + String.valueOf(values[i]);
    	// focus group string
    	code += ";focusGroups=" + focusGroupString + ";";
    	// browse link string
    	code += "browseLink=" + browseLink + ";";
    	code += "geneSymbol='" + symbol + "';";
    	// javascript function
//    	code += "prepareGraph('focus_group',val,browseLink,focusGroups)</script>";
    	code += "prepareGraph(geneSymbol,val,browseLink,focusGroups)</script>";
//    	System.out.println("bar chart value string: " + code);
    	return code;
	}
	
	public static String getExprssionHeaderHtmlCode() {
		return "<br><div id='chartTableHeaders' style='text-align:center'></div><script>prepareGraphTableHeaders('gene_strip.html?sortName=','Template ID','ASC')</script>";
	}

	/**
	 * @author xingjun - 05/11/2008
	 * @param symbol
	 * @return
	 */
	private String getSynonyms(Connection conn, String symbol) {
		
		if (symbol == null || symbol.equals("")) {
			return "";
		}

		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		String synonyms = geneStripDAO.findSynonymsBySymbol(symbol); 
        geneStripDAO = null;

        return synonyms;
	}
	
	/**
	 * @author xingjun - 07/11/2008
	 * <p>get submission numbers and stage range</p>
	 * @param symbol
	 * @return
	 */
	private String[] getInsituSubmissionNumberAndStageInfo(ArrayList insituSubmissions) {
		String[] insituSubmissionNumberAndStageInfo = null;
		if (insituSubmissions == null || insituSubmissions.size() == 0) {
			insituSubmissionNumberAndStageInfo = new String[2];
			insituSubmissionNumberAndStageInfo[0] = "";
			insituSubmissionNumberAndStageInfo[1] = "";
		} else {
    		int iLen = insituSubmissions.size();
			int counter = 0;
    		ArrayList<String> stageList = new ArrayList<String>();
    		String stageString = "";
    		for (int j=0;j<iLen;j++) {
    			String stage = ((String[])insituSubmissions.get(j))[2];
    			if (!stageList.contains(stage)) {
        			stageList.add(stage);
    			}
    			counter++;
    		}
    		String startStage = stageList.get(0);
    		String endStage = stageList.get(stageList.size()-1);
    		if (startStage.equals(endStage)) {// all submission at the same stage
        		stageString += startStage;
    		} else {
        		stageString += startStage + "-" + endStage.substring(2);
    		}
			insituSubmissionNumberAndStageInfo = new String[2];
    		insituSubmissionNumberAndStageInfo[0] = stageString;
    		insituSubmissionNumberAndStageInfo[1] = Integer.toString(counter);
		}
		return insituSubmissionNumberAndStageInfo;
	}
	
	/**
	 * @author xingjun - 27/01/2009
	 * @param symbol
	 * @return
	 */
	private String[] getGeneStagesInsitu(Connection conn, String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
		
		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		String[] stages = geneStripDAO.getGeneStages(symbol, "insitu");
        geneStripDAO = null;

		return stages;
	}
	
	/**
	 * @author xingjun - 27/01/2009
	 * @param symbol
	 * @return
	 */
	private String[] getGeneStagesArray(Connection conn, String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}

		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		String[] stages = geneStripDAO.getGeneStages(symbol, "Microarray");
        geneStripDAO = null;

		return stages;
	}
	
	/**
	 * @author xingjun - 27/01/2009
	 * modified by xingjun - 18/03/2009 - changed the way to find out the stage range
	 * @param insituGeneStages
	 * @param arrayGeneStages
	 * @return stage range of -1 to -1 when there's no relevant submissions for given gene;
	 *         otherwise relevant stage range
	 */
	private String[] getGeneStages(String[] insituGeneStages, String[] arrayGeneStages) {

		String[] stageRange = new String[2];
		if (insituGeneStages == null || insituGeneStages.length == 0) { //  no insitu submission
//			System.out.println("insitu stage value is null");
			if (arrayGeneStages == null || arrayGeneStages.length == 0) { // no array submission
//				System.out.println("array stage value is null");
				stageRange[0] = "-1";
				stageRange[1] = "-1";
			} else { // do have array submissions
				int aLen = arrayGeneStages.length;
				stageRange[0] = arrayGeneStages[0];
				stageRange[1] = arrayGeneStages[aLen-1];
			}
		}  else { // do have insitu submissions
//			System.out.println("insitu stage value is not null########");
			int iLen = insituGeneStages.length;
			if (arrayGeneStages == null || arrayGeneStages.length == 0) { // no array submission
				stageRange[0] = insituGeneStages[0];
				stageRange[1] = insituGeneStages[iLen-1];
			} else { // do have array submissions
//				System.out.println("array stage value is not null#########");
				int aLen = arrayGeneStages.length;
				// get earliest stage of insitu and array
				int ealiestStageInsitu = Integer.parseInt(insituGeneStages[0]);
				int ealiestStageArray = Integer.parseInt(arrayGeneStages[0]);
				// get latest stage of insitu and array
				int latestStageInsitu = Integer.parseInt(insituGeneStages[iLen-1]);
				int latestStageArray = Integer.parseInt(arrayGeneStages[aLen-1]);
				// get stage range
				// earliest
				if (ealiestStageInsitu <= ealiestStageArray) {
					stageRange[0] = insituGeneStages[0];
				} else {
					stageRange[0] = arrayGeneStages[0];
				}
				// latest
				if (latestStageInsitu >= latestStageArray) {
					stageRange[1] = insituGeneStages[iLen-1];
				} else {
					stageRange[1] = arrayGeneStages[aLen-1];
				}
			}
		}
//		System.out.println("stage ragne e: " + stageRange[0]);
//		System.out.println("stage ragne l: " + stageRange[1]);
		return stageRange;
	}
	
	/**
	 * @author xingjun - 07/11/2008
	 * <p>get in situ submissions linked to given gene symbol</p>
	 * @param symbol
	 * @return
	 */
	private ArrayList getRelatedInsituSubmissions(Connection conn, String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}

		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
        ArrayList relatedInsituSubmission = ishDAO.findRelatedSubmissionBySymbolISH(symbol);
        ishDAO = null;

		return relatedInsituSubmission;
	}
	
	/**
	 * @author xingjun - 07/11/2008
	 * @param insituSubmissions
	 * @return
	 */
	private String chooseRepresentativeInsituSubmission(ArrayList insituSubmissions) {
		if (insituSubmissions == null || insituSubmissions.size() == 0) {
			return null;
		}
		int len = insituSubmissions.size();
		String submissionId = null;
		/** TS23 **/
		// try to find submission at ts23 & with assay type 'section'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS23") && assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
//				System.out.println("submission id(" + submissionId +") chosen at ts23 - section!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		
		// failed, try to find submission at ts23 & with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS23") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
//				System.out.println("submission id(" + submissionId +") chosen at ts23 - wholemount!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		
		/** TS21 **/
		// failed, try to find submission at ts21 & with assay type 'section'
//		System.out.println("check ts21########");
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
//			System.out.print("stage: " + stage);
//			System.out.println("##assayType" + assayType);
			if (stage.equals("TS21") && assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
//				System.out.println("submission id(" + submissionId +") chosen at ts21 - section!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		// failed, try to find submission at ts21 & with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS21") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
//				System.out.println("submission id(" + submissionId +") chosen at ts21 - wholemount!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}

		/** TS20 **/
		// failed, try to find submission at ts20 & with assay type 'section'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS20") && assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
//				System.out.println("submission id(" + submissionId +") chosen at ts20 - section!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		// failed, try to find submission at ts20 & with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS20") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
//				System.out.println("submission id(" + submissionId +") chosen at ts20 - wholemount!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}

		// failed, start from the submissions at stage ts17, head up to the end
		// try to find the first submission with assay type 'section'
		for (int i=0;i<len;i++) {
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				String stage = ((String[])insituSubmissions.get(i))[2];
//				System.out.println("submission id(" + submissionId +") chosen at " + stage + " - section!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		
		// failed, start from the submissions at stage ts17, head up to the end
		// try to find the first submission with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				String stage = ((String[])insituSubmissions.get(i))[2];
//				System.out.println("submission id(" + submissionId +") chosen at " + stage + " - wholemount!!");
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}

		// nothing found - impossible!!!
//		System.out.println("Nothing found!!!");
		return null;
	}
	
	/**
	 * 
	 * @param submissionId
	 * @return
	 */
	private String getThumbnailURL(Connection conn, String submissionId) {
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
        // get data from database
		ArrayList imageList = ishDAO.findImageBySubmissionId(submissionId);
		String thumbnailURL = null;
		if (imageList == null || imageList.size() == 0) {
			thumbnailURL = "";
		} else {
			thumbnailURL = ((ImageInfo)imageList.get(0)).getFilePath();
		}

        ishDAO = null;
		
        // return the value object
		return thumbnailURL;
	}
	
	/**
	 * @author xingjun - 20/11/2008
	 * @param symbol
	 * @return
	 */
	private double[] getExpressionProfile(String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
		String[] interestedAnatomyStructures = 
			AdvancedSearchDBQuery.getInterestedAnatomyStructureIds();
		
		// get relevant component id list
		int analen = interestedAnatomyStructures.length;
		int barHeight = Globals.getDefaultExpressionProfileBarHeight();
		// array to store expression profiles
		// 1: present; -1: not detected; 0: not examined/uncertain
		// need to include expression profile not related to given structures - others
//		double[] expressionProfiles = new double[analen+1];
		double[] expressionProfiles = new double[analen];
		ArrayList<String> componentsOfAllGivenStructures = new ArrayList<String>();
		
		/** create a dao */
		Connection conn = DBHelper.getDBConnection();
		try{
			GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
			
			/** calculate expression profile for all given structures */
			for (int i=0;i<analen;i++) {
	//			System.out.println("structure: " + i);
				// get component ids
				String[] componentIds = 
					(String[])AdvancedSearchDBQuery.getEMAPID().get(interestedAnatomyStructures[i]);
				
				// put component ids into componentIdsInAll arrayList
				int eLen = componentIds.length;
	//			System.out.println("component id number: " + eLen);
				for (int j=0;j<eLen;j++) {
					componentsOfAllGivenStructures.add(componentIds[j]);
				}
	
				// get expression info
				ArrayList expressionOfGivenComponents = 
					geneStripDAO.getGeneExpressionForStructure(symbol, componentIds, true);
				
				// start to calculate - only relevant expression exists
				double indicator = 0;
				if (expressionOfGivenComponents != null 
						&& expressionOfGivenComponents.size() != 0) {
					int compLen = expressionOfGivenComponents.size();
					// look for 'present'
					for (int j=0;j<compLen;j++) {
						String expression = ((String[])expressionOfGivenComponents.get(j))[1];
						if (expression.equalsIgnoreCase("present")) {
							indicator = 1.00;
							break;
						}
					}
					
					if (indicator == 0) { // there's no component with expression value of 'present'
						// look for 'not detected'
						for (int j=0;j<compLen;j++) {
							String expression = ((String[])expressionOfGivenComponents.get(j))[1];
							if (expression.equalsIgnoreCase("not detected")) {
								indicator = -1.00;
								break;
							}
						}
					}
				}
				
				// put calculation result into expression profile array
				expressionProfiles[i] = indicator*barHeight;
			}
			/** return result */
			return expressionProfiles;
		
		/** calculate expression profile for other structures */
		//////// comment the code for time being - in case they will be used in future
		}
		catch(Exception e){
			System.out.println("GeneStripAssembler::getExpressionProfile !!!");
			expressionProfiles = new double[0];
			return expressionProfiles;
		}		
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	} // end of getExpressionProfile
	
	/**
	 * @author xingjun - 29/01/2009
	 * @param symbol
	 * @return
	 */
	private int getNumberOfDisease(Connection conn, String symbol) {
		if (symbol == null || symbol.equals("")) {
			return 0;
		}

		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		int diseaseNumber = geneStripDAO.getGeneDiseaseNumber(symbol);
		geneStripDAO = null;

        return diseaseNumber;
	}

	/**
	 * @author xingjun - 21/07/2011
	 */
	private ChromeDetail getChromeDetail(Connection conn, String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
		ChromeDetail chromeDetail = null;

		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		chromeDetail = geneStripDAO.getChromeDetailBySymbol(symbol);
        geneStripDAO = null;

        return chromeDetail;
	}
	
}
