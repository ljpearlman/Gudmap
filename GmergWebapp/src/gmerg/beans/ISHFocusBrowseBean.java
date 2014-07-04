package gmerg.beans;
 
import java.util.HashMap;

import gmerg.assemblers.FocusBrowseAssembler;
import gmerg.entities.Globals;
import gmerg.entities.Globals.PredefinedFilters;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.Visit;

public class ISHFocusBrowseBean {
    private boolean debug = false;

	private String organ;
	private String stage;
    private String archiveId;
    private String batchId;
    private String specimenType;
    private String title;

	public ISHFocusBrowseBean() {
	if (debug)
	    System.out.println("ISHFocusBrowseBean.constructor");

		organ = Visit.getRequestParam("focusedOrgan");
		stage = Visit.getRequestParam("stage");
		archiveId = Visit.getRequestParam("archiveId");
		batchId = Visit.getRequestParam("batchId");
		specimenType = Visit.getRequestParam("specimenType");
		
		if(specimenType==null || "".equals(specimenType))
			title="ISH submissions";
		else
			title=specimenType+" submissions";

		String viewName = "focusBrowseISH";
		if (TableUtil.isTableViewInSession())
			return;
        
       	TableUtil.saveTableView(populateIshFocusBrowseTableView(viewName));		
	}

	private GenericTableView populateIshFocusBrowseTableView(String viewName) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		String[] organs = null;
		if (organ!=null)
			organs = new String[]{organ};
		if(stage==null || stage.equals("") )
			queryParams.put("assayType", "ish");
		else
			queryParams.put("assayType", "insitu"); //insitu: ish+ihc 
		queryParams.put("organ", organs);
		queryParams.put("stage", stage);
		queryParams.put("archiveId", archiveId);
		queryParams.put("batchId", batchId);
		queryParams.put("specimenType", specimenType);
		
		FocusBrowseAssembler assembler = new FocusBrowseAssembler(queryParams);
	    GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(ISHBrowseBean.getDefaultIshFilter());
	    GenericTableView tableView = ISHBrowseBean.getDefaultIshBrowseTableView(viewName, table);
		tableView.setDisplayTotals(true); // Bernie 05/11/2010 switch to display totals
		return  tableView;
	}
	
	public String getTitle() {
		return title;  
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
	

	
}