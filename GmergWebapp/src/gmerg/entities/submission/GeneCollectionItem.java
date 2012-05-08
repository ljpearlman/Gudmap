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
    	return this.selected;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public String getGeneSymbol() {
    	return this.geneSymbol;
    }
    
    public void setGeneSymbol(String geneSymbol) {
    	this.geneSymbol = geneSymbol;
    }
    
    public String getNumberOfRelatedSubmissionsISH() {
    	return this.numberOfRelatedSubmissionsISH;
    }
    
    public void setNumberOfRelatedSubmissionsISH(String numberOfRelatedSubmissionsISH) {
    	this.numberOfRelatedSubmissionsISH = numberOfRelatedSubmissionsISH;
    }
    
    public String getNumberOfRelatedSubmissionsArray() {
    	return this.numberOfRelatedSubmissionsArray;
    }
    
    public void setNumberOfRelatedSubmissionsArray(String numberOfRelatedSubmissionsArray) {
    	this.numberOfRelatedSubmissionsArray = numberOfRelatedSubmissionsArray;
    }

}
