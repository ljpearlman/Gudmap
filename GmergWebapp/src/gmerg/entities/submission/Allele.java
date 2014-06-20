/**
 * 
 */
package gmerg.entities.submission;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class Allele {
    private boolean debug = false;
    private String title;
    private String geneSymbol;
    private String geneId;
	private String alleleId;
	private String alleleIdUrl;
    // allele symbol
	private String alleleName;
    private String reporter;
    private String visMethod;
    private String notes;
    private String alleleFirstChrom;
    private String alleleSecondChrom;
    private String type;
    
    public void print() {
	System.out.println("title = "+title);
	System.out.println("geneSymbol = "+geneSymbol);
	System.out.println("alleleId = "+alleleId);
	System.out.println("geneId = "+geneId);
	System.out.println("alleleIdUrl = "+alleleIdUrl);
	System.out.println("alleleName = "+alleleName);
	System.out.println("reporter = "+reporter);
	System.out.println("visMethod = "+visMethod);
	System.out.println("notes = "+notes);
	System.out.println("alleleFirstChrom = "+alleleFirstChrom);
	System.out.println("alleleSecondChrom = "+alleleSecondChrom);
	System.out.println("type = "+type);
    }

    public String getType() {
    	return type;
    }
    
    public void setType(String input) {
    	type = input;
    }

    public String getTitle() {
    	return title;
    }
    
    public void setTitle(String input) {
    	title = input;
    }
    
    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String value) {
        geneSymbol = value;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String value) {
        geneId = value;
    }

    public String getAlleleId() {
	if (debug)
	    System.out.println("Allele.alleleId = "+alleleId);
    	return alleleId;
    }
    
    public void setAlleleId(String input) {
    	alleleId = input;
	// if input is MGI accession, hardwire its url
	// otherwise, input cantains url
	if (null != input && -1 != input.indexOf("MGI:"))
    	    setAlleleIdUrl("http://www.informatics.jax.org/accession/" + input);

    }

    // Bernie 25/11/2010 - added new getter (mantis 336)
    public String getAlleleIdUrl() {
	if (debug)
	    System.out.println("Allele.alleleIdUrl = "+alleleIdUrl);
        return alleleIdUrl;
    }

    // Bernie 25/11/2010 - added new getter (mantis 336)
    public void setAlleleIdUrl(String value) {
        alleleIdUrl = value;
    }

    public String getAlleleName() {
    	return alleleName;
    }
    
    public void setAlleleName(String input) {
    	alleleName = input;
	if (null != alleleName &&
	    -1 != alleleName.indexOf("<")) {
		alleleName = alleleName.replace("<", "##1");
		alleleName = alleleName.replace(">", "##2");
		alleleName = alleleName.replace("##1", "<sup>");
		alleleName = alleleName.replace("##2", "</sup>");
		/*alleleName = alleleName.replace("<", "&lt;sup&gt;");
		alleleName = alleleName.replace(">", "&lt;/sup&gt;");*/
	}

    }
    
    public String getReporter() {
        return reporter;
    }

    public void setReporter(String value) {
        reporter = value;
    }

    public String getVisMethod() {
        return this.visMethod;
    }

    public void setVisMethod(String value) {
        this.visMethod = value;
    }

    public String getNotes() {
	if (debug)
	    System.out.println("Allele.notes = "+notes);
        return notes;
    }

    public void setNotes(String nts) {
        notes = nts;
        if (null != notes) {
           int index = notes.indexOf(";");
           if (-1 != index) {
	     String str1 = notes.substring(0, index);
	     String str2 = notes.substring(index + 1);
	     index = str1.indexOf(">");
	     if (-1 != index)
		 notes = str1.substring(0, index + 1)+geneSymbol+" "+str1.substring(index+1)+"&nbsp;&#38;";
	     else
		 notes = str1+"&nbsp;&#38;";
	     index = str2.indexOf(">");
	     if (-1 != index)
		 notes = notes + str2.substring(0, index + 1)+geneSymbol+" "+str2.substring(index+1);
	     else
		 notes = notes + str2;
	   }
	}
    }
    
    public String getAlleleFirstChrom() {
    	return alleleFirstChrom;
    }
    
    public void setAlleleFirstChrom(String input) {
    	alleleFirstChrom = input;
	if (null != alleleFirstChrom &&
	    -1 != alleleFirstChrom.indexOf("<")) {
		alleleFirstChrom = alleleFirstChrom.replace("<", "##1");
		alleleFirstChrom = alleleFirstChrom.replace(">", "##2");
		alleleFirstChrom = alleleFirstChrom.replace("##1", "<sup>");
		alleleFirstChrom = alleleFirstChrom.replace("##2", "</sup>");
	}
    }
    
    public String getAlleleSecondChrom() {
    	return alleleSecondChrom;
    }
    
    public void setAlleleSecondChrom(String input) {
    	alleleSecondChrom = input;
	if (null != alleleSecondChrom &&
	    -1 != alleleSecondChrom.indexOf("<")) {
		alleleSecondChrom = alleleSecondChrom.replace("<", "##1");
		alleleSecondChrom = alleleSecondChrom.replace(">", "##2");
		alleleSecondChrom = alleleSecondChrom.replace("##1", "<sup>");
		alleleSecondChrom = alleleSecondChrom.replace("##2", "</sup>");
	}
    }
}
