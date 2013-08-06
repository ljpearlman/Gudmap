/**
 * 
 */
package gmerg.entities.submission;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class ImageDetail extends ImageInfo {

	  protected String geneSymbol;
	  protected String geneName;
	  protected String age;
	  protected ArrayList allImageNotesInSameSubmission;
	  protected ArrayList allPublicImagesInSameSubmission;

	  public String getGeneSymbol() {
		return geneSymbol;
      }

      public void setGeneSymbol(String input) {
		geneSymbol = input;
	  }

	  public String getGeneName() {
		return geneName;
      }

      public void setGeneName(String input) {
		geneName = input;
	  }

	  public String getAge() {
	    return age;
	  }

	  public void setAge(String input) {
	    age = input;
	  }

	  public ArrayList getAllImageNotesInSameSubmission() {
		  return allImageNotesInSameSubmission;
	  }
	  
	  public void setAllImageNotesInSameSubmission(ArrayList input) {
		  this.allImageNotesInSameSubmission = input;
	  }
	  
	  public ArrayList getAllPublicImagesInSameSubmission() {
		  return allPublicImagesInSameSubmission;
	  }
	  
	  public void setAllPublicImagesInSameSubmission(ArrayList input){
		  allPublicImagesInSameSubmission = input;
	  }

    public String getOptViewerUrl() {
	String ret = clickFilePath;
	String worker= "";
	if (null != geneSymbol && !geneSymbol.trim().equals(""))
	    worker += "%20Gene:"+geneSymbol;
	if (null != stage && !stage.trim().equals(""))
	    worker += "%20Stage:"+stage;
	if (null != accessionId && !accessionId.trim().equals(""))
	    worker += "%20Accession:"+accessionId;

	if (!worker.equals(""))
	    ret +="&viewerTitle="+worker;
	
	return ret;
    }
}
