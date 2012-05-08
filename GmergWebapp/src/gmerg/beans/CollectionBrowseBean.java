package gmerg.beans;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.model.ClipboardDelegateCookieImp;
import gmerg.utils.FacesUtil;
import gmerg.utils.FileHandler;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.table.*;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

public class CollectionBrowseBean {

    private String collectionId;
    private int collectionType;
    private CollectionInfo collectionInfo;  //lazy getter
    private CollectionBrowseHelper browseHelper;  //lasy getter
	
	public CollectionBrowseBean() {
		collectionInfo = null;
		
		if (Utility.isUserLoggedIn()) //not logged in users only able to see clipboard
			collectionId = Visit.getRequestParam("collectionId", "clipboard");
		else
			collectionId = "clipboard";
		
		if (isClipboard())
			collectionType = Integer.parseInt(Visit.getRequestParam("collectionType", "0"));
		else
			collectionType = getCollectionInfo().getType(); 
		
		browseHelper = null;	// has a lasy getter
		String actionMethodCalled = FacesUtil.getRequestParamValue("actionMethodCalled");
		if(actionMethodCalled==null)
			initTemplate();
	}
	
	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	public String reload() {
		initTemplate();
		return null;
	}
	
	public String emptyCollection() {
		if (isClipboard()) {
			ClipboardDelegateCookieImp.removeAllValuesFromClipboard(collectionType);
			getBrowseHelper().setDynamicCollectionIds(null);
		}
		return null;
	}
	
	public String removeSelectedItems() {
		String[] selectedIds = TableUtil.getSelectedIdsForCollection(collectionType);
		if (isClipboard()) {
			ArrayList<String> newIds = ClipboardDelegateCookieImp.removeValuesFromClipboard(collectionType, selectedIds);
			getBrowseHelper().setDynamicCollectionIds(newIds);
		}
		return null;
	}

	public String saveCollection() {
		FacesUtil.setFacesRequestParamValue("collectionType", String.valueOf(collectionType));
		return "CollectionInformation";
	}
	
	public String modifyCollection() {
		FacesUtil.setFacesRequestParamValue("collectionInfo", collectionInfo);
		FacesUtil.setFacesRequestParamValue("collectionType", String.valueOf(collectionType));
		return "CollectionInformation";
	}
	
	public String downloadCollection() {		// action method
		downloadCollection(collectionId, collectionType);
		return null;
	}
	
	// ********************************************************************************
	// Static methods
	// ********************************************************************************
	public static void downloadCollection(int collectionId) {
		downloadCollection(String.valueOf(collectionId), -1);
	}
	
