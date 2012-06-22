package gmerg.beans;

import gmerg.assemblers.QueryAssembler;
import gmerg.utils.FacesUtil;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.OffMemoryTable;
import gmerg.utils.table.TableUtil;

import java.util.HashMap;

public class ISHSelSubmissionsBean {
    protected boolean debug = false;
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public ISHSelSubmissionsBean() {
	if (debug)
	    System.out.println("ISHSelSubmissionsBean.constructor");

		if (TableUtil.isTableViewInSession())
			return;
	
		String viewName = "queryPageResult";
		TableUtil.saveTableView(populateQueryPageResultTableView(viewName));
	}
	
	public boolean getDumy() {
		return false;
	}
    
	protected GenericTableView populateQueryPageResultTableView(String viewName) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();

		//get all possible parameters from request map
		getRequestQueryParams(queryParams);
		if (queryParams.get("queryType") == null)
			return ISHBrowseBean.getDefaultIshBrowseTableView(viewName, null);  //return null table
		
		QueryAssembler assembler = new QueryAssembler(queryParams);
		GenericTable table = assembler.createTable();
		GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(false);
		
		return  tableView;
	}

    private void getRequestQueryParams(HashMap<String, Object> params) {
		params.put("queryType", FacesUtil.getRequestParamValue("queryType"));
		params.put("components", FacesUtil.getRequestParamValues("components"));
		params.put("component", FacesUtil.getRequestParamValue("component"));
		params.put("startStage", FacesUtil.getRequestParamValue("startStage"));
		params.put("endStage", FacesUtil.getRequestParamValue("endStage"));
		params.put("expressionTypes", FacesUtil.getRequestParamValue("expressionTypes"));
		params.put("criteria", FacesUtil.getRequestParamValue("criteria"));
		params.put("output", FacesUtil.getRequestParamValue("output"));
		params.put("ignoreExpression", FacesUtil.getRequestParamValue("ignoreExpression"));
		params.put("inputType", FacesUtil.getRequestParamValue("inputType"));
		params.put("geneSymbol", FacesUtil.getRequestParamValue("geneSymbol"));
		params.put("stage", FacesUtil.getRequestParamValue("stage"));
		params.put("componentID", FacesUtil.getRequestParamValue("componentID"));	
    }	
}