package gmerg.beans;

import gmerg.assemblers.LabSummaryAssembler;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.TableUtil;

import java.util.HashMap;

public class LabISHEditBean {
	
	public LabISHEditBean() {
		String viewName = "labIshEdit";
		
		if (TableUtil.isTableViewInSession())
			return;
		
       	TableUtil.saveTableView(populateIshFocusBrowseTableView(viewName));		
	}

	public boolean getDumy() {
		return false;
	}
	
	private GenericTableView populateIshFocusBrowseTableView(String viewName) {
		String labId = FacesUtil.getAnyRequestParamValue("labId");
		String date = FacesUtil.getAnyRequestParamValue("date");
		String assayType = FacesUtil.getAnyRequestParamValue("assayType");
		String isPublic = FacesUtil.getAnyRequestParamValue("isPublic");
		int privilege = Utility.getUserPriviledges(); 
		String isSubmitter = (privilege<0)?null : String.valueOf(privilege);

		if (assayType == null || assayType.equals(""))
			assayType = "ISH";
		String archiveId = FacesUtil.getRequestParamValue("archiveId"); // xingjun 01/07/2011

		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("labId", labId);
		queryParams.put("assayType", assayType);
		queryParams.put("submissionDate", Utility.convertToDatabaseDate(date));
		queryParams.put("isSubmitter", isSubmitter);
		queryParams.put("isPublic", isPublic);
		queryParams.put("tableType", "ishEdit");
		queryParams.put("labIshEdit", true);
		queryParams.put("archiveId", archiveId); // xingjun - 01/07/2011

		LabSummaryAssembler assembler = new LabSummaryAssembler(queryParams);
	    GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultIshEditTableView(viewName, table);
		tableView.setDisplayTotals(false);
		return  tableView;
	}	
	
	public static GenericTableView getDefaultIshEditTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, 650, table);
//		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		tableView.addCollection(0, 0);
		tableView.addCollection(1, 1);
		tableView.setDisplayTotals(true);
		tableView.setDefaultColVisible(new boolean[]{true, true, true, false, true, false, false, true, false, true, true, true, true, true});
		HeaderItem[] tableHeader = table.getHeader();
		tableHeader[4].setSortable(false);		// lab column
		tableHeader[5].setSortable(false);		// submission data column
		tableHeader[13].setSortable(false);		// annotaion column
		return tableView;
	}
	

}
