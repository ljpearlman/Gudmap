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
    private boolean debug = false;

    private String collectionId;
    private int collectionType;
    private CollectionInfo collectionInfo;  //lazy getter
    private CollectionBrowseHelper browseHelper;  //lasy getter
	
	public CollectionBrowseBean() {
	    if (debug)
		System.out.println("CollectionBrowseBean.constructor");

		collectionInfo = null;
		
		if (Utility.isUserLoggedIn()) //not logged in users only able to see clipboard
			collectionId = Visit.getRequestParam("collectionId", "clipboard");
		else
			collectionId = "clipboard";
		
		if (isClipboard())
			collectionType = Integer.parseInt(Visit.getRequestParam("collectionType", "0"));
		else
			collectionType = getCollectionInfo().getType(); 
		
		if (debug)
		    System.out.println("CollectionBrowseBean collectionId = "+collectionId+" collectionType = "+collectionType);

		browseHelper = null;	// has a lasy getter
		String actionMethodCalled = FacesUtil.getRequestParamValue("actionMethodCalled");
		if(actionMethodCalled==null)
			initTemplate();
	}
	
	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	public String reload() {
	    if (debug)
		System.out.println("CollectionBrowseBean.reload");

		initTemplate();
		return null;
	}
	
	public String emptyCollection() {
	    if (debug)
		System.out.println("CollectionBrowseBean.emptyCollection");

		if (isClipboard()) {
			ClipboardDelegateCookieImp.removeAllValuesFromClipboard(collectionType);
			getBrowseHelper().setDynamicCollectionIds(null);
		}
		return null;
	}
	
	public void removeSelectedItems() {
	    if (debug)
		System.out.println("CollectionBrowseBean.removeSelectedItems");

	    if (isClipboard()) {
		String[] selectedIds = TableUtil.getSelectedIdsForCollection(collectionType);

		ArrayList<String> newIds = ClipboardDelegateCookieImp.removeValuesFromClipboard(collectionType, selectedIds);
		getBrowseHelper().setDynamicCollectionIds(newIds);
	    }
	}

	public String saveCollection() {
	    if (debug)
		System.out.println("CollectionBrowseBean.saveCollection");

		FacesUtil.setFacesRequestParamValue("collectionType", String.valueOf(collectionType));
		return "CollectionInformation";
	}
	
	public String modifyCollection() {
	    if (debug)
		System.out.println("CollectionBrowseBean.modifyCollection");

		FacesUtil.setFacesRequestParamValue("collectionInfo", collectionInfo);
		FacesUtil.setFacesRequestParamValue("collectionType", String.valueOf(collectionType));
		return "CollectionInformation";
	}
	
	public String downloadCollection() {		// action method
	    if (debug)
		System.out.println("CollectionBrowseBean.downloadCollection");

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
		System.out.println("CollectionBrowseBean.downloadCollection");

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
		
				
		if (collectionType == 3)
			Visit.setStatusParam("platformId", "GPL1261");
		if (collectionType == 4)
			Visit.setStatusParam("platformId", "GPL6246");
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
		if (null != collectionInfo && collectionInfo.getStatus()==0)
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
		
		int fg = 0;
		if (!isClipboard())			
			fg = getCollectionInfo().getFocusGroup();

		if (browseHelper == null)
			return Globals.getCollectionBrowseHelper(getCollectionIds(), collectionType, fg);
		return browseHelper;
	}

}
