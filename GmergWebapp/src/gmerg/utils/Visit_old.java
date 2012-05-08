package gmerg.utils;

import java.util.Hashtable;

public class Visit_old {

    private String[] opIds; //contains submission id for intersect/difference operations, defined by user
    
    private String queryType;
    
    private int numPagesForQuery;  //the number of pages in a query result set will have
    
    private Hashtable parameters;  //contains the parameter values needed to complete a particular query
    
    public Visit_old() {
    }

    public void setOpIds(String[] ids) {
        opIds = ids;
    }

    public String[] getOpIds() {
        return opIds;
    }
    
    public void setQueryType(String qType) {
        queryType = qType;
    }
    
    public String getQueryType() {
        return queryType;
    }
    
    public void setNumPagesForQuery(int i){
        numPagesForQuery = i;
    }
    
    public int getNumPagesForQuery(){
        return numPagesForQuery;
    }
    
    public void setParameters(Hashtable tbl){
        parameters = tbl;
    }
    
    public Hashtable getParameters() {
        return parameters;
    }
    
    public void clearValuesInParameters() {
        parameters = null;
    }
}
