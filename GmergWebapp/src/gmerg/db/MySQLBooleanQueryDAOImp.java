package gmerg.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import gmerg.utils.table.GenericTableFilter;

public class MySQLBooleanQueryDAOImp implements BooleanQueryDAO {
    private boolean debug = false;

	private Connection conn; 
	private int ColumnNumbers = 15;//14; // Bernie 06/03/2012 (Mantis 328) added column for Sex
	
	String outputString;

    private String[] ErrorArray={"ERROR 1:", 
                                "ERROR 2: "};
    private String[][] expression_array={{"present","p"},
                                              {"not detected","nd"},
                                              {"uncertain","u"}};

    private String SELECT_STR="SELECT ";
    private String DISTINCT_STR="DISTINCT ";
    private String FROM_STR="FROM QSC_ISH_CACHE LEFT JOIN QSC_ISH_LOCATION ON QIL_SUB_ACCESSION_ID = QIC_SUB_ACCESSION_ID ";
    private String WHERE_STR="WHERE ";
    private String AND_STR="AND ";
    private String OR_STR="OR ";
    private String ORDER_STR="ORDER BY ";
    private String UNION_STR=") UNION ( ";
    private String SUB_UNION_STR="UNION ( ";
    private String INTERSECT_STR_ENTRY="AND QIC_SUB_ACCESSION_ID IN ( ";
    private String ACCESSION_STR="QIC_SUB_ACCESSION_ID ";
    // Bernie 06/03/2012 (Mantis 328) added column for Sex
    private String COLUMN_STR="QIC_RPR_SYMBOL col1,"+
                              "'' col2,"+
                              "'' col3,"+
                              "QIC_PER_NAME col4,"+
                              "QIC_SUB_SUB_DATE col5,"+
                              "QIC_SUB_EMBRYO_STG col6,"+
                              "QIC_SPN_ASSAY_TYPE col7,"+
//                              "concat(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) col8,"+
                              "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8,"+
                              "QIC_SUB_THUMBNAIL col9,"+
                              "QIC_SUB_ACCESSION_ID col10,"+
                              "'' col11,"+
                              "'' col12,"+
                              "REPLACE(QIC_SUB_ACCESSION_ID, ':', 'no') col13,"+
                              "QIC_ASSAY_TYPE col14, QIC_SPN_SEX col15 ";

    // clause to find all parent of specified anatomy term(s)
    private String INHERITANCE_CRITERIA_STR_ANSCESTOR = " AND QIC_ATN_PUBLIC_ID IN ( " +
    		"SELECT DISTINCT ANCES_ATN.ATN_PUBLIC_ID " +
    		"FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
    		"ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
    		"WHERE ANO_COMPONENT_NAME " +
    		"AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
    		"AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
    		"AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
    		"AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
//    		"AND ANCES_ATN.ATN_STAGE_FK = STG_OID " +
    		"AND APO_NODE_FK = ANO_OID " +
    		"AND APO_IS_PRIMARY = 1) ";

    // clause to find children node of given anatomy term
    private String INHERITANCE_CRITERIA_STR_DESCENDANT = " AND QIC_ATN_PUBLIC_ID IN ( " +
	"SELECT DESCEND_ATN.ATN_PUBLIC_ID " +
	"FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
	"ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
	"WHERE ANO_COMPONENT_NAME " +
	"AND ANO_OID = ANCES_ATN.ATN_NODE_FK " +
	"AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	"AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
	"AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
//	"AND ANCES_ATN.ATN_STAGE_FK = STG_OID " +
	"AND APO_NODE_FK = ANO_OID " +
	"AND APO_IS_PRIMARY = 1) ";

    private String GENE_STR = "QIC_RPR_SYMBOL ";
    private String INTERSECT_STR_GENE="AND QIC_RPR_SYMBOL IN ( ";
//    private String ORDER_BY_GENE_STR = " ORDER BY NATURAL_SORT(QIC_RPR_SYMBOL) ";
    
    private String prevOp="";
    
    private StringBuffer where_buf;
    private StringBuffer component_buf;
    private StringBuffer stage_buf;
    private StringBuffer expression_buf;
    private StringBuffer pattern_buf;
    private StringBuffer location_buf;

    private StringBuffer output_bf=new StringBuffer();

    private Vector v_queries=new Vector(0,1);
    private Vector v_expression=new Vector(0,1);
    private Vector v_stage=new Vector(0,1);
    private Vector v_component=new Vector(0,1);
    private Vector v_pattern=new Vector(0,1);
    private Vector v_location=new Vector(0,1);

