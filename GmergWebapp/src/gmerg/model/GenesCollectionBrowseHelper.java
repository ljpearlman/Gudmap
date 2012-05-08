package gmerg.model;

import gmerg.assemblers.GeneStripAssembler;
import gmerg.beans.GeneStripBrowseBean;
import gmerg.utils.DbUtility;
import gmerg.utils.Utility;
import gmerg.utils.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GenesCollectionBrowseHelper extends CollectionBrowseHelper {

	public GenesCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectiontype, String idsParamName, int collectionViewIdCol) {
		super(collectionItemsIds, collectiontype, idsParamName, collectionViewIdCol);
	}

	public GenericTableView getCollectionTableView(String viewName) {
		return GeneStripBrowseBean.populateGeneStripTableView(viewName, Utility.toString(collectionItemsIds, ";"), false);
	}
	
	public OffMemoryTableAssembler getCollectionBrowseAssembler() {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(getIdsParamName(), collectionItemsIds);
		return new GeneStripAssembler(queryParams, this);
	}
	
	public ArrayList<String> getValidIds() {
		return DbUtility.checkGeneSymbols(collectionItemsIds);
	}
}
