/*
 * Created on November-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gmerg.utils.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

/**
 * @author Mehran Sharghi
 * 
 */

public class TableUtil {
	
	static final protected String distinguishingParam = "tableViewName";
	
	public static String getActionLinkScript(String formId, String linkId, HashMap<String,String> params) {
		return getActionLinkScript(formId, linkId, Visit.packParams(params));
	}
	
	public static String getActionLinkScript(String formId, String linkId, String params) {
		return "clickExternalCommandLink('"+ formId + "', '" + linkId + "', '" + params + "')";
	}
	
	public static HashMap<String,String> getClickedLinkParams() {
		String params = FacesUtil.getRequestParamValue("clickedLinkParams");
		return Visit.unpackParams(params);
	}
	
	//==========================================================================
	// Public Static Methods
	//==========================================================================
	public static void putTableViewInSession(GenericTableView tableView) {
		// put tableView in session
		String viewName = "tableView_" + tableView.getName();
		//		System.out.println(tableView+"****** put in session --- name= "+viewName);		
		removeTableViewFromSession(viewName);
		FacesUtil.setSessionValue(viewName, tableView);
	}

	public static GenericTableView getTableViewFromSession(String viewName) {
		// get tableView from session
		
		// System.out.println(FacesUtil.getSessionValue("tableView_"+viewName)+"******** get from session -- name= "+ "tableView_"+viewName);	
		if (viewName == null || "".equals(viewName)) 
			return null;
		return (GenericTableView)FacesUtil.getSessionValue("tableView_"+viewName);
	}

	public static GenericTableView getTableViewFromSession() {
		String viewName = FacesUtil.getAnyRequestParamValue(distinguishingParam);
//		if (viewName==null)	
//			return getSelectionsTableViewFromSession();
//		System.out.println("in getTableViewFromSession: viewName=" +viewName);
		return getTableViewFromSession(viewName);
	}

	public static GenericTableView getSelectionsTableViewFromSession() {
		String viewName = FacesUtil.getAnyRequestParamValue("selectionsTableViewName");
		//System.out.println("in getSelectionsTableViewFromSession: viewName=" +viewName);
		return getTableViewFromSession(viewName);
	}

	public static void removeTableViewFromSession(String viewName) {
		// remove tableView from session
		FacesUtil.removeSessionValue("tableView_"+viewName);
	}

	/*
	public static boolean isTableViewInSession () {
		return isTableViewInSession ("");
	}

	public static boolean isTableViewInSession (String viewName) {
		return isTableViewInSession (viewName, "");
	}
	public static boolean isTableViewInSession (String viewName, String parentId) {
		System.out.println("In isTableViewInSession");

		// First look at the requestMap for a signal indicating not to use any table from the session.
		// (This is currently used for navigation to collection page from a table browse page)
		String reloadTable = FacesUtil.getFacesRequestParamValue("reloadTable");
		if (!(reloadTable==null || "".equals(reloadTable))) {
			System.out.println("# tableView not taken from session - reload table request");
			return false;
		}

		String param = new TableBean().getDistinguishingParam();
		FacesUtil.setFacesRequestParamValue(param, viewName);
/*
//		System.out.println(param+"## viewName==="+	viewName);
		if (viewName == null || "".equals(viewName)) {
//			System.out.println("#1 tableView not found in session");
			return false;
		}
* /
		String operation = FacesUtil.getRequestParamValue("operation");
		System.out.println("## operation==="+	operation );
		if (operation == null || "".equals(operation)) {
			GenericTableView tableView = TableBean.getTableViewFromSession(viewName);
//			System.out.println("parentId="+parentId+  "            tableView.parentId(in session)=" + tableView.getParentId());
			if(tableView!=null && (tableView.getParentId()==null || "".equals(tableView.getParentId()) || !tableView.getParentId().equals(parentId)) ) {
				System.out.println("#1 tableView found in session");
				System.out.println(tableView + "-"+tableView.getParentId() + "-"+tableView.getParentId() + "-"+parentId );
				return true;
			}
			else {
				System.out.println("#2 tableView not found in session");
				return false;
			}
		}
		System.out.println("#2 tableView found in session");
		return true;
	}
*/

	public static boolean isTableViewInSession () {
		//System.out.println("In isTableViewInSession");

		// First look at the requestMap for a signal indicating not to use any table from the session.
		// (This is currently used for navigation to collection page from a table browse page)
		String reloadTable = FacesUtil.getFacesRequestParamValue("reloadTable");
		if (!(reloadTable==null || "".equals(reloadTable)) )
			if("no".equalsIgnoreCase(reloadTable) || "false".equalsIgnoreCase(reloadTable)) {
				//System.out.println("# tableView found in session - reloadTable");
				return true;
			}
			else {
				//System.out.println("# tableView not taken from session - reload table request");
				return false;
			}
		String tableOperation = FacesUtil.getRequestParamValue("tableOperation"); // initiated from table e.g navigation, sort, etc
		//System.out.println("## tableOperation==="+	tableOperation );
		if (tableOperation == null || "".equals(tableOperation)) { // || "reload".equals(tableOperation)) {
			//System.out.println("#1 tableView not found in session");
			return false;
		}
		//System.out.println("#1 tableView found in session");
		return true;
	}


	public static void saveTableView(GenericTableView view){
		saveTableView(view, "", "");
	}

