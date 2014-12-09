package gmerg.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.BrowseTableTitle;
import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.entities.submission.array.MasterTableInfo;
import gmerg.model.ClipboardDelegateCookieImp;
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
	private String listOfGenes;
	private boolean displayTreeView;
	private String tableTitle;
	private String viewMode;

	private String masterTableId;
	private String platformId;
    private String collectionId;
    private String cleartabs;
    private int collectionType;
	private CollectionInfo collectionInfo = null;
	
	
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
			    System.out.println("MasterTableBrowseBean::selected= "+selected);
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
		    System.out.println("MasterTableBrowseBean::constructor | genelistId="+genelistId+" | gene=="+geneSymbol);

		try {
		    init();
		} catch (Exception e) {
		    System.out.println("!!! error in MasterTableBrowseBean.init message = "+e.getMessage());
		}
		
	}
	
	protected void init() {
		int iSize = 0;
		int i = 0;
		displayTreeView = false;
		viewMode = "0";
		MasterTableInfo[] masterTables = DbUtility.getAllMasterTablesInfo();
		/*if (debug) {
		    iSize = 0;
		    if (null != masterTables)
			iSize = masterTables.length;
		    for(i=0; i<iSize; i++) {
		    	System.out.println("MasterTableBrowseBean::init " + i+"th master table Id = "+ masterTables[i].getId()+"  Title = "+ masterTables[i].getTitle());
		    }
		}*/

		    
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
		
		// if a new item is selected from the genelist tree then clear the selections from the dropdown menu
		cleartabs = FacesUtil.getRequestParamValue("cleartabs");
		if (cleartabs != null && cleartabs.contentEquals("true")){
			clearSelectionsString();			
			cleartabs = "";
		}	
		
		
		genelistId = Visit.getRequestParam("genelistId");
		geneSymbol = Visit.getRequestParam("gene");
		if (FacesUtil.getRequestParamValue("actionMethod") != null) 
			return;

		collectionId = Visit.getRequestParam("collectionId");
		platformId = Visit.getRequestParam("platformId");

		if (isClipboard())
		    try {
		    	collectionType = Integer.parseInt(Visit.getRequestParam("collectionType", "0"));
		    } catch (Exception e) {
		    	System.out.println("!!!possible error: "+e.getMessage());
		    	collectionType = -1;
		    }
		else if (collectionId != null)
			collectionType = getCollectionInfo().getType(); 
		
		masterTableId = Visit.getRequestParam("masterTableId");
		if (masterTableId != null) // If a specific master table is requested
		    for (MasterTableDisplayInfo masterTableInfo : allMasterTables) {
		    	masterTableInfo.selected = (masterTableInfo.getInfo().getId().equals(masterTableId));
		    }
		else if (genelistId != null) {
		    platformId = DbUtility.getGenelistPlatformId(genelistId);
		    for (MasterTableDisplayInfo masterTableInfo : allMasterTables) {
		    	masterTableInfo.selected = (masterTableInfo.getInfo().getPlatform().equals(platformId));
		    }
		    // make only Developing Kidney (MOE430) shown initially to enhance user experience (fast)
		    String str = null;
		    for (MasterTableDisplayInfo masterTableInfo : allMasterTables) {
				if (masterTableInfo.selected) {
				    str = masterTableInfo.getInfo().getTitle().toLowerCase();
				    if (-1 == str.indexOf("developing") &&
					-1 == str.indexOf("kidney"))
					masterTableInfo.selected = false;
				}
		    }		    
		}
		else if (platformId != null) {    
		    for (MasterTableDisplayInfo masterTableInfo : allMasterTables) {
		    	masterTableInfo.selected = (masterTableInfo.getInfo().getPlatform().equals(platformId));
		    }
		}
		else 
			for (MasterTableDisplayInfo masterTableInfo : allMasterTables)
				masterTableInfo.selected = true;

//		clearSelectionsString();
		updateSelectedItems();	
						
		initialseTables(null);
		/*if (debug) {
		    System.out.println("End MasterTableBrowseBean::constructor. | genelistId="+genelistId+" | gene="+geneSymbol+" | displayTreeView= "+displayTreeView+" | tableTitle="+tableTitle+" | viewMode="+viewMode);
		    iSize = 0;
		    if (null != allMasterTables)
		    	iSize = allMasterTables.size();
		    MasterTableDisplayInfo item = null;
		    for (i = 0; i < iSize; i++) {
		    	item = (MasterTableDisplayInfo)allMasterTables.get(i);
		    	System.out.println("MasterTableBrowseBean::constructor2 | "+i+"th MasterTableDisplayInfo");
		    	item.print();
		    }
		}*/
	}

	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String updatePage() {
//		String prevSelections = FacesUtil.getRequestParamValue("prevSelections");
		String prevSelections = getSelectionsString();
		
		initialseTables(prevSelections);
		return null;
	}

	public String startTreeView() {
		displayTreeView = true;
		return null;
	}

	public CollectionInfo getCollectionInfo() {
		if (collectionInfo == null)
			collectionInfo = CollectionAssembler.instance().getCollectionInfo(Integer.parseInt(collectionId));
		return collectionInfo;
	}
	
	// ********************************************************************************
	// Private Methods
	// ********************************************************************************
	private boolean isClipboard() {
		return  "clipboard".equals(collectionId);
	}

	
	private void setVisibleMasterTables(String selectionString) {
		for (int i=0; i<selectionString.length(); i++)
			allMasterTables.get(i).selected  = (selectionString.charAt(i)=='1');
	}
	
	private void initialseTables(String available) {
	    if (debug)
	    	System.out.println("MasterTableBrowseBean::initialseTables. | available ="+available);
	    
		String selectionString = getSelectionsString();
		
		int iSize = allMasterTables.size();
		MasterTableDisplayInfo masterTable = null;
		String masterTableId = null;
		int i = 0;
		GenericTableView tableView = null;

		if (debug) {
		    System.out.println("MasterTableBrowseBean::initialseTables. | allMasterTables size= "+iSize+" | selectionString = "+selectionString);
		    displayMasterTableInfo();
		}
		for(i=0; i<iSize; i++) {
		    masterTable = allMasterTables.get(i); 
			masterTableId = masterTable.info.getId();
			if (masterTable.selected) {
				tableView = null;
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
	    	System.out.println("MasterTableBrowseBean::populateGenelistTableVie = " + viewName + " | " + masterTableId);
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		String platformId = DbUtility.getMasterTablePlatformId(masterTableId);
		ArrayList<String> probeIds = new ArrayList<String>();
		
		if (collectionId != null) {
			if (isClipboard())
				probeIds = ClipboardDelegateCookieImp.getClicpboardIds(collectionType);
			else // used when viewing Stored collection
				probeIds = CollectionAssembler.instance().getCollectionItems(Integer.parseInt(collectionId));
		}
		else if (genelistId != null) {
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

		if (null != ret) {
		    ret.setInTabPane(true);
		    ret.setHeightMode(1);
		}

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
		GenericTableView tableView = new GenericTableView(viewName, 100, 350, table);
		tableView.setHeightLimittedFlexible();
	    tableView.setColWrap(false);
	    
		int ontologisColOffset = expressionTitles.length + 1;
		tableView.setColHidden(ontologisColOffset, true);
		int iSize = annotationTitles.length;

		for(int i=0; i<iSize; i++) 
		    tableView.setColVisible(ontologisColOffset+i, true);	 

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
		
		updateSelectionsString();		
		
		String selectionsString = "";
		if (null != allMasterTables)
		    for(MasterTableDisplayInfo masterTable : allMasterTables)
			selectionsString += masterTable.selected? "1" : "0";
		
		if (debug)
		    System.out.println("selectionstring = "+ selectionsString );
		return selectionsString;
		
	}
	
	public String getGeneList(){
		String genelist = "";
		if (genelistId != null) 
			genelist = DbUtility.retrieveGenelist(genelistId);
//		else if (listOfGenes != null) 
//			genelist = DbUtility.retrieveGenelistFromGenes(listOfGenes);
		else
			genelist = geneSymbol;
		
		return genelist;
	}
	public String getListOfGenes(){
		return listOfGenes;
	}

	
	public String getGeneListId(){
		return genelistId;
	}
	
    private List<String> menuItems;
    public List<String> getMenuItems(){
        menuItems = new ArrayList<String>();
        
        for(MasterTableDisplayInfo masterTable : allMasterTables){
        	menuItems.add(masterTable.info.getTitle());
        }
    	return menuItems;
    }
	
    // code to handle dropdown menu for Microarray expression profiles
    boolean selectedGonadalST1;
    public void listenerGonadalST1(ActionEvent event){
    	selectedGonadalST1 = !selectedGonadalST1;
    	if (debug) System.out.println("listenerGonadalST1 selectedGonadalST1 = "+ selectedGonadalST1 );
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GonadalST1", selectedGonadalST1);
    	updatePage();
    }    
    public boolean getSelectedGonadalST1(){
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GonadalST1") != null)
    		selectedGonadalST1 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GonadalST1");
    	return selectedGonadalST1;
    }

    boolean selectedKidneyST1;
    public void listenerKidneyST1(ActionEvent event){
    	selectedKidneyST1 = !selectedKidneyST1;
    	if (debug) System.out.println("listenerKidneyST1 selectedKidneyST1 = "+ selectedKidneyST1 );
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("KidneyST1", selectedKidneyST1);
    	updatePage();
    }    
    public boolean getSelectedKidneyST1(){
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("KidneyST1") != null)
    		selectedKidneyST1 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("KidneyST1");
    	return selectedKidneyST1;
    }

    boolean selectedJGAST1;
    public void listenerJGAST1(ActionEvent event){
    	selectedJGAST1 = !selectedJGAST1;
    	if (debug) System.out.println("listenerJGAST1 selectedJGAST1 = "+ selectedJGAST1 );
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("JGAST1", selectedJGAST1);
    	updatePage();
    }    
    public boolean getSelectedJGAST1(){
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("JGAST1") != null)
    		selectedJGAST1 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("JGAST1");
    	return selectedJGAST1;
    }
    
    boolean selectedPelvicGangliaST1;
    public void listenerPelvicGangliaST1(ActionEvent event){
    	selectedPelvicGangliaST1 = !selectedPelvicGangliaST1;
    	if (debug) System.out.println("listenerPelvicGangliaST1 selectedPelvicGangliaST1 = "+ selectedPelvicGangliaST1 );
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PelvicGangliaST1", selectedPelvicGangliaST1);
    	updatePage();
    }    
    public boolean getSelectedPelvicGangliaST1(){
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PelvicGangliaST1") != null)
    		selectedPelvicGangliaST1 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PelvicGangliaST1");
    	return selectedPelvicGangliaST1;
    }

    boolean selectedKidneyMOE430;
    public void listenerKidneyMOE430(ActionEvent event){
    	selectedKidneyMOE430 = !selectedKidneyMOE430;
    	if (debug) System.out.println("listenerKidneyMOE430 selectedKidneyMOE430 = "+ selectedKidneyMOE430 );
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("KidneyMOE430", selectedKidneyMOE430);
    	updatePage();
    }    
    public boolean getSelectedKidneyMOE430(){
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("KidneyMOE430") != null)
    		selectedKidneyMOE430 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("KidneyMOE430");
    	return selectedKidneyMOE430;
    }

    boolean selectedLUTMOE430;
    public void listenerLUTMOE430(ActionEvent event){
    	selectedLUTMOE430 = !selectedLUTMOE430;
    	if (debug) System.out.println("listenerLUTMOE430 selectedLUTMOE430 = "+ selectedLUTMOE430 );
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("LUTMOE430", selectedLUTMOE430);
    	updatePage();
   }    
    public boolean getSelectedLUTMOE430(){  
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LUTMOE430") != null)
    		selectedLUTMOE430 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LUTMOE430");
    	return selectedLUTMOE430;
    }

    boolean selectedGonadalMOE430;
    public void listenerGonadalMOE430(ActionEvent event){
    	selectedGonadalMOE430 = !selectedGonadalMOE430;
    	if (debug) System.out.println("listenerGonadalMOE430 selectedGonadalMOE430 = "+ selectedGonadalMOE430 );    	
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GonadalMOE430", selectedGonadalMOE430);
    	updatePage();
    }    
    public boolean getSelectedGonadalMOE430(){
    	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GonadalMOE430") != null)
    		selectedGonadalMOE430 = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GonadalMOE430");
    	return selectedGonadalMOE430;
    }
    
	public void clearSelectionsString() {
		
    	selectedKidneyMOE430 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("KidneyMOE430",selectedKidneyMOE430);
		selectedLUTMOE430 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("LUTMOE430",selectedLUTMOE430);
		selectedGonadalMOE430 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GonadalMOE430",selectedGonadalMOE430);
		selectedGonadalST1 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GonadalST1",selectedGonadalST1);
		
		selectedKidneyST1 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("KidneyST1",selectedLUTMOE430);
		selectedJGAST1 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("JGAST1",selectedGonadalMOE430);
		selectedPelvicGangliaST1 = false;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PelvicGangliaST1",selectedGonadalST1);
		
		
		if (debug){
			System.out.println("updateSelectionsString"); 
		    displayMasterTableInfo();
		}

	}
       
	public void updateSelectedItems () {	
	    if (debug)
		System.out.println("updateSelectedItems"); 
		
		for(MasterTableDisplayInfo masterTable : allMasterTables){
			if (masterTable.selected == true){
				if (masterTable.info.getId().contentEquals("4_6"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("LUTMOE430", true);
				if (masterTable.info.getId().contentEquals("4_5"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("KidneyMOE430", true);
				if (masterTable.info.getId().contentEquals("4_7"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GonadalMOE430", true);
				if (masterTable.info.getId().contentEquals("3_3"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("JGAST1", true);
				if (masterTable.info.getId().contentEquals("3_2"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("KidneyST1", true);
				if (masterTable.info.getId().contentEquals("3_4"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PelvicGangliaST1", true);
				if (masterTable.info.getId().contentEquals("3_1"))
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GonadalST1", true);							
			}						
		}
	}

	public void updateSelectionsString() {
		
		Object sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("KidneyMOE430");
		if (sg != null){
	    	allMasterTables.get(2).selected = (Boolean)sg;
	    	selectedKidneyMOE430 = (Boolean)sg;
		}
		sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LUTMOE430");
		if (sg != null){
	    	allMasterTables.get(1).selected = (Boolean)sg;
	    	selectedLUTMOE430 = (Boolean)sg;
		}
		sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GonadalMOE430");
		if (sg != null){
	    	allMasterTables.get(0).selected = (Boolean)sg;
	    	selectedGonadalMOE430 = (Boolean)sg;
		}
		sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GonadalST1");
		if (sg != null){
	    	allMasterTables.get(6).selected = (Boolean)sg;
	    	selectedGonadalST1 = (Boolean)sg;
		}
		sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("KidneyST1");
		if (sg != null){
	    	allMasterTables.get(5).selected = (Boolean)sg;
	    	selectedKidneyST1 = (Boolean)sg;
		}
		sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("JGAST1");
		if (sg != null){
	    	allMasterTables.get(4).selected = (Boolean)sg;
	    	selectedJGAST1 = (Boolean)sg;
		}
		sg = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PelvicGangliaST1");
		if (sg != null){
	    	allMasterTables.get(3).selected = (Boolean)sg;
	    	selectedPelvicGangliaST1 = (Boolean)sg;
		}
		
		
		if (debug){
			System.out.println("updateSelectionsString"); 
		    displayMasterTableInfo();
		}

	}

	public void displayMasterTableInfo(){
		MasterTableDisplayInfo masterTable = null;
		
	    for(int i=0; i<allMasterTables.size(); i++) {
	    	masterTable = allMasterTables.get(i); 
		if (debug)
		    System.out.println(i+"th master table Id = "+ masterTable.info.getId()+"  Title = "+ masterTable.info.getTitle()+"  selected = "+ masterTable.getSelected());
	    }

	}
	
}

