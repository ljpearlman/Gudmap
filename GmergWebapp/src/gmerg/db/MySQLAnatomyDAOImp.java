/**
 * 
 */
package gmerg.db;

import gmerg.beans.UserBean;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.ish.ISHBrowseSubmission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * @author xingjun
 *
 */
public class MySQLAnatomyDAOImp implements AnatomyDAO {
    private boolean debug = false;
    private Connection conn;
	
	// default constructor
	public MySQLAnatomyDAOImp() {
		
	}
	
	// constructor with connection initialisation
	public MySQLAnatomyDAOImp(Connection conn) {
		this.conn = conn;
	}
	

	/**
	 * 
	 */
	public ArrayList getTheilerStageRanges() {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.getTheilerStageRanges");
		
	    //retrieve the query to find all stages in db
	    ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	    String project = bundle.getString("project");

	    ResultSet resSet = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;

		if(project.equals("GUDMAP")) {
			parQ = DBQuery.getParamQuery("ANNOTATED_STAGE_RANGE");
	    }
	    else {
			parQ = DBQuery.getParamQuery("STAGES_IN_PERSPECTIVE");
	    }
//		System.out.println("get ts query: " + parQ.getQuerySQL());
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			resSet = prepStmt.executeQuery();
			ArrayList stageRanges = DBHelper.formatResultSetToArrayList(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
			return stageRanges;
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return null;
	}
	
	
	public String getAnatomyTermFromPublicId(String id){
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.getAnatomyTermFromPublicId");
		
		if(id == null || id.equals("")){
			return "";
		}
    	
    	ResultSet resSet = null;
    	PreparedStatement prepStmt = null;
    	ParamQuery parQ = DBQuery.getParamQuery("ANATOMY_TERM_FROM_ID");
    	
    	try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

    		parQ.setPrepStat(conn);
    		prepStmt = parQ.getPrepStat();
    		prepStmt.setString(1, id);
    		prepStmt.setString(2, id);
    		resSet = prepStmt.executeQuery();
    		if(resSet.first()){
    			return resSet.getString(1);
    		}
    		else {
    			return "";
    		}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	finally {
    		DBHelper.closeResultSet(resSet);
    		DBHelper.closePreparedStatement(prepStmt);
    	}
    	
    	return "";
    }
	
	public String getOnlogyTerms() {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.getOnlogyTerms");
		
		ResultSet resSet = null;
		Statement stmt = null;
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			stmt = conn.createStatement();
			if (debug)
			    System.out.println("MySQLAnatomyDAOImp.sql = "+DBQuery.ANATOMY_PERSPECTIVE_TERMS.toLowerCase());
			resSet = stmt.executeQuery(DBQuery.ANATOMY_PERSPECTIVE_TERMS);
			if(resSet.first()){
				resSet.last();
				int total = resSet.getRow();
				resSet.beforeFirst();
				StringBuffer terms = new StringBuffer("");
				int i = 0;
				while(resSet.next()){
					terms.append("\""+resSet.getString(1)+"\"");
					if(i != total-1)
						terms.append(", ");
					i++;
				}
				return terms.toString();
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally {
			DBHelper.closeResultSet(resSet);
			DBHelper.closeStatement(stmt);
		}
		return null;
	}
	
	/**
	 * 
	 * @param stageName
	 * @return
	 */
	public int findSequencebyStageName(String stageName) {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.findSequencebyStageName");
		
	    int sequence = -1;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("STAGE_SEQUENCE");
		PreparedStatement prepStmt = null;

		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, stageName);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				sequence = resSet.getInt(1);
			}
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
			return sequence;
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return sequence;
	}
	
