package gmerg.beans;

import javax.faces.model.ListDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import java.util.*;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.model.ClipboardDelegateCookieImp;
import gmerg.model.HeatmapDisplayTransform;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.Visit;

/**
 * Managed Bean for Generic tables display. Because there might be multiple tables
 * at the same time in the application it extends from MultipleInstanceBean.
 *
 * @author Mehran Sharghi
 *
*/

public class TableBean extends MultipleInstanceBean {
	
	private boolean debug = false;

	protected DataModel data = null;

	protected DataItem[][] tableData = null;

	protected DataModel columnHeaders = null;

	protected GenericTableView tableView = null;

	protected int[] visibleColumnMap = null;

//	protected String operation = null;

	protected int pageNum = 1;
	protected int pageNum2 = 1;// Bernard - 25/06/2010 - to make the newly added 'go to page' function at the bottom of browse page working
	
	protected boolean[] selections = null; 		// not realy used can be removed

////	protected boolean isDataModelLoaded = false;

	protected int selectionWorkAround = -1;

	protected String selectionsString = "";

	protected int[] totals = null;

	protected String columnSelections = "";
	
	protected String filtersSelection = "";
	
	protected int rowsPerPage = 5;

	static final protected String distinguishingParam = "tableViewName";

	int selectedCollection;

	int clipboardItemsNum;
	
	int heatmapColumnIndex = 0;
	int heatmapColNum = 0;
	
	String component;

	


	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public TableBean() {
		if(debug)
			System.out.println("#######TableBean constructor");
		selectionWorkAround = -1;
		selectionsString = "";
		String type = FacesUtil.getAnyRequestParamValue("collectionType");
		selectedCollection = Integer.parseInt(Utility.getValue(type, "-1"));
				
		if (selectedCollection==-1) {
			clipboardItemsNum = -1;
			selectedCollection = 0;
			/*selectedCollection = 1;*/
		}
		else 
			clipboardItemsNum = ClipboardDelegateCookieImp.getClicpboardSize(selectedCollection);
		
		if(debug)
			System.out.println("TableBean::TableBean | Collection Type= " +type+ " | selectedCollection="+selectedCollection+
			" | clipboardItemsNum= "+clipboardItemsNum);
	}

	public void initInstance(String tableViewName){
		// retrieve tableView object from session
		tableView = TableUtil.getTableViewFromSession(tableViewName);
//		System.out.println(tableView+"--->TableBean initInstance: tableView retrieved from session: "+tableViewName);
		if (clipboardItemsNum == -1) {		//Set defasult collection if none is specified by request parameter
			String currentCollection = tableView.getCurrentCollection();
			if (currentCollection == null) 
				currentCollection = tableView.setToDefaultCollection();
			if (currentCollection != null) 
				selectedCollection = Integer.parseInt(currentCollection);
		}
		else 
			tableView.setCurrentCollection(String.valueOf(selectedCollection));
		
		loadDataModel();
	}

	// ********************************************************************************
	// Private & Helper Methods
	// ********************************************************************************

	// Used in navigation. Set pageId for browse page distinguishing id (this page is going to be displayed next)
	private void setToCollectionPage(String pageId) {
		FacesUtil.setFacesRequestParamValue("reloadTable", "yes");
		FacesUtil.setFacesRequestParamValue("collectionPage", pageId);	// is used for old collection mechanisim
	}

	private String[] getSelectedSubmissions() {
		if(debug)
			System.out.println("TableBean::getSelectedSubmisssions | SelectionString="+selectionsString );
		int idCol = tableView.getIdCol(selectedCollection);
		if(debug)
			System.out.println("TableBean::getSelectedSubmisssions | selectedCollection="+selectedCollection+" | idCol="+idCol);
		
		//return TableUtil.getSelectedIdsFromTableView(tableView, selectionsString, 1, 0);
		return TableUtil.getSelectedIdsFromTableView(tableView, selectionsString, idCol, 0);//offset);
	}

	static int logLevel=0; //0-2;
	private void log(String message) {
		log(message, 2);
	}
	private void log(String message, int level) {
		int printLog = logLevel;
		if (level <= printLog)
			System.out.println(message);
	}
	
