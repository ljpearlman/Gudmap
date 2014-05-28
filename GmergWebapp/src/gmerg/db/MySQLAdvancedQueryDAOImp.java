package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import gmerg.db.AdvancedSearchDBQuery;
import gmerg.utils.Utility;
import gmerg.utils.table.GenericTableFilter;

public class MySQLAdvancedQueryDAOImp implements AdvancedQueryDAO{
    protected boolean debug = true;
    
    private Connection conn;
    private int ColumnNumbers = 17; //15;// 14 //Bernie - 01/03/2012 - (Mantis 619) added 'sex column so increase from 14 to 15'
    private int ColumnQuickNumbers = 15; //14;
    private int MAXROWS = 20;
    
    // default constructor
    public MySQLAdvancedQueryDAOImp() {    	
    }
    
    // constructor with connection initialisation
    
    public MySQLAdvancedQueryDAOImp(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * old version. not sure still needed
     * 
     * @param option
     * @param ishargs
     * @param micargs
     * @param order
     * @param offset
     * @param resPerPage
     * @param total
     * @param sub
     * @return
     */
    public String[] assembleSQL(ArrayList option, ArrayList ishargs, 
				ArrayList micargs, String[] order, String offset, 
				String resPerPage, int[] total, String sub) {
	if (debug) {
	    System.out.println("entered assembleSQL!!!!!!!!!!!!!");
	    if (option != null && option.size() > 0) {
        	System.out.println("option size: " + option.size());
	    }
	    if (ishargs != null && ishargs.size()>0) {
    		for (int i=0;i<ishargs.size();i++) {
		    System.out.println("ishargs size:" + ishargs.get(i));
    		}
	    }
	}
	
    	Object[] onerow = null;
    	Object[] value = null;
    	Object[] item = null;
    	String label = null;
    	ArrayList<String> bothwhere = new ArrayList<String>();
    	ArrayList<String> ishBothWhere = new ArrayList<String>();
    	ArrayList<String> micBothWhere = new ArrayList<String>();
    	ArrayList<String> ishwhere = new ArrayList<String>();
    	ArrayList<String> micwhere = new ArrayList<String>();
    	String lookup = null;
    	String[] split = null;
    	String lookup2 = null;
    	String lookup3 = null;    	    	
	
    	if(null != option) {
	    for(int i = 0; i < option.size(); i++) {
		onerow = (Object[])option.get(i);
		label = (String)onerow[0];
		if (debug)
		    System.out.println("label value: " + label);
		
		lookup = (String)AdvancedSearchDBQuery.getLookupInDB().get(label);
		lookup2 = (String)AdvancedSearchDBQuery.getLookupTable().get(lookup);
		lookup3 = (String)AdvancedSearchDBQuery.getLookupInDBTwo().get(label);
		split = lookup.split(",");
		
		
		
		value = (Object[])onerow[1];
		for(int j = 0; j < value.length; j++) {
		    item = (Object[])value[j];
		    if(label.indexOf("Probe Name") > -1
		       || label.indexOf("Gene Name") > -1
		       || label.indexOf("Gene Synonym") > -1
		       || label.indexOf("MGI ID") > -1
		       || label.indexOf("Ensembl ID") > -1
		       || label.indexOf("GenBank ID") > -1
		       || label.indexOf("Submission Date") > -1//3
		       || label.indexOf("Series GEO ID") > -1
		       || label.indexOf("Series Title") > -1
		       || label.indexOf("Sample GEO ID") > -1
		       || label.indexOf("Clone Name") > -1
		       || label.indexOf("Affymetrix ID") > -1) {
			if (debug) {
			    System.out.println("label is one of them&&&&&&&&&&&&&&&&");
			    System.out.println("item value: " + ((Object[])item)[0]);
			}
			if(null != ((Object[])item)[0] && !((Object[])item)[0].equals("")) {
			    if (debug)
				System.out.println("split length: " + split.length);
			    if(null != split) {
				if(split.length == 1) {
				    if (debug)
					System.out.println("lookup2: " + lookup2);
				    if(lookup2.indexOf("QSC_ISH_CACHE") > -1) {
					ishwhere.add(new String(lookup+"='"+((Object[])item)[0])+"'");
				    } else if(lookup2.indexOf("QSC_MIC_CACHE") > -1) {
					micwhere.add(new String(lookup+"='"+((Object[])item)[0])+"'");
				    } else if (lookup2.indexOf("MIC_BROWSE_CACHE") > -1) {
					micwhere.add(new String(lookup+"='"+((Object[])item)[0])+"'");
				    }
				} else if(split.length == 2) {
				    if(label.indexOf("Gene Name") > -1
				       || label.indexOf("Gene Synonym") > -1) {
					String[] split2 = lookup.split(",");
					ishwhere.add(new String(split2[0]+"='"+((Object[])item)[0])+"'");
					micwhere.add(new String(split2[1]+"='"+((Object[])item)[0])+"'");
				    } else {
					bothwhere.add(new String(lookup3+"='"+((Object[])item)[0])+"'");
					
					ishBothWhere.add(new String(split[0]+"='"+((Object[])item)[0])+"'");
					micBothWhere.add(new String(split[1]+"='"+((Object[])item)[0])+"'");
				    }
				    
				}
			    }
			}
			
		    } else if(label.indexOf("Tissue") > -1
			      || label.indexOf("Probe Strain") > -1
			      || label.indexOf("Gene Type") > -1
			      || label.indexOf("Visualization Method") > -1
			      || label.indexOf("Assay Type") > -1
			      || label.indexOf("Fixation Method") > -1
			      || label.indexOf("Specimen Strain") > -1
			      || label.indexOf("Sex") > -1
			      || label.indexOf("Stage") > -1
			      || label.indexOf("Age") > -1
			      || label.indexOf("Genotype") > -1
			      || label.indexOf("Lab Name") > -1
			      || label.indexOf("Pattern") > -1
			      || label.indexOf("Platform GEO ID") > -1
			      || label.indexOf("Platform Title") > -1
			      || label.indexOf("Platform Name") > -1
			      || label.indexOf("Sample Strain") > -1
			      || label.indexOf("Sample Sex") > -1
			      || label.indexOf("Sample Age") > -1
			      || label.indexOf("Sample Stage") > -1) {
			if (debug) {
			    System.out.println("AGE:"+new String(lookup3+":"+label)+":"+split.length+":"+((Object[])item)[1]+":"+lookup);
			    System.out.println("label is one of them#########");
			    System.out.println("item value: " + ((Object[])item)[1]);
			}
			if(null != ((Object[])item)[1] && !((Object[])item)[1].equals("")) {  
			    if(null != split) {
				if (debug)
				    System.out.println("split length: " + split.length);
				if(split.length == 1) {
				    if (debug)
					System.out.println("item value: " + ((Object[])item)[1]);
				    if(lookup2.indexOf("QSC_ISH_CACHE") > -1) {
					ishwhere.add(new String(lookup+"='"+((Object[])item)[1])+"'");
				    } else if(lookup2.indexOf("QSC_MIC_CACHE") > -1) {
					micwhere.add(new String(lookup+"='"+((Object[])item)[1])+"'");
				    }    			
				} else if(split.length == 2) {
				    bothwhere.add(new String(lookup3+"='"+((Object[])item)[1])+"'");
				    
				    ishBothWhere.add(new String(split[0]+"='"+((Object[])item)[1])+"'");
				    micBothWhere.add(new String(split[1]+"='"+((Object[])item)[1])+"'");
				    
				} else if (split.length == 4) {
				    bothwhere.add(new String(lookup3+"='"+((Object[])item)[1])+"'");
				    ishBothWhere.add(split[0] + "," + split[1] + "='" + ((Object[])item)[1] + "'");
				    micBothWhere.add(split[2] + "," + split[3] + "='" + ((Object[])item)[1] + "'");
				}
			    }
			}
		    } else if(label.indexOf("EMAP ID") > -1){
			
			if(null != ((Object[])item)[0] && !((Object[])item)[0].equals("")) {
			    if(null != split) {
				if(split.length == 2) {
				    bothwhere.add(new String(
							     " "+
							     ""+AdvancedSearchDBQuery.getComponentQuery((String)((Object[])item)[0])));
				    
				    ishBothWhere.add(" " + AdvancedSearchDBQuery.getComponentQuery((String)((Object[])item)[0], "ISH"));
				    micBothWhere.add(" " + AdvancedSearchDBQuery.getComponentQuery((String)((Object[])item)[0], "Microarray"));
				    
				}
			    }
			}
		    } else if(label.indexOf("Ontology Terms") > -1){
			
			if(null != ((Object[])item)[0] && !((Object[])item)[0].equals("")){
			    if(null != split) {
				if(split.length == 2) {
				    ishwhere.add(" " + AdvancedSearchDBQuery.getComponentNameQuery((String)((Object[])item)[0], "ISH"));
				    
				    String key = "P";
				    if(null != ((Object[])item)[1]) {
					if(((Object[])item)[1].equals("present")) {
					    key = "P";
					} else if(((Object[])item)[1].equals("not detected")) {
					    key = "A";
					}
				    }
				    
				    micwhere.add(" " + AdvancedSearchDBQuery.getComponentNameQuery((String)((Object[])item)[0], "Microarray"));
				}
			    }
			}
			
		    } else if(label.indexOf("Location") > -1){
			if (debug)
			    System.out.println("LSLSLSLSLSLS:"+((Object[])item)[0]+":"+((Object[])item)[1]);	
			if(null != ((Object[])item)[1]) { 
			    if(((Object[])item)[1].equals("adjacent to ")) {
				if(null != ((Object[])item)[0] && !((Object[])item)[0].equals("")) {
				    /*bothwhere.add(new String(lookup3+"='"+((Object[])item)[0])+
				      "' AND QSC_EXP_STRENGTH='" + ((Object[])item)[1]+ "' "+
				      "AND "+AdvancedSearchDBQuery.getComponentQuery((String)((Object[])item)[0]));
				    */
				    ishwhere.add(new String(
							    "QIL_LCN_LOCATION in (select ATN_PUBLIC_ID  from ANA_TIMED_NODE, ANA_NODE where ATN_NODE_FK=ANO_OID and  ANO_COMPONENT_NAME like '%"+((Object[])item)[0]+"%')"
							    ));
				} 
			    } else if (!((Object[])item)[1].equals("")){
				ishwhere.add(new String("QIL_LCN_LOCATION"+" like '%"+((Object[])item)[1]+"%'"));
				
			    }
			}
		    } else if(label.indexOf("Gene Symbol") > -1){
			if (debug)
			    System.out.println("lable value is gene symbol!!!!!!!!!!");
			if(null != ((Object[])item)[6] && !((Object[])item)[6].equals("")) {   						
			    if(null != split) {
				String[] split2 = lookup.split(",");
				
				String[] symbols = getGeneSymbolArray((String)((Object[])item)[6], "equals");
				if (debug) {
				    System.out.println("symbols size:"+ symbols.length);
				    if (symbols != null && symbols.length > 0)
					System.out.println("symbols value:"+ symbols[0]);
				}
				if(null != symbols) {
				    for(int k = 0; k < symbols.length; k++) {
					ishargs.add(symbols[k]);
				    }
				    for(int k = 0; k < symbols.length; k++) {
					micargs.add(symbols[k]);
				    }
				}
				String ishsql = criteriaClause(split2[0], "equals", symbols.length);
				String micsql = criteriaClause(split2[1], "equals", symbols.length);
				ishwhere.add(ishsql);
				micwhere.add(micsql);
			    }
			}
		    }            				
		    if (debug)
			System.out.println("item:"+((Object[])item)[0]+":"+((Object[])item)[1]+":"+((Object[])item)[6]);
		    
		}
	    }
	    
	    String ishoption = "";
	    String micoption = "";
	    String ishBothOption = "";
	    String micBothOption = "";
	    
	    //does ish
	    for(int i = 0; i < ishwhere.size()-1; i++) {
		ishoption+=ishwhere.get(i) + " AND ";
	    }
	    if(ishwhere.size()>=1) ishoption += ishwhere.get(ishwhere.size()-1)+" ";
	    
	    //does mic
	    for(int i = 0; i < micwhere.size()-1; i++) {
		micoption+=micwhere.get(i) + " AND ";
	    }
	    if(micwhere.size()>=1) micoption+=micwhere.get(micwhere.size()-1)+" ";
	    
	    for(int i = 0; i < ishBothWhere.size()-1; i++) {
		ishBothOption += ishBothWhere.get(i) + " AND ";
	    }
	    if(ishBothWhere.size() >= 1) {
		ishBothOption += ishBothWhere.get(ishBothWhere.size()-1) + " ";
	    }
	    for(int i = 0; i < micBothWhere.size()-1; i++) {
		micBothOption += micBothWhere.get(i) + " AND ";
	    }
	    if(micBothWhere.size() >= 1) {
		micBothOption += micBothWhere.get(micBothWhere.size()-1) + " ";
	    }
	    
	    if(!ishoption.equals("") && !ishBothOption.equals("")) {
		ishoption += " AND ";
	    }
	    if(!micoption.equals("")) {
		micoption = " AND " + micoption;
		
	    }
	    if(!micBothOption.equals("")) {
		micoption += " AND ";
	    }	
	    if(!ishoption.equals("") || !ishBothOption.equals(""))
		ishoption = " WHERE " + ishoption;
	    
	    
	    String orderStr = DBHelper.orderResult(order);
	    
	    String append = "";
	    String appendish = "";
	    boolean addBigTable = false;
	    
	    addBigTable = addBigTable(option);
	    
	    if(micBothWhere.size() > 0 || micwhere.size() > 0) {
		append = AdvancedSearchDBQuery.getMICSelect() +
		    (addBigTable == true?AdvancedSearchDBQuery.getMICFrom():AdvancedSearchDBQuery.getMICFromAdvanced()) +
		    micoption + micBothOption + " ) ";
	    }
	    if(ishBothWhere.size() > 0 || ishwhere.size() > 0) {
		appendish = AdvancedSearchDBQuery.getISHSelect() + AdvancedSearchDBQuery.getISHFromLocation() + 
		    ishoption + ishBothOption + " ) ";
	    }
	    
	    if (null != resPerPage && resPerPage.compareToIgnoreCase("ALL") == 0) {
		resPerPage = String.valueOf(total[0]);
            	offset = "0";
            }
	    
	    if(null != sub) {
		if(sub.equals("ISH")) {
		    append = "";
		} else if(sub.equals("Microarray")) {
		    appendish = "";
		}
	    }
	    
	    String[] all = new String[3];
	    if(appendish.equals("") && append.equals("")) {
		all[0] = null;
	    } else {
		all[0] = /*AdvancedSearchDBQuery.getISHSelect()+
			   AdvancedSearchDBQuery.getISHFrom() + 
			   ishoption + bothoption + " ) "*/ append+
		    new String(appendish.equals("")||append.equals("")?"":AdvancedSearchDBQuery.getUnion())+
		    appendish
		    +
		    new String(null == order||order.length < 2?AdvancedSearchDBQuery.getOrder():
			       orderStr + " " 
			       )
		    + new String(null == total?
				 " ":" limit "+offset+","+resPerPage+"  ");
	    }
	    
	    if(appendish.equals("")) {
		all[1] = null;
	    } else {	   		
		all[1] = AdvancedSearchDBQuery.getISHCount()+
		    AdvancedSearchDBQuery.getISHFromLocation() + 
		    ishoption + ishBothOption;
	    }
	    if(append.equals("")) {
		all[2] = null;
	    } else {
		all[2] = AdvancedSearchDBQuery.getMICCount()+
		    (addBigTable == true?AdvancedSearchDBQuery.getMICFrom():AdvancedSearchDBQuery.getMICFromAdvanced()) + 
		    micoption + micBothOption;
	    }
	    
	    if (debug && null != all) {
		if (1 < all.length) 
		    System.out.println("COUNTISH:"+all[1]);
		if (2 < all.length) 
		    System.out.println("COUNTMIC:"+all[2]);
		if (0 < all.length) 
		    System.out.println("SUBSUB:"+sub+":"+all[0]);
	    }
	    return all;
    	}
	if (debug)
	    System.out.println("!!! return null");
	
    	return null;
    }
    
    public boolean addBigTable(ArrayList option) {
    	Object[] onerow = null;
    	String label = null;
    	String value = null;
    	if(null != option) {
	    for(int i = 0; i < option.size(); i++) {
		onerow = (Object[])option.get(i);
		label = (String)onerow[0];
		if(null != label) {
		    value = (String)AdvancedSearchDBQuery.getLookup2().get(label);
		    if(value.equals("6") || 
		       value.equals("33")) {
			return true;
		    }
		}
	    }
    	}
    	return false;
    }
    
    
    
    
    
    /*begin for Gene Symbol*/
    private String[] getGeneSymbolArray(String geneSymbol, String criteria)
    {
	ArrayList<String> geneSymbolList = new ArrayList<String>();
	StringTokenizer st = new StringTokenizer(geneSymbol, " \t\n\r\f,");
	while (st.hasMoreTokens())
	    geneSymbolList.add(st.nextToken());
	
	int n = geneSymbolList.size();
	String[] geneSymbolArray = new String[n];
	geneSymbolArray = (String[])geneSymbolList.toArray(geneSymbolArray);
	geneSymbolList.clear();
	for(int i=0; i<n; i++){
	    if(criteria.equals("wildcard")) 
		geneSymbolArray[i] = "%"+geneSymbolArray[i]+"%";
	    else if(criteria.equals("begins")) 
		geneSymbolArray[i] = geneSymbolArray[i]+"%";
	}
	
	return geneSymbolArray;
    }
    
    private static String criteriaClause(String arg, String criteria, int geneSymbolNo)
    {
	String criteriaString = "";
	
	if(criteria.equals("wildcard") || criteria.equals("begins")) {
	    criteriaString = " (" + arg + " LIKE ? ";
	    for(int i=0; i<geneSymbolNo-1; i++)
	        criteriaString += "OR " + arg + " LIKE ? ";
	    criteriaString += ") ";
	} 
	else {
	    criteriaString = arg + " IN ( ?";
	    for(int i=0; i<geneSymbolNo-1; i++)
	        criteriaString += " , ?";
	    criteriaString += " ) ";
	}
	
	return criteriaString;
    }
    
    private static String geneColumnClause(String inputType, String criteria, int geneSymbolNo)
    {
	String geneColumn = "";
	
	if(inputType.equals("symbol")) 
	    geneColumn = criteriaClause("RPR_SYMBOL", criteria, geneSymbolNo);
	/*else if (inputType.equals("name")) 
	  geneColumn = criteriaClause("RPR_NAME", criteria, geneSymbolNo);
	  else if (inputType.equals("synonym")) 
	  geneColumn = criteriaClause("RSY_SYNONYM", criteria, geneSymbolNo);
	  else if (inputType.equals("all")) 
	  geneColumn = criteriaClause("RSY_SYNONYM", criteria, geneSymbolNo) + " OR " +
	  criteriaClause("RPR_NAME", criteria, geneSymbolNo) + " OR " +
	  criteriaClause("RPR_SYMBOL", criteria, geneSymbolNo) ;*/
	
	return geneColumn;    
    }	
    
    /* end for Gene Symbol*/  
    
    private ArrayList<String> formatImageResultSet(ResultSet resSetImage) throws SQLException {     
        if (resSetImage.first()) {
	    
            resSetImage.beforeFirst();           
            ArrayList<String> results = new ArrayList<String>();
            while (resSetImage.next()) {
                String columns = resSetImage.getString(1);
                if(null != columns && !columns.equals(""))
		    results.add(columns);
            }
	    
            return results;
        }
        return null;
    }    
    
    
    /**
     * method to determine which sql tables to draw data from depending on the value of a specific table column
     * @param option - the sql column name
     * @return string literal containing names of tables to query in sql format
     */
    public String decideWhere (String option) {
    	if(option.equals("QMC_SER_GEO_ID") || 
	   option.equals("QMC_PLT_GEO_ID") || 
	   option.equals("QMC_SMP_GEO_ID")) {
	    return AdvancedSearchDBQuery.getMICFromQSCOnly();
    	} else if(option.equals("MBC_GNF_SYMBOL") || option.equals("MBC_SUB_ACCESSION_ID")) {
	    return AdvancedSearchDBQuery.getMICFromMerhan() + " WHERE ";
    	}
    	else {
	    return AdvancedSearchDBQuery.getMICFromForAnatomy() + 
    		" WHERE ";
    	}
    }
    /**
     * method to determine which sql table columns to use to build sql query from depending on value of a specific table column
     * @param option - the sql column name
     * @return string literal containing names of columns to query in sql format
     */
    public String decideSelect (String option) {
    	if(option.equals("QMC_SER_GEO_ID") || 
	   option.equals("QMC_PLT_GEO_ID") || 
	   option.equals("QMC_SMP_GEO_ID")) {
	    return AdvancedSearchDBQuery.getMICSelectForGEOID();
    	} else if(option.equals("MBC_GNF_SYMBOL") || option.equals("MBC_SUB_ACCESSION_ID")) {
	    return AdvancedSearchDBQuery.getMICSelectForMerhan();
    	} else {
	    return AdvancedSearchDBQuery.getMICSelectForAnatomy();
    	}
    }
    
    /**
     * method to retrieve anatomy timed component ids of ancestor or descendent 
     * components for a particular set of user specified components 
     * @param input - list of anatomy timed component ids
     * @param relationType - determines whether to find ancestors or descendents in the list
     * @return comps - a list of timed component public ids which are either the 
     * ancestors or descendents of the list of components in 'input'
     */
    private String [] getTransitiveRelations(String [] input, String relationType){
	
    	//string to contain sql
    	String transitiveRelationsQ;
	
    	//check whether you want to find ancestor or desendent - sql dependent on this clause
    	if(relationType.equalsIgnoreCase("ancestor")) { 
    		transitiveRelationsQ = DBQuery.getComponentRelations(input.length, "ANCES_ATN.ATN_PUBLIC_ID", "DESCEND_ATN.ATN_PUBLIC_ID");
    	}
    	//must be descendent
    	else {
    		transitiveRelationsQ = DBQuery.getComponentRelations(input.length, "DESCEND_ATN.ATN_PUBLIC_ID","ANCES_ATN.ATN_PUBLIC_ID");
    	}
	
    	PreparedStatement stmt = null;
    	ResultSet resSet = null;
	
    	try {
		    stmt = conn.prepareStatement(transitiveRelationsQ);
		    for(int i=0;i<input.length;i++){
		    	stmt.setString(i+1, input[i]);
		    }
		    if (debug)
		    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
		    
		    resSet = stmt.executeQuery();
		    if(resSet.first()){
				resSet.last();
				int i = resSet.getRow();
				resSet.beforeFirst();
				String [] comps = new String[i];
				i = 0;
				//add each result row to array
				while(resSet.next()) {
				    comps[i] = resSet.getString(1);
				    i++;
				}
				return comps;
		    }
		    
	    	return null;
    	}
    	catch(SQLException e){
    		e.printStackTrace();
        	return null;
    	}
    	finally {
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(stmt);
    	}
    }
    
    /**
     * @param input
     * @return
     */
    private String [] getTimedComponentIdsFromInput(String input) {
    	//string to contain sql
    	String anaComponentsQ = DBQuery.getTimedComponentsFromSynsAndIdsQuery();
    	PreparedStatement stmt = null;
		ResultSet resSet = null;
		try{
		    stmt = conn.prepareStatement(anaComponentsQ);
		    
		    //there are 4 unions in the query so the input param will have to be set 4 times
		    for(int j=0;j<4;j++) {
		    	stmt.setString(j+1, input);
		    }
		    if (debug)
		    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
		    //execute the query for a single element in the input array
		    resSet = stmt.executeQuery();
		    if(resSet.first()){
				resSet.last();
				String [] componentSet = new String [resSet.getRow()];
				resSet.beforeFirst();
				int index = 0;
				while(resSet.next()){
				    componentSet[index] = resSet.getString(1);
				    index++;
				}
				return componentSet;
		    }		
	    	return null;
		}
		catch(SQLException e){
		    e.printStackTrace();
	    	return null;
		}
		finally {
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(stmt);
		}
    }
    
    /**
     * method to retrieve list of anatomy timed component ids from list of user inputs. 
     * This list can contain anatomy terms, anatomy synonyms, timed components public ids
     * and abstract component public ids
     * @param input - array containing list of user inputs
     * @return comps - list of timed component public ids determined from user inputs
     */
    private String [] getComponentsForSynonymsAndPublicIds(String [] input) {
	
    	//string to contain sql
    	String anaComponentsQ = DBQuery.getComponentsForSynsAndIdsQuery(input.length);
		PreparedStatement stmt = null;
		ResultSet resSet = null;
		
		try {
		    stmt = conn.prepareStatement(anaComponentsQ);
		    
		    for(int i=0;i<4;i++){
				for(int j=0;j<input.length;j++){
				    stmt.setString((i*input.length)+j+1, input[j].trim());
				}			
		    }
		    
		    if (debug)
		    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
		    
		    resSet = stmt.executeQuery();
		    if(resSet.first()){
				resSet.last();
				int len = resSet.getRow();
				resSet.beforeFirst();
				String [] comps = new String [len];
				int i=0;
				//add each result row to array
				while(resSet.next()){
				    comps[i] = resSet.getString(1);
				    
				    i++;
				}
				return comps;
		    }
	    	return null;
		}
		catch(SQLException e){
		    e.printStackTrace();
	    	return null;
		}
		finally {
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(stmt);
		}
    }
    
    /**
     * method to retrieve a list of gene symbols relating to a user-specified gene function
     * @param input - list of gene function inputs
     * @param wildcard - string to determine whether the user wants to search for an exact string,
     * a string starting with a selected input or a string containing a specified string 
     * @return geneSymbols - a list of gene symbols with annotation matching the specified gene function 
     */
    private String [] getSymbolsFromGeneFunctionInputParams(String [] input, String wildcard){
	
    	//array to contain the a specific sql query and the condition to narrow down the reslut set
    	String [] symbolsQParts;
    	String goIdListQ;
	
    	PreparedStatement stmt = null;
    	ResultSet resSet = null;
    	try {
	    
		    //if user wants to do a wild card search
		    if (wildcard.equalsIgnoreCase("contains") || wildcard.equalsIgnoreCase("starts with")) {
				//get components to build query to find GO: ids for a specific function
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGoTerm_GoId");
				//create the query string from the users input and the components stored in symbolsQParts
				goIdListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
		    }
		    //search for an exact string
		    else {
				//get components to build query to find GO: ids for a specific function
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGoTerm_GoId");
				//create the query string from the users input and the components stored in symbolsQParts
				goIdListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
		    }
		    
		    // need to execute query to get go id list here
		    String[] goIdList = null;
		    stmt = conn.prepareStatement(goIdListQ);
		    //set each user input as a query parameter
		    for(int i=0;i<input.length;i++){
				if(wildcard.equalsIgnoreCase("contains")) {
				    stmt.setString(i+1, "%"+input[i].trim()+"%");
				}
				else if(wildcard.equalsIgnoreCase("starts with")){
				    stmt.setString(i+1, input[i].trim()+"%");
				}
				else {
				    stmt.setString(i+1, input[i].trim());
				}
		    }
		    
		    if (debug)
		    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
		    
		    resSet = stmt.executeQuery();
		    if (resSet.first()) {
				resSet.last();
				int rowCount = resSet.getRow();
				resSet.beforeFirst();
				goIdList = new String[rowCount];
				int i = 0;
				//ad the lsit of go ids to an array
				while (resSet.next()) {
				    goIdList[i] = resSet.getString(1);
				    i++;
				}
				
				////get components to build query to find gene symbols using GO ids found above
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefMgiGoGene_MrkSymbol");
				//create the query string from the GO id list and the components stored in symbolsQParts
				String symbolsFromGoIdListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(goIdList,symbolsQParts[0], symbolsQParts[1], 1);
				
				stmt = conn.prepareStatement(symbolsFromGoIdListQ);
				//set each GO id as a query parameter
				for(i=0;i< goIdList.length;i++){
				    stmt.setString(i+1, goIdList[i].trim());
				}
				
				if (debug)
				    System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
				
				resSet = stmt.executeQuery();
				if(resSet.first()){
				    resSet.last();
				    rowCount = resSet.getRow();
				    resSet.beforeFirst();
				    String [] geneSymbols = new String [rowCount];
				    i = 0;
				    //add list of symbols to an array
				    while (resSet.next()) {
						geneSymbols[i] = resSet.getString(1);
						i++;
				    }
				    return geneSymbols;	
				}			
		    }
		   	return null;
    	}
		catch(SQLException e){
		    e.printStackTrace();
	    	return null;
		}
		finally {
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(stmt);
		}
   }
    
    /**
     * method to retrieve list of gene symbols from user input.
     * <p>modified by xingjun - 12/10/2009 - added code to find symbol from REF_GENE_INFO using gene synonym</p>
     * @param input - list of user inputs. Could be any one of gene name, MGI id, ENSEMBL id, gene symbol, gene synonym, MTF id
     * @param wildcard - string to determine whether the user wants to search for an exact string,
     * @return geneSymbols - a list of gene symbols determined from user input
     */
    private String[] getSymbolsFromGeneInputParams(String[] input, String wildcard) {
	
    	//array to contain components to build a specific query
		String[] symbolsQParts;
		//string to contain sql to find gene symbols from REF_PROBE using gene symbol
		String symbolsFromRefProbeSymbolQ;
		//string to contain sql to find gene symbol from REF_PROBE using gene name
		String symbolsFromRefProbeNameQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene symbol
		String symbolsFromrefGeneInfoSymbolQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene name
		String symbolsFromrefGeneInfoNameQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene synonym
		String symbolsFromrefGeneInfoSynonymQ;
		//String symbolsFromMtfsQ;
		
		// this qury will return a list of syns to be used as input in another genefinding query - symbolsFromSynListQ
		String synonymListQ;
		
		PreparedStatement stmt = null;
		ResultSet resSet = null;
	
	
		try {			
		    //if user wants to do a wild card search
		    if (wildcard.equalsIgnoreCase("contains") || wildcard.equalsIgnoreCase("starts with")) {
				//get components to build query to find symbols from REF_PROBE using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Symbol");
				//create sql from components and user input
				symbolsFromRefProbeSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_PROBE using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Name");
				//create sql from components and user input
				symbolsFromRefProbeNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_GENE_INFO using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Symbol");
				//create sql from components and user input
				symbolsFromrefGeneInfoSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_GENE_INFO using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Name");
				//create sql from components and user input
				symbolsFromrefGeneInfoNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbol from REF_GENE_INFO using gene synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_synonym");
				//create sql from components and user input
				symbolsFromrefGeneInfoSynonymQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find synonymns from REF_SYNONYM using synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefSyn_Synonym");
				//create sql from components and user input
				synonymListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
		    }
		    //search for an exact string
		    else {
				//get components to build query to find symbols from REF_PROBE using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Symbol");
				//create sql from components and user input
				symbolsFromRefProbeSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbols from REF_PROBE using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Name");
				//create sql from components and user input
				symbolsFromRefProbeNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbols from REF_GENE_INFO using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Symbol");
				//create sql from components and user input
				symbolsFromrefGeneInfoSymbolQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbols from REF_GENE_INFO using gene name as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Name");
				//create sql from components and user input
				symbolsFromrefGeneInfoNameQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find symbol from REF_GENE_INFO using gene synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_synonym");
				//create sql from components and user input
				symbolsFromrefGeneInfoSynonymQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find synonymns from REF_SYNONYM using synonym as a param to narrow search
				symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefSyn_Synonym");
				//create sql from components and user input
				synonymListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
		    }
		    
		    // need to execute query to get syn list here
		    String[] synList = null;
		    stmt = conn.prepareStatement(synonymListQ);
		    for(int i=0;i<input.length;i++){
				if(wildcard.equalsIgnoreCase("contains")) {
				    stmt.setString(i+1, "%"+input[i].trim()+"%");
				}
				else if(wildcard.equalsIgnoreCase("starts with")){
				    stmt.setString(i+1, input[i].trim()+"%");
				}
				else {
				    stmt.setString(i+1, input[i].trim());
				}
		    }
		    
		    if (debug)
		    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
		    
		    resSet = stmt.executeQuery();
		    if (resSet.first()) {
				resSet.last();
				int rowCount = resSet.getRow();
				resSet.beforeFirst();
				synList = new String[rowCount];
				int i = 0;
				while (resSet.next()) {
				    synList[i] = resSet.getString(1);
				    i++;
				}
		    }
		    
		    symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefMgiMrk_MGIAcc");
		    String symbolsFromMGiAccQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
		    
		    symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefEnsGene_EnsemblId");
		    String symbolsFromEnsemblIdQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
		    
		    //symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefProbe_MTFJax");
		    //symbolsFromMtfsQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
		    
		    // sligtly different query - had to get list of relevant synonyms
		    // from db to use as input for this query
		    symbolsQParts = (String[]) (AdvancedSearchDBQuery.getRefTableAndColTofindGeneSymbols()).get("RefMgiMrkRefSyn_Synonym");
		    String symbolsFromSynListQ = AdvancedSearchDBQuery.getSymbolsFromGeneInputParamsQuery(synList,symbolsQParts[0], symbolsQParts[1], 1);
		    
		    String union = AdvancedSearchDBQuery.getUnion();
		    //use 'union' to incorporate all queryies into a single query
		    String allQueriesQ = symbolsFromRefProbeSymbolQ + union
			+ symbolsFromRefProbeNameQ + union
			+ symbolsFromrefGeneInfoSymbolQ + union
			+ symbolsFromrefGeneInfoNameQ + union
			+ symbolsFromrefGeneInfoSynonymQ + union // 12/10/2009
			+ symbolsFromMGiAccQ + union 
			+ symbolsFromEnsemblIdQ;
		    if(!symbolsFromSynListQ.equals("")){
		    	allQueriesQ += union + symbolsFromSynListQ;
		    }
		    
		    
		    stmt = conn.prepareStatement(allQueriesQ);
		    
		    //for the first 4 in 'union' query, set the parameters
		    for(int i=0;i<5;i++){// xingjun - 12/10/2009 - change from 4 to 5
				for(int j=0;j<input.length;j++){
				    if(wildcard.equalsIgnoreCase("contains")) {
				    	stmt.setString((i*input.length)+j+1, "%"+input[j].trim()+"%");
				    }
				    else if(wildcard.equalsIgnoreCase("starts with")) {
				    	stmt.setString((i*input.length)+j+1, input[j].trim()+"%");
				    }
				    else {
				    	stmt.setString((i*input.length)+j+1, input[j].trim());
				    }
				    
				}			
		    }
		    //start the loop at 4 since we have already set params for the first four queries.
		    //set the params for the remaining 'union' queries
		    for(int i=5;i<7;i++){// xingjun - 12/10/2009 - change from 4 to 5 and 6 to 7 respectively
				for(int j=0;j<input.length;j++){
				    stmt.setString((i*input.length)+j+1, input[j].trim());
				}
		    }
		    //need to set additional params based on result of synonym search
		    if(synList != null) {
				for(int i = 0;i< synList.length;i++){
				    //as there are 6 previous queries, need to start setting params from 6 onwards.
				    stmt.setString((7*input.length+1+i), synList[i].trim());// xingjun - 12/10/2009 - change from 6 to 7
				}
		    }
		    
		    if (debug)
		    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+stmt.toString());
		    
		    resSet = stmt.executeQuery();
		    String str = null;
		    if(resSet.first()){
				resSet.last();
				//int rowCount = resSet.getRow();
				ArrayList<String> tmp = new ArrayList<String>();
				resSet.beforeFirst();
				//				int i = 0;
				//add result list to array
				while (resSet.next()) {
				    str = Utility.netTrim(resSet.getString(1));
				    if (null != str)
					tmp.add(str);
				}
				String [] geneSymbols = new String [tmp.size()];
				geneSymbols = tmp.toArray(geneSymbols);
				return geneSymbols;	
		    }
			return null;
		    
		} catch (SQLException e) {
		    e.printStackTrace();
			return null;
		} finally {
		    DBHelper.closeResultSet(resSet);
		    DBHelper.closePreparedStatement(stmt);
		}
    }
    
