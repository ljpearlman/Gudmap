package gmerg.utils.table;

import gmerg.entities.Globals;
import gmerg.utils.Utility;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class CollectionBrowseHelper {
	protected ArrayList<String> collectionItemsIds;
	protected int collectiontype;
	protected int collectionViewIdCol;
	protected String idsParamName; 

	// ********************************************************************************
	// Abstract methods 
	// ********************************************************************************
	public abstract GenericTableView getCollectionTableView(String viewName);
	public abstract OffMemoryTableAssembler getCollectionBrowseAssembler();
	public abstract ArrayList<String> getValidIds();
//	protected abstract HeaderItem[] createHeaderForCollectionTable();

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectiontype, String idsParamName, int collectionViewIdCol) { 
		this.collectionItemsIds = collectionItemsIds;
		this.collectiontype = collectiontype = 0;
		this.idsParamName = idsParamName;
		this.collectionViewIdCol = collectionViewIdCol;
	}

// ********************************************************************************
// getters & Setters
// ********************************************************************************
	public int getCollectionTableViewIdCol() {
		return collectionViewIdCol;
	}
		
	public int getCollectionType() {
		return collectiontype;	
	}

	public String getIdsParamName() {
		return idsParamName;
	}
	
	public ArrayList<String> getCollectionItemsIds() {
		return collectionItemsIds;
	}
	
	public void setCollectionItemsIds(ArrayList<String> collectionItemsIds) {
		this.collectionItemsIds = collectionItemsIds;
	}

	public void setDynamicCollectionIds(ArrayList<String> newIds) {	// sets new ids for bean and assembler and refersh associated table (example is when removing from a collection)
		collectionItemsIds = newIds;
		GenericTableView tableView = TableUtil.getTableViewFromSession();
		OffMemoryCollectionAssembler assembler = (OffMemoryCollectionAssembler)TableUtil.getTableAssembler(tableView.getName());
		assembler.setIds(collectionItemsIds);
		tableView.refreshTable();
	}
	
	public HashSet<String> getInvalidIds() {
		ArrayList<String> validIds = getValidIds();
		boolean ignoreCase = !Globals.getCollectionCategory(getCollectionType()).isCaseSensitiveIds();
		return Utility.getDifference(collectionItemsIds, validIds, ignoreCase);	// Ids that are not found (or not visible for this user) in the database
	}

}
