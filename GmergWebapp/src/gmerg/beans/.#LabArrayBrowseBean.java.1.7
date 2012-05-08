package gmerg.beans;

import java.util.HashMap;
import gmerg.assemblers.LabSummaryAssembler;
import gmerg.utils.table.*;
import gmerg.utils.*;

public class LabArrayBrowseBean {

	public LabArrayBrowseBean() {
		String viewName = "labArrayBrowse";
		
		if (TableUtil.isTableViewInSession())
			return;
		
       	TableUtil.saveTableView(populateArrayFocusBrowseTableView(viewName));		
	}

	public boolean getDumy() {
		return false;
	}
	
	private GenericTableView populateArrayFocusBrowseTableView(String viewName) {
		String labId = FacesUtil.getRequestParamValue("labId");
		String date = FacesUtil.getRequestParamValue("date");
		String archiveId = FacesUtil.getRequestParamValue("archiveId"); // xingjun 01/07/2011

		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("labId", labId);
		queryParams.put("assayType", "Microarray");
		queryParams.put("submissionDate", Utility.convertToDatabaseDate(date));
		queryParams.put("tableType", "array");
		queryParams.put("labIshEdit", false);
		queryParams.put("archiveId", archiveId); // xingjun - 01/07/2011

		LabSummaryAssembler assembler = new LabSummaryAssembler(queryParams);
		GenericTable table = assembler.createTable();
		GenericTableView tableView = ArrayFocusBrowseBean.getDefaultArrayBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);
		
		return  tableView;
	}	

}
