/**
 * 
 */
package gmerg.beans;

import gmerg.assemblers.FocusBrowseTransgenicAssembler;
import gmerg.utils.Visit;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.TableUtil;

import java.util.HashMap;

/**
 * @author xingjun
 *
 */
public class TransgenicBrowseBean {

	private String organ;
	private String gene;

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public TransgenicBrowseBean() {
		organ = Visit.getRequestParam("focusedOrgan");
		gene = Visit.getRequestParam("gene");
		String viewName = "focusBrowseTransgenic";
        	if (TableUtil.isTableViewInSession())
			return;                 
		TableUtil.saveTableView(populateTransgenicBrowsTableView(viewName));
	}	

	private GenericTableView populateTransgenicBrowsTableView(String viewName) {
		String[] organs = null;
		if (organ!=null)
			organs = new String[]{organ};
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("organs", organs);
		FocusBrowseTransgenicAssembler assembler = new FocusBrowseTransgenicAssembler(queryParams);
		GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(ISHBrowseBean.getDefaultIshFilter());
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(true); // Bernie 04/11/2010 - display totals in header
		tableView.setColHidden(9, true); // hide probe name from displaying - 29/08/2008
		tableView.setColHidden(11, true); // hide probe type from displaying - 29/08/2008
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
