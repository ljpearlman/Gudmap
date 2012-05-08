package gmerg.beans;

import gmerg.assemblers.DBSummaryAssembler;
import gmerg.assemblers.FocusDBSummaryAssembler;
import gmerg.db.AdvancedSearchDBQuery;
import gmerg.entities.summary.DBSummary;
import gmerg.utils.FacesUtil;

public class FocusDBSummaryBean {
	DBSummary dbSummary;

	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public FocusDBSummaryBean() {
		String organ = FacesUtil.getRequestParamValue("focusedOrgan");
		if(null == organ || organ.equals("")) {
			DBSummaryAssembler dbSummaryAssembler = new DBSummaryAssembler();
			dbSummary = dbSummaryAssembler.getData();
		} else {
			FocusDBSummaryAssembler dbSummaryAssembler = new FocusDBSummaryAssembler();
		    dbSummary = dbSummaryAssembler.getData((String[])AdvancedSearchDBQuery.getEMAPID().get(organ));
		}
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************

	public String getDatabaseServer() {
		
		return dbSummary.getDatabaseServer(); 
	}


	public String getNumPubArraySubmissions() {
		
		return dbSummary.getTotAvailArraySubs();
	}

	public String getNumPubISHSubmissions() {
		
		return dbSummary.getTotAvailIshSubs();
	}
	
	// added by xingjun 28-June-2007 -- add IHC data entry -- begin
	public String getNumerOfPublicSubmissionsIHC() {
		return dbSummary.getTotalAvailableSubmissionsIHC();
	}
	
	public String getNumberOfNonPublicSubmissionsIHC() {
		return dbSummary.getTotalEditSubmissionsIHC();
	}
	


}