	private void loadDataModel() {
		log("######## In loadDataModel  -- TableBean	view="+tableView.getName());
		if (isTableEmpty() || isFilteredTableEmpty())
			return;

		GenericTable table = tableView.getTable();

		if (tableView.isDisplayTotals())
			totals = table.getTotals();

		// Heatmap
		boolean displayHeatmap = tableView.isDisplayHeatmap();
//System.out.println("displayHeatmap======="+displayHeatmap);		
		int[] heatmapCols = tableView.getHeatmapColumns();
		heatmapColumnIndex = tableView.getHeatmapColumnIndex();
		heatmapColNum = (heatmapCols==null)?0 : heatmapCols.length;
		HeatmapDisplayTransform displayTransform = tableView.getHeatmapDisplayTransform();

		boolean[] visibleCol = tableView.getDisplayColumns();
		
		ArrayList<Integer> colMapList = new ArrayList<Integer>();

		int colOffset = 0;
		// create header info
		ArrayList<HeaderItem> headerList = new ArrayList<HeaderItem>();
		if (tableView.isRowsSelectable()) {	
			headerList.add(new HeaderItem("Select", false, 2));
			colMapList.add(0);
			colOffset = 1;
		}
		HeaderItem[] headers = table.getHeader();
		for (int i=0; i<headers.length; i++) {
			if(displayHeatmap && i==heatmapColumnIndex) {
				for(int j=0; j<heatmapColNum; j++) {
					headerList.add(new HeaderItem(headers[heatmapCols[j]].getTitle(), headers[heatmapCols[j]].isSortable(), 1, headers[heatmapCols[j]].getImageName()));
					colMapList.add(-(heatmapCols[j]+colOffset));	// negative is to identify heatmap columns
				}
			}
			log("~~visible["+i+"]  ======  "+visibleCol[i]);
			if(visibleCol[i]) {
				headerList.add(headers[i]);
				colMapList.add(i+colOffset);
				log("~~header["+i+"]  ======  "+headers[i].getTitle()+ "	  map==="+(i+colOffset));
			}
		}
		columnHeaders = new ListDataModel(headerList);


		
		// create data model
 		tableData = tableView.getData();
 		if (tableData==null)
 			log ("Warning(Gudmap): Table data is null - in TableBean loadDataModel. view name="+tableView.getName(), 0);
 		int dataOffset = tableView.getFirstRowIndex(); //it is 0 for Offmemory tables and some offset for InMemory tables
		int rowNum = tableView.getDisplayRowsPerPage();
		int colNum = table.getNumCols();
		if (tableData == null && rowNum>0)
			log("Error in table data:  data is null but row number is " + rowNum, 0);
		else {
			if (tableData.length!=rowNum) 
				log("Error in table data. Inconsistenancy between data and number of rows;  ---actual rows="+tableData.length+"  rows="+rowNum, 0);
			if (tableData[0] == null )
				log("table column number is zero");
		}
		
		log(selectionsString+"~~~~~~~rows="+rowNum+"	~~~~~~cols="+colNum+"	~~~~totalrows="+tableView.getTable().getNumRows()+"	   hetamapindex="+heatmapColumnIndex);
	
		ArrayList<ArrayList> rowsList = new ArrayList<ArrayList>();
		String s = "";
		for(int i=0; i<rowNum; i++) {
			ArrayList<Object> row = new ArrayList<Object>();
			if (tableView.isRowsSelectable()) 
				row.add(false);
			double[] heatmapRow = new double[heatmapColNum];
			double median = 0;
			double stdDev = 0;
			boolean medianCalculated = tableView.getHeatmapMedianColumn()>=0;
			boolean stdDevCalculated = tableView.getHeatmapStdDevColumn()>=0;
			if (medianCalculated)
				median = Double.parseDouble(tableData[i+dataOffset][tableView.getHeatmapMedianColumn()].getValue().toString());
			if (stdDevCalculated)
				stdDev = Double.parseDouble(tableData[i+dataOffset][tableView.getHeatmapStdDevColumn()].getValue().toString());
			
			for (int j=0; j<colNum; j++) {
				//System.out.println("columnHeaders = " + ((HeaderItem)columnHeaders.(i)).getTitle() );
				if(displayHeatmap && j==heatmapColumnIndex) {
					for(int k=0; k<heatmapColNum; k++) {
						if (tableData[i+dataOffset][heatmapCols[k]].getValue().getClass() == s.getClass()) {
							s = (String)(tableData[i+dataOffset][heatmapCols[k]].getValue());
							log("~~~"+i+"   "+k+"  =heatmap==s==  "+s);
							heatmapRow[k] = Double.parseDouble(s);
						}
						else
							heatmapRow[k] = (Double)(tableData[i+dataOffset][heatmapCols[k]].getValue());
					}
					if (!medianCalculated) {
						median = HeatmapDisplayTransform.findMedian(heatmapRow);
						medianCalculated = true;
					}
					if (!stdDevCalculated) {
						stdDev = HeatmapDisplayTransform.findStdDev(heatmapRow);
						stdDevCalculated = true;
					}
					
					for(int k=0; k<heatmapColNum; k++) {
						double value;
						if (tableData[i+dataOffset][heatmapCols[k]].getValue().getClass() == s.getClass()) {
							s = (String)(tableData[i+dataOffset][heatmapCols[k]].getValue());
							log("~~~"+i+"   "+k+"  =heatmap==s==  "+s);
							value = Double.parseDouble(s);
						}
						else
							value = (Double)(tableData[i+dataOffset][heatmapCols[k]].getValue());
						
						double adjustedValue = value - median;
						adjustedValue /= stdDev;
						adjustedValue = displayTransform.getAdjustedExpression(adjustedValue);
						
						
//						double adjustedValue = displayTransform.getAdjustedExpression(value);
						
						
//						row.add(new DataItem(adjustedValue, String.valueOf(adjustedValue), 25));
//						row.add(new DataItem(adjustedValue));
						row.add(new DataItem(adjustedValue, String.valueOf(value), 25));
						
						log("~~~"+i+"   "+k+"  =heatmap=====  "+adjustedValue);
					}
				}
//				if(visibleCol[j]) { // xingjun - 23/09/2010 - sometimes it cause NullPointerException
				if(visibleCol != null && visibleCol[j]) {
//					if (tableData[i+dataOffset][j]==null) { // xingjun - 23/09/2010 - sometimes it cause NullPointerException
//						log("~~~"+i+"   "+j+"  ======  "+tableData[i][j]+"************************* PROBLEM *******************");
//						row.add(new DataItem("no data")); // add a dummy item instead of null to avoid exceptions
					if (tableData == null || tableData[i+dataOffset][j]==null) {
						if (tableData == null) {
							if(debug)
								System.out.println("TableBean::loadData | tableData is null");
						} else {
							if(debug)
								System.out.println("TableBean::loadData | i="+i+" | j="+j+" | tableData[i][j]="+tableData[i][j]+" !!!!PROBLEM");
			
						}
						row.add(new DataItem("no data")); // add a dummy item instead of null to avoid exceptions
						if(debug)
							System.out.println("TableBean::loadData | added no-data dummy item");
					}
					else {
						log("~~~"+i+"   "+dataOffset+"   "+j+"  ======  "+tableData[i+dataOffset][j].getValue());
						DataItem item = tableData[i+dataOffset][j];
						if (item.isComplex()) {
							ArrayList <DataItem> multipleValue = (ArrayList <DataItem>)item.getValue();
							multipleValue.add(0, new DataItem("", -5));	// add a fake DataItem at the begining of the list so it won't display when accessing a complex value before the for loop (in the jsp page) that diplays it properly  
							if (tableView.getSelectable() == 2) 	//cells are selectable
								if (tableView.isTopSelectionBox())
									multipleValue.add(1, new DataItem(true, 20));	//add a Checkbox for individual cell at the top of cell
								else
									multipleValue.add(new DataItem(true, 20));	//add a Checkbox for individual cell at the buttom of cell
						}
						row.add(item);
					}
				}
			}
			rowsList.add(row);
		}
		data = new ListDataModel(rowsList);

		// create visible column mapping (only headers for visible columns are considered)
	  	visibleColumnMap = new int[colMapList.size()];
	  	for(int i=0; i<colMapList.size(); i++)
	  		visibleColumnMap[i] = colMapList.get(i);
	}