    private int query_num=-1;
    private int patternIndex=-1;
    private int locationIndex=-1;
    private boolean includePattern=false;
    private boolean includeLocation=false;
    private boolean firstOperator=false;
    private boolean secondOperator=false;
    private boolean doOrAnd=false;	
	
	// default constructor
	public MySQLBooleanQueryDAOImp() {
		
	}
	
	public MySQLBooleanQueryDAOImp(Connection conn) {
		this.conn = conn;
	}
	
	public ArrayList getAllSubmissions(String input, int columnIndex, boolean ascending, int offset, int num, GenericTableFilter filter) {
//		System.out.println("getAllSubmissions#########");
//		long t=System.currentTimeMillis();		
//		PreparedStatement prepStmt = null;
		Statement stmt = null;
		
		ResultSet resSet = null;
		ArrayList<String[]> result = null;
		try {
			
//			System.out.println("booleanQuery: " + getProcessBooleanQuery(input));
			String sql = getProcessBooleanQuery(input)	+ " " 
			+ DBHelper.orderResult(columnIndex, ascending) + " "     		
    		+ " limit "+String.valueOf(offset)+","+String.valueOf(num)+" ";
			
			if (sql.indexOf("DISTINCT")<0 || sql.indexOf("DISTINCT")>15)
				sql = sql.replaceFirst("SELECT", "SELECT DISTINCT");
			
//			System.out.println("\nBOOL QUERY1 (pre filter)====== sql="+sql);
			sql = filter.addFilterSql(sql);
//			System.out.println("\nBOOL QUERY11 (post filter)====== sql= "+sql);

//			prepStmt = conn.prepareStatement(sql);
//			resSet = prepStmt.executeQuery();
			stmt = conn.createStatement();
//			System.out.println("Boolean query sql: " + sql);
		    if (debug)
			System.out.println("MySQLBooleanQueryDAOImp.sql = "+sql.toLowerCase());
			resSet = stmt.executeQuery(sql);
			result = DBHelper.formatResultSetToArrayList(resSet, ColumnNumbers);
//			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeStatement(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("getAllSubmissions######### process time="+(System.currentTimeMillis()-t));
		return result;
	}
	
	/**
	 * <p>modified by xingjun - 13/08/2009 
	 * - make it be able to deal with the situation when user providing empty query string</p>
	 */
	public int getTotalNumberOfSubmissions(String input, GenericTableFilter filter) {
//		long t=System.currentTimeMillis();		
		String queryString = getProcessBooleanQuery(input);
//System.out.println("\ninput="+input+"\nqueryString======"+queryString+"\n");		
		if (queryString == null || queryString.equals("")) {
			return 0;
		}
		queryString = queryString.replaceFirst("SELECT", "SELECT DISTINCT");
//		PreparedStatement prepStmt = null;
		Statement stmt = null;
		ResultSet resSet = null;
		int total = 0;
		try {
			String sql = "select count(*) from (" +	queryString + ") as tablea";
			
//			System.out.println("\nBOOL QUERY2 (pre filter)====== sql="+sql);
			sql = filter.addFilterSql(sql);
//			System.out.println("\nBOOL QUERY22 (post filter)====== sql= "+sql);

//			System.out.println("booleanQueryTotNumInput:"+ input);
//			System.out.println("booleanQueryTotNumSQL:"+sql);
//			prepStmt = conn.prepareStatement(sql);
			stmt = conn.createStatement();
//			resSet = prepStmt.executeQuery();
		    if (debug)
			System.out.println("MySQLBooleanQueryDAOImp.sql = "+sql.toLowerCase());
			resSet = stmt.executeQuery(sql);
			if(resSet.first()) {
				total = Integer.parseInt(resSet.getString(1));
			}
//			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeStatement(stmt);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
//		System.out.println("getTotalNumberOfRows######### process time="+(System.currentTimeMillis()-t));
//		System.out.println("boolean query total = "+total);		
		return total;
	}

	public int[] getNumberOfRowsInGroups(String input, GenericTableFilter filter) {
		return new int[]{getTotalNumberOfSubmissions(input, filter),0};
	}
	
	/**
	 * @author xingjun - 06/03/2009
	 * modified by xingjun - 31/07/2009 - only return non-empty symbols
	 */
	public ArrayList getGeneSymbols(String input) {
		PreparedStatement prepStmt = null;
		ResultSet resSet = null;
		ArrayList<String> result = null;
//		System.out.println("BooleanQueryDAO:getGeneSymbols:input: " + input);
		// get query string
		String sql = this.getGeneBooleanQueryString(input);
		System.out.println("geneBooleanQueryString:"+sql);
		if (sql == null || sql.equals("")) { // user might provide empty search query string
			return null;
		}

		// query
		try {
		    if (debug)
			System.out.println("MySQLBooleanQueryDAOImp.sql = "+sql.toLowerCase());
			prepStmt = conn.prepareStatement(sql);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				resSet.beforeFirst();
				result = new ArrayList<String>();
				while (resSet.next()) {
					String symbol = resSet.getString(1).trim();
					if (symbol != null && !symbol.equals("")) {
						result.add(symbol);
					}
				}
			}
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return
		return result;
	}
	
    // modified by xingjun - 06/03/2008 
	// - added criteria to check children nodes as well as give anatomy term(s)
	// - before modification only check given anatomy term(s)
	// !!!!!!!!! the assembling sql need further refactoring !!!!!!!!!!!!
//	public String getProcessBooleanQuery(String str)
//    {
//
//        v_queries=GetSeparatedMultiples(str,"|");
//        StringBuffer[] buffers_array;
//        Vector<StringBuffer[]> v_buffers = new Vector<StringBuffer[]>(0,1);
//        query_num=v_queries.size();
//
///*TO DETERMINE IF WE HAVE AN (A OR B) AND C AS THIS IS TREATED DIFFERENTLY TO THE REST */
//        for(int i=0;i<query_num;i++)
//        {
//            String tmp_str = v_queries.elementAt(i).toString();
//            String operator_str = tmp_str.substring(tmp_str.indexOf("}") + 1).
//                                  trim();
//            switch(i)
//            {
//            case 0:
//                if(operator_str.equalsIgnoreCase("OR"))
//                firstOperator = true;
//                break;
//            case 1:
//                if(operator_str.equalsIgnoreCase("AND"))
//                secondOperator = true;
//                break;
//            }
//        }
//        if(firstOperator&&secondOperator)
//        {
//            doOrAnd = true;
//            //System.out.println("THIS IS AN AND/OR QUERY!!!!!");
//        }
///****************************************************************************/
//        /*BUILD UP THE WHERE CLAUSE*/
//        for(int i=0;i<query_num;i++)
//        {
//             buffers_array=new StringBuffer[3];
//             where_buf=new StringBuffer("WHERE (QIC_ANO_COMPONENT_NAME=");
//             component_buf=new StringBuffer();
//             stage_buf=new StringBuffer("AND QIC_SUB_EMBRYO_STG IN (");
//             expression_buf=new StringBuffer("AND QIC_EXP_STRENGTH IN (");
//             pattern_buf=new StringBuffer("AND QIC_PTN_PATTERN=");
//             location_buf=new StringBuffer("AND QIL_LCN_LOCATION=");
//
//            ResetValues();
//            //System.out.println("QUERY: " + v_queries.elementAt(i).toString());
//            //}
//            String tmp_str = v_queries.elementAt(i).toString();
//            // added by xingjun - 06/03/2009 - get component name
//            String componentName = 
//            	tmp_str.substring(tmp_str.indexOf("{in") + 3, tmp_str.indexOf("TS")).trim();
//            
//            component_buf.append("'" + componentName + "'");
//            where_buf.append(component_buf.toString()+" ");
//            String exp_str = tmp_str.substring(0, tmp_str.indexOf("{"));
//            v_expression = GetSeparatedMultiples(exp_str, ",");
//            SetExpression(v_expression);
////            for (int j = 0; j < v_expression.size(); j++) {
//                //System.out.println(v_expression.elementAt(j).toString());
////            }
//
//            where_buf.append(expression_buf.toString());
//            String stage_str = tmp_str.substring(tmp_str.indexOf("TS"),
//                                                 tmp_str.indexOf("..") + 6);
//            stage_str = SplitString(stage_str, "TS");
//            SetStage(stage_str);
//            where_buf.append(stage_buf.toString());
//            //System.out.println("IS PATTERN??: " + tmp_str.indexOf("pt=")+"  ITERATION: " + i);
//            if (tmp_str.indexOf("pt=") > -1) {
//                includePattern = true;
//                patternIndex = tmp_str.indexOf("pt=") + 3;
//                //System.out.println("PATTERN_INDEX: " + patternIndex);
//            }
//            if (tmp_str.indexOf("lc=") > -1) {
//                includeLocation = true;
//                locationIndex = tmp_str.indexOf("lc=") + 3;
//            }
//            if (includePattern)
//            {
//                SetPattern(tmp_str);
//                where_buf.append(pattern_buf.toString());
//            }
//            if (includeLocation)
//            {
//                SetLocation(tmp_str);
//                where_buf.append(location_buf.toString());
//            }
//            StringBuffer operator_buf = 
//            	new StringBuffer(tmp_str.substring(tmp_str.indexOf("}") + 1).trim());
//            
//            // added by xingjun - 06/03/2009
//            String extraCiteriaString = 
//            	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
//            			("ANO_COMPONENT_NAME = '" + componentName + "' "));
//            where_buf.append(extraCiteriaString);
//            
//            where_buf.append(") ");
//            /*System.out.println("WHERE CLAUSE: "+ where_buf.toString());
//            System.out.println("component: " + component_buf.toString());
//            System.out.println("expression: " + expression_buf.toString());
//            System.out.println("stage: " + stage_buf.toString());
//            System.out.println("pattern: " + pattern_buf.toString());
//            System.out.println("location: " + location_buf.toString());
//            System.out.println("Operator: " + operator_buf.toString());*/
//
//            buffers_array[0]=where_buf;
//            buffers_array[1]=operator_buf;
//            buffers_array[2]=new StringBuffer(prevOp);
//
//            v_buffers.addElement(buffers_array);
//
///*SEND EACH ITERATION TO THE FULL QUERY BUILDER IF ITS NOT -OR AND-*/
//            if(!doOrAnd)
//                ConstructQuery(operator_buf.toString(),i,query_num, 0);
//
//            prevOp=operator_buf.toString();
//        }
//        /*GATHER ALL THE WHERE CLAUSES INTO A VECTOR FOR PROCESSING BY THE -OR AND- QUERY BUILDER*/
//        if(doOrAnd)
//            ConstructOrAnd(v_buffers, 0);
//
//        SetOutput(output_bf);
//        output_bf = null;
//        output_bf=new StringBuffer();
//        return GetOutput();
//    }

	/**
	 * modified by xingjun - 03/08/2009
	 * changed the code: adjusted code to get pattern and location info within the query
	 * - the sequence of putting pattern info and location info was not right. should check both pattern
	 * and location info before setPattern method being invoked
	 */
	public String getProcessBooleanQuery(String str) {
		v_queries=GetSeparatedMultiples(str,"|");
		StringBuffer[] buffers_array;
		Vector<StringBuffer[]> v_buffers = new Vector<StringBuffer[]>(0,1);
		query_num=v_queries.size();
		
		/*TO DETERMINE IF WE HAVE AN (A OR B) AND C AS THIS IS TREATED DIFFERENTLY TO THE REST */
        for(int i=0;i<query_num;i++) {
        	String tmp_str = v_queries.elementAt(i).toString();
        	String operator_str = tmp_str.substring(tmp_str.indexOf("}") + 1).trim();
            switch(i) {
            case 0:
                if(operator_str.equalsIgnoreCase("OR"))
                firstOperator = true;
                break;
            case 1:
                if(operator_str.equalsIgnoreCase("AND"))
                secondOperator = true;
                break;
            }
        }
        if(firstOperator&&secondOperator) {
        	doOrAnd = true;
            //System.out.println("THIS IS AN AND/OR QUERY!!!!!");
        }

        /*BUILD UP THE WHERE CLAUSE*/
        for(int i=0;i<query_num;i++) {
        	// initialise all sections of where clause
        	buffers_array=new StringBuffer[3];
//        	where_buf=new StringBuffer("WHERE (QIC_ANO_COMPONENT_NAME=");
        	where_buf=new StringBuffer("WHERE ");
        	StringBuffer subWhereClause = new StringBuffer("");
        	component_buf=new StringBuffer();
        	stage_buf=new StringBuffer("QIC_SUB_EMBRYO_STG IN (");
//        	expression_buf=new StringBuffer("AND QIC_EXP_STRENGTH IN (");
        	expression_buf=new StringBuffer("QIC_EXP_STRENGTH= ");
        	pattern_buf=new StringBuffer("AND QIC_PTN_PATTERN=");
        	location_buf=new StringBuffer("AND QIL_LCN_LOCATION=");
        	
        	// reset data structures to re-build the query
        	ResetValues();
        	
        	// get input query text
        	String tmp_str = v_queries.elementAt(i).toString(); 
            // get component name
//            String componentName = tmp_str.substring(tmp_str.indexOf("{in") + 3, tmp_str.indexOf("TS")).trim();
            String componentName = tmp_str.substring(tmp_str.indexOf("\"")+1, tmp_str.lastIndexOf("\"")).trim();	
//            component_buf.append("'" + componentName + "'");
//            where_buf.append(component_buf.toString()+" ");
//            subWhereClause.append(component_buf.toString()+" ");
            
            // get stage
            String stage_str = 
            	tmp_str.substring(tmp_str.indexOf("TS"), tmp_str.indexOf("..") + 6);
            stage_str = SplitString(stage_str, "TS");
            SetStage(stage_str);
            where_buf.append(stage_buf.toString());
//            subWhereClause.append(stage_buf.toString());
            
            // check if pattern and/or location info included in the query string
            if (tmp_str.indexOf("pt=") > -1) {
//            	System.out.println("IS PATTERN??: " + tmp_str.indexOf("pt=")+"  ITERATION: " + i);
                includePattern = true;
                patternIndex = tmp_str.indexOf("pt=") + 3;
                //System.out.println("PATTERN_INDEX: " + patternIndex);
            }
            if (tmp_str.indexOf("lc=") > -1) {
            	includeLocation = true;
            	locationIndex = tmp_str.indexOf("lc=") + 3;
            }
            
            // get pattern if have one
            if (includePattern) {
                SetPattern(tmp_str);
                where_buf.append(pattern_buf.toString());
//                subWhereClause.append(pattern_buf.toString());
            }

            // get location if have one
            if (includeLocation) {
                SetLocation(tmp_str);
                where_buf.append(location_buf.toString());
//                subWhereClause.append(location_buf.toString());
            }
            
            // get expression (i.e. p, nd, u) and inheritance criteria
            String exp_str = tmp_str.substring(0, tmp_str.indexOf("{"));
            v_expression = GetSeparatedMultiples(exp_str, ",");
            int eLen = v_expression.size();
            if (eLen == 1) {
            	String expValue = 
            		this.getExpressionValueFromInput(v_expression.get(0).toString());
                subWhereClause.append(" AND ").append(expression_buf.toString())
                .append("'").append(expValue).append("' ") ;
                String inheritanceCiteriaString = ""; 
                if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));

                } else if (expValue.equalsIgnoreCase("not detected")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                }
                subWhereClause.append(inheritanceCiteriaString);
            } else { // more than one expression value
            	subWhereClause.append(" AND (");
            	String tempSwClause = "";
            	for (int j=0;j<eLen;j++) {
            		String expValue = 
            			this.getExpressionValueFromInput(this.v_expression.get(j).toString());
            		String expClause = expression_buf.toString() + "'" + expValue + "' ";
                    String inheritanceCiteriaString = ""; 
                    if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    	
                    } else if (expValue.equalsIgnoreCase("not detected")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    }
            		tempSwClause += "(" + expClause + inheritanceCiteriaString + ") OR ";
            	}
            	tempSwClause = tempSwClause.substring(0, tempSwClause.length()-4);
                subWhereClause.append(tempSwClause).append(")");
            }
            
//            System.out.println("subwhere: " + subWhereClause);
            where_buf.append(subWhereClause.toString());

