package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.FocusBrowseIHCAssembler;
import gmerg.utils.Visit;
import gmerg.utils.table.*;

public class IHCBrowseBean {
    private boolean debug = false;

	private String organ;
	private String gene;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public IHCBrowseBean() {
	    if (debug)
		System.out.println("IHCBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		gene = Visit.getRequestParam("gene");
		String viewName = "focusBrowseIHC";
        if (TableUtil.isTableViewInSession())
			return;                 
		TableUtil.saveTableView(populateIhcBrowsTableView(viewName));
	}	

	private GenericTableView populateIhcBrowsTableView(String viewName) {
		String[] organs = null;
		if (organ!=null)
			organs = new String[]{organ};
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("organs", organs);
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
