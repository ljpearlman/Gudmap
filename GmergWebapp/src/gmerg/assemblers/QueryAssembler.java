/**
 * 
 */
package gmerg.assemblers;

import gmerg.beans.UserBean;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.SubmissionData;
import gmerg.entities.submission.array.ArraySubmission;
import gmerg.entities.submission.ish.ISHBrowseSubmission;
import gmerg.entities.submission.ish.ISHSubmission;
import gmerg.entities.submission.ish.ISHBrowseSubmissionData;
//import gmerg.utils.Utility;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.RetrieveDataCache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author xingjun
 *
 */
public class QueryAssembler extends OffMemoryTableAssembler {

	String projectString = ResourceBundle.getBundle("configuration").getString("project").trim();
	String queryType;
    String [] components;
    String component;    
    String startStage;
    String endStage;
    String [] expressionTypes;
    String criteria;
    String output;
    String ignoreExpression;
    String inputType;
    String geneSymbol;
    String stage;
    String componentID;
       
    protected boolean debug = false;
    protected RetrieveDataCache cache = null;

	public QueryAssembler () {
	if (debug)
	    System.out.println("QueryAssembler.constructor");
		
	}
	
    public QueryAssembler (HashMap params) {
        super(params);
	if (debug)
	    System.out.println("QueryAssembler.constructor");
	}
	public void setParams() {
		super.setParams();
		queryType = getParam("queryType");
		components = getParams("components");
		if(components == null) {
			components = new String [1];
			components[0] = "";
		}
		component = getParam("component");
		startStage = getParam("startStage");
		endStage = getParam("endStage");
		String expressionTypesInString = getParam("expressionTypes");
		if(expressionTypesInString != null && !expressionTypesInString.equals(""))
		    expressionTypes = expressionTypesInString.split("_");
	        criteria = getParam("criteria");
	        output = getParam("output");
	        ignoreExpression = getParam("ignoreExpression");
	        inputType = getParam("inputType");
	        geneSymbol = getParam("geneSymbol");
	        stage = getParam("stage");
	        componentID = getParam("componentID");
	}

    /**
     * need consider submission browse result as well as component list result scenario
     */
    public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
	    if (null != cache &&
		cache.isSameQuery(column, ascending, offset, num)) {
		if (debug)
		    System.out.println("QueryAssembler.retriveData data not changed");
		
		return cache.getData();
	    }	

        ArrayList  subs = null;
//	System.out.println("qType: "+queryType);
	if(queryType.equals("geneQueryISH")) {
			subs = getDataByGene(output, ignoreExpression, inputType, geneSymbol, stage,
					criteria, column, ascending, offset, num);
			if (output.equals("anatomy")) { // the data format of component list is different with submission list
				return this.getTableDataFormatFromComponentList(subs);
			}
		}
            
		// return value
		DataItem[][] ret = ISHBrowseAssembler.getTableDataFormatFromIshList(subs);

		if (null == cache)
		    cache = new RetrieveDataCache();
		cache.setData(ret);
		cache.setColumn(column);
		cache.setAscending(ascending);
		cache.setOffset(offset);
		cache.setNum(num);	

		return ret;
	}

    /**
     *
     * @return
     */
    public int retrieveNumberOfRows() {
        String numEntries = "0";
	if(queryType.equals("geneQueryISH"))
	            numEntries = getTotalISHSubmissionsForGeneQuery(ignoreExpression, 
	            		inputType, geneSymbol, stage, criteria);
			
            
		return Integer.parseInt(numEntries);
    }
	
	
    public HeaderItem [] createHeader() {
        return ISHBrowseAssembler.createHeaderForISHBrowseTable();
    }

	private DataItem[][] getTableDataFormatFromComponentList(ArrayList componentList) {
		if (componentList==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])componentList.get(0)).length;
		int rowNum = componentList.size();
		