	/**
	 * 
	 * @param startStage
	 * @param endStage
	 * @return built tree (an array list)
	 */
	public ArrayList getAnatomyTreeByStages(String startStage, String endStage, boolean isForBooleanQ) {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.getAnatomyTreeByStages");
		
//	    ResourceBundle bundle = ResourceBundle.getBundle("configuration");
//	    String treeType = bundle.getString("perspective");

		ParamQuery parQ = DBQuery.getParamQuery("QUERY_TREE_CONTENT");
		String queryString = parQ.getQuerySQL();
		PreparedStatement prepStmt = null;
		ResultSet resSet = null;
		ArrayList treeStructure = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			// execute query
		if (debug) 
		    System.out.println("MySQLAnatomyDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, startStage);
			prepStmt.setString(2, endStage);
			resSet = prepStmt.executeQuery();
			
			// build the tree
			treeStructure = this.buildTreeStructure(resSet, false, "", isForBooleanQ);
			return treeStructure;
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * query db to find the Public (EMAP) ids of all nodes specified by the user
	 * @param components the list of node ids the to search on
	 * @param start the starting developmental stage to search for public ids
	 * @param end the ending developmental stage to search for public ids
	 * @return list of public ids
	 */
	public ArrayList findPublicIdByComponentIdAndStage(String[] components, String startStage, String endStage) {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.findPublicIdByComponentIdAndStage");
		
//		System.out.println("start: " + startStage);
		ParamQuery parQ = DBQuery.getParamQuery("ANATOMY_PUBLIC_ID_FROM_NODE_ID");
		PreparedStatement prepStmt = null;
		ResultSet resSet = null;
		ArrayList publicIds = null;
		
		// assemble query string
		String queryString = parQ.getQuerySQL();
		int componentNumber = components.length;
		if (componentNumber == 1) {
			queryString += "= '" + components[0] + "'";
		} else {
			queryString += "IN ('" + components[0] + "'";
			for (int i=1;i<componentNumber;i++) {
				queryString += ", '" + components[i] + "'";
			}
			queryString += ") ";
		}
//		System.out.println("find public id query: " + queryString);
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			// execute query
		if (debug) 
		    System.out.println("MySQLAnatomyDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, startStage);
			prepStmt.setString(2, endStage);
			resSet = prepStmt.executeQuery();
			
			// build the tree
			publicIds = DBHelper.formatResultSetToArrayList(resSet);
			return publicIds;
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return null;
	}
	
	/**
	 * query db to find submissions that have annotation linked to all public ids found in
	 * 'executeAnatomyPublicIds' method
	 * @param results the list of public ids used to search db
	 * @return
	 */
	public ISHBrowseSubmission[] getAnnotatedSubmissionByPublicIdAndStage(ArrayList publicIds,
			String startStage, String endStage, String expressionState, String[] order, String offset, String num) {
		
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.getAnnotatedSubmissionByPublicIdAndStage");

		ParamQuery parQStart = null;
		ParamQuery parQEnd = null;
		PreparedStatement prepStmt = null;
		ResultSet resSet = null;
		
		parQStart = DBQuery.getParamQuery("GENE_IN_COMPONENT_START");
		if (expressionState.equalsIgnoreCase("present")) {
			parQEnd = DBQuery.getParamQuery("GENE_EXPRESSED_IN_COMPONENT_END");
		} else { // not detected
			parQEnd = DBQuery.getParamQuery("GENE_NOT_DETECTED_IN_COMPONENT_END");
		}
		
		// assemble query string
		// get the public id string
		String publicIdString = "";
		int publicIdNumber = publicIds.size();
		if (publicIdNumber == 1) {
			publicIdString += " = '" + ((String[])publicIds.get(0))[0] + "'";
		} else {
			publicIdString += "IN ('" + ((String[])publicIds.get(0))[0] + "'";
			for (int i=1;i<publicIdNumber;i++) {
				publicIdString += ", '" + ((String[])publicIds.get(i))[0] + "'";
			}
			publicIdString += ")) ";
		}
		
		// the whole query string
		String query = parQStart.getQuerySQL() + publicIdString + parQEnd.getQuerySQL();
		// order by gene, (expression), stage, (tissue), sex
		// expression and tissue are not column header options?????????????????????????????????
		String defaultOrder = " ORDER BY " + DBQuery.ISH_BROWSE_DEFAULT_ORDER_BY_COL;
		String queryString =
			DBHelper.assembleBrowseSubmissionQueryStringISH(1, query, defaultOrder, order, offset, num);
//		System.out.println("get annotated submission query: " + queryString);

		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			// execute query
		if (debug) 
		    System.out.println("MySQLAnatomyDAOImp.sql = "+queryString.toLowerCase());
			prepStmt = conn.prepareStatement(queryString);

			if (expressionState.equalsIgnoreCase("present")) {
				prepStmt.setString(1, "present");
			} else {
				prepStmt.setString(1, "not detected");
			}
			resSet = prepStmt.executeQuery();
			
			// get submissions
			ISHBrowseSubmission[] submissions = DBHelper.formatISHBrowseResultSet(resSet);
			return submissions;
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return null;
	}
        
    /**
     * @param submissionAccessionId
     * @param isEditor
     * @return annotationTree - an arraylist containing a string of details 
     * for a tree node in each element in the array
     */
    public ArrayList findAnnotationTreeBySubmissionId(String submissionAccessionId,
                                                      boolean isEditor, UserBean userBean) {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.findAnnotationTreeBySubmissionId");

        if (submissionAccessionId == null) {
//            throw new NullPointerException("id parameter");
			return null;
        }

        ResultSet resSet = null;
        //query to find out if the submission has any entries in the expression table of the db
        ParamQuery parQ = DBQuery.getParamQuery("SUB_HAS_ANNOTATION");
        PreparedStatement prepStmt = null;
        ArrayList annotationTree = null;

        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);

            // execute
            resSet = prepStmt.executeQuery();
            
            int privilege = 0;
    		
            if(null != userBean && null != userBean.getUser())
            	privilege = userBean.getUser().getUserPrivilege();                
            
            
            //only execute other db queries if entries exist and the count > 0
            if (resSet.first() && (resSet.getInt(1) > 0 || (resSet.getInt(1) == 0 && privilege>=3))) {
            	
            	boolean hasAnnot = true;
                if(resSet.getInt(1) == 0) {
                	hasAnnot = false;
                }
                //query to get developmental stage for the submission 
                ParamQuery stageQ = DBQuery.getParamQuery("SUB_STAGE_NAME");
                stageQ.setPrepStat(conn);
                prepStmt = stageQ.getPrepStat();
                prepStmt.setString(1, submissionAccessionId);
                resSet = prepStmt.executeQuery();
                resSet.first();
                String stageName = resSet.getString(1);
                
                ParamQuery annotationQ =
                        DBQuery.getParamQuery("ANNOT_TREE_CONTENT");
                annotationQ.setPrepStat(conn);
                prepStmt = annotationQ.getPrepStat();
                prepStmt.setString(1, stageName);
                prepStmt.setString(2, submissionAccessionId);
                //System.out.println(prepStmt.toString());
                resSet = prepStmt.executeQuery();
                
                annotationTree =
                            this.buildTreeStructure(resSet, hasAnnot, submissionAccessionId,
                                               isEditor);

                DBHelper.closeResultSet(resSet);
                DBHelper.closePreparedStatement(prepStmt);

                return annotationTree;
            }

        } catch (Exception se) {
            se.printStackTrace();
        }
        return annotationTree;
    }
    
    /**
     * 
     * @param resSet - query result for the tree
     * @param hasAnnot - if annotated
     * @param accno - submission id
     * @param alernativeFunction - true if used for editing or if boolean query being used
     * @return
     * @throws SQLException
     */
    public ArrayList buildTreeStructure(ResultSet resSet, boolean hasAnnot, String accno, boolean alternativeFunction) throws SQLException {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.buildTreeStructure");
            
        if (resSet.first()) {

            //need to reset cursor as 'if' move it on a place
            resSet.beforeFirst();

            //create ArrayList to store each row of results in
            ArrayList<String> results = new ArrayList<String>();

            //contains the javascript function for the tree
            String javascriptFunc = "";
            String additionalInfo = "";
            
            //javascript function changes depending on whether tree is annotated or not
            if (!hasAnnot) {
                
            	if(FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/ish_edit_expression.jsp")) {
	            	results.add("ANNOTATEDTREE = 0");
	                results.add("OPENATVISCERAL = 1");
	                results.add("HIGHLIGHT = 1");
	                results.add("SUBMISSION_ID = \"" + accno + "\"");
			javascriptFunc = "javascript:showExprInfo";
            	} else {
            		results.add("ANNOTATEDTREE = 0");
                    results.add("OPENATVISCERAL = 1");
                    results.add("HIGHLIGHT = 1");
                    if(alternativeFunction){
                        javascriptFunc = "javascript:toggleParamGroup";
                    }
                    else {
                        javascriptFunc = "javascript:showComponentID";
                    }            		
            	}
            } else {
                results.add("ANNOTATEDTREE = 1");
                results.add("OPENATVISCERAL = 0");
                results.add("HIGHLIGHT = 0");
                results.add("SUBMISSION_ID = \"" + accno + "\"");
                    javascriptFunc = "javascript:showExprInfo";
            }
            
            results.add("USETEXTLINKS = 1");
            results.add("STARTALLOPEN = 0");
            results.add("USEFRAMES = 0");
            results.add("USEICONS = 1");
            results.add("PRESERVESTATE = 0");

            while (resSet.next()) {
                
                String patterns = "";
 
                if (!hasAnnot) {
                    additionalInfo = resSet.getString(5);
                } else {
                    patterns = this.getPatternsForAnnotatedComponent(resSet.getString(10));
                    additionalInfo = resSet.getString(3);
                }
            
                if(additionalInfo.indexOf(".") >=0){
                    int pointIndex = additionalInfo.indexOf(".");
                    additionalInfo = additionalInfo.substring(0, pointIndex);
                }
                additionalInfo = "("+ additionalInfo+")";

                int row = resSet.getRow() - 1;
                String componentName = resSet.getString(4);
                String id = resSet.getString(3);
                String expression = resSet.getString(8);
            
                String strength = resSet.getString(9);
            
                if(expression == null){
                    expression = "not examined";
                }
                if(strength == null){
                    strength = "";
                }

                //resSet.getInt(1) tells you the depth of the component in the tree so
                //each line of javasciprt code produced is determined by this
                if (resSet.getInt(1) == 0) {
                    results.add("foldersTree = gFld(\"" + componentName + " " +
                            additionalInfo + "\", \"" + javascriptFunc +
                            "(&quot;" + id + "&quot;,&quot;" + componentName +
                            "&quot;,&quot;" + row + "&quot;)\"," +
                            resSet.getInt(7) + ",\"" + expression + "\",\"" +strength + "\",\""+patterns+"\","+resSet.getInt(11)+")");

                } else if (resSet.getInt(1) > 0) {

                    //if statement to determine whether the component has children. If it does,
                    //it will be displayed as a folder in the tree.
                    if (resSet.getInt(6) > 0) {
                        //if the parent of the component is at depth 0, add 'foldersTree' to the code
                        if (resSet.getInt(1) == 1) {
                            results.add("aux1 = insFld(foldersTree, gFld(\"" +
                                    componentName + " " + additionalInfo +
                                    "\", \"" + javascriptFunc + "(&quot;" +
                                    id + "&quot;,&quot;" + componentName +
                                    "&quot;,&quot;" + row + "&quot;)\"," +
                                    resSet.getInt(7) + ",\"" +
                                    expression + "\",\"" +
                                    strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        } else {
                            results.add("aux" + resSet.getInt(1) +
                                    " = insFld (aux" +
                                    (resSet.getInt(1) - 1) + ", gFld(\"" +
                                     componentName + " " + additionalInfo +
                                     "\", \"" + javascriptFunc + "(&quot;" +
                                     id + "&quot;,&quot;" + componentName +
                                     "&quot;,&quot;" + row + "&quot;)\"," +
                                     resSet.getInt(7) + ",\"" +
                                     expression + "\",\"" +
                                     strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        }
                    } else {
                        if (resSet.getInt(1) == 1) {
                            results.add("insDoc(foldersTree, gLnk(\"S\", \"" +
                                    componentName + " " + additionalInfo +
                                    "\", \"" + javascriptFunc + "(&quot;" +
                                    id + "&quot;,&quot;" + componentName +
                                    "&quot;,&quot;" + row + "&quot;)\"," +
                                    resSet.getInt(7) + ",\"" +
                                    expression + "\",\"" +
                                    strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        } else {
                            results.add("insDoc(aux" + (resSet.getInt(1) - 1) +
                                    ", gLnk(\"S\", \"" + componentName +
                                    " " + additionalInfo + "\", \"" +
                                    javascriptFunc + "(&quot;" + id +
                                    "&quot;,&quot;" + componentName +
                                    "&quot;,&quot;" + row + "&quot;)\"," +
                                    resSet.getInt(7) + ",\"" +
                                    expression + "\",\"" +
                                    strength + "\",\""+patterns+"\","+resSet.getInt(11)+"))");
                        }
                    }
                }
            }
            return results;
        }
        return null;
    }
    
    String getPatternsForAnnotatedComponent(String expressionOID) throws SQLException{
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.getPatternsForAnnotatedComponent");
        
        ParamQuery parQ = DBQuery.getParamQuery("EXPRESSION_PATTERNS");
        PreparedStatement prepStat = null;
        ResultSet resSet = null;
        
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

          parQ.setPrepStat(conn);
          prepStat = parQ.getPrepStat();
          prepStat.setString(1, expressionOID);
        
          resSet = prepStat.executeQuery();
        
          if(resSet.first()){
              resSet.beforeFirst();
            
              StringBuffer patterns = new StringBuffer("");
            
              while(resSet.next()){
                if(resSet.isLast()) {
                    patterns.append(resSet.getString(2));
                }
                else {
                    patterns.append(resSet.getString(2)+",");
                }
                
              }
              return patterns.toString();
          }
          return "";
        }
        finally {
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStat);
        }
    }
    
    /**
     * query database to find a list of annotated components for a specific submission
     * @param submissionAccessionId - the id of the selected submission
     * @param isEditor - boolean to determine whether the user is logged in as an editor or not
     * @return result - an array of annotated components corresponding to the submissionAccessionId param
     */
    public ExpressionDetail [] findAnnotatedListBySubmissionIds(String submissionAccessionId, boolean isEditor) {
	if (debug)
	    System.out.println("MysqlAnatomyDAOImp.findAnnotatedListBySubmissionIds");
    
        if (submissionAccessionId == null) {
//            throw new NullPointerException("id parameter");
			return null;
        }
                
        //query to find out if the submission has any entries in the expression table of the db
        ParamQuery parQ = DBQuery.getParamQuery("SUB_HAS_ANNOTATION");

        //array of annotated components for this submission
        ExpressionDetail [] result = null;
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        PreparedStatement patternStmt = null;
        ResultSet patternSet = null;
        PreparedStatement locationStmt = null;
        ResultSet locationSet = null;
        PreparedStatement compNmeStmt = null;
        ResultSet compNmeSet = null;
        
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionAccessionId);

            // execute
            resSet = prepStmt.executeQuery();
        
            //only execute other db queries if entries exist and the count > 0
            if (resSet.first() && resSet.getInt(1) > 0) {
            
                //annotationQ contains query to find annotated components for specified submission
                ParamQuery annotationQ = DBQuery.getParamQuery("ANNOT_LIST");
                annotationQ.setPrepStat(conn);
                prepStmt = annotationQ.getPrepStat();
                prepStmt.setString(1,submissionAccessionId);
                
                //execute query 
                resSet = prepStmt.executeQuery();

                resSet.last();
                
                //need to count no of rows so size of Annotated component array can be determined
                int arraySize = resSet.getRow();
                result = new ExpressionDetail [arraySize];
                
                //go back to start of result set so you can obtain relevant info for each row
                resSet.beforeFirst();
                int index = 0;
        
                while(resSet.next()){
                
                    String imgPath = "/images/tree/";
                    
                    result[index] = new ExpressionDetail();
                    
                    //set values in the AnnotatedComponent object for specified element in array
                    result[index].setComponentId(resSet.getString(2));
                    result[index].setComponentName(resSet.getString(3));
                    result[index].setPrimaryStrength(resSet.getString(4));
                    result[index].setSecondaryStrength(resSet.getString(5));
                    result[index].setNoteExists(resSet.getBoolean(6));
                    
                    
                    
                    if(resSet.getString(4).trim().equalsIgnoreCase("present")){
                        if(resSet.getString(5) == null || resSet.getString(5).trim().equalsIgnoreCase("")){
                            result[index].setExpressionImage(imgPath+"DetectedRoundPlus20x20.gif");
                        }
                        else if(resSet.getString(5).trim().equals("strong")){
                            result[index].setExpressionImage(imgPath+"StrongRoundPlus20x20.gif");
                        }
                        else if(resSet.getString(5).trim().equals("moderate")){
                            result[index].setExpressionImage(imgPath+"ModerateRoundPlus20x20.gif");
                        }
                        else if(resSet.getString(5).trim().equals("weak")){
                            result[index].setExpressionImage(imgPath+"WeakRoundPlus20x20.gif");
                        }
                    }
                    else if(resSet.getString(4).trim().equalsIgnoreCase("not detected")){
                        result[index].setExpressionImage(imgPath+"NotDetectedRoundMinus20x20.gif");
                    }
                    else if(resSet.getString(4).trim().equalsIgnoreCase("uncertain") || resSet.getString(4).trim().equalsIgnoreCase("possible")){
                        result[index].setExpressionImage(imgPath+"PossibleRound20x20.gif");
                    }
                    
                    
                    //annotationQ is changed to contain query to find list of expression patterns for this component
                    annotationQ = DBQuery.getParamQuery("EXPRESSION_PATTERNS");
                    //prepare and execute query
                    annotationQ.setPrepStat(conn);    
                    patternStmt = annotationQ.getPrepStat();
                    patternStmt.setString(1,resSet.getString(1));
                    patternSet = patternStmt.executeQuery();
                    
                    //if pattern values exist for this component
                    if(patternSet.first()){
                    
                        //need to count no of rows so size of ExpressionPattern array can be determined
                        patternSet.last();                        
                        arraySize = patternSet.getRow();
                        ExpressionPattern [] patterns = new ExpressionPattern [arraySize];
                        
                        //go back to start of result set so you can obtain relevant info for each row
                        patternSet.beforeFirst();
                        int index2 = 0;

                        while(patternSet.next()){
                            
                            patterns[index2] = new ExpressionPattern();
                            
                            //set pattern value in ExpressionPattern object for specified element in array
                            patterns[index2].setPattern(patternSet.getString(2));
                            
                            if(patternSet.getString(2) != null && !patternSet.getString(2).trim().equalsIgnoreCase("")){
                                
                                if(patternSet.getString(2).indexOf("homogeneous") >= 0){
                                    patterns[index2].setPatternImage(imgPath + "HomogeneousRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("spotted") >= 0){
                                    patterns[index2].setPatternImage(imgPath + "SpottedRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("regional") >= 0){
                                    patterns[index2].setPatternImage(imgPath + "RegionalRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("graded") >= 0){
                                    patterns[index2].setPatternImage(imgPath + "GradedRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("ubiquitous") >= 0) {
                                    patterns[index2].setPatternImage(imgPath + "UbiquitousRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("other") >= 0) {
                                    patterns[index2].setPatternImage(imgPath + "OtherRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("single cell") >= 0) {
                                    patterns[index2].setPatternImage(imgPath + "SingleCellRound20x20.png");
                                }
                                else if(patternSet.getString(2).indexOf("restricted") >= 0) {
                                    patterns[index2].setPatternImage(imgPath + "RestrictedRound20x20.png");
                                }
                            }
                            
                            //annotationQ is changed to contain query to find list of locations for this pattern
                            annotationQ = DBQuery.getParamQuery("EXP_PATTERN_LOCATIONS");
                            annotationQ.setPrepStat(conn);
                            
                            
                            locationStmt = annotationQ.getPrepStat();
                            locationStmt.setString(1, patternSet.getString(1));
                            
                            //execute query
                            locationSet = locationStmt.executeQuery();
                            StringBuffer locations = null;
                            
                             //if location values exist for this pattern
                            if(locationSet.first()){
                                locationSet.beforeFirst();
                                
                                //all loations will be stored in a String (comma separated)
                                locations = new StringBuffer("");
                                
                                while(locationSet.next()){
                                 
                                    if(locationSet.isFirst()){
                                        locations.append("- ");
                                    }
                                    
                                    
                                    String adjacentTxt = "adjacent to ";
                                    //if the location string begins with 'adjacent to ', further query is required
                                    if(locationSet.getString(1).indexOf(adjacentTxt) >= 0) {
                                    
                                        //the components public id is a substring of the location string 
                                        String atnPubIdVal = locationSet.getString(1).substring(adjacentTxt.length());
                                        
                                        /*need to get the anatomy prefix and attach it to the id obtained previously
                                        then query the database to get the mane of the component*/
                                        ResourceBundle bundle = ResourceBundle.getBundle("configuration");
                                        String anatIdPrefix = bundle.getString("anatomy_id_prefix");
                                        
                                        annotationQ = DBQuery.getParamQuery("COMPONENT_NAME_FROM_ATN_PUBLIC_ID");
                                        annotationQ.setPrepStat(conn);
                                        compNmeStmt = annotationQ.getPrepStat();
                                        compNmeStmt.setString(1, anatIdPrefix+atnPubIdVal);
                                        compNmeSet = compNmeStmt.executeQuery();
                                        if(compNmeSet.first()){
                                            locations.append(adjacentTxt+ compNmeSet.getString(1));
                                        }
                                    }
                                    else {
                                        locations.append(locationSet.getString(1));
                                    }
                                    
                                    if(!locationSet.isLast()){
                                        locations.append(", ");
                                    }
                                }
                            }
                            if(locations != null){
                                patterns[index2].setLocations(locations.toString());
                            }
                            
                            index2++;
                        }
                        //set the location value in the ExpressionPattern object for the specified element in the array
                        result[index].setPattern(patterns);
                    }
                    index++;
                }
                return result;
            }
            return null;

        }
        catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(patternSet);
            DBHelper.closePreparedStatement(patternStmt);
            DBHelper.closeResultSet(locationSet);
            DBHelper.closePreparedStatement(locationStmt);
            DBHelper.closeResultSet(compNmeSet);
            DBHelper.closePreparedStatement(compNmeStmt);
        }
        return null;
    }
}
