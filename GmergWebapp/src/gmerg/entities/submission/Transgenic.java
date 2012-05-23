/**
 * 
 */
package gmerg.entities.submission;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class Transgenic {
    private boolean debug = true;
    private String geneSymbol;
    private String geneName;
    private String geneId;
    private String geneIdUrl;
	private String mutatedAlleleId;
	private String mutatedAlleleIdUrl;// Bernie 25/11/2010 (mantis 336)
	private String mutatedAlleleName;
    private String labelProduct;
    private String visMethod;
    private String notes;
    private String promoter;
    private String notePrefix;
    private String noteUrl;
    private String noteUrlText;
    private String noteSuffix;
    private String alleleFirstChrom;
    private String alleleSecondChrom;
    private String mutantType;
    private int mutantOid;
    private int serialNo;
    private String pubURL;
    
    public int getMutantOid() {
    	return this.mutantOid;
    }
    
    public void setMutantOid(int mutantOid) {
    	this.mutantOid = mutantOid;
    }
    
    public String getMutantType() {
    	return this.mutantType;
    }
    
    public void setMutantType(String mutantType) {
    	this.mutantType = mutantType;
    }
    
    public String getGeneSymbol() {
        return this.geneSymbol;
    }

    public void setGeneSymbol(String value) {
        this.geneSymbol = value;
    }

    public String getGeneName() {
        return this.geneName;
    }

    public void setGeneName(String value) {
        this.geneName = value;
    }

    public String getGeneId() {
        return this.geneId;
    }
    
    public void setGeneId(String value) {
        this.geneId = value;
        setGeneIdUrl("http://www.informatics.jax.org/searchtool/Search.do?query=" + value);
    }

    public String getGeneIdUrl() {
        return this.geneIdUrl;
    }

    public void setGeneIdUrl(String value) {
        this.geneIdUrl = value;
    }

    public String getMutatedAlleleId() {
	if (debug)
	    System.out.println("Transgenic.mutatedAlleleId = "+mutatedAlleleId);
    	return this.mutatedAlleleId;
    }
    
    public void setMutatedAlleleId(String mutatedAlleleId) {
    	this.mutatedAlleleId = mutatedAlleleId;
    }

    // Bernie 25/11/2010 - added new getter (mantis 336)
    public String getMutatedAlleleIdUrl() {
	if (debug)
	    System.out.println("Transgenic.mutatedAlleleIdUrl = "+mutatedAlleleIdUrl);
        return this.mutatedAlleleIdUrl;
    }

    // Bernie 25/11/2010 - added new getter (mantis 336)
    public void setMutatedAlleleIdUrl(String value) {
        this.mutatedAlleleIdUrl = value;
    }

    public String getMutatedAlleleName() {
    	return this.mutatedAlleleName;
    }
    
    public void setMutatedAlleleName(String mutatedAlleleName) {
    	this.mutatedAlleleName = mutatedAlleleName;
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

    public String getNotes() {
	if (debug)
	    System.out.println("Transgenic.notes = "+notes);
        return this.notes;
    }

    public void setNotes(String nts) {
        this.notes = nts;
        if (null != notes) {
           int index = notes.indexOf(";");
           if (-1 != index) {
	     String str1 = notes.substring(0, index);
	     String str2 = notes.substring(index + 1);
	     index = str1.indexOf(">");
	     notes = str1.substring(0, index + 1)+geneSymbol+" "+str1.substring(index+1)+"&nbsp;&#38;";
	     index = str2.indexOf(">");
	     notes = notes + str2.substring(0, index + 1)+geneSymbol+" "+str2.substring(index+1);
	   }
	}
    }
    
    public String getPromoter() {
        return this.promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }
    
    public String getNotePrefix() {
        return this.notePrefix;
    }

    public void setNotePrefix(String notePrefix) {
        this.notePrefix = notePrefix;
    }
    
    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }
    
    public String getNoteUrlText() {
        return this.noteUrlText;
    }

    public void setNoteUrlText(String noteUrlText) {
        this.noteUrlText = noteUrlText;
    }
    
    public String getNoteSuffix() {
        return this.noteSuffix;
    }

    public void setNoteSuffix(String noteSuffix) {
        this.noteSuffix = noteSuffix;
    }
    
    public String getAlleleFirstChrom() {
    	return this.alleleFirstChrom;
    }
    
    public void setAlleleFirstChrom(String alleleFirstChrom) {
    	this.alleleFirstChrom = alleleFirstChrom;
    }
    
    public String getAlleleSecondChrom() {
    	return this.alleleSecondChrom;
    }
    
    public void setAlleleSecondChrom(String alleleSecondChrom) {
    	this.alleleSecondChrom = alleleSecondChrom;
    }
    
    // xingjun - 09/11/2009
    public int getSerialNo() {
    	return this.serialNo;
    }
    
    public void setSerialNo(int serialNo) {
    	this.serialNo = serialNo;
    }
    
    public String getPubUrl() {
    	return this.pubURL;
    }
    
    public void setPubUrl(String pubURL) {
    	this.pubURL = pubURL;
    }
    
}