//		System.out.println("ISH Assembler retrieved rows="+rowNum);		
		    
		DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++)
			convertComponentListRowToDataItemFormat(tableData[i], 
					(String[])componentList.get(i));
			
		return tableData;
	 }

	private void convertComponentListRowToDataItemFormat (DataItem[] formatedRow, String[] row) {
		formatedRow[ 0] = new DataItem(row[0]);	  														// Component Description
		formatedRow[ 1] = new DataItem(row[1]);															// component ID
		formatedRow[ 2] = new DataItem(row[2]);															// Theiler Stage
		formatedRow[ 3] = new DataItem(row[3], "", "gene.html?gene="+row[3], 10);						// Gene Symbole
		formatedRow[ 4] = new DataItem(row[4],
				"Click to see submissions", 
				"ish_submissions.html?component="+row[0]+"&stage="+row[2]+"&geneSymbol="+row[3], 10);	// Number of entries
	}

	/**
	 * @author xingjun
	 * comply with new format of browse page
	 * 
	 * @param type
	 * @param ignoreExpression
	 * @param inputType
	 * @param inputString
	 * @param stage
	 * @param criteria
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public ArrayList getDataByGene(String type, String ignoreExpression, String inputType, 
			String inputString, String stage, String criteria, 
			int columnIndex, boolean ascending, int offset, int num) {
		
		if (inputString == null || inputString.equals("")) {
			return null;
		}

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO;
		try{
			ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			/** ---get data--- */
			if (type.equals("image")) { // posibly need further implementation
				// release the db resources
				DBHelper.closeJDBCConnection(conn);
				ishDAO = null;
				
				return null;
				
			} else if (type.equals("anatomy")) {
				
				ArrayList componentCountInfo = ishDAO.getComponentCountInfoByGeneInfo(inputType, 
						inputString,
						stage, criteria, columnIndex, ascending, offset, num);
				
				// release the db resources
				DBHelper.closeJDBCConnection(conn);
				ishDAO = null;
				
				/** ---return the object--- */
				return componentCountInfo;
				
			} else if (type.equals("gene")){
	
				// gene query
			    ArrayList submissions = ishDAO.getSubmissionByGeneInfo(inputType, 
			    		ignoreExpression, inputString,
			    		stage, criteria, columnIndex, ascending, offset, num);
	
				// release the db resources
				DBHelper.closeJDBCConnection(conn);
				ishDAO = null;
				
				/** ---return the object--- */
				return submissions;
			}
			return null;
		}
		catch(Exception e){
			System.out.println("QuickAssembler::getDataByGene failed !!!");
			return null;
		}
		finally{
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
		}
	} // end of getDataByGene
        
    public String getTotalISHComponentsExpressingGene(String ignoreExpression, 
    		String inputType, String inputString,
    		String stage, String criteria) {
    
		if (inputString == null || inputString.equals("")) {
			return null;
		}

        String totalComponents = new String("0");
        
        /** ---get data from dao--- */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        ISHDAO ishDAO;
        try{
	        ishDAO = MySQLDAOFactory.getISHDAO(conn);
	        
	        // get the value
	        totalComponents +=
	        	ishDAO.getTotalISHComponentsExpressingGeneQuery(inputType, 
	        			ignoreExpression, inputString, stage, criteria);

	        return totalComponents;
        }
		catch(Exception e){
			System.out.println("QuickAssembler::getTotalISHComponentsExpressingGene failed !!!");
	        return "0";
		}
        finally{
	        // release db resources
	        DBHelper.closeJDBCConnection(conn);
	        ishDAO = null;
        }
    }
        
    public String getTotalISHSubmissionsForGeneQuery(String ignoreExpression, 
    		String inputType, String inputString, String stage, String criteria) {
            
		if (inputString == null || inputString.equals("")) {
			return "0";
		}
		
		String totalSubmissions = new String("0");
		
		/** ---get data from dao--- */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO;
		try{
			ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			// get the value
			totalSubmissions +=
				ishDAO.getTotalNumberOfISHSubmissionsForGeneQuery(inputType,
						ignoreExpression, inputString, stage, criteria);

			/** return the value  */
			return totalSubmissions;
		}
		catch(Exception e){
			System.out.println("QuickAssembler::getTotalISHSubmissionsForGeneQuery failed !!!");
			return "0";
		}
		finally{
			// release db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
		}
		
	}

    public String getTotalISHSubmissionsForGeneQuery(String inputType, 
    		String ignoreExpression, String inputString,
    		String component, String stage, String criteria) {
                
		if (inputString == null || inputString.equals("")) {
			return "0";
		}

        String totalSubmissions = new String("0");
                
        /** ---get data from dao--- */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        ISHDAO ishDAO;
        try{
	        ishDAO = MySQLDAOFactory.getISHDAO(conn);
	                
	        // get the value
	        totalSubmissions += ishDAO.getTotalNumberOfISHSubmissionsForGeneQuery(inputType, 
	        		ignoreExpression, inputString, component, stage, criteria);

	        /** return the value  */
	        return totalSubmissions;
        }
		catch(Exception e){
			System.out.println("QuickAssembler::getTotalISHSubmissionsForGeneQuery failed !!!");
	        return "0";
		}
        finally{
	        // release db resources
	        DBHelper.closeJDBCConnection(conn);
	        ishDAO = null;
        }
    }
	
    public String getTotalISHSubmissionsForComponentQuery(String component) {
        String totalSubmissions = new String("");
                
        /** ---get data from dao--- */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        ISHDAO ishDAO;
        try{
	        ishDAO = MySQLDAOFactory.getISHDAO(conn);
	        
	        // get the value
	        totalSubmissions += 
	        	ishDAO.getTotalNumberOfISHSubmissionsForComponentQuery(component);

	        /** return the value  */
	        return totalSubmissions;
        }
		catch(Exception e){
			System.out.println("QuickAssembler::getTotalISHSubmissionsForComponentQuery failed !!!");
	        return "0";
		}
        finally{
	        // release db resources
	        DBHelper.closeJDBCConnection(conn);
	        ishDAO = null;
        }
    }
    
    /**
         * @param components - timed organism component id (e.g.EMAP no's in mouse anatomy) 
         * @return
         */
         
    public String getTotalSubmissionsForComponents(String [] components){
        String totalSubmissions = new String("");
        
        /** ---get data from dao--- */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        ISHDAO ishDAO;
        try{
	        ishDAO = MySQLDAOFactory.getISHDAO(conn);
	        
	        // get the value
	        totalSubmissions += ishDAO.getTotalNumberOfSubmissionsForComponentsQuery(components);
	        /** return the value  */
	        return totalSubmissions;
        }
		catch(Exception e){
			System.out.println("QuickAssembler::getTotalSubmissionsForComponents failed !!!");
	        return "0";
		}
        finally{
	        // release db resources
	        DBHelper.closeJDBCConnection(conn);
	        ishDAO = null;
        }
    }

    /**
     * @param components
     * @return
     */
    public String getTotalSubmissionsForComponentsQuery(String [] components, 
    		String startStg,String endStg, String [] expTypes, String criteria) {
        
            String totalSubmissions = new String("");
            /** ---get data from dao--- */
            // create a dao
            Connection conn = DBHelper.getDBConnection();
            ISHDAO ishDAO;
            try{
	            ishDAO = MySQLDAOFactory.getISHDAO(conn);
	            
	            // get the value
	            totalSubmissions +=
	            	ishDAO.getTotalNumberOfSubmissionsForComponentsQuery(components, 
	            			startStg, endStg, expTypes, criteria);

	            /** return the value  */
	            return totalSubmissions;
            }		
            catch(Exception e){
    			System.out.println("QuickAssembler::getTotalSubmissionsForComponentsQuery failed !!!");
                return "0";
    		}
            finally{
	            // release db resources
	            DBHelper.closeJDBCConnection(conn);
	            ishDAO = null;
            }
        }

	/**
	 * @param accessionId
	 * @return
	 */
	public SubmissionData getSubmissionQueryData(String accessionId, UserBean userBean) {
		
        // process the accession id
		// if it's a null or empty value, return null
		// if it contains ':' character, then go to find the submisison based on accession id
		// else if it's a pure digit string, then to to find the submisison based on submission oid
		// else it's a invalid id and return null.
		if (accessionId == null || accessionId.equals("")) {
			return null;
		}

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO;
		SubmissionData submissionData = null;
		try{
			ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			Submission submission = null;
			ISHSubmission ishSubmission = null;
			ArraySubmission arraySubmission = null;
			String assayType = null;
			if (accessionId.indexOf(":") != -1) {
				//System.out.println("accid: " + accessionId);
				assayType = ishDAO.findAssayTypeBySubmissionId(accessionId);
				submission = ishDAO.findSubmissionById(accessionId);
	//			System.out.println("assay type: " + assayType);
				
			} else {
				boolean isAnInteger = true;
				try {
					Integer.parseInt(accessionId);
				} catch(NumberFormatException nfe) {
					isAnInteger = false;
				}
				if (isAnInteger) {
	//				assayType = ishDAO.findAssayTypeBySubmissionId("GUDMAP:" + accessionId);
						assayType = ishDAO.findAssayTypeBySubmissionId("GUDMAP:" + accessionId);
	//				System.out.println("assay type: " + assayType);
	//				submission = ishDAO.findSubmissionById("GUDMAP:" + accessionId);
						submission = ishDAO.findSubmissionById("GUDMAP:" + accessionId);
					
				} else {// id is not valid
					return null;
				}
			}
			
			if (submission == null) {
				return null;
			}
			
	        /********* add by Xingjun Pi 18/04/2007 begin ***********/
			// if it is a non-public submission, return null value for the submission query
			if (!submission.isReleased()) {
				return null;
			}
	        /********* add by Xingjun Pi 18/04/2007 end ***********/
	
			// get submission data
			// if assay type is 'ISH', get ish submisison; else if assay type is 'Microarray', 
			// get array submission
	//		System.out.println("assay type: " + assayType);
			if (assayType.indexOf("ISH") >=0) {
				ISHSubmissionAssembler isa = new ISHSubmissionAssembler();
	//			System.out.println("acc: " + submission.getAccID());
				ishSubmission = isa.getData(submission.getAccID(), false, false, userBean, false);
				
				// assemble submission result
				submissionData = new SubmissionData();
				submissionData.setSubmission(ishSubmission);
				submissionData.setSubmissionType(0); // ish submission
				
			} else if (assayType.equals("Microarray")) {
	//			System.out.println("mic");
				
				ArraySubmissionAssembler asa = new ArraySubmissionAssembler();
				arraySubmission = asa.getData(submission.getAccID());
	
				// assemble submission result
				submissionData = new SubmissionData();
				submissionData.setSubmission(arraySubmission);
				submissionData.setSubmissionType(1); // array submission
				
			} else if(assayType.equals("IHC")) { // added by xingjun -- 06/08/2007
				ISHSubmissionAssembler isa = new ISHSubmissionAssembler();
	//			System.out.println("acc: " + submission.getAccID());
				ishSubmission = isa.getData(submission.getAccID(), false, false, userBean, false);
				
				// assemble submission result
				submissionData = new SubmissionData();
				submissionData.setSubmission(ishSubmission);
				submissionData.setSubmissionType(0); // ish submission
			}
			/** ---return the composite value object---  */
			return submissionData;
		}
        catch(Exception e){
			System.out.println("QuickAssembler::getSubmissionQueryData failed !!!");
			return null;
		}
		finally{
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
		}
	}
}