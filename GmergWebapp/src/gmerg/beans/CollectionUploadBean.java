package gmerg.beans;

import java.util.ArrayList;
import java.util.Date;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;
import gmerg.model.CollectionFileParser;
import gmerg.utils.Utility;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.myfaces.custom.fileupload.HtmlInputFileUpload;

public class CollectionUploadBean {
    private boolean debug = false;

    private UploadedFile uploadedFile;
	private boolean renderOverwrite;
	private String collectionName;
    private HtmlInputFileUpload uploadedFileComponent;


	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionUploadBean() {
	    if (debug)
		System.out.println("CollectionUploadBean.constructor");

		renderOverwrite = false;
		collectionName = "";
	}
	
	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	public String uploadCollection() {
		return uploadCollection(false);
	}
	
	public String overwriteCollection() {
		return uploadCollection(true);
	}
	
	public String cancelOverwrite() {
		renderOverwrite = false;
		return null;
	}
				  
	public String cancelUpload() {
		return "collectionList";
	}
	
	// ********************************************************************************
	// Private methods 
	// ********************************************************************************
	/**
	 * <p>xingjun - 22/10/2009 - typo of message have been corrected</p>
	 */
	private String uploadCollection(boolean overwrite) {
		CollectionFileParser parser = new CollectionFileParser();
		if(!parser.readFile(uploadedFile)) {
			String message = "<h3>Error uploading collection from file:</h3>\n" + parser.getErrorMessage();
			return MessageBean.showMessage(message, "collectionList");
			
		}
		int parserResult = parser.parse();
		if (parserResult <= 1) {
			CollectionInfo collectionInfo = parser.getCollectionInfo();
			String parserWarningMessage = null;
			if (parserResult > 0)
				parserWarningMessage = parser.getErrorMessage();
			
			if(overwrite)
				return saveCollection(collectionInfo, parser.getIds(), parserWarningMessage);
			
			CollectionInfo databaseCollection = CollectionAssembler.instance().getCollectionInfo(collectionInfo.getName(), Utility.getUser().getUserId());
			if(databaseCollection==null || !databaseCollection.getName().equalsIgnoreCase(collectionInfo.getName()))  
				return saveCollection(collectionInfo, parser.getIds(), parserWarningMessage);
			
			collectionName = collectionInfo.getName();
			renderOverwrite = true;
			// return null renders the same page so the bean won't instantiate again; if this was't the case (i.e going through navigation) 
			// then uploadedFile should'v passed as a parameter
			uploadedFileComponent.setUploadedFile(uploadedFile);
			return null; 
		}
		else { // Parser error
//			System.out.println("Error report:\n"+parser.getErrorMessage());
			String message = parser.getErrorMessage();
			message += "\n <em> Your collection file can not be uploaded. Please address the above problems. </em>";
			return MessageBean.showMessage("<h3>Error uploading collection from file:</h3>\n" + message, "collectionList");
		}
	}
	
	private String saveCollection(CollectionInfo collectionInfo, ArrayList<String> ids, String parserWarningMessage) {
		collectionInfo.setOwner(Utility.getUser().getUserId());
		String currentDate = Utility.convertToDatabaseDate(new Date());
		collectionInfo.setLastUpdate(currentDate);
		String errorMessage = CollectionAssembler.instance().insertCollection(collectionInfo, ids, true);
		if (errorMessage==null || "".equals(errorMessage))
			if (parserWarningMessage == null)  
				return "collectionList";
			else {		// Parser warning
				String message = parserWarningMessage;
				message += "\n <em> Your collection file has been uploaded. </em>";
				return MessageBean.showMessage(message, "collectionList");
			}
		else {
			errorMessage = "<h3>Your collection file can not be uploaded:</h3>\n\n<em>" + errorMessage + "</em>";
			if (parserWarningMessage != null)
				errorMessage += "\n" + parserWarningMessage; 
			return MessageBean.showMessage(errorMessage, "collectionList");
		}
	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getOverwriteVisibility() { 
		return renderOverwrite? "visible":"hidden";
	}

	public String getCollectionName() {
		return collectionName;
	}

	public boolean isRenderOverwrite() {
		return renderOverwrite;
	}

	public HtmlInputFileUpload getUploadedFileComponent() {
		return uploadedFileComponent;
	}

	public void setUploadedFileComponent(HtmlInputFileUpload uploadedFileComponent) {
		this.uploadedFileComponent = uploadedFileComponent;
	}

}
