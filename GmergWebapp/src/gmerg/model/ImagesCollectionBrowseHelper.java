package gmerg.model;

import gmerg.assemblers.ImageMatrixBrowseAssembler;
import gmerg.beans.ImageMatrixBrowseBean;
import gmerg.utils.DbUtility;
import gmerg.utils.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ImagesCollectionBrowseHelper extends CollectionBrowseHelper {

	public ImagesCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectiontype, String idsParamName, int collectionViewIdCol) {
		super(collectionItemsIds, collectiontype, idsParamName, collectionViewIdCol);
	}

	public GenericTableView getCollectionTableView(String viewName) {
		return ImageMatrixBrowseBean.populateImageMatrixTableView(viewName, collectionItemsIds);
	}
	
	public OffMemoryTableAssembler getCollectionBrowseAssembler() {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(getIdsParamName(), collectionItemsIds);
		
		return new ImageMatrixBrowseAssembler(queryParams, this);
	}
	
	public ArrayList<String> getValidIds() {
		return DbUtility.checkImageIds(collectionItemsIds);
	}
}
