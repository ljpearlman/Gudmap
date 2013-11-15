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
    private boolean debug = false;

	private String organ;
	private String gene;
    private String archiveId;
    private String batchId;
    
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public TransgenicBrowseBean() {
	if (debug)
	    System.out.println("TransgenicBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		gene = Visit.getRequestParam("gene");
		archiveId = Visit.getRequestParam("archiveId");
		batchId = Visit.getRequestParam("batchId");


		String viewName = "focusBrowseTransgenic";
        	if (TableUtil.isTableViewInSession())
			return;                 
		TableUtil.saveTableView(populateTransgenicBrowsTableView(viewName));
	}	

	private GenericTableView populateTransgenicBrowsTableView(String viewName) {
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
		
		FocusBrowseTransgenicAssembler assembler = new FocusBrowseTransgenicAssembler(queryParams);
		GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(ISHBrowseBean.getDefaultIshFilter());
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
//		tableView.setDisplayTotals(true); // Bernie 04/11/2010 - display totals in header
		tableView.setDefaultColVisible(new boolean[]{true, true, true, false, false, false, true, false, false, true, false, false, true});
		tableView.setColHidden(5, true); // hide probe name from displaying - 29/08/2008
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
