package gmerg.beans;

import java.util.ArrayList;
import java.util.List;

import gmerg.entities.Globals;
import gmerg.model.ClipboardDelegateCookieImp;
import gmerg.utils.table.*;
import gmerg.assemblers.CollectionAssembler;

public class ClipboardOperationBrowseBean extends CollectionOperationBrowseParentBean {
    
    protected boolean debug = false;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public ClipboardOperationBrowseBean() {
	    if (debug)
		System.out.println("ClipboardOperationBrowseBean.constructor");

		if (TableUtil.isTableViewInSession())
			return;		// initialisation is performed in the super class constructor
			
		saveTableViewAndParams(populateCollectionOperationTableView("clipboardOperationBrowse", collectionOperation));
	}

	// ********************************************************************************
	// Private & protected methods 
	// ********************************************************************************
	//Use this only for operations on non-stored collections
	protected ArrayList<String> getCollectionIds(int idCol, String resultId) {
		 
	    if (debug)
	    	System.out.println("ClipboradOperationBrowseBean::getCollectionIds | idCol= "+idCol+" | resultId= "+resultId+" | collectionType= "+collectionType);

	  //Use this only for operations on non-stored collections. gets the ids of the entry/gene
	    String[] selectedIds = TableUtil.getSelectedIdsForCollection(collectionType);  
	  //Use this only for operations on db-stored collections. Gets the ids of the collections
		String[] selectedColectionIds = TableUtil.getSelectedIds(idCol);

		// convert collection id to union of element ids of collections DEREK - WHY IS IT GOING TO THE DB?
		boolean ignoreCase = !Globals.getCollectionCategory(collectionType).isCaseSensitiveIds();
		
		/*List list = CollectionAssembler.instance().getUnionOfCollections(selectedIds,  collectionType, ignoreCase);
		
		if (null == list)
		    selectedIds = null;
		else
		    selectedIds = (String[])list.toArray(new String[0]);*/
		
		List list = CollectionAssembler.instance().getUnionOfCollections(selectedColectionIds,  collectionType, ignoreCase);
		
		if (null == list)
		   // selectedIds = null;
		;
		else
		    selectedIds = (String[])list.toArray(new String[0]);

		return ClipboardDelegateCookieImp.getResultIdsOperationWithClipboard(resultId, selectedIds, collectionType);
	}

	protected GenericTableView populateCollectionOperationTableView(String viewName, String resultId) {
		/*GenericTableView tableView = super.populateCollectionOperationTableView(viewName, resultId);*/
		GenericTableView tableView = super.populateCollectionOperationTableView(viewName, resultId, true);
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
