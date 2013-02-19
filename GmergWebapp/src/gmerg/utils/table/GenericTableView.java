package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 *
 * Class for maintaining viewing state of a table (e.g. current page, rows per page, etc).
 * It is designed in ageneric way to apply for different kind of tables.
 *  
 */
import gmerg.assemblers.MasterTableAssembler;
import gmerg.model.HeatmapDisplayTransform;
import gmerg.utils.Utility;

import java.util.*;

public class GenericTableView
{
	protected int rowsPerPage;
	protected int currentPage;
	protected GenericTable table;
	protected int width;
	protected int height;
	protected int heightMode;		// 0)no limit for body height  1)same as 0 but can change to 2/3  2)limitted fixed body height   3)limitted flexible body height
	protected boolean[] colVisible;		// Specifies columns display status 
	protected boolean[] defaultColVisible;
	protected boolean[] colHidden;	// Specifies which columns are available to be choosed for display
	protected boolean[] colWrap;
	protected int[] colMaxWidth;
	protected int[] colShortFormType; // 0)shorten but no link  30-32)shorten and link to a popup window (value indicates the GenericTable DataItem type)
	protected int[] colAlignment; 	 //textAlignment-  0:left  1:centre (default) 2:right
//	protected GenericCollection collection;
	protected int collectionBottons;  // 0)none    1)collection buttons for any page but colection page    2)collection buttons for collection page 
	protected String name;
	protected int selectable;	// 0)not selectable    1)rows selectable    )individual cells selectable
	protected boolean topSelectionBox; //where to display selection box for individual cells 
	protected boolean displayInFrame;
	protected boolean displayTotals;
	protected boolean dynamicColumns;
	protected boolean displayHeader;	// Control display of the whole header panel
	protected int minColNum;
	protected int maxColNum;
	protected String parentIdParam;    // To be used by jsp pages as request parameter name
	protected String parentId;        // To be used by jsp pages as request parameter value
	protected int[] perPageOptions;
	protected String noDataMessage;
	protected String navigationPanelMessage;
	protected static int[] defaultRowsPerPageOptions = {5, 10, 20, 50, 100};
	protected final int DEFAULT_HEIGHT = 400;
	
	protected int verticalAlign;	// 0:centre(default)  1:top   2:bottom
	protected HashMap<String, String> collections;   //keys are collection ids and values are column ids
	protected String currentCollection;		
	protected String defaultCollection;		// Stores key for the default collection. If there is no default or only single collection then holds the first one inserted in the collection map
//	protected int heatmapDisplayStatus;	//	0)no heatmap (graphic/values)	1)display graphic heatmap	2)display both values and graphic heatmaps 	3)display values  
	protected boolean displayHeatmap;  
	protected boolean displayHeatmapValues;  
	protected boolean independentHeatmapValues; // if true heatmap value columns will show on the column selection even if not displaying heatmap graphic,values or both   
	protected int[] heatmapColumns;
	protected int heatmapColumnIndex;
	protected int heatmapMedianColumn;	// if there is a precalculated median for heatmap its column number (relative to the heatmap) stored here
	protected int heatmapStdDevColumn;	// if there is a precalculated stdDev for heatmap its column number (relative to the heatmap) stored here
	protected HeatmapDisplayTransform heatmapDisplayTransform;
	protected HashMap<String, Object> viewParams; 
	
    protected boolean inTabPane = false;
	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public GenericTableView(String name, int rowsPerPage, GenericTable table) {
		this(name, rowsPerPage, 0, 0, table);
	}
	
	public GenericTableView(String name, boolean[] colVisible, boolean[] colWrap, int rowsPerPage, int h, int w, GenericTable table) {
		this(name, rowsPerPage, h, w, table);
		setColVisible(colVisible);
		setColWrap(colWrap);
	}

	public GenericTableView(String name, int rowsPerPage, int h, GenericTable table)
	{
		this(name, rowsPerPage, h, 0, table);
	}
	
