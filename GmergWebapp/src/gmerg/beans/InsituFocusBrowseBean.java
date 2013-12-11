package gmerg.beans;

import java.util.HashMap;

import gmerg.assemblers.FocusBrowseAssembler;
import gmerg.utils.table.*;
import gmerg.utils.Visit;

/**
 * modified by xingjun - 15/01/2009 - need another param, gene to get gene centric result
 *
 */
public class InsituFocusBrowseBean {
    private boolean debug = false;
	private String organ;
	private String stage;
	private String gene;
	
	public InsituFocusBrowseBean() {
	    if (debug)
		System.out.println("InsituFocusBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		stage = Visit.getRequestParam("stage");
		gene = Visit.getRequestParam("gene");
		String viewName = "focusBrowseInsitu";
		if (TableUtil.isTableViewInSession())
			return;
        
       	TableUtil.saveTableView(populateInsituFocusBrowseTableView(viewName));		
	}
	
	private GenericTableView populateInsituFocusBrowseTableView(String viewName) {
		String[] organs = null;
		if (organ!=null)
			organs = new String[]{organ};
		HashMap<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("assayType", "insitu_all");
		queryParams.put("organ", organs);
		queryParams.put("stage", stage);
		queryParams.put("gene", gene);
		FocusBrowseAssembler assembler = new FocusBrowseAssembler(queryParams);
		GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(ISHBrowseBean.getDefaultIshFilter());
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);
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
