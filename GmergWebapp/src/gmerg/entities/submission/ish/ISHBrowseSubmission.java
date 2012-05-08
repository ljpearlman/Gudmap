package gmerg.entities.submission.ish;

import gmerg.entities.submission.BrowseSubmission;

public class ISHBrowseSubmission extends BrowseSubmission {

    private String geneSymbol;
    private String assay;
    private String specimen;
    private String thumbnail;
    private String imageName;
    private String imgSerialNo = "1";

    public ISHBrowseSubmission() {
    }

    public void setGeneSymbol(String symbol) {
        geneSymbol = symbol;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setAssay(String assy) {
        assay = assy;
    }

    public String getAssay() {
        return assay;
    }

    public void setSpecimen(String spec) {
        specimen = spec;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setThumbnail(String tnail) {
        thumbnail = tnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setImageName(String value) {
        imageName = value;
    }

    public String getImageName() {
        return imageName;
    }
    
    public void setImgSerialNo(String value) {
        imgSerialNo = value;
    }

    public String getImgSerialNo() {
        return imgSerialNo;
    }
}