	public GenericTableView(String name, int rowsPerPage, int h, int w, GenericTable table) {
		this.name = name;
		width = w;
		height = h;
		/** scroll within table does not work properly
		 *  anyway, brwoser has a scroll function itself
		 *  two scoll functions are unnecessary
		 */
		/*
		if (height == 0) {
			heightMode = 1;
			height = DEFAULT_HEIGHT;
		}
		else
			heightMode = 3;
		*/
		heightMode = 0;
		parentIdParam = null;
		parentId = null;
		displayHeader = true;
		noDataMessage = "There is no data to display.";
		this.table = table;
		collectionBottons = 0;
		selectable = 0;
		topSelectionBox = true;
//		collection = null;
		displayInFrame = false;
		this.rowsPerPage = rowsPerPage;
		currentPage = 1;
		displayTotals = false;
		dynamicColumns = true;
		navigationPanelMessage = "";
		int numCols = (table == null)? 0 : table.getNumCols();
		colWrap = new boolean[numCols];
		colVisible = new boolean[numCols];
		defaultColVisible = null;
		colHidden = new boolean[numCols];
		colMaxWidth = new int[numCols];;
		colShortFormType = new int[numCols];
		colAlignment = new int[numCols];;
		minColNum = 1;
		maxColNum = numCols;
		for(int i=0; i<numCols; i++){
			colVisible[i] = true;
			colHidden[i] = false;
			colWrap[i] = true;
			colMaxWidth[i] = 0;
			colShortFormType[i] = 0;
			colAlignment[i] = 1;	// default is centre align
		}
		verticalAlign = 0;
		displayHeatmap = false;
		displayHeatmapValues = false;
		independentHeatmapValues = true;
		heatmapColumns = null;
		heatmapColumnIndex = 0;
		heatmapMedianColumn = -1;	
		heatmapStdDevColumn = -1;

		heatmapDisplayTransform = MasterTableAssembler.getDisplayTransform();
		collections = new HashMap<String, String>();
		defaultCollection = "";
		currentCollection = null;
		viewParams = new HashMap<String, Object>();
		
		if(table == null)
			return;
		
		setDefaultPerPageOptions(); //Calls adjustDefaultHeight()
	}

	// ********************************************************************************
	// Public Methods
	// ********************************************************************************
	public void clearCollections() {
		collections.clear();
		defaultCollection = "";
		currentCollection = null;
	}
	
	public boolean processNavigation(String operation, int page)  
	{
		int newCurrentPage = 1;  
		if ("firstPage".equals(operation)) {
			newCurrentPage = 1;
		}	
		if ("lastPage".equals(operation)) {
			newCurrentPage = getNumPages();
		}
		if ("nextPage".equals(operation)) {
			newCurrentPage = Math.min(currentPage+1, getNumPages());
		}
		if ("prevPage".equals(operation)) {
			newCurrentPage = Math.max(currentPage-1, 1);
		}
		if ("gotoPage".equals(operation)){
			page = Math.min(page, getNumPages());
			page = Math.max(page, 1);
			newCurrentPage = page;
		}
		
		boolean newPage = false;
		if(currentPage!=newCurrentPage)
			newPage = true;
		currentPage = newCurrentPage;
		
		return newPage;
	}

	public void sort(int col)
	{
		table.sort(col);
		currentPage = 1;
	}
	
	public void setDynamicColumnsLimits(int minColNum, int maxColNum) {
		if(table == null)
			return;
		
		dynamicColumns = true;
		this.maxColNum = Math.min(maxColNum, table.getNumCols());
		this.minColNum = Math.max(minColNum, 1);
		adjustVisibleCols();
	}

	public int getDisplayColNum() {
		int numCols = table.getNumCols();
		int visibleNum = 0;
		for (int i=0; i<numCols; i++) {
			if (colVisible[i] && !colHidden[i])
				visibleNum++;
		}
		return visibleNum;
	}
	
	public boolean[] getDisplayColumns() {
		int numCols = table.getNumCols();
		boolean[] displayCols = new boolean[numCols]; 
		for (int i=0; i<numCols; i++) 
			displayCols[i] = (colVisible[i] && !colHidden[i]);
		
		return displayCols;
	}
	
	public boolean isColumnAddable() {
		return getDisplayColNum() < maxColNum;
	}
		
	public boolean isColumnRemoveable() {
		return getDisplayColNum() > minColNum;
	}
		
	public boolean isDynamicColumns() {
		return dynamicColumns;
	}