	//==========================================================================
	// Public Methods
	//==========================================================================
	public String getDistinguishingParam() {
//		System.out.println("in TableBean.getDistinguishingParam");
		return distinguishingParam;
	}

	public int getColumnIndex(){
		if (columnHeaders.isRowAvailable())
	  		return Math.abs(visibleColumnMap[columnHeaders.getRowIndex()]);
		return -1;
	}
	
	private int getColumnIndex(String columnName)
	{
		int columnIndex = -1;
		HeaderItem[] headers = tableView.getTable().getHeader();
		for (int i=0; i<headers.length; i++)
			if (headers[i].getTitle().equals(columnName)) {
				columnIndex = i;
				break;
			}

		return columnIndex;
	}

	// ********************************************************************************
	// Listeners & actions
	// ********************************************************************************

	//******************************************* Reload *********************************************
	public String reload() {
		setClipboardItemsNum(ClipboardDelegateCookieImp.getClicpboardSize(selectedCollection));
		if(!tableView.isDisplayInFrame())
			loadDataModel();

		return null;
	}

	//************************************** Body Height Change **************************************
	public String heightModeChange() {
		String tableOperation = FacesUtil.getRequestParamValue("tableOperation");
		if ("limittedHeight".equals(tableOperation) )
			tableView.setHeightMode(3);
		else
			tableView.setHeightMode(1);

		if(!tableView.isDisplayInFrame())
			loadDataModel();

		return null;
	}

	//****************************************** Navigation ******************************************
	public String tableNavigation() {
		String tableOperation = FacesUtil.getRequestParamValue("tableOperation");
//		System.out.println("statusParams================"+FacesUtil.getRequestParamValue("statusParams"));
//		System.out.println("in navigation  tableOperation="+operation+ "   pageNum="+pageNum);
		if (tableView.processNavigation(tableOperation, pageNum))
//			data.setWrappedData(tableView.getData());
			if(!tableView.isDisplayInFrame())
				loadDataModel();

		return "refreshTable";  // This is for wide tables only
	}

	//********************************************** Sort ********************************************
	public String sort(){
	   	String title = FacesUtil.getRequestParamValue("title");
//		System.out.println("in sort: col="+title);
		tableView.sort(getColumnIndex(title));
		if(!tableView.isDisplayInFrame())
			loadDataModel();

			return null;
	}

	public String sortByDefault() {
//		System.out.println("in sort by default");
		tableView.sort(-1);
		if(!tableView.isDisplayInFrame())
			loadDataModel();

		return null;
	}


