package gmerg.beans;

import java.util.ArrayList;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.Globals;
import gmerg.utils.Utility;
import gmerg.utils.table.*;

public class CollectionOperationBrowseBean extends CollectionOperationBrowseParentBean{
    protected boolean debug = false;

    private String[] selectedIds;	// it has to be accesed using a lazy getter because of multiple populate tableViews when operation is difference
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionOperationBrowseBean() {
	    if (debug)
		System.out.println("CollectionOperationBrowseBean.constructor");

		selectedIds = null;	// has a lasy getter
		if (TableUtil.isTableViewInSession())
			return;		// initialisation is performed in the super class constructor
		
		if (collectionOperation.equalsIgnoreCase("difference")) {
			collectionOperation = "difference";
			saveTableViewAndParams(populateCollectionOperationTableView("collectionOperationDifference1", "difference1"));
			saveTableViewAndParams(populateCollectionOperationTableView("collectionOperationDifference2", "difference2"));
			saveTableViewAndParams(populateCollectionOperationTableView("collectionOperationDifferenceIntersection", "differenceIntersection"));
			saveTableViewAndParams(populateCollectionOperationTableView("collectionOperationDifferenceFirstSet", "differenceFirstSet"));
			saveTableViewAndParams(populateCollectionOperationTableView("collectionOperationDifferenceSecondSet", "differenceSecondSet"));
		}
		else 
			saveTableViewAndParams(populateCollectionOperationTableView("collectionOperationBrowse", collectionOperation));
	}

	// ********************************************************************************
	// Private & Protected methods 
	// ********************************************************************************
	private String[] getSelectedIds(int idCol) {
		if (selectedIds == null)
			selectedIds = TableUtil.getSelectedIds(idCol);
		return selectedIds;
	}
	
	protected ArrayList<String> getCollectionIds(int idCol, String resultId) {
		ArrayList<String> collectionItemsIds = new ArrayList<String>();
		String[] selectedIds = getSelectedIds(idCol);	//it contain Ids for selected collections from collectionList table

		if (debug) {
		    System.out.println("CollectionOperationBrowseBean.getCollectionIds");
		System.out.println("idCol="+idCol+"     resultId=="+resultId);
		if (null != selectedIds)
		    for(int i=0; i<selectedIds.length; i++) 
			System.out.println("selectedCollections["+i+"]="+selectedIds[i]);
		}
		
		if (selectedIds == null)
			return collectionItemsIds;

		String[] selectedCollectionNames = null;
		for (int i=0; i<selectedIds.length; i++)  
			selectedCollectionsString += ((i>0)? ", ": "") + selectedIds[i];
		
		if (collectionNames.equals("")) {	// only do it once. this is specially useful for collection difference 
			selectedCollectionNames = CollectionAssembler.instance().getCollectionNames(selectedIds);
			for (int i=0; i<Math.max(2, selectedIds.length); i++) {
				if (resultId.indexOf("difference")==0) 
					collectionNames += ((i>0)? "<span class='plaintextbold'>&nbsp;&nbsp;and&nbsp;&nbsp;</span>": "") + (char)(i + 'A') + ") "; 
				else
					collectionNames += ((i>0)? ", ": "");
				collectionNames += (i<selectedIds.length)? selectedCollectionNames[i] : " ";
			}
			if (collectionAttribute != null) {
				String attribute = Globals.getCollectionCategory(collectionType).getAttribute(Integer.valueOf(collectionAttribute)).getName();
				collectionNames += "<span class='plaintextbold'>&nbsp;&nbsp;over&nbsp;&nbsp;" + attribute +"</span> ";
			}
		}
		
		int attributeCol = Integer.parseInt(Utility.getValue(collectionAttribute, "-1"));
		
		boolean ignoreCase = ! Globals.getCollectionCategory(collectionType).isCaseSensitiveIds();
		if (attributeCol!=idCol && attributeCol>=0)
			ignoreCase = ! Globals.getCollectionCategory(collectionType).getAttribute(attributeCol).isCaseSensitive();
		
		if (resultId.equalsIgnoreCase("union")) 
			return CollectionAssembler.instance().getUnionOfCollections(selectedIds, collectionType, ignoreCase);
		
		if (resultId.indexOf("differenceIntersection") == 0)  
			return CollectionAssembler.instance().getIntersectionOfCollections(selectedIds, 2, collectionType, attributeCol, idCol, ignoreCase);
		
		if (resultId.indexOf("differenceFirstSet") == 0)
			if (selectedIds.length > 0)  
				return CollectionAssembler.instance().getCollectionItems(Integer.parseInt(selectedIds[0]));
			else
				return collectionItemsIds;
		
		if (resultId.indexOf("differenceSecondSet") == 0)
			if (selectedIds.length > 1) 
				return CollectionAssembler.instance().getCollectionItems(Integer.parseInt(selectedIds[1]));
			else
				return collectionItemsIds;
		
		if (resultId.indexOf("difference") == 0) 
			return CollectionAssembler.instance().getDifferenceOfCollections(selectedIds, collectionType, attributeCol, idCol, resultId, ignoreCase);
		
		return collectionItemsIds = CollectionAssembler.instance().getIntersectionOfCollections(selectedIds, collectionType, attributeCol, idCol, ignoreCase);
	}

	protected GenericTableView populateCollectionOperationTableView(String viewName, String resultId) {
		GenericTableView tableView = super.populateCollectionOperationTableView(viewName, resultId);
		tableView.setCollectionBottons(3); 
		return  tableView;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getOperation() {
		return Utility.captalizeFirst(collectionOperation);
	}

	public String getSelectedCollectionsString() {
		return selectedCollectionsString;
	}

	public String getCollectionNames() {
		return collectionNames;
	}
	
	public boolean getSetViewNameDifference1() {	// Dumy method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 
		TableUtil.setTableViewNameParam("collectionOperationDifference1");
		return false;
	}
	
	public boolean getSetViewNameDifference2() {		// Dumy method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2
		TableUtil.setTableViewNameParam("collectionOperationDifference2"); 
		return false;
	}
	
	public boolean getSetViewNameIntersection() {	// Dumy method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 
		TableUtil.setTableViewNameParam("collectionOperationDifferenceIntersection"); 
		return false;
	}
	
	public boolean getSetViewNameFirstSet() {	// Dumy method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 
		TableUtil.setTableViewNameParam("collectionOperationDifferenceFirstSet"); 
		return false;
	}
	
	public boolean getSetViewNameSecondSet() {	// Dumy method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 
		TableUtil.setTableViewNameParam("collectionOperationDifferenceSecondSet"); 
		return false;
	}

}