	public void setDynamicColumns(boolean dynamicColumns) {
		if (!this.dynamicColumns && dynamicColumns && table!=null)
			setDynamicColumnsLimits(1, table.getNumCols());
		else
			this.dynamicColumns = dynamicColumns;
	}
	
	public void refreshTable() {
//		System.out.println("***********>GenericTableView::refreshtable:"+name);		
		if (isCellsSelectable()) 
			adjustColumnChanges();	// this has to be called before actually refereshing table
		if (!table.isInMemory())
			((OffMemoryTable)table).refreshTable();
		adjustPerPageOptions();
		setCurrentPage(currentPage);	//if number of rows changed then current page could be invalid
		
		// Bernie 2/5/2011 - Mantis 328 added to display updated totals
		if (table.numRowsInGroups != null){
			setNavigationPanelMessage("Totals: In Situ(<b>" + table.numRowsInGroups[0] + "</b>) &nbsp&nbsp&nbsp Microarray(<b>" + table.numRowsInGroups[1] + "</b>)");
//			System.out.println("Totals: In Situ(" + table.numRowsInGroups[0] + ") Microarray(" + table.numRowsInGroups[1]+")");
		}
	}

	private void adjustColumnChanges() {	// This is used when columns of a table changed dynamically. It could for example happen for cell selectable table when removing items
		HeaderItem newHeader[] = table.getAssembler().createHeader();
		HeaderItem header[] = table.getHeader();
		int n = newHeader.length;
		if (n > header.length) {
			colVisible = new boolean[n]; 
			defaultColVisible = new boolean[n];
			colHidden = new boolean[n];
			colWrap = new boolean[n];
			colMaxWidth = new int[n];
			colShortFormType = new int[n];
			colAlignment = new int[n];
		}
		ArrayList newHeatmapColumns = new ArrayList();
		for (int i=0; i<newHeader.length; i++) {
			int f = -1;
			for (int j=0; j<header.length; j++)
				if (header[j].getTitle().equals(newHeader[i].getTitle())) {
					f = j;
					break;
				}
			if (f>=0) { 
				colVisible[i] = colVisible[f];
				colHidden[i] = colHidden[f];
				colWrap[i] = colWrap[f];
				colMaxWidth[i] = colMaxWidth[f];
				colShortFormType[i] = colShortFormType[f];
				colAlignment[i] = colAlignment[f];
				if (defaultColVisible != null)
					defaultColVisible[i] = defaultColVisible[f];
				if (Utility.intArraySearch(heatmapColumns, f)>0)
					newHeatmapColumns.add(i);
			}
			else {
				colVisible[i] = true;
				colHidden[i] = false;
				colWrap[i] = true;
				colMaxWidth[i] = 0;
				colShortFormType[i] = 0;
				colAlignment[i] = 1;
				if (defaultColVisible != null)
					defaultColVisible[i] = true;
			}
				
		}
		if (newHeatmapColumns.size()>0) {
			heatmapColumns = new int[newHeatmapColumns.size()];
			for (int i=0; i<newHeatmapColumns.size(); i++)
				heatmapColumns[i] = (Integer)newHeatmapColumns.get(i);
			Arrays.sort(heatmapColumns);
			heatmapColumnIndex = heatmapColumns[0];
		}
		else
			heatmapColumns = null;
			
	}
	
	public void adjustDefaultHeight() {
		if (height == 0)
			height = DEFAULT_HEIGHT;	// a default value used when moving to flexiblwe scroll and no height is not defined yet. It will limit to the min and max height later on
		return;
	}
	
	// ********************************************************************************
	// Private & Helper Methods
	// ********************************************************************************
	private void adjustVisibleCols () {
		if (!isColumnsLimited())
			return;
		
		int numCols = table.getNumCols();
		int visibleNum = getDisplayColNum();
		
		for (int i=0; i<numCols; i++) {
			if (colHidden[i])
				continue;
			if (visibleNum < minColNum) {
				if (!colVisible[i]) {
					visibleNum++;
					colVisible[i] = true;
				}
			}
			else
				if (visibleNum > maxColNum ) {
					if (colVisible[i]) {
						visibleNum--;
						colVisible[i] = false;
					}			
				}
				else
					break;
		}
	}	
		
