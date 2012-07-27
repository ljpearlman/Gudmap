/**
 * 
 */
package gmerg.entities.submission;

import java.util.ArrayList;
/**
 * @author xingjun
 *
 */
public class Antibody {
	
	private String name;
	private String accessionId;
	private String geneSymbol;
	private String geneName;
	private String geneId;
	private String seqStatus;
	private String seqInfo;
	private int seqStartLocation;
	private int seqEndLocation;
	private String url;
//	private String genbankID;
//	private String genbankURL;
	private String supplier;
	private String catalogueNumber;
	private String lotNumber;
	private String type;
	private String host;
	private String hybridomaValue;
	private String phageDisplayValue;
	private String speciesImmunizedValue;
	private String purificationMethod;
	private String immunoglobulinIsotype;
	private String chainType;
	private String detectedVariantValue;
	private String speciesSpecificity;
	private String labelProduct;
	private String directLabel;
	private String signalDetectionMethod;
	private String notes;
	private String uniprotId;
	private String detectionNotes;
	private String secondaryAntibody;
	private String dilution;
	private String experimentalNotes;
	private String labProbeId;
	private String maprobeId;
	private String locusTag;
    private ArrayList ishSubmissions; 
    private ArrayList<String[]> ishFilteredSubmissions; 
	
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccessionId() {
		return this.accessionId;
	}
	
	public void setAccessionId(String accessionId) {
		this.accessionId = accessionId;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getGeneSymbol() {
		return this.geneSymbol;
	}
	
	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}
	
	public String getGeneName() {
		return this.geneName;
	}
	
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}
	
	public String getGeneId() {
		return this.geneId;
	}
	
	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}
	
	public String getSeqStatus() {
		return this.seqStatus;
	}
	
    public void setSeqStatus(String value) {
        if (value.trim().equals("FULLY_SEQUENCED")) {
            this.seqStatus = "Fully Sequenced";
        } else if (value.trim().equals("PARTIALLY_SEQUENCED")) {
            this.seqStatus = "Partially Sequenced";
        } else {
            this.seqStatus = "Unsequenced";
        }
    }
    
    public int getSeqStartLocation() {
    	return this.seqStartLocation;
    }
    
    public void setseqStartLocation(int seqStartLocation) {
    	this.seqStartLocation = seqStartLocation;
    }
    
    public int getSeqEndLocation() {
    	return this.seqEndLocation;
    }
    
    public void setSeqEndLocation(int seqEndLocation) {
    	this.seqEndLocation = seqEndLocation;
    }
    
    public String getSeqInfo() {
    	return this.seqInfo;
    }
    
    public void setSeqInfo(String seqInfo) {
    	this.seqInfo = seqInfo;
    }
    
