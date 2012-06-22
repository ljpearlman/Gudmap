package gmerg.beans;

import gmerg.entities.summary.DBSummary;
import gmerg.assemblers.DBSummaryAssembler;

import gmerg.utils.FacesUtil;

import javax.faces.event.ActionEvent;

/**
 * Managed Bean for ArrayAnalysis JSF page
 *
 * @author Mehran Sharghi
 *
 */

public class EntryPageBean {
    private boolean debug = false;

	DBSummary dbSummary;
	DBSummaryAssembler dbSummaryAssembler;
    private String xenopusImg;
    private String xenMovButtonImg;
    private String xenMovButtonTxt;

	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public EntryPageBean() {
	    if (debug)
		System.out.println("EntryPageBean.constructor");

		dbSummaryAssembler = new DBSummaryAssembler();
		dbSummary = dbSummaryAssembler.getData();
	    xenopusImg = "../images/slc12a1_optStatic.gif";
	    xenMovButtonImg = "../images/play.gif";
	    xenMovButtonTxt = "Play";
	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
        
	 public String getXenopusImg() {
	     return xenopusImg;
	 }
	 
	 public void setXenopusImg(String value){
	     xenopusImg = value;
	 }
	 
	 public String getXenMovButtonImg() {
	 return xenMovButtonImg;
	 }
	 
	 public void setXenMovButtonImg(String value){
	 xenMovButtonImg = value;
	 }
	 
	 public String getXenMovButtonTxt() {
	 return xenMovButtonTxt;
	 }
	 
	 public void setXenMovButtonTxt(String value){
	 xenMovButtonTxt = value;
	 }

	public String getDatabaseServer() {
		
		return dbSummary.getDatabaseServer(); 
	}

	public String getLastEditorialUpdate() {
		
		return  dbSummary.getLastEditorUpdate();
	}

	public String getLastEntryDate() {
		
		return dbSummary.getLastEntryDate();
	}

	public String getLastSoftwareUpdate() {
		
		return dbSummary.getLastSoftwrUpdate();
	}

	public String getNumNonPubArraySubmissionsQuery() {
		
		return dbSummary.getTotEditArraySubs();
	}

	public String getNumNonPubISHSubmissionsQuery() {
		
		return dbSummary.getTotEditIshSubs();
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
	
	// added by xingjun 28-June-2007 -- add IHC data entry -- end

	public String getNumPublicGenes() {
		
		return dbSummary.getTotIshGenes();
	}

	public String getProject() {
		
		return dbSummary.getProject();
	}
        
    public void changeMovieStatus(ActionEvent e){
    
        String param = FacesUtil.getRequestParamValue("imgTxt") ;

        if(param.equals("Play")){
            xenopusImg = "../images/slc12a1_opt.gif";
            xenMovButtonImg = "../images/stop.gif";
            xenMovButtonTxt = "Stop";
        }
        else {
            xenopusImg = "../images/slc12a1_optStatic.gif";
            xenMovButtonImg = "../images/play.gif";
            xenMovButtonTxt = "Play";
        }
    }
    
    // added by xingjun - 28/07/2009
    public String getApplicationVersion() {
    	return dbSummary.getApplicationVersion();
    }

}
