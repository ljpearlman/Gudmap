package gmerg.beans;

import java.util.ArrayList;

import gmerg.entities.Globals;
import gmerg.model.ClipboardDelegateCookieImp;
import gmerg.utils.table.*;

public class ClipboardOperationBrowseBean extends CollectionOperationBrowseParentBean {
    

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public ClipboardOperationBrowseBean() {
		if (TableUtil.isTableViewInSession())
			return;		// initialisation is performed in the super class constructor
			
		saveTableViewAndParams(populateCollectionOperationTableView("clipboardOperationBrowse", collectionOperation));
	}

	// ********************************************************************************
	// Private & protected methods 
	// ********************************************************************************
	protected ArrayList<String> getCollectionIds(int idCol, String resultId) {
		String[] selectedIds = TableUtil.getSelectedIdsForCollection(collectionType);   
		return ClipboardDelegateCookieImp.getResultIdsOperationWithClipboard(resultId, selectedIds, collectionType);
	}

	protected GenericTableView populateCollectionOperationTableView(String viewName, String resultId) {
		GenericTableView tableView = super.populateCollectionOperationTableView(viewName, resultId);
		tableView.setCollectionBottons(4);
		
		return  tableView;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getClipboardName() {
		return Globals.getCollectionCategoriesNames()[collectionType];
	}
	
}
