package gmerg.model;

import gmerg.assemblers.EntriesCollectionBrowseAssembler;
import gmerg.utils.DbUtility;
import gmerg.utils.Utility;
import gmerg.utils.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EntriesCollectionBrowseHelper extends CollectionBrowseHelper {
	
	public EntriesCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectiontype, String idsParamName, int collectionViewIdCol) {
		super(collectionItemsIds, collectiontype, idsParamName, collectionViewIdCol);
	}
	
	public GenericTableView getCollectionTableView(String viewName) {
		OffMemoryTableAssembler assembler = getCollectionBrowseAssembler();
	    GenericTable table = assembler.createTable();
	    GenericTableView tableView = new GenericTableView (viewName, 20, table);
	    if(Utility.getProject().equals("GUDMAP")) 
	    	tableView.setColVisible(new boolean[]{ true, true, true, false, false, false, true, false, true, false, false, false, true, true,  false, true, true });
	    else 
	    	tableView.setColVisible(new boolean[]{ true, true, true, false, true, false, false, true, false, true, true, true, true });
		tableView.addCollection(0, 0); //CollectionType:0
		tableView.setRowsSelectable();
		tableView.setHeightUnlimittedFlexible();
		tableView.setDisplayTotals(false);
		return tableView;
	}
	
	public OffMemoryTableAssembler getCollectionBrowseAssembler() {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(getIdsParamName(), collectionItemsIds);
		queryParams.put("type", String.valueOf(getCollectionType()));
		return new EntriesCollectionBrowseAssembler(queryParams, this);
	}
	
	public ArrayList<String> getValidIds() {
			return DbUtility.checkGudmapIds(collectionItemsIds);
	}
	
}
