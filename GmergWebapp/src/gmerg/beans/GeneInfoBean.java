package gmerg.beans;
import java.util.HashMap;

import gmerg.assemblers.GeneAssembler;
import gmerg.utils.table.*;
import gmerg.entities.submission.*;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

/**
 * The GeneInfoBean retrieves a user specified string parameter (gene symbol) and passes it to a DAO to query
 * the database in order to find relevant info on the gene matching the specified symbol and display it to the user
 */
public class GeneInfoBean {

private Gene gene;
private String geneId;
private String probeset;
private boolean entrezIdExists;
private boolean xenbaseEntryExists;
private boolean linkedArraysExist;
private GeneAssembler assembler;
private String arrayDataViewName;
private String geneStripViewName;
	
public GeneInfoBean() {
	// get the gene symbol as a parameter
	geneId = FacesUtil.getRequestParamValue("gene");
	probeset = FacesUtil.getRequestParamValue("probeset");
	
	//if no parameter found, see if it is in session
	if(geneId== null || geneId.equals("")){
		geneId = (String)FacesUtil.getSessionValue("geneId");
		probeset = (String)FacesUtil.getSessionValue("geneId");
	}

	geneStripViewName = "geneStrip_";		// tableViewNames must even if geneId is null because they are referenced in the page
	arrayDataViewName = "geneArrayData_";	// tableViewNames must even if geneId is null because they are referenced in the page

	//if id parameter contains a valid string, execute code to query db
	if(geneId != null && !geneId.equals("")) {
		//store geneId and probeset in session
		FacesUtil.setSessionValue("geneId", geneId);
		FacesUtil.setSessionValue("probeset", probeset);
			
		geneStripViewName += geneId;
		arrayDataViewName += geneId;
		
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("geneId", geneId);
		queryParams.put("probeset", probeset);
		assembler = new GeneAssembler(queryParams);
		gene = assembler.getGene();
		entrezIdExists = false;
		linkedArraysExist = false;
		xenbaseEntryExists = true;
		
		if(gene!=null){
			if(gene.getEntrezID() != null)
				entrezIdExists = true;
			// System.out.println("\n\n\nmgi acc id: "+gene.getMgiAccID());
			if(gene.getMgiAccID() == null || gene.getMgiAccID().indexOf("HGNC") >=0 || gene.getMgiAccID().indexOf("MGI") >=0)
				xenbaseEntryExists = false;
			
			if (TableUtil.isTableViewInSession())
				return;

			// create geneStrip table, make it suitable for gene page and save it in session
			// modified by xingjun - 19/03/2009 
			// - use symbol from gene object instead of geneId params 
			// - sometimes geneId is actually synonym of the gene and symbol of gene object 
			//   will always be the real symbol
//			GenericTableView geneStripTableView = GeneStripBrowseBean.populateGeneStripTableView(geneStripViewName, geneId);
			GenericTableView geneStripTableView = GeneStripBrowseBean.populateGeneStripTableView(geneStripViewName, gene.getSymbol(), false);
			geneStripTableView.setDisplayHeader(false);
			geneStripTableView.setNotSelectable();
			geneStripTableView.setCollectionBottons(0);
			geneStripTableView.setColHidden(1, true);
			HeaderItem[] tableHeader = geneStripTableView.getTable().getHeader();
			for (int i=0; i<tableHeader.length; i++) 
				tableHeader[i].setSortable(false);
			TableUtil.saveTableView(geneStripTableView);

			if (Utility.getIntValue(gene.getNumMicArrays(), 0) == 0) //There is no microarray data for this gene
				return;

			if (TableUtil.isTableViewInSession()) 
				return;

			TableUtil.saveTableView(populateGeneArraysTableView(arrayDataViewName));
		}
	}
}

	public boolean getSetTableViewNameToArrayData() {	// Dumy get method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 when having multiple tables in the same page. In this case is the gene page including a genestrip table and another table
		// System.out.println("**** in SetTableViewName arrayData******* ");		
		TableUtil.setViewName(arrayDataViewName);
		return false;
	}
	
	public boolean getSetTableViewNameToGeneStrip() {	// Dumy get method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2 when having multiple tables in the same page. In this case is the gene page including a genestrip table and another table
		// System.out.println("**** in SetTableViewName geneStrip******* ");		
		TableUtil.setViewName(geneStripViewName);
		return false;
	}
	
	/**
	* populates a GenericTableView which will contain data on a particular table
	* in addition to the table data itself - in this case, microarray data for a gene.
	* @param viewName - the name of the table
	* @return
	*/
	private GenericTableView populateGeneArraysTableView(String viewName) {
		GenericTable table = assembler.createTable();
		GenericTableView tableView = new GenericTableView(viewName, 50, 400, table);
		tableView.setDisplayTotals(true);
		tableView.setColHidden(3, true);
		return tableView;
	}

	public boolean isEntrezIdExists() {
		return entrezIdExists;
	}
	
	public boolean isLinkedArraysExist() {
		return linkedArraysExist;
	}
	
	public boolean isXenbaseEntryExists() {
		return xenbaseEntryExists;
	}
	
	public void setGene(Gene gene) {
		this.gene = gene;
	}
	
	public Gene getGene() {
		return gene;
	}
	
	public String geneInfo() {
		return "sucess";
	}

}