    // xingjun - 05/09/2011 - added Tissue related statement to get correct tissue value
    public String[] AssembleQuickSQL(String type, String[] input, int orderby, boolean asc, String offset, String resPerPage, int[] total){
	
    	if(input == null){
	    return null;
    	}
	
    	String inputStr = "";
        String ishStr = "";
        String micStr = "";
        String[] ish = (String[])((Hashtable)AdvancedSearchDBQuery.getISHQuickSearchColumns()).get("All");
    	String[] mic = (String[])((Hashtable)AdvancedSearchDBQuery.getMICQuickSearchColumns()).get("All");
        String[] all = new String[3];
        String micCountStr = "select count(*) from (";
	
        if(null != type && type.equals("All")) {
	    
	    String [] comps = this.getComponentsForSynonymsAndPublicIds(input);
	    
	    if(comps != null){
			String [] tmp = new String [comps.length+input.length];
			for(int i=0;i<comps.length;i++){
			    tmp[i] = comps[i];
			}
			for(int i=0;i<input.length;i++){
			    tmp[comps.length+i] = input[i];
			}
			input = tmp;
	    }
	    
	    for(int i = 0; i < input.length; i++) {
	    	inputStr = inputStr + "'"+ input[i] + "'" +",";
	    } 
	    
	    if(input.length >= 1) {
            	inputStr = inputStr.substring(0,inputStr.length()-1);
            }
	    
	    if(input.length >= 1) {
		
		inputStr = " in (" + inputStr + ") ";
		for(int i = 0; i < ish.length; i++) {
		    ishStr = ishStr + ish[i] + inputStr + " OR ";
		}
		if(ish.length >= 1) {
		    ishStr = " ( " + ishStr.substring(0,ishStr.length()-4) + " ) ";
		}
		for(int i = 0; i < mic.length; i++) {
		    micStr = micStr + AdvancedSearchDBQuery.getUnion() + decideSelect(mic[i])+//AdvancedSearchDBQuery.getMICSelectForAnatomy()+
			decideWhere(mic[i]) +
			//AdvancedSearchDBQuery.getMICFrom() + 	
			////AdvancedSearchDBQuery.getMICFromForAnatomy() + 
			////" WHERE " + 	            		
			" ( " + mic[i] + inputStr + " )) ";
		}
		
	    }       	
	    
	    String orderStr = DBHelper.orderResult(orderby, asc);
	    
	    // xingjun - 30/05/2011 - mantis 539 - need group by clause to deal with group_concat function
	    String groupByStr = "GROUP BY col10"; // QIC_SUB_ACCESSION_ID column
	    
	    all[0] = micStr.substring(AdvancedSearchDBQuery.getUnion().length()) + 
    		AdvancedSearchDBQuery.getUnion() +
    		AdvancedSearchDBQuery.getISHSelect()+
    		AdvancedSearchDBQuery.getISHFrom() + 
    		AdvancedSearchDBQuery.fromISHTissue() + // added by xingjun - 05/09/2011
    		" WHERE "+ishStr +
    		groupByStr + // xingjun - 30/05/2011 - mantis 539
    		" ) "+
    		//micStr+    		
    		orderStr 
    		+ new String(null == resPerPage?
			     " ":" limit "+offset+","+resPerPage+"  ");
	    
	    all[1] = AdvancedSearchDBQuery.getISHCount()+
    		AdvancedSearchDBQuery.getISHFrom() + " WHERE " +
    		ishStr;
	    
	    all[2] = micCountStr + 
		micStr.substring(AdvancedSearchDBQuery.getUnion().length()) +    		
    		") as tablea";
	    
	}    
	return all;
    }
    