            // get the operator after this sub-clause
            StringBuffer operator_buf = 
            	new StringBuffer(tmp_str.substring(tmp_str.indexOf("}") + 1).trim());
            
//            System.out.println("WHERE CLAUSE: "+ where_buf.toString());
//            System.out.println("component: " + component_buf.toString());
//            System.out.println("expression: " + expression_buf.toString());
//            System.out.println("stage: " + stage_buf.toString());
//            System.out.println("pattern: " + pattern_buf.toString());
//            System.out.println("location: " + location_buf.toString());
//            System.out.println("Operator: " + operator_buf.toString());

            buffers_array[0]=where_buf;
            buffers_array[1]=operator_buf;
            buffers_array[2]=new StringBuffer(prevOp);
            v_buffers.addElement(buffers_array);
            
            // SEND EACH ITERATION TO THE FULL QUERY BUILDER IF ITS NOT -OR AND-
            if(!doOrAnd)
                ConstructQuery(operator_buf.toString(),i,query_num, 0);

            prevOp=operator_buf.toString();
        }
        // GATHER ALL THE WHERE CLAUSES INTO A VECTOR FOR PROCESSING BY THE -OR AND- QUERY BUILDER
        if(doOrAnd)
            ConstructOrAnd(v_buffers, 0);

        SetOutput(output_bf);
        output_bf = null;
        output_bf=new StringBuffer();
        return GetOutput();
    }
	
	/**
	 * @author xingjun - 07/05/2009
	 * @param input
	 * @return
	 */
	private String getExpressionValueFromInput(String input) {
		String expressionValue = "";
		for (int i=0;i<this.expression_array.length;i++) {
			if (input.equalsIgnoreCase(this.expression_array[i][1])) {
				expressionValue = this.expression_array[i][0];
				break;
			}
		}
		return expressionValue;
	}

    /**
     * <p>modified by xingjun - 10/03/2009 - make it able to construct gene query as well</p>
     * @param op
     * @param num
     * @param size
     * @param queryType - 0: entry search; 1: gene search
     */
	private void ConstructQuery(String op,int num,int size, int queryType) {
		
		if (op.equalsIgnoreCase("OR")&&num==0) {
			output_bf.append("( ");
		}
		
		output_bf.append(SELECT_STR);
		if(op.equalsIgnoreCase("AND") || op.equals("") || queryType == 1) { // add queryType condition
			output_bf.append(DISTINCT_STR);
		}
		if(prevOp.equalsIgnoreCase("AND")) {
			if (queryType == 0) {
				output_bf.append(ACCESSION_STR);
			} else if (queryType == 1) {
				output_bf.append(this.GENE_STR);
			}
		} else {
			if (queryType == 0) {
				output_bf.append(COLUMN_STR);
			} else if (queryType == 1) {
//				output_bf.append(this.DISTINCT_STR + this.GENE_STR); // added distinct modifier - xingjun - 22/04/2009
				output_bf.append(this.GENE_STR); // remove distinct modifier - xingjun - 22/04/2009
			}
		}
		output_bf.append(FROM_STR);
		output_bf.append(where_buf.toString());
		
		if(op.equalsIgnoreCase("OR"))
			output_bf.append(UNION_STR);
		else if(prevOp.equalsIgnoreCase("AND") && num<v_queries.size()-1)
			output_bf.append(") ");
		
		if(op.equalsIgnoreCase("AND")) {
			if (queryType == 0) { // entry
				output_bf.append(INTERSECT_STR_ENTRY);
			} else if (queryType == 1) { // gene
				output_bf.append(INTERSECT_STR_GENE);
			}
		}
//		System.out.println("SIZE: " + size );
		if(num==v_queries.size()-1 && size!=1)
			output_bf.append(")");
		
//		output_bf.append(this.ORDER_BY_GENE_STR);
		//PrintOutput(output_bf);
	}

    /**
     * <p>modified by xingjun - 10/03/2009 - make it able to construct gene query string as well</p>
     * @param v
     * @param queryType
     */
	private void ConstructOrAnd(Vector v, int queryType) {
    	StringBuffer []tmp_buf=new StringBuffer[8];
        output_bf.append(SELECT_STR);
        if (queryType == 0) {
    		output_bf.append(COLUMN_STR);
        } else if (queryType == 1) {
        	output_bf.append(this.DISTINCT_STR + this.GENE_STR); // added distinct modifier - xingjun - 22/04/2009
        }
        output_bf.append(FROM_STR);
        tmp_buf=(StringBuffer[])v.elementAt(2);
        output_bf.append(tmp_buf[0].toString());
        if (queryType == 0) { // entry
            output_bf.append(INTERSECT_STR_ENTRY);
        } else if (queryType == 1) { // gene
            output_bf.append(INTERSECT_STR_GENE);
        }
        output_bf.append(SELECT_STR);
        
        if (queryType == 0) { // entry
            output_bf.append(ACCESSION_STR);
        } else if (queryType == 1) { // gene
            output_bf.append(GENE_STR);
        }

        output_bf.append(FROM_STR);
        tmp_buf=(StringBuffer[])v.elementAt(0);
        output_bf.append(tmp_buf[0].toString());
        output_bf.append(SUB_UNION_STR);
        output_bf.append(SELECT_STR);
        
        if (queryType == 0) { // entry
            output_bf.append(ACCESSION_STR);
        } else if (queryType == 1) { // gene
            output_bf.append(GENE_STR);
        }
        output_bf.append(FROM_STR);
        tmp_buf=(StringBuffer[])v.elementAt(1);
        output_bf.append(tmp_buf[0].toString());
        output_bf.append(")) ");
        
//        output_bf.append(this.ORDER_BY_GENE_STR);
        
        //PrintOutput(output_bf);
    }


    private void SetOutput(StringBuffer bf)
    {
        outputString=bf.toString();
    }

    public String GetOutput()
    {
        return outputString;
    }

    private Vector GetSeparatedMultiples(String str, String separator)
    {
        Vector<String> v = new Vector<String>(0,1);
        StringTokenizer st=new StringTokenizer(str,separator);
        while(st.hasMoreTokens())
        {
           v.addElement(st.nextToken().trim());
        }
        return v;

    }

    private void ResetValues()
    {
        v_expression.clear();
        v_stage.clear();
        v_component.clear();
        v_pattern.clear();
        v_location.clear();
        includePattern=false;
        includeLocation=false;
    }

    private void SetExpression(Vector v)
    {
        for(int i=0;i<v.size();i++)
        {
            for(int j=0;j<expression_array.length;j++)
            {
                if(v.elementAt(i).toString().trim().equals(expression_array[j][1]))
                {
                    expression_buf.append("'" + expression_array[j][0] + "'");
                    break;
                }
            }
            if(i<v.size()-1)
                expression_buf.append(",");
            if(i==v.size()-1)
                expression_buf.append(") ");
        }
    }

    private void SetStage(String str)
    {
        //System.out.println("stage: " + str);
        int from=0;
        int to=0;
        from=Integer.parseInt(str.substring(0,2));
        to=Integer.parseInt(str.substring(4));
        //System.out.println("from: " + from + " to: " + to);
        if (from > to) {
        	int temp = from;
        	from = to;
        	to = temp;
        }
        for(int i=from;i<=to;i++)
        {
            stage_buf.append(i);
            if(i<to)
                stage_buf.append(",");
            if(i==to)
                stage_buf.append(") ");
        }

    }

    private void SetPattern(String str) 
    {
        int endIndex=-1;
//      if(locationIndex>-1)
//          endIndex=locationIndex-4;
      if(includeLocation) {
          endIndex=locationIndex-4;
      }
      else
          endIndex=str.indexOf("}");

//      System.out.println("pattern END INDEX: " + endIndex+" , PATTERN_INDEX: " + patternIndex);
//      System.out.println("location INDEX: " + locationIndex);

      pattern_buf.append("'"+str.substring(patternIndex,endIndex).trim()+"' ");
    }

    private void SetLocation(String str)
    {
        location_buf.append("'"+str.substring(locationIndex,str.indexOf("}")).trim()+"' ");
    }

    private String SplitString(String str,String rem)
    {
        StringBuffer sb=new StringBuffer();
        String st[]=str.split(rem);
        for(int i=0;i<st.length;i++)
        {
            sb.append(st[i]);
//            System.out.println(st[i]);
        }
        return sb.toString();
   }
    
	public String getGeneBooleanQueryString(String str) {
		v_queries=GetSeparatedMultiples(str,"|");
		StringBuffer[] buffers_array;
		Vector<StringBuffer[]> v_buffers = new Vector<StringBuffer[]>(0,1);
		query_num=v_queries.size();
		
		/*TO DETERMINE IF WE HAVE AN (A OR B) AND C AS THIS IS TREATED DIFFERENTLY TO THE REST */
        for(int i=0;i<query_num;i++) {
        	String tmp_str = v_queries.elementAt(i).toString();
        	String operator_str = tmp_str.substring(tmp_str.indexOf("}") + 1).trim();
            switch(i) {
            case 0:
                if(operator_str.equalsIgnoreCase("OR"))
                firstOperator = true;
                break;
            case 1:
                if(operator_str.equalsIgnoreCase("AND"))
                secondOperator = true;
                break;
            }
        }
        if(firstOperator&&secondOperator) {
        	doOrAnd = true;
            //System.out.println("THIS IS AN AND/OR QUERY!!!!!");
        }

        /*BUILD UP THE WHERE CLAUSE*/
        for(int i=0;i<query_num;i++) {
        	// initialise all sections of where clause
        	buffers_array=new StringBuffer[3];
//        	where_buf=new StringBuffer("WHERE (QIC_ANO_COMPONENT_NAME=");
        	where_buf=new StringBuffer("WHERE ");
        	StringBuffer subWhereClause = new StringBuffer("");
        	component_buf=new StringBuffer();
        	stage_buf=new StringBuffer("QIC_SUB_EMBRYO_STG IN (");
//        	expression_buf=new StringBuffer("AND QIC_EXP_STRENGTH IN (");
        	expression_buf=new StringBuffer("QIC_EXP_STRENGTH= ");
        	pattern_buf=new StringBuffer("AND QIC_PTN_PATTERN=");
        	location_buf=new StringBuffer("AND QIL_LCN_LOCATION=");
        	
        	// reset data structures to re-build the query
        	ResetValues();
        	
        	// get input query text
        	String tmp_str = v_queries.elementAt(i).toString(); 
        	
            // get component name
//            String componentName = tmp_str.substring(tmp_str.indexOf("{in") + 3, tmp_str.indexOf("TS")).trim();
//        	System.out.println("BooleanQueryDAO:getGeneBooleanQueryString:tmp_str: " + tmp_str); should plus one to get the component name - xingjun - 21/10/2009
            String componentName = tmp_str.substring(tmp_str.indexOf("\"")+1, tmp_str.lastIndexOf("\"")).trim();
//            System.out.println("BooleanQueryDAO:getGeneBooleanQueryString:componentName: " + tmp_str);
//            component_buf.append("'" + componentName + "'");
//            where_buf.append(component_buf.toString()+" ");
//            subWhereClause.append(component_buf.toString()+" ");
            
            // get stage
            String stage_str = 
            	tmp_str.substring(tmp_str.indexOf("TS"), tmp_str.indexOf("..") + 6);
            stage_str = SplitString(stage_str, "TS");
            SetStage(stage_str);
            where_buf.append(stage_buf.toString());
//            subWhereClause.append(stage_buf.toString());
            
            // check if pattern and/or location info included in the query string
            // System.out.println("IS PATTERN??: " + tmp_str.indexOf("pt=")+"  ITERATION: " + i);
            if (tmp_str.indexOf("pt=") > -1) {
                includePattern = true;
                patternIndex = tmp_str.indexOf("pt=") + 3;
                //System.out.println("PATTERN_INDEX: " + patternIndex);
            }
            if (tmp_str.indexOf("lc=") > -1) {
                includeLocation = true;
                locationIndex = tmp_str.indexOf("lc=") + 3;
            }

            // get pattern if have one
            if (includePattern) {
                SetPattern(tmp_str);
                where_buf.append(pattern_buf.toString());
//                subWhereClause.append(pattern_buf.toString());
            }

            // get location if have one
            if (includeLocation) {
                SetLocation(tmp_str);
                where_buf.append(location_buf.toString());
//                subWhereClause.append(location_buf.toString());
            }
            
            // get expression (i.e. p, nd, u) and inheritance criteria
            String exp_str = tmp_str.substring(0, tmp_str.indexOf("{"));
            v_expression = GetSeparatedMultiples(exp_str, ",");
            int eLen = v_expression.size();
            if (eLen == 1) {
            	String expValue = 
            		this.getExpressionValueFromInput(v_expression.get(0).toString());
                subWhereClause.append(" AND ").append(expression_buf.toString())
                .append("'").append(expValue).append("' ") ;
                String inheritanceCiteriaString = ""; 
                if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));

                } else if (expValue.equalsIgnoreCase("not detected")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                }
                subWhereClause.append(inheritanceCiteriaString);
            } else { // more than one expression value
            	subWhereClause.append(" AND (");
            	String tempSwClause = "";
            	for (int j=0;j<eLen;j++) {
            		String expValue = 
            			this.getExpressionValueFromInput(this.v_expression.get(j).toString());
            		String expClause = expression_buf.toString() + "'" + expValue + "' ";
                    String inheritanceCiteriaString = ""; 
                    if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    	
                    } else if (expValue.equalsIgnoreCase("not detected")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    }
            		tempSwClause += "(" + expClause + inheritanceCiteriaString + ") OR ";
            	}
            	tempSwClause = tempSwClause.substring(0, tempSwClause.length()-4);
                subWhereClause.append(tempSwClause).append(")");
            }
            
