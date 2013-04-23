package gmerg.beans;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import gmerg.assemblers.PredictiveTextSearchAssembler;
import gmerg.assemblers.DBSummaryAssembler;
import gmerg.assemblers.FocusDBSummaryAssembler;
import gmerg.db.AdvancedSearchDBQuery;
import gmerg.entities.Globals;
import gmerg.entities.summary.DBSummary;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import java.util.List;
import java.util.ArrayList;

/**
 * Managed Bean for gudmap database main page 
 *
 * @author Mehran Sharghi
 *
 */

public class DatabaseHomepageBean {
    private boolean debug = false;

	private String focusedOrgan;
	private String quickSearchInput;
	private String anatomyInput;
	private String accessionInput;
	private String geneInput;
	private String geneFunctionInput;
	private String geneWildcard;
	private String geneStage;
	private String geneFunctionStage;
	private String geneAnnotation;
	private String expressionType;
	private boolean includeTransitiveRels;
	private UploadedFile myFile;
	private String input;
	private String query;
	private DBSummary dbSummary;
	private List searchResult = null;
	
	private String geneSearchResultOption; // GUDMAP entries / Gene strip(s)/structure(s)
	private String geneFunctionSearchResultOption; // GUDMAP entries / Gene strip(s)
	private String uploadResultOption;	
	private final int uploadLimit = 100;	// Maximum number of genes allowed for upload in a batch query
	
	private String uploadedGenes; // xingjun - 01/06/2011
	
	static ResourceBundle bundle = ResourceBundle.getBundle("configuration");

	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public DatabaseHomepageBean() {
	    if (debug)
		System.out.println("DatabaseHomepageBean.constructor");

		focusedOrgan = Visit.getRequestParam("focusedOrgan");	// this is automatically picked from url param or current status
		query = FacesUtil.getRequestParamValue("query"); 		// this is provided by action f:param
		input = null;
		quickSearchInput = "";
		anatomyInput = "";
		accessionInput = "";
		geneInput = "";
		anatomyInput = "";
		geneWildcard = "contains";
		geneStage = "ALL";
		geneFunctionStage = "ALL";
		geneAnnotation = "0";
		expressionType = "";
		includeTransitiveRels = false;
		dbSummary = null;	// load dbSummary when first getDbSummary is called
		
//		geneSearchResultOption = "entry"; // GUDMAP entries by default
		geneSearchResultOption = "genes"; // genes by default - xingjun - 12/05/2010
//		uploadResultOption = "entry";
		uploadResultOption = "genes"; // genes by default - xingjun - 14/05/2010
		geneFunctionSearchResultOption = "entry"; // GUDMAP entries by default
	}

    public List autocomplete (Object input) {

	if (null == input)
	    return null;
	
	String pattern =  ((String)input).trim();
	if (pattern.equals(""))
	    return null;
	
	String name = null;
	String value = null;
	String which = null;

	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
	Iterator it = context.getRequestParameterNames();

	while (it.hasNext()) {
	    name = (String)it.next();
	    value = FacesUtil.getRequestParamValue(name);
	    if (!name.equals("inputvalue") && value.equals(input)) {
		if (-1 != name.indexOf("geneInput"))
		    which = "Gene";
		else {
		    if (-1 != name.indexOf("anatomy"))
			which = "Anatomy";
		    else {
			if (-1 != name.indexOf("accession"))
			    which = "Accession ID";
			else {
			    if (-1 != name.indexOf("geneFn"))
				which = "Gene Function";
			}
		    }
		}
		break;
	    }
	}
	if (null == which)
	    return null;

	PredictiveTextSearchAssembler assembler = new PredictiveTextSearchAssembler();
	List ret = null;
	int limit = 20;

	if (which.equals("Gene"))
	    ret = assembler.getGenes(pattern, 1, limit);
	else {
	    if (which.equals("Anatomy"))
		ret = assembler.getAnatomyStructures(pattern, 1, limit);
	    else {
		if (which.equals("Gene Function"))
		    ret = assembler.getGeneFunctions(pattern, 1, limit);
	    }
	}

	return ret;
    }


	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	// modified by xingjun - 11/01/2011 - added the input check statement to avoid exception
	public String search() {

		if("Gene".equals(query)) {
			input = geneInput.trim();
			if(input == null || input.equals("")) 
				return "noResult";
			
			if (this.geneSearchResultOption.equalsIgnoreCase("genes")) {
//				System.out.println("homebean:return GeneQuery!");
	   			return "GeneQuery";
				
			}
//	   		else 
//	   			return "AdvancedQuery";
		}
		else if("Anatomy".equals(query))
			input = anatomyInput.trim();
		else if("Accession ID".equals(query))
			input = accessionInput.trim();
		else if("Gene Function".equals(query))
			input = geneFunctionInput.trim();
		
		if(input == null || input.equals("")) 
			return "noResult";
		return "AdvancedQuery";
	}

