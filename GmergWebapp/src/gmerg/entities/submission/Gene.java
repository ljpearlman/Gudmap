package gmerg.entities.submission;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

public class Gene {

    private String symbol = null;
    private String name = null;
    private String synonyms = null;
    private String xsomeStart = null;
    private String xsomeEnd = null;
    private String xsomeName = null;
    private String genomeBuild = null;
    private String mgiAccID = null;
    private String mgiURL = null;
    private String ensemblURL = null;
    private String ensemblID = null;
    private String goURL = null;
    private String omimURL = null;
    private String entrezURL = null;
    private String entrezID = null;
    private String refSeqID = null;
    private String refSeqURL = null;
    private ArrayList ishSubmissions;
    private ArrayList maProbes;
    private ArrayList arraySubmissions;
    private ArrayList ontologyTerms;
    private String geneCardURL = null;
    private String hgncSearchSymbolURL = null;
    private String numMicArrays = null;
    private String ucscURL = null;
    protected String iuphar_db_URL = null;
    protected String iuphar_guide_URL = null;

    /**
     * @param value the gene symbol
     */
    public void setSymbol(String value) {
        symbol = value;
	if (null != symbol && symbol.trim().equals(""))
	    symbol = null;
    }

    /**
     * @return the gene symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param value the name of the gene
     */
    public void setName(String value) {
        name = value;
	if (null != name && name.trim().equals(""))
	    name = null;
    }

    /**
     * @return the name of the gene
     */
    public String getName() {
        return name;
    }

