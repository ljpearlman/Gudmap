package gmerg.beans;

//import java.util.HashMap;
//import gmerg.assemblers.GenelistAssembler;
//import gmerg.model.HeatmapDisplayTransform;
//import gmerg.utils.table.*;
//import gmerg.utils.FacesUtil;

/**
 * Managed Bean to display Processed Genelist data table
 * 
 * @author Mehran Sharghi
 * 
*/
public class GenelistBean {
/* 
 *
 *
 *
 *	Is not being used
 * 
 * 
 * 
 * 
 * 

	private String genelistId;
	private GenelistAssembler assembler;
	private boolean displayTreeView;

	// ********************************************************************************
	// Constructors & initializers
	// ********************************************************************************
	public GenelistBean (){
//		System.out.println("In GenelistBean constructor.");
		displayTreeView = false;
		
		if (TableUtil.isTableViewInSession()) {
			genelistId =(String)TableUtil.getAssemblerParameter("genelistId");
			return;
		}
		genelistId = FacesUtil.getRequestParamValue("genelistId");
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("genelistId", Integer.valueOf(genelistId));
	    assembler = new GenelistAssembler(queryParams);

		String viewName = "genelist_" + genelistId;

		TableUtil.saveTableView(populateGenelistTableView(viewName));
	}

	public String getDistinguishingParam() {
		return "genelistId";
	}

	/********************************************************************************
	 * populates GenericTableView for data table of a genelist 
	 *
	 * /
	public GenericTableView populateGenelistTableView(String viewName)
	{
	    GenericTable table = assembler.createTable();
		GenericTableView tableView = new GenericTableView(viewName, 20, 350, table);
//		GenericTableView tableView = new GenericTableView(viewName, 5, 250, table);
		tableView.setHeightUnlimittedFlexible();
	    tableView.setColWrap(false);
	    
		int ontologisColOffset = assembler.getAnalysisTitles().length + 1;
		int ontologiesColNum = assembler.getOntologyTitles().length;
		
		for(int i=0; i<ontologiesColNum; i++) 
			tableView.setColVisible(ontologisColOffset+i, false);	 
		int[] defaultOntologyCols = assembler.getDefaultOntologyCols();
		for (int i=0; i<defaultOntologyCols.length; i++) 
			tableView.setColVisible(ontologisColOffset+defaultOntologyCols[i], true);
		
		int[][] ontologiesColsWidth = assembler.getOntologyColsWidth();
		if (ontologiesColsWidth != null)
			for(int i=0; i<ontologiesColsWidth.length; i++)
				tableView.setColMaxWidth(ontologisColOffset+ontologiesColsWidth[i][0], ontologiesColsWidth[i][1], ontologiesColsWidth[i][2]!=0); 
		int[] heatmapCols = assembler.getHeatmapCols();
//		tableView.setHeatmap(heatmapCols, 1, true, false ); 	
		tableView.setHeatmap(heatmapCols, 1, true, false, false);
		tableView.setHeatmapDisplayTransform(new HeatmapDisplayTransform(10.0, 1.0, 1.0, 1.0));
		
		return  tableView;
	}
	
	public String getGenelistName() {
		return assembler.getGenelistInfo().getTitle();
	}

	public String getGenelistId() {
		return genelistId;
	}

	public void setGenelistId(String genelistId) {
		this.genelistId = genelistId;
	}

	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String startTreeView() {
		displayTreeView = true;

		return null;
	}
	
	//*****************************************************************
	public boolean isDisplayTreeView() {
		return displayTreeView;
	}

	public void setDisplayTreeView(boolean displayTreeView) {
		this.displayTreeView = displayTreeView;
	}
	
*/
	
}

