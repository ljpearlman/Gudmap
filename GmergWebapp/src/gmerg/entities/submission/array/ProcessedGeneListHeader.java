/**
 * 
 */
package gmerg.entities.submission.array;

import java.io.IOException;

/**
 * @author xingjun
 *
 */
public class ProcessedGeneListHeader {

	String surname;
    String labName;
    String components;
    String statistics;
    String dataTransformation;
    int tests;
    int serialNo;
    int upRegulated;
    int downRegulated;
    String fileName;
    int id;
   
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException{
      out.writeObject(this);
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
    	ProcessedGeneListHeader geneList = (ProcessedGeneListHeader)in.readObject();
      surname = geneList.surname;
      labName = geneList.labName;
      components = geneList.components;
      statistics = geneList.statistics;
      dataTransformation = geneList.dataTransformation;
      tests = geneList.tests;
      serialNo = geneList.serialNo;
      upRegulated = geneList.upRegulated;
      downRegulated = geneList.downRegulated;
      fileName = geneList.fileName;
      id = geneList.id;
    }
     
    public void setLabName(String labName){
      this.labName = labName;
    }
    public void setComponents(String components){
      this.components = components;
    }
    public void setStatistics(String statistics){
      this.statistics = statistics;
    }
    public void setDataTransformation(String dataTransformation){
      this.dataTransformation = dataTransformation;
    }
    public void setTests(int tests){
      this.tests = tests;
    }
    public void setSerialNo(int serialNo){
      this.serialNo = serialNo;
    }
    public void setUpRegulated(int upRegulated){
      this.upRegulated = upRegulated;
    }
    public void setDownRegulated(int downRegulated){
      this.downRegulated = downRegulated;
    }
    public void setFileName(String fileName){
      this.fileName = fileName;
    }
    public void setId(int id){
      this.id = id;
    }
   
    public String getLabName(){
      return labName;
    }
    public String getComponents(){
      return components;
    }
    public String getStatistics(){
      return statistics;
    }
    public String getDataTransformation(){
      return dataTransformation;
    }
    public int getTests(){
      return tests;
    }
    public int getSerialNo(){
      return serialNo;
    }
    public int getUpRegulated(){
      return upRegulated;
    }
    public int getDownRegulated(){
      return downRegulated;
    }
    public String getFileName(){
      return fileName;
    }
    public int getId(){
      return id;
    }

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
