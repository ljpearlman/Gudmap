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
    private String labProbeId; // added by xingjun - 19/01/2010
    private ArrayList ishSubmissions; // added by Bernie 29/06/2011 Mantis 558 Task6
    private ArrayList<String[]> ishFilteredSubmissions; // added by Bernie 12/08/2011 Mantis 558 

    // full string of ma probe notes
    private String maprobeNoteString;
//    private String fullSequenceString;

    public ArrayList getFullSequence() {
	return fullSequence;
	}

    public void setFullSequence(ArrayList fullSequence) {
	this.fullSequence = fullSequence;
    }
    
    public void setGeneSymbol(String value) {
        this.geneSymbol = value;
    }

    public String getGeneSymbol() {
        return this.geneSymbol;
    }

    public void setGeneName(String value) {
        this.geneName = value;
    }

    public String getGeneName() {
        return this.geneName;
    }

    public void setProbeName(String value) {
        this.probeName = value;
    }

    public String getProbeName() {
        return this.probeName;
    }

    public void setGeneID(String value) {
        this.geneID = value;
    }

    public String getGeneID() {
        return this.geneID;
    }
    
    public void setGeneIdUrl(String value) {
        this.geneIdUrl = value;
    }

    public String getGeneIdUrl() {
        return this.geneIdUrl;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getSource() {
        return this.source;
    }

    public void setStrain(String value) {
        this.strain = value;
    }

    public String getStrain() {
        return this.strain;
    }

    public void setTissue(String value) {
        this.tissue = value;
    }

    public String getTissue() {
        return this.tissue;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getType() {
        return this.type;
    }

    public String getGeneType() {
        return this.geneType;
    }

    public void setGeneType(String value) {
        this.geneType = value;
    }

    public String getLabelProduct() {
        return this.labelProduct;
    }

    public void setLabelProduct(String value) {
        this.labelProduct = value;
    }

    public String getVisMethod() {
        return this.visMethod;
    }

    public void setVisMethod(String value) {
        this.visMethod = value;
    }

    public String getGenbankID() {
        return this.genbankID;
    }

    public void setGenbankID(String value) {
        this.genbankID = value;
    }

    public String getCloneName() {
        return this.cloneName;
    }

    public void setCloneName(String value) {
        this.cloneName = value;
    }

    public String getAdditionalCloneName() {
        return this.additionalCloneName;
    }

    public void setAdditionalCloneName(String value) {
        this.additionalCloneName = value;
    }

    public String getMaprobeID() {
        return this.maprobeID;
    }

    public void setMaprobeID(String value) {
        this.maprobeID = value;
    }

    public void setNotes(String nts) {
        this.notes = nts;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getProbeNameURL() {
        return this.probeNameURL;
    }

    public void setProbeNameURL(String value) {
        if (null != probeName) {
            this.probeNameURL = value;
        } else {
            this.probeNameURL = null;
        }
    }

    public String getGenbankURL() {
        return this.genbankURL;
    }

    public void setGenbankURL(String value) {
        this.genbankURL = value;
    }

    public String getMaProbeURL() {
        return this.maProbeURL;
    }

    public void setMaProbeURL(String value) {
        this.maProbeURL = value;
    }

    public String getSeqStatus() {
        return this.seqStatus;
    }

    public void setSeqStatus(String value) {
        if (value.equals("FULLY_SEQUENCED".trim())) {
            this.seqStatus = "Fully Sequenced.";
        } else if (value.equals("PARTIALLY_SEQUENCED".trim())) {
            this.seqStatus = "Partially Sequenced.";
        } else {
            this.seqStatus = "Unsequenced.";
        }
    }

    /**
     * @param sequenceInfo
     */
    public void setSeqInfo(String sequenceInfo) {
        this.seqInfo = sequenceInfo;
    }

    public String getSeqInfo() {
        return this.seqInfo;
    }

    public String getSeq5Loc() {
        return this.seq5Loc;
    }

    public void setSeq5Loc(String value) {
        if (value.equals("0")) {
            this.seq5Loc = "n/a";
        } else {
            this.seq5Loc = value;
        }
    }

    public String getSeq3Loc() {
        return this.seq3Loc;
    }

    public void setSeq3Loc(String value) {
        if (value.equals("0")) {
            this.seq3Loc = "n/a";
        } else {
            this.seq3Loc = value;
        }
    }
    
    // added by Xingjun 02/05/2007 -- begin
    // display info of probe database in the submission detail page
    public String getSeq5Primer() {
    	return this.seq5Primer;
    }
    
    public void setSeq5Primer(String seq5Primer) {
    	this.seq5Primer = seq5Primer;
    }
    
    public String getSeq3Primer() {
    	return this.seq3Primer;
    }
    
    public void setSeq3Primer(String seq3Primer) {
    	this.seq3Primer = seq3Primer;
    }
    
    public ArrayList getMaprobeNotes() {
    	return this.maprobeNotes;
    }
    
    public void setMaprobeNotes(ArrayList maprobeNotes) {
    	this.maprobeNotes = maprobeNotes;
    }
    // added by Xingjun 02/05/2007 -- end
    
    public String getProbeNameSource() {
    	return this.probeNameSource;
    }
    
    public void setProbeNameSource(String probeNameSource) {
    	this.probeNameSource = probeNameSource;
    }
    
    public String getMaprobeNoteString() {
    	return this.maprobeNoteString;
    }
    
    public void setMaprobeNoteString(String maprobeNoteString) {
    	this.maprobeNoteString = maprobeNoteString;
    }
    
    public void setLabProbeId(String labProbeId) {
        this.labProbeId = labProbeId;
    }

    public String getLabProbeId() {
        return this.labProbeId;
    }
 
    // added by Bernie 29/06/2011 Mantis 558 Task6    
    public void setIshSubmissions(ArrayList values) {
        this.ishSubmissions = values;
        
     // added by Bernie 12/08/2011 Mantis 558 
        this.ishFilteredSubmissions = new ArrayList<String[]>();        
        for (int i=0; i<ishSubmissions.size(); i++){
        	String[] arr = (String[]) ishSubmissions.get(i);
        	if (arr[9].equalsIgnoreCase(maprobeID) || arr[6].equalsIgnoreCase(maprobeID))
        		this.ishFilteredSubmissions.add(arr);
        }
    }

    // added by Bernie 29/06/2011 Mantis 558 Task6
    public ArrayList getIshSubmissions() { 	  	
        return this.ishSubmissions;
    }

 // added by Bernie 12/08/2011 Mantis 558 
    public ArrayList getIshFilteredSubmissions() { 	  	
        return this.ishFilteredSubmissions;
    }

//    public String getFullSequenceString() {
//    	return this.fullSequenceString;
//    }
//    
//    public void setFullSequenceString(String fullSequenceString) {
//    	this.fullSequenceString = fullSequenceString;
//    }
    
}
