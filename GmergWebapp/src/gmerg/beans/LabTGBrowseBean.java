/**
 * 
 */
package gmerg.beans;

import gmerg.assemblers.LabSummaryAssembler;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
//import gmerg.utils.table.OffMemoryTable;
import gmerg.utils.table.TableUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingjun
 *
 */
public class LabTGBrowseBean {
    private boolean debug = false;
    
    private String labId;
    private String date;
    private String assayType;
    private String archiveId;
    private String batchId;

	public LabTGBrowseBean() {
	if (debug)
	    System.out.println("LabTGBrowseBean.constructor");

		String viewName = "labTGBrowse";
		
		if (TableUtil.isTableViewInSession())
			return;
		
       	TableUtil.saveTableView(populateTGFocusBrowseTableView(viewName));		
	}

	public boolean getDumy() {
		return false;
	}
	
	private GenericTableView populateTGFocusBrowseTableView(String viewName) {

		boolean validParams = checkParams();
		
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("labId", labId);
		if (assayType == null)
			assayType = "TG";
		queryParams.put("assayType", assayType);
		queryParams.put("submissionDate", Utility.convertToDatabaseDate(date));
		queryParams.put("isSubmitter", null);
		queryParams.put("tableType", "ish");
		queryParams.put("labIshEdit", false);
		queryParams.put("archiveId", archiveId); // xingjun - 01/07/2011
		queryParams.put("batchId", batchId);

		LabSummaryAssembler assembler = new LabSummaryAssembler(queryParams);
	    GenericTable table = assembler.createTable();
	    
		if (!validParams)
			table.setNumRows(0);
	    
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false); // Bernie 25/3/2011 - display totals in header
		tableView.setColHidden(9, true); // hide probe name from displaying - 29/08/2008
		tableView.setColHidden(11, true); // hide probe type from displaying - 29/08/2008
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
			if (param.equalsIgnoreCase("assayType")){
				assayType = entry.getValue();
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
