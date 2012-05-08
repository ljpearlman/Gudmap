/**
 * 
 */
package gmerg.entities.submission;

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
	private String productionMethod;
	private String hybridomaValue;
	private String phageDisplayValue;
	private String speciesImmunizedValue;
	private String purificationMethod;
	private String immunoglobulinIsotype;
	private String chainType;
	private String detectedVariantValue;
	private String speciesSpecificity;
	private String labelProduct;
	private String finalLabel;
	private String signalDetectionMethod;
	private String notes;
	
	
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
		this.lotNumber = lotNumber;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getProductionMethod() {
		return this.productionMethod;
	}
	
	public void setProductionMethod(String productionMethod) {
		this.productionMethod = productionMethod;
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
	
	public String getFinalLabel() {
		return this.finalLabel;
	}
	
	public void setFinalLabel(String finalLabel) {
		this.finalLabel = finalLabel;
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

}
