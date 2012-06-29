package gmerg.beans;

import java.util.ArrayList;
import java.util.HashMap;

import gmerg.entities.BrowseTableTitle;
import gmerg.entities.Globals;
import gmerg.entities.submission.array.MasterTableInfo;
import gmerg.utils.table.*;
import gmerg.utils.DbUtility;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

/**
 * Managed Bean to display Processed Genelist data table
 * 
 * @author Mehran Sharghi
 * 
*/
public class MasterTableBrowseBean {
    static private boolean debug = false;

	private String genelistId;
	private String geneSymbol;
	private boolean displayTreeView;
	private String tableTitle;
	private String viewMode;
	
	public class MasterTableDisplayInfo  {
		boolean selected;
		MasterTableInfo info;
		
		public MasterTableDisplayInfo(MasterTableInfo masterTableInfo) {
			this(masterTableInfo, false);
		}
		public MasterTableDisplayInfo(MasterTableInfo masterTableInfo, boolean selected) {
			info = masterTableInfo;
			this.selected = selected;
		}
		public MasterTableInfo getInfo() {
			return info;
		}
		public boolean isSelected() {
			return selected;
		}
		public boolean getSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		public boolean getSetTableViewName() {	// Dumy method to set tableViewName parameter. It is a work around for jsp:param in jsf1.2
			String viewName = getViewName(info.getId());
			TableUtil.setTableViewNameParam(viewName); 
			return false;
		}
	                public void print() {
			    System.out.println("selected = "+selected);
			    if (null != info)
				info.print();
	               }
	};
	private ArrayList<MasterTableDisplayInfo> allMasterTables;

	// ********************************************************************************
	// Constructors & initializers
	// ********************************************************************************
	
	public MasterTableBrowseBean (){
	if (debug)
	    System.out.println("-------In MasterTableBrowseBean constructor.   genelistId==="+genelistId+"   gene=="+geneSymbol);
		displayTreeView = false;
		viewMode = "0";
		MasterTableInfo[] masterTables = DbUtility.getAllMasterTablesInfo();
		allMasterTables = new ArrayList<MasterTableDisplayInfo>();
		for (MasterTableInfo info : masterTables)
			allMasterTables.add(new MasterTableDisplayInfo(info));
		
		String selections;
		tableTitle = null;
		if (TableUtil.isTableViewInSession()) {
			GenericTableView tableView = TableUtil.getTableViewFromSession();
			genelistId = (String)tableView.getViewParameter("genelistId");
			geneSymbol = (String)tableView.getViewParameter("gene");
			selections = (String)tableView.getViewParameter("selections");
			viewMode = (String)tableView.getViewParameter("viewMode");
			setVisibleMasterTables(selections);
			return;
		}
		genelistId = Visit.getRequestParam("genelistId");
		geneSymbol = Visit.getRequestParam("gene");
		if (FacesUtil.getRequestParamValue("actionMethod") != null) 
			return;

		String masterTableId = Visit.getRequestParam("masterTableId");
		if (masterTableId != null) // If a specific master table is requested
		    for (MasterTableDisplayInfo masterTableInfo : allMasterTables) {
			masterTableInfo.selected = (masterTableInfo.getInfo().getId().equals(masterTableId));
		    }
		else if (genelistId != null) {
				String platformId = DbUtility.getGenelistPlatformId(genelistId);
				for (MasterTableDisplayInfo masterTableInfo : allMasterTables) {
				    masterTableInfo.selected = (masterTableInfo.getInfo().getPlatform().equals(platformId));
				}
		}
		else 
			for (MasterTableDisplayInfo masterTableInfo : allMasterTables)
				masterTableInfo.selected = true;
		initialseTables(null);
		if (debug) {
		    System.out.println("-------End MasterTableBrowseBean constructor.   genelistId==="+genelistId+"   gene=="+geneSymbol+" displayTreeView = "+displayTreeView+" tableTitle="+tableTitle+" viewMode="+viewMode);
		    int iSize = 0;
		    if (null != allMasterTables)
			iSize = allMasterTables.size();
		    int i = 0;
		    MasterTableDisplayInfo item = null;
		    for (i = 0; i < iSize; i++) {
			item = (MasterTableDisplayInfo)allMasterTables.get(i);
			System.out.println(i+"th MasterTableDisplayInfo");
			item.print();
		    }
		}
	}

	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String updatePage() {
		String prevSelections = FacesUtil.getRequestParamValue("prevSelectios");
		initialseTables(prevSelections);
		return null;
	}

	public String startTreeView() {
		displayTreeView = true;
		return null;
	}
	
	// ********************************************************************************
	// Private Methods
	// ********************************************************************************
	private void setVisibleMasterTables(String selectionString) {
		for (int i=0; i<selectionString.length(); i++)
			allMasterTables.get(i).selected  = (selectionString.charAt(i)=='1');
	}
	
	private void initialseTables(String available) {
	    if (debug)
		System.out.println("MasterTableBrowseBean:initialseTables.   available ="+available);
		String selectionString = getSelectionsString();
		int iSize = allMasterTables.size();
		if (debug)
		    System.out.println("MasterTableBrowseBean:initialseTables.   allMasterTables size = "+iSize);

		for(int i=0; i<iSize; i++) {
			MasterTableDisplayInfo masterTable = allMasterTables.get(i); 
			String masterTableId = masterTable.info.getId();
			if (masterTable.selected) {
				GenericTableView tableView = null;
				if (available!= null && available.charAt(i)=='1') 
					tableView = TableUtil.getTableViewFromSession(getViewName(masterTableId));
				if (tableView == null){
					tableView = populateGenelistTableView(getViewName(masterTableId), masterTableId);
					tableView.setViewParameter("genelistId", genelistId);
					tableView.setViewParameter("gene", geneSymbol);
					TableUtil.saveTableView(tableView);
				}
				tableView.setViewParameter("viewMode", viewMode);
				tableView.setViewParameter("selections", selectionString);
			}
		}
	}
	
