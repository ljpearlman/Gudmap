package gmerg.beans;

import java.util.ArrayList;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.model.ClipboardDelegateCookieImp;
import gmerg.model.CollectionFileParser;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

public class CollectionInformationBean {
    private boolean debug = false;

	boolean renderOverwrite;
	UIInput collectionTypeComponent;
	UIInput collectionIdComponent;
	CollectionInfo collectionInfo;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionInformationBean() {
	    if (debug)
		System.out.println("CollectionInformationBean.constructor");

		collectionInfo = (CollectionInfo)FacesUtil.getFacesRequestParamObject("collectionInfo");
		if (collectionInfo==null) {
			collectionInfo =  new CollectionInfo();
			collectionInfo.setId(-1);
			collectionInfo.setName("");
			collectionInfo.setDescription("");
			collectionInfo.setStatus(1);
			collectionInfo.setFocusGroup(0);
		}
		collectionInfo.setType(Integer.parseInt(Utility.getValue(FacesUtil.getAnyRequestParamValue("collectionType"), "0")));
		renderOverwrite = Boolean.parseBoolean(Utility.getValue(FacesUtil.getAnyRequestParamValue("renderOverwite"), "false"));
	}

	// ********************************************************************************
	// Action methods & validators
	// ********************************************************************************
	public String reset() {		// Immediate
		FacesUtil.setFacesRequestParamValue("collectionType", getCollectionTypeFromHiddenComponent());
		collectionInfo.setType(Integer.parseInt(getCollectionTypeFromHiddenComponent()));
		collectionInfo.setId(Integer.parseInt(getCollectionIdFromHiddenComponent()));
		return "CollectionInformation"; //although it renders the same page (ie colud have been return null) this way it will enforce a new view and therefore clearing up all the values;
	}
	
	public String cancelOverwrite() {
		renderOverwrite = false;
		return null;
	}
				  
	public String overwriteCollection() {
		saveCollection(true);
		return navigateToCollectionPage();
	}
	
	public String storeCollection() {
		CollectionInfo databaseCollection = CollectionAssembler.instance().getCollectionInfo(collectionInfo.getName(), Utility.getUser().getUserId());
		if (databaseCollection==null || (isModifying() && databaseCollection.getId()==collectionInfo.getId())) {
			saveCollection(false);
			return navigateToCollectionPage();
		}
		renderOverwrite = true;
		return null; 
	}
	
	public String cancel() { 	// Immediate
		collectionInfo.setType(Integer.parseInt(getCollectionTypeFromHiddenComponent()));
		collectionInfo.setId(Integer.parseInt(getCollectionIdFromHiddenComponent()));
		return navigateToCollectionPage();
	}
	
	public void validateCollectionName(FacesContext context, UIComponent component, Object value) {
		String collectionName = ((String)value).trim();
		FacesMessage message = null;
		if (collectionName.equals(""))
			message = new FacesMessage("A name must be specified for collection", 
									   "A name must be specified for collection");
		else if (collectionName.length()>60) 
				message = new FacesMessage("A collection name should be no more than 60 characters long.", 
											"A collection name should be no more than 60 characters long.");
		else if (!CollectionFileParser.isCollectionNameValid(collectionName))
			message = new FacesMessage("Valid characters for a name are: letters digits _ -", 
										"Valid characters for a name are: letters digits _ -");
		if (message!=null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	// ********************************************************************************
	// Private methods 
	// ********************************************************************************
	private void saveCollection(boolean overwrite) {
		collectionInfo.setOwner(Utility.getUser().getUserId());
		String currentDate = Utility.convertToDatabaseDate(new Date());
		collectionInfo.setLastUpdate(currentDate);
		if (isModifying()) { 
			CollectionAssembler.instance().updateCollectionSummary(collectionInfo, overwrite);
			return;
		}
		ArrayList<String> ids = ClipboardDelegateCookieImp.getClicpboardIds(collectionInfo.getType());
		CollectionAssembler.instance().insertCollection(collectionInfo, ids, overwrite);
	}

	private String navigateToCollectionPage() {
		if (isModifying())
			FacesUtil.setFacesRequestParamValue("collectionId", collectionInfo.getId());
		else
			FacesUtil.setFacesRequestParamValue("collectionId", "clipboard");
		FacesUtil.setFacesRequestParamValue("collectionType", String.valueOf(collectionInfo.getType()));
		return "collectionBrowse";
	}

	private String getCollectionTypeFromHiddenComponent() { 
		// because this action is immediate bean value for collectionComponet wouldn't be updated
		String collectionTypeComponentValue = (String)collectionTypeComponent.getSubmittedValue(); 
		if (collectionTypeComponentValue==null || "".equals(collectionTypeComponentValue)) {
			System.out.println("ERROR! in CollectionInformationBean; collectionType value is null");
			return "0";
		}
		else
			return collectionTypeComponentValue;
	}

	private String getCollectionIdFromHiddenComponent() { 
		// because this action is immediate bean value for collectionComponet wouldn't be updated
		String collectionIdComponentValue = (String)collectionIdComponent.getSubmittedValue(); 
		if (collectionIdComponentValue==null || "".equals(collectionIdComponentValue)) {
			System.out.println("ERROR! in CollectionInformationBean; collectionId value is null");
			return "-1";
		}
		else
			return collectionIdComponentValue;
	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public SelectItem[] getGroupItems() {
		String[] focusGroups = Globals.getFocusGroups();
		int n = focusGroups.length;
		SelectItem[] groupItems = new SelectItem[n];
		for (int i=0; i<n; i++) 
			groupItems[i] = new SelectItem(i, focusGroups[i]);
		return groupItems;
	}

	public String getType() {
		return Globals.getCollectionCategoryName(collectionInfo.getType());
	}

	public boolean isRenderOverwrite() {
		return renderOverwrite;
	}

	public UIInput getCollectionTypeComponent() {
		return collectionTypeComponent;
	}

	public void setCollectionTypeComponent(UIInput collectionTypeComponent) {
		this.collectionTypeComponent = collectionTypeComponent;
	}
	
	public String getOverwriteVisibility() {   //This is a work around because setting render attribute caused problem with action methods not rendered at first time
		return renderOverwrite? "visible":"hidden";
	}

	public UIInput getCollectionIdComponent() {
		return collectionIdComponent;
	}

	public void setCollectionIdComponent(UIInput collectionIdComponent) {
		this.collectionIdComponent = collectionIdComponent;
	}

	public CollectionInfo getCollectionInfo() {
		return collectionInfo;
	}

	public boolean isModifying() {
		if (collectionInfo == null) {
			System.out.println("ERROR! in CollectionInformationBean:isModifying; collectionInfo is null");
			return false;
		}
		return collectionInfo.getId()>=0;
	}
}
