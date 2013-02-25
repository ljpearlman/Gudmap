/**
 * 
 */
package gmerg.entities.submission;

import java.util.ArrayList;

/**
 * @author xingjun
 * <p>modified by xingjun - 13/02/2009 - added a new property: pyramidTileSize</p>
 *
 */
public class ImageDetail {

	  private String accessionId;
	  private String geneSymbol;
	  private String geneName;
	  private String stage; // theiler stage
	  private String age;
	  private String assayType;
	  private String specimenType;
	  private String filePath;
	  private String imageName;
	  private String serialNo;
	  private ArrayList allImageNotesInSameSubmission;
	  private ArrayList allPublicImagesInSameSubmission;
	  private String pyramidTileSize;
	  

	  public String getAccessionId() {
		return accessionId;
      }

      public void setAccessionId(String accessionId) {
		this.accessionId = accessionId;
	  }

	  public String getGeneSymbol() {
		return geneSymbol;
      }

      public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	  }

	  public String getGeneName() {
		return geneName;
      }

      public void setGeneName(String geneName) {
		this.geneName = geneName;
	  }

	  public String getStage() {
	    return stage;
	  }

	  public void setStage(String embryoStage) {
	    this.stage = embryoStage;
	  }

	  public String getAge() {
	    return age;
	  }

	  public void setAge(String givenStage) {
	    this.age = givenStage;
	  }

	  public String getAssayType() {
	    return assayType;
	  }
	  
	  public void setAssayType(String assayType) {
	    this.assayType = assayType;
	  }

	  public String getSpecimenType() {
	    return specimenType;
	  }
		  
	  public void setSpecimenType(String specimenType) {
	    this.specimenType = specimenType;
	  }

	  public String getFilePath() {
	    return filePath;
	  }
		  
	  public void setFilePath(String filePath) {
	    this.filePath = filePath;
	  }

	  public String getImageName() {
	    return imageName;
	  }
		  
	  public void setImageName(String imageName) {
	    this.imageName = imageName;
	  }

	  public String getSerialNo() {
	    return serialNo;
	  }
		  
	  public void setSerialNo(String serialNo) {
	    this.serialNo = serialNo;
	  }
	  
	  public ArrayList getAllImageNotesInSameSubmission() {
		  return allImageNotesInSameSubmission;
	  }
	  
	  public void setAllImageNotesInSameSubmission(ArrayList allImageNotesInSameSubmission) {
		  this.allImageNotesInSameSubmission = allImageNotesInSameSubmission;
	  }
	  
	  public ArrayList getAllPublicImagesInSameSubmission() {
		  return allPublicImagesInSameSubmission;
	  }
	  
	  public void setAllPublicImagesInSameSubmission(ArrayList allPublicImagesInSameSubmission){
		  this.allPublicImagesInSameSubmission = allPublicImagesInSameSubmission;
	  }
	  
	  public String getPyramidTileSize() {
		  return pyramidTileSize;
	  }
	  
	  public void setPyramidTileSize(String pyramidTileSize) {
		  this.pyramidTileSize = pyramidTileSize;
	  }
	  
	  public String getImageId() {
		  return accessionId.substring(7) + "_" + this.imageName;
	  }
}