	private void setDefaultPerPageOptions() {
		perPageOptions = defaultRowsPerPageOptions;
		adjustPerPageOptions();
	}

	private void adjustPerPageOptions() {
		Arrays.sort(perPageOptions);
		int n = table.getNumRows();
		ArrayList<Integer> l = new ArrayList<Integer>();
		boolean setValueAdded = false;
		for (int i=0; i<perPageOptions.length; i++) {
			if (perPageOptions[i] <= rowsPerPage) {
				l.add(perPageOptions[i]);
				if (perPageOptions[i] == rowsPerPage)  
					setValueAdded = true;
			}
			else {
				if (!setValueAdded) {
					l.add(rowsPerPage);
					setValueAdded = true;
				}
				l.add(perPageOptions[i]);
				if (perPageOptions[i] > n)
					break;
			}
		}
		if (!setValueAdded)
			l.add(rowsPerPage);
		perPageOptions = new int[l.size()];
		for (int i=0; i<l.size(); i++)
			perPageOptions[i] = (Integer)l.get(i);
		
		adjustDefaultHeight();
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public void setTable(GenericTable table)
	{
		this.table = table;
		refreshTable();
	}

	public void setColVisible(boolean[] colVisible)
	{
		if (table == null)
			return;
		
		for(int i=0; i<colVisible.length; i++)
			this.colVisible[i] = colVisible[i];
		if (dynamicColumns)
			adjustVisibleCols();
	}

	public void setColVisible(int colIndex, boolean visible)
	{
		if (table == null)
			return;
		
		if (dynamicColumns && ( (visible && !colHidden[colIndex] && !isColumnAddable()) || (!visible && !colHidden[colIndex] && !isColumnRemoveable())))  
			return;
			
		colVisible[colIndex] = visible;
	}

	public void setDefaultColVisible(boolean[] colVisible)
	{
		if (table == null)
			return;
		if (defaultColVisible == null)
			defaultColVisible = new boolean[table.getNumCols()];
		for(int i=0; i<colVisible.length; i++)
			this.defaultColVisible[i] = this.colVisible[i] = colVisible[i];
		if (dynamicColumns)
			adjustVisibleCols();
	}

/*
	public void setVisible(int col, boolean visible){
		if (table == null)
			return;
		
		this.colVisible[col] = visible;
	}
*/
	
	public void setColHidden(boolean[] colHidden)
	{
		if (table == null)
			return;
		
		for(int i=0; i<colHidden.length; i++)
			this.colHidden[i] = colHidden[i];
		if (dynamicColumns)
			adjustVisibleCols();
	}

	public void setColHidden(int colIndex, boolean hidden)
	{
		if (table == null)
			return;
		
		if (dynamicColumns && ( (!hidden && colVisible[colIndex] && !isColumnAddable()) || (hidden && colVisible[colIndex] && !isColumnRemoveable())))  
			return;
			
		colHidden[colIndex] = hidden;
	}

	public void setHiddenCols(int[] cols, boolean hidden)
	{
		if (table==null || cols==null)
			return;
		
		Arrays.sort(cols);
		for(int i=0, j=0; i<colHidden.length; i++)
			if(j<cols.length && i==cols[j]) {
				setColHidden(i, hidden);
				j++;
			}
		if (dynamicColumns)
			adjustVisibleCols();
	}

	public void setColWrap(boolean colWrap)
	{
		if (table == null)
			return;
		
		for(int i=0; i<table.getNumCols(); i++)
			this.colWrap[i] = colWrap;
	}

	public void setColWrap(boolean[] colWrap)
	{
		if (table == null)
			return;
		
		for(int i=0; i<colWrap.length; i++)
			this.colWrap[i] = colWrap[i];
	}

	public void setColMaxWidth(int col, int size){
		if (table == null)
			return;
		
		colMaxWidth[col] = size;
	}
	
	public void setColMaxWidth(int col, int size, boolean expandable){	   
		if (table == null)
			return;
		
		colMaxWidth[col] = size;
		colShortFormType[col] = expandable? 30 : 0;
	}
	
	public int getColMaxWidth(int col) {
		if (table == null)
			return 0;
		
		return colMaxWidth[col]; 		
	}
	
	public int[] getColMaxWidth() {
		
		return colMaxWidth; 
	}
	
	public void setColShortFormType(int col, int type){
		if (table == null)
			return;
		
		colShortFormType[col] = type;
	}
	
	public int getColShortFormType(int col) {
		if (table == null)
			return 0;
		
		return colShortFormType[col]; 
	}
	
	public int[] getColShortFormType() {
		if (table == null)
			return null;
		
		return colShortFormType; 
	}
	
	public void setColLeftAligned(int col){
		if (table == null)
			return;
		colAlignment[col] = 0;
	}
	
	public void setColAlignment(int col, int alignment){
		if (table == null)
			return;
		colAlignment[col] = alignment;
	}
	
	public int getColAlignment(int col) {
		if (table == null)
			return 0;
		return colAlignment[col]; 
	}
	
	public int[] getColAlignment() {
		return colAlignment; 
	}
	
	public void setRowsPerPage(int n){
		int oldRowOffset = getRowOffset();
		rowsPerPage = n;
		currentPage = (int) (oldRowOffset / rowsPerPage) + 1;
		adjustDefaultHeight();
	}

	public void setWidth(int w){
		width = w;
	}

	public void setHeight(int h){
		height = h;
//		if (heightMode < 2)
//			setHeightMode(3);
	}

	public void setCurrentPage(int page)
	{
		currentPage = page;
		currentPage = Math.min(currentPage, getNumPages());
		currentPage = Math.max(currentPage, 1);
	}

	public GenericTable getTable(){		
		return table;
	}

	public void setNotSelectable(){
		selectable = 0;
	}
	
	public void setRowsSelectable(){
		selectable = 1;
	}
	
	public void setCellsSelectable(){
		selectable = 2;
	}
	
	public void setSelectable(int selectable){
		this.selectable = selectable;
	}

	public int[] getPerPageOptions() {
		return perPageOptions;
	}

	public void setPerPageOptions(int[] perPageOptions) {
		this.perPageOptions = perPageOptions;
	}
	
	public int getSelectable(){
		return selectable;
	}

	public boolean isRowsSelectable(){
		return selectable == 1;
	}

	public boolean isCellsSelectable(){
		return selectable == 2;
	}

	public int getNumPages(){
		int numPages = Math.max((int)Math.ceil((double)table.getNumRows()/rowsPerPage), 1);
		return numPages;
	}

	public int getRowsPerPage(){
		return rowsPerPage;
	}

	public int getCurrentPage(){
		return currentPage;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public boolean[] getColVisible(){
		return colVisible;
	}

	public boolean[] getColHidden(){
		return colHidden;
	}

	public boolean isColumnAvailable(int col) {			// If column can be displayed by selection (no matter if it is visible or not)
		boolean isHeatmapCol = false;
		if (heatmapColumns != null)
			for(int i=0; i<heatmapColumns.length; i++)
				if (col == heatmapColumns[i]) {
					isHeatmapCol = true;
					break;
				}
		return (!colHidden[col] || isHeatmapCol && independentHeatmapValues);
	}
	
	public boolean[] getDefaultColVisible(){
		return defaultColVisible;
	}
	
	public boolean isDefaultColumns() {
		return defaultColVisible != null;
	}
	
	public boolean[] getColWrap(){
		return colWrap;
	}

	public String getName(){
		return name;
	}

	public int getFirstRowIndex(){
		if (table.isInMemory())
			return rowsPerPage*(currentPage-1);
		else
			return 0;
	}

	public int getRowOffset(){
		return rowsPerPage*(currentPage-1);
	}
	
	public int getDisplayRowsPerPage() {
 		if (currentPage < getNumPages() || (table.getNumRows()%rowsPerPage == 0))
			return rowsPerPage;
		else
			return table.getNumRows() % rowsPerPage; 
	}

	public String getRequestParam(){
		if (table != null)
			return "tableViewName="+name + "&tableSelectable="+String.valueOf(selectable) + "&tableNumPages="+String.valueOf(getNumPages());
		return "tableViewName="+name;
	}
	
	public DataItem[][] getData(){
		return table.getData(getRowOffset(), rowsPerPage);
	}
	
	public boolean isDisplayInFrame() {
		return displayInFrame;
	}

	public void setDisplayInFrame(boolean displayInFrame) {
		this.displayInFrame = displayInFrame;
	}

	public boolean isDisplayTotals() {
		return displayTotals;
	}

	public void setDisplayTotals(boolean displayTotals) {
		this.displayTotals = displayTotals;
	}

	public int getCollectionBottons() {
		return collectionBottons;
	}

	public void setCollectionBottons(int collectionBottons) {
		this.collectionBottons = collectionBottons;
	}

	public void setParentIdParam(String param) {
		parentIdParam = param;
	}
	
	public String getParentIdParam() {
		if (parentIdParam == null || "".equals(parentIdParam) )
			return "parentIdParam";
		return parentIdParam;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String value) {
		parentId = value;
	}

	public String getNoDataMessage() {
		return noDataMessage;
	}

	public void setNoDataMessage(String noDataMessage) {
		this.noDataMessage = noDataMessage;
	}

	public int getMaxColNum() {
		return maxColNum;
	}

	public void setMaxColNum(int maxColNum) {
		this.maxColNum = maxColNum;
	}

	public int getMinColNum() {
		return minColNum;
	}

	public void setMinColNum(int minColNum) {
		this.minColNum = minColNum;
	}

	public boolean isColumnsLimited() {
		return (table == null || maxColNum<table.getNumCols()); 
	}
	
	public String getNavigationPanelMessage() {
		return navigationPanelMessage;
	}

	public void setNavigationPanelMessage(String navigationPanelMessage) {
		this.navigationPanelMessage = navigationPanelMessage;
	}

	//-------------------------------- flexible scroll ------------------
	public boolean isHeightLimittedFlexible() {
		return heightMode == 3;
	}
	
	public boolean isHeightLimittedFixed() {
		return heightMode == 2;
	}
	
	public boolean isHeightUnlimittedFlexible() {
		return heightMode == 1;
	}
	
	public boolean isHeightUnlimittedFixed() {
		return heightMode == 0;
	}
	
	public boolean isHeightLimitted() {
		return heightMode >= 2;
	}
	
	public boolean isHeightFlexible() {
		return (heightMode == 1 || heightMode == 3);
	}
	
	public void setHeightLimittedFlexible() {
		setHeightMode(3);
	}
	
	public void setHeightLimittedFixed() {
		setHeightMode(2);
	}
	
	public void setHeightUnlimittedFlexible() {
		setHeightMode(1);
	}
	
	public void setHeightUnlimittedFixed() {
		setHeightMode(0);
	}
	
	public int getHeightMode() {
		return heightMode;
	}

	public void setHeightMode(int heightMode) {
		this.heightMode = heightMode;
	}

	//------- Heatmap -----------------------------
	public int[] getHeatmapColumns() {
		return heatmapColumns;
	}

	public int getHeatmapColumnIndex() {
		return heatmapColumnIndex;
	}

	public boolean isInTabPane() {
		return inTabPane;
	}

	public void setInTabPane(boolean input) {
		inTabPane = input;
	}

	public boolean isDisplayHeatmap() {
		return displayHeatmap;
	}

	public boolean isDisplayHeatmapValues() {
		return displayHeatmapValues;
	}
	
	public void setDisplayHeatmap(boolean displayHeatmap) {
		this.displayHeatmap = displayHeatmap;
	}
	
	public void setDisplayHeatmapValues(boolean displayHeatmapValues) {
		this.displayHeatmapValues = displayHeatmapValues;
		setHiddenCols(heatmapColumns, !displayHeatmapValues);
	}

	public void setHeatmap(int heatmapColumnIndex, int heatmapColumnsNum, boolean displayHeatmap, boolean displayHeatmapValues) {
		setHeatmap(heatmapColumnIndex, heatmapColumnsNum, displayHeatmap, displayHeatmapValues, true);
	}
	
	public void setHeatmap(int heatmapColumnIndex, int heatmapColumnsNum, boolean displayHeatmap, boolean displayHeatmapValues, boolean independentHeatmapValues) {
		int[] heatmapCols = new int[heatmapColumnsNum];
		for (int i=0; i<heatmapColumnsNum; i++)
			heatmapCols[i] = i + heatmapColumnIndex;
		setHeatmap(heatmapCols, heatmapColumnIndex, displayHeatmap, displayHeatmapValues, independentHeatmapValues);
	}
	
	public void setHeatmap(int[] heatmapColumns, int heatmapColumnIndex, boolean displayHeatmap, boolean displayHeatmapValues) {
		setHeatmap(heatmapColumns, heatmapColumnIndex, displayHeatmap, displayHeatmapValues, true);
	}
	
	public void setHeatmap(int[] heatmapColumns, int heatmapColumnIndex, boolean displayHeatmap, boolean displayHeatmapValues, boolean independentHeatmapValues) {
		removeHeatmap();
		this.heatmapColumns = heatmapColumns;
		this.heatmapColumnIndex = heatmapColumnIndex;
		this.displayHeatmap  = displayHeatmap;
		this.displayHeatmapValues  = displayHeatmapValues;
		this.independentHeatmapValues = independentHeatmapValues;
		if (displayHeatmapValues)
			setHiddenCols(heatmapColumns, false);
		else
			setHiddenCols(heatmapColumns, true);
	}
	
	public void removeHeatmap() {
		setHiddenCols(heatmapColumns, false);
		heatmapColumns = null;
		independentHeatmapValues = true;
		displayHeatmap = false;
		displayHeatmapValues = false;
		heatmapColumnIndex = 0;
	}

	public boolean isIndependentHeatmapValues() {
		return independentHeatmapValues;
	}

	public HeatmapDisplayTransform getHeatmapDisplayTransform() {
		return heatmapDisplayTransform;
	}

	public void setHeatmapDisplayTransform(
			HeatmapDisplayTransform heatmapDisplayTransform) {
		this.heatmapDisplayTransform = heatmapDisplayTransform;
	}

	public HashMap<String, String> getCollections() {
		return collections;
	}

	public void addCollection(int collectionId, int columnId) {
		String key = String.valueOf(collectionId);
		collections.put(key, String.valueOf(columnId));
		if ("".equals(defaultCollection))
			defaultCollection = key;
		if (currentCollection == null)
			currentCollection = key;
			
	}
	
	public String setToDefaultCollection() {
		currentCollection = ("".equals(defaultCollection))? null : defaultCollection;
		return currentCollection;
	}

	public int getIdCol(int collectionId) {
		String colId = collections.get(String.valueOf(collectionId)); 
		if(colId == null)
			return -1;
		return Integer.parseInt(colId);
	}

	public boolean isDisplayHeader() {
		return displayHeader;
	}

	public void setDisplayHeader(boolean displayHeader) {
		this.displayHeader = displayHeader;
	}

	public int getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(int verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	public boolean isTopSelectionBox() {
		return topSelectionBox;
	}

	public void setTopSelectionBox(boolean topSelectionBox) {
		this.topSelectionBox = topSelectionBox;
	}

	public String getDefaultCollection() {
		return defaultCollection;
	}

	public void setDefaultCollection(String defaultCollection) {
		this.defaultCollection = defaultCollection;
	}

	public int getVisibleColNum() {
		int visibleNum = 0;
		for (int i=0; i<colVisible.length; i++)
			if (colVisible[i] && isColumnAvailable(i))
				visibleNum++;
		return visibleNum;
	}

	public HashMap<String, Object> getViewParams() {
		return viewParams;
	}
	
	public Object getViewParameter(String name) {
		return viewParams.get(name);
	}

	public void setViewParameter(String name, Object value) {
		viewParams.put(name, value);
	}

	public String getCurrentCollection() {
		return currentCollection;
	}

	public void setCurrentCollection(String currentCollection) {
		this.currentCollection = currentCollection;
	}

	public int getHeatmapMedianColumn() {
		return heatmapMedianColumn;
	}

	public void setHeatmapMedianColumn(int heatmapMedianColumn) {
		this.heatmapMedianColumn = heatmapMedianColumn;
	}

	public int getHeatmapStdDevColumn() {
		return heatmapStdDevColumn;
	}

	public void setHeatmapStdDevColumn(int heatmapStdDevColumn) {
		this.heatmapStdDevColumn = heatmapStdDevColumn;
	}
}