	/*
	 * Delete ISH/Microarray submission into my collection
	 */
	public String deleteSelectedEntries() {
//		System.out.println("In delete selected submisssions");
		String[] selectedIds = getSelectedSubmissions();
	
		boolean result = tableView.getTable().deleteRows(selectedIds);

		if(result && !tableView.isDisplayInFrame())
			loadDataModel();

		return null;
	}

	//***************************************** Items per Page ***************************************
	public String changeResultsPerPage() {
		// entries per page automatically set using the value in the page
   		tableView.setRowsPerPage(rowsPerPage);
		if(!tableView.isDisplayInFrame())
	   		loadDataModel();

		return null;
	}


	public String getResultsPerPage() {
	   	return String.valueOf(tableView.getRowsPerPage());
	}

	public void setResultsPerPage(String n) {
		rowsPerPage = Integer.parseInt(n);
	}

	public List getPerPageOptions() {
		int[] perPageOptions = tableView.getPerPageOptions();

		List selectOptions = new ArrayList();
		for (int i=0; i<perPageOptions.length; i++) 
			selectOptions.add(new SelectItem(String.valueOf(perPageOptions[i])));

		return selectOptions;
	}

	//***************************************** Apply Filter *****************************************
	public String applyFilter() {
	
		tableView.refreshTable();
		reload();
		return null;
	}

	//*************************************** Add/Remove Column **************************************
	public String applyColumnSelection() {
		boolean[] visibleCols = tableView.getColVisible();
		for (int i=0, j=0; i<visibleCols.length; i++)
			if (tableView.isColumnAvailable(i))
				visibleCols[i] = (columnSelections.charAt(j++) == '1');
   		loadDataModel();

		return null;
	}

	//****************************************** Collection ******************************************
	/*
	 * Add ISH/Microarray submission into my collection
	 */
	public String addToClipboard() {
//		System.out.println("In addToCollection ##SelectedCollection=========="+selectedCollection);
		String[] selectedSubs = getSelectedSubmissions();
		if(debug)
			System.out.println("TableBean::addToClipboard | selections="+Utility.toArrayList(selectedSubs).toString());
		setClipboardItemsNum(ClipboardDelegateCookieImp.addToClicpboard(selectedSubs, selectedCollection));
		return null;
	}
	
	public String replaceClipboardWithSelected() {
//		System.out.println("==========In replace Clipboard With Selected");
		String[] selectedSubs = getSelectedSubmissions();
		setClipboardItemsNum(ClipboardDelegateCookieImp.replaceClicpboard(selectedSubs, selectedCollection));
		return null;
	}

	public String replaceClipboard() {
//		System.out.println("==========In replace Clipboard");
		String columnId = tableView.getCollections().get(String.valueOf(selectedCollection));
		if (columnId==null) {
			System.out.println("Warnning! replaceClipboard in tableBean no collection is associated with tableView");
			return null;
		}
		GenericTableAssembler assembler = tableView.getTable().getAssembler();
		ArrayList<String> ids;
		if (OffMemoryCollectionAssembler.class.isAssignableFrom(assembler.getClass()))
			ids = ((OffMemoryCollectionAssembler)assembler).getCollectionIds();
		else {
			ArrayList<Object> columnValues = assembler.getColumnValues(Integer.parseInt(columnId)); //assemblers needing this should implement the method
			if (columnValues == null)
				return null;
			ids = new ArrayList<String>();
			for (Object item: columnValues)
				ids.add(item.toString()); 
		}
		String[] tempArray = new String[ids.size()];
		setClipboardItemsNum(ClipboardDelegateCookieImp.replaceClicpboard(ids.toArray(tempArray), selectedCollection));
		return null;
	}

	public String removeClipboardWithSelected() {
//		System.out.println("==========In remove Clipboard With Selected");
		String[] selectedSubs = getSelectedSubmissions();
		setClipboardItemsNum(ClipboardDelegateCookieImp.removeValuesFromClipboard(selectedCollection, selectedSubs).size());
		
		if (selectedCollection == 0)
			return "leaf0";
		else if (selectedCollection == 3)
			return "leaf3";
		else if (selectedCollection == 4)
			return "leaf4";
		else
			return null;
		
	}	
	
	public String removeCollectionWithSelected() {
		FacesUtil.setFacesRequestParamValue("selectionsString", selectionsString);
		String[] selectedSubs = getSelectedSubmissions();
		
		String type = FacesUtil.getAnyRequestParamValue("collectionType");
		String collectionId = FacesUtil.getAnyRequestParamValue("collectionId");
		
		CollectionInfo collectionInfo = CollectionAssembler.instance().getCollectionInfo(Integer.parseInt(collectionId));
		collectionInfo.setOwner(Utility.getUser().getUserId());
		String currentDate = Utility.convertToDatabaseDate(new Date());
		collectionInfo.setLastUpdate(currentDate);
		
		CollectionAssembler.instance().removeCollectionItems(collectionId, Utility.getUser().getUserId(), selectedSubs);
		
		return "leaf";
		
	}	

