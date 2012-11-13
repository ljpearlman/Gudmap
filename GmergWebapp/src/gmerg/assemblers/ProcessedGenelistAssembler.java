/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.array.ProcessedGeneListHeader;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.InMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.FacesContext;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class ProcessedGenelistAssembler extends InMemoryTableAssembler{
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

	private String  component;
	private String lab;
    
    public ProcessedGenelistAssembler (HashMap params) {
    	super(params);
	if (debug)
	    System.out.println("ProcessedGenelistAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
    	component = getParam("component");
		lab = getParam("lab");
    }

	public void applyDataRetrivalParams(Object[] params) {
		
	}
	
    
    public DataItem[][] retrieveData() {
	if (null != cache) {
		if (debug)
		    System.out.println("ProcessedGenelistAssembler.retriveData data not changed");
		
		return cache.getData();
}

    	// populate list of genelists 
    	ArrayList listOfGenelists = null;
    	if (component!=null)
    		listOfGenelists = retrieveGenelistByComponentName(component);
    	else{
    		if (lab!=null)
    			listOfGenelists = retrieveGenelistByLabName(lab);
    	}
    	DataItem[][] ret = getTableDataFormatFromListOfgenelists(listOfGenelists);

	if (null == cache)
	    cache = new RetrieveDataCache();
	cache.setData(ret);

	return ret;
    }

	/********************************************************************************
	 * Create Headers for list of processed genelists
	 *
	 */
	public HeaderItem[] createHeader() {
		String headerTitles[] = {"Gene List File", "Source",  "Components",   "Statistics",     "Data Transformation",
	                             "Tests", "Serial No", "Up Regulated", "Down Regulated", "GeneListData", "Summary" };
	      
	    boolean headerSortable[] = {true, true, true, true, true, false, true, true, true, false, false };

	    int colNum = headerTitles.length; 
	    HeaderItem[] tableHeader = new HeaderItem[colNum];
	    for(int i=0; i<colNum; i++)
	    	tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
	    return tableHeader;
	}

	/**
	 * 
	 * @param componentName
	 * @return
	 */
	public ArrayList retrieveGenelistByComponentName(String componentName) {

		if (componentName == null || componentName.equals("")) {
			return null;
		}
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get data
		// get component list
		ArrayList componentNames = arrayDAO.getComponentListByName(componentName);
		if (componentNames == null || componentNames.size()== 0) {
			return null;
		}
		
		// get genelists
		ArrayList genelistHeaders = arrayDAO.getGenelistHeadersByComponentNames(componentNames);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the composite value object---  */
		return genelistHeaders;
	}
	
	/**
	 * 
	 * @param labName
	 * @return
	 */
	public ArrayList retrieveGenelistByLabName(String labId) {

		if (labId== null || labId.equals("")) {
			return null;
		}
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDAO arrayDAO = MySQLDAOFactory.getArrayDAO(conn);
		
		// get genelists
		ArrayList genelistHeaders = arrayDAO.getGenelistHeadersByLabId(labId);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		arrayDAO = null;
		
		/** ---return the composite value object---  */
		return genelistHeaders;
	}
	

    /********************************************************************************
	 * populates GenericTable for list of processed genelists
	 *
	 */
	private DataItem[][] getTableDataFormatFromListOfgenelists(ArrayList listOfGeneLists)
	{
		if(listOfGeneLists == null) 
	    	return null;

		int colNum = 11;
	    int rowNum = listOfGeneLists.size();
	    
    	String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	    DataItem[][] tableData = new DataItem[rowNum][colNum];
	    for(int i=0; i<rowNum; i++){
	    	ProcessedGeneListHeader geneList = (ProcessedGeneListHeader)listOfGeneLists.get(i);  
	    	tableData[i][0] = new DataItem(geneList.getFileName(), geneList.getFileName(), gmerg.utils.Utility.domainUrl+"Gudmap/arrayData/potter/october_2006/potter_171006/data/"+geneList.getFileName(), 10);
	    	tableData[i][1] = new DataItem(geneList.getSurname(), geneList.getSurname(), "../pages/lab_detail.jsf?personId="+geneList.getLabName(), 4);
	    	tableData[i][2] = new DataItem(geneList.getComponents());
	    	tableData[i][3] = new DataItem(geneList.getStatistics());
	    	tableData[i][4] = new DataItem(geneList.getDataTransformation());
	    	tableData[i][5] = new DataItem(new Integer(geneList.getTests()));
	    	tableData[i][6] = new DataItem(new Integer(geneList.getSerialNo()));
	    	tableData[i][7] = new DataItem(new Integer(geneList.getUpRegulated()));
	    	tableData[i][8] = new DataItem(new Integer(geneList.getDownRegulated()));
	    	tableData[i][9] = new DataItem("View array gene list with link to ISH data", "data",  
	    									contextPath+"/pages/genelist_data.jsf?genelistId="+String.valueOf(geneList.getId())+"&genelistName="+geneList.getFileName(), 2);
	    	tableData[i][10] = new DataItem("summary", "summary", "../pages/organ_description.jsf?component="+component, 4);
	    }        

    	return tableData;
	}
	
}
