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

/**
 * @author xingjun
 *
 */
public class LabTGBrowseBean {
	public LabTGBrowseBean() {
		String viewName = "labTGBrowse";
		
		if (TableUtil.isTableViewInSession())
			return;
		
       	TableUtil.saveTableView(populateTGFocusBrowseTableView(viewName));		
	}

	public boolean getDumy() {
		return false;
	}
	
	private GenericTableView populateTGFocusBrowseTableView(String viewName) {
		String labId = FacesUtil.getRequestParamValue("labId");
		String date = FacesUtil.getRequestParamValue("date");
		String assayType = FacesUtil.getRequestParamValue("assayType");
		String archiveId = FacesUtil.getRequestParamValue("archiveId"); // xingjun 01/07/2011
		
//		if (assayType == null || assayType.equals(""))
//			assayType = "ISH";
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("labId", labId);
		queryParams.put("assayType", assayType);
		queryParams.put("submissionDate", Utility.convertToDatabaseDate(date));
		queryParams.put("isSubmitter", null);
		queryParams.put("tableType", "ish");
		queryParams.put("labIshEdit", false);
		queryParams.put("archiveId", archiveId); // xingjun - 01/07/2011

		LabSummaryAssembler assembler = new LabSummaryAssembler(queryParams);
	    GenericTable table = assembler.createTable();
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(true); // Bernie 25/3/2011 - display totals in header
		tableView.setColHidden(9, true); // hide probe name from displaying - 29/08/2008
		tableView.setColHidden(11, true); // hide probe type from displaying - 29/08/2008
		return  tableView;
	}	

}
