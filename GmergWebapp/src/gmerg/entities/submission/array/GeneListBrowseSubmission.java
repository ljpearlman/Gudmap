package gmerg.entities.submission.array;

public class GeneListBrowseSubmission {

  private String geneSymbol;
  private String probeId;
  private String signal;
  private String detection;
  private String pValue;
  private boolean selected;
  
  public GeneListBrowseSubmission() {
	  
  }
  
  public void setGeneSymbol(String geneSymbol) {
    this.geneSymbol = geneSymbol;
  }

  public String getGeneSymbol() {
    return geneSymbol;
  }
  
  public void setProbeId(String probeId) {
	  this.probeId = probeId;
  }
  
  public String getProbeId() {
	  return probeId;
  }

  public void setSignal(String signal) {
    this.signal = signal;
  }

  public String getSignal() {
    return signal;
  }

  public void setDetection(String detection) {
    this.detection = detection;
  }

  public String getDetection() {
    return detection;
  }

  public void setPvalue(String pValue) {
    this.pValue = pValue;
  }

  public String getPvalue() {
    return pValue;
  }

  public void setSelected(boolean sel) {
      selected = sel;
  }

  public boolean getSelected() {
      return selected;
  }

}
