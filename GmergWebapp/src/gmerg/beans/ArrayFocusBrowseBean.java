package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.FocusBrowseAssembler;
import gmerg.utils.table.*;
import gmerg.utils.Visit;
import gmerg.utils.Utility;

public class ArrayFocusBrowseBean {
    private boolean debug = false;

	private String organ;
	private String stage;
	private String archiveId;
	private String batchId;
	
	public ArrayFocusBrowseBean() {
	    if (debug)
		System.out.println("ArrayFocusBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		stage = Visit.getRequestParam("stage");
		archiveId = Visit.getRequestParam("archiveId");
		batchId = Visit.getRequestParam("batchId");

		String viewName = "focusBrowseArray";
		if (TableUtil.isTableViewInSession())
			return;
       	TableUtil.saveTableView(populateArrayFocusBrowseTableView(viewName));		
	}

	protected GenericTableView populateArrayFocusBrowseTableView(String viewName) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		String[] organs = null;
		if (organ!=null)
			organs = new String[]{organ};
		queryParams.put("assayType", "array");
		queryParams.put("organ", organs);
		queryParams.put("stage", stage);
		queryParams.put("archiveId", archiveId);
		queryParams.put("batchId", batchId);
		FocusBrowseAssembler assembler = new FocusBrowseAssembler(queryParams); 
		GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultArrayBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);
		
		return  tableView;
	}	
	
	/**
	 * <p>xingjun - 07/12/2009 - added extra column Gene Reported (position 9)</p>
	 */
	public static GenericTableView getDefaultArrayBrowseTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.addCollection(0, 0);
		tableView.setCollectionBottons(1);
		tableView.setColVisible(new boolean[]{true, true, true, true, true, false, false, false, true, false, true, true});
		tableView.setColMaxWidth(9, 30);  // TITLE Trim value if it is more than 30 characters (adds ... at the end)
		tableView.setColAlignment(8, 0);	// DESCRIPTION column left aligned
		tableView.setColAlignment(11, 0);	// COMPONENTS column left aligned
		return  tableView;
	}	
	
	public String getOrganTitle() {
		if (organ==null || "".equals(organ))
			return "";
		
		return " in <em>" + DatabaseHomepageBean.getOrganName(organ) + "</em>"; 
	}
	
	public String getStageTitle() {
		if (stage==null || "".equals(stage))
			return "";
		
		return " at Theiler stage <em>" + stage + "</em>";  
	}
	
	public String getTitle() {
		
		if(Utility.getProject().equalsIgnoreCase("GUDMAP")){
			return "Microarray submissions" + this.getOrganTitle() + this.getStageTitle();
		}
		else 
			return "Microarray Samples";
	}
}
