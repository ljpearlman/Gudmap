/**
 * 
 */
package gmerg.entities.submission.ish;

/**
 * @author xingjun
 *
 */
public class ISHBrowseMolecularMarkerCandidate {
	
	private String tissueName;
	private String componentId;
	private String geneSymbol;
	private String[] theilerStages;
	private String[] ISHSubmissionIDList;
	private String[] IHCSubmissionIDList;
	private String[] arraySubmissionIDList;
	
	public ISHBrowseMolecularMarkerCandidate() {
		
	}
	
	public String getTissueName() {
		return this.tissueName;
	}
	
	public void setTissueName(String tissueName) {
		this.tissueName = tissueName;
	}
	
	public String getComponentId() {
		return this.componentId;
	}
	
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	
	public String getGeneSymbol() {
		return this.geneSymbol;
	}
	
	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}
	
	public String[] getTheilerStages() {
		return this.theilerStages;
	}
	
	public void setTheilerStages(String[] theilerStages) {
		this.theilerStages = theilerStages;
	}
	
	public String[] getISHSubmissionIDList() {
		return this.ISHSubmissionIDList;
	}
	
	public void setISHSubmissionIDList(String[] ISHSubmissionIDList) {
		this.ISHSubmissionIDList = ISHSubmissionIDList;
	}
	
	public String[] getIHCSubmissionIDList() {
		return this.IHCSubmissionIDList;
	}
	
	public void setIHCSubmissionIDList(String[] IHCSubmissionIDList) {
		this.IHCSubmissionIDList = IHCSubmissionIDList;
	}
	
	public String[] getarraySubmissionIDList() {
		return this.arraySubmissionIDList;
	}
	
	public void setarraySubmissionIDList(String[] arraySubmissionIDList) {
		this.arraySubmissionIDList = arraySubmissionIDList;
	}

}