	public String removeAllValuesFromClipboard() {
//		System.out.println("==========In removeAllClipboardValues");
		
		ClipboardDelegateCookieImp.removeAllValuesFromClipboard(selectedCollection);
		setClipboardItemsNum(0);
//		return null;
		return "leaf";
	}

    public String getResultURL () {
		String type = FacesUtil.getAnyRequestParamValue("collectionType");
		String id = FacesUtil.getAnyRequestParamValue("collectionId");

		String result = "collection_browse.html?collectionId="+id+"&collectionType="+type;
    	
    	if (debug)
    		System.out.println("getSearchParams:result: " + result);
    	
        return result;
    }

    public String getSearchParams(){
	
		String urlParams = null;
		HashMap<String, String> params = new HashMap<String, String>();		
		
		Integer selectedCollection = getSelectedCollection();
		params.put("collectionType", selectedCollection.toString());
		
    	urlParams = Visit.packParams(params);	
    	
		if (urlParams==null)
			return "";

		return "?" + urlParams;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getSelectionsString () {
		return selectionsString;
	}

	public void setSelectionsString (String s) {
		selectionsString = s;
	}

	public DataModel getData() {
		return data;
	}

	public DataModel getColumnHeaders() {
		return columnHeaders;
	}

	public String getTotal() {
		if (!tableView.isDisplayTotals())
			return "";

	   	int col = getColumnIndex();

		if (tableView.isRowsSelectable()) 
			return (col==0)? "" : "("+String.valueOf(totals[col-1])+")";
		else
			return "("+String.valueOf(totals[col])+")";
	}

	public DataItem getDataItem() {
		DataItem item = getDataItem0();
		if (!item.isComplex()) {
			complexValue = null;	// This is to make sure to reset complexValue when moving to a normal item in case that isComplexDataItem is not called 
			return item;
		}
		if (complexValue==null)		// complexValue not initialised yet. It can be initialised by calling getDataItemComplexValue
			return item;
		
		if (!complexValue.isRowAvailable())	// this is to avoid exception - the return item is not one of the complex items 
			return item;
		
	   	DataItem item1 = (DataItem)complexValue.getRowData();	// access complex DataItem
	   	item1 = adjustDataItem(item1); 
		return item1;
	}
	
	public Object getDataItemValue() {
	   	return getDataItem().getValue();
	}
	
	public int getDataItemtype() {
	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return col;
		if (col==0 && tableView.isRowsSelectable())
   			return 20;
	   	return getDataItem().getType();
	}
	
//**********************	
	DataModel complexValue = null;
	int currentRow = 0;
	int currentCol = 0;
	DataItem currentItem = null;
	
	public String getCurrentRow()
	{
		return Integer.toString(currentRow);
	}
	
	public String getCurrentCol()
	{
		return Integer.toString(currentCol);
	}
	
	public Object getDataItemComplexValue() {	// this will also initialise complexValue for later access 
		DataItem item = getDataItem0();
		if (!item.isComplex())
			return null;
	   	return complexValue;
	}
	
	public String getComplexDataItemStyle() {
		DataItem item = getDataItem0();
		if (!item.isComplex())
			return null;
		int type = getDataItem0().getType();
		switch (type) {
			case 80 : return "margin-left:0";
			case 81 : return "margin:auto";
			case 82 : return "margin-right:0";
		}
		return "";
	}
	
	public boolean isComplexDataItem() {
	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return false;
		if (col==0 && tableView.isRowsSelectable())
   			return false;
		return getDataItem0().isComplex();
	}
	
//	**********************
	public DataItem getDataItem0() {
		int dataCol = columnHeaders.getRowIndex();
		int dataRow = data.getRowIndex();
		if (currentCol==dataCol && currentRow==dataRow && currentItem!=null)
			return currentItem;
		currentCol = dataCol;
	   	currentRow = dataRow;
		//System.out.println(data.getRowData()+"-------col=="+dataCol);
	   	currentItem = (DataItem)(((ArrayList)data.getRowData()).get(dataCol));

		if (currentItem.isComplex()) 	
			complexValue = new ListDataModel((ArrayList<DataItem>)currentItem.getValue());	// this initialise the item only once an for the first time row/col changes

		currentItem = adjustDataItem(currentItem); 
		return currentItem;
	}
	
	private DataItem adjustDataItem(DataItem item) {
		int col = getColumnIndex();
		if (tableView.isRowsSelectable())
			col--;
		int maxWidth = tableView.getColMaxWidth(col);
		if (maxWidth == 0)
			return item;
		String currentValue =  (String)item.getValue();
//		System.out.println("currentValue=="+currentValue);		
		if (currentValue==null || currentValue.getClass()!=String.class)
			return item;
		if (currentValue.length() <= maxWidth)
			return item;
		String shortValue = currentValue.substring(0, maxWidth-2) + "\205";
		if(tableView.getColShortFormType(col) > 0)
	   		return new DataItem(shortValue, currentValue, "", tableView.getColShortFormType(col)); //url to popup window to display full value
		else
			return new DataItem(shortValue);
	}

//******************************************************************************	
//******************************************************************************	
//******************************************************************************	
//******************************************************************************	

	// the mouseover display for the column headers
	public String getColumnHeaderMouseOver() {
	   	if (!tableView.isDisplayHeatmap())
	   		return "";
		
	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return "";
		if (col == 0 && tableView.isRowsSelectable())
   			return "";

		HeaderItem headerItem = (HeaderItem)columnHeaders.getRowData();
	   	return (headerItem.getType() == 1)? headerItem.getMouseOver() : "";
	
	}
	
	public String getColumnStyle() {
	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return "";
	   	String borderStyle = "";
		if (col == 0 && tableView.isRowsSelectable())
				return "";
		
	   	if (tableView.isDisplayHeatmap() && col>0) {
		   	borderStyle = "border-left:2px solid white;";
		   	if (isHeatmapCol()) {
		   		if (isFirstHeatmapCol())
		   			return borderStyle + getDataItemHeatmapStyle();
	   			return getDataItemHeatmapStyle();
		   	}
	   	}
	    String verticalAlignStyle;
	    switch (tableView.getVerticalAlign()) {
	    	case  1: verticalAlignStyle = "vertical-align:top;"; break;
	    	case  2: verticalAlignStyle = "vertical-align:bottom;"; break;
	    	default: verticalAlignStyle = "vertical-align:middle;"; break;
	    }
	    
	   	String textAlign = "";
	   	
		col = getColumnIndex();
		if (tableView.isRowsSelectable())
			col--;
	   	switch (tableView.getColAlignment(col)) {
	   		case  0: textAlign = "text-align:left;"; break; 
	   		case  2: textAlign = "text-align:right;"; break;
	   	}

	   	return borderStyle + getWrapStyle() + verticalAlignStyle + textAlign;
	}

	private boolean isHeatmapCol() {
		if (!tableView.isDisplayHeatmap())
			return false;
		
	   	int col = getColumnIndex();
	   	if (col==0 && tableView.isRowsSelectable())
			return false;
	   	
  		return visibleColumnMap[columnHeaders.getRowIndex()] < 0;
	}
	
	public String getColumnHeaderStyleClass() {
		String styleClass = "align-top-stripey";
	   	if (!tableView.isDisplayHeatmap())
	   		return styleClass;
	   	
	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return "";
	   	
	   	if (isHeatmapCol() && !isFirstHeatmapCol())
	   		styleClass += " heatmapHeader";
	   	else
	   		styleClass += (col==0)? " noneHeatmapHeaderFirst" : " noneHeatmapHeader";
	   	return styleClass;
	}

	public String getCellSpacing() {
	   	if (tableView.isDisplayHeatmap()) 
	   		return "0";
	   	return "2";
	}
	
	public String getCellPadding() {
	   	if (tableView.isDisplayHeatmap()) 
	   		return "1";
	   	return "2";
	}
	
	public boolean isFirstHeatmapCol() {
		return (getColumnIndex() - (tableView.isRowsSelectable()? 1 : 0) == tableView.getHeatmapColumnIndex());
	}

//********************************************
	public String getDataItemHeatmapStyle() {
		double value = (Double)getDataItemValue();
		int colorValue = (int)Math.round(value*255);
if(!true)		
return "background-color:" + htmlColor(150,150, 150);
		
		if (colorValue < 0)
			return "background-color:" + htmlColor(0, 0, -colorValue);
		else
			return "background-color:" + htmlColor(colorValue, 0, 0);
	}

	public String htmlColor(int r, int g, int b) {
		return "#" + twoDigitHex(r) + twoDigitHex(g) + twoDigitHex(b);
	}

	private String twoDigitHex(int value) {
		value = Math.min(255, value);
		value = Math.max(0, value);
		String hex = Integer.toHexString(value);
		if (hex.length() < 2 )
			hex = "0" + hex;
		return hex;
	}


	public void setDataItem(Object value) {
//		System.out.println(tableView.getVisibleColNum()+"~~~~~~~~~~~~~~~~~~~~~~~ " + data.getRowIndex()+"  "+columnHeaders.getRowIndex()+"   "+value+"	 "+value.getClass());	
		if (!tableView.isRowsSelectable())
			return;
		
		selectionWorkAround++;
		int numCols = tableView.getDisplayColNum() + 1;
		if (selectionWorkAround% numCols == 0) {
			int rowIndex = (int)(selectionWorkAround/numCols);
//			ArrayList displayedData = (ArrayList)(data.getWrappedData());
//			ArrayList row = (ArrayList)displayedData.get(rowIndex);
//			row.set(0, value);
			selections[rowIndex] = (Boolean)value;
//			System.out.println("setDataItem~~~~~~~"+selectionWorkAround+"		   "+rowIndex+"  "+value);
		}
	}

	public String getWrapStyle()
	{
		int col = getColumnIndex();
		String wrapStyle = "";

		if (data.isRowAvailable() && col>=0){
			if (tableView.isRowsSelectable()) {
				if (col>0)
					if(!tableView.getColWrap()[col-1])
						wrapStyle += "nowrap";
			}
			else
				if(!tableView.getColWrap()[col])
					wrapStyle += "nowrap";
		}
		else
			wrapStyle += "auto";

		return ((wrapStyle.length()>0)? "white-space:" : "") + wrapStyle + ";";
	}

	public boolean isDefaultOrderDefined() {
		HeaderItem[] headers = tableView.getTable().getHeader();
		for (HeaderItem header: headers)
			if (header.isSortable())
				return true;
		return false;
	}
	
	public GenericTableView getTableView() {
//		System.out.println("getTableiew");
		return tableView;
	}

	public void setTableView(GenericTableView tableView) {
		this.tableView = tableView;
	}

	public String getWidth(){
		int w = tableView.getWidth();
		if (w == 0)
			return "100%";
		else
			return String.valueOf(tableView.getWidth());
	}

	public String getTime(){
		return String.valueOf(System.currentTimeMillis());
	}

	public String getPageNum() {
//		return (pageNum>0)?String.valueOf(pageNum):"";
		return "";
	}

	public void setPageNum(String pageNum) {
		try {
			this.pageNum = Integer.parseInt(pageNum);
		}
		catch (NumberFormatException e) {
			this.pageNum = 1;
		}
	}
	
	// added by Bernard - 25/06/2010
	public String getPageNum2() {
		return "";
	}

	public void setPageNum2(String pageNum2) {
		try 
		{
			this.pageNum2 = Integer.parseInt(pageNum2);
			if (this.pageNum2 > 1) 
				this.pageNum = this.pageNum2;
				
		}
		catch (NumberFormatException e) 
		{
			this.pageNum2 = 1;
		}
	}	

	public String getTableBodyFormTarget() {
		if (tableView.getNumPages()>1)
			return "_parent";
		else
			return "_self";
	}

	public boolean isTableEmpty() {
//		System.out.println("TableBean: isTableEmpty viewName=="+((tableView==null)?"Null":tableView.getName()));

		if (tableView==null || tableView.getTable()==null)
			return true;
		int numRows = tableView.getTable().getNumRows();
		if (numRows==0 && ! isFilterActive() )
			return true;
		return false;
	}

	public boolean isFilteredTableEmpty() {
		int numRows = tableView.getTable().getNumRows();
		if (numRows==0 && isFilterActive())
			return true;
		return false;
	}
	
	public boolean isFilterDefined() {
		GenericTableFilter filter = tableView.getTable().getAssembler().getFilter();
		return filter != null;
	}
	
	public DataModel getFilterList() {
		GenericTableFilter filter = tableView.getTable().getAssembler().getFilter();
		ArrayList<FilterItem> filterList = new ArrayList<FilterItem>();
		
		if (filter != null)
			for (FilterItem filterItem :filter.getFilters().values()) {
				int col = filterItem.getCol();
				if (tableView.isColumnAvailable(col)) 
					filterList.add(filterItem);
				
			}
		return new ListDataModel(filterList);
	}
	
	public String getColumnSelections() {
		return columnSelections;
	}

	public void setColumnSelections(String columnSelections) {
		this.columnSelections = columnSelections;
	}

	public int getVisibleColNum() {
		return tableView.getVisibleColNum();
	}
	
	public void setVisibleColNum(int colNum) {
		return; // this function exists to prevent problems caused by tomhawk data table update model
	}

	public DataModel getColumnList() {
		HeaderItem[] headers = tableView.getTable().getHeader();
		boolean[] visibleCols = tableView.getColVisible();

		ArrayList<ArrayList> columnList = new ArrayList<ArrayList>();
		for (int i=0; i<headers.length; i++)
			if (tableView.isColumnAvailable(i)) {
				ArrayList<Object> column = new ArrayList<Object>();
				column.add(visibleCols[i]);
				column.add(headers[i].getShortTitle());
				columnList.add(column);
			}

		return new ListDataModel(columnList);
	}

	public String getDefaultColumns() {
		boolean[] defaultCols = tableView.getDefaultColVisible();
		if (defaultCols == null)
			return "";

		String s = "";
		for(int i=0; i<defaultCols.length; i++)
			if (tableView.isColumnAvailable(i))
				s += (defaultCols[i])? "1" : "0";

		return s;
	}

	public String getColumnClasses() {
//		return colClasses;		a list of classes for columnclasses tag of t:dataTable currently doesn't work, it may be fixed it in future
		return "heatmap";
	}

	public void setDefaultColumns(String s) {   // Only to avoid page update problem for hidden field defaultColumns in the jsp page
		return;
	}

	//******** these are to avoid direct access and automatic modification by page update of tableView values
	public String getTableViewName() {
		if (tableView != null)
			return tableView.getName();
		else
		{
			System.out.println("** TableBean:getTableViewName tableView is null **");
			return null;
		}
	}

	public void setTableViewName(String tableViewName) { // Only to avoid page update problem for hidden field tableViewName in the jsp page
		return;
	}
	
	public int getMaxPageNum() {
		return tableView.getNumPages();
	}
	
	public void setMaxPageNum(int n) {	// Only to avoid page update problem for hidden field maxPageNum in the jsp page
	}
	
	public int getActualRowsPerPage() {
		return tableView.getDisplayRowsPerPage();
	}
	
	public void setActualRowsPerPage(int n) {	// Only to avoid page update problem for hidden field maxPageNum in the jsp page
	}

	public int getClipboardItemsNum() {
		Integer passedValue = (Integer)FacesUtil.getFacesRequestParamObject("Clipboard"+selectedCollection);
		if (passedValue != null) 
			clipboardItemsNum = passedValue;
		if (clipboardItemsNum == -1) 
			clipboardItemsNum = ClipboardDelegateCookieImp.getClicpboardSize(selectedCollection);
		return clipboardItemsNum; 
	}
	
	private void setClipboardItemsNum(int value) {
		clipboardItemsNum = value;
		if(debug)
			System.out.println("############TableBean::setClipboardItemsNum | value="+value);
		FacesUtil.setFacesRequestParamValue("Clipboard"+selectedCollection, value);
	}
		
	public String getClipboardName() {
		return Globals.getCollectionCategoriesNames()[selectedCollection];
	}

	public boolean isMultipleCollections() {
		HashMap<String, String> collections = tableView.getCollections();
		return collections.size()>1;
	}

	public SelectItem[] getCollectionsSelectItems() {
		String[] collectionCategories = Globals.getCollectionCategoriesNames();
		HashMap<String, String> collections = tableView.getCollections();
		int n = collections.size();
		SelectItem[] categories = new SelectItem[n];
		Set<String> collectionIds = collections.keySet();
		Iterator<String> iterator = collectionIds.iterator();
		int i=0;
		while (iterator.hasNext()) {
			int collectionId = Integer.parseInt(iterator.next());
			if(debug)
				System.out.println("TableBean::getCollectionsSelectItems | collectionId="+collectionId+" | category="+collectionCategories[collectionId]);
			categories[i++] = new SelectItem(collectionId, collectionCategories[collectionId]);
		}
		return categories;
	}

	public int getCollectionBottons() {
		//if(debug)
			//System.out.println("TableBean::getCollectionBottons | tableView.getCollectionBottons="+tableView.getCollectionBottons());
		if (tableView.getCollections().size()==0)   //I am not sure about keeping this line
		{
			//if(debug)	
				//System.out.println("TableBean::getCollectionBottons | tableView.getCollections().size()=0");
			return 0;
		}
		
		return tableView.getCollectionBottons();
	}

	public int getSelectedCollection() {
		return selectedCollection;
	}

	public void setSelectedCollection(int selectedCollection) {
		this.selectedCollection = selectedCollection;
	}
	
	public int getCellSelection() {
		if (tableView.getSelectable() < 2)
			return 0;
		return tableView.getVisibleColNum();		//when individual cells are selectable
	}
	
	public void setCellSelection(int value) {		//Dumy setter for the hidden input 
	}

	public String getFiltersSelection() {
		return filtersSelection;
	}

	public void setFiltersSelection(String filtersSelection) {
		this.filtersSelection = filtersSelection;
	}
	
	public boolean isFilterActive() {
		GenericTableFilter filters = tableView.getTable().getAssembler().getFilter();
		if (filters==null)
			return false;
		LinkedHashMap<String, FilterItem> filterList  = filters.getFilters();
		for (FilterItem filterItem: filterList.values())  
			if (filterItem.isActive()) 
				return true;
		return false; 
	}

	public String getHeatmapMouseOver() {
		if (!tableView.isDisplayHeatmap())
	   		return "";
		
	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return "";
		if (col == 0 && tableView.isRowsSelectable())
   			return "";
		
		HeaderItem headerItem = (HeaderItem)columnHeaders.getRowData();
	   	//return (headerItem.getType() == 1)? headerItem.getMouseOver() + " :: " + getDataItem().getTitle() : "";	
	   	return (headerItem.getType() == 1)? tableData[currentRow][0].getValue() + ", " + headerItem.getTitle() + ", " + getDataItem().getTitle() : "";	

	   	//return (headerItem.getType() == 1)? headerItem.getMouseOver() + " - " + getDataItem().getTitle() + " - " + getColumnIndex() + " - " + tableData[1][0].getValue() : "";	
	}

	public String getComponent() {
		if (!tableView.isDisplayHeatmap())
	   		return "";
		
		int index = getColumnIndex("Probe Id");

	   	int col = columnHeaders.getRowIndex();
	   	if (col<0)
	   		return "";
		if (col == 0 && tableView.isRowsSelectable())
   			return "";

		HeaderItem headerItem = (HeaderItem)columnHeaders.getRowData();
	   	component = (headerItem.getType() == 1)? headerItem.getTitle() : "";
	   	
	   	return component;
	}
	
	public String gotoCollectionPage(){
		
		return "collection_browse.html?collectionType=" + getSelectedCollection();
	}
	

}