	public static void saveTableView(GenericTableView view, String parentIdParam, String parentId){
//		System.out.println("in saveTableView  viewName="+view.getName()+ "   parentIdParam="+parentIdParam + "   parentId="+parentId);
		if (view != null) {
			view.setParentIdParam(parentIdParam);
			view.setParentId(parentId);
			FacesUtil.setFacesRequestParamValue(distinguishingParam, view.getName()); //added to as a workaround for jsf1.2 not passing jsp parameter
			putTableViewInSession(view);
//			System.out.println("TableView saved in session: Name="+view.getName());
		}

	}

	public static void setViewName(String viewName) {
		FacesUtil.setFacesRequestParamValue(distinguishingParam, viewName);
//		System.out.println("**** in setViewName ******* "+distinguishingParam+"       value="+ viewName);
	}
	
	public static void setActiveTableViewName() {
//		System.out.println("**** in setActiveTableViewName ******* ");		
		String activeTableViewName = FacesUtil.getRequestParamValue(distinguishingParam);
		if (activeTableViewName != null && !"".equals(activeTableViewName))
			FacesUtil.setFacesRequestParamValue(distinguishingParam, activeTableViewName);
	}
	
	//***** these two methods can be used to retrieve selected ids from a generic table to be used from outside the template in the same page. TableViewName has to be in request
	public static int getSelectionsNum() {
		int n=0;
		String selectionsString = FacesUtil.getRequestParamValue("selectionsString");
//System.out.println("  ------------>	SelectionString==========="+selectionsString );
		for (int i=0; i<selectionsString.length(); i++)
			if (selectionsString.charAt(i) == '1')
				n++;
		return n;
	}

	public static String[] getSelectedIds() {
		return getSelectedIds(-1);
	}

	public static String[] getSelectedIdsForCollection(int collectionId) {
		return getSelectedIds(collectionId, true);
	}

	public static String[] getSelectedIds(int columnId) {
		return getSelectedIds(columnId, false);
	}

	private static String[] getSelectedIds(int id, boolean isCollectionId) {  // id can be either a column Id (number) or a collection category id. This is specified by the second parameter isCollectionId
		String selectionsString = FacesUtil.getAnyRequestParamValue("selectionsString");
//		System.out.println("   in get selected Ids:	SelectionString==========="+selectionsString + "     id="+id);

		if (selectionsString == null) 
			return null;
//		GenericTableView tableView = getSelectionsTableViewFromSession();
		GenericTableView tableView = getTableViewFromSession();
//		System.out.println("   in get selected Ids:	tableView ="+tableView.getName());
		if(tableView==null) {
			System.out.println("NULLLLLLLLLLL============tableview in getSelectedIds");
			return null;
		}
		
		int columnId = 0;
		if(id==-1) {
			Collection <String> collectionIds = tableView.getCollections().values();
			if (!collectionIds.isEmpty())
				columnId = Integer.parseInt(collectionIds.iterator().next());
		}
		else
			if (isCollectionId) {
				String collectionId = String.valueOf(id);
				columnId = Integer.parseInt(tableView.getCollections().get(collectionId));
			}
			else
				columnId = id;
		return getSelectedIdsFromTableView(tableView, selectionsString, columnId, 0);
	}

	public static String[] getSelectedIdsFromTableView(GenericTableView tableView, String selectionString, int columnId, int offset) {
		DataItem[][] tableData = tableView.getData();
		//Selection Ids might be repeated so I used a Set to uniqly insert them in a list
		HashSet<String> selectedIds = new HashSet<String>();
		int colNum = tableView.getVisibleColNum();
 		int dataOffset = tableView.getFirstRowIndex(); //it is 0 for Offmemory tables and some offset for InMemory tables
		for (int i=0; i<selectionString.length(); i++)
			if (selectionString.charAt(i) == '1') {
				DataItem item;
				if (tableView.isRowsSelectable()) 
					item = tableData[i+dataOffset][columnId];
				else {
					int row = i / colNum + dataOffset;
					int col = i % colNum;
					item = tableData[row][col];
					if (item.isComplex()) {
						
//for (DataItem d:(ArrayList<DataItem>)(item.getValue())) 
//	System.out.println("d==="+d.getValue());
						item = ((ArrayList<DataItem>)(item.getValue())).get(columnId + offset);
//System.out.println(columnId + "     " + offset+ "     value="+ item.getValue());
					}
				}
				String value = (String)item.getValue();
				if (value!=null && !"".equals(value))
					selectedIds.add(value);
			}
		
		String[] selections = new String[selectedIds.size()];

		selectedIds.toArray(selections);
//for(int i=0; i<selections.length; i++)	System.out.println("Selection["+i+"]====="+selections[i]);
		return selections;
		
	}
	
	public static GenericTableAssembler getTableAssembler() {
		GenericTableView tableView = getTableViewFromSession();
		if(tableView==null) 
			return null;
		return tableView.getTable().getAssembler();
	}
	
	public static GenericTableAssembler getTableAssembler(String viewName) {
		GenericTableView tableView = getTableViewFromSession(viewName);
		if(tableView==null) 
			return null;
		return tableView.getTable().getAssembler();
	}

	public static Object getAssemblerParameter(String name) {
		GenericTableAssembler assembler = getTableAssembler();
		return assembler.getDataRetrivalParams().get(name);
	}

	public static void setAssemblerParameter(String name, Object value) {
		GenericTableAssembler assembler = getTableAssembler();
		assembler.getDataRetrivalParams().put(name, value);
	}

	public static void setTableViewNameParam(String viewName) {
		FacesUtil.setFacesRequestParamValue(distinguishingParam, viewName); 
	}
}