	public String getSearchParams() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("input", input);
		params.put("query", query);
		params.put("focusedOrgan", focusedOrgan);
		params.put("geneWildcard", geneWildcard);
		params.put("geneStage", geneStage);
		params.put("geneAnnotation", geneAnnotation);
		params.put("exp", expressionType);
		if(includeTransitiveRels){
			params.put("ttvRels", "1");
		}
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);  

		//////////// added by xingjun - 06/04/2009 ///////////
//		params.put("searchResultOption", searchResultOption);
		//////////////////////////////////////////////////////
		String urlParams = Visit.packParams(params);
		//System.out.println("urlParams = " + urlParams);
		if (urlParams==null)
			return "";
		return "?"+urlParams;
	}

	public String redirectToDatabaseHomepage() {
		return "DatabaseHome";
	}
	
	// ********************************************************************************
	// Public methods
	// ********************************************************************************
	public static String getOrganName(String organ) {
		if (organ==null || "".equals(organ))
			return "";
		
		String[] organs = Globals.getFocusGroups();
		int index = Integer.parseInt(organ);
		return organs[index];
	}
	
	public static String getOrganTitle() {
		String focusedOrgan = Visit.getRequestParam("focusedOrgan");
		if (focusedOrgan==null || "".equals(focusedOrgan))
			return "";
		
		return " in <em>" + getOrganName(focusedOrgan) + "</em>"; 
	}
	
	public static String getStageTitle() {
		String stage = Visit.getRequestParam("stage");
		if (stage==null || "".equals(stage))
			return "";
		
		return " at Theiler stage <em>" + stage + "</em>";  
	}
	
	/**
	 * Navigation method. Processes a text or csv file containing gene lists and redirects the user to an appropriate
	 * page where the input of the file will be used to query the database
	 * @return a string determining where the user should be redirected to 
	 */
	public String processMyFile() {
		int geneNum=0;
	    String uploadedGenes = "";
		try {   
			if (myFile.getName().indexOf("csv") == -1 && myFile.getName().indexOf("CSV") == -1 &&
					myFile.getName().indexOf("txt") == -1 && myFile.getName().indexOf("TXT") == -1 )  
				return null;
			InputStream in = new BufferedInputStream(myFile.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bReader = new BufferedReader(inReader, in.available());
			String oneLine = "";
			String[] splitedLine;
			while (bReader.ready() && geneNum<=uploadLimit) {
				oneLine = bReader.readLine();
				splitedLine = oneLine.split("\t");
				if (splitedLine!=null)
					for (int i=0; i<splitedLine.length; i++) {
						splitedLine[i] = splitedLine[i].trim();
						if (geneNum==0) 		
							if (splitedLine[i].charAt(0) == 0xFEFF) // This is to remove a special character(BOM) added by some editors (e.g oofiice) at the beging of a file 
								splitedLine[i] = splitedLine[i].substring(1).trim();
						if(splitedLine[i]==null || splitedLine[i].equals(""))
							continue;
						geneNum++;
						if (geneNum>uploadLimit) 
							break;
						uploadedGenes += ((geneNum==1)?"":"; ") + splitedLine[i];
					}
			}
			in.close();
			inReader.close();
			bReader.close();
		}
		catch (Exception ex) {
		    ex.printStackTrace();
		} 
            
		query = "Gene";
		input = uploadedGenes;
		if (uploadResultOption.equalsIgnoreCase("genes")) 
			if (geneNum>uploadLimit)
				return MessageBean.showMessage("There are too many genes in your file! only the first 100 are uploaded.", "gene_query_result.jsp"+ getSearchParams());
			else
				return "GeneQuery";
   		else 
//   			if (this.geneSearchResultOption.equalsIgnoreCase("entry"))
			if (geneNum>uploadLimit)
				return MessageBean.showMessage("There are too many genes in your file! only the first 100 are uploaded.", "focus_gene_browse.jsp"+ getSearchParams());
			else
				return "AdvancedQuery";
	} 
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getFocusedOrgan() {
		return focusedOrgan;
	}

	public void setFocusedOrgan(String focusedOrgan) {
		this.focusedOrgan = focusedOrgan;
	}
	                 
	public String getOrganParam() {
		if (focusedOrgan==null || "".equals(focusedOrgan))
			return "";
		
		return "?focusedOrgan=" + focusedOrgan;
	}

	public String getQuickSearchInput() {
		return quickSearchInput;
	}

	public void setQuickSearchInput(String quickSearchInput) {
		this.quickSearchInput = quickSearchInput;
	}

	public String getAnatomyInput() {
		return anatomyInput;
	}

	public void setAnatomyInput(String anatomyInput) {
		this.anatomyInput = anatomyInput;
	}
	
	public String getAccessionInput() {
		return accessionInput;
	}

	public void setAccessionInput(String accessionInput) {
		
		// check for empty string
		if (accessionInput == "")
		{
			this.accessionInput = accessionInput;
		}
		else
		{
			//System.out.println("DatabaseHomepageBean:setAccessionInput accessionInput = " + accessionInput);
					
			String[] accessionList = accessionInput.split("\\;");
			String parsedString = "";
			for (int i=0; i<accessionList.length; i++)
			{
				parsedString += Utility.checkAccessionInput(accessionList[i].trim()) + ";";
			}
			this.accessionInput = parsedString.substring(0,parsedString.length()-1);
			
			//System.out.println("DatabaseHomepageBean:setAccessionInput accessionInput = " + this.accessionInput);
		}
	}
	
	public String getGeneAnnotation() {
		return geneAnnotation;
	}

	public void setGeneAnnotation(String geneAnnotation) {
		this.geneAnnotation = geneAnnotation;
	}

	public String getGeneInput() {
		return geneInput;
	}

	public void setGeneInput(String geneInput) {
		this.geneInput = geneInput;
	}

	public String getGeneFunctionInput() {
		return geneFunctionInput;
	}
	
	public void setGeneFunctionInput(String value){
		geneFunctionInput = value;
	}

	public String getGeneStage() {
		return geneStage;
	}

	public void setGeneStage(String geneStage) {
		this.geneStage = geneStage;
	}

	public String getGeneWildcard() {
		return geneWildcard;
	}

	public void setGeneWildcard(String geneWildcard) {
		this.geneWildcard = geneWildcard;
	}
	
    public UploadedFile getMyFile() {
        return myFile;
    }

    public void setMyFile(UploadedFile myFile) {
        this.myFile = myFile;
    }
    
    public boolean getIncludeTransitiveRels() {
    	return includeTransitiveRels;
    }
    
    public void setIncludeTransitiveRels(boolean value){
    	includeTransitiveRels = value;
    }
    
    public String getExpressionType() {
    	return expressionType;
    }
    
    public void setExpressionType(String value){
    	expressionType = value;
    }
    
    public SelectItem [] getExpressionTypes() {
    	return new SelectItem [] {
    		new SelectItem("all", "all"),
    		new SelectItem("present", "present"),
    		new SelectItem("unknown", "possible"),
    		new SelectItem("absent","not detected")
    	};
    	
    }
    
	public SelectItem[] getWidecardItems() {
		SelectItem[] widecardItems = new SelectItem[]{ new SelectItem("equals", "equals"),
									 //new SelectItem("contains", "contains"),
				new SelectItem("starts with", "starts with") };
		return widecardItems;
	}

	public SelectItem[] getAnnotationItems() {
		SelectItem[] annotationItems = new SelectItem[]{new SelectItem("0", "List all entries"), 
										new SelectItem("1", "List annotated entries only")};
		return annotationItems;
	}

	public SelectItem[] getStageItems() {
		SelectItem[] stageItems = new SelectItem[] {new SelectItem("ALL", "ALL"), 
													new SelectItem("17","TS17"), new SelectItem("18","TS18"), 
                									new SelectItem("19", "TS19"), new SelectItem("20", "TS20"),
                									new SelectItem("21","TS21"), new SelectItem("22", "TS22"),
                									new SelectItem("23","TS23"), new SelectItem("24", "TS24"), 
                									new SelectItem("25", "TS25"), new SelectItem("26", "TS26"), 
                									new SelectItem("27", "TS27"), new SelectItem("28", "TS28")};
		return stageItems;
	}

	public DBSummary getDbSummary() {
		if (dbSummary == null)
			if(focusedOrgan==null || focusedOrgan.equals("")) {
				DBSummaryAssembler dbSummaryAssembler = new DBSummaryAssembler();
				dbSummary = dbSummaryAssembler.getData();
			} else {
				FocusDBSummaryAssembler dbSummaryAssembler = new FocusDBSummaryAssembler();
			    dbSummary = dbSummaryAssembler.getData((String[])AdvancedSearchDBQuery.getEMAPID().get(focusedOrgan));
			}
		return dbSummary;
	}
	
	
	public ArrayList getResult() {
		
		return (ArrayList)searchResult;
	}

	//////// added by xingjun - 06/04/2009 /////////
	// - give user options to display search result: gene strip/submission entries
	public String getGeneSearchResultOption() {
		return this.geneSearchResultOption;
	}
	
	public void setGeneSearchResultOption(String searchResultOption) {
		this.geneSearchResultOption = searchResultOption;
	}
	//////////////////////////////////////////////
	
	public String getGeneFunctionSearchResultOption() {
		return this.geneFunctionSearchResultOption;
	}
	
	public void setGeneFunctionSearchResultOption(String searchResultOption) {
		this.geneFunctionSearchResultOption = searchResultOption;
	}

	/**
	 * <p>xingjun - 12/05/2010 - changed the default gene search options from entry to genes </p>
	 * @return
	 */
	public SelectItem[] getGeneSearchResultOptionItems() {
//        return new SelectItem [] { new SelectItem(Utility.getProject(),Utility.getProject()+" entry"),
//                                 new SelectItem("GENE","genes") };
        return new SelectItem [] { new SelectItem("genes","Expression Summaries"),
        		new SelectItem("entry",Utility.getProject()+" entries")
        /*,
                new SelectItem("structures", "Structures that Express Gene")*/
        };
	}
	
	public SelectItem[] getGeneFunctionSearchResultOptionItems() {
      return new SelectItem [] { new SelectItem("entry",Utility.getProject()+" entries")/*,
              new SelectItem("genes","Expression Summaries")*/
      };
	}

	public String getGeneFunctionStage() {
		return geneFunctionStage;
	}

	public void setGeneFunctionStage(String geneFunctionStage) {
		this.geneFunctionStage = geneFunctionStage;
	}

	public String getUploadResultOption() {
		return uploadResultOption;
	}

	public void setUploadResultOption(String uploadResultOption) {
		this.uploadResultOption = uploadResultOption;
	}
	
    
    // xingjun - 01/06/2011 - need this property to make euregenedb front page working
    public String getUploadedGenes() {
    	return this.uploadedGenes;
    }
    
    public void setUploadGenes(String uploadedGenes) {
    	this.uploadedGenes = uploadedGenes;
    }
}
