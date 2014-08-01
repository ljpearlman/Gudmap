package gmerg.beans;

import java.util.ArrayList;

import gmerg.entities.Globals;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.table.*;

abstract public class CollectionOperationBrowseParentBean {
    protected boolean debug = true;

    protected int collectionType;
    protected String collectionOperation;
    protected String selectedCollectionsString;
    protected String collectionNames;
    protected String collectionAttribute;
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionOperationBrowseParentBean() {
	    if (debug)
		System.out.println("CollectionOperationBrowseParentBean.constructor");
		
		GenericTableView tableView;
		if (TableUtil.isTableViewInSession()) {
			tableView = TableUtil.getTableViewFromSession();
			collectionOperation = (String)tableView.getViewParameter("collectionOperation");
			collectionType = (Integer)tableView.getViewParameter("collectionType");
			selectedCollectionsString = (String)tableView.getViewParameter("selectedCollections");
			collectionAttribute = (String)tableView.getViewParameter("collectionAttribute");
			collectionNames = (String)tableView.getViewParameter("collectionNames");
			return;
		}
		collectionOperation = Visit.getRequestParam("collectionOperation");
		collectionType = Integer.parseInt(Visit.getRequestParam("collectionType", "0"));
		selectedCollectionsString = Visit.getRequestParam("selectedCollections", "");
		collectionAttribute = Visit.getRequestParam("collectionAttribute");
		/*collectionAttribute = Visit.getRequestParam("collectionAttribute", "0");//DEREK SET DEFAULT
*/		collectionNames = "";
	}

	// ********************************************************************************
	// Abstract methods 
	// ********************************************************************************
	abstract ArrayList<String> getCollectionIds(int idCol, String resultId);
	
	// ********************************************************************************
	// Private & Protected methods 
	// ********************************************************************************
	protected void saveTableViewAndParams(GenericTableView tableView) {
		tableView.setViewParameter("collectionOperation", collectionOperation);
		tableView.setViewParameter("collectionType", collectionType);
		tableView.setViewParameter("selectedCollections", selectedCollectionsString);
		tableView.setViewParameter("collectionNames", collectionNames);
		/*tableView.setViewParameter("collectionAttribute", collectionAttribute);//DEREK get operation on entries over genes
*/		TableUtil.saveTableView(tableView);
	}
	
	/*protected GenericTableView populateCollectionOperationTableView(String viewName, String resultId) {
	    if (debug)
	    System.out.println("CollectionOperationBrowseParentBean.populateCollectionOperationTableView  viewName = "+viewName+" resultId = "+resultId+" collectionType = "+collectionType);
		CollectionBrowseHelper collectionBrowseHelper = Globals.getCollectionBrowseHelper(null, collectionType);
		if (collectionBrowseHelper==null)
			return null;
		int idCol = collectionBrowseHelper.getCollectionTableViewIdCol();
		ArrayList<String> collectionItemsIds = getCollectionIds(idCol, resultId);
		if (debug) 
		    System.out.println("CollectionOperationBrowseParentBean.populateCollectionOperationTableView  idCol = "+idCol+" resultId = "+resultId+" collectionItemsIds = "+collectionItemsIds);

		collectionBrowseHelper.setCollectionItemsIds(collectionItemsIds);
		GenericTableView tableView = collectionBrowseHelper.getCollectionTableView(viewName);
		return  tableView;
	}*/
	
	protected GenericTableView populateCollectionOperationTableView(String viewName, String resultId, boolean collectionVsCookies) {
	    if (debug)
	    System.out.println("CollectionOperationBrowseParentBean.populateCollectionOperationTableView  viewName = "+viewName+" resultId = "+resultId+" collectionType = "+collectionType);
		CollectionBrowseHelper collectionBrowseHelper = Globals.getCollectionBrowseHelper(null, collectionType);
		if (collectionBrowseHelper==null)
			return null;
		int idCol = collectionBrowseHelper.getCollectionTableViewIdCol();
		/*int idCol = (collectionVsCookies)?1:collectionBrowseHelper.getCollectionTableViewIdCol();*/
		ArrayList<String> collectionItemsIds = getCollectionIds(idCol, resultId);
		if (debug) 
		    System.out.println("CollectionOperationBrowseParentBean.populateCollectionOperationTableView  idCol = "+idCol+" resultId = "+resultId+" collectionItemsIds = "+collectionItemsIds);

		collectionBrowseHelper.setCollectionItemsIds(collectionItemsIds);
		GenericTableView tableView = collectionBrowseHelper.getCollectionTableView(viewName);
		return  tableView;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getOperation() {
		return Utility.captalizeFirst(collectionOperation);
	}

	public String getClipboardName() {
		return Globals.getCollectionCategoriesNames()[collectionType];
	}
	
	public String getSelectedCollectionsString() {
		return selectedCollectionsString;
	}

	public String getCollectionNames() {
		return collectionNames;
	}
	
	public String getIntersectionSymbol() {	// To return Intersection symbol for tab pane label
		return "\u2229";
	}
	
}
