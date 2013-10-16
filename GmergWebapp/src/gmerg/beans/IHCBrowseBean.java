package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.FocusBrowseIHCAssembler;
import gmerg.utils.Visit;
import gmerg.utils.table.*;

public class IHCBrowseBean {
    private boolean debug = false;

	private String organ;
	private String gene;
    private String archiveId;
    private String batchId;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public IHCBrowseBean() {
	    if (debug)
		System.out.println("IHCBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		gene = Visit.getRequestParam("gene");
		archiveId = Visit.getRequestParam("archiveId");
		batchId = Visit.getRequestParam("batchId");

		String viewName = "focusBrowseIHC";
        if (TableUtil.isTableViewInSession())
			return;                 
		TableUtil.saveTableView(populateIhcBrowsTableView(viewName));
	}	

	private GenericTableView populateIhcBrowsTableView(String viewName) {
		String[] organs = null;
		if (organ!=null)
			organs = new String[]{organ};
		String[] archiveIds = null;
		if (archiveId!=null)
			archiveIds = new String[]{archiveId};
		String[] batchIds = null;
		if (batchId!=null)
			batchIds = new String[]{batchId};

		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("organs", organs);
		queryParams.put("archiveIds", archiveIds);
		queryParams.put("batchIds", batchIds);

		FocusBrowseIHCAssembler assembler = new FocusBrowseIHCAssembler(queryParams);
		GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(ISHBrowseBean.getDefaultIshFilter());
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(true);	// Bernie 04/11/2010 - switched on Totals display
		return  tableView;
	}
	
	public String getOrganTitle() {
		if (organ==null || "".equals(organ))
			return "";
		
		return " in <em>" + DatabaseHomepageBean.getOrganName(organ) + "</em>"; 
	}
	
	/**
	 * @author xingjun - 30/01/2009
	 * @return
	 */
	public String getGene() {
		if (gene == null || gene.equals("")) {
			return "";
		} else {
			return gene;
		}
	}
}
