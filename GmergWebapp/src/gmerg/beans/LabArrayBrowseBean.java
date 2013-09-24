package gmerg.beans;

import java.util.HashMap;
import java.util.Map;

import gmerg.assemblers.LabSummaryAssembler;
import gmerg.utils.table.*;
import gmerg.utils.*;

public class LabArrayBrowseBean {
    private boolean debug = false;
    
    private String labId;
    private String date;
    private String archiveId;
    private String batchId;

	public LabArrayBrowseBean() {
	if (debug)
	    System.out.println("BLabArrayBrowseean.constructor");

		String viewName = "labArrayBrowse";
		
		if (TableUtil.isTableViewInSession())
			return;
		
       	TableUtil.saveTableView(populateArrayFocusBrowseTableView(viewName));		
	}

	public boolean getDumy() {
		return false;
	}
	
	private GenericTableView populateArrayFocusBrowseTableView(String viewName) {

		boolean validParams = checkParams();

		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("labId", labId);
		queryParams.put("assayType", "Microarray");
		queryParams.put("submissionDate", Utility.convertToDatabaseDate(date));
		queryParams.put("tableType", "array");
		queryParams.put("labIshEdit", false);
		queryParams.put("archiveId", archiveId); // xingjun - 01/07/2011
		queryParams.put("batchId", batchId);

		LabSummaryAssembler assembler = new LabSummaryAssembler(queryParams);
		GenericTable table = assembler.createTable();
		
		if (!validParams)
			table.setNumRows(0);
		
		GenericTableView tableView = ArrayFocusBrowseBean.getDefaultArrayBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);
		
		return  tableView;
	}	

	// check for parameter values that match the required entries
	// looks for misspelling and missing params
	private boolean checkParams() {
		Map<String,String> paramMap = FacesUtil.getRequestParamMap();
		
		if (paramMap.isEmpty())
			return false;
		
		int paramCount = paramMap.size();
		int count = 0;
		
		for (Map.Entry<String,String> entry: paramMap.entrySet()){
			String param = entry.getKey();
			if (param.equalsIgnoreCase("labId")){
				labId = entry.getValue();
				count++;
			}
			if (param.equalsIgnoreCase("date")){
				date = entry.getValue();
				count++;
			}
			if (param.equalsIgnoreCase("archiveId")){
				archiveId = entry.getValue();
				count++;
			}
			if (param.equalsIgnoreCase("batchId")){
				batchId = entry.getValue();
				count++;
			}
		}
		
		if (paramCount == count)
			return true;
		else
			return false;
	}
	
}