//    public String getGenbankID() {
//        return this.genbankID;
//    }
//
//    public void setGenbankID(String genbankID) {
//        this.genbankID = genbankID;
//    }
//
//    public String getGenbankURL() {
//        return this.genbankURL;
//    }
//
//    public void setGenbankURL(String genbankURL) {
//        this.genbankURL = genbankURL;
//    }

	
	public String getSupplier() {
		return this.supplier;
	}
	
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
	public String getCatalogueNumber() {
		return this.catalogueNumber;
	}
	
	public void setCatalogueNumber(String catalogueNumber) {
		this.catalogueNumber = catalogueNumber;
	}
	
	public String getLotNumber() {
		return this.lotNumber;
	}
	
	public void setLotNumber(String lotNumber) {
		if (lotNumber.equalsIgnoreCase("na"))
			this.lotNumber = "";
		else
			this.lotNumber = lotNumber;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setSubtype(String type) {
		this.type = type;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getHybridomaValue() {
		return this.hybridomaValue;
	}
	
	public void setHybridomaValue(String hybridomaValue) {
		this.hybridomaValue = hybridomaValue;
	}
	
	public String getPhageDisplayValue() {
		return this.phageDisplayValue;
	}
	
	public void setPhageDisplayValue(String phageDisplayValue) {
		this.phageDisplayValue = phageDisplayValue;
	}
	
	public String getSpeciesImmunizedValue() {
		return this.speciesImmunizedValue;
	}
	
	public void setSpeciesImmunizedValue(String speciesImmunizedValue) {
		this.speciesImmunizedValue = speciesImmunizedValue;
	}
	
	public String getPurificationMethod() {
		return this.purificationMethod;
	}
	
	public void setPurificationMethod(String purificationMethod) {
		this.purificationMethod = purificationMethod;
	}
	
	public String getImmunoglobulinIsotype() {
		return this.immunoglobulinIsotype;
	}
	
	public void setImmunoglobulinIsotype(String immunoglobulinIsotype) {
		this.immunoglobulinIsotype = immunoglobulinIsotype;
	}
	
	public String getChainType() {
		return this.chainType;
	}
	
	public void setChainType(String chainType) {
		this.chainType = chainType;
	}
	
	public String getDetectedVariantValue() {
		return this.detectedVariantValue;
	}
	
	public void setDetectedVariantValue(String detectedVariantValue) {
		this.detectedVariantValue = detectedVariantValue;
	}
	
	public String getSpeciesSpecificity() {
		return this.speciesSpecificity;
	}
	
	public void setSpeciesSpecificity(String speciesSpecificity) {
		this.speciesSpecificity = speciesSpecificity;
	}
	
	public String getLabelProduct() {
		return this.labelProduct;
	}
	
	public void setLabelProduct(String labelProduct) {
		this.labelProduct = labelProduct;
	}
	
	public String getDirectLabel() {
		return this.directLabel;
	}
	
	public void setDirectLabel(String directLabel) {
		this.directLabel = directLabel;
	}
	
	public String getSignalDetectionMethod() {
		return this.signalDetectionMethod;
	}
	
	public void setSignalDetectionMethod(String signalDetectionMethod) {
		this.signalDetectionMethod = signalDetectionMethod;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUniprotId(){
		return this.uniprotId;
	}
	
	public void setUniprotId(String uniprotId) {
		this.uniprotId = uniprotId;
	}

	public String getDetectionNotes() {
		return this.detectionNotes;
	}
	
	public void setDetectionNotes(String detectionNotes) {
		this.detectionNotes = detectionNotes;
	}

	public String getSecondaryAntibody() {
		return this.secondaryAntibody;
	}
	
	public void setSecondaryAntibody(String secondaryAntibody) {
		this.secondaryAntibody = secondaryAntibody;
	}

	public String getDilution(){
		return this.dilution;
	}
	
	public void setDilution(String dilution) {
		this.dilution = dilution;
	}

	public String getExperimentalNotes() {
		return this.experimentalNotes;
	}
	
	public void setExperimentalNotes(String experimentalNotes) {
		this.experimentalNotes = experimentalNotes;
	}

	public String getLabProbeId() {
		return this.labProbeId;
	}
	
	public void setLabProbeId(String labProbeId) {
		this.labProbeId = labProbeId;
	}

	public String getMaProbeId() {
		return this.maprobeId;
	}
	
	public void setMaProbeId(String maprobeId) {
		this.maprobeId = maprobeId;
	}

	public String getLocusTag() {
		return this.locusTag;
	}
	
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

    public ArrayList getIshSubmissions() { 	  	
        return this.ishSubmissions;
    }
	
    public void setIshSubmissions(ArrayList<String[]> values) {
        this.ishSubmissions = values;

        this.ishFilteredSubmissions = new ArrayList<String[]>();        
        for (int i=0; i<ishSubmissions.size(); i++){
        	String[] arr = (String[]) ishSubmissions.get(i);
        	if (arr[9].equalsIgnoreCase(maprobeId) || arr[6].equalsIgnoreCase(maprobeId))
        		this.ishFilteredSubmissions.add(arr);
        }
    }
	
    public ArrayList<String[]> getIshFilteredSubmissions() { 	  	
        return this.ishFilteredSubmissions;
    }
}
