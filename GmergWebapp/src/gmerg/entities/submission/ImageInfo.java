/**
 * 
 */
package gmerg.entities.submission;

public class ImageInfo {

	protected String accessionId;
	protected String stage; // theiler stage
	protected String specimenType;
	protected String filePath;
	protected String clickFilePath;
	protected String serialNo;
	protected String note;

	public void print() {
		System.out.println(" accessionId = "+ accessionId);
		System.out.println(" stage = "+stage);
		System.out.println(" specimenType = "+ specimenType);
		System.out.println(" filePath = "+filePath);
		System.out.println(" clickFilePath = "+ clickFilePath);
		System.out.println(" serialNo = "+serialNo);
		System.out.println(" note = "+note);
	}

    public String getAccessionId() {
	return accessionId;
    }
    
    public void setAccessionId(String input) {
	accessionId = input;
    }
    
    public String getStage() {
	return stage;
    }

	  public void setStage(String input) {
	    stage = input;
	  }

	  public String getSpecimenType() {
	    return specimenType;
	  }
		  
	  public void setSpecimenType(String input) {
	    specimenType = input;
	  }

	  public String getFilePath() {
	    return filePath;
	  }
		  
	  public void setFilePath(String input) {
	    filePath = input;
	  }

	  public String getClickFilePath() {
	      // do not know why zoom-viewer dose not work for microarray tif 
	      // so special put 'microarray' into specimenType so that
	      // microarray click image can be treated specially
	      if (null != specimenType && specimenType.equals("microarray")){
	    	  
//	    	  System.out.println("getClickFilePath1 = "+ filePath);
	    	  return filePath;
	      }

	      if (null == clickFilePath || clickFilePath.endsWith("tif")) {
			  String ret = gmerg.utils.Utility.appUrl+"pages/zoom_viewer.html?id="+accessionId;
			  if (null == serialNo)
			      ret += "&serialNo=1";
			  else
			      ret =ret + "&serialNo="+serialNo;

//		      System.out.println("getClickFilePath2 = "+ ret);
			  
			  return ret;
	      }
	      
//	      System.out.println("getClickFilePath3 = "+ clickFilePath);
	      return clickFilePath;
	  }
		  
	  public void setClickFilePath(String input) {
//		System.out.println("setClickFilePath = "+ input);
	    clickFilePath = input;
	  }

	  public String getSerialNo() {
	    return serialNo;
	  }
		  
	  public void setSerialNo(String input) {
	    serialNo = input;
	    if (null != serialNo) {
			serialNo = serialNo.trim();
			if (serialNo.equals(""))
			    serialNo = null;
		    }
	  }

	  public String getNote() {
	    return note;
	  }
		  
	  public void setNote(String input) {
	    note = input;
	    if (null == note || note.trim().toLowerCase().equals("null"))
	    	note = "";
	  }
}
