package gmerg.beans;

import gmerg.assemblers.NGDSubmissionAssembler;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.entities.submission.nextgen.NGDSubmission;
import gmerg.utils.FacesUtil;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;


public class NGDSingleSubmissionBean {
    private boolean debug = false;

    private String submissionId;  //user-defined submission id
    private NGDSubmissionAssembler assembler; //used to interrogate db and return correct array data
    private NGDSubmission submission;  //contains all data concerned with a particular next gen submission
    private GeneListBrowseSubmission [] glItem;  //array of objects containing gene list data
    private static final int RES_PER_PAGE = 500;
    
    private ArrayDataModel geneList = null;

    private String geneListPageNum;
    private String numGeneListPages;
    private String geneSymbol; //assigned value of to user input when doing gene search
    private boolean renderPage;

    public NGDSingleSubmissionBean() {
        	if (debug)
	    System.out.println("NGDSingleSubmissionBean.constructor");

        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> requestParams =
            context.getExternalContext().getRequestParameterMap();

        submissionId = (String)requestParams.get("id");
        submission = new NGDSubmission();
        assembler = new NGDSubmissionAssembler();
        
        if(submissionId != null && !submissionId.equals("")){
        	FacesUtil.setSessionValue("NGDSubId", submissionId);
        }
        else {
            String id = (String) FacesUtil.getSessionValue("submissionID");
            if (id != null && !id.equals("")){
            	FacesUtil.setSessionValue("NGDSubId", (String) FacesUtil.getSessionValue("submissionID"));
            }
        }
        
        String subId = (String) FacesUtil.getSessionValue("NGDSubId");
        
        if(subId != null && !subId.equals("")) {
            submission = assembler.getData(subId);
            
        }
        else {
            //error message
        }
    }
    
   public String getId() {
        return submissionId;
    }

    public void setId(String submissionId) {   
        this.submissionId = submissionId;
    }

    public void setSubmission(NGDSubmission sub) {
        submission = sub;
    }

    public NGDSubmission getSubmission() {
        return submission;
    }
       
    public void setNumGeneListPages(String value){
        numGeneListPages = value;
    }
    
    public String getNumGeneListPages() {
        
        String subId = (String)FacesUtil.getSessionValue("NGDSubId");
        
        String numEntries = assembler.getTotalGeneListItems(subId);
        
        int numRows; //numerical representation of total no of rows in set
        try {
            numRows = Integer.parseInt(numEntries);
        } catch (NumberFormatException e) {
            numRows = 0;
        }
        
        int pages; //numerical rep. of toal no. pages
        pages = numRows / RES_PER_PAGE;

        int remainder = numRows % RES_PER_PAGE;

        if (remainder > 0) {
            pages++;
        }

        numGeneListPages = String.valueOf(pages);
        
        
        return numGeneListPages;
    }
    
    public void setGeneSymbol(String value){
    	geneSymbol = value;
    }
    
    public String getGeneSymbol(){
    	return geneSymbol;
    }
    
    
    public String getGeneListPageNum() {
        return geneListPageNum;
    }
    
    /**
     * method to find which page the first occurrence of a
     * user specified gene-symbol occurs at. This method will
     * set the gene list page number to match the value retrieved
     * if a valid value is returned 
     * @param e
     */
    public void findPageNumberForGeneSymbol(ActionEvent e) {
    	
    	FacesUtil.setSessionValue("glSortBy", null);
    	FacesUtil.setSessionValue("glSortDir", null);
    	
    	//make sure the user has entered a value for gene symbol
    	if(geneSymbol != null && !geneSymbol.trim().equals("")) {
    		int rowNumOfGeneSymbol = assembler.getRowNumberOfFirstOccurrenceOfGene((String)FacesUtil.getSessionValue("NGDSubId"), geneSymbol);
    		
    		if(rowNumOfGeneSymbol > 0){
    			int pnum = rowNumOfGeneSymbol / RES_PER_PAGE;
    		
    			pnum = pnum +1;
    			geneListPageNum = String.valueOf(pnum);
    		}
    	}
    }
    
    public void setGeneListPageNum(String value) {

        int pnum; //numerical rep of page no.
        int npges; //numerical rep of no. of pages

        try {
            pnum = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            pnum = 1;
        }

        String numPges = this.getNumGeneListPages();
        try {
            npges = Integer.parseInt(numPges);
        } catch (NumberFormatException e) {
            npges = 1;
        }

        if(pnum < 1) 
            pnum = 1;
        if(pnum > npges)
            pnum = npges;
        geneListPageNum = String.valueOf(pnum);
    }

    public void sortByAny(ActionEvent event){
        String sort =
            (String)event.getComponent().getAttributes().get("sortBy");
        String sortDir = (String)FacesUtil.getSessionValue("glSortDir");
        String oldSort = (String)FacesUtil.getSessionValue("glSortBy");

        if (null == sort)
            sort = "byGene";
            
        FacesUtil.setSessionValue("glSortBy", sort);
        if (null == sortDir || (null != oldSort && !oldSort.equals(sort))) {
        	FacesUtil.setSessionValue("glSortDir", "ASC");
        } else if (sortDir.equals("DESC")) {
        	FacesUtil.setSessionValue("glSortDir", "ASC");
        } else if (sortDir.equals("ASC")) {
        	FacesUtil.setSessionValue("glSortDir", "DESC");
        }
    }
    
    /** returns the part of the SQL determining how the result set should be
     *  ordered after receiving user defined value and using predetermined
     *  sort parameters
     */
    public void orderResults() {
        String colToSortBy = (String)FacesUtil.getRequestParamValue("sortBy");
        String origSortedCol = (String) FacesUtil.getSessionValue("glSortBy");  
        String origDirection = (String) FacesUtil.getSessionValue("glSortDir");

        String sortDir = ""; //sortDir will hold info what sort direction gets sorted in session
//        String orderBy = ""; //contains the literal to be appended to the db query
        String sort =
            ""; //contains the item the user wants to order results on

        //value to check whether the user has requested another page without changing the sort ordering
        boolean isNextPage = false;

        if (colToSortBy != null) {
            sort = colToSortBy;
        } else if ((colToSortBy == null || colToSortBy.equals("")) &&
                   origSortedCol != null) {
            sort = origSortedCol;
            isNextPage = true;
        } else {
            sort = "byGene";
        }


        if (isNextPage) {
            if (origDirection != null || origDirection.compareTo("") != 0) {
                if (origDirection.equals("ASC")) {
                    origDirection = "DESC";
                } else if (origDirection.equals("DESC")) {
                    origDirection = "ASC";
                }
            }
        }

        if (colToSortBy != null && origSortedCol != null &&
            colToSortBy.compareTo(origSortedCol) != 0) {
            origDirection = "ASC";
        }

        //if no sort direction has been specified, default is to sort ascending
        if (origDirection == null || origDirection.equals("")) {
            origDirection = "ASC";
            sortDir = "DESC";
        }
        //change current sortDir to be opposite of origSortDir
        else if (origDirection.equals("ASC")) {
            sortDir = "DESC";
        } else if (origDirection.equals("DESC")) {
            sortDir = "ASC";
        }
        
        FacesUtil.setSessionValue("glSortBy", sort);
        FacesUtil.setSessionValue("glSortDir", sortDir);

        
    }
    
    public boolean isRenderPage() {
        
        if(submission == null){
            renderPage = false;
        }
        else {
            renderPage = true;
        }
        return renderPage;
    }
}
