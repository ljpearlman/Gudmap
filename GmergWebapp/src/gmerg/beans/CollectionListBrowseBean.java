package gmerg.beans;

import java.util.*;
import javax.faces.model.SelectItem;

import gmerg.assemblers.CollectionAssembler;
import gmerg.assemblers.CollectionListAssembler;
import gmerg.entities.Globals;
import gmerg.entities.Globals.CollectionCategory;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.table.*;


public class CollectionListBrowseBean {
    private boolean debug = false;

    private boolean displayOwns;
    private boolean displayPublics;
    private int collectionType;
    private int collectionAttribute;
    
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionListBrowseBean() {
	    if (debug)
		System.out.println("CollectionListBrowseBean.constructor");

		displayOwns = Boolean.parseBoolean(Visit.getRequestParam("displayOwns", "true"));
		displayPublics = Boolean.parseBoolean(Visit.getRequestParam("displayPublics", "true"));
		collectionType = Integer.parseInt(Visit.getRequestParam("collectionType", "0"));
		String actionMethodCalled = FacesUtil.getRequestParamValue("actionMethodCalled");
		if(actionMethodCalled==null)
			initTemplate();
	}

	// ********************************************************************************
	// Private methods 
	// ********************************************************************************
	private void initTemplate() {
		// saveStatus 
		Visit.setStatusParam("displayOwns", displayOwns);
		Visit.setStatusParam("displayPublics", displayPublics);
		Visit.setStatusParam("collectionType", collectionType);
		String viewName = "collectionListBrowse";
		if (TableUtil.isTableViewInSession())
			return;
		GenericTableView tableView = populatecollectionListBrowseTableView(viewName); 
       	TableUtil.saveTableView(tableView);
	}

	private GenericTableView populatecollectionListBrowseTableView(String viewName) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		int status = (displayOwns?1:0) + (displayPublics?1:-1); //0)Owns 1)Others 2)Both 
		//System.out.println("--------->status="+status);		
		queryParams.put("userId", Utility.getUser().getUserId());
		queryParams.put("collectionType", collectionType);
		queryParams.put("status", status);
		
		OffMemoryTableAssembler assembler = new CollectionListAssembler(queryParams);
	    GenericTable table = assembler.createTable();

	    GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(0);
		//tableView.setColHidden(0, true); // Hide Id column from display 
		tableView.setColHidden(1, true); // Hide Id column from display DEREK
		tableView.setColHidden(3, true); // Hide OwnerId column from display 
		tableView.setDisplayTotals(false);
		tableView.setColVisible(10, false);
		tableView.setColLeftAligned(1);
		tableView.setColLeftAligned(2);
		tableView.setColLeftAligned(4);
		tableView.setColLeftAligned(5);
		
		return  tableView;
	}

	// ********************************************************************************
	// Action methods
	// ********************************************************************************
	public String reload() {
		initTemplate();
		return null;
	}
	
	public String removeCollections() {
		String[] selectedIds = TableUtil.getSelectedIds(0);
		String result = CollectionAssembler.instance().removeCollections(selectedIds, Utility.getUser().getUserId());
		TableUtil.getTableViewFromSession().refreshTable();
		if (result==null)
			return null;
		
//		return MessageBean.showMessage("<h3>Warning:</h3>\n"+result, "collectionList_browse.jsp");
		return MessageBean.showMessage("<h3>Warning:</h3>\n"+result);
	}
	
	public String downloadCollection() {
		HashMap<String,String> params = TableUtil.getClickedLinkParams();
		String collectionId = params.get("collectionId");
		CollectionBrowseBean.downloadCollection(Integer.parseInt(collectionId));
		return null;
	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public boolean isDisplayOwns() {
		return displayOwns;
	}

	public void setDisplayOwns(boolean displayPrivates) {
		this.displayOwns = displayPrivates;
	}

	public boolean isDisplayPublics() {
		return displayPublics;
	}

	public void setDisplayPublics(boolean displayPublics) {
		this.displayPublics = displayPublics;
	}
	
	public int getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(int collectionType) {
		FacesUtil.setFacesRequestParamValue("collectionType", String.valueOf(collectionType));
		//Visit.setStatusParam("collectionType", collectionType);
		this.collectionType = collectionType;
	}

	public SelectItem[] getCollectionCategories() {
		return CollectionBrowseBean.getCollectionCategoriesItem();
	}

	public SelectItem[] getCollectionAttributes() {
		CollectionCategory collectionCategory = Globals.getCollectionCategory(collectionType);
		return collectionCategory.getAttributesSelectItems();
	}
	
	public boolean isAttributes() {
		return Globals.getCollectionCategory(collectionType).getAttributes().size()>0;
	}

	public int getCollectionAttribute() {
		return collectionAttribute;
	}

	public void setCollectionAttribute(int collectionAttribute) {
		this.collectionAttribute = collectionAttribute;
	}
	
	
}