//            System.out.println("subwhere: " + subWhereClause);
            where_buf.append(subWhereClause.toString());

            // get the operator after this sub-clause
            StringBuffer operator_buf = 
            	new StringBuffer(tmp_str.substring(tmp_str.indexOf("}") + 1).trim());
            
//            System.out.println(i+ ":WHERE CLAUSE: "+ where_buf.toString());
//            System.out.println("component: " + component_buf.toString());
//            System.out.println("expression: " + expression_buf.toString());
//            System.out.println("stage: " + stage_buf.toString());
//            System.out.println("pattern: " + pattern_buf.toString());
//            System.out.println("location: " + location_buf.toString());
//            System.out.println("Operator: " + operator_buf.toString());

            buffers_array[0]=where_buf;
            buffers_array[1]=operator_buf;
            buffers_array[2]=new StringBuffer(prevOp);
            v_buffers.addElement(buffers_array);
            
            // SEND EACH ITERATION TO THE FULL QUERY BUILDER IF ITS NOT -OR AND-
            if(!doOrAnd)
                ConstructQuery(operator_buf.toString(),i,query_num, 1);

            prevOp=operator_buf.toString();
        } // end of for loop
        
        // GATHER ALL THE WHERE CLAUSES INTO A VECTOR FOR PROCESSING BY THE -OR AND- QUERY BUILDER
        if(doOrAnd)
            ConstructOrAnd(v_buffers, 1);

        SetOutput(output_bf);
        output_bf = null;
        output_bf=new StringBuffer();
        return GetOutput();
    }
    
}
