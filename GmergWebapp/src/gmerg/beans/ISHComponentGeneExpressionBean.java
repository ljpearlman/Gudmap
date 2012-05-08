package gmerg.beans;

import gmerg.assemblers.QueryAssembler;

import gmerg.utils.FacesUtil;
import gmerg.utils.Visit_old;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.faces.event.ActionEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

public class ISHComponentGeneExpressionBean  {
    
    private ArrayDataModel model = null;
    String [][] subs;
    private QueryAssembler queryAssembler;

    private String pNum;
    private boolean resultExists;
    private Visit_old storedVars;
    private Hashtable params;
    
    public ISHComponentGeneExpressionBean () {
        super();
        queryAssembler = new QueryAssembler();
        resultExists = true;
    }
    
    public boolean isResultExists() {
        return resultExists;
    }
    
    /**
     * set the value of the current page
     * @param num - the current page number
     */
    public void setPnum(String num) {

        int pgNum;
        int numPages = getNumPages();

        try {
            pgNum = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            pgNum = 1;
        }
        if(pgNum < 1){
            pgNum = 1;
        }
        else if (pgNum > numPages) {
            pgNum = numPages;
        }
        pNum = String.valueOf(pgNum);

    }
    
    /**
     * returns the value of the current page
     * @return pageNum - the current page number
     */
    public String getPnum() {
        return pNum;
    }

    /**
     * sets the total number of pages available for browsing
     * @param value - the total numebr of pages
     */
    public void setNumPages(int value) {
        numPages = value;
    }
    
    public String getQueryString() {

        return "";
    }


    /**
     * DAO is accessed to determine total number of entries (numEntries) in a result. NumPages
     * can be calculated from this and the number of results to be displayed on a single 
     * page (getResPerPage()). The value of numEntries changes depending on the specific query.
     * @return numPages - the number of pages in a result
     */
    public int getNumPages() {
        
        String numEntries = "";
        int numRows;
        int rpp;
        
        storedVars = (Visit_old)FacesUtil.getSessionValue("compGeneExpStoredVars");
        
        if (storedVars == null) {
            resultExists = false;
            numPages = 0;
            return numPages;
        }
        params = storedVars.getParameters();
        if(params == null || storedVars.getQueryType()== null){
            resultExists = false;
            numPages = 0;
            return numPages;
        }
        
        numPages = storedVars.getNumPagesForQuery();
        
        if(numPages == -1) {
            String queryType = storedVars.getQueryType();
            
            if(queryType.equals("geneQueryWithAnnotation")){
            
                String inputType = (String) params.get("inputType");
                String criteria = (String) params.get("criteria");
                String geneSymbol = (String) params.get("geneSymbol");
                String stage = (String) params.get("stage");
                String ignoreExpression = (String) params.get("ignoreExpression");
                
                numEntries = queryAssembler.getTotalISHComponentsExpressingGene(ignoreExpression, inputType, geneSymbol, stage, criteria);
            }
            else {
                numEntries = "0";
            }
            
            try {
                numRows = Integer.parseInt(numEntries);
            } catch (NumberFormatException e) {
                numRows = -1;
            }

            //no. of pages should be 0 if resPerPage is 'all'
            if (getResPerPage().equals("ALL")) {
                setNumPages(1);
            }
            //else perform calculations to determine no of pages
            else {
                try {
                    rpp = Integer.parseInt(getResPerPage());
                } catch (NumberFormatException e) {
                    rpp = -1;

                }
                
                int pages; //numerical rep. of toal no. pages
                pages = numRows / rpp;
                int remainder = numRows % rpp;

                if (remainder > 0) {
                    pages++;
                }

                numPages = pages;

            }

        }
        if(numPages == 0){
            resultExists = false;
        }
        
        storedVars.setNumPagesForQuery(numPages);
        FacesUtil.setSessionValue("compGeneExpStoredVars", storedVars);
        return numPages;
    }
    
