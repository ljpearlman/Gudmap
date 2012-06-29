package gmerg.utils;

import gmerg.utils.table.DataItem;

// RetrieveDataCache - a class to cache data returned by the retrieveData() method
// use to cache parameters of a call to retrieveData() and its results
// then use to compare if retrieveData() has been called with the same parameters as previously
// if so - return the cached results 
public class RetrieveDataCache{
  
  private DataItem[][] data;
  private int column;
  private boolean ascending;
  private int offset; 
  private int num;
  
  // ********************************************************************************
  // Constructor
  // ********************************************************************************
  public RetrieveDataCache() {
    this.setData(null);
    this.setColumn(-1);
    this.setAscending(false);
    this.setOffset(-1);
    this.setNum(-1);
  }
  
  public void setData(DataItem[][] data) {
    this.data = data;
  }
  
  public DataItem[][] getData() {
    return data;
  }
  
  public void setColumn(int column) {
    this.column = column;
  }
  
  public int getColumn() {
    return column;
  }
  
  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }
  
  public boolean isAscending() {
    return ascending;
  }
  
  public void setOffset(int offset) {
    this.offset = offset;
  }
  
  public int getOffset() {
    return offset;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public int getNum() {
    return num;
  }
  
  public boolean isSameQuery(int columnCompare, boolean ascendingCompare, int offsetCompare,int numCompare) {
    Boolean ascCompare = new Boolean(ascendingCompare);
    Boolean ascCached = new Boolean(ascending);
    if (columnCompare == column && ascCompare.equals(ascCached) && offsetCompare == offset && numCompare == num) {
      return true;
    }
    else {
      return false;
    }
  }
  
}