	public static void downloadCollection(String collectionId, int collectionType) {
		String outputString = "";
		String fileName;
		if ("clipboard".equalsIgnoreCase(collectionId)) {
			//Add header 
			outputString += "name = clipboard\n";
			outputString += "type = " + Globals.getCollectionCategoryName(collectionType).toLowerCase() + "\n";
			ArrayList<String> ids = ClipboardDelegateCookieImp.getClicpboardIds(collectionType);
			if (ids.size() == 0)	//empty clipboard
				return;
			//Add collection items IDs 
			for (int i = 0; i < ids.size(); i++)
				outputString += ((i == 0) ? "" : "\t") + ids.get(i);
			fileName = Globals.getCollectionCategoryName(collectionType).toLowerCase() + "_clipboard.txt";
		}
		else {
			CollectionInfo collectionInfo = CollectionAssembler.instance().getCollectionInfo(Integer.parseInt(collectionId));
			outputString = collectionInfo.getAllInfo();
			fileName = "collection_" + collectionInfo.getName() + ".txt";
		}
		
		// Save in a file
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		String contentType = "application/text;charset=iso-8859-1";
		String header = "attachment;filename=\""+ fileName + "\"";
		FileHandler.saveStringToDesktop(response, outputString, header, contentType);
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public static SelectItem[] getCollectionCategoriesItem() {
		String[] collectionCategories = Globals.getCollectionCategoriesNames();
		int n = collectionCategories.length;
		SelectItem[] categories = new SelectItem[n];
		for (int i=0; i<n; i++) 
			categories[i] = new SelectItem(i, collectionCategories[i]);
		return categories;
	}

/*	Moved to Globals
 
	public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType) {
		switch (collectionType) {
			case 0:	return new EntriesCollectionBrowseHelper(collectionItemsIds);
			case 1:	return new GenesCollectionBrowseHelper(collectionItemsIds);
			case 2:	return new ImagesCollectionBrowseHelper(collectionItemsIds);
			case 3:	return new ProbesCollectionBrowseHelper(collectionItemsIds, "GPL1261"); 
			case 4:	return new ProbesCollectionBrowseHelper(collectionItemsIds, "GPL6246");
		}	
		return null;
	}
*/
	
	// ********************************************************************************
	// Private methods 
	// ********************************************************************************
	protected void initTemplate() {
		String viewName = "storedCollectionBrowse";
		if(isClipboard())
			viewName = "clipboardBrowse";
		
		if (TableUtil.isTableViewInSession())
			return;
       	TableUtil.saveTableView(populateCollectionBrowseTableView(viewName));

       	// saveStatus
		Visit.setStatusParam("collectionId", collectionId);
		Visit.setStatusParam("collectionType", collectionType);
	}
	
	protected ArrayList<String> getCollectionIds() {
		ArrayList<String> collectionIds = new ArrayList<String>();
		if (isClipboard())
			collectionIds = ClipboardDelegateCookieImp.getClicpboardIds(collectionType);
		else //Stored collection
			collectionIds = CollectionAssembler.instance().getCollectionItems(Integer.parseInt(collectionId));
		
		return collectionIds;
	}

	protected GenericTableView populateCollectionBrowseTableView(String viewName) {
		GenericTableView tableView;
		CollectionBrowseHelper collectionBrowseHelper = getBrowseHelper();
		if (collectionBrowseHelper==null)
			return null;
		tableView = collectionBrowseHelper.getCollectionTableView(viewName);
		if(isClipboard()) {
			tableView.setCollectionBottons(0);
			tableView.setNoDataMessage("Your " + Globals.getCollectionCategoryName(collectionType) + " is empty");
		}
		else 
			tableView.setCollectionBottons(2); 
		
		return  tableView;
	}
	
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public CollectionInfo getCollectionInfo() {
		if (collectionInfo == null)
			collectionInfo = CollectionAssembler.instance().getCollectionInfo(Integer.parseInt(collectionId));
		return collectionInfo;
	}

	public boolean isClipboard() {
		return  "clipboard".equals(collectionId);
	}
	
	public int getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(int i) {
		collectionType = i;
	}
	
	public SelectItem[] getCollectionCategories() {
		return getCollectionCategoriesItem();
	}

	public boolean isUserOwnedCollection() {
		return getCollectionInfo().getOwner() == Utility.getUser().getUserId();
	}
	
	public boolean isShowRemoveButtons() {	//remove, empty
		return isClipboard() || isUserOwnedCollection();
	}
	
	public String getClipboardName() {
		return Globals.getCollectionCategoriesNames()[collectionType];
	}
	
	public int getClipboardItemsNum() {
		if (collectionType == 0)
			return 12;
		return 8;
	}
	
	public String getCollectionName() {
		if (isClipboard())
			return " my " + Globals.getCollectionCategories()[collectionType].getCategory();
		
		return getCollectionInfo().getName();
	}
	
	public String getStatus() {
		if (collectionInfo.getStatus()==0)
			return "private";
		return "public";
	}
	
	public String getFocusGroup() {
		return Globals.getFocusGroup(getCollectionInfo().getFocusGroup());
	}

	public String getCollectionId() {
		return collectionId;
	}

	public CollectionBrowseHelper getBrowseHelper() {
//		String fg = getFocusGroup();
//		int focusGroup = 1;
//		if (fg.equalsIgnoreCase("metanephros"))
//			focusGroup = 1;
//		if (fg.equalsIgnoreCase("early reproductive system"))
//			focusGroup = 2;
//		if (fg.equalsIgnoreCase("male reproductive system"))
//			focusGroup = 3;
//		if (fg.equalsIgnoreCase("female reproductive system"))
//			focusGroup = 4;
//		if (fg.equalsIgnoreCase("lower urinary tract"))
//			focusGroup = 5;
		
		if (browseHelper == null)
			return Globals.getCollectionBrowseHelper(getCollectionIds(), collectionType);//, focusGroup);
		return browseHelper;
	}

}
