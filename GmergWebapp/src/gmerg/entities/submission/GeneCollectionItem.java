/**
 * 
 */
package gmerg.entities.submission;

/**
 * @author xingjun
 *
 */
public class GeneCollectionItem {
	
    private boolean selected;
    private String geneSymbol;
    private String numberOfRelatedSubmissionsISH;
    private String numberOfRelatedSubmissionsArray;
    
    public boolean getSelected() {
    	return selected;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public String getGeneSymbol() {
    	return geneSymbol;
    }
    
    public void setGeneSymbol(String geneSymbol) {
    	this.geneSymbol = geneSymbol;
    }
    
    public String getNumberOfRelatedSubmissionsISH() {
    	return numberOfRelatedSubmissionsISH;
    }
    
    public void setNumberOfRelatedSubmissionsISH(String numberOfRelatedSubmissionsISH) {
    	this.numberOfRelatedSubmissionsISH = numberOfRelatedSubmissionsISH;
    }
    
    public String getNumberOfRelatedSubmissionsArray() {
    	return numberOfRelatedSubmissionsArray;
    }
    
    public void setNumberOfRelatedSubmissionsArray(String numberOfRelatedSubmissionsArray) {
    	this.numberOfRelatedSubmissionsArray = numberOfRelatedSubmissionsArray;
    }

}