	private GenericTableView populateGenelistTableView(String viewName, String masterTableId) {
	    if (debug)
		System.out.println("===MasterTableBrowseBean===populateGenelistTableView = " + viewName + " " + masterTableId);
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		String platformId = DbUtility.getMasterTablePlatformId(masterTableId);
		ArrayList<String> probeIds = new ArrayList<String>();
		
		if (genelistId != null) {
			probeIds = DbUtility.retrieveGenelistProbeIds(genelistId, platformId);
			if (platformId.equals(DbUtility.getGenelistPlatformId(genelistId)))  
				queryParams.put("genelistId", genelistId);	// It will be null except for genelists when the same platform is probes are requested
		}
		else 
			if (geneSymbol != null) 
				probeIds = DbUtility.retrieveGeneProbeIds(geneSymbol, platformId);
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(probeIds, getCollectionType(platformId), masterTableId);

		GenericTableView ret = getDefaultTableViewForMasterTable(viewName, helper.getCollectionBrowseAssembler(), platformId);

		if (debug) 
		    System.out.println("MasterTableBrowseBean CollectionBrowseHekper class = "+helper.getClass().getName()+" ret = "+ret);

		return ret;
	}

	private String getViewName(String id) {
		String viewName = "masterTable_";
		if (genelistId!=null) 
			viewName += genelistId;
		else 
			if (geneSymbol != null) 
				viewName += geneSymbol;		
		return viewName + "_" + id;
	}
	
	private static int getCollectionType(String platformId) {
		return ("GPL6246".equalsIgnoreCase(platformId))? 4 : 3;
	}
	
	// ********************************************************************************
	// static methods
	// ********************************************************************************
	public static GenericTableView getDefaultTableViewForMasterTable(String viewName, GenericTableAssembler assembler, String platformId) {
		if (debug)
		    System.out.println("MasterTableBrowseBean.getDefaultTableViewForMasterTable viewName = "+viewName+" assembler class = "+assembler.getClass().getName()+" platformId = "+platformId);

		BrowseTableTitle[] expressionTitles = (BrowseTableTitle[])assembler.getDataRetrivalParams().get("expressionTitles");
		BrowseTableTitle[] annotationTitles = (BrowseTableTitle[])assembler.getDataRetrivalParams().get("annotationTitles");

		if (debug)
		    System.out.println("MasterTableBrowseBean.getDefaultTableViewForMasterTable expressionTitles size = "+expressionTitles.length+" annotationTitles size = "+annotationTitles.length);

		GenericTable table = assembler.createTable();
		GenericTableView tableView = new GenericTableView(viewName, 20, 350, table);
		tableView.setHeightLimittedFlexible();
	    tableView.setColWrap(false);
	    
		int ontologisColOffset = expressionTitles.length + 1;

		tableView.setColHidden(ontologisColOffset, true);
		
		for(int i=0; i<annotationTitles.length; i++) 
			tableView.setColVisible(ontologisColOffset+i, false);	 

		int[] defaultOntologyCols = Globals.getDefaultOntologyCols(platformId);
		if (defaultOntologyCols != null)
			for (int i=0; i<defaultOntologyCols.length; i++) 
				tableView.setColVisible(ontologisColOffset+defaultOntologyCols[i], true);

		int[] leftAlignedOntologyCols = Globals.getLeftAlignedOntologyCols(platformId);
		if (leftAlignedOntologyCols != null)
			for (int i=0; i<leftAlignedOntologyCols.length; i++)
				tableView.setColAlignment(ontologisColOffset+leftAlignedOntologyCols[i], 0);
		
		tableView.setHeatmap(4, expressionTitles.length, true, false, false); 
		//		tableView.setHeatmap(3, expressionTitles.length, true, false, false);
		//		tableView.setHeatmap(3, expressionTitles.length, true, false); 
		tableView.setHeatmapMedianColumn(1); 	// specifies column number where median is stored 
		tableView.setHeatmapStdDevColumn(2); 	// specifies column number where stand dev is stored
		tableView.setColHidden(1, true);
		tableView.setColHidden(2, true);
		
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		tableView.addCollection(getCollectionType(platformId), 0);
		tableView.setColMaxWidth(3, 20, true);	//limit gene symbol colum width - xingjun - 20/04/2011
		return  tableView;
	}

	// ********************************************************************************
	// getters & Setters
	// ********************************************************************************
	public boolean isDisplayTreeView() {
		return displayTreeView;
	}

	public void setDisplayTreeView(boolean displayTreeView) {
		this.displayTreeView = displayTreeView;
	}
	
	public String getTitle() {
		if (tableTitle == null)
			if (genelistId != null) 
				tableTitle = DbUtility.retrieveGenelistTitle(genelistId) + " (gene list)";
			else
				tableTitle = geneSymbol;
		
		return tableTitle;
	}
	
	public boolean isGenelist() {
		return (genelistId != null);
	}
	

	public ArrayList<MasterTableDisplayInfo> getAllMasterTables() {
		return allMasterTables;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public String getSelectionsString() {
		String selectionsString = "";
		for(MasterTableDisplayInfo masterTable : allMasterTables)
			selectionsString += masterTable.selected? "1" : "0";
		
		return selectionsString;
	}
	
}

