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
	
	public QueryAssembler () {
		super();
	}
	
    public QueryAssembler (HashMap params) {
        super(params);
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
        ArrayList  subs = null;
//	System.out.println("qType: "+queryType);
        if (queryType.equals("genesInAnatomyISH")) 
            subs = getDataByComponentIdList(components, startStage, endStage,
            		expressionTypes, criteria, column, ascending, offset, num);
        
		else if(queryType.equals("geneQueryISH")) {
			subs = getDataByGene(output, ignoreExpression, inputType, geneSymbol, stage,
					criteria, column, ascending, offset, num);
			if (output.equals("anatomy")) { // the data format of component list is different with submission list
				return this.getTableDataFormatFromComponentList(subs);
			}
		}
            
		else if(queryType.equals("componentGeneSubsISH"))
			subs = getDataByGene("gene", "false", "symbol", geneSymbol, component, stage,
					"equals", column, ascending, offset, num);
		
        //find submissions with annotation 'present' in timed anatomy component (e.g. EMAP:2220)
		else if (queryType.equals("componentSubsISH")) 
			subs = getDataByComponentId(componentID, null, column, ascending, offset, num);
		// return value
		return ISHBrowseAssembler.getTableDataFormatFromIshList(subs);
	}

    /**
     *
     * @return
     */
    public int retrieveNumberOfRows() {
        String numEntries = "0";
		if (queryType.equals("genesInAnatomyISH")) 
		    numEntries = getTotalSubmissionsForComponentsQuery(components, 
		    		startStage, endStage, expressionTypes, criteria);
			
		else if(queryType.equals("geneQueryISH"))
	            numEntries = getTotalISHSubmissionsForGeneQuery(ignoreExpression, 
	            		inputType, geneSymbol, stage, criteria);
			
		else if(queryType.equals("componentGeneSubsISH"))
	            numEntries = getTotalISHSubmissionsForGeneQuery(inputType, 
	            		"false", geneSymbol, component, stage, criteria);
				
		else if (queryType.equals("componentSubsISH")) 
	            numEntries = getTotalISHSubmissionsForComponentQuery(componentID);
            
//        System.out.println("*****num Entries: "+numEntries);
            
		return Integer.parseInt(numEntries);
    }
	
	/**
	 * 
	 */
	/*public int[] retrieveTotals() {

		//get data from dao
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);

		// get data from database
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_QENE_QUERY",
                "TOTAL_NUMBER_OF_GENE_SYMBOL_QENE_QUERY",
                "TOTAL_NUMBER_OF_THEILER_STAGE_QENE_QUERY",
                "TOTAL_NUMBER_OF_GIVEN_STAGE_QENE_QUERY",
                "TOTAL_NUMBER_OF_LAB_QENE_QUERY",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE_QENE_QUERY",
                "TOTAL_NUMBER_OF_ASSAY_TYPE_QENE_QUERY",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE_QENE_QUERY",
                "TOTAL_NUMBER_OF_IMAGE_QENE_QUERY"};

	    // get all column numbers, store the col totals in a 2D array
		String[][] columnNumbers = null;
		if(queryType.equals("geneQueryISH"))
			columnNumbers = ishDAO.getTotalNumberOfColumnsGeneQuery(allColTotalsQueries,
					inputType, ignoreExpression, geneSymbol, stage, criteria);
		
		else if (queryType.equals("componentGeneSubsISH"))
			columnNumbers = ishDAO.getTotalNumberOfColumnsGeneQuery(allColTotalsQueries, inputType, ignoreExpression,
					geneSymbol, component, stage, criteria);
		
		// convert to interger array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++) {
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
		}

		// return result
		return totalNumbers;
	}*/
	
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

	public Object getDataByGene(String type, String ignoreExpression, 
			String inputType, String inputString,
			String stage, String criteria, String[] order, String offset, String num) {
		
		if (inputString == null || inputString.equals("")) {
			return null;
		}

		ISHBrowseSubmission[] submissions = null;
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_QENE_QUERY",
                "TOTAL_NUMBER_OF_GENE_SYMBOL_QENE_QUERY",
                "TOTAL_NUMBER_OF_THEILER_STAGE_QENE_QUERY",
                "TOTAL_NUMBER_OF_GIVEN_STAGE_QENE_QUERY",
                "TOTAL_NUMBER_OF_LAB_QENE_QUERY",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE_QENE_QUERY",
                "TOTAL_NUMBER_OF_ASSAY_TYPE_QENE_QUERY",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE_QENE_QUERY",
                "TOTAL_NUMBER_OF_IMAGE_QENE_QUERY"};
		String[][] columnNumbers = null;

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		if (type.equals("image")) { // posibly need further implementation
			return null;
			
		} else if (type.equals("anatomy")) {
			
			ArrayList componentCountInfo =
				ishDAO.getComponentCountInfoByGeneInfo(inputType, 
						inputString, stage, criteria, order, offset, num);
			
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
			
			/** ---return the object--- */
			return componentCountInfo;
			
		} else { // gene
		    ISHBrowseSubmissionData ibsData = new ISHBrowseSubmissionData();

			// gene query
		    submissions =
		        ishDAO.getSubmissionByGeneInfo(inputType, ignoreExpression,
		                                       inputString, stage, criteria,
		        		                       order, offset, num);

		    // get all column numbers, store the col totals in a 2D array
			columnNumbers = 
				ishDAO.getTotalNumberOfColumnsGeneQuery(allColTotalsQueries, 
						                                inputType,
						                                ignoreExpression,
						                                inputString,
						                                stage, criteria);

			/** ---construct the composite value object---  */
			ibsData.setBroseSubmissionData(submissions);
			ibsData.setAllColumnTotalNumbers(columnNumbers);
			
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
			
			/** ---return the object--- */
			return ibsData;
		}
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
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		if (type.equals("image")) { // posibly need further implementation
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
	} // end of getDataByGene
    
	/**
	 * 
	 * @param type: image, anatomy, gene
	 * @param ignoreExpression: whether annotated
	 * @param inputType: symbol, name, synonym
	 * @param inputString
	 * @param componentId
	 * @param stage
	 * @param criteria: equals, beginin, wildcard
	 * @param order
	 * @param offset
	 * @param num
	 * @return
	 */
	public Object getDataByGene(String type, String ignoreExpression, 
			String inputType, String inputString, String componentId, String stage, 
			String criteria, String[] order, String offset, String num) {
		
		if (inputString == null || inputString.equals("")) {
			return null;
		}
		
		if (componentId == null || componentId.equals("")) {
			return null;
		}
		
		ISHBrowseSubmission[] submissions = null;
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_QENE_QUERY",
				"TOTAL_NUMBER_OF_GENE_SYMBOL_QENE_QUERY",
				"TOTAL_NUMBER_OF_THEILER_STAGE_QENE_QUERY",
				"TOTAL_NUMBER_OF_GIVEN_STAGE_QENE_QUERY",
				"TOTAL_NUMBER_OF_LAB_QENE_QUERY",
				"TOTAL_NUMBER_OF_SUBMISSION_DATE_QENE_QUERY",
				"TOTAL_NUMBER_OF_ASSAY_TYPE_QENE_QUERY",
				"TOTAL_NUMBER_OF_SPECIMEN_TYPE_QENE_QUERY",
				"TOTAL_NUMBER_OF_IMAGE_QENE_QUERY"};
		String[][] columnNumbers = null;
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		if (type.equals("image")) { // possibly need further implementation
			return null;
		} else if (type.equals("anatomy")) {
			ArrayList componentCountInfo =
				ishDAO.getComponentCountInfoByGeneInfo(inputType, inputString, 
						stage, criteria, order, offset, num);
			
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
			
			/** ---return the object--- */
			return componentCountInfo;
			
		} else { // gene
			ISHBrowseSubmissionData ibsData = new ISHBrowseSubmissionData();
			// gene query
			submissions = ishDAO.getSubmissionByGeneInfo(inputType, ignoreExpression,
					inputString, componentId, stage, criteria, order, offset, num);
			
			// get all column numbers, store the col totals in a 2D array
			columnNumbers = ishDAO.getTotalNumberOfColumnsGeneQuery(allColTotalsQueries, 
					inputType, ignoreExpression,
					inputString, componentId, stage, criteria);
			
			/** ---construct the composite value object---  */
			ibsData.setBroseSubmissionData(submissions);
			ibsData.setAllColumnTotalNumbers(columnNumbers);
			
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
			
			/** ---return the object--- */
			return ibsData;
		}
	}
        
	/**
	 * @author xingjun
	 * comply with new format of browse style page
	 * 
	 * @param type
	 * @param ignoreExpression
	 * @param inputType
	 * @param inputString
	 * @param componentId
	 * @param stage
	 * @param criteria
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public ArrayList getDataByGene(String type, String ignoreExpression, 
			String inputType, String inputString, String componentId, String stage, 
			String criteria, int columnIndex, boolean ascending, int offset, int num) {
		
		if (inputString == null || inputString.equals("")) {
			return null;
		}
		
		if (componentId == null || componentId.equals("")) {
			return null;
		}
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		if (type.equals("image")) { // possibly need further implementation
			return null;
		} else if (type.equals("anatomy")) {
			ArrayList componentCountInfo = ishDAO.getComponentCountInfoByGeneInfo(inputType, 
					inputString, stage, criteria, columnIndex, ascending, offset, num);
			
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
			
			/** ---return the object--- */
			return componentCountInfo;
			
		} else if (type.equals("gene")) {

			// gene query
			ArrayList submissions = ishDAO.getSubmissionByGeneInfo(inputType, 
					ignoreExpression, inputString,
					componentId, stage, criteria, columnIndex, ascending, offset, num);
			
			// release the db resources
			DBHelper.closeJDBCConnection(conn);
			ishDAO = null;
			
			/** ---return the object--- */
			return submissions;
		}
		return null;
	} // end of getDataByGene: for given component
        
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
        ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
        
        // get the value
        totalComponents +=
        	ishDAO.getTotalISHComponentsExpressingGeneQuery(inputType, 
        			ignoreExpression, inputString, stage, criteria);
        
        // release db resources
        DBHelper.closeJDBCConnection(conn);
        ishDAO = null;
        
        return totalComponents;
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
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get the value
		totalSubmissions +=
			ishDAO.getTotalNumberOfISHSubmissionsForGeneQuery(inputType,
					ignoreExpression, inputString, stage, criteria);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** return the value  */
		return totalSubmissions;
		
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
        ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
                
        // get the value
        totalSubmissions += ishDAO.getTotalNumberOfISHSubmissionsForGeneQuery(inputType, 
        		ignoreExpression, inputString, component, stage, criteria);
                
        // release db resources
        DBHelper.closeJDBCConnection(conn);
        ishDAO = null;

        /** return the value  */
        return totalSubmissions;
    }
	
	/**
	 * 
	 * @param component
	 * @param stage
	 * @return
	 */
	public ISHBrowseSubmissionData getDataByComponentId(String component, String stage,
			                               String[] order, String offset, String num) {
	    ISHBrowseSubmission[] submissions = null;
//		String[][] columnNumbers = null;

	    ISHBrowseSubmissionData ibsData = new ISHBrowseSubmissionData();

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		submissions = ishDAO.getSubmissionByComponentId(component, stage, order, offset, num);
		
		/** ---construct the composite value object---  */
		ibsData.setBroseSubmissionData(submissions);
//		ibsData.setAllColumnTotalNumbers(columnNumbers);

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		
		/** ---return the object--- */
		return ibsData;
	}
	
	/**
	 * @author xingjun
	 * used for new format of browse style page
	 * 
	 * @param component
	 * @param stage
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public ArrayList getDataByComponentId(String component, String stage,
            int columnIndex, boolean ascending, int offset, int num) {
		
		ArrayList submissions = null;
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		submissions = ishDAO.getSubmissionByComponentId(component, stage, 
				columnIndex, ascending, offset, num);
		
		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the object--- */
		return submissions;
	}
	
	/**
	 * 
	 * @param components - list of selected anatomy components
     * @param startStg - selected developmental start stage
     * @param endStg - selected developmental end stage
     * @param expStatus - 'present' or 'not detected'
	 * @param order
	 * @param offset
	 * @param num
	 * @return
	 */
	public ISHBrowseSubmission[] getDataByComponentIdList(String[] components, 
			String startStg, String endStg,
			String [] expTypes,String criteria, String[] order, String offset, String num) {

	    ISHBrowseSubmission[] submissions = null;

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		submissions =
			ishDAO.getSubmissionByComponentIds(components, startStg, endStg, 
					expTypes, criteria, order, offset, num);
		
		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the object--- */
		return submissions;
	}
        
	/**
	 * @author xingjun
	 * comply with new format of browse style page
	 * 
	 * @param components
	 * @param startStage
	 * @param endStage
	 * @param annotationTypes
	 * @param criteria
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public ArrayList getDataByComponentIdList(String[] components, String startStage, 
			String endStage, String [] annotationTypes, String criteria, 
			int columnIndex, boolean ascending, int offset, int num) {
		
		ArrayList submissions = null;

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		submissions = ishDAO.getSubmissionByComponentIds(components, startStage, endStage, 
				annotationTypes, criteria,
				columnIndex, ascending, offset, num);

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the object--- */
		return submissions;
	}
        
    public ISHBrowseSubmission[] getDataByComponentIdList(String[] components, 
    		String[] order, String offset, String num) {

        ISHBrowseSubmission[] submissions = null;

            /** ---get data from dao---  */
            // create a dao
            Connection conn = DBHelper.getDBConnection();
            ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
            
            /** ---get data--- */
            submissions = ishDAO.getSubmissionByComponentIds(components, order, offset, num);
            
            // release the db resources
            DBHelper.closeJDBCConnection(conn);
            ishDAO = null;
            
            /** ---return the object--- */
            return submissions;
    }
    
    /**
     * @author xingjun
     * comply with new format of browse style page
     * 
     * @param components
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return
     */
    public ArrayList getDataByComponentIdList(String[] components, int columnIndex,
    		boolean ascending, int offset, int num) {

        ArrayList submissions = null;

            /** ---get data from dao---  */
            // create a dao
            Connection conn = DBHelper.getDBConnection();
            ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
            
            /** ---get data--- */
            submissions = ishDAO.getSubmissionByComponentIds(components, 
            		columnIndex, ascending, offset, num);
            
            // release the db resources
            DBHelper.closeJDBCConnection(conn);
            ishDAO = null;
            
            /** ---return the object--- */
            return submissions;
    }
    
    public String getTotalISHSubmissionsForComponentQuery(String component) {
                String totalSubmissions = new String("");
                
                /** ---get data from dao--- */
                // create a dao
                Connection conn = DBHelper.getDBConnection();
                ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
                
                // get the value
                totalSubmissions += 
                	ishDAO.getTotalNumberOfISHSubmissionsForComponentQuery(component);
                
                // release db resources
                DBHelper.closeJDBCConnection(conn);
                ishDAO = null;

                /** return the value  */
                return totalSubmissions;
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
            ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
            
            // get the value
            totalSubmissions += ishDAO.getTotalNumberOfSubmissionsForComponentsQuery(components);
            
            // release db resources
            DBHelper.closeJDBCConnection(conn);
            ishDAO = null;

            /** return the value  */
            return totalSubmissions;
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
            ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
            
            // get the value
            totalSubmissions +=
            	ishDAO.getTotalNumberOfSubmissionsForComponentsQuery(components, 
            			startStg, endStg, expTypes, criteria);
            
            // release db resources
            DBHelper.closeJDBCConnection(conn);
            ishDAO = null;
            /** return the value  */
            return totalSubmissions;
        }

	/**
	 * <p>xingjun - changed to make it work for EuReGene app as well</p>
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
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
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
				// xingjun - 06/06/2011 - make it work for EuReGene app - start
				if (projectString.equalsIgnoreCase("GUDMAP")) {
					assayType = ishDAO.findAssayTypeBySubmissionId("GUDMAP:" + accessionId);
				} else if (projectString.equalsIgnoreCase("EuReGene")) {
					assayType = ishDAO.findAssayTypeBySubmissionId("ERG:" + accessionId);
				}
//				System.out.println("assay type: " + assayType);
//				submission = ishDAO.findSubmissionById("GUDMAP:" + accessionId);
				if (projectString.equalsIgnoreCase("GUDMAP")) {
					submission = ishDAO.findSubmissionById("GUDMAP:" + accessionId);
				} else if (projectString.equalsIgnoreCase("EuReGene")) {
					submission = ishDAO.findSubmissionById("ERG:" + accessionId);
				}
				// xingjun - 06/06/2011 - make it work for EuReGene app - end
				
			} else {// id is not valid
				return null;
			}
		}
		
		if (submission == null) {
			return null;
		}
		
        /********* add by Xingjun Pi 18/04/2007 begin ***********/
		// if it is a non-public submission, return null value for the submission query
		if (submission.getPublicFlag() == 0) {
			return null;
		}
        /********* add by Xingjun Pi 18/04/2007 end ***********/

		// get submission data
		// if assay type is 'ISH', get ish submisison; else if assay type is 'Microarray', 
		// get array submission
		SubmissionData submissionData = null;
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

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the composite value object---  */
		return submissionData;
	}
}