    /** --- Quick Search --- */
    public ArrayList<String[]> getQuickSearch(String type, String[] input, int orderby, boolean asc, String offset, String resPerPage, int[] total){
        String[] query = AssembleQuickSQL(type, input, orderby, asc, offset, resPerPage, total);

		ResultSet resSet = null;
		if(null != query && null != query[0]) {
		    PreparedStatement prepStmt = null;
		    
		    try {
				prepStmt = conn.prepareStatement(query[0]);
		
				if (debug)
				    System.out.println("MySQLAdvancedQueryDAOImp.sql = "+prepStmt.toString());
		
				resSet = prepStmt.executeQuery();
				ArrayList<String[]> list = DBHelper.formatResultSetToArrayList(resSet, ColumnQuickNumbers);
				return list;

		    } catch (Exception se) {
		    	se.printStackTrace();
		    	return null;
		    }
		    finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
		    }	    	    
		}		
		return null;
    }
    
    public int[] getQuickNumberOfRows(String type, String[] input) {
    	PreparedStatement prepStmt = null;
    	ResultSet resSet = null;
    	String ish = null;
    	String mic = null;
	
    	String[] query = AssembleQuickSQL(type, input, 0, true, null, null, null);
	
    	if(null != query && null != query[0]) {
		    try {
				if(null != query[1]) {
				    prepStmt = conn.prepareStatement(query[1]);	    			
				    if (debug)
				    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+prepStmt.toString());
				    
				    resSet = prepStmt.executeQuery();
				    if(resSet.first()) {
				    	ish = resSet.getString(1);
				    	
					if (debug)
					    System.out.println("~~~ish total = "+ish);
		
				    }
				}
			
				if(null != query[2]) {
				    
				    prepStmt = conn.prepareStatement(query[2]);	  
		
				    if (debug)
				    	System.out.println("MySQLAdvancedQueryDAOImp.sql = "+prepStmt.toString());
		
				    resSet = prepStmt.executeQuery();
				    if(resSet.first()) {
						mic = resSet.getString(1);
						if (debug)
						    System.out.println("~~~mic total = "+mic);
			
					    }
				}
				int[] total = new int[]{(null == ish?0:Integer.parseInt(ish)) + (null == mic?0:Integer.parseInt(mic)),(null == ish?0:Integer.parseInt(ish)), (null == mic?0:Integer.parseInt(mic))};			
				return total; 
				
		    } catch (Exception se) {
                se.printStackTrace();
            	return new int[]{0,0,0};
            }
		    finally{
			    DBHelper.closePreparedStatement(prepStmt);
			    DBHelper.closeResultSet(resSet);
		    }
    	}	
    	return new int[]{0,0,0};
    }
    /**
     * method to return a list of database entries based on a particular type of query
     * @param type - what user wants to search on (anatomy term, gene symbol/name, accession id, gene function)  
     * @param input - user defined array of inputs
     * @param orderby - value to determine which column in the result to order the result by 
     * @param asc - do you want to order the result in ascending or descending order
     * @param offset - which result to start at
     * @param resperpage - how many entries per page to display
     * @param organ - which specific focus organ you want to narrow the results to
     * @param sub - assay types to search for (e.g. ish/microarray)
     * @param exp - what type of expression to search for (present, absent, uncertain, etc)
     * @param queryCriteria - array containing params to determine whether to search on a specific stage,
     * use 'input' param as a wildcard entry, whether to only search for annotated entries
     * @param transitiveRelations - whether to include substructures/superstructures in the search or not
     * @return result - a list of database entries to be displayed to the user determined from above params 
     */
    
    public ArrayList<String[]> getFocusQuery(String type, 
		     String[] input, int orderby, boolean asc, 
		     String offset, String resperpage, String organ,
		     String sub, String exp, String[] queryCriteria, String transitiveRelations, 
		     String archiveId, String batchId, GenericTableFilter filter) {

		if (debug) {
			System.out.println("=============getFocusQuery===============");
			System.out.println("sub1:" + sub);
			System.out.println("wildcard:" + queryCriteria[0]);
		}
		
		
		String assayValue = filter.getActiveAssay();
		if (assayValue != null){
		if(assayValue.equalsIgnoreCase("Array"))
			sub = "mic";
		if(assayValue.contains("ISH") || assayValue.contains("IHC") || assayValue.contains("TG"))
			sub = "ish";
		if((assayValue.contains("ISH") || assayValue.contains("IHC") || assayValue.contains("TG")) && assayValue.contains("Array"))
			sub = null;
		}
		
		ArrayList <String []> list = assembleSQL(type, input, 0, true, null, 
						 null, organ, sub, exp, queryCriteria, transitiveRelations, archiveId, batchId);
		
		if(list == null){
			return null;
		}
		
		String[] sql = (String [])list.get(0);
		if (debug) {
			for (int i=0;i<input.length;i++)
				System.out.println("input:" + input[i]);
		}
		
		ArrayList<String[]> result = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		
		if(null != sql && null != sql[0]) {
			try {
				if (debug)
				    System.out.println("before adding filter"+sql[0]);
			
				sql[0] = filter.addFilterSql(sql[0], null);
				if (debug)
				    System.out.println("after adding filter sql[0] = "+sql[0] );
				
				// they do not want low level QIC_ANO_COMPONENT_NAME
				// they want high level in ISH_SP_TISSUE 
				// so add this "post-process" block
				if (-1 != sql[0].indexOf("QIC_ANO_COMPONENT_NAME") ||
				    -1 != sql[0].indexOf("qic_ano_component_name")) {
				    sql[0] = sql[0].replaceAll("QIC_ANO_COMPONENT_NAME", "ANO_COMPONENT_NAME");
				    sql[0] = sql[0].replaceAll("QSC_ISH_CACHE", "QSC_ISH_CACHE "+AdvancedSearchDBQuery.fromISHTissue());
				}
			
				String orderpart = DBHelper.orderResult(orderby, asc) + 
			    new String((null == offset || offset.trim().equals(""))&& (resperpage==null || resperpage.trim().equals(""))? " ":" limit "+ offset + "," + resperpage + " ");
			
				if (debug)
				    System.out.println("orderpart = "+orderpart );
	        	
				sql[0] = "SELECT DISTINCT x.col1, GROUP_CONCAT(DISTINCT x.col2), x.col3, x.col4, x.col5, x.col6, x.col7, x.col8, x.col9, x.col10, x.col11, x.col12, x.col13, x.col14, x.col15, col16, col17 FROM ("+sql[0]+") AS x GROUP BY x.col10 " + orderpart.replaceAll("col", "x.col");

				if (debug)
				    System.out.println("sql[0] = "+sql[0] );
				
				prepStmt = conn.prepareStatement(sql[0]);
				int ishIterations = 0;
				ishIterations = Integer.parseInt(sql[3]);
			
				input = (String [])list.get(1);
				for(int i=0;i< ishIterations;i++){
				    for(int j=0;j<input.length;j++){
				    	prepStmt.setString((i*input.length)+j+1, input[j]);
				    }
				}
				
				int micIterations = 0;
				micIterations = Integer.parseInt(sql[4]);
				String []micinput = (String [])list.get(2);
			
				for(int i=ishIterations*input.length;i< ishIterations*input.length+micIterations;i++){
				    for(int j=0;j<micinput.length;j++){
				    	prepStmt.setString(i+j+1, micinput[j]);
				    }
				}
				
				if (debug)
				    System.out.println("before executing sql = "+prepStmt.toString());
				resSet = prepStmt.executeQuery();
				
				result = DBHelper.formatResultSetToArrayList(resSet, ColumnNumbers);
				return result;
		    } 
		    catch (Exception se) {
		    	se.printStackTrace();
				return null;
	        }
		    finally{
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
		    }
		}
		return null;
	}
    
    /**
     * 
     * @param type - Gene, Accession ID, Anatomy
     * @param input - user input keywords
     * @param organ
     * @param sub - ish, microarray
     * @param exp - expression strength (present, absent, unknow for ish; P, A, M for array)
     * @param queryCriteria - 3 elements: query type (contains, equals, starts with); stage (All/number); ifAnnotated (0/1)
     * @return
     */
    
    public int getNumberOfRows(String type, String[] input, String organ,
			       String sub, String exp, String[] queryCriteria, String transitiveRelations, 
			       String archiveId, String batchId, GenericTableFilter filter) {
    	
		if (debug) {
		    System.out.println("enter getNumberOfRows method!!!!!!!");
		    for (int i=0;i<input.length;i++)
			 System.out.println("input for getNumberOfRows:" + input[i]);
		}
		
		String assayValue = filter.getActiveAssay();
		if (assayValue != null){
		    if(assayValue.equalsIgnoreCase("Array"))
		    	sub = "mic";
		    if(assayValue.contains("ISH") || assayValue.contains("IHC") || assayValue.contains("TG"))
		    	sub = "ish";
		    if((assayValue.contains("ISH") || assayValue.contains("IHC") || assayValue.contains("TG")) && assayValue.contains("Array"))
		    	sub = null;
		}
		
		ArrayList <String []> list = assembleSQL(type, input, 0, true, null, null, organ, sub, exp, queryCriteria, transitiveRelations, archiveId, batchId);
		if(list == null){
		    return 0;
		}
    	String[] sql = (String [])list.get(0);
	
    	ResultSet resSet = null;
    	PreparedStatement prepStmt = null;
    	String ish = null;
    	String mic = null;
	
    	if(null != sql) {
		    try {
				if(null != sql[1]) {
				    sql[1] = filter.addFilterCountSql(sql[1]);
				    prepStmt = conn.prepareStatement(sql[1]);
		
				    int iterations = 0;
				    iterations = Integer.parseInt(sql[3]);
				    input = (String [])list.get(1);
				    for(int i=0;i< iterations;i++){
						for(int j=0;j<input.length;j++){
						    prepStmt.setString((i*input.length)+j+1, input[j]);
						}
				    }
				    if (debug)
				    	System.out.println("MySQLAdvancedQueryDAOImp:getNumberOfRows1-prepStmt= "+prepStmt.toString());
				    
				    resSet = prepStmt.executeQuery();
				    if(resSet.first()) {
				    	ish = resSet.getString(1);
						if (debug)
						    System.out.println("ISH number = "+ ish);
				    }
				}
			
				if(null != sql[2]) {
				    sql[2] = filter.addFilterCountSql(sql[2]);
				    prepStmt = conn.prepareStatement(sql[2]);
				    int iterations = 0;
				    iterations = Integer.parseInt(sql[4]);
				    
				    input = (String [])list.get(2);
				    for(int i=0;i < iterations;i++){
						for(int j=0;j<input.length;j++){
						    prepStmt.setString((i*input.length)+j+1, input[j]);
						}
				    }
				    if (debug)
				    	System.out.println("MySQLAdvancedQueryDAOImp:getNumberOfRows2-prepStmt= "+prepStmt.toString());
				    
				    resSet = prepStmt.executeQuery();
				    if(resSet.first()) {
						mic = resSet.getString(1);
						if (debug)
						    System.out.println("MIC number = "+ mic);
				    }				    
				}
				int total = (null == ish?0:Integer.parseInt(ish)) + (null == mic?0:Integer.parseInt(mic));
				return total;
			
		    } 
		    catch (Exception se) {
                se.printStackTrace();
               	return 0;
	        }
		    finally {
		    	DBHelper.closePreparedStatement(prepStmt);
		    	DBHelper.closeResultSet(resSet);
		    }
    	}
		if (debug)
		    System.out.println("getNumberOfRows@total number: " + 0);
    	return 0;
    }
    
    /**
     * @author xingjun
     * at moment the result we return for total number of ish and microarray are hard coded (copy from ying's code).
     * if new assay type data come, need change the code. considering modify the code to make it more flexible
     * @param type - Gene, Accession ID, Anatomy
     * @param input - user input keywords
     * @param organ
     * @param sub - ish, microarray
     * @param exp - expression strength (present, absent, unknow for ish; P, A, M for array)
     * @param queryCriteria - 3 elements: query type (contains, equals, starts with); stage (All/number); ifAnnotated (0/1)
     * @return
     */
    
    public int[] getNumberOfRowsInGroups(String type, String[] input, String organ,
					 String sub, String exp, String[] queryCriteria, String transitiveRelations, 
					 String archiveId, String batchId, GenericTableFilter filter) {
    	
		if (debug)
		    System.out.println("enter getNumberOfRowsInGroups method!!!!!!!");
		
		String assayValue = filter.getActiveAssay();
		if (assayValue != null){
		    if(assayValue.equalsIgnoreCase("Array"))
		    	sub = "mic";
		    if(assayValue.contains("ISH") || assayValue.contains("IHC") || assayValue.contains("TG"))
		    	sub = "ish";
		    if((assayValue.contains("ISH") || assayValue.contains("IHC") || assayValue.contains("TG")) && assayValue.contains("Array"))
		    	sub = null;
		}
	
		ArrayList <String []> list = assembleSQL(type, input, 0, true, null, null, organ, sub, exp, queryCriteria, transitiveRelations, archiveId, batchId); 
		
		if(list == null){
		    return new int[]{0, 0};
		}
	
    	String[] sql = (String [])list.get(0);
    	ResultSet resSet = null;
    	PreparedStatement prepStmt = null;
    	String ish = null;
    	String mic = null;
	
    	if(null != sql) {
		    try {
				if(null != sql[1]) {
				    String ishsql = filter.addFilterCountSql(sql[1]);
		
				    prepStmt = conn.prepareStatement(ishsql);
				    
				    int iterations = 0;
				    iterations = Integer.parseInt(sql[3]);
				    input = (String [])list.get(1);
				    for(int i=0;i< iterations;i++){
						for(int j=0;j<input.length;j++){
						    prepStmt.setString((i*input.length)+j+1, input[j]);
						}
				    }
				    if (debug)
			    		System.out.println("MySQLAdvancedQueryDAOImp:getNumberOfRowsInGroups1 - prepStmt= "+prepStmt.toString());
				    resSet = prepStmt.executeQuery();
				    if(resSet.first()) {
						ish = resSet.getString(1);
						if (debug)
						    System.out.println("~~~ish total = "+ish);
				    }
				}
			
				if(null != sql[2]) {
				    sql[2] = filter.addFilterCountSql(sql[2]);
		
				    prepStmt = conn.prepareStatement(sql[2]);	    					    
				    int iterations = 0;
				    iterations = Integer.parseInt(sql[4]);
				    
				    input = (String [])list.get(2);
				    for(int i=0;i < iterations;i++){
						for(int j=0;j<input.length;j++){
						    prepStmt.setString((i*input.length)+j+1, input[j]);
						}
				    }
				    if (debug)
			    		System.out.println("MySQLAdvancedQueryDAOImp:getNumberOfRowsInGroups2 - prepStmt= "+prepStmt.toString());
				    
				    resSet = prepStmt.executeQuery();
				    if(resSet.first()) {
						mic = resSet.getString(1);
						if (debug)
						    System.out.println("~~~mic total = "+mic);
				    }
				    
				}
				int[] total = new int[]{(null == ish?0:Integer.parseInt(ish)), (null == mic?0:Integer.parseInt(mic))};			
				return total;    			
		    } 
		    catch (Exception se) {
	            se.printStackTrace();
	         	return new int[]{0, 0};
	        }
		    finally {
		    	DBHelper.closePreparedStatement(prepStmt);
		    	DBHelper.closeResultSet(resSet);
		    }
    	}
    	return new int[]{0, 0};
    } // end of getNumberOfRowsInGroups
    
    /**
     * overload of assembleSQL
     * 
     * method to return array of sql strings (e.g. query to find number of results in query, query to find ish/mic entries) 
     * and array of parameters to be added to sql to specify search
     * <p>modified by xingjun - 04/05/2009  
     *    - added criteria 'queryCriteria[2] != null' in 3 places to avoid NullPointerException </p>
     * <p>modified by xingjun - 14/07/2009 - deal with apostrophe character(s) - see ###140709### tag </p> 
     * @param queryType - what user wants to search on (anatomy term, gene symbol/name, accession id, gene function)
     * @param input - user defined array of inputs
     * @param orderby - value to determine which column in the result to order the result by
     * @param asc - do you want to order the result in ascending or descending order
     * @param offset - which result to start at
     * @param recordNumber - how many entries per page to display
     * @param organ - which specific focus organ you want to narrow the results to
     * @param subType - assay types to search for (e.g. ish/microarray)
     * @param expStrength - what type of expression to search for (present, absent, uncertain, etc)
     * @param queryCriteria- array containing params to determine whether to search on a specific stage,
     * use 'input' param as a wildcard entry, whether to only search for annotated entries
     * @param transitiveRelations - whether to include sub/super-structures in the search or not
     * @return array of sql string and array of corresponding parameters to be incorporated into sql
     */
    public ArrayList<String[]> assembleSQL(String queryType, String[] input, 
					   int orderby, boolean asc, String offset, String recordNumber, 
					   String organ, String subType, String expStrength, String[] queryCriteria, String transitiveRelations, String archiveId, String batchId) {

    	// TODO in order to use archiveId and batchId as part of url we need to add the archiveID and batchID to the cache tables in the database
    	// this has still to be implemented, so the updated sql has not been added to this method yet. - Bernie
    	
    	
    	if (debug) {
		    System.out.println("assembleSQL############");
		    System.out.println("AssembleSQLForFocusQuery:queryType: " + queryType);
		    System.out.println("AssembleSQLForFocusQuery:subType: " + subType);
		    System.out.println("AssembleSQLForFocusQuery:transitiveRelations: " + transitiveRelations);
		    System.out.println("AssembleSQLForFocusQuery:organ: " + organ);
		    System.out.println("AssembleSQLForFocusQuery:archiveId: " + archiveId);
		    System.out.println("AssembleSQLForFocusQuery:batchId: " + batchId);
		    for (int i=0;i<input.length;i++) 
		    	System.out.println("AdvancedQueryDAO:assembleSQL:input( " + i + "): " + input[i]); 
		    for (int i=0;i<queryCriteria.length;i++) 
		    	System.out.println("AdvancedQueryDAO:assembleSQL:queryCriteria( " + i + "): " + queryCriteria[i]);
		}
	
    	ArrayList<String[]> sqlAndParams = new ArrayList <String[]>();
	
    	if (input == null) { // user didn't provide any keyword
    		return null;
    	}
	
    	//need to remove strings containing only white space or empty strings from array
    	ArrayList<String> tmpList = new ArrayList<String>();
    	for(int i = 0;i<input.length;i++) {
		    if(input[i]!= null && input[i].trim().length() >= 1){
		    	tmpList.add(input[i].trim());
		    }
    	}
    	//put the new values back into the input array
    	input = new String[tmpList.size()];
    	for(int i=0;i<input.length;i++){
    		input[i] = tmpList.get(i);
    	}
	
    	String inputStr = "";  //contains list of input parameters or '?' for each input parameter
        String ishStr = "";
        String micStr = "";
        String[] all = null;
        String[] ish = (String[])((Hashtable)AdvancedSearchDBQuery.getISHQuickSearchColumns()).get(queryType);
    	String[] mic = (String[])((Hashtable)AdvancedSearchDBQuery.getMICQuickSearchColumns()).get(queryType);
    	String countStr = "select count(distinct col13) from ";
    	String countMicStr = "select count(distinct col10) from ";
    	String organISHStr = "";
    	String organMicStr = "";
    	String ishStageString = "";
    	String micStageString = "";
    	String ishAnnotationString = "";
    	String micAnnotationString = "";
	
    	//specific focus organ has been selected so sql will have to be modified accordingly
    	if(null != organ && !organ.equals("") && !organ.equals("null")) {
		    if (debug)
	    		System.out.println("OUTPUT ORGAN:"+organ+":"+organ);
		    String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
		    String ids = "";
		    if(null != emapids) {
				for(int i = 0; i < emapids.length; i++) {
				    ids += "'"+emapids[i] + "',";
				}
				if(emapids.length >= 1) {
				    ids = ids.substring(0, ids.length()-1);
				}
				String temp = "in (select QAI_COMPONENT_ID from QSC_ANA_INDEX where QAI_ORGAN_KEY = " + organ + ") ";
				organISHStr = " AND QIC_ATN_PUBLIC_ID " + temp;
				organMicStr = " AND QMC_ATN_PUBLIC_ID " + temp;
		    }
    	}
	
    	//specific assay type has been selected so need to make relevant modification to sql
		if(null != expStrength) {
		    if(expStrength.equals("present")) {
		    	organISHStr += " AND QIC_EXP_STRENGTH='present' ";
		    } else if (expStrength.equals("absent")) {
		    	organISHStr += " AND QIC_EXP_STRENGTH='not detected' ";
		    } else if (expStrength.equals("unknown")) {
		    	organISHStr += " AND QIC_EXP_STRENGTH not in ('not detected', 'present') ";
		    } else { 
		    	organISHStr += " AND QIC_EXP_STRENGTH='uncertain' ";
		    }
		    
		    if(expStrength.equals("P")) {
		    	organMicStr += " AND MBC_GLI_DETECTION='P' ";
		    } else if (expStrength.equals("A")) {
		    	organMicStr += " AND MBC_GLI_DETECTION='A' ";
		    } else if (expStrength.equals("M")){
		    	organMicStr += " AND MBC_GLI_DETECTION='M' ";
		    }
		}
	
    	//user query is based on gene query
		if(queryType.equals("Gene")) {
		    //need to set query criteria params if none specified by user
		    if(null == queryCriteria) {
				queryCriteria = new String[3];
				queryCriteria[0] = "equals";
				queryCriteria[1] = "All";
				queryCriteria[2] = "0";
		    }
		    // need to modify later to a mixture of input including gene symbols and MTFs - xingjun - 22/10/2010
		    //value to determine whether user wants to search for MTFs in db
		    int findMTFs = -1;
		    //if the user is only going to search for all MTFs
		    if(input.length == 1 && input[0] != null && input[0].trim().equalsIgnoreCase("MTF#")){
				input = new String[0];
				findMTFs = 0;
		    }
		    //else if the user wants to search for more that 1 MTF
		    else if(input.length >= 1 && input[0] != null && input[0].toUpperCase().indexOf("MTF#") == 0){
				findMTFs = 1;
				ish = new String[0];
		    }
		    else {
				//change input to the result of query to find all symbols related to users original input strings  
				input = this.getSymbolsFromGeneInputParams(input, queryCriteria[0]);	
		    }
	    
		    if(input == null){ //no gene symbols have been found based on users input
		    	return null;
		    }
	    
		    // assemble stage string (only needs to be done if specific stage has been entered by user)
		    if (queryCriteria[1] != null && !queryCriteria[1].equalsIgnoreCase("All")) {
				ishStageString = " AND QIC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
				micStageString = " AND QMC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
		    }
	    
		    // assemble annotation string - user is only looking for genes with expression annotation
		    if (queryCriteria[2] != null && queryCriteria[2].equalsIgnoreCase("1")) {
				ishAnnotationString = " AND LENGTH(QIC_ATN_PUBLIC_ID) > 0 ";
				micAnnotationString = " AND LENGTH(QMC_ATN_PUBLIC_ID) > 0 ";
		    }
	    
		    //set '?' in query as param for each input
		    for(int i = 0; i < input.length; i++) {
				if(i == input.length-1){
				    inputStr = inputStr + "?";
				}
				else {
				    inputStr = inputStr + " ?,";
				}
		    }
	    
		    //if the number of inputs is at least 1
		    if(input.length >= 1) {
				inputStr = " in (" + inputStr + ") ";
				for(int i = 0; i < ish.length; i++) {
				    if(i == ish.length-1){
				    	ishStr = ishStr + ish[i] + inputStr;
				    }
				    else {
				    	ishStr = ishStr + ish[i] + inputStr + " OR ";
				    }
				}
		
				for(int i = 0; i < mic.length; i++) {
				    micStr = micStr + AdvancedSearchDBQuery.getMICSelectForMerhan()+
					AdvancedSearchDBQuery.getMICFrom() +
					//added for organ
					organMicStr + " AND ( " + mic[i] + inputStr + ")";
				    if(i < mic.length-1){
				    	micStr += AdvancedSearchDBQuery.getUnion();
				    }
				    
				}
		    }
	    
		    String mtfStr = "";
	    
		    //how do you want to order the result
		    String orderStr = DBHelper.orderResult(orderby, asc);
		    all = new String[5];        	
	    
		    //ishpart - string to contain the query to interrogate the ish cache tables
		    // Bernie 26/10/2010 - added AdvancedSearchDBQuery.fromISHTissue() to return tissue values
		    String ishpart = 
			AdvancedSearchDBQuery.getISHSelect() + AdvancedSearchDBQuery.getISHFrom() + AdvancedSearchDBQuery.fromISHTissue() + " WHERE "+ ishStr;  
		    
		    if(findMTFs == 0){
				mtfStr = " QIC_RPR_MTF_JAX LIKE 'MTF#%' ";
				ishpart += mtfStr;
		    }
		    else if(findMTFs == 1){
				mtfStr = " QIC_RPR_MTF_JAX " + inputStr;
				ishpart += mtfStr;
		    }
		    //Bernie 27/10/2010 - added groupart to return tissue values
		    String grouppart = "GROUP BY col10";
		    
		    //added for organ
		    ishpart += organISHStr + ishStageString + ishAnnotationString + grouppart + ") ";
	    
		    //micpart - string to contain the query to interrogate the microarray cache tables
		    String micpart = micStr + micStageString + micAnnotationString + ")";
		    
		    String orderpart = orderStr + 
	        	new String((null == offset || offset.equals(""))&& (recordNumber==null || recordNumber.equals(""))? " ":" limit "+ offset + "," + recordNumber + " ");
	    
		    if(null != subType) {
				if(subType.equals("ish")) {
				    all[0] = ishpart + orderpart;
				    all[1] = AdvancedSearchDBQuery.getISHCount() + AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
		            		//added for organ
		            		organISHStr + ishStageString + ishAnnotationString;
				    all[2] = null;
				    all[3] = String.valueOf(ish.length);  //how many times you have to cycle through the list of inputs to set the params in the sql
				    all[4] = "0";  //how many times you have to cycle through the list of inputs to set the params in the sql
				} else if(subType.equals("mic")) {
		    
				    //if user is looking for mtfs but has specified array assay types
				    if(findMTFs > -1)
					return null;
		    
				    all[0] = micpart + orderpart;
				    all[1] = null;
				    all[2] = countMicStr + micStr + micStageString + micAnnotationString + ") as tablea";
				    all[3] = "0"; //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				    all[4] = String.valueOf(mic.length); //how many times you have to cycle through the list of inputs to set the params in the sql for array
				}
		    } else {
		
				if(findMTFs > -1) {
				    all[0] = ishpart + orderpart;
				    all[1] = AdvancedSearchDBQuery.getISHCount()+ AdvancedSearchDBQuery.getISHFrom() +" WHERE " + mtfStr + 
		            		//added for organ
		            		organISHStr + ishStageString + ishAnnotationString;
				    all[2] = null;
				    if(findMTFs == 0)
					all[3] = "0";  //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				    else {
					all[3] = "1";
				    }
				    all[4] = "0";  //how many times you have to cycle through the list of inputs to set the params in the sql for array
				}
				else {
				    all[0] = ishpart + AdvancedSearchDBQuery.getUnion() + micpart + orderpart;
				    all[1] = AdvancedSearchDBQuery.getISHCount()+ AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
		            		//added for organ
		            		organISHStr + ishStageString + ishAnnotationString;
				    all[2] = countMicStr + micStr + micStageString + micAnnotationString + ") as tablea";
				    all[3] = String.valueOf(ish.length); //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				    all[4] = String.valueOf(mic.length); //how many times you have to cycle through the list of inputs to set the params in the sql for array
				}
		    }
		    sqlAndParams.add(all);
		    sqlAndParams.add(input);
		    sqlAndParams.add(input);
		    
		}
		//if user is searching for gene symbols
		else if (queryType.equalsIgnoreCase("GeneSymbol")){
		    //need to set query criteria params if none specified by user
		    if(null == queryCriteria) {
				queryCriteria = new String[3];
				queryCriteria[0] = "equals";
				queryCriteria[1] = "All";
				queryCriteria[2] = "0";
		    }
	    
		    // assemble stage string (only needs to be done if specific stage has been entered by user)
		    if (queryCriteria[1] != null && !queryCriteria[1].equalsIgnoreCase("All")) {
				//stageString = " AND QSC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";// not sure which table does the column come from: need to test - xingjun - 05/12/2007
				ishStageString = " AND QIC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
				micStageString = " AND QMC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
		    }
	    
		    // assemble annotation string - user is only looking for genes with expression annotation
		    if (queryCriteria[2] != null && queryCriteria[2].equalsIgnoreCase("1")) {
				//annotationString = " AND LENGTH(QSC_ATN_PUBLIC_ID) > 0 ";// not sure which table does the column come from: need to test - xingjun - 05/12/2007
				ishAnnotationString = " AND LENGTH(QIC_ATN_PUBLIC_ID) > 0 ";
				micAnnotationString = " AND LENGTH(QMC_ATN_PUBLIC_ID) > 0 ";
		    }
		    
		    //set '?' in query as param for each input
		    for(int i = 0; i < input.length; i++) {
				if(i == input.length-1){
				    inputStr = inputStr + "?";
				}
				else {
				    inputStr = inputStr + " ?,";
				}
		    }
	    
		    //if the number of inputs is at least 1
		    if(input.length >= 1) {
				inputStr = " in (" + inputStr + ") ";
				for(int i = 0; i < ish.length; i++) {
				    if(i == ish.length-1){
				    	ishStr = ishStr + ish[i] + inputStr;
				    }
				    else {
				    	ishStr = ishStr + ish[i] + inputStr + " OR ";
				    }
				}
				
				for(int i = 0; i < mic.length; i++) {
				    micStr = micStr + AdvancedSearchDBQuery.getMICSelectForMerhan()+
					AdvancedSearchDBQuery.getMICFrom() +
					//added for organ
					organMicStr + " AND ( " + mic[i] + inputStr + ")";
				    if(i < mic.length-1){
				    	micStr += AdvancedSearchDBQuery.getUnion();
				    }
				    
				}
		    }
		    //Bernie 27/10/2010 - added groupart to return tissue values
		    String grouppart = "GROUP BY col10";
		    
		    //how do you want to order the result
		    String orderStr = DBHelper.orderResult(orderby, asc);
		    all = new String[5];        	
		    
		    //ishpart - string to contain the query to interrogate the ish cache tables
		    String ishpart = 
			AdvancedSearchDBQuery.getISHSelect() + AdvancedSearchDBQuery.getISHFrom() + AdvancedSearchDBQuery.fromISHTissue() + " WHERE "+ ishStr;
		    
		    //added for organ
		    ishpart += organISHStr + ishStageString + ishAnnotationString + grouppart + ") ";
		    
		    //micpart - string to contain the query to interrogate the microarray cache tables
		    String micpart = micStr + micStageString + micAnnotationString + ")";
		    
		    String orderpart = orderStr + 
	        	new String((null == offset || offset.equals(""))&& (recordNumber==null || recordNumber.equals(""))? " ":" limit "+ offset + "," + recordNumber + " ");
	    
		    if(null != subType) {
				if(subType.equals("ish")) {
				    all[0] = ishpart + orderpart;
				    all[1] = AdvancedSearchDBQuery.getISHCount() + AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
		            		//added for organ
		            		organISHStr + ishStageString + ishAnnotationString;
				    all[2] = null;
				    all[3] = String.valueOf(ish.length);  //how many times you have to cycle through the list of inputs to set the params in the sql
				    all[4] = "0";  //how many times you have to cycle through the list of inputs to set the params in the sql
				} else if(subType.equals("mic")) {
				    
				    all[0] = micpart + orderpart;
				    all[1] = null;
				    all[2] = countMicStr + micStr + micStageString + micAnnotationString + ") as tablea";
				    all[3] = "0"; //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				    all[4] = String.valueOf(mic.length); //how many times you have to cycle through the list of inputs to set the params in the sql for array
				}
		    } else {
				all[0] = ishpart + AdvancedSearchDBQuery.getUnion() + micpart + orderpart;
	            all[1] = AdvancedSearchDBQuery.getISHCount()+ AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
			    //added for organ
			    organISHStr + ishStageString + ishAnnotationString;
	        	all[2] = countMicStr + micStr + micStageString + micAnnotationString + ") as tablea";
	        	all[3] = String.valueOf(ish.length); //how many times you have to cycle through the list of inputs to set the params in the sql for ish
	        	all[4] = String.valueOf(mic.length); //how many times you have to cycle through the list of inputs to set the params in the sql for array
			
		    }
		    
		    sqlAndParams.add(all);
		    sqlAndParams.add(input);
		    sqlAndParams.add(input);
	    
		}
		else if (queryType.equalsIgnoreCase("Gene Function")){
		    //need to set query criteria params if none specified by user
		    if(null == queryCriteria) {
				queryCriteria = new String[3];
				queryCriteria[0] = "equals";
				queryCriteria[1] = "All";
				queryCriteria[2] = "0";
		    }
		    //change input to the result of query to find all symbols related to users original input strings
		    input = this.getSymbolsFromGeneFunctionInputParams(input, queryCriteria[0]);
		    
		    if(input == null){ //no gene symbols have been found based on users input
		    	return null;
		    }
	    
		    // assemble stage string (only needs to be done if specific stage has been entered by user)
		    if (queryCriteria[1] != null && !queryCriteria[1].equalsIgnoreCase("All")) {
				//stageString = " AND QSC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";// not sure which table does the column come from: need to test - xingjun - 05/12/2007
				ishStageString = " AND QIC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
				micStageString = " AND QMC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
		    }
		    
		    // assemble annotation string - user is only looking for genes with expression annotation
		    if (queryCriteria[2] != null && queryCriteria[2].equalsIgnoreCase("1")) {
				//annotationString = " AND LENGTH(QSC_ATN_PUBLIC_ID) > 0 ";// not sure which table does the column come from: need to test - xingjun - 05/12/2007
				ishAnnotationString = " AND LENGTH(QIC_ATN_PUBLIC_ID) > 0 ";
				micAnnotationString = " AND LENGTH(QMC_ATN_PUBLIC_ID) > 0 ";
		    }
		    
		    //set '?' in query as param for each input
		    inputStr = this.formatParamsForFocusQuery(input);
	    
		    //if the number of inputs is at least 1
		    if(input.length >= 1) {
				inputStr = " in (" + inputStr + ") ";
				for(int i = 0; i < ish.length; i++) {
				    if(i == ish.length-1){
				    	ishStr = ishStr + ish[i] + inputStr;
				    }
				    else {
				    	ishStr = ishStr + ish[i] + inputStr + " OR ";
				    }
				}
		
				for(int i = 0; i < mic.length; i++) {
				    micStr = micStr + AdvancedSearchDBQuery.getMICSelectForMerhan()+
					AdvancedSearchDBQuery.getMICFrom() +
					//added for organ
					organMicStr + " AND ( " + mic[i] + inputStr + ")";
				    if(i < mic.length-1){
				    	micStr += AdvancedSearchDBQuery.getUnion();
				    }
			    
				}
		    }
		    //Bernie 27/10/2010 - added groupart to return tissue values
		    String grouppart = "GROUP BY col10";
		    
		    //how do you want to order the result
		    String orderStr = DBHelper.orderResult(orderby, asc);
		    all = new String[5];        	
		    
		    //ishpart - string to contain the query to interrogate the ish cache tables
		    String ishpart = 
			AdvancedSearchDBQuery.getISHSelect() + AdvancedSearchDBQuery.getISHFrom() + AdvancedSearchDBQuery.fromISHTissue() + " WHERE "+ ishStr;
	    
		    //added for organ
		    ishpart += organISHStr + ishStageString + ishAnnotationString + grouppart + ") ";
		    
		    //micpart - string to contain the query to interrogate the microarray cache tables
		    String micpart = micStr + micStageString + micAnnotationString + ")";
		    
		    String orderpart = orderStr + 
	        	new String((null == offset || offset.equals(""))&& (recordNumber==null || recordNumber.equals(""))? " ":" limit "+ offset + "," + recordNumber + " ");
		    
		    /*TEMP CODE - ONLY FINDS ISH RESULTS. IGNORES ARRAY DATA*/
		    all[0] = "(" + ishpart + orderpart + ")";
		    all[1] = AdvancedSearchDBQuery.getISHCount() + AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
	    		//added for organ
	    		organISHStr + ishStageString + ishAnnotationString;
		    all[2] = null;
		    all[3] = String.valueOf(ish.length);  //how many times you have to cycle through the list of inputs to set the params in the sql
		    all[4] = "0";  //how many times you have to cycle through the list of inputs to set the params in the sql
		    /*END TEMP CODE*/
	    
		    sqlAndParams.add(all);
		    sqlAndParams.add(input);
		    sqlAndParams.add(input);
		    
		}
		//if user is searching for a specific accession id
		else if (queryType.equals("Accession ID")) {
	    
	    
		    //set '?' in query as param for each input
		    for(int i = 0; i < input.length; i++) {
				if(i == input.length-1){
				    inputStr = inputStr + "?";
				}
				else {
				    inputStr = inputStr + " ?,";
				}
		    }
	    
		    /*for(int i = 0; i < input.length; i++) {
		      inputStr = inputStr + "'"+ input[i] + "'" +",";
		      } 
		      
		      if(input.length >= 1) {
		      inputStr = inputStr.substring(0,inputStr.length()-1);
		      }*/
	    
		    if(input.length >= 1) {
				inputStr = " in (" + inputStr + ") ";
				
				ishStr = " ( ";
				for(int i = 0; i < ish.length; i++) {
				    if(i == ish.length-1){
				    	ishStr = ishStr + ish[i] + inputStr;
				    }
				    else {
				    	ishStr = ishStr + ish[i] + inputStr + " OR ";
				    }
				}
				ishStr += " ) ";
		
				for(int i = 0; i < mic.length; i++) {
				    micStr = micStr + AdvancedSearchDBQuery.getMICSelectForMerhan()+
					AdvancedSearchDBQuery.getMICFrom() +
					//added for organ
					organMicStr + " AND ( " + mic[i] + inputStr + ")";
				    if(i < mic.length-1){
				    	micStr += AdvancedSearchDBQuery.getUnion();
				    }
				    
				}
		    }
		    //Bernie 27/10/2010 - added groupart to return tissue values
		    String grouppart = "GROUP BY col10";
		    
		    String orderStr = DBHelper.orderResult(orderby, asc);
		    all = new String[5];        	
		    
		    String ishpart = AdvancedSearchDBQuery.getISHSelect()+
	    		AdvancedSearchDBQuery.getISHFrom() + AdvancedSearchDBQuery.fromISHTissue() +
	    		" WHERE "+ishStr + 
	    		//added for organ
	    		organISHStr + grouppart + " ) ";
		    String micpart = micStr +")";
		    String orderpart = orderStr + 
	        	new String((null == offset || offset.equals(""))&& (recordNumber==null || recordNumber.equals(""))? " ":" limit "+ offset + "," + recordNumber + " ");
	    
		    if(null != subType) {
				if(subType.equals("ish")) {
				    all[0] = ishpart + orderpart;
				    all[1] = AdvancedSearchDBQuery.getISHCount()+
		            		AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
		            		//added for organ
		            		organISHStr;
				    all[2] = null;
				    all[3] = String.valueOf(ish.length); //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				    all[4] = "0"; //how many times you have to cycle through the list of inputs to set the params in the sql for array
				} else if(subType.equals("mic")) {
				    all[0] = micpart + orderpart;
				    all[1] = null;
				    all[2] = countMicStr + micStr + ") as tablea";
				    all[3] = "0"; //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				    all[4] = String.valueOf(mic.length); //how many times you have to cycle through the list of inputs to set the params in the sql for array
				}
		    } else {
				all[0] = ishpart + AdvancedSearchDBQuery.getUnion() + micpart +
				    //micStr+
				    orderpart;
				
				all[1] = AdvancedSearchDBQuery.getISHCount()+
				    AdvancedSearchDBQuery.getISHFrom() + " WHERE " + ishStr + 
				    //added for organ
				    organISHStr;
				
				all[2] = countMicStr + micStr + ") as tablea";
				all[3] = String.valueOf(ish.length); //how many times you have to cycle through the list of inputs to set the params in the sql for ish
				all[4] = String.valueOf(mic.length); //how many times you have to cycle through the list of inputs to set the params in the sql for array
			
		    }

		    sqlAndParams.add(all);
		    sqlAndParams.add(input);
		    sqlAndParams.add(input);
		    
		}
		//else if the user is looking for data on an anatomy component
		else if(queryType.equals("Anatomy")) {
		    
		    ///////////////////////////////////////////////
		    //need to set query criteria params if none specified by user
		    if(null == queryCriteria) {
				queryCriteria = new String[3];
				queryCriteria[0] = "equals";
				queryCriteria[1] = "All";
				queryCriteria[2] = "0";
		    }
	    
		    // assemble stage string (only needs to be done if specific stage has been entered by user)
		    if (queryCriteria[1] != null && !queryCriteria[1].equalsIgnoreCase("All")) {		
				ishStageString = "QIC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
				micStageString = "QMC_SUB_EMBRYO_STG = '" + queryCriteria[1] + "' ";
		    }
		    /////////////////////////////////////////////
			
			//contains the ish part of the query
			StringBuffer ishQuery = new StringBuffer("");
			//contains the array part of the query
			StringBuffer micQuery = new StringBuffer("");
			
			//contains params for the ish query
			ArrayList <String[]> ishParamsContainer = new ArrayList<String[]>();
			//contains params for the mic query
			ArrayList <String[]> micParamsContainer = new ArrayList<String[]>();
			
			int totalIshComps = 0;
			int totalMicComps = 0;
		
			//for each user-specified input
			String worker = null;
			String inputString = null;
		
			for(int i=0;i<input.length;i++){
			    inputString = Utility.normaliseApostrophe(input[i]);
			    worker = AdvancedSearchDBQuery.getISHSelectForAnatomy(inputString) + AdvancedSearchDBQuery.getISHFrom()+" WHERE ";
			    //			    worker = AdvancedSearchDBQuery.getISHSelectForAnatomy(inputString) + AdvancedSearchDBQuery.getISHFrom()+AdvancedSearchDBQuery.fromISHTissue()+" WHERE ";
			    
			    //get a list of emap components based on the single input of the user
			    String [] comps = this.getTimedComponentIdsFromInput(input[i]);
			    //contains list of descendents of values in 'comps'
			    String [] subComps = null;
			    //contains list of ancestors of values in 'comps'
			    String [] superComps = null;
			    
			    //if no EMAP components were found, dont continue with the query constructions for this input element
			    if(comps!= null){
					//strings to contain queries including sub and super components of user input in searches
					StringBuffer ishPresQuery = new StringBuffer("");
					StringBuffer ishNotDetectedQuery = new StringBuffer("");
					StringBuffer ishPossibleQuery = new StringBuffer("");
					//					StringBuffer ishUncertainQuery = new StringBuffer("");
				
					//if the user only wants to find direct annotation, don't find the transitive Relations of the input
					if(transitiveRelations != null && transitiveRelations.equals("0")) {
					    subComps = comps;
					    superComps = comps;
					} else {
					    subComps = this.getTransitiveRelations(comps, "descendent");
					    superComps = this.getTransitiveRelations(comps, "ancestor");
					}
				
					//param values for subcomponents query
					StringBuffer subParamsString = new StringBuffer(" in (");
					for(int j=0;j<subComps.length;j++){
					    if(j == subComps.length-1){
					    	subParamsString.append("?");
					    }
					    else {
					    	subParamsString.append(" ?,");
					    }
					}
					subParamsString.append(")");
				
					//param values for super components query
					StringBuffer superParamsString = new StringBuffer(" in (");
					for(int j=0;j<superComps.length;j++){
					    if(j == superComps.length-1){
					    	superParamsString.append("?");
					    }
					    else {
					    	superParamsString.append(" ?,");
					    }
					}
					superParamsString.append(")");
				
					//param values for direct annotations
					StringBuffer paramsString = new StringBuffer(" in (");
					for(int j=0;j<comps.length;j++){
					    if(j == comps.length-1){
					    	paramsString.append("?");
					    }
					    else {
					    	paramsString.append(" ?,");
					    }
					}
					paramsString.append(")");
				
					String [] allComps = null;
					if(expStrength == null) {
					    totalIshComps += subComps.length + superComps.length + comps.length;
					    allComps = new String [subComps.length + superComps.length+ comps.length];
					    
					    int allIndex = 0;
					    for(int j=0;j<subComps.length;j++){
							allComps[allIndex] = subComps[j];
							allIndex++;
					    }
					    for(int j=0;j<superComps.length;j++){
							allComps[allIndex] = superComps[j];
							allIndex++;
					    }
					    for(int j=0;j<comps.length;j++){
							allComps[allIndex] = comps[j];
							allIndex++;
					    }
					    
					    //build the queries
					    
					    ishPresQuery.append(worker);
					    ishNotDetectedQuery.append(AdvancedSearchDBQuery.getUnion()+worker);
					    ishPossibleQuery.append(AdvancedSearchDBQuery.getUnion()+worker);
					    
					    if (!ishStageString.equals("")) {
							ishPresQuery.append(ishStageString + "AND ");
							ishNotDetectedQuery.append(ishStageString + "AND ");
							ishPossibleQuery.append(ishStageString + "AND ");
					    }
				    
					    for(int j=0;j<ish.length;j++){
							ishPresQuery.append(" (");
							if(j == 0) {
							    ishPresQuery.append(ish[j] + subParamsString);
							} else {
							    ishPresQuery.append(" OR "+ish[j] + subParamsString);
							}
							ishPresQuery.append(") ");
					    }
					    ishPresQuery.append(" AND QIC_EXP_STRENGTH='present' ) ");
				    
				    
					    for(int j=0;j<ish.length;j++){
							ishNotDetectedQuery.append(" (");
							if(j == 0) {
							    ishNotDetectedQuery.append(ish[j] + superParamsString.toString());
							}
							else {
							    ishNotDetectedQuery.append(" OR "+ish[j] + superParamsString.toString());
							}
							ishNotDetectedQuery.append(") ");
					    }
					    ishNotDetectedQuery.append(" AND QIC_EXP_STRENGTH='not detected') ");
				    
					    for(int j=0;j<ish.length;j++){
							ishPossibleQuery.append(" (");
							if(j==0){
							    ishPossibleQuery.append(ish[j] + paramsString);
							}
							else {
							    ishPossibleQuery.append(" OR "+ish[j] + paramsString);
							}
							ishPossibleQuery.append(") ");
					    }
					    ishPossibleQuery.append(" AND QIC_EXP_STRENGTH not in('present', 'not detected') ) ");
					    //						ishPossibleQuery.append(" AND QIC_EXP_STRENGTH not in('present', 'not detected', 'uncertain') ) ");
					    
					}
					else {
					    if(expStrength.equalsIgnoreCase("present")) {
							totalIshComps += subComps.length;
							allComps = subComps;
							
							ishPresQuery.append(worker);
							
							if (!ishStageString.equals("")) {
							    ishPresQuery.append(ishStageString + "AND ");
							}
							
							for(int j=0;j<ish.length;j++){
							    ishPresQuery.append(" (");
							    if(j == 0) {
							    	ishPresQuery.append(ish[j] + subParamsString);
							    } else {
							    	ishPresQuery.append(" OR "+ish[j] + subParamsString);
							    }
							    ishPresQuery.append(") ");
							}
							ishPresQuery.append(" AND QIC_EXP_STRENGTH='present' ) ");
					
					    }				    
					    else if(expStrength.equalsIgnoreCase("absent")) {
							totalIshComps += superComps.length;
							allComps = superComps;
						
							ishNotDetectedQuery.append(worker);
							
							if (!ishStageString.equals("")) {
							    ishNotDetectedQuery.append(ishStageString + "AND ");
							}
						
							for(int j=0;j<ish.length;j++){
							    ishNotDetectedQuery.append(" (");
							    if(j == 0) {
							    	ishNotDetectedQuery.append(ish[j] + superParamsString.toString());
							    } else {
							    	ishNotDetectedQuery.append(" OR "+ish[j] + superParamsString.toString());
							    }
							    ishNotDetectedQuery.append(") ");
							}
							ishNotDetectedQuery.append(" AND QIC_EXP_STRENGTH='not detected') ");
					    }
					    else if(expStrength.equalsIgnoreCase("unknown")) {
							totalIshComps += comps.length;
							allComps = comps;
							
							ishPossibleQuery.append(worker);
							if (!ishStageString.equals("")) {
							    ishPossibleQuery.append(ishStageString + "AND ");
							}
					
							for(int j=0;j<ish.length;j++){
							    ishPossibleQuery.append(" (");
							    if(j==0){
							    	ishPossibleQuery.append(ish[j] + paramsString);
							    }
							    else {
							    	ishPossibleQuery.append(" OR "+ish[j] + paramsString);
							    }
							    ishPossibleQuery.append(") ");
							}
							ishPossibleQuery.append(" AND QIC_EXP_STRENGTH not in('present', 'not detected') ) ");
							//							ishPossibleQuery.append(" AND QIC_EXP_STRENGTH not in('present', 'not detected', 'uncertain') ) ");
					    }
					    else {
					    	return null;
					    }
					}
					
					if(allComps != null) {
					    ishParamsContainer.add(allComps);
					}
					
					// if there is more than 1 input value, 'union' the queries produced
				
					if(i > 0) {
		
					    if (!ishQuery.toString().trim().equals("")) {
					    	ishQuery.append(AdvancedSearchDBQuery.getUnion());
					    }
					    if (!micQuery.toString().trim().equals("")) {
					    	micQuery.append(AdvancedSearchDBQuery.getUnion());
					    }
					}
					// 'IF' statements below commented and modified by xingjun - 21/10/2010 - end
					
					ishQuery.append(ishPresQuery.toString() + ishNotDetectedQuery.toString() + ishPossibleQuery.toString());
					
					//contsturct the array query
					micQuery.append(AdvancedSearchDBQuery.getMICSelectForAnatomy()+ AdvancedSearchDBQuery.getMICFromForAnatomy() + " WHERE ");
					
					if (!micStageString.equals("")) {
					    micQuery.append(micStageString + "AND ");
					}
				
					//params addition
					for(int j=0;j<mic.length;j++){
					    micQuery.append(" (");
					    if(j == 0) {
					    	micQuery.append(mic[j] + subParamsString);
					    } else {
					    	micQuery.append(" OR "+mic[j] + subParamsString);
					    }
					    micQuery.append(")) ");
					}
				
					totalMicComps += subComps.length;
					micParamsContainer.add(subComps);
			    }
			}
			//if there are no components to query then no point in doing the query
			if(totalIshComps == 0 && totalMicComps == 0){
			    return null;
			}
		
			//add all components from all arrays produced above to a single array.
			//This will be processed and added to the query at query execution time
			String [] allIshComps = new String [totalIshComps];
			String [] allMicComps = new String [totalMicComps];
			int index = 0;
			for(int i=0;i<ishParamsContainer.size();i++){
			    String [] tmp = ishParamsContainer.get(i);
			    for(int j=0;j<tmp.length;j++){
					allIshComps[index] = tmp[j];
					index++;
			    }
			}
			index = 0;
			for(int i=0;i<micParamsContainer.size();i++){
			    String [] tmp = micParamsContainer.get(i);
			    for(int j=0;j<tmp.length;j++){
					allMicComps[index] = tmp[j];
					index++;
			    }
			}
			//input = allComps;
			String orderStr = DBHelper.orderResult(orderby, asc);
			
			all = new String[5];
			
			String orderpart = orderStr+ 
			    new String((null == offset || offset.equals(""))&& (recordNumber==null || recordNumber.equals(""))? " ":" limit "+ offset + "," + recordNumber + " ");
		
			if(null != subType){
			    if(subType.equals("ish")){
					all[0] = ishQuery.toString() + orderpart;
					all[1] = countStr + "("+ishQuery.toString() + ") as tablea";
					all[2] = null;
					all[3] = "1";
					all[4] = "0";
			    }
			    else if(subType.equals("mic")) {
					all[0] = micQuery.toString() + orderpart;
					all[1] = null;
					all[2] = countMicStr + "("+micQuery.toString() + ") as tablea";
					all[3] = "0";
					all[4] = "1";					
			    }
			}
			else {
			    all[0] = ishQuery.toString() + AdvancedSearchDBQuery.getUnion() + micQuery.toString() + orderpart;
			    all[1] = countStr + "("+ishQuery.toString() + ") as tablea";
			    all[2] = countMicStr + "("+micQuery.toString() + ") as tablea";				
			    all[3] = "1";
			    all[4] = "1";
			    //input = allComps;
			}

			sqlAndParams.add(all);
			sqlAndParams.add(allIshComps);
			sqlAndParams.add(allMicComps);
		}
	
		//sqlAndParams.add(all);
		//sqlAndParams.add(input);
		
		return sqlAndParams;
	} // end of AssembleSQLForFocusQuery
    
    /**
     * method to return a string containing correct number of parameters for an sql query based on the size of user input
     * @param input - list of user spcified inputs
     * @return string containing correct number of parametrs
     */
    private String formatParamsForFocusQuery(String [] input){
    	StringBuffer inputStr = new StringBuffer("");
	
    	for(int i = 0; i < input.length; i++) {
		    if(i == input.length-1){
		    	inputStr.append("?");
		    }
		    else {
		    	inputStr.append("?,");
		    }
		    
		}
	
    	return inputStr.toString();
    }
    
}
