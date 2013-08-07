package gmerg.entities.submission;

import java.util.ArrayList;

/**
 * @author chris tindal
 *
 */

public class Probe {

    private String geneSymbol;
    private String geneName;
    private String probeName;
    private String geneID;
    private String geneIdUrl;
    private String source;
    private String strain;
    private String tissue;
    private String type;
    private String geneType;
    private String labelProduct;
    private String visMethod;
    private String cloneName;
    private String additionalCloneName;
    private String genbankID;
    private String maprobeID;
    private String notes;
    private String probeNameURL;
    private String genbankURL;
    private String maProbeURL;
    private String seqStatus;
    private String seqInfo;
    private String seq5Loc;
    private String seq3Loc;
    private String seq5Primer;
    private String seq3Primer;
    private ArrayList maprobeNotes;
    private String probeNameSource; // specify where the probe name come from
    private ArrayList fullSequence;
    private String labProbeId; 
    private ArrayList ishSubmissions; 
    private ArrayList<String[]> ishFilteredSubmissions; 


    private String maprobeNoteString;

    public ArrayList getFullSequence() {
	return fullSequence;
	}

    public void setFullSequence(ArrayList value) {
	fullSequence = value;
    }
    
    public void setGeneSymbol(String value) {
        geneSymbol = value;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneName(String value) {
        geneName = value;
	if (null != geneName && geneName.equals("0"))
	    geneName = null;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setProbeName(String value) {
        probeName = value;
    }

    public String getProbeName() {
        return probeName;
    }

    public void setGeneID(String value) {
        geneID = value;
    }

    public String getGeneID() {
        return geneID;
    }
    
    public void setGeneIdUrl(String value) {
        geneIdUrl = value;
    }

    public String getGeneIdUrl() {
        return geneIdUrl;
    }

    public void setSource(String value) {
        source = value;
    }

    public String getSource() {
        return source;
    }

    public void setStrain(String value) {
        strain = value;
    }

    public String getStrain() {
        return strain;
    }

    public void setTissue(String value) {
        tissue = value;
    }

    public String getTissue() {
        return tissue;
    }

    public void setType(String value) {
        type = value;
    }

    public String getType() {
        return type;
    }

    public String getGeneType() {
        return geneType;
    }

    public void setGeneType(String value) {
        geneType = value;
    }

    public String getLabelProduct() {
        return labelProduct;
    }

    public void setLabelProduct(String value) {
        labelProduct = value;
    }

    public String getVisMethod() {
        return visMethod;
    }

    public void setVisMethod(String value) {
        visMethod = value;
    }

    public String getGenbankID() {
        return genbankID;
    }

    public void setGenbankID(String value) {
        genbankID = value;
    }

    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String value) {
        cloneName = value;
    }

    public String getAdditionalCloneName() {
        return additionalCloneName;
    }

    public void setAdditionalCloneName(String value) {
        additionalCloneName = value;
    }

    public String getMaprobeID() {
        return maprobeID;
    }

    public void setMaprobeID(String value) {
        maprobeID = value;
    }

    public void setNotes(String nts) {
        notes = nts;
    }

    public String getNotes() {
        return notes;
    }

    public String getProbeNameURL() {
        return probeNameURL;
    }

    public void setProbeNameURL(String value) {
        if (null != probeName) {
            probeNameURL = value;
        } else {
            probeNameURL = null;
        }
       if (null != probeNameURL) {
	   probeNameURL = probeNameURL.trim();
	   if (probeNameURL.equals(""))
	       probeNameURL = null;
       }
    }

    public String getGenbankURL() {
        return genbankURL;
    }

    public void setGenbankURL(String value) {
        genbankURL = value;
    }

    public String getMaProbeURL() {
        return maProbeURL;
    }

    public void setMaProbeURL(String value) {
        maProbeURL = value;
    }

    public String getSeqStatus() {
        return seqStatus;
    }

    public void setSeqStatus(String value) {
	if (null == value)
            seqStatus = "";

        else if (value.equals("FULLY_SEQUENCED".trim())) {
            seqStatus = "Fully Sequenced.";
        } else if (value.equals("PARTIALLY_SEQUENCED".trim())) {
            seqStatus = "Partially Sequenced.";
        } else {
            seqStatus = "Unsequenced.";
        }
    }

    /**
     * @param sequenceInfo
     */
    public void setSeqInfo(String value) {
        seqInfo = value;
    }

    public String getSeqInfo() {
        return seqInfo;
    }

    public String getSeq5Loc() {
        return seq5Loc;
    }

    public void setSeq5Loc(String value) {
        if (null == value || value.equals("0")) {
            seq5Loc = "n/a";
        } else {
            seq5Loc = value;
        }
    }

    public String getSeq3Loc() {
        return seq3Loc;
    }

    public void setSeq3Loc(String value) {
        if (null == value || value.equals("0")) {
            seq3Loc = "n/a";
        } else {
            seq3Loc = value;
        }
    }
    
    // display info of probe database in the submission detail page
    public String getSeq5Primer() {
    	return seq5Primer;
    }
    
    public void setSeq5Primer(String value) {
    	seq5Primer = value;
    }
    
    public String getSeq3Primer() {
    	return seq3Primer;
    }
    
    public void setSeq3Primer(String value) {
    	seq3Primer = value;
    }
    
    public ArrayList getMaprobeNotes() {
    	return maprobeNotes;
    }
    
    public void setMaprobeNotes(ArrayList value) {
    	maprobeNotes = value;
    }
    
    public String getProbeNameSource() {
    	return probeNameSource;
    }
    
    public void setProbeNameSource(String value) {
    	probeNameSource = value;
    }
    
    public String getMaprobeNoteString() {
    	return maprobeNoteString;
    }
    
    public void setMaprobeNoteString(String value) {
    	maprobeNoteString = value;
    }
    
    public void setLabProbeId(String value) {
        labProbeId = value;
    }

    public String getLabProbeId() {
        return labProbeId;
    }
 
    public void setIshSubmissions(ArrayList values) {
        ishSubmissions = values;
        if (null == values) {
	    if (null != ishFilteredSubmissions)
		ishFilteredSubmissions.clear();
	    return;
	}

        ishFilteredSubmissions = new ArrayList<String[]>();   
	if (null != ishSubmissions)
        for (int i=0; i<ishSubmissions.size(); i++){
        	String[] arr = (String[]) ishSubmissions.get(i);
        	if (arr[9].equalsIgnoreCase(maprobeID) || arr[6].equalsIgnoreCase(maprobeID))
        		ishFilteredSubmissions.add(arr);
        }
    }

    public ArrayList getIshSubmissions() { 	  	
        return ishSubmissions;
    }

    public ArrayList getIshFilteredSubmissions() { 	  	
        return ishFilteredSubmissions;
    }

}