    public void setSynonyms(String value) {
        synonyms = value;
	if (null != synonyms && synonyms.trim().equals(""))
	    synonyms = null;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setXsomeStart(String value) {
        xsomeStart = value;
	if (null != xsomeStart && xsomeStart.trim().equals(""))
	    xsomeStart = null;
    }

    public String getXsomeStart() {
        return xsomeStart;
    }

    public void setXsomeEnd(String value) {
        xsomeEnd = value;
	if (null != xsomeEnd && xsomeEnd.trim().equals(""))
	    xsomeEnd = null;
    }

    public String getXsomeEnd() {
        return xsomeEnd;
    }

    public void setXsomeName(String value) {
        xsomeName = value;
	if (null != xsomeName && xsomeName.trim().equals(""))
	    xsomeName = null;
    }

    public String getXsomeName() {
        return xsomeName;
    }

    public void setGenomeBuild(String value) {
        genomeBuild = value;
	if (null != genomeBuild && genomeBuild.trim().equals(""))
	    genomeBuild = null;
    }

    public String getGenomeBuild() {
        return genomeBuild;
    }

    /**
     * @param value MGI Accession ID
     */
    public void setMgiAccID(String value) {
        mgiAccID = value;
	if (null != mgiAccID && mgiAccID.trim().equals(""))
	    mgiAccID = null;
    }

    public String getMgiAccID() {
        return mgiAccID;
    }

    public void setMgiURL(String value) {
        mgiURL = value;
	if (null != mgiURL && mgiURL.trim().equals(""))
	    mgiURL = null;
    }

    public String getMgiURL() {
        return mgiURL;
    }

    public void setEntrezURL(String value) {
        entrezURL = value;
	if (null != entrezURL && entrezURL.trim().equals(""))
	    entrezURL = null;
    }

    public String getEntrezURL() {
        return entrezURL;
    }

    public void setEntrezID(String value) {
        entrezID = value;
	if (null != entrezID && entrezID.trim().equals(""))
	    entrezID = null;
    }

    public String getEntrezID() {
        return entrezID;
    }

    public void setRefSeqURL(String value) {
        refSeqURL = value;
	if (null != refSeqURL && refSeqURL.trim().equals(""))
	    refSeqURL = null;
    }

    public String getRefSeqURL() {
        return refSeqURL;
    }

    public void setRefSeqID(String value) {
        refSeqID = value;
	if (null != refSeqID && refSeqID.trim().equals(""))
	    refSeqID = null;
    }

    public String getRefSeqID() {
        return refSeqID;
    }

    public void setEnsemblURL(String value) {
        ensemblURL = value;
	if (null != ensemblURL && ensemblURL.trim().equals(""))
	    ensemblURL = null;
    }

    public String getEnsemblURL() {
        return ensemblURL;
    }

    public void setEnsemblID(String value) {
        ensemblID = value;
	if (null != ensemblID && ensemblID.trim().equals(""))
	    ensemblID = null;
    }

    public String getEnsemblID() {
        return ensemblID;
    }

    public void setGoURL(String value) {
        goURL = value;
	if (null != goURL && goURL.trim().equals(""))
	    goURL = null;
    }

    public String getGoURL() {
        return goURL;
    }

    public void setOmimURL(String value) {
        omimURL = value;
	if (null != omimURL && omimURL.trim().equals(""))
	    omimURL = null;
    }

    public String getOmimURL() {
        return omimURL;
    }

    public void setIshSubmissions(ArrayList values) {
        ishSubmissions = values;
	if (null != ishSubmissions && 0 == ishSubmissions.size())
	    ishSubmissions = null;
    }

    public ArrayList getIshSubmissions() { 	  	
        return ishSubmissions;
    }

    public void setAssocProbes(ArrayList values) {
        maProbes = values;
	if (null != maProbes && 0 == maProbes.size())
	    maProbes = null;
    }

    public ArrayList getAssocProbes() {
        return maProbes;
    }

    public void setArraySubmissions(ArrayList arraysubs) {
        arraySubmissions = arraysubs;
	if (null != arraySubmissions && 0 == arraySubmissions.size())
	    arraySubmissions = null;
    }

    public ArrayList getArraySubmissions() {
        return arraySubmissions;
    }
    
    public String getGeneCardURL() {
        return geneCardURL;
    }
    
    public void setGeneCardURL(String value){
        geneCardURL = value;
	if (null != geneCardURL && geneCardURL.trim().equals(""))
	    geneCardURL = null;
    }
    
    public String getHgncSearchSymbolURL() {
        return hgncSearchSymbolURL;
    }
    
    public void setHgncSearchSymbolURL(String value){
        hgncSearchSymbolURL = value;
	if (null != hgncSearchSymbolURL && hgncSearchSymbolURL.trim().equals(""))
	    hgncSearchSymbolURL = null;
    }
    
    public String getNumMicArrays() {
        return numMicArrays;
    }
    
    public void setNumMicArrays(String value){
        numMicArrays = value;
	if (null != numMicArrays) {
	    String str = numMicArrays.trim();
	    if (str.equals("") || str.equals("0"))
		numMicArrays = null;
	}
    }
    public ArrayList getOntologyTerms() {
    	return ontologyTerms;
    }
    
    public void setOntologyTerms(ArrayList terms) {
    	ontologyTerms = terms;
	if (null != ontologyTerms && 0 == ontologyTerms.size())
	    ontologyTerms = null;
    }
    
    public String getUcscURL() {
    	return ucscURL;
    }
    
    public void setUcscURL(String ucscURL) {
    	ucscURL = ucscURL;
	if (null != ucscURL && ucscURL.trim().equals(""))
	    ucscURL = null;
    }

    public String getIuphar_db_URL() {
    	return iuphar_db_URL;
    }
    
    public void setIuphar_db_URL(String input) {
    	iuphar_db_URL = input;
	if (null != iuphar_db_URL && iuphar_db_URL.trim().equals(""))
	    iuphar_db_URL = null;
    }

    public String getIuphar_guide_URL() {
    	return iuphar_guide_URL;
    }
    
    public void setIuphar_guide_URL(String input) {
    	iuphar_guide_URL = input;
	if (null != iuphar_guide_URL && iuphar_guide_URL.trim().equals(""))
	    iuphar_guide_URL = null;
    }

}
