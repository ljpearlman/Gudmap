package gmerg.entities.submission.array;

public class GeneListItem {

    private String geneSymbol;
    private String probeId;
    private String signal;
    private String detection;
    private String pvalue;
    private boolean selected = false;

    public GeneListItem() {
    }

    public void setGeneSymbol(String value) {
        geneSymbol = value;
    }

    public String getgeneSymbol() {
        return geneSymbol;
    }

    public void setProbeId(String value) {
        probeId = value;
    }

    public String getProbeId() {
        return probeId;
    }

    public void setSignal(String value) {
        signal = value;
    }

    public String getSignal() {
        return signal;
    }

    public void setDetection(String value) {
        detection = value;
    }

    public String getDetection() {
        return detection;
    }

    public void setPvalue(String value) {
        pvalue = value;
    }

    public String getPvalue() {
        return pvalue;
    }

    public void setSelected(boolean value) {
        selected = value;
    }

    public boolean getSelected() {
        return selected;
    }
}
