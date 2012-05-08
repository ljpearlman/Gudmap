package gmerg.beans;

import gmerg.assemblers.ArraySubmissionAssembler;
import gmerg.entities.submission.array.ArraySubmission;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.utils.FacesUtil;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;


public class MicroarraySingleSubmissionBean {

    private String submissionId;  //user-defined submission id
    private ArraySubmissionAssembler assembler; //used to interrogate db and return correct array data
    private ArraySubmission submission;  //contains all data concerned with a particular microarray submission
    private GeneListBrowseSubmission [] glItem;  //array of objects containing gene list data
    private static final int RES_PER_PAGE = 500;
    
    private ArrayDataModel geneList = null;

    private String geneListPageNum;
    private String numGeneListPages;
    private String geneSymbol; //assigned value of to user input when doing gene search

    public MicroarraySingleSubmissionBean() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> requestParams =
            context.getExternalContext().getRequestParameterMap();

        submissionId = (String)requestParams.get("id");
        submission = new ArraySubmission();
        assembler = new ArraySubmissionAssembler();
        //ArraySubmission sub = new ArraySubmission();
        
        if(submissionId != null && !submissionId.equals("")){
        	FacesUtil.setSessionValue("arraySubId", submissionId);
        }
        else {
            String id = (String) FacesUtil.getSessionValue("submissionID");
            if (id != null && !id.equals("")){
            	FacesUtil.setSessionValue("arraySubId", (String) FacesUtil.getSessionValue("submissionID"));
            }
        }
        
        String subId = (String) FacesUtil.getSessionValue("arraySubId");
        
        if(subId != null && !subId.equals("")) {
            submission = assembler.getData(subId);
            
        }
        else {
            //error message
        }
        
        
        /*
        if (submissionId != null && !submissionId.equals("")) {
            setFacesSessionValue("arraySubId", submissionId);
            
            if(assembler.getData(submissionId) != null){
                sub = assembler.getData(submissionId);
            }
            else {
                //faces error message
            }
            
        } else {
        	if(assembler.getData((String)getFacesSessionValue("submissionID")) != null){
        		System.out.println("FROM MIC SINGLE:"+(String)getFacesSessionValue("submissionID"));
        		sub = assembler.getData((String)getFacesSessionValue("submissionID"));
        		setFacesSessionValue("arraySubId", (String)getFacesSessionValue("submissionID"));
        	} else {
        	//error message shuld go here.
            this.setSubmission(null);
        	}
        }
        if(sub == null) {
            //create faces error message
        }
        else {
            this.setSubmission(sub);
        }
        */
    }

    public void setSubmission(ArraySubmission sub) {
        submission = sub;
    }

    public ArraySubmission getSubmission() {
        return submission;
    }
    
    public String getTransgenicTitle() {
    	if (submission==null || submission.getTransgenics()==null)
    		return "";
    	String type = submission.getTransgenics()[0].getMutantType();
    	if ("transgenic insertion".equalsIgnoreCase(type))
    		return "Transgenic Reporter Allele"+ ((submission.isMultipleTransgenics())?"s":"");
    	if ("mutant allele".equalsIgnoreCase(type))
    		return "Mutant Allele"+ ((submission.isMultipleTransgenics())?"s":"");
    	return "";
    }
    
    public void setNumGeneListPages(String value){
        numGeneListPages = value;
    }
    
    public String getNumGeneListPages() {
        
        String subId = (String)FacesUtil.getSessionValue("arraySubId");
        
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
    		int rowNumOfGeneSymbol = assembler.getRowNumberOfFirstOccurrenceOfGene((String)FacesUtil.getSessionValue("arraySubId"), geneSymbol);
    		
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

    public DataModel getGeneList() {
        
    	
        String subId = (String)FacesUtil.getSessionValue("arraySubId");
        
        String sortDir = (String)FacesUtil.getSessionValue("glSortDir");
        String sortParam = (String)FacesUtil.getSessionValue("glSortBy");
        String [] order = new String [2];
        
        if(sortDir == null || sortParam == null){
            order = null;
        }
        else{
            order [0] = sortParam;
            order[1] = sortDir;
        }
        
        String offSet = ""; //determines which subset of entries are displayed on page
        
        int pNum;  //integer representation of current page
        
         try {
             pNum = Integer.parseInt(getGeneListPageNum());
         }
         catch (NumberFormatException e){
             pNum = 1;
         }
        
         int startNo = (pNum - 1) * RES_PER_PAGE + 1;
         offSet = Integer.toString(startNo);
         
        glItem = assembler.getGeneList(subId,order,offSet);
         
        geneList = new ArrayDataModel(glItem);
        return geneList;

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
}
