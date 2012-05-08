package gmerg.model;

import gmerg.assemblers.MasterTableAssembler;
import gmerg.assemblers.MasterTableBrowseAssembler;
import gmerg.beans.MasterTableBrowseBean;
import gmerg.entities.BrowseTableTitle;
import gmerg.utils.DbUtility;
import gmerg.utils.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ProbesCollectionBrowseHelper extends CollectionBrowseHelper {
	
	String platformId;
	String masterTableId;

	public ProbesCollectionBrowseHelper(String platformId, String masterTableId, ArrayList<String> collectionItemsIds, int collectiontype, String idsParamName, int collectionViewIdCol) {
		super(collectionItemsIds, collectiontype, idsParamName, collectionViewIdCol);
		this.platformId = platformId;
		this.masterTableId = masterTableId;
	}
	
	public GenericTableView getCollectionTableView(String viewName) {
		OffMemoryTableAssembler assembler = getCollectionBrowseAssembler();
		return MasterTableBrowseBean.getDefaultTableViewForMasterTable(viewName, assembler, platformId);
	}
	
	public OffMemoryTableAssembler getCollectionBrowseAssembler() {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(getIdsParamName(), collectionItemsIds);
		BrowseTableTitle[] expressionTitles = MasterTableAssembler.retrieveExpressionTitles(masterTableId);
		BrowseTableTitle[] annotationTitles = MasterTableAssembler.retrieveAnnotaionTitles(platformId);
		queryParams.put("expressionTitles", expressionTitles);
		queryParams.put("annotationTitles", annotationTitles);
		queryParams.put("masterTableId", masterTableId);
		return new MasterTableBrowseAssembler(queryParams, this);
	}
	
	public ArrayList<String> getValidIds() {
		return DbUtility.checkProbeSetIds(collectionItemsIds, platformId);
	}
}