    public DataModel getComponents() {
    
        String sortDir = (String)FacesUtil.getSessionValue("sortDir");
        String sortParam = (String)FacesUtil.getSessionValue("sortBy");
        
        String [] order = new String [2];
        
        if(sortDir == null || sortParam == null){
            order = null;
        }
        else{
            order [0] = sortParam;
            order[1] = sortDir;
        }
        
        String offSet = ""; //determines which subset of entries are displayed on page
        
        
         if (getResPerPage().compareToIgnoreCase("ALL") != 0) {
         
            int currentPage;
            int rpp;
            try {
                currentPage = Integer.parseInt(pNum);
                if(currentPage > this.getNumPages()){
                    currentPage = this.getNumPages();
                }
                rpp = Integer.parseInt(getResPerPage());
                
            }
            catch (NumberFormatException e){
         
                currentPage = 1;
                rpp = 20;
            }
            int startNo = (currentPage - 1) * rpp + 1;
            offSet = Integer.toString(startNo);
        }
        
        storedVars = (Visit_old)FacesUtil.getSessionValue("compGeneExpStoredVars");
        
        if (storedVars == null) {
            resultExists = false;
            return null;    
        }
        params = storedVars.getParameters();
        if(params == null || storedVars.getQueryType()== null){
            resultExists = false;
            return null;
        }
        
        String queryType = storedVars.getQueryType();
        
        subs = null;
        
        if(queryType.equals("geneQueryWithAnnotation")){
            String inputType = (String) params.get("inputType");
            String criteria = (String) params.get("criteria");
            String geneSymbol = (String) params.get("geneSymbol");
            String stage = (String) params.get("stage");
            String output = (String) params.get("output");
            String ignoreExpression = (String) params.get("ignoreExpression");
            
            ArrayList result = (ArrayList)queryAssembler.getDataByGene(output, ignoreExpression, inputType, geneSymbol, stage, criteria, order, offSet, getResPerPage());
            if(null != result && result.size() > 0) {
                subs = new String[result.size()][];
                for(int i = 0; i < result.size(); i++) {
                    subs[i] = (String[])result.get(i);
                }
            }
        }
        
        model = new ArrayDataModel(subs); 
        return model;
        
    }
    
    
    
    
//*******************************************************************************************************
//* teken from ISHSubmissionsBean ******
    
    protected String sortDir; //sort direction, 'asc' or 'desc' for a table of submissions
    protected String sortParam; //value of the column to be sorted on in a table of submissions
    protected String resPerPage; // no of results to display per page

    protected int numPages;
    protected String pageNum; //current page display
    protected String pageId;

    public void setPageId(String id) {
        pageId = id;
    }

    public String getPageId() {
        return pageId;
    }

    public void setVisit(Visit_old visit) {
        storedVars = visit;
    }

    public Visit_old getVisit() {
        return storedVars;
    }

    /* set the value of the sort direction (asc or desc)
       @param dir - the diection in which to sort */

    public void setSortDir(String dir) {
        sortDir = dir;
    }

    /* returns the value of the sort direction (asc or desc)
       @return sortDir - the diection in which to sort */

    public String getSortDir() {
        return sortDir;
    }

    /* set the value of the column to sort on
       @param - sort the column on which to sort by*/

    public void setSortParam(String sort) {
        sortParam = sort;
    }

    /* returns the value of the column to sort on
       @return sortParam - the column on which to sort by*/

    public String getSortParam() {
        return sortParam;
    }

    /* set the value of the results to display per page
       @param rpp - the number of results per page */

    public void setResPerPage(String rpp) {
        if (rpp == null) {
            resPerPage = "20";
        } else {
            resPerPage = rpp;
        }
        FacesUtil.setSessionValue("resPerPage", rpp);
    }

    /* returns the value of the results to display per page
       @return resPerPage - the number of results per page */

    public String getResPerPage() {
        return resPerPage;
    }

    /* set the value of the current page
       @param num - the current page number */

    public void setPageNum(String num) {

        int pNum;
        try {
            pNum = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            pNum = 1;
        }
        if(pNum < 1){
            pNum = 1;
        }
        pageNum = String.valueOf(pNum);

    }

    /* returns the value of the current page
       @return pageNum - the current page number */

    public String getPageNum() {
        return pageNum;
    }

    /* returns the value to determine whether all results are displayed on a single page.
     * If this is true some items components should not be rendered
     * @return true - specified items are to be rendered on page
     * @return false - specified items are not to be rendered on page
     */

    public boolean isRppAll() {
        if (getResPerPage().equals("ALL")) {
            return false;
        } else {
            return true;
        }
    }
    
  
    public void sortByAny(ActionEvent event) {
        String sort =
            (String)event.getComponent().getAttributes().get("sortBy");
        String sortDir = (String)FacesUtil.getSessionValue("sortDir");
        String oldSort = (String)FacesUtil.getSessionValue("sortBy");

        if (null == sort)
            sort = "byGene";
            
        FacesUtil.setSessionValue("sortBy", sort);
        if (null == sortDir || (null != oldSort && !oldSort.equals(sort))) {
        	FacesUtil.setSessionValue("sortDir", "ASC");
        } else if (sortDir.equals("DESC")) {
        	FacesUtil.setSessionValue("sortDir", "ASC");
        } else if (sortDir.equals("ASC")) {
        	FacesUtil.setSessionValue("sortDir", "DESC");
        }

    }


    /*page navigation methods */

    public String goToCollection() {
        return "success";
    }

    private String goToOperationResults() {
        return "success";
    }

    public String submissionPage() {
        return "success";
    }

    
}